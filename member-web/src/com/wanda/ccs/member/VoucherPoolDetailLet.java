package com.wanda.ccs.member;

import org.springframework.beans.factory.annotation.Autowired;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TMemberVoucherPoolDetailService;
import com.wanda.ccs.model.IDimType;
import com.wanda.ccs.model.TVoucherPoolDetail;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.SpringContextUtil;

/**
 * 券库明细管理
 * @author admin
 *
 */
public class VoucherPoolDetailLet extends BaseCrudWinlet<TVoucherPoolDetail>
		implements IDimType {

	private static final long serialVersionUID = 2566904688581079984L;
	public boolean hide = false;

	public Long memberId;
	public String invoked;
	public Long voucherPoolId;
	
	public boolean showVouher = false;
	
	private TVoucherPoolLet tVoucherPoolLet;
	public TVoucherPoolLet gettVoucherPoolLet(IModuleRequest req) throws Exception {
		if(tVoucherPoolLet == null) {
			tVoucherPoolLet = (TVoucherPoolLet) req.getWinlet(TVoucherPoolLet.class.getName());
		}
		return tVoucherPoolLet;
	}
	
	public Long getVoucherPoolId() {
		return voucherPoolId;
	}


	public void setVoucherPoolId(Long voucherPoolId) {
		this.voucherPoolId = voucherPoolId;
		this.query.put("voucherPoolId", voucherPoolId);
	}
	
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
		this.query.put("memberId", memberId);
	}

	public String getInvoked() {
		return invoked;
	}

	public void setInvoked(String invoked) {
		this.invoked = invoked;
		this.showVouher = true;
	}

	@Autowired
	private TMemberVoucherPoolDetailService service;

	public VoucherPoolDetailLet() {

	}

	protected TVoucherPoolDetail newModel() {
		return new TVoucherPoolDetail();
	}

	protected ICrudService<TVoucherPoolDetail> getCrudService() {
		if (service == null) {
			service = SpringContextUtil
					.getBean(TMemberVoucherPoolDetailService.class);
		}
		return service;
	}

	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (query.get("memberId") != null && showVouher) {
			return super.showList(req, resp);
		} else
			return RETCODE_HIDE;
	}

	public int showTicketList(IModuleRequest req, IModuleResponse resp) {
		PageResult<TVoucherPoolDetail> pageResult = SpringContextUtil.getBean(TMemberVoucherPoolDetailService.class).findByCriteria(query, null);
		if(hide){
			return RETCODE_HIDE;
		}
		// New Displaytag使用的结果集，并将其放到Request中，使JSP可引用。
		req.setAttribute(NAME_OF_PAGE_RESULT, buildResult(pageResult));
		return RETCODE_OK;
	}

	public TVoucherPoolLet gettVoucherPoolLet() {
		return tVoucherPoolLet;
	}


	public void settVoucherPoolLet(TVoucherPoolLet tVoucherPoolLet) {
		this.tVoucherPoolLet = tVoucherPoolLet;
	}

	public TMemberVoucherPoolDetailService getService() {
		return service;
	}

	public void setService(TMemberVoucherPoolDetailService service) {
		this.service = service;
	}

	@Override
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		hide = true;
		return super.cancelEdit(req, resp);
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}
}
