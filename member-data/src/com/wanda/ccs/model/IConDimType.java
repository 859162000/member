package com.wanda.ccs.model;

/**
 * 卖品维数据定义
 * 
 * @author xiaofeng
 *
 */
public interface IConDimType extends IDimType {
	/** 卖品供应商、品牌状态 */
	public static final String DIMTYPE_CON_CONBRAND_STATUS = "174";
	public static final long LDIMTYPE_CON_CONBRAND_STATUS = 174;

	/** 卖品管理 扩展属性定义 录入类型 */
	public static final String DIMTYPE_CON_EXTPROPERTY_DATATYPE = "175";
	public static final long LDIMTYPE_CON_EXTPROPERTY_DATATYPE = 175;

	/** 卖品管理 物料分类定义 品项类型  */
	public static final String DIMTYPE_CON_CATEGORY_ITEMTYPE = "176";
	public static final long LDIMTYPE_CON_CATEGORY_ITEMTYPE = 176;
	
	/** 卖品供货范围类型 */
	public static final String DIMTYPE_CON_PA_TYPE = "177";
	public static final long LDIMTYPE_CON_PA_TYPE = 177;
	
	/** 采购类型 */
	public static final String DIMTYPE_CON_PURCHASE_TYPE = "178";
	public static final long LDIMTYPE_CON_PURCHASE_TYPE = 178;
	
	/** 计量单位 */
	public static final String DIMTYPE_CON_UNIT = "179";
	public static final long LDIMTYPE_CON_UNIT = 179;
	
	/** 套餐人数类型 */
	public static final String DIMTYPE_CON_SET_PERSON_NUMBER_TYPE = "180";
	public static final long LDIMTYPE_CON_SET_PERSON_NUMBER_TYPE = 180;
	
	/** 套餐主题类型 */
	public static final String DIMTYPE_CON_SET_SUBJECT_TYPE = "181";
	public static final long LDIMTYPE_CON_SET_SUBJECT_TYPE = 181;
	
	/** 适用类型 */
	public static final String DIMTYPE_CON_REGION_TYPE = "182";
	public static final long LDIMTYPE_CON_REGION_TYPE = 182;
	
	/** 售卖键销售渠道 */
	public static final String DIMTYPE_CON_SALE_UNIT_CHANNEL = "183";
	public static final long LIMTYPE_CON_SALE_UNIT_CHANNEL = 183;
	
	/** 售卖键销售类型 */
	public static final String DIMTYPE_CON_SALE_UNIT_SALE_TYPE = "184";
	public static final long LIMTYPE_CON_SALE_UNIT_SALE_TYPE = 184;
	
	/** 售卖键活动类型 */
	public static final String DIMTYPE_CON_SALE_UNIT_PROMOTION_TYPE = "185";
	public static final long LIMTYPE_CON_SALE_UNIT_PROMOTION_TYPE = 185;
	
	/** 物料库存扣减来源 */
	public static final String DIMTYPE_CON_ITEMSTORAGE_DEDUCE_TYPE = "186";
	public static final long LDIMTYPE_CON_ITEMSTORAGE_DEDUCE_TYPE = 186;
	
	/** 物料库存状态 */
	public static final String DIMTYPE_CON_ITEMSTORAGE_STATUS = "187";
	public static final long LDIMTYPE_CON_ITEMSTORAGE_STATUS = 187;
	
	/** 卖品信息 入库类型*/
	public static final String DIMTYPE_POS_STOCK_INTYPE = "190";
	public static final long LDIMTYPE_POS_STOCK_INTYPE = 190;
	
	/** 卖品信息 出库类型*/
	public static final String DIMTYPE_POS_STOCK_OUTTYPE = "191";
	public static final long LDIMTYPE_POS_STOCK_OUTTYPE = 191;
	
	/** 卖品信息 出库损耗类型*/
	public static final String DIMTYPE_POS_STOCK_LOSSTYPE = "192";
	public static final long LDIMTYPE_POS_STOCK_LOSSTYPE = 192;
	
	/** 合成品类型*/
	public static final String DIMTYPE_COMPOUND_TYPE = "194";
	public static final long LDIMTYPE_COMPOUND_TYPE = 194;
	
