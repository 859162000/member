package com.wanda.ccs.core;

import java.util.Map;

import org.displaytag.properties.SortOrderEnum;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.xcesys.extras.core.dao.model.AbstractEntity;
import com.xcesys.extras.core.exception.ApplicationException;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.ICrudService;
import com.xcesys.extras.core.util.StringUtil;
import com.xcesys.extras.util.ConvertUtil;
import com.xcesys.extras.webapp.model.DisplayTagPageResult;

public abstract class BaseSearchWinlet<T extends AbstractEntity> extends
		BaseWinlet {

	private static final long serialVersionUID = 5431831601515558678L;
	/**
	 * 查询得出的可翻页结果集。
	 */
	// protected PageResult<T> pageResult;

	/**
	 * 查询条件封装对象，包括翻页与排序参数。
	 */
	protected QueryCriteria<String, Object> query = new QueryCriteria<String, Object>();

	/**
	 * 单一数据记录对象ID，常用于编辑或删除时传递用。
	 */
	protected Long id;

	private boolean pagging;
	private boolean sorting;

	/**
	 * 判断查询结果是否为空。
	 */
	// protected boolean hasResult = false;

	public BaseSearchWinlet() {
		super();
	}

	/**
	 * 创建使用Displagtag标签库的分页结果集。
	 * <p>
	 * 子类可重载此方法，增加自定义的查询结果处理逻辑。
	 * </p>
	 * 
	 * @param pageResult
	 * @return
	 */
	protected DisplayTagPageResult<T> buildResult(PageResult<T> pageResult) {
		DisplayTagPageResult<T> result = new DisplayTagPageResult<T>(pageResult);
		result.setSortDirection(QueryCriteria.DIRECTION_ASC
				.equalsIgnoreCase(query.getDirection()) ? SortOrderEnum.DESCENDING
				: SortOrderEnum.ASCENDING);
		result.setSortCriterion(query.getSort());
		return result;
	}

	/**
	 * 输入查询条件，并执行查询操作。
	 */
	public int doSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		id = null;
		// 判断是否处于翻页，排序处理，如果是，则不重新处理查询条件。
		Boolean tmp = ConvertUtil.convertObjectToBoolean(req
				.getParameter("pagging"));
		pagging = tmp == null ? false : tmp.booleanValue();
		tmp = ConvertUtil.convertObjectToBoolean(req.getParameter("sorting"));
		sorting = tmp == null ? false : tmp.booleanValue();

		if (pagging || sorting) {
			preparePaggingQuery(req);
		} else {
			pagging = false;
			sorting = false;
			prepareQuery(req);
		}
		req.setAttribute(NAME_OF_QUERY, query);
		return RETCODE_OK;
	}

	/**
	 * 从列表行纪录中选择后的详细信息的查询。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int doView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		id = req.getParameter("id", 0L);
		log.debug("#### 执行查看方法:doView ####");
		return RETCODE_OK;
	}

	/**
	 * 根据数据对象id读取数据，如果数据不存在，则抛出异常。
	 * 
	 * @param id
	 *            主键ID
	 * @return 主键对应的数据记录。
	 */
	protected T findById(Long id) {
		T model = null;
		if (id != null && id.longValue() > 0) {
			model = getCrudService().getById(id);
		}
		if (model == null) {
			throw new ApplicationException("ID:" + id + "对应的数据记录未找到，可能已被删除。");
		}
		return model;
	}

	/**
	 * 返回对应的CrudService实例，通过SpringContextUtil从Spring容器中获得。
	 * 
	 * @return Crud操作Service.
	 */
	protected abstract ICrudService<T> getCrudService();

	/**
	 * 返回列表中默认每页显示纪录行数，默认值为10。
	 * 
	 * @return 每页显示纪录行数
	 */
	protected int getDefaultPageSize() {
		return 10;
	}

	/**
	 * 根据维数据类型ID获取维数据Map，Map的key为维数据code，value为维数据name.
	 * 
	 * @param req 
	 * @param typeId
	 *            维数据类型ID
	 * @return 维数据Map
	 */
	protected final Map<String, String> getServletContextDimDefByTypeId(IModuleRequest req , String typeId) {
		return getCtxDIMS(req).get(typeId);
	}

	/**
	 * 初始化查询条件，可将初始值Put到QueryCriteria中，页面从QueryCriteria中获取并显示。
	 * 
	 * @param req
	 * @param resp
	 * @see #doSearch(IModuleRequest, IModuleResponse)
	 */
	protected void initSearch(IModuleRequest req, IModuleResponse resp) {
		// items per page
		query.setPageSize(getDefaultPageSize());
	}

	/**
	 * 准备翻页及排序参数。
	 * 
	 * @param req
	 */
	private void preparePaggingQuery(IModuleRequest req) {
		// set sort field
		String v = req.getParameter("sort");
		if (!StringUtil.isNullOrBlank(v)) {
			query.setSort(v);
		}
		// set sort direction
		v = req.getParameter("dir");
		v = StringUtil.isNullOrBlank(v) ? req.getParameter("direction") : v;
		if (!StringUtil.isNullOrBlank(v)) {
			query.setDirection(v);
		}
		// set start index.
		int p = req.getParameter("page", 1);
		if (p > 0) {
			query.setPage(p);
		}
	}

	/**
	 * 从查询form的参数中初始化查询QueryCriteria，包括排序sort,dir和翻页page.
	 * <p>
	 * 子类应当重载该方法，从IModuleRequest中读取查询条件的值传入QueryCriteria对象中。
	 * 子类必须调用一次父类的本方法一次，才能保证列表的排序与翻页功能正常。
	 * </p>
	 * 
	 * @param req
	 */
	protected void prepareQuery(IModuleRequest req) {
		query.setPage(1);
	}

	/**
	 * 显示列表窗口内容，分页查询结果为null时则隐藏该窗口。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int showList(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		PageResult<T> pageResult = getCrudService().findByCriteria(query);
		if (pageResult == null) {
			return RETCODE_HIDE;
		}
		// New Displaytag使用的结果集，并将其放到Request中，使JSP可引用。
		req.setAttribute(NAME_OF_PAGE_RESULT, buildResult(pageResult));
		return RETCODE_OK;
	}

	/**
	 * 显示查询页面。
	 */
	public int showSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		initSearch(req, resp);
		req.setAttribute(NAME_OF_QUERY, query);
		return RETCODE_OK;
	}
}