package com.worldcheck.atlas.frontend.report;

import com.savvion.acl.impl.SBMACLUser;
import com.savvion.sbm.bpmportal.domain.Dashboard;
import com.worldcheck.atlas.bl.report.ViewJLPReport;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class ViewJLPReportController extends AbstractController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.ViewJLPReportController");
	private String REPORT_TYPE_LIST = "reportTypes";
	private String JSP = "misJLPView";
	private String VIEW_DASHBOARD = "ViewDashboard";
	private String DASHBOARD_LIST = "mydashboards";
	private String DASHBOARD = "dashboard";
	private String DISABLE_RESTORE_BUTTON = "disableRestoreButton";
	private String CUSTOM_DASHBOARD_ID = "customdashboardid";
	private String BCP_REQUEST_PARAM = "isRequestFromBcp";
	private PropertyReaderUtil propertyReader;
	private ViewJLPReport viewJLPReport;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public void setViewJLPReport(ViewJLPReport viewJLPReport) {
		this.viewJLPReport = viewJLPReport;
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ModelAndView modelandview = null;

		try {
			this.logger.debug("in  ViewJLPReportController ");
			HttpSession httpsession = httpServletRequest.getSession(false);
			SBMACLUser sbmacluser = (SBMACLUser) httpsession.getAttribute("sbmAclUserObj");
			Dashboard dash = this.viewJLPReport.getDashBoard();
			if (dash != null) {
				List<String> userList = new ArrayList();
				userList.add(sbmacluser.getName());
				dash.setUsers(userList);
				HashMap<String, String> hashmap = new HashMap();
				hashmap.put(String.valueOf(dash.getId()), dash.getName());
				httpsession.setAttribute(this.VIEW_DASHBOARD, dash);
				httpsession.setAttribute("reportName", "JLPReport");
				modelandview = new ModelAndView(this.JSP);
				this.logger.debug("reportTypes :: " + this.propertyReader.getOfficeUtilizationReportTypes());
				List<String> reportTypelist = StringUtils
						.commaSeparatedStringToList(this.propertyReader.getOfficeUtilizationReportTypes());
				modelandview.addObject(this.REPORT_TYPE_LIST, reportTypelist);
				modelandview.addObject(this.DASHBOARD_LIST, hashmap);
				modelandview.addObject(this.DASHBOARD, dash);
				modelandview.addObject(this.DISABLE_RESTORE_BUTTON, false);
				modelandview.addObject(this.CUSTOM_DASHBOARD_ID, dash.getId());
				modelandview.addObject(this.BCP_REQUEST_PARAM, true);
			} else {
				this.logger.error("Office Utilization dashboard neither exists nor created.");
			}

			this.logger.debug("exiting  ViewJLPReportController ");
			return modelandview;
		} catch (UnsupportedOperationException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (ClassCastException var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		} catch (NullPointerException var12) {
			return AtlasUtils.getExceptionView(this.logger, var12);
		} catch (IllegalArgumentException var13) {
			return AtlasUtils.getExceptionView(this.logger, var13);
		} catch (CMSException var14) {
			return AtlasUtils.getExceptionView(this.logger, var14);
		} catch (Exception var15) {
			return AtlasUtils.getExceptionView(this.logger, var15);
		}
	}
}