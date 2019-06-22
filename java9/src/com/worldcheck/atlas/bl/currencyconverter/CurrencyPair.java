package com.worldcheck.atlas.bl.currencyconverter;

public class CurrencyPair {
	protected String from;
	protected String to;
	protected int pips;
	protected float price;

	public CurrencyPair(String from, String to, int pips) {
		this.from = from;
		this.to = to;
		this.pips = pips;
	}

	public CurrencyPair(String from, String to) {
		this(from, to, 0);
	}

	public CurrencyPair(String fromToPips) {
		this(fromToPips.substring(0, 3), fromToPips.substring(3, 6));
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return this.to;
	}

	public void setTo(String to) {
		this.to = to;
	}
}