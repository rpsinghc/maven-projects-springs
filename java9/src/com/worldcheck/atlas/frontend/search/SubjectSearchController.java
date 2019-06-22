package com.worldcheck.atlas.frontend.search;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.bl.search.SearchFactory;
import com.worldcheck.atlas.bl.search.SubjectSearchManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.search.SubjectSearchVO;
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

public class SubjectSearchController extends MultiActionController {
	private static final String EXCEL_GENERATION_STR = "excelGeneration";
	private static final String PART_STR_TOKEN = "Part";
	private static final String COUNTRY_ID = "countryId";
	private static final String COUNTRY_STR_PARAM = "country";
	private static final int NUMBER_OF_ROWS_INEXCEL_VAL = 4760;
	private static final String SESSION_ID = "sessionID";
	private static final String SUBJECT_DATA = "subjectData";
	private static final String EXCEL_FILE_NAME = "Subject Search";
	private static final String SUBJECT_NAME = "Subject Name";
	private static final String COUNTRY = "Country";
	private static final String SUBJECT_TYPE = "Subject Type";
	private static final String CASE_STATUS = "Case Status";
	private static final String RELATED_CASES = "Related Cases";
	private static final Object intSyncUp = new Object();
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.search.SubjectSearchController");
	SearchFactory searchFactory = null;

	public void setSearchFactory(SearchFactory searchFactory) {
		this.searchFactory = searchFactory;
	}

