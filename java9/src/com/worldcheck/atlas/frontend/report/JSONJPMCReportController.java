package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.masters.JPMCReportManager;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.MaterializedViewVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONJPMCReportController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JsonJPMCReportController");
	private ModelAndView mv = null;
	JPMCReportManager jpmcReportManager = null;

	public void setJpmcReportManager(JPMCReportManager jpmcReportManager) {
		this.jpmcReportManager = jpmcReportManager;
	}

	public ModelAndView getMaterializedViewRefreshTimeJPMCDailyReports(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			this.mv = new ModelAndView("jsonView");
			this.logger.debug("MV name::::" + request.getParameter("mvName"));
			List<MaterializedViewVO> mvRefreshList = this.jpmcReportManager
					.getMaterializedViewRefreshTime(request.getParameter("mvName"));
			this.logger.debug("MV LIST ON controller:" + mvRefreshList.size());
			this.mv.addObject("mvRefreshList", mvRefreshList);
		} catch (NullPointerException var4) {
			return AtlasUtils.getExceptionView(this.logger, var4);
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}

		return this.mv;
	}
}