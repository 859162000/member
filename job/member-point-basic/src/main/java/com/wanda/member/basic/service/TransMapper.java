package com.wanda.member.basic.service;

import java.util.List;

public interface TransMapper {
	public List<String> selectTicketTransCinema(String bizDate);
	public List<String> selectGoodsTransCinema(String bizDate);
	
}
