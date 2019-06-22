package com.worldcheck.atlas.utils;

import java.util.Comparator;

public class CRNCompare implements Comparator<String> {
	public int compare(String crn1, String crn2) {
		String[] temp1 = crn1.split("::")[0].split("\\\\");
		int int1 = Integer.parseInt(temp1[3]);
		String[] temp2 = crn2.split("::")[0].split("\\\\");
		int int2 = Integer.parseInt(temp2[3]);
		if (int1 > int2) {
			return 1;
		} else {
			return int1 == int2 ? crn1.split("::")[0].compareTo(crn2.split("::")[0]) : -1;
		}
	}
}