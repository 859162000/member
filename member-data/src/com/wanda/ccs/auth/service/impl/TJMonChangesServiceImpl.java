package com.wanda.ccs.auth.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aggrepoint.adk.IModuleRequest;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.auth.dao.ITJMonChangesDao;
import com.wanda.ccs.auth.service.TJMonChangesService;
import com.wanda.ccs.auth.service.TJMonOfficealJobsService;
import com.wanda.ccs.model.TJMonChanges;
import com.wanda.ccs.model.TJMonOfficialJobs;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.util.SpringContextUtil;

@Service
public class TJMonChangesServiceImpl extends BaseCrudServiceImpl<TJMonChanges>
		implements TJMonChangesService {

	@Autowired
	private ITJMonChangesDao dao = null;

	@Override
	public IBaseDao<TJMonChanges> getDao() {
		return dao;
	}

	@Override
	public PageResult<TJMonChanges> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();

		String fromClause = "select c from TJMonChanges c ";
		String joinClause = "";
		queryParam.add("c.approveFlag =:approveFlag  ");
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = " c.approveDate desc, c.createdDate desc, c.regionOrgId ,c.cinemaOrgId  ,c.jobName  ";
		}
		PageResult<TJMonChanges> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public void doApprove(String str, IModuleRequest req) {
		String[] strs = str.split(",");

		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		for (int i = 0; i < strs.length; i++) {
			if ("on".equals(strs[i])) {

			} else {
				String[] s = strs[i].split(";");
				TJMonChanges jmoc = getDao().findById(Long.valueOf(s[0]));
				if ("Y".equals(s[1])) {
					if (null != jmoc.getFromRTX() && null != jmoc.getToRTX()) {
						TJMonOfficialJobs job = SpringContextUtil.getBean(
								TJMonOfficealJobsService.class).getJMonOJByRTX(
								jmoc.getFromRTX());
						if (null != job) {
							job.setCode(jmoc.getToECode());
							job.setEmpName(jmoc.getToEName());
							job.setRtxName(jmoc.getToRTX());
							job.setPhone(jmoc.getToPhone());
							// job.setCinemaOrgId(jmoc.getCinemaOrgId());
							// job.setRegionOrgId(jmoc.getRegionOrgId());
							// job.setJobName(jmoc.getJobName());
							// job.setMainFlag(jmoc.getToMainFlag());
							SpringContextUtil.getBean(
									TJMonOfficealJobsService.class).update(job);
						}
					} else if (null != jmoc.getFromRTX()
							&& null == jmoc.getToRTX()) {
						TJMonOfficialJobs job = SpringContextUtil.getBean(
								TJMonOfficealJobsService.class).getJMonOJByRTX(
								jmoc.getFromRTX());
						if (job != null) {
							SpringContextUtil.getBean(
									TJMonOfficealJobsService.class).delete(job);
						}

					} else if (null == jmoc.getFromRTX()
							&& null != jmoc.getToRTX()) {
						TJMonOfficialJobs job = new TJMonOfficialJobs();
						job.setCode(jmoc.getToECode());
						job.setEmpName(jmoc.getToEName());
						job.setRtxName(jmoc.getToRTX());
						job.setPhone(jmoc.getToPhone());
						job.setCinemaOrgId(jmoc.getCinemaOrgId());
						job.setRegionOrgId(jmoc.getRegionOrgId());
						job.setJobName(jmoc.getJobName());
						job.setMainFlag(jmoc.getToMainFlag());
						job.setCreatedBy(jmoc.getRequestBy());
						job.setUpdatedBy(jmoc.getApproveBy());
						SpringContextUtil.getBean(
								TJMonOfficealJobsService.class).create(job);
					}

				}
				jmoc.setApproveBy(user.getName());
				jmoc.setApproveById(user.getId());
				jmoc.setApproveDate(new Date());
				jmoc.setApproveFlag(s[1]);
				getDao().update(jmoc);
			}
		}
	}

	@Override
	public void doChanges(TJMonOfficialJobs oldJMonOJ,
			TJMonOfficialJobs newJMonOJ, IModuleRequest req) {
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		TJMonChanges tJMonChanges = new TJMonChanges();
		if (oldJMonOJ == null) {
			tJMonChanges.setChangeType("N");
			tJMonChanges.setCinemaOrgId(newJMonOJ.getCinemaOrgId());
			tJMonChanges.setRegionOrgId(newJMonOJ.getRegionOrgId());
			tJMonChanges.setJobName(newJMonOJ.getJobName());
			tJMonChanges.setToECode(null);
			tJMonChanges.setToEName(newJMonOJ.getEmpName());
			tJMonChanges.setToMainFlag(newJMonOJ.getMainFlag());
			tJMonChanges.setToPhone(newJMonOJ.getPhone());
			tJMonChanges.setToRTX(newJMonOJ.getRtxName());
		} else if (newJMonOJ == null) {
			tJMonChanges.setChangeType("D");
			tJMonChanges.setFormMainFlag(oldJMonOJ.getMainFlag());
			tJMonChanges.setFromECode(oldJMonOJ.getCode());
			tJMonChanges.setFromEName(oldJMonOJ.getEmpName());
			tJMonChanges.setFromPhone(oldJMonOJ.getPhone());
			tJMonChanges.setFromRTX(oldJMonOJ.getRtxName());
			tJMonChanges.setCinemaOrgId(oldJMonOJ.getCinemaOrgId());
			tJMonChanges.setRegionOrgId(oldJMonOJ.getRegionOrgId());
			tJMonChanges.setJobName(oldJMonOJ.getJobName());
		} else {
			tJMonChanges.setChangeType("C");
			tJMonChanges.setFormMainFlag(oldJMonOJ.getMainFlag());
			tJMonChanges.setFromECode(oldJMonOJ.getCode());
			tJMonChanges.setFromEName(oldJMonOJ.getEmpName());
			tJMonChanges.setFromPhone(oldJMonOJ.getPhone());
			tJMonChanges.setFromRTX(oldJMonOJ.getRtxName());
			tJMonChanges.setCinemaOrgId(newJMonOJ.getCinemaOrgId());
			tJMonChanges.setRegionOrgId(newJMonOJ.getRegionOrgId());
			tJMonChanges.setJobName(newJMonOJ.getJobName());
			tJMonChanges.setToECode(null);
			tJMonChanges.setToEName(newJMonOJ.getEmpName());
			tJMonChanges.setToMainFlag(newJMonOJ.getMainFlag());
			tJMonChanges.setToPhone(newJMonOJ.getPhone());
			tJMonChanges.setToRTX(newJMonOJ.getRtxName());
		}
		tJMonChanges.setCreatedBy(user.getId());
		tJMonChanges.setCreatedDate(new Date());
		tJMonChanges.setRequestBy(user.getName());
		tJMonChanges.setRequestById(user.getId());
		tJMonChanges.setSourceType("C");
		tJMonChanges.setApproveFlag("W");
		getDao().createOrUpdate(tJMonChanges);
	}

	@Override
	public List<TJMonChanges> doFindTJMonChangesList(String approveFlag) {
		String hql = " select c from TJMonChanges c where c.approveFlag =:approveFlag order by  c.approveDate desc, c.createdDate desc, c.regionOrgId ,c.cinemaOrgId  ,c.jobName  ";
		Map<String, Object> params = new HashMap<String, Object>();
		if (!"".equals(approveFlag)) {
			params.put("approveFlag", approveFlag);
		}
		return getDao().queryHql(hql, params);
	}

	@Override
	public boolean checkRTX(String toRTX) {
		String hql = " select c from TJMonChanges c ";
		Map<String, Object> params = new HashMap<String, Object>();
		if (!"".equals(toRTX)) {
			hql += " where c.toRTX =:toRTX and c.approveFlag = 'W'";
			params.put("toRTX", toRTX);
		}
		return super.queryCountHql(hql, params, null) > 0 ? true : false;
	}

	@SuppressWarnings("static-access")
	@Override
	public List<TJMonChanges> doStickieList() {
		String hql = " select c from TJMonChanges c where c.approveFlag =:approveFlag and c.approveDate >=:approveDate order by c.approveDate desc, c.regionOrgId ,c.cinemaOrgId  ,c.jobName  ";
		Map<String, Object> params = new HashMap<String, Object>();
		Date nowDate = new Date();
		Date approveDate = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar canlandar = Calendar.getInstance();
		canlandar.setTime(nowDate);
		canlandar.add(canlandar.DATE, -5);
		try {
			approveDate = df.parse(df.format(canlandar.getTime()));
			params.put("approveFlag", "Y");
			params.put("approveDate", approveDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return getDao().queryHql(hql, params);
	}

}
