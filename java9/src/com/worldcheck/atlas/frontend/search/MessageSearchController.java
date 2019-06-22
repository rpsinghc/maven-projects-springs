package com.worldcheck.atlas.frontend.search;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.search.MessageSearchManager;
import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.bl.search.SearchFactory;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.notification.NotificationVO;
import com.worldcheck.atlas.vo.search.MessageSearchVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class MessageSearchController extends MultiActionController {
	private static final String SEARCH_TYPE = "searchType";
	private static final String RECURRENCE_TOKEN = "recurrence";
	private static final String EXCEL_GENERATION_TOKEN = "excelGeneration";
	private static final String REC_STR_TOKEN = "rec";
	private static final int NUMBER_OF_ROWS_INEXCEL_VAL = 4760;
	private static final String CRN_NUMBERS_STR_CONST = "crnNumbers";
	private static final String PART_STR_TOKEN = "Part";
	private static final String NORMAL_STR_TOKEN = "normal";
	private static final String LOGIN_USER = "loginUser";
	private static final String SESSION_ID = "sessionID";
	private static final String MESSAGE_DATA = "messageSearchData";
	private static final String EXCEL_FILE_NAME = "Message Search";
	private static final String CRN = "Case Reference Number";
	private static final String TEXT = "Text";
	private static final String SENT_FROM = "Sent From";
	private static final String SENT_AT = "Sent At";
	private static final String SENT_TO = "Sent To";
	private static final String ACKNOWLEDGED = "Acknowledged?";
	private static final Object intSyncUp = new Object();
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.search.MessageSearchController");
	SearchFactory searchFactory = null;

	public void setSearchFactory(SearchFactory searchFactory) {
		this.searchFactory = searchFactory;
	}

	public ModelAndView messageSearch(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			SearchCriteria searchCriteria = new SearchCriteria();
			if (null != request.getParameter("msgTxtMessageSearch")) {
				this.logger.debug("message text " + request.getParameter("msgTxtMessageSearch"));
				searchCriteria.setMessageText(request.getParameter("msgTxtMessageSearch"));
			}

			modelAndView = new ModelAndView("mesgSearch");
			modelAndView.addObject("messageDetails", searchCriteria);
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView setupAdvanceMessageSearch(HttpServletRequest request, HttpServletResponse response,
			Object command) throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("mesgSearch");
			modelAndView.addObject("isAdvanceSearch", "true");
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView exportToExcelMessageSearch(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) {
		this.logger.debug("in export to excel for message search");
		ModelAndView modelandview = null;
		if (null == request.getSession().getAttribute("sessionID")
				|| "".equalsIgnoreCase((String) request.getSession().getAttribute("sessionID"))) {
			request.getSession().setAttribute("sessionID", request.getSession().getId());

			ModelAndView var6;
			try {
				Object var5 = intSyncUp;
				synchronized (intSyncUp) {
					UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
					this.logger.debug("for message search user is " + userBean.getUserName());
					String excelParams = request.getParameter("excelParams");
					Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
					excelParamMap.put("crnNumbers", StringUtils.commaSeparatedStringToList(searchCriteria.getCrnNo()));
					this.logger.debug("parameter for message search " + excelParamMap);
					MessageSearchManager messageSearch = (MessageSearchManager) this.searchFactory
							.getSearchImpl("MessageSearch");
					excelParamMap.put("loginUser", userBean.getUserName());
					List<MessageSearchVO> messageSearchList = messageSearch.searchForExport(excelParamMap);
					this.logger.debug("number of records to export for message search " + messageSearchList.size());
					Map<String, Object> resultMap = this.writeToExcel(messageSearchList, response);
					modelandview = new ModelAndView("excelDownloadPopup");
					modelandview.addObject("fileBytes", resultMap.get("fileBytes"));
					modelandview.addObject("fileName", resultMap.get("fileName"));
					this.logger.debug("user " + userBean.getUserName() + "'s request has been processed");
					break label25;
				}
			} catch (CMSException var19) {
				var6 = AtlasUtils.getExceptionView(this.logger, var19);
			} catch (Exception var20) {
				var6 = AtlasUtils.getExceptionView(this.logger, var20);
				return var6;
			} finally {
				request.getSession().removeAttribute("sessionID");
			}

			return var6;
		}

		this.logger.debug("before returning in export to excel for message search");
		return modelandview;
	}

	private Map<String, Object> writeToExcel(List<MessageSearchVO> resultList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  write To Excel for message search ");
		List<String> lstHeader = this.getHeaderListNormal();
		List<LinkedHashMap<String, String>> dataList = new ArrayList();
		MessageSearchVO messageSearchVO = null;

		try {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				messageSearchVO = (MessageSearchVO) iterator.next();
				this.populateDataMap(messageSearchVO, datamap);
				dataList.add(datamap);
			}
		} catch (UnsupportedOperationException var8) {
			throw new CMSException(this.logger, var8);
		} catch (ClassCastException var9) {
			throw new CMSException(this.logger, var9);
		} catch (NullPointerException var10) {
			throw new CMSException(this.logger, var10);
		} catch (IllegalArgumentException var11) {
			throw new CMSException(this.logger, var11);
		}

		return ExcelDownloader.writeToExcel(lstHeader, dataList, "messageSearchData", (short) 0, (short) 1, response,
				"Message Search");
	}

	public ModelAndView writeToZipMessageSearch(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) {
		String zipFile = "";
		this.logger.debug("in export to excel for message search(zip)");
		if (null == request.getSession().getAttribute("sessionID")
				|| "".equalsIgnoreCase((String) request.getSession().getAttribute("sessionID"))) {
			request.getSession().setAttribute("sessionID", request.getSession().getId());

			label133 : {
				ModelAndView var6;
				try {
					Object var5 = intSyncUp;
					synchronized (intSyncUp) {
						List<List<MessageSearchVO>> partedList = new ArrayList();
						UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
						this.logger.debug("for message search(zip) user is " + userBean.getUserName());
						String excelParams = request.getParameter("excelParams");
						Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
						excelParamMap.put("crnNumbers",
								StringUtils.commaSeparatedStringToList(searchCriteria.getCrnNo()));
						this.logger.debug("parameter for message search " + excelParamMap);
						MessageSearchManager messageSearch = (MessageSearchManager) this.searchFactory
								.getSearchImpl("MessageSearch");
						excelParamMap.put("loginUser", userBean.getUserName());
						List<MessageSearchVO> messageSearchList = messageSearch.searchForExport(excelParamMap);
						this.logger.debug("number of records to export for message search " + messageSearchList.size());
						int rowsAccomodated = 4760;
						List<String> fileNamesList = new ArrayList();
						int N = messageSearchList.size();
						int L = rowsAccomodated;

						int i;
						for (i = 0; i < N; i += L) {
							partedList.add(partedList.size(), messageSearchList.subList(i, Math.min(N, i + L)));
						}

						for (i = 0; i < partedList.size(); ++i) {
							String fileName = this.writeToMulipleExcel((List) partedList.get(i), response,
									"Part" + (i + 1), request, "normal");
							fileNamesList.add(fileName);
						}

						zipFile = ExcelDownloader.generateZipFile(fileNamesList, "Message Search", response);
						this.logger.debug("user " + userBean.getUserName() + "'s request has been processed");
						break label133;
					}
				} catch (CMSException var27) {
					var6 = AtlasUtils.getExceptionView(this.logger, var27);
				} catch (Exception var28) {
					var6 = AtlasUtils.getExceptionView(this.logger, var28);
					return var6;
				} finally {
					request.getSession().removeAttribute("sessionID");
				}

				return var6;
			}

			try {
				ExcelDownloader.openZipFile(zipFile, response);
			} catch (Exception var25) {
				return AtlasUtils.getExceptionView(this.logger, var25);
			}
		}

		this.logger.debug("before returning in export to excel for message search(zip)");
		return null;
	}

	public ModelAndView writeToZipMessageSearchRec(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) {
		String zipFile = "";
		this.logger.debug("in export to excel for message rec search(zip)");
		if (null == request.getSession().getAttribute("sessionID")
				|| "".equalsIgnoreCase((String) request.getSession().getAttribute("sessionID"))) {
			request.getSession().setAttribute("sessionID", request.getSession().getId());

			label133 : {
				ModelAndView var6;
				try {
					Object var5 = intSyncUp;
					synchronized (intSyncUp) {
						List<List<MessageSearchVO>> partedList = new ArrayList();
						UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
						this.logger.debug("for rec message search(zip) user is " + userBean.getUserName());
						String excelParams = request.getParameter("excelParams");
						Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
						this.logger.debug("parameter for case search " + excelParamMap);
						excelParamMap.put("crnNumbers",
								StringUtils.commaSeparatedStringToList(searchCriteria.getCrnNo()));
						excelParamMap.put("searchType", "recurrence");
						excelParamMap.put("loginUser", userBean.getUserName());
						MessageSearchManager messageSearchManager = (MessageSearchManager) this.searchFactory
								.getSearchImpl("MessageSearch");
						List<MessageSearchVO> messageSearchList = messageSearchManager
								.searchForExportRec(excelParamMap);
						this.logger.debug("number of records to export for recurrence message search "
								+ messageSearchList.size());
						int rowsAccomodated = 4760;
						List<String> fileNamesList = new ArrayList();
						int N = messageSearchList.size();
						int L = rowsAccomodated;

						int i;
						for (i = 0; i < N; i += L) {
							partedList.add(partedList.size(), messageSearchList.subList(i, Math.min(N, i + L)));
						}

						for (i = 0; i < partedList.size(); ++i) {
							String fileName = this.writeToMulipleExcel((List) partedList.get(i), response,
									"Part" + (i + 1), request, "rec");
							fileNamesList.add(fileName);
						}

						zipFile = ExcelDownloader.generateZipFile(fileNamesList, "Message Search", response);
						this.logger.debug("user " + userBean.getUserName() + "'s request has been processed");
						break label133;
					}
				} catch (CMSException var27) {
					var6 = AtlasUtils.getExceptionView(this.logger, var27);
					return var6;
				} catch (Exception var28) {
					var6 = AtlasUtils.getExceptionView(this.logger, var28);
				} finally {
					request.getSession().removeAttribute("sessionID");
				}

				return var6;
			}

			try {
				ExcelDownloader.openZipFile(zipFile, response);
			} catch (Exception var25) {
				return AtlasUtils.getExceptionView(this.logger, var25);
			}
		}

		this.logger.debug("before returning in export to excel for message rec search(zip)");
		return null;
	}

	private String writeToMulipleExcel(List<MessageSearchVO> resultList, HttpServletResponse response, String partName,
			HttpServletRequest request, String type) throws CMSException {
		String var9;
		try {
			request.getSession().setAttribute("excelGeneration", new Date());
			List<String> lstHeader = null;
			List<List<String>> dataList = null;
			if ("normal".equalsIgnoreCase(type)) {
				lstHeader = this.getHeaderListNormal();
				dataList = this.populateDataMapForZip(resultList);
			} else {
				lstHeader = this.getHeaderListRec();
				dataList = this.populateDataMapForZipRec(resultList);
			}

			String fileName = ExcelDownloader.writeToExcel1(lstHeader, dataList, "Message Search" + partName, (short) 0,
					(short) 1, response, "Message Search" + partName);
			var9 = fileName;
		} finally {
			request.getSession().removeAttribute("excelGeneration");
		}

		return var9;
	}

	private void populateDataMap(MessageSearchVO messageSearchVO, LinkedHashMap<String, String> datamap) {
		datamap.put("Case Reference Number", String.valueOf(messageSearchVO.getCrnNo()));
		String clobValue = messageSearchVO.getDescription();
		if (clobValue != null && !clobValue.trim().equals("") && !clobValue.isEmpty() && clobValue.length() > 5000) {
			clobValue = clobValue.substring(0, 5000);
			clobValue = clobValue + "...";
		}

		datamap.put("Text", clobValue);
		datamap.put("Sent From", String.valueOf(messageSearchVO.getSenderFullName()));
		datamap.put("Sent At", String.valueOf(messageSearchVO.getRecvTime()));
		String clobValue1 = messageSearchVO.getRecipientFullName();
		if (clobValue1 != null && !clobValue1.trim().equals("") && !clobValue1.isEmpty()
				&& clobValue1.length() > 5000) {
			clobValue1 = clobValue1.substring(0, 5000);
			clobValue1 = clobValue1 + "...";
		}

		datamap.put("Sent To", clobValue1);
		String clobValue2 = messageSearchVO.getAckDate();
		if (clobValue2 != null && !clobValue2.trim().equals("") && !clobValue2.isEmpty()
				&& clobValue2.length() > 5000) {
			clobValue2 = clobValue2.substring(0, 5000);
			clobValue2 = clobValue2 + "...";
		}

		datamap.put("Acknowledged?", clobValue2);
	}

	private List<List<String>> populateDataMapForZip(List<MessageSearchVO> resultList) {
		List<List<String>> dataList = new ArrayList();
		Iterator iterator = resultList.iterator();

		while (iterator.hasNext()) {
			List<String> datamap = new ArrayList();
			MessageSearchVO messageSearchVO = (MessageSearchVO) iterator.next();
			datamap.add(String.valueOf(messageSearchVO.getCrnNo()));
			String clobValue = messageSearchVO.getDescription();
			if (clobValue != null && !clobValue.trim().equals("") && clobValue.length() > 5000) {
				clobValue = clobValue.substring(0, 5000);
				clobValue = clobValue + "...";
			}

			datamap.add(clobValue);
			datamap.add(String.valueOf(messageSearchVO.getSenderFullName()));
			datamap.add(String.valueOf(messageSearchVO.getRecvTime()));
			String clobValue1 = messageSearchVO.getRecipientFullName();
			if (clobValue1 != null && !clobValue1.trim().equals("") && !clobValue1.isEmpty()
					&& clobValue1.length() > 5000) {
				clobValue1 = clobValue1.substring(0, 5000);
				clobValue1 = clobValue1 + "...";
			}

			datamap.add(clobValue1);
			String clobValue2 = messageSearchVO.getAckDate();
			if (clobValue2 != null && !clobValue2.trim().equals("") && !clobValue2.isEmpty()
					&& clobValue2.length() > 5000) {
				clobValue2 = clobValue2.substring(0, 5000);
				clobValue2 = clobValue2 + "...";
			}

			datamap.add(clobValue2);
			dataList.add(datamap);
		}

		return dataList;
	}

	private List<List<String>> populateDataMapForZipRec(List<MessageSearchVO> resultList) {
		List<List<String>> dataList = new ArrayList();
		Iterator iterator = resultList.iterator();

		while (iterator.hasNext()) {
			List<String> datamap = new ArrayList();
			MessageSearchVO messageSearchVO = (MessageSearchVO) iterator.next();
			String clobValue = messageSearchVO.getDescription();
			if (clobValue != null && !clobValue.trim().equals("") && clobValue.length() > 5000) {
				clobValue = clobValue.substring(0, 5000);
				clobValue = clobValue + "...";
			}

			datamap.add(clobValue);
			datamap.add(String.valueOf(messageSearchVO.getRequestRecvdDate()));
			datamap.add(String.valueOf(messageSearchVO.getRecipientFullName()));
			datamap.add(String.valueOf(messageSearchVO.getAckDate()));
			dataList.add(datamap);
		}

		return dataList;
	}

	private List<String> getHeaderListNormal() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add("Case Reference Number");
			lstHeader.add("Text");
			lstHeader.add("Sent From");
			lstHeader.add("Sent At");
			lstHeader.add("Sent To");
			lstHeader.add("Acknowledged?");
			return lstHeader;
		} catch (UnsupportedOperationException var3) {
			throw new CMSException(this.logger, var3);
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (IllegalArgumentException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public ModelAndView mesgSearchHistory(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			SearchCriteria searchCriteria = new SearchCriteria();
			modelAndView = new ModelAndView("mesgSearch");
			String excelParams = (String) request.getSession().getAttribute("searchHistory");
			if (null != excelParams) {
				this.setMessageSearchParams(searchCriteria, (Map) JSONValue.parse(excelParams));
				request.getSession().removeAttribute("searchHistory");
			} else {
				modelAndView.addObject("isAdvanceSearch", "true");
			}

			modelAndView.addObject("messageDetails", searchCriteria);
			return modelAndView;
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	private void setMessageSearchParams(SearchCriteria searchCriteria, Map jsonObject) {
		if (jsonObject.get("startCaseRecvDate") != null) {
			searchCriteria.setStartCaseRecvDate((String) jsonObject.get("startCaseRecvDate"));
		}

		if (jsonObject.get("endCaseRecvDate") != null) {
			searchCriteria.setEndCaseRecvDate((String) jsonObject.get("endCaseRecvDate"));
		}

		searchCriteria.setMessageText((String) jsonObject.get("messageText"));
		searchCriteria.setCrnNo((String) jsonObject.get("crnNo"));
		searchCriteria.setCaseStatus((String) jsonObject.get("caseStatus"));
		searchCriteria.setAcknowledge((String) jsonObject.get("acknowledge"));
		searchCriteria.setMessageType((String) jsonObject.get("messageType"));
		searchCriteria.setSender((String) jsonObject.get("sender"));
		searchCriteria.setRecipient((String) jsonObject.get("recipient"));
	}

	public ModelAndView exportToExcelRecMessageSearch(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) {
		this.logger.debug("in export to excel for recurrence case search");
		ModelAndView modelandview = null;
		if (null == request.getSession().getAttribute("sessionID")
				|| "".equalsIgnoreCase((String) request.getSession().getAttribute("sessionID"))) {
			request.getSession().setAttribute("sessionID", request.getSession().getId());

			ModelAndView var6;
			try {
				Object var5 = intSyncUp;
				synchronized (intSyncUp) {
					UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
					this.logger.debug("for rec message search user is " + userBean.getUserName());
					String excelParams = request.getParameter("excelParams");
					Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
					this.logger.debug("parameter for case search " + excelParamMap);
					excelParamMap.put("crnNumbers", StringUtils.commaSeparatedStringToList(searchCriteria.getCrnNo()));
					excelParamMap.put("searchType", "recurrence");
					excelParamMap.put("loginUser", userBean.getUserName());
					MessageSearchManager messageSearchManager = (MessageSearchManager) this.searchFactory
							.getSearchImpl("MessageSearch");
					List<MessageSearchVO> messageSearchList = messageSearchManager.searchForExportRec(excelParamMap);
					this.logger.debug(
							"number of records to export for recurrence message search " + messageSearchList.size());
					Map<String, Object> resultMap = this.writeToExcelRec(messageSearchList, response);
					modelandview = new ModelAndView("excelDownloadPopup");
					modelandview.addObject("fileBytes", resultMap.get("fileBytes"));
					modelandview.addObject("fileName", resultMap.get("fileName"));
					this.logger.debug("user " + userBean.getUserName() + "'s request has been processed");
					break label25;
				}
			} catch (CMSException var19) {
				var6 = AtlasUtils.getExceptionView(this.logger, var19);
				return var6;
			} catch (Exception var20) {
				var6 = AtlasUtils.getExceptionView(this.logger, var20);
			} finally {
				request.getSession().removeAttribute("sessionID");
			}

			return var6;
		}

		this.logger.debug("before returning MV in export to excel for recurrence case search");
		return modelandview;
	}

	private Map<String, Object> writeToExcelRec(List<MessageSearchVO> messageSearchList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  write To Excel for rec ");
		List<String> lstHeader = this.getHeaderListRec();
		List<LinkedHashMap<String, String>> dataList = new ArrayList();
		MessageSearchVO messageSearchVO = null;

		try {
			Iterator iterator = messageSearchList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				messageSearchVO = (MessageSearchVO) iterator.next();
				this.populateDataMapRec(messageSearchVO, datamap);
				dataList.add(datamap);
			}
		} catch (UnsupportedOperationException var8) {
			throw new CMSException(this.logger, var8);
		} catch (ClassCastException var9) {
			throw new CMSException(this.logger, var9);
		} catch (NullPointerException var10) {
			throw new CMSException(this.logger, var10);
		} catch (IllegalArgumentException var11) {
			throw new CMSException(this.logger, var11);
		}

		return ExcelDownloader.writeToExcel(lstHeader, dataList, "messageSearchData", (short) 0, (short) 1, response,
				"Message Search");
	}

	private void populateDataMapRec(MessageSearchVO messageSearchVO, LinkedHashMap<String, String> datamap) {
		String clobValue = messageSearchVO.getDescription();
		if (clobValue != null && !clobValue.trim().equals("") && clobValue.length() > 5000) {
			clobValue = clobValue.substring(0, 5000);
			clobValue = clobValue + "...";
		}

		datamap.put("Text", clobValue);
		datamap.put("Sent At", String.valueOf(messageSearchVO.getRequestRecvdDate()));
		datamap.put("Sent To", String.valueOf(messageSearchVO.getRecipientFullName()));
		datamap.put("Acknowledged?", String.valueOf(messageSearchVO.getAckDate()));
	}

	private List<String> getHeaderListRec() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add("Text");
			lstHeader.add("Sent At");
			lstHeader.add("Sent To");
			lstHeader.add("Acknowledged?");
			return lstHeader;
		} catch (UnsupportedOperationException var3) {
			throw new CMSException(this.logger, var3);
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (IllegalArgumentException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public ModelAndView createSearchRecCase(HttpServletRequest request, HttpServletResponse response,
			NotificationVO notificationVO) {
		ModelAndView mv = new ModelAndView("mesgSearch");
		String message = "Case Created Successfully.";
		String recCaseSchedulerId = "";
		if (null != request.getParameter("recCaseSchedulerId")) {
			recCaseSchedulerId = request.getParameter("recCaseSchedulerId");
		}

		String recClientCaseId = "";
		if (null != request.getParameter("recClientCaseId")) {
			recClientCaseId = request.getParameter("recClientCaseId");
		}

		this.logger.debug("recClientCaseId " + recClientCaseId + " recCaseSchedulerId " + recCaseSchedulerId);

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			if (null != recClientCaseId && !recClientCaseId.equalsIgnoreCase("")) {
				this.logger.debug("Going to create case");
				ResourceLocator.self().getNotificationService().createRecCase(recClientCaseId, recCaseSchedulerId,
						userBean.getUserName(), SBMUtils.getSession(request));
			}
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}

		request.getSession().setAttribute("message", message);
		return mv;
	}
}