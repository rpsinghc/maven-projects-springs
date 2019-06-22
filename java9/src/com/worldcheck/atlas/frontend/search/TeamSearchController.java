package com.worldcheck.atlas.frontend.search;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.interfaces.ITeamSearchManager;
import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.bl.search.SearchFactory;
import com.worldcheck.atlas.bl.search.TeamSearchManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.search.TeamSearchVO;
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

public class TeamSearchController extends MultiActionController {
	private static final String PART_STR_CONST = "Part";
	private static final String CRN_NO = "crnNo";
	private static final String CRN_NUMBERS_TOKEN = "crnNumbers";
	private static final String SESSION_ID = "sessionID";
	private static final int NUMBER_OF_ROWS_INEXCEL_VAL = 4760;
	private static final String EXCEL_GENERATION_TOKEN = "excelGeneration";
	private static final String PRIMARY_SUBJECT = "Primary Subject";
	private static final String OFFICE = "office";
	private static final String EXCEL_FILE_NAME = "Team Search";
	private static final String CRN = "Case Reference Number";
	private static final String ANALYST = "Analyst";
	private static final String REVIEWER1 = "Reviewer1";
	private static final String REVIEWER2 = "Reviewer2";
	private static final String REVIEWER3 = "Reviewer3";
	private static final String VENDOR_MANAGER = "Vendor Manager";
	private static final String BI_ANALYST = "Bi Analyst";
	private static final String MANAGER = "Manager";
	private static final String INTERIM_DUE_DATE1 = "Interim 1 Due Date";
	private static final String INTERIM_DUE_DATE2 = "Interim 2 Due Date";
	private static final String FINAL_DUE_DATE = "Final Due Date";
	private static final String TEAM_DATA = "teamData";
	private static final String PROCESSCYCLE = "Process Cycle";
	private static final String ACTIVETASK = "Active Task";
	private static final Object intSyncUp = new Object();
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.search.TeamSearchController");
	ITeamSearchManager teamSearchManager;
	SearchFactory searchFactory = null;

	public void setSearchFactory(SearchFactory searchFactory) {
		this.searchFactory = searchFactory;
	}

