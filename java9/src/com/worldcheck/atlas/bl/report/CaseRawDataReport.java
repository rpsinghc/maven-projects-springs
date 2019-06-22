package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.CaseRawDataDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.JsonBeanUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.JSONBean;
import com.worldcheck.atlas.vo.MaterializedViewVO;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.report.CTExcelVO;
import com.worldcheck.atlas.vo.report.CaseRawTableVO;
import com.worldcheck.atlas.vo.report.SubReportTypeVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CaseRawDataReport implements ICaseRawDataReport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.report.CaseRawDataReport");
	private CaseRawDataDAO caseRawDataDAO;

	public void setCaseRawDataDAO(CaseRawDataDAO caseRawDataDAO) {
		this.caseRawDataDAO = caseRawDataDAO;
	}

	public List<CaseRawTableVO> getCaseRawDataGrid(CaseRawTableVO caseRawTableVO, int start, int limit,
			String sortColumnName, String sortType) throws CMSException {
		caseRawTableVO.setStart(new Integer(start + 1));
		caseRawTableVO.setLimit(new Integer(start + limit));
		caseRawTableVO.setSortColumnName(sortColumnName);
		caseRawTableVO.setSortType(sortType);
		return this.caseRawDataDAO.getCaseRawDataGrid(caseRawTableVO);
	}

	public int getCaseRawDataGridCount(CaseRawTableVO caseRawTableVO) throws CMSException {
		return this.caseRawDataDAO.getCaseRawDataGridCount(caseRawTableVO);
	}

	public List<UserMasterVO> getAllTemplateCreator() throws CMSException {
		return this.caseRawDataDAO.getAllTemplateCreator();
	}

	public boolean isExistCaseRawDataTemplateName(String templateName) throws CMSException {
		int templateNameCount = this.caseRawDataDAO.getCountExistTemplateName(templateName);
		return templateNameCount > 0;
	}

	public List<CaseRawTableVO> getInfoListCaseRawDataTemplate() throws CMSException {
		return this.caseRawDataDAO.getInfoListCaseRawDataTemplate();
	}

	public List<JSONBean> getAvailableOption(String listType) throws CMSException {
		HashMap<String, String> dataMap = null;
		List subReportVOList;
		Iterator var5;
		if (listType.equals("REPORT_TYPE")) {
			this.logger.debug("REPORT_TYPE List");
			subReportVOList = ResourceLocator.self().getCacheService().getCacheItemsList("REPORT_TYPE_MASTER");
			dataMap = new HashMap();
			var5 = subReportVOList.iterator();

			while (var5.hasNext()) {
				ReportTypeMasterVO reportTypeMasterVO = (ReportTypeMasterVO) var5.next();
				dataMap.put(String.valueOf(reportTypeMasterVO.getReportTypeId()), reportTypeMasterVO.getReportType());
			}
		}

		if (listType.equals("CLIENT_CODE")) {
			this.logger.debug("CLIENT_CODE List");
			subReportVOList = ResourceLocator.self().getCacheService().getCacheItemsList("CLIENT_MASTER");
			dataMap = new HashMap();
			var5 = subReportVOList.iterator();

			while (var5.hasNext()) {
				ClientMasterVO clientMasterVO = (ClientMasterVO) var5.next();
				dataMap.put(clientMasterVO.getClientMasterId(), clientMasterVO.getClientCode());
			}
		}

		if (listType.equals("CASE_MANAGER")) {
			this.logger.debug("CASE_MANAGER List");
			subReportVOList = ResourceLocator.self().getTaskService().getCaseManagerList();
			dataMap = new HashMap();
			var5 = subReportVOList.iterator();

			while (var5.hasNext()) {
				UserMasterVO userMasterVO = (UserMasterVO) var5.next();
				dataMap.put(userMasterVO.getUserID(), userMasterVO.getUserFullName());
			}
		}

		if (listType.equals("CASE_STATUS")) {
			this.logger.debug("CASE_STATUS List");
			subReportVOList = ResourceLocator.self().getCacheService().getCacheItemsList("CASE_STATUS_MASTER");
			dataMap = new HashMap();
			var5 = subReportVOList.iterator();

			while (var5.hasNext()) {
				CTExcelVO cTExcelVO = (CTExcelVO) var5.next();
				dataMap.put(cTExcelVO.getCaseStatusId(), cTExcelVO.getCaseStatus());
			}
		}

		if (listType.equals("PRIMARY_TEAM_OFFICE") || listType.equals("SUPPORTING_1_TEAM_OFFICE")
				|| listType.equals("SUPPORTING_2_TEAM_OFFICE") || listType.equals("SUPPORTING_3_TEAM_OFFICE")) {
			this.logger.debug("PRIMARY_TEAM_OFFICE List");
			subReportVOList = ResourceLocator.self().getCacheService().getCacheItemsList("OFFICE_MASTER");
			dataMap = new HashMap();
			var5 = subReportVOList.iterator();

			while (var5.hasNext()) {
				BranchOfficeMasterVO branchOfficeMasterVO = (BranchOfficeMasterVO) var5.next();
				this.logger.debug("BranchOffice ID:" + branchOfficeMasterVO.getBranchOfficeId() + " BranchOffice:"
						+ branchOfficeMasterVO.getBranchOffice());
				dataMap.put(String.valueOf(branchOfficeMasterVO.getBranchOfficeId()),
						branchOfficeMasterVO.getBranchOffice());
			}
		}

		if (listType.equals("SUB_REPORT_TYPE")) {
			this.logger.debug("SUB_REPORT_TYPE List");
			subReportVOList = ResourceLocator.self().getTaskService().getSubReportTypesList();
			dataMap = new HashMap();
			var5 = subReportVOList.iterator();

			while (var5.hasNext()) {
				SubReportTypeVO subReportVO = (SubReportTypeVO) var5.next();
				dataMap.put(subReportVO.getSubReportId(), subReportVO.getSubReportName());
			}
		}

		return JsonBeanUtil.toJsonBeanFromHashMap(dataMap, listType);
	}

	public int saveCaseRawDataTemplate(CaseRawTableVO caseRawTableVO) throws CMSException {
		this.logger.debug("Selected Information & Option:" + caseRawTableVO.getSelectedInfo());
		return this.caseRawDataDAO.saveCaseRawDataTemplate(caseRawTableVO);
	}

	public long deleteCaseRawDataTemplate(long templateId) throws CMSException {
		return (long) this.caseRawDataDAO.deleteCaseRawDataTemplate(templateId);
	}

	public CaseRawTableVO getCaseRawDataTemplateInfo(long templateId) throws CMSException {
		return this.caseRawDataDAO.getCaseRawDataTemplateInfo(templateId);
	}

	public int updateCaseRawDataTemplate(CaseRawTableVO caseRawTableVO) throws CMSException {
		return this.caseRawDataDAO.updateCaseRawDataTemplate(caseRawTableVO);
	}

	public long setDefaultCaseRawDataTemplate(long templateId, long status, String updated_by) throws CMSException {
		return this.caseRawDataDAO.setDefaultCaseRawDataTemplate(templateId, status, updated_by);
	}

	public List<CaseRawTableVO> getSelectedInfoFieldsListCaseRawDataTemplate(long caseRawDataTemplateId)
			throws CMSException {
		return this.caseRawDataDAO.getSelectedInfoFieldsListCaseRawDataTemplate(caseRawDataTemplateId);
	}

	public List<CaseRawTableVO> getSelectedOption(String listType, long selectedInfoId) throws CMSException {
		return this.caseRawDataDAO.getSelectedOption(listType, selectedInfoId);
	}

	public CaseRawTableVO caseRawDataExportToExcel(String templateId, String dateType, String startDate, String endDate,
			String buttonName) throws CMSException {
		this.logger.debug("In CaseRawDataReport::caseRawDataExportToExcel");
		CaseRawTableVO excelFilter = new CaseRawTableVO();
		List<String> selectedField = new ArrayList();
		this.logger.debug("button name in report method:::" + buttonName);
		String jobLoadingPoint = "";
		List<CaseRawTableVO> selectedInfo = this.caseRawDataDAO
				.getSelectedInfoFieldsListCaseRawDataTemplate(Long.parseLong(templateId));
		if (selectedInfo.size() > 0) {
			selectedInfo.remove(selectedInfo.size() - 1);
		}

		for (Iterator var13 = selectedInfo.iterator(); var13.hasNext(); excelFilter.setFieldName(selectedField)) {
			CaseRawTableVO caseRawTableVO = (CaseRawTableVO) var13.next();
			selectedField.add(caseRawTableVO.getSelectedInfo());
			if (caseRawTableVO.getSelectedInfo().equals("REPORT_TYPE")
					|| caseRawTableVO.getSelectedInfo().equals("SUB_REPORT_TYPE")
					|| caseRawTableVO.getSelectedInfo().equals("CLIENT_CODE")
					|| caseRawTableVO.getSelectedInfo().equals("CASE_MANAGER")
					|| caseRawTableVO.getSelectedInfo().equals("CASE_STATUS")
					|| caseRawTableVO.getSelectedInfo().equals("PRIMARY_TEAM_OFFICE")
					|| caseRawTableVO.getSelectedInfo().equals("SUPPORTING_1_TEAM_OFFICE")
					|| caseRawTableVO.getSelectedInfo().equals("SUPPORTING_2_TEAM_OFFICE")
					|| caseRawTableVO.getSelectedInfo().equals("SUPPORTING_3_TEAM_OFFICE")) {
				List<CaseRawTableVO> selectedOption = this.caseRawDataDAO.getSelectedOption(
						caseRawTableVO.getSelectedInfo(), Long.parseLong(caseRawTableVO.getFieldMapId()));
				String conditionString = "";
				Iterator var15 = selectedOption.iterator();

				while (var15.hasNext()) {
					CaseRawTableVO selectedOptionVO = (CaseRawTableVO) var15.next();
					if (conditionString.equals("")) {
						conditionString = "'" + selectedOptionVO.getSelectedOption() + "'";
					} else {
						conditionString = conditionString + ",'" + selectedOptionVO.getSelectedOption() + "'";
					}
				}

				if (caseRawTableVO.getSelectedInfo().equals("REPORT_TYPE")) {
					excelFilter.setSelectedReportType(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("SUB_REPORT_TYPE")) {
					excelFilter.setSelectedSubReportType(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("CLIENT_CODE")) {
					excelFilter.setSelectedClientCode(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("CASE_MANAGER")) {
					excelFilter.setSelectedCaseManager(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("CASE_STATUS")) {
					excelFilter.setSelectedCaseStatus(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("PRIMARY_TEAM_OFFICE")) {
					excelFilter.setSelectedPriTeamOffice(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("SUPPORTING_1_TEAM_OFFICE")) {
					excelFilter.setSelectedSuppTeam1(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("SUPPORTING_2_TEAM_OFFICE")) {
					excelFilter.setSelectedSuppTeam2(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("SUPPORTING_3_TEAM_OFFICE")) {
					excelFilter.setSelectedSuppTeam3(conditionString);
				}
			}
		}

		excelFilter.setStartDate(startDate);
		excelFilter.setEndDate(endDate);
		excelFilter.setDateType(dateType);
		excelFilter.setButtonName(buttonName);
		List<HashMap> excelDataList = this.caseRawDataDAO.getExportToExcelData(excelFilter);
		excelFilter.setExcelDataList(excelDataList);
		this.logger.debug("Exit CaseRawDataReport::caseRawDataExportToExcel");
		return excelFilter;
	}

	public HashMap<String, Integer> crdExcelCount(String templateId, String dateType, String startDate, String endDate,
			String buttonName) throws CMSException {
		this.logger.debug("In CaseRawDataReport::  crdExcelCount");
		this.logger.debug("button name in report method:::" + buttonName);
		int rowCount = false;
		int colCount = false;
		int cellsInExportedExcel = 0;
		HashMap resultMap = new HashMap();
		CaseRawTableVO excelFilter = new CaseRawTableVO();
		List<String> selectedField = new ArrayList();
		String jobLoadingPoint = "";
		List<CaseRawTableVO> selectedInfo = this.caseRawDataDAO
				.getSelectedInfoFieldsListCaseRawDataTemplate(Long.parseLong(templateId));
		if (selectedInfo.size() > 0) {
			selectedInfo.remove(selectedInfo.size() - 1);
		}

		for (Iterator var17 = selectedInfo.iterator(); var17.hasNext(); excelFilter.setFieldName(selectedField)) {
			CaseRawTableVO caseRawTableVO = (CaseRawTableVO) var17.next();
			selectedField.add(caseRawTableVO.getSelectedInfo());
			if (caseRawTableVO.getSelectedInfo().equals("JOB_LOADING_POINTS_PT")
					|| caseRawTableVO.getSelectedInfo().equals("JOB_LOADING_POINTS_ST1")
					|| caseRawTableVO.getSelectedInfo().equals("JOB_LOADING_POINTS_ST2")
					|| caseRawTableVO.getSelectedInfo().equals("JOB_LOADING_POINTS_ST3")
					|| caseRawTableVO.getSelectedInfo().equals("JOB_LOADING_POINTS_ST4")
					|| caseRawTableVO.getSelectedInfo().equals("JOB_LOADING_POINTS_ST5")
					|| caseRawTableVO.getSelectedInfo().equals("JOB_LOADING_POINTS_ST6")) {
				selectedField.remove(jobLoadingPoint + " TOTAL_JOB_LOADING_POINTS");
				if (jobLoadingPoint.equalsIgnoreCase("")) {
					jobLoadingPoint = "NVL(" + caseRawTableVO.getSelectedInfo() + ",0)";
				} else {
					jobLoadingPoint = jobLoadingPoint + "+ NVL(" + caseRawTableVO.getSelectedInfo() + ",0)";
				}

				selectedField.add(jobLoadingPoint + " TOTAL_JOB_LOADING_POINTS");
			}

			if (caseRawTableVO.getSelectedInfo().equals("REPORT_TYPE")
					|| caseRawTableVO.getSelectedInfo().equals("CLIENT_CODE")
					|| caseRawTableVO.getSelectedInfo().equals("CASE_MANAGER")
					|| caseRawTableVO.getSelectedInfo().equals("CASE_STATUS")
					|| caseRawTableVO.getSelectedInfo().equals("PRIMARY_TEAM_OFFICE")
					|| caseRawTableVO.getSelectedInfo().equals("SUPPORTING_1_TEAM_OFFICE")
					|| caseRawTableVO.getSelectedInfo().equals("SUPPORTING_2_TEAM_OFFICE")
					|| caseRawTableVO.getSelectedInfo().equals("SUPPORTING_3_TEAM_OFFICE")) {
				List<CaseRawTableVO> selectedOption = this.caseRawDataDAO.getSelectedOption(
						caseRawTableVO.getSelectedInfo(), Long.parseLong(caseRawTableVO.getFieldMapId()));
				String conditionString = "";
				Iterator var19 = selectedOption.iterator();

				while (var19.hasNext()) {
					CaseRawTableVO selectedOptionVO = (CaseRawTableVO) var19.next();
					if (conditionString.equals("")) {
						conditionString = "'" + selectedOptionVO.getSelectedOption() + "'";
					} else {
						conditionString = conditionString + ",'" + selectedOptionVO.getSelectedOption() + "'";
					}
				}

				if (caseRawTableVO.getSelectedInfo().equals("REPORT_TYPE")) {
					excelFilter.setSelectedReportType(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("CLIENT_CODE")) {
					excelFilter.setSelectedClientCode(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("CASE_MANAGER")) {
					excelFilter.setSelectedCaseManager(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("CASE_STATUS")) {
					excelFilter.setSelectedCaseStatus(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("PRIMARY_TEAM_OFFICE")) {
					excelFilter.setSelectedPriTeamOffice(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("SUPPORTING_1_TEAM_OFFICE")) {
					excelFilter.setSelectedSuppTeam1(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("SUPPORTING_2_TEAM_OFFICE")) {
					excelFilter.setSelectedSuppTeam2(conditionString);
				}

				if (caseRawTableVO.getSelectedInfo().equals("SUPPORTING_3_TEAM_OFFICE")) {
					excelFilter.setSelectedSuppTeam3(conditionString);
				}
			}
		}

		excelFilter.setStartDate(startDate);
		excelFilter.setEndDate(endDate);
		excelFilter.setButtonName(buttonName);
		excelFilter.setDateType(dateType);
		int rowCount = this.caseRawDataDAO.getExportToExcelDataCount(excelFilter);
		int colCount = selectedField.size();
		this.logger.debug("rowCount " + rowCount);
		this.logger.debug("colCount " + colCount);
		if (rowCount > 0 && colCount > 0) {
			cellsInExportedExcel = rowCount * colCount;
		}

		resultMap.put("rowCount", rowCount);
		resultMap.put("cellsInExportedExcel", cellsInExportedExcel);
		this.logger.debug("Exit CaseRawDataReport::  crdExcelCount");
		return resultMap;
	}

	public List<ReportTypeMasterVO> getSubReportList() throws CMSException {
		return this.caseRawDataDAO.getSubReportList();
	}

	public List<MaterializedViewVO> getMaterializedViewRefreshTime(String mvName) throws CMSException {
		return this.caseRawDataDAO.getMaterializedViewRefreshTime(mvName);
	}
}