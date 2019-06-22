package com.worldcheck.atlas.vo.masters;

public class VendorUploadMasterVO {
	private String vendorCode;
	private int vendorUploadId;
	private String vendorMasterId;
	private String uploadedBy;
	private boolean isNda;
	private String fileName;
	private String fileType;
	private String uploadedOn;
	private float fileSize;

	public void setVendorUploadId(int vendorUploadId) {
		this.vendorUploadId = vendorUploadId;
	}

	public int getVendorUploadId() {
		return this.vendorUploadId;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setIsNda(boolean isNda) {
		this.isNda = isNda;
	}

	public boolean getIsNda() {
		return this.isNda;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getUploadedBy() {
		return this.uploadedBy;
	}

	public void setVendorMasterId(String vendorMasterId) {
		this.vendorMasterId = vendorMasterId;
	}

	public String getVendorMasterId() {
		return this.vendorMasterId;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getVendorCode() {
		return this.vendorCode;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setUploadedOn(String uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	public String getUploadedOn() {
		return this.uploadedOn;
	}

	public void setFileSize(float fileSize) {
		this.fileSize = fileSize;
	}

	public float getFileSize() {
		return this.fileSize;
	}
}