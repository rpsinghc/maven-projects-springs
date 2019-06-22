package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.REMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.report.SubReportTypeVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class ReportTypeDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.ReportTypeDAO");
	private String REPORT_ID_LIST = "reportTypeIdList";
	private String REPORT_STATUS = "status";

	public List<ReportTypeMasterVO> searchReportType(String rptMasterId) throws CMSException {
		try {
			this.logger.debug("reportType MasterID:" + rptMasterId);
			new ArrayList();
			List<ReportTypeMasterVO> reportTypeList = this.queryForList("ReportTypeMaster.searchReportType",
					rptMasterId);
			this.logger.debug("searchReportType size :" + reportTypeList.size());
			return reportTypeList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public long addReportType(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		long id = 0L;

		try {
			this.insert("ReportTypeMaster.addReportType", reportTypeMasterVO);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("generated id is ::::  " + id);
		return id;
	}

	public void insertReMap(String reIds, long id, String userName) throws CMSException {
		HashMap<String, String> map = new HashMap();
		map.put("reIds", reIds);
		map.put("rptId", String.valueOf(id));
		map.put("userName", userName);

		try {
			this.insert("ReportTypeMaster.addReportTypeReMap", map);
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List<String> getReId(String reName) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("ReportTypeMaster.getReId", reName);
			return list;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public long insertSubReport(ReportTypeMasterVO rptVO) throws CMSException {
		long id = 0L;

		try {
			this.insert("ReportTypeMaster.insertSubReport", rptVO);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("generated id is ::::  " + id);
		return id;
	}

	public void insertSubReportReMap(String reIds, long subrptId, String userName) throws CMSException {
		HashMap<String, String> map = new HashMap();
		map.put("reIds", reIds);
		map.put("rptId", String.valueOf(subrptId));
		map.put("userName", userName);

		try {
			this.insert("ReportTypeMaster.addSubReportReMap", map);
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public List<REMasterVO> getReForRpt(long rptId) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("ReportTypeMaster.getReForRpt", rptId);
			return list;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<ReportTypeMasterVO> getCmpOrIndReForRpt() throws CMSException {
		List list = null;

		try {
			list = this.queryForList("ReportTypeMaster.getCmpOrIndReForRpt");
			return list;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public ReportTypeMasterVO getRptInfo(long rptId) throws CMSException {
		ReportTypeMasterVO vo = null;
		this.logger.debug("report type id in getReptInfo is : :: " + rptId);

		try {
			vo = (ReportTypeMasterVO) this.queryForObject("ReportTypeMaster.getRptInfo", rptId);
			return vo;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<ReportTypeMasterVO> getSubReport(long rptIdVal) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("ReportTypeMaster.getSubReport", rptIdVal);
			return list;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<ReportTypeMasterVO> getReForSubReport() throws CMSException {
		List list = null;

		try {
			list = this.queryForList("ReportTypeMaster.getReForSubReport");
			return list;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<REMasterVO> getReList() throws CMSException {
		List list = null;

		try {
			list = this.queryForList("ReportTypeMaster.getReList");
			return list;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getSubRptCount(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("ReportTypeMaster.getsubRptCount", reportTypeMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ReportTypeMasterVO> searchSubReportType(String rptMasterId) throws CMSException {
		try {
			new ArrayList();
			List<ReportTypeMasterVO> reportTypeList = this.queryForList("ReportTypeMaster.searchSubReportType",
					rptMasterId);
			return reportTypeList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<Long> getSubReportId(long rptIdVal) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("ReportTypeMaster.getSubRptId", rptIdVal);
			return list;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void deleteSubRe(String idString) throws CMSException {
		try {
			this.delete("ReportTypeMaster.deleteSubRe", idString);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void deleteSubReport(String idString) throws CMSException {
		try {
			this.delete("ReportTypeMaster.deleteSubRpt", idString);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void updateReportType(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		try {
			this.update("ReportTypeMaster.updateReportMaster", reportTypeMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public void deleteReportRe(long reportTypeId) throws CMSException {
		try {
			this.delete("ReportTypeMaster.deleteReportRe", reportTypeId);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int changeStatus(String idString, String status) throws CMSException {
		HashMap<String, Object> paramMap = new HashMap();
		List<String> reportTypeIdList = null;
		boolean var5 = true;

		try {
			reportTypeIdList = StringUtils.commaSeparatedStringToList(idString);
			paramMap.put(this.REPORT_ID_LIST, reportTypeIdList);
			paramMap.put(this.REPORT_STATUS, status);
			int count = this.update("ReportTypeMaster.changeStatus", paramMap);
			return count;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public int checkSubRpt(long rptIdVal) throws CMSException {
		boolean var3 = false;

		try {
			int count = (Integer) this.queryForObject("ReportTypeMaster.checkSubRpt", rptIdVal);
			return count;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<Long> getReportTypeMasterId(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("ReportTypeMaster.gerRptMasterId", reportTypeMasterVO);
			return list;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int checkReportIfExist(String rptName) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("ReportTypeMaster.checkReportIfExist", rptName);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public long getMaxId() throws CMSException {
		long count = 0L;

		try {
			count = (Long) this.queryForObject("ReportTypeMaster.getMaxId");
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ReportTypeMasterVO> getReportTypeMasterGrid(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		try {
			this.logger.debug("Report Type :" + reportTypeMasterVO.getReportType() + " Status:"
					+ reportTypeMasterVO.getReportTypeStatus());
			this.logger.debug("Column Name:" + reportTypeMasterVO.getSortColumnName() + ">> SortType:"
					+ reportTypeMasterVO.getSortType());
			new ArrayList();
			List<ReportTypeMasterVO> reportTypeList = this.queryForList("ReportTypeMaster.reportTypeMasterGrid",
					reportTypeMasterVO);
			this.logger.debug("Total Reports Fetched:" + reportTypeList.size());
			this.logger.debug("countryGridList size :" + reportTypeList.size());
			return reportTypeList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getReportTypeMasterCount(ReportTypeMasterVO reportTypeMasterVO) throws CMSException {
		this.logger
				.debug("count Total Record Fetched:>>>>>>>>>>>>>>>>>>>>>>inside dao" + reportTypeMasterVO.getStart());

		try {
			return (Integer) this.queryForObject("ReportTypeMaster.reportTypeMasterCount", reportTypeMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int isSubReportTypeExist(SubReportTypeVO subReportTypeVO) throws CMSException {
		this.logger.debug("inside isSubReportTypeExist");

		try {
			int count = Integer
					.parseInt(this.queryForObject("ReportTypeMaster.isSubReportTypeExist", subReportTypeVO).toString());
			this.logger.debug("SubRportType Name Found:" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int isSubReportInitialExist(String subReportTypeInitial) throws CMSException {
		this.logger.debug("inside isSubReportTypeExist");

		try {
			int count = Integer.parseInt(
					this.queryForObject("ReportTypeMaster.isSubReportInitialExist", subReportTypeInitial).toString());
			this.logger.debug("SubRportType Name Found:" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public ReportTypeMasterVO checkAssociatedMaster(String reportTypeId) throws CMSException {
		this.logger.debug("inside ReportTypeDAO :: checkAssociatedMaster reportTypeId::" + reportTypeId);

		try {
			return (ReportTypeMasterVO) this.queryForObject("ReportTypeMaster.checkAssociatedMaster", reportTypeId);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public ReportTypeMasterVO checkAssociatedSubreport(String subReportTypeId) throws CMSException {
		this.logger.debug("inside ReportTypeDAO :: checkAssociatedSubreport reportTypeId::" + subReportTypeId);

		try {
			return (ReportTypeMasterVO) this.queryForObject("ReportTypeMaster.checkAssociatedSubreport",
					subReportTypeId);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int checkInitialCRNExist(String initialCRN) throws CMSException {
		this.logger.debug("inside checkInitialCRNExist::" + initialCRN);

		try {
			int count = Integer
					.parseInt(this.queryForObject("ReportTypeMaster.checkInitialCRNExist", initialCRN).toString());
			this.logger.debug("Initial CRN Found:" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deActiveSubReport(String idString) throws CMSException {
		try {
			int count = this.update("ReportTypeMaster.deActiveSubReport", idString);
			return count;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int activeSubReport(String idString) throws CMSException {
		try {
			int count = this.update("ReportTypeMaster.activeSubReport", idString);
			return count;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getSubReportType(String subReportId) throws CMSException {
		String subReport = "";

		try {
			subReport = (String) this.queryForObject("ReportTypeMaster.getSubReportType", subReportId);
			return subReport;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deleteActSubReport(String subReportId) throws CMSException {
		try {
			int count = this.update("ReportTypeMaster.deleteActSubReport", subReportId);
			return count;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public boolean isSUBreportExist(String subReportType) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("ReportTypeMaster.isSubReportExist", subReportType);
			return count > 0;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getSUBreportId(String subReportType) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("ReportTypeMaster.getSUBreportId", subReportType);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void deleteSubReportRe(long subReportTypeId) throws CMSException {
		try {
			this.delete("ReportTypeMaster.deleteSubReportRe", subReportTypeId);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public void updateSubreport(String initialsEndCrn, String subReportName) throws CMSException {
		try {
			HashMap<String, Object> map = new HashMap();
			map.put("subReportName", subReportName);
			map.put("initialsEndCrn", initialsEndCrn);
			this.update("ReportTypeMaster.updateSubReportMaster", map);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<SubReportTypeVO> getSubReportDetails(long subrptIdVal) throws CMSException {
		this.logger.debug("IN ReportTypeDAO::getSubReportDetails");
		List list = null;

		try {
			list = this.queryForList("ReportTypeMaster.getSubReportDetails", subrptIdVal);
			return list;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public long getReportSeq() throws CMSException {
		long count = 0L;

		try {
			count = (Long) this.queryForObject("ReportTypeMaster.getReportypeID");
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public long getSubReportSeq() throws CMSException {
		long count = 0L;

		try {
			count = (Long) this.queryForObject("ReportTypeMaster.getSubReportypeID");
			this.logger.debug("Count #####" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}