package model;

public class Pagination {
	Integer start;
	Integer pageSize;

	public Pagination() {
		super();
	}

	public Pagination(Integer start, Integer pageSize) {
		super();
		this.start = start;
		this.pageSize = pageSize;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isPaginatedQuery() {
		if ((this.start != null) && (this.start > 0) && (this.pageSize != null) && (this.pageSize > 0)) {
			return true;
		}
		return false;
	}
}
