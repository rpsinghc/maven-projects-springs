package com.worldcheck.atlas.frontend.sbm;

import com.integrascreen.orders.Attribute;
import com.integrascreen.orders.CRNRiskData;
import com.integrascreen.orders.CaseLevelRisks;
import com.integrascreen.orders.Country;
import com.integrascreen.orders.CountryBreakDown;
import com.integrascreen.orders.Risk;
import com.integrascreen.orders.RiskAggregation;
import com.integrascreen.orders.RiskCategory;
import com.integrascreen.orders.RiskProfile;
import com.integrascreen.orders.Subject;
import com.integrascreen.orders.SubjectLevelRisks;
import com.integrascreen.orders.Subjects;
import com.savvion.sbm.bizlogic.util.Session;
import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusIndustryVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusRiskVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.customds.CaseInfo;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubjectLevelAggregation;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.masters.RiskAggregationVO;
import com.worldcheck.atlas.vo.masters.RiskProfileVO;
import com.worldcheck.atlas.vo.masters.TotalRiskAggregationVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import com.worldcheck.atlas.vo.task.invoice.AccountsVO;
import com.worldcheck.atlas.vo.task.invoice.InvoiceVO;
import com.worldcheck.atlas.vo.timetracker.TimeTrackerVO;
import com.worldcheck.juno.exception.JunoException;
import com.worldcheck.juno.util.AtlasJunoTriggers;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class TaskManagementController extends MultiActionController {
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.sbm.TaskManagementController");
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	private String successView = "";
	private String successCompleteView = "";
	private String successCompleteTaskView = "";
	private String errorView = "caseStatusErrorPage";
	private String isisPingFailView = "isisPingFailErrorPage";
	private String isisQueueErrorPage = "isisQueueErrorPage";
	private String taskErrorView = "taskStatusErrorPage";
	private String CASE_STATUS_NAME = "caseStatusName";
	private String CANCLLED_STATUS = "Cancelled";
	private String PARENT = "parent";
	private String RISKDATA = "riskData";
	private String RISK_HBD_DATA = "riskHBDData";
	private String SUB_INDUSTRY_DATA = "subIndustryData";
	private String RISK_AGGR_DATA = "riskAggregationData";
	private String TOTAL_RISK_AGGR_DATA = "overAllRiskAggregationData";
	private String SEPERATOR1 = "@@#Sep1#@@";
	private String SEPERATOR2 = "@@#Sep2#@@";
	private String SEPERATOR3 = "@@#Sep3#@@";
	private String ATTR1 = "riskCodeJSP";
	private String ATTR2 = "attrIdJSP";
	private String ATTRNAME = "riskAttrNameJSP";
	private String ATTR3 = "newattrValueJSP";
	private String ATTR4 = "isAppliedJSP";
	private String ATTR5 = "countryIdJSP";
	private String ATTR6 = "attrOldValueJSP";
	private String ATTR7 = "riskCatIdJSP";
	private String ATTR8 = "subIdJSP";
	private String ATTR9 = "indusIdJSP";
	private String ATT_CRN = "CRNJSP";
	private String ATT_CAT_ID = "catIdJSP";
	private String ATT_SUB_ID = "subIdJSP";
	private String ATT_RISK_TYPE = "riskTypeJSP";
	private String ATT_AGGR_VALUE = "aggrValueJSP";
	private String ATTR_SUB_ID = "subIdJSP";
	private String ATTR_RISK_TYPE = "riskTypeJSP";
	private String ATT_CASE_AGGR = "caseLevelAggrValueJSP";
	private String ATT_SUB_AGGR = "subLevelAggrValueJSP";
	private String ATT_CRN_AGGR = "crnAggrValueJSP";
	private String SINGLE_SPACE = " ";
	private String ATT_RISK_LABEL = "riskLabelJSP";
	private String IS_RISK_MANDATORY = "isRiskMandatoryJSP";
	private String ATT_VISIBLE_TO_CLIENT = "visibleToClientJSP";
	private String subjectRiskScreen = "subject_Risks";
	private static final String ZERO = "0";
	private static final String INVOICE_DATE_TEMP = "invoiceDate";
	private static final String CT_DATE_TEMP = "ctDate";
	private static final String INVOICE_NO = "invoiceNO";
	private static final String INVOICE_AMOUNT = "invoiceAmount";
	private static final String CAPWTOWN_ID = "capetownID";
	private static final String INVOICE_CURRENCY_CODE = "invoiceCurrencyCode";
	private static final String PID = "pid";
	private static final String SPLITER_CONS1 = "::";
	private static final String SPLITER_CONS2 = "#";
	private static final String Action_CONS = "save";
	private static final String ACTION = "action";
	private static String REQ_TOCAPETOWN = "toCapetown";
	private static String REQ_CURRENCYCODE = "currencyCode";
	private static String IS_CHECKED = "cancelledCharges";
	private static String REQ_ISISUSER = "isisUsers";
	private static String REQ_UPDATEBY = "updatedBy";
	private static String REQ_CRN = "crn";
	private static String DISCOUNT = "discount";
	private static String CLIENT_CODE = "clientCode";
	private static String CAPETOWN_ID = "capetownID";
	private static String CASE_FEE = "caseFee";
	private static String CREDIT = "credit";
	private static String MULTIPLE_YEAR_BONUS = "multipleYearBonus";
	private static String INVOICE_TO = "invoiceTo";
	private static String BILLING_ADDRESS = "billingAddress";
	private static String INVOICE_INSTRUCTION = "invoiceInst";
	private static String ACCOUNT_ID = "accountID";
	private static String REGISTER_DATE = "regDate";
	private static String DISBURSEMENT = "disbursment";
	private static String SPL_BILLING_INST = "spclBillingInst";
	private static String OTHER_INST = "otherInst";
	private static String BILLING_METHODS = "billingMethods";
	private static String HANDLE_BY = "h_handleBy";
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("mm/dd/yyyy");
	final ResourceLocator resourceLocator = ResourceLocator.self();

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public void setSuccessCompleteTaskView(String successCompleteTaskView) {
		this.successCompleteTaskView = successCompleteTaskView;
	}

	public void setSuccessCompleteView(String successCompleteView) {
		this.successCompleteView = successCompleteView;
	}

	public ModelAndView saveTask(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(this.successView);
		String[] temp = (String[]) null;
		this.logger.debug("in saveTask..");
		ResourceLocator resourceLocator = ResourceLocator.self();

		try {
			if (!this.checkISISPing(request)) {
				return new ModelAndView(this.isisPingFailView);
			} else if (this.checkForCaseStatus(request)) {
				return new ModelAndView(this.errorView);
			} else {
				String workItemName = request.getParameter("workItem");
				this.logger.debug("workItemName in saveTask Method:::" + workItemName);
				if (workItemName != null) {
					temp = workItemName.split("::");
				}

				Session session = SBMUtils.getSession(request);
				if (resourceLocator.getSBMService().wiExistsForUser(workItemName, session, session.getUser())) {
					this.logger.debug("The workstep" + workItemName + "exists for user " + session.getUser());
					if (request.getParameter("CFinal") != null && !"".equals(request.getParameter("CFinal"))) {
						HashMap<String, Object> dsMap = new HashMap();
						String riskData = request.getParameter(this.RISKDATA);
						this.logger.debug("riskData ==>" + riskData);
						List<RiskProfileVO> riskProfileList = new ArrayList();
						String crn = request.getParameter("crn");
						String taskName = "";
						if (temp.length >= 2) {
							taskName = temp[1];
						}

						String loggedInUser = session.getUser();
						if (riskData.trim().length() > 0) {
							riskProfileList = this.createRiskDataList(riskData, crn, loggedInUser);
						}

						this.logger.debug("riskDatalist size  ==>" + ((List) riskProfileList).size());
						String riskHBDData = request.getParameter(this.RISK_HBD_DATA);
						this.logger.debug("riskHBDData ==>" + riskHBDData);
						List<RiskProfileVO> riskProfileListWithHBD = new ArrayList();
						if (riskHBDData.trim().length() > 0) {
							new ArrayList();
							List<RiskProfileVO> riskForProfileIDList = this.getProfileList(riskHBDData, crn,
									loggedInUser);
							long riskProfileId = resourceLocator.getCaseDetailService()
									.fetchProfileId(riskForProfileIDList);
							riskProfileListWithHBD = this.createRiskDataWithHBDList(riskHBDData, crn, loggedInUser,
									riskProfileId);
						}

						this.logger.debug("Risk HBD list size:==>" + ((List) riskProfileListWithHBD).size());
						String subIndustryData = request.getParameter(this.SUB_INDUSTRY_DATA);
						this.logger.debug("SubIndustry" + subIndustryData);
						List<RiskProfileVO> subIndusList = new ArrayList();
						if (subIndustryData.trim().length() > 0) {
							subIndusList = this.createIndustryCodeWithSubjectId(subIndustryData, crn, loggedInUser);
						}

						this.logger.debug("sub list size::" + ((List) subIndusList).size());
						String riskAggregationData = request.getParameter(this.RISK_AGGR_DATA);
						this.logger.debug("riskAggregationData::" + riskAggregationData);
						List<RiskAggregationVO> riskAggregationList = new ArrayList();
						if (riskAggregationData.trim().length() > 0) {
							riskAggregationList = this.createRiskAggregationData(riskAggregationData, loggedInUser);
						}

						this.logger.debug("risk aggregation list size::" + ((List) riskAggregationList).size());
						String totalRiskAggregationData = request.getParameter(this.TOTAL_RISK_AGGR_DATA);
						this.logger.debug("totalRiskAggregationData" + totalRiskAggregationData);
						List<TotalRiskAggregationVO> totalRiskAggregationList = new ArrayList();
						if (totalRiskAggregationData.trim().length() > 0) {
							totalRiskAggregationList = this.createTotalRiskAggregationData(totalRiskAggregationData,
									loggedInUser);
						}

						this.logger.debug(
								"totalRiskAggregation list size is::" + ((List) totalRiskAggregationList).size());
						List<SubjectLevelAggregation> subjectAggregationList = new ArrayList();
						if (crn != null && crn.trim().length() > 0) {
							int totalRiskAggrId = resourceLocator.getCaseDetailService().fetchTotalAggrId(crn);
							String eachSubjectAggregationData = request
									.getParameter("eachSubjectAggregationDataObject");
							this.logger.debug("eachSubjectAggregationData.." + eachSubjectAggregationData);
							if (totalRiskAggrId > 0 && eachSubjectAggregationData != null
									&& eachSubjectAggregationData.trim().length() > 0) {
								subjectAggregationList = this.createSubjectAggregationData(eachSubjectAggregationData,
										crn, loggedInUser, totalRiskAggrId);
							}
						}

						resourceLocator.getCaseDetailService().saveSavvionCaseInformation(
								this.settingCaseDetails(request, dsMap), session, dsMap, (List) riskProfileList,
								(List) riskProfileListWithHBD, (List) subIndusList, (List) riskAggregationList,
								taskName, (List) totalRiskAggregationList);
						if (((List) subjectAggregationList).size() > 0) {
							resourceLocator.getCaseDetailService()
									.saveSubjectAggregation((List) subjectAggregationList);
						}
					}

					this.logger.debug("temp[0]:::::" + temp[0].split("#")[1] + "   temp[1]::::" + temp[1]);
					if (temp[1].equalsIgnoreCase("Client Submission Task")) {
						this.logger.debug("in Client Submission Task::");
						AccountsVO accountsVO = resourceLocator.getInvoiceService()
								.getAccountDetailForCrn(request.getParameter("crn"));
						this.logger.debug("disbursement from accountsVo:::" + accountsVO.getDisbursment()
								+ "disbursement from request:::" + request.getParameter("disbursment"));
						this.logger.debug("Billing Method from request:::" + request.getParameter("billingMethods")
								+ "Handle By from request:::" + request.getParameter("h_handleBy"));
						this.saveOrCompleteInvoice(request, response, accountsVO);
					} else {
						resourceLocator.getSBMService().saveTask(Long.parseLong(temp[0].split("#")[1]), temp[1],
								SBMUtils.getSession(request), new HashMap());
					}

					this.logger.debug("ws saved Sucessfully..");
					return modelAndView;
				} else {
					this.logger.debug("The workstep" + workItemName + "doen't exist for user " + session.getUser()
							+ " . Thus redirecting it to the error page");
					return new ModelAndView(this.taskErrorView);
				}
			}
		} catch (NullPointerException var25) {
			return AtlasUtils.getExceptionView(this.logger, var25);
		} catch (CMSException var26) {
			return AtlasUtils.getExceptionView(this.logger, var26);
		} catch (Exception var27) {
			return AtlasUtils.getExceptionView(this.logger, var27);
		}
	}

	private List<RiskProfileVO> createRiskDataList(String riskProfileData, String crn, String loggedInUser) {
		List<RiskProfileVO> riskProfileList = new ArrayList();
		String[] records = riskProfileData.split(this.SEPERATOR3);
		this.logger.debug("length of records(No. of risks)" + records.length);

		for (int i = 0; i < records.length; ++i) {
			String[] attributeRecord = records[i].split(this.SEPERATOR2);
			RiskProfileVO riskProVO = new RiskProfileVO();

			for (int j = 0; j < attributeRecord.length; ++j) {
				if (attributeRecord[j].endsWith(this.SEPERATOR1)) {
					attributeRecord[j] = attributeRecord[j] + this.SINGLE_SPACE;
				}

				String[] attributes = attributeRecord[j].split(this.SEPERATOR1);

				for (int k = 0; k < attributes.length - 1; ++k) {
					this.logger.debug("Attribute name" + attributes[k]);
					this.logger.debug("Attribute Value" + attributes[k + 1]);
					if (attributes[k].equalsIgnoreCase(this.ATTR1)) {
						riskProVO.setRiskCode(attributes[k + 1]);
					} else if (attributes[k].equalsIgnoreCase(this.ATTR2)) {
						riskProVO.setAttributeId(attributes[k + 1]);
					} else if (attributes[k].equalsIgnoreCase(this.ATTRNAME)) {
						riskProVO.setAttributeName(attributes[k + 1]);
						this.logger.debug("ATTR name::" + riskProVO.getAttributeName());
					} else if (attributes[k].equalsIgnoreCase(this.ATTR3)) {
						if (attributes[k + 1].equals(this.SINGLE_SPACE)) {
							attributes[k + 1] = "";
						}

						riskProVO.setNewattrValue(attributes[k + 1]);
					} else if (attributes[k].equalsIgnoreCase(this.ATTR4)) {
						riskProVO.setIsApplied(Long.parseLong(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.ATTR6)) {
						if (attributes[k + 1].equals(this.SINGLE_SPACE)) {
							attributes[k + 1] = "";
						}

						riskProVO.setOldAttrValue(attributes[k + 1]);
					} else if (attributes[k].equalsIgnoreCase(this.ATTR7)) {
						riskProVO.setRiskCategoryId(Long.parseLong(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.ATT_SUB_ID)) {
						riskProVO.setSubjectId(Long.parseLong(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.ATT_RISK_TYPE)) {
						riskProVO.setRiskType(Long.parseLong(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.IS_RISK_MANDATORY)) {
						riskProVO.setIsRiskMandatory(Integer.parseInt(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.ATT_RISK_LABEL)) {
						riskProVO.setRiskLabel(attributes[k + 1]);
					} else if (attributes[k].equalsIgnoreCase(this.ATT_VISIBLE_TO_CLIENT)) {
						this.logger.debug("Visible to client value is::" + attributes[k + 1]);
						riskProVO.setVisibleToClient(Long.parseLong(attributes[k + 1]));
					} else {
						this.logger.debug("No Match found");
					}

					++k;
				}
			}

			riskProVO.setCRN(crn);
			riskProVO.setUpdatedBy(loggedInUser);
			riskProfileList.add(riskProVO);
		}

		return riskProfileList;
	}

	private List<RiskProfileVO> createRiskDataWithHBDList(String riskProfileData, String crn, String loggedInUser,
			long riskProfileId) {
		List<RiskProfileVO> riskProfileList = new ArrayList();
		String[] records = riskProfileData.split(this.SEPERATOR3);
		this.logger.debug(
				"createRiskDataWithHBDList .. length of records(No. of risks)" + records.length + ".." + riskProfileId);

		try {
			for (int i = 0; i < records.length; ++i) {
				String[] attributeRecord = records[i].split(this.SEPERATOR2);
				RiskProfileVO riskProVO = new RiskProfileVO();

				for (int j = 0; j < attributeRecord.length; ++j) {
					if (attributeRecord[j].endsWith(this.SEPERATOR1)) {
						attributeRecord[j] = attributeRecord[j] + this.SINGLE_SPACE;
					}

					String[] attributes = attributeRecord[j].split(this.SEPERATOR1);

					for (int k = 0; k < attributes.length - 1; ++k) {
						this.logger.debug("Attribute name" + attributes[k]);
						this.logger.debug("Attribute Value" + attributes[k + 1]);
						if (attributes[k].equalsIgnoreCase(this.ATTR1)) {
							riskProVO.setRiskCode(attributes[k + 1]);
						} else if (attributes[k].equalsIgnoreCase(this.ATTR5)) {
							this.logger.debug("before CountryID::" + attributes[k + 1]);
							riskProVO.setCountryId((long) Integer.parseInt(attributes[k + 1]));
							this.logger.debug("CountryID::" + riskProVO.getCountryId());
						} else if (attributes[k].equalsIgnoreCase(this.ATTR2)) {
							riskProVO.setAttributeId(attributes[k + 1]);
						} else if (attributes[k].equalsIgnoreCase(this.ATTRNAME)) {
							riskProVO.setAttributeName(attributes[k + 1]);
							this.logger.debug("ATTR name::" + riskProVO.getAttributeName());
						} else if (attributes[k].equalsIgnoreCase(this.ATTR3)) {
							if (attributes[k + 1].equals(this.SINGLE_SPACE)) {
								attributes[k + 1] = "";
							}

							riskProVO.setNewattrValue(attributes[k + 1]);
						} else if (attributes[k].equalsIgnoreCase(this.ATTR6)) {
							if (attributes[k + 1].equals(this.SINGLE_SPACE)) {
								attributes[k + 1] = "";
							}

							riskProVO.setOldAttrValue(attributes[k + 1]);
						} else if (attributes[k].equalsIgnoreCase(this.ATTR7)) {
							riskProVO.setRiskCategoryId(Long.parseLong(attributes[k + 1]));
						} else if (attributes[k].equalsIgnoreCase(this.ATT_SUB_ID)) {
							riskProVO.setSubjectId(Long.parseLong(attributes[k + 1]));
						} else if (attributes[k].equalsIgnoreCase(this.IS_RISK_MANDATORY)) {
							riskProVO.setIsRiskMandatory(Integer.parseInt(attributes[k + 1]));
						} else if (attributes[k].equalsIgnoreCase(this.ATT_RISK_TYPE)) {
							riskProVO.setRiskType(Long.parseLong(attributes[k + 1]));
						} else if (attributes[k].equalsIgnoreCase(this.ATT_RISK_LABEL)) {
							riskProVO.setRiskLabel(attributes[k + 1]);
						} else if (attributes[k].equalsIgnoreCase(this.ATT_VISIBLE_TO_CLIENT)) {
							this.logger.debug("HBD Visible to client value is::" + attributes[k + 1]);
							riskProVO.setVisibleToClient(Long.parseLong(attributes[k + 1]));
						} else {
							this.logger.debug("No Match found");
						}

						++k;
					}
				}

				riskProVO.setCRN(crn);
				riskProVO.setUpdatedBy(loggedInUser);
				List<RiskProfileVO> riskForProfileIDList = new ArrayList();
				RiskProfileVO rVO = new RiskProfileVO();
				rVO.setCRN(riskProVO.getCRN());
				rVO.setRiskCode(riskProVO.getRiskCode());
				rVO.setRiskCategoryId(riskProVO.getRiskCategoryId());
				rVO.setSubjectId(riskProVO.getSubjectId());
				riskForProfileIDList.add(rVO);
				this.logger.debug("profile details list.." + riskProVO.getRiskCode() + ":"
						+ riskProVO.getRiskCategoryId() + ":" + riskProVO.getSubjectId() + ":" + riskProVO.getCRN());
				riskProVO.setRiskProfileId(
						this.resourceLocator.getCaseDetailService().fetchProfileId(riskForProfileIDList));
				riskProfileList.add(riskProVO);
			}
		} catch (Exception var14) {
			;
		}

		return riskProfileList;
	}

	private List<RiskProfileVO> getProfileList(String riskProfileData, String crn, String loggedInUser) {
		List<RiskProfileVO> riskProfileList = new ArrayList();
		String[] records = riskProfileData.split(this.SEPERATOR3);
		this.logger.debug("length of records(No. of risks)" + records.length);

		for (int i = 0; i < records.length && riskProfileList.size() != 1; ++i) {
			String[] attributeRecord = records[i].split(this.SEPERATOR2);
			RiskProfileVO riskProVO = new RiskProfileVO();

			for (int j = 0; j < attributeRecord.length; ++j) {
				String[] attributes = attributeRecord[j].split(this.SEPERATOR1);

				for (int k = 0; k < attributes.length - 1; ++k) {
					this.logger.debug("Attribute name" + attributes[k]);
					this.logger.debug("Attribute Value" + attributes[k + 1]);
					if (attributes[k].equalsIgnoreCase(this.ATTR1)) {
						riskProVO.setRiskCode(attributes[k + 1]);
					} else if (attributes[k].equalsIgnoreCase(this.ATTR7)) {
						riskProVO.setRiskCategoryId(Long.parseLong(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.ATT_SUB_ID)) {
						riskProVO.setSubjectId(Long.parseLong(attributes[k + 1]));
					} else {
						this.logger.debug("No Match found");
					}

					++k;
				}
			}

			riskProVO.setCRN(crn);
			riskProfileList.add(riskProVO);
		}

		return riskProfileList;
	}

	private List<RiskProfileVO> createIndustryCodeWithSubjectId(String subIndustryData, String CRN,
			String loggedInUser) {
		List<RiskProfileVO> subDetailsList = new ArrayList();
		String[] records = subIndustryData.split(this.SEPERATOR3);
		this.logger.debug("length of records(No. of risks)" + records.length);

		for (int i = 0; i < records.length; ++i) {
			String[] attributeRecord = records[i].split(this.SEPERATOR2);
			RiskProfileVO subDetailsVO = new RiskProfileVO();

			for (int j = 0; j < attributeRecord.length; ++j) {
				String[] attributes = attributeRecord[j].split(this.SEPERATOR1);

				for (int k = 0; k < attributes.length - 1; ++k) {
					this.logger.debug("Attribute name" + attributes[k]);
					this.logger.debug("Attribute Value" + attributes[k + 1]);
					if (attributes[k].equalsIgnoreCase(this.ATTR8)) {
						subDetailsVO.setSubjectId((long) Integer.parseInt(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.ATTR9)) {
						subDetailsVO.setIndustryCode(attributes[k + 1]);
					} else {
						this.logger.debug("No Match found");
					}

					++k;
				}
			}

			subDetailsVO.setUpdatedBy(loggedInUser);
			subDetailsVO.setCRN(CRN);
			subDetailsList.add(subDetailsVO);
		}

		return subDetailsList;
	}

	private List<RiskAggregationVO> createRiskAggregationData(String riskAggrData, String loggedInUser) {
		List<RiskAggregationVO> riskAggregationList = new ArrayList();
		String[] records = riskAggrData.split(this.SEPERATOR3);
		this.logger.debug("length of records(No. of risks)" + records.length);

		for (int i = 0; i < records.length; ++i) {
			String[] attributeRecord = records[i].split(this.SEPERATOR2);
			RiskAggregationVO riskAggrVO = new RiskAggregationVO();

			for (int j = 0; j < attributeRecord.length; ++j) {
				String[] attributes = attributeRecord[j].split(this.SEPERATOR1);

				for (int k = 0; k < attributes.length - 1; ++k) {
					this.logger.debug("Attribute name" + attributes[k]);
					this.logger.debug("Attribute Value" + attributes[k + 1]);
					if (attributes[k].equalsIgnoreCase(this.ATT_CRN)) {
						riskAggrVO.setCrn(attributes[k + 1]);
					} else if (attributes[k].equalsIgnoreCase(this.ATT_CAT_ID)) {
						riskAggrVO.setCatId(Long.parseLong(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.ATT_SUB_ID)) {
						riskAggrVO.setSubId(Long.parseLong(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.ATT_RISK_TYPE)) {
						riskAggrVO.setRiskType(Long.parseLong(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.ATT_AGGR_VALUE)) {
						riskAggrVO.setAggrValue(Long.parseLong(attributes[k + 1]));
					} else {
						this.logger.debug("No Match found");
					}

					++k;
				}
			}

			riskAggrVO.setUpdatedBy(loggedInUser);
			riskAggregationList.add(riskAggrVO);
		}

		return riskAggregationList;
	}

	private List<TotalRiskAggregationVO> createTotalRiskAggregationData(String totalRiskAggrData, String userName) {
		List<TotalRiskAggregationVO> totalRiskAggregationList = new ArrayList();
		String[] records = totalRiskAggrData.split(this.SEPERATOR3);
		this.logger.debug("length of records(No. of risks)" + records.length);

		for (int i = 0; i < records.length; ++i) {
			String[] attributeRecord = records[i].split(this.SEPERATOR2);
			TotalRiskAggregationVO totalRiskAggrVO = new TotalRiskAggregationVO();

			for (int j = 0; j < attributeRecord.length; ++j) {
				String[] attributes = attributeRecord[j].split(this.SEPERATOR1);

				for (int k = 0; k < attributes.length - 1; ++k) {
					this.logger.debug("Attribute name" + attributes[k]);
					this.logger.debug("Attribute Value" + attributes[k + 1]);
					if (attributes[k].equalsIgnoreCase(this.ATT_CRN)) {
						totalRiskAggrVO.setCrn(attributes[k + 1]);
					} else if (attributes[k].equalsIgnoreCase(this.ATT_CASE_AGGR)) {
						totalRiskAggrVO.setTotalCaseLevelAggrValue(Integer.parseInt(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.ATT_SUB_AGGR)) {
						totalRiskAggrVO.setTotalSubLevelAggrValue(Integer.parseInt(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.ATT_CRN_AGGR)) {
						totalRiskAggrVO.setTotalCRNLevelAggrValue(Integer.parseInt(attributes[k + 1]));
					} else {
						this.logger.debug("No Match found");
					}

					++k;
				}
			}

			totalRiskAggrVO.setUpdatedBy(userName);
			totalRiskAggregationList.add(totalRiskAggrVO);
		}

		return totalRiskAggregationList;
	}

	private List<SubjectLevelAggregation> createSubjectAggregationData(String eachSubjectAggregationData, String crn,
			String loggedInUser, int totalRiskAggrId) {
		List<SubjectLevelAggregation> slAggrList = new ArrayList();
		String[] records = eachSubjectAggregationData.split(this.SEPERATOR3);
		this.logger.debug("length of records(No. of risks)" + records.length);

		for (int i = 0; i < records.length; ++i) {
			String[] attributeRecord = records[i].split(this.SEPERATOR2);
			SubjectLevelAggregation slAggr = new SubjectLevelAggregation();

			for (int j = 0; j < attributeRecord.length; ++j) {
				String[] attributes = attributeRecord[j].split(this.SEPERATOR1);

				for (int k = 0; k < attributes.length - 1; ++k) {
					this.logger.debug("Attribute name" + attributes[k]);
					this.logger.debug("Attribute Value" + attributes[k + 1]);
					if (attributes[k].equalsIgnoreCase(this.ATT_SUB_ID)) {
						slAggr.setSubjectId(Long.parseLong(attributes[k + 1]));
					} else if (attributes[k].equalsIgnoreCase(this.ATT_AGGR_VALUE)) {
						slAggr.setEachSubLvlAggr(Integer.parseInt(attributes[k + 1]));
					} else {
						this.logger.debug("No Match found");
					}

					++k;
				}
			}

			slAggr.setCrn(crn);
			slAggr.setUpdatedBy(loggedInUser);
			slAggr.setTotalAggregationId(totalRiskAggrId);
			slAggrList.add(slAggr);
		}

		return slAggrList;
	}

	public ModelAndView completeTask(HttpServletRequest request, HttpServletResponse response) {
		String[] temp = (String[]) null;
		this.logger.debug("in completeTask..");
		request.getSession().setAttribute(this.PARENT, "yes");
		ModelAndView modelAndView = new ModelAndView(this.successCompleteView);

		try {
			if (!this.checkISISPing(request)) {
				return new ModelAndView(this.isisPingFailView);
			} else if (this.checkForCaseStatus(request)) {
				return new ModelAndView(this.errorView);
			} else {
				String workItemName = request.getParameter("workItem");
				if (workItemName != null) {
					temp = workItemName.split("::");
				}

				ResourceLocator resourceLocator = ResourceLocator.self();
				Session session = SBMUtils.getSession(request);
				if (resourceLocator.getSBMService().wiExistsForUser(workItemName, session, session.getUser())) {
					this.logger.debug("The workstep" + workItemName + "exists for user " + session.getUser());
					long pid = Long.parseLong(temp[0].split("#")[1]);
					String crn = (String) resourceLocator.getSBMService().getDataslotValue(pid, "CRN", session);
					String cycleName = (String) resourceLocator.getSBMService().getDataslotValue(pid, "ProcessCycle",
							session);
					boolean isPullBackReq = false;
					String clientSentDate;
					if ("Final".equalsIgnoreCase(cycleName)) {
						clientSentDate = (String) resourceLocator.getSBMService().getDataslotValue(pid, "CInterim1",
								session);
						if (clientSentDate != null) {
							String interim2 = (String) resourceLocator.getSBMService().getDataslotValue(pid,
									"CInterim2", session);
							if (interim2 == null && request.getParameter("CInterim2") != null
									&& !"".equalsIgnoreCase(request.getParameter("CInterim2"))) {
								this.logger.info(
										"Pull back req. User has added interim2 cycle in the case and tried to complete the task");
								isPullBackReq = true;
							}
						} else if (request.getParameter("CInterim1") != null
								&& !"".equalsIgnoreCase(request.getParameter("CInterim1"))) {
							this.logger.info(
									"Pull back req. User has added interim1 cycle in the case and tried to complete the task");
							isPullBackReq = true;
						}
					}

					if (isPullBackReq) {
						if (request.getParameter("CFinal") != null && !"".equals(request.getParameter("CFinal"))) {
							this.logger.debug("inside the CaseDetail API========**");
							HashMap<String, Object> dsMap = new HashMap();
							resourceLocator.getCaseDetailService().saveSavvionCaseInformation(
									this.settingCaseDetails(request, dsMap), SBMUtils.getSession(request), dsMap);
							this.logger.debug("saveSavvionCaseInformation=========successfully done----**");
						}

						return modelAndView;
					} else {
						clientSentDate = request.getParameter("ClientSentDate");
						this.logger.debug("clientSentDate :: " + clientSentDate);
						TimeTrackerVO timeTrackerVO = new TimeTrackerVO();
						timeTrackerVO.setCrn(crn);
						timeTrackerVO.setUserName(session.getUser());
						timeTrackerVO.setUpdatedBy(session.getUser());
						timeTrackerVO.setTaskName(temp[1]);
						timeTrackerVO.setTaskPid(String.valueOf(pid));
						ResourceLocator.self().getTimeTrackerService().stopTimeTracker(timeTrackerVO);
						this.logger.debug(
								" tracker attribute in session :: " + request.getSession().getAttribute("TrackerOn"));
						request.getSession().removeAttribute("TrackerOn");
						boolean processFlag = true;
						String riskData;
						String riskHBDData;
						String subIndustryData;
						String riskAggregationData;
						String totalRiskAggregationData;
						String eachSubjectAggregationData;
						CaseHistory caseHistory;
						String decodedTaskName;
						int timeTrackerId;
						Object riskProfileList;
						Object riskProfileListWithHBD;
						String loggedInUser;
						Object subIndusList;
						if ("Consolidation Task".equalsIgnoreCase(temp[1])) {
							if (!"true".equalsIgnoreCase(request.getParameter("reRejected"))) {
								caseHistory = new CaseHistory();
								caseHistory.setCRN(crn);
								caseHistory.setProcessCycle(cycleName);
								caseHistory.setPid(String.valueOf(pid));
								caseHistory.setTaskName("Consolidation Task");
								caseHistory.setTaskStatus("Approved");
								if (request.getSession().getAttribute("loginLevel") != null) {
									caseHistory.setPerformer((String) request.getSession().getAttribute("performedBy"));
								} else {
									caseHistory.setPerformer(session.getUser());
								}

								this.logger.debug("CRN:" + caseHistory.getCRN());
								this.logger.debug("processCycle:" + caseHistory.getProcessCycle());
								this.logger.debug("PID:" + caseHistory.getPid());
								this.logger.debug("Performer:" + caseHistory.getPerformer());
								ResourceLocator.self().getCaseHistoryService()
										.setCaseHistoryForTaskApproved(caseHistory);
							}
						} else if ("Review Task".equalsIgnoreCase(temp[1])) {
							if (!"true".equalsIgnoreCase(request.getParameter("reRejected"))) {
								caseHistory = new CaseHistory();
								caseHistory.setCRN(crn);
								caseHistory.setProcessCycle(cycleName);
								caseHistory.setPid(String.valueOf(pid));
								caseHistory.setTaskName("Review Task");
								caseHistory.setTaskStatus("Approved");
								caseHistory.setPerformer(session.getUser());
								this.logger.debug("CRN:" + caseHistory.getCRN());
								this.logger.debug("processCycle:" + caseHistory.getProcessCycle());
								this.logger.debug("PID:" + caseHistory.getPid());
								this.logger.debug("Performer:" + caseHistory.getPerformer());
								ResourceLocator.self().getCaseHistoryService()
										.setCaseHistoryForTaskApproved(caseHistory);
							}

							int reviewerSize = ((Vector) resourceLocator.getSBMService().getDataslotValue(pid,
									"Reviewers", session)).size();
							this.logger.debug("reviewer size is " + reviewerSize);
							if (reviewerSize < 1) {
								String teamType = (String) resourceLocator.getSBMService().getDataslotValue(pid,
										"TeamTypeList", session);
								long teamId = Long.parseLong(teamType.split("#")[1]);
								this.logger.debug("team id is " + teamId);
								decodedTaskName = (String) resourceLocator.getSBMService().getDataslotValue(pid,
										"TeamCycleName", session);
								if (decodedTaskName == null || "".equalsIgnoreCase(decodedTaskName)) {
									decodedTaskName = cycleName;
								}

								timeTrackerId = resourceLocator.getTaskService().updateDateForMIS(teamId,
										decodedTaskName, session.getUser());
								this.logger.debug("updated row for MIS is " + timeTrackerId);
							}
						} else if ("Client Submission Task".equalsIgnoreCase(temp[1])) {
							String clientFeedBack = request.getParameter("ClientFeedback");
							if (clientFeedBack == null || "".equalsIgnoreCase(clientFeedBack.trim())) {
								clientFeedBack = "";
							}

							if (!"true".equalsIgnoreCase(request.getParameter("reRejected"))) {
								resourceLocator.getTaskService().updateClientSubmissionDate(clientSentDate, cycleName,
										crn, session.getUser(), clientFeedBack);
								CaseHistory caseHistory = new CaseHistory();
								caseHistory.setCRN(crn);
								caseHistory.setProcessCycle(cycleName);
								caseHistory.setPid(String.valueOf(pid));
								caseHistory.setTaskName("Client Submission Task");
								caseHistory.setTaskStatus("Approved");
								caseHistory.setPerformer(session.getUser());
								this.logger.debug("CRN:" + caseHistory.getCRN());
								this.logger.debug("processCycle:" + caseHistory.getProcessCycle());
								this.logger.debug("PID:" + caseHistory.getPid());
								this.logger.debug("Performer:" + caseHistory.getPerformer());
								ResourceLocator.self().getCaseHistoryService()
										.setCaseHistoryForTaskApproved(caseHistory);
								if ("Interim1".equalsIgnoreCase(cycleName)) {
									this.logger.debug("Interim1 cycle ");
									AtlasJunoTriggers.sendProcessCycleCompletionTrigger(crn, "Interim1");
								} else if ("Interim2".equalsIgnoreCase(cycleName)) {
									this.logger.debug("Interim2 cycle ");
									AtlasJunoTriggers.sendProcessCycleCompletionTrigger(crn, "Interim2");
								} else if ("Final".equalsIgnoreCase(cycleName)) {
									this.logger.debug("Final cycle::");
									crn = request.getParameter("crn");
									loggedInUser = session.getUser();
									if (resourceLocator.getTaskService().getISISStatus(crn) == 1) {
										ClientCaseStatusVO clientCaseStatusVO = resourceLocator.getTaskService()
												.getFileForISIS(pid);
										clientCaseStatusVO.setCRN(crn);
										clientCaseStatusVO.setExpressCase((Boolean) resourceLocator.getSBMService()
												.getDataslotValue(pid, "ExpressCase", session) ? "True" : "False");
										clientCaseStatusVO.setStatus("CMP");
										clientCaseStatusVO.setUpdateType("Case");
										ClientCaseStatusIndustryVO[] industryArray = ResourceLocator.self()
												.getSubjectService().getSubjectsIndustryForISIS(crn);
										ClientCaseStatusRiskVO[] riskArray = ResourceLocator.self().getSubjectService()
												.getSubjectsRisksForISIS(crn);
										clientCaseStatusVO.setSubjectIndustry(industryArray);
										clientCaseStatusVO.setSubjectRisk(riskArray);
										riskData = request.getParameter(this.RISKDATA);
										this.logger.debug("riskData ==>" + riskData);
										riskProfileList = new ArrayList();
										if (riskData.trim().length() > 0) {
											riskProfileList = this.createRiskDataList(riskData, crn, loggedInUser);
										}

										this.logger.debug("riskDatalist size  ==>" + ((List) riskProfileList).size());
										riskHBDData = request.getParameter(this.RISK_HBD_DATA);
										this.logger.debug("riskHBDData ==>" + riskHBDData);
										riskProfileListWithHBD = new ArrayList();
										if (riskHBDData.trim().length() > 0) {
											new ArrayList();
											List<RiskProfileVO> riskForProfileIDList = this.getProfileList(riskHBDData,
													crn, loggedInUser);
											long riskProfileId = resourceLocator.getCaseDetailService()
													.fetchProfileId(riskForProfileIDList);
											riskProfileListWithHBD = this.createRiskDataWithHBDList(riskHBDData, crn,
													loggedInUser, riskProfileId);
										}

										this.logger.debug(
												"Risk HBD list size:==>" + ((List) riskProfileListWithHBD).size());
										subIndustryData = request.getParameter(this.SUB_INDUSTRY_DATA);
										this.logger.debug("SubIndustry" + subIndustryData);
										subIndusList = new ArrayList();
										if (subIndustryData.trim().length() > 0) {
											subIndusList = this.createIndustryCodeWithSubjectId(subIndustryData, crn,
													loggedInUser);
										}

										this.logger.debug("sub list size::" + ((List) subIndusList).size());
										riskAggregationData = request.getParameter(this.RISK_AGGR_DATA);
										this.logger.debug("riskAggregationData::" + riskAggregationData);
										List<RiskAggregationVO> riskAggregationList = new ArrayList();
										if (riskAggregationData.trim().length() > 0) {
											riskAggregationList = this.createRiskAggregationData(riskAggregationData,
													loggedInUser);
										}

										this.logger.debug(
												"risk aggregation list size::" + ((List) riskAggregationList).size());
										totalRiskAggregationData = request.getParameter(this.TOTAL_RISK_AGGR_DATA);
										this.logger.debug("totalRiskAggregationData" + totalRiskAggregationData);
										List<TotalRiskAggregationVO> totalRiskAggregationList = new ArrayList();
										if (totalRiskAggregationData.trim().length() > 0) {
											totalRiskAggregationList = this.createTotalRiskAggregationData(
													totalRiskAggregationData, loggedInUser);
										}

										this.logger.debug("totalRiskAggregation list size is::"
												+ ((List) totalRiskAggregationList).size());
										int totalRiskAggrId = resourceLocator.getCaseDetailService()
												.fetchTotalAggrId(crn);
										eachSubjectAggregationData = request
												.getParameter("eachSubjectAggregationDataObject");
										this.logger.debug("eachSubjectAggregationData.." + eachSubjectAggregationData);
										List<SubjectLevelAggregation> subjectAggregationList = new ArrayList();
										if (totalRiskAggrId > 0 && eachSubjectAggregationData != null
												&& eachSubjectAggregationData.trim().length() > 0) {
											subjectAggregationList = this.createSubjectAggregationData(
													eachSubjectAggregationData, crn, loggedInUser, totalRiskAggrId);
										}

										this.logger.debug("<Parent>");
										this.logger.debug("<CRN>" + clientCaseStatusVO.getCRN() + "</CRN>");
										this.logger.debug("<Status>" + clientCaseStatusVO.getStatus() + "</Status>");
										this.logger
												.debug("<FileName>" + clientCaseStatusVO.getFileName() + "</FileName>");
										this.logger.debug(
												"<flVersion>" + clientCaseStatusVO.getVersion() + "</flVersion>");
										this.logger.debug("<ExpressCase>" + clientCaseStatusVO.getExpressCase()
												+ "</ExpressCase>");
										this.logger.debug(
												"<UpdateType>" + clientCaseStatusVO.getUpdateType() + "</UpdateType>");
										this.logger.debug("<SubjectIndustries>");
										if (industryArray != null && industryArray.length > 0) {
											for (int i = 0; i < industryArray.length; ++i) {
												this.logger.debug("<ISISSubjectID>"
														+ industryArray[i].getISISSubjectID() + "</ISISSubjectID>");
												this.logger.debug("<AtlasSubjectID>"
														+ industryArray[i].getAtlasSubjectID() + "</AtlasSubjectID>");
												this.logger.debug("<IndustryName>" + industryArray[i].getIndustryName()
														+ "</IndustryName>");
												this.logger.debug("<IndustryID>" + industryArray[i].getIndustryID()
														+ "</IndustryID>");
											}
										}

										this.logger.debug("</SubjectIndustries>");
										RiskProfile riskProfile = this.setRiskProfileISISVo(crn, (List) riskProfileList,
												(List) riskProfileListWithHBD, (List) subIndusList,
												(List) riskAggregationList, (List) totalRiskAggregationList,
												loggedInUser, (List) subjectAggregationList);
										this.logger.debug("Complete Task:Risk Profile Object is---" + riskProfile);
										this.logger.debug("Complete Task:Risk Profile Class Name is---"
												+ riskProfile.getClass().getName());
										this.logger.debug("Before setting VO");
										clientCaseStatusVO.setOtherinformation(riskProfile);
										this.logger.debug("After setting VO");
										processFlag = resourceLocator.getAtlasWebServiceClient()
												.updateStatus(clientCaseStatusVO);
										this.logger.debug("After setting process flag");
									}

									if (processFlag) {
										AtlasJunoTriggers.sendCaseCompletionTrigger(crn,
												resourceLocator.getTaskService().isFinalAndJunoUploadedFile(pid));
									}
								}
							}
						}

						this.logger.debug("processFlag vlaue for case completion is :: " + processFlag);
						if (!processFlag) {
							return new ModelAndView(this.isisQueueErrorPage);
						} else {
							this.logger.debug("Check CFinal::" + request.getParameter("CFinal"));
							HashMap dsMap;
							if (request.getParameter("CFinal") != null && !"".equals(request.getParameter("CFinal"))) {
								this.logger.debug("inside the CaseDetail API========**");
								dsMap = new HashMap();
								riskData = request.getParameter(this.RISKDATA);
								this.logger.debug("riskData ==>" + riskData);
								List<RiskProfileVO> riskProfileList = new ArrayList();
								crn = request.getParameter("crn");
								loggedInUser = "";
								if (temp.length >= 2) {
									loggedInUser = temp[1];
								}

								String loggedInUser = session.getUser();
								if (riskData.trim().length() > 0) {
									riskProfileList = this.createRiskDataList(riskData, crn, loggedInUser);
								}

								this.logger.debug("riskDatalist size  ==>" + ((List) riskProfileList).size());
								riskHBDData = request.getParameter(this.RISK_HBD_DATA);
								this.logger.debug("riskHBDData ==>" + riskHBDData);
								List<RiskProfileVO> riskProfileListWithHBD = new ArrayList();
								if (riskHBDData.trim().length() > 0) {
									new ArrayList();
									List<RiskProfileVO> riskForProfileIDList = this.getProfileList(riskHBDData, crn,
											loggedInUser);
									long riskProfileId = resourceLocator.getCaseDetailService()
											.fetchProfileId(riskForProfileIDList);
									riskProfileListWithHBD = this.createRiskDataWithHBDList(riskHBDData, crn,
											loggedInUser, riskProfileId);
								}

								this.logger.debug("Risk HBD list size:==>" + ((List) riskProfileListWithHBD).size());
								subIndustryData = request.getParameter(this.SUB_INDUSTRY_DATA);
								this.logger.debug("SubIndustry" + subIndustryData);
								List<RiskProfileVO> subIndusList = new ArrayList();
								if (subIndustryData.trim().length() > 0) {
									subIndusList = this.createIndustryCodeWithSubjectId(subIndustryData, crn,
											loggedInUser);
								}

								this.logger.debug("sub list size::" + ((List) subIndusList).size());
								riskAggregationData = request.getParameter(this.RISK_AGGR_DATA);
								this.logger.debug("riskAggregationData::" + riskAggregationData);
								riskProfileList = new ArrayList();
								if (riskAggregationData.trim().length() > 0) {
									riskProfileList = this.createRiskAggregationData(riskAggregationData, loggedInUser);
								}

								this.logger.debug("risk aggregation list size::" + ((List) riskProfileList).size());
								totalRiskAggregationData = request.getParameter(this.TOTAL_RISK_AGGR_DATA);
								this.logger.debug("totalRiskAggregationData" + totalRiskAggregationData);
								riskProfileListWithHBD = new ArrayList();
								if (totalRiskAggregationData.trim().length() > 0) {
									riskProfileListWithHBD = this
											.createTotalRiskAggregationData(totalRiskAggregationData, loggedInUser);
								}

								this.logger.debug(
										"totalRiskAggregation list size is::" + ((List) riskProfileListWithHBD).size());
								this.logger.debug("crn is.." + crn);
								subIndusList = new ArrayList();
								if (crn != null && crn.trim().length() > 0) {
									int totalRiskAggrId = resourceLocator.getCaseDetailService().fetchTotalAggrId(crn);
									eachSubjectAggregationData = request
											.getParameter("eachSubjectAggregationDataObject");
									this.logger.debug("eachSubjectAggregationData.." + eachSubjectAggregationData);
									if (totalRiskAggrId > 0 && eachSubjectAggregationData != null
											&& eachSubjectAggregationData.trim().length() > 0) {
										subIndusList = this.createSubjectAggregationData(eachSubjectAggregationData,
												crn, loggedInUser, totalRiskAggrId);
									}
								}

								resourceLocator.getCaseDetailService().saveSavvionCaseInformation(
										this.settingCaseDetails(request, dsMap), session, dsMap, (List) riskProfileList,
										(List) riskProfileListWithHBD, (List) subIndusList, (List) riskProfileList,
										loggedInUser, (List) riskProfileListWithHBD);
								if (((List) subIndusList).size() > 0) {
									resourceLocator.getCaseDetailService().saveSubjectAggregation((List) subIndusList);
								}

								this.logger.debug("saveSavvionCaseInformation=========successfully done----**");
							}

							if ("Final".equalsIgnoreCase(cycleName)
									&& "Client Submission Task".equalsIgnoreCase(temp[1])
									&& !"true".equalsIgnoreCase(request.getParameter("reRejected"))) {
								resourceLocator.getCaseDetailService().updateCaseStatus(crn, session.getUser(),
										"Completed Client Submission");
							}

							resourceLocator.getSBMService().completeTask(session, new HashMap(), pid, temp[1]);
							this.logger.debug("ws completed Successfully..");
							this.logger.info("Task " + temp[1] + " has been completed for crn " + crn);
							if ("Office Assignment Task".equalsIgnoreCase(temp[1])
									|| "Team Assignment Task".equalsIgnoreCase(temp[1])) {
								dsMap = new HashMap();
								dsMap.put("crn", crn);
								dsMap.put("userName", session.getUser());
								List<MyTaskPageVO> getAllTATasks = resourceLocator.getTaskService()
										.getAllTeamAssignmentTasks(dsMap);
								this.logger.debug("getAllTATasks" + getAllTATasks.size());

								try {
									for (int i = 0; i < 7 && !"Team Assignment Task".equalsIgnoreCase(temp[1])
											&& getAllTATasks.size() <= 0; ++i) {
										Thread.currentThread();
										Thread.sleep((long) (i * 1000));
										getAllTATasks = resourceLocator.getTaskService()
												.getAllTeamAssignmentTasks(dsMap);
									}
								} catch (InterruptedException var36) {
									var36.printStackTrace();
								}

								if (getAllTATasks.size() > 0) {
									MyTaskPageVO myTaskPageVO = null;
									Iterator<MyTaskPageVO> iterator = getAllTATasks.iterator();
									if (iterator.hasNext()) {
										myTaskPageVO = (MyTaskPageVO) iterator.next();
										request.getSession().setAttribute("getTAWorkItemId",
												myTaskPageVO.getWorkItemId());
										request.getSession().setAttribute("getTACrn", myTaskPageVO.getCrn());
										request.getSession().setAttribute("getTAPid",
												myTaskPageVO.getProcessInstanceId());
										decodedTaskName = "(" + myTaskPageVO.getTeamTypeList().split("#")[0] + ")";
										request.getSession().setAttribute("getTATeamType", decodedTaskName);
										this.logger.debug("teamType::" + decodedTaskName);
										this.logger.debug(
												"after getProcessInstanceId::" + myTaskPageVO.getProcessInstanceId());
										this.logger.debug("after getWorkItemId::" + myTaskPageVO.getWorkItemId());
										this.logger.debug("after getCRN::" + myTaskPageVO.getCrn());
									}

									timeTrackerVO.setCrn(crn);
									timeTrackerVO.setUserName(session.getUser());
									this.logger.debug("myTaskPageVO.getProcessInstanceId()::"
											+ myTaskPageVO.getProcessInstanceId());
									timeTrackerVO.setTaskPid(String.valueOf(myTaskPageVO.getProcessInstanceId()));
									timeTrackerVO.setProcessCycle(myTaskPageVO.getCurrentCycle());
									timeTrackerVO.setTaskStatus(myTaskPageVO.getStatus());
									decodedTaskName = myTaskPageVO.getTaskName();
									timeTrackerVO
											.setTaskName(decodedTaskName == null ? "" : decodedTaskName.split("::")[1]);
									timeTrackerVO.setWorkItemName(decodedTaskName == null ? "" : decodedTaskName);
									timeTrackerVO.setUpdatedBy(session.getUser());
									timeTrackerId = resourceLocator.getTimeTrackerService()
											.startTimeTracker(timeTrackerVO);
									timeTrackerVO.setTrackerId(timeTrackerId);
									request.getSession().setAttribute("trackerBean", timeTrackerVO);
									request.getSession().setAttribute("TrackerOn", "TrackerOn");
									ResourceLocator.self().getCacheService().updateTTTokenCache(session.getUser(),
											String.valueOf(timeTrackerId));
									this.logger.debug("after getAllTATasks" + getAllTATasks.size());
									modelAndView = new ModelAndView(this.successCompleteTaskView);
								}
							}

							return modelAndView;
						}
					}
				} else {
					this.logger.debug("The workstep" + workItemName + "doen't exist for user " + session.getUser()
							+ " . Thus redirecting it to the error page");
					return new ModelAndView(this.taskErrorView);
				}
			}
		} catch (NullPointerException var37) {
			return AtlasUtils.getExceptionView(this.logger, var37);
		} catch (CMSException var38) {
			return AtlasUtils.getExceptionView(this.logger, var38);
		} catch (JunoException var39) {
			return AtlasUtils.getExceptionView(this.logger, var39);
		}
	}

	public ModelAndView completeTaskResearch(HttpServletRequest request, HttpServletResponse response) {
		String[] temp = (String[]) null;
		this.logger.debug("in completeResearchTask..");
		request.getSession().setAttribute(this.PARENT, "yes");
		ModelAndView modelAndView = new ModelAndView(this.successCompleteView);

		try {
			if (!this.checkISISPing(request)) {
				return new ModelAndView(this.isisPingFailView);
			} else if (this.checkForCaseStatus(request)) {
				return new ModelAndView(this.errorView);
			} else {
				String workItemName = request.getParameter("workItem");
				this.logger.debug("WorkItem Name::::" + workItemName);
				if (workItemName != null) {
					temp = workItemName.split("::");
				}

				long pid = Long.parseLong(temp[0].split("#")[1]);
				this.logger.debug("In research task. PID is " + pid);
				Session session = SBMUtils.getSession(request);
				ResourceLocator resourceLocator = ResourceLocator.self();
				if (resourceLocator.getSBMService().wiExistsForUser(workItemName, session, session.getUser())) {
					this.logger.debug("The workstep" + workItemName + "exists for user " + session.getUser());
					this.logger.debug("WS name is " + temp[1]);
					String crn = (String) resourceLocator.getSBMService().getDataslotValue(pid, "CRN", session);
					TimeTrackerVO timeTrackerVO = new TimeTrackerVO();
					timeTrackerVO.setCrn(crn);
					timeTrackerVO.setUserName(session.getUser());
					timeTrackerVO.setUpdatedBy(session.getUser());
					timeTrackerVO.setTaskName("Research Task");
					timeTrackerVO.setTaskPid(String.valueOf(pid));
					ResourceLocator.self().getTimeTrackerService().stopTimeTracker(timeTrackerVO);
					this.logger.debug(
							" tracker attribute in session :: " + request.getSession().getAttribute("TrackerOn"));
					request.getSession().removeAttribute("TrackerOn");
					String taskName;
					String loggedInUser;
					if (request.getParameter("CFinal") != null && !"".equals(request.getParameter("CFinal"))) {
						HashMap<String, Object> dsMap = new HashMap();
						this.logger.debug("the name of manager in research task" + request.getParameter("CaseManager"));
						String riskData = request.getParameter(this.RISKDATA);
						this.logger.debug("riskData ==>" + riskData);
						List<RiskProfileVO> riskProfileList = new ArrayList();
						taskName = "";
						if (temp.length >= 2) {
							taskName = temp[1];
						}

						loggedInUser = session.getUser();
						if (riskData.trim().length() > 0) {
							riskProfileList = this.createRiskDataList(riskData, crn, loggedInUser);
						}

						this.logger.debug("riskDatalist size  ==>" + ((List) riskProfileList).size());
						String riskHBDData = request.getParameter(this.RISK_HBD_DATA);
						this.logger.debug("riskHBDData ==>" + riskHBDData);
						List<RiskProfileVO> riskProfileListWithHBD = new ArrayList();
						if (riskHBDData.trim().length() > 0) {
							new ArrayList();
							List<RiskProfileVO> riskForProfileIDList = this.getProfileList(riskHBDData, crn,
									loggedInUser);
							long riskProfileId = resourceLocator.getCaseDetailService()
									.fetchProfileId(riskForProfileIDList);
							riskProfileListWithHBD = this.createRiskDataWithHBDList(riskHBDData, crn, loggedInUser,
									riskProfileId);
						}

						this.logger.debug("Risk HBD list size:==>" + ((List) riskProfileListWithHBD).size());
						String subIndustryData = request.getParameter(this.SUB_INDUSTRY_DATA);
						this.logger.debug("SubIndustry" + subIndustryData);
						List<RiskProfileVO> subIndusList = new ArrayList();
						if (subIndustryData.trim().length() > 0) {
							subIndusList = this.createIndustryCodeWithSubjectId(subIndustryData, crn, loggedInUser);
						}

						this.logger.debug("sub list size::" + ((List) subIndusList).size());
						String riskAggregationData = request.getParameter(this.RISK_AGGR_DATA);
						this.logger.debug("riskAggregationData::" + riskAggregationData);
						List<RiskAggregationVO> riskAggregationList = new ArrayList();
						if (riskAggregationData.trim().length() > 0) {
							riskAggregationList = this.createRiskAggregationData(riskAggregationData, loggedInUser);
						}

						this.logger.debug("risk aggregation list size::" + ((List) riskAggregationList).size());
						String totalRiskAggregationData = request.getParameter(this.TOTAL_RISK_AGGR_DATA);
						this.logger.debug("totalRiskAggregationData" + totalRiskAggregationData);
						List<TotalRiskAggregationVO> totalRiskAggregationList = new ArrayList();
						if (totalRiskAggregationData.trim().length() > 0) {
							totalRiskAggregationList = this.createTotalRiskAggregationData(totalRiskAggregationData,
									loggedInUser);
						}

						this.logger.debug(
								"totalRiskAggregation list size is::" + ((List) totalRiskAggregationList).size());
						List<SubjectLevelAggregation> subjectAggregationList = new ArrayList();
						if (crn != null && crn.trim().length() > 0) {
							int totalRiskAggrId = resourceLocator.getCaseDetailService().fetchTotalAggrId(crn);
							String eachSubjectAggregationData = request
									.getParameter("eachSubjectAggregationDataObject");
							this.logger.debug("eachSubjectAggregationData.." + eachSubjectAggregationData);
							if (totalRiskAggrId > 0 && eachSubjectAggregationData != null
									&& eachSubjectAggregationData.trim().length() > 0) {
								subjectAggregationList = this.createSubjectAggregationData(eachSubjectAggregationData,
										crn, loggedInUser, totalRiskAggrId);
							}
						}

						resourceLocator.getCaseDetailService().saveSavvionCaseInformation(
								this.settingCaseDetails(request, dsMap), SBMUtils.getSession(request), dsMap,
								(List) riskProfileList, (List) riskProfileListWithHBD, (List) subIndusList,
								(List) riskAggregationList, taskName, (List) totalRiskAggregationList);
						if (((List) subjectAggregationList).size() > 0) {
							resourceLocator.getCaseDetailService()
									.saveSubjectAggregation((List) subjectAggregationList);
						}
					}

					int rowsUpdated = resourceLocator.getTaskService().updateRESstauts(session.getUser(), crn);
					this.logger.debug("Total rows updated " + rowsUpdated);
					String teamName = (String) resourceLocator.getSBMService().getDataslotValue(pid, "TeamTypeList",
							session);
					this.logger.debug("team name is " + teamName);
					taskName = "";
					taskName = (String) resourceLocator.getSBMService().getDataslotValue(pid, "TeamCycleName", session);
					resourceLocator.getSBMService().completeTask(SBMUtils.getSession(request), new HashMap(), pid,
							temp[1]);
					this.logger.debug("cycle name is " + taskName);
					loggedInUser = "";
					if (teamName.contains("Primary")) {
						loggedInUser = "PTTaskCompleted";
					} else {
						loggedInUser = "STTaskCompleted";
					}

					resourceLocator.getSBMService().checkAndSendNotification(crn, loggedInUser, session, taskName);
					this.logger.debug("ws completed Successfully..");
					this.logger.info("Research for team " + teamName + " has been completed for crn " + crn);
					return modelAndView;
				} else {
					this.logger.debug("The workstep" + workItemName + "doen't exist for user " + session.getUser()
							+ " . Thus redirecting it to the error page");
					return new ModelAndView(this.taskErrorView);
				}
			}
		} catch (NullPointerException var28) {
			return AtlasUtils.getExceptionView(this.logger, var28);
		} catch (CMSException var29) {
			return AtlasUtils.getExceptionView(this.logger, var29);
		}
	}

	public ModelAndView completeTaskBIResearch(HttpServletRequest request, HttpServletResponse response) {
		String[] temp = (String[]) null;
		this.logger.debug("in completeBIResearchTask..");
		request.getSession().setAttribute(this.PARENT, "yes");
		ModelAndView modelAndView = new ModelAndView(this.successCompleteView);

		try {
			if (!this.checkISISPing(request)) {
				return new ModelAndView(this.isisPingFailView);
			} else if (this.checkForCaseStatus(request)) {
				return new ModelAndView(this.errorView);
			} else {
				String workItemName = request.getParameter("workItem");
				if (workItemName != null) {
					temp = workItemName.split("::");
				}

				long pid = Long.parseLong(temp[0].split("#")[1]);
				this.logger.debug("In research task. PID is " + pid);
				ResourceLocator resourceLocator = ResourceLocator.self();
				Session session = SBMUtils.getSession(request);
				if (resourceLocator.getSBMService().wiExistsForUser(workItemName, session, session.getUser())) {
					this.logger.debug("The workstep" + workItemName + "exists for user " + session.getUser());
					String cycleName = (String) resourceLocator.getSBMService().getDataslotValue(pid, "ProcessCycle",
							session);
					String teamType = (String) resourceLocator.getSBMService().getDataslotValue(pid,
							"BIVendorTaskTypeList", session);
					this.logger.debug("Team is " + teamType + " and cycle name is " + cycleName);
					TimeTrackerVO timeTrackerVO = new TimeTrackerVO();
					String crn = (String) resourceLocator.getSBMService().getDataslotValue(pid, "CRN", session);
					timeTrackerVO.setCrn(crn);
					timeTrackerVO.setUserName(session.getUser());
					timeTrackerVO.setUpdatedBy(session.getUser());
					if (teamType.contains("Vendor")) {
						timeTrackerVO.setTaskName("Vendor Research Task");
					} else {
						timeTrackerVO.setTaskName("BI Research Task");
					}

					timeTrackerVO.setTaskPid(String.valueOf(pid));
					ResourceLocator.self().getTimeTrackerService().stopTimeTracker(timeTrackerVO);
					this.logger.debug(
							" tracker attribute in session :: " + request.getSession().getAttribute("TrackerOn"));
					request.getSession().removeAttribute("TrackerOn");
					if (request.getParameter("CFinal") != null && !"".equals(request.getParameter("CFinal"))) {
						HashMap<String, Object> dsMap = new HashMap();
						String riskData = request.getParameter(this.RISKDATA);
						this.logger.debug("riskData ==>" + riskData);
						List<RiskProfileVO> riskProfileList = new ArrayList();
						String taskName = "";
						if (temp.length >= 2) {
							taskName = temp[1];
						}

						String loggedInUser = session.getUser();
						if (riskData.trim().length() > 0) {
							riskProfileList = this.createRiskDataList(riskData, crn, loggedInUser);
						}

						this.logger.debug("riskDatalist size  ==>" + ((List) riskProfileList).size());
						String riskHBDData = request.getParameter(this.RISK_HBD_DATA);
						this.logger.debug("riskHBDData ==>" + riskHBDData);
						List<RiskProfileVO> riskProfileListWithHBD = new ArrayList();
						if (riskHBDData.trim().length() > 0) {
							new ArrayList();
							List<RiskProfileVO> riskForProfileIDList = this.getProfileList(riskHBDData, crn,
									loggedInUser);
							long riskProfileId = resourceLocator.getCaseDetailService()
									.fetchProfileId(riskForProfileIDList);
							riskProfileListWithHBD = this.createRiskDataWithHBDList(riskHBDData, crn, loggedInUser,
									riskProfileId);
						}

						this.logger.debug("Risk HBD list size:==>" + ((List) riskProfileListWithHBD).size());
						String subIndustryData = request.getParameter(this.SUB_INDUSTRY_DATA);
						this.logger.debug("SubIndustry" + subIndustryData);
						List<RiskProfileVO> subIndusList = new ArrayList();
						if (subIndustryData.trim().length() > 0) {
							subIndusList = this.createIndustryCodeWithSubjectId(subIndustryData, crn, loggedInUser);
						}

						this.logger.debug("sub list size::" + ((List) subIndusList).size());
						String riskAggregationData = request.getParameter(this.RISK_AGGR_DATA);
						this.logger.debug("riskAggregationData::" + riskAggregationData);
						List<RiskAggregationVO> riskAggregationList = new ArrayList();
						if (riskAggregationData.trim().length() > 0) {
							riskAggregationList = this.createRiskAggregationData(riskAggregationData, loggedInUser);
						}

						this.logger.debug("risk aggregation list size::" + ((List) riskAggregationList).size());
						String totalRiskAggregationData = request.getParameter(this.TOTAL_RISK_AGGR_DATA);
						this.logger.debug("totalRiskAggregationData" + totalRiskAggregationData);
						List<TotalRiskAggregationVO> totalRiskAggregationList = new ArrayList();
						if (totalRiskAggregationData.trim().length() > 0) {
							totalRiskAggregationList = this.createTotalRiskAggregationData(totalRiskAggregationData,
									loggedInUser);
						}

						this.logger.debug(
								"totalRiskAggregation list size is::" + ((List) totalRiskAggregationList).size());
						List<SubjectLevelAggregation> subjectAggregationList = new ArrayList();
						if (crn != null && crn.trim().length() > 0) {
							int totalRiskAggrId = resourceLocator.getCaseDetailService().fetchTotalAggrId(crn);
							String eachSubjectAggregationData = request
									.getParameter("eachSubjectAggregationDataObject");
							this.logger.debug("eachSubjectAggregationData.." + eachSubjectAggregationData);
							if (totalRiskAggrId > 0 && eachSubjectAggregationData != null
									&& eachSubjectAggregationData.trim().length() > 0) {
								subjectAggregationList = this.createSubjectAggregationData(eachSubjectAggregationData,
										crn, loggedInUser, totalRiskAggrId);
							}
						}

						resourceLocator.getCaseDetailService().saveSavvionCaseInformation(
								this.settingCaseDetails(request, dsMap), SBMUtils.getSession(request), dsMap,
								(List) riskProfileList, (List) riskProfileListWithHBD, (List) subIndusList,
								(List) riskAggregationList, taskName, (List) totalRiskAggregationList);
						if (((List) subjectAggregationList).size() > 0) {
							resourceLocator.getCaseDetailService()
									.saveSubjectAggregation((List) subjectAggregationList);
						}
					}

					int rowsUpdated = resourceLocator.getTaskService().updateRESstauts(session.getUser(), crn);
					this.logger.debug("Total rows updated " + rowsUpdated);
					long teamId = Long.parseLong(teamType.split("#")[1]);
					this.logger.debug("team id is " + teamId);
					int updatedRowsForMIS = resourceLocator.getTaskService().updateDateForMIS(teamId, cycleName,
							session.getUser());
					this.logger.debug("updated row for MIS is " + updatedRowsForMIS);
					cycleName = (String) resourceLocator.getSBMService().getDataslotValue(pid, "BITeamCycleName",
							session);
					resourceLocator.getSBMService().completeTask(session, new HashMap(), pid, temp[1]);
					this.logger.debug("cycle name is " + cycleName);
					String notificationType = "";
					notificationType = "STTaskCompleted";
					resourceLocator.getSBMService().checkAndSendNotification(crn, notificationType, session, cycleName);
					this.logger.debug("ws completed Successfully..");
					this.logger.info("Research for team " + teamType + " has been completed for crn " + crn);
					return modelAndView;
				} else {
					this.logger.debug("The workstep" + workItemName + "doen't exist for user " + session.getUser()
							+ " . Thus redirecting it to the error page");
					return new ModelAndView(this.taskErrorView);
				}
			}
		} catch (NullPointerException var30) {
			return AtlasUtils.getExceptionView(this.logger, var30);
		} catch (CMSException var31) {
			return AtlasUtils.getExceptionView(this.logger, var31);
		}
	}

	public ModelAndView saveCaseTask(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(this.successView);
		String[] temp = (String[]) null;
		this.logger.debug("in saveTask..");
		ResourceLocator resourceLocator = ResourceLocator.self();

		try {
			if (!this.checkISISPing(request)) {
				return new ModelAndView(this.isisPingFailView);
			} else if (this.checkForCaseStatus(request)) {
				return new ModelAndView(this.errorView);
			} else {
				String workItemName = request.getParameter("workItem");
				if (workItemName != null) {
					temp = workItemName.split("::");
				}

				HashMap<String, Object> dsMap = new HashMap();
				if (request.getParameter("CFinal") != null && !"".equals(request.getParameter("CFinal"))) {
					this.logger.debug("inside the CaseDetail API========**");
					resourceLocator.getCaseDetailService().saveSavvionCaseInformation(
							this.settingCaseDetails(request, dsMap), SBMUtils.getSession(request), dsMap);
					this.logger.debug("saveSavvionCaseInformation=========successfully done----**");
				}

				dsMap = new HashMap();
				if (request.getParameter("isAutoOA") != null) {
					dsMap.put("isAutoOA", request.getParameter("isAutoOA"));
				}

				resourceLocator.getSBMService().saveTask(Long.parseLong(temp[0].split("#")[1]), temp[1],
						SBMUtils.getSession(request), dsMap);
				this.logger.debug("ws saved Sucessfully..");
				return modelAndView;
			}
		} catch (NullPointerException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView completeCaseTask(HttpServletRequest request, HttpServletResponse response) {
		String[] temp = (String[]) null;
		request.getSession().setAttribute(this.PARENT, "yes");
		this.logger.debug("in completeCaseTask..");
		ModelAndView modelAndView = new ModelAndView(this.successCompleteView);

		try {
			if (!this.checkISISPing(request)) {
				return new ModelAndView(this.isisPingFailView);
			} else if (this.checkForCaseStatus(request)) {
				return new ModelAndView(this.errorView);
			} else {
				String workItemName = request.getParameter("workItem");
				Session session = SBMUtils.getSession(request);
				if (workItemName != null) {
					temp = workItemName.split("::");
				}

				HashMap<String, Object> dataSlot = (HashMap) SBMUtils.setDS(request);
				this.logger.debug("IS Auto OA value is  " + (String) dataSlot.get("isAutoOA"));
				if ("true".equalsIgnoreCase((String) dataSlot.get("isAutoOA"))) {
					this.logger.debug("calling saveAOADetailsForCase");
					this.resourceLocator.getOfficeAssignmentService().saveAOADetailsForCase(request);
					this.logger.debug("returned from saveAOADetailsForCase and now completing case creation task.");
				} else {
					this.logger.debug("case created with manual office assignment");
				}

				HashMap<String, Object> dsMap = new HashMap();
				if (request.getParameter("CFinal") != null && !"".equals(request.getParameter("CFinal"))) {
					this.resourceLocator.getCaseDetailService()
							.saveSavvionCaseInformation(this.settingCaseDetails(request, dsMap), session, dsMap);
				}

				dsMap = new HashMap();
				if (request.getParameter("isAutoOA") != null) {
					dsMap.put("isAutoOA", request.getParameter("isAutoOA"));
				}

				long pid = Long.parseLong(temp[0].split("#")[1]);
				String crn = (String) this.resourceLocator.getSBMService().getDataslotValue(pid, "CRN", session);
				this.resourceLocator.getSBMService().completeTask(session, dsMap, pid, temp[1]);
				String notificationType = "";
				notificationType = "financeTaskGenearted";
				this.resourceLocator.getSBMService().checkAndSendNotification(crn, notificationType, session, "");
				this.createMailBody(crn);
				this.logger.debug("ws completed Successfully..");
				this.logger.info("The completing case creation ws has been completed successfully for crn " + crn);
				return modelAndView;
			}
		} catch (NullPointerException var13) {
			return AtlasUtils.getExceptionView(this.logger, var13);
		} catch (CMSException var14) {
			return AtlasUtils.getExceptionView(this.logger, var14);
		}
	}

	private void createMailBody(String crn) throws CMSException {
		this.resourceLocator.getCaseDetailService().getMailBody(crn, "0", "1", "0");
	}

	public ModelAndView completeTaskForOffice(HttpServletRequest request, HttpServletResponse response) {
		String[] temp = (String[]) null;
		this.logger.debug("in completeTaskForOffice..");
		request.getSession().setAttribute(this.PARENT, "yes");
		ModelAndView modelAndView = new ModelAndView(this.successCompleteView);

		try {
			if (!this.checkISISPing(request)) {
				return new ModelAndView(this.isisPingFailView);
			} else if (this.checkForCaseStatus(request)) {
				return new ModelAndView(this.errorView);
			} else {
				String workItemName = request.getParameter("workItem");
				if (workItemName != null) {
					temp = workItemName.split("::");
				}

				ResourceLocator resourceLocator = ResourceLocator.self();
				HashMap<String, Object> dataSlot = new HashMap();
				Session session = SBMUtils.getSession(request);
				if (resourceLocator.getSBMService().wiExistsForUser(workItemName, session, session.getUser())) {
					this.logger.debug("The workstep" + workItemName + "exists for user " + session.getUser());
					this.logger.debug("before flow ctrl");
					long pid = Long.parseLong(temp[0].split("#")[1]);
					TimeTrackerVO timeTrackerVO = new TimeTrackerVO();
					timeTrackerVO
							.setCrn((String) resourceLocator.getSBMService().getDataslotValue(pid, "CRN", session));
					timeTrackerVO.setUserName(session.getUser());
					timeTrackerVO.setUpdatedBy(session.getUser());
					timeTrackerVO.setTaskName("Office Assignment Task");
					timeTrackerVO.setTaskPid(String.valueOf(pid));
					ResourceLocator.self().getTimeTrackerService().stopTimeTracker(timeTrackerVO);
					request.getSession().removeAttribute("TrackerOn");
					this.logger.debug("After flow ctrl");
					resourceLocator.getSBMService().completeTask(SBMUtils.getSession(request), dataSlot,
							Long.parseLong(temp[0].split("#")[1]), temp[1]);
					this.logger.debug("ws completed Successfully..");
					return modelAndView;
				} else {
					this.logger.debug("The workstep" + workItemName + "doen't exist for user " + session.getUser()
							+ " . Thus redirecting it to the error page");
					return new ModelAndView(this.taskErrorView);
				}
			}
		} catch (NullPointerException var12) {
			return AtlasUtils.getExceptionView(this.logger, var12);
		} catch (CMSException var13) {
			return AtlasUtils.getExceptionView(this.logger, var13);
		}
	}

	public ModelAndView otherInfo(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in otherInfo");
		ModelAndView displayView = new ModelAndView("otherInfo");

		try {
			HttpSession httpSession = request.getSession();
			UserDetailsBean userDetailsBean = (UserDetailsBean) httpSession.getAttribute("userDetailsBean");
			displayView.addObject("crn", request.getParameter("crn"));
			String crn = request.getParameter("crn");
			List<TeamDetails> teamList = ResourceLocator.self().getTeamAssignmentService().getTeamNamesForCRN(crn);
			CaseDetails caseDetails = ResourceLocator.self().getCaseDetailService().getCaseInfo(crn);
			if (caseDetails != null) {
				displayView.addObject("reportType", caseDetails.getReportType());
				displayView.addObject("clientName", caseDetails.getClientName());
			}

			Iterator<TeamDetails> iterator = teamList.iterator();

			String teamIds;
			TeamDetails teamDetails;
			for (teamIds = ""; iterator.hasNext(); teamIds = teamIds + teamDetails.getTeamId() + ",") {
				teamDetails = (TeamDetails) iterator.next();
			}

			List<TeamDetails> teamCompleteDetails = ResourceLocator.self().getTeamAssignmentService()
					.getCaseTeamDetails(crn);
			boolean isReviewer = false;
			Iterator<TeamDetails> iterator2 = teamCompleteDetails.iterator();
			String loginUserId = userDetailsBean.getLoginUserId();
			this.logger.debug("loginUserId " + loginUserId);

			while (iterator2.hasNext()) {
				TeamDetails teamDetail = (TeamDetails) iterator2.next();
				this.logger.debug("Reviewer1 " + teamDetail.getReviewer1() + "Reviewer2 " + teamDetail.getReviewer2()
						+ " Reviewer3 " + teamDetail.getReviewer3());
				if (loginUserId.equals(teamDetail.getReviewer1())) {
					isReviewer = true;
					break;
				}

				if (loginUserId.equals(teamDetail.getReviewer2())) {
					isReviewer = true;
					break;
				}

				if (loginUserId.equals(teamDetail.getReviewer3())) {
					isReviewer = true;
					break;
				}
			}

			displayView.addObject("isReviewer", isReviewer);
			String teamCycleName = "";
			if (caseDetails != null) {
				Long pid = Long.parseLong(caseDetails.getPid());
				Session session = ResourceLocator.self().getSBMService().getSession(userDetailsBean.getLoginUserId());
				this.logger.debug("userDetailsBean.getLoginUserId() " + userDetailsBean.getLoginUserId());
				this.logger.debug("caseDetails.getPid() " + caseDetails.getPid());
				if (userDetailsBean.getLoginUserId() != null && caseDetails.getPid() != null
						&& !ResourceLocator.self().getSBMService().isTaskCompleted(pid, session)) {
					teamCycleName = (String) ResourceLocator.self().getSBMService()
							.getDataslotValue(Long.parseLong(caseDetails.getPid()), "ProcessCycle", session);
				}
			}

			this.logger.debug("teamCycleName " + teamCycleName);
			displayView.addObject("teamCycleName", teamCycleName);
			this.logger.debug("teamIds " + teamIds);
			displayView.addObject("team_ID", teamIds);
			return displayView;
		} catch (CMSException var18) {
			return AtlasUtils.getExceptionView(this.logger, var18);
		} catch (Exception var19) {
			return AtlasUtils.getExceptionView(this.logger, var19);
		}
	}

	public ModelAndView goToMyTask(HttpServletRequest request, HttpServletResponse response)
			throws CMSException, IOException {
		this.logger.debug("Entring TaskManagermentController:goToMyTask");
		HttpSession httpSession = request.getSession();
		String headerInfo = request.getHeader("User-agent").replaceAll("Mozilla/5.0", "").replaceAll("Gecko/20100101",
				"");
		UserDetailsBean userDetailsBean = (UserDetailsBean) httpSession.getAttribute("userDetailsBean");
		String loginUserId = "";

		try {
			loginUserId = userDetailsBean.getLoginUserId();
		} catch (Exception var11) {
			response.sendRedirect("/sbm/bpmportal/login.jsp");
		}

		Map<String, String> map = new HashMap();
		map.put("unlocked_by", "");
		map.put("userID", loginUserId);
		map.put("isReset", "true");
		ResourceLocator.self().getUserService().updateUserLoginAttempt(map);
		UserMasterVO userMasterVO = ResourceLocator.self().getUserService().getUserPassExpiryDetails(loginUserId);
		ModelAndView modelAndView;
		if (userMasterVO.getIsNewUser() == 1) {
			this.logger.debug("New User Login Redirecting to reset password");
			modelAndView = new ModelAndView("reset_password_profile");
			modelAndView.addObject("REDIRECT_MESSAGE", "New User");
			return modelAndView;
		} else if (userMasterVO.getIsPasswordExpire().equalsIgnoreCase("0")) {
			this.logger.debug("Password Expired Redirecting to reset password");
			modelAndView = new ModelAndView("reset_password_profile");
			modelAndView.addObject("REDIRECT_MESSAGE", "Password Expire");
			return modelAndView;
		} else if (userMasterVO.getIsNewUser() == 2) {
			this.logger.debug("Force Password Redirecting to reset password");
			modelAndView = new ModelAndView("reset_password_profile");
			modelAndView.addObject("REDIRECT_MESSAGE", "Force Password");
			return modelAndView;
		} else if (userDetailsBean != null && userDetailsBean.getRoleList().size() > 0
				&& userDetailsBean.getRoleList().contains("R0")) {
			this.logger.debug("Monitoring the system up and running status");
			modelAndView = new ModelAndView("atlas_monitor");
			return modelAndView;
		} else {
			try {
				if (this.resourceLocator.getTaskService().isHeaderRecorded(loginUserId) > 0) {
					this.resourceLocator.getTaskService().updateHeaderInfo(loginUserId, headerInfo);
				} else {
					this.resourceLocator.getTaskService().insertHeaderInfo(loginUserId, headerInfo);
				}
			} catch (CMSException var10) {
				this.logger.debug("Exception occured in saving/updating header info ");
				var10.printStackTrace();
			}

			modelAndView = new ModelAndView("mytask_custom");
			return modelAndView;
		}
	}

	public ModelAndView goToIncomingTask(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("myIncoming_custom");
		return modelAndView;
	}

	public ModelAndView goToCaseStatus(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("case_status");
		modelAndView.addObject("crn", request.getParameter("crn"));
		return modelAndView;
	}

	public ModelAndView goToMyTeamTask(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("my_team_task");
		return modelAndView;
	}

	public ModelAndView goToUnCompletedCase(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("incompleteCases");
		return modelAndView;
	}

	public ModelAndView goToOnHoldCases(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("onholdCases_custom");
		return modelAndView;
	}

	private CaseDetails settingCaseDetails(HttpServletRequest request, HashMap<String, Object> dsMap)
			throws CMSException {
		CaseDetails caseDetails = new CaseDetails();
		UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
		String userName = userBean.getUserName();
		this.logger.debug("request parameters for case details " + request.getParameterMap());
		this.logger.debug("========** userName is --" + userName);

		try {
			caseDetails.setUpdatedBy(userName);
			if (request.getParameter("crnVal") != null) {
				caseDetails.setCrn(request.getParameter("crnVal"));
				dsMap.put("CRN", request.getParameter("crnVal"));
			}

			if (request.getParameter("taskPerformer") != null) {
				dsMap.put("taskPerformer", request.getParameter("taskPerformer"));
			}

			if (request.getParameter("CaseManager") != null) {
				caseDetails.setCaseMgrId(request.getParameter("CaseManager"));
				dsMap.put("CaseManager", request.getParameter("CaseManager"));
			}

			this.logger.debug("request.getParameter() " + request.getParameter("caseStatusName"));
			if (request.getParameter("caseStatusName") != null) {
				caseDetails.setCaseStatus(request.getParameter("caseStatusName"));
				dsMap.put("CaseStatus", request.getParameter("caseStatusName"));
			}

			if (request.getParameter("BranchOffice") != null) {
				caseDetails.setOfficeId(request.getParameter("BranchOffice"));
				dsMap.put("BranchOffice", request.getParameter("BranchOffice"));
			}

			if (request.getParameter("ClientReference") != null) {
				dsMap.put("ClientReference", request.getParameter("ClientReference"));
				caseDetails.setClientRef(request.getParameter("ClientReference"));
			}

			if (request.getParameter("CaseInformation") != null) {
				CaseInfo caseInfo = new CaseInfo();
				caseInfo.setCaseInfoBlock(request.getParameter("CaseInformation"));
				HashMap<String, CaseInfo> map = new HashMap();
				map.put("CaseInfoBlock", caseInfo);
				dsMap.put("CaseInfoBlock", map);
				caseDetails.setCaseInfo(request.getParameter("CaseInformation"));
			}

			if (request.getParameter("caseStatusId") != null) {
				caseDetails.setCaseStatusId(Integer.parseInt(request.getParameter("caseStatusId")));
			}

			String expressCase;
			if (request.getParameter("CInterim2") != null && !"".equals(request.getParameter("CInterim2"))) {
				expressCase = request.getParameter("CInterim2");
				dsMap.put("CInterim2", request.getParameter("CInterim2"));
				caseDetails.setcInterim2(new Date(this.sdf.parse(expressCase).getTime()));
			}

			if (request.getParameter("CInterim1") != null && !"".equals(request.getParameter("CInterim1"))) {
				expressCase = request.getParameter("CInterim1");
				dsMap.put("CInterim1", request.getParameter("CInterim1"));
				caseDetails.setcInterim1(new Date(this.sdf.parse(expressCase).getTime()));
			}

			if (request.getParameter("CFinal") != null && !"".equals(request.getParameter("CFinal"))) {
				expressCase = request.getParameter("CFinal");
				dsMap.put("CFinal", request.getParameter("CFinal"));
				caseDetails.setFinalDueDate(new Date(this.sdf.parse(expressCase).getTime()));
			}

			if (request.getParameter("RInterim1") != null && !"".equals(request.getParameter("RInterim1"))) {
				expressCase = request.getParameter("RInterim1");
				dsMap.put("RInterim1", request.getParameter("RInterim1"));
				caseDetails.setrInterim1(new Date(this.sdf.parse(expressCase).getTime()));
			}

			if (request.getParameter("RInterim2") != null && !"".equals(request.getParameter("RInterim2"))) {
				expressCase = request.getParameter("RInterim2");
				dsMap.put("RInterim2", request.getParameter("RInterim2"));
				caseDetails.setrInterim2(new Date(this.sdf.parse(expressCase).getTime()));
			}

			if (request.getParameter("RFinal") != null && !"".equals(request.getParameter("RFinal"))) {
				expressCase = request.getParameter("RFinal");
				dsMap.put("RFinal", request.getParameter("RFinal"));
				caseDetails.setFinalRDueDate(new Date(this.sdf.parse(expressCase).getTime()));
			}

			if (request.getParameter("isCancelled") != null && !"".equals(request.getParameter("isCancelled"))) {
				dsMap.put("isCancelled", request.getParameter("isCancelled"));
			}

			if (request.getParameter("isCSApproved") != null && !"".equals(request.getParameter("isCSApproved"))) {
				dsMap.put("isCSApproved", request.getParameter("isCSApproved"));
			}

			if (request.getParameter("isFinalReviewApproved") != null
					&& !"".equals(request.getParameter("isFinalReviewApproved"))) {
				dsMap.put("isFinalReviewApproved", request.getParameter("isFinalReviewApproved"));
			}

			if (request.getParameter("isPullback") != null && !"".equals(request.getParameter("isPullback"))) {
				dsMap.put("isPullback", request.getParameter("isPullback"));
			}

			if (request.getParameter("isFinalCycle") != null && !"".equals(request.getParameter("isFinalCycle"))) {
				dsMap.put("isFinalCycle", request.getParameter("isFinalCycle"));
			}

			if (request.getParameter("MainAnalyst") != null && !"".equals(request.getParameter("MainAnalyst"))) {
				dsMap.put("MainAnalyst", request.getParameter("MainAnalyst"));
			}

			if (request.getParameter("ProcessCycle") != null && !"".equals(request.getParameter("ProcessCycle"))) {
				dsMap.put("ProcessCycle", request.getParameter("ProcessCycle"));
			}

			if (request.getParameter("isRejected") != null && !"".equals(request.getParameter("isRejected"))) {
				dsMap.put("isRejected", request.getParameter("isRejected"));
			}

			if (request.getParameter("ClientName") != null && !"".equals(request.getParameter("ClientName"))) {
				dsMap.put("ClientName", request.getParameter("ClientName"));
			}

			if (request.getParameter("ClientFeedback") != null && !"".equals(request.getParameter("ClientFeedback"))) {
				dsMap.put("ClientFeedback", request.getParameter("ClientFeedback"));
			}

			if (request.getParameter("ClientSentDate") != null && !"".equals(request.getParameter("ClientSentDate"))) {
				dsMap.put("ClientSentDate", request.getParameter("ClientSentDate"));
			}

			if (request.getParameter("expCase") != null) {
				expressCase = request.getParameter("expCase");
				this.logger.debug(" expressCase " + expressCase);
				if (!expressCase.equalsIgnoreCase("on") && !expressCase.equalsIgnoreCase("true")) {
					caseDetails.setExpressCase(0);
					dsMap.put("ExpressCase", false);
				} else {
					caseDetails.setExpressCase(1);
					dsMap.put("ExpressCase", true);
				}
			} else {
				caseDetails.setExpressCase(0);
				dsMap.put("ExpressCase", false);
			}

			if (request.getSession().getAttribute("loginLevel") != null) {
				caseDetails.setCaseHistoryPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				caseDetails.setCaseHistoryPerformer(caseDetails.getUpdatedBy());
			}

			return caseDetails;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private boolean checkForCaseStatus(HttpServletRequest request) throws CMSException {
		boolean status = false;
		String crn = request.getParameter("crnVal");
		this.logger.debug("CRN is " + crn);
		if (crn != null && !"null".equalsIgnoreCase(crn)) {
			long pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(crn);
			this.logger.debug("pid is " + pid);
			String caseStatus = (String) ResourceLocator.self().getSBMService().getDataslotValue(pid, "CaseStatus",
					SBMUtils.getSession(request));
			this.logger.debug("Case status is " + caseStatus);
			if (caseStatus == null || "null".equalsIgnoreCase(caseStatus) || "Cancelled".equalsIgnoreCase(caseStatus)
					|| "On Hold".equalsIgnoreCase(caseStatus)) {
				status = true;
				this.logger.debug("inside condition for cancelled or on-hold tasks");
			}
		}

		return status;
	}

	private boolean checkISISPing(HttpServletRequest request) throws CMSException {
		boolean status = true;
		String crn = request.getParameter("crnVal");
		CaseDetails caseDetails = ResourceLocator.self().getCaseDetailService().getCaseInfo(crn);
		if (crn != null && !"null".equalsIgnoreCase(crn)
				&& request.getParameter(this.CASE_STATUS_NAME) == this.CANCLLED_STATUS
				&& caseDetails.getIsISIS() == 1) {
			status = ResourceLocator.self().getAtlasWebServiceClient().pingISIS();
			System.out.println("Status is" + status);
		}

		return status;
	}

	public ModelAndView saveTaskRiskIndustryData(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView(this.subjectRiskScreen);
		String crn = null;
		boolean processFlag = true;
		this.logger.debug("in saveTaskRiskIndustryData..");
		ResourceLocator resourceLocator = ResourceLocator.self();

		try {
			if (!this.checkISISPing(request)) {
				return new ModelAndView(this.isisPingFailView);
			} else if (this.checkForCaseStatus(request)) {
				return new ModelAndView(this.errorView);
			} else {
				String workItemName = request.getParameter("workItem");
				crn = request.getParameter("crn");
				this.logger.debug("saveTaskRiskIndustryData::CRN--" + crn);
				Session session = SBMUtils.getSession(request);
				HashMap<String, Object> dsMap = new HashMap();
				String taskName = "";
				String loggedInUser = session.getUser();
				String riskData;
				String riskHBDData;
				String subIndustryData;
				String riskAggregationData;
				String totalRiskAggregationData;
				String eachSubjectAggregationData;
				Object subjectAggregationList;
				if (resourceLocator.getTaskService().getISISStatus(crn) == 1) {
					long pid = resourceLocator.getTaskService().getPID(crn);
					ClientCaseStatusVO clientCaseStatusVO = resourceLocator.getTaskService().getFileForISIS(pid);
					clientCaseStatusVO.setCRN(crn);
					clientCaseStatusVO.setExpressCase(
							(Boolean) resourceLocator.getSBMService().getDataslotValue(pid, "ExpressCase", session)
									? "True"
									: "False");
					clientCaseStatusVO.setStatus("CMP");
					clientCaseStatusVO.setUpdateType("Case");
					ClientCaseStatusIndustryVO[] industryArray = ResourceLocator.self().getSubjectService()
							.getSubjectsIndustryForISIS(crn);
					ClientCaseStatusRiskVO[] riskArray = ResourceLocator.self().getSubjectService()
							.getSubjectsRisksForISIS(crn);
					clientCaseStatusVO.setSubjectIndustry(industryArray);
					clientCaseStatusVO.setSubjectRisk(riskArray);
					riskData = request.getParameter(this.RISKDATA);
					this.logger.debug("riskData ==>" + riskData);
					subjectAggregationList = new ArrayList();
					if (riskData.trim().length() > 0) {
						subjectAggregationList = this.createRiskDataList(riskData, crn, loggedInUser);
					}

					this.logger.debug("riskDatalist size  ==>" + ((List) subjectAggregationList).size());
					riskHBDData = request.getParameter(this.RISK_HBD_DATA);
					this.logger.debug("riskHBDData ==>" + riskHBDData);
					List<RiskProfileVO> riskProfileListWithHBD = new ArrayList();
					if (riskHBDData.trim().length() > 0) {
						new ArrayList();
						List<RiskProfileVO> riskForProfileIDList = this.getProfileList(riskHBDData, crn, loggedInUser);
						long riskProfileId = resourceLocator.getCaseDetailService()
								.fetchProfileId(riskForProfileIDList);
						riskProfileListWithHBD = this.createRiskDataWithHBDList(riskHBDData, crn, loggedInUser,
								riskProfileId);
					}

					this.logger.debug("Risk HBD list size:==>" + ((List) riskProfileListWithHBD).size());
					subIndustryData = request.getParameter(this.SUB_INDUSTRY_DATA);
					this.logger.debug("SubIndustry" + subIndustryData);
					List<RiskProfileVO> subIndusList = new ArrayList();
					if (subIndustryData.trim().length() > 0) {
						subIndusList = this.createIndustryCodeWithSubjectId(subIndustryData, crn, loggedInUser);
					}

					this.logger.debug("sub list size::" + ((List) subIndusList).size());
					riskAggregationData = request.getParameter(this.RISK_AGGR_DATA);
					this.logger.debug("riskAggregationData::" + riskAggregationData);
					List<RiskAggregationVO> riskAggregationList = new ArrayList();
					if (riskAggregationData.trim().length() > 0) {
						riskAggregationList = this.createRiskAggregationData(riskAggregationData, loggedInUser);
					}

					this.logger.debug("risk aggregation list size::" + ((List) riskAggregationList).size());
					totalRiskAggregationData = request.getParameter(this.TOTAL_RISK_AGGR_DATA);
					this.logger.debug("totalRiskAggregationData" + totalRiskAggregationData);
					List<TotalRiskAggregationVO> totalRiskAggregationList = new ArrayList();
					if (totalRiskAggregationData.trim().length() > 0) {
						totalRiskAggregationList = this.createTotalRiskAggregationData(totalRiskAggregationData,
								loggedInUser);
					}

					this.logger.debug("totalRiskAggregation list size is::" + ((List) totalRiskAggregationList).size());
					int totalRiskAggrId = resourceLocator.getCaseDetailService().fetchTotalAggrId(crn);
					eachSubjectAggregationData = request.getParameter("eachSubjectAggregationDataObject");
					this.logger.debug("eachSubjectAggregationData.." + eachSubjectAggregationData);
					List<SubjectLevelAggregation> subjectAggregationList = new ArrayList();
					if (totalRiskAggrId > 0 && eachSubjectAggregationData != null
							&& eachSubjectAggregationData.trim().length() > 0) {
						subjectAggregationList = this.createSubjectAggregationData(eachSubjectAggregationData, crn,
								loggedInUser, totalRiskAggrId);
					}

					this.logger.debug("<Parent>");
					this.logger.debug("<CRN>" + clientCaseStatusVO.getCRN() + "</CRN>");
					this.logger.debug("<Status>" + clientCaseStatusVO.getStatus() + "</Status>");
					this.logger.debug("<FileName>" + clientCaseStatusVO.getFileName() + "</FileName>");
					this.logger.debug("<flVersion>" + clientCaseStatusVO.getVersion() + "</flVersion>");
					this.logger.debug("<ExpressCase>" + clientCaseStatusVO.getExpressCase() + "</ExpressCase>");
					this.logger.debug("<UpdateType>" + clientCaseStatusVO.getUpdateType() + "</UpdateType>");
					this.logger.debug("<SubjectIndustries>");
					if (industryArray != null && industryArray.length > 0) {
						for (int i = 0; i < industryArray.length; ++i) {
							this.logger.debug(
									"<ISISSubjectID>" + industryArray[i].getISISSubjectID() + "</ISISSubjectID>");
							this.logger.debug(
									"<AtlasSubjectID>" + industryArray[i].getAtlasSubjectID() + "</AtlasSubjectID>");
							this.logger
									.debug("<IndustryName>" + industryArray[i].getIndustryName() + "</IndustryName>");
							this.logger.debug("<IndustryID>" + industryArray[i].getIndustryID() + "</IndustryID>");
						}
					}

					this.logger.debug("</SubjectIndustries>");
					RiskProfile riskProfile = this.setRiskProfileISISVo(crn, (List) subjectAggregationList,
							(List) riskProfileListWithHBD, (List) subIndusList, (List) riskAggregationList,
							(List) totalRiskAggregationList, loggedInUser, (List) subjectAggregationList);
					clientCaseStatusVO.setOtherinformation(riskProfile);
					processFlag = resourceLocator.getAtlasWebServiceClient().updateStatus(clientCaseStatusVO);
				}

				if (processFlag) {
					riskData = request.getParameter(this.RISKDATA);
					this.logger.debug("riskData ==>" + riskData);
					List<RiskProfileVO> riskProfileList = new ArrayList();
					if (riskData.trim().length() > 0) {
						riskProfileList = this.createRiskDataList(riskData, crn, loggedInUser);
					}

					this.logger.debug("riskDatalist size  ==>" + ((List) riskProfileList).size());
					riskHBDData = request.getParameter(this.RISK_HBD_DATA);
					this.logger.debug("riskHBDData ==>" + riskHBDData);
					List<RiskProfileVO> riskProfileListWithHBD = new ArrayList();
					if (riskHBDData.trim().length() > 0) {
						new ArrayList();
						List<RiskProfileVO> riskForProfileIDList = this.getProfileList(riskHBDData, crn, loggedInUser);
						long riskProfileId = resourceLocator.getCaseDetailService()
								.fetchProfileId(riskForProfileIDList);
						riskProfileListWithHBD = this.createRiskDataWithHBDList(riskHBDData, crn, loggedInUser,
								riskProfileId);
					}

					this.logger.debug("Risk HBD list size:==>" + ((List) riskProfileListWithHBD).size());
					subIndustryData = request.getParameter(this.SUB_INDUSTRY_DATA);
					this.logger.debug("SubIndustry" + subIndustryData);
					List<RiskProfileVO> subIndusList = new ArrayList();
					if (subIndustryData.trim().length() > 0) {
						subIndusList = this.createIndustryCodeWithSubjectId(subIndustryData, crn, loggedInUser);
					}

					this.logger.debug("sub list size::" + ((List) subIndusList).size());
					riskAggregationData = request.getParameter(this.RISK_AGGR_DATA);
					this.logger.debug("riskAggregationData::" + riskAggregationData);
					List<RiskAggregationVO> riskAggregationList = new ArrayList();
					if (riskAggregationData.trim().length() > 0) {
						riskAggregationList = this.createRiskAggregationData(riskAggregationData, loggedInUser);
					}

					this.logger.debug("risk aggregation list size::" + ((List) riskAggregationList).size());
					totalRiskAggregationData = request.getParameter(this.TOTAL_RISK_AGGR_DATA);
					this.logger.debug("totalRiskAggregationData" + totalRiskAggregationData);
					List<TotalRiskAggregationVO> totalRiskAggregationList = new ArrayList();
					if (totalRiskAggregationData.trim().length() > 0) {
						totalRiskAggregationList = this.createTotalRiskAggregationData(totalRiskAggregationData,
								loggedInUser);
					}

					this.logger.debug("totalRiskAggregation list size is::" + ((List) totalRiskAggregationList).size());
					subjectAggregationList = new ArrayList();
					if (crn != null && crn.trim().length() > 0) {
						int totalRiskAggrId = resourceLocator.getCaseDetailService().fetchTotalAggrId(crn);
						eachSubjectAggregationData = request.getParameter("eachSubjectAggregationDataObject");
						this.logger.debug("eachSubjectAggregationData.." + eachSubjectAggregationData);
						if (totalRiskAggrId > 0 && eachSubjectAggregationData != null
								&& eachSubjectAggregationData.trim().length() > 0) {
							subjectAggregationList = this.createSubjectAggregationData(eachSubjectAggregationData, crn,
									loggedInUser, totalRiskAggrId);
						}
					}

					resourceLocator.getCaseDetailService().saveAfterCompletionSavvionCaseInformation(
							this.settingCaseDetails(request, dsMap), session, dsMap, (List) riskProfileList,
							(List) riskProfileListWithHBD, (List) subIndusList, (List) riskAggregationList,
							workItemName, (List) totalRiskAggregationList, (List) subjectAggregationList);
					this.logger.debug("SAVE TASK RISK INDUSTRY DATA crn--" + crn);
					this.logger.debug("ws saved Sucessfully..");
					modelAndView.addObject("crn", crn);
					modelAndView.addObject("activeTab", 0);
				}

				return modelAndView;
			}
		} catch (NullPointerException var31) {
			return AtlasUtils.getExceptionView(this.logger, var31);
		} catch (CMSException var32) {
			return AtlasUtils.getExceptionView(this.logger, var32);
		} catch (Exception var33) {
			return AtlasUtils.getExceptionView(this.logger, var33);
		}
	}

	public ModelAndView saveOrCompleteInvoice(HttpServletRequest request, HttpServletResponse response,
			AccountsVO accountsVO) {
		String[] temp = (String[]) null;
		this.logger.debug("cancelledCharges::" + request.getParameter("cancelledCharges"));
		this.logger.debug("in saveInvoice..");
		this.logger.debug("Action value is ::" + request.getParameter("action"));
		ResourceLocator resourceLocator = ResourceLocator.self();
		boolean submitFlag = false;

		ModelAndView modelAndView;
		try {
			this.logger.debug("client code in accountsVO::::" + accountsVO.getClientCode()
					+ " registerDate in accountsVO:::" + accountsVO.getRegDate());
			this.logger.debug("request capetownID" + request.getParameter("capetownID") + "request accountID"
					+ request.getParameter("accountID") + "request caseFee" + request.getParameter("caseFee")
					+ " request Credit " + request.getParameter("credit") + " request multipleYearBonus "
					+ request.getParameter("multipleYearBonus"));
			if (request.getParameter("reqFrom") == null) {
				submitFlag = true;
			}

			if (request.getParameter("workItem") != null) {
				temp = request.getParameter("workItem").split("::");
			} else {
				temp = ("CaseCreation#" + request.getParameter("pid") + "::" + "Invoicing").split("::");
			}

			this.logger.debug("temp " + temp[0]);
			accountsVO = this.getCompleteAccountVO(accountsVO, request);
			this.logger.debug("account Id:::" + accountsVO.getAccountID());
			String isSubjectLevelBudgetUpdated = request.getParameter("isSubjectLevelBudgetUpdated");
			if (isSubjectLevelBudgetUpdated.equalsIgnoreCase("true")) {
				accountsVO.setIsBudgetDueDateConfirmed(false);
			} else if (isSubjectLevelBudgetUpdated.equalsIgnoreCase("false")
					&& request.getParameter("isSubjLevelSubRptReq").equalsIgnoreCase("true")) {
				accountsVO.setBudgetConfirmedFlag("true");
			}

			if (accountsVO.getAccountID() != null && !accountsVO.getAccountID().isEmpty()
					&& !accountsVO.getAccountID().equals("0")) {
				this.logger.debug("Account Updation is called");
				resourceLocator.getInvoiceService().updateInvoiceDetails(accountsVO);
				if (request.getParameter("isSubjLevelSubRptReq").equalsIgnoreCase("true")) {
					String[] modifiedRecord = request.getParameterValues("modifiedRecords");
					String gridData = Arrays.toString(modifiedRecord);
					this.logger.debug("modifiedRecords::" + gridData);
					this.logger.debug("Account CRN:::" + accountsVO.getCrn());
					if (isSubjectLevelBudgetUpdated.equalsIgnoreCase("true")) {
						ResourceLocator.self().getSubjectService().updateSubjectBudget(gridData, accountsVO.getCrn());
					}
				}
			} else {
				this.logger.debug("Account insertion is called");
				resourceLocator.getInvoiceService().insertInvoiceDetails(accountsVO);
			}

			InvoiceVO invoiceVO = this.getCompleteInvoiceVO(request);
			if (request.getParameter("capetownID") != null && !request.getParameter("capetownID").isEmpty()
					&& !"0".equals(request.getParameter("capetownID"))) {
				this.logger.debug("updateCapeTownDetails  is called");
				invoiceVO.setCapetownID(Integer.parseInt(request.getParameter("capetownID")));
				resourceLocator.getInvoiceService().updateCapeTownDetails(invoiceVO);
				resourceLocator.getInvoiceService().updateClientDetails(invoiceVO);
			} else {
				this.logger.debug("Insert CapeTownDetails  is called");
				resourceLocator.getInvoiceService().insertCapeTownDetails(invoiceVO);
			}

			this.logger.debug("invoicing performer is >> " + request.getParameter(REQ_UPDATEBY));
			this.logger.debug("pid is >> " + request.getParameter("pid"));
			String[] activatedWSNames = ResourceLocator.self().getSBMService().getActivatedWSNames(
					SBMUtils.getSession(request), (long) Integer.parseInt(request.getParameter("pid")));
			HashMap<String, Object> dsValues = new HashMap();

			for (int i = 0; i < activatedWSNames.length; ++i) {
				this.logger.debug("activated workstep for case creation process is " + activatedWSNames[i]);
				if (activatedWSNames[i].equals("Invoicing Task")) {
					this.logger.debug("inside condition for Invoicing task workstep");
					dsValues.put("taskPerformer", request.getParameter(REQ_UPDATEBY));
					ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request),
							(long) Integer.parseInt(request.getParameter("pid")), dsValues);
					this.logger.debug("taskPerformer dataslot updated for invoicing...");
					break;
				}
			}

			this.logger.debug("action in saveOrCompleteInvoice method::" + request.getParameter("action"));
			if (submitFlag) {
				if ("save".equals(request.getParameter("action"))) {
					modelAndView = new ModelAndView(this.successView);
					resourceLocator.getSBMService().saveTask(Long.parseLong(temp[0].split("#")[1]), temp[1],
							SBMUtils.getSession(request), (HashMap) SBMUtils.setDS(request));
				} else {
					modelAndView = new ModelAndView(this.successCompleteView);
					resourceLocator.getSBMService().completeTask(SBMUtils.getSession(request), (HashMap) null,
							Long.parseLong(temp[0].split("#")[1]), temp[1]);
					TimeTrackerVO timeTrackerVO = new TimeTrackerVO();
					timeTrackerVO.setCrn(request.getParameter(REQ_CRN));
					timeTrackerVO.setUserName(request.getParameter(REQ_UPDATEBY));
					timeTrackerVO.setUpdatedBy(request.getParameter(REQ_UPDATEBY));
					timeTrackerVO.setTaskName("Invoicing Task");
					timeTrackerVO.setTaskPid(temp[0].split("#")[1]);
					ResourceLocator.self().getTimeTrackerService().stopTimeTracker(timeTrackerVO);
					request.getSession().removeAttribute("TrackerOn");
				}
			} else {
				modelAndView = new ModelAndView(
						"redirect:/bpmportal/atlas/accountSection.do?crn=" + request.getParameter(REQ_CRN));
			}

			CaseHistory caseHistory = new CaseHistory();
			caseHistory.setCRN(request.getParameter(REQ_CRN));
			caseHistory.setTaskName("Invoicing Task");
			if (request.getSession().getAttribute("loginLevel") != null) {
				caseHistory.setPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				caseHistory.setPerformer(request.getParameter(REQ_UPDATEBY));
			}

			caseHistory.setTaskStatus("In Progress");
			caseHistory.setPid(request.getParameter("pid"));
			ResourceLocator.self().getCaseHistoryService().setCaseHistory(this.getOldCaseDetails(request),
					this.getNewCaseDetails(request, accountsVO.getCaseFee()), caseHistory, "Invoicing Task");
		} catch (NumberFormatException var13) {
			return AtlasUtils.getExceptionView(this.logger, var13);
		} catch (NullPointerException var14) {
			return AtlasUtils.getExceptionView(this.logger, var14);
		} catch (CMSException var15) {
			return AtlasUtils.getExceptionView(this.logger, var15);
		} catch (Exception var16) {
			return AtlasUtils.getExceptionView(this.logger, var16);
		}

		this.logger.debug("\n ws saved Sucessfully..");
		return modelAndView;
	}

	private AccountsVO getCompleteAccountVO(AccountsVO accountsVO, HttpServletRequest request) {
		if (request.getParameter(REQ_TOCAPETOWN) == null) {
			accountsVO.setToCapetown("0");
		} else {
			accountsVO.setToCapetown(request.getParameter(REQ_TOCAPETOWN));
		}

		accountsVO.setCaseFee(request.getParameter(CASE_FEE));
		accountsVO.setRegDate(request.getParameter(REGISTER_DATE));
		accountsVO.setDisbursment(request.getParameter(DISBURSEMENT));
		accountsVO.setSpclBillingInst(request.getParameter(SPL_BILLING_INST));
		accountsVO.setOtherInst(request.getParameter(OTHER_INST));
		accountsVO.setBillingMethod(request.getParameter(BILLING_METHODS));
		accountsVO.setHandledBY(request.getParameter(HANDLE_BY));
		accountsVO.setCredit(request.getParameter(CREDIT));
		accountsVO.setMultipleYearBonus(request.getParameter(MULTIPLE_YEAR_BONUS));
		accountsVO.setCurrencyCode(request.getParameter(REQ_CURRENCYCODE));
		accountsVO.setCancelledCharges(request.getParameter(IS_CHECKED));
		accountsVO.setIsisUser(request.getParameter(REQ_ISISUSER));
		accountsVO.setUpdateBy(request.getParameter(REQ_UPDATEBY));
		accountsVO.setCrn(request.getParameter(REQ_CRN));
		accountsVO.setAccountID(request.getParameter(ACCOUNT_ID));
		this.logger.debug("AccountID " + accountsVO.getAccountID() + "BillingMethod " + accountsVO.getBillingMethod()
				+ "CaseFee " + accountsVO.getCaseFee() + "Credit " + accountsVO.getCredit() + "Multiple Year Bonus "
				+ accountsVO.getMultipleYearBonus() + "ClientCode " + accountsVO.getClientCode() + "Crn "
				+ accountsVO.getCrn() + "CurrencyCode " + accountsVO.getCurrencyCode() + "HandledBY "
				+ accountsVO.getHandledBY() + "IsisUser" + accountsVO.getIsisUser() + "Capetown "
				+ request.getParameter("toCapetown") + "Cancelled Charges" + accountsVO.getCancelledCharges());
		this.logger.debug("Billing Method from request:::" + request.getParameter("billingMethods")
				+ "Handle By from request:::" + request.getParameter("h_handleBy"));
		return accountsVO;
	}

	private InvoiceVO getCompleteInvoiceVO(HttpServletRequest request) {
		this.logger.debug("invoiceAddress::" + request.getParameter(BILLING_ADDRESS) + "invoiceTo::"
				+ request.getParameter(INVOICE_TO) + "invoiceInstruction::" + request.getParameter(INVOICE_INSTRUCTION)
				+ "Client Code::" + request.getParameter(CLIENT_CODE) + "capetown Id:::"
				+ request.getParameter(CAPETOWN_ID));
		InvoiceVO invoiceVO = new InvoiceVO();
		invoiceVO.setClientCode(request.getParameter(CLIENT_CODE));
		invoiceVO.setInvoiceTo(request.getParameter(INVOICE_TO));
		invoiceVO.setInvoiceAddress(request.getParameter(BILLING_ADDRESS));
		invoiceVO.setInvoiceInstruction(request.getParameter(INVOICE_INSTRUCTION));
		invoiceVO.setCapetownID(Integer.parseInt(request.getParameter(CAPETOWN_ID)));
		invoiceVO.setInvoiceAmount(request.getParameter("invoiceAmount"));
		invoiceVO.setInvoiceNO(request.getParameter("invoiceNO"));
		invoiceVO.setInvoiceCurrencyCode(request.getParameter("invoiceCurrencyCode"));
		invoiceVO.setInvoiceDate(request.getParameter("invoiceDate"));
		invoiceVO.setCtDate(request.getParameter("ctDate"));
		invoiceVO.setDiscount(request.getParameter(DISCOUNT));
		invoiceVO.setCrn(request.getParameter(REQ_CRN));
		invoiceVO.setUpdateBy(request.getParameter(REQ_UPDATEBY));
		return invoiceVO;
	}

	private CaseDetails getOldCaseDetails(HttpServletRequest request) throws ParseException {
		CaseDetails caseDetails = new CaseDetails();
		this.logger.debug("Invoice no " + request.getParameter("ch_invoiceNo") + " CapeTown "
				+ request.getParameter("ch_toCapetown") + " Case Currency " + request.getParameter("ch_caseCurrency"));
		caseDetails.setInvoiceNumber(request.getParameter("ch_invoiceNo"));
		if (request.getParameter("ch_ctDate") != null && !request.getParameter("ch_ctDate").isEmpty()) {
			this.logger.debug("Ct Date" + request.getParameter("ch_ctDate"));
			caseDetails.setCapeTownDate(this.dateFormat.parse(request.getParameter("ch_ctDate")));
		}

		if (request.getParameter("ch_toCapetown") == null) {
			caseDetails.setCapeTown(false);
		} else if ("0".equals(request.getParameter("ch_toCapetown"))) {
			caseDetails.setCapeTown(false);
		} else {
			caseDetails.setCapeTown(true);
		}

		caseDetails.setCaseCurrency(request.getParameter("ch_caseCurrency"));
		caseDetails.setCaseFee(request.getParameter("ch_caseFee"));
		return caseDetails;
	}

	private CaseDetails getNewCaseDetails(HttpServletRequest request, String caseFee) throws ParseException {
		CaseDetails caseDetails = new CaseDetails();
		caseDetails.setInvoiceNumber(request.getParameter("invoiceNO"));
		this.logger.debug("CT_DATE_TEMP" + request.getParameter("ctDate"));
		this.logger.debug("REQ_TOCAPETOWN " + request.getParameter(REQ_TOCAPETOWN));
		if (request.getParameter("ctDate") != null && !request.getParameter("ctDate").isEmpty()) {
			caseDetails.setCapeTownDate(this.dateFormat2.parse(request.getParameter("ctDate")));
		}

		if (request.getParameter(REQ_TOCAPETOWN) == null) {
			caseDetails.setCapeTown(false);
		} else if ("0".equals(request.getParameter(REQ_TOCAPETOWN))) {
			caseDetails.setCapeTown(false);
		} else {
			caseDetails.setCapeTown(true);
		}

		caseDetails.setCaseCurrency(request.getParameter(REQ_CURRENCYCODE));
		caseDetails.setCaseFee(caseFee);
		return caseDetails;
	}

	public RiskProfile setRiskProfileISISVo(String crn, List<RiskProfileVO> riskProfileList,
			List<RiskProfileVO> riskProfileListHBD, List<RiskProfileVO> subIndusList,
			List<RiskAggregationVO> riskAggrVO, List<TotalRiskAggregationVO> totalRiskAggrVO, String loggedInUser,
			List<SubjectLevelAggregation> subjectAggregationList) {
		RiskProfile riskProfile = new RiskProfile();
		if (riskProfileList.size() > 0) {
			riskProfile.setCRNHasRiskData(1);
		}

		this.logger.debug("<OtherInformation>");
		this.logger.debug("<RiskProfile>");
		this.logger.debug("<CRNHasRiskData>" + riskProfile.getCRNHasRiskData() + "</CRNHasRiskData>");
		this.logger.debug("<CRNRiskData>");
		CRNRiskData crnRiskData = new CRNRiskData();
		crnRiskData.setCRN(crn);
		this.logger.debug("<CRN>" + crnRiskData.getCRN() + "</CRN>");
		String orderId = null;

		try {
			orderId = this.resourceLocator.getTaskService().getISISOrderId(crn);
		} catch (CMSException var88) {
			var88.printStackTrace();
		}

		crnRiskData.setOrderID(orderId);
		this.logger.debug("<ISIS_ORDER_GUID>" + crnRiskData.getOrderID() + "</ISIS_ORDER_GUID>");
		RiskAggregation totalCRNLevelRiskAggregationVO = new RiskAggregation();
		Iterator<TotalRiskAggregationVO> totalCRNRiskAggrItr = totalRiskAggrVO.iterator();
		if (totalCRNRiskAggrItr.hasNext()) {
			TotalRiskAggregationVO totalCrnRiskAggr = (TotalRiskAggregationVO) totalCRNRiskAggrItr.next();
			totalCRNLevelRiskAggregationVO.setTotalCRNRiskSummary(totalCrnRiskAggr.getTotalCRNLevelAggrValue());
		}

		this.logger.debug("<RiskAggregation>");
		this.logger.debug("<TotalCRN-RiskSummary>" + totalCRNLevelRiskAggregationVO.getTotalCRNRiskSummary()
				+ "</TotalCRN-RiskSummary>");
		this.logger.debug("<TotalCRN-RiskScore>" + totalCRNLevelRiskAggregationVO.getTotalCRNRiskScore()
				+ "</TotalCRN-RiskScore>");
		this.logger.debug("<TotalCRN-RAGColour>" + totalCRNLevelRiskAggregationVO.getTotalCRNRAGColour()
				+ "</TotalCRN-RAGColour>");
		this.logger.debug("</RiskAggregation>");
		crnRiskData.setRiskAggregation(totalCRNLevelRiskAggregationVO);
		List<RiskProfileVO> riskProfileCommonList = new ArrayList();
		riskProfileCommonList.addAll(riskProfileList);
		riskProfileCommonList.addAll(riskProfileListHBD);
		Iterator riskProfileCaseItr = riskProfileCommonList.iterator();

		while (riskProfileCaseItr.hasNext()) {
			RiskProfileVO riskVo = (RiskProfileVO) riskProfileCaseItr.next();
			if (riskVo.getRiskType() == 1L) {
				crnRiskData.setHasCaseLevelRisks(1);
				break;
			}
		}

		this.logger.debug("<HasCaseLevelRisks>" + crnRiskData.getHasCaseLevelRisks() + "</HasCaseLevelRisks>");
		this.logger.debug(" <CaseLevelRisks>");
		Iterator riskProfileSubItr = riskProfileCommonList.iterator();

		while (riskProfileSubItr.hasNext()) {
			RiskProfileVO riskVo = (RiskProfileVO) riskProfileSubItr.next();
			if (riskVo.getRiskType() == 2L) {
				crnRiskData.setHasSubjectLevelRisks(1);
				break;
			}
		}

		CaseLevelRisks caseLevelRiskData = new CaseLevelRisks();
		RiskAggregation totalCaseLevelRiskAggrVO = new RiskAggregation();
		Iterator<TotalRiskAggregationVO> totalCaseLevelAggrItr = totalRiskAggrVO.iterator();
		if (totalCaseLevelAggrItr.hasNext()) {
			TotalRiskAggregationVO totalCaseRiskAggr = (TotalRiskAggregationVO) totalCaseLevelAggrItr.next();
			totalCaseLevelRiskAggrVO.setTotalCaseLevelRiskSummary(totalCaseRiskAggr.getTotalCaseLevelAggrValue());
		}

		this.logger.debug("<RiskAggregation>");
		this.logger.debug("<TotalCaseLevel-RiskSummary>" + totalCaseLevelRiskAggrVO.getTotalCaseLevelRiskSummary()
				+ "</TotalCaseLevel-RiskSummary>");
		this.logger.debug("<TotalCaseLevel-RiskScore>" + totalCaseLevelRiskAggrVO.getTotalCaseLevelRiskScore()
				+ "</TotalCaseLevel-RiskScore>");
		this.logger.debug("<TotalCaseLevel-RAGColour>" + totalCaseLevelRiskAggrVO.getTotalCaseLevelRAGColour()
				+ "</TotalCaseLevel-RAGColour>");
		this.logger.debug("</RiskAggregation>");
		caseLevelRiskData.setRiskAggregation(totalCaseLevelRiskAggrVO);
		RiskCategory[] riskCatagories = (RiskCategory[]) null;
		Set<Long> categorySet = new HashSet();
		this.logger.debug("<RiskCategories>");
		Iterator riskCaseLevelCategoryItr = riskProfileCommonList.iterator();

		while (riskCaseLevelCategoryItr.hasNext()) {
			RiskProfileVO riskProfileCaseLevelRiskVO = (RiskProfileVO) riskCaseLevelCategoryItr.next();
			if (riskProfileCaseLevelRiskVO.getRiskType() == 1L) {
				categorySet.add(riskProfileCaseLevelRiskVO.getRiskCategoryId());
			}
		}

		riskCatagories = new RiskCategory[categorySet.size()];
		Set<String> riskCodeSet = new HashSet();
		Set<String> riskCodeSetHBD = new HashSet();

		int l;
		RiskProfileVO riskProfileVO;
		for (l = 0; l < riskProfileList.size(); ++l) {
			riskProfileVO = (RiskProfileVO) riskProfileList.get(l);
			if (riskProfileVO.getRiskType() == 1L) {
				riskCodeSet.add(riskProfileVO.getRiskCode());
			}
		}

		for (l = 0; l < riskProfileListHBD.size(); ++l) {
			riskProfileVO = (RiskProfileVO) riskProfileListHBD.get(l);
			if (riskProfileVO.getRiskType() == 1L) {
				riskCodeSetHBD.add(riskProfileVO.getRiskCode());
			}
		}

		ArrayList cntryTempList;
		for (l = 0; l < categorySet.size(); ++l) {
			this.logger.debug("<RiskCategory>");
			List<RiskProfileVO> riskProfileNewList = new ArrayList();
			List<RiskProfileVO> riskProfileNewListHBD = new ArrayList();
			ArrayList<Long> categoryList = new ArrayList(categorySet);
			Long categoryId = (Long) categoryList.get(l);
			RiskCategory riskCategory = new RiskCategory();
			riskCategory.setCategoryID(Integer.parseInt(categoryId.toString()));
			this.logger.debug("<CategoryId>" + riskCategory.getCategoryID() + "</CategoryId>");
			String categoryName = null;

			try {
				categoryName = this.resourceLocator.getTaskService().getCategoryLabel(categoryId);
			} catch (CMSException var87) {
				var87.printStackTrace();
			}

			riskCategory.setCategoryLabel(categoryName);
			this.logger.debug("<CategoryLabel>" + riskCategory.getCategoryLabel() + "</CategoryLabel>");
			this.logger.debug("<RiskAggregation>");
			Iterator<RiskAggregationVO> riskAggrVOItr = riskAggrVO.iterator();
			RiskAggregation riskAggForCategoryWise = new RiskAggregation();

			while (riskAggrVOItr.hasNext()) {
				RiskAggregationVO riskCaseLvlCategoryAggrVo = (RiskAggregationVO) riskAggrVOItr.next();
				if (riskCaseLvlCategoryAggrVo.getCatId() == categoryId
						&& riskCaseLvlCategoryAggrVo.getRiskType() == 1L) {
					riskAggForCategoryWise.setCategoryRiskSummary(
							Integer.parseInt(Long.toString(riskCaseLvlCategoryAggrVo.getAggrValue())));
				}
			}

			this.logger.debug("<Category-RiskSummary>" + riskAggForCategoryWise.getCategoryRiskSummary()
					+ "</Category-RiskSummary>");
			this.logger.debug(
					"<Category-RiskScore>" + riskAggForCategoryWise.getCategoryRiskScore() + "</Category-RiskScore>");
			this.logger.debug(
					"<Category-RAGColour>" + riskAggForCategoryWise.getCategoryRAGColour() + "</Category-RAGColour>");
			riskCategory.setRiskAggregation(riskAggForCategoryWise);
			this.logger.debug("</RiskAggregation>");
			this.logger.debug("<Risks>");
			Risk[] riskArray = (Risk[]) null;
			Iterator riskProfileVOItr = riskProfileCommonList.iterator();

			while (true) {
				while (riskProfileVOItr.hasNext()) {
					RiskProfileVO riskProfileVO = (RiskProfileVO) riskProfileVOItr.next();
					if (riskProfileVO.getRiskCategoryId() == categoryId && riskProfileVO.getRiskType() == 1L
							&& riskCodeSet.contains(riskProfileVO.getRiskCode())) {
						riskProfileNewList.add(riskProfileVO);
					} else if (riskProfileVO.getRiskCategoryId() == categoryId && riskProfileVO.getRiskType() == 1L
							&& riskCodeSetHBD.contains(riskProfileVO.getRiskCode())) {
						riskProfileNewListHBD.add(riskProfileVO);
					}
				}

				riskArray = new Risk[riskCodeSet.size() + riskCodeSetHBD.size()];
				int k = 0;
				int profileCount = 0;

				for (String riskLabelNoHBD = ""; profileCount < riskProfileNewList.size(); ++k) {
					this.logger.debug("<Risk>");
					Risk riskVO = new Risk();
					RiskProfileVO riskProfileNewListVo = (RiskProfileVO) riskProfileNewList.get(profileCount);
					riskVO.setCode(riskProfileNewListVo.getRiskCode());
					this.logger.debug("<Code>" + riskVO.getCode() + "</Code>");

					try {
						riskLabelNoHBD = this.resourceLocator.getTaskService()
								.getRiskLabel(riskProfileNewListVo.getRiskCode());
					} catch (CMSException var86) {
						var86.printStackTrace();
					}

					riskVO.setLabel(riskLabelNoHBD);
					this.logger.debug("<Label>" + riskVO.getLabel() + "</Label>");
					riskVO.setRiskGroup(riskProfileNewListVo.getIsRiskMandatory());
					this.logger.debug("<RiskGroup>" + riskVO.getRiskGroup() + "</RiskGroup>");
					riskVO.setRiskIsActive(1);
					this.logger.debug("<RiskIsActive>" + riskVO.getRiskIsActive() + "</RiskIsActive>");
					riskVO.setVisibleToClient((int) riskProfileNewListVo.getVisibleToClient());
					this.logger.debug("<VisibleToClient>" + riskVO.getVisibleToClient() + "</VisibleToClient>");
					riskVO.setDisplayOnProfile(1);
					this.logger.debug("<DisplayOnProfile>" + riskVO.getDisplayOnProfile() + "</DisplayOnProfile>");
					riskVO.setRiskType(1);
					this.logger.debug("<RiskType>" + riskVO.getRiskType() + "</RiskType>");
					riskVO.setRiskHasCountryBreakDown(0);
					this.logger.debug("<RiskHasCountryBreakdown>" + riskVO.getRiskHasCountryBreakDown()
							+ "</RiskHasCountryBreakdown>");
					this.logger.debug("<Attributes>");
					this.logger.debug("<Attribute>");
					Attribute[] caseLevelAttrArr = new Attribute[2];
					Attribute outerAttrVO = new Attribute();
					outerAttrVO.setKey(riskProfileNewListVo.getAttributeId());
					outerAttrVO.setValue(riskProfileNewListVo.getNewattrValue());
					this.logger.debug("<Key>" + outerAttrVO.getKey() + "</Key>");
					this.logger.debug("<Value>" + outerAttrVO.getValue() + "</Value>");
					this.logger.debug("</Attribute>");
					caseLevelAttrArr[0] = outerAttrVO;

					for (int j = profileCount + 1; j < riskProfileNewList.size(); ++j) {
						RiskProfileVO riskProfileRiskAttrVO = (RiskProfileVO) riskProfileNewList.get(j);
						Attribute innerAttrVO = new Attribute();
						if (riskProfileRiskAttrVO.getRiskCode().equals(riskProfileNewListVo.getRiskCode())) {
							innerAttrVO.setKey(riskProfileRiskAttrVO.getAttributeId());
							innerAttrVO.setValue(riskProfileRiskAttrVO.getNewattrValue());
							caseLevelAttrArr[1] = innerAttrVO;
							riskProfileNewList.remove(riskProfileNewListVo);
							riskProfileNewList.remove(riskProfileRiskAttrVO);
							this.logger.debug("<Attribute>");
							this.logger.debug("<Key>" + innerAttrVO.getKey() + "</Key>");
							this.logger.debug("<Value>" + innerAttrVO.getValue() + "</Value>");
							this.logger.debug("</Attribute>");
							break;
						}
					}

					this.logger.debug("</Attributes>");
					this.logger.debug("</Risk>");
					riskVO.setAttributes(caseLevelAttrArr);
					riskArray[k] = riskVO;
				}

				Set<String> alreadyAddedHBDRiskCode = new HashSet();
				String riskLabel = "";

				for (int i = 0; i < riskProfileNewListHBD.size(); ++i) {
					Risk riskVO = new Risk();
					this.logger.debug("<Risk>");
					RiskProfileVO riskProfileNewListVo = (RiskProfileVO) riskProfileNewListHBD.get(i);
					if (!alreadyAddedHBDRiskCode.contains(riskProfileNewListVo.getRiskCode())) {
						alreadyAddedHBDRiskCode.add(riskProfileNewListVo.getRiskCode());
						riskVO.setCode(riskProfileNewListVo.getRiskCode());
						this.logger.debug("<Code>" + riskVO.getCode() + "</Code>");

						try {
							riskLabel = this.resourceLocator.getTaskService()
									.getRiskLabel(riskProfileNewListVo.getRiskCode());
						} catch (CMSException var85) {
							var85.printStackTrace();
						}

						riskVO.setLabel(riskLabel);
						this.logger.debug("<Label>" + riskVO.getLabel() + "</Label>");
						riskVO.setRiskGroup(riskProfileNewListVo.getIsRiskMandatory());
						this.logger.debug("<RiskGroup>" + riskVO.getRiskGroup() + "</RiskGroup>");
						riskVO.setRiskIsActive(1);
						this.logger.debug("<RiskIsActive>" + riskVO.getRiskIsActive() + "</RiskIsActive>");
						riskVO.setVisibleToClient((int) riskProfileNewListVo.getVisibleToClient());
						this.logger.debug("<VisibleToClient>" + riskVO.getVisibleToClient() + "</VisibleToClient>");
						riskVO.setDisplayOnProfile(1);
						this.logger.debug("<DisplayOnProfile>" + riskVO.getDisplayOnProfile() + "</DisplayOnProfile>");
						riskVO.setRiskType(1);
						this.logger.debug("<RiskType>" + riskVO.getRiskType() + "</RiskType>");
						riskVO.setRiskHasCountryBreakDown(1);
						this.logger.debug("<RiskHasCountryBreakdown>" + riskVO.getRiskHasCountryBreakDown()
								+ "</RiskHasCountryBreakdown>");
						Set<Long> countryIdSet = new HashSet();
						HashMap<String, Set<Long>> riskCountryMap = new HashMap();

						for (int j = 0; j < riskProfileNewListHBD.size(); ++j) {
							RiskProfileVO riskProfileVo = (RiskProfileVO) riskProfileNewListHBD.get(j);
							if (riskProfileVo.getRiskCode().equals(riskProfileNewListVo.getRiskCode())) {
								countryIdSet.add(riskProfileVo.getCountryId());
							}

							riskCountryMap.put(riskProfileNewListVo.getRiskCode(), countryIdSet);
						}

						Set<Long> cntryIdTempSet = (Set) riskCountryMap.get(riskProfileNewListVo.getRiskCode());
						cntryTempList = new ArrayList(cntryIdTempSet);
						RiskAggregation cntryRiskSummaryAggr = new RiskAggregation();
						cntryRiskSummaryAggr.setCountryRiskSummaryValue("");
						this.logger.debug("<CountryBreakdown>");
						this.logger.debug("<RiskAggregation>");
						this.logger.debug("<CountryRiskSummaryCount>"
								+ cntryRiskSummaryAggr.getCountryRiskSummaryCount() + "</CountryRiskSummaryCount>");
						this.logger.debug("<TotalCategoryScore>" + cntryRiskSummaryAggr.getTotalCategoryScore()
								+ "</TotalCategoryScore>");
						this.logger.debug("<CountryRiskSummaryValue>"
								+ cntryRiskSummaryAggr.getCountryRiskSummaryValue() + "</CountryRiskSummaryValue>");
						this.logger.debug("</RiskAggregation>");
						this.logger.debug("<Countries>");
						Country[] countryArr = new Country[cntryTempList.size()];
						long cntryId = 0L;
						String countryCode = null;

						for (int n = 0; n < cntryTempList.size(); ++n) {
							this.logger.debug("<Country>");
							cntryId = (Long) cntryTempList.get(n);
							Country country = new Country();

							try {
								countryCode = this.resourceLocator.getTaskService().getCountryCode(cntryId);
							} catch (CMSException var84) {
								var84.printStackTrace();
							}

							country.setCountry(countryCode);
							this.logger.debug("<country>" + country.getCountry() + "</country>");
							this.logger.debug("<Attributes>");
							Attribute[] caseLevelAttrArr = (Attribute[]) null;
							List<Attribute> attrArrList = new ArrayList();

							for (int m = 0; m < riskProfileNewListHBD.size(); ++m) {
								RiskProfileVO riskProfileVo = (RiskProfileVO) riskProfileNewListHBD.get(m);
								if (riskProfileVo.getRiskCode().equals(riskProfileNewListVo.getRiskCode())
										&& riskProfileVo.getCountryId() == cntryId) {
									Attribute outerAttrVO = new Attribute();
									outerAttrVO.setKey(riskProfileVo.getAttributeId());
									outerAttrVO.setValue(riskProfileVo.getNewattrValue());
									this.logger.debug("<Attribute>");
									this.logger.debug("<Key>" + outerAttrVO.getKey() + "</Key>");
									this.logger.debug("<Value>" + outerAttrVO.getValue() + "</Value>");
									this.logger.debug("</Attribute>");
									attrArrList.add(outerAttrVO);
								}
							}

							this.logger.debug("<Attributes>");
							this.logger.debug("</Country>");
							caseLevelAttrArr = new Attribute[attrArrList.size()];
							attrArrList.toArray(caseLevelAttrArr);
							country.setAttributes(caseLevelAttrArr);
							countryArr[n] = country;
						}

						this.logger.debug("</countries>");
						this.logger.debug("</CountryBreakdown>");
						this.logger.debug("</Risk>");
						CountryBreakDown cntryBrkDown = new CountryBreakDown();
						cntryBrkDown.setRiskAggregation(cntryRiskSummaryAggr);
						cntryBrkDown.setCountries(countryArr);
						riskVO.setCOuntryBreak(cntryBrkDown);
						riskArray[k] = riskVO;
						++k;
					}
				}

				this.logger.debug("</Risks>");
				this.logger.debug("</RiskCategory>");
				riskCategory.setRisks(riskArray);
				riskCatagories[l] = riskCategory;
				break;
			}
		}

		this.logger.debug("</RiskCategories>");
		this.logger.debug("</CaseLevelRisks>");
		caseLevelRiskData.setRiskCatagories(riskCatagories);
		crnRiskData.setCaseLevelRisks(caseLevelRiskData);
		this.logger.debug("<HasSubjectLevelRisks>" + crnRiskData.getHasSubjectLevelRisks() + "</HasSubjectLevelRisks>");
		SubjectLevelRisks subjectLevelRisksData = new SubjectLevelRisks();
		this.logger.debug("<SubjectLevelRisks>");
		Subjects subjects = new Subjects();
		this.logger.debug("<Subjects>");
		Subject[] subArr = (Subject[]) null;
		this.logger.debug("<RiskAggregation>");
		RiskAggregation totalSubjetLevelRiskAggrVO = new RiskAggregation();
		Iterator<TotalRiskAggregationVO> totalSubjectLevelAggrItr = totalRiskAggrVO.iterator();
		if (totalSubjectLevelAggrItr.hasNext()) {
			TotalRiskAggregationVO totalSubjectRiskAggr = (TotalRiskAggregationVO) totalSubjectLevelAggrItr.next();
			totalSubjetLevelRiskAggrVO.setTotalSubjectRiskSummary(totalSubjectRiskAggr.getTotalSubLevelAggrValue());
		}

		this.logger.debug("<TotalSubjectsLevel-RiskSummary>" + totalSubjetLevelRiskAggrVO.getTotalSubjectRiskSummary()
				+ "</TotalSubjectsLevel-RiskSummary>");
		this.logger.debug("<TotalSubjectsLevel-RiskScore>" + totalSubjetLevelRiskAggrVO.getTotalSubjectRiskScore()
				+ "</TotalSubjectsLevel-RiskScore>");
		this.logger.debug("<TotalSubjectsLevel-RAGColour>" + totalSubjetLevelRiskAggrVO.getTotalSubjectLevelRAGColour()
				+ "</TotalSubjectsLevel-RAGColour>");
		subjects.setRiskAggregation(totalSubjetLevelRiskAggrVO);
		this.logger.debug("</RiskAggregation>");
		RiskCategory[] riskSubCatagories = (RiskCategory[]) null;
		Set<Long> categorySubSet = new HashSet();
		Set<Long> subjectSet = new HashSet();
		Iterator riskSubjectNamesItr = riskProfileCommonList.iterator();

		while (riskSubjectNamesItr.hasNext()) {
			RiskProfileVO riskProfileVO = (RiskProfileVO) riskSubjectNamesItr.next();
			if (riskProfileVO.getRiskType() == 2L) {
				subjectSet.add(riskProfileVO.getSubjectId());
			}
		}

		subArr = new Subject[subjectSet.size()];
		Iterator<Long> subjectListItr = subjectSet.iterator();

		for (int subCount = 0; subjectListItr.hasNext(); ++subCount) {
			this.logger.debug("<Subject>");
			long subjectId = (Long) subjectListItr.next();
			Subject subVo = new Subject();

			try {
				subVo = this.resourceLocator.getTaskService().getSubjectDetailsForISIS(subjectId);
			} catch (CMSException var83) {
				var83.printStackTrace();
			}

			this.logger.debug("<SubjectId>" + subVo.getSubjectID() + "</SubjectId>");
			this.logger.debug("<SubjectName>" + subVo.getSubjectName() + "</SubjectName>");
			this.logger.debug("<IsisSubjectId>" + subVo.getISISSubjectID() + "</IsisSubjectId>");
			this.logger.debug("<SubjectType>" + subVo.getSubjectType() + "</SubjectType>");
			this.logger.debug("<country>" + subVo.getCountry() + "</country>");
			this.logger.debug("<SubjectIndustryCode>" + subVo.getSubjectIndustryCode() + "</SubjectIndustryCode>");
			this.logger.debug("<RiskAggregation>");
			RiskAggregation indivdualRiskAggregation = new RiskAggregation();
			Iterator eachSubAggrItr = subjectAggregationList.iterator();

			while (eachSubAggrItr.hasNext()) {
				SubjectLevelAggregation eachSubLevelRiskVo = (SubjectLevelAggregation) eachSubAggrItr.next();
				if (eachSubLevelRiskVo.getSubjectId() != null && eachSubLevelRiskVo.getSubjectId() == subjectId) {
					indivdualRiskAggregation.setIndividualSubjectRiskSummary(eachSubLevelRiskVo.getEachSubLvlAggr());
				}
			}

			this.logger.debug("<IndividualSubject-RiskSummary>"
					+ indivdualRiskAggregation.getIndividualSubjectRiskSummary() + "</IndividualSubject-RiskSummary>");
			this.logger.debug("<IndividualSubject-RiskScore>" + indivdualRiskAggregation.getSubjectRiskScore()
					+ "</IndividualSubject-RiskScore>");
			this.logger.debug("<IndividualSubject-RAGColour>" + indivdualRiskAggregation.getIndividualSubjectRAGColour()
					+ "</IndividualSubject-RAGColour>");
			this.logger.debug("</RiskAggregation>");
			subVo.setRiskAggregation(indivdualRiskAggregation);
			Iterator<RiskProfileVO> riskSubLevelCategoryItr = riskProfileCommonList.iterator();
			this.logger.debug("<RiskCategories>>");

			while (riskSubLevelCategoryItr.hasNext()) {
				RiskProfileVO riskProfileSubjectLevelRiskVO = (RiskProfileVO) riskSubLevelCategoryItr.next();
				if (riskProfileSubjectLevelRiskVO.getRiskType() == 2L
						&& riskProfileSubjectLevelRiskVO.getSubjectId() == subjectId) {
					categorySubSet.add(riskProfileSubjectLevelRiskVO.getRiskCategoryId());
				}
			}

			riskSubCatagories = new RiskCategory[categorySubSet.size()];
			Set<String> subRiskCodeSet = new HashSet();
			Set<String> subRiskCodeSetHBD = new HashSet();

			int l;
			RiskProfileVO riskProfileVO;
			for (l = 0; l < riskProfileList.size(); ++l) {
				riskProfileVO = (RiskProfileVO) riskProfileList.get(l);
				if (riskProfileVO.getRiskType() == 2L && riskProfileVO.getSubjectId() == subjectId) {
					subRiskCodeSet.add(riskProfileVO.getRiskCode());
				}
			}

			for (l = 0; l < riskProfileListHBD.size(); ++l) {
				riskProfileVO = (RiskProfileVO) riskProfileListHBD.get(l);
				if (riskProfileVO.getRiskType() == 2L && riskProfileVO.getSubjectId() == subjectId) {
					subRiskCodeSetHBD.add(riskProfileVO.getRiskCode());
				}
			}

			for (l = 0; l < categorySubSet.size(); ++l) {
				this.logger.debug("<RiskCategory>");
				List<RiskProfileVO> riskProfileNewList = new ArrayList();
				List<RiskProfileVO> riskProfileNewListHBD = new ArrayList();
				cntryTempList = new ArrayList(categorySubSet);
				Long categoryId = (Long) cntryTempList.get(l);
				RiskCategory riskCategory = new RiskCategory();
				riskCategory.setCategoryID(Integer.parseInt(categoryId.toString()));
				this.logger.debug("<CategoryId>" + riskCategory.getCategoryID() + "</CategoryId>");
				String categoryName = null;

				try {
					categoryName = this.resourceLocator.getTaskService().getCategoryLabel(categoryId);
				} catch (CMSException var82) {
					var82.printStackTrace();
				}

				riskCategory.setCategoryLabel(categoryName);
				this.logger.debug("<CategoryLabel>" + riskCategory.getCategoryLabel() + "</CategoryLabel>");
				this.logger.debug("<RiskAggregation>");
				Iterator<RiskAggregationVO> riskAggrVOItr = riskAggrVO.iterator();
				RiskAggregation riskAggForCategoryWise = new RiskAggregation();

				while (riskAggrVOItr.hasNext()) {
					RiskAggregationVO riskSubLvlCategoryAggrVo = (RiskAggregationVO) riskAggrVOItr.next();
					if (riskSubLvlCategoryAggrVo.getCatId() == categoryId
							&& riskSubLvlCategoryAggrVo.getRiskType() == 2L
							&& riskSubLvlCategoryAggrVo.getSubId() == subjectId) {
						riskAggForCategoryWise.setSubjectCategoryRiskSummary(
								Integer.parseInt(Long.toString(riskSubLvlCategoryAggrVo.getAggrValue())));
					}
				}

				this.logger.debug("<SubjectCategory-RiskSummary>"
						+ riskAggForCategoryWise.getSubjectCategoryRiskSummary() + "</SubjectCategory-RiskSummary>");
				this.logger.debug("<SubjectCategory-RiskScore>" + riskAggForCategoryWise.getCategoryRiskScore()
						+ "</SubjectCategory-RiskScore>");
				this.logger.debug("<SubjectCategory-RAGColour>" + riskAggForCategoryWise.getCategoryRAGColour()
						+ "</SubjectCategory-RAGColour>");
				this.logger.debug("</RiskAggregation>");
				riskCategory.setRiskAggregation(riskAggForCategoryWise);
				Risk[] riskArray = (Risk[]) null;
				Iterator riskProfileVOItr = riskProfileCommonList.iterator();

				while (true) {
					while (riskProfileVOItr.hasNext()) {
						RiskProfileVO riskProfileVO = (RiskProfileVO) riskProfileVOItr.next();
						if (riskProfileVO.getRiskCategoryId() == categoryId && riskProfileVO.getRiskType() == 2L
								&& riskProfileVO.getSubjectId() == subjectId
								&& subRiskCodeSet.contains(riskProfileVO.getRiskCode())) {
							riskProfileNewList.add(riskProfileVO);
						} else if (riskProfileVO.getRiskCategoryId() == categoryId && riskProfileVO.getRiskType() == 2L
								&& riskProfileVO.getSubjectId() == subjectId
								&& subRiskCodeSetHBD.contains(riskProfileVO.getRiskCode())) {
							riskProfileNewListHBD.add(riskProfileVO);
						}
					}

					this.logger.debug("<Risks>");
					riskArray = new Risk[subRiskCodeSet.size() + subRiskCodeSetHBD.size()];
					int k = 0;
					int subProfileCount = 0;

					for (String riskLabelNoHBD = ""; subProfileCount < riskProfileNewList.size(); ++k) {
						this.logger.debug("<Risk>");
						Risk riskVO = new Risk();
						RiskProfileVO riskProfileNewListVo = (RiskProfileVO) riskProfileNewList.get(subProfileCount);
						riskVO.setCode(riskProfileNewListVo.getRiskCode());
						this.logger.debug("<Code>" + riskVO.getCode() + "</Code>");

						try {
							riskLabelNoHBD = this.resourceLocator.getTaskService()
									.getRiskLabel(riskProfileNewListVo.getRiskCode());
						} catch (CMSException var81) {
							var81.printStackTrace();
						}

						riskVO.setLabel(riskLabelNoHBD);
						this.logger.debug("<Label>" + riskVO.getLabel() + "</Label>");
						riskVO.setRiskGroup(riskProfileNewListVo.getIsRiskMandatory());
						this.logger.debug("<RiskGroup>" + riskVO.getRiskGroup() + "</RiskGroup>");
						riskVO.setRiskIsActive(1);
						this.logger.debug("<RiskIsActive>" + riskVO.getRiskIsActive() + "</RiskIsActive>");
						riskVO.setVisibleToClient((int) riskProfileNewListVo.getVisibleToClient());
						this.logger.debug("<VisibleToClient>" + riskVO.getVisibleToClient() + "</VisibleToClient>");
						riskVO.setDisplayOnProfile(1);
						this.logger.debug("<DisplayOnProfile>" + riskVO.getDisplayOnProfile() + "</DisplayOnProfile>");
						riskVO.setRiskType(2);
						this.logger.debug("<RiskType>" + riskVO.getRiskType() + "</RiskType>");
						riskVO.setRiskHasCountryBreakDown(0);
						this.logger.debug("<RiskHasCountryBreakdown>" + riskVO.getRiskHasCountryBreakDown()
								+ "</RiskHasCountryBreakdown>");
						this.logger.debug("<Attributes>");
						Attribute[] caseLevelAttrArr = new Attribute[2];
						this.logger.debug("<Attribute>");
						Attribute outerAttrVO = new Attribute();
						outerAttrVO.setKey(riskProfileNewListVo.getAttributeId());
						outerAttrVO.setValue(riskProfileNewListVo.getNewattrValue());
						this.logger.debug("<Key>" + outerAttrVO.getKey() + "</Key>");
						this.logger.debug("<Value>" + outerAttrVO.getValue() + "</Value>");
						this.logger.debug("</Attribute>");
						caseLevelAttrArr[0] = outerAttrVO;

						for (int j = subProfileCount + 1; j < riskProfileNewList.size(); ++j) {
							RiskProfileVO riskProfileRiskAttrVO = (RiskProfileVO) riskProfileNewList.get(j);
							Attribute innerAttrVO = new Attribute();
							if (riskProfileRiskAttrVO.getRiskCode().equals(riskProfileNewListVo.getRiskCode())) {
								this.logger.debug("<Attribute>");
								innerAttrVO.setKey(riskProfileRiskAttrVO.getAttributeId());
								innerAttrVO.setValue(riskProfileRiskAttrVO.getNewattrValue());
								caseLevelAttrArr[1] = innerAttrVO;
								this.logger.debug("<Key>" + innerAttrVO.getKey() + "</Key>");
								this.logger.debug("<Value>" + innerAttrVO.getValue() + "</Value>");
								this.logger.debug("</Attribute>");
								riskProfileNewList.remove(riskProfileNewListVo);
								riskProfileNewList.remove(riskProfileRiskAttrVO);
								break;
							}
						}

						this.logger.debug("</Attributes>");
						this.logger.debug("</Risk>");
						riskVO.setAttributes(caseLevelAttrArr);
						riskArray[k] = riskVO;
					}

					Set<String> alreadyAddedHBDRiskCodeForSubLevel = new HashSet();
					String riskLabel = "";

					for (int i = 0; i < riskProfileNewListHBD.size(); ++i) {
						this.logger.debug("<Risk>");
						Risk riskVO = new Risk();
						RiskProfileVO riskProfileNewListVo = (RiskProfileVO) riskProfileNewListHBD.get(i);
						if (!alreadyAddedHBDRiskCodeForSubLevel.contains(riskProfileNewListVo.getRiskCode())) {
							alreadyAddedHBDRiskCodeForSubLevel.add(riskProfileNewListVo.getRiskCode());
							riskVO.setCode(riskProfileNewListVo.getRiskCode());
							this.logger.debug("<Code>" + riskVO.getCode() + "</Code>");

							try {
								riskLabel = this.resourceLocator.getTaskService()
										.getRiskLabel(riskProfileNewListVo.getRiskCode());
							} catch (CMSException var80) {
								var80.printStackTrace();
							}

							riskVO.setLabel(riskLabel);
							this.logger.debug("<Label>" + riskVO.getLabel() + "</Label>");
							riskVO.setRiskGroup(riskProfileNewListVo.getIsRiskMandatory());
							this.logger.debug("<RiskGroup>" + riskVO.getRiskGroup() + "</RiskGroup>");
							riskVO.setRiskIsActive(1);
							this.logger.debug("<RiskIsActive>" + riskVO.getRiskIsActive() + "</RiskIsActive>");
							riskVO.setVisibleToClient((int) riskProfileNewListVo.getVisibleToClient());
							this.logger.debug("<VisibleToClient>" + riskVO.getVisibleToClient() + "</VisibleToClient>");
							riskVO.setDisplayOnProfile(1);
							this.logger
									.debug("<DisplayOnProfile>" + riskVO.getDisplayOnProfile() + "</DisplayOnProfile>");
							riskVO.setRiskType(2);
							this.logger.debug("<RiskType>" + riskVO.getRiskType() + "</RiskType>");
							riskVO.setRiskHasCountryBreakDown(1);
							this.logger.debug("<RiskHasCountryBreakdown>" + riskVO.getRiskHasCountryBreakDown()
									+ "</RiskHasCountryBreakdown>");
							Set<Long> countryIdSet = new HashSet();
							HashMap<String, Set<Long>> riskCountryMap = new HashMap();

							for (int j = 0; j < riskProfileNewListHBD.size(); ++j) {
								RiskProfileVO riskProfileVo = (RiskProfileVO) riskProfileNewListHBD.get(j);
								if (riskProfileVo.getRiskCode().equals(riskProfileNewListVo.getRiskCode())) {
									countryIdSet.add(riskProfileVo.getCountryId());
								}

								riskCountryMap.put(riskProfileNewListVo.getRiskCode(), countryIdSet);
							}

							Set<Long> cntryIdTempSet = (Set) riskCountryMap.get(riskProfileNewListVo.getRiskCode());
							List<Long> cntryTempList = new ArrayList(cntryIdTempSet);
							this.logger.debug("<CountryBreakdown>");
							RiskAggregation cntryRiskSummaryAggrSub = new RiskAggregation();
							cntryRiskSummaryAggrSub.setCountryRiskSummaryValue("");
							this.logger.debug("<RiskAggregation>");
							this.logger.debug(
									"<CountryRiskSummaryCount>" + cntryRiskSummaryAggrSub.getCountryRiskSummaryCount()
											+ "</CountryRiskSummaryCount>");
							this.logger.debug("<TotalCategoryScore>" + cntryRiskSummaryAggrSub.getTotalCategoryScore()
									+ "</TotalCategoryScore>");
							this.logger.debug(
									"<CountryRiskSummaryValue>" + cntryRiskSummaryAggrSub.getCountryRiskSummaryValue()
											+ "</CountryRiskSummaryValue>");
							this.logger.debug("</RiskAggregation>");
							this.logger.debug("<countries>");
							Country[] countryArr = new Country[cntryTempList.size()];
							long cntryId = 0L;
							String countryCode = null;

							for (int n = 0; n < cntryTempList.size(); ++n) {
								this.logger.debug("<Country>");
								cntryId = (Long) cntryTempList.get(n);
								Country country = new Country();

								try {
									countryCode = this.resourceLocator.getTaskService().getCountryCode(cntryId);
								} catch (CMSException var79) {
									var79.printStackTrace();
								}

								country.setCountry(countryCode);
								this.logger.debug("<country>" + country.getCountry() + "</country>");
								Attribute[] caseLevelAttrArr = (Attribute[]) null;
								this.logger.debug("<Attributes>");
								List<Attribute> attrArrList = new ArrayList();

								for (int m = 0; m < riskProfileNewListHBD.size(); ++m) {
									RiskProfileVO riskProfileVo = (RiskProfileVO) riskProfileNewListHBD.get(m);
									if (riskProfileVo.getRiskCode().equals(riskProfileNewListVo.getRiskCode())
											&& riskProfileVo.getCountryId() == cntryId) {
										Attribute outerAttrVO = new Attribute();
										outerAttrVO.setKey(riskProfileVo.getAttributeId());
										outerAttrVO.setValue(riskProfileVo.getNewattrValue());
										this.logger.debug("<Attribute>");
										this.logger.debug("<Key>" + outerAttrVO.getKey() + "</Key>");
										this.logger.debug("<Value>" + outerAttrVO.getValue() + "</Value>");
										this.logger.debug("</Attribute>");
										attrArrList.add(outerAttrVO);
									}
								}

								this.logger.debug("</Attributes>");
								this.logger.debug("</Country>");
								caseLevelAttrArr = new Attribute[attrArrList.size()];
								attrArrList.toArray(caseLevelAttrArr);
								country.setAttributes(caseLevelAttrArr);
								countryArr[n] = country;
							}

							this.logger.debug("</countries>");
							this.logger.debug("</CountryBreakdown>");
							this.logger.debug("</Risk>");
							CountryBreakDown cntryBrkDown = new CountryBreakDown();
							cntryBrkDown.setRiskAggregation(cntryRiskSummaryAggrSub);
							cntryBrkDown.setCountries(countryArr);
							riskVO.setCOuntryBreak(cntryBrkDown);
							riskArray[k] = riskVO;
							++k;
						}
					}

					this.logger.debug("</Risks>");
					this.logger.debug("</RiskCategory>");
					riskCategory.setRisks(riskArray);
					riskSubCatagories[l] = riskCategory;
					break;
				}
			}

			this.logger.debug("</RiskCategories>");
			this.logger.debug("</Subject>");
			subVo.setRiskCategories(riskSubCatagories);
			subArr[subCount] = subVo;
		}

		this.logger.debug("</Subjects>");
		this.logger.debug("</SubjectLevelRisks>");
		this.logger.debug("</CRNRiskData>");
		this.logger.debug("</RiskProfile>");
		this.logger.debug("</OtherInformation>");
		subjects.setSubject(subArr);
		subjectLevelRisksData.setSubjects(subjects);
		crnRiskData.setSubjectLevelRisks(subjectLevelRisksData);
		riskProfile.setCRNRiskData(crnRiskData);
		return riskProfile;
	}
}