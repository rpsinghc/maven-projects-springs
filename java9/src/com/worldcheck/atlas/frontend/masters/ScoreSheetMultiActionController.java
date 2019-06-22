package com.worldcheck.atlas.frontend.masters;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.bl.interfaces.IScoreSheetMaster;
import com.worldcheck.atlas.bl.masters.CategoryXmlParser;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.ScoreSheetCategoryVO;
import com.worldcheck.atlas.vo.masters.ScoreSheetMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class ScoreSheetMultiActionController extends MultiActionController {
	private static final String SUBMIT_TYPE = "submitType";
	private static final String UPDATE = "update";
	private static final String BLANK = "";
	private static final String SAVE = "save";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.ScoreSheetMultiActionController");
	private String SCORESHEET_MASTER_LIST = "scoresheet_master_list";
	private String SCORESHEET_ADD = "scoresheet_add";
	HashMap<Object, Object> keyValueMap = new HashMap();
	List userRoleTypeList = new ArrayList();
	private List<ScoreSheetCategoryVO> categoryList;
	Map<String, List<ScoreSheetCategoryVO>> categoryMap = new HashMap();
	private IScoreSheetMaster scoreSheetManager;

	public void setScoreSheetManager(IScoreSheetMaster scoreSheetManager) {
		this.scoreSheetManager = scoreSheetManager;
	}

	public ModelAndView getScoreSheetInfo(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) throws Exception {
		ModelAndView mv = null;
		this.logger.debug("ScoreSheetMultiActionController.getScoreSheetInfo()++++++++++"
				+ scoreSheetMasterVO.getGetScoreSheetMasterId());

		try {
			if (null != scoreSheetMasterVO.getGetScoreSheetMasterId()
					&& !scoreSheetMasterVO.getGetScoreSheetMasterId().equalsIgnoreCase("")) {
				mv = new ModelAndView(this.SCORESHEET_ADD);
				scoreSheetMasterVO = this.scoreSheetManager
						.getScoreSheetInfo(scoreSheetMasterVO.getGetScoreSheetMasterId());
				mv.addObject("scoreSheetMasterVO", scoreSheetMasterVO);
				mv.addObject("action", "update");
				return mv;
			} else {
				return new ModelAndView("redirect:scoreSheetSearchJspRedirect.do");
			}
		} catch (CMSException var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		} catch (Exception var7) {
			return AtlasUtils.getExceptionView(this.logger, var7);
		}
	}

	public ModelAndView addScoreSheet(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) throws Exception {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView("redirect:scoreSheetSearchJspRedirect.do");
			String message = "Add NEW SCORESHEET";
			HttpSession session = request.getSession();
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			scoreSheetMasterVO.setUpdatedBy(userBean.getUserName());
			this.logger.debug("coreSheetMultiActionController.addScoreSheet()");
			this.logger.debug("\nOfficeCode:" + scoreSheetMasterVO.getOffice() + "\nSchoreSheetName:"
					+ scoreSheetMasterVO.getScoreSheetName() + "\nSelected Fields:"
					+ scoreSheetMasterVO.getSelectedReviewField() + "\nGrading System:"
					+ scoreSheetMasterVO.getGradingSystem() + "\nCategory Data List:"
					+ scoreSheetMasterVO.getCategoryString());
			scoreSheetMasterVO.setCategoryList(CategoryXmlParser.parseXml(scoreSheetMasterVO.getCategoryString()));
			if (null != request.getParameter("action")) {
				if (request.getParameter("action").equalsIgnoreCase("update")) {
					this.logger.debug("update method called");
					this.logger.debug("\n<<<<ScoreSheet Master Id (Deleted):>>>"
							+ scoreSheetMasterVO.getScoresheetMasterId() + "<<<****>>>>");
					this.scoreSheetManager.updateScoreSheet(scoreSheetMasterVO);
					session.setAttribute("update", true);
				} else {
					this.logger.debug("New ScoreSheet Will be Added..");
					this.scoreSheetManager.addScoreSheet(scoreSheetMasterVO);
					session.setAttribute("save", true);
				}

				return mv;
			} else {
				return mv;
			}
		} catch (CMSException var8) {
			return AtlasUtils.getExceptionView(this.logger, var8);
		} catch (Exception var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		}
	}

	public ModelAndView scoreSheetAddUpdateJspRedirect(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView(this.SCORESHEET_ADD);
			return mv;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}

	public ModelAndView scoreSheetSearchJspRedirect(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) {
		ModelAndView mv = null;

		try {
			mv = new ModelAndView(this.SCORESHEET_MASTER_LIST);
			if (request.getSession().getAttribute("save") != null) {
				mv.addObject("submitType", "save");
				request.getSession().removeAttribute("save");
			}

			if (request.getSession().getAttribute("update") != null) {
				mv.addObject("submitType", "update");
				request.getSession().removeAttribute("update");
			}

			return mv;
		} catch (Exception var6) {
			return AtlasUtils.getExceptionView(this.logger, var6);
		}
	}
}