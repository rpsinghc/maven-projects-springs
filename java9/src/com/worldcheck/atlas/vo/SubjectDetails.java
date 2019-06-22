package com.worldcheck.atlas.vo;

import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.EntityTypeMasterVO;
import com.worldcheck.atlas.vo.masters.IndustryMasterVO;
import com.worldcheck.atlas.vo.masters.ResearchElementMasterVO;
import com.worldcheck.atlas.vo.masters.RisksMasterVO;
import java.util.Date;
import java.util.List;

public class SubjectDetails {
	private List<ResearchElementMasterVO> reList;
	private String crn;
	private int subjectId;
	private String subjectName;
	private boolean primarySub;
	private int entityTypeId;
	private String position;
	private int countryId;
	private String countryCode;
	private String address;
	private String otherDetails;
	private String industryCode;
	private String industryName;
	private String updatedBy;
	private Date updatedOn;
	private String entityName;
	private String countryName;
	private String industryVode;
	private String caseStatus;
	private String reIds;
	private String reNames;
	private CountryMasterVO countryVO;
	private EntityTypeMasterVO entityVO;
	private IndustryMasterVO indVO;
	private List<RisksMasterVO> subjectRisk = null;
	private String[] modifiedRecords;
	private boolean pullBackToResearch;
	private boolean checkBizSolo;
	private String redirectString;
	private String subjectAddedBIMgr;
	private String isBIDelete;
	private String reportSubjectName;
	private int reportOrder;
	private String isisSubjectId;
	private String isLegacyData;
	private int isisCase;
	private String color;
	private int isClicked;
	private int totalDisAssociatedCount;
	private int totalAssociatedCount;
	private float reJlpPoints;
	private String teamType;
	private String teamTypeId;
	private String rePoints;
	private String office;
	private int isShared;
	private int isReplicated;
	private List<TeamOfficeVO> replicationListofRE;
	private String reportTypeId;
	private String reportType;
	private String isSubjLevelSubRptReq;
	private String subReportTypeId;
	private String subReportType;
	private String clientCode;
	private String slCurrency;
	private float slBudget;
	private int updateSubjectCount;
	private int newSubjectId;
	private String isisErrorMessge;
	private String currencycode;
	private String isUpdated;

	public String getCurrencycode() {
		return this.currencycode;
	}

	public int getUpdateSubjectCount() {
		return this.updateSubjectCount;
	}

	public void setUpdateSubjectCount(int updateSubjectCount) {
		this.updateSubjectCount = updateSubjectCount;
	}

	public int getNewSubjectId() {
		return this.newSubjectId;
	}

	public void setNewSubjectId(int newSubjectId) {
		this.newSubjectId = newSubjectId;
	}

	public String getIsisErrorMessge() {
		return this.isisErrorMessge;
	}

