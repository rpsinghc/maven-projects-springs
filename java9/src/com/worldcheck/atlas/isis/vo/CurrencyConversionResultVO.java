package com.worldcheck.atlas.isis.vo;

import java.util.Date;

public class CurrencyConversionResultVO {
	private int errorCode;
	private String errorMessage;
	private float amont;
	private Date date;

	public int getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public float getAmont() {
		return this.amont;
	}

	public void setAmont(float amont) {
		this.amont = amont;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}