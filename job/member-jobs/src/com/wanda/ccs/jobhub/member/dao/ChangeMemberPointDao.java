package com.wanda.ccs.jobhub.member.dao;

import java.io.FileInputStream;
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
	
	public ExtJdbcTemplate getJdbcTemplate()  {
		if(this.extJdbcTemplate == null) {
			this.extJdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.extJdbcTemplate;
	}
	
	/*public static void main(String[] args) throws SQLException, ClassNotFoundException {
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
			dao.getFile("com.wanda.ccs.model.TMemberPoint", "S", 183L);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	 */
	public void getFile(String refOjectType, String fileStatus, Long fileId) {
		
		if (refOjectType != null && fileStatus != null) {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT FILE_ATTACH_ID ,FILE_NAME, FILE_DATA, CREATE_BY FROM T_FILE_ATTACH WHERE ref_object_type = '")
				.append(refOjectType).append("' and file_status = '")
				.append(fileStatus).append("' and FILE_ATTACH_ID =")
				.append(fileId);
			
			List<Map<String, Object>> list = getJdbcTemplate().query(sql.toString(), new RowMapper<Map<String, Object>>() {
				
				public Map<String, Object> mapRow(ResultSet rs, int row)
						throws SQLException {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("FILE_DATA", rs.getBlob("FILE_DATA")); // 文件内容
					map.put("FILE_ATTACH_ID", rs.getLong("FILE_ATTACH_ID")); // 上传文件的ID
					map.put("FILE_NAME", rs.getString("FILE_NAME")); // 文件名称
					map.put("CREATE_BY", rs.getString("CREATE_BY")); // 创建人
					
					return map;
				}
			});
			
			if(list != null && list.size() > 0) {
				InputStream in = null;
				Map<String, Object> map = list.get(0);
				try {
					Blob blob = (Blob) map.get("FILE_DATA");
					String fileName = (String) map.get("FILE_NAME");
					
					in = blob.getBinaryStream();
					//in = new FileInputStream("D:\\sqlload\\珠海.xls");
					ExcelUtil excelUtil = ExcelUtil.getInstance();
					// 打开数据流
					excelUtil.openIn(in);
					int count = excelUtil.getRowCount(0);
					final List<String[]> beanDatas = new ArrayList<String[]>();
					for (int row = 1; row <= count; row++) {
						String[] rows = excelUtil.readExcelLine(row);
						beanDatas.add(rows);
						if(beanDatas.size() > 1000) {
							saveProperty(beanDatas, fileId, fileName, (String) map.get("CREATE_BY"));// 文件数据，ID,文件名
							beanDatas.clear();
						}
					}
					// 处理缓存中的数据
					if(beanDatas.size() > 0) {
						saveProperty(beanDatas, fileId, fileName, (String) map.get("CREATE_BY"));// 文件数据，ID,文件名
						beanDatas.clear();
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new SystemException(e);
				} finally {
					if(in != null) {
						try {
							in.close();
							in = null;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				throw new SystemException("查询数据出错-->"+sql);
			}
		}

//		File file = new File("D:\\sqlload\\new.xls");
//		ExcelUtil excelUtil = ExcelUtil.getInstance();
//		// 打开文件
//		excelUtil.open(file);
//		// 总行数
//		int rowCount = excelUtil.getRowCount(0);
//		final List<String[]> strArray = new ArrayList<String[]>();
//		// 循环读取Excel文件中的内容
//		for (int i = 1; i <= rowCount; i++) {
//			String[] rows = excelUtil.readExcelLine(i);
//			String[] beans = new String[rows.length];
//			for (int j = 0; j < rows.length; j++) {
//				beans[j] = rows[j];
//			}
//			strArray.add(beans);
//		}
//		System.out.println("方法执行开始时间" + new Date());
//		try {
//			saveProperty(strArray, 1l, "test");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("方法执行结束时间" + new Date());
	}
	
	/**
	 * 获取行数据
	 * 
	 * @param @param sb
	 * @param @param beans    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	private void rowDateLog(StringBuilder sb, String[] beans, String msg, int rowNumber) {
		sb.append("第" + (rowNumber+1)+"行： ").append(msg).append("-->");
		sb.append("当前行数据").append("|");
		for(int i=0,len=beans.length;i<len;i++) {
			sb.append(beans[i]).append(",");
		}
		sb.deleteCharAt(sb.length() - 1).append("|");
	}

	public void saveProperty(final List<String[]> strArray ,Long fileId,String fileName, String createBy) throws Exception {
		Connection con = null;
		
		try {
			if(strArray.size()<=0 || strArray == null){
				throw new Exception("入库数据为空,请检查数据文件！");
			}
			boolean flag = false;
			/* 影城内码缓存  */
			final Map<String, String> innerCodeCache = new HashMap<String, String>();
			//Modified by Charlie Zhang
			//保证拿到的是同一个线程中的同一个 Connection，不会导致重新拿Connection。
			con = DataSourceUtils.getConnection(getJdbcTemplate().getDataSource());
			con.setAutoCommit(false);
			
			PointHistroy pointHistroy = null;
			/*  */
			StringBuilder logMsg = null;
			String[] beans = null;
			
			for (int i = 0; i < strArray.size(); i++) {
				beans = strArray.get(i);	
				logMsg = new StringBuilder("");
				
				if(beans.length < 6){
					// 不合法
					rowDateLog(logMsg, beans, "请校验填写的数据是否按照模板提示填写。", i);
				}else{
					pointHistroy = new PointHistroy();
					// 迭代一行数据
					outer:for(int c=0,len=beans.length;c<len;c++) {
						switch(c) {
						case 0:	// 手机号
							if (beans[c] != null && !"".equals(beans[c])) {
								String mobile = beans[c];
								if (mobile.indexOf(".") > -1) { // 如果有出现0.00
									mobile = mobile.substring(0, mobile.indexOf("."));
								}
								// 判断手机号码长度
								switch(mobile.length()) {
								case 10:
									mobile += "0"; 
								case 11:
									MemberBean member = baseDao.getMemberBean(mobile);
									if(member != null) {
										pointHistroy.setMemberid(member.getMemberId());
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
						case 1: //定级积分
							if(beans[c]!=null && !"".equals(beans[c])){
								try {
									BigDecimal point =new BigDecimal(beans[c]).setScale(0,BigDecimal.ROUND_DOWN);
									pointHistroy.setLevel_Point(point.longValue());
								} catch (Exception e) {
									rowDateLog(logMsg, beans, "定级积分格式错误", i);
								}
							}else{
								pointHistroy.setLevel_Point(0);
							}
							break;
						case 2:	//非定级积分
							if(beans[c]!=null && !"".equals(beans[c])) {
								try {
									BigDecimal point =new BigDecimal(beans[c]).setScale(0, BigDecimal.ROUND_DOWN);
									pointHistroy.setActivity_Point(point.longValue());
								} catch (Exception e) {
									rowDateLog(logMsg, beans, "非定级积分格式错误", i);
								}
							} else {
								pointHistroy.setActivity_Point(0);
							}
							break;
						case 3:	// 生效日期
							String setTime = beans[c];
							if (setTime != null && !"".equals(setTime)) {
								try {
									Date date = new SimpleDateFormat("yyyy-MM-dd").parse(setTime);
									Date nowDate = new Date();
									if(date.before(nowDate)) {
										String te = " 23:59:59";
										String settime =setTime+te; 
										pointHistroy.setSetTime(settime);
									} else {
										rowDateLog(logMsg, beans, "生效日期格式有误(例：2008-08-08),日期范围去年一月一日到昨天的时间.并以文本格式保存", i);
									}
								} catch (Exception e) {
									Date nowDate = new Date();
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									pointHistroy.setSetTime(format.format(nowDate));
								}
							} else {
								rowDateLog(logMsg, beans, "生效日期不能为空", i);
							}
							break;
						case 4:	// 注册影城
							if (beans[c] != null && !"".equals(beans[c])) {
								// 读取影城内码缓存数据
								if(innerCodeCache == null || innerCodeCache.size() == 0) {
									baseDao.getAllCinema(innerCodeCache);
									if(innerCodeCache.size() == 0) {
										new SystemException("读取影城内码缓存失败");
									}
								}
								
								if(innerCodeCache.containsKey(beans[c])) { //影城内码存在
									pointHistroy.setCinema_inner_code(beans[c]);
								} else {
									rowDateLog(logMsg, beans, "影城内码不存在", i);
								}
							} else {
								rowDateLog(logMsg, beans, "影城内码不能为空", i);
							}
							break;
						case 5:	// 调整原因
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
						
						if(logMsg.length() > 0) {// 有错误消息
							System.out.println(logMsg);
							break outer;
						}
					}
					
					if(logMsg.length() == 0 && pointHistroy != null) {
						//可兑换积分
						pointHistroy.setExchange_Point(pointHistroy.getLevel_Point()+pointHistroy.getActivity_Point());
						pointHistroy.setCreate_By(createBy);
						pointHistroy.setPoint_Type("4");//维表.积分操作类型(1:购买;2:礼品;3:奖励;4调整;5:会员终止;6:积分兑换;其他值:其他）
						pointHistroy.setTicket_Count(0);
						pointHistroy.setPoint_Sys("3");//维表.源系统(1:POS;2:网站;3:会员系统;Others 其他)
						pointHistroy.setPoint_Trans_Type("1");//维表.单据类型(1:交易;2:积分兑换;3:特殊积分活动;4积分清零)
						pointHistroy.setAdj_reason_type("4");//老会员回馈,会员投诉,活动赠送,其他,会员终止
					}
				}
				
				if (logMsg.length() > 0) {
					flag = true;
					
					StringBuffer errSql = new StringBuffer(" INSERT INTO  T_ABATCH_ERRE_LOG (ABATCH_ERRE_ID,ERRE_FILE_NAME,ERRE_DATA,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,FILE_ID) VALUES ( ");
					errSql.append(" S_T_ABATCH_ERRE_LOG.nextval ");
					errSql.append(", '");
					errSql.append(fileName);
					errSql.append("' ,'");
					errSql.append(logMsg.toString());
					errSql.append("',");
					errSql.append("'"+createBy+"'");
					errSql.append(",");
					errSql.append("systimestamp");
					errSql.append(", '");
					errSql.append(createBy);
					errSql.append("' ,");
					errSql.append("systimestamp");
					errSql.append(",");
					errSql.append(0);
					errSql.append(",");
					errSql.append(fileId);
					errSql.append(" ) ");
					System.out.println("输入错误信息sql================================================================="+errSql.toString());
					getJdbcTemplate().execute(errSql.toString());
				} else {
					System.out.println("===============================批量调整==================================");
					PointAdjust pAdjust = new PointAdjust();
					//pAdjust.adjust(con, pointHistroy);
					pAdjust.adjustNoCommit(con, pointHistroy);
				}
				
				pointHistroy = null;
				logMsg = null;
				beans = null;
			}
			
			if(flag){
				String updateFileSql = " update T_FILE_ATTACH set FILE_STATUS = 'E' where FILE_ATTACH_ID = " + fileId ;
				getJdbcTemplate().execute(updateFileSql);
			}else{
				String updateFileSql = " update T_FILE_ATTACH set FILE_STATUS = 'S' where FILE_ATTACH_ID = " + fileId ;
				getJdbcTemplate().execute(updateFileSql);
			}
			
			con.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			con.rollback();
			e.printStackTrace();
			throw e;	
		}
	}

}
