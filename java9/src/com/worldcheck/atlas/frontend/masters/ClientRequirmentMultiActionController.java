package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IClientRequirement;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ClientRequirmentMasterVO;
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

public class ClientRequirmentMultiActionController extends MultiActionController {
	private String loggerClass = "com.worldcheck.atlas.frontend.masters.ClientRequirmentMultiActionController";
	private ILogProducer logger;
	private static final String SHOWMESSAGE = "showmessage";
	private static final String UPLOAD_REPORT_TEMPLATE = "Upload ReportTemplate";
	private static final String JSON_SELECTED_CLIENT = "jsonSelectedClientList";
	private static final String JSON_CLIENT = "jsonClientList";
	private HashMap<String, ClientRequirmentMasterVO> sessionUploadableFilesHashMap;
	private static final String MESSAGE_ADD_CLIENT_REQ = "Add ReportTemplate";
	private static final String LIMIT_EXCEED = "Limit Exceeded (totalfiles <= 5 & total size <= 100MB & individual file size <= 20MB)";
	private static final String SESSION_UPLOAD = "sessionUploadFiles";
	private static final String SESSION_UPLOADED = "sessionUploadedFiles";
	private static final String SESSION_HISTORY = "sessionHistoryFiles";
	private static final String NEW_CLIENT_REQ = "New Client Requirement";
	private static final String CLIENT_REQ_ADD = "client_req_add";
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
	private static final String clientReqMaster_jsp = "client_req_list";
	private static final String UPDATEPAGE = "isUpdatePage";
	private static final String LIMIT_EXCEED_MSG = "limitExceedMsg";
	private static final String TEMP_FILEUPLOAD_ERROR = "tempFileUploadError";
	private static final String SEPERATOR = "@SEPERATOR@";
	private static final String MODELANDVIEWHMAP = "modelAndViewHMap";
	private static final String REDIRECT_NEWANDUPDATECLIENTREQUIREMENT = "redirect:newAndUpdateClientRequirement.do";
	private static final String URL_HASH = "\\";
	File file;
	String[] codeNames;
	String[] selectedCodeNames;
	List<String> selectedClientCodesList;
	String reportType;
	String comment;
	String instruction;
	String general;
	IClientRequirement clientRequirementService;

	public ClientRequirmentMultiActionController() {
		this.logger = LogProducerImpl.getLogger(this.loggerClass);
		this.sessionUploadableFilesHashMap = null;
		this.file = null;
		this.codeNames = null;
		this.selectedCodeNames = null;
		this.selectedClientCodesList = null;
		this.reportType = null;
		this.clientRequirementService = null;
	}

	public void setClientRequirementService(IClientRequirement clientRequirementService) {
		this.clientRequirementService = clientRequirementService;
	}

