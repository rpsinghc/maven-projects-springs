package com.worldcheck.atlas.vo.document;

public class DisplayDocVO {
	private String id;
	private String folderName;
	private String fileNameURL;
	private String version;
	private String fileSize;
	private String dateUploded;
	private String teamOfAnalyst;
	private String owner;
	private String status;
	private boolean uploadedByJuno;
	private int officeID;
	private long dateUploadedTime;

	public long getDateUploadedTime() {
		return this.dateUploadedTime;
	}

	public void setDateUploadedTime(long dateUploadedTime) {
		this.dateUploadedTime = dateUploadedTime;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFolderName() {
		return this.folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getFileNameURL() {
		return this.fileNameURL;
	}

	public void setFileNameURL(String fileNameURL) {
		this.fileNameURL = fileNameURL;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getDateUploded() {
		return this.dateUploded;
	}

	public void setDateUploded(String dateUploded) {
		this.dateUploded = dateUploded;
	}

	public String getTeamOfAnalyst() {
		return this.teamOfAnalyst;
	}

	public void setTeamOfAnalyst(String teamOfAnalyst) {
		this.teamOfAnalyst = teamOfAnalyst;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean getUploadedByJuno() {
		return this.uploadedByJuno;
	}

	public void setUploadedByJuno(boolean uploadedByJuno) {
		this.uploadedByJuno = uploadedByJuno;
	}

	public int getOfficeID() {
		return this.officeID;
	}

	public void setOfficeID(int officeID) {
		this.officeID = officeID;
	}
}