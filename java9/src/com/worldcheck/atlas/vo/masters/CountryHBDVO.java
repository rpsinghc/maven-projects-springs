package com.worldcheck.atlas.vo.masters;

public class CountryHBDVO {
	private long countryHBDID;
	private long profileId;
	private long attributeId;
	private String attributeValue;
	private long subjectId;
	private long countryMasterId;
	private String updatedBy;
	private String countryName;

	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public long getCountryHBDID() {
		return this.countryHBDID;
	}

	public void setCountryHBDID(long countryHBDID) {
		this.countryHBDID = countryHBDID;
	}

	public long getProfileId() {
		return this.profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	public long getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(long attributeId) {
		this.attributeId = attributeId;
	}

	public String getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public long getCountryMasterId() {
		return this.countryMasterId;
	}

	public void setCountryMasterId(long countryMasterId) {
		this.countryMasterId = countryMasterId;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}