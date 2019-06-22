package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IRiskMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.RiskAttributesMasterVO;
import com.worldcheck.atlas.vo.masters.RiskCategoryMasterVO;
import com.worldcheck.atlas.vo.masters.RisksHistoryVO;
import com.worldcheck.atlas.vo.masters.RisksMapVO;
import com.worldcheck.atlas.vo.masters.RisksMasterVO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

public class JSONRiskMaster extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.masters.JSONRiskMaster");
	private String JSONVIEW = "jsonView";
	private String RISKGRIDLIST = "riskGridList";
	private String SUCCESS = "success";
	private String RESULT = "result";
	private String TRUE = "true";
	private String FALSE = "false";
	private String BLANK = "";
	private int count;
	private String RISKCODE = "riskCode";
	private String RISK_STATUS = "riskStatus";
	private String RESULT_MSG = "resultMsg";
	private String riskCode;
	private String riskStatus;
	private ModelAndView mv = null;
	private String Category_Id = "categoryId";
	private String ACTION = "action";
	private String UPDATE = "update";
	private String resultMsg = "";
	private String RISKLIST = "riskList";
	private String RISK_HISTORY = "riskHistoryList";
	private String RISK_CATEGORIES = "allRiskCategories";
	private IRiskMaster riskManager;

	public void setRiskManager(IRiskMaster riskManager) {
		this.riskManager = riskManager;
	}

	public ModelAndView searchRisk(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		ModelAndView viewForJson = null;

		try {
			viewForJson = new ModelAndView(this.JSONVIEW);
			ArrayList riskGridList = null;
			if (this.BLANK.equals(risksMasterVO.getRisks()) && this.BLANK.equals(risksMasterVO.getStatus())) {
				riskGridList = new ArrayList();
			} else {
				riskGridList = (ArrayList) this.riskManager.getRiskGrid(risksMasterVO,
						Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
						request.getParameter("dir"));
			}

			this.count = this.riskManager.getRiskGridCount(risksMasterVO);
			viewForJson.addObject("total", this.count);
			viewForJson.addObject(this.RISKGRIDLIST, riskGridList);
			return viewForJson;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getRisks(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		ModelAndView viewForJson = null;

		try {
			viewForJson = new ModelAndView(this.JSONVIEW);
			ArrayList<RisksMasterVO> riskGridList = null;
			this.logger.debug("Risk Label::" + risksMasterVO.getRiskLabel() + " Risk Status::"
					+ risksMasterVO.getRiskActive() + " Risk Type:::" + risksMasterVO.getRiskType()
					+ " Visible To Client:::" + risksMasterVO.getVisibleToClient() + " Risk Group::"
					+ risksMasterVO.getRiskGroup() + " Report Types:::" + risksMasterVO.getSelectedReportTypes()
					+ " Sub Report Types:::" + risksMasterVO.getSelectedSubReportTypes() + " Client Codes::::"
					+ risksMasterVO.getSelectedClients() + " Research Elements:::"
					+ risksMasterVO.getSelectedResearchElements() + " Risk Categories:::"
					+ risksMasterVO.getSelectedRiskCategories() + " Subject Countries:::"
					+ risksMasterVO.getSelectedSubjectCountries() + " Start Date:::" + risksMasterVO.getFromDate()
					+ " End Date:::" + risksMasterVO.getToDate());
			if (!risksMasterVO.getSelectedClients().trim().isEmpty()
					&& !this.BLANK.equals(risksMasterVO.getSelectedClients())) {
				risksMasterVO.setClientCodeList(this.getFiltersList(risksMasterVO.getSelectedClients()));
			}

			if (!risksMasterVO.getSelectedSubjectCountries().trim().isEmpty()
					&& !this.BLANK.equals(risksMasterVO.getSelectedSubjectCountries())) {
				risksMasterVO.setSubjectCountryList(this.getFiltersList(risksMasterVO.getSelectedSubjectCountries()));
			}

			if (!risksMasterVO.getSelectedReportTypes().trim().isEmpty()
					&& !this.BLANK.equals(risksMasterVO.getSelectedReportTypes())) {
				risksMasterVO.setReportTypeList(this.getFiltersList(risksMasterVO.getSelectedReportTypes()));
			}

			if (!risksMasterVO.getSelectedSubReportTypes().trim().isEmpty()
					&& !this.BLANK.equals(risksMasterVO.getSelectedSubReportTypes())) {
				risksMasterVO.setSubReportTypeList(this.getFiltersList(risksMasterVO.getSelectedSubReportTypes()));
			}

			if (!risksMasterVO.getSelectedResearchElements().trim().isEmpty()
					&& !this.BLANK.equals(risksMasterVO.getSelectedResearchElements())) {
				risksMasterVO.setResearchElementList(this.getFiltersList(risksMasterVO.getSelectedResearchElements()));
			}

			if (!risksMasterVO.getSelectedRiskCategories().trim().isEmpty()
					&& !this.BLANK.equals(risksMasterVO.getSelectedRiskCategories())) {
				risksMasterVO.setRiskCategoryList(this.getFiltersList(risksMasterVO.getSelectedRiskCategories()));
			}

			riskGridList = (ArrayList) this.riskManager.getRiskGrid(risksMasterVO,
					Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("limit")),
					request.getParameter("sort"), request.getParameter("dir"));
			this.logger.debug("riskGridList length:::" + riskGridList.size());
			this.count = this.riskManager.getRisksCount(risksMasterVO);
			viewForJson.addObject("total", this.count);
			viewForJson.addObject(this.RISKGRIDLIST, riskGridList);
			return viewForJson;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getAllRisks(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		ModelAndView viewForJson = null;

		try {
			viewForJson = new ModelAndView(this.JSONVIEW);
			ArrayList riskList = null;
			this.logger.debug("riskCode-->>" + risksMasterVO.getRiskCode());
			riskList = (ArrayList) this.riskManager.getAllRisks(risksMasterVO);
			this.logger.debug("in controller-- riskGridList" + riskList.size());
			viewForJson.addObject(this.RISKLIST, riskList);
			return viewForJson;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView changeRiskStatus(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		ModelAndView mv = null;
		String resultMessage = null;
		ArrayList risksList = new ArrayList();

		try {
			mv = new ModelAndView(this.JSONVIEW);
			this.logger.debug(" Change the Status of Risk in changeRiskStatus method::::");
			this.riskCode = request.getParameter(this.RISKCODE);
			JSONObject jsonObject = null;
			String riskData = request.getParameter("hiddenData");
			this.logger.debug("data------>>" + riskData);
			JSONArray jsonArray = new JSONArray(riskData);
			this.logger.debug("jsonArray" + jsonArray);
			this.logger.debug("jsonArray.length()--->>" + jsonArray.length());
			int riskJsonArrayLength = jsonArray.length();

			for (int i = 0; i < riskJsonArrayLength; ++i) {
				RisksMasterVO riskMaserDataVO = new RisksMasterVO();
				jsonObject = jsonArray.getJSONObject(i);
				this.logger.debug("json object returned is ----" + jsonObject);
				riskMaserDataVO.setRiskCode(jsonObject.get("riskCode").toString());
				riskMaserDataVO.setRiskLabel(jsonObject.get("riskLabel").toString());
				riskMaserDataVO.setRiskCategory(jsonObject.get("riskCategory").toString());
				riskMaserDataVO.setRisksStatus(jsonObject.get("riskActive").toString());
				riskMaserDataVO
						.setDisplayOnProfileForm(Integer.parseInt(jsonObject.get("displayOnProfileForm").toString()));
				riskMaserDataVO.setRemarks(jsonObject.get("remarks").toString());
				risksList.add(i, riskMaserDataVO);
			}

			this.riskStatus = request.getParameter(this.RISK_STATUS);
			this.logger.debug("riskCodes: " + this.riskCode + "riskStatus :" + this.riskStatus);
			this.riskCode = this.riskCode.substring(0, this.riskCode.length() - 1);
			this.logger.debug("riskCodes: " + this.riskCode + "---" + "riskStatus :" + this.riskStatus);
			String[] riskCodeList = this.riskCode.split(",");
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			resultMessage = this.riskManager.changeRiskStatus(riskCodeList, this.riskStatus, userBean.getUserName(),
					risksList);
			mv.addObject(this.RESULT, resultMessage);
			return mv;
		} catch (CMSException var14) {
			return AtlasUtils.getJsonExceptionView(this.logger, var14, response);
		} catch (Exception var15) {
			return AtlasUtils.getJsonExceptionView(this.logger, var15, response);
		}
	}

	public ModelAndView getRiskCaseHistory(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		ModelAndView viewForJson = null;

		try {
			this.logger.debug("in getRiskCaseHistory ");
			viewForJson = new ModelAndView(this.JSONVIEW);
			ArrayList<RisksMasterVO> riskHistoryList = null;
			this.logger.debug(" Risk Code::" + risksMasterVO.getRiskcode());
			if (this.BLANK.equals(risksMasterVO.getRiskcode())) {
				riskHistoryList = new ArrayList();
			} else {
				riskHistoryList = (ArrayList) this.riskManager.getRiskCaseHistory(risksMasterVO,
						Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
						request.getParameter("dir"));
			}

			this.logger.debug("riskHistoryList length:::" + riskHistoryList.size());
			this.count = this.riskManager.getRiskCaseHistoryCount(risksMasterVO);
			viewForJson.addObject("total", this.count);
			viewForJson.addObject(this.RISK_HISTORY, riskHistoryList);
			return viewForJson;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView searchExistRisk(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		ModelAndView mv = null;
		this.logger.debug("Searching for Exist Currency");

		try {
			mv = new ModelAndView("jsonView");
			this.logger.debug("in search exist risk ===>>" + risksMasterVO.getRiskCategory());
			boolean resultRisk = this.riskManager.isExistRisk(risksMasterVO);
			if (resultRisk) {
				mv.addObject(this.SUCCESS, this.TRUE);
			} else {
				mv.addObject(this.SUCCESS, this.FALSE);
			}

			return mv;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView isRiskAssociated(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView("jsonView");
			this.logger.debug("Risk Master Code:" + risksMasterVO.getRiskCode());
			risksMasterVO = this.riskManager.checkAssociatedMaster(
					risksMasterVO.getRiskCode().substring(0, risksMasterVO.getRiskCode().length() - 1));
			if (!risksMasterVO.getTotalCRN().equalsIgnoreCase("0")
					|| !risksMasterVO.getSubjectCount().equalsIgnoreCase("0")) {
				mv.addObject("isAssociated", true);
				mv.addObject("risksMasterVO", risksMasterVO);
			}

			return mv;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getRiskCategory(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView("jsonView");
			List<RiskCategoryMasterVO> riskCategoryVO = this.riskManager.getRiskCategory();
			mv.addObject("riskCategoryList", riskCategoryVO);
			return mv;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView addMapping(HttpServletRequest request, HttpServletResponse response, RisksMapVO risksMapVO) {
		String resultMessage = null;
		int count = false;
		String msg = "";
		ModelAndView mv = null;
		this.logger.debug("Adding a Single MApping");

		try {
			mv = new ModelAndView("jsonView");
			HttpSession session = request.getSession();
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			risksMapVO.setUpdatedBy(userBean.getUserName());
			int mappingId = risksMapVO.getMappingId();
			if (mappingId == 0) {
				this.logger.debug("inside add Mapping method");
				this.logger.debug("values-->>" + risksMapVO.getRiskCode() + "--" + risksMapVO.getRiskMappingName()
						+ "--" + risksMapVO.getReportTypes() + "--" + risksMapVO.getSubReportTypes() + "--"
						+ risksMapVO.getResearchElements() + "--" + risksMapVO.getClientCodes() + "--"
						+ risksMapVO.getVisibleToClient() + "--" + risksMapVO.getRiskGroup() + "--"
						+ risksMapVO.getSubjectType());
				this.logger.debug("inside add Mapping method----countryCodes are ====" + risksMapVO.getCountryCodes());
				resultMessage = this.riskManager.addMapping(risksMapVO);
				this.logger.debug("reultMessage inside JSONRiskMAster addMapping::" + resultMessage.split("#")[1]);
				if (resultMessage.split("#")[1].equals("success")) {
					session.setAttribute("Message", "success");
					mv.addObject(this.SUCCESS, this.TRUE);
					mv.addObject("Message", "success");
				} else {
					session.setAttribute("Message", resultMessage.split("#")[0]);
					mv.addObject(this.SUCCESS, this.FALSE);
					mv.addObject("Message", resultMessage.split("#")[0]);
				}
			} else {
				msg = this.riskManager.updateMapping(risksMapVO);
				if (msg.split("#")[1].equals("success")) {
					session.setAttribute("Message", "success");
					mv.addObject(this.SUCCESS, this.TRUE);
					mv.addObject("Message", "success");
				} else if (msg.split("#")[1].equals("NoChange")) {
					session.setAttribute("Message", "NoChange");
					mv.addObject(this.SUCCESS, this.TRUE);
					mv.addObject("Message", "NoChange");
				} else {
					session.setAttribute("Message", msg.split("#")[0]);
					mv.addObject(this.SUCCESS, this.FALSE);
					mv.addObject("Message", msg.split("#")[0]);
				}
			}

			return mv;
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView getRiskAttributes(HttpServletRequest request, HttpServletResponse response,
			RisksMasterVO risksMasterVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView("jsonView");
			this.logger.debug("inside getRiskAttributes in JSON RISK MASTER & categoryId is ---->>"
					+ risksMasterVO.getRiskCategory());
			List<RiskAttributesMasterVO> riskAttributesVO = this.riskManager.getRiskAttributes(risksMasterVO);
			mv.addObject("riskAttributesList", riskAttributesVO);
			return mv;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView changeMappingStatus(HttpServletRequest request, HttpServletResponse response,
			RisksMapVO risksMapVO) {
		ModelAndView mv = null;
		String resultMessage = null;

		try {
			mv = new ModelAndView(this.JSONVIEW);
			this.logger.debug(" changing Mapping Status");
			this.logger.debug("mapping status --->>>" + risksMapVO.getMappingStatus() + "--->> mapping ID -->>"
					+ risksMapVO.getMappingId());
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			risksMapVO.setUpdatedBy(userBean.getUserName());
			resultMessage = this.riskManager.changeMappingStatus(risksMapVO);
			if (resultMessage.split("#")[1].equals("success")) {
				mv.addObject(this.RESULT, this.SUCCESS);
			} else {
				mv.addObject(this.RESULT, resultMessage.split("#")[0]);
			}

			if (risksMapVO.getMappingStatus() == 1) {
				this.logger.info(risksMapVO.getRiskMappingName() + " successfully deactivated");
			} else {
				this.logger.info(risksMapVO.getRiskMappingName() + " successfully activated");
			}

			return mv;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView getMappingHistory(HttpServletRequest request, HttpServletResponse response,
			RisksHistoryVO risksHistoryVO) {
		ModelAndView viewForJson = null;
		boolean var5 = false;

		try {
			viewForJson = new ModelAndView(this.JSONVIEW);
			ArrayList mappingList = null;
			this.logger.debug("risksHistoryVO.getMAPPING_ID/======" + risksHistoryVO.getMAPPING_ID());
			String mappingId = request.getParameter("MAPPING_ID");
			this.logger.debug("MAPPING_ID======" + mappingId);
			mappingList = (ArrayList) this.riskManager.getMappingHistory(risksHistoryVO,
					Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("limit")),
					request.getParameter("sort"), request.getParameter("dir"));
			int count = this.riskManager.getMappingHistoryGridCount(Integer.parseInt(mappingId));
			viewForJson.addObject("total", count);
			viewForJson.addObject("mappingHistoryList", mappingList);
			return viewForJson;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	private List<String> getFiltersList(String tempString) {
		List<String> tempList = new ArrayList();
		String[] tempArray = tempString.split(",");
		String[] var7 = tempArray;
		int var6 = tempArray.length;

		for (int var5 = 0; var5 < var6; ++var5) {
			String str = var7[var5];
			tempList.add(str);
		}

		return tempList;
	}

	public ModelAndView getLHSClientList(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView(this.JSONVIEW);
		this.logger.debug("inside getLHSClientList");

		try {
			String clientCodes = httpServletRequest.getParameter("clientlist");
			this.logger.debug("Client List received from Form=====");
			this.logger.debug(clientCodes);
			List<ClientMasterVO> clientCodeList = this.riskManager.getLHSClientList(clientCodes);
			viewForJSON.addObject("clientListSortedLHS", clientCodeList);
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView getRHSClientList(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView(this.JSONVIEW);
		this.logger.debug("inside getRHSClientList");

		try {
			String clientCodes = httpServletRequest.getParameter("clientlist");
			this.logger.debug("Client List received from Form=====");
			this.logger.debug(clientCodes);
			List<ClientMasterVO> clientCodeList = this.riskManager.getRHSClientList(clientCodes);
			viewForJSON.addObject("clientListSortedRHS", clientCodeList);
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView getLHSCountryList(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView(this.JSONVIEW);
		this.logger.debug("inside getLHSCountryList LHS");

		try {
			String countrylist = httpServletRequest.getParameter("countrylist");
			this.logger.debug("LHS - Country List received from Form=====");
			this.logger.debug(countrylist);
			List<CountryMasterVO> countryMasterList = this.riskManager.getLHSCountryList(countrylist);
			viewForJSON.addObject("countryListSortedLHS", countryMasterList);
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView getRHSCountryList(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView viewForJSON = new ModelAndView(this.JSONVIEW);
		this.logger.debug("inside getRHSCountryList RHS");

		try {
			String countrylist = httpServletRequest.getParameter("countrylist");
			this.logger.debug("RHS - Country List received from Form=====");
			this.logger.debug(countrylist);
			List<CountryMasterVO> countryMasterList = this.riskManager.getRHSCountryList(countrylist);
			viewForJSON.addObject("countryListSortedRHS", countryMasterList);
			return viewForJSON;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}
}