package com.wanda.ccs.member;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.displaytag.properties.SortOrderEnum;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.form.Input;
import com.aggrepoint.adk.form.Validate;
import com.aggrepoint.adk.form.Validates;
import com.aggrepoint.adk.ui.ValidateResult;
import com.aggrepoint.adk.ui.ValidateResultType;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.basemgt.service.TDimManService;
import com.wanda.ccs.basemgt.service.TDimTypeDefService;
import com.wanda.ccs.core.BaseCrudWinlet;
import com.wanda.ccs.mem.service.TMemGoodsTransOrderService;
import com.wanda.ccs.mem.service.TMemTicketTransOrderService;
import com.wanda.ccs.mem.service.TMemberPointService;
import com.wanda.ccs.mem.service.TMemberService;
import com.wanda.ccs.model.IMemberDimType;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TDimDef;
import com.wanda.ccs.model.TGoodsTransOrder;
import com.wanda.ccs.model.TMember;
import com.wanda.ccs.model.TMemberAddr;
import com.wanda.ccs.model.TTicketTransOrder;
import com.wanda.ccs.service.SpringCommonService;
import com.wanda.mms.control.stream.service.IntegralInitialization;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.util.ConvertUtil;
import com.xcesys.extras.util.SpringContextUtil;
import com.xcesys.extras.webapp.model.DisplayTagPageResult;

