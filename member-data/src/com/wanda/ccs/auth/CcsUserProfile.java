package com.wanda.ccs.auth;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.StringTokenizer;

import com.aggrepoint.adk.plugin.WinletUserProfile;

public class CcsUserProfile extends WinletUserProfile {
	static final String PROP_SESSION_ID = "sid";
	static final String PROP_SYS_ID = "sysid";
	static final String PROP_FROM_POS = "frompos";
	static final String PROP_FROM_SSO = "fromsso";
	static final String PROP_FROM_NC = "fromnc";
	static final String PROP_RIGHTS = "rights";
	static final String PROP_CINEMA_ID = "cinemaid";
	static final String PROP_CINEMA_NAME = "cinemaname";
	static final String PROP_REGION_CODE = "regioncode";
	static final String PROP_REGION_NAME = "regionname";
	static final String PROP_USER_LEVEL = "level";
	static final String PROP_EMPOLYEE_CODE = "employeecode";
	static final String PROP_POS_TYPE = "postype";
	static final String PROP_OP_GROUP = "opgroup";
	static final String PROP_OP_STATION = "opstation";
	static final String PROP_NC_PK_PSNBASDOC = "pk_psnbasdoc";
	static final String PROP_NC_UNITNAME = "ncunitname";
	static final String PROP_NC_UNITCODE = "ncunitcode";
	static final String VALUE_YES = "yes";
	static final String VALUE_NO = "no";
	static final String PROP_FROM_CENTER = "fromcenter";
	static final String PROP_REGION_RIGHTS = "regionrights";
	static final String PROP_CINEMA_RIGHTS = "cinemarights";

	public CcsUserProfile() {
		m_htProperties.put(PROP_FROM_POS, VALUE_NO);
		m_htProperties.put(PROP_FROM_SSO, VALUE_NO);
	}

	public CcsUserProfile(String str) throws UnsupportedEncodingException {
		super(str);
	}

	void setProp(String name, String value) {
		if (name == null)
			return;

		if (value == null)
			m_htProperties.remove(name);
		else
			m_htProperties.put(name, value);
	}

	/**
	 * 获取用户 ID
	 * 
	 * @return 用户ID
	 */
	public String getId() {
		return m_htProperties.get(PROPERTY_ID);
	}

	/**
	 * 设置用户ID
	 * 
	 * @param id
	 *            用户ID
	 */
	public void setId(String id) {
		setProp(PROPERTY_ID, id);
	}

	/**
	 * 获取用户 ID
	 * 
	 * @return 用户ID
	 */
	public Long getSysId() {
		try {
			return Long.parseLong(m_htProperties.get(PROP_SYS_ID));
		} catch (Exception e) {
			return 0l;
		}
	}

	/**
	 * 设置用户ID
	 * 
	 * @param id
	 *            用户ID
	 */
	public void setSysId(Long id) {
		if (id == null)
			m_htProperties.remove(PROP_SYS_ID);
		setProp(PROP_SYS_ID, id.toString());
	}

	/**
	 * 获取用户姓名
	 * 
	 * @return 用户姓名
	 */
	public String getName() {
		return m_htProperties.get(PROPERTY_NAME);
	}

	public void setName(String name) {
		setProp(PROPERTY_NAME, name);
	}

	/**
	 * 获取员工工号。只有从POS嵌入卡前台登录才会有该属性
	 * 
	 * @return 员工工号
	 */
	public String getEmployeeCode() {
		return m_htProperties.get(PROP_EMPOLYEE_CODE);
	}

	public void setEmployeeCode(String code) {
		setProp(PROP_EMPOLYEE_CODE, code);
	}

	/**
	 * 获取用户电子邮件
	 * 
	 * @return　用户电子邮件
	 */
	public String getEmail() {
		return m_htProperties.get(PROPERTY_EMAIL);
	}

	public void setEmail(String str) {
		setProp(PROPERTY_EMAIL, str);
	}

	/**
	 * 获取用户所属级别
	 * 
	 * @return 用户所属级别
	 */
	public UserLevel getLevel() {
		return UserLevel.fromName(m_htProperties.get(PROP_USER_LEVEL));
	}