	/** 录入类型 文本 */
	public static final String DIMDEF_CON_EXTPROPERTY_DATATYPE_STRING = "String";
	/** 录入类型 整数 */
	public static final String DIMDEF_CON_EXTPROPERTY_DATATYPE_INTEGER = "Integer";
	/** 录入类型 浮点数 */
	public static final String DIMDEF_CON_EXTPROPERTY_DATATYPE_FLOAT = "Float";
	/** 录入类型 自定义 */
	public static final String DIMDEF_CON_EXTPROPERTY_DATATYPE_USERDEFINED = "UserDefined";
	/** 录入类型 维数据选择 */
	public static final String DIMDEF_CON_EXTPROPERTY_DATATYPE_DIMTYPE = "DimType";
	/** 录入类型 日期 */
	public static final String DIMDEF_CON_EXTPROPERTY_DATATYPE_DATE = "Date";
	/** 录入类型 日期时间 */
	public static final String DIMDEF_CON_EXTPROPERTY_DATATYPE_DATETIME = "DateTime";
	
	/** 品项类型 所有 */
	public static final String DIMDEF_CON_CATEGORY_ITEMTYPE_ALL = "01";
	/** 品项类型 单品 */
	public static final String DIMDEF_CON_CATEGORY_ITEMTYPE_SINGLE = "02";
	/** 品项类型 原材料 */
	public static final String DIMDEF_CON_CATEGORY_ITEMTYPE_MATERIAL = "03";
	/** 品项类型 套餐 */
	public static final String DIMDEF_CON_CATEGORY_ITEMTYPE_SET	 = "04";
	/** 品项类型 合成品 */
	public static final String DIMDEF_CON_CATEGORY_ITEMTYPE_COMPOUND = "05";
	
	/**售卖键 */
	public static final String DIMDEF_CON_SALE_UNIT = "06";
	
	/** 供应商 */
	public static final String DIMDEF_CON_PROVIDER = "07";
	
	/** 供应范围 */
	public static final String DIMDEF_CON_PA = "08";
	
	/** 品牌 */
	public static final String DIMDEF_CON_BRAND = "10";
	
	/** 仓库 */
	public static final String DIMDEF_CON_WAREHOURSE = "11";
	
	/** 货架 */
	public static final String DIMDEF_CON_SHELF = "12";
	
	/** 售卖键分组 */
	public static final String DIMDEF_CON_SALE_GROUP = "12";
	
	/** 采购类型 集采*/
	public static final String DIMDEF_CON_SINGLEITEM_PURCHASETYPE_SET = "01";
	/** 采购类型 分采*/
	public static final String DIMDEF_CON_SINGLEITEM_PURCHASETYPE_SEPARATE = "02";
	
	/** 适用类型 院线通用*/
	public static final String DIMDEF_CON_REGION_TYPE_GROUP = "1";
	/** 适用类型 影城专用*/
	public static final String DIMDEF_CON_REGION_TYPE_CINEMA = "2";
	
	/** 卖品供货范围类型 框架协议 */
	public static final String DIMTYPE_CON_PA_TYPE_FRAMEWORK = "1";
	/** 卖品供货范围类型 落地协议 */
	public static final String DIMTYPE_CON_PA_TYPE_WORKABLE = "2";
	/** 卖品供货范围类型 普通协议 */
	public static final String DIMTYPE_CON_PA_TYPE_GENERAL = "3";
	
	/** 审批状态  已撤销*/
	public static final String DIMDEF_CON_ITEM_STATUS_CANCEL = EnumConState.CANCEL.getOrdinal();
	/** 审批状态  草稿*/
	public static final String DIMDEF_CON_ITEM_STATUS_SAVED = EnumConState.SAVED.getOrdinal();
	/** 审批状态  已提交*/
	public static final String DIMDEF_CON_ITEM_STATUS_SUBMIT = EnumConState.SUBMIT.getOrdinal();
	/** 审批状态  已审批*/
	public static final String DIMDEF_CON_ITEM_STATUS_APPROVED = EnumConState.APPROVED.getOrdinal();
	/** 审批状态  拒绝审批*/
	public static final String DIMDEF_CON_ITEM_STATUS_REJECTED = EnumConState.REJECTED.getOrdinal();
	
	/** 售卖键状态未下发*/
	public static final String DIMDEF_CON_SALE_UNIT_STATUS_NOT_DISTRIBUTE = "0";
	/** 售卖键状态修改未下发*/
	public static final String DIMDEF_CON_SALE_UNIT_STATUS_EDIT_NOT_DISTRIBUTE = "1";
//	/** 售卖键状态数据已修改未下发*/
//	public static final String DIMDEF_CON_SALE_UNIT_STATUS_DATAEDIT_NOT_DISTRIBUTE = "2";
	/** 售卖键状态正在下发*/
	public static final String DIMDEF_CON_SALE_UNIT_STATUS_DATAEDIT_NOT_DISTRIBUTE = "2";
	/** 售卖键状态已下发*/
	public static final String DIMDEF_CON_SALE_UNIT_STATUS_DISTRIBUTE = "3";
	
