package com.xcesys.extras.webapp.model;

import java.util.List;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;

import com.xcesys.extras.core.model.PageResult;

public class DisplayTagPageResult<T> implements PaginatedList {

	private static final long serialVersionUID = 226687182034214461L;

	// ITAF pageResult 对象
	private PageResult<T> pageResult = new PageResult<T>();

	// desc or asc, not implemented yet
	private String sortCriterion;

	// order sequence, not implemented yet
	private SortOrderEnum sortDirection = SortOrderEnum.ASCENDING;

	public DisplayTagPageResult() {
	}

	public DisplayTagPageResult(PageResult<T> pageResult) {
		this.pageResult = pageResult;
	}

	@Override
	public int getFullListSize() {
		return pageResult.getTotalCount();
	}

	@Override
	public List<T> getList() {
		return pageResult.getContents();
	}

	@Override
	public int getObjectsPerPage() {
		int pageSize = pageResult.getPageSize();
		return pageSize > 0 ? pageSize : Integer.MAX_VALUE;
	}

	@Override
	public int getPageNumber() {
		return pageResult.getCurrentPage();
	}

	public PageResult<T> getPageResult() {
		return pageResult;
	}

	@Override
	public String getSearchId() {
		// Not implemented for now.
		// This is required, if we want the ID to be included in the paginated
		// purpose.
		return null;
	}

	@Override
	public String getSortCriterion() {
		return sortCriterion;
	}

	@Override
	public SortOrderEnum getSortDirection() {
		return sortDirection;
	}

	public int getStartIndex() {
		return pageResult.getStartIndex();
	}

	public int getTotalPages() {
		return pageResult.getTotalPage();
	}

	public void setSortCriterion(String sortCriterion) {
		this.sortCriterion = sortCriterion;
	}

	public void setSortDirection(SortOrderEnum sortDirection) {
		this.sortDirection = sortDirection;
	}
}
