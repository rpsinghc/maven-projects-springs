package com.worldcheck.atlas.utils;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.vo.ExcelDownloadMultiTabVO;
import java.security.SecureRandom;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.servlet.ModelAndView;

public class AtlasUtils {
	private static String ERROR_CODE = "errorCode";
	private static String JSP = "atlasErrorPage";

	public static ExcelDownloadMultiTabVO getExcelVO(List<String> lstHeader, List<List<String>> dataList,
			String sheetName) {
		ExcelDownloadMultiTabVO excelDownloadMultiTabVO = new ExcelDownloadMultiTabVO();
		excelDownloadMultiTabVO.setDataList(dataList);
		excelDownloadMultiTabVO.setLstHeader(lstHeader);
		excelDownloadMultiTabVO.setSheetName(sheetName);
		return excelDownloadMultiTabVO;
	}

	public static ModelAndView getExceptionView(ILogProducer logger, Exception e) {
		CMSException cmsException = null;
		ModelAndView mvForException = null;
		if (e instanceof CMSException) {
			cmsException = (CMSException) e;
		} else {
			cmsException = new CMSException(logger, e);
		}

		if (cmsException.getCause() != null) {
			mvForException = new ModelAndView("redirect:/bpmportal/myhome/dateErrorPage.jsp");
		} else {
			mvForException = new ModelAndView(JSP);
		}

		mvForException.addObject(ERROR_CODE, cmsException.getErrorCode());
		return mvForException;
	}

	public static ModelAndView getJsonExceptionView(ILogProducer logger, Exception e, HttpServletResponse response) {
		if (!(e instanceof CMSException)) {
			new CMSException(logger, e);
		}

		ModelAndView mvForException = new ModelAndView("jsonView");
		response.setStatus(500);
		return mvForException;
	}

	public static ModelAndView getSessionExceptionView(ILogProducer logger, Exception e, HttpServletResponse response) {
		if (!(e instanceof CMSException)) {
			new CMSException(logger, e);
		}

		ModelAndView mvForException = new ModelAndView("jsonView");
		response.setStatus(900);
		return mvForException;
	}

	public static String generatePassword() {
		String credential = "";
		char[] possibleSpecialCharacter = (new String("!@#$^&*")).toCharArray();
		String randomSpcChStr = RandomStringUtils.random(2, 0, possibleSpecialCharacter.length - 1, false, false,
				possibleSpecialCharacter, new SecureRandom());
		char[] possibleNumericCharacters = (new String("0123456789")).toCharArray();
		String randomNumStr = RandomStringUtils.random(2, 0, possibleNumericCharacters.length - 1, false, false,
				possibleNumericCharacters, new SecureRandom());
		char[] possibleCapitalAlphaCharacters = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ")).toCharArray();
		String randomCapStr = RandomStringUtils.random(2, 0, possibleCapitalAlphaCharacters.length - 1, false, false,
				possibleCapitalAlphaCharacters, new SecureRandom());
		char[] possibleSmallAlphaCharacters = (new String("abcdefghijklmnopqrstuvwxyz")).toCharArray();
		String randomSmallStr = RandomStringUtils.random(2, 0, possibleSmallAlphaCharacters.length - 1, false, false,
				possibleSmallAlphaCharacters, new SecureRandom());
		credential = credential + randomCapStr + randomSmallStr + randomSpcChStr + randomNumStr;
		return credential;
	}
}