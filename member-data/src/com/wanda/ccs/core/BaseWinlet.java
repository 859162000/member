package com.wanda.ccs.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONNull;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.aggrepoint.adk.Winlet;
import com.aggrepoint.adk.plugin.WinletUserProfile;
import com.wanda.ccs.auth.CcsUserProfile;
import com.wanda.ccs.auth.service.EHROrgnizationService;
import com.wanda.ccs.basemgt.service.TCinemaService;
import com.wanda.ccs.basemgt.service.TCityService;
import com.wanda.ccs.basemgt.service.TProvinceService;
import com.wanda.ccs.basemgt.service.TPublisherService;
import com.wanda.ccs.film.service.TFilmService;
import com.wanda.ccs.model.EHROrgnization;
import com.wanda.ccs.model.TCinema;
import com.wanda.ccs.model.TCity;
import com.wanda.ccs.model.TFilm;
import com.wanda.ccs.model.TProvince;
import com.wanda.ccs.model.TPublisher;
import com.wanda.ccs.service.SpringCommonService;
import com.xcesys.extras.core.exception.ApplicationException;
import com.xcesys.extras.core.model.ValueLabelItem;
import com.xcesys.extras.util.SpringContextUtil;

public abstract class BaseWinlet extends Winlet {

	private static final long serialVersionUID = -3874252375876418472L;
	/**
	 * 保存在Request中查询条件QueryCriteria的名称常量。
	 */
	protected static final String NAME_OF_QUERY = "query";
	/**
	 * 保存在查询结果列表DisplageTagePageResult的名称常量。
	 */
	public static final String NAME_OF_PAGE_RESULT = "pageResult";
	/**
	 * 保存正在编辑中的数据的名称常量。
	 */
	public static final String NAME_OF_EDITING_OBJECT = "model";
	/**
	 * 日志log,使用Commons-logging.
	 */
	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * 窗口隐藏的返回码
	 */
	protected static final int RETCODE_HIDE = 8000;
	/**
	 * 正常执行成功的返回码
	 */
	protected static final int RETCODE_OK = 0;
	/**
	 * 执行错误，异常时的返回码。
	 */
	protected static final int RETCODE_ERR = 1;
	/**
	 * 验证错误时候的返回码。
	 */
	protected static final int RETCODE_VERR = 2;

	/**
	 * 显示错误信息的集合
	 */
	private Set<String> msg = new HashSet<String>();

	public BaseWinlet() {
		super();
	}

	/**
	 * 执行审核操作
	 * 
	 * @param req
	 *            请求
	 * @param resp
	 *            响应
	 * @return
	 */
	public int doApprove(IModuleRequest req, IModuleResponse resp) {
		securityContext(req);
		// 调用具体的业务逻辑
		return 0;
	}

	/**
	 * 根据区域编码获取影院列表，返回JSON结果。
	 * 
	 * @return
	 */
	public int doGetCinema(IModuleRequest req, IModuleResponse resp) {
		List<TCinema> list = null;

		String area = req.getParameter("param");
		String like = req.getParameter("like");
		if (StringUtils.isNotBlank(area)) {
			list = SpringCommonService.getCinemasByArea(area);
		} else if (StringUtils.isNotBlank(like)) {
			list = getService(TCinemaService.class).findByLikeCodeOrName(like);
		}

		List<ValueLabelItem> items = new ArrayList<ValueLabelItem>();
		if (list != null && !list.isEmpty()) {
			for (TCinema cinema : list) {
				items.add(new ValueLabelItem(cinema.getId(), cinema.getCode(),
						cinema.getShortName(), cinema.getOutName()));
			}
		}
		writeJSON(resp, items);
		return 0;
	}

	/**
	 * 获取某城市下影院，返回JSON结果。
	 * 
	 * @return
	 */
	public int doGetCinemaOfACity(IModuleRequest req, IModuleResponse resp) {
		Long city = req.getParameter("param", 0L);
		List<TCinema> list = getService(TCinemaService.class)
				.findUnDeletedCinemas(city);
		List<ValueLabelItem> items = new ArrayList<ValueLabelItem>();
		if (list != null && !list.isEmpty()) {
			for (TCinema cinema : list) {
				items.add(new ValueLabelItem(cinema.getId(), cinema.getCode(),
						cinema.getShortName(), cinema.getOutName()));
			}
		}
		writeJSON(resp, items);
		return 0;
	}

	
	
