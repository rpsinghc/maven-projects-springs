package com.worldcheck.atlas.isis.vo;

import com.integrascreen.orders.ResponseVO;

public class ISISResponseVO {
	private boolean isSuccess;
	private ResponseVO responseVO;

	public boolean isSuccess() {
		return this.isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public ResponseVO getResponseVO() {
		return this.responseVO;
	}

	public void setResponseVO(ResponseVO responseVO) {
		this.responseVO = responseVO;
	}
}