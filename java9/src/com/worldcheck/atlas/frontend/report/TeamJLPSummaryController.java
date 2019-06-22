package com.worldcheck.atlas.frontend.report;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.ITeamJLPSummary;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.services.report.AtlasReportService;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ReportConstants;
import com.worldcheck.atlas.utils.StringUtils;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class TeamJLPSummaryController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.TeamJLPSummaryController");
	private static final String misTeamJLP = "misTeamJLP";
	private static final String YEAR_LIST = "yearList";
	private static final String MONTH_MAP = "monthList";
	private static final String CURRENT_MONTH = "currentMonth";
	private static final String LOGIN_ID = "loginId";
	private static final String CURRENT_YEAR = "currentYear";
	private PropertyReaderUtil propertyReader;
	private AtlasReportService atlasReportService;
	private ITeamJLPSummary teamJLPSummaryManager;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public void setAtlasReportService(AtlasReportService atlasReportService) {
		this.atlasReportService = atlasReportService;
	}

	public void setTeamJLPSummaryManager(ITeamJLPSummary teamJLPSummaryManager) {
		this.teamJLPSummaryManager = teamJLPSummaryManager;
	}

	public ModelAndView teamJLPSummary(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.logger.debug("In TeamJLPSummaryController : teamJLPSummary");

		try {
			ModelAndView modelAndView = new ModelAndView("misTeamJLP");
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			List<String> yearList = StringUtils.commaSeparatedStringToList(this.propertyReader.getYearList());
			Calendar cal = Calendar.getInstance();
			int currentYear = cal.get(1);
			int currentMonth = cal.get(2) + 1;
			modelAndView.addObject("yearList", yearList);
			modelAndView.addObject("monthList", ReportConstants.monthMap);
			modelAndView.addObject("currentYear", currentYear);
			modelAndView.addObject("currentMonth", currentMonth);
			modelAndView.addObject("loginId", userBean.getUserName());
			return modelAndView;
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}
}