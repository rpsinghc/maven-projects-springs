package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.util.PService;
import com.worldcheck.atlas.bl.interfaces.ISupportApp;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.SBMUtils;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.SupportAppVO;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONSupportAppController extends JSONMultiActionController {
	private static final String JSONVIEW = "jsonView";
	private static final String TRUEVAL = "true";
	private static final String FALSEVAL = "false";
	private static final String INVALIDVAL = "invalid";
	private static final String CRNVAL = "crn";
	private static final String CDD = "cdd";
	private static final String CSD = "csd";
	private static final String ACTION = "action";
	private static final String SUCCESS = "success";
	private static final String SEQKEY = "seqkey";
	private static final String SECURITYCODE = "atlas@support";
	private static final String AUTHTWO_VAL = "pwdVal";
	private static final String CASESTATUS = "caseStatus";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.JSONSupportAppController");
	ISupportApp supportAppManager = null;

	public void setSupportAppManager(ISupportApp supportAppManager) {
		this.supportAppManager = supportAppManager;
	}

	public ModelAndView searchCRNDetails(HttpServletRequest request, HttpServletResponse response,
			SupportAppVO supportAppVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		new SupportAppVO();
		this.logger.debug("IN searchCRNDetails");
		String crn = request.getParameter("action");
		this.logger.debug("The Crn is ==" + crn);

		try {
			SupportAppVO suppVO = this.supportAppManager.searchCRNDetails(crn);
			if (suppVO != null) {
				modelAndView.addObject("success", "true");
				modelAndView.addObject("crn", suppVO.getCrn());
				modelAndView.addObject("cdd", suppVO.getClientDueDate());
				modelAndView.addObject("csd", suppVO.getClientSentDate());
				modelAndView.addObject("caseStatus", suppVO.getCase_status());
			} else {
				modelAndView.addObject("success", "false");
			}

			return modelAndView;
		} catch (CMSException var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView updateCRNDates(HttpServletRequest request, HttpServletResponse response,
			SupportAppVO supportAppVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		SupportAppVO suppVO = new SupportAppVO();
		this.logger.debug("IN updateCRNDates");
		String crn = request.getParameter("action");
		String cdd = request.getParameter("cdd");
		String csd = request.getParameter("csd");
		this.logger.debug("The Crn is ==" + crn + "cdd" + cdd + "csd" + csd);

		try {
			suppVO.setCrn(crn);
			suppVO.setClientDueDate(cdd);
			suppVO.setClientSentDate(csd);
			this.supportAppManager.updateCRNDates(suppVO);
			modelAndView.addObject("success", "true");
			return modelAndView;
		} catch (CMSException var10) {
			modelAndView.addObject("success", "false");
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		} catch (Exception var11) {
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		}
	}

	public ModelAndView getUserPWD(HttpServletRequest request, HttpServletResponse response, SupportAppVO supportAppVO)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		this.logger.debug("IN getUserPWD");
		String userId = request.getParameter("action");
		String seqrKey = request.getParameter("seqkey");
		this.logger.debug("The UserId is ==" + userId + "===SeqKey==" + seqrKey);

		try {
			if (seqrKey.equals("atlas@support")) {
				this.logger.debug("Correct Security Code.");
				String passwrd = this.supportAppManager.getUserPWD(userId);
				passwrd = PService.self().decrypt(passwrd);
				modelAndView.addObject("pwdVal", passwrd);
				modelAndView.addObject("success", "true");
			} else {
				modelAndView.addObject("success", "invalid");
			}

			return modelAndView;
		} catch (CMSException var8) {
			modelAndView.addObject("success", "false");
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView pullBackToClientSubmission(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		this.logger.debug("IN doPullBackToClientSubmission");
		String crn = request.getParameter("crn");
		this.logger.debug("The CRN is ==" + crn);
		HashMap<String, Object> dsMap = new HashMap();
		String[] activatedWSNames = (String[]) null;
		String wsName = null;

		try {
			if (crn != null) {
				long pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(crn);
				this.logger.debug("pid for case::::" + pid);
				boolean taskCompleted = ResourceLocator.self().getSBMService().isTaskCompleted(Long.valueOf(pid),
						SBMUtils.getSession(request));
				if (!taskCompleted) {
					activatedWSNames = ResourceLocator.self().getSBMService()
							.getActivatedWSNames(SBMUtils.getSession(request), pid);
					this.logger.debug("activatedWSNames.length:::" + activatedWSNames.length);
					if (activatedWSNames.length == 1 && activatedWSNames[0].equals("ProcessCompletion")) {
						wsName = activatedWSNames[0];
						this.logger.debug("WorkStep Name is::::" + wsName);
						dsMap.put("isPullback", true);
						ResourceLocator.self().getSBMService().updateDataSlots(SBMUtils.getSession(request), pid,
								dsMap);
						this.logger.debug("after updating dataslots....");
						ResourceLocator.self().getSBMService().completeTask(SBMUtils.getSession(request), dsMap,
								Long.valueOf(pid), wsName);
						this.logger.debug("After pullback::");
						modelAndView.addObject("success", "true");
					}
				} else {
					modelAndView.addObject("success", "false");
				}
			}

			return modelAndView;
		} catch (CMSException var11) {
			this.logger.debug("Exception::" + var11.getStackTrace());
			return AtlasUtils.getJsonExceptionView(this.logger, var11, response);
		} catch (Exception var12) {
			return AtlasUtils.getJsonExceptionView(this.logger, var12, response);
		}
	}
}