public class MemberLet extends BaseCrudWinlet<TMember> implements
		IMemberDimType {
	private static final long serialVersionUID = -2178964987589833101L;

	private TMemberService service;

	public TMember tModel;
	public TMemberAddr tMemberAddr;

	public Long resetFlag;
	public String transType = "T";

	public boolean showTrans = false;
	public boolean showOptions = false;
	boolean isEditMem = false;
	boolean newCreateMen=false;
	List<TDimDef> facFilmTypeList;
	List<TDimDef> facContancList;
	String[] filmtypes;
	String[] contancs;
	public Long memberId;
	public Map<String, String> cinemaMaps;
	public boolean isChangeStatus = false;
	String str;

	public MemberPointHistoryLet memPointHistoryLet = new MemberPointHistoryLet();
	
	private Map<String, Map<String, String>> dims;

	MemberPointHistoryLet getMemberPointHistoryLet(IModuleRequest req)
			throws Exception {
		if (memPointHistoryLet == null) {
			memPointHistoryLet = (MemberPointHistoryLet) req
					.getWinlet(MemberPointHistoryLet.class.getName());
		}
		return memPointHistoryLet;
	}

	public MemberLevelHistoryLet memLevelHistoryLet = new MemberLevelHistoryLet();
	
	MemberExgPointLet getMemberExgPointLet(IModuleRequest req)
			throws Exception {
		if (memberExgPointLet == null) {
			memberExgPointLet = (MemberExgPointLet) req
					.getWinlet(MemberExgPointLet.class.getName());
		}
		return memberExgPointLet;
	}

	public MemberExgPointLet memberExgPointLet = new MemberExgPointLet();

	MemberLevelHistoryLet getMemberLevelHistoryLet(IModuleRequest req)
			throws Exception {
		if (memLevelHistoryLet == null) {
			memLevelHistoryLet = (MemberLevelHistoryLet) req
					.getWinlet(MemberLevelHistoryLet.class.getName());
		}
		return memLevelHistoryLet;
	}

	public VoucherPoolDetailLet voucherPoolDetailLet = new VoucherPoolDetailLet();

	VoucherPoolDetailLet getVoucherPoolDetailLet(IModuleRequest req)
			throws Exception {
		if (voucherPoolDetailLet == null) {
			voucherPoolDetailLet = (VoucherPoolDetailLet) req
					.getWinlet(VoucherPoolDetailLet.class.getName());
		}
		return voucherPoolDetailLet;
	}

	public MemCardRelLet memCardRelLet = new MemCardRelLet();

	MemCardRelLet getMemCardRelLet(IModuleRequest req) throws Exception {
		if (memCardRelLet == null) {
			memCardRelLet = (MemCardRelLet) req.getWinlet(MemCardRelLet.class
					.getName());
		}
		return memCardRelLet;
	}
	
	public MemberLogLet memberLogLet = new MemberLogLet();

	MemberLogLet getMemberLogLet(IModuleRequest req)
			throws Exception {
		if (memberLogLet == null) {
			memberLogLet = (MemberLogLet) req
					.getWinlet(MemberLogLet.class.getName());
		}
		return memberLogLet;
	}

	public TMember gettModel() {
		return tModel;
	}

	public Long getId() {
		return id;
	}

	public void settModel(TMember tModel) {
		this.tModel = tModel;
	}

	public TMemberAddr gettMemberAddr() {
		return tMemberAddr;
	}

	public void settMemberAddr(TMemberAddr tMemberAddr) {
		this.tMemberAddr = tMemberAddr;
	}

	/**
	 * 影院
	 */
	private List<TCinema> cinemaMap;
	
	private List<TCinema> allCinemaMap;

	private Map<Long, String> provinceMap;

	private Map<Long, String> cityMap;

	/**
	 * 默认无参构造函数。
	 */
	public MemberLet() {

	}

	@Override
	protected ICrudService<TMember> getCrudService() {
		if (service == null) {
			service = SpringContextUtil.getBean(TMemberService.class);
		}
		return service;
	}

	protected TMember newModel() {
		return new TMember();
	}

	@Override
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		initMaps(req);
		if (query.get("memberstatus") == null)
			query.put("memberstatus", null);
		
		if (query.get("memberisdeleted") == null)
			query.put("memberisdeleted", null);
		
		super.initSearch(req, resp);
	}

	private void initMaps(IModuleRequest req) {
		CcsUserProfile user = getUser(req);
		//登陆用户是影城级别的用户
		if(cinemaMap == null || cinemaMap.isEmpty()){
			if("CINEMA".equals(user.getLevelName())){
				TCinema tCinema = getService(TCinemaService.class).findById(user.getCinemaId());
				cinemaMap = new ArrayList<TCinema>();
				cinemaMap.add(tCinema);
			}else if("REGION".equals(user.getLevelName())){
				cinemaMap = SpringCommonService.getUnDeletedCinemasOrderByName(user.getRegionCode());
			}else{
				cinemaMap = SpringCommonService.getUnDeletedCinemasOrderByName(null);
			}
		}
		if(allCinemaMap == null || allCinemaMap.isEmpty()){
			allCinemaMap = SpringCommonService.getUnDeletedCinemasOrderByName(null);
			allCinemaMap.add(getService(TCinemaService.class).findById(0L));
		}
		if(provinceMap == null || provinceMap.isEmpty())
			provinceMap = SpringCommonService.getProvince();
		if(cityMap == null || cityMap.isEmpty())
			cityMap = SpringCommonService.getCity();
		if(facFilmTypeList == null || facFilmTypeList.isEmpty())
			facFilmTypeList = getService(TDimManService.class).findByTypeId(LDIMTYPE_FILM_TYPE);
		if(facContancList == null || facContancList.isEmpty())
			facContancList = getService(TDimManService.class).findByTypeId(LIMTYPE_MEMBER_FACCONTACT);
		req.setAttribute("cinemaMap", cinemaMap);
		req.setAttribute("provinceMap", provinceMap);
		req.setAttribute("cityMap", cityMap);
		req.setAttribute("allCinemaMap", allCinemaMap);

	}

	@Override
	protected void prepareQuery(IModuleRequest req) {
		super.prepareQuery(req);
		query.put("membermobile", req.getParameter("mobile", null));
		query.put("tMackDaddyCardNo", req.getParameter("tMackDaddyCardNo", null));
		query.put("name", req.getParameter("name", null));
		query.put("email", req.getParameter("email", null));
		if (req.getParameter("idCardType", null) != null
				&& !"".equals(req.getParameter("idCardType", null))) {
			query.put("idCardType",
					Integer.parseInt(req.getParameter("idCardType", null)));
		} else {
			query.put("idCardType", req.getParameter("idCardType", null));
		}
		if (req.getParameter("manageCinema", null) != null
				&& !"".equals(req.getParameter("manageCinema", null))) {
			query.put("manageCinema",
					Long.parseLong(req.getParameter("manageCinema", null)));
		} else {
			query.put("manageCinema", req.getParameter("manageCinema", null));
		}
		if (req.getParameter("registCinemaId", null) != null
				&& !"".equals(req.getParameter("registCinemaId", null))) {
			query.put("registCinemaId",
					Long.parseLong(req.getParameter("registCinemaId", null)));
		} else {
			query.put("registCinemaId", req.getParameter("registCinemaId", null));
		}
		query.put("idCardNo", req.getParameter("idCardNo", null));
		
		if( req.getParameter("minExgPointBalance", null) != null){
			try{
				Long.parseLong((String)req.getParameter("minExgPointBalance", null).toString());
				query.put("minExgPointBalance", req.getParameter("minExgPointBalance", null));
			}catch (Exception e) {
				query.put("minExgPointBalance", null);
			}
		}
		if( req.getParameter("maxExgPointBalance", null) != null){
			try{
				Long.parseLong((String)req.getParameter("maxExgPointBalance", null).toString());
				query.put("maxExgPointBalance", req.getParameter("maxExgPointBalance", null));
			}catch (Exception e) {
				query.put("maxExgPointBalance", null);
			}
		}
		
		if(req.getParameter("query_start", null) != null && !"".equals(req.getParameter("query_start", null))){
			String queryStartStr = req.getParameter("query_start", null);
			query.put("query_start", queryStartStr + " 06:00:00");
		}else{
			query.put("query_start", req.getParameter("query_start", null));
		}
		
		if(req.getParameter("query_end", null) != null && !"".equals(req.getParameter("query_end", null))){
			String queryEndStr = req.getParameter("query_end", null);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String endDate = null;
			try {
				Date end = df.parse(queryEndStr);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(end);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				endDate = df.format(calendar.getTime());
			} catch (ParseException e) {
				endDate = req.getParameter("query_end", null);
			}
			query.put("query_end", endDate + " 06:00:00");
		}else{
			query.put("query_end", req.getParameter("query_end", null));
		}
	
		query.put("channelId", req.getParameter("channelId", null));
		//query.put("memberstatus", req.getParameter("memberstatus", 1L));
		//query.put("memberisdeleted", req.getParameter("memberisdeleted", 0L));
	}

	@Override
	public int showSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		return super.showSearch(req, resp);
	}

	@Override
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		filmtypes = null;
		contancs = null;
		tModel = null;
		query.put("transByMemberId", null);
		return super.cancelEdit(req, resp);
	}

	@Override
	public int doCreate(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		tModel = new TMember();
		newCreateMen = true;
		tModel.setRegistOpName(getUser(req).getName());
		tModel.setRegistOpNo(getUser(req).getId());
		showTrans = false;
		showOptions = false;
		isChangeStatus = false;
		resetFlag = new Date().getTime();
		if (getMemberPointHistoryLet(req) != null) {
			getMemberPointHistoryLet(req).setMemberId(null);
		}
		if (getMemberLevelHistoryLet(req) != null) {
			getMemberLevelHistoryLet(req).setMemberId(null);
		}
		if (getVoucherPoolDetailLet(req) != null) {
			getVoucherPoolDetailLet(req).setMemberId(null);
		}
		if (getMemCardRelLet(req) != null) {
			getMemCardRelLet(req).setMemberId(null);
		}
		
		if (getMemberLogLet(req) != null) {
			getMemberLogLet(req).setMemberId(null);
		}

		return super.doCreate(req, resp);
	}

	@Override
	public int doDelete(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		securityContext(req);
		id = req.getParameter("id", 0l);
		if (id != 0) {
			log.debug("#### 执行删除方法:doDelete，删除对象ID[" + id + "] ####");
			ids = new Long[] { id };
		} else {
			String[] oids = req.getParameterValues("ids");
			ids = ConvertUtil.convertStringArrayToLongArray(oids);
			log.debug("#### 执行删除方法:doDelete，删除对象ID[" + ids + "] ####");
		}
		if (ids != null && ids.length > 0) {
			if (service != null) {
				service.deleteTMembers(ids);
			} else {
				getService(TMemberService.class).deleteTMembers(ids);
			}
		}
		id = null;
		ids = null;
		editing = false;
		isChangeStatus = false;
		showOptions = false;
		return RETCODE_OK;
	}

	/**
	 * 修改会员状态
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int doChangeStatus(IModuleRequest req, IModuleResponse resp)
			throws Exception {

		// securityContext(req);
		securityContext(req);
		str = req.getParameter("str", "");
		isChangeStatus = true;
		showOptions = false;
		return RETCODE_OK;
	}

	/**
	 * 修改会员状态视图
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int showChangeView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if(isChangeStatus){
			return RETCODE_OK;
		}else{
			return RETCODE_HIDE;
		}
	}

	/**
	 * 保存
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int saveChangeStatus(IModuleRequest req, IModuleResponse resp)
			throws Exception {

		Long memStatus = req.getParameter("status", 0l);
		String changeResion = req.getParameter("changeResion", "");
		getService(TMemberService.class).updateMemberStatus(str, req, memStatus, changeResion);
		str = null;
		isChangeStatus = false;
		return RETCODE_OK;
	}

	/**
	 * 取消修改会员状态编辑
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int cancelStatusEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		isChangeStatus = false;
		str = null;
		return RETCODE_OK;
	}

	@Override
	public int doEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		tModel = null;
		showOptions = false;
		isEditMem = true;
		newCreateMen = false;
		isChangeStatus = false;
		resetFlag = new Date().getTime();
		if (getMemberPointHistoryLet(req) != null) {
			getMemberPointHistoryLet(req).setMemberId(null);
		}
		if (getMemberLevelHistoryLet(req) != null) {
			getMemberLevelHistoryLet(req).setMemberId(null);
		}
		if (getVoucherPoolDetailLet(req) != null) {
			getVoucherPoolDetailLet(req).setMemberId(null);
		}
		if (getMemCardRelLet(req) != null) {
			getMemCardRelLet(req).setMemberId(null);
		}
		
		if (getMemberLogLet(req) != null) {
			getMemberLogLet(req).setMemberId(null);
		}
		showTrans = false;
		return super.doEdit(req, resp);
	}

	@Override
	public int doSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (getMemberPointHistoryLet(req) != null) {
			getMemberPointHistoryLet(req).setMemberId(null);
		}
		if (getMemberLevelHistoryLet(req) != null) {
			getMemberLevelHistoryLet(req).setMemberId(null);
		}
		if (getVoucherPoolDetailLet(req) != null) {
			getVoucherPoolDetailLet(req).setMemberId(null);
		}
		if (getMemCardRelLet(req) != null) {
			getMemCardRelLet(req).setMemberId(null);
		}
		
		if (getMemberLogLet(req) != null) {
			getMemberLogLet(req).setMemberId(null);
		}
		showTrans = false;
		showOptions = false;
		isChangeStatus = false;
		query.put("pagemember", req.getParameter("pagemember", 1));
		query.put("transByMemberId", null);
		return super.doSearch(req, resp);
	}

	@Override
	public int doView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (getMemberPointHistoryLet(req) != null) {
			getMemberPointHistoryLet(req).setMemberId(null);
		}
		if (getMemberLevelHistoryLet(req) != null) {
			getMemberLevelHistoryLet(req).setMemberId(null);
		}
		if (getVoucherPoolDetailLet(req) != null) {
			getVoucherPoolDetailLet(req).setMemberId(null);
		}
		if (getMemCardRelLet(req) != null) {
			getMemCardRelLet(req).setMemberId(null);
		}
		
		if (getMemberLogLet(req) != null) {
			getMemberLogLet(req).setMemberId(null);
		}
		
		//Added by Charlie Zhang(2014-05-04). 
		//Hide the MemberExgPoint view, after the member has been switched in the list.
		if(getMemberExgPointLet(req) != null) {
			getMemberExgPointLet(req).setMemberId(null);
		}
		
		showTrans = false;
		showOptions = true;
		isEditMem = false;
		newCreateMen = false;
		isChangeStatus =false;
		return super.doView(req, resp);
	}

	@Override
	public int saveEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (req.isValidateField()) {
			if (req.getForm().getValidateField() == null) {
				return 10;
			}
			return req.getForm().getValidateField().isHasError() ? 11 : 10;
		}
		if (req.getForm().hasError())
			return 2;
		TMember model = null;
		if (tModel != null) {
			model = tModel;
		} else {
			if (id != null) {
				model = super.findById(id);
			}
		}
		// 从request中把数据populate到编辑对象中
		req.setAttribute(NAME_OF_EDITING_OBJECT, model);
		// 保存前执行数据验证。
		if("".equals(model.getGender())){
			model.setGender("O");
		}
		validation(req, resp, model);
		// 初始化安全环境上下文。
		securityContext(req);
		// 执行保存对象操作。
		if (model != null) {
			preSave(model);
			service.createOrUpdate(model);
			if (getService(TMemberPointService.class).getTMemberPointByMemId(model.getId()) == null) {
				IntegralInitialization integral = new IntegralInitialization();
				integral.addMemberPointByID(service.getUniversalDao().getConnection(), model.getId(), model.getCreatedBy(), 0l);
			}
			query.put("membermobile", model.getMobile());
			// 成功保存后清空变量
			editing = false;
			tModel = null;
			id = null;
			contancs = null;
			showOptions = false;
			filmtypes = null;
			log.debug("#### 执行数据保存方法:saveEdit，保存对象[" + model + "] ####");
		}
		return RETCODE_OK;
	}

	/**
	 * 按照会员编码生成规则生成会员编码 1位 ：C 2-4：注册影城内码 5-20：连续数字，0补空位
	 */
	protected void preSave(TMember model) {
		if (model.getMemberNo() == null) {
			// DecimalFormat format = new DecimalFormat("0000000000000000");
			String targetTopNum = getService(TMemberService.class)
					.getMemSeqForMemNum();
			// Integer num = 0;
			// if (targetTopNum != null && !"null".equals(targetTopNum)) {
			// num = Integer.parseInt(targetTopNum) + 1;
			//
			// } else {
			// num = 1;
			// }
			TCinema tcinema = getService(TCinemaService.class).findById(
					model.getRegistCinemaId());
			model.setMemberNo("C" + tcinema.getInnerCode()
					+ String.format("%016d", Integer.parseInt(targetTopNum)));
			model.gettMemberInfo().setManageCinema(model.getRegistCinemaId());
		}
		super.preSave(model);
	}

	@Override
	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		initMaps(req);
		TDimDef dim = new TDimDef();
		dim.setTypeId(211L);

		int retNo = super.showEdit(req, resp);
		if (retNo == RETCODE_OK) {
			if (id != null && id != 0) {
				// 编辑或查看的场景
				try {
					// 编辑情况
					tModel = findById(id);
				} catch (Exception e) {
					resp.setUserMessage(e.getLocalizedMessage());
					return RETCODE_ERR;
				}
			}
			if (tModel == null) {
				log.debug("#### 隐藏Edit窗口 ####");
				return RETCODE_HIDE;
			}
			
			if (tModel != null) {
				tModel.setEditing(editing && canEdit(tModel));
			}
			
			if(tModel.gettMemFavFilmtypes()!=null){
				if (tModel.gettMemFavFilmtypes().getFilmTypes() != null) {
					String types = tModel.gettMemFavFilmtypes().getFilmTypes();
					filmtypes = types.split(",");
				}
			}
			
			if(tModel.gettMemFavContacts()!=null){
				if (tModel.gettMemFavContacts().getContactMeans() != null) {
					String contactMeans = tModel.gettMemFavContacts()
							.getContactMeans();
					contancs = contactMeans.split(",");
				}
			}
			
			req.setAttribute("EDITMEMBER", isEditMem);
			req.setAttribute("CREATEMEMBER", newCreateMen);
			req.setAttribute("USERS", getUser(req).getName());
			req.setAttribute(NAME_OF_EDITING_OBJECT, tModel);
		}
		if(dims == null || dims.isEmpty()){
			dims = getService(TDimTypeDefService.class).getAllDimDefsOrderByCode();
		}
		req.setAttribute("dims", dims);
		return retNo;
	}

	public int showOptions(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		initMaps(req);
		TDimDef dim = new TDimDef();
		dim.setTypeId(211L);

		if (id != null && id != 0) {
			// 编辑或查看的场景
			try {
				// 编辑情况
				tModel = findById(id);
			} catch (Exception e) {
				resp.setUserMessage(e.getLocalizedMessage());
				return RETCODE_ERR;
			}
		}
		if (!showOptions || tModel == null) {
			log.debug("#### 隐藏Edit窗口 ####");
			return RETCODE_HIDE;
		}
		if (tModel.gettMemFavFilmtypes() != null) {
			if (tModel.gettMemFavFilmtypes().getFilmTypes() != null) {
				String types = tModel.gettMemFavFilmtypes().getFilmTypes();
				filmtypes = types.split(",");
			}

		}
		if (tModel.gettMemFavContacts() != null) {
			if (tModel.gettMemFavContacts().getContactMeans() != null) {
				String contactMeans = tModel.gettMemFavContacts()
						.getContactMeans();
				contancs = contactMeans.split(",");
			}

		}
		req.setAttribute("EDITMEMBER", isEditMem);
		req.setAttribute("USERS", getUser(req).getId());
		req.setAttribute("MEMBERMODEL", tModel);
		return 0;
	}

	/**
	 * 编辑页面Ajax输入验证。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@Validates({
			@Validate(name = "email", id = "regexp", args = { "(^[\\S]{0,0}$)|(^[\\S]*@[\\S]*.[\\S]*$)" }, error = "请输入正确的email."),
			@Validate(name = "mobile", id = "regexp", args = { "(^[\\d]{0,0}$)|(^[1][3-8]+\\d{9})" }, error = "请输入正确的手机号."),
			@Validate(name = "mobile", id = "tne", error = "会员手机号不能为空。"),
			@Validate(name = "registCinemaId", id = "tne", error = "注册影城不能为空。"),
			@Validate(name = "mgHistoryId", id = "tne", error = "管理影城不能为空。"),
			@Validate(name = "provinceId", id = "tne", error = "所属省不能为空。"),
			@Validate(name = "channelId", id = "tne", error = "招募渠道不能为空。") })
	public ValidateResult validate(IModuleRequest req, Input input) {
		if (id == null) {
			if ("mobile".equals(input.getName())) {
				if (service.checkMobile(input.getValue().toString(), 1L)) {
					return new ValidateResult(
							ValidateResultType.FAILED_SKIP_PROPERTY,
							"此手机号系统中已经存在，请重新输入.");
				}
			}
		} else {
			if ("mobile".equals(input.getName())) {
				String memberNo = service.checkMobile(input.getValue().toString());
				if(memberNo != null){
					if (!memberNo.equals(tModel.getMemberNo())) {
						return new ValidateResult(
								ValidateResultType.FAILED_SKIP_PROPERTY,
								"此手机号系统中已经存在，请重新输入.");
					}
				}
			}
		}
		return ValidateResult.PASS;
	}

	@Override
	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		initMaps(req);
		CcsUserProfile user = getUser(req);
		//登陆用户是影城级别的用户  暂时解除此限制  在service中已注释，如需要，可打开注释
		if("CINEMA".equals(user.getLevelName())){
			Long[] cinemaIds = new Long[] { user.getCinemaId() };
			query.put("cinemaIds", cinemaIds);
		}else if("REGION".equals(user.getLevelName())){
			List<TCinema> tCinemaList = SpringCommonService.getCinemasByArea(user.getRegionCode());
			Long[] cinemaIds = new Long[tCinemaList.size()];
			for(int i=0;i<tCinemaList.size();i++){
				cinemaIds[i] = tCinemaList.get(i).getId();
			}
			query.put("cinemaIds", cinemaIds);
		}else{
			query.put("cinemaIds", null);
		}
		if((query.get("membermobile")!=null && !"".equals(query.get("membermobile"))) || (query.get("tMackDaddyCardNo")!=null && !"".equals(query.get("tMackDaddyCardNo"))) || (query.get("name")!=null && !"".equals(query.get("name"))) 
				|| (query.get("email")!=null && !"".equals(query.get("email"))) || (query.get("registCinemaId")!=null && !"".equals(query.get("registCinemaId")))||(query.get("minExgPointBalance")!=null && !"".equals(query.get("minExgPointBalance")))
				||(query.get("maxExgPointBalance")!=null && !"".equals(query.get("maxExgPointBalance")))|| (query.get("query_start")!=null && !"".equals(query.get("query_start"))) ||(query.get("query_end")!=null && !"".equals(query.get("query_end")))
				||(query.get("channelId")!=null && !"".equals(query.get("channelId"))) ||(query.get("memberstatus")!=null) ||(query.get("memberisdeleted")!=null 
						|| (query.get("manageCinema")!=null && !"".equals(query.get("manageCinema"))))){
			query.put("hasResult", "T");
		}else{
			query.put("hasResult", "F");
		}
		return super.showList(req, resp);
	}

	// 是否选中会员喜欢的联系方式
	public boolean getContactMeans(String contactMeans) {
		if (contancs != null) {
			for (String conid : contancs) {
				if (conid.equals(contactMeans)) {
					return true;
				}
			}
		}
		return false;
	}

	// 编辑是否选中会员喜欢的联系方式
	public void setContactMeans(String contactMeans, boolean b) {
		if (tModel == null)
			return;

		if (b) {
			if (tModel.gettMemFavContacts().getContactMeans() == null
					|| "".equals(tModel.gettMemFavContacts().getContactMeans())) {
				tModel.gettMemFavContacts().setContactMeans(contactMeans);
			} else
				tModel.gettMemFavContacts().setContactMeans(
						tModel.gettMemFavContacts().getContactMeans() + ","
								+ contactMeans);
		} else {
			List<String> list = Arrays.asList(tModel.gettMemFavContacts()
					.getContactMeans().split(","));
			List<TDimDef> fagDim = new ArrayList<TDimDef>();
			for (TDimDef dim : facContancList) {
				if (dim.getCode().equals(contactMeans)) {
					fagDim.add(dim);
				}
				if (!dim.getCode().equals(contactMeans)
						&& !list.contains(dim.getCode())) {
					fagDim.add(dim);
				}
			}
			facContancList.removeAll(fagDim);
			StringBuffer stMFC = new StringBuffer();
			for (int i = 0; i < facContancList.size(); i++) {
				stMFC.append(facContancList.get(i).getCode());
				if (i < (facContancList.size() - 1)) {
					stMFC.append(",");
				}
			}
			tModel.gettMemFavContacts().setContactMeans(stMFC.toString());
		}
	}

	// 是否选中会员喜欢的影片类型
	public boolean getFilmTypes(String filmType) {
		if (filmtypes != null) {
			for (String filmtypeid : filmtypes) {
				if (filmtypeid.equals(filmType)) {
					return true;
				}
			}
		}
		return false;
	}

	// 编辑是否选中会员喜欢的影片类型
	public void setFilmTypes(String filmType, boolean b) {
		if (tModel == null)
			return;

		if (b) {
			if (tModel.gettMemFavFilmtypes().getFilmTypes() == null
					|| "".equals(tModel.gettMemFavFilmtypes().getFilmTypes())) {
				tModel.gettMemFavFilmtypes().setFilmTypes(filmType);
			} else
				tModel.gettMemFavFilmtypes().setFilmTypes(
						tModel.gettMemFavFilmtypes().getFilmTypes() + ","
								+ filmType);
		} else {
			List<String> list = Arrays.asList(tModel.gettMemFavFilmtypes()
					.getFilmTypes().split(","));
			List<TDimDef> fagDim = new ArrayList<TDimDef>();
			for (TDimDef dim : facFilmTypeList) {
				if (dim.getCode().equals(filmType)) {
					fagDim.add(dim);
				}
				if (!dim.getCode().equals(filmType)
						&& !list.contains(dim.getCode())) {
					fagDim.add(dim);
				}
			}
			facFilmTypeList.removeAll(fagDim);
			StringBuffer stMFf = new StringBuffer();
			for (int i = 0; i < facFilmTypeList.size(); i++) {
				stMFf.append(facFilmTypeList.get(i).getCode());
				if (i < (facFilmTypeList.size() - 1)) {
					stMFf.append(",");
				}
			}
			tModel.gettMemFavFilmtypes().setFilmTypes(stMFf.toString());
		}
	}

	// 查询会员积分历史
	public int doSearchPoint(IModuleRequest req, IModuleResponse resp)
			throws Exception {

		memberId = req.getParameter("id", 0l);
		if (getMemberPointHistoryLet(req) != null) {
			getMemberPointHistoryLet(req).setMemberId(memberId);
			getMemberPointHistoryLet(req).setInvoked(
					MemberPointHistoryLet.class.getName());
		}
		if (getMemberLevelHistoryLet(req) != null) {
			getMemberLevelHistoryLet(req).setMemberId(null);
		}
		if (getMemberExgPointLet(req) != null) {
			getMemberExgPointLet(req).setMemberId(null);
		}	
		if (getVoucherPoolDetailLet(req) != null) {
			getVoucherPoolDetailLet(req).setMemberId(null);
		}
		if (getMemCardRelLet(req) != null) {
			getMemCardRelLet(req).setMemberId(null);
		}
		if (getMemberLogLet(req) != null) {
			getMemberLogLet(req).setMemberId(null);
		}
		showTrans = false;
		return RETCODE_OK;
	}

	// 查询会员等级历史
	public int doSearchLevel(IModuleRequest req, IModuleResponse resp)
			throws Exception {

		memberId = req.getParameter("id", 0l);
		if (getMemberPointHistoryLet(req) != null) {
			getMemberPointHistoryLet(req).setMemberId(null);
		}
		if (getMemberLevelHistoryLet(req) != null) {
			getMemberLevelHistoryLet(req).setMemberId(memberId);
			getMemberLevelHistoryLet(req).setInvoked(
					MemberLevelHistoryLet.class.getName());
		}
		if (getMemberExgPointLet(req) != null) {
			getMemberExgPointLet(req).setMemberId(null);
		}	
		if (getVoucherPoolDetailLet(req) != null) {
			getVoucherPoolDetailLet(req).setMemberId(null);
		}
		if (getMemCardRelLet(req) != null) {
			getMemCardRelLet(req).setMemberId(null);
		}
		
		if (getMemberLogLet(req) != null) {
			getMemberLogLet(req).setMemberId(null);
		}
		showTrans = false;
		return RETCODE_OK;
	}
	
	
	// 查询会员积分账户
	public int doSearchExgPoint(IModuleRequest req, IModuleResponse resp)
			throws Exception {

		memberId = req.getParameter("id", 0l);
		if (getMemberPointHistoryLet(req) != null) {
			getMemberPointHistoryLet(req).setMemberId(null);
		}
		if (getMemberLevelHistoryLet(req) != null) {
			getMemberLevelHistoryLet(req).setMemberId(null);
		}
		if (getMemberExgPointLet(req) != null) {
			getMemberExgPointLet(req).setMemberId(memberId);
			getMemberExgPointLet(req).setInvoked(
					MemberExgPointLet.class.getName());
		}
		if (getVoucherPoolDetailLet(req) != null) {
			getVoucherPoolDetailLet(req).setMemberId(null);
		}
		if (getMemCardRelLet(req) != null) {
			getMemCardRelLet(req).setMemberId(null);
		}
		
		if (getMemberLogLet(req) != null) {
			getMemberLogLet(req).setMemberId(null);
		}
		showTrans = false;
		return RETCODE_OK;
	}

	// 查询会员券包
	public int doSearchVoucher(IModuleRequest req, IModuleResponse resp)
			throws Exception {

		memberId = req.getParameter("id", 0l);
		if (getMemberPointHistoryLet(req) != null) {
			getMemberPointHistoryLet(req).setMemberId(null);
		}
		if (getMemberLevelHistoryLet(req) != null) {
			getMemberLevelHistoryLet(req).setMemberId(null);
		}
		if (getMemberExgPointLet(req) != null) {
			getMemberExgPointLet(req).setMemberId(null);
		}	
		if (getVoucherPoolDetailLet(req) != null) {
			getVoucherPoolDetailLet(req).setMemberId(memberId);
			getVoucherPoolDetailLet(req).setInvoked(
					VoucherPoolDetailLet.class.getName());
		}
		if (getMemCardRelLet(req) != null) {
			getMemCardRelLet(req).setMemberId(null);
		}
		
		if (getMemberLogLet(req) != null) {
			getMemberLogLet(req).setMemberId(null);
		}
		showTrans = false;
		return RETCODE_OK;
	}

	// 查询会员卡包
	public int doSearchCardRel(IModuleRequest req, IModuleResponse resp)
			throws Exception {

		memberId = req.getParameter("id", 0l);
		if (getMemberPointHistoryLet(req) != null) {
			getMemberPointHistoryLet(req).setMemberId(null);
		}
		if (getMemberLevelHistoryLet(req) != null) {
			getMemberLevelHistoryLet(req).setMemberId(null);
		}
		if (getMemberExgPointLet(req) != null) {
			getMemberExgPointLet(req).setMemberId(null);
		}	
		if (getVoucherPoolDetailLet(req) != null) {
			getVoucherPoolDetailLet(req).setMemberId(null);
		}
		if (getMemCardRelLet(req) != null) {
			getMemCardRelLet(req).setMemberId(memberId);
			getMemCardRelLet(req).setInvoked(MemCardRelLet.class.getName());
		}
		
		if (getMemberLogLet(req) != null) {
			getMemberLogLet(req).setMemberId(null);
		}
		showTrans = false;
		return RETCODE_OK;
	}

	// 查询会员交易历史
	public int doSearchTrans(IModuleRequest req, IModuleResponse resp)
			throws Exception {

		memberId = req.getParameter("id", 0l);
		if (getMemberPointHistoryLet(req) != null) {
			getMemberPointHistoryLet(req).setMemberId(null);
		}
		if (getMemberLevelHistoryLet(req) != null) {
			getMemberLevelHistoryLet(req).setMemberId(null);
		}
		if (getMemberExgPointLet(req) != null) {
			getMemberExgPointLet(req).setMemberId(null);
		}	
		if (getVoucherPoolDetailLet(req) != null) {
			getVoucherPoolDetailLet(req).setMemberId(null);
		}
		if (getMemCardRelLet(req) != null) {
			getMemCardRelLet(req).setMemberId(null);
			getMemCardRelLet(req).setInvoked(MemCardRelLet.class.getName());
		}
		if (getMemberLogLet(req) != null) {
			getMemberLogLet(req).setMemberId(null);
		}
		showTrans = true;
		return RETCODE_OK;
	}
	
	// 查询会员日志
	public int doSearchMemberLog(IModuleRequest req, IModuleResponse resp)
			throws Exception {

		memberId = req.getParameter("id", 0l);
		if (getMemberPointHistoryLet(req) != null) {
			getMemberPointHistoryLet(req).setMemberId(null);
		}
		if (getMemberLevelHistoryLet(req) != null) {
			getMemberLevelHistoryLet(req).setMemberId(null);
		}
		if (getVoucherPoolDetailLet(req) != null) {
			getVoucherPoolDetailLet(req).setMemberId(null);
		}
		if (getMemCardRelLet(req) != null) {
			getMemCardRelLet(req).setMemberId(null);
		}
		
		if (getMemberLogLet(req) != null) {
			getMemberLogLet(req).setMemberId(memberId);
			getMemberLogLet(req).setInvoked(MemberLogLet.class.getName());
		}
		showTrans = false;
		return RETCODE_OK;
	}
	
	
	public int dosearchTransAndGoodsPage(IModuleRequest req, IModuleResponse resp)
	throws Exception {
		query.put("pages", req.getParameter("page", 1));
		req.setAttribute(NAME_OF_QUERY, query);
		return RETCODE_OK;
	}

	// 会员交易历史列表
	public int showTransList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (showTrans) {
			if(cinemaMaps == null || cinemaMaps.isEmpty())
				cinemaMaps = SpringCommonService.getAllCinemaMap(null);
			query.put("transByMemberId", memberId);
			if ("G".equals(transType)) {
				PageResult<TGoodsTransOrder> pageResult = getService(
						TMemGoodsTransOrderService.class).findByCriteria(query);
				if (pageResult == null) {
					return RETCODE_HIDE;
				}
				// New Displaytag使用的结果集，并将其放到Request中，使JSP可引用。
				req.setAttribute("TRANSRESULT", buildResultGoods(pageResult));
				req.setAttribute("current", "G");
			} else {
				PageResult<TTicketTransOrder> pageResult = getService(
						TMemTicketTransOrderService.class)
						.findByCriteria(query);
				if (pageResult == null) {
					return RETCODE_HIDE;
				}
				// New Displaytag使用的结果集，并将其放到Request中，使JSP可引用。
				req.setAttribute("TRANSRESULT",
						buildResultTicketTrans(pageResult));
				req.setAttribute("current", "T");
			}
			req.setAttribute("CINEMAMAP", cinemaMaps);
			return RETCODE_OK;
		} else {
			return RETCODE_HIDE;
		}

	}

	// 查询会员交易历史
	public int searchGoodsOrTrans(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		transType = req.getParameter("transType", "G");
		return RETCODE_OK;

	}

	protected DisplayTagPageResult<TGoodsTransOrder> buildResultGoods(
			PageResult<TGoodsTransOrder> pageResult) {
		DisplayTagPageResult<TGoodsTransOrder> result = new DisplayTagPageResult<TGoodsTransOrder>(
				pageResult);
		result.setSortDirection(QueryCriteria.DIRECTION_ASC
				.equalsIgnoreCase(query.getDirection()) ? SortOrderEnum.DESCENDING
				: SortOrderEnum.ASCENDING);
		return result;
	}

	protected DisplayTagPageResult<TTicketTransOrder> buildResultTicketTrans(
			PageResult<TTicketTransOrder> pageResult) {
		DisplayTagPageResult<TTicketTransOrder> result = new DisplayTagPageResult<TTicketTransOrder>(
				pageResult);
		result.setSortDirection(QueryCriteria.DIRECTION_ASC
				.equalsIgnoreCase(query.getDirection()) ? SortOrderEnum.DESCENDING
				: SortOrderEnum.ASCENDING);
		result.setSortCriterion(query.getSort());
		return result;
	}
}
