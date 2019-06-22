package com.worldcheck.atlas.utils;

public interface Constants {
	String USER_DETAILS = "userDetailsBean";
	String USER_ID = "user_Id";
	String USERID = "userId";
	String PERMISSION_MAP = "permissionMap";
	String ROLE_LIST = "roleList";
	String EMPTY = "";
	String COMMA_SEPERATOR = ",";
	String USER_BEAN = "userBean";
	String DATE_FORMAT_SOURCE = "dd MMM, yyyy";
	String DATE_FORMAT_TARGET = "dd-MMM-yyyy";
	String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	String TIMESTAMP_FORMAT = "DD-mm-y H:m:s";
	String WORKITEM = "workItem";
	String BLANK = "";
	String CRN = "crn";
	String CRN_VAL = "crnVal";
	String SUBJECT_ID = "subjectID";
	String SUBJECT_NAME = "subjectName";
	String COUNTRY_NAME = "countryName";
	String ENTITY_TYPE = "entityType";
	String ATERT_INVALID_SUBJECTNAME_COUNTRY_COMBINATION = "Invalid Subject Name,Country Name And Entity Type Combination";
	String INDUSTRY_CODE = "industryCode";
	String RISK_ID = "riskID";
	String SELECTED_RISK_STATUS = "1";
	String CURRENT_USER = "currentUser";
	String SUBJECT_IND_RISK_JSP = "subject_Risks";
	String JSON_SUBJECT_ID = "jsonSubjectID";
	String JSON_INDUSTRY_ID = "jsonIndustryID";
	String JSON_RISK_STRING = "riskString";
	String JSON_VIEW = "jsonView";
	String CASE_DETAILS = "caseDetails";
	String ASSOCIATE_CASES = "assCaseList";
	String ASSOCIATE_SUBJECT_ID = "associateSubjectID";
	String SUBJECT_ID_STRING = "subIDString";
	String SUBJECT_NAME_STRING = "subNameString";
	String SUCCESS_ALERT = "success";
	String ALERT = "alert";
	String TOKEN_STRING = ":true";
	String PRIMARY_SUBJECT_ALERT = " is a Primary Subject and can't be deleted. <br>";
	String SUBJECT_TEAM_ALERT1 = " cannot be deleted as it will remove ";
	String SUBJECT_TEAM_ALERT2 = " teams from this case. <br>";
	String INDUSTRY_MASTER = "INDUSTRY_MASTER";
	String COUNTRY_MASTER = "COUNTRY_MASTER";
	String CURRENCY_MASTER = "CURRENCY_MASTER";
	String RISK_MASTER = "RISK_MASTER";
	String VENDOR_MASTER = "VENDOR_MASTER";
	String DEFAULT_PAGINATION_MIN = "0";
	String DEFAULT_PAGINATION_LIMIT = "10";
	String TOTAL_RECORD_COUNT = "total";
	String PAGINATION_MIN = "start";
	String PAGINATION_MAX = "limit";
	String PAGINATION_SORTINGCOLUMN = "sort";
	String PAGINATION_SORT_TYPE = "dir";
	String IS_BIZSOLO = "isBizsolo";
	String ACTIVE_TAB = "activeTab";
	String DEFAULT_ACTIVE_TAB_RISK_INDUSTRY = "0";
	String ACTIVE_TAB_RISK = "1";
	String ACTIVE_TAB_INDUSTRY = "0";
	String FINANCE_RAW_DATA_REPORT = "financeRawDataReport";
	String REVIEWER_RAW_DATA_REPORT = "reviewerRawDataReport";
	String INVOICE_CT_EXCEL_REPORT = "invoiceCTExcelReport";
	String OFFICE_SUMMARY_REPORT = "OfficeSummaryReport";
	String COMMA = ",";
	String selected = "1";
	String ENTITY = "entity";
	String SUBJECTENTITY = "subjectEntity";
	String NONESTRING = "None";
	String DEFAULT_ENTITY_TYPE_ID = "2";
	String OTHER_ENTITY_TYPE_ID = "1";
	String COMPANY_ENTITY_ID = "2";
	String INDIVIDUAL_ENTITY_ID = "1";
	String REDIRECT_STRING = "redirectString";
	String SUBJECT_VO = "subDetVO";
	String ASSOCIATE_CRN_STRING = "associateCRNString";
	String COUNTRY_CODE_FOR_MODIFY_RE = "China,Mongolia";
	String RE_NAME_REMOVED_FOR_COUNTRY = "Bankruptcy/Limit Credit Check";
	int PRIMARY_TEAM_ID = 1;
	int SUPPORTING_TEAM_INTERNAL_PROCESS_TEAM_ID = 4;
	int SUPPORTING_TEAM_BI_PROCESS_TEAM_ID = 2;
	int SUPPORTING_TEAM_VENDOR_PROCESS_TEAM_ID = 3;
	String TEAM_ID_STRING_FOR_ANALYST = "1,2,4";
	String TEAM_ID_STRING_FOR_REVIEWERS = "1,4";
	String TEAM_ID_STRING_FOR_MANAGER = "3";
	String ADD_SUBJECT_NOTIFICATION = "New Subject(s) added to the case(0). New subject(s) :";
	String DELETE_SUBJECT_NOTIFICATION = "Subject(s) deleted from the case(0). Deleted Subject(s) :";
	String UPDATE_SUBJECT_NON_RE = "Please note, Subject information has been updated for the case(0). Following are the details updated:(1)";
	String UPDATE_SUBJECT_RE = "Subject Research Element has been updated for the case(0). Please check it out. Updated Subject Name :";
	String TEAM_ID = "team_ID";
	String TOP_TEN_COUNTRY_SEPERATOR = "=============";
	String TOP_TEN_COUNTRY_SEPERATOR_VALUE = "--";
	String REPORT_NAME = "reportName";
	String CASEDUE_REPORT = "casedue";
	String JLP_REPORT = "JLPReport";
	String PRODUCTIVITY_REVENUE_SUMMARY_REPORT = "productivityrevenuesummary";
	String CHART_TAG_TO_REPLACE = "<chart bgColor='FFFFFF' legendPosition='BOTTOM' labelDisplay='Stagger'>";
	String CHART_TAG_FOR_JLP_REPORT = "<chart  showValues='0' caption='XXXX Month-To-Date JLP Data' xAxisName='Offices' yAxisName='Points/Analyst' bgColor='FFFFFF' legendPosition='BOTTOM' labelDisplay='Stagger'>";
	String UTILIZATION_PRODUCTIVITY_POINTS_CASES_REPORT = "utilizationproductivitypointscases";
	String CASES_RECEIVED_SUMMARY_REPORT = "casesreceivedsummary";
	String UTILIZATION_BY_REVENUE_REPORT = "utilizationbyrevenue";
	String VENDOR_DATA_SUMMARY_REPORT = "vendordatasummary";
	String VENDOR_RAW_DATA_REPORT = "vendorrawdata";
	String TIME_TRACKER_DATA_SUMMARY_REPORT = "timetrackerdatasummary";
	String TIME_TRACKER_RAW_DATA_REPORT = "timetrackerrawdata";
	String CURRENT_ANALYST_LOADING_REPORT = "currentanalystloading";
	String CASE_STATUS_MASTER = "CASE_STATUS_MASTER";
	String START_DATE = "startDate";
	String END_DATE = "endDate";
	String CASES = "cases";
	String REVENUE = "revenue";
	String COMBINED = "combined";
	String CASES_PIE_XML_STRING = "casesPieXMLString";
	String REVENUE_PIE_XML_STRING = "revenuePieXMLString";
	String SUB_REPORT_TYPE = "subReportType";
	String REVENUE_REPORT_TYPE = "revenueReportType";
	String REVENUE_SUB_REPORT_TYPE = "revenueSubReportType";
	String DASH_SEPERATOR = "-";
	String ROLE_R1 = "R1";
	String USER_NAME = "userName";
	String AUTHONE_CONFSTATE_FOUR = "4";
	String AUTHTWO_CONFSTATE_TWO = "2";
	String JSON_CONTENT_TYPE = "text/json;charset=UTF-8";
	String PROP_FILE_NAME = "atlas.properties";
	String WEB_SERVICE_PROP_FILE_NAME = "atlas";
	String OTHER_RISK_FACTORS = "Other Risk Factors";
	String ATLAS_PRAMATI_PROVIDER_URL = "atlas.pramati.provider.url";
	String ATLAS_PRAMATI_FACTORY_INITIAL = "atlas.pramati.factory.initial";
	String ATLAS_PRAMATI_PROVIDER_JNDINAME = "atlas.pramati.provider.jndiname";
	String ATLAS_PRAMATI_CLIENT_CONTEXT_FACTORY = "com.pramati.naming.client.PramatiClientContextFactory";
	String ATLAS_NAMING_PROVIDER_URL = "java.naming.provider.url";
	String ATLAS_PRAMATIIDS_JNDI = "atlas.pramatids.jndi";
	String ATLAS_NAMING_SECURITY_CREDENTIALS = "java.naming.security.credentials";
	String ATLAS_NAMING_SECURITY_PRINCIPAL = "java.naming.security.principal";
	String ATLAS_APP_JNDI = "atlas.app.jndiname";
	String ATLAS_QUEUE_CONNECTION_FACTORY = "QueueConnectionFactory";
	String ATLAS_JMS_PROCESSMANAGER = "jms.processmanager";
	String ATLAS_LOGGING_MODE = "logging.mode";
	String ATLAS_DOCUMENT_TEMPPATH = "tempFilePath";
	String ATLAS_REPORT_OFFUTI_REPORTTYPES = "report.offuti.reporttypes";
	String ATLAS_NONCRN_PAYMENTCYCLE = "atlas.noncrn.paymentcyle";
	String ATLAS_RECORDS_PAGESIZE = "atlas.records.pagesize";
	String ATLAS_NOTIFICATION_PROCESS_NAME = "atlas.notification.process.name";
	String ATLAS_APP_ALERT_JNDINAME = "atlas.app.alert.jndiname";
	String ATLAS_SYSTEM_USER = "atlas.system.user";
	String ATLAS_INTERIMCYCLE_ERRORCODE = "atlas.interimcycle.errorcode";
	String BLANK_CELL_MESSAGE = "Blank Cell Not Allowed";
	String COMMISSIONING_DATE = "Commissioning Date";
	String DATE_FORMAT_MMDDYYYY = "MM/dd/yyyy";
	String FALSE = "false";
	String TRUE = "true";
	String SQUARE_BRACKET_START = "[";
	String INVALID = "_invalid";
	String SQUARE_BRACKET_END = "]";
	String COUNTRY = "Country";
	String VENDOR_NAME = "Vendor Name";
	String CURRENCY = "Currency";
	String AMOUNT = "Amount";
	String NAME_SEARCHED = "Name Searched";
	String JANUARY = "1";
	String FEBRUARY = "2";
	String MARCH = "3";
	String APRIL = "4";
	String MAY = "5";
	String JUNE = "6";
	String JULY = "7";
	String AUGUST = "8";
	String SEPTEMBER = "9";
	String OCTOBER = "10";
	String NOVEMBER = "11";
	String DECEMBER = "12";
	String FIRST_QUARTER = "1,2,3";
	String SECOND_QUARTER = "4,5,6";
	String THIRD_QUARTER = "7,8,9";
	String FOURTH_QUARTER = "10,11,12";
	String ERROR_MESSAGE = "errorMessage";
	String ATLAS_ERROR = "altas_error";
	String CLIENT_MASTER = "CLIENT_MASTER";
	String DATE_FORMAT_FULL_TARGET = "yyyy-MM-dd'T'HH:mm:ss";
	String DATE_FORMAT_FORM = "dd-MM-yy hh:mm:ss";
	String CREATE_CASE_NOTIFICATION = "You are assigned the Case Manager for this case";
	String ISIS_CREATE_CASE_NOTIFICATION = "New case created in EDDO";
	String ISIS_FILE_UPLOAD_NOTIFICATION = "File(s) uploaded by client from EDDO. Please check the Attachment section";
	String BY_NAME = "byName";
	String BY_CODE = "byCode";
	String ACTION = "action";
	String CLIENT_NAME = "clientName";
	String REPORT_TYPE = "reportType";
	String REPORT_TYPE_ID = "reportTypeId";
	String SUB_REPORT_TYPE_ID = "subReportTypeId";
	String PRIMARY_CODE = "primaryCode";
	String RISK_LEVEL = "riskLevel";
	String DOUBLECOLONSEPARATOR = "::";
	String SINGLECOLONSEPARATOR = ":";
	String HASHKEYSEPARATOR = "#";
	String DOUBLEAMPERSANDSEPARATOR = "&&";
	String ATSYMBOLSEPARATOR = "@";
	String INTERIM_DATE = "interimDate";
	String DAYFORMAT = "EEEE";
	String SUNDAY = "Sunday";
	String SATURDAY = "Saturday";
	String USER_DATA_REPORT = "User Information";
	String EXCEL_PARAMS = "excelParams";
	String ANNUALY = "Annually";
	String QUARTERLY = "Quarterly";
	String MONTHLY = "Monthly";
	String END_BY = "End By";
	String END_AFTER_DAYS = "End After";
	String SelectToken = "Select...";
	String TOKEN = "Param1";
	String TOKEN2 = "Param2";
	String MAPSTRING = "map";
	String UPDATE_TOKEN = "updateToken";
	String ADD_TOKEN = "addToken";
	String UPDATE_MASTER = "updateMaster";
	String UPDATE_TT_TOKEN = "updateTTToken";
	String DELETE_TT_TOKEN = "deleteTTToken";
	String TRACKER_ID = "trackerId";
	String OFFICE_ASSIGNMENT = "Office";
	String TEAM_ASSIGNMENT = "Team";
	String ADD_SUBJECT = "AddSubject";
	String DELETE_SUBJECT = "DeleteSubject";
	String UPDATE_SUBJECT = "UpdateSubject";
	String UPDATE_OFFICE_FOR_SUBJECT = "UpdateOfficeForSubject";
	String INVOICING = "Invoicing";
	String PRODUCTIVITY_POINT_BY_OFFICE = "productivityPointByOffice";
	String PRODUCTIVITY_POINT_ANALYST_BY_OFFICE = "productivityPointAnalystByOffice";
	String COMPLETED_CASE_BY_OFFICE = "completedCaseByOffice";
	String COMPLETED_CASE_ANALYST_BY_OFFICE = "completedCaseAnalystByOffice";
	String SCORESHEET_MIS_REPORT = "analystScoringPerformanceScoreSheetMIS";
	String REVENUE_OFFICE_PERFORMANCE_REPORT = "RevenueByOfficePerformance";
	String REVENUE_AND_ANALYST__OFFICE_PERFORMANCE_REPORT = "revenueAndAnalystByOfficePerformance";
	String REPORT_TYPE_OFFICE_SUMMARY_REPORT = "reportTypeAndOfficeSummary";
	String INVOICE_TAT_REPORT = "invoiceTATReport";
	String OVERDUE_REPORT = "overdueReport";
	String CASEDUETODAY_REPORT = "caseDueTodayReport";
	String ALLONHOLDCASE_REPORT = "allOnHoldReport";
	String MY_PERFORMANCE_REPORT = "myPerformanceReport";
	String OLD_CASE_DETAILS = "oldCaseDetails";
	String NEW_CASE_DETAILS = "newCaseDetails";
	String RE_MASTER_INDIVIDUAL = "RE_MASTER_INDIVIDUAL";
	String RE_MASTER_COMPANY = "RE_MASTER_COMPANY";
	String CASE_INFORMATION = "CaseInformation";
	String TEAM_PERFORMANCE_REPORT = "TeamPerformanceReport";
	String DATE_OF_REVIEW = "Date of Review";
	String ANALYST = "Analyst";
	String Reviewer = "Reviewer";
	String COMPLEXITY = "Complexity";
	String REVIEW_TIME = "Review Time";
	String FINAL_INTERIM = "Final/Interim";
	String PRIMARY_TEAM = "Primary Team";
	String SUPPORTING_TEAM1 = "Supporting Team 1";
	String SUPPORTING_TEAM2 = "Supporting Team 2";
	String SUPPORTING_TEAM3 = "Supporting Team 3";
	String SUPPORTING_TEAM4 = "Supporting Team 4";
	String SUPPORTING_TEAM5 = "Supporting Team 5";
	String SUPPORTING_TEAM6 = "Supporting Team 6";
	String REPORT_DUE_DATE = "Report Due Date";
	String REVIEWER1 = "Reviewer 1";
	String REVIEWER2 = "Reviewer 2";
	String REVIEWER3 = "Reviewer 3";
	String GENERAL_COMMENT = "General Comment";
	String SR_ANALYST = "Sr. Analyst";
	String EDITOR = "Editor";
	String DATE_REPORT_FILED = "Date Report Filed";
	String COMPLETE_OS_RCVD_DATE = "Complete OS Rcvd Date";
	String SCORESHEET_NAME = "ScoreSheetName";
	String FINANCE_REVENUE_SUMMARY = "FinanceRevenueSummary";
	String INTERIM1 = "interim1";
	String INTERIM2 = "interim2";
	String BOTH = "both";
	String TEAM_JLP_SUMMARY = "teamJLPSummary";
	String TRACKER_ON = "TrackerOn";
	String TRACKER_BEAN = "trackerBean";
	String SINGLESPACE = " ";
	String ONE = "1";
	String ZERO = "0";
	String SUBJECT = "Subject";
	String PULLBACK = "Pullback";
	String AUTO_OFFICE_ASSIGNMENT = "Auto Office Assignment";
	String STATUS_ON_HOLD = "On Hold";
	String STATUS_NEW_CASE = "New Case";
	String STATUS_IN_PROGRESS = "In Progress";
	String STATUS_CANCELLED = "Cancelled";
	String STATUS_COMPLETED = "Completed";
	String STATUS_CCS = "Completed Client Submission";
	String TIMETRACKERCRN = "timeTrackerCrn";
	String PRIMARY_TEAM_DB_NAME = "Primary";
	String ISISCANCELCASESTATUS = "CNL";
	String _NULL = "null";
	String OTHERS = "Others";
	String EXCEL_POPUP = "excelDownloadPopup";
	String MIS_EXCEL_POPUP = "misExcelDownloadPopup";
	String EXCEL_GENERATION = "excelGeneration";
	String ZIP_GENERATION = "excelDownloadPopupZip";
	String FILE_BYTES = "fileBytes";
	String FILE_NAME = "fileName";
	String CASE_STATUS_ERROR_PAGE = "redirect:/bpmportal/atlas/caseStatusErrorPage.jsp";
	String SECTION_CASE_STATUS_ERROR_PAGE = "redirect:/bpmportal/myhome/sectionCaseStatusError.jsp";
	String TASK_STATUS_ERROR_PAGE = "redirect:/bpmportal/atlas/taskStatusErrorPage.jsp";
	String BACKUP_LOGIN_LEVEL = "loginLevel";
	String BACKUP_PERFORMED_BY = "performedBy";
	String ROLE_R3 = "R3";
	String PRIMARY = "Primary";
	String SUPPORTING_INTERNAL = "Supporting - Internal";
	String SUPPORTING_BI = "Supporting - BI";
	String SUPPORTING_VENDOR = "Supporting - Vendor";
	String ID = "ID";
	String ST_PRE_FIX = "ST-";
	String VT_PRE_FIX = "VT-";
	String FULL_NAME = "fullName";
	String L2MANAGER = "l2manager";
	String UPDATEBY = "<updateBy>";
	String REJECTION_REASON = "<rejectionReason>";
	String PT_ANALYST = "<PTanalystname>";
	String ST_ANALYST = "<STanalystname>";
	String BI_ANALYST = "<BIanalystname>";
	String VT_ANALYST = "<VTanalystname>";
	String ALL = "all";
	String IS_ZIP = "isZip";
	int ONE_LAKH_COUNT = 100000;
	int CSV_CELL_CHARACTER_LIMIT = 32000;
	int XL_CHAR_LIMIT = 5000;
	String PROCESS_INSTANCE_LIST = "processInstanceList";
	String OFFICE_LIST = "officeList";
	String LEGACY_FINAL_FOLDER_NAME = "FINAL-REPORT";
	String COLOR = "green";
	String COLOUR = "colour";
	String CURRENT_CASE_STATUS = "currentCaseStatus";
	String Task_NAME = "taskName";
	String HEADER_INFO = "headerInfo";
	String HEADER_ID = "headerId";
	String IS_WATCHLISTED = "iswatchListed";
	String CYCLE = "cycle";
	String TEAM_TYPE_LIST = "teamTypeList";
	String CRN_OFFICE = "crnOffice";
	String AUTO_CASE_DETAILS = "autoCaseDetails";
	String RE_LIST_FOR_REPLICATION = "reListForReplication";
	String SUBORDINATE_ALL_TASK = "subordinateAllTask";
	String SUBORDINATE_ALL_TASK_PARAM = "'''subordinateAllTask'''";
	String[] CASEDATASERIESENAME = new String[]{"Number of Cases"};
	String[] REVENUEDATASETSERIES = new String[]{"Revenue(USD)"};
	String[] CASE_COLORS = new String[]{"0066B3"};
	String[] REVENUE_COLORS = new String[]{"00B200"};
	String[] REPORT_COLORS = new String[]{"0066B3", "FFFF00", "00DE48", "EA0000", "0DF4EA", "970099", "FFA258",
			"7A7A7A", "35638B", "FFF491", "458C70", "F98072"};
	String[] SUB_REPORT_COLORS = new String[]{"0066B3", "FFFF00", "00DE48", "EA0000", "0DF4EA", "970099", "FFA258",
			"7A7A7A", "35638B", "FFF491", "458C70", "F98072", "00B200", "A600CF", "E68A00", "A14081", "6BB26B",
			"FF6666", "009900", "FFC000", "4BACC6", "F79646", "4BACC6", "C00000"};
	String CASES_DATA_XML_STRING = "casesDataXMLString";
	String REVENUE_DATA_XML_STRING = "revenueDataXMLString";
	String isDataPresent = "isDataPresent";
	String CASETABLE = "caseTable";
	String REVENUETABLE = "revenueTable";
	String TABLEDATA = "tableData";
	String FUSIONXMLSTRING = "fusionXMLString";
	String CLIENT_CASE_LIST = "clientCaseList";
	String CLIENT_REVENUE_LIST = "clientRevenueList";
	String STATISTICS_TABLE = "statisticsTable";
	String TAB_NAME_BY_MONTH = "dataByMonth";
	String TAB_NAME_BY_STATUS = "dataByCaseStatus";
	String TAB_NAME_BY_REPORT_TYPE = "dataByReportType";
	String TAB_NAME_BY_CLIENTS = "dataByClients";
	String TAB_NAME_BY_PRIMARY_SUB_COUNTRY = "dataByPrimarySubjectCountry";
	String TAB_NAME_BY_DELIVERY_PERCENTAGE = "dataOnTimeDeliveryPercentage";
	String TAB_NAME_BY_TAT_HISTOGRAM = "dataTatHistogram";
	int BY_MONTH = 1;
	int BY_STATUS = 2;
	int BY_REPORT = 3;
	int BY_CLIENT = 4;
	int BY_COUNTRY = 5;
	String CLIENTCODE = "clientCode";
	String IS_ISIS = "isIsis";
	String IS_ATLAS = "isAtlas";
	String IS_BULK = "isBulk";
	String MAILBODY = "mailBody";
	String EMAILBODY = "body";
	String EMAIL_SUBJECT = "subject";
	String NEWLINE_HTML = "<br />";
	String MARK_FINAL_HISTORY = "Marked Final Report";
	String DELETE_NON_MARK_HISTORY = "Delete File";
	String RISK_CODE = "riskCode";
	String SUB_ID = "subID";
	String RE_MASTER = "RE_MASTER";
	String IS_SUBJECT_LEVEL_SUBRPT = "isSubjLevelSubRptReq";
	String IS_SUBJECT_LEVEL_SUBRPT_REQ = "IS_SUBJ_LEVEL_SUBRPT_REQ";
	String SUBREPORT_TYPE_MASTER = "CMS_SUBREPORT_TYPE_MASTER";
	String SUB_REPORT_NAME = "SUB_REPORT";
	String SUBREPORT_ID = "SUBREPORT_TYPE_MASTER_ID";
	String CMS_CLIENTCASE = "CMS_CLIENTCASE";
	int ACTIVE = 1;
	int DEACTIVE = 0;
	String ACTION_ADD = "ADD";
	String ACTION_UPDATE = "Update";
	String ACTION_ACTIVE = "Activate";
	String ACTION_DEACTIVE = "Deactivate";
	String SUBJECT_DETAILS = "subjectDetails";
	String REPORT_TYPE_ID_LIST = "reportTypeIdList";
	String ISIS_ERROR_MESSAGE = "ISISErrorMessage";
	String USER_MASTER_ID_LIST = "userMasterIdList";
	boolean TRUEBOOLEAN = true;
	boolean FALSEBOOLEAN = false;
	String CURRENT_PASSWORD = "currentPassword";
	String INVALID_PASSWORD_HISTORY = "Password should not be the same as the last six passwords used";
	String m_message = "Your Atlas account has been created. \n\rYour new login information as below:\n\r";
	String m_message_Forgot = "You have requested forgot password for the your Login ID : ";
	String EMAIl_SUFFIX = "\n\r Thank you.\n\rThomson Reuters \n\r\n\rrisk.thomsonreuters.com \n\r*********************************************************************************************** \n\rThis email was sent to you by Thomson Reuters,the global news and Information Company.\r Any views expressed in this message are those of the individual sender,\rexcept where the sender specifically states them to be the views of Thomson Reuters. \r************************************************************************************************ ";
}