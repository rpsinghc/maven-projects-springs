package com.worldcheck.atlas.vo.task;

public class ReviewScoreSheetVO {
	private long id;
	private long officeId;
	private long sheetId;
	private String analyst;
	private String sheetName;
	private String office;
	private String reviewer;
	private long qualityPercent;
	private String performer;
	private long reId;
	private long subjectId;
	private long teamId;
	private String subjectName;
	private String country;
	private String reName;
	private String teamType;
	private String scoresheetName;
	private long scoresheetMasterId;
	private String fieldName;
	private long scoresheetFldsMasterId;
	private String category;
	private String subCategory;
	private long cntOfErrors;
	private long points;

	public String getPerformer() {
		return this.performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	public long getReId() {
		return this.reId;
	}

	public void setReId(long reId) {
		this.reId = reId;
	}

	public long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public long getTeamId() {
		return this.teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getReName() {
		return this.reName;
	}

	public void setReName(String reName) {
		this.reName = reName;
	}

	public String getTeamType() {
		return this.teamType;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	public String getScoresheetName() {
		return this.scoresheetName;
	}

	public void setScoresheetName(String scoresheetName) {
		this.scoresheetName = scoresheetName;
	}

	public long getScoresheetMasterId() {
		return this.scoresheetMasterId;
	}

	public void setScoresheetMasterId(long scoresheetMasterId) {
		this.scoresheetMasterId = scoresheetMasterId;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public long getScoresheetFldsMasterId() {
		return this.scoresheetFldsMasterId;
	}

	public void setScoresheetFldsMasterId(long scoresheetFldsMasterId) {
		this.scoresheetFldsMasterId = scoresheetFldsMasterId;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return this.subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public long getCntOfErrors() {
		return this.cntOfErrors;
	}

	public void setCntOfErrors(long cntOfErrors) {
		this.cntOfErrors = cntOfErrors;
	}

	public long getPoints() {
		return this.points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(long officeId) {
		this.officeId = officeId;
	}

	public long getSheetId() {
		return this.sheetId;
	}

	public void setSheetId(long sheetId) {
		this.sheetId = sheetId;
	}

	public String getAnalyst() {
		return this.analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public String getSheetName() {
		return this.sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getOffice() {
		return this.office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getReviewer() {
		return this.reviewer;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public long getQualityPercent() {
		return this.qualityPercent;
	}

	public void setQualityPercent(long qualityPercent) {
		this.qualityPercent = qualityPercent;
	}
}