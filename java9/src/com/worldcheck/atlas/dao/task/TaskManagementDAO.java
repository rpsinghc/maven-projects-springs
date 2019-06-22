package com.worldcheck.atlas.dao.task;

import com.integrascreen.orders.Subject;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.audit.RiskHistory;
import com.worldcheck.atlas.vo.document.DocMapVO;
import com.worldcheck.atlas.vo.masters.CategoryDetailsVO;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ClientRequirmentMasterVO;
import com.worldcheck.atlas.vo.masters.CountryHBDVO;
import com.worldcheck.atlas.vo.masters.RiskAttributeVO;
import com.worldcheck.atlas.vo.masters.RiskCategoryMasterVO;
import com.worldcheck.atlas.vo.masters.RiskProfileVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.report.SubReportTypeVO;
import com.worldcheck.atlas.vo.task.CountryDBNamesVO;
import com.worldcheck.atlas.vo.task.CountryDatabaseVO;
import com.worldcheck.atlas.vo.task.CountrySubjectVO;
import com.worldcheck.atlas.vo.task.CustomTaskVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import com.worldcheck.atlas.vo.task.RecurrentCaseVO;
import com.worldcheck.atlas.vo.task.SubjectListForUserVO;
import com.worldcheck.atlas.vo.task.VendorForCrnDetailsVO;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class TaskManagementDAO extends SqlMapClientTemplate {
	private static final String SELECTED_YEAR2 = "selected_year";
	private static final String RETTOKEN = "rettoken";
	private static final String UPDATED_BY = "updatedBy";
	private static final String TEAM_ID = "teamId";
	private final ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.task.TaskManagementDAO");
	private static final String COUNTRY_LIST = "TaskConfig.getCountries";
	private static final String COUNTRY_DBNAMES_LIST = "TaskConfig.getCountryDBNames";
	private static final String SUBJECT_LIST_FORUSER = "TaskConfig.getSubListForUser";
	private static final String SUBJECT_LEVEL_RISK_FOR_USER = "TaskConfig.getSubLevelRiskForUser";
	private static final String CASE_LEVEL_RISK_FOR_USER = "TaskConfig.getCaseLevelRiskForUser";
	private static final String SUBJECT_LIST_COUNT_FORUSER = "TaskConfig.getSubListCountForUser";
	private static final String RE_LIST = "TaskConfig.getREListForUser";
	private static final String CASE_MANAGER = "TaskConfig.getCaseManagers";
	private static final String CLIENT_REQ_DATA = "TaskConfig.getClientReqData";
	private static final String SUBJECT_INFO = "TaskConfig.getAddedSubject";
	private static final String CRN_SEQ = "TaskConfig.getSeqForCRN";
	private static final String SEQ_CHECK = "TaskConfig.checkForRecurrId";
	private static final String RECURRENT_SEQ = "TaskConfig.getSeqForRecurrence";
	private static final String REPORT_INITIALS = "TaskConfig.getReportInitials";
	private static final String SUB_REPORT_INITIALS = "TaskConfig.getSubReportInitials";
	private static final String SUB_REPORT_INITIALS2 = "TaskConfig.getSubReportInitials2";
	private static final String SUB_REPORT_ID_INITIALS = "TaskConfig.getSubReportInitialsByID";
	private static final String INSERT_RECURRENT_CASE = "TaskConfig.insertRecurrentDetails";
	private static final String SUBREPORT_TYPE_MASTER = "TaskConfig.getSubReportType";
	private static final String UPDTAE_WITH_SUBREPORT = "CreateCase.updateCreateCase_Client";
	private static final String UPDTAE_WITHOUT_SUBREPORT = "CreateCase.updateCreateCase_Client_Without_Subject";
	private static final String VENDOR_DETAILS = "TaskConfig.getVendorDetails";
	private static final String GET_RISK_ASSOCIATED_WITH_CASE = "TaskConfig.getRiskAssociatedWithCase";
	private static final String UPDATE_RISK_FLAG = "TaskConfig.updateRiskFlag";
	private static final String UPDATE_PID = "TaskConfig.updatePID";
	private static final String GET_PID = "TaskConfig.getPID";
	private static final String CLIENT_MASTER_BYCODE = "TaskConfig.client_master_bycode";
	private static final String TOP10_CLIENT_MASTER_BYCODE = "TaskConfig.top10_client_master_bycode";
	private static final String CLIENT_MASTER_BYNAME = "TaskConfig.client_master_byname";
	private static final String TOP10_CLIENT_MASTER_BYNAME = "TaskConfig.top10_client_master_byname";
	private static final String TURN_AROUND_TIME = "TaskConfig.getTurnAround";
	private static final String GET_DOCUEMNTS = "TaskConfig.getDocs";
	private static final String INSERT_DOCUMENTS = "TaskConfig.insertDocs";
	private static final String RESET_STATUS = "TaskConfig.resetStatus";
	private static final String DELETE_DOCUMENTS = "TaskConfig.deleteDocuments";
	private static final String CHECK_SENSITIVE_FOLDER_PERMISSION = "TaskConfig.sensitiveFolderPermission";
	private static final String CHECK_DELETE_FILE_PERMISSION = "TaskConfig.hasDeletePermission";
	private static final String CHECK_UPLOAD_FILE_PERMISSION = "TaskConfig.hasUploadPermissionAfterComplete";
	private static final String GET_OFFICE_ID = "TaskConfig.getOfficeId";
	private static final String GET_OFFICE_CODE = "TaskConfig.getOfficeCode";
	private static final String GET_MARKED_COUNT = "TaskConfig.getMarkedCount";
	private static final String UPDATE_STATUS = "TaskConfig.updateStatus";
	private static final String CHECK_FOR_HOLIDAY = "TaskConfig.isHoliday";
	private static final String CHECK_FOR_HOLIDAY_OFFICE_ID = "TaskConfig.isHolidayForOfficeId";
	private static final String CHECK_FOR_HOLIDAY_CDD = "TaskConfig.isHolidayForClientDueDate";
	private static final String CHECK_FOR_HOLIDAY_ANALYST = "TaskConfig.isHolidayForAnalyst";
	private static final String GET_OFFCIE_FORCLIENT = "TaskConfig.getCountryForClient";
	private String CASE_MANAGER_ROLE = "R2";
	private static final String UPDATE_SEND_DATE = "TaskConfig.updateSendDate";
	private static final String ANALYST_MANAGER_LIST = "TeamAssignment.getAnalystForManager";
	private static final String COUNTRY_DATABASE = "TaskConfig.getCountryDatabaseForCountry";
	private static final String COUNTRY_DATABASE_Count = "TaskConfig.getCountryDatabaseForCountryCount";
	private static final String UPDATE_JUNO_STATUS = "TaskConfig.updateStatusForJuno";
	private static final String UPLOADED_BY_JUNO_STATUS = "TaskConfig.MarkJuno";
	private static final String VERSION_CONTROL = "TaskConfig.versionControl";
	private static final String MAX_DOC_COUNT = "TaskConfig.getMaxCount";
	private static final String MAX_VERSION = "TaskConfig.getMaxVersion";
	private static final String GET_TASK_FOR_MANAGER = "TaskConfig.getTaskForManager";
	private static final String UPDATE_RE_STATUS = "TaskConfig.updateREForUser";
	private static final String IS_FINAL_JUNOUPLOADED_FILE = "TaskConfig.isMarkedAndJUNOUploadedFile";
	private static final String UPDATE_DATEFORMIS = "TaskConfig.updateDateForMIS";
	private static final String GETFILENAME = "TaskConfig.getFileForISIS";
	private static final String ISIS_STATUS = "TaskConfig.isISISCase";
	private static final String GET_VERSION = "TaskConfig.getDocVersion";
	private static final String CBD_VALUE = "TaskConfig.getCBDValue";
	private static final String CUSTOM_TASK = "TaskConfig.getSavvionTasks";
	private static final String CUSTOM_TASK_COUNT = "TaskConfig.getSavvionTasksCount";
	private static final String HEADER_COUNT = "TaskConfig.getHeaderCount";
	private static final String UPDATE_HEADER = "TaskConfig.updateHeader";
	private static final String INSERT_HEADER = "TaskConfig.insertHeader";
	private static final String INCOMING_CUSTOM_TASK = "TaskConfig.getMyIncomingTasks";
	private static final String INCOMING_CUSTOM_TASK_COUNT = "TaskConfig.getMyIncomingTasksCount";
	private static final String GET_ANALYSTS = "TaskConfig.getAnalyst";
	private static final String TEAM_ASSIGNMENT_TASK_LOGIN_USER = "TaskConfig.getTeamAssignmentTasks";
	private static final String SET_LOGOUT_TIME = "TaskConfig.setLogoutTime";
	private static final String SUB_REPORT_TYPE_LIST = "TaskConfig.getSubReportTypeList";
	private static final String UPDATE_OPTIONAL_RISK = "TaskConfig.updateOptionalRisk";
	private static final String UPDATE_RISK_HISTORY_FOR_DELETE = "TaskConfig.insertRiskHistoryForDelete";
	private static final String DELETE_HBD_MAPPING = "TaskConfig.deleteHBDMapping";
	private static final String DELETE_HBD_COUNTRY = "TaskConfig.deleteHBDCountry";
	private static final String GET_PROFILE_ID_FOR_RISK_CODE = "TaskConfig.fetchProfileIdForRiskCode";
	private static final String INSERT_OPTIONAL_RISK_WITH_SUBJECT = "TaskConfig.insertOptionalRiskWithSubject";
	private static final String INSERT_OPTIONAL_RISK_WITHOUT_SUBJECT = "TaskConfig.insertOptionalRiskWithoutSubject";
	private static final String INSERT_HBD_OPTIONAL_RISK_WITH_SUBJECT = "TaskConfig.insertHBDOptionalRiskWithSubject";
	private static final String INSERT_HBD_OPTIONAL_RISK_WITHOUT_SUBJECT = "TaskConfig.insertHBDOptionalRiskWithoutSubject";
	private static final String INSERT_HBD_COUNTRIES_WITH_SUBJECT = "TaskConfig.insertHBDOptionalRiskCountriesWithSubject";
	private static final String INSERT_HBD_COUNTRIES_WITHOUT_SUBJECT = "TaskConfig.insertHBDOptionalRiskCountriesWithoutSubject";
	private static final String GET_NEXT_PROFILE_ID = "TaskConfig.getNextProfileId";
	private static final String GET_COUNTRIES_FOR_RISKCODE = "TaskConfig.getCountriesForRiskCode";
	private static final String GET_CATEGORY_DETAILS = "TaskConfig.getCategoryDetails";
	private static final String CASE_ISIS_ORDER_ID = "TaskConfig.caseISISOrderId";
	private static final String GET_CATEGORY_LABEL = "TaskConfig.getCategoryLabel";
	private static final String getISISSubjectDetails = "TaskConfig.getISISSubjectDetails";
	private static final String getCountryCode = "TaskConfig.getCountryCode";
	private static final String getRiskLabel = "TaskConfig.getRiskLabel";
	private static final String GET_ATTRIBUTE_DEFAULT_VALUES = "TaskConfig.getAttributeDefaultValues";

	public List<CountrySubjectVO> getCntryList(String crn) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("TaskConfig.getCountries", crn);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("size of the list is " + list.size());
		return list;
	}

	public List<CountryDBNamesVO> getcntryDBNames(String countryId) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("TaskConfig.getCountryDBNames", countryId);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("size of the list is " + list.size());
		return list;
	}

	public List<SubjectListForUserVO> getSubjectList(Map<String, String> mapForParams) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("TaskConfig.getSubListForUser", mapForParams);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("size of the list is " + list.size());
		return list;
	}

	public List<SubjectListForUserVO> getREList(Map<String, Object> mapForParams) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("TaskConfig.getREListForUser", mapForParams);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("size of the list is " + list.size());
		return list;
	}

	public List<UserMasterVO> getCaseManagerList() throws CMSException {
		List list = null;

		try {
			list = this.queryForList("TaskConfig.getCaseManagers", this.CASE_MANAGER_ROLE);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("size of the list is " + list.size());
		return list;
	}

	public String getISISOrderId(String crn) throws CMSException {
		String orderId = null;

		try {
			orderId = (String) this.queryForObject("TaskConfig.caseISISOrderId", crn);
			return orderId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getCategoryLabel(long categoryId) throws CMSException {
		String categoryName = null;

		try {
			categoryName = (String) this.queryForObject("TaskConfig.getCategoryLabel", categoryId);
			return categoryName;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ClientRequirmentMasterVO> getClientReqData(String clientName) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("TaskConfig.getClientReqData", clientName);
			this.logger.debug("size of the list is " + list.size());
			return list;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public boolean isSubAdded(String crn) throws CMSException {
		boolean isAdded = false;

		try {
			List<String> list = this.queryForList("TaskConfig.getAddedSubject", crn);
			if (list != null && list.size() > 0) {
				isAdded = true;
			}

			return isAdded;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public long getSeqForCRN(int selected_year) throws CMSException {
		long seqNumber = 0L;
		long getRow = 0L;
		Map<String, Integer> paramMap = new HashMap();
		paramMap.put("selected_year", selected_year);
		paramMap.put("rettoken", 0);

		try {
			do {
				this.queryForList("TaskConfig.getSeqForCRN", paramMap);
				seqNumber = (long) (Integer) paramMap.get("rettoken");
				this.logger.debug("value of seqNumber is " + seqNumber);
				int minimumDigitsNumber = 5;
				NumberFormat numberFormatter = NumberFormat.getInstance();
				numberFormatter.setMinimumIntegerDigits(minimumDigitsNumber);
				numberFormatter.setGroupingUsed(false);
				getRow = (Long) this.queryForObject("TaskConfig.checkForRecurrId",
						numberFormatter.format(seqNumber) + "%");
				this.logger.debug("getRow value is " + getRow + " .seqNumber is " + seqNumber);
			} while (getRow > 0L);

			return seqNumber;
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public Subject getSubjectDetailsForISIS(long subjectId) throws CMSException {
		Subject subjectDetails = null;

		try {
			subjectDetails = (Subject) this.queryForObject("TaskConfig.getISISSubjectDetails", subjectId);
			return subjectDetails;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getCountryCode(long countryId) throws CMSException {
		String countryCode = null;

		try {
			countryCode = (String) this.queryForObject("TaskConfig.getCountryCode", countryId);
			return countryCode;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getRiskLabel(String riskCode) throws CMSException {
		String countryCode = null;

		try {
			countryCode = (String) this.queryForObject("TaskConfig.getRiskLabel", riskCode);
			return countryCode;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public long getSeqForRecurrence() throws CMSException {
		long seqNumber = 0L;

		try {
			seqNumber = (Long) this.queryForObject("TaskConfig.getSeqForRecurrence");
			return seqNumber;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String getInitialsForReport(String reportType) throws CMSException {
		String initials = "";

		try {
			initials = (String) this.queryForObject("TaskConfig.getReportInitials", reportType);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug(initials + " :: " + reportType);
		return initials;
	}

	public String getInitialsForSubReport(String subReportType, String reportType) throws CMSException {
		String initials = "";
		Map<String, String> map = new HashMap();
		this.logger.debug("DAO subReportType :: " + subReportType);
		this.logger.debug("DAO reportType :: " + reportType);
		map.put("subReportType", subReportType);
		map.put("reportType", reportType);

		try {
			initials = (String) this.queryForObject("TaskConfig.getSubReportInitials2", map);
			return initials;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int insertRecordForRecurrentCase(RecurrentCaseVO recurrenceVO) throws CMSException {
		boolean var2 = false;

		try {
			this.insert("TaskConfig.insertRecurrentDetails", recurrenceVO);
			int i = 1;
			return i;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<SubReportTypeVO> getSubReport(String reportType) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("TaskConfig.getSubReportType", reportType);
			this.logger.debug("size of the list is " + list.size());
			return list;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateRecordForCompleteCase(CaseDetails caseDetails) throws CMSException {
		boolean var2 = false;

		try {
			int i;
			if (caseDetails.getSubReportTypeId() == null) {
				i = this.update("CreateCase.updateCreateCase_Client_Without_Subject", caseDetails);
			} else {
				i = this.update("CreateCase.updateCreateCase_Client", caseDetails);
			}

			return i;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<VendorForCrnDetailsVO> getVendorDetails(String crn) throws CMSException {
		List listForVendorDetails = null;

		try {
			listForVendorDetails = this.queryForList("TaskConfig.getVendorDetails", crn);
			return listForVendorDetails;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<String> getRiskAssociatedWithCase(String crn) throws CMSException {
		List listOfRiskCode = null;

		try {
			listOfRiskCode = this.queryForList("TaskConfig.getRiskAssociatedWithCase", crn);
			return listOfRiskCode;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int updateRiskFlag(String crn) throws CMSException {
		boolean var2 = false;

		try {
			int updateRiskFlag = this.update("TaskConfig.updateRiskFlag", crn);
			return updateRiskFlag;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int insertPID(HashMap<String, Object> insertVal) throws CMSException {
		boolean var2 = false;

		try {
			this.insert("TaskConfig.updatePID", insertVal);
			int i = 1;
			return i;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public long getPID(String crn) throws CMSException {
		long i = 0L;

		try {
			i = (Long) this.queryForObject("TaskConfig.getPID", crn);
			return i;
		} catch (ClassCastException var5) {
			throw new CMSException(this.logger, var5);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<ClientMasterVO> getClientMasterByName() throws CMSException {
		new ArrayList();

		try {
			List<ClientMasterVO> clientMasterList = this.queryForList("TaskConfig.top10_client_master_byname");
			if (clientMasterList != null && clientMasterList.size() != 0) {
				ClientMasterVO separatorVO = new ClientMasterVO();
				separatorVO.setClientCode("--");
				separatorVO.setClientName("--");
				separatorVO.setCodeName("=============");
				separatorVO.setIsSubreportRequired("=============");
				clientMasterList.add(separatorVO);
			}

			clientMasterList.addAll(this.queryForList("TaskConfig.client_master_byname"));
			return clientMasterList;
		} catch (ClassCastException var3) {
			throw new CMSException(this.logger, var3);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<ClientMasterVO> getClientMasterByCode() throws CMSException {
		new ArrayList();

		try {
			List<ClientMasterVO> clientMasterList = this.queryForList("TaskConfig.top10_client_master_bycode");
			if (clientMasterList != null && clientMasterList.size() != 0) {
				ClientMasterVO separatorVO = new ClientMasterVO();
				separatorVO.setClientCode("--");
				separatorVO.setClientName("--");
				separatorVO.setCodeName("=============");
				separatorVO.setIsSubreportRequired("=============");
				clientMasterList.add(separatorVO);
			}

			clientMasterList.addAll(this.queryForList("TaskConfig.client_master_bycode"));
			return clientMasterList;
		} catch (ClassCastException var3) {
			throw new CMSException(this.logger, var3);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getTurnAround(String reportType) throws CMSException {
		int i = 0;

		try {
			Object obj = this.queryForObject("TaskConfig.getTurnAround", reportType);
			if (obj != null) {
				i = (Integer) obj;
			}

			return i;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String checkForHoliday(String holidayDate, String officeName) throws CMSException {
		Map<String, String> map = new HashMap();
		map.put("holidayDate", holidayDate);
		map.put("officeName", officeName);

		try {
			String holidayName = (String) this.queryForObject("TaskConfig.isHoliday", map);
			return holidayName;
		} catch (ClassCastException var6) {
			throw new CMSException(this.logger, var6);
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public boolean checkHolidayWeakendForCaseOffice(String holidayDate, String officeId) throws CMSException {
		boolean isHoliday = false;
		Map<String, String> map = new HashMap();
		map.put("holidayDate", holidayDate);
		map.put("officeName", officeId);

		try {
			Object obj = this.queryForObject("TaskConfig.isHolidayForOfficeId", map);
			if (obj != null && (Integer) obj > 0) {
				isHoliday = true;
			}

			return isHoliday;
		} catch (ClassCastException var6) {
			throw new CMSException(this.logger, var6);
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public String checkHolidayWeakendCDD(String holidayDate) throws CMSException {
		try {
			String holidayOfficeList = (String) this.queryForObject("TaskConfig.isHolidayForClientDueDate",
					holidayDate);
			return holidayOfficeList;
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getOfficeForClient(String clientCode) throws CMSException {
		String officeName = "";

		try {
			officeName = (String) this.queryForObject("TaskConfig.getCountryForClient", clientCode);
			return officeName;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<UserMasterVO> getAnalystForManager(String managerId) throws CMSException {
		new ArrayList();

		try {
			List<UserMasterVO> analystForManager = this.queryForList("TeamAssignment.getAnalystForManager", managerId);
			this.logger.debug(" list size in dao " + analystForManager.size());
			return analystForManager;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<CountryDatabaseVO> getCountryDBForCountry(Map parameter) throws CMSException {
		new ArrayList();

		try {
			List<CountryDatabaseVO> countryDBList = this.queryForList("TaskConfig.getCountryDatabaseForCountry",
					parameter);
			this.logger.debug(" list size in dao " + countryDBList.size());
			return countryDBList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getCountryDBForCountyCount(String countryId) throws CMSException {
		boolean var2 = false;

		try {
			int count = (Integer) this.queryForObject("TaskConfig.getCountryDatabaseForCountryCount", countryId);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<DocMapVO> getDocuments(String docIds) throws CMSException {
		new ArrayList();

		try {
			List<DocMapVO> list = this.queryForList("TaskConfig.getDocs", docIds);
			this.logger.debug(" list size in dao " + list.size());
			return list;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateDocuments(List<String> listOfDocNames, String docids, String teamName, long pid)
			throws CMSException {
		int i = false;
		Map<String, Object> mapToUpdateDocs = new HashMap();
		mapToUpdateDocs.put("teamname", teamName);
		mapToUpdateDocs.put("docid", docids);
		mapToUpdateDocs.put("pid", pid);

		int i;
		try {
			i = this.update("TaskConfig.insertDocs", mapToUpdateDocs);
			this.logger.debug(" updation value is " + i);
		} catch (DataAccessException var17) {
			throw new CMSException(this.logger, var17);
		} catch (Exception var18) {
			throw new CMSException(this.logger, var18);
		}

		try {
			String[] docid = docids.split(",");
			int j = 0;
			int k = false;
			Iterator iterator = listOfDocNames.iterator();

			while (iterator.hasNext()) {
				String docName = (String) iterator.next();
				String doc = docid[j++];
				mapToUpdateDocs = new HashMap();
				mapToUpdateDocs.put("docName", docName);
				mapToUpdateDocs.put("docId", doc);
				mapToUpdateDocs.put("pid", pid);
				this.logger.debug("checking version for document " + docName + " doc id is " + doc);
				if ((Integer) this.queryForObject("TaskConfig.getMaxCount", mapToUpdateDocs) > 1) {
					int g = (Integer) this.queryForObject("TaskConfig.getMaxVersion", mapToUpdateDocs);
					++g;
					this.logger.debug("version is " + g);
					mapToUpdateDocs.put("maxVersion", g);
					mapToUpdateDocs.put("docId", doc.replaceAll("'", ""));
					int k = this.update("TaskConfig.versionControl", mapToUpdateDocs);
					this.logger.debug("Rows updated for version is " + k);
				}
			}

			return i;
		} catch (DataAccessException var15) {
			throw new CMSException(this.logger, var15);
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}
	}

	public int resetStatus(String docIds, String docId) throws CMSException {
		boolean var3 = false;

		try {
			int i = this.update("TaskConfig.resetStatus", docIds);
			this.logger.debug(" updation value is " + i);
			i = this.update("TaskConfig.updateStatus", docId);
			this.logger.debug(" updation value is " + i);
			return i;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int resetStatusForJuno(String docIds, String docId) throws CMSException {
		boolean var3 = false;

		try {
			int i = this.update("TaskConfig.resetStatus", docIds);
			this.logger.debug(" updation value is " + i);
			i = this.update("TaskConfig.updateStatusForJuno", docId);
			this.logger.debug(" updation value is " + i);
			return i;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int markJuno(String docId) throws CMSException {
		boolean var2 = false;

		try {
			int i = this.update("TaskConfig.MarkJuno", docId);
			this.logger.debug(" updation value is " + i);
			return i;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int deleteDocumentFromCustom(String docIds) throws CMSException {
		boolean var2 = false;

		try {
			int i = this.delete("TaskConfig.deleteDocuments", docIds);
			this.logger.debug("list is " + docIds + " value is i is " + i);
			return i;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public boolean hasPermissionForSensitiveFile(String userId) throws CMSException {
		boolean var2 = false;

		int i;
		try {
			i = (Integer) this.queryForObject("TaskConfig.sensitiveFolderPermission", userId);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("total number of rows " + i);
		return this.hasPermission(i);
	}

	public boolean hasDeletePermission(String userId) throws CMSException {
		boolean var2 = false;

		int i;
		try {
			i = (Integer) this.queryForObject("TaskConfig.hasDeletePermission", userId);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		return this.hasPermission(i);
	}

	public boolean hasUploadPermission(String userId) throws CMSException {
		boolean var2 = false;

		int i;
		try {
			i = (Integer) this.queryForObject("TaskConfig.hasUploadPermissionAfterComplete", userId);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		return this.hasPermission(i);
	}

	public int getOfficeId(String userId) throws CMSException {
		int i = 0;

		try {
			Object object = this.queryForObject("TaskConfig.getOfficeId", userId);
			if (object != null) {
				i = (Integer) object;
			}

			return i;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getOfficeCode(String userId) throws CMSException {
		String officeCode = "";

		try {
			officeCode = (String) this.queryForObject("TaskConfig.getOfficeCode", userId);
			return officeCode;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getMarkedCount(String docIds) throws CMSException {
		boolean var2 = false;

		try {
			this.logger.debug("::: doc ids are " + docIds);
			int i = (Integer) this.queryForObject("TaskConfig.getMarkedCount", docIds);
			return i;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateClientDate(String sendDate, String cycle, String crn, String updatedBy, String clientFeedBack)
			throws CMSException {
		int insertedRow = false;
		HashMap<String, String> map = new HashMap();
		map.put("CRN", crn);
		map.put("updatedBy", updatedBy);
		map.put("clientFeedBack", clientFeedBack);
		map.put(cycle, sendDate);
		this.logger.debug("cycle is " + cycle);
		this.logger.debug("crn is " + crn);
		this.logger.debug("updatedBy is " + updatedBy);
		this.logger.debug("clientFeedBack is " + clientFeedBack);

		try {
			int insertedRow = this.update("TaskConfig.updateSendDate", map);
			return insertedRow;
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public List<Map<String, Object>> getTaskForManager(String userId) throws CMSException {
		new ArrayList();

		try {
			List<Map<String, Object>> mapForManager = this.queryForList("TaskConfig.getTaskForManager", userId);
			return mapForManager;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updateRESstauts(String performer, String crn) throws CMSException {
		int updatedRows = false;
		HashMap<String, String> map = new HashMap();
		map.put("crn", crn);
		map.put("updatedBy", performer);
		map.put("performer", performer);
		this.logger.debug("crn is " + crn);
		this.logger.debug("updatedBy is " + performer);

		int updatedRows;
		try {
			updatedRows = this.update("TaskConfig.updateREForUser", map);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("updated rows are " + updatedRows);
		return updatedRows;
	}

	public boolean isFinalAndJunoUploadedFile(long pid) throws CMSException {
		int getCount = false;
		this.logger.debug("checking for pid " + pid);

		int getCount;
		try {
			getCount = (Integer) this.queryForObject("TaskConfig.isMarkedAndJUNOUploadedFile", pid);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("getCOunt value is " + getCount);
		return this.hasPermission(getCount);
	}

	public int updateDateForMIS(long teamId, String processCycle, String updatedBy) throws CMSException {
		int getCount = false;
		this.logger.debug("team id is  " + teamId + " updating for ProcessCycle " + processCycle);
		HashMap<String, Object> updationMap = new HashMap();
		updationMap.put("teamId", teamId);
		updationMap.put(processCycle, processCycle);
		updationMap.put("updatedBy", updatedBy);

		int getCount;
		try {
			getCount = this.update("TaskConfig.updateDateForMIS", updationMap);
		} catch (DataAccessException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}

		this.logger.debug("getCOunt value is " + getCount);
		return getCount;
	}

	public String getFileForISIS(long pid) throws CMSException {
		String fileName = "";

		try {
			fileName = (String) this.queryForObject("TaskConfig.getFileForISIS", pid);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("file name " + fileName + " for pid " + pid);
		return fileName;
	}

	public int getISISStatus(String crn) throws CMSException {
		boolean var2 = false;

		int status;
		try {
			status = (Integer) this.queryForObject("TaskConfig.isISISCase", crn);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("status is " + status + " for crn " + crn);
		return status;
	}

	public int getSubjectListCount(Map<String, String> mapForParams) throws CMSException {
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("TaskConfig.getSubListCountForUser", mapForParams);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("count is " + count);
		return count;
	}

	public int getDocumentVersion(String docId) throws CMSException {
		boolean var2 = false;

		int version;
		try {
			version = (Integer) this.queryForObject("TaskConfig.getDocVersion", docId);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("version is " + version);
		return version;
	}

	public int getCBDValue(String crn) throws CMSException {
		boolean var2 = false;

		int cbdVal;
		try {
			cbdVal = (Integer) this.queryForObject("TaskConfig.getCBDValue", crn);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("cbdVal is " + cbdVal);
		return cbdVal;
	}

	public boolean hasPermission(int i) {
		boolean isPermissionGranted = false;
		isPermissionGranted = i > 0;
		return isPermissionGranted;
	}

	public List<CustomTaskVO> getSavvionTasks(Map<String, String> mapOfParams) throws CMSException {
		List listOfTasks = null;

		try {
			this.logger.debug("limit is " + (String) mapOfParams.get("limit"));
			this.logger.debug("limit is " + (String) mapOfParams.get("start"));
			listOfTasks = this.queryForList("TaskConfig.getSavvionTasks", mapOfParams);
			return listOfTasks;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public long getSavvionTasksCount(Map<String, String> mapOfParams) throws CMSException {
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("TaskConfig.getSavvionTasksCount", mapOfParams);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("count is " + count);
		return (long) count;
	}

	public List<TeamDetails> getMyIncomingTasks(Map<String, String> mapOfParams) throws CMSException {
		List listOfTasks = null;

		try {
			this.logger.debug("limit is " + (String) mapOfParams.get("limit"));
			this.logger.debug("start is " + (String) mapOfParams.get("start"));
			listOfTasks = this.queryForList("TaskConfig.getMyIncomingTasks", mapOfParams);
			System.out.println("TaskList size ::" + listOfTasks.size());
			return listOfTasks;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public long getMyIncomingTasksCount(Map<String, String> mapOfParams) throws CMSException {
		boolean var2 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("TaskConfig.getMyIncomingTasksCount", mapOfParams);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}

		this.logger.debug("count is " + count);
		return (long) count;
	}

	public List<MyTaskPageVO> getAllTeamAssignmentTasks(Map<String, String> mapOfParams) throws CMSException {
		List listOfTeamTasks = null;

		try {
			this.logger.debug("mapOfParams of getAllTeamAssignmentTasks::" + mapOfParams);
			listOfTeamTasks = this.queryForList("TaskConfig.getTeamAssignmentTasks", mapOfParams);
			return listOfTeamTasks;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String checkHolidayForAnalyst(String holidayDate, String analyst) throws CMSException {
		String holidayName = "";
		Map<String, String> map = new HashMap();
		map.put("holidayDate", holidayDate);
		map.put("analyst", analyst);

		try {
			holidayName = (String) this.queryForObject("TaskConfig.isHolidayForAnalyst", map);
			return holidayName;
		} catch (ClassCastException var6) {
			throw new CMSException(this.logger, var6);
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public int isHeaderRecorded(String userId) throws CMSException {
		Map<String, String> mapForParams = new HashMap();
		mapForParams.put("userId", userId);
		boolean var3 = false;

		int count;
		try {
			count = (Integer) this.queryForObject("TaskConfig.getHeaderCount", mapForParams);
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug("isHeaderRecorded count is " + count);
		return count;
	}

	public int updateHeaderInfo(String userId, String headerInfo) throws CMSException {
		Map<String, String> mapForParams = new HashMap();
		mapForParams.put("userId", userId);
		mapForParams.put("headerInfo", headerInfo);
		boolean var4 = false;

		int getCount;
		try {
			getCount = this.update("TaskConfig.updateHeader", mapForParams);
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug(" Exit updateHeaderInfo" + getCount);
		return getCount;
	}

	public int insertHeaderInfo(String userId, String headerInfo) throws CMSException {
		Map<String, String> mapForParams = new HashMap();
		mapForParams.put("userId", userId);
		mapForParams.put("headerInfo", headerInfo);
		mapForParams.put("headerId", "");
		boolean var4 = false;

		byte i;
		try {
			this.insert("TaskConfig.insertHeader", mapForParams);
			i = 1;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug(" Exit insertHeaderInfo" + i);
		return i;
	}

	public List<SubReportTypeVO> getSubReportTypesList() throws CMSException {
		List list = null;

		try {
			list = this.queryForList("TaskConfig.getSubReportTypeList");
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}

		this.logger.debug("size of the list is " + list.size());
		return list;
	}

	public String getAnalysts(String crn, String cycle, String teamtypeList) throws CMSException {
		Map<String, String> mapForParams = new HashMap();
		mapForParams.put("crn", crn);
		mapForParams.put("cycle", cycle);
		mapForParams.put("teamTypeList", teamtypeList);
		String analysts = "";

		try {
			analysts = (String) this.queryForObject("TaskConfig.getAnalyst", mapForParams);
			return analysts;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List<SubjectListForUserVO> getSubjectLevelRisk(Map<String, String> mapForParams) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("TaskConfig.getSubLevelRiskForUser", mapForParams);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("size of the list is " + list.size());
		return list;
	}

	public List<RiskCategoryMasterVO> getCaseLevelRisk(Map<String, String> mapForParams) throws CMSException {
		List list = null;

		try {
			list = this.queryForList("TaskConfig.getCaseLevelRiskForUser", mapForParams);
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("size of the list is " + list.size());
		return list;
	}

	public int updateOptionalRiskData(RiskProfileVO rVO) throws CMSException {
		boolean var2 = false;

		byte i;
		try {
			long profileId;
			HashMap map;
			if (rVO.getCountryId() != 0L) {
				profileId = (Long) this.queryForObject("TaskConfig.fetchProfileIdForRiskCode", rVO);
				map = new HashMap();
				map.put("riskProfileId", profileId);
				map.put("countryId", rVO.getCountryId());
				map.put("subjectId", rVO.getSubjectId());
				this.delete("TaskConfig.deleteHBDCountry", map);
			} else {
				if (rVO.getHasCountryBreakDown() == 1L) {
					profileId = (Long) this.queryForObject("TaskConfig.fetchProfileIdForRiskCode", rVO);
					map = new HashMap();
					map.put("riskProfileId", profileId);
					this.delete("TaskConfig.deleteHBDMapping", map);
				}

				this.delete("TaskConfig.updateOptionalRisk", rVO);
			}

			i = 1;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		}

		this.logger.debug(" Exit updateOptionalRiskData" + i);
		return i;
	}

	public int updateOptionalRiskDataHistory(RiskHistory rh) throws CMSException {
		int i = 0;
		this.logger.debug("updateOptionalRiskDataHistory");

		int i;
		try {
			this.insert("TaskConfig.insertRiskHistoryForDelete", rh);
			i = i + 1;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}

		this.logger.debug("exit updateOptionalRiskDataHistory");
		return i;
	}

	public int insertOptionalRiskData(List<RiskProfileVO> rVOList, List<CountryHBDVO> countryHBDResponseList)
			throws CMSException {
		int i = 0;

		try {
			this.getSqlMapClient().startTransaction();
			Iterator var5 = rVOList.iterator();

			label129 : while (true) {
				while (true) {
					while (var5.hasNext()) {
						RiskProfileVO rVO = (RiskProfileVO) var5.next();
						if (rVO.getAttributeId().equals("")) {
							CountryHBDVO cntryHBDVO;
							Iterator var7;
							if (rVO.getSubjectId() == 0L) {
								this.insert("TaskConfig.insertHBDOptionalRiskWithoutSubject", rVO);
								++i;

								for (var7 = countryHBDResponseList.iterator(); var7.hasNext(); ++i) {
									cntryHBDVO = (CountryHBDVO) var7.next();
									this.insert("TaskConfig.insertHBDOptionalRiskCountriesWithoutSubject", cntryHBDVO);
								}
							} else {
								this.insert("TaskConfig.insertHBDOptionalRiskWithSubject", rVO);
								++i;

								for (var7 = countryHBDResponseList.iterator(); var7.hasNext(); ++i) {
									cntryHBDVO = (CountryHBDVO) var7.next();
									this.insert("TaskConfig.insertHBDOptionalRiskCountriesWithSubject", cntryHBDVO);
								}
							}
						} else if (rVO.getSubjectId() == 0L) {
							this.insert("TaskConfig.insertOptionalRiskWithoutSubject", rVO);
							++i;
						} else {
							this.insert("TaskConfig.insertOptionalRiskWithSubject", rVO);
							++i;
						}
					}

					this.getSqlMapClient().commitTransaction();
					break label129;
				}
			}
		} catch (DataAccessException var16) {
			throw new CMSException(this.logger, var16);
		} catch (Exception var17) {
			throw new CMSException(this.logger, var17);
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException var15) {
				throw new CMSException(this.logger, var15);
			}
		}

		this.logger.debug(" Exit insertOptionalRiskData" + i);
		return i;
	}

	public int insertHBDCountryRiskData(List<CountryHBDVO> countryHBDResponseList) throws CMSException {
		int i = 0;

		try {
			this.getSqlMapClient().startTransaction();
			Iterator var4 = countryHBDResponseList.iterator();

			while (true) {
				if (!var4.hasNext()) {
					this.getSqlMapClient().commitTransaction();
					break;
				}

				CountryHBDVO cntryHBDVO = (CountryHBDVO) var4.next();
				if (cntryHBDVO.getSubjectId() == 0L) {
					this.insert("TaskConfig.insertHBDOptionalRiskCountriesWithoutSubject", cntryHBDVO);
					++i;
				} else {
					this.insert("TaskConfig.insertHBDOptionalRiskCountriesWithSubject", cntryHBDVO);
					++i;
				}
			}
		} catch (DataAccessException var13) {
			throw new CMSException(this.logger, var13);
		} catch (Exception var14) {
			throw new CMSException(this.logger, var14);
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException var12) {
				throw new CMSException(this.logger, var12);
			}
		}

		this.logger.debug(" Exit insertOptionalRiskData" + i);
		return i;
	}

	public long getNextProfileId() throws CMSException {
		long nextId = 0L;

		try {
			nextId = (Long) this.queryForObject("TaskConfig.getNextProfileId");
			return nextId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<CountryHBDVO> getCountries(String crn, String subID) throws CMSException {
		new ArrayList();
		Map<String, String> map = new HashMap();
		map.put("crn", crn);
		map.put("subID", subID);

		try {
			List<CountryHBDVO> countryList = this.queryForList("TaskConfig.getCountriesForRiskCode", map);
			return countryList;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List<CategoryDetailsVO> getCategoryDetails() throws CMSException {
		new ArrayList();

		try {
			List<CategoryDetailsVO> categoryList = this.queryForList("TaskConfig.getCategoryDetails");
			return categoryList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<RiskAttributeVO> getRiskAttributeDefaultValues(List<Long> attributeIdsList) throws CMSException {
		List defaultValuesList = null;

		try {
			defaultValuesList = this.queryForList("TaskConfig.getAttributeDefaultValues", attributeIdsList);
			return defaultValuesList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public String getInitialsForSubReportByID(String subReportTypeID) throws CMSException {
		String initials = "";

		try {
			initials = (String) this.queryForObject("TaskConfig.getSubReportInitialsByID", subReportTypeID);
			return initials;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		}
	}
}