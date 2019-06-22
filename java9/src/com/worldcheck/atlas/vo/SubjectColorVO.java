package com.worldcheck.atlas.vo;

public class SubjectColorVO {
	private int color_Id;
	private int subjectId;
	private String crn;
	private String color;
	private int isClicked;

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setColor_Id(int color_Id) {
		this.color_Id = color_Id;
	}

	public int getColor_Id() {
		return this.color_Id;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getSubjectId() {
		return this.subjectId;
	}

	public void setIsClicked(int isClicked) {
		this.isClicked = isClicked;
	}

	public int getIsClicked() {
		return this.isClicked;
	}
}