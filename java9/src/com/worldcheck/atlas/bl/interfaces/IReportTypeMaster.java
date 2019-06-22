package com.worldcheck.atlas.bl.interfaces;

import java.io.IOException;
import java.util.List;

import org.xml.sax.SAXException;

import com.worldcheck.atlas.dao.masters.ReportTypeDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.history.AtlasHistoryVO;
import com.worldcheck.atlas.vo.masters.REMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;

/*
 ***********************************************************************
 * Copyright (c) 2010 World-Check, All Rights Reserved.
 *
 ** Source:package com.worldcheck.atlas.bl.masters;
 ** IReportTypeMaster.java
 ** Application Name:WorldCheckCMS
 ** Application SIC Code:
 *
 * 
 ** Author:@author
 *
 ** Date Created: 2:10:55 AM
 *
 ** General Description(Purpose):
 *
 ** Usage Examples:
 *
 ** Known Issues/Possible Points of Failure:
 *
 ** Update History:
 *
 *
 ***********************************************************************
 */

public interface IReportTypeMaster {

	/**
	 * @param reportTypeDAO
	 *            the reportTypeDAO to set
	 */
	public void setReportTypeDAO(ReportTypeDAO reportTypeDAO);

	public String addReportISIS(ReportTypeMasterVO reportTypeMasterVO) throws CMSException;
	/**
	 * This method will perform add operation for Report type master
	 * 
	 * @param reportTypeMasterVO
	 * @throws CMSException
	 */
	public String addReportType(ReportTypeMasterVO reportTypeMasterVO)
			throws CMSException;

	/**
	 * @param reportTypeMasterVO
	 * @return
	 * @throws CMSException
	 */
	public List<ReportTypeMasterVO> searchReportType(String rptMasterId)
			throws CMSException;

	/**
	 * This method will return list of RE's
	 * 
	 * @param rptIdVal
	 * @return
	 * @throws CMSException
	 */
	public List<REMasterVO> getReForRpt(long rptIdVal) throws CMSException;

	/**
	 * This method will fetch report type data and populate the VO to update the
	 * report type record
	 * 
	 * @param rptIdVal
	 * @return
	 * @throws CMSException
	 */
	public ReportTypeMasterVO getRptInfo(long rptIdVal) throws CMSException;

	/**
	 * @param rptIdVal
	 * @return
	 * @throws CMSException
	 */
	public List<ReportTypeMasterVO> getSubReport(long rptIdVal)
			throws CMSException;

	/**
	 * @return
	 * @throws CMSException
	 */
	public List<REMasterVO> getReList() throws CMSException;

	/**
	 * @param reportTypeMasterVO
	 * @return
	 * @throws CMSException
	 */
	public int getSubRptCount(ReportTypeMasterVO reportTypeMasterVO)
			throws CMSException;

	/**
	 * @param reportTypeMasterVO
	 * @return
	 * @throws CMSException
	 */
	public List<ReportTypeMasterVO> searchSubReportType(String rptMasterId)
			throws CMSException;

	public String updateReportISIS(ReportTypeMasterVO reportTypeMasterVO) throws CMSException;
	/**
	 * This method will update Report type
	 * 
	 * @param reportTypeMasterVO
	 * @throws CMSException
	 */
	public String updateReportType(ReportTypeMasterVO reportTypeMasterVO)
			throws CMSException;

	/**
	 * This method will deactivate Report type in system
	 * 
	 * @param rptId
	 * @throws CMSException
	 */
	public String changeStatus(String rptId, String status,String userName) throws CMSException;

	/**
	 * This method will return count as 0 if there is no subreport found for
	 * Report type
	 * 
	 * @param rptIdVal
	 * @return
	 * @throws CMSException
	 */
	public int checkSubRpt(long rptIdVal) throws CMSException;

	/**
	 * This method will return all the report master id as comma seperated
	 * String .This string will use in search function
	 * 
	 * @param reportTypeMasterVO
	 * @return
	 * @throws CMSException
	 */
	public String getReportTypeMasterId(ReportTypeMasterVO reportTypeMasterVO)
			throws CMSException;

	/**
	 * This method will return 0 if there is no match found for given report
	 * name
	 * 
	 * @param rptName
	 * @return
	 * @throws CMSException
	 */
	public int checkReportIfExist(String rptName) throws CMSException;
	public int checkInitialCRNExist(String initialCRN) throws CMSException;
	public List<ReportTypeMasterVO> getReportTypeGrid(
			ReportTypeMasterVO reportTypeMasterVO, int start, int limit,
			String sortColumnName, String sortType) throws CMSException;

	public int getReportTypeCount(ReportTypeMasterVO reportTypeMasterVO)
			throws CMSException;
	public boolean isSubReportTypeExist(String subReportTypeName) throws CMSException;
	
	//This Code is added by satish to check the uniquess of (Initials to use at End of CRN )
	
	public boolean isSubReportInitialExist(String subReportTypeName) throws CMSException;
	
	public ReportTypeMasterVO checkAssociatedMaster(String reportTypeId)throws CMSException;
	
	public List<ReportTypeMasterVO> getCmpOrIndReForRpt() throws CMSException;
	
	public List<ReportTypeMasterVO> getReForSubReport() throws CMSException;

	/**
	 * @param atlasHistoryVO
	 * @return
	 */
	public List<AtlasHistoryVO> getReportHistory(AtlasHistoryVO atlasHistoryVO) throws CMSException, SAXException, IOException;
	public int getReportHistoryCount(AtlasHistoryVO atlasHistoryVO) throws CMSException;
}