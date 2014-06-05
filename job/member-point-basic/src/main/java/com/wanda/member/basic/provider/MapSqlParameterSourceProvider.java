package com.wanda.member.basic.provider;

import java.util.Map;

import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class MapSqlParameterSourceProvider implements ItemSqlParameterSourceProvider<Map<String, Object>> {

	@Override
	public SqlParameterSource createSqlParameterSource(Map<String, Object> map) {
		return new MapSqlParameterSource(map);
	}

}
