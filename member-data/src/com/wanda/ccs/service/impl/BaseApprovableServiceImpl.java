package com.wanda.ccs.service.impl;

import org.apache.commons.lang.ArrayUtils;

import com.wanda.ccs.model.AbstractStateEntity;
import com.wanda.ccs.model.State;
import com.wanda.ccs.service.IApprovableService;
import com.xcesys.extras.core.dao.model.ApprovableEntity;
import com.xcesys.extras.core.exception.ApplicationException;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.xcesys.extras.core.util.DateUtil;
import com.xcesys.extras.util.SecurityUtil;

public abstract class BaseApprovableServiceImpl<T extends ApprovableEntity>
		extends BaseCrudServiceImpl<T> implements IApprovableService<T> {

	@Override
	public void approve(Long id, String okStatus, String[] allowsStatus) {
		T o = findById(id);
		if (o.getApproved() != null && o.getApproved().booleanValue()) {
			log.info("数据" + o + "已经被其他用户执行了审核通过操作。");
			throw new ApplicationException("数据已经被其他用户执行了审核通过操作。");
		}
		String status = o.getStatus();
		if (allowsStatus != null
				&& (status == null || !ArrayUtils
						.contains(allowsStatus, status))) {
			throw new ApplicationException("数据状态不满足可执行审核的前提。");
		}
		o.setStatus(okStatus);
		o.setApproved(Boolean.TRUE);
		o.setApprovedTime(DateUtil.getCurrentDate());
		o.setApprovedBy(SecurityUtil.getLoginUser());
		super.update(o);
	}

	public void cancelSubmit(Long id, String cancelStatus, String[] allowsStatus) {
		T o = findById(id);
		if (o.getSubmit() == null || !o.getSubmit().booleanValue()) {
			log.info("数据" + o + "已经被其他用户执行了提交取回操作。");
			throw new ApplicationException("数据已经被其他用户执行了提交取回操作。");
		}
		String status = o.getStatus();
		if (allowsStatus != null
				&& (status == null || !ArrayUtils
						.contains(allowsStatus, status))) {
			throw new ApplicationException("数据处于不能执行提交取回状态:" + status);
		}
		o.setStatus(cancelStatus);
		o.setSubmit(Boolean.FALSE);
		o.setSubmitTime(null);
		o.setSubmitBy(null);
		super.update(o);
	}

	@Override
	public void submit(Long id, String okStatus, String[] allowsStatus) {
		T o = findById(id);
		if (o.getSubmit() != null && o.getSubmit().booleanValue()) {
			log.info("数据" + o + "已经被其他用户执行了提交审核操作。");
			throw new ApplicationException("数据已经被其他用户执行了提交审核操作。");
		}
		String status = o.getStatus();
		if (allowsStatus != null
				&& (status == null || !ArrayUtils
						.contains(allowsStatus, status))) {
			throw new ApplicationException("数据状态不满足可提交审核的前提。");
		}
		o.setStatus(okStatus);
		o.setSubmit(Boolean.TRUE);
		o.setSubmitTime(DateUtil.getCurrentDate());
		o.setSubmitBy(SecurityUtil.getLoginUser());
		super.update(o);
	}

	public void submit(Long id) {
		T o = findById(id);
		if (o instanceof AbstractStateEntity) {
			((AbstractStateEntity) o).setState(((AbstractStateEntity) o)
					.getState().desire(State.SUBMIT));
			o.setSubmit(Boolean.TRUE);
			o.setSubmitTime(DateUtil.getCurrentDate());
			o.setSubmitBy(SecurityUtil.getLoginUser());
		}
	}
}
