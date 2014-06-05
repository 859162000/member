package com.wanda.ccs.jobhub.smsreport.service.impl;

import javax.sql.DataSource;

import org.springframework.transaction.annotation.Transactional;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.jobhub.smsreport.service.StatusChangingService;


public class StatusChangingServiceImpl implements StatusChangingService {
	
	@InstanceIn(path = "/dataSource")
	private DataSource dataSource;
	
	private ExtJdbcTemplate extJdbcTemplate = null;
	
	public ExtJdbcTemplate getJdbcTemplate()  {
		if(this.extJdbcTemplate == null) {
			this.extJdbcTemplate = new ExtJdbcTemplate(dataSource);
		}
		return this.extJdbcTemplate;
	}
	
	/* (non-Javadoc)
	 * @see com.wanda.ccs.jobhub.member.service.impl.StatusChangingService#updateStatus()
	 */
	@Transactional
	public void updateStatus() {
		String[] sqlArray = {
				" update T_EXT_POINT_RULE set status = '30' where sysdate >= START_DTIME and status = '20' ", 
				" update T_EXT_POINT_RULE set status = '40' where sysdate >= END_DTIME and status != '50' ",
				" update T_CAMPAIGN set status = '30' where sysdate >= START_DATE and STATUS != '30' ",
				" update T_CAMPAIGN set status = '40' where sysdate >= END_DATE and STATUS != '40' ",
				" update T_CMN_PHASE set status = '30' where sysdate >= START_DATE and STATUS != '30' ",
				" update T_CMN_PHASE set status = '40' where sysdate >= END_DATE and STATUS != '40' ",
				" update T_CMN_ACTIVITY set status = '30' where sysdate >= START_DTIME and STATUS = '20' ",
				" update T_CMN_ACTIVITY set status = '40' where sysdate >= END_DTIME and (STATUS = '20' or STATUS = '30')",};
		getJdbcTemplate().batchUpdate(sqlArray);
	}
	
}
