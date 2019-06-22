package com.worldcheck.atlas.vo.junointerface;

import java.util.List;

public class CRNInfoVO {
	private List subjects;
	private String reportType;
	private String subReportType;

	public List getSubjects() {
		return this.subjects;
	}

	public void setSubjects(List subjects) {
		this.subjects = subjects;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getSubReportType() {
		return this.subReportType;
	}

	public void setSubReportType(String subReportType) {
		this.subReportType = subReportType;
	}
}