package com.worldcheck.atlas.sbm.util;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

public class BizSoloTagStringUtil {
	private static ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.sbm.util.BizSoloTagStringUtil");

	public static String getDisplayTag(String stringValue) {
		String newString = stringValue;

		try {
			if (null != stringValue && !"".equalsIgnoreCase(stringValue.trim())) {
				newString = stringValue.replaceAll("\\\\", "\\\\\\\\");
				String doubleQuote = Matcher.quoteReplacement("\"");
				newString = newString.replaceAll(doubleQuote, "\\\\\"");
				String newline = Matcher.quoteReplacement("\n");
				newString = newString.replaceAll(newline, "\\\\n");
				String carriageReturn = Matcher.quoteReplacement("\r");
				newString = newString.replaceAll(carriageReturn, "\\\\r");
				String apostrophesChar = Matcher.quoteReplacement("'");
				newString = newString.replaceAll(apostrophesChar, "\\\\'");
			} else {
				newString = new String("");
			}
		} catch (PatternSyntaxException var6) {
			;
		}

		return newString;
	}

	public static String getTaskName(String inputVal) {
		logger.debug("inside getTask Massage");
		String[] temp = null;
		String tempVal = "";
		if (null != inputVal && !"".equalsIgnoreCase(inputVal)) {
			temp = inputVal.split("::");
			if (temp.length > 2) {
				tempVal = temp[1];
			} else {
				tempVal = "";
			}
		}

		return tempVal;
	}
}