	public ModelAndView clientRequirment(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) throws Exception {
		this.logger.debug("In clientRequirment method");
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("client_req_list");
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView openNewClientRequirement(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) throws Exception {
		this.logger.debug("In openNewClientRequirement method");
		String message = "New Client Requirement";
		ModelAndView modelAndView = new ModelAndView("client_req_add");

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
		} catch (NullPointerException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	public ModelAndView newAndUpdateClientRequirement(HttpServletRequest request, HttpServletResponse response,
			ReportTemplateMasterVO reportTemplateMasterVO) throws Exception {
		this.logger.debug("In newAndUpdateClientRequirement method");
		ModelAndView modelAndView = new ModelAndView("client_req_add");

		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("modelAndViewHMap") != null) {
				HashMap<String, Object> hmap = (HashMap) session.getAttribute("modelAndViewHMap");
				modelAndView.addObject("isUpdatePage", hmap.get("isUpdatePage"));
				modelAndView.addObject("clientReqId", hmap.get("clientReqId"));
				modelAndView.addObject("reportType", hmap.get("reportType"));
				modelAndView.addObject("general", hmap.get("general"));
				modelAndView.addObject("limitExceedMsg", hmap.get("limitExceedMsg"));
				modelAndView.addObject("reportType", hmap.get("reportType"));
				modelAndView.addObject("general", hmap.get("general"));
				modelAndView.addObject("tempFileUploadError", hmap.get("tempFileUploadError"));
				modelAndView.addObject("reportType", hmap.get("reportType"));
				modelAndView.addObject("comment", hmap.get("comment"));
				modelAndView.addObject("instruction", hmap.get("instruction"));
				modelAndView.addObject("general", hmap.get("general"));
				modelAndView.addObject("isUpdatePage", hmap.get("isUpdatePage"));
				modelAndView.addObject("clientReqId", hmap.get("clientReqId"));
				session.removeAttribute("modelAndViewHMap");
			}

			return modelAndView;
		} catch (NullPointerException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	public ModelAndView addClientRequirement(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO clientRequirmentMasterVO) throws Exception {
		this.logger.debug("In addClientRequirement method");
		ModelAndView modelAndView = new ModelAndView("redirect:newAndUpdateClientRequirement.do");
		HashMap<String, Object> modelAndViewHMap = new HashMap();
		HttpSession session = request.getSession();

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			if (session.getAttribute("sessionUploadFiles") != null) {
				this.sessionUploadableFilesHashMap = (HashMap) session.getAttribute("sessionUploadFiles");
			} else {
				this.sessionUploadableFilesHashMap = new HashMap();
				this.clientRequirementService.deleteUserFiles(userName);
			}

			this.logger.info("Add ReportTemplate");
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = null;
				items = upload.parseRequest(request);
				Iterator<FileItem> itr = items.iterator();
				this.fileItemIteratorMethod(itr, clientRequirmentMasterVO, modelAndViewHMap, request);
				String[] selectedClientCodes = new String[this.selectedClientCodesList.size()];

				for (int i = 0; i < this.selectedClientCodesList.size(); ++i) {
					selectedClientCodes[i] = (String) this.selectedClientCodesList.get(i);
				}

				this.uiClientGrid(session);
				modelAndViewHMap.put("reportType", clientRequirmentMasterVO.getReportType());
				modelAndViewHMap.put("general", clientRequirmentMasterVO.getIsGeneral());
				if (this.fileUploadlimitExceed(modelAndViewHMap, clientRequirmentMasterVO)) {
					session.setAttribute("modelAndViewHMap", modelAndViewHMap);
					return modelAndView;
				}

				this.uploadableFileInSession(clientRequirmentMasterVO, userName, session);
				modelAndViewHMap.put("tempFileUploadError", false);
				this.logger.info("Client requirement download is Temporary added/updated");
			}
		} catch (CMSException var16) {
			this.logger.debug("CMSException " + var16);
			modelAndViewHMap.put("tempFileUploadError", true);
		} catch (NullPointerException var17) {
			this.logger.debug("NullPointerException " + var17);
			modelAndViewHMap.put("tempFileUploadError", true);
		} catch (Exception var18) {
			this.logger.debug("Exception " + var18);
			modelAndViewHMap.put("tempFileUploadError", true);
		}

		session.setAttribute("modelAndViewHMap", modelAndViewHMap);
		return modelAndView;
	}