	public void setIsisErrorMessge(String isisErrorMessge) {
		this.isisErrorMessge = isisErrorMessge;
	}

	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}

	public String getIsUpdated() {
		return this.isUpdated;
	}

	public void setIsUpdated(String isUpdated) {
		this.isUpdated = isUpdated;
	}

	public int getIsReplicated() {
		return this.isReplicated;
	}

	public void setIsReplicated(int isReplicated) {
		this.isReplicated = isReplicated;
	}

	public float getReJlpPoints() {
		return this.reJlpPoints;
	}

	public void setReJlpPoints(float reJlpPoints) {
		this.reJlpPoints = reJlpPoints;
	}

	public String getRedirectString() {
		return this.redirectString;
	}

	public void setRedirectString(String redirectString) {
		this.redirectString = redirectString;
	}

	public boolean isCheckBizSolo() {
		return this.checkBizSolo;
	}

	public void setCheckBizSolo(boolean checkBizSolo) {
		this.checkBizSolo = checkBizSolo;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public boolean isPullBackToResearch() {
		return this.pullBackToResearch;
	}

	public void setPullBackToResearch(boolean pullBackToResearch) {
		this.pullBackToResearch = pullBackToResearch;
	}

	public String[] getModifiedRecords() {
		return this.modifiedRecords;
	}

	public void setModifiedRecords(String[] modifiedRecords) {
		this.modifiedRecords = modifiedRecords;
	}

	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getIndustryVode() {
		return this.industryVode;
	}

	public void setIndustryVode(String industryVode) {
		this.industryVode = industryVode;
	}

	public List<ResearchElementMasterVO> getReList() {
		return this.reList;
	}

	public void setReList(List<ResearchElementMasterVO> reList) {
		this.reList = reList;
	}

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public int getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public boolean isPrimarySub() {
		return this.primarySub;
	}

	public boolean getPrimarySub() {
		return this.primarySub;
	}

	public void setPrimarySub(boolean primarySub) {
		this.primarySub = primarySub;
	}

	public int getEntityTypeId() {
		return this.entityTypeId;
	}

	public void setEntityTypeId(int entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getCountryId() {
		return this.countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOtherDetails() {
		return this.otherDetails;
	}

	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}

	public String getIndustryCode() {
		return this.industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getReIds() {
		return this.reIds;
	}

	public void setReIds(String reIds) {
		this.reIds = reIds;
	}

	public String getReNames() {
		return this.reNames;
	}

	public void setReNames(String reNames) {
		this.reNames = reNames;
	}

	public CountryMasterVO getCountryVO() {
		return this.countryVO;
	}

	public void setCountryVO(CountryMasterVO countryVO) {
		this.countryVO = countryVO;
	}

	public EntityTypeMasterVO getEntityVO() {
		return this.entityVO;
	}

	public void setEntityVO(EntityTypeMasterVO entityVO) {
		this.entityVO = entityVO;
	}

	public IndustryMasterVO getIndVO() {
		return this.indVO;
	}

	public void setIndVO(IndustryMasterVO indVO) {
		this.indVO = indVO;
	}

	public List<RisksMasterVO> getSubjectRisk() {
		return this.subjectRisk;
	}

	public void setSubjectRisk(List<RisksMasterVO> subjectRisk) {
		this.subjectRisk = subjectRisk;
	}

	public String getSubjectAddedBIMgr() {
		return this.subjectAddedBIMgr;
	}

	public void setSubjectAddedBIMgr(String subjectAddedBIMgr) {
		this.subjectAddedBIMgr = subjectAddedBIMgr;
	}

	public String getIsBIDelete() {
		return this.isBIDelete;
	}

	public void setIsBIDelete(String isBIDelete) {
		this.isBIDelete = isBIDelete;
	}

	public String getReportSubjectName() {
		return this.reportSubjectName;
	}

	public void setReportSubjectName(String reportSubjectName) {
		this.reportSubjectName = reportSubjectName;
	}

	public int getReportOrder() {
		return this.reportOrder;
	}

	public void setReportOrder(int reportOrder) {
		this.reportOrder = reportOrder;
	}

	public String getIsisSubjectId() {
		return this.isisSubjectId;
	}

	public void setIsisSubjectId(String isisSubjectId) {
		this.isisSubjectId = isisSubjectId;
	}

	public String getCaseStatus() {
		return this.caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public int getIsisCase() {
		return this.isisCase;
	}

	public void setIsisCase(int isisCase) {
		this.isisCase = isisCase;
	}

	public String getIndustryName() {
		return this.industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getIsLegacyData() {
		return this.isLegacyData;
	}

	public void setIsLegacyData(String isLegacyData) {
		this.isLegacyData = isLegacyData;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setTotalDisAssociatedCount(int totalDisAssociatedCount) {
		this.totalDisAssociatedCount = totalDisAssociatedCount;
	}

	public int getTotalDisAssociatedCount() {
		return this.totalDisAssociatedCount;
	}

	public void setTotalAssociatedCount(int totalAssociatedCount) {
		this.totalAssociatedCount = totalAssociatedCount;
	}

	public int getTotalAssociatedCount() {
		return this.totalAssociatedCount;
	}

	public void setIsClicked(int isClicked) {
		this.isClicked = isClicked;
	}

	public int getIsClicked() {
		return this.isClicked;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	public String getTeamType() {
		return this.teamType;
	}

	public void setTeamTypeId(String teamTypeId) {
		this.teamTypeId = teamTypeId;
	}

	public String getTeamTypeId() {
		return this.teamTypeId;
	}

	public void setRePoints(String rePoints) {
		this.rePoints = rePoints;
	}

	public String getRePoints() {
		return this.rePoints;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getOffice() {
		return this.office;
	}

	public void setIsShared(int isShared) {
		this.isShared = isShared;
	}

	public int getIsShared() {
		return this.isShared;
	}

	public void setReplicationListofRE(List<TeamOfficeVO> replicationListofRE) {
		this.replicationListofRE = replicationListofRE;
	}

	public List<TeamOfficeVO> getReplicationListofRE() {
		return this.replicationListofRE;
	}

	public String getReportTypeId() {
		return this.reportTypeId;
	}

	public void setReportTypeId(String reportTypeId) {
		this.reportTypeId = reportTypeId;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getIsSubjLevelSubRptReq() {
		return this.isSubjLevelSubRptReq;
	}

	public void setIsSubjLevelSubRptReq(String isSubjLevelSubRptReq) {
		this.isSubjLevelSubRptReq = isSubjLevelSubRptReq;
	}

	public String getSubReportTypeId() {
		return this.subReportTypeId;
	}

	public void setSubReportTypeId(String subReportTypeId) {
		this.subReportTypeId = subReportTypeId;
	}

	public String getSubReportType() {
		return this.subReportType;
	}

	public void setSubReportType(String subReportType) {
		this.subReportType = subReportType;
	}

	public String getClientCode() {
		return this.clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getSlCurrency() {
		return this.slCurrency;
	}

	public void setSlCurrency(String slCurrency) {
		this.slCurrency = slCurrency;
	}

	public float getSlBudget() {
		return this.slBudget;
	}

	public void setSlBudget(float slBudget) {
		this.slBudget = slBudget;
	}

	public void setSlBudget(String slBudget) {
		this.slBudget = Float.parseFloat(slBudget);
	}
}