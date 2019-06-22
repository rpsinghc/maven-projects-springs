package com.worldcheck.atlas.frontend.search;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.search.CaseSearchManager;
import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.bl.search.SearchFactory;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import com.worldcheck.atlas.vo.search.CaseSearchVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CaseSearchController extends MultiActionController {
	private static final String EXCEL_GENERATION = "excelGeneration";
	private static final String CRN_NO = "crnNo";
	private static final String CRN_NUMBERS_TOKEN = "crnNumbers";
	private static final String NORMAL_STR_TOKEN = "normal";
	private static final String REC_STR_TOKEN = "rec";
	private static final String PART_STR_TOKEN = "Part";
	private static final String SEARCH_TYPE_TOKEN = "searchType";
	private static final String RECURRENCE_STR_TOKEN = "recurrence";
	private static final String SESSION_ID = "sessionID";
	private static final String RECURRENCE_CASE_NUMBER = "Recurrence Case Number";
	private static final String CASE_CREATOR = "Case Creator";
	private static final String CASE_STATUS = "Case Status";
	private static final String PRIMARY_TEAM = "Primary Team";
	private static final String CASE_MANAGER = "Case Manager";
	private static final String REPORT_TYPE = "Report Type";
	private static final String CLIENT_DUE_DATE = "Client Due Date";
	private static final String RESEARCH_DUE_DATE = "Research Due Date";
	private static final String RECEIVED_DATE = "Received Date";
	private static final String CLIENT_REFERENCE_NUMBER = "Client(s) Reference Number";
	private static final String OTHER_SUBJECT = "Other Subjects";
	private static final String COUNTRY = "Country";
	private static final int NUMBER_OF_ROWS_INEXCEL_VAL = 4760;
	private static final String PRIMARY_SUBJECT = "Primary Subject";
	private static final String CASE_DATA = "caseData";
	private static final String EXCEL_FILE_NAME = "Case Search";
	private static final String CRN = "Case Reference Number";
	private static final String CLIENT_CODE = "Client Code";
	private static final String RECURRENCE_PATTERN = "Recurrence Pattern";
	private static final Object intSyncUp = new Object();
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.search.CaseSearchController");
	SearchFactory searchFactory = null;
	List<String> rolePermissionList = new ArrayList();

	public void setSearchFactory(SearchFactory searchFactory) {
		this.searchFactory = searchFactory;
	}

	public ModelAndView caseSearch(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			SearchCriteria searchCriteria = new SearchCriteria();
			this.logger.debug("request.getParameter()::" + request.getParameter("exactMatch"));
			if (null != request.getParameter("crnNumCaseSearch")) {
				searchCriteria.setCrnNo(request.getParameter("crnNumCaseSearch"));
				this.logger.debug("crn number " + searchCriteria.getCrnNo());
			}

			if (null != request.getParameter("subjectNameCaseSearch")) {
				searchCriteria.setSubjectName(request.getParameter("subjectNameCaseSearch"));
				this.logger.debug("subject name " + searchCriteria.getSubjectName());
			}

			if (null != request.getParameter("exactMatch")) {
				searchCriteria.setExactMatch(request.getParameter("exactMatch"));
				this.logger.debug("Exact Match:::" + searchCriteria.getExactMatch());
			}

			searchCriteria.setLegacyData("true");
			searchCriteria.setSearchType("normal");
			modelAndView = new ModelAndView("caseSearch");
			modelAndView.addObject("caseDetails", searchCriteria);
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView executeLastSearch(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			HttpSession session = request.getSession();
			modelAndView = new ModelAndView("caseSearch");
			session.setAttribute("ecuteLastSearchClick", "yes");
			if (null != session.getAttribute("lastSearchCriteria")) {
				this.logger.debug("lastsearch==" + session.getAttribute("lastSearchCriteria"));
				modelAndView.addObject("caseDetails", (SearchCriteria) session.getAttribute("lastSearchCriteria"));
			} else {
				modelAndView.addObject("isAdvanceSearch", "true");
			}

			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView caseSearchHistory(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			SearchCriteria searchCriteria = new SearchCriteria();
			String excelParams = (String) request.getSession().getAttribute("searchHistory");
			if (null != excelParams) {
				modelAndView = this.setSearchParameters(searchCriteria, excelParams);
				request.getSession().removeAttribute("searchHistory");
			} else {
				modelAndView = new ModelAndView("caseSearch");
				modelAndView.addObject("isAdvanceSearch", "true");
			}

			modelAndView.addObject("caseDetails", searchCriteria);
			return modelAndView;
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	private ModelAndView setSearchParameters(SearchCriteria searchCriteria, String excelParams) {
		ModelAndView modelAndView = null;
		Map jsonObject = (Map) JSONValue.parse(excelParams);
		if (jsonObject.get("searchType") != null) {
			if (((String) jsonObject.get("searchType")).equals("caseSearch")) {
				this.setCaseSearchParams(searchCriteria, jsonObject);
				modelAndView = new ModelAndView("caseSearch");
			} else if (((String) jsonObject.get("searchType")).equals("recSearch")) {
				this.setRecSearchParams(searchCriteria, jsonObject);
				modelAndView = new ModelAndView("caseSearch");
			}
		}

		this.logger.debug("excel params are " + excelParams);
		return modelAndView;
	}

	private void setTeamSearchParams(SearchCriteria searchCriteria, Map jsonObject) {
	}

	private void setSubSearchParams(SearchCriteria searchCriteria, Map jsonObject) {
	}

	private void setMesgSearchParams(SearchCriteria searchCriteria, Map jsonObject) {
	}

	private void setRecSearchParams(SearchCriteria searchCriteria, Map jsonObject) {
	}

	private void setCaseSearchParams(SearchCriteria searchCriteria, Map jsonObject) {
		searchCriteria.setCrnNo((String) jsonObject.get("crnNo"));
		searchCriteria.setSubjectCountry((String) jsonObject.get("subjectCountry"));
		searchCriteria.setSubjectOtherdetails((String) jsonObject.get("subjectOtherdetails"));
		searchCriteria.setLegacyData((String) jsonObject.get("legacyData"));
		searchCriteria.setExactMatch((String) jsonObject.get("exactMatch"));
		searchCriteria.setClientRefNo((String) jsonObject.get("clientRefNo"));
		searchCriteria.setCaseCreator((String) jsonObject.get("caseCreator"));
		searchCriteria.setCaseManager((String) jsonObject.get("caseManager"));
		searchCriteria.setIsisBulkOrder((String) jsonObject.get("isisBulkOrder"));
		searchCriteria.setSortType((String) jsonObject.get("sortType"));
		searchCriteria.setKeyWord((String) jsonObject.get("keyWord"));
		searchCriteria.setCaseType((String) jsonObject.get("caseType"));
		searchCriteria.setSubjectName((String) jsonObject.get("subjectName"));
		searchCriteria.setCaseStatus((String) jsonObject.get("caseStatus"));
		if (jsonObject.get("includeIntrimDueDate") != null) {
			searchCriteria.setIncludeIntrimDueDate((String) jsonObject.get("includeIntrimDueDate"));
		}

		if (jsonObject.get("startCaseRecvDate") != null) {
			searchCriteria.setStartCaseRecvDate((String) jsonObject.get("startCaseRecvDate"));
		}

		if (jsonObject.get("endCaseRecvDate") != null) {
			searchCriteria.setEndCaseRecvDate((String) jsonObject.get("endCaseRecvDate"));
		}

		if (jsonObject.get("startClientDueDate") != null) {
			searchCriteria.setStartClientDueDate((String) jsonObject.get("startClientDueDate"));
		}

		if (jsonObject.get("endClientDueDate") != null) {
			searchCriteria.setEndClientDueDate((String) jsonObject.get("endClientDueDate"));
		}

	}

	public ModelAndView setupAdvanceCaseSearch(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("caseSearch");
			modelAndView.addObject("isAdvanceSearch", "true");
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView exportToExcelRecCaseSearch(HttpServletRequest request, HttpServletResponse response,
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
					this.logger.debug("for rec case search user is " + userBean.getUserName());
					String excelParams = request.getParameter("excelParams");
					Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
					this.logger.debug("parameter for case search " + excelParamMap);
					excelParamMap.put("searchType", "recurrence");
					CaseSearchManager caseSearch = (CaseSearchManager) this.searchFactory.getSearchImpl("CaseSearch");
					List<CaseSearchVO> caseSearchList = caseSearch.searchForExport(excelParamMap);
					this.logger
							.debug("number of records to export for recurrence case search " + caseSearchList.size());
					Map<String, Object> resultMap = this.writeToExcelRec(caseSearchList, response);
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

		this.logger.debug("brfore returning model and view in export to excel for recurrence case search");
		return modelandview;
	}

	public synchronized ModelAndView exportToExcelCaseSearch(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) {
		this.logger.debug("in export to excel FOR CASE SEACRH");
		ModelAndView modelandview = new ModelAndView("excelDownloadPopup");
		UserDetailsBean userDetailBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
		this.rolePermissionList = userDetailBean.getRoleList();
		if (null == request.getSession().getAttribute("sessionID")
				|| "".equalsIgnoreCase((String) request.getSession().getAttribute("sessionID"))) {
			request.getSession().setAttribute("sessionID", request.getSession().getId());

			ModelAndView var7;
			try {
				Object var6 = intSyncUp;
				synchronized (intSyncUp) {
					UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
					this.logger.debug("for case search user is " + userBean.getUserName());
					String excelParams = request.getParameter("excelParams");
					Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
					this.logger.debug("parameter " + excelParamMap);
					excelParamMap.put("crnNumbers",
							StringUtils.commaSeparatedStringToList((String) excelParamMap.get("crnNo")));
					CaseSearchManager caseSearch = (CaseSearchManager) this.searchFactory.getSearchImpl("CaseSearch");
					List<CaseSearchVO> caseSearchList = caseSearch.searchForExport(excelParamMap);
					this.logger.debug("number of records " + caseSearchList.size());
					Map<String, Object> resultMap = this.writeToExcelNormal(caseSearchList, response);
					modelandview.addObject("fileBytes", resultMap.get("fileBytes"));
					modelandview.addObject("fileName", resultMap.get("fileName"));
					this.logger.debug("user " + userBean.getUserName() + "'s request has been processed");
					break label25;
				}
			} catch (CMSException var20) {
				var7 = AtlasUtils.getExceptionView(this.logger, var20);
				return var7;
			} catch (Exception var21) {
				var7 = AtlasUtils.getExceptionView(this.logger, var21);
			} finally {
				request.getSession().removeAttribute("sessionID");
			}

			return var7;
		}

		this.logger.debug("before returning mv in export to excel FOR CASE SEACRH");
		return modelandview;
	}

	public ModelAndView writeToZipCaseSearch(HttpServletRequest request, HttpServletResponse response) {
		String zipFile = "";
		this.logger.debug("in export to excel FOR CASE SEACRH(zip)");
		if (null == request.getSession().getAttribute("sessionID")
				|| "".equalsIgnoreCase((String) request.getSession().getAttribute("sessionID"))) {
			request.getSession().setAttribute("sessionID", request.getSession().getId());

			label133 : {
				ModelAndView var5;
				try {
					Object var4 = intSyncUp;
					synchronized (intSyncUp) {
						UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
						this.logger.debug("for case search(zip) user is " + userBean.getUserName());
						String excelParams = request.getParameter("excelParams");
						Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
						List<List<CaseSearchVO>> partedList = new ArrayList();
						this.logger.debug("parameter " + excelParamMap);
						excelParamMap.put("crnNumbers",
								StringUtils.commaSeparatedStringToList((String) excelParamMap.get("crnNo")));
						CaseSearchManager caseSearch = (CaseSearchManager) this.searchFactory
								.getSearchImpl("CaseSearch");
						List<CaseSearchVO> caseSearchList = caseSearch.searchForExport(excelParamMap);
						this.logger.debug("number of records " + caseSearchList.size());
						int rowsAccomodated = 4760;
						List<String> fileNamesList = new ArrayList();
						int N = caseSearchList.size();
						int L = rowsAccomodated;

						int i;
						for (i = 0; i < N; i += L) {
							partedList.add(partedList.size(), caseSearchList.subList(i, Math.min(N, i + L)));
						}

						for (i = 0; i < partedList.size(); ++i) {
							String fileName = this.writeToMulipleExcel((List) partedList.get(i), response,
									"Part" + (i + 1), request, "normal");
							fileNamesList.add(fileName);
						}

						zipFile = ExcelDownloader.generateZipFile(fileNamesList, "Case Search", response);
						this.logger.debug("user " + userBean.getUserName() + "'s request has been processed");
						break label133;
					}
				} catch (CMSException var26) {
					var5 = AtlasUtils.getExceptionView(this.logger, var26);
					return var5;
				} catch (Exception var27) {
					var5 = AtlasUtils.getExceptionView(this.logger, var27);
				} finally {
					request.getSession().removeAttribute("sessionID");
				}

				return var5;
			}

			try {
				ExcelDownloader.openZipFile(zipFile, response);
			} catch (Exception var24) {
				return AtlasUtils.getExceptionView(this.logger, var24);
			}
		}

		this.logger.debug("before returning in export to excel FOR CASE SEACRH(zip)");
		return null;
	}

	public ModelAndView writeToZipCaseSearchRec(HttpServletRequest request, HttpServletResponse response) {
		String zipFile = "";
		this.logger.debug("in export to excel for recurrence case search(zip)");
		if (null == request.getSession().getAttribute("sessionID")
				|| "".equalsIgnoreCase((String) request.getSession().getAttribute("sessionID"))) {
			request.getSession().setAttribute("sessionID", request.getSession().getId());

			label133 : {
				ModelAndView var5;
				try {
					Object var4 = intSyncUp;
					synchronized (intSyncUp) {
						UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
						this.logger.debug("for rec case search(zip) user is " + userBean.getUserName());
						List<List<CaseSearchVO>> partedList = new ArrayList();
						String excelParams = request.getParameter("excelParams");
						Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
						this.logger.debug("parameter for case search " + excelParamMap);
						excelParamMap.put("searchType", "recurrence");
						CaseSearchManager caseSearch = (CaseSearchManager) this.searchFactory
								.getSearchImpl("CaseSearch");
						List<CaseSearchVO> caseSearchList = caseSearch.searchForExport(excelParamMap);
						this.logger.debug(
								"number of records to export for recurrence case search " + caseSearchList.size());
						int rowsAccomodated = 4760;
						List<String> fileNamesList = new ArrayList();
						int N = caseSearchList.size();
						int L = rowsAccomodated;

						int i;
						for (i = 0; i < N; i += L) {
							partedList.add(partedList.size(), caseSearchList.subList(i, Math.min(N, i + L)));
						}

						for (i = 0; i < partedList.size(); ++i) {
							String fileName = this.writeToMulipleExcel((List) partedList.get(i), response,
									"Part" + (i + 1), request, "rec");
							fileNamesList.add(fileName);
						}

						zipFile = ExcelDownloader.generateZipFile(fileNamesList, "Case Search", response);
						this.logger.debug("user " + userBean.getUserName() + "'s request has been processed");
						break label133;
					}
				} catch (CMSException var26) {
					var5 = AtlasUtils.getExceptionView(this.logger, var26);
				} catch (Exception var27) {
					var5 = AtlasUtils.getExceptionView(this.logger, var27);
					return var5;
				} finally {
					request.getSession().removeAttribute("sessionID");
				}

				return var5;
			}

			try {
				ExcelDownloader.openZipFile(zipFile, response);
			} catch (Exception var24) {
				return AtlasUtils.getExceptionView(this.logger, var24);
			}
		}

		this.logger.debug("before returning in export to excel for recurrence case search(zip)");
		return null;
	}

	private String writeToMulipleExcel(List<CaseSearchVO> resultList, HttpServletResponse response, String partName,
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

			String fileName = ExcelDownloader.writeToExcel1(lstHeader, dataList, "Case Search" + partName, (short) 0,
					(short) 1, response, "Case Search" + partName);
			var9 = fileName;
		} finally {
			request.getSession().removeAttribute("excelGeneration");
		}

		return var9;
	}

	private Map<String, Object> writeToExcelNormal(List<CaseSearchVO> resultList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  write To Excel ");
		List<String> lstHeader = this.getHeaderListNormal();
		List<LinkedHashMap<String, String>> dataList = new ArrayList();
		CaseSearchVO caseSearchVO = null;

		try {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				caseSearchVO = (CaseSearchVO) iterator.next();
				this.populateDataMapNormal(caseSearchVO, datamap);
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

		return ExcelDownloader.writeToExcel(lstHeader, dataList, "caseData", (short) 0, (short) 1, response,
				"Case Search");
	}

	private Map<String, Object> writeToExcelRec(List<CaseSearchVO> resultList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  write To Excel for rec ");
		List<String> lstHeader = this.getHeaderListRec();
		List<LinkedHashMap<String, String>> dataList = new ArrayList();
		CaseSearchVO caseSearchVO = null;

		try {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				caseSearchVO = (CaseSearchVO) iterator.next();
				this.populateDataMapRec(caseSearchVO, datamap);
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

		return ExcelDownloader.writeToExcel(lstHeader, dataList, "caseData", (short) 0, (short) 1, response,
				"Case Search");
	}

	private List<String> getHeaderListRec() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add("Recurrence Case Number");
			lstHeader.add("Client Code");
			lstHeader.add("Report Type");
			lstHeader.add("Case Manager");
			lstHeader.add("Recurrence Pattern");
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

	private void populateDataMapRec(CaseSearchVO caseSearchVO, LinkedHashMap<String, String> datamap) {
		datamap.put("Case Reference Number", String.valueOf(caseSearchVO.getCrnNo()));
		datamap.put("Client Code", String.valueOf(caseSearchVO.getClientCode()));
		datamap.put("Report Type", String.valueOf(caseSearchVO.getReportType()));
		datamap.put("Case Manager", String.valueOf(caseSearchVO.getCaseManager()));
		datamap.put("Recurrence Pattern", String.valueOf(caseSearchVO.getRecurrPattern()));
	}

	private List<List<String>> populateDataMapForZip(List<CaseSearchVO> resultList) {
		List<List<String>> dataList = new ArrayList();
		Iterator iterator = resultList.iterator();

		while (iterator.hasNext()) {
			List<String> datamap = new ArrayList();
			CaseSearchVO caseSearchVO = (CaseSearchVO) iterator.next();
			datamap.add(String.valueOf(caseSearchVO.getCrnNo()));
			datamap.add(String.valueOf(caseSearchVO.getPrimarySub()));
			datamap.add(String.valueOf(caseSearchVO.getPriCountry()));
			String clobValue = caseSearchVO.getOtherSubject();
			if (clobValue != null && !clobValue.trim().equals("") && !clobValue.isEmpty()
					&& clobValue.length() > 5000) {
				clobValue = clobValue.substring(0, 5000);
				clobValue = clobValue + "...";
			}

			datamap.add(String.valueOf(clobValue));
			datamap.add(String.valueOf(caseSearchVO.getClientRefNo()));
			datamap.add(String.valueOf(caseSearchVO.getReqRecdDate()));
			datamap.add(String.valueOf(caseSearchVO.getClientDueDate()));
			datamap.add(String.valueOf(caseSearchVO.getReportType()));
			datamap.add(String.valueOf(caseSearchVO.getCaseManager()));
			datamap.add(String.valueOf(caseSearchVO.getPrimaryTeamOffice()));
			datamap.add(String.valueOf(caseSearchVO.getCaseStatus()));
			datamap.add(String.valueOf(caseSearchVO.getCaseCreator()));
			dataList.add(datamap);
		}

		return dataList;
	}

	private List<List<String>> populateDataMapForZipRec(List<CaseSearchVO> resultList) {
		List<List<String>> dataList = new ArrayList();
		Iterator iterator = resultList.iterator();

		while (iterator.hasNext()) {
			List<String> datamap = new ArrayList();
			CaseSearchVO caseSearchVO = (CaseSearchVO) iterator.next();
			datamap.add(String.valueOf(caseSearchVO.getCrnNo()));
			datamap.add(String.valueOf(caseSearchVO.getClientCode()));
			datamap.add(String.valueOf(caseSearchVO.getReportType()));
			datamap.add(String.valueOf(caseSearchVO.getCaseManager()));
			datamap.add(String.valueOf(caseSearchVO.getRecurrPattern()));
			dataList.add(datamap);
		}

		return dataList;
	}

	private void populateDataMapNormal(CaseSearchVO caseSearchVO, LinkedHashMap<String, String> datamap) {
		datamap.put("Case Reference Number", String.valueOf(caseSearchVO.getCrnNo()));
		datamap.put("Primary Subject", String.valueOf(caseSearchVO.getPrimarySub()));
		datamap.put("Country", String.valueOf(caseSearchVO.getPriCountry()));
		String clobValue = caseSearchVO.getOtherSubject();
		if (clobValue != null && !clobValue.trim().equals("") && !clobValue.isEmpty() && clobValue.length() > 5000) {
			clobValue = clobValue.substring(0, 5000);
			clobValue = clobValue + "...";
		}

		datamap.put("Other Subjects", String.valueOf(clobValue));
		datamap.put("Client(s) Reference Number", String.valueOf(caseSearchVO.getClientRefNo()));
		datamap.put("Received Date", String.valueOf(caseSearchVO.getReqRecdDate()));
		datamap.put("Client Due Date", String.valueOf(caseSearchVO.getClientDueDate()));
		if (this.rolePermissionList.size() == 2 && this.rolePermissionList.contains("R5")
				&& this.rolePermissionList.contains("R11")) {
			datamap.remove("Client Due Date");
		}

		if (this.rolePermissionList.size() == 1
				&& (this.rolePermissionList.contains("R5") || this.rolePermissionList.contains("R11"))) {
			datamap.remove("Client Due Date");
		}

		datamap.put("Research Due Date", String.valueOf(caseSearchVO.getFinalRDueDate()));
		datamap.put("Report Type", String.valueOf(caseSearchVO.getReportType()));
		datamap.put("Case Manager", String.valueOf(caseSearchVO.getCaseManager()));
		datamap.put("Primary Team", String.valueOf(caseSearchVO.getPrimaryTeamOffice()));
		datamap.put("Case Status", String.valueOf(caseSearchVO.getCaseStatus()));
		datamap.put("Case Creator", String.valueOf(caseSearchVO.getCaseCreator()));
	}

	private List<String> getHeaderListNormal() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add("Case Reference Number");
			lstHeader.add("Primary Subject");
			lstHeader.add("Country");
			lstHeader.add("Other Subjects");
			lstHeader.add("Client(s) Reference Number");
			lstHeader.add("Received Date");
			lstHeader.add("Client Due Date");
			if (this.rolePermissionList.size() == 2 && this.rolePermissionList.contains("R5")
					&& this.rolePermissionList.contains("R11")) {
				lstHeader.remove("Client Due Date");
			}

			if (this.rolePermissionList.size() == 1
					&& (this.rolePermissionList.contains("R5") || this.rolePermissionList.contains("R11"))) {
				lstHeader.remove("Client Due Date");
			}

			lstHeader.add("Research Due Date");
			lstHeader.add("Report Type");
			lstHeader.add("Case Manager");
			lstHeader.add("Primary Team");
			lstHeader.add("Case Status");
			lstHeader.add("Case Creator");
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
}