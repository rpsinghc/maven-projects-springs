package com.worldcheck.atlas.vo;

import java.util.List;

public class MaterializedViewVO {
	private String mview_name;
	private String startTime;
	private String endTime;
	private String fullrefreshtime;
	private String increfreshtime;
	private List<String> mvList;

	public List<String> getMvList() {
		return this.mvList;
	}

	public void setMvList(List<String> mvList) {
		this.mvList = mvList;
	}

	public String getMview_name() {
		return this.mview_name;
	}

	public void setMview_name(String mviewName) {
		this.mview_name = mviewName;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getFullrefreshtime() {
		return this.fullrefreshtime;
	}

	public void setFullrefreshtime(String fullrefreshtime) {
		this.fullrefreshtime = fullrefreshtime;
	}

	public String getIncrefreshtime() {
		return this.increfreshtime;
	}

	public void setIncrefreshtime(String increfreshtime) {
		this.increfreshtime = increfreshtime;
	}
}