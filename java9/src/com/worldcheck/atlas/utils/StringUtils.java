package com.worldcheck.atlas.utils;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StringUtils {
	private static ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.utils.StringUtils");

	public static List<String> commaSeparatedStringToList(String commaSeparateString) throws CMSException {
		ArrayList resultList = null;

		try {
			logger.debug("in commaSeparatedStringToList ..commaSeparateString :: " + commaSeparateString);
			resultList = new ArrayList();
			if (null != commaSeparateString && !commaSeparateString.equals("")) {
				String[] stringArray = commaSeparateString.split(",");
				logger.debug("stringArray length :: " + stringArray.length);

				for (int i = 0; i < stringArray.length; ++i) {
					resultList.add(stringArray[i]);
				}

				logger.debug("resultList size :: " + resultList.size());
			}

			return resultList;
		} catch (UnsupportedOperationException var4) {
			throw new CMSException(logger, var4);
		} catch (ClassCastException var5) {
			throw new CMSException(logger, var5);
		} catch (NullPointerException var6) {
			throw new CMSException(logger, var6);
		} catch (IllegalArgumentException var7) {
			throw new CMSException(logger, var7);
		}
	}

	public static List<Integer> commaSeparatedStringToIntList(String commaSeparateString) throws CMSException {
		ArrayList resultList = null;

		try {
			logger.debug("in commaSeparatedStringToIntList ..commaSeparateString :: " + commaSeparateString);
			resultList = new ArrayList();
			if (null != commaSeparateString && !commaSeparateString.equals("")) {
				String[] stringArray = commaSeparateString.split(",");
				logger.debug("stringArray length :: " + stringArray.length);

				for (int i = 0; i < stringArray.length; ++i) {
					resultList.add(Integer.parseInt(stringArray[i]));
				}

				logger.debug("resultList size :: " + resultList.size());
			}

			return resultList;
		} catch (UnsupportedOperationException var4) {
			throw new CMSException(logger, var4);
		} catch (ClassCastException var5) {
			throw new CMSException(logger, var5);
		} catch (NullPointerException var6) {
			throw new CMSException(logger, var6);
		} catch (NumberFormatException var7) {
			throw new CMSException(logger, var7);
		} catch (IllegalArgumentException var8) {
			throw new CMSException(logger, var8);
		}
	}

	public static String listToCommaSeparatedString(List<Integer> integerList) throws CMSException {
		StringBuffer resultString = new StringBuffer();

		try {
			logger.debug("in listToCommaSeparatedString ..integerList :: " + integerList);
			Iterator i$ = integerList.iterator();

			while (i$.hasNext()) {
				Integer integer = (Integer) i$.next();
				if (resultString.length() == 0) {
					resultString.append(integer.toString());
				} else {
					resultString.append("," + integer.toString());
				}
			}

			logger.debug("resultString :: " + resultString.toString());
			return resultString.toString();
		} catch (NullPointerException var4) {
			throw new CMSException(logger, var4);
		} catch (IllegalArgumentException var5) {
			throw new CMSException(logger, var5);
		}
	}

	public static String listToCommaSeparatedStringObject(List<String> stringList) throws CMSException {
		StringBuffer resultString = new StringBuffer();

		try {
			logger.debug("in listToCommaSeparatedStringObject ..stringList :: " + stringList);
			Iterator i$ = stringList.iterator();

			while (i$.hasNext()) {
				String stringObj = (String) i$.next();
				if (resultString.length() == 0) {
					resultString.append(stringObj);
				} else {
					resultString.append("," + stringObj);
				}
			}

			logger.debug("resultString :: " + resultString.toString());
			return resultString.toString();
		} catch (NullPointerException var4) {
			throw new CMSException(logger, var4);
		} catch (IllegalArgumentException var5) {
			throw new CMSException(logger, var5);
		}
	}

	public static float Round(float val, int place) {
		float p = (float) Math.pow(10.0D, (double) place);
		val *= p;
		float tmp = (float) Math.round(val);
		return tmp / p;
	}

	public static String[] hexCodeGeneratorForColors() {
		String[] returnColorsArray = getDistinctColorCodes();
		return returnColorsArray;
	}

	public static String checkPaginationParams(String paramName, String paramValue) {
		String retValue;
		if ("start".equals(paramName)) {
			if (null != paramValue && paramValue.trim().length() > 0) {
				retValue = paramValue;
			} else {
				logger.debug("Encountered NULL or Empty value in start parameter so defaulting it to 0");
				retValue = "0";
			}
		} else if ("limit".equals(paramName)) {
			if (null != paramValue && paramValue.trim().length() > 0) {
				retValue = paramValue;
			} else {
				logger.debug("Encountered NULL or Empty value in limit parameter so defaulting it to 10");
				retValue = "10";
			}
		} else {
			retValue = paramValue;
		}

		return retValue;
	}

	public static String[] getDistinctColorCodes() {
		String[] returnColorsArray = new String[]{"#CC0000", "#CCFF00", "#990099", "#FFFF00", "#FF6600", "#336600",
				"#330066", "#2EC7AE", "#614051", "#50C878", "#FF4F00", "#66FF00", "#003399", "#003300", "#6699FF",
				"#009900", "#FF00FF", "#8A2BE2", "#33CCFF", "#336699", "#3CDC27", "#669999", "#0000FF", "#C2B280",
				"#E80000", "#282828", "#0033FF", "#FF00CC", "#FF6666", "#483D8B", "#1E90FF", "#4B0082", "#C71585",
				"#DA70D6", "#993366", "#BC8F8F", "#40E0D0", "#6A5ACD", "#FA8072", "#EEE8AA", "#3CB371", "#9370D8",
				"#90EE90", "#CD5C5C", "#FFD700", "#D3D3D3", "#CC00FF", "#66FF66", "#6600CC", "#006699"};
		return returnColorsArray;
	}
}