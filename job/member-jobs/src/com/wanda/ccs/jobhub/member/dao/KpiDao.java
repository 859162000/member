package com.wanda.ccs.jobhub.member.dao;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
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

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.jobhub.member.ExcelUtil;
import com.wanda.ccs.jobhub.member.SystemException;
import com.wanda.ccs.jobhub.member.vo.KpiBean;

/**
 * KPI导入
 * 
 * @author admin
 * @date 2013年12月3日 上午11:31:43 
 *
 */
public class KpiDao {
	
	@InstanceIn(path = "BaseCoreDao")
	private BaseCoreDao baseDao;
	
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
			
			KpiDao dao = new KpiDao();
			dao.dataSource = dataSource;
			dao.baseDao = new BaseCoreDao(dataSource);
			dao.getFile("com.wanda.ccs.model.TMemberKPI", "S", 256L);
			
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
					in = ((Blob) map.get("FILE_DATA")).getBinaryStream();
					//in = new FileInputStream("D:\\sqlload\\KPI_other.xls");
					ExcelUtil readExcel = ExcelUtil.getInstance();
					readExcel.openIn(in);
					int count = readExcel.getRowCount(0);
					final List<String[]> beanDatas = new ArrayList<String[]>();
					for (int i = 1; i <= count; i++) {
						String[] rows = readExcel.readExcelLine(i);
						beanDatas.add(rows);
						if(beanDatas.size() > 1000) {	//缓存1000条数据
							saveProperty(beanDatas, fileId, (String) map.get("FILE_NAME"), (String) map.get("CREATE_BY"));
							beanDatas.clear();
						}
					}
					// 处理缓存中的剩余数据
					if(beanDatas.size() > 0) {
						saveProperty(beanDatas, fileId, (String) map.get("FILE_NAME"), (String) map.get("CREATE_BY"));
						beanDatas.clear();
					}
				}
				catch (Exception e) {
					throw new SystemException(e);
				} finally {
					if(in != null)
						try {
							in.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			} else {
				throw new SystemException("查询数据出错-->"+sql);
			}
		}

		// File file = new File("E:\\批量导入会员模板.xls");
		// ExecuteExcel readExcel = new ExecuteExcel(file);
		// //打开文件
		//
		// try {
		// readExcel.open();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// readExcel.setSheetNum(0); // 设置读取索引为0的工作表
		// // 总行数
		// int count = readExcel.getRowCount();
		// final List<String[]> strArray = new ArrayList<String[]>();
		// //循环读取Excel文件中的内容
		// for (int i = 1; i <= count; i++) {
		// String[] rows = readExcel.readExcelLine(i);
		// String[] beans = new String[rows.length];
		// for (int j = 0; j < rows.length; j++) {
		// beans[j] = rows[j];
		// }
		// strArray.add(beans);
		// }
		// System.out.println("方法执行开始时间" + new Date());
		// try {
		// saveProperty(strArray,1l,"test");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// System.out.println("方法执行结束时间" + new Date());
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
	
	/**
	 * 拼装错误sql
	 * 
	 * @param @param fileName
	 * @param @param fileId
	 * @param @param msg
	 * @param @param createBy
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	private String getErrorSql(String fileName, Long fileId, String msg, String createBy) {
		StringBuffer errSql = new StringBuffer(" INSERT INTO  T_ABATCH_ERRE_LOG (ABATCH_ERRE_ID,ERRE_FILE_NAME,ERRE_DATA,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,FILE_ID) VALUES ( ");
		errSql.append(" S_T_ABATCH_ERRE_LOG.nextval ");
		errSql.append(", '");
		errSql.append(fileName);
		errSql.append("' ,'");
		errSql.append(msg);
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
		return errSql.toString();
	}
	
	public void saveProperty(final List<String[]> strArray ,Long fileId, String fileName, String createBy) throws Exception {
		if(strArray.size()<=0 || strArray == null){
			throw new Exception("入库数据为空,请检查数据文件！");
		}
		
		boolean flag = false;
		/* 影城内码缓存  */
		final Map<String, String> innerCodeCache = new HashMap<String, String>();
		// 批量跟新缓存
		List<String> batch = new ArrayList<String>();
		// 格式化
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		KpiBean kpiBean = null;
		//AbatchErreLogBean errBean = null;
		// 每行记录
		String[] beans = null;
		/* 缓存错误信息 */
		StringBuilder logMsg = null;
		for (int i = 0; i < strArray.size(); i++) {
			logMsg = new StringBuilder();
			beans = strArray.get(i);
			if(beans.length < 4) {
				rowDateLog(logMsg, beans, "请校验填写的数据是否按照模板提示填写。", i);
			} else {
				kpiBean = new KpiBean();
				// 迭代每行数据
				outer:for(int c=0,len=beans.length;c<len;c++) {
					switch(c) {
					case 0:	//KPI名称
						if (beans[c] != null && !"".equals(beans[c])) {
							kpiBean.setKpiName(beans[c]);
						} else {
							rowDateLog(logMsg, beans, "KPI名称不能为空", i);
						}
						break;
					case 1:	//KPI年份
						if (beans[c] != null && !"".equals(beans[c])) {
							try {
								Date da = format.parse(beans[c]);
								kpiBean.setKpiYear(format.format(da));
							} catch (Exception e) {
								rowDateLog(logMsg, beans, "日期格式有误(例：2008-08-08).请以文本格式保存", i);
							}
						} else {
							rowDateLog(logMsg, beans, "KPI年份不能为空", i);
						}
						break;
					case 2:	//影城内码
						if (beans[c] != null && !"".equals(beans[c])) {
							// 读取影城内码缓存数据
							if(innerCodeCache == null || innerCodeCache.size() == 0) {
								baseDao.getAllCinema(innerCodeCache);
								if(innerCodeCache.size() == 0) {
									new SystemException("读取影城内码缓存失败");
								}
							}
							if(innerCodeCache.containsKey(beans[c])) { //影城内码存在
								// 存注册影城Id
								kpiBean.setCinemaId(Long.parseLong(innerCodeCache.get(beans[c])));
							} else {
								rowDateLog(logMsg, beans, "影城内码不存在", i);
							}
						} else {
							rowDateLog(logMsg, beans, "影城内码不能为空", i);
						}
						break;
					case 3:	//KPI值
						if (beans[c] != null && !"".equals(beans[c])) {
							try {
								// 四舍五入取整
								BigDecimal kpi = new BigDecimal(beans[c]).setScale(0, BigDecimal.ROUND_HALF_UP);
								kpiBean.setKpiValue(kpi.doubleValue());
							} catch (Exception e) {
								rowDateLog(logMsg, beans, "KPI数值必须为数字", i);
							}
								
						} else {
							rowDateLog(logMsg, beans, "KPI数值不能为空", i);
						}
						break;
					default:
						break outer;
					}
					
					if(logMsg.length() > 0) { //如果消息日志不为空说明有错误信息输出
						System.out.println(logMsg.toString());
						break;
					}
				}
			}
			
			if (logMsg != null && logMsg.length() > 0) {
				flag = true;
				batch.add(getErrorSql(fileName, fileId, logMsg.toString(), createBy));
			} else {
				// 验证KPI是否已存在
				if(baseDao.existKpiValue(kpiBean.getKpiYear(),kpiBean.getKpiName() , kpiBean.getCinemaId()+"")) {
					System.out.println("数据已存在不做入库处理");
					rowDateLog(logMsg, beans, "数据已存在不做入库处理", i);
					flag = true;
					batch.add(getErrorSql(fileName, fileId, logMsg.toString(), createBy));
				} else {
					// 1、注册时默认为新会员; 注册类型，需要接口中定义该字段 1:主动注册;2:自动转换;3:批量导入
					// 2招募渠道
					// 3会员状态，注册是设置成有效 1有效 0无效
					// 4逻辑删除标识,默认:0 未删除;1删除;其他:非法
					StringBuffer memberKpiSqlBf = new StringBuffer(
							" INSERT INTO T_MEMBER_KPI (MEMBER_KPI_ID, KPI_YEAR, CINEMA_ID, KPI_VALUE,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,VERSION,KPI_NAME,KPI_TYPE ) VALUES ( S_T_MEMBER_KPI.NEXTVAL, '");
					// 添加插入值
					memberKpiSqlBf.append(kpiBean.getKpiYear());
					memberKpiSqlBf.append("' , ");
					memberKpiSqlBf.append(kpiBean.getCinemaId());
					memberKpiSqlBf.append(" , ");
					memberKpiSqlBf.append(kpiBean.getKpiValue());
					memberKpiSqlBf.append(" , ");
					memberKpiSqlBf.append("'"+createBy+"'");
					memberKpiSqlBf.append(" , ");
					memberKpiSqlBf.append("systimestamp");
					memberKpiSqlBf.append(" , '");
					memberKpiSqlBf.append(createBy);
					memberKpiSqlBf.append("' ,");
					memberKpiSqlBf.append("systimestamp");
					
					memberKpiSqlBf.append(",");
					memberKpiSqlBf.append(0);
					
					memberKpiSqlBf.append(", '");
					memberKpiSqlBf.append(kpiBean.getKpiName());
					
					memberKpiSqlBf.append("' ,");
					memberKpiSqlBf.append("'R'");
					
					memberKpiSqlBf.append(" )");
					System.out.println("KPI 执行sql语句==================================="
							+ memberKpiSqlBf.toString());
					batch.add(memberKpiSqlBf.toString());
					//getJdbcTemplate().execute(memberKpiSqlBf.toString());
				}
			}
			
			if(batch.size() > 100) {
				// KPI批量更新入库
				getJdbcTemplate().batchUpdate(batch.toArray(new String[] {}));
				// 清除
				batch.clear();
			}
			
			kpiBean = null;
			logMsg = null;
			beans = null;
		}
		
		// 更新最后的数据
		if(batch.size() > 0) {
			// KPI批量更新入库
			getJdbcTemplate().batchUpdate(batch.toArray(new String[] {}));
			// 清除
			batch.clear();
		}
		
		if(flag){
			String updateFileSql = " update T_FILE_ATTACH set FILE_STATUS = 'E' where FILE_ATTACH_ID = " + fileId ;
			getJdbcTemplate().execute(updateFileSql);
		}else{
			String updateFileSql = " update T_FILE_ATTACH set FILE_STATUS = 'S' where FILE_ATTACH_ID = " + fileId ;
			getJdbcTemplate().execute(updateFileSql);
		}
		
	}
}
