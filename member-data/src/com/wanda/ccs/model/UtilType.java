package com.wanda.ccs.model;
/**
 * 维数据类型常量类
 * @author Chen
 *
 */
public class UtilType implements IDimType {
	/**
	 * 对应系统设定中放映制式的name标识
	 * 数字制式
	 */
	public static final String HALL_DIGITAL = "PROJECTION_TYPE_DIGIT";
	public static final String FILM_DIGITAL = "FILM_PROJECTION_TYPE_DIGIT";
	/**
	 * 对应系统设定中放映制式的name标识
	 * 胶片制式
	 */
	public static final String HALL_FILM = "PROJECTION_TYPE_FILM";
	public static final String FILM_FILM = "FILM_PROJECTION_TYPE_FILM";
	/**
	 * 对应系统设定中放映制式的name标识
	 * 3D制式
	 */
	public static final String HALL_THREE_D = "PROJECTION_TYPE_3D";
	public static final String FILM_THREE_D = "FILM_PROJECTION_TYPE_3D";
	/**
	 * 对应系统设定中放映制式的name标识
	 * IMAX制式
	 */
	public static final String HALL_IMAX = "PROJECTION_TYPE_IMAX";
	public static final String FILM_IMAX = "FILM_PROJECTION_TYPE_IMAX";
	/**
	 * 对应系统设定中放映制式的name标识
	 * realD制式
	 */
	public static final String HALL_REALD = "PROJECTION_TYPE_REALD";
	public static final String FILM_REALD = "FILM_PROJECTION_TYPE_REALD";
	/**
	 * 对应系统设定影片合同或协议文件上传路径
	 */
	public static final String CONTRACT_FILE_PATH = "CONTRACT_FILE_PATH";
	
	/**
	 * 系统中导出Excel 默认条数
	 */
	public static final String NUMBER_OF_EXPORT_EXCEL = "NUMBER_OF_EXPORT_EXCEL";
	
	/**
	 * 邮件 SMTP Server
	 */
	public static final String EMAIL_SMTP_SERVER = "EMAIL_SMTP_SERVER";
	/**
	 * 邮件发送方地址
	 */
	public static final String EMAIL_FROM_URL = "EMAIL_FROM_URL";
	/**
	 * 邮件发送方密码
	 */
	public static final String EMAIL_FROM_PASSWORD = "EMAIL_FROM_PASSWORD";
	
	/**
	 * 对应系统设定中放映制式的name标识
	 * 对应ehr系统中区域的上一级ORGID
	 */
	public static final String EHR_AREA_P = "EHR_AREA_P";
	/**
	 * 对应系统设定中放映制式的name标识
	 * 对应ehr系统中直销店的上一级ORGID
	 */
	public static final String EHR_DER_P = "EHR_DER_P";
	
	/**
	 * 对应系统设定中放映制式的name标识
	 * EHR_JOBS查询EHR变更管理所涉及的岗位
	 */
	public static final String EHR_JOBS = "EHR_JOBS";
	
	
	/**
	 * 万达院线上报票房 发送邮件 SMTP Server
	 */
	public static final String WD_EMAIL_SMTP_SERVER = "WD_EMAIL_SMTP_SERVER";
	/**
	 * 万达院线上报票房邮件发送方地址
	 */
	public static final String WD_EMAIL_FROM_URL = "WD_EMAIL_FROM_URL";
	/**
	 * 万达院线上报票房邮件发送方密码
	 */
	public static final String WD_EMAIL_FROM_PASSWORD = "WD_EMAIL_FROM_PASSWORD";
	/**
	 * 万达院线上报票房邮件发送 对方邮件地址
	 */
	public static final String WD_EMAIL_TO_ADDRESS = "WD_EMAIL_TO_ADDRESS";
	/**
	 * 万达院线上报票房邮件发送 邮件标题
	 */
	public static final String WD_EMAIL_TITLE = "WD_EMAIL_TITLE";
	/**
	 * 万达院线上报票房邮件发送 邮件内容简述 100字以内
	 */
	public static final String WD_EMAIL_CONTENT = "WD_EMAIL_CONTENT";
	/**
	 * 导出excle使用的保护密码
	 */
	public static final String WD_EXPOT_EXCLE_PASSWORD="WD_EXPOT_EXCLE_PASSWORD";
}
