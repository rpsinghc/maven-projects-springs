package com.worldcheck.atlas.vo.masters;

import java.util.HashMap;
import java.util.List;

public class CurrencyMasterVO {
	private String startDate;
	private String endDate;
	private String USD;
	private String INR;
	private String SGD;
	private String HKD;
	private String GBP;
	private String RMB;
	private String EUR;
	private String AED;
	private String updateBy;
	private String updatedOn;
	private List<CurrencyMasterVO> currencyConversionList;
	private String[] modifiedRecords;
	private String currencyCode;
	private String currency;
	private String currencyShortForm;
	private String currencyStatus;
	private String exchangeRate;
	private long currencyRateId;
	private Integer start;
	private Integer limit;
	private String sortColumnName;
	private String acCount;
	private String tocptownCount;
	private String vendorCount;
	private String noncrnCount;
	private String recCaseCount;
	private String sortType;
	private List<HashMap<String, String>> currencyConversionHashMapList;

	public String getRecCaseCount() {
		return this.recCaseCount;
	}

	public void setRecCaseCount(String recCaseCount) {
		this.recCaseCount = recCaseCount;
	}

	public String getAcCount() {
		return this.acCount;
	}

	public void setAcCount(String acCount) {
		this.acCount = acCount;
	}

	public String getTocptownCount() {
		return this.tocptownCount;
	}

	public void setTocptownCount(String tocptownCount) {
		this.tocptownCount = tocptownCount;
	}

	public String getVendorCount() {
		return this.vendorCount;
	}

	public void setVendorCount(String vendorCount) {
		this.vendorCount = vendorCount;
	}

	public String getNoncrnCount() {
		return this.noncrnCount;
	}

	public void setNoncrnCount(String noncrnCount) {
		this.noncrnCount = noncrnCount;
	}

	public String getSortColumnName() {
		return this.sortColumnName;
	}

	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}

	public String getSortType() {
		return this.sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public Integer getStart() {
		return this.start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return this.limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public long getCurrencyRateId() {
		return this.currencyRateId;
	}

	public void setCurrencyRateId(long currencyRateId) {
		this.currencyRateId = currencyRateId;
	}

	public String getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencyShortForm() {
		return this.currencyShortForm;
	}

	public void setCurrencyShortForm(String currencyShortForm) {
		this.currencyShortForm = currencyShortForm;
	}

	public String getCurrencyStatus() {
		return this.currencyStatus;
	}

	public void setCurrencyStatus(String currencyStatus) {
		this.currencyStatus = currencyStatus;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getUSD() {
		return this.USD;
	}

	public void setUSD(String usd) {
		this.USD = usd;
	}

	public String getINR() {
		return this.INR;
	}

	public void setINR(String inr) {
		this.INR = inr;
	}

	public String getSGD() {
		return this.SGD;
	}

	public void setSGD(String sgd) {
		this.SGD = sgd;
	}

	public String getHKD() {
		return this.HKD;
	}

	public void setHKD(String hkd) {
		this.HKD = hkd;
	}

	public String getGBP() {
		return this.GBP;
	}

	public void setGBP(String gbp) {
		this.GBP = gbp;
	}

	public String getRMB() {
		return this.RMB;
	}

	public void setRMB(String rmb) {
		this.RMB = rmb;
	}

	public String getEUR() {
		return this.EUR;
	}

	public void setEUR(String eur) {
		this.EUR = eur;
	}

	public String getAED() {
		return this.AED;
	}

	public void setAED(String aED) {
		this.AED = aED;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public List<CurrencyMasterVO> getCurrencyConversionList() {
		return this.currencyConversionList;
	}

	public void setCurrencyConversionList(List<CurrencyMasterVO> currencyConversionList) {
		this.currencyConversionList = currencyConversionList;
	}

	public String[] getModifiedRecords() {
		return this.modifiedRecords;
	}

	public void setModifiedRecords(String[] modifiedRecords) {
		this.modifiedRecords = modifiedRecords;
	}

	public List<HashMap<String, String>> getCurrencyConversionHashMapList() {
		return this.currencyConversionHashMapList;
	}

	public void setCurrencyConversionHashMapList(List<HashMap<String, String>> currencyConversionHashMapList) {
		this.currencyConversionHashMapList = currencyConversionHashMapList;
	}
}