package com.worldcheck.atlas.vo;

import java.util.List;

public class ExcelDownloadMultiTabVO {
	List<String> lstHeader;
	String sheetName;
	List<List<String>> dataList;

	public List<String> getLstHeader() {
		return this.lstHeader;
	}

	public void setLstHeader(List<String> lstHeader) {
		this.lstHeader = lstHeader;
	}

	public String getSheetName() {
		return this.sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public void setDataList(List<List<String>> dataList) {
		this.dataList = dataList;
	}

	public List<List<String>> getDataList() {
		return this.dataList;
	}
}