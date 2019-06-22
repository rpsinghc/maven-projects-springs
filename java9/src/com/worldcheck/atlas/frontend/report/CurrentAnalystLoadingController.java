package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.downloadexcel.CurrentAnalystLoadingExcelDownloader;
import com.worldcheck.atlas.bl.report.CurrentAnalystLoadingReport;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.report.CurrentAnalystLoadingVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class CurrentAnalystLoadingController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.CurrentAnalystLoadingController");
	private PropertyReaderUtil propertyReader;
	private String HOME = "redirect:viewCAL.do";
	private String REPORT_TYPE_LIST = "reportTypes";
	private String JSP = "misCALView";
	private String LOADING_LIST = "currentAnalystLoadingList";
	private String REPORT_TO_REMOVE = "Utilization by Revenue";
	private String KEY_TO_REMOVE = "cms.sub7Menu9";
	private String NA_KEY = "NA";
	private String SUPERVISOR = "Supervisor";
	private String SAT = "SAT";
	private String SATSeprator = "#SAT#";
	private String ANALYST = "Analyst";
	private String TOTALCASES = "Total Cases";
	private String month = "";
	private String CRN = "CRN";
	private String ITRM1 = "Itrm1";
	private String ITRM2 = "Itrm2";
	private String FINALDATE = "FinalDate";
	private String CURRENT_ANALYST_LOADING = "Current Analyst Loading Sheet";
	private String PRIMARYSUBJECT = "Primary Subject";
	private String PRIMARYSUBCOUNTRY = "Primary Subject Country";
	private String TASK = "Task";
	private String CASESTATUS = "caseStatus";
	private AtlasReportService atlasReportService = null;
	private String LEAVEDATE = "leaveDate";
	private String LEAVETYPE = "leaveType";
	public String startDate;
	public String endDate;
	private String INTERIM2 = "Interim2";
	private String INTERIM1 = "Interim1";
	private String FINAL = "Final";
	private String nonEmpty = "nonEmpty";
	private String nonEmptyFlag = "";
	private String TrueVal = "true";
	private String FalseVal = "false";
	private String startDat = "retstartDate";
	private String endDat = "retendDate";
	private String officeName = "retofficename";
	private String recStartDate = "startDate";
	private String recEndDate = "endDate";
	private String OFFICE = "office";

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public ModelAndView viewCAL(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = null;
		this.logger.debug("in CurrentAnalystLoadingController");

		try {
			this.logger.debug("reportTypes :: " + this.propertyReader.getOfficeCapacityReportTypes());
			List<String> reportTypeList = StringUtils
					.commaSeparatedStringToList(this.propertyReader.getOfficeCapacityReportTypes());
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			HashMap permissionMap = null;
			permissionMap = (HashMap) userDetailsBean.getPermissionMap();
			String val = (String) permissionMap.get(this.KEY_TO_REMOVE);
			if (null != val && val.trim().length() > 0 && val.equalsIgnoreCase(this.NA_KEY)) {
				reportTypeList.remove(this.REPORT_TO_REMOVE);
			}

			this.logger.debug("updated reportTypeList size :: " + reportTypeList.size() + " :: val :: " + val);
			modelAndView = new ModelAndView(this.JSP);
			modelAndView.addObject(this.REPORT_TYPE_LIST, reportTypeList);
			this.logger.debug("exiting CurrentAnalystLoadingController");
			if (request.getSession().getAttribute(this.nonEmpty) == this.TrueVal) {
				modelAndView.addObject(this.nonEmpty, true);
				modelAndView.addObject(this.startDat, request.getSession().getAttribute(this.startDat));
				modelAndView.addObject(this.endDat, request.getSession().getAttribute(this.endDat));
				modelAndView.addObject(this.officeName, request.getSession().getAttribute(this.officeName));
			}

			request.getSession().removeAttribute(this.nonEmpty);
			request.getSession().removeAttribute(this.startDat);
			request.getSession().removeAttribute(this.endDat);
			request.getSession().removeAttribute(this.officeName);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView currentAnalystLoading_exportToExcel(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in  currentAnalystLoading.exportToExcel ");
		ModelAndView modelandview = new ModelAndView(this.HOME);

		ModelAndView var5;
		try {
			request.setAttribute("reportName", "currentanalystloading");
			request.getSession().setAttribute("excelGeneration", new Date());
			List<String> crnList = new ArrayList();
			HashMap<String, String> ptMainAnalystMap = new HashMap();
			List<CurrentAnalystLoadingVO> currentAnalystLoadingList = this.atlasReportService.getReport(request,
					response);
			this.startDate = request.getParameter("startDate");
			this.endDate = request.getParameter("endDate");
			CurrentAnalystLoadingReport calrPH = (CurrentAnalystLoadingReport) this.atlasReportService
					.getReportObject(request);
			HashMap<String, Object> hmap = new HashMap();
			hmap.put("stDate", request.getParameter("startDate"));
			hmap.put("edDate", request.getParameter("endDate"));
			hmap.put("office", request.getParameter("office"));
			String pHolidays = calrPH.fetchPublicHolidays(hmap);

			int k;
			String actualTaskCrnList;
			for (int a = 0; a < currentAnalystLoadingList.size(); ++a) {
				String[] crnSplited = ((CurrentAnalystLoadingVO) currentAnalystLoadingList.get(a)).getCrn().split(",");
				String[] arr$ = crnSplited;
				k = crnSplited.length;

				for (int i$ = 0; i$ < k; ++i$) {
					actualTaskCrnList = arr$[i$];
					if (crnList.isEmpty() || !crnList.contains(actualTaskCrnList)) {
						crnList.add(actualTaskCrnList);
					}
				}
			}

			this.logger.debug("the Size of the crnLIst==>" + crnList.size());
			List<CurrentAnalystLoadingVO> mergedCALList = new ArrayList();
			List<TeamDetails> primaryTemMainAnalyst = new ArrayList();
			List<MyTaskPageVO> resultList = new ArrayList();
			if (crnList.size() > 0) {
				if (crnList.size() < 1000) {
					primaryTemMainAnalyst = ResourceLocator.self().getOfficeAssignmentService()
							.getPTMainAnalystForCases(crnList);
					resultList = ResourceLocator.self().getSBMService().getAllTaskForCrnList(crnList);
				} else {
					k = 0;
					List<TeamDetails> tempPTAnalyst = null;
					actualTaskCrnList = null;

					while (k < crnList.size()) {
						int start = k;
						int end = k + 1000;
						if (end >= crnList.size()) {
							end = crnList.size() + 1;
						}

						this.logger.debug("After :: start ::  " + k + " end :: " + end);
						tempPTAnalyst = ResourceLocator.self().getOfficeAssignmentService()
								.getPTMainAnalystForCases(crnList.subList(k, end - 1));
						((List) primaryTemMainAnalyst).addAll(tempPTAnalyst);
						List<MyTaskPageVO> tempTaskList = ResourceLocator.self().getSBMService()
								.getAllTaskForCrnList(crnList.subList(k, end - 1));
						k += 999;
						this.logger.debug("after k updated :: start is  " + start + " end is " + end);
						((List) resultList).addAll(tempTaskList);
					}
				}

				this.logger.debug("primaryTemMainAnalyst size :: " + ((List) primaryTemMainAnalyst).size()
						+ " :: resultList size :: " + ((List) resultList).size());
				Iterator itrPTMA = ((List) primaryTemMainAnalyst).iterator();

				while (itrPTMA.hasNext()) {
					TeamDetails vo = (TeamDetails) itrPTMA.next();
					ptMainAnalystMap.put(vo.getCrn(), vo.getMainAnalyst());
				}

				String taskList = "";
				actualTaskCrnList = "";
				String priSubDetails = "";
				String priSubCountryDetails = "";
				String IDD1 = "";
				String IDD2 = "";
				String FDD = "";
				String caseStatus = "";

				for (int i = 0; i < currentAnalystLoadingList.size(); ++i) {
					actualTaskCrnList = "";
					taskList = "";
					priSubDetails = "";
					priSubCountryDetails = "";
					CurrentAnalystLoadingVO vo = (CurrentAnalystLoadingVO) currentAnalystLoadingList.get(i);
					this.logger.debug("crn in list:::" + vo.getCrn());
					String[] var10000;
					if (vo.getItrm1() != null) {
						vo.getItrm1().split(",");
					} else {
						var10000 = new String[0];
					}

					if (vo.getItrm2() != null) {
						vo.getItrm2().split(",");
					} else {
						var10000 = new String[0];
					}

					if (vo.getFinalDueDate() != null) {
						vo.getFinalDueDate().split(",");
					} else {
						var10000 = new String[0];
					}

					if (vo.getCrn().trim().isEmpty()) {
						vo.setCrn("");
						vo.setTask("");
						vo.setPrimarySubject("");
						vo.setPrimarySubjectCountry("");
						vo.setItrm1("");
						vo.setItrm2("");
						vo.setFinalDueDate("");
						vo.setCaseStatus("");
						vo.setTotal(0L);
						mergedCALList.add(vo);
					} else {
						MyTaskPageVO prevTaskVo = null;
						String[] crns = vo.getCrn().split(",");
						String[] subDetails = vo.getPrimarySubject().split("SP~@@");
						String[] subCountryDetails = vo.getPrimarySubjectCountry().split("SP~@@");
						String[] intrim_1 = vo.getItrm1().split(",");
						String[] intrim_2 = vo.getItrm2().split(",");
						String[] final_date = vo.getFinalDueDate().split(",");
						String[] caseStatusArr = vo.getCaseStatus().split(",");

						String currentCRN;
						int j;
						for (int k = 0; k < crns.length; ++k) {
							prevTaskVo = null;
							currentCRN = crns[k];

							for (j = 0; j < ((List) resultList).size(); ++j) {
								MyTaskPageVO taskVo = (MyTaskPageVO) ((List) resultList).get(j);
								if (taskVo.getTaskName().equals("Awaiting Consolidation")) {
									if (vo.getPerformer().equalsIgnoreCase((String) ptMainAnalystMap.get(currentCRN))
											&& currentCRN.equalsIgnoreCase(taskVo.getCrn())) {
										prevTaskVo = taskVo;
										break;
									}
								} else if (vo.getPerformer().equalsIgnoreCase(taskVo.getPerformer())
										&& currentCRN.equalsIgnoreCase(taskVo.getCrn())
										&& !taskVo.getTaskName().equals("Office Assignment Task")
										&& !taskVo.getTaskName().equals("Invoicing Task")
										&& !taskVo.getTaskName().equals("Client Submission Task")
										&& !taskVo.getTaskName().equals("Team Assignment Task")) {
									if (null == prevTaskVo) {
										prevTaskVo = taskVo;
									} else if (!taskVo.getCurrentCycle().contains(this.FINAL)
											|| prevTaskVo.getCurrentCycle().contains(this.FINAL)) {
										if (!taskVo.getCurrentCycle().contains(this.FINAL)
												&& prevTaskVo.getCurrentCycle().contains(this.FINAL)) {
											prevTaskVo = taskVo;
										} else if (taskVo.getCurrentCycle().contains(this.INTERIM1)
												|| !prevTaskVo.getCurrentCycle().contains(this.INTERIM1)) {
											if (taskVo.getCurrentCycle().contains(this.INTERIM1)
													&& !prevTaskVo.getCurrentCycle().contains(this.INTERIM1)) {
												prevTaskVo = taskVo;
											} else if ((taskVo.getCurrentCycle().contains(this.INTERIM2)
													|| taskVo.getCurrentCycle().contains(this.INTERIM1)
													|| !prevTaskVo.getCurrentCycle().contains(this.INTERIM2))
													&& taskVo.getCurrentCycle()
															.equalsIgnoreCase(prevTaskVo.getCurrentCycle())) {
												if (actualTaskCrnList.trim().length() > 0) {
													actualTaskCrnList = actualTaskCrnList + "," + currentCRN;
													taskList = taskList + "," + prevTaskVo.getTaskName();
													priSubDetails = priSubDetails + "SP~@@" + subDetails[k];
													priSubCountryDetails = priSubCountryDetails + "SP~@@"
															+ subCountryDetails[k];
													IDD1 = IDD1 + "," + intrim_1[k];
													IDD2 = IDD2 + "," + intrim_2[k];
													FDD = FDD + "," + final_date[k];
													caseStatus = caseStatus + "," + caseStatusArr[k];
												} else {
													actualTaskCrnList = currentCRN;
													taskList = prevTaskVo.getTaskName();
													priSubDetails = subDetails[k];
													priSubCountryDetails = subCountryDetails[k];
													IDD1 = intrim_1[k];
													IDD2 = intrim_2[k];
													FDD = final_date[k];
													caseStatus = caseStatusArr[k];
												}

												((List) resultList).remove(prevTaskVo);
												--j;
												prevTaskVo = taskVo;
											}
										}
									}
								}
							}

							if (null != prevTaskVo) {
								if (actualTaskCrnList.trim().length() > 0) {
									actualTaskCrnList = actualTaskCrnList + "," + currentCRN;
									taskList = taskList + "," + prevTaskVo.getTaskName();
									priSubDetails = priSubDetails + "SP~@@" + subDetails[k];
									priSubCountryDetails = priSubCountryDetails + "SP~@@" + subCountryDetails[k];
									IDD1 = IDD1 + "," + intrim_1[k];
									IDD2 = IDD2 + "," + intrim_2[k];
									FDD = FDD + "," + final_date[k];
									caseStatus = caseStatus + "," + caseStatusArr[k];
								} else {
									actualTaskCrnList = currentCRN;
									taskList = prevTaskVo.getTaskName();
									priSubDetails = subDetails[k];
									priSubCountryDetails = subCountryDetails[k];
									IDD1 = intrim_1[k];
									IDD2 = intrim_2[k];
									FDD = final_date[k];
									caseStatus = caseStatusArr[k];
								}

								((List) resultList).remove(prevTaskVo);
							}
						}

						this.logger.debug(
								"CRN List for First Call:--------->" + actualTaskCrnList + " :: taskList :: " + taskList
										+ " :: subject name :: " + vo.getPrimarySubject() + " :: final DD :: " + FDD);
						if (actualTaskCrnList.isEmpty()) {
							vo.setCrn("");
							vo.setTask("");
							vo.setPrimarySubject("");
							vo.setPrimarySubjectCountry("");
							vo.setItrm1("");
							vo.setItrm2("");
							vo.setFinalDueDate("");
							vo.setCaseStatus("");
							vo.setTotal(0L);
							mergedCALList.add(vo);
						} else {
							vo.setCrn(actualTaskCrnList);
							vo.setTask(taskList);
							vo.setPrimarySubject(priSubDetails);
							vo.setPrimarySubjectCountry(priSubCountryDetails);
							vo.setItrm1(IDD1);
							vo.setItrm2(IDD2);
							vo.setFinalDueDate(FDD);
							vo.setCaseStatus(caseStatus);
							String[] crnListCount = actualTaskCrnList.split(",");
							currentCRN = "";

							for (j = 0; j < crnListCount.length; ++j) {
								if (j == 0) {
									currentCRN = crnListCount[j];
								} else if (!currentCRN.contains(crnListCount[j])) {
									currentCRN = currentCRN + "," + crnListCount[j];
								}
							}

							this.logger.debug("tempCrn :: " + currentCRN);
							vo.setTotal((long) currentCRN.split(",").length);
							mergedCALList.add(vo);
						}
					}
				}
			}

			this.logger.debug("merged call list size-- :" + mergedCALList.size());
			String fName;
			if (mergedCALList.size() > 0) {
				this.logger.debug("case one");
				modelandview = new ModelAndView("misExcelDownloadPopup");
				fName = this.writeToExcel(mergedCALList, response, this.startDate, this.endDate, pHolidays);
				modelandview.addObject("fileName", fName);
				modelandview.addObject(this.LOADING_LIST, mergedCALList);
				this.nonEmptyFlag = this.FalseVal;
			} else if (currentAnalystLoadingList.size() > 0) {
				this.logger.debug("case2");
				modelandview = new ModelAndView("misExcelDownloadPopup");
				fName = this.writeToExcel(currentAnalystLoadingList, response, this.startDate, this.endDate, pHolidays);
				modelandview.addObject("fileName", fName);
				modelandview.addObject(this.LOADING_LIST, currentAnalystLoadingList);
				this.nonEmptyFlag = this.FalseVal;
			} else {
				this.logger.debug("case3");
				this.nonEmptyFlag = this.TrueVal;
				request.getSession().setAttribute(this.startDat, request.getParameter(this.recStartDate));
				request.getSession().setAttribute(this.endDat, request.getParameter(this.recEndDate));
				request.getSession().setAttribute(this.officeName, request.getParameter(this.OFFICE));
				this.logger.debug("startDate:: " + request.getParameter(this.recStartDate) + " :: endDate :: "
						+ request.getParameter(this.recEndDate) + " :: Office :: " + request.getParameter(this.OFFICE));
			}

			request.getSession().setAttribute(this.nonEmpty, this.nonEmptyFlag);
			return modelandview;
		} catch (UnsupportedOperationException var40) {
			var5 = AtlasUtils.getExceptionView(this.logger, var40);
		} catch (Exception var41) {
			var5 = AtlasUtils.getExceptionView(this.logger, var41);
			return var5;
		} finally {
			if (null != request.getSession().getAttribute("excelGeneration")) {
				request.getSession().removeAttribute("excelGeneration");
			}

		}

		return var5;
	}

	private String writeToExcel(List<CurrentAnalystLoadingVO> currentAnalystLoadingList, HttpServletResponse response,
			String startDate, String endDate, String phd) throws CMSException, ParseException {
		int countsubCell = 0;

		try {
			int MILLIS_IN_DAY = 86400000;
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy");
			SimpleDateFormat mon = new SimpleDateFormat("MMMM");
			Date date = sdf.parse(startDate);
			Date date2 = sdf.parse(endDate);
			String strDateFormat = "E";
			SimpleDateFormat sdfDay = new SimpleDateFormat(strDateFormat);
			List<String> lstHeader = new ArrayList();
			List<String> lstSubHeader1 = new ArrayList();
			List<String> lstSubHeader2 = new ArrayList();
			lstHeader.add(this.SUPERVISOR);
			lstHeader.add(this.ANALYST);
			lstHeader.add(this.TOTALCASES);
			lstHeader.add(this.PRIMARYSUBJECT);
			lstHeader.add(this.PRIMARYSUBCOUNTRY);
			lstHeader.add(this.TASK);
			if (mon.format(date).equalsIgnoreCase(mon.format(date2))) {
				this.month = mon.format(date);
			} else {
				this.month = mon.format(date) + "-" + mon.format(date2);
			}

			lstHeader.add(this.month);

			while (date.getTime() <= date2.getTime()) {
				++countsubCell;
				lstSubHeader1.add(String.valueOf(date.getDate()));
				String day = sdfDay.format(date);
				this.logger.debug(String.valueOf(date.getDate()));
				lstSubHeader2.add(String.valueOf(day.charAt(0)));
				date = new Date(date.getTime() + (long) MILLIS_IN_DAY);
			}

			List<HashMap<String, String>> dataList = new ArrayList();
			Iterator iterator = currentAnalystLoadingList.iterator();

			while (iterator.hasNext()) {
				HashMap<String, String> datamap = new HashMap();
				CurrentAnalystLoadingVO currentAnalystLoadingVO = (CurrentAnalystLoadingVO) iterator.next();
				String satValue = currentAnalystLoadingVO.getSupervisor() + this.SATSeprator
						+ currentAnalystLoadingVO.getAnalyst() + this.SATSeprator + currentAnalystLoadingVO.getTotal();
				datamap.put(this.SAT, satValue);
				datamap.put(this.PRIMARYSUBJECT, currentAnalystLoadingVO.getPrimarySubject());
				datamap.put(this.PRIMARYSUBCOUNTRY, currentAnalystLoadingVO.getPrimarySubjectCountry());
				datamap.put(this.TASK, currentAnalystLoadingVO.getTask());
				datamap.put(this.CRN, String.valueOf(currentAnalystLoadingVO.getCrn()));
				datamap.put(this.ITRM1, String.valueOf(currentAnalystLoadingVO.getItrm1()));
				datamap.put(this.ITRM2, String.valueOf(currentAnalystLoadingVO.getItrm2()));
				datamap.put(this.FINALDATE, String.valueOf(currentAnalystLoadingVO.getFinalDueDate()));
				datamap.put(this.LEAVEDATE, String.valueOf(currentAnalystLoadingVO.getLeaveDate()));
				datamap.put(this.LEAVETYPE, String.valueOf(currentAnalystLoadingVO.getLeaveType()));
				datamap.put(this.CASESTATUS, String.valueOf(currentAnalystLoadingVO.getCaseStatus()));
				dataList.add(datamap);
			}

			this.logger.debug("cellCount---------:" + countsubCell);
			return CurrentAnalystLoadingExcelDownloader.writeToExcel(lstHeader, lstSubHeader1, lstSubHeader2, dataList,
					this.CURRENT_ANALYST_LOADING, (short) 9, (short) 12, response, this.CURRENT_ANALYST_LOADING,
					countsubCell, startDate, endDate, phd);
		} catch (ClassCastException var23) {
			throw new CMSException(this.logger, var23);
		} catch (NullPointerException var24) {
			throw new CMSException(this.logger, var24);
		}
	}
}