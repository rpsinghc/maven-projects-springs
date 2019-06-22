package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IReportTypeMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.history.AtlasHistoryVO;
import com.worldcheck.atlas.vo.masters.REMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.report.SubReportTypeVO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONReportTypeMaster extends JSONMultiActionController {
	private static final String INITIALS_USE_CRN = "initialsUseCRN";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.JSONReportTypeMaster");
	private String JSONVIEW = "jsonView";
	private String REPORT_TYPE_SEARCH_LIST = "reportTypeSearchList";
	private String RE_FOR_REPORT = "reForReportList";
	private String BOTH_RE_FOR_REPORT = "bothReForReportList";
	private String RE_LIST = "reList";
	private String RPT_ID = "rptId";
	private String SUB_RPT_ID = "subRptId";
	private String REPORT_NAME = "rptName";
	private ModelAndView viewForJson = null;
	private IReportTypeMaster reportTypeManager = null;
	private String REPORT_TYPE_GRIDLIST = "reportTypeMaster";
	private String STATUS = "reportTypeStatus";
	private String RESULT = "result";
	private String SUCCESS = "success";
	private int count;
	private String REPORTTYPEID = "reportTypeId";

	public void setReportTypeManager(IReportTypeMaster reportTypeManager) {
		this.reportTypeManager = reportTypeManager;
	}

	public ModelAndView searchReportType(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) {
		ModelAndView viewForJson = new ModelAndView(this.JSONVIEW);
		this.logger.debug("inside json report type controller ");

		try {
			String rptMasterId = this.reportTypeManager.getReportTypeMasterId(reportTypeMasterVO);
			this.logger.debug("JSONReportTypeMaster.searchReportType()       :    " + rptMasterId);
			if (rptMasterId != null && !rptMasterId.equals("")) {
				List<ReportTypeMasterVO> list = this.reportTypeManager.searchReportType(rptMasterId);
				Map<String, String> finalMap = this.getCommaSeperatedList(list);
				List<ReportTypeMasterVO> listSub = this.reportTypeManager.searchSubReportType(rptMasterId);
				Map<String, String> finalMapSub = this.getSubRptCommaSeperatedList(listSub);
				List<ReportTypeMasterVO> finallist = this.getSearchResult(finalMap, finalMapSub);
				viewForJson.addObject(this.REPORT_TYPE_SEARCH_LIST, finallist);
			}

			return viewForJson;
		} catch (CMSException var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		} catch (Exception var12) {
			return AtlasUtils.getExceptionView(this.logger, var12);
		}
	}

	private Map<String, String> getCommaSeperatedList(List<ReportTypeMasterVO> list) {
		String commaSeparatedValues = "";
		String comma = ",";
		Map<String, String> mapCSV = new HashMap();
		List<String> listStr = new ArrayList();

		String key;
		String value;
		for (Iterator var7 = list.iterator(); var7.hasNext(); mapCSV.put(key, value)) {
			ReportTypeMasterVO reportTypeMasterVO = (ReportTypeMasterVO) var7.next();
			key = reportTypeMasterVO.getReportType() + comma + reportTypeMasterVO.getReportTypeCode() + comma
					+ reportTypeMasterVO.getReportTypeId();
			value = (String) mapCSV.get(key);
			if (value == null) {
				value = "";
			}

			String reVal = reportTypeMasterVO.getResearchElement();
			String lisVal = reportTypeMasterVO.getReportType() + comma + reportTypeMasterVO.getResearchElement();
			if (!listStr.contains(lisVal)) {
				value = value + reVal + comma;
				listStr.add(lisVal);
			}
		}

		return mapCSV;
	}

	public ModelAndView getReForRpt(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) {
		ModelAndView viewForJson = new ModelAndView(this.JSONVIEW);
		this.logger.debug("inside json report type controller getReForRpt   ");
		String rptId = request.getParameter(this.RPT_ID);
		long rptIdVal = 0L;
		if (rptId != null && !rptId.equals("")) {
			rptIdVal = Long.parseLong(rptId);
		}

		try {
			List<REMasterVO> listVO = this.reportTypeManager.getReForRpt(rptIdVal);
			viewForJson.addObject(this.RE_FOR_REPORT, listVO);
			return viewForJson;
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView getCmpOrIndReForRpt(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) {
		ModelAndView viewForJson = new ModelAndView(this.JSONVIEW);
		this.logger.debug("inside json report type controller getCmpOrIndReForRpt   ");
		String rptId = request.getParameter(this.RPT_ID);
		long rptIdVal = 0L;
		if (rptId != null && !rptId.equals("")) {
			rptIdVal = Long.parseLong(rptId);
		}

		try {
			List<ReportTypeMasterVO> listVO = this.reportTypeManager.getCmpOrIndReForRpt();
			viewForJson.addObject(this.BOTH_RE_FOR_REPORT, listVO);
			return viewForJson;
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView getsubReportType(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) {
		ModelAndView viewForJson = new ModelAndView(this.JSONVIEW);
		String rptId = request.getParameter(this.RPT_ID);
		long rptIdVal = 0L;
		if (rptId != null && !rptId.equals("")) {
			rptIdVal = Long.parseLong(rptId);
		}

		try {
			List<ReportTypeMasterVO> list = this.reportTypeManager.getSubReport(rptIdVal);
			List<ReportTypeMasterVO> finallist = this.getCommaSeperatedListforSubReport(list);
			viewForJson.addObject(this.REPORT_TYPE_SEARCH_LIST, finallist);
			return viewForJson;
		} catch (CMSException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}
	}

	public ModelAndView getReForSubReport(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) {
		ModelAndView viewForJson = new ModelAndView(this.JSONVIEW);
		String subRptId = request.getParameter(this.SUB_RPT_ID);
		long subRptIdVal = 0L;
		if (subRptId != null && !subRptId.equals("")) {
			subRptIdVal = Long.parseLong(subRptId);
		}

		try {
			List<ReportTypeMasterVO> reList = this.reportTypeManager.getReForSubReport();
			viewForJson.addObject(this.REPORT_TYPE_SEARCH_LIST, reList);
			return viewForJson;
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	private List<ReportTypeMasterVO> getCommaSeperatedListforSubReport(List<ReportTypeMasterVO> list) {
		this.logger.debug("Inside JSONReportTypeMaster:getCommaSeperatedListforSubReport");
		List<ReportTypeMasterVO> finalList = new ArrayList();
		Iterator itr = list.iterator();

		while (itr.hasNext()) {
			ReportTypeMasterVO rptvo = (ReportTypeMasterVO) itr.next();
			ReportTypeMasterVO vo = new ReportTypeMasterVO();
			vo.setSubReportType(rptvo.getSubReportType());
			vo.setInitialsUseEndCRN(rptvo.getInitialsUseEndCRN());
			vo.setSubReportTypeId(rptvo.getSubReportTypeId());
			vo.setResearchElement(rptvo.getResearchElement());
			vo.setResearchElementID(rptvo.getResearchElementID());
			vo.setHdnResearchElementID(rptvo.getResearchElementID());
			vo.setHdnResearchElement(rptvo.getResearchElement());
			vo.setSubreportStatus(rptvo.getSubreportStatus());
			vo.setSubReportTypeCode(rptvo.getSubReportTypeCode());
			finalList.add(vo);
		}

		return finalList;
	}

	public ModelAndView getReList(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) {
		ModelAndView viewForJson = new ModelAndView(this.JSONVIEW);
		this.logger.debug("inside json report type controller getReForRpt   ");

		try {
			List<REMasterVO> listVO = this.reportTypeManager.getReList();
			viewForJson.addObject(this.RE_LIST, listVO);
			return viewForJson;
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	private Map<String, String> getSubRptCommaSeperatedList(List<ReportTypeMasterVO> list) {
		String commaSeparatedValues = "";
		String comma = ",";
		Map<String, String> mapCSV = new HashMap();
		List<String> listStr = new ArrayList();

		String key;
		String value;
		for (Iterator var7 = list.iterator(); var7.hasNext(); mapCSV.put(key, value)) {
			ReportTypeMasterVO reportTypeMasterVO = (ReportTypeMasterVO) var7.next();
			this.logger.debug("subreport type is : " + reportTypeMasterVO.getSubReportType());
			key = reportTypeMasterVO.getReportType() + comma + reportTypeMasterVO.getReportTypeCode() + comma
					+ reportTypeMasterVO.getReportTypeId() + comma + reportTypeMasterVO.getSubReportType();
			value = (String) mapCSV.get(key);
			if (value == null) {
				value = "";
			}

			String reVal = reportTypeMasterVO.getResearchElement();
			String lisVal = reportTypeMasterVO.getReportType() + comma + reportTypeMasterVO.getResearchElement();
			if (!listStr.contains(lisVal)) {
				value = value + reVal + comma;
				listStr.add(lisVal);
			}
		}

		return mapCSV;
	}

	private List<ReportTypeMasterVO> getSearchResult(Map<String, String> mapCSV, Map<String, String> mapCSVSub) {
		Iterator it = mapCSVSub.entrySet().iterator();
		ArrayList finalList = new ArrayList();

		ReportTypeMasterVO vo;
		Entry pairs;
		String reValues;
		String keyVal;
		String[] keys;
		while (it.hasNext()) {
			vo = new ReportTypeMasterVO();
			pairs = (Entry) it.next();
			reValues = (String) pairs.getValue();
			keyVal = (String) pairs.getKey();
			keys = keyVal.split(",");
			vo.setReportType(keys[0]);
			vo.setReportTypeCode(keys[1]);
			vo.setReportTypeId(Long.parseLong(keys[2]));
			vo.setSubReportType(keys[3]);
			if (reValues != null && !reValues.equals("")) {
				reValues = reValues.substring(0, reValues.lastIndexOf(44));
			}

			vo.setResearchElement(reValues);
			finalList.add(vo);
		}

		it = mapCSV.entrySet().iterator();

		while (it.hasNext()) {
			vo = new ReportTypeMasterVO();
			pairs = (Entry) it.next();
			reValues = (String) pairs.getValue();
			keyVal = (String) pairs.getKey();
			keys = keyVal.split(",");
			vo.setReportType(keys[0]);
			vo.setReportTypeCode(keys[1]);
			vo.setReportTypeId(Long.parseLong(keys[2]));
			vo.setSubReportType("");
			if (reValues != null && !reValues.equals("")) {
				reValues = reValues.substring(0, reValues.lastIndexOf(44));
			}

			vo.setResearchElement(reValues);
			finalList.add(vo);
		}

		Collections.sort(finalList);
		return finalList;
	}

	public ModelAndView checkReportIfExist(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) throws Exception {
		this.logger.debug("#################Check for ReportName & initialCRN ####################");
		ModelAndView viewForJson = new ModelAndView(this.JSONVIEW);
		String rptName = request.getParameter(this.REPORT_NAME);
		String initialCRN = request.getParameter("initialsUseCRN");
		this.logger.debug("rptName::" + rptName + "  ### intialCRN::" + initialCRN);
		int count = false;
		boolean var8 = false;

		try {
			if (rptName != null) {
				int count = this.reportTypeManager.checkReportIfExist(rptName);
				if (count > 0) {
					viewForJson.addObject("success", "true");
				} else {
					viewForJson.addObject("success", "false");
				}
			}

			if (initialCRN != null) {
				this.logger.debug("initialCRN::" + initialCRN);
				int initialCRNStatus = this.reportTypeManager.checkInitialCRNExist(initialCRN);
				if (initialCRNStatus > 0) {
					viewForJson.addObject("initialCRNStatus", "true");
				} else {
					viewForJson.addObject("initialCRNStatus", "false");
				}
			}

			return viewForJson;
		} catch (CMSException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}
	}

	public ModelAndView deActivateReportType(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) throws Exception {
		ModelAndView mv = null;
		String var5 = "deActivate ReportType";

		try {
			mv = new ModelAndView(this.JSONVIEW);
			String rptId = request.getParameter(this.RPT_ID);
			String status = request.getParameter(this.STATUS);
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			this.logger.debug("JsonReport Type Master Change Status update by::::::::::: " + userBean.getUserName());
			String userName = userBean.getUserName();
			if (rptId != null && !rptId.equals("")) {
				String resultMessage = this.reportTypeManager.changeStatus(rptId, status, userName);
				if (resultMessage.split("#")[1].equals("success")) {
					mv.addObject(this.RESULT, this.SUCCESS);
				} else {
					mv.addObject(this.RESULT, resultMessage.split("#")[0]);
				}

				ResourceLocator.self().getCacheService().addToCacheRunTime("REPORT_TYPE_MASTER");
				if (status.equals("1")) {
					this.logger.info(reportTypeMasterVO.getReportType() + " successfully deactivate");
				} else {
					this.logger.info(reportTypeMasterVO.getReportType() + " successfully activate");
				}
			}

			return mv;
		} catch (CMSException var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		} catch (Exception var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		}
	}

	public ModelAndView searchReportTypeMaster(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) {
		ModelAndView viewForJson = null;

		try {
			viewForJson = new ModelAndView(this.JSONVIEW);
			this.logger.debug("Inside the JSONReportTypeMaster" + request.getParameter("start"));
			this.logger.debug(request.getParameter("limit"));
			this.logger.debug("ReportType :>>>>>>>>" + reportTypeMasterVO.getReportType() + " Status ::>>>>>>>>"
					+ reportTypeMasterVO.getReportTypeStatus());
			ArrayList reportTypeGridList = null;
			if ("".equals(reportTypeMasterVO.getReportType()) && "".equals(reportTypeMasterVO.getReportTypeStatus())) {
				reportTypeGridList = new ArrayList();
			} else {
				reportTypeGridList = (ArrayList) this.reportTypeManager.getReportTypeGrid(reportTypeMasterVO,
						Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
						request.getParameter("dir"));
				this.logger.info("Row Return:" + reportTypeGridList.size());
				this.count = this.reportTypeManager.getReportTypeCount(reportTypeMasterVO);
				viewForJson.addObject("total", this.count);
			}

			viewForJson.addObject(this.REPORT_TYPE_GRIDLIST, reportTypeGridList);
			return viewForJson;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView isSubReportTypeExist(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) {
		ModelAndView viewForJson = null;
		boolean resultSubReportType = false;

		try {
			viewForJson = new ModelAndView(this.JSONVIEW);
			String subReportTypeName = request.getParameter("subReportTypeName");
			String reportTypeId = request.getParameter("reportTypeID");
			this.logger.debug("Subreport Type" + subReportTypeName + "Report Type ID" + reportTypeId);
			SubReportTypeVO subreportTypeVo = new SubReportTypeVO();
			subreportTypeVo.setSubReportName(subReportTypeName);
			subreportTypeVo.setReportTypeId(reportTypeId);
			if (subReportTypeName != null && subReportTypeName.length() != 0) {
				resultSubReportType = this.reportTypeManager.isSubReportTypeExist(subreportTypeVo);
			}

			this.logger.debug("resultSubReportType:" + resultSubReportType);
			if (resultSubReportType) {
				viewForJson.addObject(this.SUCCESS, "true");
			} else {
				viewForJson.addObject(this.SUCCESS, "false");
			}

			return viewForJson;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView isSubReportInitialExist(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) {
		ModelAndView viewForJson = null;
		boolean resultSubReportTypeInitial = false;

		try {
			viewForJson = new ModelAndView(this.JSONVIEW);
			String subReportIntialuse = request.getParameter("subReportIntialuse");
			if (subReportIntialuse != null && subReportIntialuse.length() != 0) {
				resultSubReportTypeInitial = this.reportTypeManager.isSubReportInitialExist(subReportIntialuse);
			}

			this.logger.debug("resultSubReportType:" + resultSubReportTypeInitial);
			if (resultSubReportTypeInitial) {
				viewForJson.addObject(this.SUCCESS, "true");
			} else {
				viewForJson.addObject(this.SUCCESS, "false");
			}

			return viewForJson;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView isReportTypeAssociated(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView("jsonView");
			this.logger.debug("ReportType Master id:" + reportTypeMasterVO.getReportTypeId());
			reportTypeMasterVO = this.reportTypeManager
					.checkAssociatedMaster(String.valueOf(reportTypeMasterVO.getReportTypeId()));
			if (!reportTypeMasterVO.getTotalCRN().equalsIgnoreCase("0")
					|| !reportTypeMasterVO.getClientReqMaster().equalsIgnoreCase("0")
					|| !reportTypeMasterVO.getRptClientMap().equalsIgnoreCase("0")
					|| !reportTypeMasterVO.getReMap().equalsIgnoreCase("0")
					|| !reportTypeMasterVO.getReportDownloadMaster().equalsIgnoreCase("0")
					|| !reportTypeMasterVO.getTotalRecurrCRN().equalsIgnoreCase("0")) {
				mv.addObject("isAssociated", true);
				mv.addObject("reportTypeMasterVO", reportTypeMasterVO);
			}

			return mv;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView isSubreportTypeAssociated(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) {
		this.logger.debug("Inside JSONREPORTTYPEMASTER:isSubreportTypeAssociated");
		ModelAndView mv = null;

		try {
			mv = new ModelAndView("jsonView");
			String subReportTypeID = request.getParameter("subreportreportTypeId");
			this.logger.debug("Subreport Type ID is :" + subReportTypeID);
			reportTypeMasterVO = this.reportTypeManager.checkAssociatedSubreport(subReportTypeID);
			if (!reportTypeMasterVO.getTotalCRN().equalsIgnoreCase("0")
					|| !reportTypeMasterVO.getTotalRecurrCRN().equalsIgnoreCase("0")
					|| !reportTypeMasterVO.getTotalCRNSLSubReport().equalsIgnoreCase("0")
					|| !reportTypeMasterVO.getTotalRecurrCRNSLSubReport().equalsIgnoreCase("0")) {
				mv.addObject("issubReportAssociated", true);
				mv.addObject("reportTypeMasterVO", reportTypeMasterVO);
			}

			return mv;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getReportHistory(HttpServletRequest request, HttpServletResponse response,
			ReportTypeMasterVO reportTypeMasterVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug(
					"Inside getReportHistory#### " + reportTypeMasterVO.getReportTypeId() + " Report Type ID >>>   ");
			AtlasHistoryVO atlasHistoryVO = new AtlasHistoryVO();
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			int count = false;
			atlasHistoryVO.setHistoryKey("" + reportTypeMasterVO.getReportTypeId());
			atlasHistoryVO.setStart(start + 1);
			atlasHistoryVO.setLimit(start + limit);
			atlasHistoryVO.setSortColumnName(request.getParameter("sort"));
			atlasHistoryVO.setSortType(request.getParameter("dir"));
			List<AtlasHistoryVO> reportHistoryList = this.reportTypeManager.getReportHistory(atlasHistoryVO);
			int count = this.reportTypeManager.getReportHistoryCount(atlasHistoryVO);
			this.logger.debug("List Size:::::: " + reportHistoryList.size());
			this.logger.debug("Report Type Count::::" + count);
			modelAndView.addObject("reportTypeIdList", reportHistoryList);
			modelAndView.addObject("total", count);
			return modelAndView;
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}
}