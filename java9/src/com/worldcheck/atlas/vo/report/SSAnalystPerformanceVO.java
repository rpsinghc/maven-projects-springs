package com.worldcheck.atlas.vo.report;

public class SSAnalystPerformanceVO {
	private String analystName;
	private String rating;
	private int numberOfreports;
	private float maxPoints;
	private float score;
	private float quality;

	public String getAnalystName() {
		return this.analystName;
	}

	public void setAnalystName(String analystName) {
		this.analystName = analystName;
	}

	public String getRating() {
		return this.rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public int getNumberOfreports() {
		return this.numberOfreports;
	}

	public void setNumberOfreports(int numberOfreports) {
		this.numberOfreports = numberOfreports;
	}

	public float getMaxPoints() {
		return this.maxPoints;
	}

	public void setMaxPoints(float maxPoints) {
		this.maxPoints = maxPoints;
	}

	public float getScore() {
		return this.score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getQuality() {
		return this.quality;
	}

	public void setQuality(float quality) {
		this.quality = quality;
	}
}