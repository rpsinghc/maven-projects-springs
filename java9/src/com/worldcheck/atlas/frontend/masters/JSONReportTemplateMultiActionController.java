package com.worldcheck.atlas.frontend.masters;

import com.worldcheck.atlas.bl.interfaces.IReportTemplateMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTemplateMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;

public class JSONReportTemplateMultiActionController extends JSONMultiActionController {
	private String loggerClass = "com.worldcheck.atlas.frontend.masters.JSONReportTemplateMultiActionController";
	private ILogProducer logger;
	private static final String BLANK = "";
	private static final String SHOWMESSAGE = "showmessage";
	private static final String MESSAGE_ADD_REPORT_TEMPLATE = "Add ReportTemplate";
	private static final String UPLOAD_REPORT_TEMPLATE = "Upload ReportTemplate";
	private static final String UPLOADABLE_FILE_LIST = "uploadableFileList";
	private static final String UPLOADED_FILE_LIST = "uploadedFileList";
	private static final String HISTORY_FILE_LIST = "historyFileList";
	private static final String SEARCH_LIST = "searchList";
	private static final String GET_UPLOADABLE_REPORT_TEMPLATE = "UPLOADABLE FILE LIST";
	private static final String SESSION_UPLOAD = "sessionUploadFiles";
	private static final String SESSION_UPLOADED = "sessionUploadedFiles";
	private static final String SESSION_HISTORY = "sessionHistoryFiles";
	private static final String LIMIT_EXCEED = "Limit Exceeded (totalfiles <= 5 & total size <= 100MB)";
	private static final String CLIENT_LIST = "clientList";
	private static final String SELECTED_CLIENT_LIST = "selectedClientList";
	private static final String JSON_SELECTED_CLIENT = "jsonSelectedClientList";
	private static final String JSON_CLIENT = "jsonClientList";
	private static final String SUCCESS = "success";
	private static final String DELETE_RPT_TMP_MSG = "delete ReportTemplate";
	private static final String HISTORY_RPT_TEM_MSG = "History ReportTemplate";
	private static final String REMOVE_HISTORY_RPT_TEMP_MSG = "Remove History ReportTemplate";
	private static final String SEARCH_RPT_TEMP_MSG = "Search ReportTemplate";
	private static final String isGeneral = "isGeneral";
	private static final String GENERAL = "general";
	private static final String error = "error";
	private static final String ZERO = "0";
	private static final String CLIENTCODE = "clientCode";
	private static final String REPORTTYPE = "reportType";
	private static final String SEARCHLIST = "searchList";
	private HashMap<String, ReportTemplateMasterVO> sessionUploadableFilesHashMap;
	private HashMap<String, ReportTemplateMasterVO> sessionUploadedFilesHashMap;
	private HashMap<String, ReportTemplateMasterVO> sessionHistoryFilesHashMap;
	private IReportTemplateMaster reportTemplateService;

	public JSONReportTemplateMultiActionController() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
		this.sessionUploadableFilesHashMap = null;
		this.sessionUploadedFilesHashMap = null;
		this.sessionHistoryFilesHashMap = null;
		this.reportTemplateService = null;
	}

	public void setReportTemplateService(IReportTemplateMaster reportTemplateService) {
		this.reportTemplateService = reportTemplateService;
	}

	public ModelAndView getUploadableReportTemplate(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
		this.logger.debug("In getUploadableReportTemplate method");
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
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
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
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
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

	public ModelAndView upLoadReportTemplate(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
		this.logger.debug(" In upLoadReportTemplate method");
		String message = "Upload ReportTemplate";
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.reportTemplateService.upLoadReportTemplate(reportTemplateMasterVO, request);
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

		this.logger.info("Report download template is successfully uploaded");
		return modelAndView;
	}

	public ModelAndView getCurrentlyUploadedFiles(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) {
		this.logger.debug("iN  getCurrentlyUploadedFiles METHOD");
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

	public ModelAndView deleteReportTemplate(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
		this.logger.debug("In deleteReportTemplate method");
		String message = "delete ReportTemplate";
		this.logger.info(message);
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.reportTemplateService.deleteReportTemplate(request, reportTemplateMasterVO);
			modelAndView.addObject("success", true);
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}

		this.logger.info("Report download template is successfully deleted from Temporary");
		return modelAndView;
	}

	public ModelAndView historyReportTemplate(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
		this.logger.debug("In historyReportTemplate method");
		String message = "History ReportTemplate";
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

	public ModelAndView removeHistoryReportTemplate(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
		this.logger.debug("In removeHistoryReportTemplate method");
		String message = "Remove History ReportTemplate";
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.reportTemplateService.removeHistoryReportTemplate(reportTemplateMasterVO, request);
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

		this.logger.info("Report download template is successfully removed to history");
		return modelAndView;
	}

	public ModelAndView searchReportTemplate(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
		this.logger.debug("In searchReportTemplate method");
		String message = "Search ReportTemplate";
		this.logger.info(message);
		ModelAndView modelAndView = new ModelAndView("jsonView");
		Object searchList = null;

		try {
			if (reportTemplateMasterVO.getClientCode().equalsIgnoreCase("")
					&& reportTemplateMasterVO.getClientName().equalsIgnoreCase("")
					&& reportTemplateMasterVO.getReportType().equalsIgnoreCase("")
					&& reportTemplateMasterVO.getUpdateEndDate().equalsIgnoreCase("")
					&& reportTemplateMasterVO.getUpdateStartDate().equalsIgnoreCase("")
					&& reportTemplateMasterVO.getComment().equalsIgnoreCase("")) {
				searchList = new ArrayList();
			} else {
				String updateStartDate = null;
				String updateEndDate = null;
				if (reportTemplateMasterVO.getUpdateStartDate() != null
						&& !reportTemplateMasterVO.getUpdateStartDate().equalsIgnoreCase("")) {
					updateStartDate = reportTemplateMasterVO.getUpdateStartDate();
				}

				if (reportTemplateMasterVO.getUpdateEndDate() != null
						&& !reportTemplateMasterVO.getUpdateEndDate().equalsIgnoreCase("")) {
					updateEndDate = reportTemplateMasterVO.getUpdateEndDate();
				}

				searchList = this.reportTemplateService.searchReportTemplDown(reportTemplateMasterVO,
						Integer.parseInt(request.getParameter("start")),
						Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
						request.getParameter("dir"));
				reportTemplateMasterVO.setUpdateEndDate(updateEndDate);
				reportTemplateMasterVO.setUpdateStartDate(updateStartDate);
				modelAndView.addObject("total",
						this.reportTemplateService.searchReportTemplDownCount(reportTemplateMasterVO));
			}

			modelAndView.addObject("searchList", searchList);
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView getGeneralClients(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) {
		this.logger.debug("In getGeneralClients method");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("jsonSelectedClientList") != null
					&& !((List) session.getAttribute("jsonSelectedClientList")).isEmpty()) {
				session.removeAttribute("jsonSelectedClientList");
			}

			List<ClientMasterVO> generalClientList = this.reportTemplateService.getGeneralClients();
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
			ReportTemplateMasterVO reportTemplateMasterVO) {
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
			hmap.put("reportType", request.getParameter("reportType"));
			hmap.put("start", String.valueOf(start + 1));
			hmap.put("limit", String.valueOf(limit));
			List<ReportTemplateMasterVO> searchList = this.reportTemplateService.getFilesForCaseDetail(hmap);
			modelAndView.addObject("searchList", searchList);
			modelAndView.addObject("total", this.reportTemplateService.getFilesForCaseDetailCount(hmap));
			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}
}