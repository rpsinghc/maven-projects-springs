package com.worldcheck.atlas.vo.document;

public class DocMapVO {
	private String names;
	private String docId;
	private long version;
	private String pid;
	private long marked;
	private long uploadedByJuno;
	private String teamName;

	public String getDocId() {
		return this.docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getNames() {
		return this.names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public long getVersion() {
		return this.version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTeamName() {
		return this.teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public long getMarked() {
		return this.marked;
	}

	public void setMarked(long marked) {
		this.marked = marked;
	}

	public long getUploadedByJuno() {
		return this.uploadedByJuno;
	}

	public void setUploadedByJuno(long uploadedByJuno) {
		this.uploadedByJuno = uploadedByJuno;
	}
}