	public ModelAndView subjectSearch(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			SearchCriteria searchCriteria = new SearchCriteria();
			this.logger.debug("request.getParameter()::" + request.getParameter("exactMatch"));
			if (null != request.getParameter("subjectNameSearch")) {
				this.logger
						.debug("subject to be searched in subject search " + request.getParameter("subjectNameSearch"));
				searchCriteria.setSubjectName(request.getParameter("subjectNameSearch"));
			}

			if (null != request.getParameter("exactMatch")) {
				this.logger.debug("Exact Match in subject search " + request.getParameter("exactMatch"));
				searchCriteria.setExactMatch(request.getParameter("exactMatch"));
			}

			modelAndView = new ModelAndView("subjectSearch");
			modelAndView.addObject("caseDetails", searchCriteria);
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView setupAdvanceSubjectSearch(HttpServletRequest request, HttpServletResponse response,
			Object command) throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("subjectSearch");
			modelAndView.addObject("isAdvanceSearch", "true");
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView exportToExcelSubjectSearch(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) {
		this.logger.debug("in export to excel for subject search");
		ModelAndView modelandview = null;
		if (null == request.getSession().getAttribute("sessionID")
				|| "".equalsIgnoreCase((String) request.getSession().getAttribute("sessionID"))) {
			request.getSession().setAttribute("sessionID", request.getSession().getId());

			ModelAndView var6;
			try {
				Object var5 = intSyncUp;
				synchronized (intSyncUp) {
					UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
					this.logger.debug("for subject search user is " + userBean.getUserName());
					String excelParams = request.getParameter("excelParams");
					Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
					this.logger.debug("parameter " + excelParamMap);
					excelParamMap.put("country",
							StringUtils.commaSeparatedStringToList((String) excelParamMap.get("countryId")));
					SubjectSearchManager subjectSearch = (SubjectSearchManager) this.searchFactory
							.getSearchImpl("SubjectSearch");
					List<SubjectSearchVO> subjectSearchList = subjectSearch.searchForExport(excelParamMap);
					this.logger.debug("number of records " + subjectSearchList.size());
					Map<String, Object> resultMap = this.writeToExcel(subjectSearchList, response);
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

		this.logger.debug("before returning MV in export to excel for subject search");
		return modelandview;
	}

	private Map<String, Object> writeToExcel(List<SubjectSearchVO> subjectSearchList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  write To Excel ");
		List<String> lstHeader = this.getHeaderList();
		List<LinkedHashMap<String, String>> dataList = new ArrayList();
		SubjectSearchVO subjectSearchVO = null;

		try {
			Iterator iterator = subjectSearchList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				subjectSearchVO = (SubjectSearchVO) iterator.next();
				this.populateDataMap(subjectSearchVO, datamap);
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

		return ExcelDownloader.writeToExcel(lstHeader, dataList, "subjectData", (short) 0, (short) 1, response,
				"Subject Search");
	}

	public ModelAndView writeToZipSubjectSearch(HttpServletRequest request, HttpServletResponse response) {
		String zipFile = "";
		this.logger.debug("in export to excel for subject search(zip)");
		if (null == request.getSession().getAttribute("sessionID")
				|| "".equalsIgnoreCase((String) request.getSession().getAttribute("sessionID"))) {
			request.getSession().setAttribute("sessionID", request.getSession().getId());

			label133 : {
				ModelAndView var5;
				try {
					Object var4 = intSyncUp;
					synchronized (intSyncUp) {
						UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
						this.logger.debug("for subject search(zip) user is " + userBean.getUserName());
						List<List<SubjectSearchVO>> partedList = new ArrayList();
						String excelParams = request.getParameter("excelParams");
						Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
						this.logger.debug("parameter " + excelParamMap);
						excelParamMap.put("country",
								StringUtils.commaSeparatedStringToList((String) excelParamMap.get("countryId")));
						SubjectSearchManager subjectSearch = (SubjectSearchManager) this.searchFactory
								.getSearchImpl("SubjectSearch");
						List<SubjectSearchVO> subjectSearchList = subjectSearch.searchForExport(excelParamMap);
						this.logger.debug("number of records " + subjectSearchList.size());
						int rowsAccomodated = 4760;
						List<String> fileNamesList = new ArrayList();
						int N = subjectSearchList.size();
						int L = rowsAccomodated;

						int i;
						for (i = 0; i < N; i += L) {
							partedList.add(partedList.size(), subjectSearchList.subList(i, Math.min(N, i + L)));
						}

						for (i = 0; i < partedList.size(); ++i) {
							String fileName = this.writeToMulipleExcel((List) partedList.get(i), response,
									"Part" + (i + 1), request);
							fileNamesList.add(fileName);
						}

						zipFile = ExcelDownloader.generateZipFile(fileNamesList, "Subject Search", response);
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

		this.logger.debug("before returning in export to excel for subject search(zip)");
		return null;
	}

	private String writeToMulipleExcel(List<SubjectSearchVO> resultList, HttpServletResponse response, String partName,
			HttpServletRequest request) throws CMSException {
		String var8;
		try {
			request.getSession().setAttribute("excelGeneration", new Date());
			List<String> lstHeader = null;
			List<List<String>> dataList = null;
			lstHeader = this.getHeaderList();
			dataList = this.populateDataMapForZip(resultList);
			String fileName = ExcelDownloader.writeToExcel1(lstHeader, dataList, "Subject Search" + partName, (short) 0,
					(short) 1, response, "Subject Search" + partName);
			var8 = fileName;
		} finally {
			request.getSession().removeAttribute("excelGeneration");
		}

		return var8;
	}

	private List<String> getHeaderList() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add("CRN");
			lstHeader.add("Subject Name");
			lstHeader.add("Country");
			lstHeader.add("Subject Type");
			lstHeader.add("Case Status");
			lstHeader.add("Related Cases");
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

	private void populateDataMap(SubjectSearchVO subjectSearchVO, LinkedHashMap<String, String> datamap) {
		datamap.put("CRN", String.valueOf(subjectSearchVO.getCrnNo()));
		datamap.put("Subject Name", String.valueOf(subjectSearchVO.getSubjectName()));
		datamap.put("Country", String.valueOf(subjectSearchVO.getSubjectCountry()));
		datamap.put("Subject Type", String.valueOf(subjectSearchVO.getEntityName()));
		datamap.put("Case Status", String.valueOf(subjectSearchVO.getCaseStatus()));
		String clobValue = subjectSearchVO.getRelatedCrn();
		if (clobValue != null && !clobValue.trim().equals("") && !clobValue.isEmpty() && clobValue.length() > 5000) {
			clobValue = clobValue.substring(0, 5000);
			clobValue = clobValue + "...";
		}

		datamap.put("Related Cases", clobValue);
	}

	private List<List<String>> populateDataMapForZip(List<SubjectSearchVO> resultList) {
		List<List<String>> dataList = new ArrayList();
		Iterator iterator = resultList.iterator();

		while (iterator.hasNext()) {
			List<String> datamap = new ArrayList();
			SubjectSearchVO subjectSearchVO = (SubjectSearchVO) iterator.next();
			datamap.add(String.valueOf(subjectSearchVO.getCrnNo()));
			datamap.add(String.valueOf(subjectSearchVO.getSubjectName()));
			datamap.add(String.valueOf(subjectSearchVO.getSubjectCountry()));
			datamap.add(String.valueOf(subjectSearchVO.getEntityName()));
			datamap.add(String.valueOf(subjectSearchVO.getCaseStatus()));
			String clobValue = subjectSearchVO.getRelatedCrn();
			if (clobValue != null && !clobValue.trim().equals("") && !clobValue.isEmpty()
					&& clobValue.length() > 5000) {
				clobValue = clobValue.substring(0, 5000);
				clobValue = clobValue + "...";
			}

			datamap.add(clobValue);
			dataList.add(datamap);
		}

		return dataList;
	}

	public ModelAndView subjectSearchHistory(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			SearchCriteria searchCriteria = new SearchCriteria();
			modelAndView = new ModelAndView("subjectSearch");
			String excelParams = (String) request.getSession().getAttribute("searchHistory");
			if (null != excelParams) {
				this.setSubjectSearchParams(searchCriteria, (Map) JSONValue.parse(excelParams));
				request.getSession().removeAttribute("searchHistory");
			} else {
				modelAndView.addObject("isAdvanceSearch", "true");
			}

			modelAndView.addObject("caseDetails", searchCriteria);
			return modelAndView;
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	private void setSubjectSearchParams(SearchCriteria searchCriteria, Map jsonObject) {
		if (jsonObject.get("startCaseRecvDate") != null) {
			searchCriteria.setStartCaseRecvDate((String) jsonObject.get("startCaseRecvDate"));
		}

		if (jsonObject.get("endCaseRecvDate") != null) {
			searchCriteria.setEndCaseRecvDate((String) jsonObject.get("endCaseRecvDate"));
		}

		searchCriteria.setSubjectName((String) jsonObject.get("subjectName"));
		searchCriteria.setLegacyData((String) jsonObject.get("legacyData"));
		searchCriteria.setCountryId((String) jsonObject.get("countryId"));
		searchCriteria.setEntityTypeId((String) jsonObject.get("entityTypeId"));
		searchCriteria.setKeyWord((String) jsonObject.get("keyWord"));
	}
}