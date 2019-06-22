package com.worldcheck.atlas.isis.bl.interfaces;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.ArrayOf_xsd_anyType;
import com.worldcheck.atlas.isis.vo.CaseDetailsVO;
import com.worldcheck.atlas.isis.vo.CaseResultVO;
import com.worldcheck.atlas.isis.vo.DownloadOnlineReportResultVO;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

public interface IAtlasWebServiceManager {
	CaseResultVO createCaseForISIS(CaseDetailsVO var1) throws CMSException;

	CaseResultVO updateCaseForISIS(CaseDetailsVO var1) throws ParseException, CMSException;

	DownloadOnlineReportResultVO downloadOnlineReport(String var1, String var2, String var3) throws CMSException;

	CaseResultVO cancelOnlineOrder(String var1) throws CMSException;

	String createISISCase(CaseDetailsVO var1) throws CMSException;

	long createPIDForISIS(CaseDetailsVO var1, boolean var2) throws CMSException;

	void insertToClinetCase(CaseDetailsVO var1, long var2) throws ParseException, CMSException;

	void insertTOAccountsAndNotification(CaseDetailsVO var1) throws CMSException;

	ArrayOf_xsd_anyType createSubjectForISISCase(CaseDetailsVO var1) throws CMSException;

	void completeComplteCaseCreation(long var1) throws CMSException;

	void downLoadFilesFromFTP(CaseDetailsVO var1, long var2);

	void createDocsForISIS(String var1, String var2, String var3, String var4) throws CMSException;

	List convertCommaStringToList(String var1) throws CMSException;

	void updateISISCase(CaseDetailsVO var1) throws CMSException, ParseException;

	ArrayOf_xsd_anyType updateSubjects(CaseDetailsVO var1, String var2) throws CMSException;

	void updateFiles(CaseDetailsVO var1);

	String convertDateForDS(Calendar var1);

	Timestamp convertDateToTimeStamp(Calendar var1) throws ParseException;

	Date convertDateToSqlDate(Calendar var1);

	void updateSBMDSForCaseUpdate(CaseDetailsVO var1) throws CMSException;

	void updateClientCaseForCaseUpdate(CaseDetailsVO var1) throws ParseException, CMSException;

	boolean checkDatabaseConnection() throws CMSException;
}