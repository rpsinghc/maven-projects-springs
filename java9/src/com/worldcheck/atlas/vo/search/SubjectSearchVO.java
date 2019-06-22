package com.worldcheck.atlas.vo.search;

import com.worldcheck.atlas.bl.search.SearchCriteria;

public class SubjectSearchVO extends SearchCriteria {
	private String entityName;
	private String isLegacyData;
	private String isExactMatch;
	private String subjectId;
	private String crn;
	private String relatedCrn;

	public String getRelatedCrn() {
		return this.relatedCrn;
	}

	public void setRelatedCrn(String relatedCrn) {
		this.relatedCrn = relatedCrn;
	}

	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getIsLegacyData() {
		return this.isLegacyData;
	}

	public void setIsLegacyData(String isLegacyData) {
		this.isLegacyData = isLegacyData;
	}

	public String getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public void setIsExactMatch(String isExactMatch) {
		this.isExactMatch = isExactMatch;
	}

	public String getIsExactMatch() {
		return this.isExactMatch;
	}
}