	private void uploadableFileInSession(ClientRequirmentMasterVO clientRequirmentMasterVO, String userName,
			HttpSession session) throws CMSException {
		HashMap uploadableFileHashMap = null;

		try {
			uploadableFileHashMap = this.clientRequirementService.addClientReq(clientRequirmentMasterVO, this.file,
					userName);
			Set<String> keySet = uploadableFileHashMap.keySet();

			String key;
			for (Iterator keySetIterator = keySet.iterator(); keySetIterator.hasNext(); this.logger
					.info("addclientReq:::::::::::::" + key + "::"
							+ ((ClientRequirmentMasterVO) this.sessionUploadableFilesHashMap.get(key))
									.getUploadedFile())) {
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

	private Boolean fileUploadlimitExceed(HashMap modelAndViewHMap, ClientRequirmentMasterVO clientRequirmentMasterVO)
			throws CMSException {
		String NOOFUPLOADFILES = "NoOfUploadFiles";
		String UPLOADFILESIZE = "UploadFileSize";
		String var5 = "IndividualUploadFileSize";

		try {
			int limit = 1;
			List<ClientRequirmentMasterVO> list = new ArrayList(this.sessionUploadableFilesHashMap.values());
			double size = 0.0D;
			double indSize = 0.0D;
			int limit = limit + list.size() / this.selectedClientCodesList.size();

			for (Iterator sessionItr = list.iterator(); sessionItr.hasNext(); size += (double) Float
					.parseFloat(((ClientRequirmentMasterVO) sessionItr.next()).getFileSize())) {
				;
			}

			size += (double) this.file.length() / 1024.0D;
			indSize = (double) this.file.length() / 1048576.0D;
			this.logger.debug(size + "::" + indSize + "::" + limit + ":::" + this.selectedClientCodesList.size() + ":::"
					+ list.size());
			if (limit > Integer.parseInt(this.clientRequirementService.getproperties().getProperty("NoOfUploadFiles"))
					|| size / 1024.0D > (double) Float
							.parseFloat(this.clientRequirementService.getproperties().getProperty("UploadFileSize"))
					|| indSize > (double) Float.parseFloat(
							this.clientRequirementService.getproperties().getProperty("IndividualUploadFileSize"))) {
				modelAndViewHMap.put("limitExceedMsg",
						"Limit Exceeded (totalfiles <= 5 & total size <= 100MB & individual file size <= 20MB)");
				modelAndViewHMap.put("reportType", clientRequirmentMasterVO.getReportType());
				modelAndViewHMap.put("general", clientRequirmentMasterVO.getIsGeneral());
				this.file.delete();
				return true;
			}
		} catch (Exception var13) {
			throw new CMSException(this.logger, var13);
		}

		return false;
	}

	private void uiClientGrid(HttpSession session) throws CMSException {
		List<ClientMasterVO> uiclientlist = null;
		ArrayList uiselectedClientlist = null;

		try {
			uiselectedClientlist = new ArrayList();
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
				uiselectedClientlist.add(cmvo);
			}

			session.setAttribute("jsonSelectedClientList", uiselectedClientlist);
			uiclientlist = new ArrayList();
			if (this.codeNames != null) {
				arr$ = this.codeNames;
				len$ = arr$.length;

				for (i$ = 0; i$ < len$; ++i$) {
					codeName = arr$[i$];
					cmvo = new ClientMasterVO();
					cmvo.setClientCode(codeName.split("-")[0]);
					code = codeName.split("-")[0];
					cmvo.setCodeName(codeName);
					uiclientlist.add(cmvo);
				}
			}

			session.setAttribute("jsonClientList", uiclientlist);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	private void fileItemIteratorMethod(Iterator<FileItem> itr, ClientRequirmentMasterVO clientRequirmentMasterVO,
			HashMap modelAndViewHMap, HttpServletRequest request) throws CMSException {
		String var5 = "UTF-8";

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

					String sb2;
					int index;
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("selectedCodeName")) {
							this.selectedCodeNames = item.getString().split("@SEPERATOR@");
							this.selectedClientCodesList = new ArrayList();
							String[] arr$ = this.selectedCodeNames;
							int len$ = arr$.length;

							for (index = 0; index < len$; ++index) {
								String selectedCodeName = arr$[index];
								this.selectedClientCodesList.add(selectedCodeName.split("-")[0]);
							}
						}

						if (item.getFieldName().equalsIgnoreCase("codeName") && item.getString() != null
								&& !item.getString().isEmpty() && item.getString().length() > 1) {
							this.codeNames = item.getString().split("@SEPERATOR@");
						}

						if (item.getFieldName().equalsIgnoreCase("hreportTypeCmb")) {
							this.reportType = item.getString();
							this.logger.info("reportType:" + this.reportType);
							clientRequirmentMasterVO.setReportType(this.reportType);
						}

						BufferedReader r1;
						if (item.getFieldName().equalsIgnoreCase("comments")) {
							sb2 = "";

							try {
								for (r1 = new BufferedReader(new InputStreamReader(item.getInputStream(),
										"UTF-8")); (this.comment = r1.readLine()) != null; sb2 = sb2 + this.comment
												+ "\n") {
									;
								}
							} catch (Exception var13) {
								this.logger
										.debug("Error in POI to handle special chars in string  for comment" + var13);
							}

							this.logger.info("comment:" + sb2);
							clientRequirmentMasterVO.setComment(sb2);
						}

						if (item.getFieldName().equalsIgnoreCase("instructions")) {
							sb2 = "";

							try {
								for (r1 = new BufferedReader(new InputStreamReader(item.getInputStream(),
										"UTF-8")); (this.instruction = r1.readLine()) != null; sb2 = sb2
												+ this.instruction + "\n") {
									;
								}
							} catch (Exception var14) {
								this.logger.debug(
										"Error in POI to handle special chars in string  for instruction" + var14);
							}

							this.logger.info("Instruction:" + sb2);
							clientRequirmentMasterVO.setInstruction(sb2);
						}

						if (item.getFieldName().equalsIgnoreCase("general")) {
							this.general = item.getString();
							clientRequirmentMasterVO.setIsGeneral(this.general);
							this.logger.info(".do>>general" + this.general);
						}

						if (item.getFieldName().equalsIgnoreCase("isUpdatePage")) {
							if (!item.getString().equalsIgnoreCase("")) {
								modelAndViewHMap.put("isUpdatePage", true);
							}

							this.logger.info("isUpdatePage:::" + item.getString());
						}

						if (item.getFieldName().equalsIgnoreCase("clientReqId")) {
							modelAndViewHMap.put("clientReqId", item.getString());
							this.logger.info("CLIENT_REQ_ID FROM FORM::" + item.getString());
						}
					} else {
						sb2 = "";

						try {
							sb2 = this.clientRequirementService.getproperties().getProperty("uploadableFilesTempPath");
						} catch (Exception var12) {
							throw new CMSException(this.logger, var12);
						}

						String fileName = item.getName();
						if (fileName.contains("\\")) {
							index = fileName.lastIndexOf("\\");
							fileName = fileName.substring(index + 1);
						}

						this.logger.info(fileName);
						this.file = new File(sb2, fileName);

						try {
							item.write(this.file);
						} catch (Exception var11) {
							throw new CMSException(this.logger, var11);
						}
					}
				}
			}
		} catch (Exception var15) {
			throw new CMSException(this.logger, var15);
		}
	}

	public ModelAndView openUpdateClientRequirement(HttpServletRequest request, HttpServletResponse response,
			ClientRequirmentMasterVO ClientRequirmentMasterVO) throws Exception {
		this.logger.debug("In openUpdateClientRequirement method");
		ModelAndView modelAndView = new ModelAndView("redirect:newAndUpdateClientRequirement.do");
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

			String clientReqId = request.getParameter("clientReqId");
			ClientRequirmentMasterVO crvo = this.clientRequirementService.getClientRequirementDAO()
					.getInsertedData(Integer.parseInt(clientReqId));
			HashMap<String, ClientRequirmentMasterVO> sessionHashMap = null;
			if (session.getAttribute("sessionUploadedFiles") != null) {
				sessionHashMap = (HashMap) session.getAttribute("sessionUploadedFiles");
			} else {
				sessionHashMap = new HashMap();
			}

			String key = crvo.getClientCode() + crvo.getReportType() + crvo.getUploadedFile();
			sessionHashMap.put(key, crvo);
			session.setAttribute("sessionUploadedFiles", sessionHashMap);
			List<ClientRequirmentMasterVO> historydatavoList = this.clientRequirementService.getClientRequirementDAO()
					.getHistoryDataOfClient(crvo.getClientCode());
			Iterator<ClientRequirmentMasterVO> itr = historydatavoList.iterator();
			HashMap historyDataSessionHashMap = new HashMap();

			String comment;
			while (itr.hasNext()) {
				ClientRequirmentMasterVO crvoitr = (ClientRequirmentMasterVO) itr.next();
				comment = crvoitr.getClientCode() + crvoitr.getReportType() + crvoitr.getUploadedFile();
				historyDataSessionHashMap.put(comment, crvoitr);
			}

			session.setAttribute("sessionHistoryFiles", historyDataSessionHashMap);
			String reportType = crvo.getReportType();
			comment = crvo.getComment();
			String instruction = crvo.getInstruction();
			String general = crvo.getIsGeneral();
			uiselectedClientlist = new ArrayList();
			ClientMasterVO cmvo = new ClientMasterVO();
			cmvo.setClientCode(crvo.getClientCode());
			cmvo.setCodeName(crvo.getClientCode() + "-" + crvo.getClientName());
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
			modelAndViewHMap.put("instruction", instruction);
			modelAndViewHMap.put("general", general);
			modelAndViewHMap.put("isUpdatePage", true);
			modelAndViewHMap.put("clientReqId", clientReqId);
		} catch (NullPointerException var25) {
			return AtlasUtils.getExceptionView(this.logger, var25);
		} catch (Exception var26) {
			return AtlasUtils.getExceptionView(this.logger, var26);
		}

		session.setAttribute("modelAndViewHMap", modelAndViewHMap);
		return modelAndView;
	}
}