package com.wanda.ccs.jobhub.member.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.jobhub.member.vo.CityBean;
import com.wanda.ccs.jobhub.member.vo.MemberBean;
import com.wanda.ccs.jobhub.member.vo.Province;
import com.wanda.ccs.jobhub.member.vo.TCinemaBean;
/**
 * 整合批量导入dao 可继承该类 重写saveProperty方法
 * 
 * @author chenxm
 * 
 */
public class BaseCoreDao  {

	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;
	
	private ExtJdbcTemplate extJdbcTemplate = null;
	
	public ExtJdbcTemplate getJdbcTemplate()  {
		if(this.extJdbcTemplate == null) {
			this.extJdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.extJdbcTemplate;
	}

	/**
	 * 根据不同需求重写方法内容,实现自己的数据的合法性验证等相关操作
	 * 
	 * @param beanDatas
	 *            文件对应的每行数据
	 * @throws Exception
	 */
	public void saveProperty(final List<String[]> beanDatas, Long fileId,
			String fileName) throws Exception {
		if (beanDatas.size() <= 0 || beanDatas == null) {
			throw new Exception("入库数据为空,请检查数据文件！");
		}
		// String sql =
		// " insert into t_voucher_pool_detail(voucher_pool_detail_id, voucher_pool_id, voucher_number, print_code) values (S_T_VOUCHER_POOL_DETAIL.NEXTVAL, ?, ?, ?) ";
		// try{
		// jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter(){
		// public int getBatchSize() {
		// return strArray.size();
		// }
		// public void setValues(PreparedStatement pst, int i) throws
		// SQLException {
		// VoucherPoolBean bean = null;
		// String[] beans = strArray.get(i);
		// if(beans[0] == null && "".equals(beans[0])){
		// System.out.println("第" + i + "行出现错误请从新输入");
		// }else if(beans[1] == null || "".equals(beans[1])) {
		// System.out.println("第" + i + "行出现错误请从新输入");
		// }else if(beans[2] == null || "".equals(beans[2])) {
		// System.out.println("第" + i + "行出现错误请从新输入");
		// }
		// bean = new VoucherPoolBean();
		// bean.setVoucherPoolId(Double.parseDouble(beans[0]));
		// bean.setVoucherPoolName(beans[1]);
		// bean.setVoucherPoolCode(beans[2]);
		// pst.setDouble(1, bean.getVoucherPoolId());
		// pst.setString(2, bean.getVoucherPoolCode());
		// pst.setString(3, bean.getVoucherPoolName());
		// }
		// });
		// }catch(Exception e){
		// e.printStackTrace();
		// throw new Exception(e.getMessage());
		// }
	}

	/**
	 * 检查此手机号是否已注册
	 * 
	 * @param mobile
	 * @return
	 */
	public boolean chechMobileIsHave(String mobile) {
		String mobileSql = " SELECT COUNT(*) FROM T_MEMBER WHERE MOBILE = '"
				+ mobile.replace(".", "").substring(0, 11) + "'";
		int count = getJdbcTemplate().queryForInt(mobileSql);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 获取影城信息
	 * 
	 * @param cinemaName
	 * @return
	 */
	public TCinemaBean getCinema(final String innerCode) {
		String sql = " Select SEQID,INNER_NAME,INNER_CODE from T_CINEMA where INNER_CODE = '"
				+ innerCode + "'";
		
		List<TCinemaBean> list = getJdbcTemplate().query(sql, new RowMapper<TCinemaBean>() {
			public TCinemaBean mapRow(ResultSet rs, int row)
					throws SQLException {
					TCinemaBean cinemaBean = new TCinemaBean();
					cinemaBean.setCinemaId(rs.getLong("SEQID"));
					cinemaBean.setInnerName(rs.getString("INNER_NAME"));
					cinemaBean.setInnerCode(rs.getString("INNER_CODE"));
				return cinemaBean;
			}
			
		});
		
		if(list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 所有影城内码缓存
	 * 
	 * @param @param innerCode
	 * @param @return    设定文件 
	 * @return TCinemaBean    返回类型 
	 * @throws
	 */
	public void getAllCinema(final Map<String, String> innerCodeCache) {
		
		String sql = "select T.SEQID, T.INNER_CODE from T_CINEMA T";
		
		getJdbcTemplate().query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				innerCodeCache.put(rs.getString("INNER_CODE"), rs.getString("SEQID"));
			}
		});
	}
	
	/**
	 * 
	 * 
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public boolean existKpiValue(String date, String kpiName, String cinemaId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select COUNT(*) from T_MEMBER_KPI T where T.KPI_YEAR='").append(date).append("' and T.KPI_NAME='").append(kpiName).append("' and T.CINEMA_ID=").append(cinemaId);
		int count = getJdbcTemplate().queryForInt(sql.toString());
		if(count > 0) {
			return true;
		}
		
		return false;
	}

	/**
	 * 获取城市
	 * 
	 * @param cityName
	 * @return
	 */
	public CityBean getCity(final String cityName) {
		final CityBean city = new CityBean();
		String sql = " Select NAME ,SEQID from T_CITY where NAME LIKE '%"
				+ cityName + "%'";
		getJdbcTemplate().query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				String cName = rs.getString("NAME");
				Long cityId = rs.getLong("SEQID");
				city.setCityId(cityId);
				city.setCityName(cName);
			}
		});
		return city;
	}

	/**
	 * 获取省
	 * 
	 * @param provinceName
	 * @return
	 */
	public Province getProvince(final String provinceName) {
		final Province province = new Province();
		String sql = " Select NAME ,SEQID from T_PROVINCE where NAME LIKE '%"
				+ provinceName + "%'";
		getJdbcTemplate().query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				String pName = rs.getString("NAME");
				Long pId = rs.getLong("SEQID");
				province.setProvince(pName);
				province.setProvinceId(pId);
			}
		});
		return province;
	}
	
	/**
	 * 获取会员信息
	 * 
	 * @param mobile
	 * @return
	 */
	public MemberBean getMemberBean(String mobile) {
		String sql = " Select MEMBER_ID from T_MEMBER where MOBILE = '" + mobile + "'";
		List<MemberBean> list = getJdbcTemplate().query(sql, new RowMapper<MemberBean>() {
			public MemberBean mapRow(ResultSet rs, int row)
					throws SQLException {
				MemberBean memberBean = new MemberBean();
				memberBean.setMemberId(rs.getLong("MEMBER_ID"));
				return memberBean;
			}
		});
		
		if(list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 获取会员
	 * 
	 * @param mobile
	 * @return
	 */
	/*public MemberBean getMemberBean(final String mobile) {
		final MemberBean memberBean = new MemberBean();
		String sql = " Select MEMBER_ID  from T_MEMBER where MOBILE = '"
				+ mobile + "'";
		getJdbcTemplate().query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				// String pName = rs.getString("NAME");
				Long memberId = rs.getLong("MEMBER_ID");
				memberBean.setMemberId(memberId);
				// province.setProvince(pName);
				// province.setProvinceId(pId);
			}
		});
		return memberBean;
	}*/

}