	public void setLevel(UserLevel level) {
		setProp(PROP_USER_LEVEL, level.name());
	}

	public String getLevelName() {
		return UserLevel.fromName(m_htProperties.get(PROP_USER_LEVEL)).name();
	}

	/**
	 * 获取区域或影城用户所属区域代码
	 * 
	 * @return 区域或影城用户所属区域代码
	 */
	public String getRegionCode() {
		return m_htProperties.get(PROP_REGION_CODE);
	}

	public void setRegionCode(String str) {
		setProp(PROP_REGION_CODE, str);
	}

	/**
	 * 获取区域或影城用户所属区域名称
	 * 
	 * @return 区域或影城用户所属区域名称
	 */
	public String getRegionName() {
		return m_htProperties.get(PROP_REGION_NAME);
	}

	/**
	 * 设置区域或影城用户所属区域名称
	 * 
	 * @param str
	 */
	public void setRegionName(String str) {
		setProp(PROP_REGION_NAME, str);
	}

	/**
	 * 获取影城用户所属影城ID
	 * 
	 * @return 影城用户所属影城ID
	 */
	public long getCinemaId() {
		String scinemaid = m_htProperties.get(PROP_CINEMA_ID);
		return scinemaid == null ? 0l : Long.parseLong(scinemaid);
	}

	/**
	 * 设置影城用户所属影城ID
	 * 
	 * @param id
	 *            影城用户所属影城ID
	 */
	public void setCinemaId(long id) {
		setProp(PROP_CINEMA_ID, Long.toString(id));
	}

	/**
	 * 获取影城用户所属影城名称
	 * 
	 * @return 影城用户所属影城名称
	 */
	public String getCinemaName() {
		return m_htProperties.get(PROP_CINEMA_NAME);
	}

	/**
	 * 设置影城名称
	 * 
	 * @param str
	 *            影城用户所属影城名称
	 */
	public void setCinemaName(String str) {
		setProp(PROP_CINEMA_NAME, str);
	}

	/**
	 * 获取用户具备的权限
	 * 
	 * @return 用户具备的权限
	 */
	public HashSet<String> getRights() {
		HashSet<String> rights = new HashSet<String>();

		String str = m_htProperties.get(PROP_RIGHTS);
		StringTokenizer st = new StringTokenizer(str, ",");
		while (st.hasMoreTokens())
			rights.add(st.nextToken());

		return rights;
	}

	public boolean hasRight(String right) {
		if (right == null)
			return false;

		String str = m_htProperties.get(PROP_RIGHTS);
		StringTokenizer st = new StringTokenizer(str, ",");
		while (st.hasMoreTokens())
			if (right.equals(st.nextToken()))
				return true;
		return false;
	}

	/**
	 * 设置用户权限
	 * 
	 * @param rights
	 */
	public void setRights(HashSet<String> rights) {
		boolean bFirst = true;
		StringBuffer sb = new StringBuffer();
		for (String right : rights) {
			if (bFirst)
				bFirst = false;
			else
				sb.append(",");
			sb.append(right);
		}

		setProp(PROP_RIGHTS, sb.toString());
	}

	/**
	 * 设置用户权限
	 * 
	 * @param rights
	 */
	public void setRights(String[] rights) {
		boolean bFirst = true;
		StringBuffer sb = new StringBuffer();
		for (String right : rights) {
			if (bFirst)
				bFirst = false;
			else
				sb.append(",");
			sb.append(right);
		}

		setProp(PROP_RIGHTS, sb.toString());
	}

	public boolean isFromPos() {
		return "yes".equalsIgnoreCase(m_htProperties.get(PROP_FROM_POS));
	}

	public void setFromPos() {
		setProp(PROP_FROM_POS, VALUE_YES);
	}

	public boolean isFromSso() {
		return "yes".equalsIgnoreCase(m_htProperties.get(PROP_FROM_SSO));
	}

