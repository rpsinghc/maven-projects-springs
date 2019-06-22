package com.worldcheck.atlas.frontend.masters;

import com.worldcheck.atlas.bl.interfaces.IClientRequirement;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ClientRequirmentMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;

public class JSONClientRequirmentMultiActionController extends JSONMultiActionController {
	private String loggerClass = "com.worldcheck.atlas.frontend.masters.JSONClientRequirmentMultiActionController";
	private ILogProducer logger;
	private static final String BLANK = "";
	private static final String SHOWMESSAGE = "showmessage";
	private static final String MESSAGE_ADD_CLIENT_REQ = "Add Client Requirement";
	private static final String UPLOAD_CLIENT_REQ = "Upload client requirement";
	private static final String UPLOADABLE_FILE_LIST = "uploadableFileList";
	private static final String UPLOADED_FILE_LIST = "uploadedFileList";
	private static final String HISTORY_FILE_LIST = "historyFileList";
	private static final String SEARCH_LIST = "searchList";
	private static final String GET_UPLOADABLE_CLIENT_REQ = "UPLOADABLE FILE LIST";
	private static final String SESSION_UPLOAD = "sessionUploadFiles";
	private static final String SESSION_UPLOADED = "sessionUploadedFiles";
	private static final String SESSION_HISTORY = "sessionHistoryFiles";
	private static final String LIMIT_EXCEED = "Limit Exceeded (totalfiles <= 5 & total size <= 100MB)";
	private static final String CLIENT_LIST = "clientList";
	private static final String SELECTED_CLIENT_LIST = "selectedClientList";
	private static final String JSON_SELECTED_CLIENT = "jsonSelectedClientList";
	private static final String JSON_CLIENT = "jsonClientList";
	private static final String SUCCESS = "success";
	private static final String DELETE_CLIENT_REQ_MSG = "delete client Requirement";
	private static final String HISTORY_CLIENT_REQ_MSG = "History client Requirement";
	private static final String REMOVE_HISTORY_CLIENT_REQ_MSG = "Remove History client Requirement";
	private static final String SEARCH_CLIENT_REQ_MSG = "Search client Requirement";
	private static final String isGeneral = "isGeneral";
	private static final String GENERAL = "general";
	private static final String error = "error";
	private static final String CLIENTCODE = "clientCode";
	private static final String ZERO = "0";
	private static final String SEARCHLIST = "searchList";
	private HashMap<String, ClientRequirmentMasterVO> sessionUploadableFilesHashMap;
	private HashMap<String, ClientRequirmentMasterVO> sessionUploadedFilesHashMap;
	private HashMap<String, ClientRequirmentMasterVO> sessionHistoryFilesHashMap;
	IClientRequirement clientRequirementService;

