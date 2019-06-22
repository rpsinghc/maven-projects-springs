package com.worldcheck.atlas.vo.masters;

public class GradingPoint {
	private String countOfError;
	private String point;
	private Long subCategoryId;

	public String getCountOfError() {
		return this.countOfError;
	}

	public void setCountOfError(String countOfError) {
		this.countOfError = countOfError;
	}

	public String getPoint() {
		return this.point;
	}

	public void setPoint(String point) {
		this.point = point;
	}
}