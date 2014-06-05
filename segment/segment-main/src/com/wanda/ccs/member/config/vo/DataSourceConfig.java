package com.wanda.ccs.member.config.vo;

public class DataSourceConfig {
	
	private String databaseType;

	private String driverClass;
	
	private String jdbcUrl;
	
	private String username;
	
	private String password;
	
	private boolean dataInitialized;

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isDataInitialized() {
		return dataInitialized;
	}

	public void setDataInitialized(boolean dataInitialized) {
		this.dataInitialized = dataInitialized;
	}

}