	public void setFromSso() {
		setProp(PROP_FROM_SSO, VALUE_YES);
	}

	public boolean isFromNc() {
		return "yes".equalsIgnoreCase(m_htProperties.get(PROP_FROM_NC));
	}

	public void setFromNc(String fromNc) {
		setProp(PROP_FROM_NC, fromNc);
	}
	
	public boolean isFromCenter() {
		return "yes".equalsIgnoreCase(m_htProperties.get(PROP_FROM_CENTER));
	}

	public void setFromCenter(String isCenter) {
		setProp(PROP_FROM_CENTER, isCenter);
	}

	/**
	 * 获取POS类型。只有从POS嵌入卡前台登录才会有该属性
	 * 
	 * @return POS类型
	 */
	public String getPosType() {
		return m_htProperties.get(PROP_POS_TYPE);
	}

	public void setPosType(String type) {
		setProp(PROP_POS_TYPE, type);
	}

	/**
	 * 获取工作站组。只有从POS嵌入卡前台登录才会有该属性
	 * 
	 * @return 工作站组
	 */
	public String getOpGroup() {
		return m_htProperties.get(PROP_OP_GROUP);
	}

	public void setOpGroup(String group) {
		setProp(PROP_OP_GROUP, group);
	}

	/**
	 * 获取工作站。只有从POS嵌入卡前台登录才会有该属性
	 * 
	 * @return 工作站
	 */
	public String getOpStation() {
		return m_htProperties.get(PROP_OP_STATION);
	}

	public void setOpStation(String station) {
		setProp(PROP_OP_STATION, station);
	}

	public String getNcPkPsnbasedoc() {
		return m_htProperties.get(PROP_NC_PK_PSNBASDOC);
	}

	public void setNcPkPsnbasedoc(String str) {
		setProp(PROP_NC_PK_PSNBASDOC, str);
	}

	public String getNcUnitName() {
		return m_htProperties.get(PROP_NC_UNITNAME);
	}

	public void setNcUnitName(String name) {
		setProp(PROP_NC_UNITNAME, name);
	}

	public String getNcUnitCode() {
		return m_htProperties.get(PROP_NC_UNITCODE);
	}

	public void setNcUnitCode(String code) {
		setProp(PROP_NC_UNITCODE, code);
	}
	
	/**
	 * 获取中心店用户区域身份具备的权限
	 * 
	 * @return 用户具备的权限
	 */
	public HashSet<String> getRegionRights() {
		HashSet<String> rights = new HashSet<String>();

		String str = m_htProperties.get(PROP_REGION_RIGHTS);
		StringTokenizer st = new StringTokenizer(str, ",");
		while (st.hasMoreTokens())
			rights.add(st.nextToken());

		return rights;
	}


	/**
	 * 设置中心店用户区域权限
	 * 
	 * @param rights
	 */
	public void setRegionRights(HashSet<String> rights) {
		boolean bFirst = true;
		StringBuffer sb = new StringBuffer();
		for (String right : rights) {
			if (bFirst)
				bFirst = false;
			else
				sb.append(",");
			sb.append(right);
		}

		setProp(PROP_REGION_RIGHTS, sb.toString());
	}
	
	/**
	 * 获取中心店用户影城身份具备的权限
	 * 
	 * @return 用户具备的权限
	 */
	public HashSet<String> getCinemaRights() {
		HashSet<String> rights = new HashSet<String>();

		String str = m_htProperties.get(PROP_CINEMA_RIGHTS);
		StringTokenizer st = new StringTokenizer(str, ",");
		while (st.hasMoreTokens())
			rights.add(st.nextToken());

		return rights;
	}


	/**
	 * 设置中心店用户影城权限
	 * 
	 * @param rights
	 */
	public void setCinemaRights(HashSet<String> rights) {
		boolean bFirst = true;
		StringBuffer sb = new StringBuffer();
		for (String right : rights) {
			if (bFirst)
				bFirst = false;
			else
				sb.append(",");
			sb.append(right);
		}

		setProp(PROP_CINEMA_RIGHTS, sb.toString());
	}
}
