package com.wanda.ccs.film.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wanda.ccs.film.dao.ITFilmCopyDao;
import com.wanda.ccs.film.dao.ITFilmCopyScheduleDao;
import com.wanda.ccs.film.service.TFilmCopyScheduleService;
import com.wanda.ccs.film.service.TFilmCopyTrackLogService;
import com.wanda.ccs.model.IDimType;
import com.wanda.ccs.model.TFilmCopy;
import com.wanda.ccs.model.TFilmCopySchedule;
import com.wanda.ccs.model.TFilmCopyTrackLog;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.exception.ApplicationException;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.core.util.StringUtil;
import com.xcesys.extras.util.SecurityUtil;

@Service
public class TFilmCopyScheduleServiceImpl extends
		BaseCrudServiceImpl<TFilmCopySchedule> implements
		TFilmCopyScheduleService {

	@Autowired
	private ITFilmCopyScheduleDao schDao;
	@Autowired
	private ITFilmCopyDao copyDao;
	@Autowired
	private TFilmCopyTrackLogService trackService;

	@Override
	public IBaseDao<TFilmCopySchedule> getDao() {
		return schDao;
	}

	@Override
	public PageResult<TFilmCopySchedule> findByCriteria(
			QueryCriteria<String, Object> query) {
		
		String fromClause = "from TFilmCopySchedule c left join fetch c.fromTPublisher fp left join fetch c.fromCinema fc left join fetch c.toTPublisher tp left join fetch c.toCinema tc";
		String[] whereBodies = new String[] {
				"UPPER(c.tFilmCopy.copyNo) like UPPER(:copyNo)", 
				"UPPER(c.tFilm.filmName) like UPPER(:filmName)",
				"c.status = :status", 
				"(UPPER(fp.publishername) like :sendFrom or UPPER(fc.shortName) like :sendFrom)",
				"(UPPER(tp.publishername) like UPPER(:sendTo) or UPPER(tc.shortName) like UPPER(:sendTo))",
				"c.tFilmCopy.tFilm.yearMonth  like (:yearMonth)",
				"c.sendDate >= :startSendDate", 
				"c.sendDate <=:endSendDate",
				"c.receiveDate >= :stsrtReceiveDate",
				"c.receiveDate <= :endReceiveDate"
				};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.status asc,c.sendDate desc";
		}
		PageResult<TFilmCopySchedule> result = schDao.queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;
	}

	@Override
	public boolean checkExisted(String copyNo, String filmId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TFilmCopy c where ";
		if (!StringUtil.isNullOrBlank(copyNo)) {
			hql += " c.copyNo = :copyNo ";
			params.put("copyNo", copyNo);
		} else {
			return false;
		}
		if (!StringUtil.isNullOrBlank(filmId)) {
			hql += " and c.filmId = :filmId ";
			params.put("filmId", NumberUtils.toLong(filmId));
		} else {
			return false;
		}
		int count = super.queryCountHql(hql, params, null);
		return count > 0;
	}

	/**
	 * 修改拷贝状态，确认接收
	 */
	@Override
	public void receive(Long[] ids) {

		List<TFilmCopySchedule> uptList = new ArrayList<TFilmCopySchedule>();
		List<TFilmCopyTrackLog> trkList = new ArrayList<TFilmCopyTrackLog>();

		for (TFilmCopySchedule sch : findByIds(ids)) {
			if (IDimType.DIMTYPE_FILMCOPY_STATUS_SEND.equals(sch.getStatus())) {
				sch.setStatus(IDimType.DIMTYPE_FILMCOPY_STATUS_IS);
				sch.setReceiveDate(DateUtil.getCurrentDate());
				uptList.add(sch);
				
				//更新拷贝行踪
				updateTrackLog(sch,trkList);
			} else {
				throw new ApplicationException("未提交或已确认接收的调度不能做确认接收操作!");
			}

		}
		if (uptList.size() > 0) {
			super.update(uptList);
			trackService.update(trkList);
		}
	}

	@Override
	public void delete(Collection<TFilmCopySchedule> list) {
		List<TFilmCopyTrackLog> trkList = new ArrayList<TFilmCopyTrackLog>();
		List<TFilmCopySchedule> removableSchList = new ArrayList<TFilmCopySchedule>();
		for (TFilmCopySchedule o : list) {
			if (IDimType.DIMTYPE_FILMCOPY_STATUS_NOTSEND.equals(o.getStatus())) {
				removableSchList.add(o);
				
				//更新拷贝行踪
				updateTrackLog(o,trkList);
			}else{
				throw new ApplicationException("已发送的调度不可删除");
			}
		}
		if (removableSchList.size() > 0) {
			super.delete(removableSchList);
			trackService.delete(trkList);
		}
	}

	@Override
	public void sendBackPublisher(Long[] ids) {

		Assert.isTrue(ids != null, "请选择数据记录！");

		List<TFilmCopySchedule> savableList = new ArrayList<TFilmCopySchedule>();
		List<TFilmCopyTrackLog> savableLogs = new ArrayList<TFilmCopyTrackLog>();

		for (TFilmCopySchedule sch : findByIds(ids)) {
			if (IDimType.DIMTYPE_FILMCOPY_STATUS_IS.equals(sch.getStatus())) {
				if (IDimType.DIMTYPE_COPY_TYPE_MAIDUAN.equals(sch.gettFilmCopy().getCopyType())) {
					throw new ApplicationException("影片[" + sch.gettFilm().getFilmName() + "]为买断不可还片商!<br>");
				}
				
				TFilmCopySchedule newSch = new TFilmCopySchedule();
				BeanUtils.copyProperties(sch, newSch,new String[]{"id","version"});
				newSch.setId(null);
				newSch.setStatus(IDimType.DIMTYPE_FILMCOPY_STATUS_SEND);
				newSch.setSendFrom(sch.getSendTo());
				newSch.setFromFlag(sch.getToFlag());
				if (sch.gettFilmCopy().getSupplyPublisherId() != null) {
					newSch.setSendTo(sch.gettFilmCopy().getSupplyPublisherId());
				}else if(sch.gettFilm().getPublisherId()!=null){
					newSch.setSendTo(sch.gettFilm().getPublisherId());
				}else if(sch.gettFilm().getPublisherId2()!=null){
					newSch.setSendTo(sch.gettFilm().getPublisherId2());
				}else{
					throw new ApplicationException("无法找到影片["+sch.gettFilm().getFilmName()+"]的来源供片商,故不可还片商!<br>");
				}
				newSch.setToFlag(IDimType.FILMCOPY_FROMTO_PUBLISHER);
				newSch.setSendDate(DateUtil.getCurrentDate());
				newSch.setSendOperator(SecurityUtil.getLoginUser());
				savableList.add(newSch);

				// 新增行踪
				TFilmCopyTrackLog newLog = new TFilmCopyTrackLog();
				BeanUtils.copyProperties(newSch, newLog, new String[] { "id","version" });
				savableLogs.add(newLog);
			}else{
				throw new ApplicationException("不是"+'"'+"已接受"+'"'+"状态的调度不可还片商");
			}
		}


		if (savableList.size() > 0) {
			super.createOrUpdate(savableList);
			trackService.createOrUpdate(savableLogs);
		}

	}

	@Override
	public void createOrUpdate(TFilmCopySchedule s) {
		TFilmCopy c = s.gettFilmCopy();
		if (c != null) {
			copyDao.createOrUpdate(c);
		}

		// 拷贝行踪
		TFilmCopyTrackLog cu = new TFilmCopyTrackLog();
		if(s.getId() != null){
			TFilmCopySchedule old = getDao().getById(s.getId());
			cu.setFilmId(old.getFilmId());
			cu.setSendFrom(old.getSendFrom());
			cu.setSendTo(old.getSendTo());
			cu.setFromFlag(old.getFromFlag());
			cu.setToFlag(old.getToFlag());
			getDao().evict(old);
			List<TFilmCopyTrackLog> list = trackService.find(cu);
			if(list.size() > 0){
				TFilmCopyTrackLog newlog = list.get(0);
				BeanUtils.copyProperties(s,newlog, new String[] {"id","version"});
				trackService.update(newlog);
			}
		}else{
			BeanUtils.copyProperties(s, cu, new String[] { "id","version"});
			trackService.create(cu);
		}
		super.createOrUpdate(s);
	}

	@Override
	public void commitSchedule(Long[] ids) {

		List<TFilmCopySchedule> uptList = new ArrayList<TFilmCopySchedule>();
		List<TFilmCopyTrackLog> trkList = new ArrayList<TFilmCopyTrackLog>();
		List<TFilmCopySchedule> srhList = findByIds(ids);
		for (TFilmCopySchedule sch : srhList) {
			if (IDimType.DIMTYPE_FILMCOPY_STATUS_NOTSEND
					.equals(sch.getStatus())) {
				sch.setStatus(IDimType.DIMTYPE_FILMCOPY_STATUS_SEND);
				//sch.setSendDate(DateUtil.getCurrentDate());
				sch.setSendOperator(SecurityUtil.getLoginUser());
				uptList.add(sch);
				
				//更新拷贝行踪
				updateTrackLog(sch,trkList);
			} else {
				throw new ApplicationException("已提交调度的不可重复提交!");
			}
		}

		update(uptList);
		trackService.update(trkList);
	}

	private void updateTrackLog(TFilmCopySchedule sch, List<TFilmCopyTrackLog> trkList) {
		TFilmCopyTrackLog cu = new TFilmCopyTrackLog();
		TFilmCopySchedule old = getDao().getById(sch.getId());
		cu.setFilmId(old.getFilmId());
		cu.setSendFrom(old.getSendFrom());
		cu.setSendTo(old.getSendTo());
		cu.setFromFlag(old.getFromFlag());
		cu.setToFlag(old.getToFlag());
		getDao().evict(old);
		List<TFilmCopyTrackLog> list = trackService.find(cu);
		if(list.size() > 0){
			TFilmCopyTrackLog log = list.get(0);
			BeanUtils.copyProperties(sch, log, new String[] { "id","version"});
			trkList.add(log);
		}
	}

	@Override
	public void cancelSchedule(Long[] ids) {

		List<TFilmCopySchedule> uptList = new ArrayList<TFilmCopySchedule>();
		List<TFilmCopyTrackLog> trkList = new ArrayList<TFilmCopyTrackLog>();
		List<TFilmCopySchedule> srhList = findByIds(ids);
		for (TFilmCopySchedule sch : srhList) {
			if (IDimType.DIMTYPE_FILMCOPY_STATUS_SEND.equals(sch.getStatus())) {
				sch.setStatus(IDimType.DIMTYPE_FILMCOPY_STATUS_NOTSEND);
				sch.setSendDate(null);
				sch.setSendOperator(null);
				uptList.add(sch);
				
				//更新拷贝行踪
				updateTrackLog(sch,trkList);
			} else {
				throw new ApplicationException("未提交或已确认接收的调度不能做撤销操作!");
			}
		}
		
		trackService.update(trkList);
		update(uptList);
	}
}
