package com.worldcheck.atlas.vo.masters;

import com.worldcheck.atlas.vo.SubjectDetails;
import java.util.List;

public class BranchOfficeMasterVO {
	private String branchOfficeCode;
	private String branchOffice;
	private String branchOfficeUsername;
	private String branchOfficeStatus;
	private int branchOfficeId;
	private int statusval;
	private String updatedBy;
	private Integer limit;
	private Integer start;
	private String ccCount;
	private String ssmCount;
	private String cmCount;
	private String rccCount;
	private String tdCount;
	private String hmCount;
	private String umCount;
	private String sortColumnName;
	private String sortType;
	private List<SubjectDetails> subjectList;

	public String getCcCount() {
		return this.ccCount;
	}

	public void setCcCount(String ccCount) {
		this.ccCount = ccCount;
	}

	public String getSsmCount() {
		return this.ssmCount;
	}

	public void setSsmCount(String ssmCount) {
		this.ssmCount = ssmCount;
	}

	public String getCmCount() {
		return this.cmCount;
	}

	public void setCmCount(String cmCount) {
		this.cmCount = cmCount;
	}

	public String getRccCount() {
		return this.rccCount;
	}

	public void setRccCount(String rccCount) {
		this.rccCount = rccCount;
	}

	public String getTdCount() {
		return this.tdCount;
	}

	public void setTdCount(String tdCount) {
		this.tdCount = tdCount;
	}

	public String getHmCount() {
		return this.hmCount;
	}

	public void setHmCount(String hmCount) {
		this.hmCount = hmCount;
	}

	public String getUmCount() {
		return this.umCount;
	}

	public void setUmCount(String umCount) {
		this.umCount = umCount;
	}

	public Integer getLimit() {
		return this.limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getStart() {
		return this.start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getStatusval() {
		return this.statusval;
	}

	public void setStatusval(int statusval) {
		this.statusval = statusval;
	}

	public int getBranchOfficeId() {
		return this.branchOfficeId;
	}

	public void setBranchOfficeId(int branchOfficeId) {
		this.branchOfficeId = branchOfficeId;
	}

	public String getBranchOfficeCode() {
		return this.branchOfficeCode;
	}

	public void setBranchOfficeCode(String branchOfficeCode) {
		this.branchOfficeCode = branchOfficeCode;
	}

	public String getBranchOffice() {
		return this.branchOffice;
	}

	public void setBranchOffice(String branchOffice) {
		this.branchOffice = branchOffice;
	}

	public String getBranchOfficeUsername() {
		return this.branchOfficeUsername;
	}

	public void setBranchOfficeUsername(String branchOfficeUsername) {
		this.branchOfficeUsername = branchOfficeUsername;
	}

	public String getBranchOfficeStatus() {
		return this.branchOfficeStatus;
	}

	public void setBranchOfficeStatus(String branchOfficeStatus) {
		this.branchOfficeStatus = branchOfficeStatus;
	}

	public String getSortColumnName() {
		return this.sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	public String getSortType() {
		return this.sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public void setSubjectList(List<SubjectDetails> subjectList) {
		this.subjectList = subjectList;
	}

	public List<SubjectDetails> getSubjectList() {
		return this.subjectList;
	}
}