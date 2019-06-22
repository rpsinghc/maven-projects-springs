package com.worldcheck.atlas.bl.masters;

import com.integrascreen.orders.SubReportTypeMasterVO;
import com.integrascreen.orders.UPMasterVO;
import com.worldcheck.atlas.bl.interfaces.IReportTypeMaster;
import com.worldcheck.atlas.dao.masters.ReportTypeDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ISISResponseVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasHistoryUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.history.AtlasHistoryVO;
import com.worldcheck.atlas.vo.masters.REMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.report.SubReportTypeVO;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.simple.JSONValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ReportTypeManager implements IReportTypeMaster {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.masters.ReportTypeManager");
	private String ISRPT = "ISRPT";
	private ReportTypeDAO reportTypeDAO;
	private AtlasHistoryUtil atlasHistoryUtil;
	private String needSubreportFlag = "false";
	private String haveDeactiveSubReport = "false";

	public void setReportTypeDAO(ReportTypeDAO reportTypeDAO) {
		this.reportTypeDAO = reportTypeDAO;
	}

	public void setAtlasHistoryUtil(AtlasHistoryUtil atlasHistoryUtil) {
		this.atlasHistoryUtil = atlasHistoryUtil;
	}

	public String addReportISIS(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		SubReportTypeMasterVO[] subReportTypeMasterVOArray = (SubReportTypeMasterVO[]) null;
		SubReportTypeVO[] subReportTypeVOHistoryArray = (SubReportTypeVO[]) null;
		this.haveDeactiveSubReport = "false";

		try {
			long reptSeq = this.reportTypeDAO.getReportSeq();
			this.logger.debug("Report type sequence" + reptSeq);
			String reptCode = this.ISRPT + reptSeq;
			reportTypeMasterVO.setReportTypeCode(reptCode);
			reportTypeMasterVO.setReportTypeId(reptSeq);
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Report");
			upMasterVO.setUpdateType("Insert");
			com.integrascreen.orders.ReportTypeMasterVO masterObject = new com.integrascreen.orders.ReportTypeMasterVO();
			this.logger.debug("#ReportTypeCode::" + reportTypeMasterVO.getReportTypeCode() + "#ReportType::"
					+ reportTypeMasterVO.getReportType() + "#Initials to use at CRN::"
					+ reportTypeMasterVO.getInitialsUseCRN() + "#NeedSubReportType::"
					+ reportTypeMasterVO.getNeedSubReportType() + "#Turnaround Time::"
					+ reportTypeMasterVO.getTurnaroundTime() + "#Report Type Status::"
					+ reportTypeMasterVO.getReportTypeStatus() + "#Associated Research Element::"
					+ reportTypeMasterVO.getResearchElement() + "#Sub Report Type::"
					+ reportTypeMasterVO.getSubReportType());
			masterObject.setReportypeCode(reportTypeMasterVO.getReportTypeCode());
			masterObject.setDescription(reportTypeMasterVO.getReportType());
			if (reportTypeMasterVO.getTurnaroundTime() != null) {
				masterObject.setTAT(Integer.parseInt(reportTypeMasterVO.getTurnaroundTime()));
			}

			if (reportTypeMasterVO.getNeedSubReportType() != null) {
				if (reportTypeMasterVO.getNeedSubReportType().equalsIgnoreCase("2")) {
					masterObject.setHasSubReport(0);
					reportTypeMasterVO.setHasSubReport("0");
				} else if (reportTypeMasterVO.getNeedSubReportType().equalsIgnoreCase("1")) {
					masterObject.setHasSubReport(1);
					reportTypeMasterVO.setHasSubReport("1");
				}

				reportTypeMasterVO.setNeedSubreportTypeFlag(this.needSubreportFlag);
			}

			if (reportTypeMasterVO.getNeedSubReportType().equals("2") && reportTypeMasterVO.getResearchElement() != null
					&& reportTypeMasterVO.getResearchElement().trim().length() > 0) {
				masterObject.setRLDefaultREs(reportTypeMasterVO.getResearchElement());
			}

			if (reportTypeMasterVO.getNeedSubReportType() != null
					&& reportTypeMasterVO.getNeedSubReportType().equals("1")) {
				String[] modifiedRecords = reportTypeMasterVO.getModifiedRecords();
				String[] subReportCode = new String[modifiedRecords.length];
				long[] subReportID = new long[modifiedRecords.length];
				subReportTypeMasterVOArray = new SubReportTypeMasterVO[modifiedRecords.length];
				subReportTypeVOHistoryArray = new SubReportTypeVO[modifiedRecords.length];

				for (int i = 0; i < modifiedRecords.length; ++i) {
					long subRptSeq = this.reportTypeDAO.getSubReportSeq();
					this.logger.debug("Subreport" + (i + 1) + subRptSeq);
					subReportID[i] = subRptSeq;
					String subReptCode = "SPT" + subRptSeq;
					subReportCode[i] = subReptCode;
					SubReportTypeMasterVO subReportTypeMasterVO = new SubReportTypeMasterVO();
					SubReportTypeVO subReportTypeHistroy = new SubReportTypeVO();
					String JSONstring = modifiedRecords[i];
					Map jsonObject = (Map) JSONValue.parse(JSONstring);
					this.logger.debug(
							i + 1 + "##SubReportType subReportType::" + (String) jsonObject.get("subReportType"));
					this.logger.debug(i + 1 + "##SubReportType initialsUseEndCRN::"
							+ (String) jsonObject.get("initialsUseEndCRN"));
					this.logger.debug(i + 1 + "##Assosiate research element#####::"
							+ (String) jsonObject.get("researchElementID"));
					this.logger.debug(i + 1 + "##Subreport ID #####::" + subRptSeq);
					this.logger.debug(i + 1 + "##SubreportCode::" + subReptCode);
					subReportTypeMasterVO.setSubReportTypeCode(subReptCode);
					subReportTypeMasterVO.setSubReportTypeDescription((String) jsonObject.get("subReportType"));
					subReportTypeHistroy.setSubReportTypeCode(subReptCode);
					subReportTypeHistroy.setSubReportName((String) jsonObject.get("subReportType"));
					String associatedResearchElement = (String) jsonObject.get("researchElementID");
					String reseachElementsNames = (String) jsonObject.get("researchElement");
					if (associatedResearchElement != null && associatedResearchElement.trim().length() > 0) {
						subReportTypeMasterVO.setSRLDefaultREs(associatedResearchElement);
					}

					if (reseachElementsNames != null && reseachElementsNames.trim().length() > 0) {
						subReportTypeHistroy.setAssociatedResearchElements(reseachElementsNames);
					}

					subReportTypeMasterVO.setSRStatus(1);
					subReportTypeMasterVO.setSRLAction("ADD");
					subReportTypeHistroy.setSRStatus("1");
					subReportTypeHistroy.setSRLAction("Add");
					subReportTypeHistroy.setSubCrnInitial((String) jsonObject.get("initialsUseEndCRN"));
					subReportTypeVOHistoryArray[i] = subReportTypeHistroy;
					subReportTypeMasterVOArray[i] = subReportTypeMasterVO;
				}

				masterObject.setSubReportType(subReportTypeMasterVOArray);
				reportTypeMasterVO.setSubReportTypeVO(subReportTypeVOHistoryArray);
				reportTypeMasterVO.setSubReportCode(subReportCode);
				reportTypeMasterVO.setSubReportID(subReportID);
			}

			masterObject.setRLActionType("ADD");
			masterObject.setRLStatus(reportTypeMasterVO.getReportTypeStatus().equals("0") ? 0 : 1);
			upMasterVO.setReportTypeMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			if (isisResponseVO.isSuccess()) {
				this.logger.debug("Success response from EDDO");
				String msg = this.addReportType(reportTypeMasterVO) + "#success";

				String xml;
				try {
					xml = this.createHistoryData(reportTypeMasterVO);
					this.logger.debug("History Data" + xml);
				} catch (ParserConfigurationException var23) {
					throw new CMSException(this.logger, var23);
				} catch (Exception var24) {
					throw new CMSException(this.logger, var24);
				}

				AtlasHistoryVO atlasHistoryVO = new AtlasHistoryVO();
				atlasHistoryVO.setHistoryKey("" + reptSeq);
				atlasHistoryVO.setNewInfo(xml);
				atlasHistoryVO.setOldInfo("");
				atlasHistoryVO.setAction("ADD");
				atlasHistoryVO.setUpdatedBy(reportTypeMasterVO.getUserName());
				String message = this.atlasHistoryUtil.insertData("REPORT_TYPE_MASTER_HISTORY", atlasHistoryVO);
				this.logger.debug("ADD REPORT_TYPE_MASTER_HISTORY::::::::::::::" + message);
				return msg;
			} else if (isisResponseVO.getResponseVO().getErrorCode().equalsIgnoreCase("P001")) {
				return "Report" + reportTypeMasterVO.getReportType() + "does not exist in EDDO" + "#failure";
			} else if (isisResponseVO.getResponseVO().getErrorCode().equalsIgnoreCase("P002")) {
				return "Report" + reportTypeMasterVO.getReportType() + "is Deactive currently" + "#failure";
			} else {
				return isisResponseVO.getResponseVO().getMessage() != null
						&& isisResponseVO.getResponseVO().getMessage().trim().length() > 0
								? isisResponseVO.getResponseVO().getMessage() + "#failure"
								: "Unable to process this request. Error#failure";
			}
		} catch (CMSException var25) {
			throw new CMSException(this.logger, var25);
		}
	}

	public String addReportType(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		this.reportTypeDAO.addReportType(reportTypeMasterVO);
		String[] subReportCode;
		if (reportTypeMasterVO.getNeedSubReportType() != null
				&& reportTypeMasterVO.getNeedSubReportType().equals("2")) {
			String reStr = reportTypeMasterVO.getResearchElement();
			subReportCode = reStr.split(",");

			for (int i = 0; i < subReportCode.length; ++i) {
				this.logger.debug("going to isnsert RE ::::  " + subReportCode[i]);
				String reIds = subReportCode[i];
				this.reportTypeDAO.insertReMap(reIds, reportTypeMasterVO.getReportTypeId(),
						reportTypeMasterVO.getUserName());
			}
		} else if (reportTypeMasterVO.getNeedSubReportType() != null
				&& reportTypeMasterVO.getNeedSubReportType().equals("1")) {
			String[] modifiedRecords = reportTypeMasterVO.getModifiedRecords();
			subReportCode = reportTypeMasterVO.getSubReportCode();
			long[] subReportID = reportTypeMasterVO.getSubReportID();

			for (int i = 0; i < modifiedRecords.length; ++i) {
				ReportTypeMasterVO rptVO = new ReportTypeMasterVO();
				String JSONstring = modifiedRecords[i];
				String subRptCode = subReportCode[i];
				long subRptId = subReportID[i];
				Map jsonObject = (Map) JSONValue.parse(JSONstring);
				this.logger.debug(
						"ReportTypeManager.addReportType() subreport  " + (String) jsonObject.get("subReportType"));
				this.logger.debug(
						"ReportTypeManager.addReportType() re list  " + (String) jsonObject.get("researchElement"));
				this.logger.debug(
						"ReportTypeManager.addReportType()  inital " + (String) jsonObject.get("initialsUseEndCRN"));
				rptVO.setSubReportType((String) jsonObject.get("subReportType"));
				rptVO.setReportTypeId(reportTypeMasterVO.getReportTypeId());
				rptVO.setUserName(reportTypeMasterVO.getUserName());
				rptVO.setInitialsUseEndCRN((String) jsonObject.get("initialsUseEndCRN"));
				rptVO.setReportTypeStatus(reportTypeMasterVO.getReportTypeStatus());
				rptVO.setSubReportTypeId(subRptId);
				rptVO.setSubReportTypeCode(subRptCode);
				this.reportTypeDAO.insertSubReport(rptVO);
				this.logger.debug("Generated  sub report type id is ::::   " + subRptId);
				String reStr = (String) jsonObject.get("researchElementID");
				String[] reList = reStr.split(",");

				for (int j = 0; j < reList.length; ++j) {
					this.logger.debug("going to isnsert RE ::::  " + reList[j]);
					String reIds = reList[j];
					this.reportTypeDAO.insertSubReportReMap(reIds, subRptId, reportTypeMasterVO.getUserName());
				}
			}
		}

		return "message#success";
	}

	public List<ReportTypeMasterVO> searchReportType(String rptMasterId) throws CMSException {
		List<ReportTypeMasterVO> list = this.reportTypeDAO.searchReportType(rptMasterId);
		return list;
	}

	public List<REMasterVO> getReForRpt(long rptIdVal) throws CMSException {
		List<REMasterVO> listVO = this.reportTypeDAO.getReForRpt(rptIdVal);
		return listVO;
	}

	public List<ReportTypeMasterVO> getCmpOrIndReForRpt() throws CMSException {
		List<ReportTypeMasterVO> listVO = this.reportTypeDAO.getCmpOrIndReForRpt();
		return listVO;
	}

	public ReportTypeMasterVO getRptInfo(long rptIdVal) throws CMSException {
		ReportTypeMasterVO vo = null;
		vo = this.reportTypeDAO.getRptInfo(rptIdVal);
		return vo;
	}

	public List<ReportTypeMasterVO> getSubReport(long rptIdVal) throws CMSException {
		List<ReportTypeMasterVO> list = this.reportTypeDAO.getSubReport(rptIdVal);

		ReportTypeMasterVO var5;
		for (Iterator itr = list.iterator(); itr.hasNext(); var5 = (ReportTypeMasterVO) itr.next()) {
			;
		}

		return list;
	}

	public List<ReportTypeMasterVO> getReForSubReport() throws CMSException {
		List<ReportTypeMasterVO> list = this.reportTypeDAO.getReForSubReport();
		return list;
	}

	public List<REMasterVO> getReList() throws CMSException {
		List<REMasterVO> list = this.reportTypeDAO.getReList();
		return list;
	}

	public int getSubRptCount(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		int count = false;
		int count = this.reportTypeDAO.getSubRptCount(reportTypeMasterVO);
		return count;
	}

	public List<ReportTypeMasterVO> searchSubReportType(String rptMasterId) throws CMSException {
		List<ReportTypeMasterVO> list = this.reportTypeDAO.searchSubReportType(rptMasterId);
		return list;
	}

	public String updateReportISIS(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		SubReportTypeMasterVO[] subReportTypeMasterVOArray = (SubReportTypeMasterVO[]) null;
		ReportTypeMasterVO newRPTVO = new ReportTypeMasterVO();
		ReportTypeMasterVO oldRPTVO = new ReportTypeMasterVO();
		SubReportTypeVO[] newSubreportInfoArray = (SubReportTypeVO[]) null;
		SubReportTypeVO[] oldSubreportInfoArray = (SubReportTypeVO[]) null;
		this.haveDeactiveSubReport = "false";
		String deactiveSubreportList = "";
		String activeSubreportList = "";
		String action = "UPDATE";
		String needSubreportFlag = "false";
		UPMasterVO upMasterVO = new UPMasterVO();
		upMasterVO.setMaster("Report");
		upMasterVO.setUpdateType("Update");
		com.integrascreen.orders.ReportTypeMasterVO masterObject = new com.integrascreen.orders.ReportTypeMasterVO();
		String isReportStatusChangeFlag = reportTypeMasterVO.getIsRPTStatusChange();
		this.logger.debug("#ReportTypeCode::" + reportTypeMasterVO.getReportTypeCode() + "#ReportType::"
				+ reportTypeMasterVO.getReportType() + "#Initials to use at CRN::"
				+ reportTypeMasterVO.getInitialsUseCRN() + "#NeedSubReportType::"
				+ reportTypeMasterVO.getNeedSubReportType() + "#Turnaround Time::"
				+ reportTypeMasterVO.getTurnaroundTime() + "#Report Type Status::"
				+ reportTypeMasterVO.getReportTypeStatus() + "#Associated Research Element::"
				+ reportTypeMasterVO.getResearchElement() + "#Sub Report Type::" + reportTypeMasterVO.getSubReportType()
				+ "#Is report status change:" + reportTypeMasterVO.getIsRPTStatusChange());
		masterObject.setReportypeCode(reportTypeMasterVO.getReportTypeCode());
		masterObject.setDescription(reportTypeMasterVO.getReportType());
		masterObject.setTAT(Integer.parseInt(reportTypeMasterVO.getTurnaroundTime()));
		if (!reportTypeMasterVO.getHdnInitialsUseCRN().equalsIgnoreCase(reportTypeMasterVO.getInitialsUseCRN())) {
			newRPTVO.setInitialsUseCRN(reportTypeMasterVO.getInitialsUseCRN());
			oldRPTVO.setInitialsUseCRN(reportTypeMasterVO.getHdnInitialsUseCRN());
		}

		if (!reportTypeMasterVO.getHdnTurnaroundTime().equalsIgnoreCase(reportTypeMasterVO.getTurnaroundTime())) {
			newRPTVO.setTurnaroundTime(reportTypeMasterVO.getTurnaroundTime());
			oldRPTVO.setTurnaroundTime(reportTypeMasterVO.getHdnTurnaroundTime());
		}

		newRPTVO.setNeedSubReportType(reportTypeMasterVO.getNeedSubReportType());
		oldRPTVO.setNeedSubReportType(reportTypeMasterVO.getHdnNeedSubReportType());
		List listVO;
		int i;
		SubReportTypeVO newSubreportInfo;
		if (!reportTypeMasterVO.getHdnNeedSubReportType().equalsIgnoreCase(reportTypeMasterVO.getNeedSubReportType())) {
			if (reportTypeMasterVO.getHdnNeedSubReportType().equalsIgnoreCase("1")
					&& reportTypeMasterVO.getNeedSubReportType().equalsIgnoreCase("2")) {
				listVO = this.getSubReport(reportTypeMasterVO.getReportTypeId());
				List<ReportTypeMasterVO> finallist = this.getCommaSeperatedListforSubReport(listVO);
				newSubreportInfoArray = new SubReportTypeVO[finallist.size()];
				oldSubreportInfoArray = new SubReportTypeVO[finallist.size()];
				Iterator<ReportTypeMasterVO> itr = finallist.iterator();

				for (i = 0; itr.hasNext(); ++i) {
					SubReportTypeVO newSubreportInfo = new SubReportTypeVO();
					newSubreportInfo = new SubReportTypeVO();
					ReportTypeMasterVO rptvo = (ReportTypeMasterVO) itr.next();
					this.logger.debug("ID" + rptvo.getSubReportType() + "Status" + rptvo.getSubreportStatus()
							+ "Subreportcode" + rptvo.getSubReportTypeCode());
					newSubreportInfo.setSubReportName(rptvo.getSubReportType());
					newSubreportInfo.setSRStatus("0");
					newSubreportInfo.setSRLAction("Deactive");
					newSubreportInfo.setSubReportName(rptvo.getSubReportType());
					newSubreportInfo.setSRStatus(rptvo.getSubreportStatus());
					newSubreportInfo.setSRLAction("oldAction");
					newSubreportInfoArray[i] = newSubreportInfo;
					oldSubreportInfoArray[i] = newSubreportInfo;
				}

				newRPTVO.setSubReportTypeVO(newSubreportInfoArray);
				oldRPTVO.setSubReportTypeVO(oldSubreportInfoArray);
				this.haveDeactiveSubReport = "true";
			}

			newRPTVO.setHasSubReport(reportTypeMasterVO.getNeedSubReportType().equalsIgnoreCase("1") ? "1" : "0");
			oldRPTVO.setHasSubReport(reportTypeMasterVO.getHdnNeedSubReportType().equalsIgnoreCase("1") ? "1" : "0");
		}

		if (reportTypeMasterVO.getHdnNeedSubReportType().equalsIgnoreCase("2")
				&& reportTypeMasterVO.getNeedSubReportType().equalsIgnoreCase("1")) {
			this.haveDeactiveSubReport = "true";
		}

		newRPTVO.setNeedSubreportTypeFlag(needSubreportFlag);
		oldRPTVO.setNeedSubreportTypeFlag(needSubreportFlag);
		if (reportTypeMasterVO.getNeedSubReportType().equalsIgnoreCase("2")) {
			masterObject.setHasSubReport(0);
		} else if (reportTypeMasterVO.getNeedSubReportType().equalsIgnoreCase("1")) {
			masterObject.setHasSubReport(1);
		}

		String message;
		String newXml;
		if (reportTypeMasterVO.getNeedSubReportType().equals("2") && reportTypeMasterVO.getResearchElement() != null
				&& reportTypeMasterVO.getResearchElement().trim().length() > 0) {
			masterObject.setRLDefaultREs(reportTypeMasterVO.getResearchElement());
			listVO = this.reportTypeDAO.getReForRpt(reportTypeMasterVO.getReportTypeId());
			message = "";
			newXml = "";
			Iterator itr = listVO.iterator();

			while (true) {
				while (itr.hasNext()) {
					REMasterVO rptvo = (REMasterVO) itr.next();
					if (message.equalsIgnoreCase("") && message.trim().length() == 0) {
						message = message + rptvo.getName();
					} else {
						message = message + "," + rptvo.getName();
					}

					if (newXml.equalsIgnoreCase("") && newXml.trim().length() == 0) {
						newXml = newXml + rptvo.getrEMasterId();
					} else {
						newXml = newXml + "," + rptvo.getrEMasterId();
					}
				}

				String[] oldREID = newXml.split(",");
				String[] newREID = reportTypeMasterVO.getResearchElement().split(",");
				Set<String> oldRESet = new HashSet(Arrays.asList(oldREID));
				Set<String> newRESet = new HashSet(Arrays.asList(newREID));
				if (!isArraysEquals(oldRESet, newRESet)) {
					newRPTVO.setResearchElementNames(reportTypeMasterVO.getResearchElementNames());
					oldRPTVO.setResearchElementNames(message);
				}
				break;
			}
		}

		if (!isReportStatusChangeFlag.equalsIgnoreCase("true") && reportTypeMasterVO.getNeedSubReportType() != null
				&& reportTypeMasterVO.getNeedSubReportType().equals("1")) {
			String[] modifiedRecords = reportTypeMasterVO.getModifiedRecords();
			String[] subReportCode = new String[modifiedRecords.length];
			long[] subReportID = new long[modifiedRecords.length];
			subReportTypeMasterVOArray = new SubReportTypeMasterVO[modifiedRecords.length];
			newSubreportInfoArray = new SubReportTypeVO[modifiedRecords.length];
			oldSubreportInfoArray = new SubReportTypeVO[modifiedRecords.length];

			for (i = 0; i < modifiedRecords.length; ++i) {
				SubReportTypeMasterVO subReportTypeMasterVO = new SubReportTypeMasterVO();
				newSubreportInfo = new SubReportTypeVO();
				SubReportTypeVO oldSubreportInfo = new SubReportTypeVO();
				String JSONstring = modifiedRecords[i];
				Map jsonObject = (Map) JSONValue.parse(JSONstring);
				String subreportType = (String) jsonObject.get("subReportType");
				String subreportTypeInitailUse = (String) jsonObject.get("initialsUseEndCRN");
				String associatedResearchElement = (String) jsonObject.get("researchElementID");
				String reseachElementsNames = (String) jsonObject.get("researchElement");
				String sptCode = (String) jsonObject.get("subReportTypeCode");
				String srtAction = (String) jsonObject.get("action");
				String srStatus = (String) jsonObject.get("subreportStatus");
				String hdnResearchID = (String) jsonObject.get("hdnResearchElementID");
				String hdnResearchName = (String) jsonObject.get("hdnResearchElement");
				String sStatusChange = "false";
				this.logger.debug(i + 1 + "##SubReportType subReportType::" + subreportType);
				this.logger.debug(i + 1 + "##SubReportType initialsUseEndCRN::" + subreportTypeInitailUse);
				this.logger.debug(i + 1 + "##associatedResearchElement::" + associatedResearchElement);
				this.logger.debug(i + 1 + "##reseachElementsNames code::" + reseachElementsNames);
				this.logger.debug(i + 1 + "##SubReportType code::" + sptCode);
				this.logger.debug(i + 1 + "##srtAction ::" + srtAction);
				this.logger.debug(i + 1 + "##srStatus ::" + srStatus);
				this.logger.debug(i + 1 + "##hdnRE ::" + hdnResearchID);
				this.logger.debug(i + 1 + "##hdnRename ::" + hdnResearchName);
				if (sptCode != null && !srtAction.equalsIgnoreCase("ADD")) {
					subReportTypeMasterVO.setSubReportTypeCode(sptCode);
				} else if (srtAction.equalsIgnoreCase("ADD")) {
					long subRptSeq = this.reportTypeDAO.getSubReportSeq();
					this.logger.debug("Subreport" + (i + 1) + subRptSeq);
					subReportID[i] = subRptSeq;
					String subReptCode = "SPT" + subRptSeq;
					subReportCode[i] = subReptCode;
					subReportTypeMasterVO.setSubReportTypeCode(subReptCode);
				}

				subReportTypeMasterVO.setSubReportTypeDescription(subreportType);
				if (associatedResearchElement != null && associatedResearchElement.trim().length() > 0) {
					subReportTypeMasterVO.setSRLDefaultREs(associatedResearchElement);
				}

				if ((String) jsonObject.get("isSubRPTStatusChange") != null
						&& ((String) jsonObject.get("isSubRPTStatusChange")).equalsIgnoreCase("true")) {
					if (srStatus != null && srStatus.equalsIgnoreCase("1")) {
						subReportTypeMasterVO.setSRStatus(1);
						subReportTypeMasterVO.setSRLAction("Update");
						sStatusChange = "true";
						newSubreportInfo = new SubReportTypeVO();
						oldSubreportInfo = new SubReportTypeVO();
						newSubreportInfo.setSubReportName(subreportType);
						newSubreportInfo.setSRStatus("1");
						newSubreportInfo.setSRLAction("Active");
						oldSubreportInfo.setSubReportName(subreportType);
						oldSubreportInfo.setSRStatus("0");
						oldSubreportInfo.setSRLAction("OldAction");
						if (activeSubreportList.equalsIgnoreCase("") && activeSubreportList.trim().length() == 0) {
							activeSubreportList = (String) jsonObject.get("subReportTypeId");
						} else {
							activeSubreportList = activeSubreportList + ","
									+ (String) jsonObject.get("subReportTypeId");
						}
					} else if ((String) jsonObject.get("subreportStatus") != null
							&& ((String) jsonObject.get("subreportStatus")).equalsIgnoreCase("0")) {
						subReportTypeMasterVO.setSRStatus(0);
						subReportTypeMasterVO.setSRLAction("Update");
						sStatusChange = "true";
						newSubreportInfo = new SubReportTypeVO();
						oldSubreportInfo = new SubReportTypeVO();
						newSubreportInfo.setSubReportName(subreportType);
						newSubreportInfo.setSRStatus("0");
						newSubreportInfo.setSRLAction("Deactive");
						oldSubreportInfo.setSubReportName(subreportType);
						oldSubreportInfo.setSRStatus("1");
						oldSubreportInfo.setSRLAction("OldAction");
						if (deactiveSubreportList.equalsIgnoreCase("") && deactiveSubreportList.trim().length() == 0) {
							deactiveSubreportList = (String) jsonObject.get("subReportTypeId");
						} else {
							deactiveSubreportList = deactiveSubreportList + ","
									+ (String) jsonObject.get("subReportTypeId");
						}
					}
				} else if ((String) jsonObject.get("subreportStatus") != null
						&& ((String) jsonObject.get("subreportStatus")).equalsIgnoreCase("1")) {
					subReportTypeMasterVO.setSRStatus(1);
				} else if ((String) jsonObject.get("subreportStatus") != null
						&& ((String) jsonObject.get("subreportStatus")).equalsIgnoreCase("0")) {
					subReportTypeMasterVO.setSRStatus(0);
				}

				reportTypeMasterVO.setDeactivateSubreportList(deactiveSubreportList);
				reportTypeMasterVO.setActivateSubreportList(activeSubreportList);
				if (srtAction != null && srtAction.equalsIgnoreCase("ADD")) {
					subReportTypeMasterVO.setSRLAction("ADD");
					newSubreportInfo = new SubReportTypeVO();
					oldSubreportInfo = new SubReportTypeVO();
					newSubreportInfo.setSubReportName(subreportType);
					newSubreportInfo.setSubReportTypeCode(subReportTypeMasterVO.getSubReportTypeCode());
					newSubreportInfo.setSubCrnInitial(subreportTypeInitailUse);
					newSubreportInfo.setAssociatedResearchElements(reseachElementsNames);
					newSubreportInfo.setSRStatus(srStatus);
					newSubreportInfo.setSRLAction(srtAction);
					oldSubreportInfo.setSRLAction("Old");
				} else if (!sStatusChange.equalsIgnoreCase("true")) {
					newSubreportInfo = new SubReportTypeVO();
					oldSubreportInfo = new SubReportTypeVO();
					oldSubreportInfo.setSRLAction("Old");
					newSubreportInfo.setSRLAction("Old");
					if (srtAction != null && srtAction.equalsIgnoreCase("UPDATE")) {
						subReportTypeMasterVO.setSRLAction("Update");
						if (hdnResearchID != null && associatedResearchElement != null) {
							String[] oldREID = hdnResearchID.split(",");
							String[] newREID = associatedResearchElement.split(",");
							Set<String> oldRESet = new HashSet(Arrays.asList(oldREID));
							Set<String> newRESet = new HashSet(Arrays.asList(newREID));
							if (!isArraysEquals(oldRESet, newRESet)) {
								newSubreportInfo.setSubReportName(subreportType);
								newSubreportInfo.setAssociatedResearchElements(reseachElementsNames);
								newSubreportInfo.setSRLAction("Update");
								oldSubreportInfo.setSubReportName(subreportType);
								oldSubreportInfo.setAssociatedResearchElements(hdnResearchName);
								oldSubreportInfo.setSRLAction("OldAction");
							}
						}
					}
				}

				if (newSubreportInfo.getSRLAction() != null && newSubreportInfo.getSRLAction().trim().length() > 0) {
					newSubreportInfoArray[i] = newSubreportInfo;
				}

				if (oldSubreportInfo.getSRLAction() != null && oldSubreportInfo.getSRLAction().trim().length() > 0) {
					oldSubreportInfoArray[i] = oldSubreportInfo;
				}

				subReportTypeMasterVOArray[i] = subReportTypeMasterVO;
			}

			this.logger.debug("active list" + activeSubreportList + ",Deactivelist" + deactiveSubreportList);
			newRPTVO.setSubReportTypeVO(newSubreportInfoArray);
			oldRPTVO.setSubReportTypeVO(oldSubreportInfoArray);
			masterObject.setSubReportType(subReportTypeMasterVOArray);
			reportTypeMasterVO.setSubReportCode(subReportCode);
			reportTypeMasterVO.setSubReportID(subReportID);
		}

		if (isReportStatusChangeFlag.equalsIgnoreCase("true") && reportTypeMasterVO.getReportTypeStatus().equals("0")) {
			masterObject.setRLActionType("Deactivate");
			action = "Deactive";
			newRPTVO = new ReportTypeMasterVO();
			oldRPTVO = new ReportTypeMasterVO();
			newRPTVO.setReportTypeStatus("0");
			newRPTVO.setNeedSubReportType("2");
			oldRPTVO.setReportTypeStatus("1");
			oldRPTVO.setNeedSubReportType("2");
		} else if (isReportStatusChangeFlag.equalsIgnoreCase("true")
				&& reportTypeMasterVO.getReportTypeStatus().equals("1")) {
			masterObject.setRLActionType("Activate");
			action = "Active";
			newRPTVO = new ReportTypeMasterVO();
			oldRPTVO = new ReportTypeMasterVO();
			newRPTVO.setReportTypeStatus("1");
			newRPTVO.setNeedSubReportType("2");
			oldRPTVO.setReportTypeStatus("0");
			oldRPTVO.setNeedSubReportType("2");
		} else {
			masterObject.setRLActionType("Update");
		}

		masterObject.setRLStatus(reportTypeMasterVO.getReportTypeStatus().equals("0") ? 0 : 1);
		upMasterVO.setReportTypeMaster(masterObject);
		ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
		if (isisResponseVO.isSuccess()) {
			message = this.updateReportType(reportTypeMasterVO) + "#success";

			String oldXml;
			try {
				newXml = this.createHistoryData(newRPTVO);
				this.logger.debug("NEW XML File for History" + newXml);
				oldXml = this.createHistoryData(oldRPTVO);
				this.logger.debug("OLD XML File for History" + oldXml);
			} catch (ParserConfigurationException var38) {
				throw new CMSException(this.logger, var38);
			} catch (Exception var39) {
				throw new CMSException(this.logger, var39);
			}

			AtlasHistoryVO atlasHistoryVO = new AtlasHistoryVO();
			if (newXml != null && newXml.trim().length() > 0) {
				atlasHistoryVO.setHistoryKey("" + reportTypeMasterVO.getReportTypeId());
				atlasHistoryVO.setNewInfo(newXml);
				atlasHistoryVO.setOldInfo(oldXml);
				atlasHistoryVO.setAction(action);
				atlasHistoryVO.setUpdatedBy(reportTypeMasterVO.getUserName());
				String historyID = this.atlasHistoryUtil.insertData("REPORT_TYPE_MASTER_HISTORY", atlasHistoryVO);
				this.logger.debug("Inserted record  in History table Id is ##" + historyID);
			}

			return message;
		} else if (isisResponseVO.getResponseVO().getErrorCode().equalsIgnoreCase("P001")) {
			return "Report" + reportTypeMasterVO.getReportType() + "does not exist in EDDO" + "#failure";
		} else if (isisResponseVO.getResponseVO().getErrorCode().equalsIgnoreCase("P002")) {
			return "Report" + reportTypeMasterVO.getReportType() + "is Deactive currently" + "#failure";
		} else {
			return isisResponseVO.getResponseVO().getMessage() != null
					&& isisResponseVO.getResponseVO().getMessage().trim().length() > 0
							? isisResponseVO.getResponseVO().getMessage() + "#failure"
							: "Unable to process this request. Error#failure";
		}
	}

	public String updateReportType(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		this.logger.debug("reportTypeMasterVO.getNeedSubReportType() ::" + reportTypeMasterVO.getNeedSubReportType());
		String deActivatedSubreportLIst;
		if (reportTypeMasterVO.getNeedSubReportType() != null
				&& reportTypeMasterVO.getNeedSubReportType().equals("2")) {
			List<Long> subRptId = this.reportTypeDAO.getSubReportId(reportTypeMasterVO.getReportTypeId());
			this.logger.debug("All Sub ReportType Id's ::" + subRptId);
			String commaSepVal = "";

			Long longVal;
			for (Iterator iterator = subRptId.iterator(); iterator
					.hasNext(); commaSepVal = commaSepVal + longVal + ",") {
				longVal = (Long) iterator.next();
			}

			if (commaSepVal != null && !commaSepVal.equals("")) {
				commaSepVal = commaSepVal.substring(0, commaSepVal.length() - 1);
				this.logger.debug("Sub report id's  :: " + commaSepVal);
				this.reportTypeDAO.deActiveSubReport(commaSepVal);
			}

			this.reportTypeDAO.updateReportType(reportTypeMasterVO);
			this.reportTypeDAO.deleteReportRe(reportTypeMasterVO.getReportTypeId());
			this.logger.debug("succesfully update report type master .Now going to insert re's in RE Map table");
			deActivatedSubreportLIst = reportTypeMasterVO.getResearchElement();
			String[] reList = deActivatedSubreportLIst.split(",");

			for (int i = 0; i < reList.length; ++i) {
				this.logger.debug("going to isnsert RE ::::  " + reList[i]);
				String reIds = reList[i];
				this.reportTypeDAO.insertReMap(reIds, reportTypeMasterVO.getReportTypeId(),
						reportTypeMasterVO.getUserName());
			}
		} else if (reportTypeMasterVO.getNeedSubReportType() != null
				&& reportTypeMasterVO.getNeedSubReportType().equals("1")) {
			this.reportTypeDAO.deleteReportRe(reportTypeMasterVO.getReportTypeId());
			this.reportTypeDAO.updateReportType(reportTypeMasterVO);
			String[] modifiedRecords = reportTypeMasterVO.getModifiedRecords();
			List<Long> subRptId = this.reportTypeDAO.getSubReportId(reportTypeMasterVO.getReportTypeId());
			deActivatedSubreportLIst = reportTypeMasterVO.getDeactivateSubreportList();
			this.logger.debug("deActivatedLIst" + deActivatedSubreportLIst);
			String activatedSubreportLIst = reportTypeMasterVO.getActivateSubreportList();
			this.logger.debug("ActivatedLIst" + activatedSubreportLIst);
			String isNameChanged = "";

			for (int i = 0; i < modifiedRecords.length; ++i) {
				ReportTypeMasterVO rptVO = new ReportTypeMasterVO();
				String JSONstring = modifiedRecords[i];
				Map jsonObject = (Map) JSONValue.parse(JSONstring);
				this.logger.debug("ReportTypeManager.subReportTypeId :: " + (String) jsonObject.get("subReportTypeId")
						+ (String) jsonObject.get("subReportType"));
				this.logger.debug(
						"ReportTypeManager.addReportType() re list  " + (String) jsonObject.get("researchElement"));
				this.logger.debug(
						"ReportTypeManager.addReportType()  inital " + (String) jsonObject.get("initialsUseEndCRN"));
				this.logger.debug("SubReportTypeId::" + jsonObject.get("subReportTypeId"));
				rptVO.setSubReportType((String) jsonObject.get("subReportType"));
				rptVO.setReportTypeId(reportTypeMasterVO.getReportTypeId());
				rptVO.setUserName(reportTypeMasterVO.getUserName());
				rptVO.setInitialsUseEndCRN((String) jsonObject.get("initialsUseEndCRN"));
				this.logger.debug("##########ID" + rptVO.getSubReportType() + "Status"
						+ (String) jsonObject.get("subreportStatus"));
				rptVO.setReportTypeStatus((String) jsonObject.get("subreportStatus"));
				long subrptId;
				if (jsonObject.get("subReportTypeId") == null) {
					if (this.reportTypeDAO.isSUBreportExist((String) jsonObject.get("subReportType"))) {
						this.logger.debug("initialsUseEndCRN" + (String) jsonObject.get("initialsUseEndCRN")
								+ "   sub_report_name " + (String) jsonObject.get("subReportType"));
						this.reportTypeDAO.updateSubreport((String) jsonObject.get("initialsUseEndCRN"),
								(String) jsonObject.get("subReportType"));
					}

					if (deActivatedSubreportLIst != null && !deActivatedSubreportLIst.trim().equals("")
							&& !deActivatedSubreportLIst.trim().equals("undefined")) {
						this.logger.debug("in Deactivation section");
						this.reportTypeDAO.deActiveSubReport(deActivatedSubreportLIst);
					}

					if (activatedSubreportLIst != null && !activatedSubreportLIst.trim().equals("")
							&& !activatedSubreportLIst.trim().equals("undefined")) {
						this.logger.debug("in Active subreport section");
						this.reportTypeDAO.activeSubReport(activatedSubreportLIst);
					}

					this.logger.debug("in null section id\n");
					String[] subReportCode = reportTypeMasterVO.getSubReportCode();
					long[] subReportID = reportTypeMasterVO.getSubReportID();
					String subRptCode = subReportCode[i];
					long subRptId1 = subReportID[i];
					subrptId = subReportID[i];
					rptVO.setSubReportTypeCode(subRptCode);
					rptVO.setSubReportTypeId(subRptId1);
					this.reportTypeDAO.insertSubReport(rptVO);
					this.logger.debug("Generated  sub report type id is ::::   " + subrptId);
					String reStr = (String) jsonObject.get("researchElementID");
					String[] reList = reStr.split(",");

					for (int j = 0; j < reList.length; ++j) {
						this.logger.debug("going to isnsert RE ::::  " + reList[j]);
						this.logger.debug("total Id for selected RE    ::  " + reList.length);
						String reIds = reList[j];
						this.reportTypeDAO.insertSubReportReMap(reIds, subrptId, reportTypeMasterVO.getUserName());
					}
				} else {
					isNameChanged = this.reportTypeDAO.getSubReportType((String) jsonObject.get("subReportTypeId"));
					this.logger.debug("in else section id\n");
					this.logger.debug(
							"old name=\t" + isNameChanged + "new name \t" + (String) jsonObject.get("subReportTypeId"));
					if (deActivatedSubreportLIst != null && !deActivatedSubreportLIst.trim().equals("")
							&& !deActivatedSubreportLIst.trim().equals("undefined")) {
						this.logger.debug("in Deactivation section");
						this.reportTypeDAO.deActiveSubReport(deActivatedSubreportLIst);
					}

					if (activatedSubreportLIst != null && !activatedSubreportLIst.trim().equals("")
							&& !activatedSubreportLIst.trim().equals("undefined")) {
						this.logger.debug("in Active subreport section");
						this.reportTypeDAO.activeSubReport(activatedSubreportLIst);
					}

					String reStr;
					String[] reList;
					int j;
					String reIds;
					if (isNameChanged.equals(jsonObject.get("subReportType"))) {
						this.logger.debug("name same");
						subrptId = Long.valueOf((String) jsonObject.get("subReportTypeId"));
						this.reportTypeDAO.deleteSubRe((String) jsonObject.get("subReportTypeId"));
						this.logger.debug("Deactive RE List" + (String) jsonObject.get("researchElementID"));
						reStr = (String) jsonObject.get("researchElementID");
						reList = reStr.split(",");

						for (j = 0; j < reList.length; ++j) {
							this.logger.debug("going to isnsert RE ::::  " + reList[j]);
							this.logger.debug("total Id for selected RE    ::  " + reList.length);
							reIds = reList[j];
							this.reportTypeDAO.insertSubReportReMap(reIds, subrptId, reportTypeMasterVO.getUserName());
						}
					} else {
						this.logger.debug("name not same ");
						this.reportTypeDAO.deleteActSubReport((String) jsonObject.get("subReportTypeId"));
						subrptId = this.reportTypeDAO.insertSubReport(rptVO);
						this.logger.debug("Generated  sub report type id is ::::   " + subrptId);
						reStr = (String) jsonObject.get("researchElement");
						reList = reStr.split(",");

						for (j = 0; j < reList.length; ++j) {
							this.logger.debug("going to isnsert RE ::::  " + reList[j]);
							reIds = reList[j];
							this.reportTypeDAO.insertSubReportReMap(reIds, subrptId, reportTypeMasterVO.getUserName());
						}
					}
				}
			}
		}

		return "message#success";
	}

	public String changeStatus(String rptId, String status, String userName) throws CMSException {
		String message = "";

		try {
			ReportTypeMasterVO newRPTVO = new ReportTypeMasterVO();
			ReportTypeMasterVO oldRPTVO = new ReportTypeMasterVO();
			UPMasterVO upMasterVO = new UPMasterVO();
			upMasterVO.setMaster("Report");
			upMasterVO.setUpdateType("Update");
			com.integrascreen.orders.ReportTypeMasterVO masterObject = new com.integrascreen.orders.ReportTypeMasterVO();
			ReportTypeMasterVO reportTypeMasterVO = this.reportTypeDAO.getRptInfo(Long.parseLong(rptId));
			this.logger.debug("Status::" + status);
			masterObject.setReportypeCode(reportTypeMasterVO.getReportTypeCode());
			masterObject.setDescription(reportTypeMasterVO.getReportType());
			masterObject.setTAT(Integer.parseInt(reportTypeMasterVO.getTurnaroundTime()));
			int count = this.checkSubRpt(Long.parseLong(rptId));
			if (count > 0) {
				masterObject.setHasSubReport(1);
				reportTypeMasterVO.setHasSubReport("Yes");
			} else {
				masterObject.setHasSubReport(0);
				reportTypeMasterVO.setHasSubReport("No");
			}

			this.logger.debug("Deactive/Active Research elements" + reportTypeMasterVO.getAssociatedResearchElements()
					+ reportTypeMasterVO.getNeedSubReportType());
			masterObject.setRLStatus(status.equals("1") ? 0 : 1);
			if (status.equals("1")) {
				masterObject.setRLActionType("Deactivate");
				newRPTVO.setReportTypeStatus("0");
				oldRPTVO.setReportTypeStatus("1");
			} else {
				masterObject.setRLActionType("Activate");
				newRPTVO.setReportTypeStatus("1");
				oldRPTVO.setReportTypeStatus("0");
			}

			upMasterVO.setReportTypeMaster(masterObject);
			ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
			if (isisResponseVO.isSuccess()) {
				message = this.reportTypeDAO.changeStatus(rptId, status) + "#success";

				String newXml;
				String oldXml;
				try {
					newXml = this.createHistoryData(newRPTVO);
					this.logger.debug("New XML File for History" + newXml);
					oldXml = this.createHistoryData(oldRPTVO);
					this.logger.debug("Old XML File for History" + oldXml);
				} catch (ParserConfigurationException var16) {
					throw new CMSException(this.logger, var16);
				} catch (Exception var17) {
					throw new CMSException(this.logger, var17);
				}

				AtlasHistoryVO atlasHistoryVO = new AtlasHistoryVO();
				atlasHistoryVO.setHistoryKey("" + reportTypeMasterVO.getReportTypeId());
				atlasHistoryVO.setNewInfo(newXml);
				atlasHistoryVO.setOldInfo(oldXml);
				atlasHistoryVO.setUpdatedBy(userName);
				if (status.equalsIgnoreCase("1")) {
					atlasHistoryVO.setAction("Deactive");
				} else {
					atlasHistoryVO.setAction("Active");
				}

				String id = this.atlasHistoryUtil.insertData("REPORT_TYPE_MASTER_HISTORY", atlasHistoryVO);
				this.logger.debug("ACTIVE/DEACTIVE:: REPORT_TYPE_MASTER_HISTORY::::::::::::::" + id);
				return message;
			} else {
				return isisResponseVO.getResponseVO().getMessage() + "#failure";
			}
		} catch (CMSException var18) {
			throw new CMSException(this.logger, var18);
		}
	}

	public int checkSubRpt(long rptIdVal) throws CMSException {
		int count = false;
		int count = this.reportTypeDAO.checkSubRpt(rptIdVal);
		return count;
	}

	public String getReportTypeMasterId(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		List<Long> subRptId = this.reportTypeDAO.getReportTypeMasterId(reportTypeMasterVO);
		String commaSepVal = "";

		try {
			Long longVal;
			for (Iterator iterator = subRptId.iterator(); iterator
					.hasNext(); commaSepVal = commaSepVal + longVal + ",") {
				longVal = (Long) iterator.next();
			}

			if (commaSepVal != null && !commaSepVal.equals("")) {
				commaSepVal = commaSepVal.substring(0, commaSepVal.length() - 1);
				this.logger.debug("getReprtTypeMasterId report id's  :: " + commaSepVal);
			}

			return commaSepVal;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int checkReportIfExist(String rptName) throws CMSException {
		int count = this.reportTypeDAO.checkReportIfExist(rptName);
		return count;
	}

	private long getMaxId() throws CMSException {
		long count = 0L;
		count = this.reportTypeDAO.getMaxId();
		return count;
	}

	public List<ReportTypeMasterVO> getReportTypeGrid(ReportTypeMasterVO reportTypeMasterVO, int start, int limit,
			String sortColumnName, String sortType) throws CMSException {
		reportTypeMasterVO.setStart(new Integer(start + 1));
		reportTypeMasterVO.setLimit(new Integer(start + limit));
		reportTypeMasterVO.setSortColumnName(sortColumnName);
		reportTypeMasterVO.setSortType(sortType);
		this.logger.debug("Inside the Manager:::>>>>>>" + reportTypeMasterVO);
		List<ReportTypeMasterVO> reportTypeList = this.reportTypeDAO.getReportTypeMasterGrid(reportTypeMasterVO);
		return reportTypeList;
	}

	public int getReportTypeCount(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		return this.reportTypeDAO.getReportTypeMasterCount(reportTypeMasterVO);
	}

	public boolean isSubReportTypeExist(SubReportTypeVO subReportTypeVO) throws CMSException {
		int subReportTypeCount = this.reportTypeDAO.isSubReportTypeExist(subReportTypeVO);
		return subReportTypeCount > 0;
	}

	public boolean isSubReportInitialExist(String subReportTypeNameInitials) throws CMSException {
		int subReportTypeCount = this.reportTypeDAO.isSubReportInitialExist(subReportTypeNameInitials);
		return subReportTypeCount > 0;
	}

	public ReportTypeMasterVO checkAssociatedMaster(String reportTypeId) throws CMSException {
		return this.reportTypeDAO.checkAssociatedMaster(reportTypeId);
	}

	public ReportTypeMasterVO checkAssociatedSubreport(String subReportTypeId) throws CMSException {
		this.logger.debug("Inside ReportTypeManager:checkAssociatedSubreport");
		return this.reportTypeDAO.checkAssociatedSubreport(subReportTypeId);
	}

	public int checkInitialCRNExist(String initialCRN) throws CMSException {
		int count = this.reportTypeDAO.checkInitialCRNExist(initialCRN);
		return count;
	}

	public List<AtlasHistoryVO> getReportHistory(AtlasHistoryVO atlasHistoryVO)
			throws CMSException, SAXException, IOException {
		this.logger.debug("IN ReportType Master::getReportHistory");
		List<AtlasHistoryVO> reportHistoryList = this.atlasHistoryUtil.getHistoryData(atlasHistoryVO);
		this.logger.debug("Existing ReportType Master::getReportHistory");
		return reportHistoryList;
	}

	public int getReportHistoryCount(AtlasHistoryVO atlasHistoryVO) throws CMSException {
		this.logger.debug("IN ReportType Master::getReportHistoryCount");
		return this.atlasHistoryUtil.getHistoryDataCount(atlasHistoryVO);
	}

	public String createHistoryData(ReportTypeMasterVO reportTypeMasterVO)
			throws ParserConfigurationException, Exception {
		this.logger.debug("In ReportType Master::createHistoryData");
		String historyData = "";
		String prefix = "<br/>&emsp;<b>";
		String prefixforsubreport = "<br/>&emsp;&emsp;<b>";
		String suffix = ":</b>";
		if (reportTypeMasterVO.getReportType() != null && !reportTypeMasterVO.getReportType().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "Report Type" + suffix + reportTypeMasterVO.getReportType();
		}

		if (reportTypeMasterVO.getReportTypeCode() != null
				&& !reportTypeMasterVO.getReportTypeCode().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "Report Type Code" + suffix + reportTypeMasterVO.getReportTypeCode();
		}

		if (reportTypeMasterVO.getTurnaroundTime() != null
				&& !reportTypeMasterVO.getTurnaroundTime().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "Turnaround Time" + suffix + reportTypeMasterVO.getTurnaroundTime();
		}

		if (reportTypeMasterVO.getInitialsUseCRN() != null
				&& !reportTypeMasterVO.getInitialsUseCRN().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "Initials to use at CRN" + suffix
					+ reportTypeMasterVO.getInitialsUseCRN();
		}

		if (reportTypeMasterVO.getHasSubReport() != null
				&& !reportTypeMasterVO.getHasSubReport().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "Need Sub Report Type" + suffix
					+ (reportTypeMasterVO.getHasSubReport().equals("1") ? "Yes" : "No");
		}

		if (reportTypeMasterVO.getResearchElementNames() != null
				&& !reportTypeMasterVO.getResearchElementNames().equalsIgnoreCase("")) {
			historyData = historyData + prefix + "Research Elements" + suffix
					+ reportTypeMasterVO.getResearchElementNames();
		}

		if (reportTypeMasterVO.getReportTypeStatus() != null) {
			String Status = reportTypeMasterVO.getReportTypeStatus();
			if (Status.equalsIgnoreCase("1")) {
				Status = "Active";
			}

			if (Status.equalsIgnoreCase("0")) {
				Status = "Deactive";
			}

			historyData = historyData + prefix + "Report Type Status" + suffix + Status;
		}

		try {
			if (reportTypeMasterVO.getNeedSubReportType() != null
					&& reportTypeMasterVO.getNeedSubReportType().equalsIgnoreCase("1")
					&& reportTypeMasterVO.getNeedSubreportTypeFlag().equalsIgnoreCase("false")
					|| reportTypeMasterVO.getNeedSubReportType() != null
							&& reportTypeMasterVO.getNeedSubReportType().equalsIgnoreCase("2")
							&& this.haveDeactiveSubReport.equalsIgnoreCase("true")) {
				for (int i = 0; i < reportTypeMasterVO.getSubReportTypeVO().length; ++i) {
					if (!reportTypeMasterVO.getSubReportTypeVO()[i].getSRLAction().equalsIgnoreCase("Old")) {
						historyData = historyData + prefix + "<br/>&emsp;" + "Sub Report Type" + suffix;
						if (reportTypeMasterVO.getSubReportTypeVO()[i].getSubReportName() != null
								&& !reportTypeMasterVO.getSubReportTypeVO()[i].getSubReportName()
										.equalsIgnoreCase("")) {
							historyData = historyData + prefixforsubreport + "Sub Report Type" + suffix
									+ reportTypeMasterVO.getSubReportTypeVO()[i].getSubReportName();
						}

						if (reportTypeMasterVO.getSubReportTypeVO()[i].getSubReportTypeCode() != null
								&& !reportTypeMasterVO.getSubReportTypeVO()[i].getSubReportTypeCode()
										.equalsIgnoreCase("")) {
							historyData = historyData + prefixforsubreport + "Sub Report Code" + suffix
									+ reportTypeMasterVO.getSubReportTypeVO()[i].getSubReportTypeCode();
						}

						if (reportTypeMasterVO.getSubReportTypeVO()[i].getSRStatus() != null
								&& reportTypeMasterVO.getSubReportTypeVO()[i].getSRStatus().equalsIgnoreCase("1")) {
							historyData = historyData + prefixforsubreport + "Sub Report Status" + suffix + "Active";
						}

						if (reportTypeMasterVO.getSubReportTypeVO()[i].getSRStatus() != null
								&& reportTypeMasterVO.getSubReportTypeVO()[i].getSRStatus().equalsIgnoreCase("0")) {
							historyData = historyData + prefixforsubreport + "Sub Report Status" + suffix + "Deactive";
						}

						if (reportTypeMasterVO.getSubReportTypeVO()[i].getSubCrnInitial() != null
								&& !reportTypeMasterVO.getSubReportTypeVO()[i].getSubCrnInitial()
										.equalsIgnoreCase("")) {
							historyData = historyData + prefixforsubreport + "Initials to use at End of CRN" + suffix
									+ reportTypeMasterVO.getSubReportTypeVO()[i].getSubCrnInitial();
						}

						if (reportTypeMasterVO.getSubReportTypeVO()[i].getAssociatedResearchElements() != null
								&& !reportTypeMasterVO.getSubReportTypeVO()[i].getAssociatedResearchElements()
										.equalsIgnoreCase("")) {
							historyData = historyData + prefixforsubreport + "Research Elements" + suffix
									+ reportTypeMasterVO.getSubReportTypeVO()[i].getAssociatedResearchElements();
						}

						if (reportTypeMasterVO.getSubReportTypeVO()[i].getSRLAction() != null
								&& !reportTypeMasterVO.getSubReportTypeVO()[i].getSRLAction().equalsIgnoreCase("")
								&& !reportTypeMasterVO.getSubReportTypeVO()[i].getSRLAction()
										.equalsIgnoreCase("oldAction")) {
							historyData = historyData + prefixforsubreport + "Action" + suffix
									+ reportTypeMasterVO.getSubReportTypeVO()[i].getSRLAction();
						}
					}
				}
			}

			return historyData;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public String parseXML(String xmlString) throws CMSException, SAXException, IOException {
		this.logger.debug("IN ReportTypeManager::parseContactXML");
		this.logger.debug("Before Parsing" + xmlString);

		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(xmlString)));
			String reportHistory = "";
			String xmldata = "";
			String prefix = "<br/>&emsp;<b>";
			String prefixforsubreport = "<br/>&emsp;&emsp;<b>";
			String suffix = ":</b>";
			NodeList reportNodeList = doc.getElementsByTagName("Report");
			NodeList subReportNodeList = doc.getElementsByTagName("SubReportType");
			this.logger.debug("Node Names::" + doc.getDocumentElement().getNodeName());
			if (reportNodeList.getLength() > 0) {
				for (int i = 0; i < reportNodeList.getLength(); ++i) {
					Node reportNode = reportNodeList.item(i);
					if (reportNode.getNodeType() == 1) {
						Element reportTypeElements = (Element) reportNode;
						reportHistory = reportHistory + "<br/>";
						if (this.nullCheck(reportTypeElements, "ReportLabel", i)) {
							reportHistory = reportHistory + prefix + "ReportName" + suffix
									+ reportTypeElements.getElementsByTagName("ReportLabel").item(i).getTextContent();
						}

						if (this.nullCheck(reportTypeElements, "ReportTypeCode", i)) {
							reportHistory = reportHistory + prefix + "ReportCode" + suffix + reportTypeElements
									.getElementsByTagName("ReportTypeCode").item(i).getTextContent();
						}

						if (this.nullCheck(reportTypeElements, "IntialUse", i)) {
							reportHistory = reportHistory + prefix + "Initial" + suffix
									+ reportTypeElements.getElementsByTagName("IntialUse").item(i).getTextContent();
						}

						if (this.nullCheck(reportTypeElements, "HasSubReport", i)) {
							reportHistory = reportHistory + prefix + "HasSubReport" + suffix
									+ reportTypeElements.getElementsByTagName("HasSubReport").item(i).getTextContent();
						}

						if (this.nullCheck(reportTypeElements, "TAT", i)) {
							reportHistory = reportHistory + prefix + "TAT" + suffix
									+ reportTypeElements.getElementsByTagName("TAT").item(i).getTextContent();
						}

						if (this.nullCheck(reportTypeElements, "RLDefaultREs", i)) {
							reportHistory = reportHistory + prefix + "RE" + suffix
									+ reportTypeElements.getElementsByTagName("RLDefaultREs").item(i).getTextContent();
						}

						if (this.nullCheck(reportTypeElements, "ReportStatus", i)) {
							reportHistory = reportHistory + prefix + "ReportStatus" + suffix
									+ reportTypeElements.getElementsByTagName("ReportStatus").item(i).getTextContent();
						}

						if (subReportNodeList.getLength() > 0) {
							reportHistory = reportHistory + prefix + "<br/>&emsp;" + "SubReportType" + suffix;

							for (int j = 0; j < subReportNodeList.getLength(); ++j) {
								Node subReportNode = subReportNodeList.item(j);
								if (subReportNode.getNodeType() == 1) {
									Element subreport = (Element) subReportNode;
									reportHistory = reportHistory + "<br/>";
									if (this.nullCheck(reportTypeElements, "SubReportTypeDescription", j)) {
										reportHistory = reportHistory + prefixforsubreport + "SubReportName" + suffix
												+ reportTypeElements.getElementsByTagName("SubReportTypeDescription")
														.item(j).getTextContent();
									}

									if (this.nullCheck(reportTypeElements, "SubReportTypeCode", j)) {
										reportHistory = reportHistory + prefixforsubreport + "SubReportCode" + suffix
												+ reportTypeElements.getElementsByTagName("SubReportTypeCode").item(j)
														.getTextContent();
									}

									if (this.nullCheck(reportTypeElements, "SubIntialUse", j)) {
										reportHistory = reportHistory + prefixforsubreport + "SubIntialUse" + suffix
												+ reportTypeElements.getElementsByTagName("SubIntialUse").item(j)
														.getTextContent();
									}

									if (this.nullCheck(reportTypeElements, "SubreportStatus", j)) {
										reportHistory = reportHistory + prefixforsubreport + "SubReportStatus" + suffix
												+ reportTypeElements.getElementsByTagName("SubreportStatus").item(j)
														.getTextContent();
									}

									if (this.nullCheck(reportTypeElements, "SRLDefaultREs", j)) {
										reportHistory = reportHistory + prefixforsubreport + "SubreportRE" + suffix
												+ reportTypeElements.getElementsByTagName("SRLDefaultREs").item(j)
														.getTextContent();
									}

									if (this.nullCheck(reportTypeElements, "SRLAction", j)) {
										reportHistory = reportHistory + prefixforsubreport + "SRLAction" + suffix
												+ reportTypeElements.getElementsByTagName("SRLAction").item(j)
														.getTextContent();
									}
								}
							}
						}
					}
				}
			}

			this.logger.debug("after Parsing" + reportHistory);
			return reportHistory;
		} catch (ParserConfigurationException var16) {
			throw new CMSException(this.logger, var16);
		}
	}

	public boolean nullCheck(Element element, String value, int i) {
		this.logger.debug("element::" + element + " value :" + value);
		NodeList n1 = element.getElementsByTagName(value);
		if (n1.getLength() > 0) {
			return element.getElementsByTagName(value).item(i) != null
					&& element.getElementsByTagName(value).item(i).getTextContent() != null
					&& element.getElementsByTagName(value).item(i).getTextContent().trim().length() > 0;
		} else {
			return false;
		}
	}

	public static boolean isArraysEquals(Set<?> s1, Set<?> s2) {
		if (s1.size() != s2.size()) {
			return false;
		} else {
			return s1.containsAll(s2);
		}
	}

	private List<ReportTypeMasterVO> getCommaSeperatedListforSubReport(List<ReportTypeMasterVO> list) {
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
}