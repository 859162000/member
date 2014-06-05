package com.wanda.ccs.member.segment.service;

import java.util.List;

import com.wanda.ccs.member.segment.vo.CodeEntryVo;

/**
 * 用于定义主数据中的枚举类型数据。
 *
 * 功能描述：  .  <BR>
 * 开发者: 张晨龙
 * 时间：2010-7-19
 */
public interface CodeList extends java.io.Serializable{
	/**
	 * 用以唯一标识MdmEnum的名称
	 * 方法功能：. <BR>
	 * @return
	 * @since
	 */
	String getTypeId();

	/**
	 *
	 * 方法功能：得到枚举条目的列表，列表中的数据是按MdmEnumEntry中orderSn的大小正序排列的. <BR>
	 * @return
	 */
	List<CodeEntryVo> getEntrys();

	/**
	 *
	 * 方法功能：通过值找到对应的条目. <BR>
	 * @param value
	 * @return
	 * @since
	 */
	CodeEntryVo getEntryByValue(String value);

	/**
	 *
	 * 方法功能：通过名字找到对应的条目. <BR>
	 * @param name
	 * @return
	 * @since
	 */
	CodeEntryVo getEntryByName(String name);

	/**
	 *
	 * 方法功能：通过值找对应条目的名称. 如果对应值不存在，则返回 null<BR>
	 * @param value
	 * @return
	 * @since
	 */
	String getEntryNameByValue(String value);
}