	/**
	 * 获取某区域下影院，返回JSON结果。 EHR变更信息功能使用
	 * 
	 * @return
	 */
	public int doGetCinemaByArea(IModuleRequest req, IModuleResponse resp) {
		String area = req.getParameter("param", null);
		List<EHROrgnization> list = null;
		if("null".equals(area)||area== null ||"".equals(area)){
			list = null;
		}else{
			list = getService(EHROrgnizationService.class)
			.getParentID(area);
		}
		
		List<ValueLabelItem> items = new ArrayList<ValueLabelItem>();
		if (list != null && !list.isEmpty()) {
			for (EHROrgnization org : list) {
				items.add(new ValueLabelItem(org.getId(), org.getOrgCode(),
						org.getShortName(), org.getParentUnitId().toString()));
			}
		}
		writeJSON(resp, items);
		return 0;
	}

	/**
	 * 根据省Id获取城市列表，返回JSON结果。
	 * 
	 * @return
	 */
	public int doGetCity(IModuleRequest req, IModuleResponse resp) {
		Long provinceId = req.getParameter("param", 0L);
		List<TCity> list = SpringCommonService.getCityByProviceId(provinceId);
		List<ValueLabelItem> items = new ArrayList<ValueLabelItem>();
		if (list != null && !list.isEmpty()) {
			for (TCity tCity : list) {
				items.add(new ValueLabelItem(tCity.getId(), tCity.getCode(),
						tCity.getName(), tCity.getCitylevel()));
			}
		}
		writeJSON(resp, items);
		return 0;
	}

	/**
	 * 根据区域编码获取所属城市表，返回JSON结果。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public int doGetCityByArea(IModuleRequest req, IModuleResponse resp) {
		String area = req.getParameter("param");
		List<TCity> list = getService(TCityService.class).findByArea(area);
		List<ValueLabelItem> items = new ArrayList<ValueLabelItem>();
		if (list != null && !list.isEmpty()) {
			for (TCity tCity : list) {
				items.add(new ValueLabelItem(tCity.getId(), tCity.getCode(),
						tCity.getName(), tCity.getCitylevel()));
			}
		}
		writeJSON(resp, items);
		return 0;

	}

	/**
	 * 根据影片拼码或片名模糊查询影片，返回JSON结果(用于Autocomplete)。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public int doGetFilm(IModuleRequest req, IModuleResponse resp) {
		String like = req.getParameter("like");
		List<TFilm> list = getService(TFilmService.class).findByCodeName(like);
		List<ValueLabelItem> items = new ArrayList<ValueLabelItem>();
		if (list != null && !list.isEmpty()) {
			for (TFilm tfilm : list) {
				items.add(new ValueLabelItem(tfilm.getId(),
						tfilm.getFilmCode(), tfilm.getFilmName(), ""
								+ tfilm.getRunningTime()));
			}
		}
		writeJSON(resp, items);
		return 0;
	}

	/**
	 * 根据发行商名称模糊查询发行商，返回JSON结果(用于Autocomplete)。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public int doGetPublisher(IModuleRequest req, IModuleResponse resp) {
		String like = req.getParameter("like");
		if ("".equals(like)) {
			return RETCODE_OK;
		}
		List<TPublisher> list = getService(TPublisherService.class).findByName(
				like);
		List<ValueLabelItem> items = new ArrayList<ValueLabelItem>();
		if (list != null && !list.isEmpty()) {
			for (TPublisher publisher : list) {
				items.add(new ValueLabelItem(publisher.getId(), null, publisher
						.getPublishername(), publisher.getPublishername()));
			}
		}
		writeJSON(resp, items);
		return RETCODE_OK;
	}

	/**
	 * 根据区域编码获取所属省列表，返回JSON结果。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public int doGetProvinceByArea(IModuleRequest req, IModuleResponse resp) {
		String area = req.getParameter("param");
		List<TProvince> list = getService(TProvinceService.class).findByArea(
				area);
		List<ValueLabelItem> items = new ArrayList<ValueLabelItem>();
		if (list != null && !list.isEmpty()) {
			for (TProvince tProvince : list) {
				items.add(new ValueLabelItem(tProvince.getId(), tProvince
						.getCode(), tProvince.getName(), tProvince.getCode()));
			}
		}
		writeJSON(resp, items);
		return 0;

	}

	/**
	 * 根据城市编码获取所属省列表，返回JSON结果。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	public int doGetProvinceByCity(IModuleRequest req, IModuleResponse resp) {
		Long cityId = req.getParameter("param", 0L);
		TCity city = null;
		List<ValueLabelItem> items = new ArrayList<ValueLabelItem>();
		if (cityId > 0) {
			city = getService(TCityService.class).findById(cityId);
			TProvince province = getService(TProvinceService.class).getById(
					city.gettProvince().getId());
			if (province != null) {
				items.add(new ValueLabelItem(province.getId(), province
						.getCode(), province.getName(), province.getArea()));
			}

		} else {
			List<TProvince> list = getService(TProvinceService.class).findAll();
			if (list != null && !list.isEmpty()) {
				for (TProvince tProvince : list) {
					items.add(new ValueLabelItem(tProvince.getId(), tProvince
							.getCode(), tProvince.getName(), tProvince
							.getArea()));
				}
			}
		}
		writeJSON(resp, items);
		return 0;

	}

	/**
	 * 执行提交审核操作
	 * 
	 * @param req
	 *            请求
	 * @param resp
	 *            响应
	 * @return
	 */
	public int doSubmit(IModuleRequest req, IModuleResponse resp) {
		securityContext(req);
		// 调用具体的业务逻辑
		return 0;
	}
	
