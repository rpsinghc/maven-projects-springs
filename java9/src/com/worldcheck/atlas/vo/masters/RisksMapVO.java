package com.worldcheck.atlas.vo.masters;

import java.util.ArrayList;
import java.util.List;

public class RisksMapVO {
	private int id;
	private int mappingId;
	private int historyMappingId;
	private String riskCode;
	private String riskMappingName;
	private int mappingStatus;
	private int riskGroup;
	private String subjectType;
	private int visibleToClient;
	private String clientCodes;
	private String reportTypes;
	private String subReportTypes;
	private String subjectCountry;
	private String researchElements;
	private String UpdatedBy;
	private String UpdatedOn;
	private String reportTypeNames;
	private ArrayList<String> SubReportTypeNames;
	private String countryCodes;
	private List<String> listOfElements;
	private String elementsData;

	public String getElementsData() {
		return this.elementsData;
	}

	public void setElementsData(String elementsData) {
		this.elementsData = elementsData;
	}

	public List<String> getListOfElements() {
		return this.listOfElements;
	}

	public void setListOfElements(List<String> listOfElements) {
		this.listOfElements = listOfElements;
	}

	public int getMappingId() {
		return this.mappingId;
	}

	public void setMappingId(int mappingId) {
		this.mappingId = mappingId;
	}

	public int getHistoryMappingId() {
		return this.historyMappingId;
	}

	public void setHistoryMappingId(int historyMappingId) {
		this.historyMappingId = historyMappingId;
	}

	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getRiskMappingName() {
		return this.riskMappingName;
	}

	public void setRiskMappingName(String riskMappingName) {
		this.riskMappingName = riskMappingName;
	}

	public int getMappingStatus() {
		return this.mappingStatus;
	}

	public void setMappingStatus(int mappingStatus) {
		this.mappingStatus = mappingStatus;
	}

	public int getRiskGroup() {
		return this.riskGroup;
	}

	public void setRiskGroup(int riskGroup) {
		this.riskGroup = riskGroup;
	}

	public String getSubjectType() {
		return this.subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public int getVisibleToClient() {
		return this.visibleToClient;
	}

	public void setVisibleToClient(int visibleToClient) {
		this.visibleToClient = visibleToClient;
	}

	public String getClientCodes() {
		return this.clientCodes;
	}

	public void setClientCodes(String clientCodes) {
		this.clientCodes = clientCodes;
	}

	public String getReportTypes() {
		return this.reportTypes;
	}

	public void setReportTypes(String reportTypes) {
		this.reportTypes = reportTypes;
	}

	public String getSubReportTypes() {
		return this.subReportTypes;
	}

	public void setSubReportTypes(String subReportTypes) {
		this.subReportTypes = subReportTypes;
	}

	public String getSubjectCountry() {
		return this.subjectCountry;
	}

	public void setSubjectCountry(String subjectCountry) {
		this.subjectCountry = subjectCountry;
	}

	public String getResearchElements() {
		return this.researchElements;
	}

	public void setResearchElements(String researchElements) {
		this.researchElements = researchElements;
	}

	public String getUpdatedBy() {
		return this.UpdatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.UpdatedBy = updatedBy;
	}

	public String getUpdatedOn() {
		return this.UpdatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.UpdatedOn = updatedOn;
	}

	public String getReportTypeNames() {
		return this.reportTypeNames;
	}

	public void setReportTypeNames(String reportTypeNames) {
		this.reportTypeNames = reportTypeNames;
	}

	public ArrayList<String> getSubReportTypeNames() {
		return this.SubReportTypeNames;
	}

	public void setSubReportTypeNames(ArrayList<String> subReportTypeNames) {
		this.SubReportTypeNames = subReportTypeNames;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountryCodes() {
		return this.countryCodes;
	}

	public void setCountryCodes(String countryCodes) {
		this.countryCodes = countryCodes;
	}
}