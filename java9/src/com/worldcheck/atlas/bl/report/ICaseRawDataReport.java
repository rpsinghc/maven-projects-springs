package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.dao.report.CaseRawDataDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.JSONBean;
import com.worldcheck.atlas.vo.MaterializedViewVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.report.CaseRawTableVO;
import java.util.HashMap;
import java.util.List;

public interface ICaseRawDataReport {
	void setCaseRawDataDAO(CaseRawDataDAO var1);

	List<CaseRawTableVO> getCaseRawDataGrid(CaseRawTableVO var1, int var2, int var3, String var4, String var5)
			throws CMSException;

	int getCaseRawDataGridCount(CaseRawTableVO var1) throws CMSException;

	List<UserMasterVO> getAllTemplateCreator() throws CMSException;

	boolean isExistCaseRawDataTemplateName(String var1) throws CMSException;

	List<CaseRawTableVO> getInfoListCaseRawDataTemplate() throws CMSException;

	List<JSONBean> getAvailableOption(String var1) throws CMSException;

	int saveCaseRawDataTemplate(CaseRawTableVO var1) throws CMSException;

	long deleteCaseRawDataTemplate(long var1) throws CMSException;

	CaseRawTableVO getCaseRawDataTemplateInfo(long var1) throws CMSException;

	int updateCaseRawDataTemplate(CaseRawTableVO var1) throws CMSException;

	long setDefaultCaseRawDataTemplate(long var1, long var3, String var5) throws CMSException;

	List<CaseRawTableVO> getSelectedInfoFieldsListCaseRawDataTemplate(long var1) throws CMSException;

	List<CaseRawTableVO> getSelectedOption(String var1, long var2) throws CMSException;

	CaseRawTableVO caseRawDataExportToExcel(String var1, String var2, String var3, String var4, String var5)
			throws CMSException;

	HashMap<String, Integer> crdExcelCount(String var1, String var2, String var3, String var4, String var5)
			throws CMSException;

	List<ReportTypeMasterVO> getSubReportList() throws CMSException;

	List<MaterializedViewVO> getMaterializedViewRefreshTime(String var1) throws CMSException;
}