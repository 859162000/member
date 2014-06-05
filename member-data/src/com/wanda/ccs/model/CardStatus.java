package com.wanda.ccs.model;

public interface CardStatus {
	/** 已预制*/
	public static final String CARD_STATUS_I = "I";
	
	/** 已初始化但未发放*/
	public static final String CARD_STATUS_P = "P";
	
	/** 正常*/
	public static final String CARD_STATUS_O = "O";
	
	/** 禁用*/
	public static final String CARD_STATUS_D = "D";
	
	/** 已换卡不可用*/
	public static final String CARD_STATUS_R = "R";
	
	/** 已升级不可用*/
	public static final String CARD_STATUS_U = "U";
	
	/** 已丢失不可用*/
	public static final String CARD_STATUS_L = "L";
	
	/** 已销卡*/
	public static final String CARD_STATUS_X = "X";
	
	/** 预制卡失败*/
	public static final String CARD_STATUS_E = "E";
	
	/** 已退卡不可用*/
	public static final String CARD_STATUS_T = "T";
	
	/** 已补卡不可用*/
	public static final String CARD_STATUS_N = "N";
}