	/**
	 * 返回维数据Map.
	 *
	 * @param req
	  *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Map<String, Map<String, String>> getCtxDIMS(IModuleRequest req) {
			HttpServletRequest httpReq = (HttpServletRequest) req
					.getRequestObject();
			Map<String, Map<String, String>> dims = (Map<String, Map<String, String>>) httpReq
					.getSession().getServletContext().getAttribute("DIMS");
			return dims;
	}

	/**
	 * 返回维数据Map.
	 * 
	 * @return
	 */
//	protected Map<String, Map<String, String>> getDIMS() {
//		return SpringCommonService.getAllDimDefs();
//	}

	/**
	 * 从Spring容器中获取指定Class的 Bean。
	 * 
	 * @param <K>
	 *            Bean
	 * @param clazz
	 *            Bean class名称
	 * 
	 * @return
	 */
	protected <T> T getService(Class<T> clazz) {
		return SpringContextUtil.getBean(clazz);
	}

	/**
	 * 获取当前用户身份
	 * 
	 * @param req
	 * @return
	 */
	public CcsUserProfile getUser(IModuleRequest req) {
		return (CcsUserProfile) req.getUserProfile();
	}

	/**
	 * 传递用户信息到Extras框架中，Extras框架使用Spring Security.
	 * 
	 * @param req
	 *            请求。
	 */
	protected void securityContext(IModuleRequest req) {
		SecurityContext context = SecurityContextHolder.getContext();
		WinletUserProfile userProfile = (WinletUserProfile) req
				.getUserProfile();
		if (!userProfile.isAnonymous()) {
			context.setAuthentication(new UsernamePasswordAuthenticationToken(
					userProfile.getProperty(WinletUserProfile.PROPERTY_ID),
					null));
		} else {
			log.warn("用户没有登录系统，但试图执行数据操作。");
		}

	}

	/**
	 * 将Java对象以JSON格式写到Response中并返回。
	 * 
	 * @param resp
	 * @param data
	 */
	protected void writeJSON(IModuleResponse resp, Object data) {
		JSON json = null;
		if (data == null) {
			data = JSONNull.getInstance();
		}
		json = JSONSerializer.toJSON(data);
		try {
			HttpServletResponse rep = (HttpServletResponse) resp
					.getResponseObject();
			rep.setContentType("application/json;charset=UTF-8");
			if (json != null) {
				rep.getWriter().write(json.toString());
			}
			rep.getWriter().flush();
		} catch (IOException e) {
			log.error("数据转换并返回JSON时发生异常。", e);
		}
	}

	/**
	 * 添加错误消息。
	 * 
	 * @param msg
	 */
	protected void addErrorMessage(String msg) {
		this.msg.add(msg);
	}

	protected void addAllErrorMessage(Set<String> msgs) {
		this.msg.addAll(msg);
	}

	/**
	 * 清除错误消息。
	 */
	protected void clearErrorMessage() {
		this.msg.clear();
	}

	/**
	 * 是否存在错误？
	 * 
	 * @return
	 */
	protected boolean hasErrors() {
		return !msg.isEmpty();
	}

	/**
	 * 以用户信息方式显示错误信息。
	 * 
	 * @param resp
	 * @param msg
	 */
	protected void showErrors(IModuleResponse resp) {
		StringBuffer sb = new StringBuffer();
		if (!msg.isEmpty()) {
			for (String m : msg) {
				sb.append(m).append("<br/>");
			}
			clearErrorMessage();
			resp.setUserMessage(sb.toString());
		}
	}

	/**
	 * 抛出多个异常错误，错误有try catch处理后添加到msg集合里。
	 * 
	 * @param msg
	 */
	protected void throwErrors() {
		StringBuffer sb = new StringBuffer();
		if (!msg.isEmpty()) {
			for (String m : msg) {
				sb.append(m).append("<br/>");
			}
			clearErrorMessage();
			throw new ApplicationException(sb.toString());
		}
	}

	protected void throwErrors(Set<String> msg) {
		StringBuffer sb = new StringBuffer();
		if (!msg.isEmpty()) {
			for (String m : msg) {
				sb.append(m).append("<br/>");
			}
			throw new ApplicationException(sb.toString());
		}
	}
}