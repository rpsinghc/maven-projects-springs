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
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusIndustryVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusRiskVO;
import com.worldcheck.atlas.isis.vo.ClientCaseStatusVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.SubjectDetails;
import com.worldcheck.atlas.vo.SubjectLevelAggregation;
import com.worldcheck.atlas.vo.audit.RiskHistory;
import com.worldcheck.atlas.vo.masters.CategoryDetailsVO;
import com.worldcheck.atlas.vo.masters.CountryHBDVO;
import com.worldcheck.atlas.vo.masters.RiskAggregationVO;
import com.worldcheck.atlas.vo.masters.RiskAttributeVO;
import com.worldcheck.atlas.vo.masters.RiskCategoryMasterVO;
import com.worldcheck.atlas.vo.masters.RiskProfileVO;
import com.worldcheck.atlas.vo.masters.RisksMasterVO;
import com.worldcheck.atlas.vo.masters.TotalRiskAggregationVO;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import com.worldcheck.atlas.vo.task.SubjectListForUserVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

public class JSONTaskManagementController extends JSONMultiActionController {
	private static final String TEAM_TYPE_ID = "teamTypeId";
	private static final String TASK_NAME = "taskName";
	private static final String ID_OF_TEAM = "idOfTeam";
	private static final String HASH = "#";
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.sbm.JSONTaskManagementController");
	private final String JSON_VIEW = "jsonView";
	private static final String CRN = "crn";
	private static final String COUNTRY_ID = "countryId";
	private static final String PERFORMER = "performer";
	private static final String USER_ID = "userId";
	private static final String SELECTED_DATE = "selectedDate";
	private static final String SELECTED_OFFICE = "selectedOffice";
	private static final String SELECTED_ANALYST = "selectedAnalyst";
	private static final String YES = "yes";
	private static final String NO = "no";
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
	private String ATT_CASE_AGGR = "caseLevelAggrValueJSP";
	private String ATT_SUB_AGGR = "subLevelAggrValueJSP";
	private String ATT_CRN_AGGR = "crnAggrValueJSP";
	private String SINGLE_SPACE = " ";
	private String ATT_RISK_LABEL = "riskLabelJSP";
	private String IS_RISK_MANDATORY = "isRiskMandatoryJSP";
	private String FIRST_ATTRIBUTE_ID = "firstAttributeId";
	private String SECOND_ATTRIBUTE_ID = "secondAttributeId";

	public ModelAndView getCountryNameRet(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getCountry name for retriever section");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String crn = "";
			if (httpServletRequest.getParameter("crn") != null) {
				crn = httpServletRequest.getParameter("crn");
			}

			this.logger.debug("value of crn is :: " + crn);
			viewForJSON.addObject("listForCntry", resourceLocator.getTaskService().getAllCountries(crn));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView getCountryDBNameRet(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getCountry DB name for retriever section");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String countryId = "";
			if (httpServletRequest.getParameter("countryId") != null) {
				countryId = httpServletRequest.getParameter("countryId");
			}

			this.logger.debug("value of countryId is :: " + countryId);
			viewForJSON.addObject("listForCntryDB",
					resourceLocator.getTaskService().getAllDBNamesForCountry(countryId));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView getRiskAttributeDefaultValues(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getRiskAttributeDefaultValues");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			long firstAttributeId = 0L;
			long secondAttributeId = 0L;
			if (httpServletRequest.getParameter(this.FIRST_ATTRIBUTE_ID) != null) {
				firstAttributeId = Long.parseLong(httpServletRequest.getParameter(this.FIRST_ATTRIBUTE_ID));
			}

			if (httpServletRequest.getParameter(this.SECOND_ATTRIBUTE_ID) != null) {
				secondAttributeId = Long.parseLong(httpServletRequest.getParameter(this.SECOND_ATTRIBUTE_ID));
			}

			List<Long> attributeIdsList = new ArrayList();
			attributeIdsList.add(firstAttributeId);
			attributeIdsList.add(secondAttributeId);
			viewForJSON.addObject("attributeDefaultValues",
					resourceLocator.getTaskService().getRiskAttributeDefaultValues(attributeIdsList));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}
	}

	public ModelAndView getVendorDetails(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getVendorDetails for BI/Vendor Research task");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String crn = "";
			if (httpServletRequest.getParameter("crn") != null) {
				crn = httpServletRequest.getParameter("crn");
			}

			viewForJSON.addObject("listForVendorDetails", resourceLocator.getTaskService().getVendorDetails(crn));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView getSubjectListForUser(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getSubjectListForUser for SubjectList Research task");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String crn = "";
			String performer = "";
			String teamTypeId = "";
			if (httpServletRequest.getParameter("crn") != null) {
				crn = httpServletRequest.getParameter("crn");
			}

			if (httpServletRequest.getParameter("performer") != null) {
				performer = httpServletRequest.getParameter("performer");
			}

			if (httpServletRequest.getParameter("teamTypeId") != null) {
				teamTypeId = httpServletRequest.getParameter("teamTypeId");
			}

			this.logger.debug("value of performer is :: " + performer);
			this.logger.debug("value of crn is :: " + crn);
			viewForJSON.addObject("listForSub", resourceLocator.getTaskService().getSubjectListForUser(crn, performer,
					teamTypeId,
					Integer.parseInt(
							StringUtils.checkPaginationParams("start", httpServletRequest.getParameter("start"))),
					Integer.parseInt(
							StringUtils.checkPaginationParams("limit", httpServletRequest.getParameter("limit")))));
			int count = resourceLocator.getTaskService().getSubjectListCountForUser(crn, performer);
			viewForJSON.addObject("total", count);
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView getSubjectLevelRiskForUser(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getSubjectListForUser for SubjectList Research task");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String crn = "";
			String performer = "";
			String teamTypeId = "";
			String taskName = "";
			String idOfTeam = "";
			String teamName = "";
			String teamId = "";
			if (httpServletRequest.getParameter("crn") != null) {
				crn = httpServletRequest.getParameter("crn");
			}

			if (httpServletRequest.getParameter("performer") != null) {
				performer = httpServletRequest.getParameter("performer");
			}

			if (httpServletRequest.getParameter("teamTypeId") != null) {
				teamTypeId = httpServletRequest.getParameter("teamTypeId");
			}

			if (httpServletRequest.getParameter("taskName") != null) {
				taskName = httpServletRequest.getParameter("taskName");
			}

			if (httpServletRequest.getParameter("idOfTeam") != null) {
				idOfTeam = httpServletRequest.getParameter("idOfTeam");
			}

			this.logger.debug("value of performer is :: " + performer);
			this.logger.debug("value of crn is :: " + crn);
			this.logger.debug("task name is :: " + taskName);
			this.logger.debug("id of team is ::" + idOfTeam);
			if (idOfTeam.trim().length() > 0) {
				String[] temp = idOfTeam.split("#");
				teamName = temp[0];
				teamId = temp[1];
			}

			this.logger.debug("teamName is in getSubjectLevelRiskForUser" + teamName);
			this.logger.debug("teamId is in getSubjectLevelRiskForUser" + teamId);
			List<SubjectListForUserVO> svoList = resourceLocator.getTaskService().getSubjectLevelRiskForUser(crn,
					performer, taskName, teamTypeId, teamName, teamId);
			this.logger.debug("Subject VO Size::" + svoList.size());

			for (int i = 0; i < svoList.size(); ++i) {
				SubjectListForUserVO slvo = (SubjectListForUserVO) svoList.get(i);
				this.logger.debug("Sub Id::" + slvo.getSubId());
				this.logger.debug("Sub Name::" + slvo.getrName());
				this.logger.debug("Sub Entity::" + slvo.getEntityName());
				this.logger.debug("CountryName::" + slvo.getCountryName());
				List<RiskCategoryMasterVO> rvCOList = slvo.getCategoryMasterDataList();

				for (int j = 0; j < rvCOList.size(); ++j) {
					RiskCategoryMasterVO rcVO = (RiskCategoryMasterVO) rvCOList.get(j);
					this.logger.debug("Category Id::" + rcVO.getRiskCategoryId());
					this.logger.debug("Category Name::" + rcVO.getCategory());
					List<RisksMasterVO> rmVOList = rcVO.getRiskMasterDataList();

					for (int k = 0; k < rmVOList.size(); ++k) {
						RisksMasterVO rVO = (RisksMasterVO) rmVOList.get(k);
						this.logger.debug("Risk code::" + rVO.getRiskCode());
						this.logger.debug("Risk label::" + rVO.getRiskLabel());
						this.logger.debug("Is risk mandatory::" + rVO.getIsRiskMandatory());
						List<RiskAttributeVO> raVO = rVO.getRiskAttributeVOList();

						for (int l = 0; l < raVO.size(); ++l) {
							RiskAttributeVO ra = (RiskAttributeVO) raVO.get(l);
							this.logger.debug("Attribute Name::" + ra.getAttributeName());
							this.logger.debug("Attribute type::" + ra.getAttributeType());
							this.logger.debug("Attribute Value::" + ra.getAttributeValue());
							this.logger.debug("Risk attribute value::" + ra.getRiskAttributeValue());
						}
					}
				}
			}

			viewForJSON.addObject("listForSub", svoList);
			viewForJSON.addObject("total", 2);
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var24) {
			return AtlasUtils.getExceptionView(this.logger, var24);
		} catch (Exception var25) {
			return AtlasUtils.getExceptionView(this.logger, var25);
		}
	}

	public ModelAndView isRiskAssociatedWithCase(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside isRiskAssociatedWithCase");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String crn = "";
			if (httpServletRequest.getParameter("crn") != null) {
				crn = httpServletRequest.getParameter("crn");
			}

			List<String> riskAssociatedList = resourceLocator.getTaskService().getRiskAssociatedWithCase(crn);
			int risk_associated_flag = 0;
			this.logger.debug("riskAssociatedList--" + riskAssociatedList);
			if (riskAssociatedList.size() > 0) {
				risk_associated_flag = 1;
				int updatedRecord = resourceLocator.getTaskService().updateRiskFlag(crn);
				this.logger.debug("Updated records are--" + updatedRecord);
			}

			viewForJSON.addObject("isRiskAssociatedWithCase", Integer.valueOf(risk_associated_flag));
			this.logger.debug("After getting values exit from listForRiskAssociatedWithCase method");
			return viewForJSON;
		} catch (NullPointerException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView getCaseLevelRiskForUser(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getCaseLevelRiskForUser for user Research task");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String crn = "";
			String performer = "";
			String teamTypeId = "";
			if (httpServletRequest.getParameter("crn") != null) {
				crn = httpServletRequest.getParameter("crn");
			}

			if (httpServletRequest.getParameter("performer") != null) {
				performer = httpServletRequest.getParameter("performer");
			}

			if (httpServletRequest.getParameter("teamTypeId") != null) {
				teamTypeId = httpServletRequest.getParameter("teamTypeId");
			}

			this.logger.debug("value of performer is :: " + performer);
			this.logger.debug("value of crn is :: " + crn);
			List<RiskCategoryMasterVO> categoryList = resourceLocator.getTaskService().getCaseLevelRiskForUser(crn,
					performer);
			this.logger.debug("Category VO Size::" + categoryList.size());

			for (int i = 0; i < categoryList.size(); ++i) {
				RiskCategoryMasterVO rcVO = (RiskCategoryMasterVO) categoryList.get(i);
				this.logger.debug("Iterate " + i + " category");
				this.logger.debug("Category Id::" + rcVO.getRiskCategoryId());
				this.logger.debug("Category Name::" + rcVO.getCategory());
				List<RisksMasterVO> rmVOList = rcVO.getRiskMasterDataList();

				for (int k = 0; k < rmVOList.size(); ++k) {
					RisksMasterVO rVO = (RisksMasterVO) rmVOList.get(k);
					this.logger.debug("Risk code::" + rVO.getRiskCode());
					this.logger.debug("Risk label::" + rVO.getRiskLabel());
					this.logger.debug("Is risk mandatory::" + rVO.getIsRiskMandatory());
					List<RiskAttributeVO> raVO = rVO.getRiskAttributeVOList();

					for (int l = 0; l < raVO.size(); ++l) {
						RiskAttributeVO ra = (RiskAttributeVO) raVO.get(l);
						this.logger.debug("Attribute ID::" + ra.getAttributeId());
						this.logger.debug("Attribute Name::" + ra.getAttributeName());
						this.logger.debug("Attribute type::" + ra.getAttributeType());
						this.logger.debug("Attribute Value::" + ra.getAttributeValue());
						this.logger.debug("Risk attribute value::" + ra.getRiskAttributeValue());
					}
				}
			}

			viewForJSON.addObject("listForSub", categoryList);
			viewForJSON.addObject("total", categoryList.size());
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var17) {
			return AtlasUtils.getExceptionView(this.logger, var17);
		} catch (Exception var18) {
			return AtlasUtils.getExceptionView(this.logger, var18);
		}
	}

	public ModelAndView getREList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getREList for SubjectList Research task");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String crn = "";
			String performer = "";
			String subjectName = "";
			String subjectID = "";
			String teamId = "";
			if (httpServletRequest.getParameter("crn") != null) {
				crn = httpServletRequest.getParameter("crn");
			}

			if (httpServletRequest.getParameter("performer") != null) {
				performer = httpServletRequest.getParameter("performer");
			}

			if (httpServletRequest.getParameter("subjectName") != null) {
				subjectName = httpServletRequest.getParameter("subjectName");
			}

			if (httpServletRequest.getParameter("subjectID") != null) {
				subjectID = httpServletRequest.getParameter("subjectID");
			}

			if (httpServletRequest.getParameter("team_ID") != null) {
				teamId = httpServletRequest.getParameter("team_ID");
			}

			this.logger.debug("value of performer is :: " + performer);
			this.logger.debug("value of crn is :: " + crn);
			this.logger.debug("value of subjectName is :: " + subjectName);
			this.logger.debug("value of subjectID is :: " + subjectID);
			this.logger.debug("value of teamId is :: " + teamId);
			viewForJSON.addObject("listForRE",
					resourceLocator.getTaskService().getREList(crn, performer, subjectName, subjectID, teamId));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}
	}

