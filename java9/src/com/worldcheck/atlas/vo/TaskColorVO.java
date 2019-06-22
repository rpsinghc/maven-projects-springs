package com.worldcheck.atlas.vo;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;

public class TaskColorVO {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.vo.TaskColorVO");
	private int colorId;
	private String crn;
	private String taskName;
	private String taskStatus;
	private String color;

	public String getCrn() {
		return this.crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setColorId(int colorId) {
		this.colorId = colorId;
	}

	public int getColorId() {
		return this.colorId;
	}
}