package com.worldcheck.atlas.vo;

public class CommentDetails {
	private int commentId;
	private String CRN;
	private String comment;
	private String sentFrom;
	private String sentFromUserLoginId;
	private String sentTo;
	private String sentDate;
	private String updatedBy;
	private boolean isPerformedByBakup;

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSentFrom() {
		return this.sentFrom;
	}

	public void setSentFrom(String sentFrom) {
		this.sentFrom = sentFrom;
	}

	public String getSentFromUserLoginId() {
		return this.sentFromUserLoginId;
	}

	public void setSentFromUserLoginId(String sentFromUserLoginId) {
		this.sentFromUserLoginId = sentFromUserLoginId;
	}

	public String getSentTo() {
		return this.sentTo;
	}

	public void setSentTo(String sentTo) {
		this.sentTo = sentTo;
	}

	public String getSentDate() {
		return this.sentDate;
	}

	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}

	public int getCommentId() {
		return this.commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getCRN() {
		return this.CRN;
	}

	public void setCRN(String cRN) {
		this.CRN = cRN;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public boolean isPerformedByBakup() {
		return this.isPerformedByBakup;
	}

	public void setPerformedByBakup(boolean isPerformedByBakup) {
		this.isPerformedByBakup = isPerformedByBakup;
	}
}