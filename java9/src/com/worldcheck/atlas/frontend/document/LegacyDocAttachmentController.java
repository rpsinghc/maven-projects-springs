package com.worldcheck.atlas.frontend.document;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.document.LegacyDocAttachmentService;
import com.worldcheck.atlas.utils.Constants;
import com.worldcheck.atlas.vo.document.LegacyDocAttachmentVO;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class LegacyDocAttachmentController extends MultiActionController {
	Constants constants;
	LegacyDocAttachmentService legacyDocAttachmentService;
	String LEGACYATTACHMENT = "legacyAttachment";
	String REQUESTDOCNAME = "docName";
	String REQUESTCRN = "crn";
	private static final String byteParams = "bytes";
	private static final String docNameParam = "docName";
	private static final String showDocJSP = "showDocument";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.document.LegacyDocAttachmentController");

	public void setLegacyDocAttachmentService(LegacyDocAttachmentService legacyDocAttachmentService) {
		this.legacyDocAttachmentService = legacyDocAttachmentService;
	}

	public ModelAndView displayAllLegacyDocuments(HttpServletRequest request, HttpServletResponse response,
			LegacyDocAttachmentVO legacyDocAttachmentVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView(this.LEGACYATTACHMENT);
		Constants var10001 = this.constants;
		modelAndView.addObject("crn", request.getParameter("crn"));
		return modelAndView;
	}

	public ModelAndView showLegacyDocument(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in showDocument");
		ModelAndView displayView = new ModelAndView("showDocument");
		String docName = request.getParameter(this.REQUESTDOCNAME);
		String crn = request.getParameter(this.REQUESTCRN);

		try {
			displayView.addObject("bytes", this.legacyDocAttachmentService.showLegacyDocument(crn, docName));
		} catch (IOException var7) {
			this.logger.error(var7);
		}

		displayView.addObject("docName", docName);
		return displayView;
	}
}