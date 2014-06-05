/**
 * 
 */
package com.wanda.ccs.jobhub.member.dao;

import static com.wanda.ccs.jobhub.member.utils.MemberJobUtils.checkMobileIsTrue;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.jobhub.member.utils.ExcelReader;
import com.wanda.ccs.jobhub.member.utils.RowHandler;
import com.wanda.ccs.jobhub.member.vo.ContactHistoryTempBean;

/**
 * 批量导入会员数据
 * 
 * @author YangJianbin
 *
 */
public class ImportExcelMemberDao {
	//private Logger logHelper = Logger.getLogger("BaseCoreDao");
	
	private final static int BATCH_FETCH_SIZE = 1000;
	
	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;
	
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
			
			ImportExcelMemberDao dao = new ImportExcelMemberDao();
			dao.dataSource = dataSource;
			dao.saveMembersFromFile("TCmnActivity", 396L, "group");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dataSource.close();
		}
	}*/
	
	
	/**
	 * 获取导入后的文件内容,并解析成手机号数组
	 * 
	 * @param refObjectType
	 *            objectType类型
	 * @param fileId
	 *            文件id
	 * @param userId
	 *            操作人
	 * @throws Exception 
	 */
	public void saveMembersFromFile(String refObjectType, final Long fileId, final String userId) throws Exception {
		InputStream in = null;
		// 此参数主要为了在内部类中使用
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("hasError", false);
		map.put("row", 0);
		
		if (refObjectType != null) {
			try {
				String sql = "SELECT FILE_ATTACH_ID ,FILE_NAME, FILE_DATA, CONTENT_TYPE FROM t_file_attach t WHERE t.ref_object_type = ? and t.FILE_ATTACH_ID = ?";
				List<Map<String, Object>> results = getJdbcTemplate().query(sql, new Object[] {refObjectType, fileId}, new RowMapper<Map<String, Object>>() {
					
					public Map<String, Object> mapRow(ResultSet rs, int row)
							throws SQLException {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("FILE_DATA", rs.getBlob("FILE_DATA"));	// 文件内容
						map.put("FILE_NAME", rs.getString("FILE_NAME"));	// 文件内容
						
						return map;
					}
				});
				
				if(results == null || results.size() == 0) {
					throw new Exception("查询数据出错-->"+sql);
				}
				
				if(results != null && results.size() > 0) {
					Map<String, Object> result = results.get(0);
					final String fileName = (String) result.get("FILE_NAME");
					Blob blob = (Blob) result.get("FILE_DATA");
					
					in = blob.getBinaryStream();
					//in = new FileInputStream("D:\\sqlload\\qqqqqq.xlsx");
					/* 待处理手机号缓存 */
					final List<String> mobiles = new ArrayList<String>();
					
					new ExcelReader(in, new RowHandler() {
						public void row(int sheetIndex, int curRow, List<String> rowList) {
							if(curRow != 1) {	//不读取第一行的“手机号”内容
								if(rowList.size() > 0) {
									mobiles.add(rowList.get(0));
								} else {
									mobiles.add("");
								}
								
								if(mobiles.size() >= BATCH_FETCH_SIZE) {
									List<ContactHistoryTempBean> list = checkMobiles(curRow - mobiles.size(),mobiles, fileId, fileName, userId);
									//如果不一样说明有错误数据存在
									if(mobiles.size() != list.size() && (Boolean)map.get("hasError") == false) {
										map.put("hasError", true);
									}
									if(list != null && list.size() > 0) {
										// 将正确的手机号入库
										saveContactHistoryTemp(list, userId);
										mobiles.clear();
									}
								}
							}
							map.put("row", curRow);
						}
					}).readOneSheet();
					
					if((Integer)map.get("row") > 1) {
						if(mobiles != null && mobiles.size() > 0) {
							List<ContactHistoryTempBean> list = checkMobiles((Integer)map.get("row") - mobiles.size(),mobiles, fileId, fileName, userId);
							//如果不一样说明有错误数据存在
							if(mobiles.size() != list.size() && (Boolean)map.get("hasError") == false) {
								map.put("hasError", true);
							}
							if(list != null && list.size() > 0) {
								// 将正确的手机号入库
								saveContactHistoryTemp(list, userId);
								mobiles.clear();
							}
						}
					} else {
						map.put("hasError", true);
						sqlLog(fileId, fileName, userId, "该文件中没有手机号存在");
					}
				}
			} catch (Exception e) {
				map.put("hasError", true);
				throw e;
			} finally {
				String updateFileSql = "update T_FILE_ATTACH set FILE_STATUS = '"+((Boolean) map.get("hasError")?"E":"S")+"' where FILE_ATTACH_ID = ?";
				getJdbcTemplate().update(updateFileSql, new Object[] {fileId});
				
				try {
					if(in != null) {
						in.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 根据手机号获取会员id
	 * @param mobile
	 * @return
	 */
	public Long getMemberIdByMobile(String mobile) {
		String mobileSql = " SELECT MEMBER_ID FROM T_MEMBER WHERE MOBILE = '"
				+ mobile.replace(".", "").substring(0, 11) + "'";
		
		List<Map<String,Object>> list = getJdbcTemplate().queryForList(mobileSql);
		if(list != null && !list.isEmpty()) {
			Map<String,Object> map = list.get(0);
			return map.get("MEMBER_ID") == null
					|| map.get("MEMBER_ID").toString().equals("") ? null : Long
					.valueOf(map.get("MEMBER_ID").toString());
		}
		return null;
	}
	
	public Map<String, String> getMemberListByMobile(String[] mobile) {
		StringBuilder sql = new StringBuilder(" SELECT MOBILE, MEMBER_ID FROM T_MEMBER WHERE MOBILE in (");
		for(int i=0,len=mobile.length;i<len;i++) {
			sql.append("'").append(mobile[i]).append("',");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		
		final Map<String, String> members = new HashMap<String, String>();
		getJdbcTemplate().query(sql.toString(), new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				members.put(rs.getString("MOBILE"), rs.getString("MEMBER_ID"));
			}
		});
		
		return members;
	}
	
	/**
	 * 检测所有的手机号是否合法，并且为会员
	 * @param mobiles
	 * @return
	 */
	public List<ContactHistoryTempBean> checkMobiles(int startRow,List<String> mobiles, Long fileId, String fileName, String userId){
		List<ContactHistoryTempBean> list = new ArrayList<ContactHistoryTempBean>();
		/* 会员信息 */
		Map<String, String> members = getMemberListByMobile(mobiles.toArray(new String[] {}));
		for(int i = 0; i < mobiles.size(); i++) {
			String mobile = mobiles.get(i);
			StringBuffer error = new StringBuffer();
			if(mobile == null || mobile.equals("")){
				//校验手机号是否为空
				error.append("第").append(startRow+i+1).append("行").append("手机号为空");
			}else if(!checkMobileIsTrue(mobile)){
				// 校验手机号是否合法
				error.append("第").append(startRow+i+1).append("行").append("手机号").append(mobile).append("不合法");
			}else{
				//Long memberId = getMemberIdByMobile(mobile);
				if(members.containsKey(mobile) == false){
					//校验手机号是否为会员
					error.append("第").append(startRow+i+2).append("行").append("手机号").append(mobile).append("不是会员");
				} else {
					ContactHistoryTempBean bean = new ContactHistoryTempBean();
					bean.setFileAttachId(fileId);
					bean.setMemberId(Long.parseLong(members.get(mobile)));
					bean.setMobile(mobile);
					if(list.contains(bean)){
						sqlLog(fileId, userId, fileName, "第"+(startRow+i+1)+"行手机号"+mobile+"已存在重复数据");
					}else{
						list.add(bean);
					}
				}
			}
			
			if(error.length() > 0){
				sqlLog(fileId, userId, fileName, error.toString());
			}
		}
		
		return list;
	}
	
	private void sqlLog(Long fileId, String userId, String fileName, String message) {
		String sql = "INSERT INTO T_ABATCH_ERRE_LOG (ABATCH_ERRE_ID,ERRE_FILE_NAME,ERRE_DATA,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,FILE_ID, ERRE_LEVEL) VALUES (S_T_ABATCH_ERRE_LOG.nextval,?,?,?,systimestamp,?,systimestamp,0,?,10)";
		getJdbcTemplate().update(sql, new Object[] {fileName, message, userId, userId, fileId});
	}
	
	/**
	 * 保存excel中的联络清单到临时表中
	 * @param memberIdList
	 * @param actRargetId
	 * @param userId
	 * @throws Exception
	 */
	public void saveContactHistoryTemp(final List<ContactHistoryTempBean> contactHistoryTempList,
			 final String userId) {
		if(contactHistoryTempList == null || contactHistoryTempList.isEmpty())
			return;
		String sql = " insert into T_CONTACT_HISTORY_TEMP(CONTACT_HISTORY_TEMP_ID, MEMBER_ID, MOBILE, FILE_ATTACH_ID, create_by, create_date, version) values (S_T_CONTACT_HISTORY_TEMP.NEXTVAL, ?, ?, ?, ?, ?, ?) ";
		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return contactHistoryTempList.size();
			}
			
			public void setValues(PreparedStatement pst, int i)
					throws SQLException {
				ContactHistoryTempBean bean = contactHistoryTempList.get(i);
				pst.setLong(1, bean.getMemberId());
				pst.setString(2, bean.getMobile());
				pst.setLong(3, bean.getFileAttachId());
				pst.setString(4, userId);
				pst.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
				pst.setString(6, "0");
			}
		});
	}
	
	/**
	 * 删除excel导入的联络清单
	 * @param actTargetId
	 * @throws Exception
	 */
	public void delContactHistoryTemp(Long fileAttachId) throws Exception {
		try {
			String delSql = "delete from T_CONTACT_HISTORY_TEMP contact where contact.FILE_ATTACH_ID = ?";
			ArrayList<Object> delArgList = new ArrayList<Object>();
			delArgList.add(fileAttachId);
			getJdbcTemplate().update(delSql, delArgList.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	
	/**
	 * 删除错误的日志信息
	 * @param actTargetId
	 * @throws Exception
	 */
	public void delAbatchErrorLog(Long fileAttachId) throws Exception {
		try {
			String delSql = "delete from T_ABATCH_ERRE_LOG errlog where errlog.FILE_ID = ?";
			ArrayList<Object> delArgList = new ArrayList<Object>();
			delArgList.add(fileAttachId);
			getJdbcTemplate().update(delSql, delArgList.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
}
