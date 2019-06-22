package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.downloadexcel.CSVDownloader;
import com.worldcheck.atlas.bl.interfaces.IFeedBack;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import com.worldcheck.atlas.vo.masters.ClientFeedBackVO;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class ClientFeedBackController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.ClientFeedBackController");
	private String CLIENTFEEDBACKJSP = "clientFeedBackSearch";
	private String ADDCLIENTFEEDBACK = "addClientFeedBack";
	private String SEARCHPAGE = "searchpage";
	private final String FEEDBACKID = "fbID";
	private final String FBID_TO_UPDATE = "fbIdInfo";
	private final String UPDATEDONE = "updatedone";
	private final String CREATEUPDATE = "createupdate";
	private static final String GETCFBINFO = "getCFBInfo";
	private String ACTION = "action";
	private String FBIDONREFRESH = "fbidonrefresh";
	private String UPDATE = "update";
	private String CREATED = "created";
	private static final String showDocJSP = "showDocument";
	private static PropertyReaderUtil propertyReader = null;
	private static final String CONTENT_TYPE_TEXT_HTML = "text/html";
	private static final String REDIRECT_FEEDBACK_SEARCH = "redirect:goToClientFeedBack.do";
	private static final String byteParams = "bytes";
	private static final String docNameParam = "docName";
	private static final String LOGGED_ON = "Logged On";
	private static final String CLIENT_CODE = "Client Code";
	private static final String CLIENT_NAME = "Client Name";
	private static final String CLIENT_CONTACT_FIELD = "Client Contact Field";
	private static final String LINKED_CRN = "Linked CRN";
	private static final String FEEDBACK_CATEGORY = "Feedback Category";
	private static final String FEEDBACK_DESCRIPTION = "Feedback Description";
	private static final String LAST_ACTION = "Last Action taken";
	private static final String ATTACHMENT = "Attachement";
	private static final String CASE_STATUS = "Feedback Status";
	private static final String CASE_OWNER = "Case Owner";
	private static final String DATE_CLOSED = "Date Closed";
	private static final String FEEDBACK_DATA = "FeedbackData";
	IFeedBack clientFeedBackManager = null;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public void setClientFeedBackManager(IFeedBack clientFeedBackManager) {
		this.clientFeedBackManager = clientFeedBackManager;
	}

	public ModelAndView goToClientFeedBack(HttpServletRequest request, HttpServletResponse response,
			UserMasterVO userMasterVO) {
		ModelAndView modelAndView = new ModelAndView(this.CLIENTFEEDBACKJSP);

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			long userID = ResourceLocator.self().getSBMService().getUserID(userMasterVO.getUserID());
			this.logger.debug("goToClientFeedBack User Name is (R-1.3.1)>>>>>  " + userName);
			userMasterVO.setUserMasterId(userID);
			this.logger.debug("goToClientFeedBack User ID is (R-1.3.1)>>>>>" + userMasterVO.getUserID());
			if (request.getSession().getAttribute(this.ACTION) != null
					&& request.getSession().getAttribute(this.ACTION).equals("updatedone")) {
				modelAndView.addObject(this.ACTION, request.getSession().getAttribute(this.ACTION));
				request.getSession().removeAttribute(this.ACTION);
			} else if (request.getSession().getAttribute(this.ACTION) != null
					&& request.getSession().getAttribute(this.ACTION).equals("createupdate")) {
				modelAndView.addObject(this.ACTION, this.CREATED);
				request.getSession().removeAttribute(this.ACTION);
			} else if (request.getSession().getAttribute(this.ACTION) != null
					&& request.getSession().getAttribute(this.ACTION).equals(this.CREATED)) {
				modelAndView.addObject(this.ACTION, this.SEARCHPAGE);
				request.getSession().removeAttribute(this.ACTION);
			}

			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView addClientFeedBack(HttpServletRequest request, HttpServletResponse response,
			BranchOfficeMasterVO branchOfficeMasterVO) {
		this.logger.debug("In addClientFeedBack method");
		ModelAndView modelAndView = null;

		try {
			request.getSession().removeAttribute(this.ACTION);
			String sourceTempAttachments = propertyReader.getFeedback_temp_attachment_path()
					+ request.getSession().getId();
			File srcTemp = new File(sourceTempAttachments);
			if (srcTemp.exists()) {
				File[] sFiles = srcTemp.listFiles();
				File[] var11 = sFiles;
				int var10 = sFiles.length;

				for (int var9 = 0; var9 < var10; ++var9) {
					File f = var11[var9];
					f.delete();
				}
			}

			modelAndView = new ModelAndView(this.ADDCLIENTFEEDBACK);
			modelAndView.addObject("create", true);
			return modelAndView;
		} catch (Exception var12) {
			return AtlasUtils.getExceptionView(this.logger, var12);
		}
	}

	public ModelAndView saveClientFeedBack(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) {
		ModelAndView modelAndView = null;
		String isAction = (String) request.getSession().getAttribute(this.ACTION);

		try {
			this.logger.debug("IN saveClientFeedBack");
			this.logger.debug("LInkedCRNList==" + request.getParameter("linkedCRNList"));
			this.logger.debug("Add Details:: Client Name=" + clientFeedBackVO.getClientNameField()
					+ "**FeedBackCategory=" + clientFeedBackVO.getFbCategoryField() + "**Case Owner="
					+ clientFeedBackVO.getCasOwnerField() + "**Case Status" + clientFeedBackVO.getFbStatusField()
					+ "**Client Contect**" + clientFeedBackVO.getClientContactField() + "**Feedback Desc**"
					+ clientFeedBackVO.getFbDescriptionField() + "**Action Taken**"
					+ clientFeedBackVO.getActionTakenField() + "###FeedbackTypeID: "
					+ request.getParameter("fbFeedbackTypeField"));
			clientFeedBackVO.setFeedback_type_id(request.getParameter("fbFeedbackTypeField"));
			long fbID;
			if (isAction != null && isAction.equalsIgnoreCase(this.CREATED)) {
				this.logger.debug(
						"saveClientFeedBack else block==>" + request.getSession().getAttribute(this.FBIDONREFRESH));
				fbID = Long.parseLong(request.getSession().getAttribute(this.FBIDONREFRESH).toString());
			} else {
				this.logger.debug("saveClientFeedBack if block");
				if (Integer.parseInt(clientFeedBackVO.getFbStatusField()) == 5) {
					Date date = new Date();
					String completeDate = (new SimpleDateFormat("dd-MM-yy")).format(date);
					this.logger.debug("sysdate========>>>" + completeDate);
					clientFeedBackVO.setDateClosed(completeDate);
				} else {
					clientFeedBackVO.setDateClosed((String) null);
				}

				fbID = this.clientFeedBackManager.saveClientFeedBack(clientFeedBackVO, request);
				request.getSession().setAttribute(this.FBIDONREFRESH, fbID);
			}

			request.getSession().setAttribute(this.ACTION, this.CREATED);
			if (clientFeedBackVO.getFbStatusField().equals("5")) {
				modelAndView = new ModelAndView("redirect:goToClientFeedBack.do");
			} else {
				modelAndView = this.getFeedBackInfoForAddAttachment(fbID);
			}

			modelAndView.addObject("create", true);
			return modelAndView;
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView getFeedBackInfo(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView(this.ADDCLIENTFEEDBACK);
			this.logger.debug("IN getFeedBackInfo");
			this.logger.debug("Feed Back ID==" + request.getParameter("fbID"));
			if (request.getParameter("fbID") == null) {
				return new ModelAndView("redirect:goToClientFeedBack.do");
			} else {
				long feedBackId = (long) Integer.parseInt(request.getParameter("fbID"));
				ClientFeedBackVO cfbVO = this.clientFeedBackManager.getFeedBackInfo(feedBackId);
				if (cfbVO == null) {
					return new ModelAndView("redirect:goToClientFeedBack.do");
				} else {
					modelAndView.addObject("getCFBInfo", cfbVO);
					if (request.getSession().getAttribute(this.ACTION) == null) {
						request.getSession().setAttribute(this.ACTION, this.UPDATE);
					}

					return modelAndView;
				}
			}
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView updateClientFeedBack(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) {
		ModelAndView modelAndView = null;

		try {
			this.logger.debug("IN updateClientFeedBack");
			this.logger.debug("LInkedCRNList==" + request.getParameter("linkedCRNList"));
			this.logger.debug("Feed Back ID==" + request.getParameter("fbIdInfo"));
			this.logger.debug("Add Details:: Client Name=" + clientFeedBackVO.getClientNameField()
					+ "**FeedBackCategory=" + clientFeedBackVO.getFbCategoryField() + "**Case Owner="
					+ clientFeedBackVO.getCasOwnerField() + "**Case Status" + clientFeedBackVO.getFbStatusField()
					+ "**Client Contect**" + clientFeedBackVO.getClientContactField() + "**Feedback Desc**"
					+ clientFeedBackVO.getFbDescriptionField() + "**Action Taken**"
					+ clientFeedBackVO.getActionTakenField());
			modelAndView = new ModelAndView("redirect:goToClientFeedBack.do");
			clientFeedBackVO.setNewFbSeqId((long) Integer.parseInt(request.getParameter("fbIdInfo")));
			clientFeedBackVO.setFeedback_type_id(request.getParameter("fbFeedbackTypeField"));
			this.clientFeedBackManager.updateClientFeedBack(clientFeedBackVO, request);
			if (request.getSession().getAttribute(this.ACTION) != null
					&& !request.getSession().getAttribute(this.ACTION).equals("update")) {
				if (request.getSession().getAttribute(this.ACTION).equals(this.CREATED)) {
					request.getSession().setAttribute(this.ACTION, "createupdate");
				}
			} else {
				request.getSession().setAttribute(this.ACTION, "updatedone");
			}

			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getFeedBackInfoForAddAttachment(long fbID) throws CMSException {
		ModelAndView modelAndView = null;
		modelAndView = new ModelAndView(this.ADDCLIENTFEEDBACK);
		this.logger.debug("IN getFeedBackInfo");
		this.logger.debug("Feed Back ID==" + fbID);
		ClientFeedBackVO cfbVO = this.clientFeedBackManager.getFeedBackInfo(fbID);
		if (cfbVO == null) {
			return new ModelAndView("redirect:goToClientFeedBack.do");
		} else {
			modelAndView.addObject("getCFBInfo", cfbVO);
			return modelAndView;
		}
	}

	public ModelAndView uploadFeedBackAttachment(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in side upload method");
		List<FileItem> items = null;
		JSONObject jsonObject = new JSONObject();

		try {
			String userName = SBMUtils.getSession(request).getUser();
			this.logger.debug("username>>>>>>" + userName);
			String message = "uploadDocument Method of Document Action";
			this.logger.debug(message);
			this.logger.debug("Feedback ID==" + request.getSession().getAttribute("fbIDforAttach"));
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			long fbID = (long) Integer.parseInt((String) request.getSession().getAttribute("fbIDforAttach"));
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				items = upload.parseRequest(request);
				this.logger.debug("" + items);
				float limit = propertyReader.getFeedback_attachment_size_limit();
				this.logger.debug("limit=" + limit);
				if ((float) ((FileItem) items.get(0)).getSize() > limit) {
					jsonObject.put("success", false);
					jsonObject.put("failure", true);
					jsonObject.put("fileSize", ((FileItem) items.get(0)).getSize());
				} else {
					this.clientFeedBackManager.uploadFeedBackAttachment(items, fbID, userName);
					jsonObject.put("success", true);
				}

				response.setContentType("text/html");
				jsonObject.write(response.getWriter());
			}
		} catch (FileUploadException var13) {
			this.logger.error(var13);
		} catch (CMSException var14) {
			this.logger.error(var14);
		} catch (Exception var15) {
			this.logger.error(var15);
		}

		return null;
	}

	public ModelAndView uploadTempFeedBackAttachment(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in side upload method");
		List<FileItem> items = null;
		JSONObject jsonObject = new JSONObject();

		try {
			String userName = SBMUtils.getSession(request).getUser();
			this.logger.debug("username>>>>>>" + userName);
			String message = "uploadDocument Method of Document Action";
			this.logger.debug(message);
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				items = upload.parseRequest(request);
				ClientFeedBackVO clientFeedBackVO = new ClientFeedBackVO();
				clientFeedBackVO.setLogInUserId(userName);
				clientFeedBackVO.setUpdatedBy(userName);
				clientFeedBackVO.setSessionID(request.getSession().getId());
				clientFeedBackVO.setFilePath(
						propertyReader.getFeedback_temp_attachment_path() + "\\" + request.getSession().getId() + "\\");
				clientFeedBackVO.setFileSize(((FileItem) items.get(0)).getSize() / 1024L + " KB");
				clientFeedBackVO.setFileName(((FileItem) items.get(0)).getName());
				this.logger.debug("" + items);
				float limit = propertyReader.getFeedback_attachment_size_limit();
				this.logger.debug("limit=" + limit);
				if ((float) ((FileItem) items.get(0)).getSize() > limit) {
					jsonObject.put("success", false);
					jsonObject.put("failure", true);
					jsonObject.put("fileSize", ((FileItem) items.get(0)).getSize());
				} else {
					this.clientFeedBackManager.uploadTempFeedBackAttachment(items, userName, clientFeedBackVO);
					jsonObject.put("success", true);
				}

				response.setContentType("text/html");
				jsonObject.write(response.getWriter());
			}
		} catch (FileUploadException var12) {
			this.logger.error(var12);
		} catch (CMSException var13) {
			this.logger.error(var13);
		} catch (Exception var14) {
			this.logger.error(var14);
		}

		return null;
	}

	public ModelAndView showFbAttachment(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in showFbAttachment");
		String fileName = request.getParameter("attFileName");
		String filePath = request.getParameter("attFilePath");
		ModelAndView displayView = new ModelAndView("showDocument");

		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];

			int readNum;
			while ((readNum = fis.read(buf)) != -1) {
				bos.write(buf, 0, readNum);
				this.logger.debug("read>>>>>>> " + readNum + " bytes,");
			}

			byte[] b = bos.toByteArray();
			this.logger.debug("file Name>>" + fileName + ">>FilePath>>" + filePath + ">>bytes>>" + b);
			displayView.addObject("bytes", b);
			displayView.addObject("docName", fileName);
			return displayView;
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView exportToExcelClientFeedback(HttpServletRequest request, HttpServletResponse response,
			ClientMasterVO clientMasterVO) {
		this.logger.debug("in export to excel");
		ModelAndView modelandview = null;

		try {
			String excelParams = request.getParameter("excelParams");
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			this.logger.debug("Values " + excelParamMap.get("dateLoggedStart"));
			this.logger.debug("Values " + excelParamMap.get("dateLoggedEnd"));
			this.logger.debug("Values " + excelParamMap.get("searchClientCode"));
			this.logger.debug("Values " + excelParamMap.get("searchCaseOwner"));
			this.logger.debug("Values " + excelParamMap.get("searchCaseStatus"));
			this.logger.debug("parameter " + excelParamMap);
			new ArrayList();
			List<ClientFeedBackVO> searchResult = this.clientFeedBackManager.getFeedbackForExport(excelParamMap);
			this.logger.debug("number of records " + searchResult.size());
			String fileName = this.writeToExcel(searchResult, response);
			modelandview = new ModelAndView("misExcelDownloadPopup");
			modelandview.addObject("fileName", fileName);
			return modelandview;
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	private String writeToExcel(List<ClientFeedBackVO> resultList, HttpServletResponse response) throws CMSException {
		this.logger.debug("in  write To Excel ");
		List<String> lstHeader = this.getHeaderList();
		List<List<String>> dataList = new ArrayList();
		ClientFeedBackVO clientFeedBackVO = null;

		try {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				List<String> datamap = new ArrayList();
				clientFeedBackVO = (ClientFeedBackVO) iterator.next();
				this.populateDataMap(clientFeedBackVO, datamap);
				dataList.add(datamap);
			}

			return CSVDownloader.exportCSV(lstHeader, dataList, "FeedbackData", response);
		} catch (UnsupportedOperationException var8) {
			throw new CMSException(this.logger, var8);
		} catch (ClassCastException var9) {
			throw new CMSException(this.logger, var9);
		} catch (NullPointerException var10) {
			throw new CMSException(this.logger, var10);
		} catch (IllegalArgumentException var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	private List<String> getHeaderList() throws CMSException {
		ArrayList lstHeader = new ArrayList();

		try {
			lstHeader.add("Logged On");
			lstHeader.add("Client Code");
			lstHeader.add("Client Name");
			lstHeader.add("Client Contact Field");
			lstHeader.add("Linked CRN");
			lstHeader.add("Feedback Category");
			lstHeader.add("Feedback Description");
			lstHeader.add("Last Action taken");
			lstHeader.add("Attachement");
			lstHeader.add("Feedback Status");
			lstHeader.add("Case Owner");
			lstHeader.add("Date Closed");
			return lstHeader;
		} catch (UnsupportedOperationException var3) {
			throw new CMSException(this.logger, var3);
		} catch (ClassCastException var4) {
			throw new CMSException(this.logger, var4);
		} catch (NullPointerException var5) {
			throw new CMSException(this.logger, var5);
		} catch (IllegalArgumentException var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private void populateDataMap(ClientFeedBackVO clientFeedBackVO, List<String> datamap) {
		datamap.add(String
				.valueOf(clientFeedBackVO.getSearchDateLogged() != null ? clientFeedBackVO.getSearchDateLogged() : ""));
		datamap.add(String
				.valueOf(clientFeedBackVO.getSearchClientCode() != null ? clientFeedBackVO.getSearchClientCode() : ""));
		datamap.add(String
				.valueOf(clientFeedBackVO.getSearchClientName() != null ? clientFeedBackVO.getSearchClientName() : ""));
		datamap.add(String.valueOf(
				clientFeedBackVO.getClientContactField() != null ? clientFeedBackVO.getClientContactField() : ""));
		datamap.add(String.valueOf(clientFeedBackVO.getSearchCRN() != null ? clientFeedBackVO.getSearchCRN() : ""));
		datamap.add(String.valueOf(
				clientFeedBackVO.getFeedBackCategoryName() != null ? clientFeedBackVO.getFeedBackCategoryName() : ""));
		datamap.add(String.valueOf(
				clientFeedBackVO.getSearchfbDescription() != null ? clientFeedBackVO.getSearchfbDescription() : ""));
		datamap.add(String
				.valueOf(clientFeedBackVO.getActionTakenField() != null ? clientFeedBackVO.getActionTakenField() : ""));
		datamap.add(String.valueOf(clientFeedBackVO.getIsAttach() != null ? clientFeedBackVO.getIsAttach() : ""));
		datamap.add(String
				.valueOf(clientFeedBackVO.getSearchCaseStatus() != null ? clientFeedBackVO.getSearchCaseStatus() : ""));
		datamap.add(String
				.valueOf(clientFeedBackVO.getSearchCaseOwner() != null ? clientFeedBackVO.getSearchCaseOwner() : ""));
		datamap.add(String.valueOf(clientFeedBackVO.getDateClosed() != null ? clientFeedBackVO.getDateClosed() : ""));
	}

	public ModelAndView feedbackSearchHistory(HttpServletRequest request, HttpServletResponse response,
			ClientFeedBackVO clientFeedBackVO) {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView(this.ADDCLIENTFEEDBACK);
			this.logger.debug("IN feedbackSearchHistory");
			String excelParams = (String) request.getSession().getAttribute("searchHistory");
			if (excelParams != null) {
				Map jsonObject = (Map) JSONValue.parse(excelParams);
				if (jsonObject.get("fbID") == null) {
					return new ModelAndView("redirect:goToClientFeedBack.do");
				} else {
					this.logger.debug("Feed Back ID==" + (String) jsonObject.get("fbID"));
					long feedBackId = (long) Integer.parseInt((String) jsonObject.get("fbID"));
					ClientFeedBackVO cfbVO = this.clientFeedBackManager.getFeedBackInfo(feedBackId);
					if (cfbVO == null) {
						return new ModelAndView("redirect:goToClientFeedBack.do");
					} else {
						modelAndView.addObject("getCFBInfo", cfbVO);
						if (request.getSession().getAttribute(this.ACTION) == null) {
							request.getSession().setAttribute(this.ACTION, this.UPDATE);
						}

						request.getSession().removeAttribute("searchHistory");
						return modelAndView;
					}
				}
			} else {
				return new ModelAndView("redirect:goToClientFeedBack.do");
			}
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}
}