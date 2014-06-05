package com.wanda.ccs.campaign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.auth.UserLevel;
import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.core.BaseWinlet;
import com.wanda.ccs.model.IDimType;
import com.wanda.ccs.model.TCampaign;
import com.wanda.ccs.model.TCampaignCinema;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TConItem;
import com.wanda.ccs.model.TConItemCinema;
import com.xcesys.extras.core.exception.ApplicationException;
import com.xcesys.extras.util.ConvertUtil;
import com.xcesys.extras.util.PinYinUtil;

/**
 * 适用影城winlet
 * 
 * @author YangJianbin
 * 
 */
public class SelCinemaLet extends BaseWinlet implements IDimType {
	private static final long serialVersionUID = 1L;

	/**
	 * 是否全选
	 */
	private Boolean selectedAll = Boolean.FALSE;
	/**
	 * Map<所有区域编码, 所有区域下影城>
	 */
	private Map<String, List<TCinema>> rMap;
	/**
	 * Map<所选择的区域编码, 所选择的区域下影城>
	 */
	private Map<String, List<TCinema>> selMap = new LinkedHashMap<String, List<TCinema>>();
	/**
	 * Map<影城ID, 影城>
	 */
	private Map<Long, TCinema> cinemaMap;

	/**
	 * Map<区域名称第一个汉字的首字母, List<区域编码>>
	 */
	public Map<String, List<String>> charRegionCodeMap = null;

	/**
	 * 选择地影城ID
	 */
	private Long[] selcinemaIds;
	
	/**
	 * 是否隐藏做为被调用且弹出显示的本winlet
	 */
	private boolean hide = true;
	/**
	 * 该winlet调用者类名
	 */
	private String invoked;

	public String getInvoked() {
		return invoked;
	}

	public void setInvoked(String invoked) {
		this.invoked = invoked;
		this.hide = false;
	}
	
	public SelCinemaLet() {
		super();
		init();
	}
	
	/**
	 * 活动中选择适用影城
	 */
	private CampaignManLet campaignManLet;
	public CampaignManLet getCampaignManLet(IModuleRequest req) throws Exception{
		if(campaignManLet == null)
			campaignManLet = (CampaignManLet) req.getWinlet(CampaignManLet.class.getName());
		return campaignManLet;
	}

	/**
	 * 初始化
	 * 
	 * @param req
	 * @param resp
	 */
	private void init() {
		selMap.clear();
	}


	/**
	 * 营销活动初始化
	 * 
	 * @param conpa
	 */
	public void init(TCampaign campaign) {
		init();
		campaign.gettCampaignCinemas().size();
		if (campaign.getAllCinema()) {
			selectedAll = true;
		} else {
			for (TCampaignCinema cc : campaign.gettCampaignCinemas()) {
				this.addItem(cc.gettCinema());
			}
			selectedAll = false;
		}
	}

	/**
	 * 品项装配适用影城
	 * 
	 * @param conpa
	 */
	public TConItem populate(TConItem item) {
		// 是否适用所有影城
		item.setApplyAllCinema(selectedAll);
		if (selectedAll) {
			Set<TConItemCinema> del = new HashSet<TConItemCinema>();
			for (TConItemCinema ic : item.gettConItemCinemas()) {
				if(!item.getApproved().booleanValue())
					del.add(ic);
			}
			item.gettConItemCinemas().removeAll(del);
		} else {
			if (selcinemaIds != null) {
				List<Long> selectedIds = Arrays.asList(selcinemaIds);
				Set<TConItemCinema> del = new HashSet<TConItemCinema>();
				for (TConItemCinema ic : item.gettConItemCinemas()) {
					if (!selectedIds.contains(ic.getCinemaId().longValue())) 
						if(!item.getApproved().booleanValue())
							del.add(ic);
				}
				item.gettConItemCinemas().removeAll(del);
			}

			Set<Long> exCinemaIds = new HashSet<Long>();
			for (TConItemCinema c : item.gettConItemCinemas()) {
				exCinemaIds.add(c.getCinemaId());
			}

			for (String region : getSelMap().keySet()) {
				List<TCinema> cinemas = getSelMap().get(region);
				for (TCinema c : cinemas) {
					if (!exCinemaIds.contains(c.getId())) {
						TConItemCinema e = new TConItemCinema();
						e.setCinemaId(c.getId());
						e.settCinema(cinemaMap.get(c.getId()));
						e.settConItem(item);
						item.gettConItemCinemas().add(e);
					}
				}
			}
		}
		return item;
	}


	/**
	 * 添加一项
	 * 
	 * @param item
	 */
	private void addItem(TCinema newone) {
		if (selMap != null) {
			if (selMap.get(newone.getArea()) == null) {
				selMap.put(newone.getArea(), new ArrayList<TCinema>());
			}
			selMap.get(newone.getArea()).add(newone);
		}
	}

	/**
	 * 移除一项
	 * 
	 * @param item
	 */
	private void removeItem(TCinema revone) {
		List<TCinema> list = selMap.get(revone.getArea());
		if (list != null) {
			int i = 0;
			for (TCinema c : list) {
				if (c.getId().longValue() == revone.getId().longValue())
					break;
				i++;
			}
			list.remove(i);
		}
	}

