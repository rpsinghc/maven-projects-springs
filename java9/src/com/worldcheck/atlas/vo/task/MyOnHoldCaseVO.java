package com.worldcheck.atlas.vo.task;

public class MyOnHoldCaseVO {
	private String crn;
	private String country;
	private String case_creation_date;
	private String subject_name;
	private String case_status_id;

	public String getCase_status_id() {
		return this.case_status_id;
	}

	public void setCase_status_id(String caseStatusId) {
		this.case_status_id = caseStatusId;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCase_creation_date() {
		return this.case_creation_date;
	}

	public void setCase_creation_date(String caseCreationDate) {
		this.case_creation_date = caseCreationDate;
	}

	public String getSubject_name() {
		return this.subject_name;
	}

	public void setSubject_name(String subjectName) {
		this.subject_name = subjectName;
	}
}