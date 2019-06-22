package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.interfaces.IBranchOfficeMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONOfficeUBPPCController extends JSONController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.JSONOfficeUBPPCController");
	private IBranchOfficeMaster branchOfficeMultiActionManager = null;
	private String OFFICE_LIST = "officeList";

	public void setBranchOfficeMultiActionManager(IBranchOfficeMaster branchOfficeMultiActionManager) {
		this.branchOfficeMultiActionManager = branchOfficeMultiActionManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		this.logger.debug("in JSONOfficeUBPPCController");
		ModelAndView modelandview = super.handleRequest(request, response);
		if (null != modelandview) {
			return modelandview;
		} else {
			try {
				List<BranchOfficeMasterVO> officeList = this.branchOfficeMultiActionManager
						.getUserAndSubordinateOffices(request);
				this.logger.debug("officeList size :: " + officeList.size());
				modelandview = new ModelAndView("jsonView");
				modelandview.addObject(this.OFFICE_LIST, officeList);
				this.logger.debug("out of JSONOfficeUBPPCController");
				return modelandview;
			} catch (CMSException var5) {
				return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
			} catch (Exception var6) {
				return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
			}
		}
	}
}