	public ModelAndView teamSearch(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			SearchCriteria searchCriteria = new SearchCriteria();
			if (null != request.getParameter("officeTeamSearch")) {
				this.logger.debug("office for team search " + request.getParameter("officeTeamSearch"));
				searchCriteria.setOffice(request.getParameter("officeTeamSearch"));
			}

			modelAndView = new ModelAndView("teamSearch");
			modelAndView.addObject("caseDetails", searchCriteria);
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView setupAdvanceTeamSearch(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("teamSearch");
			modelAndView.addObject("isAdvanceSearch", "true");
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView exportToExcelTeamSearch(HttpServletRequest request, HttpServletResponse response,
			SearchCriteria searchCriteria) {
		this.logger.debug("in export to excel FOR tEAM SEACRH");
		ModelAndView modelandview = null;
		if (null == request.getSession().getAttribute("sessionID")
				|| "".equalsIgnoreCase((String) request.getSession().getAttribute("sessionID"))) {
			request.getSession().setAttribute("sessionID", request.getSession().getId());

			ModelAndView var6;
			try {
				Object var5 = intSyncUp;
				synchronized (intSyncUp) {
					UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
					this.logger.debug("for team search user is " + userBean.getUserName());
					String excelParams = request.getParameter("excelParams");
					Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
					this.logger.debug("parameter " + excelParamMap);
					excelParamMap.put("crnNumbers",
							StringUtils.commaSeparatedStringToList((String) excelParamMap.get("crnNo")));
					TeamSearchManager teamSearch = (TeamSearchManager) this.searchFactory.getSearchImpl("TeamSearch");
					List<TeamSearchVO> teamSearchList = teamSearch.searchForExport(excelParamMap);
					this.logger.debug("number of records " + teamSearchList.size());
					Map<String, Object> resultMap = this.writeToExcel(teamSearchList, response);
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

		this.logger.debug("before returning MV in export to excel FOR tEAM SEACRH");
		return modelandview;
	}

	private Map<String, Object> writeToExcel(List<TeamSearchVO> resultList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  write To Excel ");
		List<String> lstHeader = this.getHeaderList();
		List<LinkedHashMap<String, String>> dataList = new ArrayList();
		TeamSearchVO teamSearchVO = null;

		try {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				teamSearchVO = (TeamSearchVO) iterator.next();
				this.populateDataMap(teamSearchVO, datamap);
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

		return ExcelDownloader.writeToExcel(lstHeader, dataList, "teamData", (short) 0, (short) 1, response,
				"Team Search");
	}

	public ModelAndView writeToZipTeamSearch(HttpServletRequest request, HttpServletResponse response) {
		String zipFile = "";
		this.logger.debug("in export to excel FOR tEAM SEACRH(zip)");
		if (null == request.getSession().getAttribute("sessionID")
				|| "".equalsIgnoreCase((String) request.getSession().getAttribute("sessionID"))) {
			request.getSession().setAttribute("sessionID", request.getSession().getId());

			label133 : {
				ModelAndView var5;
				try {
					Object var4 = intSyncUp;
					synchronized (intSyncUp) {
						UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
						this.logger.debug("for team search(zip) user is " + userBean.getUserName());
						List<List<TeamSearchVO>> partedList = new ArrayList();
						String excelParams = request.getParameter("excelParams");
						Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
						this.logger.debug("parameter " + excelParamMap);
						excelParamMap.put("crnNumbers",
								StringUtils.commaSeparatedStringToList((String) excelParamMap.get("crnNo")));
						TeamSearchManager teamSearch = (TeamSearchManager) this.searchFactory
								.getSearchImpl("TeamSearch");
						List<TeamSearchVO> teamSearchList = teamSearch.searchForExport(excelParamMap);
						this.logger.debug("number of records " + teamSearchList.size());
						int rowsAccomodated = 4760;
						List<String> fileNamesList = new ArrayList();
						int N = teamSearchList.size();
						int L = rowsAccomodated;

						int i;
						for (i = 0; i < N; i += L) {
							partedList.add(partedList.size(), teamSearchList.subList(i, Math.min(N, i + L)));
						}

						for (i = 0; i < partedList.size(); ++i) {
							String fileName = this.writeToMulipleExcel((List) partedList.get(i), response,
									"Part" + (i + 1), request);
							fileNamesList.add(fileName);
						}

						zipFile = ExcelDownloader.generateZipFile(fileNamesList, "Team Search", response);
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

		this.logger.debug("before returning in export to excel FOR tEAM SEACRH(zip)");
		return null;
	}

	private String writeToMulipleExcel(List<TeamSearchVO> resultList, HttpServletResponse response, String partName,
			HttpServletRequest request) throws CMSException {
		String var8;
		try {
			request.getSession().setAttribute("excelGeneration", new Date());
			List<String> lstHeader = null;
			List<List<String>> dataList = null;
			lstHeader = this.getHeaderList();
			dataList = this.populateDataMapForZip(resultList);
			String fileName = ExcelDownloader.writeToExcel1(lstHeader, dataList, "Team Search" + partName, (short) 0,
					(short) 1, response, "Team Search" + partName);
			var8 = fileName;
		} finally {
			request.getSession().removeAttribute("excelGeneration");
		}

		return var8;
	}

	private List<List<String>> populateDataMapForZip(List<TeamSearchVO> resultList) {
		List<List<String>> dataList = new ArrayList();
		Iterator iterator = resultList.iterator();

		while (iterator.hasNext()) {
			List<String> datamap = new ArrayList();
			TeamSearchVO teamSearchVO = (TeamSearchVO) iterator.next();
			datamap.add(String.valueOf(teamSearchVO.getCrnNo()));
			datamap.add(String.valueOf(teamSearchVO.getPrimarySubject()));
			datamap.add(String.valueOf(teamSearchVO.getOffice()));
			datamap.add(String.valueOf(teamSearchVO.getProcessCycle()));
			datamap.add(String.valueOf(teamSearchVO.getActiveTask()));
			datamap.add(String.valueOf(teamSearchVO.getAnalyst()));
			datamap.add(String.valueOf(teamSearchVO.getReviewer1()));
			datamap.add(String.valueOf(teamSearchVO.getReviewer2()));
			datamap.add(String.valueOf(teamSearchVO.getReviewer3()));
			datamap.add(String.valueOf(teamSearchVO.getVendorManager()));
			datamap.add(String.valueOf(teamSearchVO.getBiAnalyst()));
			datamap.add(String.valueOf(teamSearchVO.getCaseManager()));
			datamap.add(String.valueOf(teamSearchVO.getIntrimDueDate1()));
			datamap.add(String.valueOf(teamSearchVO.getIntrimDueDate2()));
			datamap.add(String.valueOf(teamSearchVO.getFinalDueDate()));
			dataList.add(datamap);
		}

		return dataList;
	}

	private List<String> getHeaderList() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add("Case Reference Number");
			lstHeader.add("Primary Subject");
			lstHeader.add("office");
			lstHeader.add("Process Cycle");
			lstHeader.add("Active Task");
			lstHeader.add("Analyst");
			lstHeader.add("Reviewer1");
			lstHeader.add("Reviewer2");
			lstHeader.add("Reviewer3");
			lstHeader.add("Vendor Manager");
			lstHeader.add("Bi Analyst");
			lstHeader.add("Manager");
			lstHeader.add("Interim 1 Due Date");
			lstHeader.add("Interim 2 Due Date");
			lstHeader.add("Final Due Date");
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

	private void populateDataMap(TeamSearchVO teamSearchVO, LinkedHashMap<String, String> datamap) {
		datamap.put("Case Reference Number", String.valueOf(teamSearchVO.getCrnNo()));
		datamap.put("Primary Subject", String.valueOf(teamSearchVO.getPrimarySubject()));
		datamap.put("office", String.valueOf(teamSearchVO.getOffice()));
		datamap.put("Process Cycle", String.valueOf(teamSearchVO.getProcessCycle()));
		datamap.put("Active Task", String.valueOf(teamSearchVO.getActiveTask()));
		datamap.put("Analyst", String.valueOf(teamSearchVO.getAnalyst()));
		datamap.put("Reviewer1", String.valueOf(teamSearchVO.getReviewer1()));
		datamap.put("Reviewer2", String.valueOf(teamSearchVO.getReviewer2()));
		datamap.put("Reviewer3", String.valueOf(teamSearchVO.getReviewer3()));
		datamap.put("Vendor Manager", String.valueOf(teamSearchVO.getVendorManager()));
		datamap.put("Bi Analyst", String.valueOf(teamSearchVO.getBiAnalyst()));
		datamap.put("Manager", String.valueOf(teamSearchVO.getCaseManager()));
		datamap.put("Interim 1 Due Date", String.valueOf(teamSearchVO.getIntrimDueDate1()));
		datamap.put("Interim 2 Due Date", String.valueOf(teamSearchVO.getIntrimDueDate2()));
		datamap.put("Final Due Date", String.valueOf(teamSearchVO.getFinalDueDate()));
	}

	public ModelAndView teamSearchHistory(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			SearchCriteria searchCriteria = new SearchCriteria();
			modelAndView = new ModelAndView("teamSearch");
			String excelParams = (String) request.getSession().getAttribute("searchHistory");
			if (null != excelParams) {
				this.setTeamSearchParams(searchCriteria, (Map) JSONValue.parse(excelParams));
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

	private void setTeamSearchParams(SearchCriteria searchCriteria, Map jsonObject) {
		searchCriteria.setCrnNo((String) jsonObject.get("crnNo"));
		if (jsonObject.get("startCaseRecvDate") != null) {
			searchCriteria.setStartCaseRecvDate((String) jsonObject.get("startCaseRecvDate"));
		}

		if (jsonObject.get("endCaseRecvDate") != null) {
			searchCriteria.setEndCaseRecvDate((String) jsonObject.get("endCaseRecvDate"));
		}

		searchCriteria.setSubjectName((String) jsonObject.get("subjectName"));
		searchCriteria.setAnalyst((String) jsonObject.get("analyst"));
		searchCriteria.setReviewer((String) jsonObject.get("reviewer"));
		searchCriteria.setBiAnalyst((String) jsonObject.get("biAnalyst"));
		searchCriteria.setVendorManager((String) jsonObject.get("vendorManager"));
		searchCriteria.setOffice((String) jsonObject.get("office"));
		searchCriteria.setCaseStatus((String) jsonObject.get("caseStatus"));
		searchCriteria.setCaseManager((String) jsonObject.get("caseManager"));
		searchCriteria.setShowOnlyTeamLevelResults((String) jsonObject.get("showOnlyTeamLevelResults"));
	}
}