	public ModelAndView getCaseManagerList(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getCaseManagerList");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			this.logger.debug("getCaseManagerList before getting values ");
			viewForJSON.addObject("listOfCM", resourceLocator.getTaskService().getCaseManagerList());
			this.logger.debug("getCaseManagerList After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView getClientReqData(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getClientReqData");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String clientName = "";
			if (httpServletRequest.getParameter("clientName") != null) {
				clientName = httpServletRequest.getParameter("clientName");
			}

			this.logger.debug("value of clientCode is " + clientName);
			viewForJSON.addObject("clientReqData", resourceLocator.getTaskService().getClientReqData(clientName));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView getSubReportType(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getSubReportType");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String reportType = "";
			if (httpServletRequest.getParameter("reportType") != null) {
				reportType = httpServletRequest.getParameter("reportType");
			}

			this.logger.debug("value of Report Type is " + reportType);
			viewForJSON.addObject("subReportTypeList", resourceLocator.getTaskService().getSubReportTypes(reportType));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView isSubjectAdded(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside isSubjectAdded");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String crn = "";
			if (httpServletRequest.getParameter("crn") != null) {
				crn = httpServletRequest.getParameter("crn");
			}

			this.logger.debug("value of CRN is " + crn);
			viewForJSON.addObject("isSubAdded", resourceLocator.getTaskService().isSubjectAdded(crn));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView getClientList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getClientList");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String action = "";
			if (httpServletRequest.getParameter("action") != null) {
				action = httpServletRequest.getParameter("action");
			}

			this.logger.debug(" getClientList value of action is " + action);
			if (action != null && "byName".equalsIgnoreCase(action)) {
				viewForJSON.addObject("clientListSorted", resourceLocator.getTaskService().getClientByName());
			} else if (action != null && "byCode".equalsIgnoreCase(action)) {
				viewForJSON.addObject("clientListSorted", resourceLocator.getTaskService().getCLientByCode());
			} else {
				viewForJSON.addObject("clientListSorted", resourceLocator.getTaskService().getCLientByCode());
				this.logger.debug("getClientList No action found in client list json call. So using default action");
			}

			this.logger.debug("getClientList After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView getRDDForST(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getRDDForST");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String teamTypeList = httpServletRequest.getParameter("teamTypeList");
			String officeList = httpServletRequest.getParameter("officeList");
			String int1RDD = httpServletRequest.getParameter("int1RDD");
			String int2RDD = httpServletRequest.getParameter("int2RDD");
			String ptRDD = httpServletRequest.getParameter("ptRDD");
			String daysBeforeList = httpServletRequest.getParameter("daysBeforeList");
			CaseDetails caseDetails = ResourceLocator.self().getCaseDetailService()
					.getCaseInfo(httpServletRequest.getParameter("crn"));
			String receivedDate = caseDetails.getReqRecdDate1();
			this.logger.debug("teamTypeList :::: " + teamTypeList);
			this.logger.debug("officeList :::: " + officeList);
			this.logger.debug("int1RDD :::: " + int1RDD);
			this.logger.debug("int2RDD :::: " + int2RDD);
			this.logger.debug("ptRDD :::: " + ptRDD);
			this.logger.debug("daysBeforeList :::: " + daysBeforeList);
			this.logger.debug("receivedDate :::: " + receivedDate);
			String[] teamTypeArray = teamTypeList.split(",");
			String[] officeArray = officeList.split(",");
			String[] daysBeforeArray = daysBeforeList.split(",");
			String[] stInt1RDDArray = new String[officeArray.length];
			String[] stInt2RDDArray = new String[officeArray.length];
			String[] stFinalRDDArray = new String[officeArray.length];

			for (int i = 0; i < officeArray.length; ++i) {
				String teamType = teamTypeArray[i];
				String office = officeArray[i];
				int daysBefore = Integer.parseInt(daysBeforeArray[i]);
				if (httpServletRequest.getParameter("int1RDD") != null
						&& !httpServletRequest.getParameter("int1RDD").isEmpty()) {
					stInt1RDDArray[i] = resourceLocator.getTaskService().getRDDForST(teamType, office, int1RDD,
							daysBefore, receivedDate);
					this.logger.debug("stInt1RDD >>>> " + stInt1RDDArray[i]);
				}

				if (httpServletRequest.getParameter("int2RDD") != null
						&& !httpServletRequest.getParameter("int2RDD").isEmpty()) {
					stInt2RDDArray[i] = resourceLocator.getTaskService().getRDDForST(teamType, office, int2RDD,
							daysBefore, receivedDate);
					this.logger.debug("stInt2RDD >>>> " + stInt2RDDArray[i]);
				}

				stFinalRDDArray[i] = resourceLocator.getTaskService().getRDDForST(teamType, office, ptRDD, daysBefore,
						receivedDate);
				this.logger.debug("stFinalRDD >>>> " + stFinalRDDArray[i]);
			}

			this.logger.debug("stInt1RDDArray.length - " + stInt1RDDArray.length + ", stInt2RDDArray.length - "
					+ stInt2RDDArray.length + ", stFinalRDDArray.length - " + stFinalRDDArray.length);
			viewForJSON.addObject("stInt1RDDArray", stInt1RDDArray);
			viewForJSON.addObject("stInt2RDDArray", stInt2RDDArray);
			viewForJSON.addObject("stFinalRDDArray", stFinalRDDArray);
			return viewForJSON;
		} catch (NullPointerException var23) {
			return AtlasUtils.getExceptionView(this.logger, var23);
		} catch (Exception var24) {
			return AtlasUtils.getExceptionView(this.logger, var24);
		}
	}

	public ModelAndView getDueDates(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getDueDates");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String reportType = "";
			String interimDate = null;
			String officeName = "";
			String receivedDate = "";
			if (httpServletRequest.getParameter("reportType") != null) {
				reportType = httpServletRequest.getParameter("reportType");
			}

			if (httpServletRequest.getParameter("Office") != null) {
				officeName = httpServletRequest.getParameter("Office");
			}

			if (httpServletRequest.getParameter("interimDate") != null
					&& !"".equalsIgnoreCase(httpServletRequest.getParameter("interimDate"))) {
				interimDate = httpServletRequest.getParameter("interimDate");
			}

			if (httpServletRequest.getParameter("receivedDate") != null
					&& !"".equalsIgnoreCase(httpServletRequest.getParameter("receivedDate"))) {
				receivedDate = httpServletRequest.getParameter("receivedDate");
			}

			this.logger.debug("value of reportType is " + reportType);
			this.logger.debug("value of interimDate is " + interimDate);
			viewForJSON.addObject("getDueDates", resourceLocator.getTaskService().getAllDatesFormat(reportType,
					interimDate, officeName, receivedDate));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView checkHolidayWeakend(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		String holidayName = "";
		this.logger.debug("inside checkHolidayWeakend");

		try {
			String office = "";
			String date = "";
			if (httpServletRequest.getParameter("selectedDate") != null) {
				date = httpServletRequest.getParameter("selectedDate");
			}

			if (httpServletRequest.getParameter("selectedOffice") != null) {
				office = httpServletRequest.getParameter("selectedOffice");
			}

			ResourceLocator resourceLocator = ResourceLocator.self();
			holidayName = resourceLocator.getTaskService().checkHolidayWeakend(date, office);
			if (holidayName != null && holidayName.length() > 0) {
				viewForJSON.addObject("holidayExist", "yes");
				viewForJSON.addObject("holidayName", holidayName);
			} else {
				viewForJSON.addObject("holidayExist", "no");
			}

			return viewForJSON;
		} catch (NullPointerException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView checkHolidayWeakendForCaseOffice(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside checkHolidayWeakend");

		try {
			String office = "";
			String date = "";
			if (httpServletRequest.getParameter("selectedDate") != null) {
				date = httpServletRequest.getParameter("selectedDate");
			}

			if (httpServletRequest.getParameter("selectedOffice") != null) {
				office = httpServletRequest.getParameter("selectedOffice");
			}

			ResourceLocator resourceLocator = ResourceLocator.self();
			if (resourceLocator.getTaskService().checkHolidayWeakendForCaseOffice(date, office)) {
				viewForJSON.addObject("holidayExist", "yes");
			} else {
				viewForJSON.addObject("holidayExist", "no");
			}

			return viewForJSON;
		} catch (NullPointerException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	public ModelAndView checkHolidayWeakendCDD(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		String holidayOfficeList = "";
		this.logger.debug("inside checkHolidayWeakendCDD");

		try {
			String date = "";
			if (httpServletRequest.getParameter("selectedDate") != null) {
				date = httpServletRequest.getParameter("selectedDate");
			}

			ResourceLocator resourceLocator = ResourceLocator.self();
			holidayOfficeList = resourceLocator.getTaskService().checkHolidayWeakendCDD(date);
			if (holidayOfficeList != null && holidayOfficeList.length() > 0) {
				viewForJSON.addObject("holidayExist", "yes");
				viewForJSON.addObject("holidayOfficeList", holidayOfficeList);
			} else {
				viewForJSON.addObject("holidayExist", "no");
			}

			return viewForJSON;
		} catch (NullPointerException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	public ModelAndView getSubReportTypesList(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getSubReportTypesList");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			this.logger.debug("getSubReportTypesList before getting values ");
			viewForJSON.addObject("listOfSubReportTypes", resourceLocator.getTaskService().getSubReportTypesList());
			this.logger.debug("getSubReportTypesList After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView checkHolidayWeakendAnalyst(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside checkHolidayWeakendAnalyst");
		String holidayName = "";

		try {
			String analyst = "";
			String date = "";
			if (httpServletRequest.getParameter("selectedDate") != null) {
				date = httpServletRequest.getParameter("selectedDate");
			}

			if (httpServletRequest.getParameter("selectedAnalyst") != null) {
				analyst = httpServletRequest.getParameter("selectedAnalyst");
			}

			ResourceLocator resourceLocator = ResourceLocator.self();
			holidayName = resourceLocator.getTaskService().checkHolidayWeakendAnalyst(date, analyst);
			if (holidayName != null && holidayName.length() > 0) {
				viewForJSON.addObject("holidayExist", "yes");
				viewForJSON.addObject("holidayName", holidayName);
			} else {
				viewForJSON.addObject("holidayExist", "no");
			}

			return viewForJSON;
		} catch (NullPointerException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView addSubAndCompleteCase(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("Request is " + httpServletRequest.getParameterMap());
		SubjectDetails subDetails = new SubjectDetails();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(subDetails);
		binder.bind(httpServletRequest);
		this.logger.debug("subject details subject name is " + subDetails.getSubjectName());
		this.logger.debug("inside addSubAndCompleteCase");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			UserDetailsBean userDetailsBean = (UserDetailsBean) httpServletRequest.getSession()
					.getAttribute("userDetailsBean");
			String currentUser = userDetailsBean.getLoginUserId();
			subDetails.setUpdatedBy(currentUser);
			String caseHistoryPerformer = "";
			if (httpServletRequest.getSession().getAttribute("loginLevel") != null) {
				caseHistoryPerformer = (String) httpServletRequest.getSession().getAttribute("performedBy");
			}

			resourceLocator.getSubjectService().saveNewSubjectForCase(subDetails,
					SBMUtils.getSession(httpServletRequest), caseHistoryPerformer);
			viewForJSON.addObject("status", "ok");
			return viewForJSON;
		} catch (NullPointerException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}
	}

	public ModelAndView editSubAndCompleteCase(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside editSubAndCompleteCase");
		this.logger.debug("Request is " + httpServletRequest.getParameterMap());
		SubjectDetails subDetails = new SubjectDetails();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(subDetails);
		binder.bind(httpServletRequest);
		this.logger.debug("subject details subject name is " + subDetails.getSubjectName());

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			subDetails.setSubjectId(Integer.parseInt(httpServletRequest.getParameter("subjectID")));
			UserDetailsBean userDetailsBean = (UserDetailsBean) httpServletRequest.getSession()
					.getAttribute("userDetailsBean");
			String currentUser = userDetailsBean.getLoginUserId();
			subDetails.setUpdatedBy(currentUser);
			String caseHistoryPerformer = "";
			if (httpServletRequest.getSession().getAttribute("loginLevel") != null) {
				caseHistoryPerformer = (String) httpServletRequest.getSession().getAttribute("performedBy");
			}

			resourceLocator.getSubjectService().saveSubjectForCase(subDetails, SBMUtils.getSession(httpServletRequest),
					caseHistoryPerformer);
			viewForJSON.addObject("status", "ok");
			return viewForJSON;
		} catch (NullPointerException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}
	}

	public ModelAndView addSubIndustry(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			CaseDetails caseDetails) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside addIndustryAndSave");
		this.logger.debug("Request is " + httpServletRequest.getParameterMap());

		try {
			String[] modifiedRecords = caseDetails.getModifiedRecords();
			UserDetailsBean userDetailsBean = (UserDetailsBean) httpServletRequest.getSession()
					.getAttribute("userDetailsBean");
			String currentUser = userDetailsBean.getLoginUserId();
			int i = ResourceLocator.self().getSubjectService().saveSubjectIndustry(modifiedRecords, currentUser);
			this.logger.debug("after saveSubjectIndustry call " + i);
			viewForJSON.addObject("status", "ok");
			return viewForJSON;
		} catch (NullPointerException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView getOfficeForClient(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getOfficeForClient");
		String clientName = httpServletRequest.getParameter("clientName");
		if (clientName != null && !"".equalsIgnoreCase(clientName)) {
			try {
				ResourceLocator resourceLocator = ResourceLocator.self();
				viewForJSON.addObject("officeName", resourceLocator.getTaskService().getOfficeForClient(clientName));
				this.logger.debug("After getting values exit from method");
			} catch (NullPointerException var6) {
				return AtlasUtils.getExceptionView(this.logger, var6);
			} catch (Exception var7) {
				return AtlasUtils.getExceptionView(this.logger, var7);
			}
		} else {
			viewForJSON.addObject("officeName", "");
		}

		return viewForJSON;
	}

	public ModelAndView getCountryDBForCountry(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getCountry name for retriever section");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String countryId = "";
			if (httpServletRequest.getParameter("countryId") != null) {
				countryId = httpServletRequest.getParameter("countryId");
			}

			HashMap<String, Object> paramMap = new HashMap();
			int start = Integer
					.parseInt(StringUtils.checkPaginationParams("start", httpServletRequest.getParameter("start")));
			int limit = Integer
					.parseInt(StringUtils.checkPaginationParams("limit", httpServletRequest.getParameter("limit")));
			limit += start;
			paramMap.put("start", start + 1);
			paramMap.put("limit", limit);
			paramMap.put("countryId", countryId);
			this.logger.debug("value of countryId is :: " + countryId);
			viewForJSON.addObject("listForCntryDBStore",
					resourceLocator.getTaskService().getCountryDBForCountry(paramMap));
			viewForJSON.addObject("total", resourceLocator.getTaskService().getCountryDBForCountryCount(countryId));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView getOfficeId(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getOfficeId name for document section");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String userId = httpServletRequest.getParameter("userId");
			this.logger.debug("value of userId is :: " + userId);
			viewForJSON.addObject("getOfficeId", resourceLocator.getTaskService().getOfficeId(userId));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView getOfficeCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getOfficeCode method");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String userId = httpServletRequest.getParameter("userId");
			this.logger.debug("value of userId is :: " + userId);
			viewForJSON.addObject("getOfficeCode", resourceLocator.getTaskService().getOfficeCode(userId));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView hasDeletePermission(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside hasDeletePermission name for document section");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String userId = httpServletRequest.getParameter("userId");
			this.logger.debug("value of userId is :: " + userId);
			viewForJSON.addObject("hasDeletePermission", resourceLocator.getTaskService().hasDeletePermission(userId));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView hasUploadPermission(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside hasUploadPermission name for document section");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String userId = httpServletRequest.getParameter("userId");
			this.logger.debug("value of userId is :: " + userId);
			viewForJSON.addObject("hasUploadPermission", resourceLocator.getTaskService().hasUploadPermission(userId));
			this.logger.debug("After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView checkISISAvailability(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		boolean isISISReady = true;
		boolean cbdConfirmed = true;

		try {
			String crn = httpServletRequest.getParameter("crn");
			ResourceLocator resourceLocator = ResourceLocator.self();
			this.logger.debug("crn is " + crn);
			if (resourceLocator.getTaskService().getISISStatus(crn) == 1) {
				this.logger.debug("The case for crn " + crn + " is a ISIS case.");
				if (resourceLocator.getAtlasWebServiceClient().pingISIS()) {
					this.logger.debug("THe webservices are up ");
					if (resourceLocator.getTaskService().getCBDValue(crn) == 0) {
						this.logger.debug("oops budget is not confimred");
						cbdConfirmed = false;
					}
				} else {
					isISISReady = false;
				}
			}
		} catch (NullPointerException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}

		this.logger.debug("ISISValue is " + isISISReady);
		this.logger.debug("cbdConfirmed is " + cbdConfirmed);
		viewForJSON.addObject("ISISValue", isISISReady);
		viewForJSON.addObject("cbdConfirmed", cbdConfirmed);
		return viewForJSON;
	}

	public ModelAndView updateOptionalRiskData(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside updateOptionalRiskData");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String crn = request.getParameter("crn");
			String riskCode = request.getParameter("riskCode");
			String catId = request.getParameter("catId");
			long isRiskApplied = Long.parseLong(request.getParameter("isRiskApplied"));
			long subID = Long.parseLong(request.getParameter("subID"));
			long hasHBDRisk = Long.parseLong(request.getParameter("hasHBDRisk"));
			long countryId = Long.parseLong(request.getParameter("countryId"));
			String firstAttrName = request.getParameter("firstAttrName");
			String secondAttrName = request.getParameter("secondAttrName");
			String firstAttrValue = request.getParameter("firstAttrValue");
			String secondAttrValue = request.getParameter("secondAttrValue");
			String tempTaskName = request.getParameter("taskNme");
			this.logger.debug("Temp task name:" + tempTaskName);
			String[] temp = (String[]) null;
			if (tempTaskName != null) {
				temp = tempTaskName.split("::");
			}

			String taskName = "";
			if (temp != null && temp.length >= 2) {
				taskName = temp[1];
			}

			this.logger.debug("Task name:" + taskName);
			Session session = SBMUtils.getSession(request);
			RiskHistory rh = new RiskHistory();
			rh.setCRN(crn);
			rh.setRiskCategoryId(Long.parseLong(catId));
			rh.setRiskCode(riskCode);
			rh.setCountryMasterId(countryId);
			rh.setSubjectId(subID);
			rh.setAction("Delete");
			rh.setTask(taskName);
			rh.setUpdatedBy(session.getUser());
			String info = "";
			if (firstAttrValue != null && firstAttrValue.trim().length() > 0) {
				info = "<b>" + firstAttrName + "</b>:" + firstAttrValue;
			}

			if (secondAttrValue != null && secondAttrValue.trim().length() > 0) {
				if (info.trim().length() > 0) {
					info = info + "<br/>";
				}

				info = info + "<b>" + secondAttrName + "</b>:" + secondAttrValue;
			}

			rh.setOldInfo(info);
			rh.setNewInfo("");
			resourceLocator.getTaskService().updateOptionalRiskDataHistory(rh);
			RiskProfileVO rVO = new RiskProfileVO();
			rVO.setCRN(crn);
			rVO.setRiskCode(riskCode);
			rVO.setRiskCategoryId(Long.parseLong(catId));
			rVO.setIsApplied(isRiskApplied);
			rVO.setSubjectId(subID);
			rVO.setUpdatedBy(session.getUser());
			rVO.setHasCountryBreakDown(hasHBDRisk);
			rVO.setCountryId(countryId);
			int x = resourceLocator.getTaskService().updateOptionalRiskData(rVO);
			this.logger.debug("inside updateOptionalRiskData::" + x);
			return viewForJSON;
		} catch (NullPointerException var28) {
			return AtlasUtils.getExceptionView(this.logger, var28);
		} catch (Exception var29) {
			return AtlasUtils.getExceptionView(this.logger, var29);
		}
	}

	public ModelAndView getCategoryDetails(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside getCategoryDetails");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			List<CategoryDetailsVO> categoryList = resourceLocator.getTaskService().getCategoryDetails();
			viewForJSON.addObject("categoryList", categoryList);
			this.logger.debug("exit getCategoryDetails");
			return viewForJSON;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView insertOptionalRiskData(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside insertOptionalRiskData");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String crn = request.getParameter("crn");
			String riskCode = request.getParameter("riskCode");
			String catId = request.getParameter("catId");
			Session session = SBMUtils.getSession(request);
			long subID = Long.parseLong(request.getParameter("subjectId"));
			long riskTypeValue = Long.parseLong(request.getParameter("riskTypeValue"));
			String firstRiskAttributeDefaultValue = request.getParameter("firstRiskAttributeDefaultValue");
			String firstAttributeIdForCombo = request.getParameter("firstAttributeIdForCombo");
			String secondAttributeIdForCombo = request.getParameter("secondAttributeIdForCombo");
			long hasHBDRisk = Long.parseLong(request.getParameter("hasHBDRisk"));
			long visibleToClient = Long.parseLong(request.getParameter("visibleToClient"));
			this.logger.debug("crn::" + crn);
			this.logger.debug("riskCode::" + riskCode);
			this.logger.debug("insertOptionalRiskData visible to client.." + visibleToClient);
			String firstAttrName = request.getParameter("firstAttrName");
			this.logger.debug("First Attr Name:" + firstAttrName);
			String secondAttrName = request.getParameter("secondAttrName");
			this.logger.debug("Second Attr Name:" + secondAttrName);
			String firstAttrValue = request.getParameter("firstRiskAttributeDefaultValue");
			this.logger.debug("First Attr Value:" + firstAttrValue);
			String secondAttrValue = request.getParameter("secondRiskAttributeDefaultValue");
			this.logger.debug("Second Attr Value:" + secondAttrValue);
			String tempTaskName = request.getParameter("taskNme");
			this.logger.debug("Temp task name:" + tempTaskName);
			String[] temp = (String[]) null;
			if (tempTaskName != null) {
				temp = tempTaskName.split("::");
			}

			String taskName = "";
			if (temp != null && temp.length >= 2) {
				taskName = temp[1];
			}

			this.logger.debug("Task name:" + taskName);
			List<RiskProfileVO> lstNewRiskVO = new ArrayList();
			List<CountryHBDVO> countryHBDResponseList = new ArrayList();
			RiskHistory rh;
			RiskProfileVO rVO;
			if (hasHBDRisk == 0L) {
				rh = new RiskHistory();
				rh.setCRN(crn);
				rh.setRiskCategoryId(Long.parseLong(catId));
				rh.setRiskCode(riskCode);
				rh.setCountryMasterId(0L);
				rh.setSubjectId(subID);
				rh.setAction("Add");
				rh.setTask(taskName);
				rh.setUpdatedBy(session.getUser());
				rh.setOldInfo("");
				rh.setNewInfo("<b>" + firstAttrName + "</b>:" + firstAttrValue + "<br/><b>" + secondAttrName + "</b>:"
						+ secondAttrValue);
				resourceLocator.getTaskService().updateOptionalRiskDataHistory(rh);
				rVO = new RiskProfileVO();
				rVO.setRiskProfileId(resourceLocator.getTaskService().getNextProfileId());
				rVO.setRiskCategoryId(Long.parseLong(catId));
				rVO.setCRN(crn);
				rVO.setRiskCode(riskCode);
				rVO.setAttributeId(firstAttributeIdForCombo);
				rVO.setNewattrValue(firstRiskAttributeDefaultValue);
				rVO.setSubjectId(subID);
				rVO.setRiskType(riskTypeValue);
				rVO.setUpdatedBy(session.getUser());
				rVO.setVisibleToClient(visibleToClient);
				RiskProfileVO rVOSecond = new RiskProfileVO();
				rVOSecond.setRiskProfileId(resourceLocator.getTaskService().getNextProfileId());
				rVOSecond.setRiskCategoryId(Long.parseLong(catId));
				rVOSecond.setCRN(crn);
				rVOSecond.setRiskCode(riskCode);
				rVOSecond.setAttributeId(secondAttributeIdForCombo);
				rVOSecond.setNewattrValue("");
				rVOSecond.setSubjectId(subID);
				rVOSecond.setRiskType(riskTypeValue);
				rVOSecond.setUpdatedBy(session.getUser());
				rVOSecond.setVisibleToClient(visibleToClient);
				lstNewRiskVO.add(rVO);
				lstNewRiskVO.add(rVOSecond);
			} else {
				rh = new RiskHistory();
				rh.setCRN(crn);
				rh.setRiskCategoryId(Long.parseLong(catId));
				rh.setRiskCode(riskCode);
				rh.setCountryMasterId(0L);
				rh.setSubjectId(subID);
				rh.setAction("Add");
				rh.setTask(taskName);
				rh.setUpdatedBy(session.getUser());
				resourceLocator.getTaskService().updateOptionalRiskDataHistory(rh);
				rVO = new RiskProfileVO();
				long profileId = resourceLocator.getTaskService().getNextProfileId();
				rVO.setRiskProfileId(profileId);
				rVO.setRiskCategoryId(Long.parseLong(catId));
				rVO.setCRN(crn);
				rVO.setRiskCode(riskCode);
				rVO.setAttributeId("");
				rVO.setSubjectId(subID);
				rVO.setRiskType(riskTypeValue);
				rVO.setUpdatedBy(session.getUser());
				rVO.setVisibleToClient(visibleToClient);
				lstNewRiskVO.add(rVO);
				Long subIdInString = subID;
				List<CountryHBDVO> countryList = resourceLocator.getTaskService().getCountries(crn,
						subIdInString.toString());
				Iterator var36 = countryList.iterator();

				while (var36.hasNext()) {
					CountryHBDVO cntryHBDVO = (CountryHBDVO) var36.next();
					CountryHBDVO chbd = new CountryHBDVO();
					chbd.setProfileId(profileId);
					chbd.setAttributeId(Long.parseLong(firstAttributeIdForCombo));
					chbd.setAttributeValue(firstRiskAttributeDefaultValue);
					chbd.setSubjectId(subID);
					chbd.setCountryMasterId(cntryHBDVO.getCountryMasterId());
					chbd.setCountryName(cntryHBDVO.getCountryName());
					chbd.setUpdatedBy(session.getUser());
					CountryHBDVO chbdSecond = new CountryHBDVO();
					chbdSecond.setProfileId(profileId);
					chbdSecond.setAttributeId(Long.parseLong(secondAttributeIdForCombo));
					chbdSecond.setAttributeValue("");
					chbdSecond.setSubjectId(subID);
					chbdSecond.setCountryMasterId(cntryHBDVO.getCountryMasterId());
					chbdSecond.setCountryName(cntryHBDVO.getCountryName());
					chbdSecond.setUpdatedBy(session.getUser());
					countryHBDResponseList.add(chbd);
					countryHBDResponseList.add(chbdSecond);
					RiskHistory rh1 = new RiskHistory();
					rh1.setCRN(crn);
					rh1.setRiskCategoryId(Long.parseLong(catId));
					rh1.setRiskCode(riskCode);
					rh1.setCountryMasterId(cntryHBDVO.getCountryMasterId());
					rh1.setSubjectId(subID);
					rh1.setAction("Add");
					rh1.setTask(taskName);
					rh1.setUpdatedBy(session.getUser());
					rh1.setNewInfo("<b>" + firstAttrName + "</b>:" + firstRiskAttributeDefaultValue + "<br/><b>"
							+ secondAttrName + "</b>:");
					resourceLocator.getTaskService().updateOptionalRiskDataHistory(rh1);
				}
			}

			int x = resourceLocator.getTaskService().insertOptionalRiskData(lstNewRiskVO, countryHBDResponseList);
			this.logger.debug("inside insertOptionalRiskData::" + x);
			viewForJSON.addObject("countryHBDResponseList", countryHBDResponseList);
			return viewForJSON;
		} catch (NullPointerException var40) {
			return AtlasUtils.getExceptionView(this.logger, var40);
		} catch (Exception var41) {
			return AtlasUtils.getExceptionView(this.logger, var41);
		}
	}

	public ModelAndView insertHBDCountryRiskData(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside insertHBDRiskData");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			Session session = SBMUtils.getSession(request);
			String crn = request.getParameter("crn");
			String riskCode = request.getParameter("riskCode");
			long catId = Long.parseLong(request.getParameter("catId"));
			long subID = Long.parseLong(request.getParameter("subjectId"));
			long riskTypeValue = Long.parseLong(request.getParameter("riskTypeValue"));
			String firstRiskAttributeDefaultValue = request.getParameter("firstRiskAttributeDefaultValue");
			String firstAttributeIdForCombo = request.getParameter("firstAttributeIdForCombo");
			String secondAttributeIdForCombo = request.getParameter("secondAttributeIdForCombo");
			long countryId = Long.parseLong(request.getParameter("countryId"));
			List<RiskProfileVO> riskForProfileIDList = new ArrayList();
			String firstAttrName = request.getParameter("firstAttrName");
			this.logger.debug("First Attr Name:" + firstAttrName);
			String secondAttrName = request.getParameter("secondAttrName");
			this.logger.debug("Second Attr Name:" + secondAttrName);
			String firstAttrValue = request.getParameter("firstRiskAttributeDefaultValue");
			this.logger.debug("First Attr Value:" + firstAttrValue);
			String secondAttrValue = request.getParameter("secondRiskAttributeDefaultValue");
			this.logger.debug("Second Attr Value:" + secondAttrValue);
			String tempTaskName = request.getParameter("taskNme");
			this.logger.debug("Temp task name:" + tempTaskName);
			String[] temp = (String[]) null;
			if (tempTaskName != null) {
				temp = tempTaskName.split("::");
			}

			String taskName = "";
			if (temp != null && temp.length >= 2) {
				taskName = temp[1];
			}

			this.logger.debug("Task name:" + taskName);
			RiskHistory rh = new RiskHistory();
			rh.setCRN(crn);
			rh.setRiskCategoryId(catId);
			rh.setRiskCode(riskCode);
			rh.setCountryMasterId(countryId);
			rh.setSubjectId(subID);
			rh.setAction("Add");
			rh.setTask(taskName);
			rh.setUpdatedBy(session.getUser());
			rh.setOldInfo("");
			rh.setNewInfo("<b>" + firstAttrName + "</b>:" + firstAttrValue + "<br/><b>" + secondAttrName + "</b>:"
					+ secondAttrValue);
			resourceLocator.getTaskService().updateOptionalRiskDataHistory(rh);
			RiskProfileVO rpVO = new RiskProfileVO();
			rpVO.setCRN(crn);
			rpVO.setRiskCode(riskCode);
			rpVO.setRiskCategoryId(catId);
			rpVO.setSubjectId(subID);
			riskForProfileIDList.add(rpVO);
			long riskProfileId = resourceLocator.getCaseDetailService().fetchProfileId(riskForProfileIDList);
			List<CountryHBDVO> cntryHBDVOList = new ArrayList();
			CountryHBDVO chbd = new CountryHBDVO();
			chbd.setProfileId(riskProfileId);
			chbd.setAttributeId(Long.parseLong(firstAttributeIdForCombo));
			chbd.setAttributeValue(firstRiskAttributeDefaultValue);
			chbd.setSubjectId(subID);
			chbd.setCountryMasterId(countryId);
			chbd.setUpdatedBy(session.getUser());
			CountryHBDVO chbdSecond = new CountryHBDVO();
			chbdSecond.setProfileId(riskProfileId);
			chbdSecond.setAttributeId(Long.parseLong(secondAttributeIdForCombo));
			chbdSecond.setAttributeValue("");
			chbdSecond.setSubjectId(subID);
			chbdSecond.setCountryMasterId(countryId);
			chbdSecond.setUpdatedBy(session.getUser());
			cntryHBDVOList.add(chbd);
			cntryHBDVOList.add(chbdSecond);
			resourceLocator.getTaskService().insertHBDCountryRiskData(cntryHBDVOList);
			List<CountryHBDVO> countryHBDResponseList = new ArrayList();
			viewForJSON.addObject("countryHBDResponseList", countryHBDResponseList);
		} catch (Exception var35) {
			return AtlasUtils.getExceptionView(this.logger, var35);
		}

		this.logger.debug("exit insertHBDRiskData");
		return viewForJSON;
	}

	public ModelAndView saveAfterCaseCompletionCategoryRisk(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside updateOptionalRiskData");
		boolean processFlag = true;
		String[] temp = (String[]) null;
		String crn = "";
		this.logger.debug("in saveAfterCaseCompletionCategoryRisk.....");

		try {
			Session session = SBMUtils.getSession(request);
			ResourceLocator resourceLocator = ResourceLocator.self();
			String userName = session.getUser();
			crn = request.getParameter("crn");
			String workItemName = request.getParameter("workItem");
			String taskName = "";
			String loggedInUser = session.getUser();
			String riskData;
			String riskHBDData;
			String subIndustryData;
			String riskAggrData;
			String totalRiskAggrData;
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
				this.logger.debug("riskData::" + riskData.length());
				subjectAggregationList = new ArrayList();
				if (riskData.trim().length() > 0) {
					subjectAggregationList = this.createRiskDataList(riskData, request.getParameter("crn"), userName);
				}

				this.logger.debug("riskData ==>" + ((List) subjectAggregationList).size());
				riskHBDData = request.getParameter(this.RISK_HBD_DATA);
				this.logger.debug("RiskHBDDAta::" + riskHBDData.length());
				List<RiskProfileVO> riskProfileListWithHBD = new ArrayList();
				if (riskHBDData.trim().length() > 0) {
					new ArrayList();
					List<RiskProfileVO> riskForProfileIDList = this.getProfileList(riskHBDData,
							request.getParameter("crn"), userName);
					long riskProfileId = resourceLocator.getCaseDetailService().fetchProfileId(riskForProfileIDList);
					riskProfileListWithHBD = this.createRiskDataWithHBDList(riskHBDData, request.getParameter("crn"),
							userName, riskProfileId);
				}

				this.logger.debug("Risk data with HBD==>" + ((List) riskProfileListWithHBD).size());
				subIndustryData = request.getParameter(this.SUB_INDUSTRY_DATA);
				this.logger.debug("SubIndustry" + subIndustryData);
				riskAggrData = request.getParameter(this.RISK_AGGR_DATA);
				List<RiskAggregationVO> riskAggregationList = new ArrayList();
				if (riskAggrData.trim().length() > 0) {
					riskAggregationList = this.createRiskAggregationData(riskAggrData, userName);
				}

				totalRiskAggrData = request.getParameter(this.TOTAL_RISK_AGGR_DATA);
				this.logger.debug("totalRiskAggrData.." + totalRiskAggrData);
				List<TotalRiskAggregationVO> totalRiskAggrList = new ArrayList();
				if (totalRiskAggrData.trim().length() > 0) {
					totalRiskAggrList = this.createTotalRiskAggregationData(totalRiskAggrData, userName);
				}

				this.logger.debug("totalRiskAggrData list size::" + ((List) totalRiskAggrList).size());
				RiskProfile riskProfile = this.setRiskProfileISISVo(crn, (List) subjectAggregationList,
						(List) riskProfileListWithHBD, (List) null, (List) riskAggregationList,
						(List) totalRiskAggrList, userName);
				clientCaseStatusVO.setOtherinformation(riskProfile);
				processFlag = resourceLocator.getAtlasWebServiceClient().updateStatus(clientCaseStatusVO);
			}

			if (processFlag) {
				riskData = request.getParameter(this.RISKDATA);
				this.logger.debug("riskData::" + riskData.length());
				List<RiskProfileVO> riskProfileList = new ArrayList();
				if (riskData.trim().length() > 0) {
					riskProfileList = this.createRiskDataList(riskData, request.getParameter("crn"), userName);
				}

				this.logger.debug("riskData ==>" + ((List) riskProfileList).size());
				subIndustryData = request.getParameter(this.SUB_INDUSTRY_DATA);
				this.logger.debug("SubIndustry" + subIndustryData);
				List<RiskProfileVO> subIndusList = new ArrayList();
				if (subIndustryData.trim().length() > 0) {
					subIndusList = this.createIndustryCodeWithSubjectId(subIndustryData, crn, loggedInUser);
				}

				this.logger.debug("sub list size::" + ((List) subIndusList).size());
				riskHBDData = request.getParameter(this.RISK_HBD_DATA);
				this.logger.debug("RiskHBDDAta::" + riskHBDData.length());
				List<RiskProfileVO> riskProfileListWithHBD = new ArrayList();
				if (riskHBDData.trim().length() > 0) {
					new ArrayList();
					List<RiskProfileVO> riskForProfileIDList = this.getProfileList(riskHBDData,
							request.getParameter("crn"), userName);
					long riskProfileId = resourceLocator.getCaseDetailService().fetchProfileId(riskForProfileIDList);
					riskProfileListWithHBD = this.createRiskDataWithHBDList(riskHBDData, request.getParameter("crn"),
							userName, riskProfileId);
				}

				this.logger.debug("Risk data with HBD==>" + ((List) riskProfileListWithHBD).size());
				subIndustryData = request.getParameter(this.SUB_INDUSTRY_DATA);
				this.logger.debug("SubIndustry" + subIndustryData);
				riskAggrData = request.getParameter(this.RISK_AGGR_DATA);
				this.logger.debug("riskAggrData.." + riskAggrData);
				List<RiskAggregationVO> riskAggregationList = new ArrayList();
				if (riskAggrData.trim().length() > 0) {
					riskAggregationList = this.createRiskAggregationData(riskAggrData, userName);
				}

				totalRiskAggrData = request.getParameter(this.TOTAL_RISK_AGGR_DATA);
				this.logger.debug("totalRiskAggrData.." + totalRiskAggrData);
				List<TotalRiskAggregationVO> totalRiskAggrList = new ArrayList();
				if (totalRiskAggrData.trim().length() > 0) {
					totalRiskAggrList = this.createTotalRiskAggregationData(totalRiskAggrData, userName);
				}

				this.logger.debug("totalRiskAggrData list size::" + ((List) totalRiskAggrList).size());
				subjectAggregationList = new ArrayList();
				if (crn != null && crn.trim().length() > 0) {
					int totalRiskAggrId = resourceLocator.getCaseDetailService().fetchTotalAggrId(crn);
					String eachSubjectAggregationData = request.getParameter("eachSubjectAggregationDataObject");
					this.logger.debug("eachSubjectAggregationData.." + eachSubjectAggregationData);
					if (totalRiskAggrId > 0 && eachSubjectAggregationData != null
							&& eachSubjectAggregationData.trim().length() > 0) {
						subjectAggregationList = this.createSubjectAggregationData(eachSubjectAggregationData, crn,
								loggedInUser, totalRiskAggrId);
					}
				}

				long x = resourceLocator.getTaskService().saveCategoryRisk((List) riskProfileList,
						(List) riskProfileListWithHBD, workItemName, session, (List) riskAggregationList,
						(List) totalRiskAggrList, (List) subIndusList);
				this.logger.debug("inside saveAfterCaseCompletionCategoryRisk::" + x);
				if (((List) subjectAggregationList).size() > 0) {
					resourceLocator.getCaseDetailService().saveSubjectAggregation((List) subjectAggregationList);
				}
			}

			return viewForJSON;
		} catch (NullPointerException var28) {
			return AtlasUtils.getExceptionView(this.logger, var28);
		} catch (Exception var29) {
			return AtlasUtils.getExceptionView(this.logger, var29);
		}
	}

	private List createSubjectAggregationData(String eachSubjectAggregationData, String crn, String loggedInUser,
			int totalRiskAggrId) {
		List slAggrList = new ArrayList();
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

	public ModelAndView saveCategoryRisk(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		this.logger.debug("inside updateOptionalRiskData");
		String[] temp = (String[]) null;
		this.logger.debug("in saveCategoryRisk..");

		try {
			Session session = SBMUtils.getSession(request);
			ResourceLocator resourceLocator = ResourceLocator.self();
			String userName = session.getUser();
			String riskData = request.getParameter(this.RISKDATA);
			this.logger.debug("riskData::" + riskData.length());
			String workItemName = request.getParameter("workItem");
			if (workItemName != null) {
				temp = workItemName.split("::");
			}

			List<RiskProfileVO> riskProfileList = new ArrayList();
			String crn = request.getParameter("crn");
			String taskName = "";
			if (temp.length >= 2) {
				taskName = temp[1];
			}

			String loggedInUser = session.getUser();
			if (riskData.trim().length() > 0) {
				riskProfileList = this.createRiskDataList(riskData, request.getParameter("crn"), userName);
			}

			this.logger.debug("riskData ==>" + ((List) riskProfileList).size());
			String subIndustryData = request.getParameter(this.SUB_INDUSTRY_DATA);
			this.logger.debug("SubIndustry" + subIndustryData);
			List<RiskProfileVO> subIndusList = new ArrayList();
			if (subIndustryData.trim().length() > 0) {
				subIndusList = this.createIndustryCodeWithSubjectId(subIndustryData, crn, loggedInUser);
			}

			this.logger.debug("sub list size::" + ((List) subIndusList).size());
			String riskHBDData = request.getParameter(this.RISK_HBD_DATA);
			this.logger.debug("RiskHBDDAta::" + riskHBDData.length());
			List<RiskProfileVO> riskProfileListWithHBD = new ArrayList();
			if (riskHBDData.trim().length() > 0) {
				new ArrayList();
				List<RiskProfileVO> riskForProfileIDList = this.getProfileList(riskHBDData, crn, userName);
				long riskProfileId = resourceLocator.getCaseDetailService().fetchProfileId(riskForProfileIDList);
				this.logger.debug("riskprofileID.." + riskProfileId);
				riskProfileListWithHBD = this.createRiskDataWithHBDList(riskHBDData, crn, userName, riskProfileId);
			}

			this.logger.debug("Risk data with HBD==>" + ((List) riskProfileListWithHBD).size());
			subIndustryData = request.getParameter(this.SUB_INDUSTRY_DATA);
			this.logger.debug("SubIndustry" + subIndustryData);
			String riskAggrData = request.getParameter(this.RISK_AGGR_DATA);
			List<RiskAggregationVO> riskAggregationList = new ArrayList();
			if (riskAggrData.trim().length() > 0) {
				riskAggregationList = this.createRiskAggregationData(riskAggrData, userName);
			}

			String totalRiskAggrData = request.getParameter(this.TOTAL_RISK_AGGR_DATA);
			this.logger.debug("totalRiskAggrData.." + totalRiskAggrData);
			List<TotalRiskAggregationVO> totalRiskAggrList = new ArrayList();
			if (totalRiskAggrData.trim().length() > 0) {
				totalRiskAggrList = this.createTotalRiskAggregationData(totalRiskAggrData, userName);
			}

			this.logger.debug("totalRiskAggrData list size::" + ((List) totalRiskAggrList).size());
			List subjectAggregationList = new ArrayList();
			if (crn != null && crn.trim().length() > 0) {
				int totalRiskAggrId = resourceLocator.getCaseDetailService().fetchTotalAggrId(crn);
				String eachSubjectAggregationData = request.getParameter("eachSubjectAggregationDataObject");
				this.logger.debug("eachSubjectAggregationData.." + eachSubjectAggregationData);
				if (totalRiskAggrId > 0 && eachSubjectAggregationData != null
						&& eachSubjectAggregationData.trim().length() > 0) {
					subjectAggregationList = this.createSubjectAggregationData(eachSubjectAggregationData, crn,
							loggedInUser, totalRiskAggrId);
				}
			}

			long x = resourceLocator.getTaskService().saveCategoryRisk((List) riskProfileList,
					(List) riskProfileListWithHBD, taskName, session, (List) riskAggregationList,
					(List) totalRiskAggrList, (List) subIndusList);
			this.logger.debug("inside saveCategoryRisk::" + x);
			if (((List) subjectAggregationList).size() > 0) {
				resourceLocator.getCaseDetailService().saveSubjectAggregation((List) subjectAggregationList);
			}

			return viewForJSON;
		} catch (NullPointerException var25) {
			return AtlasUtils.getExceptionView(this.logger, var25);
		} catch (Exception var26) {
			return AtlasUtils.getExceptionView(this.logger, var26);
		}
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

	private List<RiskAggregationVO> createRiskAggregationData(String riskAggrData, String userName) {
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

			riskAggrVO.setUpdatedBy(userName);
			riskAggregationList.add(riskAggrVO);
		}

		return riskAggregationList;
	}

	public RiskProfile setRiskProfileISISVo(String crn, List<RiskProfileVO> riskProfileList,
			List<RiskProfileVO> riskProfileListHBD, List<RiskProfileVO> subIndusList,
			List<RiskAggregationVO> riskAggrVO, List<TotalRiskAggregationVO> totalRiskAggrVO, String loggedInUser) {
		ResourceLocator resourceLocator = ResourceLocator.self();
		RiskProfile riskProfile = new RiskProfile();
		if (riskProfileList.size() > 0) {
			riskProfile.setCRNHasRiskData(1);
		}

		CRNRiskData crnRiskData = new CRNRiskData();
		crnRiskData.setCRN(crn);
		String orderId = null;

		try {
			orderId = resourceLocator.getTaskService().getISISOrderId(crn);
		} catch (CMSException var77) {
			var77.printStackTrace();
		}

		crnRiskData.setOrderID(orderId);
		RiskAggregation totalCRNLevelRiskAggregationVO = new RiskAggregation();
		Iterator<TotalRiskAggregationVO> totalCRNRiskAggrItr = totalRiskAggrVO.iterator();
		if (totalCRNRiskAggrItr.hasNext()) {
			TotalRiskAggregationVO totalCrnRiskAggr = (TotalRiskAggregationVO) totalCRNRiskAggrItr.next();
			totalCRNLevelRiskAggregationVO.setTotalCRNRiskSummary(totalCrnRiskAggr.getTotalCRNLevelAggrValue());
		}

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

		caseLevelRiskData.setRiskAggregation(totalCaseLevelRiskAggrVO);
		RiskCategory[] riskCatagories = (RiskCategory[]) null;
		Set<Long> categorySet = new HashSet();
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

		int l;
		RiskProfileVO riskProfileVO;
		RiskProfileVO riskProfileVO;
		HashSet subRiskCodeSet;
		ArrayList riskProfileNewList;
		for (l = 0; l < categorySet.size(); ++l) {
			List<RiskProfileVO> riskProfileNewList = new ArrayList();
			List<RiskProfileVO> riskProfileNewListHBD = new ArrayList();
			ArrayList<Long> categoryList = new ArrayList(categorySet);
			Long categoryId = (Long) categoryList.get(l);
			RiskCategory riskCategory = new RiskCategory();
			riskCategory.setCategoryID(Integer.parseInt(categoryId.toString()));
			String categoryName = null;

			try {
				categoryName = resourceLocator.getTaskService().getCategoryLabel(categoryId);
			} catch (CMSException var76) {
				var76.printStackTrace();
			}

			riskCategory.setCategoryLabel(categoryName);
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

				int i;
				Risk riskVO;
				RiskProfileVO riskProfileNewListVo;
				for (i = 0; i < riskProfileNewList.size(); ++i) {
					riskVO = new Risk();
					riskProfileNewListVo = (RiskProfileVO) riskProfileNewList.get(i);
					riskVO.setCode(riskProfileNewListVo.getRiskCode());
					riskVO.setLabel(riskProfileNewListVo.getRiskLabel());
					riskVO.setRiskGroup(riskProfileNewListVo.getIsRiskMandatory());
					riskVO.setRiskIsActive(1);
					riskVO.setDisplayOnProfile(1);
					riskVO.setRiskHasCountryBreakDown(0);
					Attribute[] caseLevelAttrArr = new Attribute[2];
					Attribute outerAttrVO = new Attribute();
					outerAttrVO.setKey(riskProfileNewListVo.getAttributeId());
					outerAttrVO.setValue(riskProfileNewListVo.getNewattrValue());
					caseLevelAttrArr[0] = outerAttrVO;

					for (l = i + 1; l < riskProfileNewList.size(); ++l) {
						riskProfileVO = (RiskProfileVO) riskProfileNewList.get(l);
						Attribute innerAttrVO = new Attribute();
						if (riskProfileVO.getRiskCode().equals(riskProfileNewListVo.getRiskCode())) {
							innerAttrVO.setKey(riskProfileVO.getAttributeId());
							innerAttrVO.setValue(riskProfileVO.getNewattrValue());
							caseLevelAttrArr[1] = innerAttrVO;
							riskProfileNewList.remove(riskProfileNewListVo);
							riskProfileNewList.remove(riskProfileVO);
							break;
						}
					}

					riskVO.setAttributes(caseLevelAttrArr);
					riskArray[k] = riskVO;
					++k;
				}

				for (i = 0; i < riskProfileNewListHBD.size(); ++i) {
					riskVO = new Risk();
					riskProfileNewListVo = (RiskProfileVO) riskProfileNewListHBD.get(i);
					riskVO.setCode(riskProfileNewListVo.getRiskCode());
					riskVO.setLabel(riskProfileNewListVo.getRiskLabel());
					riskVO.setRiskGroup(riskProfileNewListVo.getIsRiskMandatory());
					riskVO.setRiskIsActive(1);
					riskVO.setDisplayOnProfile(1);
					riskVO.setRiskHasCountryBreakDown(1);
					subRiskCodeSet = new HashSet();
					HashMap<String, Set<Long>> riskCountryMap = new HashMap();

					for (l = 0; l < riskProfileNewListHBD.size(); ++l) {
						riskProfileVO = (RiskProfileVO) riskProfileNewListHBD.get(l);
						if (riskProfileVO.getRiskCode().equals(riskProfileNewListVo.getRiskCode())) {
							subRiskCodeSet.add(riskProfileVO.getCountryId());
						}

						riskCountryMap.put(riskProfileNewListVo.getRiskCode(), subRiskCodeSet);
					}

					Set<Long> cntryIdTempSet = (Set) riskCountryMap.get(riskProfileNewListVo.getRiskCode());
					riskProfileNewList = new ArrayList(cntryIdTempSet);
					Country[] countryArr = new Country[riskProfileNewList.size()];
					long cntryId = 0L;
					String countryCode = null;

					for (int n = 0; n < riskProfileNewList.size(); ++n) {
						cntryId = (Long) riskProfileNewList.get(i);
						Country country = new Country();

						try {
							countryCode = resourceLocator.getTaskService().getCountryCode(cntryId);
						} catch (CMSException var75) {
							var75.printStackTrace();
						}

						country.setCountry(countryCode);
						Attribute[] caseLevelAttrArr = (Attribute[]) null;
						List<Attribute> attrArrList = new ArrayList();

						for (int m = 0; m < riskProfileNewListHBD.size(); ++m) {
							riskProfileVO = (RiskProfileVO) riskProfileNewListHBD.get(m);
							if (riskProfileVO.getRiskCode().equals(riskProfileNewListVo.getRiskCode())
									&& riskProfileVO.getCountryId() == cntryId) {
								Attribute outerAttrVO = new Attribute();
								outerAttrVO.setKey(riskProfileNewListVo.getAttributeId());
								outerAttrVO.setValue(riskProfileNewListVo.getNewattrValue());
								attrArrList.add(outerAttrVO);
							}
						}

						caseLevelAttrArr = new Attribute[attrArrList.size()];
						attrArrList.toArray(caseLevelAttrArr);
						country.setAttributes(caseLevelAttrArr);
						countryArr[n] = country;
					}

					CountryBreakDown cntryBrkDown = new CountryBreakDown();
					cntryBrkDown.setCountries(countryArr);
					riskVO.setCOuntryBreak(cntryBrkDown);
					riskArray[k] = riskVO;
					++k;
				}

				riskCategory.setRisks(riskArray);
				riskCatagories[l] = riskCategory;
				break;
			}
		}

		caseLevelRiskData.setRiskCatagories(riskCatagories);
		crnRiskData.setCaseLevelRisks(caseLevelRiskData);
		SubjectLevelRisks subjectLevelRisksData = new SubjectLevelRisks();
		Subjects subjects = new Subjects();
		Subject[] subArr = (Subject[]) null;
		RiskAggregation totalSubjetLevelRiskAggrVO = new RiskAggregation();
		Iterator<TotalRiskAggregationVO> totalSubjectLevelAggrItr = totalRiskAggrVO.iterator();
		if (totalSubjectLevelAggrItr.hasNext()) {
			TotalRiskAggregationVO totalSubjectRiskAggr = (TotalRiskAggregationVO) totalSubjectLevelAggrItr.next();
			totalSubjetLevelRiskAggrVO.setTotalSubjectRiskSummary(totalSubjectRiskAggr.getTotalSubLevelAggrValue());
		}

		subjects.setRiskAggregation(totalSubjetLevelRiskAggrVO);
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
			long subjectId = (Long) subjectListItr.next();
			Subject subVo = new Subject();

			try {
				subVo = resourceLocator.getTaskService().getSubjectDetailsForISIS(subjectId);
			} catch (CMSException var74) {
				var74.printStackTrace();
			}

			Iterator riskSubLevelCategoryItr = riskProfileCommonList.iterator();

			while (riskSubLevelCategoryItr.hasNext()) {
				RiskProfileVO riskProfileSubjectLevelRiskVO = (RiskProfileVO) riskSubLevelCategoryItr.next();
				if (riskProfileSubjectLevelRiskVO.getRiskType() == 2L
						&& riskProfileSubjectLevelRiskVO.getSubjectId() == subjectId) {
					categorySubSet.add(riskProfileSubjectLevelRiskVO.getRiskCategoryId());
				}
			}

			riskSubCatagories = new RiskCategory[categorySubSet.size()];
			subRiskCodeSet = new HashSet();
			Set<String> subRiskCodeSetHBD = new HashSet();

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
				riskProfileNewList = new ArrayList();
				List<RiskProfileVO> riskProfileNewListHBD = new ArrayList();
				ArrayList<Long> categoryList = new ArrayList(categorySubSet);
				Long categoryId = (Long) categoryList.get(l);
				RiskCategory riskCategory = new RiskCategory();
				riskCategory.setCategoryID(Integer.parseInt(categoryId.toString()));
				String categoryName = null;

				try {
					categoryName = resourceLocator.getTaskService().getCategoryLabel(categoryId);
				} catch (CMSException var73) {
					var73.printStackTrace();
				}

				riskCategory.setCategoryLabel(categoryName);
				Iterator<RiskAggregationVO> riskAggrVOItr = riskAggrVO.iterator();
				RiskAggregation riskAggForCategoryWise = new RiskAggregation();

				while (riskAggrVOItr.hasNext()) {
					RiskAggregationVO riskSubLvlCategoryAggrVo = (RiskAggregationVO) riskAggrVOItr.next();
					if (riskSubLvlCategoryAggrVo.getCatId() == categoryId
							&& riskSubLvlCategoryAggrVo.getRiskType() == 2L
							&& riskSubLvlCategoryAggrVo.getSubId() == subjectId) {
						riskAggForCategoryWise.setCategoryRiskSummary(
								Integer.parseInt(Long.toString(riskSubLvlCategoryAggrVo.getAggrValue())));
					}
				}

				Risk[] riskArray = (Risk[]) null;
				Iterator riskProfileVOItr = riskProfileCommonList.iterator();

				while (true) {
					while (riskProfileVOItr.hasNext()) {
						riskProfileVO = (RiskProfileVO) riskProfileVOItr.next();
						if (riskProfileVO.getRiskCategoryId() == categoryId && riskProfileVO.getRiskType() == 2L
								&& riskProfileVO.getSubjectId() == subjectId
								&& riskCodeSet.contains(riskProfileVO.getRiskCode())) {
							riskProfileNewList.add(riskProfileVO);
						} else if (riskProfileVO.getRiskCategoryId() == categoryId && riskProfileVO.getRiskType() == 2L
								&& riskProfileVO.getSubjectId() == subjectId
								&& riskCodeSetHBD.contains(riskProfileVO.getRiskCode())) {
							riskProfileNewListHBD.add(riskProfileVO);
						}
					}

					riskArray = new Risk[riskCodeSet.size() + riskCodeSetHBD.size()];
					int k = 0;

					Risk riskVO;
					RiskProfileVO riskProfileNewListVo;
					int j;
					RiskProfileVO riskProfileVo;
					int i;
					for (i = 0; i < riskProfileNewList.size(); ++i) {
						riskVO = new Risk();
						riskProfileNewListVo = (RiskProfileVO) riskProfileNewList.get(i);
						riskVO.setCode(riskProfileNewListVo.getRiskCode());
						riskVO.setLabel(riskProfileNewListVo.getRiskLabel());
						riskVO.setRiskGroup(riskProfileNewListVo.getIsRiskMandatory());
						riskVO.setRiskIsActive(1);
						riskVO.setDisplayOnProfile(1);
						riskVO.setRiskHasCountryBreakDown(0);
						Attribute[] caseLevelAttrArr = new Attribute[2];
						Attribute outerAttrVO = new Attribute();
						outerAttrVO.setKey(riskProfileNewListVo.getAttributeId());
						outerAttrVO.setValue(riskProfileNewListVo.getNewattrValue());
						caseLevelAttrArr[0] = outerAttrVO;

						for (j = i + 1; j < riskProfileNewList.size(); ++j) {
							riskProfileVo = (RiskProfileVO) riskProfileNewList.get(j);
							Attribute innerAttrVO = new Attribute();
							if (riskProfileVo.getRiskCode().equals(riskProfileNewListVo.getRiskCode())) {
								innerAttrVO.setKey(riskProfileVo.getAttributeId());
								innerAttrVO.setValue(riskProfileVo.getNewattrValue());
								caseLevelAttrArr[1] = innerAttrVO;
								riskProfileNewList.remove(riskProfileNewListVo);
								riskProfileNewList.remove(riskProfileVo);
								break;
							}
						}

						riskVO.setAttributes(caseLevelAttrArr);
						riskArray[k] = riskVO;
						++k;
					}

					for (i = 0; i < riskProfileNewListHBD.size(); ++i) {
						riskVO = new Risk();
						riskProfileNewListVo = (RiskProfileVO) riskProfileNewListHBD.get(i);
						riskVO.setCode(riskProfileNewListVo.getRiskCode());
						riskVO.setLabel(riskProfileNewListVo.getRiskLabel());
						riskVO.setRiskGroup(riskProfileNewListVo.getIsRiskMandatory());
						riskVO.setRiskIsActive(1);
						riskVO.setDisplayOnProfile(1);
						riskVO.setRiskHasCountryBreakDown(1);
						Set<Long> countryIdSet = new HashSet();
						HashMap<String, Set<Long>> riskCountryMap = new HashMap();

						for (j = 0; j < riskProfileNewListHBD.size(); ++j) {
							riskProfileVo = (RiskProfileVO) riskProfileNewListHBD.get(j);
							if (riskProfileVo.getRiskCode().equals(riskProfileNewListVo.getRiskCode())) {
								countryIdSet.add(riskProfileVo.getCountryId());
							}

							riskCountryMap.put(riskProfileNewListVo.getRiskCode(), countryIdSet);
						}

						Set<Long> cntryIdTempSet = (Set) riskCountryMap.get(riskProfileNewListVo.getRiskCode());
						List<Long> cntryTempList = new ArrayList(cntryIdTempSet);
						Country[] countryArr = new Country[cntryTempList.size()];
						long cntryId = 0L;
						String countryCode = null;

						for (int n = 0; n < cntryTempList.size(); ++n) {
							cntryId = (Long) cntryTempList.get(i);
							Country country = new Country();

							try {
								countryCode = resourceLocator.getTaskService().getCountryCode(cntryId);
							} catch (CMSException var72) {
								var72.printStackTrace();
							}

							country.setCountry(countryCode);
							Attribute[] caseLevelAttrArr = (Attribute[]) null;
							List<Attribute> attrArrList = new ArrayList();

							for (int m = 0; m < riskProfileNewListHBD.size(); ++m) {
								RiskProfileVO riskProfileVo = (RiskProfileVO) riskProfileNewListHBD.get(m);
								if (riskProfileVo.getRiskCode().equals(riskProfileNewListVo.getRiskCode())
										&& riskProfileVo.getCountryId() == cntryId) {
									Attribute outerAttrVO = new Attribute();
									outerAttrVO.setKey(riskProfileNewListVo.getAttributeId());
									outerAttrVO.setValue(riskProfileNewListVo.getNewattrValue());
									attrArrList.add(outerAttrVO);
								}
							}

							caseLevelAttrArr = new Attribute[attrArrList.size()];
							attrArrList.toArray(caseLevelAttrArr);
							country.setAttributes(caseLevelAttrArr);
							countryArr[n] = country;
						}

						CountryBreakDown cntryBrkDown = new CountryBreakDown();
						cntryBrkDown.setCountries(countryArr);
						riskVO.setCOuntryBreak(cntryBrkDown);
						riskArray[k] = riskVO;
						++k;
					}

					riskCategory.setRisks(riskArray);
					riskSubCatagories[l] = riskCategory;
					break;
				}
			}

			subVo.setRiskCategories(riskSubCatagories);
			subArr[subCount] = subVo;
		}

		subjects.setSubject(subArr);
		subjectLevelRisksData.setSubjects(subjects);
		crnRiskData.setSubjectLevelRisks(subjectLevelRisksData);
		riskProfile.setCRNRiskData(crnRiskData);
		return riskProfile;
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

	private List<RiskProfileVO> createRiskDataWithHBDList(String riskProfileData, String crn, String userName,
			long riskProfileId) {
		List<RiskProfileVO> riskProfileList = new ArrayList();
		String[] records = riskProfileData.split(this.SEPERATOR3);
		this.logger.debug("length of records(No. of risks)" + records.length);

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
						} else {
							this.logger.debug("No Match found");
						}

						++k;
					}
				}

				riskProVO.setCRN(crn);
				riskProVO.setUpdatedBy(userName);
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
						ResourceLocator.self().getCaseDetailService().fetchProfileId(riskForProfileIDList));
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
						this.logger.debug("getRiskCode.." + riskProVO.getRiskCode());
					} else if (attributes[k].equalsIgnoreCase(this.ATTR7)) {
						riskProVO.setRiskCategoryId(Long.parseLong(attributes[k + 1]));
						this.logger.debug("getRiskCategory.." + riskProVO.getRiskCategoryId());
					} else if (attributes[k].equalsIgnoreCase(this.ATT_SUB_ID)) {
						riskProVO.setSubjectId(Long.parseLong(attributes[k + 1]));
						this.logger.debug("getSubject.." + riskProVO.getSubjectId());
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

	public ModelAndView getClientMappingList(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView("jsonView");
		new ArrayList();
		this.logger.debug("inside getClientMappingList");

		try {
			ResourceLocator resourceLocator = ResourceLocator.self();
			String action = "";
			String riskCode = "";
			if (httpServletRequest.getParameter("action") != null) {
				action = httpServletRequest.getParameter("action");
			}

			if (httpServletRequest.getParameter("riskCode") != null) {
				riskCode = httpServletRequest.getParameter("riskCode");
			}

			this.logger.debug(" getClientMappingList value of action is " + action);
			this.logger.debug(" getClientMappingList value of mapping is " + riskCode);
			if (action != null && "byName".equalsIgnoreCase(action)) {
				viewForJSON.addObject("clientMappingListSorted", resourceLocator.getTaskService().getClientByName());
				this.logger
						.debug("getClientMappingList No action found in client list json call. using BY_NAME action");
			} else if (action != null && "byCode".equalsIgnoreCase(action)) {
				viewForJSON.addObject("clientMappingListSorted",
						resourceLocator.getTaskService().getClientMappingMasterByCode(riskCode));
				this.logger
						.debug("getClientMappingList No action found in client list json call. using BY_CODE action");
			} else {
				viewForJSON.addObject("clientMappingListSorted", resourceLocator.getTaskService().getCLientByCode());
				this.logger.debug(
						"getClientMappingList No action found in client list json call. So using default action");
			}

			this.logger.debug("getClientMappingList After getting values exit from method");
			return viewForJSON;
		} catch (NullPointerException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}
}