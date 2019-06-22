package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.bl.interfaces.IREMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.ResearchElementGroupMasterVO;
import com.worldcheck.atlas.vo.masters.ResearchElementMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class ReSearchElementMultiActionController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.ReSearchElementMultiActionController");
	private String RE_NAME_VAL = "reNameval";
	private String RE_STATUS_VAL = "reStatusval";
	private String RE_ENTITYTYPE_ID_VAL = "reEntityTypeIdval";
	private String RE_REMOVE_ENGLISH = "reRemEng";
	private String RE_BI_TEAM = "reBITeam";
	private String RE_SUPVEN_TEAM = "reSubVenTeam";
	private String RE_POINT_VAL = "rePointsval";
	private String GET_REG = "getREG";
	private String UPDATE_DONE = "updatedone";
	private String RE_CODE = "reCode";
	private String HRE_GROUP_ID = "hreGroupId";
	private String RE_INFO = "reInfo";
	private String IS_UPDATE = "isUpdate";
	private String GET_RE = "getRE";
	private String STATUS = "status";
	private String SEARCH_NAME = "searchResult";
	private String TOTAL = "total";
	private String ACTION = "action";
	private String SUCCESS = "success";
	private String TRUE = "true";
	private String FALSE = "false";
	private String GROUP_NAME = "groupName";
	private String MAIN_JSP = "research_element_master";
	private String ADD_JSP = "research_element_master_add";
	private String REDIRECT_RE_MASTER = "redirect:researchElement.do";
	private String COUNT = "count";
	private String researchElementgroup = "Research Element Group";
	private String researchElementCode = "Research Element Code";
	private String subjectType = "Subject Type";
	private String researchElements = "Research Elements";
	private String points = "Points";
	private String supportVendorTeam1 = "Support/Vendor Team 1";
	private String biTeam = "BI Team";
	private String researchElementType = "Research Element Type";
	private String status = "Research Element Status";
	private String remove_English = "Remove for English-Country";
	private String RE_MASTER = "Research Element Master";
	private ModelAndView mv = null;
	IREMaster reMultiActionManager = null;

	public void setReMultiActionManager(IREMaster reMultiActionManager) {
		this.reMultiActionManager = reMultiActionManager;
	}

	public ModelAndView researchElement(HttpServletRequest request, HttpServletResponse response,
			ResearchElementMasterVO researchElementMasterVO) {
		try {
			this.logger.debug("in researchElement");
			this.mv = new ModelAndView(this.MAIN_JSP);
			if (request.getSession().getAttribute(this.SUCCESS) != null) {
				if (request.getSession().getAttribute(this.SUCCESS).equals(this.TRUE)) {
					this.mv.addObject(this.SUCCESS, this.TRUE);
				} else {
					this.mv.addObject(this.SUCCESS, request.getSession().getAttribute(this.SUCCESS));
				}

				request.getSession().removeAttribute(this.SUCCESS);
			} else if (request.getSession().getAttribute(this.UPDATE_DONE) != null) {
				if (request.getSession().getAttribute(this.UPDATE_DONE).equals(this.TRUE)) {
					this.mv.addObject(this.UPDATE_DONE, this.TRUE);
				} else {
					this.mv.addObject(this.UPDATE_DONE, request.getSession().getAttribute(this.UPDATE_DONE));
				}

				request.getSession().removeAttribute(this.UPDATE_DONE);
			}
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}

		return this.mv;
	}

	public ModelAndView researchElementReportExport(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in researchElementReportExport");
		ModelAndView modelandview = null;

		try {
			String excelParams = request.getParameter("excelParams");
			this.logger.debug("excelParams::" + excelParams);
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			List<ResearchElementMasterVO> reDataList = this.reMultiActionManager.getReport(excelParamMap);
			this.logger.debug("fetched reDataList>>Size is " + reDataList.size());
			Map<String, Object> resultMap = this.writeToExcel(reDataList, response);
			modelandview = new ModelAndView("excelDownloadPopup");
			modelandview.addObject("fileBytes", resultMap.get("fileBytes"));
			modelandview.addObject("fileName", resultMap.get("fileName"));
			this.logger.debug("exiting  Export to Excel ResearchElementController");
			return modelandview;
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	private Map<String, Object> writeToExcel(List<ResearchElementMasterVO> reDataList, HttpServletResponse response)
			throws CMSException {
		try {
			List<String> lstHeader = new ArrayList();
			lstHeader.add(this.researchElementgroup);
			lstHeader.add(this.researchElementCode);
			lstHeader.add(this.researchElementType);
			lstHeader.add(this.subjectType);
			lstHeader.add(this.status);
			lstHeader.add(this.researchElements);
			lstHeader.add(this.points);
			lstHeader.add(this.supportVendorTeam1);
			lstHeader.add(this.remove_English);
			lstHeader.add(this.biTeam);
			List<LinkedHashMap<String, String>> dataList = new ArrayList();
			Iterator iterator = reDataList.iterator();

			while (iterator.hasNext()) {
				LinkedHashMap<String, String> datamap = new LinkedHashMap();
				ResearchElementMasterVO researchElementMasterVO = (ResearchElementMasterVO) iterator.next();
				datamap.put(this.researchElementgroup,
						String.valueOf(researchElementMasterVO.getResearchElementGroupName()));
				datamap.put(this.researchElementCode, String.valueOf(researchElementMasterVO.getrEMasterId()));
				datamap.put(this.researchElementType, String.valueOf(researchElementMasterVO.getResearchElementType()));
				datamap.put(this.subjectType, String.valueOf(researchElementMasterVO.getSubjectType()));
				datamap.put(this.status, String.valueOf(researchElementMasterVO.getResearchElementStatus()));
				datamap.put(this.researchElements, String.valueOf(researchElementMasterVO.getResearchElementName()));
				datamap.put(this.points, String.valueOf(researchElementMasterVO.getPoints()));
				datamap.put(this.supportVendorTeam1, String.valueOf(researchElementMasterVO.getSupportingVendorTeam()));
				datamap.put(this.remove_English, String.valueOf(researchElementMasterVO.getRemoveForEnglishCountry()));
				datamap.put(this.biTeam, String.valueOf(researchElementMasterVO.getBiTeam()));
				dataList.add(datamap);
			}

			return ExcelDownloader.writeToExcel(lstHeader, dataList, this.RE_MASTER, (short) 0, (short) 1, response,
					this.RE_MASTER);
		} catch (ClassCastException var8) {
			throw new CMSException(this.logger, var8);
		} catch (NullPointerException var9) {
			throw new CMSException(this.logger, var9);
		}
	}

	public ModelAndView addResearchElementAndNew(HttpServletRequest request, HttpServletResponse response,
			ResearchElementMasterVO researchElementMasterVO) {
		this.logger.debug("in addResearchElementAndNew ");
		String message = "";

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			researchElementMasterVO.setUpdatedBy(userName);
			if (Integer.parseInt(researchElementMasterVO.getResearchElementType()) != 0) {
				this.logger.debug("adding individual RE");
				researchElementMasterVO.setIsIndividual(1);
				researchElementMasterVO.setReGroupId(0);
				message = this.reMultiActionManager.addResearchElement(researchElementMasterVO);
				if (message.trim().length() > 0 && message.equals(this.SUCCESS)) {
					ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER");
					if (Integer.parseInt(researchElementMasterVO.getReEntityTypeId()) == 1) {
						ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER_INDIVIDUAL");
					} else {
						ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER_COMPANY");
					}
				}
			} else {
				this.logger.debug(
						"adding group REs for group name :: " + researchElementMasterVO.getResearchElementGroupName());
				int gId = this.reMultiActionManager.getGroupId(researchElementMasterVO.getResearchElementGroupName());
				this.logger.debug("gId :: " + gId);
				String[] modifiedRecords = researchElementMasterVO.getModifiedRecords();

				for (int i = 0; i < modifiedRecords.length; ++i) {
					message = "";
					researchElementMasterVO.setReGroupId(gId);
					String JSONstring = modifiedRecords[i];
					Map jsonObject = (Map) JSONValue.parse(JSONstring);
					researchElementMasterVO.setResearchElementName((String) jsonObject.get(this.RE_NAME_VAL));
					this.logger.debug("rename :: " + (String) jsonObject.get(this.RE_NAME_VAL) + " :: point :: "
							+ Float.parseFloat(jsonObject.get(this.RE_POINT_VAL).toString()));
					researchElementMasterVO.setPointval(Float.parseFloat(jsonObject.get(this.RE_POINT_VAL).toString()));
					researchElementMasterVO.setSupportingVendorTeam((String) jsonObject.get(this.RE_SUPVEN_TEAM));
					this.logger.debug("re supvenTeam :: " + (String) jsonObject.get(this.RE_SUPVEN_TEAM)
							+ " :: re BITeam :: " + (String) jsonObject.get(this.RE_BI_TEAM));
					researchElementMasterVO.setBiTeam((String) jsonObject.get(this.RE_BI_TEAM));
					researchElementMasterVO.setRemoveForEnglishCountry((String) jsonObject.get(this.RE_REMOVE_ENGLISH));
					researchElementMasterVO.setReEntityTypeId(jsonObject.get(this.RE_ENTITYTYPE_ID_VAL).toString());
					researchElementMasterVO.setResearchElementStatus(jsonObject.get(this.RE_STATUS_VAL).toString());
					researchElementMasterVO.setIsIndividual(0);
					message = this.reMultiActionManager.addResearchElementGroup(researchElementMasterVO);
					if (message.trim().length() > 0 && !message.equals(this.SUCCESS)) {
						break;
					}
				}

				ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER_INDIVIDUAL");
				ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER_COMPANY");
				ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER");
			}

			this.mv = new ModelAndView(this.REDIRECT_RE_MASTER);
			if (message.trim().length() > 0 && message.equals(this.SUCCESS)) {
				request.getSession().setAttribute(this.SUCCESS, this.TRUE);
				this.logger.info("Successfully added RE details ");
			} else {
				request.getSession().setAttribute(this.SUCCESS, message);
			}
		} catch (CMSException var12) {
			return AtlasUtils.getExceptionView(this.logger, var12);
		} catch (Exception var13) {
			return AtlasUtils.getExceptionView(this.logger, var13);
		}

		return this.mv;
	}

	public ModelAndView addResearchElement(HttpServletRequest request, HttpServletResponse response,
			ResearchElementMasterVO researchElementMasterVO) throws Exception {
		this.logger.debug("in addResearchElement..loading add RE page.");
		return new ModelAndView(this.ADD_JSP);
	}

	public ModelAndView searchReSearchElement(HttpServletRequest request, HttpServletResponse response,
			ResearchElementMasterVO researchElementMasterVO) {
		try {
			this.logger.debug("in searchReSearchElement...getResearchElementStatus :: "
					+ researchElementMasterVO.getResearchElementStatus() + " :: column :: "
					+ request.getParameter("sort") + " :: sort type :: " + request.getParameter("dir"));
			researchElementMasterVO.setSortColumnName(request.getParameter("sort"));
			researchElementMasterVO.setSortType(request.getParameter("dir"));
			List<ResearchElementMasterVO> searchResult = this.reMultiActionManager.searchRE(researchElementMasterVO);
			int total = this.reMultiActionManager.searchRECount(researchElementMasterVO);
			this.logger.debug("searchResult size :: " + searchResult.size() + " :: total count :: " + total);
			this.mv = new ModelAndView("jsonView");
			this.mv.addObject(this.SEARCH_NAME, searchResult);
			this.mv.addObject(this.TOTAL, total);
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		return this.mv;
	}

	public ModelAndView deActivateReSearchElement(HttpServletRequest request, HttpServletResponse response,
			ResearchElementMasterVO researchElementMasterVO) {
		this.mv = new ModelAndView("jsonView");
		this.logger.debug("in deActivateReSearchElement");
		String message = "";

		try {
			String rEMasterId = request.getParameter(this.ACTION);
			int statusVal = Integer.parseInt(request.getParameter(this.STATUS));
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			if (statusVal == 0) {
				int count = this.reMultiActionManager.canREsDeactivated(rEMasterId);
				if (count == 0) {
					message = this.reMultiActionManager.updateREStatus(rEMasterId, statusVal, userName);
					if (message.trim().length() > 0 && message.equals(this.SUCCESS)) {
						this.logger.info("Successfully deactivated REs with id :: " + rEMasterId);
						this.mv.addObject(this.SUCCESS, this.TRUE);
						ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER_INDIVIDUAL");
						ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER_COMPANY");
						ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER");
					} else {
						this.mv.addObject(this.SUCCESS, message);
					}
				} else {
					this.mv.addObject(this.SUCCESS, this.FALSE);
					this.mv.addObject(this.COUNT, count);
				}
			} else {
				message = this.reMultiActionManager.updateREStatus(rEMasterId, statusVal, userName);
				if (message.trim().length() > 0 && message.equals(this.SUCCESS)) {
					this.logger.info("Successfully activated REs with id :: " + rEMasterId);
					this.mv.addObject(this.SUCCESS, this.TRUE);
					ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER_INDIVIDUAL");
					ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER_COMPANY");
					ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER");
				} else {
					this.mv.addObject(this.SUCCESS, message);
				}
			}
		} catch (CMSException var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}

		return this.mv;
	}

	public ModelAndView getREInfo(HttpServletRequest request, HttpServletResponse response,
			ResearchElementMasterVO researchElementMasterVO) {
		this.logger.debug("in getREInfo");

		try {
			this.mv = new ModelAndView(this.ADD_JSP);
			this.logger.debug("reCode : " + researchElementMasterVO.getrEMasterId() + " :: status :: "
					+ researchElementMasterVO.getResearchElementStatus());
			if (researchElementMasterVO.getrEMasterId() != 0) {
				ResearchElementMasterVO vo = this.reMultiActionManager.getREInfo(researchElementMasterVO);
				if (vo != null) {
					this.logger.debug("getReGroupIdVal :: " + vo.getReGroupIdVal()
							+ " :: getResearchElementGroupName :: " + vo.getResearchElementGroupName()
							+ " :: getGroupval :: " + vo.getGroupval() + " :: getPoints :: " + vo.getPoints());
					if (Integer.parseInt(vo.getReGroupIdVal()) == 0) {
						this.logger.debug(" individual RE");
					} else {
						this.logger.debug("group RE");
						this.mv.addObject("researchElementcode", researchElementMasterVO.getrEMasterId());
					}

					this.mv.addObject(this.GET_RE, vo);
					this.mv.addObject(this.IS_UPDATE, this.TRUE);
				} else {
					this.mv = new ModelAndView(this.REDIRECT_RE_MASTER);
				}
			} else {
				this.mv = new ModelAndView(this.REDIRECT_RE_MASTER);
			}
		} catch (CMSException var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}

		return this.mv;
	}

	public ModelAndView getREGridInfo(HttpServletRequest request, HttpServletResponse response,
			ResearchElementMasterVO researchElementMasterVO) {
		this.logger.debug("in getREGridInfo");

		try {
			List<ResearchElementGroupMasterVO> rgv = null;
			this.logger.debug("reCode :: " + request.getParameter(this.RE_CODE));
			researchElementMasterVO.setrEMasterId(Integer.parseInt(request.getParameter(this.RE_CODE)));
			if (researchElementMasterVO.getrEMasterId() != 0) {
				rgv = this.reMultiActionManager.getGroupInfo(researchElementMasterVO);
			}

			this.mv = new ModelAndView("jsonView");
			this.mv.addObject(this.GET_REG, rgv);
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		return this.mv;
	}

	public ModelAndView updateRE(HttpServletRequest request, HttpServletResponse response,
			ResearchElementMasterVO researchElementMasterVO) {
		this.logger.debug("IN UpdateRE");
		String message = "";

		try {
			int groupId = 0;
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			researchElementMasterVO.setUpdatedBy(userName);
			this.logger.debug("getResearchElementType() :: " + researchElementMasterVO.getResearchElementType()
					+ " :: groupID :: " + request.getParameter(this.HRE_GROUP_ID));
			if (request.getParameter(this.HRE_GROUP_ID) != null
					&& !request.getParameter(this.HRE_GROUP_ID).equalsIgnoreCase("")
					&& Integer.parseInt(request.getParameter(this.HRE_GROUP_ID)) != 0) {
				groupId = Integer.parseInt(request.getParameter(this.HRE_GROUP_ID));
			} else if (Integer.parseInt(researchElementMasterVO.getResearchElementType()) == 0
					&& Integer.parseInt(request.getParameter(this.HRE_GROUP_ID)) == 0) {
				groupId = this.reMultiActionManager.getGroupId(researchElementMasterVO.getResearchElementGroupName());
			}

			this.logger.debug("groupId :: " + groupId);
			this.logger.debug("update successful");
			message = this.reMultiActionManager.updateRE(researchElementMasterVO, groupId);
			ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER_INDIVIDUAL");
			ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER_COMPANY");
			ResourceLocator.self().getCacheService().addToCacheRunTime("RE_MASTER");
			this.mv = new ModelAndView(this.REDIRECT_RE_MASTER);
			if (message.trim().length() > 0 && message.equals(this.SUCCESS)) {
				this.logger.info("Successfully updated REs details");
				request.getSession().setAttribute(this.UPDATE_DONE, this.TRUE);
			} else {
				request.getSession().setAttribute(this.UPDATE_DONE, message);
			}
		} catch (CMSException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}

		return this.mv;
	}

	public ModelAndView isGroupNameUnique(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in isGroupNameUnique");

		try {
			this.logger.debug("input parameters:: " + request.getParameter(this.GROUP_NAME));
			String groupName = request.getParameter(this.GROUP_NAME);
			HashMap<String, Object> paramMap = new HashMap();
			paramMap.put(this.GROUP_NAME, groupName);
			boolean result = this.reMultiActionManager.isGroupNameUnique(paramMap);
			this.logger.debug("result for  isGroupNameUnique :: " + result);
			this.mv = new ModelAndView("jsonView");
			if (result) {
				this.mv.addObject(this.SUCCESS, this.TRUE);
			} else {
				this.mv.addObject(this.SUCCESS, this.FALSE);
			}
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		return this.mv;
	}

	public ModelAndView isSubjectTypeREUnique(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in isSubjectTypeREUnique");

		try {
			this.logger.debug("input parameters:: " + request.getParameter(this.RE_INFO));
			String reInfo = request.getParameter(this.RE_INFO);
			String result = this.reMultiActionManager.isSubjectTypeREUnique(reInfo);
			this.logger.debug("result for  isSubjectTypeREUnique :: " + result);
			this.mv = new ModelAndView("jsonView");
			if (result != null && result.trim().length() > 0) {
				this.mv.addObject(this.SUCCESS, this.TRUE);
			} else {
				this.mv.addObject(this.SUCCESS, this.FALSE);
			}

			this.mv.addObject("result", result);
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		return this.mv;
	}

	public ModelAndView isWIPCaseImpacted(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in isWIPCaseImpacted");

		try {
			this.logger.debug("input parameters:: " + request.getParameter(this.RE_INFO));
			String reInfo = request.getParameter(this.RE_INFO);
			String result = this.reMultiActionManager.isWIPCaseImpacted(reInfo);
			this.logger.debug("result for  isWIPCaseImpacted :: " + result);
			this.mv = new ModelAndView("jsonView");
			if (result != null && result.trim().length() > 0) {
				this.mv.addObject(this.SUCCESS, this.TRUE);
			} else {
				this.mv.addObject(this.SUCCESS, this.FALSE);
			}
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		return this.mv;
	}

	public ModelAndView canReDeactivated(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in canReDeactivated");

		try {
			this.logger.debug("input parameters:: " + request.getParameter(this.RE_INFO));
			String reInfo = request.getParameter(this.RE_INFO);
			int result = this.reMultiActionManager.canREsDeactivated(reInfo);
			this.logger.debug("result for  canReDeactivated :: " + result);
			this.mv = new ModelAndView("jsonView");
			if (result > 0) {
				this.mv.addObject(this.SUCCESS, this.FALSE);
			} else {
				this.mv.addObject(this.SUCCESS, this.TRUE);
			}
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		return this.mv;
	}
}