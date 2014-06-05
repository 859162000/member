package com.wanda.ccs.jobhub.member.dao;

import static com.wanda.ccs.jobhub.member.utils.MemberJobUtils.checkMobileIsTrue;

import java.io.IOException;
import java.io.InputStream;
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
import com.wanda.ccs.jobhub.member.vo.CityBean;
import com.wanda.ccs.jobhub.member.vo.MemberBean;
import com.wanda.ccs.jobhub.member.vo.Province;
import com.wanda.mms.control.stream.service.IntegralInitialization;

/**
 * 批量导入会员
 * 
 * @author chenxm
 * @version 1
 */
public class MemberDao {

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

	/*
	 * public static void main(String[] args) throws SQLException,
	 * ClassNotFoundException { com.mchange.v2.c3p0.ComboPooledDataSource
	 * dataSource = null; try { dataSource = new
	 * com.mchange.v2.c3p0.ComboPooledDataSource();
	 * dataSource.setUser("ccs_mbr_prod");
	 * dataSource.setPassword("ccs_mbr_prod");
	 * dataSource.setJdbcUrl("jdbc:oracle:thin:@10.199.201.105:1521:ccsstag");
	 * 
	 * MemberDao dao = new MemberDao(); dao.dataSource = dataSource; BaseCoreDao
	 * baseDao = new BaseCoreDao(); baseDao.dataSource = dataSource; dao.baseDao
	 * = baseDao; dao.getFile("com.wanda.ccs.model.TMember", "W", 43L); } catch
	 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * finally { dataSource.close(); } }
	 */

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
	public void getFile(String refOjectType, String fileStatus, Long fileId) throws Exception {
		if (refOjectType != null && fileStatus != null) {
			boolean isError = false;
			InputStream in = null;
			
			try {
				String sql = " SELECT FILE_ATTACH_ID ,FILE_NAME, FILE_DATA, CREATE_BY FROM t_file_attach WHERE ref_object_type = ? and file_status = ? and FILE_ATTACH_ID=?";
				List<Map<String, Object>> list = getJdbcTemplate().query(
				sql.toString(),new Object[] {refOjectType, fileStatus, fileId} , new RowMapper<Map<String, Object>>() {
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

				if(list == null || list.size() == 0) {
					throw new Exception("查询数据出错-->" + sql);
				}
				
				Map<String, Object> map = list.get(0);
				Blob blob = (Blob) map.get("FILE_DATA");
				String fileName = (String) map.get("FILE_NAME");
				
				in = blob.getBinaryStream();
				// in = new FileInputStream("D:\\sqlload\\会员.xls");
				ExcelUtil excelUtil = ExcelUtil.getInstance();
				// 打开数据流
				excelUtil.openIn(in);
				int count = excelUtil.getRowCount(0);
				List<String[]> beanDatas = new ArrayList<String[]>();
				for (int row = 1; row <= count; row++) {
					String[] rows = excelUtil.readExcelLine(row);
					if (rows != null && rows.length > 0)
						beanDatas.add(rows);
					if (beanDatas.size() > 1000) {
						boolean flag = saveProperty(beanDatas, fileId,
								fileName, (String) map.get("CREATE_BY"));// 文件数据，ID,文件名
						beanDatas.clear();
						// 如果数据导入错误则不再更新标志
						if(!isError) {
							isError = flag;
						}
					}
				}
				// 处理缓存中的数据
				if (beanDatas.size() > 0) {
					boolean flag = saveProperty(beanDatas, fileId, fileName,
							(String) map.get("CREATE_BY"));// 文件数据，ID,文件名
					beanDatas.clear();
					// 如果数据导入错误则不再更新标志
					if(!isError) {
						isError = flag;
					}
				}
			} catch(Exception e) {
				isError = true;
				throw e;
			} finally {
				String updateFileSql = "update T_FILE_ATTACH set FILE_STATUS = '"+(isError?"E":"S")+"' where FILE_ATTACH_ID = ?";
				getJdbcTemplate().update(updateFileSql, new Object[] {fileId});
				
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
			String fileName, String userName) throws Exception {
		if (strArray.size() <= 0 || strArray == null) {
			throw new Exception("入库数据为空,请检查数据文件！");
		}
		boolean flag = false;
		// 保证拿到的是同一个线程中的同一个 Connection，不会导致重新拿Connection。
		Connection con = DataSourceUtils.getConnection(getJdbcTemplate()
				.getDataSource());
		/* 影城内码缓存 */
		final Map<String, String> innerCodeCache = new HashMap<String, String>();
		MemberBean succMember = new MemberBean();
		StringBuilder logMsg = new StringBuilder();
		Long memberId = 0L;
		Long memberNum = 0L;
		for (int i = 0; i < strArray.size(); i++) {
			logMsg = new StringBuilder();
			String[] beans = strArray.get(i);

			// 判断是否填写的是9列数据
			// if(beans.length > 9){
			// // 不合法
			// rowDateLog(logMsg, beans, "请校验填写的数据是否按照模板提示填写。提示：共9列为可填项", i);
			// }else{
			// 如果出现没有填写必填字段，则需要补齐9列数据后再进行解析
			beans = java.util.Arrays.copyOf(beans, 9);

			succMember = new MemberBean();
			outer: for (int c = 0, len = beans.length; c < len; c++) {
				switch (c) {
				case 0:// 会员名称
					if (beans[c] != null && !"".equals(beans[c])) {
						succMember.setMemberName(beans[c]);
					} else {
						succMember.setMemberName(null);
					}
					break;
				case 1:// 手机号
					if (beans[c] != null && !"".equals(beans[c])) {
						String mobile = beans[c];
						if (mobile.indexOf(".") > -1) { // 如果有出现0.00
							mobile = mobile.substring(0, mobile.indexOf("."));
						}
						// 判断手机号码长度
						switch (mobile.length()) {
						case 10:
							mobile += "0";
						case 11:
							if (checkMobileIsTrue(mobile)) {
								if (baseDao.chechMobileIsHave(mobile)) {
									// 已存在
									rowDateLog(logMsg, beans, "手机号已存在", i);
								} else {
									succMember.setMobile(mobile);// 手机号可以注册
								}
							} else {
								// 不合法
								rowDateLog(logMsg, beans, "手机号不合法", i);
							}
							break;
						default:
							// 不合法
							rowDateLog(logMsg, beans, "手机号格式不正确", i);
							break;
						}
					} else {
						rowDateLog(logMsg, beans, "手机号不能为空", i);
					}
					break;
				case 2:// 性别
					if (beans[c] != null && !"".equals(beans[c])) {
						if ("男".equals(beans[c])) {
							// 存M
							succMember.setGender("M");
						} else if ("女".equals(beans[c])) {
							// 存F
							succMember.setGender("F");
						} else {
							// 存M
							succMember.setGender("O");
						}
					} else {
						succMember.setGender("O");
					}
					break;
				case 3:// 会员生日
					if (beans[c] != null && !"".equals(beans[c])) {
						try {
							Date da = new SimpleDateFormat("yyyy-MM-dd")
									.parse(beans[c]);
							succMember.setBirthDate(da);
						} catch (Exception e) {
							rowDateLog(logMsg, beans,
									"出生日期格式有误(例：2008-08-08).请以文本格式保存", i);
						}
					} else {
						succMember.setBirthDate(null);
					}
					break;
				case 4:// 注册影城
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
							// 存注册影城Id
							succMember.setCinemaId(Long.valueOf(innerCodeCache
									.get(beans[c])));
							succMember.setInnerCode(beans[c]);
						} else {
							// 注册影城不存在
							rowDateLog(logMsg, beans, "影城内码不存在", i);
						}
					} else {
						// 注册影城不存在
						rowDateLog(logMsg, beans, "影城内码不能为空", i);
					}
					break;
				case 5:// 是否希望被联系
					if (beans[c] != null && !"".equals(beans[c])) {
						if ("是".equals(beans[c])
								|| beans[c].equalsIgnoreCase("YES")) {
							// 存1
							succMember.setContactable(1l);
						} else if ("否".equals(beans[c])
								|| beans[c].equalsIgnoreCase("NO")) {
							// 存0
							succMember.setContactable(0l);
						} else {
							// 存0
							succMember.setContactable(2l);
						}
					} else {
						succMember.setContactable(2l);
					}
					break;
				case 6:// 会员联系地址
					if (beans[c] != null && !"".equals(beans[c])) {
						succMember.setAddress(beans[c]);
					} else {
						succMember.setAddress(null);
					}
					break;
				case 7:// 省
					if (beans[c] != null && !"".equals(beans[c])) {
						Province pro = baseDao.getProvince(beans[c]);
						if (pro != null) {
							// 省id
							succMember.setPrivence(pro.getProvinceId());
						} else {
							// 省
							rowDateLog(logMsg, beans, "省不存在", i);
						}
					} else {
						succMember.setPrivence(null);
					}
					break;
				case 8:// 城市
					if (beans[c] != null && !"".equals(beans[c])) {
						CityBean cbean = baseDao.getCity(beans[c]);
						if (cbean != null) {
							// 城市id
							succMember.setCity(cbean.getCityId());
						} else {
							// 城市
							rowDateLog(logMsg, beans, "城市不存在", i);
						}
					} else {
						succMember.setCity(null);
					}
					break;
				default:
					break outer;
				}

				if (logMsg.length() > 0) { // 如果消息日志不为空说明有错误信息输出
					System.out.println(logMsg.toString());
					break;
				}
			}
			// }

			if (logMsg.length() > 0) {
				flag = true;
				errorSql(fileName, userName, fileId, logMsg.toString());
			} else {
				memberId = getJdbcTemplate().queryForLong(
						"select S_T_MEMBER.NEXTVAL from dual");
				memberNum = getJdbcTemplate().queryForLong(
						"select S_MEMBER_NUM.NEXTVAL from dual");

				// 1、注册时默认为新会员; 注册类型，需要接口中定义该字段 1:主动注册;2:自动转换;3:批量导入
				// 2招募渠道
				// 3会员状态，注册是设置成有效 1有效 0无效
				// 4逻辑删除标识,默认:0 未删除;1删除;其他:非法
				StringBuffer memberSqlBf = new StringBuffer(
						" INSERT INTO T_MEMBER (MEMBER_ID,MEMBER_NO,MOBILE,NAME,GENDER,BIRTHDAY,REGIST_TYPE,REGIST_OP_NO,REGIST_OP_NAME,REGIST_CINEMA_ID,STATUS,ISCONTACTABLE,ISDELETE,REGIST_DATE,SOURCE_TYPE,FAV_CINEMA_ID,REGIST_CHNID) VALUES ( ");
				// 添加插入值
				// 1,会员主键
				memberSqlBf.append(memberId);
				memberSqlBf.append(", '");
				// 2会员编码
				memberSqlBf.append("C"
						+ succMember.getInnerCode()
						+ String.format("%016d",
								Integer.parseInt(memberNum.toString())));
				memberSqlBf.append("' , '");
				// 3会员手机
				memberSqlBf.append(succMember.getMobile());
				memberSqlBf.append("' , '");
				// 4会员名称
				memberSqlBf.append(succMember.getMemberName());
				memberSqlBf.append("' , '");
				// 5会员性别
				memberSqlBf.append(succMember.getGender());

				// 6生日
				if (succMember.getBirthDate() != null) {
					memberSqlBf.append("' , TO_DATE('");
					memberSqlBf.append(new SimpleDateFormat("yyyy-MM-dd")
							.format(succMember.getBirthDate()));
					memberSqlBf.append("' ,'yyyy-MM-dd'),");
				} else {
					memberSqlBf.append("' , ");
					memberSqlBf.append(succMember.getBirthDate());
					memberSqlBf.append(",");
				}

				// 7 1、注册时默认为新会员; 注册类型，需要接口中定义该字段 1:主动注册;2:自动转换;3:批量导入
				memberSqlBf.append(3);
				memberSqlBf.append(", '");
				// 招募员工号
				memberSqlBf.append(userName);
				memberSqlBf.append("' , '");
				// 招募员工姓名
				memberSqlBf.append(userName);
				memberSqlBf.append("' ,");
				// REGIST_CINEMA_ID,STATUS,ISCONTACTABLEISDELETE
				// 注册影城
				memberSqlBf.append(succMember.getCinemaId());
				memberSqlBf.append(",");
				// 会员状态
				memberSqlBf.append(1);
				memberSqlBf.append(",");
				// 是否希望被联系
				memberSqlBf.append(succMember.getContactable());
				memberSqlBf.append(",");
				memberSqlBf.append(0);
				// 会员招募时间
				memberSqlBf.append(",");
				memberSqlBf.append("systimestamp");
				// 来源 会员来源 1：POS 2：网站 3：其他
				memberSqlBf.append(",");
				memberSqlBf.append("3");
				// 会员常去影城
				memberSqlBf.append(",");
				memberSqlBf.append(succMember.getCinemaId());
				// 招募渠道 合作伙伴
				memberSqlBf.append(",");
				memberSqlBf.append("4");
				memberSqlBf.append(" )");
				System.out
						.println("注册会员sql==================================="
								+ memberSqlBf.toString());
				// 会员信息入库
				getJdbcTemplate().execute(memberSqlBf.toString());
				// 会员概况入库
				String memberInfoSql = " insert into T_MEMBER_INFO (MEMBER_INFO_ID,MEMBER_ID,MANAGE_CINEMA_ID,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION) values (S_T_MEMBER_INFO.nextval, "
						+ memberId
						+ " , "
						+ succMember.getCinemaId()
						+ " ,0, "
						+ "'"
						+ userName
						+ "',systimestamp,'"
						+ userName + "' ,systimestamp,0 )";
				getJdbcTemplate().execute(memberInfoSql);
				// 会员住址入库
				String memberAddSql = " insert into T_MEMBER_ADDR (MEMBER_ADDR_ID,MEMBER_ID,ADDRESS1,PROVINCE_ID,CITY_ID,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION) values (S_T_MEMBER_ADDR.nextval, "
						+ memberId
						+ " ,'"
						+ succMember.getAddress()
						+ "' ,"
						+ succMember.getPrivence()
						+ " ,"
						+ succMember.getCity()
						+ ", 0,"
						+ "'"
						+ userName
						+ "',systimestamp,'"
						+ userName
						+ "' ,systimestamp,0 )";
				getJdbcTemplate().execute(memberAddSql);
				// 会员喜欢的影片类型入库
				String memberFavFilmSql = "  insert into T_MEM_FAV_FILMTYPE (FAV_FILMTYPE_ID,MEMBER_ID,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION) values (S_T_MEM_FAV_FILMTYPE.nextval, "
						+ memberId
						+ " , 0,"
						+ "'"
						+ userName
						+ "',systimestamp,'"
						+ userName
						+ "' ,systimestamp,0 ) ";
				getJdbcTemplate().execute(memberFavFilmSql);
				// 会员喜欢的联系方式入库
				String memberFavContSql = " insert into T_MEM_FAV_CONTACT (FAV_CONTACT_ID,MEMBER_ID,ISDELETE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION) values (S_T_MEM_FAV_CONTACT.nextval, "
						+ memberId
						+ " , 0,"
						+ "'"
						+ userName
						+ "',systimestamp,'"
						+ userName
						+ "' ,systimestamp,0 )";
				getJdbcTemplate().execute(memberFavContSql);
				// 生成默认会员的初始积分和级别
				IntegralInitialization registPointAndLevel = new IntegralInitialization();
				registPointAndLevel.addMemberPointByID(con, memberId,
						userName, 0L);
			}

			succMember = null;
			logMsg = null;
			memberId = null;
			memberNum = null;
		}

		return flag;
	}

	private void errorSql(String fileName, String userName, Long fileId,
			String logMsg) {
		String errSql = " INSERT INTO  T_ABATCH_ERRE_LOG (ABATCH_ERRE_ID,ERRE_FILE_NAME,ERRE_DATA,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,FILE_ID) VALUES ( S_T_ABATCH_ERRE_LOG.nextval,?,?,?,systimestamp,?,systimestamp,0,?)";
		getJdbcTemplate().update(errSql, new Object[] {fileName, logMsg, userName, userName, fileId});
	}

}
