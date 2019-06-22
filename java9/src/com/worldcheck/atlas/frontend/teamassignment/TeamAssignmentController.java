package com.worldcheck.atlas.frontend.teamassignment;

import com.worldcheck.atlas.bl.interfaces.ITeamAssignment;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class TeamAssignmentController extends MultiActionController {
	private static final String REDIRECT_TEAM_ASSIGNMENT_TAB_DO = "redirect:teamAssignmentTab.do";
	private static final String TEAM_ASS_SECTION_JSP = "setupTeamAssignmentSection";
	private static final String TEAM_ASS_SECTION_JSP_READ = "setupTeamAssignmentSectionRead";
	private static final String TEAM_ASS_JSP = "teamAssignmentSection";
	private static final String TEAM_ASS_JSP_READ = "teamAssignmentSectionRead";
	private static final String TEAMTYPE = "teamType";
	private static final String TEAM_ASS_TAB_JSP = "teamAssignmentTab";
	private static final String LEGACY_TEAM_ASS_SECTION_JSP = "legacyTeamAssignmentSection";
	private ITeamAssignment teamAssignmentManager = null;
	private String successReassignView = "";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.teamassignment.TeamAssignmentController");

	public void setSuccessReassignView(String successReassignView) {
		this.successReassignView = successReassignView;
	}

	public void setTeamAssignmentManager(ITeamAssignment teamAssignmentManager) {
		this.teamAssignmentManager = teamAssignmentManager;
	}

	public ModelAndView completeTeamAssignment(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		HashMap<String, Object> hmAttributes = new HashMap();
		hmAttributes.put("PRIORITY", "medium");
		System.out.println("\n Process Created Sucessfully..");
		return new ModelAndView("");
	}

	public ModelAndView saveTeamAssignment(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = this.teamAssignmentManager.saveAssignmentTask(request);
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView reassignTeamAssignment(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			this.teamAssignmentManager.reassignTeamAssignmentTask(request);
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}

		modelAndView = new ModelAndView(this.successReassignView);
		return modelAndView;
	}

	public ModelAndView teamAssignment(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn in team assignment section " + crn);
			modelAndView = new ModelAndView("setupTeamAssignmentSection");
			modelAndView.addObject(crn);
			modelAndView.addObject("crn", crn);
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView teamAssignmentR(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn in team assignment section " + crn);
			modelAndView = new ModelAndView("setupTeamAssignmentSectionRead");
			modelAndView.addObject(crn);
			modelAndView.addObject("crn", crn);
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView teamAssignmentLegacy(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn in legacy team assignment section " + crn);
			modelAndView = new ModelAndView("legacyTeamAssignmentSection");
			modelAndView.addObject(crn);
			modelAndView.addObject("crn", crn);
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView updateTeamAssignment(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn is " + crn);
			modelAndView = new ModelAndView("teamAssignmentSection");
			modelAndView.addObject("crn", crn);
			modelAndView.addObject("teamType", request.getParameter("teamType"));
			modelAndView.addObject(request.getParameter("teamType"));
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView readOnlyTeamAssignment(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn is " + crn);
			modelAndView = new ModelAndView("teamAssignmentSectionRead");
			modelAndView.addObject("crn", crn);
			modelAndView.addObject("teamType", request.getParameter("teamType"));
			modelAndView.addObject(request.getParameter("teamType"));
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView legacyTeamDetails(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			String crn = request.getParameter("crn");
			this.logger.debug("crn is " + crn);
			modelAndView = new ModelAndView("legacyTeamDetailsSection");
			modelAndView.addObject("crn", crn);
			modelAndView.addObject("teamType", request.getParameter("teamType"));
			modelAndView.addObject(request.getParameter("teamType"));
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView teamAssignmentTab(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;

		try {
			modelAndView = new ModelAndView("teamAssignmentTab");
			return modelAndView;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView saveTeamAssignmentTab(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		ModelAndView modelAndView = null;
		String jspName = "redirect:teamAssignmentTab.do";

		try {
			String crn = request.getParameter("crn");
			if (null != request.getParameter("teamAssignmentLeftNav")
					&& request.getParameter("teamAssignmentLeftNav").trim().length() > 0
					&& request.getParameter("teamAssignmentLeftNav").equals("true")) {
				jspName = "redirect:teamAssignment.do";
			}

			this.teamAssignmentManager.saveAssignmentTaskTab(request);
			modelAndView = new ModelAndView(jspName);
			modelAndView.addObject("crn", crn);
			return modelAndView;
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}
}