	public JSONClientRequirmentMultiActionController() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
		this.sessionUploadableFilesHashMap = null;
		this.sessionUploadedFilesHashMap = null;
		this.sessionHistoryFilesHashMap = null;
		this.clientRequirementService = null;
	}

	public void setClientRequirementService(IClientRequirement clientRequirementService) {
		this.clientRequirementService = clientRequirementService;
	}

	public ModelAndView getUploadableClientReq(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) throws Exception {
		this.logger.debug("In getUploadableClientReq method");
		String message = "UPLOADABLE FILE LIST";
		ModelAndView modelAndView = new ModelAndView("jsonView");
		ArrayList uploadableFilesList = new ArrayList();

		try {
			this.logger.info(message);
			HttpSession session = request.getSession();
			if (session.getAttribute("sessionUploadFiles") != null) {
				this.sessionUploadableFilesHashMap = (HashMap) session.getAttribute("sessionUploadFiles");
				Set<String> keySet = this.sessionUploadableFilesHashMap.keySet();
				Iterator keySetIterator = keySet.iterator();

				while (keySetIterator.hasNext()) {
					String key = (String) keySetIterator.next();
					uploadableFilesList.add(this.sessionUploadableFilesHashMap.get(key));
				}
			}

			modelAndView.addObject("uploadableFileList", uploadableFilesList);
			return modelAndView;
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView getClientSelectionData(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) throws Exception {
		this.logger.debug("In getClientSelectionData method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("jsonClientList") != null) {
				modelAndView.addObject("clientList", (List) session.getAttribute("jsonClientList"));
			} else {
				List<ClientMasterVO> clientMasterList = ResourceLocator.self().getCacheService()
						.getCacheItemsList("CLIENT_MASTER");
				session.setAttribute("jsonClientList", clientMasterList);
				modelAndView.addObject("clientList", clientMasterList);
			}

			return modelAndView;
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getSelectedClientData(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) throws Exception {
		this.logger.debug("In getSelectedClientData method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("jsonSelectedClientList") != null
					&& !((List) session.getAttribute("jsonSelectedClientList")).isEmpty()) {
				modelAndView.addObject("selectedClientList", (List) session.getAttribute("jsonSelectedClientList"));
			} else {
				modelAndView.addObject("selectedClientList", new ArrayList());
			}

			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView upLoadClientRequirment(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) throws Exception {
		this.logger.debug("In upLoadClientRequirment method");
		String message = "Upload client requirement";
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.clientRequirementService.upLoadClientReq(clientRequirmentMasterVO, request);
			this.logger.info(message);
			String generalStatus = "0";
			if (request.getParameter("general") != null && !request.getParameter("general").equals("")) {
				generalStatus = request.getParameter("general");
			}

			modelAndView.addObject("success", true);
			modelAndView.addObject("isGeneral", generalStatus);
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}

		this.logger.info("client requirement download is successfully uploaded");
		return modelAndView;
	}

	public ModelAndView getCurrentlyUploadedFiles(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) {
		this.logger.debug("In getCurrentlyUploadedFiles method");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		ArrayList uploadedFilesList = new ArrayList();

		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("sessionUploadedFiles") != null) {
				this.sessionUploadedFilesHashMap = (HashMap) session.getAttribute("sessionUploadedFiles");
				Set<String> keySet = this.sessionUploadedFilesHashMap.keySet();
				Iterator keySetIterator = keySet.iterator();

				while (keySetIterator.hasNext()) {
					String key = (String) keySetIterator.next();
					uploadedFilesList.add(this.sessionUploadedFilesHashMap.get(key));
				}
			}

			modelAndView.addObject("uploadedFileList", uploadedFilesList);
			return modelAndView;
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView deleteClientReq(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) throws Exception {
		this.logger.debug("In deleteClientReq method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.clientRequirementService.deleteClientReq(request, clientRequirmentMasterVO);
			String message = "delete client Requirement";
			this.logger.info(message);
			modelAndView.addObject("success", true);
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}

		this.logger.info("Client requirement download is deleted successfully");
		return modelAndView;
	}

	public ModelAndView historyClientRequirment(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) throws Exception {
		this.logger.debug("In historyClientRequirment method");
		String message = "History client Requirement";
		this.logger.info(message);
		ModelAndView modelAndView = new ModelAndView("jsonView");
		ArrayList HistoryFilesList = new ArrayList();

		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("sessionHistoryFiles") != null) {
				this.sessionHistoryFilesHashMap = (HashMap) session.getAttribute("sessionHistoryFiles");
				Set<String> keySet = this.sessionHistoryFilesHashMap.keySet();
				Iterator keySetIterator = keySet.iterator();

				while (keySetIterator.hasNext()) {
					String key = (String) keySetIterator.next();
					HistoryFilesList.add(this.sessionHistoryFilesHashMap.get(key));
				}
			}

			modelAndView.addObject("historyFileList", HistoryFilesList);
			return modelAndView;
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView removeHistoryClientRequirment(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) throws Exception {
		this.logger.debug("In removeHistoryClientRequirment method");
		String message = "Remove History client Requirement";
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.clientRequirementService.removeHistoryClientReq(clientRequirmentMasterVO, request);
			String generalStatus = "0";
			if (request.getParameter("general") != null && !request.getParameter("general").equals("")) {
				generalStatus = request.getParameter("general");
			}

			modelAndView.addObject("success", true);
			modelAndView.addObject("isGeneral", generalStatus);
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}

		this.logger.info("Client requirement download is removed to history successfully");
		return modelAndView;
	}

	public ModelAndView searchClientRequirment(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) throws Exception {
		this.logger.debug("In searchClientRequirment method");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		Object searchList = null;

		try {
			if (clientRequirmentMasterVO.getClientCode().equalsIgnoreCase("")
					&& clientRequirmentMasterVO.getClientName().equalsIgnoreCase("")
					&& clientRequirmentMasterVO.getReportType().equalsIgnoreCase("")
					&& clientRequirmentMasterVO.getComment().equalsIgnoreCase("")
					&& clientRequirmentMasterVO.getUpdateEndDate().equalsIgnoreCase("")
					&& clientRequirmentMasterVO.getUpdateStartDate().equalsIgnoreCase("")) {
				searchList = new ArrayList();
			} else {
				String updateStartDate = null;
				String updateEndDate = null;
				if (clientRequirmentMasterVO.getUpdateStartDate() != null
						&& !clientRequirmentMasterVO.getUpdateStartDate().equalsIgnoreCase("")) {
					updateStartDate = clientRequirmentMasterVO.getUpdateStartDate();
				}

				if (clientRequirmentMasterVO.getUpdateEndDate() != null
						&& !clientRequirmentMasterVO.getUpdateEndDate().equalsIgnoreCase("")) {
					updateEndDate = clientRequirmentMasterVO.getUpdateEndDate();
				}

				searchList = this.clientRequirementService.searchClientReq(clientRequirmentMasterVO,
						Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
						request.getParameter("dir"));
				clientRequirmentMasterVO.setUpdateStartDate(updateStartDate);
				clientRequirmentMasterVO.setUpdateEndDate(updateEndDate);
				modelAndView.addObject("total",
						this.clientRequirementService.searchClientReqCount(clientRequirmentMasterVO));
			}

			modelAndView.addObject("searchList", searchList);
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getGeneralClients(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) {
		this.logger.debug("In getGeneralClients method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("jsonSelectedClientList") != null
					&& !((List) session.getAttribute("jsonSelectedClientList")).isEmpty()) {
				session.removeAttribute("jsonSelectedClientList");
			}

			List<ClientMasterVO> generalClientList = this.clientRequirementService.getGeneralClients();
			session.setAttribute("jsonSelectedClientList", generalClientList);
			if (generalClientList.size() > 0) {
				modelAndView.addObject("isGeneral", true);
			} else {
				modelAndView.addObject("isGeneral", false);
			}

			if (session.getAttribute("jsonClientList") != null
					&& !((List) session.getAttribute("jsonClientList")).isEmpty()) {
				List<ClientMasterVO> allClientList = (List) session.getAttribute("jsonClientList");
				List<ClientMasterVO> NotGeneralClients = new ArrayList();
				Iterator allClientItr = allClientList.iterator();

				while (allClientItr.hasNext()) {
					ClientMasterVO allClient = (ClientMasterVO) allClientItr.next();
					int flag = false;
					Iterator generalClientItr = generalClientList.iterator();

					while (generalClientItr.hasNext()) {
						ClientMasterVO genClient = (ClientMasterVO) generalClientItr.next();
						if (genClient.getClientCode().equals(allClient.getClientCode())) {
							flag = true;
							break;
						}
					}

					if (!flag) {
						NotGeneralClients.add(allClient);
					}
				}

				session.removeAttribute("jsonClientList");
				session.setAttribute("jsonClientList", NotGeneralClients);
			}

			modelAndView.addObject("success", true);
			return modelAndView;
		} catch (CMSException var14) {
			return AtlasUtils.getJsonExceptionView(this.logger, var14, response);
		} catch (Exception var15) {
			return AtlasUtils.getJsonExceptionView(this.logger, var15, response);
		}
	}

	public ModelAndView resetClientSelectionGrid(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) {
		this.logger.debug("In resetClientSelectionGrid method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			HttpSession session = request.getSession();
			List<ClientMasterVO> clientMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("CLIENT_MASTER");
			if (session.getAttribute("jsonClientList") != null
					&& !((List) session.getAttribute("jsonClientList")).isEmpty()) {
				session.removeAttribute("jsonClientList");
			}

			session.setAttribute("jsonClientList", clientMasterList);
			modelAndView.addObject("success", true);
			if (session.getAttribute("jsonSelectedClientList") != null) {
				session.removeAttribute("jsonSelectedClientList");
			}

			return modelAndView;
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getFilesForCaseDetail(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("In getFilesForCaseDetail");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = start + new Integer(request.getParameter("limit"));
			HashMap<String, String> hmap = new HashMap();
			hmap.put("clientCode", request.getParameter("clientCode"));
			hmap.put("start", String.valueOf(start + 1));
			hmap.put("limit", String.valueOf(limit));
			List<ClientRequirmentMasterVO> searchList = this.clientRequirementService.getFilesForCaseDetail(hmap);
			modelAndView.addObject("searchList", searchList);
			modelAndView.addObject("total", this.clientRequirementService.getFilesForCaseDetailCount(hmap));
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}
}