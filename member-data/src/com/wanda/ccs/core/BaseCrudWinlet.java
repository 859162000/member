package com.wanda.ccs.core;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.beanutils.PropertyUtils;

import com.aggrepoint.adk.IModuleRequest;
import com.aggrepoint.adk.IModuleResponse;
import com.wanda.ccs.model.TPriceBase;
import com.xcesys.extras.core.dao.model.AbstractEntity;
import com.xcesys.extras.core.exception.ValidationException;
import com.xcesys.extras.core.util.StringUtil;
import com.xcesys.extras.util.BeanUtil;
import com.xcesys.extras.util.ConvertUtil;

/**
 * 增删改查基础Winlet类。
 * <p>
 * 增删改查功能可继承此类，重写或实现主要的方法便可完成基本增删改查功能。
 * </p>
 * 
 * @author Danne
 * 
 * @param <T>
 *            AbstractEntity的子类，Hibernate的数据Model.
 * 
 */
public abstract class BaseCrudWinlet<T extends AbstractEntity> extends
		BaseSearchWinlet<T> {
	private static final long serialVersionUID = 4679069015199778193L;
	/**
	 * 一组数据记录对象ID，常用于多行删除时传递用。
	 */
	protected Long[] ids;

	/**
	 * 复制操作的源数据对象id.
	 */
	protected Long copyFromId;

	@Deprecated
	/** 
	 * 编辑或查看的数据对象 
	 * 
	 */
	protected T mEdit;
	/**
	 * 编辑还是查看状态中。
	 */
	protected boolean editing = false;
	/**
	 * Hibernate Validator.
	 */
	protected static Validator validator = Validation
			.buildDefaultValidatorFactory().getValidator();

	/**
	 * 编辑页面Ajax输入验证。
	 * 
	 * @param req
	 *            请求
	 * @param resp
	 *            响应
	 * @return
	 * @throws Exception
	 */
	public int ajaxValidate(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		return RETCODE_OK;
	}

	/**
	 * 编辑窗口取消关闭。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int cancelEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		id = null;
		editing = false;
		copyFromId = null;
		mEdit = null;
		return RETCODE_OK;
	}

	/**
	 * 判断参数中的对象是否可以执行编辑(进入编辑状态)，可以返回true，否则返回false，默认为可编辑。
	 * <p>
	 * 子类如需要自己特定可编辑与否的控制逻辑，请重载此方法。
	 * </p>
	 * 
	 * @param mEdit
	 *            编辑对象。
	 * @return 可以返回true，否则返回false.
	 */
	protected boolean canEdit(T mEdit) {
		return true;
	}

	/**
	 * 使用PropertyUtils.copyProperties从源对象中复制属性值到目标对象中，
	 * 复制后目标对象的公用属性会被调用initialize方法重新初始化.
	 * <p>
	 * 注：子类可重载此方法，即如自己的值复制处理。
	 * </p>
	 * 
	 * @param req
	 *            请求内容。
	 * 
	 * @param mEdit
	 *            目标对象
	 * @param copyFromObject
	 *            复制源对象。
	 */
	protected void copyFrom(IModuleRequest req, T mEdit, T copyFromObject) {
		try {
			PropertyUtils.copyProperties(mEdit, copyFromObject);
			mEdit.initialize();
		} catch (Exception e) {
			log.error("复制新增时复制属性发生异常。", e);
		}
	}

	/**
	 * 新建排片信息表
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int doCreate(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		editing = true;
		id = null;
		copyFromId = req.getParameter("copyFromId", 0L);

		// 设定对象为正在编辑状态
		log.debug("#### 执行新增方法:doCreate ####");

		return RETCODE_OK;
	}

	/**
	 * 删除指定id或ids的数据，如果数据是DeleteableEntity的字类，则执行的是逻辑删除。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
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
			getCrudService().delete(ids);
		}
		id = null;
		ids = null;
		editing = false;
		return RETCODE_OK;
	}

	/**
	 * 从列表行纪录中选择后的详细信息的查询，并进入编辑状态。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int doEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		log.debug("#### 执行编辑方法:doEdit，编辑对象 ####");
		editing = true;
		mEdit = null;
		id = req.getParameter("id", 0L);
		return RETCODE_OK;
	}

	@Override
	public int doSearch(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		mEdit = null;
		editing = false;
		return super.doSearch(req, resp);
	}

	@Override
	public int doView(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		editing = false;
		mEdit = null;
		return super.doView(req, resp);
	}

	/**
	 * 返回model新实例，用于新建数据记录。
	 * <p>
	 * 子类应当重载此方法，提供如何实例化一个数据对象，通常为new操作。
	 * </p>
	 * 
	 * @return
	 */
	protected T newModel() {
		throw new UnsupportedOperationException("未实现的数据对象创建方法。");
	}

	/**
	 * 新增或修改后的值从Form传入后，使用BeanUtils实现IModuleRequest中的数据(Map类型)自动复制到编辑的数据模型中。
	 * 
	 * @param mEdit
	 * @param req
	 */
	@SuppressWarnings("rawtypes")
	protected void populate(T mEdit, IModuleRequest req) throws Exception {
		// FIX: 处理空字符串，避免外键Long型数据被置0，导致insert或update失败。
		Hashtable<String, Object> map = req.getParameters();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			// 属性类型是Long的去掉空字符串。
			String pn = it.next();
			Class type = PropertyUtils.getPropertyType(mEdit, pn);
			if (Long.class.equals(type) || Date.class.equals(type)) {
				Object o = map.get(pn);
				if (o != null && StringUtil.isNullOrBlank((String) o)) {
					it.remove();
				}
				PropertyUtils.setProperty(mEdit, pn, null);
			}
		}
		BeanUtil.populate(mEdit, map);
	}

	/**
	 * 执行保存处理后,需要进行的额外处理,默认识清空id和mEdit对象，让编辑窗口隐藏。
	 * <p>
	 * 注意：子类可以重载该方法，实现自己逻辑，如不清空id和mEdit对象，保持编辑窗口显示状态。
	 * </p>
	 * 
	 * @param mEdit
	 *            保存成功后的对象，可以获取到保存后的id值。
	 */
	protected void postSave() {
		// 清空属性
		id = null;
		mEdit = null;
		editing = false;
	}

	/**
	 * 保存操作前需要额外处理的方法。
	 * 
	 * @param model
	 *            TODO
	 * @param mEdit
	 *            将要保存的对象。
	 */
	protected void preSave(T model) {

	}

	/**
	 * 保存数据操作。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int saveEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		// try {
		T model = null;
		if (mEdit != null) {
			// TODO:mEdit需要移除
			model = mEdit;
		} else {
			if (id != null) {
				model = super.findById(id);
			} else {
				model = newModel();
			}
		}
		// 从request中把数据populate到编辑对象中
		populate(model, req);
		req.setAttribute(NAME_OF_EDITING_OBJECT, model);
		// 保存前执行数据验证。
		validation(req, resp, model);

		// 初始化安全环境上下文。
		securityContext(req);

		// 执行保存对象操作。
		if (model != null) {
			preSave(null);
			getCrudService().createOrUpdate(model);
			postSave();
			log.debug("#### 执行数据保存方法:saveEdit，保存对象[" + model + "] ####");
		}
		// } catch (ValidationException e) {
		// log.error("数据输入校验异常.", e);
		// resp.setUserMessage(e.getLocalizedMessage());
		// return RETCODE_VERR;
		// } catch (TransientDataAccessException e) {
		// log.error(e);
		// resp.setUserMessage("保存数据时发生异常，数据已经被其他用户修改或删除。");
		// return RETCODE_ERR;
		// } catch (Exception e) {
		// log.error(e);
		// resp.setUserMessage("保存数据时发生异常:" + e.getLocalizedMessage());
		// return RETCODE_ERR;
		// }
		// 成功保存后清空变量
		id = null;
		copyFromId = null;
		editing = false;
		mEdit = null;
		return RETCODE_OK;
	}

	/**
	 * 显示编辑窗口，当前编辑对象为null时则隐藏该窗口。
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	public int showEdit(IModuleRequest req, IModuleResponse resp)
			throws Exception {
		T model = null;
		if (id != null && id != 0) {
			// 编辑或查看的场景
			try {
				// 编辑情况
				model = findById(id);
			} catch (Exception e) {
				resp.setUserMessage(e.getLocalizedMessage());
				return RETCODE_ERR;
			}
		} else if (editing) {
			// 新增数据的场景
			model = newModel();
			// 如果有拷贝源ID，则拷贝
			if (copyFromId != null && copyFromId != 0) {
				try {
					T copyFromObject = super.findById(copyFromId);
					if (copyFromObject != null) {
						copyFrom(req, model, copyFromObject);
					}
				} catch (Exception e) {
					log.warn(e);
				}
			}
		}
		if (model == null) {
			log.debug("#### 隐藏Edit窗口 ####");
			return 8000;
		}
		if (model != null) {
			model.setEditing(editing && canEdit(model));
		}
		// TODO:确认mEdit属性不再被使用后，删除此赋值行
		mEdit = model;
		log.debug("#### 显示Edit窗口 ####");
		req.setAttribute(NAME_OF_EDITING_OBJECT, model);
		return RETCODE_OK;
	}

	/**
	 * 执行对于某个类对应的属性输入值的校验操作，如果校验错误，则会调用IModuleResponse.setUserMessage()
	 * 将错误信息保存并返回页面.
	 * <p>
	 * 本方法主要应用于页面实现的Ajax输入验证逻辑，Ajax输入验证每次只验证一个输入数值。
	 * </p>
	 * 
	 * @param req
	 *            请求
	 * @param resp
	 *            响应
	 * @param clz
	 *            输入数据对应数据模型的Class.
	 * @param property
	 *            需要进行输入验证的数据模型的属性名称。
	 * @param value
	 *            需要进行输入验证的数据模型属性对应的输入值，值数据类型必须与属性数据类型匹配。
	 * @param group
	 *            验证分组的类名称
	 * @return 验证通过返回true，否则返回false.
	 */
	protected boolean validateValue(IModuleRequest req, IModuleResponse resp,
			Class<T> clz, String property, Object value, Class<TPriceBase> group) {
		Set<ConstraintViolation<T>> violations = validator.validateValue(clz,
				property, value);
		String errMsg = "";
		if (violations != null && !violations.isEmpty()) {
			for (ConstraintViolation<T> v : violations) {
				errMsg += v.getMessage();
			}
			resp.setUserMessage(errMsg);
			return false;
		}
		return true;
	}

	/**
	 * 数据保存前的必要服务器端数据输入验证，验证错误时抛出异常，并且错误详细信息被保存到IModuleResponse中。
	 * 
	 * @param req
	 *            请求
	 * @param resp
	 *            响应
	 * @param mEdit
	 *            需要进行输入验证的数据实例。
	 * @throws ValidationException
	 *             输入验证不通过时抛出异常。
	 */
	protected void validation(IModuleRequest req, IModuleResponse resp, T mEdit)
			throws ValidationException {
		String msg = "";
		Set<ConstraintViolation<T>> violations = validator.validate(mEdit);
		int i = 1;
		if (violations != null && !violations.isEmpty()) {
			for (ConstraintViolation<T> v : violations) {
				log.debug(v.getPropertyPath() + v.getMessage());
				msg += (i++) + "." + v.getMessage() + "<br>";
			}
			throw new ValidationException(msg);
		}
	}
}