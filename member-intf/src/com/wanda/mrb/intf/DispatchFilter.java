package com.wanda.mrb.intf;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.wanda.mrb.intf.member.BindVoucher;
import com.wanda.mrb.intf.member.CheckMember;
import com.wanda.mrb.intf.member.GetPointHistory;
import com.wanda.mrb.intf.member.GetTransHistory;
import com.wanda.mrb.intf.member.IssueMemberCard;
import com.wanda.mrb.intf.member.MemberJoinCard;
import com.wanda.mrb.intf.member.PassCheckCode;
import com.wanda.mrb.intf.member.PointsBalance;
import com.wanda.mrb.intf.member.QueryCarList;
import com.wanda.mrb.intf.member.QueryVoucherList;
import com.wanda.mrb.intf.member.RedeemOnline;
import com.wanda.mrb.intf.member.RedeemResult;
import com.wanda.mrb.intf.member.Register;
import com.wanda.mrb.intf.member.Reward;
import com.wanda.mrb.intf.member.SendCheckCode;
import com.wanda.mrb.intf.member.UpdateMember;
import com.wanda.mrb.intf.member.WebTransOrder;
import com.wanda.mrb.intf.utils.FormatTools;


/**
 * 请求分发
 */
public class DispatchFilter implements Filter {
	protected final org.apache.log4j.Logger log = Logger.getLogger(this.getClass());
	public static final String RESP_CONT_TYPE = "text/xml;charset=UTF-8";
	
	public static final String REGISTER = "/member/register";
	public static final String UPDATE_MEMEBER = "/member/updatemember";
	public static final String CHECK_MEMEBER = "/member/checkmember";
	public static final String GET_TRANS_HISTORY = "/member/gettranshistory";
	public static final String GET_POINT_HISTORY = "/member/getpointhistory";
	public static final String GET_POINTS_BALANCE = "/member/getpointsbalance";
	public static final String REDEEM_MONLINE = "/member/redeemonline";
	public static final String REDEEM_RESULT = "/member/redeemresult";
	public static final String BIND_VOUCHER = "/member/bindvoucher";
	public static final String QUERY_VOUCHER_LIST = "/member/queryvoucherlist";
	public static final String QUERY_CARD_LIST = "/member/querycardlist";
	public static final String ISSUE_MEMBER_CARD = "/member/issuemembercard";
	public static final String SEND_CHECK_CODE = "/member/sendcheckcode";
	public static final String PASS_CHECK_CODE = "/member/passcheckcode";
	public static final String MEMBER_JOIN_CARD = "/member/memberjoincard";
	public static final String WEB_TRANS_ORDER = "/member/webtransorder";
	public static final String REWARD = "/member/reward";
	
    public DispatchFilter() {
	}
	
	public void init(FilterConfig fConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURI().substring(req.getContextPath().length());
		HttpServletResponse rep = (HttpServletResponse) response;

		if (log.isDebugEnabled()) {
			log.debug("url => {}" + url);
		}

		StringBuffer xmlResponse = null;
		ServiceBase action = null;
		if (url.startsWith(REGISTER)) {
			action = new Register();
		} else if (url.startsWith(UPDATE_MEMEBER)) {
			action = new UpdateMember();
		} else if (url.startsWith(CHECK_MEMEBER)) {
			action = new CheckMember();
		}else if (url.startsWith(GET_TRANS_HISTORY)) {
			action = new GetTransHistory();
		}else if (url.startsWith(GET_POINTS_BALANCE)) {
			action=new PointsBalance();
		}else if (url.startsWith(REDEEM_MONLINE)) {
			action=new RedeemOnline();
		}else if (url.startsWith(BIND_VOUCHER)) {
			action=new BindVoucher();
		}else if (url.startsWith(GET_POINT_HISTORY)) {
			action=new GetPointHistory();
		}else if (url.startsWith(QUERY_VOUCHER_LIST)) {
			action=new QueryVoucherList();
		}else if (url.startsWith(QUERY_CARD_LIST)) {
			action=new QueryCarList();
		}else if (url.startsWith(REDEEM_MONLINE)) {
			action=new RedeemOnline();
		}else if (url.startsWith(REDEEM_RESULT)) {
			action=new RedeemResult();
		}else if (url.startsWith(ISSUE_MEMBER_CARD)) {
			action=new IssueMemberCard();
		}else if (url.startsWith(SEND_CHECK_CODE)) {
			action=new SendCheckCode();
		}else if (url.startsWith(PASS_CHECK_CODE)) {
			action=new PassCheckCode();
		}else if (url.startsWith(MEMBER_JOIN_CARD)) {
			action=new MemberJoinCard();
		}else if (url.startsWith(WEB_TRANS_ORDER)) {
			action=new WebTransOrder();
		}else if (url.startsWith(REWARD)) {
			action=new Reward();
		}else if (url.equals("/")) {
			request.getRequestDispatcher("/a.html").forward(request, response);                    
			return;
		}
//		
		xmlResponse = action.defaultProc(req, rep);
		action = null;
		response.setContentType(RESP_CONT_TYPE);
		if (xmlResponse != null) {
			response.getWriter().print(FormatTools.setTagEmpey(xmlResponse.toString()));                  //处理为NULL情况
		}
		xmlResponse = null;
		url = null;
		return;
	}
	
	public void destroy() {
		log.debug("开始销毁初始化过滤器");
	}
}
