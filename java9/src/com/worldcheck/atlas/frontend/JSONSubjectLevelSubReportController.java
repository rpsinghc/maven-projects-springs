package com.worldcheck.atlas.frontend;

import com.worldcheck.atlas.bl.sublevelsubreport.SubjectLevelSubReportManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.report.SubReportTypeVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONSubjectLevelSubReportController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.JSONSubjectLevelSubReportController");
	private static final String JSONVIEW = "jsonView";
	private SubjectLevelSubReportManager subjectLevelSubReportManager = null;

	public void setSubjectLevelSubReportManager(SubjectLevelSubReportManager subjectLevelSubReportManager) {
		this.subjectLevelSubReportManager = subjectLevelSubReportManager;
	}

	public ModelAndView getReportTypeMasterForSubjectLevel(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("In getReportTypeMasterForSubjectLevel method");
		ModelAndView modelAndView = new ModelAndView("jsonView");
		String code_Name = null;

		try {
			if (request.getParameter("code_Name") != null) {
				code_Name = request.getParameter("code_Name");
				code_Name = code_Name.split("-")[0];
			}

			this.logger.debug("In getReportTypeMasterForSubjectLevel method code_Name:" + code_Name);
			List<ReportTypeMasterVO> reportTypeMasterList = this.subjectLevelSubReportManager
					.getReportTypeMasterForSubjectLevel(code_Name);
			modelAndView.addObject("reportTypeMasterList", reportTypeMasterList);
			this.logger.debug("out getReportTypeMasterForSubjectLevel method" + reportTypeMasterList);
			return modelAndView;
		} catch (CMSException var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getSubReportTypeMasterForSubj(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<SubReportTypeVO> subReportTypeMasterList = this.subjectLevelSubReportManager
					.getSubReportTypeMasterForSubj(request.getParameter("reportType"));
			modelAndView.addObject("subReportTypeMasterList", subReportTypeMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getClientMasterForSubjLevelConfig(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<ClientMasterVO> clientMasterList = this.subjectLevelSubReportManager
					.getClientMasterForSubjLevelConfig(request.getParameter("clientCode"));
			modelAndView.addObject("clientMasterList", clientMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}
}