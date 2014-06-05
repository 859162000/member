package com.wanda.ccs.jobhub.member.dao;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.jobhub.member.ExcelUtil;
import com.wanda.ccs.jobhub.member.SystemException;
import com.wanda.ccs.jobhub.member.vo.MemberBean;
import com.wanda.mms.control.stream.PointAdjust;
import com.wanda.mms.control.stream.vo.PointHistroy;

/**
 * 批量更改会员积分导入
 * 
 * @author chenxm
 * @version 1
 */
public class ChangeMemberPointDao {

	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;

	@InstanceIn(path = "BaseCoreDao")
	private BaseCoreDao baseDao;
	
	private ExtJdbcTemplate extJdbcTemplate = null;

	public ExtJdbcTemplate getJdbcTemplate() {
		if (this.extJdbcTemplate == null) {
			this.extJdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.extJdbcTemplate;
	}

	/*public static void main(String[] args) throws SQLException,
			ClassNotFoundException {
		com.mchange.v2.c3p0.ComboPooledDataSource dataSource = null;
		try {
			dataSource = new com.mchange.v2.c3p0.ComboPooledDataSource();
			dataSource.setUser("ccs_mbr_prod");
			dataSource.setPassword("ccs_mbr_prod");
			dataSource.setJdbcUrl("jdbc:oracle:thin:@10.199.201.105:1521:ccsstag");
			
			ChangeMemberPointDao dao = new ChangeMemberPointDao();
			dao.dataSource = dataSource;// 105
			dao.baseDao = new BaseCoreDao();
			dao.baseDao.dataSource = dataSource;
			dao.getFile("com.wanda.ccs.model.TMemberPoint", "E", 183L);
		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dataSource.close();
		}
	}*/

	/**
	 * 获取导入后的文件内容
	 * 
	 * @param refOjectType
	 *            objectType类型
	 * 
	 * @param fileStatus
	 *            状态 'W'
	 * @throws Exception
	 */
	public void getFile(String refOjectType, String fileStatus, Long fileId)
			throws Exception {
		boolean isError = false;
		InputStream in = null;

		try {
			if (refOjectType != null && fileStatus != null) {
				String sql = " SELECT FILE_ATTACH_ID ,FILE_NAME, FILE_DATA, CREATE_BY FROM T_FILE_ATTACH WHERE ref_object_type=? and file_status=? and FILE_ATTACH_ID=?";
				List<Map<String, Object>> list = getJdbcTemplate().query(sql,
						new Object[] { refOjectType, fileStatus, fileId },
						new RowMapper<Map<String, Object>>() {

							public Map<String, Object> mapRow(ResultSet rs,
									int row) throws SQLException {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("FILE_DATA", rs.getBlob("FILE_DATA")); // 文件内容
								map.put("FILE_ATTACH_ID",
										rs.getLong("FILE_ATTACH_ID")); // 上传文件的ID
								map.put("FILE_NAME", rs.getString("FILE_NAME")); // 文件名称
								map.put("CREATE_BY", rs.getString("CREATE_BY")); // 创建人

								return map;
							}
						});

				if (list == null || list.size() == 0) {
					throw new Exception("查询数据出错-->" + sql);
				}

				Map<String, Object> map = list.get(0);
				Blob blob = (Blob) map.get("FILE_DATA");
				String fileName = (String) map.get("FILE_NAME");
				
				in = blob.getBinaryStream();
				//in = new FileInputStream("D:\\sqlload\\ttt.xls");
				ExcelUtil excelUtil = ExcelUtil.getInstance();
				// 打开数据流
				excelUtil.openIn(in);
				int count = excelUtil.getRowCount(0);
				final List<String[]> beanDatas = new ArrayList<String[]>();
				for (int row = 1; row <= count; row++) {
					String[] rows = excelUtil.readExcelLine(row);
					beanDatas.add(rows);
				}

				// 处理缓存中的数据
				if (beanDatas.size() > 0) {
					isError = saveProperty(beanDatas, fileId, fileName,
							(String) map.get("CREATE_BY"));// 文件数据，ID,文件名
					beanDatas.clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isError = true;
			throw e;
		} finally {
			String updateFileSql = "update T_FILE_ATTACH set FILE_STATUS = '"
					+ (isError ? "E" : "S") + "' where FILE_ATTACH_ID = ?";
			getJdbcTemplate().update(updateFileSql, new Object[] { fileId });

			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 获取行数据
	 * 
	 * @param @param sb
	 * @param @param beans 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void rowDateLog(StringBuilder sb, String[] beans, String msg,
			int rowNumber) {
		sb.append("第" + (rowNumber + 1) + "行： ").append(msg).append("-->");
		sb.append("当前行数据").append("|");
		for (int i = 0, len = beans.length; i < len; i++) {
			sb.append(beans[i]).append(",");
		}
		sb.deleteCharAt(sb.length() - 1).append("|");
	}

	public boolean saveProperty(final List<String[]> strArray, Long fileId,
			String fileName, String createBy) throws Exception {
		Connection con = null;
		boolean isError = false;

		try {
			if (strArray.size() <= 0 || strArray == null) {
				throw new Exception("入库数据为空,请检查数据文件！");
			}

			/* 影城内码缓存 */
			final Map<String, String> innerCodeCache = new HashMap<String, String>();
			// 保证拿到的是同一个线程中的同一个 Connection，不会导致重新拿Connection。
			con = DataSourceUtils.getConnection(getJdbcTemplate()
					.getDataSource());
			con.setAutoCommit(false);

			PointHistroy pointHistroy = null;
			/*  */
			StringBuilder logMsg = new StringBuilder();
			String[] beans = null;

			for (int i = 0; i < strArray.size(); i++) {
				beans = strArray.get(i);
				logMsg = new StringBuilder();

				if (beans.length < 6) {
					// 不合法
					rowDateLog(logMsg, beans, "请校验填写的数据是否按照模板提示填写。", i);
				} else {
					MemberBean member = null;
					pointHistroy = new PointHistroy();
					// 迭代一行数据
					outer: for (int c = 0, len = beans.length; c < len; c++) {
						switch (c) {
						case 0: // 手机号
							if (beans[c] != null && !"".equals(beans[c])) {
								String mobile = beans[c];
								if (mobile.indexOf(".") > -1) { // 如果有出现0.00
									mobile = mobile.substring(0,
											mobile.indexOf("."));
								}
								// 判断手机号码长度
								switch (mobile.length()) {
								case 10:
									mobile += "0";
								case 11:
									member = baseDao.getMemberBean(mobile);
									if (member != null) {
										pointHistroy.setMemberid(member
												.getMemberId());
									} else {
										rowDateLog(logMsg, beans, "会员手机号不存在", i);
									}
									break;
								default:
									// 不合法
									rowDateLog(logMsg, beans, "手机号格式不正确", i);
									break;
								}
							} else {
								rowDateLog(logMsg, beans, "会员手机号不能为空", i);
							}
							break;
						case 1: // 定级积分
							if (beans[c] != null && !"".equals(beans[c])) {
								try {
									BigDecimal point = new BigDecimal(beans[c])
											.setScale(0, BigDecimal.ROUND_DOWN);
									if (point.longValue() < 0) {
										rowDateLog(logMsg, beans, "定级积分只能为正", i);
									}
									pointHistroy.setLevel_Point(point
											.longValue());
								} catch (Exception e) {
									rowDateLog(logMsg, beans, "定级积分格式错误", i);
								}
							} else {
								pointHistroy.setLevel_Point(0);
							}
							break;
						case 2: // 非定级积分
							if (beans[c] != null && !"".equals(beans[c])) {
								try {
									BigDecimal point = new BigDecimal(beans[c])
											.setScale(0, BigDecimal.ROUND_DOWN);
									if (point.longValue() > 0) {
										rowDateLog(logMsg, beans, "非定级积分只能为负",
												i);
									}
									if (pointHistroy.getLevel_Point() != 0) {
										rowDateLog(logMsg, beans,
												"定级或非定级积分不能同时出现", i);
									}
									// 判断非定级积分余额是否存在
									Long pointBalance = baseDao
											.getMemberPointBalance(member
													.getMemberId());
									if (pointBalance + point.longValue() >= 0) {
										pointHistroy.setActivity_Point(point
												.longValue());
									} else {
										rowDateLog(logMsg, beans,
												"修改非定级积分时，积分余额不足", i);
									}
								} catch (Exception e) {
									e.printStackTrace();
									rowDateLog(logMsg, beans, "非定级积分格式错误", i);
								}
							} else {
								if (pointHistroy.getLevel_Point() == 0) {
									rowDateLog(logMsg, beans, "必须填写定级或非定级积分", i);
								}
								pointHistroy.setActivity_Point(0);
							}
							member = null;
							break;
						case 3: // 生效日期
							String setTime = beans[c];
							if (setTime != null && !"".equals(setTime)) {
								try {
									Date date = new SimpleDateFormat(
											"yyyy-MM-dd").parse(setTime);
									Date nowDate = new Date();
									if (date.before(nowDate)) {
										String te = " 23:59:59";
										String settime = setTime + te;
										pointHistroy.setSetTime(settime);
									} else {
										rowDateLog(
												logMsg,
												beans,
												"生效日期格式有误(例：2008-08-08),日期范围去年一月一日到昨天的时间.并以文本格式保存",
												i);
									}
								} catch (Exception e) {
									Date nowDate = new Date();
									SimpleDateFormat format = new SimpleDateFormat(
											"yyyy-MM-dd HH:mm:ss");
									pointHistroy.setSetTime(format
											.format(nowDate));
								}
							} else {
								rowDateLog(logMsg, beans, "生效日期不能为空", i);
							}
							break;
						case 4: // 注册影城
							if (beans[c] != null && !"".equals(beans[c])) {
								// 读取影城内码缓存数据
								if (innerCodeCache == null
										|| innerCodeCache.size() == 0) {
									baseDao.getAllCinema(innerCodeCache);
									if (innerCodeCache.size() == 0) {
										new SystemException("读取影城内码缓存失败");
									}
								}

								if (innerCodeCache.containsKey(beans[c])) { // 影城内码存在
									pointHistroy.setCinema_inner_code(beans[c]);
								} else {
									rowDateLog(logMsg, beans, "影城内码不存在", i);
								}
							} else {
								rowDateLog(logMsg, beans, "影城内码不能为空", i);
							}
							break;
						case 5: // 调整原因
							if (beans[c] != null && !"".equals(beans[c])) {
								pointHistroy.setAdj_reason(beans[c]);
								pointHistroy.setAdj_Resion(beans[c]);
							} else {
								// 调整原因没有维护
								rowDateLog(logMsg, beans, "调整原因没有填写", i);
							}
							break;
						default:
							break outer;
						}

						if (logMsg.length() > 0) {// 有错误消息
							break outer;
						}
					}

					if (logMsg.length() == 0 && pointHistroy != null
							&& isError == false) {
						// 可兑换积分
						pointHistroy.setExchange_Point(pointHistroy
								.getLevel_Point()
								+ pointHistroy.getActivity_Point());
						pointHistroy.setCreate_By(createBy);
						pointHistroy.setPoint_Type("4");// 维表.积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
						pointHistroy.setTicket_Count(0);
						pointHistroy.setPoint_Sys("3");// 维表.源系统(1:POS;2:网站;3:会员系统;Others
														// 其他)
						pointHistroy.setPoint_Trans_Type("1");// 维表.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
						pointHistroy.setAdj_reason_type("4");// 老会员回馈,会员投诉,活动赠送,其他,会员终止
					}
				}

				if (logMsg.length() > 0) {
					if (isError == false) {
						con.rollback();
						isError = true;
					}

					String errSql = " INSERT INTO T_ABATCH_ERRE_LOG (ABATCH_ERRE_ID,ERRE_FILE_NAME,ERRE_DATA,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,FILE_ID) VALUES (S_T_ABATCH_ERRE_LOG.nextval,?,?,?,systimestamp,?,systimestamp,0,?)";
					getJdbcTemplate().update(
							errSql,
							new Object[] { fileName, logMsg.toString(),
									createBy, createBy, fileId });
					// return isError;
					// throw new Exception("导入错误整体进行回滚");
				} else if(isError == false) {
					PointAdjust pAdjust = new PointAdjust();
					pAdjust.adjustNoCommit(con, pointHistroy);
				}

				pointHistroy = null;
				logMsg = null;
				beans = null;
			}

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			isError = true;
			con.rollback();
			throw e;
		}

		return isError;
	}

}
