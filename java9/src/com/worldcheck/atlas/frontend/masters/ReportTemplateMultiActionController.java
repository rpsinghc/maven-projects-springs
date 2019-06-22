package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IReportTemplateMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTemplateMasterVO;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class ReportTemplateMultiActionController extends MultiActionController {
	private String loggerClass = "com.worldcheck.atlas.frontend.masters.ReportTemplateMultiActionController";
	private ILogProducer logger;
	private static final String SHOWMESSAGE = "showmessage";
	private static final String UPLOAD_REPORT_TEMPLATE = "Upload ReportTemplate";
	private static final String JSON_SELECTED_CLIENT = "jsonSelectedClientList";
	private static final String JSON_CLIENT = "jsonClientList";
	private HashMap<String, ReportTemplateMasterVO> sessionUploadableFilesHashMap;
	private static final String MESSAGE_ADD_REPORT_TEMPLATE = "Add ReportTemplate";
	private static final String LIMIT_EXCEED = "Limit Exceeded (totalfiles <= 5 & total size <= 100MB & individual file size <= 20MB)";
	private static final String SESSION_UPLOAD = "sessionUploadFiles";
	private static final String SESSION_UPLOADED = "sessionUploadedFiles";
	private static final String SESSION_HISTORY = "sessionHistoryFiles";
	private static final String NEW_RPT_TMP = "New Report Template";
	private static final String RPT_TEMP_DOWN_ADD = "report_temp_down_add";
	private static final String FILE_PATH_ROOT = "uploadableFilesTempPath";
	private static final String HYPEN = "-";
	private static final String COMMA = ",";
	private static final String SELECTED_CODE_NAME = "selectedCodeName";
	private static final String CODE_NAME = "codeName";
	private static final String H_REPORT_TYPE_CMB = "hreportTypeCmb";
	private static final String COMMENTS = "comments";
	private static final String INSTRUCTIONS = "instructions";
	private static final String GENERAL = "general";
	private static final String REPORT_TYPE = "reportType";
	private static final String CLIENT_REQ_ID = "clientReqId";
	private static final String INSTRUCTION = "instruction";
	private static final String COMMENT = "comment";
	private static final String RPT_TMPL_DOWN_MASTER_JSP = "report_temp_down_list";
	private static final String UPDATEPAGE = "isUpdatePage";
	private static final String REPORTDOWNID = "reptDownId";
	private static final String LIMIT_EXCEED_MSG = "limitExceedMsg";
	private static final String TEMP_FILEUPLOAD_ERROR = "tempFileUploadError";
	String[] codeNames;
	String[] selectedCodeNames;
	List<String> selectedClientCodesList;
	String reportType;
	String comment;
	String general;
	List<ClientMasterVO> uiclientlist;
	List<ClientMasterVO> uiselectedClientlist;
	File file;
	private static final String SEPERATOR = "@SEPERATOR@";
	private static final String MODELANDVIEWHMAP = "modelAndViewHMap";
	private static final String REDIRECT_NEWANDUPDATEREPORTTEMPLATE = "redirect:newAndUpdateReportTemplate.do";
	private static final String UPLOADFILESIZE = "UploadFileSize";
	private static final String INDIVIDUALUPLOADFILESIZE = "IndividualUploadFileSize";
	private static final String UTF8 = "UTF-8";
	private static final String URL_HASH = "\\";
	private static final String REPTDOWNID = "reptDownId";
	private IReportTemplateMaster reportTemplateService;

	public ReportTemplateMultiActionController() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
		this.sessionUploadableFilesHashMap = null;
		this.codeNames = null;
		this.selectedCodeNames = null;
		this.selectedClientCodesList = null;
		this.reportType = null;
		this.uiclientlist = null;
		this.uiselectedClientlist = null;
		this.file = null;
		this.reportTemplateService = null;
	}

	public void setReportTemplateService(IReportTemplateMaster reportTemplateService) {
		this.reportTemplateService = reportTemplateService;
	}

	public ModelAndView reportTemplate(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
		this.logger.debug("In reportTemplate method");
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("report_temp_down_list");
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView openNewReportTemplate(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
		this.logger.debug("In openNewReportTemplate method");
		ModelAndView modelAndView = new ModelAndView("report_temp_down_add");

		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("sessionUploadFiles") != null) {
				session.removeAttribute("sessionUploadFiles");
			}

			if (session.getAttribute("sessionUploadedFiles") != null) {
				session.removeAttribute("sessionUploadedFiles");
			}

			if (session.getAttribute("sessionHistoryFiles") != null) {
				session.removeAttribute("sessionHistoryFiles");
			}

			if (session.getAttribute("jsonClientList") != null) {
				session.removeAttribute("jsonClientList");
			}

			if (session.getAttribute("jsonSelectedClientList") != null) {
				session.removeAttribute("jsonSelectedClientList");
			}

			return modelAndView;
		} catch (NullPointerException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView newAndUpdateReportTemplate(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
		this.logger.debug("In newAndUpdateReportTemplate");
		ModelAndView modelAndView = new ModelAndView("report_temp_down_add");

		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("modelAndViewHMap") != null) {
				HashMap<String, Object> hmap = (HashMap) session.getAttribute("modelAndViewHMap");
				modelAndView.addObject("isUpdatePage", hmap.get("isUpdatePage"));
				modelAndView.addObject("reptDownId", hmap.get("reptDownId"));
				modelAndView.addObject("reportType", hmap.get("reportType"));
				modelAndView.addObject("general", hmap.get("general"));
				modelAndView.addObject("limitExceedMsg", hmap.get("limitExceedMsg"));
				modelAndView.addObject("reportType", hmap.get("reportType"));
				modelAndView.addObject("general", hmap.get("general"));
				modelAndView.addObject("tempFileUploadError", hmap.get("tempFileUploadError"));
				modelAndView.addObject("reportType", hmap.get("reportType"));
				modelAndView.addObject("comment", hmap.get("comment"));
				modelAndView.addObject("general", hmap.get("general"));
				modelAndView.addObject("isUpdatePage", hmap.get("isUpdatePage"));
				modelAndView.addObject("reptDownId", hmap.get("reptDownId"));
				session.removeAttribute("modelAndViewHMap");
			}

			return modelAndView;
		} catch (NullPointerException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	public ModelAndView addReportTemplate(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
		this.logger.debug("In addReportTemplate method");
		ModelAndView modelAndView = new ModelAndView("redirect:newAndUpdateReportTemplate.do");
		HashMap<String, Object> modelAndViewHMap = new HashMap();
		HttpSession session = request.getSession();

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			if (session.getAttribute("sessionUploadFiles") != null) {
				this.sessionUploadableFilesHashMap = (HashMap) session.getAttribute("sessionUploadFiles");
			} else {
				this.sessionUploadableFilesHashMap = new HashMap();
				this.reportTemplateService.deleteUserFiles(userName);
			}

			this.logger.info("Add ReportTemplate");
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = null;
				items = upload.parseRequest(request);
				Iterator<FileItem> itr = items.iterator();
				this.fileItemIteratorMethod(itr, reportTemplateMasterVO, modelAndViewHMap);
				String[] selectedClientCodes = new String[this.selectedClientCodesList.size()];

				for (int i = 0; i < this.selectedClientCodesList.size(); ++i) {
					selectedClientCodes[i] = (String) this.selectedClientCodesList.get(i);
				}

				this.uiClientGrid(session);
				modelAndViewHMap.put("reportType", reportTemplateMasterVO.getReportType());
				modelAndViewHMap.put("general", reportTemplateMasterVO.getIsGeneral());
				if (this.fileUploadlimitExceed(modelAndViewHMap, reportTemplateMasterVO)) {
					session.setAttribute("modelAndViewHMap", modelAndViewHMap);
					return modelAndView;
				}

				this.uploadableFileInSession(reportTemplateMasterVO, userName, session);
				modelAndViewHMap.put("tempFileUploadError", false);
				this.logger.info("Report download template is temporary added/Updated ");
			}
		} catch (CMSException var16) {
			this.logger.debug("cmsexception:" + var16);
			modelAndViewHMap.put("tempFileUploadError", true);
		} catch (NullPointerException var17) {
			this.logger.debug("NullPointerException:" + var17);
			modelAndViewHMap.put("tempFileUploadError", true);
		} catch (Exception var18) {
			this.logger.debug("Exception " + var18);
			modelAndViewHMap.put("tempFileUploadError", true);
		}

		session.setAttribute("modelAndViewHMap", modelAndViewHMap);
		return modelAndView;
	}

	private void uploadableFileInSession(ReportTemplateMasterVO reportTemplateMasterVO, String userName,
			HttpSession session) throws CMSException {
		this.logger.debug(" In uploadableFileInSession method");
		HashMap<String, ReportTemplateMasterVO> uploadableFileHashMap = null;
		this.logger.debug(this.file.getAbsolutePath());

		try {
			uploadableFileHashMap = this.reportTemplateService.addReportTemplate(reportTemplateMasterVO, this.file,
					userName);
			Set<String> keySet = uploadableFileHashMap.keySet();

			String key;
			for (Iterator keySetIterator = keySet.iterator(); keySetIterator.hasNext(); this.logger
					.info("addReportTemplate::::::::::::::" + key + "::"
							+ ((ReportTemplateMasterVO) this.sessionUploadableFilesHashMap.get(key)).getFileName())) {
				key = (String) keySetIterator.next();
				if (this.sessionUploadableFilesHashMap != null && this.sessionUploadableFilesHashMap.isEmpty()) {
					this.sessionUploadableFilesHashMap.put(key, uploadableFileHashMap.get(key));
				} else if (this.sessionUploadableFilesHashMap != null
						&& !this.sessionUploadableFilesHashMap.isEmpty()) {
					this.sessionUploadableFilesHashMap.put(key, uploadableFileHashMap.get(key));
				}
			}

			this.file.delete();
			session.setAttribute("sessionUploadFiles", this.sessionUploadableFilesHashMap);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private boolean fileUploadlimitExceed(HashMap modelAndViewHMap, ReportTemplateMasterVO reportTemplateMasterVO)
			throws CMSException {
		this.logger.debug("In fileUploadlimitExceed method");

		try {
			int limit = 1;
			List<ReportTemplateMasterVO> list = new ArrayList(this.sessionUploadableFilesHashMap.values());
			double size = 0.0D;
			double indSize = 0.0D;
			int limit = limit + list.size() / this.selectedClientCodesList.size();

			for (Iterator sessionItr = list.iterator(); sessionItr.hasNext(); size += (double) Float
					.parseFloat(((ReportTemplateMasterVO) sessionItr.next()).getFileSize())) {
				;
			}

			size += (double) this.file.length() / 1024.0D;
			indSize = (double) this.file.length() / 1048576.0D;
			this.logger.debug(size + "::" + indSize + "::" + limit + ":::" + this.selectedClientCodesList.size() + ":::"
					+ list.size());
			if (limit <= Integer.parseInt(this.reportTemplateService.getproperties().getProperty("NoOfUploadFiles"))
					&& size / 1024.0D <= (double) Float
							.parseFloat(this.reportTemplateService.getproperties().getProperty("UploadFileSize"))
					&& indSize <= (double) Float.parseFloat(
							this.reportTemplateService.getproperties().getProperty("IndividualUploadFileSize"))) {
				return false;
			} else {
				modelAndViewHMap.put("limitExceedMsg",
						"Limit Exceeded (totalfiles <= 5 & total size <= 100MB & individual file size <= 20MB)");
				modelAndViewHMap.put("reportType", reportTemplateMasterVO.getReportType());
				modelAndViewHMap.put("general", reportTemplateMasterVO.getIsGeneral());
				this.file.delete();
				return true;
			}
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	private void uiClientGrid(HttpSession session) throws CMSException {
		this.logger.debug("In uiClientGrid method");

		try {
			this.uiselectedClientlist = new ArrayList();
			String[] arr$ = this.selectedCodeNames;
			int len$ = arr$.length;

			int i$;
			String codeName;
			ClientMasterVO cmvo;
			String code;
			for (i$ = 0; i$ < len$; ++i$) {
				codeName = arr$[i$];
				cmvo = new ClientMasterVO();
				cmvo.setClientCode(codeName.split("-")[0]);
				code = codeName.split("-")[0];
				cmvo.setCodeName(codeName);
				this.uiselectedClientlist.add(cmvo);
			}

			session.setAttribute("jsonSelectedClientList", this.uiselectedClientlist);
			this.uiclientlist = new ArrayList();
			if (this.codeNames != null) {
				arr$ = this.codeNames;
				len$ = arr$.length;

				for (i$ = 0; i$ < len$; ++i$) {
					codeName = arr$[i$];
					cmvo = new ClientMasterVO();
					cmvo.setClientCode(codeName.split("-")[0]);
					code = codeName.split("-")[0];
					cmvo.setCodeName(codeName);
					this.uiclientlist.add(cmvo);
				}
			}

			session.setAttribute("jsonClientList", this.uiclientlist);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private void fileItemIteratorMethod(Iterator<FileItem> itr, ReportTemplateMasterVO reportTemplateMasterVO,
			HashMap modelAndViewHMap) throws CMSException {
		this.logger.debug("In fileItemIteratorMethod method ");

		try {
			while (true) {
				while (true) {
					FileItem item;
					do {
						if (!itr.hasNext()) {
							return;
						}

						item = (FileItem) itr.next();
					} while (null == item);

					String sb1;
					int i$;
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("selectedCodeName")) {
							this.selectedCodeNames = item.getString().split("@SEPERATOR@");
							this.selectedClientCodesList = new ArrayList();
							String[] arr$ = this.selectedCodeNames;
							int len$ = arr$.length;

							for (i$ = 0; i$ < len$; ++i$) {
								String selectedCodeName = arr$[i$];
								this.selectedClientCodesList.add(selectedCodeName.split("-")[0]);
							}
						}

						if (item.getFieldName().equalsIgnoreCase("codeName") && item.getString() != null
								&& !item.getString().isEmpty() && item.getString().length() > 1) {
							this.codeNames = item.getString().split("@SEPERATOR@");
						}

						if (item.getFieldName().equalsIgnoreCase("hreportTypeCmb")) {
							this.reportType = item.getString();
							reportTemplateMasterVO.setReportType(this.reportType);
						}

						if (item.getFieldName().equalsIgnoreCase("comments")) {
							sb1 = "";

							try {
								for (BufferedReader r1 = new BufferedReader(new InputStreamReader(item.getInputStream(),
										"UTF-8")); (this.comment = r1.readLine()) != null; sb1 = sb1 + this.comment
												+ "\n") {
									;
								}
							} catch (Exception var10) {
								this.logger.debug(
										"Error occured when reading string object from multipart request which has special character:"
												+ var10);
							}

							this.logger.debug("comment for uploaded file is :" + sb1);
							reportTemplateMasterVO.setComment(sb1);
						}

						if (item.getFieldName().equalsIgnoreCase("general")) {
							this.general = item.getString();
							reportTemplateMasterVO.setIsGeneral(this.general);
							this.logger.info(".do>>general" + this.general);
						}

						if (item.getFieldName().equalsIgnoreCase("isUpdatePage")) {
							if (!item.getString().equalsIgnoreCase("")) {
								modelAndViewHMap.put("isUpdatePage", true);
							}

							this.logger.info("isUpdatePage:::" + item.getString());
						}

						if (item.getFieldName().equalsIgnoreCase("reptDownId")) {
							modelAndViewHMap.put("reptDownId", item.getString());
							this.logger.info("reptDownIdFromForm::" + item.getString());
						}
					} else {
						sb1 = "";
						sb1 = this.reportTemplateService.getproperties().getProperty("uploadableFilesTempPath");
						String fileName = item.getName();
						if (fileName.contains("\\")) {
							i$ = fileName.lastIndexOf("\\");
							fileName = fileName.substring(i$ + 1);
						}

						this.logger.info(fileName);
						this.file = new File(sb1, fileName);

						try {
							item.write(this.file);
						} catch (Exception var9) {
							throw new CMSException(this.logger, var9);
						}
					}
				}
			}
		} catch (Exception var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public ModelAndView openUpdateReportTemplate(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
		this.logger.debug("In openUpdateReportTemplate method");
		ModelAndView modelAndView = new ModelAndView("redirect:newAndUpdateReportTemplate.do");
		HashMap<String, Object> modelAndViewHMap = new HashMap();
		List<ClientMasterVO> uiclientlist = null;
		List<ClientMasterVO> uiselectedClientlist = null;
		HttpSession session = request.getSession();

		try {
			if (session.getAttribute("sessionUploadFiles") != null) {
				session.removeAttribute("sessionUploadFiles");
			}

			if (session.getAttribute("sessionUploadedFiles") != null) {
				session.removeAttribute("sessionUploadedFiles");
			}

			if (session.getAttribute("sessionHistoryFiles") != null) {
				session.removeAttribute("sessionHistoryFiles");
			}

			String rptId = request.getParameter("reptDownId");
			ReportTemplateMasterVO rtmvo = this.reportTemplateService.getReportTemplateDAO()
					.getInsertedData(Integer.parseInt(rptId));
			HashMap<String, ReportTemplateMasterVO> sessionHashMap = null;
			if (session.getAttribute("sessionUploadedFiles") != null) {
				sessionHashMap = (HashMap) session.getAttribute("sessionUploadedFiles");
			} else {
				sessionHashMap = new HashMap();
			}

			String key = rtmvo.getClientCode() + rtmvo.getReportType() + rtmvo.getFileName();
			sessionHashMap.put(key, rtmvo);
			session.setAttribute("sessionUploadedFiles", sessionHashMap);
			List<ReportTemplateMasterVO> historydatavoList = this.reportTemplateService.getReportTemplateDAO()
					.getHistoryDataOfClient(rtmvo.getClientCode());
			Iterator<ReportTemplateMasterVO> itr = historydatavoList.iterator();
			HashMap historyDataSessionHashMap = new HashMap();

			String comment;
			while (itr.hasNext()) {
				ReportTemplateMasterVO rtdmvoitr = (ReportTemplateMasterVO) itr.next();
				comment = rtdmvoitr.getClientCode() + rtdmvoitr.getReportType() + rtdmvoitr.getFileName();
				historyDataSessionHashMap.put(comment, rtdmvoitr);
			}

			session.setAttribute("sessionHistoryFiles", historyDataSessionHashMap);
			String reportType = rtmvo.getReportType();
			comment = rtmvo.getComment();
			String general = rtmvo.getIsGeneral();
			uiselectedClientlist = new ArrayList();
			ClientMasterVO cmvo = new ClientMasterVO();
			cmvo.setClientCode(rtmvo.getClientCode());
			cmvo.setCodeName(rtmvo.getClientCode() + "-" + rtmvo.getClientName());
			uiselectedClientlist.add(cmvo);
			session.setAttribute("jsonSelectedClientList", uiselectedClientlist);
			List<ClientMasterVO> uiclientMasterListCheck = ResourceLocator.self().getCacheService()
					.getCacheItemsList("CLIENT_MASTER");
			List<ClientMasterVO> uiclientMasterList = new ArrayList();
			Iterator clientMasterListIterator = uiclientMasterListCheck.iterator();

			while (clientMasterListIterator.hasNext()) {
				new ClientMasterVO();
				ClientMasterVO cm = (ClientMasterVO) clientMasterListIterator.next();
				if (!cm.getClientCode().equalsIgnoreCase(cmvo.getClientCode())) {
					uiclientMasterList.add(cm);
				}
			}

			session.setAttribute("jsonClientList", uiclientMasterList);
			modelAndViewHMap.put("reportType", reportType);
			modelAndViewHMap.put("comment", comment);
			modelAndViewHMap.put("general", general);
			modelAndViewHMap.put("isUpdatePage", true);
			modelAndViewHMap.put("reptDownId", rptId);
		} catch (NullPointerException var24) {
			return AtlasUtils.getExceptionView(this.logger, var24);
		} catch (Exception var25) {
			return AtlasUtils.getExceptionView(this.logger, var25);
		}

		session.setAttribute("modelAndViewHMap", modelAndViewHMap);
		return modelAndView;
	}
}