	/** 销售类型 正价销售*/
	public static final String DIMDEF_CON_SALE_UNIT_SALE_TYPE_NON_PROMOTIONAL = "01";
	/** 销售类型 促销*/
	public static final String DIMDEF_CON_SALE_UNIT_SALE_TYPE_PROMOTION = "02";
	
	/** 售卖显示时间规则 不限时*/
	public static final String DIMDEF_CON_SALE_UNIT_TIME_RULE_TYPE_NOT_TIME_LIMIT = "1";
	/** 售卖显示时间规则 区间*/
	public static final String DIMDEF_CON_SALE_UNIT_TIME_RULE_TYPE_REGION = "2";
	/** 售卖显示时间规则 周期*/
	public static final String DIMDEF_CON_SALE_UNIT_TIME_RULE_TYPE_CYCLE = "3";
	
	/** 物料库存扣减来源 仓库*/
	public static final String DIMTYPE_CON_ITEMSTORAGE_DEDUCE_TYPE_WAREHOUSE = "1";
	/** 物料库存扣减来源 货架*/
	public static final String DIMTYPE_CON_ITEMSTORAGE_DEDUCE_TYPE_SHELF = "2";
	
	/** 物料名称长度*/
	public static final String CategoryNameLength = "20";
	/** 物料描述长度*/
	public static final String CategoryDescLength = "250";
	
	/** 扩展属性名称长度*/
	public static final String ExtPropertyNameLength = "50";
	/** 扩展属性简称长度*/
	public static final String ExtPropertyShortNameLength = "15";
	/** 扩展属性说明长度*/
	public static final String ExtPropertyDescLength = "250";
	
	/** 单品名称长度*/
	public static final String SingleItemNameLength = "50";
	/** 单品简称长度*/
	public static final String SingleItemShortNameLength = "15";
	/** 单品描述长度*/
	public static final String SingleItemDescLength = "250";
	/** 单品审批意见长度*/
	public static final String SingleItemAppCommentsLength = "250";
	
	/** 合成品名称长度*/
	public static final String CompoundDefNameLength = "50";
	/** 合成品简称长度*/
	public static final String CompoundDefShortNameLength = "15";
	
	/** 售卖键名称长度*/
	public static final String SaleUnitNameLength = "50";
	/** 售卖键备注长度*/
	public static final String SaleUnitCommentsLength = "250";
	
	/** 套餐计量单位 */
	public static final String DIMTYPE_CON_UNIT_SET = "17";
	
	/**
	 * 套餐销售查询-维度类型-套餐分类
	 */
	public static final String WINLET_PARAM_DIMENSIONTYPE_CATEGORY = "setCategory";

	/**
	 * 套餐销售查询-维度类型-主题类型
	 */
	public static final String WINLET_PARAM_DIMENSIONTYPE_SUBJECTTYPE = "subjectType";

	/**
	 * 套餐销售查询-维度类型-套餐人数
	 */
	public static final String WINLET_PARAM_DIMENSIONTYPE_PERSONNUMBER = "personNumber";

	/**
	 * 套餐销售查询-维度类型-价格范围
	 */
	public static final String WINLET_PARAM_DIMENSIONTYPE_PRICERANGE = "priceRange";
	
	/**
	 * 影城卖品入库、出库、库存数据库dblink名
	 */
	public static final String RAW_DBLINK = "@RAW_DWLINK";
	
	/**
	 * 卖品星型结构报表数据库dblink名
	 * 
	 */
	public static final String REPORT_DWLINK = "REPORT_DWLINK";
	
	/**
	 * 下发过程节点状态：POS已从DET上取走文件
	 */
	public static final String CON_PUBLISH_MILESTONE_TAKEDBYPOS = "0";
	
	/**
	 * 下发过程节点状态：已经下发到DET，等待POS取走
	 */
	public static final String CON_PUBLISH_MILESTONE_ARRIVEDDET = "1";
	
	/**
	 * 下发过程节点状态：数据已经生成，还没下发到DET
	 */
	public static final String CON_PUBLISH_MILESTONE_GOINGTODET = "2";
	
	
	/** 合成品类型 成品*/
	public static final String CON_COMPOUND_TYPE_1 = "1";
	/** 合成品类型 中间品*/
	public static final String CON_COMPOUND_TYPE_2 = "2";
	
	/** 合成品组成中的成份类型  普通*/
	public static final String CON_COMPOUND_DEF_DETAIL_TYPE_ORDINARY = "1";
	
	/** 合成品组成中的成份类型  中间品*/
	public static final String CON_COMPOUND_DEF_DETAIL_TYPE_INTERMEDIATE = "2";
	
	/** 合成品组成中的成份类型  中间品成分*/
	public static final String CON_COMPOUND_DEF_DETAIL_TYPE_INTERMEDIATE_COMPOSITION = "3";
}
