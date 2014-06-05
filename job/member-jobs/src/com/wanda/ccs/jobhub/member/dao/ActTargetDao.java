/**
 * 
 */
package com.wanda.ccs.jobhub.member.dao;

import javax.sql.DataSource;

import com.google.code.pathlet.config.anno.InstanceIn;
import com.google.code.pathlet.jdbc.EntityRowMapper;
import com.google.code.pathlet.jdbc.ExtJdbcTemplate;
import com.wanda.ccs.jobhub.member.vo.TargetVo;


/**
 * @author YangJianbin
 *
 */
public class ActTargetDao {

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
	 * 根据目标id获取波次目标信息
	 * @param targetId
	 * 			波次目标id
	 * @return
	 */
	public TargetVo getTargetVo(Long targetId){
		TargetVo target = (TargetVo) getJdbcTemplate().queryForObject(
				"select target.*,activity.CMN_ACTIVITY_ID, f.FILE_ATTACH_ID from T_ACT_TARGET target left join (T_CMN_ACTIVITY activity left join T_FILE_ATTACH f on activity.CMN_ACTIVITY_ID = f.REF_OBJECT_ID and f.REF_OBJECT_TYPE = 'TCmnActivity') on target.ACT_TARGET_ID = activity.ACT_TARGET_ID where target.ACT_TARGET_ID=?",
				new Object[] { targetId },
				new EntityRowMapper<TargetVo>(TargetVo.class));
		return target;
	}
	
	
}
