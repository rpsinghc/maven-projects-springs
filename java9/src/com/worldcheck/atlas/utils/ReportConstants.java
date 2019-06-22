package com.worldcheck.atlas.utils;

import java.util.LinkedHashMap;

public class ReportConstants {
	public static LinkedHashMap<String, Integer> monthMap = null;
	public static LinkedHashMap<String, Integer> fullmonthMap;
	public static LinkedHashMap<String, Integer> fiscalYearFullMonthMap;

	static {
		monthMap = new LinkedHashMap();
		monthMap.put("Jan", 1);
		monthMap.put("Feb", 2);
		monthMap.put("Mar", 3);
		monthMap.put("Apr", 4);
		monthMap.put("May", 5);
		monthMap.put("Jun", 6);
		monthMap.put("Jul", 7);
		monthMap.put("Aug", 8);
		monthMap.put("Sep", 9);
		monthMap.put("Oct", 10);
		monthMap.put("Nov", 11);
		monthMap.put("Dec", 12);
		fullmonthMap = null;
		fullmonthMap = new LinkedHashMap();
		fullmonthMap.put("January", 1);
		fullmonthMap.put("February", 2);
		fullmonthMap.put("March", 3);
		fullmonthMap.put("April", 4);
		fullmonthMap.put("May", 5);
		fullmonthMap.put("June", 6);
		fullmonthMap.put("July", 7);
		fullmonthMap.put("August", 8);
		fullmonthMap.put("September", 9);
		fullmonthMap.put("October", 10);
		fullmonthMap.put("November", 11);
		fullmonthMap.put("December", 12);
		fiscalYearFullMonthMap = null;
		fiscalYearFullMonthMap = new LinkedHashMap();
		fiscalYearFullMonthMap.put("January", 1);
		fiscalYearFullMonthMap.put("February", 2);
		fiscalYearFullMonthMap.put("March", 3);
		fiscalYearFullMonthMap.put("April", 4);
		fiscalYearFullMonthMap.put("May", 5);
		fiscalYearFullMonthMap.put("June", 6);
		fiscalYearFullMonthMap.put("July", 7);
		fiscalYearFullMonthMap.put("August", 8);
		fiscalYearFullMonthMap.put("September", 9);
		fiscalYearFullMonthMap.put("October", 10);
		fiscalYearFullMonthMap.put("November", 11);
		fiscalYearFullMonthMap.put("December", 12);
	}
}