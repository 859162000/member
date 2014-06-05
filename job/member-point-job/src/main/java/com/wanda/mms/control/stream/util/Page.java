package com.wanda.mms.control.stream.util;

import java.util.ArrayList;

public class Page {
	
	private long pageSize;	//页大小

	private long pageCount;	 //总页数

	private ArrayList<Object> list=new ArrayList<Object>(); //数据总内容

	private long page;	//当前页码

	private long dataCount; //数据总行数

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

	public ArrayList<Object> getList() {
		return list;
	}

	public void setList(ArrayList<Object> list) {
		this.list = list;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public long getDataCount() {
		return dataCount;
	}

	public void setDataCount(long dataCount) {
		this.dataCount = dataCount;
	}
	
	

}
