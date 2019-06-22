package com.worldcheck.atlas.isis.services;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.bl.interfaces.IAtlasWebServiceManager;
import com.worldcheck.atlas.isis.util.WebServiceMailSender;
import com.worldcheck.atlas.isis.vo.CaseDetailsVO;
import com.worldcheck.atlas.isis.vo.CaseResultVO;
import com.worldcheck.atlas.isis.vo.DownloadOnlineReportResultVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;

public class AtlasWebService {
	private WebServiceMailSender webServiceMailSender;
	private IAtlasWebServiceManager atlasWebServiceManager;
	private String loggerClass = "com.worldcheck.atlas.isis.services.AtlasWebService";
	private ILogProducer logger;

	public AtlasWebService() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
	}

	public void setAtlasWebServiceManager(IAtlasWebServiceManager atlasWebServiceManager) {
		this.atlasWebServiceManager = atlasWebServiceManager;
	}

	public void setWebServiceMailSender(WebServiceMailSender webServiceMailSender) {
		this.webServiceMailSender = webServiceMailSender;
	}

	public CaseResultVO createCaseForISIS(CaseDetailsVO caseDetailsVO, String soapBodyString) {
		this.logger.debug("inside createCaseForISIS method of AtlasWebService class");
		CaseResultVO caseResultVO = new CaseResultVO();

		try {
			this.webServiceMailSender.sendMail("Create case request from ISIS for GUID " + caseDetailsVO.getOrderGUID(),
					soapBodyString);
			caseResultVO = this.atlasWebServiceManager.createCaseForISIS(caseDetailsVO);
			this.logger.info("Case created successfully for ISIS request, genrated crn is: " + caseResultVO.getCrn());
			ResourceLocator.self().getCaseDetailService().getMailBody(caseResultVO.getCrn(), "1", "0",
					String.valueOf(caseDetailsVO.getBulkOrder()));
		} catch (IllegalArgumentException var5) {
			caseResultVO.setErrorCode("COC_3");
			caseResultVO.setErrorMessage(var5.getMessage());
			caseResultVO.setSuccess(false);
		} catch (CMSException var6) {
			caseResultVO.setErrorCode("COC_4");
			caseResultVO.setErrorMessage("Logical error occured while processing data for create case");
			caseResultVO.setSuccess(false);
		} catch (Exception var7) {
			caseResultVO.setErrorCode("COC_4");
			caseResultVO.setErrorMessage("Logical error occured while processing data for create case");
			caseResultVO.setSuccess(false);
		}

		this.webServiceMailSender.sendMail("Create Case response for GUID " + caseDetailsVO.getOrderGUID(),
				"Success: " + caseResultVO.isSuccess() + "\nCRN is: " + caseResultVO.getCrn() + "\nError Code is: "
						+ caseResultVO.getErrorCode() + "\nError Messages is: " + caseResultVO.getErrorMessage());
		return caseResultVO;
	}

	public CaseResultVO updateCaseForISIS(CaseDetailsVO caseDetailsVO, String soapBodyString) {
		this.logger.debug("inside updateCaseForISIS method of AtlasWebService class");
		CaseResultVO caseResultVO = new CaseResultVO();

		try {
			this.webServiceMailSender.sendMail("Update Case request for CRN " + caseDetailsVO.getCrn(), soapBodyString);
			caseResultVO = this.atlasWebServiceManager.updateCaseForISIS(caseDetailsVO);
			this.logger.info("Case updated successfully for ISIS request, updated crn is: " + caseResultVO.getCrn());
		} catch (IllegalArgumentException var5) {
			caseResultVO.setCrn(caseDetailsVO.getCrn());
			caseResultVO.setErrorCode("UOC_3");
			caseResultVO.setErrorMessage(var5.getMessage());
			caseResultVO.setSuccess(false);
		} catch (CMSException var6) {
			caseResultVO.setCrn(caseDetailsVO.getCrn());
			caseResultVO.setErrorCode("UOC_4");
			caseResultVO.setErrorMessage("Logical error occured while processing data for update case");
			caseResultVO.setSuccess(false);
		} catch (Exception var7) {
			caseResultVO.setCrn(caseDetailsVO.getCrn());
			caseResultVO.setErrorCode("UOC_4");
			caseResultVO.setErrorMessage("Logical error occured while processing data for update case");
			caseResultVO.setSuccess(false);
		}

		this.webServiceMailSender.sendMail("Update Case response for CRN " + caseDetailsVO.getCrn(),
				"Success: " + caseResultVO.isSuccess() + "\nCRN is: " + caseResultVO.getCrn() + "\nError Code is: "
						+ caseResultVO.getErrorCode() + "\nError Messages is: " + caseResultVO.getErrorMessage());
		return caseResultVO;
	}

	public DownloadOnlineReportResultVO downloadOnlineReport(String crn, String fileName, String version,
			String soapBodyString) {
		this.logger.debug("inside downloadOnlineReport method of AtlasWebService class");
		DownloadOnlineReportResultVO downloadOnlineReportResultVO = new DownloadOnlineReportResultVO();

		try {
			this.webServiceMailSender.sendMail("DownLoad OnLine Report request for CRN " + crn, soapBodyString);
			downloadOnlineReportResultVO = this.atlasWebServiceManager.downloadOnlineReport(crn, fileName, version);
			this.logger.info("successfully completed ISIS request for download online report for crn : "
					+ downloadOnlineReportResultVO.getCrn());
		} catch (IllegalArgumentException var7) {
			downloadOnlineReportResultVO.setCrn(crn);
			downloadOnlineReportResultVO.setErrorCode("DOR_3");
			downloadOnlineReportResultVO.setErrorMessage(var7.getMessage());
			downloadOnlineReportResultVO.setSuccess(false);
		} catch (CMSException var8) {
			downloadOnlineReportResultVO.setCrn(crn);
			downloadOnlineReportResultVO.setErrorCode("DOR_4");
			downloadOnlineReportResultVO
					.setErrorMessage("Logical error occured while processing data for download report");
			downloadOnlineReportResultVO.setSuccess(false);
		} catch (Exception var9) {
			downloadOnlineReportResultVO.setCrn(crn);
			downloadOnlineReportResultVO.setErrorCode("DOR_4");
			downloadOnlineReportResultVO
					.setErrorMessage("Logical error occured while processing data for download report");
			downloadOnlineReportResultVO.setSuccess(false);
		}

		this.webServiceMailSender.sendMail("DownLoad On Line Report response for CRN " + crn,
				"Error Code is " + downloadOnlineReportResultVO.getErrorCode() + "Success: "
						+ downloadOnlineReportResultVO.getSuccess() + "\nCRN is: "
						+ downloadOnlineReportResultVO.getCrn() + "\nError Code is: "
						+ downloadOnlineReportResultVO.getErrorCode() + "\nError Messages is: "
						+ downloadOnlineReportResultVO.getErrorMessage());
		return downloadOnlineReportResultVO;
	}

	public CaseResultVO cancelOnlineOrder(String crn, String soapBodyString) {
		this.logger.debug("inside cancelOnlineOrder method of AtlasWebService class");
		CaseResultVO caseResultVO = new CaseResultVO();

		try {
			this.webServiceMailSender.sendMail("Cancel Case request for CRN " + crn, soapBodyString);
			caseResultVO = this.atlasWebServiceManager.cancelOnlineOrder(crn);
			this.logger.info("Case canceled successfully for crn :+crn");
		} catch (IllegalArgumentException var5) {
			caseResultVO.setCrn(crn);
			caseResultVO.setErrorCode("COO_3");
			caseResultVO.setErrorMessage(var5.getMessage());
			caseResultVO.setSuccess(false);
		} catch (CMSException var6) {
			caseResultVO.setCrn(crn);
			caseResultVO.setErrorCode("COO_4");
			caseResultVO.setErrorMessage("Logical error occured while processing data for cancel case");
			caseResultVO.setSuccess(false);
		} catch (Exception var7) {
			caseResultVO.setCrn(crn);
			caseResultVO.setErrorCode("COO_4");
			caseResultVO.setErrorMessage("Logical error occured while processing data for cancel case");
			caseResultVO.setSuccess(false);
		}

		this.webServiceMailSender.sendMail("Cancel Case response for CRN " + crn,
				"Success: " + caseResultVO.isSuccess() + "\nCRN is: " + caseResultVO.getCrn() + "\nError Code is: "
						+ caseResultVO.getErrorCode() + "\nError Messages is: " + caseResultVO.getErrorMessage());
		return caseResultVO;
	}

	public String pingAtlas() {
		this.logger.debug("inside pingAtlas method of AtlasWebService class");
		this.logger.info("ping Atlas request completed successfully..");

		try {
			if (this.atlasWebServiceManager.checkDatabaseConnection()) {
				return "Ping_0";
			}
		} catch (CMSException var2) {
			this.logger.debug("Unable to connect with database");
		}

		return "Ping_1";
	}
}