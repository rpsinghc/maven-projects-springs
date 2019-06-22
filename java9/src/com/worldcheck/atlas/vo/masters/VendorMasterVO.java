package com.worldcheck.atlas.vo.masters;

import java.util.List;

public class VendorMasterVO {
	private String vMasterId;
	private int vendorMasterId;
	private String vendorCode;
	private String vendorName;
	private boolean allowAnalystToView;
	private String vendorType;
	private String vendorTypeId;
	private String vendorClassification;
	private String createdDate;
	private String contractExpiry;
	private String expertise;
	private String costs;
	private String comments;
	private String contactName;
	private String email1;
	private String email2;
	private String legacyVendorCode;
	private String phone1;
	private String phone2;
	private String fax;
	private String country;
	private String address;
	private String fileName;
	private String updatedBy;
	private String updatedOn;
	private String status;
	private String vendorStatusId;
	private String contractExpiryEnd;
	private String contractExpiryStart;
	private boolean isNDA;
	private String ndaExists;
	private String contractExists;
	private String uploadedBy;
	private String uploadTime;
	private int principleCountryId;
	private List<Integer> vendorCountryList;

	public void setVMasterId(String vMasterId) {
		this.vMasterId = vMasterId;
	}

	public String getVMasterId() {
		return this.vMasterId;
	}

	public String getVendorTypeId() {
		return this.vendorTypeId;
	}

	public void setVendorTypeId(String vendorTypeId) {
		this.vendorTypeId = vendorTypeId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getVendorMasterId() {
		return this.vendorMasterId;
	}

	public void setVendorMasterId(int vendorMasterId) {
		this.vendorMasterId = vendorMasterId;
	}

	public int getPrincipleCountryId() {
		return this.principleCountryId;
	}

	public void setPrincipleCountryId(int principalCountryId) {
		this.principleCountryId = principalCountryId;
	}

	public String getVendorCode() {
		return this.vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getVendorName() {
		return this.vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public boolean getAllowAnalystToView() {
		return this.allowAnalystToView;
	}

	public void setAllowAnalystToView(boolean allowAnalystToView) {
		this.allowAnalystToView = allowAnalystToView;
	}

	public String getVendorType() {
		return this.vendorType;
	}

	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	public String getVendorClassification() {
		return this.vendorClassification;
	}

	public void setVendorClassification(String vendorClassification) {
		this.vendorClassification = vendorClassification;
	}

	public String getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getContractExpiry() {
		return this.contractExpiry;
	}

	public void setContractExpiry(String contractExpiry) {
		this.contractExpiry = contractExpiry;
	}

	public String getExpertise() {
		return this.expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}

	public String getCosts() {
		return this.costs;
	}

	public void setCosts(String costs) {
		this.costs = costs;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getEmail1() {
		return this.email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return this.email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getLegacyVendorCode() {
		return this.legacyVendorCode;
	}

	public void setLegacyVendorCode(String legacyVendorCode) {
		this.legacyVendorCode = legacyVendorCode;
	}

	public String getPhone1() {
		return this.phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return this.phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVendorStatusId() {
		return this.vendorStatusId;
	}

	public void setVendorStatusId(String vendorStatusId) {
		this.vendorStatusId = vendorStatusId;
	}

	public String getContractExpiryEnd() {
		return this.contractExpiryEnd;
	}

	public void setContractExpiryEnd(String contractExpiryEnd) {
		this.contractExpiryEnd = contractExpiryEnd;
	}

	public String getContractExpiryStart() {
		return this.contractExpiryStart;
	}

	public void setContractExpiryStart(String contractExpiryStart) {
		this.contractExpiryStart = contractExpiryStart;
	}

	public boolean getIsNDA() {
		return this.isNDA;
	}

	public void setIsNDA(boolean isNDA) {
		this.isNDA = isNDA;
	}

	public void setVendorCountryList(List<Integer> vendorCountryList) {
		this.vendorCountryList = vendorCountryList;
	}

	public List<Integer> getVendorCountryList() {
		return this.vendorCountryList;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getUploadedBy() {
		return this.uploadedBy;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getUploadTime() {
		return this.uploadTime;
	}

	public void setNdaExists(String ndaExists) {
		this.ndaExists = ndaExists;
	}

	public String getNdaExists() {
		return this.ndaExists;
	}

	public void setContractExists(String contractExists) {
		this.contractExists = contractExists;
	}

	public String getContractExists() {
		return this.contractExists;
	}
}