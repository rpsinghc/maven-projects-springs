package com.worldcheck.atlas.frontend.document;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import java.io.IOException;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class DocumentMultiActionController extends MultiActionController {
	private static final String PROCSS_CYCLE = "ProcssCycle";
	private static final String ACTIVITY = "activity";
	private static final String CASE_STATUS = "CaseStatus";
	private static final String CASE_MANAGER = "CaseManager";
	PropertyReaderUtil propertyReader = null;
	private static final long MB = 1048576L;
	private static final String fileName = "fileName";
	private static final String singleFileSize = "fileSize";
	private static final String totalFileSize = "totalFileSize";
	private static final String attachmentJSP = "Attachment";
	private static final String byteParams = "bytes";
	private static final String docNameParam = "docName";
	private static final String docIdParam = "docId";
	private static final String showDocJSP = "showDocument";
	private static final String CONTENT_TYPE_TEXT_HTML = "text/html";
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.document.DocumentMultiActionController");

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public ModelAndView uploadDocument(HttpServletRequest request, HttpServletResponse response) {
		long maxSize = (long) this.propertyReader.getMaxAllowedTotalFileSize();
		long maxTotalSizeReceived = 0L;
		List<FileItem> items = null;
		JSONObject jsonObject = new JSONObject();

		try {
			String message = "uploadDocument Method of Document Action";
			this.logger.debug(message);
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			this.logger.debug("request is " + request);
			String activityName = "";
			String folderName1 = "";
			String folderName2 = "";
			String folderName3 = "";
			String folderName4 = "";
			String folderName5 = "";
			String userName;
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				items = upload.parseRequest(request);
				this.logger.debug(items + "");
				Iterator itr = items.iterator();

				while (itr.hasNext()) {
					FileItem item = (FileItem) itr.next();
					if (null != item) {
						if (item.isFormField()) {
							this.logger.debug("Item name is " + item.getFieldName());
							if (item.getFieldName().equalsIgnoreCase("activity")) {
								activityName = item.getString();
							} else if (item.getFieldName().equalsIgnoreCase("folderName1")) {
								folderName1 = item.getString();
							} else if (item.getFieldName().equalsIgnoreCase("folderName2")) {
								folderName2 = item.getString();
							} else if (item.getFieldName().equalsIgnoreCase("folderName3")) {
								folderName3 = item.getString();
							} else if (item.getFieldName().equalsIgnoreCase("folderName4")) {
								folderName4 = item.getString();
							} else if (item.getFieldName().equalsIgnoreCase("folderName5")) {
								folderName5 = item.getString();
							}
						} else {
							userName = item.getFieldName();
							long fileSize = item.getSize();
							if (userName.startsWith("file") && !item.getName().equalsIgnoreCase("")) {
								this.logger.debug("size of the file" + userName + " is " + item.getSize());
								if (0L == fileSize) {
									response.setContentType("text/html");
									jsonObject.put("fileName", item.getName());
									jsonObject.put("fileSize", 0);
									jsonObject.write(response.getWriter());
									this.logger.debug("before returning null");
									return null;
								}

								fileSize = item.getSize() / 1048576L;
								maxTotalSizeReceived += fileSize;
								if ((float) fileSize > this.propertyReader.getMaxAllowedIndividualFileSize()) {
									response.setContentType("text/html");
									jsonObject.put("fileName", item.getName());
									jsonObject.put("fileSize", fileSize);
									jsonObject.write(response.getWriter());
									this.logger.debug("before returning null");
									return null;
								}
							}
						}
					}
				}
			}

			if (maxTotalSizeReceived > maxSize) {
				response.setContentType("text/html");
				jsonObject.put("totalFileSize", maxTotalSizeReceived);
				jsonObject.write(response.getWriter());
				this.logger.debug("before returning null");
				return null;
			}

			this.logger.debug("activity name is " + activityName);
			ResourceLocator locator = ResourceLocator.self();
			String pid = activityName.split("::")[0].split("#")[1];
			this.logger.debug(pid);
			String tempFolderNames = "";
			if (null != folderName1 && !"Select...".equalsIgnoreCase(folderName1)
					&& !"".equalsIgnoreCase(folderName1)) {
				tempFolderNames = folderName1;
			}

			if (null != folderName2 && !"Select...".equalsIgnoreCase(folderName2)
					&& !"".equalsIgnoreCase(folderName2)) {
				if ("".equalsIgnoreCase(tempFolderNames)) {
					tempFolderNames = folderName2;
				} else {
					tempFolderNames = tempFolderNames + "," + folderName2;
				}
			}

			if (null != folderName3 && !"Select...".equalsIgnoreCase(folderName3)
					&& !"".equalsIgnoreCase(folderName3)) {
				if ("".equalsIgnoreCase(tempFolderNames)) {
					tempFolderNames = folderName3;
				} else {
					tempFolderNames = tempFolderNames + "," + folderName3;
				}
			}

			if (null != folderName4 && !"Select...".equalsIgnoreCase(folderName4)
					&& !"".equalsIgnoreCase(folderName4)) {
				if ("".equalsIgnoreCase(tempFolderNames)) {
					tempFolderNames = folderName4;
				} else {
					tempFolderNames = tempFolderNames + "," + folderName4;
				}
			}

			if (null != folderName5 && !"Select...".equalsIgnoreCase(folderName5)
					&& !"".equalsIgnoreCase(folderName5)) {
				if ("".equalsIgnoreCase(tempFolderNames)) {
					tempFolderNames = folderName5;
				} else {
					tempFolderNames = tempFolderNames + "," + folderName5;
				}
			}

			this.logger.debug("folder names :: " + tempFolderNames);
			String[] folderNames = tempFolderNames.split(",");
			userName = SBMUtils.getSession(request).getUser();
			this.logger.debug("request.getSession().getAttribute(loginLevel) is "
					+ request.getSession().getAttribute("loginLevel"));
			if (null != request.getSession().getAttribute("loginLevel")) {
				userName = (String) request.getSession().getAttribute("performedBy");
			}

			this.logger.debug("in upload section .User name is " + userName);
			locator.getDocService().CreateDoc(userName, items, pid, folderNames, SBMUtils.getSession(request));
			response.setContentType("text/html");
			jsonObject.put("success", true);
			jsonObject.write(response.getWriter());
		} catch (FileUploadException var34) {
			this.logger.error(var34);

			try {
				response.setContentType("text/html");
				jsonObject.put("fileSize", "");
				jsonObject.write(response.getWriter());
			} catch (JSONException var32) {
				this.logger.error(var32);
			} catch (IOException var33) {
				this.logger.error(var33);
			}
		} catch (CMSException var35) {
			this.logger.error(var35);

			try {
				response.setContentType("text/html");
				jsonObject.put("fileSize", "");
				jsonObject.write(response.getWriter());
			} catch (JSONException var30) {
				this.logger.error(var30);
			} catch (IOException var31) {
				this.logger.error(var31);
			}
		} catch (IOException var36) {
			this.logger.error(var36);

			try {
				response.setContentType("text/html");
				jsonObject.put("fileSize", "");
				jsonObject.write(response.getWriter());
			} catch (JSONException var28) {
				this.logger.error(var28);
			} catch (IOException var29) {
				this.logger.error(var29);
			}
		} catch (JSONException var37) {
			this.logger.error(var37);

			try {
				response.setContentType("text/html");
				jsonObject.put("fileSize", "");
				jsonObject.write(response.getWriter());
			} catch (JSONException var26) {
				this.logger.error(var26);
			} catch (IOException var27) {
				this.logger.error(var27);
			}
		} catch (Exception var38) {
			this.logger.error(var38);

			try {
				response.setContentType("text/html");
				jsonObject.put("fileSize", "");
				jsonObject.write(response.getWriter());
			} catch (JSONException var24) {
				this.logger.error(var24);
			} catch (IOException var25) {
				this.logger.error(var25);
			}
		}

		return null;
	}

	public ModelAndView showDocument(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in showDocument");
		ResourceLocator locator = ResourceLocator.self();
		String docId = request.getParameter("docId");
		Map<String, Object> mp = locator.getDocService().displayDocument(docId);
		ModelAndView displayView = new ModelAndView("showDocument");
		displayView.addObject("bytes", mp.get("bytes"));
		displayView.addObject("docName", mp.get("docName"));
		return displayView;
	}

	public ModelAndView attachment(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in attachment");
		ModelAndView displayView = new ModelAndView("Attachment");
		String crn = request.getParameter("crn");
		if (null != crn && !"".equalsIgnoreCase(crn)) {
			this.logger.debug("CRN is " + crn);

			try {
				CaseDetails caseDetailsVO = ResourceLocator.self().getCaseDetailService().getCaseInfo(crn);
				displayView.addObject("CaseManager", caseDetailsVO.getCaseMgrId());
				displayView.addObject("CaseStatus", caseDetailsVO.getCaseStatus());
				displayView.addObject("activity", "CaseCreation#" + caseDetailsVO.getPid());
				displayView.addObject("crn", crn);
				String processCycle = "";
				if (ResourceLocator.self().getSBMService().isTaskCompleted(Long.parseLong(caseDetailsVO.getPid()),
						SBMUtils.getSession(request))) {
					processCycle = "Final";
				} else {
					processCycle = (String) ResourceLocator.self().getSBMService().getDataslotValue(
							Long.parseLong(caseDetailsVO.getPid()), "ProcessCycle", SBMUtils.getSession(request));
				}

				displayView.addObject("ProcssCycle", processCycle);
			} catch (CMSException var7) {
				this.logger.error("Exception occured " + var7);
			}
		}

		return displayView;
	}
}