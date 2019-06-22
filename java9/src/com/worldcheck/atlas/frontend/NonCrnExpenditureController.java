package com.worldcheck.atlas.frontend;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.INonCrnExpenditure;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.noncrnexpenditure.NonCRNExpenditureVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class NonCrnExpenditureController extends MultiActionController {
	private static final String DATA_SAVED_SUCCESSFULLY = "Data Saved Successfully.";
	private static final String MESSAGE = "message";
	private static final String NONCRN_CUSTOM_JSP = "nonCrnExpenditure";
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.NonCrnExpenditureController");
	private final String REDIRECT_PAGE = "redirect:setupNonCRNExpenditure.do";
	private INonCrnExpenditure nonCrnExpenditureService = null;

	public void setNonCrnExpenditureService(INonCrnExpenditure nonCrnExpenditureService) {
		this.nonCrnExpenditureService = nonCrnExpenditureService;
	}

	public ModelAndView saveCRNData(HttpServletRequest request, HttpServletResponse response,
			NonCRNExpenditureVO nonCRNExpenditureVO) throws Exception {
		ModelAndView mv = new ModelAndView("redirect:setupNonCRNExpenditure.do");

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			nonCRNExpenditureVO.setUserName(userBean.getUserName());
			this.logger.debug(" data captured " + nonCRNExpenditureVO.toString());
			this.nonCrnExpenditureService.saveCRNData(nonCRNExpenditureVO);
			HttpSession session = request.getSession();
			session.setAttribute("message", "Data Saved Successfully.");
			this.logger.info("Data saved successfully");
			return mv;
		} catch (CMSException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}

	public ModelAndView setupNonCRNExpenditure(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("nonCrnExpenditure");
			HttpSession session = request.getSession();
			if (session.getAttribute("message") != null) {
				modelAndView.addObject("message", session.getAttribute("message"));
				session.removeAttribute("message");
			}

			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getExceptionView(this.logger, var5);
		}
	}
}