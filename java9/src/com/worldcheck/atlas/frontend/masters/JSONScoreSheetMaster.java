package com.worldcheck.atlas.frontend.masters;

import com.worldcheck.atlas.bl.interfaces.IScoreSheetMaster;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.vo.masters.ScoreSheetCategoryVO;
import com.worldcheck.atlas.vo.masters.ScoreSheetMasterVO;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONScoreSheetMaster extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.JSONScoreSheetMaster");
	private String SCORESHEETGRIDLIST = "scoreSheetGridList";
	private String SUCCESS = "success";
	private String TRUE = "true";
	private String FALSE = "false";
	private String ACTION = "action";
	private String BLANK = "";
	private String result = "";
	private int count;
	private ArrayList scoreSheetGridList = null;
	private ModelAndView mv = null;
	private IScoreSheetMaster scoreSheetManager;

	public void setScoreSheetManager(IScoreSheetMaster scoreSheetManager) {
		this.scoreSheetManager = scoreSheetManager;
	}

	public ModelAndView searchScoreSheet(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) {
		try {
			this.mv = new ModelAndView("jsonView");
			this.logger.debug("Inside the JSONContryMaster");
			this.scoreSheetGridList = (ArrayList) this.scoreSheetManager.getScoreSheetGrid(scoreSheetMasterVO,
					Integer.parseInt(request.getParameter("start")), Integer.parseInt(request.getParameter("limit")),
					request.getParameter("sort"), request.getParameter("dir"));
			this.count = this.scoreSheetManager.getScoreSheetCount(scoreSheetMasterVO);
			this.mv.addObject("total", this.count);
			this.mv.addObject(this.SCORESHEETGRIDLIST, this.scoreSheetGridList);
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		return this.mv;
	}

	public ModelAndView searchExistScoreSheetName(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) {
		String message = "Searching for Exist Score SheetName";
		String scoreSheetName = request.getParameter(this.ACTION);
		boolean result = this.scoreSheetManager.isExistScoreSheetName(scoreSheetName);
		this.mv = new ModelAndView("jsonView");
		if (result) {
			this.mv.addObject(this.SUCCESS, this.TRUE);
		} else {
			this.mv.addObject(this.SUCCESS, this.FALSE);
		}

		return this.mv;
	}

	public ModelAndView getScoreSheetInfo(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) {
		String scoreSheetMasterId = request.getParameter("scoreSheetMasterId");

		try {
			this.mv = new ModelAndView("jsonView");
			scoreSheetMasterVO = this.scoreSheetManager.getScoreSheetInfo(scoreSheetMasterId);
			this.mv.addObject("scoreSheetInfo", scoreSheetMasterVO);
			this.mv.addObject("scoreSheetMasterVO", scoreSheetMasterVO.getCategoryList());
			Iterator i$ = scoreSheetMasterVO.getCategoryList().iterator();

			while (i$.hasNext()) {
				ScoreSheetCategoryVO vo = (ScoreSheetCategoryVO) i$.next();
				this.logger.debug("subcategory size:" + vo.getScoreSheetSubCategory().size());
			}

			this.mv.addObject(this.ACTION, "update");
			return this.mv;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		} catch (Exception var8) {
			return AtlasUtils.getJsonExceptionView(this.logger, var8, response);
		}
	}

	public ModelAndView deleteScoreSheet(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) {
		try {
			this.mv = new ModelAndView("jsonView");
			this.result = this.scoreSheetManager.deleteScoreSheet(scoreSheetMasterVO.getScoreSheetIdList());
			this.logger.info(scoreSheetMasterVO.getScoreSheetName() + " successfully deleted.");
			this.mv.addObject(this.ACTION, this.result);
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}

		return this.mv;
	}
}