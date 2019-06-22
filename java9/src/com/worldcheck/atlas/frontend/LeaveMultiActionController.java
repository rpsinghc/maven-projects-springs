package com.worldcheck.atlas.frontend;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.ILeaveMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.leave.LeaveVO;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class LeaveMultiActionController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.frontend.LeaveMultiActionController");
	private ILeaveMaster leaveService = null;
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
	SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
	private static final String LEAVE_TYPE = "leaveType";
	private static final String FROM_DATE = "fromDate";
	private static final String TO_DATE = "toDate";
	private static final String LEAVE_ID = "leaveId";
	private final String REDIRECT_PAGE = "redirect:leaveSearch.do";
	private static final String USER_NAME = "userName";
	private static final String LEAVES_CUSTOM_JSP = "leaves_custom";
	private static final String MESSAGE = "message";
	private static final String BACKUP1 = "backup1Value";
	private static final String BACKUP2 = "backup2Value";

	public void setLeaveService(ILeaveMaster leaveService) {
		this.leaveService = leaveService;
	}

	public ModelAndView saveLeave(HttpServletRequest request, HttpServletResponse response, LeaveVO leaveVO) {
		String userId = "";
		String leaveType = "";
		String startDate = "";
		String endDate = "";
		String message = "";
		String backup1 = "";
		String backup2 = "";
		ModelAndView mv = new ModelAndView("redirect:leaveSearch.do");
		userId = request.getParameter("userName");
		leaveType = request.getParameter("leaveType");
		startDate = request.getParameter("fromDate");
		endDate = request.getParameter("toDate");
		backup1 = request.getParameter("backup1Value");
		backup2 = request.getParameter("backup2Value");
		this.logger.debug("LeaveMultiActionController.saveLeave() " + leaveType + " userId= " + userId + " startDate = "
				+ startDate + " endDate= " + endDate + " backuup1 = " + backup1 + " backup2= " + backup2);
		leaveVO.setUserName(userId);
		leaveVO.setLeaveType(leaveType);

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			leaveVO.setFromDate(startDate);
			leaveVO.setToDate(endDate);
			leaveVO.setBackup1(backup1);
			leaveVO.setBackup2(backup2);
			leaveVO.setUpdatedBy(userBean.getUserName());
			this.leaveService.addLeave(leaveVO);
		} catch (CMSException var13) {
			return AtlasUtils.getExceptionView(this.logger, var13);
		} catch (Exception var14) {
			return AtlasUtils.getExceptionView(this.logger, var14);
		}

		message = "Leave Saved Successfully.";
		HttpSession session = request.getSession();
		session.setAttribute("message", message);
		return mv;
	}

	public ModelAndView deleteLeave(HttpServletRequest request, HttpServletResponse response, LeaveVO leaveVO) {
		String message = "";
		ModelAndView mv = new ModelAndView("redirect:leaveSearch.do");
		String leaveId = null;
		if (null != request.getParameter("leaveId")) {
			leaveId = request.getParameter("leaveId");

			try {
				this.leaveService.deleteLeave(leaveId);
			} catch (CMSException var8) {
				return AtlasUtils.getExceptionView(this.logger, var8);
			} catch (Exception var9) {
				return AtlasUtils.getExceptionView(this.logger, var9);
			}

			message = "Leave Deleted successfully.";
		}

		HttpSession session = request.getSession();
		session.setAttribute("message", message);
		return mv;
	}

	public ModelAndView leaveSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("leaves_custom");
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

	public ModelAndView updateLeave(HttpServletRequest request, HttpServletResponse response, LeaveVO leaveVO) {
		ModelAndView mv = new ModelAndView("redirect:leaveSearch.do");

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			this.logger.debug("LeaveMultiActionController.updateLeave()");
			this.leaveService.updateLeave(leaveVO.getModifiedRecords(), userBean.getUserName());
			HttpSession session = request.getSession();
			session.setAttribute("message", "Leave Updated Successfully.");
			return mv;
		} catch (CMSException var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		} catch (Exception var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		}
	}
}