package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.ReportTemplateDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTemplateMasterVO;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;

public interface IReportTemplateMaster {
	ReportTemplateDAO getReportTemplateDAO();

	void setReportTemplateDAO(ReportTemplateDAO var1);

	HashMap<String, ReportTemplateMasterVO> addReportTemplate(ReportTemplateMasterVO var1, File var2, String var3)
			throws CMSException;

	void upLoadReportTemplate(ReportTemplateMasterVO var1, HttpServletRequest var2) throws CMSException;

	void deleteReportTemplate(HttpServletRequest var1, ReportTemplateMasterVO var2) throws CMSException;

	List<ReportTemplateMasterVO> searchReportTemplDown(ReportTemplateMasterVO var1, int var2, int var3, String var4,
			String var5) throws CMSException;

	int searchReportTemplDownCount(ReportTemplateMasterVO var1) throws CMSException;

	void getHistoryReportTemplate(ReportTemplateMasterVO var1);

	void removeHistoryReportTemplate(ReportTemplateMasterVO var1, HttpServletRequest var2) throws CMSException;

	Properties getproperties() throws CMSException;

	void deleteUserFiles(String var1) throws CMSException;

	void deleteDirectory(File var1) throws CMSException;

	List<ClientMasterVO> getGeneralClients() throws CMSException;

	List<ReportTemplateMasterVO> getFilesForCaseDetail(HashMap var1) throws CMSException;

	int getFilesForCaseDetailCount(HashMap var1) throws CMSException;
}