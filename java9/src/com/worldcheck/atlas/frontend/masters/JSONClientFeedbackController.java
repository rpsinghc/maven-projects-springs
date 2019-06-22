package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IFeedBack;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.ClientFeedBackVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONClientFeedbackController extends JSONMultiActionController {
	private static final String JSONVIEW = "jsonView";
	private static final String GETCASEWONERLIST = "getCaseOwnerList";
	private static final String GETFEEDBACKCATEGORYLIST = "getFeedBackCategoryList";
	private static final String GETFEEDBACKTYPELIST = "getFeedBackTypeList";
	private static final String GETCRNLIST = "getCRNList";
	private static final String GETFEEDBACKLIST = "searchResult";
	private static final String GETLINKEDCRNLIST = "getLinkedCRNList";
	private static final String LISTOFATTACHMENTS = "attachDocList";
	private static final String ACTION = "action";
	private static final String MAPFBID = "mapFBId";
	private static final String CRNLIST = "crnList";
	private static final String SUCCESS = "success";
	private static final String TOTAL_RECORD_COUNT = "total";
	private static final String DOCLIST = "docList";
	private static final String ATTACHMENTIDLIST = "attachmentId";
	private static final String URL_PARAMS = "/sbm/bpmportal/atlas/showFbAttachment.do?attFileName=";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.JSONUserMultiActionController");
	IFeedBack clientFeedBackManager = null;

	public void setClientFeedBackManager(IFeedBack clientFeedBackManager) {
		this.clientFeedBackManager = clientFeedBackManager;
	}

	public ModelAndView getcaseOwnerList(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<UserMasterVO> getCaseOwnerList = this.clientFeedBackManager.getcaseOwnerList();
			modelAndView.addObject("getCaseOwnerList", getCaseOwnerList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getFeedBackCategoryList(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<ClientFeedBackVO> getFeedBackCategoryList = this.clientFeedBackManager.getFeedBackCategoryList();
			modelAndView.addObject("getFeedBackCategoryList", getFeedBackCategoryList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getCRNList(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) throws CMSException {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		long count = 0L;

		try {
			this.logger.debug("IN getCRNList");
			this.logger.debug(request.getParameter("primarySubject"));
			clientFeedBackVO.setSortColumnName(request.getParameter("sort"));
			clientFeedBackVO.setSortType(request.getParameter("dir"));
			this.logger.debug(
					"values of fields::" + clientFeedBackVO.getCrn() + ">>" + clientFeedBackVO.getCaseReceivedDate()
							+ ">>" + clientFeedBackVO.getClientCode() + ">>" + clientFeedBackVO.getPrimarySubject()
							+ ">>" + clientFeedBackVO.getSortColumnName() + ">>" + clientFeedBackVO.getSortType());
			count = this.clientFeedBackManager.getCRNListCount(clientFeedBackVO);
			List<ClientFeedBackVO> getCRNList = this.clientFeedBackManager.getCRNList(clientFeedBackVO);
			modelAndView.addObject("total", count);
			modelAndView.addObject("getCRNList", getCRNList);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView searchFeedback(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) throws CMSException {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		long count = 0L;

		try {
			this.logger.debug("IN searchFeedback");
			clientFeedBackVO.setSortColumnName(request.getParameter("sort"));
			clientFeedBackVO.setSortType(request.getParameter("dir"));
			this.logger.debug("values of fields::" + clientFeedBackVO.getDateLoggedStart() + ">>"
					+ clientFeedBackVO.getDateLoggedEnd() + ">>" + clientFeedBackVO.getSearchClientCode() + ">>"
					+ clientFeedBackVO.getSearchCaseStatus() + ">>" + clientFeedBackVO.getSearchCaseOwner());
			count = this.clientFeedBackManager.searchFeedbackCount(clientFeedBackVO);
			List<ClientFeedBackVO> getFeedbackList = this.clientFeedBackManager.searchFeedback(clientFeedBackVO);
			modelAndView.addObject("total", count);
			modelAndView.addObject("searchResult", getFeedbackList);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getLinkedCRNList(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) throws CMSException {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("IN getLinkedCRNList");
			this.logger.debug(
					"values of fields::" + clientFeedBackVO.getCrnMapID() + ">>" + clientFeedBackVO.getCrnName());
			List<ClientFeedBackVO> getCRNList = this.clientFeedBackManager
					.getLinkedCRNList(clientFeedBackVO.getCrnMapID());
			modelAndView.addObject("getLinkedCRNList", getCRNList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView linkUnlinkCrnOnUpdate(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) throws CMSException {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("IN getLinkedCRNList");
			int actionType = Integer.parseInt(request.getParameter("action"));
			String crnList = request.getParameter("crnList");
			long fbId = Long.parseLong(request.getParameter("mapFBId"));
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			this.logger.debug("values of fields CRN List::" + crnList + ">>FeedbackId" + fbId + ">>Action Type"
					+ actionType + ">>Username" + userName);
			this.clientFeedBackManager.linkUnlinkCrnOnUpdate(crnList, actionType, fbId, userName);
			modelAndView.addObject("success", true);
			return modelAndView;
		} catch (CMSException var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		} catch (Exception var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		}
	}

	public ModelAndView displayAttachDocuments(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) throws CMSException {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("IN displayAttachDocuments");
			this.logger.debug("FeedBAck ID === CR7" + clientFeedBackVO.getNewFbSeqId());
			List<ClientFeedBackVO> listofAttachemnt = this.clientFeedBackManager
					.displayAttachDocuments(clientFeedBackVO.getNewFbSeqId());
			Iterator var7 = listofAttachemnt.iterator();

			while (var7.hasNext()) {
				ClientFeedBackVO cfv = (ClientFeedBackVO) var7.next();
				String fileName = cfv.getFileName();
				String filePath = cfv.getFilePath();
				String tempURL = "<a href='/sbm/bpmportal/atlas/showFbAttachment.do?attFileName=" + fileName
						+ "&attFilePath=" + filePath + "'>" + fileName + "</a>";
				this.logger.debug("URL String>>>>" + tempURL);
				cfv.setFileURL(tempURL);
			}

			modelAndView.addObject("attachDocList", listofAttachemnt);
			return modelAndView;
		} catch (CMSException var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		} catch (Exception var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		}
	}

	public ModelAndView displayTempAttachDocuments(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) throws CMSException {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("IN displayTempAttachDocuments");
			String userName = SBMUtils.getSession(request).getUser();
			clientFeedBackVO.setLogInUserId(userName);
			String strDocsList = clientFeedBackVO.getAttachedFiles();
			if (strDocsList != null) {
				this.logger.debug("attachedFiles::::" + strDocsList);
				clientFeedBackVO.setAttachedFilesList(Arrays.asList(strDocsList.split(",")));
			}

			clientFeedBackVO.setSessionID(request.getSession().getId());
			this.logger.debug("JsonClient Feedback ID: " + request.getSession().getId());
			List<ClientFeedBackVO> listofAttachemnt = this.clientFeedBackManager
					.displayTempAttachDocuments(clientFeedBackVO);
			this.logger.debug("listofAttachemnt::::" + listofAttachemnt.size());
			this.logger.debug("listofAttachemnt::::" + listofAttachemnt);
			String fileName = null;
			String filePath = null;
			String tempURL = null;
			Iterator var12 = listofAttachemnt.iterator();

			while (var12.hasNext()) {
				ClientFeedBackVO cfv = (ClientFeedBackVO) var12.next();
				fileName = cfv.getFileName();
				filePath = cfv.getFilePath();
				tempURL = "<a href='/sbm/bpmportal/atlas/showFbAttachment.do?attFileName=" + fileName + "&attFilePath="
						+ filePath + "\\" + fileName + "'>" + fileName + "</a>";
				this.logger.debug("URL String>>>>" + tempURL);
				cfv.setFileURL(tempURL);
			}

			modelAndView.addObject("attachDocList", listofAttachemnt);
			return modelAndView;
		} catch (CMSException var13) {
			return AtlasUtils.getJsonExceptionView(this.logger, var13, response);
		} catch (Exception var14) {
			return AtlasUtils.getJsonExceptionView(this.logger, var14, response);
		}
	}

	public ModelAndView removeAttachments(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) throws CMSException {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("IN removeAttachments");
			this.logger.debug("FeedBAck ID === CR7" + clientFeedBackVO.getNewFbSeqId());
			String documentList = request.getParameter("docList");
			String attachIds = request.getParameter("attachmentId");
			String storeId = request.getParameter("storeId");
			this.logger.debug("List of Attachments >>>>" + documentList);
			this.logger.debug("List of Attachments IDs >>>>" + attachIds);
			this.logger.debug("storeId of Attachment grid >>>>" + storeId);
			if ("1".equals(storeId)) {
				this.clientFeedBackManager.removeAttachments(documentList, attachIds);
			} else {
				this.clientFeedBackManager.removeAttachments(documentList, attachIds, storeId);
			}

			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getFeedbackTypeList(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) throws CMSException {
		this.logger.debug("Inside getFeedbackTypeList");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String fieldId = request.getParameter("fieldId");
			String moduleId = request.getParameter("moduleId");
			this.logger.debug("fieldId:::" + fieldId);
			this.logger.debug("moduleId::::" + moduleId);
			List<ClientFeedBackVO> getFeedBackTypeList = this.clientFeedBackManager.getFeedbackTypeList(fieldId,
					moduleId);
			modelAndView.addObject("getFeedBackTypeList", getFeedBackTypeList);
			this.logger.debug("Exit getFeedbackTypeList");
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}
}