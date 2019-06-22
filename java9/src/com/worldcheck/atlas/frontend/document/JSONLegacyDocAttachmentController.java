package com.worldcheck.atlas.frontend.document;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.document.LegacyDocAttachmentService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.Constants;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.document.LegacyDocAttachmentVO;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONLegacyDocAttachmentController extends JSONMultiActionController {
	Constants constants;
	private String empty = "";
	LegacyDocAttachmentService legacyDocAttachmentService;
	String LISTREFERENCE = "listInReference";
	private static PropertyReaderUtil propertyReader;
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.JSONLegacyDocAttachmentController");
	private static final String URL_PARAMS = "/sbm/bpmportal/atlas/showLegacyDocument.do?crn=";

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public void setLegacyDocAttachmentService(LegacyDocAttachmentService legacyDocAttachmentService) {
		this.legacyDocAttachmentService = legacyDocAttachmentService;
	}

	public ModelAndView displayAllLegacyDocumentsInfo(HttpServletRequest request, HttpServletResponse response,
			LegacyDocAttachmentVO legacyDocAttachmentVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			Constants var10001 = this.constants;
			String crnVal = request.getParameter("crn");
			legacyDocAttachmentVO.setCrn(crnVal);
			new ArrayList();
			List<LegacyDocAttachmentVO> legacyDocAttachmentList = this.legacyDocAttachmentService.getAttachmentDetails(
					legacyDocAttachmentVO, Integer.parseInt(request.getParameter("start")),
					Integer.parseInt(request.getParameter("limit")), request.getParameter("sort"),
					request.getParameter("dir"));
			int count = this.legacyDocAttachmentService.getAttachmentDetailsCount(legacyDocAttachmentVO);
			int i = 0;
			List<LegacyDocAttachmentVO> legacyDocAttachmentList = new ArrayList();
			String fileNameWithVersion = "";
			String newFileNameTemp = "";
			int ind = true;
			Iterator iterator = legacyDocAttachmentList.iterator();

			while (iterator.hasNext()) {
				LegacyDocAttachmentVO legacyDocAttachmentVO2 = (LegacyDocAttachmentVO) iterator.next();
				fileNameWithVersion = "";
				newFileNameTemp = "";
				ind = true;
				String fileName = legacyDocAttachmentVO2.getFileName();
				this.logger.debug("file name is comming" + propertyReader.getLegacyDocAttachmentUrl() + "\\"
						+ legacyDocAttachmentVO2.getFolderName() + "\\" + crnVal.replace("\\", "~")
						+ legacyDocAttachmentVO2.getFileName());
				fileNameWithVersion = legacyDocAttachmentVO2.getFileName();
				int ind = fileNameWithVersion.lastIndexOf(".");
				if (ind != -1) {
					newFileNameTemp = fileNameWithVersion.substring(0, ind);
					newFileNameTemp = newFileNameTemp + "_" + legacyDocAttachmentVO2.getVersion()
							+ fileNameWithVersion.substring(ind);
					fileNameWithVersion = newFileNameTemp;
				} else {
					fileNameWithVersion = fileNameWithVersion + "_" + legacyDocAttachmentVO2.getVersion();
				}

				this.logger.debug("fileNameWithVersion :: " + fileNameWithVersion);
				File newFile = new File(
						propertyReader.getLegacyDocAttachmentUrl() + "\\" + legacyDocAttachmentVO2.getFolderName()
								+ "\\" + crnVal.replace("\\", "~") + "\\" + fileNameWithVersion);
				if (null != legacyDocAttachmentVO2.getFolderName()
						&& !legacyDocAttachmentVO2.getFolderName().equals(this.empty) && newFile.exists()) {
					legacyDocAttachmentVO2.setFileSize((double) (newFile.length() / 1024L));
					String encodedurl = URLEncoder.encode(fileNameWithVersion, "UTF-8");
					legacyDocAttachmentVO2.setFileName("<a href='/sbm/bpmportal/atlas/showLegacyDocument.do?crn="
							+ legacyDocAttachmentVO2.getFolderName() + "\\" + crnVal.replace("\\", "~") + "&docName="
							+ encodedurl + "'>" + fileName + "</a>");
				} else {
					legacyDocAttachmentVO2.setFileSize(0.0D);
					legacyDocAttachmentVO2.setFileName("<a href='javascript:void(null);' onclick='noFolderAlert()''>"
							+ legacyDocAttachmentVO2.getFileName() + "</a>");
				}

				legacyDocAttachmentList.add(legacyDocAttachmentVO2);
				++i;
				this.logger.debug("fetching VO " + i);
			}

			modelAndView.addObject(this.LISTREFERENCE, legacyDocAttachmentList);
			modelAndView.addObject("total", count);
			return modelAndView;
		} catch (CMSException var18) {
			return AtlasUtils.getJsonExceptionView(this.logger, var18, response);
		} catch (Exception var19) {
			return AtlasUtils.getJsonExceptionView(this.logger, var19, response);
		}
	}
}