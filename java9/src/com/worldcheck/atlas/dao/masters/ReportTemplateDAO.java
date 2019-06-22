package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTemplateMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class ReportTemplateDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.ReportTemplateDAO");
	private String insertReportTempDown = "ReportTemplDown.insertReportTempDown";
	private String searchReportTemplDown = "ReportTemplDown.searchReportTemplDown";
	private String searchReportTemplDownCount = "ReportTemplDown.searchReportTemplDownCount";
	private String getInsertedData = "ReportTemplDown.getInsertedData";
	private String updateHistoryStatus = "ReportTemplDown.updateHistoryStatus";
	private String updateRecord = "ReportTemplDown.updateRecord";
	private String getUpdatedRecord = "ReportTemplDown.getUpdatedRecord";
	private String getGeneralClient = "ReportTemplDown.getGeneralClients";
	private String getFilesForCaseDetail = "ReportTemplDown.getFilesForCaseDetail";
	private String getFilesForCaseDetailCount = "ReportTemplDown.getFilesForCaseDetailCount";
	private String getHistoryDataOfClient = "ReportTemplDown.getHistoryDataOfClient";

	public int insertReportTemplate(ReportTemplateMasterVO reportTemplateMasterVO) throws CMSException {
		this.logger.debug("In insertReportTemplate(ReportTemplateMasterVO)");

		try {
			return (Integer) this.insert(this.insertReportTempDown, reportTemplateMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<ReportTemplateMasterVO> searchReportTemplDown(ReportTemplateMasterVO reportTemplateMasterVO)
			throws CMSException {
		this.logger.debug("In searchReportTemplDown method");
		List searchList = null;

		try {
			searchList = this.queryForList(this.searchReportTemplDown, reportTemplateMasterVO);
			return searchList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int searchReportTemplDownCount(ReportTemplateMasterVO reportTemplateMasterVO) throws CMSException {
		this.logger.debug("In searchReportTemplDownCount method");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject(this.searchReportTemplDownCount, reportTemplateMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public ReportTemplateMasterVO getInsertedData(int id) throws CMSException {
		this.logger.debug("In getInsertedData method");
		ReportTemplateMasterVO rtmvo = null;

		try {
			rtmvo = (ReportTemplateMasterVO) this.queryForObject(this.getInsertedData, id);
			return rtmvo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ReportTemplateMasterVO> getHistoryDataOfClient(String clientCode) throws CMSException {
		this.logger.debug("In getInsertedData method");
		ArrayList rtmvo = null;

		try {
			rtmvo = (ArrayList) this.queryForList(this.getHistoryDataOfClient, clientCode);
			this.logger.debug("number of record in history:" + rtmvo.size());
			return rtmvo;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateHistoryStatus(HashMap<String, String> hmap) throws CMSException {
		this.logger.debug("In updateHistoryStatus method");
		boolean var2 = false;

		try {
			int updated = this.update(this.updateHistoryStatus, hmap);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateRecord(ReportTemplateMasterVO rtdmvo) throws CMSException {
		this.logger.debug("In updateRecord method");
		boolean var2 = false;

		try {
			int updated = this.update(this.updateRecord, rtdmvo);
			return updated;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ClientMasterVO> getGeneralClientList() throws CMSException {
		this.logger.debug("In getGeneralClientList method");
		List generalClientList = null;

		try {
			generalClientList = this.queryForList(this.getGeneralClient);
			return generalClientList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<ReportTemplateMasterVO> getFilesForCaseDetail(HashMap hmap) throws CMSException {
		this.logger.debug("In getFilesForCaseDetail method");
		List searchList = null;

		try {
			searchList = this.queryForList(this.getFilesForCaseDetail, hmap);
			return searchList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getFilesForCaseDetailCount(HashMap hmap) throws CMSException {
		this.logger.debug("In getFilesForCaseDetailCount method");
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject(this.getFilesForCaseDetailCount, hmap);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}