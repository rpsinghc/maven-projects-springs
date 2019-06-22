package com.worldcheck.atlas.vo.currencyconverter;

import java.util.Date;

public class CurrencyConverterVO {
	private float USDConversionRate;
	private float INRConversionRate;
	private float SGDConversionRate;
	private float AEDConversionRate;
	private float HKDConversionRate;
	private float GBPConversionRate;
	private float RMBConversionRate;
	private float EURConversionRate;
	private Date updatedOn;
	private String updatedBy;
	private Date date;

	public float getUSDConversionRate() {
		return this.USDConversionRate;
	}

	public void setUSDConversionRate(float conversionRate) {
		this.USDConversionRate = conversionRate;
	}

	public float getINRConversionRate() {
		return this.INRConversionRate;
	}

	public void setINRConversionRate(float conversionRate) {
		this.INRConversionRate = conversionRate;
	}

	public float getSGDConversionRate() {
		return this.SGDConversionRate;
	}

	public void setSGDConversionRate(float conversionRate) {
		this.SGDConversionRate = conversionRate;
	}

	public float getAEDConversionRate() {
		return this.AEDConversionRate;
	}

	public void setAEDConversionRate(float conversionRate) {
		this.AEDConversionRate = conversionRate;
	}

	public float getHKDConversionRate() {
		return this.HKDConversionRate;
	}

	public void setHKDConversionRate(float conversionRate) {
		this.HKDConversionRate = conversionRate;
	}

	public float getGBPConversionRate() {
		return this.GBPConversionRate;
	}

	public void setGBPConversionRate(float conversionRate) {
		this.GBPConversionRate = conversionRate;
	}

	public float getRMBConversionRate() {
		return this.RMBConversionRate;
	}

	public void setRMBConversionRate(float conversionRate) {
		this.RMBConversionRate = conversionRate;
	}

	public float getEURConversionRate() {
		return this.EURConversionRate;
	}

	public void setEURConversionRate(float conversionRate) {
		this.EURConversionRate = conversionRate;
	}

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}