	/**
	 * 添加或移除本影城
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public int doAddMyCinema(IModuleRequest req, IModuleResponse resp) {
		if (getUser(req).getLevel().equals(UserLevel.CINEMA)) {
			Boolean flag = Boolean.valueOf(req.getParameter("flag"));
			if (cinemaMap == null)
				cinemaMap = getService(TCinemaService.class)
						.findUnDeletedCinemasMap();
			if (flag) {
				if(cinemaMap.get(getUser(req).getCinemaId()) == null){
					throw new ApplicationException("本影城不存在或不是开业状态或不是正常供片状态！");
				}
				addItem(cinemaMap.get(getUser(req).getCinemaId()));
			} else {
				if (selectedAll) {
					for (Long id : cinemaMap.keySet()) {
						if (id.longValue() == getUser(req).getCinemaId())
							continue;
						TCinema c = cinemaMap.get(id);
						String area = c.getArea();
						if (selMap.get(area) == null) {
							selMap.put(area, new ArrayList<TCinema>());
						}
						selMap.get(area).add(c);
					}
					selectedAll = Boolean.FALSE;
				} else {
					removeItem(cinemaMap.get(getUser(req).getCinemaId()));
				}
			}
		}
		return RETCODE_OK;
	}

	/**
	 * 关闭列表
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public int doClose(IModuleRequest req, IModuleResponse resp) {
		hide = true;
		return RETCODE_HIDE;
	}


	/**
	 * 保存选择地适用影城
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public int doSave(IModuleRequest req, IModuleResponse resp) throws Exception{
		init();
		List<TCinema> list = new ArrayList<TCinema>();
		selectedAll = false;
		String[] selcienmas = req.getParameterValues("selcienmas");
		if (selcienmas != null && selcienmas.length > 0) {
			if (selcienmas.length == cinemaMap.size()) {
				this.selectedAll = true;
			} else {
				selcinemaIds = ConvertUtil
						.convertStringArrayToLongArray(selcienmas);
				for (Long id : selcinemaIds) {
					TCinema c = cinemaMap.get(id);
//					String area = c.getArea();
//					if (selMap.get(area) == null) {
//						selMap.put(area, new ArrayList<TCinema>());
//					}
//					selMap.get(area).add(c);
					list.add(c);
				}
			}
		}
		if(getInvoked().equals(CampaignManLet.class.getName())){
			getCampaignManLet(req).saveCinema(list, selectedAll);
		}
		hide = true;
		return RETCODE_OK;
	}

	public Map<Long, TCinema> getCinemaMap() {
		return cinemaMap;
	}

	public Map<String, List<TCinema>> getrMap() {
		return rMap;
	}

	public Map<String, List<TCinema>> getSelMap() {
		return selMap;
	}

	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if (getUser(req).getLevel().equals(UserLevel.CINEMA)) {
			String regionCode = getUser(req).getRegionCode();
			boolean includeMe = selectedAll;
			if (!selectedAll && selMap.containsKey(regionCode)) {
				for (TCinema c : selMap.get(regionCode)) {
					if (c.getId().longValue() == getUser(req).getCinemaId()) {
						includeMe = true;
						break;
					}
				}
			}
			req.setAttribute("includeMe", includeMe);
		}
		return RETCODE_OK;
	}

	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		if(hide)
			return RETCODE_HIDE;
		CcsUserProfile user = (CcsUserProfile) req.getUserProfile();
		if (rMap == null) {
			rMap = new LinkedHashMap<String, List<TCinema>>();
			if (cinemaMap == null){
				if(user.getLevel() == UserLevel.GROUP){
					cinemaMap = getService(TCinemaService.class)
							.findUnDeletedCinemasMap();
				}else{
					cinemaMap =  getService(TCinemaService.class)
							.findUnDeletedCinemasMapByReqgion(user.getRegionCode());
				}
			}
				
			for (Long id : cinemaMap.keySet()) {
				TCinema c = cinemaMap.get(id);
				String area = c.getArea();
				if (rMap.get(area) == null) {
					rMap.put(area, new ArrayList<TCinema>());
				}
				rMap.get(area).add(c);
			}
		}
		// 加载
		if (charRegionCodeMap == null) {
			charRegionCodeMap = new LinkedHashMap<String, List<String>>();
			Map<String, String> regionCodeMap = getCtxDIMS(req).get(
					DIMTYPE_AREA);
			for (String regionCode : regionCodeMap.keySet()) {
				String pinYin = PinYinUtil.getPinYin(
						regionCodeMap.get(regionCode).substring(0, 1))
						.toUpperCase();
				if (charRegionCodeMap.get(pinYin) == null) {
					charRegionCodeMap.put(pinYin, new ArrayList<String>());
				}
				charRegionCodeMap.get(pinYin).add(regionCode);
			}
		}
		return RETCODE_OK;
	}

	public Boolean getSelectedAll() {
		return selectedAll;
	}

	public int doSetSelectedAll(IModuleRequest req, IModuleResponse resp) {
		this.selectedAll = Boolean.valueOf(req.getParameter("selectedAll"));
		return RETCODE_OK;
	}

	public Map<String, List<String>> getCharRegionCodeMap() {
		return charRegionCodeMap;
	}

}
