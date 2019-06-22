package com.worldcheck.atlas.frontend.sbm.task;

import com.worldcheck.atlas.bl.interfaces.IReviewTaskManagementService;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.retrieverclient.ReportGeneratorService;
import com.worldcheck.atlas.retrieverclient.ReportGeneratorServiceLocator;
import com.worldcheck.atlas.retrieverclient.ReportGeneratorServiceSoap;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.audit.ReviewHistory;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetCategoryVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetSubCatgVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetTemplateVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetVO;
import com.worldcheck.atlas.vo.task.review.ScoreSheetVO;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

public class JsonTaskReviewController extends JSONMultiActionController {
	private final ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.sbm.task.JsonTaskReviewController");
	private IReviewTaskManagementService reviewTaskManagementService;
	private static String RESULT_LIST = "resultList";
	private static String ITEMS_LIST = "itemList";
	private static String ANALYIST_LIST = "analyist_LIST";
	private static String REVIEWER1 = "reviewer1";
	private static String REVIEWER2 = "reviewer2";
	private static String REVIEWER3 = "reviewer3";
	private static String REVIEWERS = "reviewers";
	private static String REPORT_DUE_DATE = "reportDueDate";
	private static String SHEET_ID = "sheet_id";
	private String connectionPath;

	public void setReviewTaskManagementService(IReviewTaskManagementService reviewTaskManagementService) {
		this.reviewTaskManagementService = reviewTaskManagementService;
	}

	public void setConnectionPath(String connectionPath) {
		this.connectionPath = connectionPath;
	}

	public ModelAndView getScoreSheetForReview(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<ScoreSheetVO> reviewScoreSheetList = this.reviewTaskManagementService
					.getReviewScoreSheet(request.getParameter("team_ID"));
			modelAndView.addObject(RESULT_LIST, reviewScoreSheetList);
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		}
	}

	public ModelAndView getReviewScoreSheetTemplate(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<ReviewScoreSheetTemplateVO> reviewScoreSheetList = this.reviewTaskManagementService
					.getReviewScoreSheetTemplate(request.getParameter("scoreSheetId"));
			modelAndView.addObject(RESULT_LIST, reviewScoreSheetList);
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		}
	}

	public ModelAndView getReviewedScoreSheet(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			String crn = request.getParameter("crn").trim();
			HashMap<String, Object> paramMap = new HashMap();
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			limit += start;
			paramMap.put("start", start);
			paramMap.put("limit", limit);
			paramMap.put("crn", crn);
			List<ReviewScoreSheetVO> reviewedScoreSheetList = this.reviewTaskManagementService
					.getReviewedScoreSheet(paramMap);
			modelAndView.addObject(RESULT_LIST, reviewedScoreSheetList);
			modelAndView.addObject("total", this.reviewTaskManagementService.getReviewedScoreSheetCount(paramMap));
			return modelAndView;
		} catch (Exception var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		}
	}

	public ModelAndView getReviewScoreItemList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("Scoressheet Id " + request.getParameter("scoreSheetId"));
			List<ScoreSheetVO> rsSheetFieldMapList = this.reviewTaskManagementService
					.getReviewScoreSheetFieldMap(request.getParameter("scoreSheetId"));
			this.logger.debug("rsSheetFieldMapList :-" + rsSheetFieldMapList);
			modelAndView.addObject("fieldMapList", rsSheetFieldMapList);
			Map<String, String> parameterMap = new HashMap();
			String teamId = request.getParameter("team_ID");
			String crn = request.getParameter("crn");
			parameterMap.put("team_ID", teamId);
			parameterMap.put("crn", crn);
			this.logger.debug("teamId" + teamId);
			this.logger.debug("crn " + crn);
			String[] teamsIds = teamId.split(",");
			List<TeamDetails> teamDetailsList = ResourceLocator.self().getTeamAssignmentService()
					.getCaseTeamDetails(crn);
			List<TeamDetails> supportingTeamList = new ArrayList();
			modelAndView.addObject("teamDetails", teamDetailsList);
			if (teamDetailsList != null && !teamDetailsList.isEmpty()) {
				List<String> reviewerList = new ArrayList();
				List<String> reviewer1List = new ArrayList();
				List<String> reviewer2List = new ArrayList();
				List<String> reviewer3List = new ArrayList();
				Iterator teamDetailsItr = teamDetailsList.iterator();

				while (teamDetailsItr.hasNext()) {
					TeamDetails details = (TeamDetails) teamDetailsItr.next();
					if (details.getReviewer1() != null) {
						reviewerList.add(details.getRev1FullName());
					}

					if (details.getReviewer2() != null) {
						reviewerList.add(details.getRev2FullName());
					}

					if (details.getReviewer3() != null) {
						reviewerList.add(details.getRev3FullName());
					}

					this.logger.debug("details.getTeamId() " + details.getTeamId() + " teamId " + teamId);

					for (int i = 0; i < teamsIds.length; ++i) {
						if (Integer.parseInt(teamsIds[i]) == details.getTeamId()) {
							reviewer1List.add(details.getRev1FullName());
							reviewer2List.add(details.getRev2FullName());
							reviewer3List.add(details.getRev3FullName());
						}
					}

					modelAndView.addObject(REPORT_DUE_DATE, details.getFinalDueDate());
					this.logger.debug("details.getTeamType() " + details.getTeamType());
					if ("Primary".equals(details.getTeamType())) {
						modelAndView.addObject("Primary", details.getOfficeName());
					} else {
						supportingTeamList.add(details);
					}
				}

				modelAndView.addObject(REVIEWERS, reviewerList);
				modelAndView.addObject(REVIEWER1, reviewer1List);
				modelAndView.addObject(REVIEWER2, reviewer2List);
				modelAndView.addObject(REVIEWER3, reviewer3List);
			}

			Iterator srItr = rsSheetFieldMapList.iterator();

			while (srItr.hasNext()) {
				ScoreSheetVO scoreSheetVO = (ScoreSheetVO) srItr.next();
				String fieldName = scoreSheetVO.getFieldName();
				if (fieldName.equals("Supporting Team 1") || fieldName.equals("Supporting Team 2")
						|| fieldName.equals("Supporting Team 3") || fieldName.equals("Supporting Team 4")
						|| fieldName.equals("Supporting Team 5") || fieldName.equals("Supporting Team 6")) {
					int supportingTeamLength = supportingTeamList.size();
					this.logger.debug("supportingTeamLength " + supportingTeamLength);
					int count = 1;

					for (int i = 0; i < supportingTeamLength && i < 6; ++i) {
						TeamDetails teamDetails = (TeamDetails) supportingTeamList.get(i);
						this.logger.debug("Supporting Team " + teamDetails.getOfficeName());
						modelAndView.addObject("supportingTeam" + count++, teamDetails.getOfficeName());
					}
				}

				if (fieldName.equals("Analyst")) {
					modelAndView.addObject(ANALYIST_LIST, this.reviewTaskManagementService.getAnalyst(parameterMap));
				}

				if (fieldName.equals("Editor")) {
					modelAndView.addObject("Editor",
							this.reviewTaskManagementService.getEditiors(request.getParameter("scoreSheetId")));
				}
			}

			return modelAndView;
		} catch (Exception var18) {
			return AtlasUtils.getJsonExceptionView(this.logger, var18, response);
		}
	}

	public ModelAndView saveReviewScoreSheet(HttpServletRequest request, HttpServletResponse response,
			ReviewScoreSheetVO reviewScoreSheetVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			HttpSession session = request.getSession();
			UserDetailsBean userDetailsBean = (UserDetailsBean) session.getAttribute("userDetailsBean");
			String reviewerName = "";
			if (session.getAttribute("loginLevel") != null) {
				if (session.getAttribute("performedBy") != null) {
					reviewerName = (String) session.getAttribute("performedBy");
				} else {
					reviewerName = userDetailsBean.getLoginUserId();
				}
			} else {
				reviewerName = userDetailsBean.getLoginUserId();
			}

			JSONObject jsonObject = null;
			String scoreSheetdata = request.getParameter("rwScoresheetGrid");
			JSONArray jsonArray = new JSONArray(scoreSheetdata);
			int scoreSheetdataLength = jsonArray.length();
			String[] category = new String[scoreSheetdataLength];
			String[] categoryId = new String[scoreSheetdataLength];
			String[] sub_category = new String[scoreSheetdataLength];
			String[] sub_categoryId = new String[scoreSheetdataLength];
			String[] scores = new String[scoreSheetdataLength];
			String[] cnt_of_errors = new String[scoreSheetdataLength];
			String[] points = new String[scoreSheetdataLength];
			String[] comments = new String[scoreSheetdataLength];
			String[] applicablity = new String[scoreSheetdataLength];
			String[] select = new String[scoreSheetdataLength];

			for (int i = 0; i < scoreSheetdataLength; ++i) {
				jsonObject = jsonArray.getJSONObject(i);
				if (jsonObject.get("category").toString() != null
						&& jsonObject.get("category").toString().trim().length() > 0) {
					category[i] = jsonObject.get("category").toString();
				}

				if (jsonObject.get("categoryId").toString() != null
						&& jsonObject.get("categoryId").toString().trim().length() > 0) {
					categoryId[i] = jsonObject.get("categoryId").toString();
				}

				if (jsonObject.get("sub_category").toString() != null
						&& jsonObject.get("sub_category").toString().trim().length() > 0) {
					sub_category[i] = jsonObject.get("sub_category").toString();
				}

				if (jsonObject.get("sub_categoryId").toString() != null
						&& jsonObject.get("sub_categoryId").toString().trim().length() > 0) {
					sub_categoryId[i] = jsonObject.get("sub_categoryId").toString();
				}

				if (jsonObject.get("cnt_of_errors").toString() != null
						&& jsonObject.get("cnt_of_errors").toString().trim().length() > 0) {
					cnt_of_errors[i] = jsonObject.get("cnt_of_errors").toString();
				}

				if (jsonObject.get("scores").toString() != null
						&& jsonObject.get("scores").toString().trim().length() > 0) {
					scores[i] = jsonObject.get("scores").toString();
				}

				if (jsonObject.get("comments").toString() != null) {
					comments[i] = jsonObject.get("comments").toString();
				}

				if (jsonObject.get("points").toString() != null
						&& jsonObject.get("points").toString().trim().length() > 0) {
					points[i] = jsonObject.get("points").toString();
				}

				if (jsonObject.get("applicablity").toString() != null) {
					applicablity[i] = jsonObject.get("applicablity").toString();
				}

				if (jsonObject.get("select").toString() != null) {
					select[i] = jsonObject.get("select").toString();
				}
			}

			if (category.length > 0) {
				;
			}

			reviewScoreSheetVO.setCategory(category);
			if (categoryId.length > 0) {
				reviewScoreSheetVO.setCategoryId(categoryId);
			}

			if (sub_category.length > 0) {
				reviewScoreSheetVO.setSub_category(sub_category);
			}

			if (sub_categoryId.length > 0) {
				reviewScoreSheetVO.setSub_categoryId(sub_categoryId);
			}

			if (scores.length > 0) {
				reviewScoreSheetVO.setScores(scores);
			}

			if (cnt_of_errors.length > 0) {
				reviewScoreSheetVO.setCnt_of_errors(cnt_of_errors);
			}

			if (applicablity.length > 0) {
				reviewScoreSheetVO.setApplicablity(applicablity);
			}

			if (select.length > 0) {
				reviewScoreSheetVO.setSelect(select);
			}

			if (comments.length > 0) {
				reviewScoreSheetVO.setComments(comments);
			}

			if (points.length > 0) {
				reviewScoreSheetVO.setPoints(points);
			}

			reviewScoreSheetVO.setUpdated_by(userDetailsBean.getLoginUserId());
			reviewScoreSheetVO.setReviewer(reviewerName);
			String review_sheetID = this.reviewTaskManagementService.insertReviewScoreSheetDetails(reviewScoreSheetVO)
					+ "".trim();
			reviewScoreSheetVO.setSheet_id(review_sheetID);
			this.reviewTaskManagementService.insertReviewScoreSheetItems(reviewScoreSheetVO);
			String[] categories = reviewScoreSheetVO.getCategory();
			String[] subCategories = reviewScoreSheetVO.getSub_category();
			String tempCategory = "";
			Map<String, Integer> categoryMAP = new HashMap();

			int i;
			for (i = 0; i < categories.length; ++i) {
				ReviewScoreSheetCategoryVO categoryVO = new ReviewScoreSheetCategoryVO();
				if (!tempCategory.equals(categories[i])) {
					tempCategory = categories[i];
					categoryVO.setCategory(categories[i]);
					categoryVO.setSs_category_id(reviewScoreSheetVO.getCategoryId()[i]);
					categoryVO.setComments(reviewScoreSheetVO.getComments()[i]);
					categoryVO.setUpdated_by(reviewScoreSheetVO.getUpdated_by());
					categoryVO.setReview_sheet_id(review_sheetID);
					categoryMAP.put(categories[i], this.reviewTaskManagementService.insertCategoryDetails(categoryVO));
				}
			}

			for (i = 0; i < subCategories.length; ++i) {
				ReviewScoreSheetSubCatgVO subCategoryVO = new ReviewScoreSheetSubCatgVO();
				subCategoryVO.setSub_category(subCategories[i]);
				subCategoryVO.setApplicability(reviewScoreSheetVO.getApplicablity()[i].trim());
				subCategoryVO.setCnt_of_errors(reviewScoreSheetVO.getCnt_of_errors()[i].trim());
				subCategoryVO.setPoints(reviewScoreSheetVO.getPoints()[i].trim());
				subCategoryVO
						.setReview_ss_category_id(categoryMAP.get(reviewScoreSheetVO.getCategory()[i]) + "".trim());
				subCategoryVO.setScore(reviewScoreSheetVO.getScores()[i].trim());
				subCategoryVO.setSelect(reviewScoreSheetVO.getSelect()[i].trim());
				subCategoryVO.setSs_sub_category_id(reviewScoreSheetVO.getSub_categoryId()[i].trim());
				subCategoryVO.setUpdated_by(reviewScoreSheetVO.getUpdated_by().trim());
				this.reviewTaskManagementService.insertSubCategoryDetails(subCategoryVO);
			}

			this.saveCaseHistory("Added", request);
			return modelAndView;
		} catch (Exception var29) {
			return AtlasUtils.getJsonExceptionView(this.logger, var29, response);
		}
	}

	public ModelAndView isAnalystReviewedScoresheet(HttpServletRequest request, HttpServletResponse response,
			ReviewScoreSheetVO reviewScoreSheetVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			Map<String, String> parameterMap = new HashMap();
			this.logger.debug("Test" + reviewScoreSheetVO.getScoresheet_name() + "," + reviewScoreSheetVO.getCrn() + ","
					+ reviewScoreSheetVO.getAnalyst());
			parameterMap.put("ScoreSheetName", reviewScoreSheetVO.getScoresheet_name().trim());
			parameterMap.put("crn", reviewScoreSheetVO.getCrn().trim());
			parameterMap.put("Analyst", reviewScoreSheetVO.getAnalyst().trim());
			boolean isReviewAllow = this.reviewTaskManagementService.isAnalystReviewedScoresheet(parameterMap);
			modelAndView.addObject("reviewStatus", isReviewAllow ? "No" : "Yes");
			return modelAndView;
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView removedReviewedScoresheet(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("Sheet Ids to remove" + request.getParameter(SHEET_ID));
			this.reviewTaskManagementService.removedReviewedSSDetails(request.getParameter(SHEET_ID));
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		}
	}

	public ModelAndView saveReviewComment(HttpServletRequest request, HttpServletResponse response,
			ReviewHistory reviewHistory) {
		reviewHistory.setReviewStatus("Approved");
		UserDetailsBean detailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
		if (request.getSession().getAttribute("loginLevel") != null) {
			reviewHistory.setCommentFrom((String) request.getSession().getAttribute("performedBy"));
		} else {
			reviewHistory.setCommentFrom(detailsBean.getLoginUserId());
		}

		reviewHistory.setUpdatedBy(detailsBean.getLoginUserId());
		reviewHistory.setCRN(request.getParameter("crn"));
		reviewHistory.setTaskName(request.getParameter("workItemName"));
		this.logger.debug("reviewHistory " + reviewHistory);
		this.logger.debug("CommentFrom " + reviewHistory.getCommentFrom());
		this.logger.debug("CRN " + reviewHistory.getCRN());
		this.logger.debug("ReviewComment " + reviewHistory.getReviewComment());
		this.logger.debug("CommentFrom " + reviewHistory.getReviewStatus());
		this.logger.debug("task Name " + request.getParameter("workItemName"));
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<ReviewHistory> reviewHistoryList = new ArrayList();
			reviewHistoryList.add(reviewHistory);
			ResourceLocator.self().getReviewHistoryService().setReviewHistory(reviewHistoryList);
			return modelAndView;
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getReviewedSSCategoryDetails(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<ReviewScoreSheetTemplateVO> reviewScoreSheetCatList = this.reviewTaskManagementService
					.getReviewedSSCategoryDetails(request.getParameter(SHEET_ID));
			modelAndView.addObject(RESULT_LIST, reviewScoreSheetCatList);
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		}
	}

	public ModelAndView getReviewedSSItemDetails(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<ReviewScoreSheetVO> reviewScoreSheetItemList = this.reviewTaskManagementService
					.getReviewedSSItemsDetails(request.getParameter(SHEET_ID));
			modelAndView.addObject(ITEMS_LIST, reviewScoreSheetItemList);
			return modelAndView;
		} catch (Exception var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		}
	}

	public ModelAndView updateReviewSSCategoryDetail(HttpServletRequest request, HttpServletResponse response,
			ReviewScoreSheetVO reviewScoreSheetVO) {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			JSONObject jsonObject = null;
			String scoreSheetdata = request.getParameter("rwScoresheetGrid");
			JSONArray jsonArray = new JSONArray(scoreSheetdata);
			int scoreSheetdataLength = jsonArray.length();
			String[] category = new String[scoreSheetdataLength];
			String[] categoryId = new String[scoreSheetdataLength];
			String[] sub_category = new String[scoreSheetdataLength];
			String[] sub_categoryId = new String[scoreSheetdataLength];
			String[] scores = new String[scoreSheetdataLength];
			String[] cnt_of_errors = new String[scoreSheetdataLength];
			String[] points = new String[scoreSheetdataLength];
			String[] comments = new String[scoreSheetdataLength];
			String[] applicablity = new String[scoreSheetdataLength];
			String[] select = new String[scoreSheetdataLength];

			for (int i = 0; i < scoreSheetdataLength; ++i) {
				jsonObject = jsonArray.getJSONObject(i);
				if (jsonObject.get("category").toString() != null
						&& jsonObject.get("category").toString().trim().length() > 0) {
					category[i] = jsonObject.get("category").toString();
				}

				if (jsonObject.get("categoryId").toString() != null
						&& jsonObject.get("categoryId").toString().trim().length() > 0) {
					categoryId[i] = jsonObject.get("categoryId").toString();
				}

				if (jsonObject.get("sub_category").toString() != null
						&& jsonObject.get("sub_category").toString().trim().length() > 0) {
					sub_category[i] = jsonObject.get("sub_category").toString();
				}

				if (jsonObject.get("sub_categoryId").toString() != null
						&& jsonObject.get("sub_categoryId").toString().trim().length() > 0) {
					sub_categoryId[i] = jsonObject.get("sub_categoryId").toString();
				}

				if (jsonObject.get("cnt_of_errors").toString() != null
						&& jsonObject.get("cnt_of_errors").toString().trim().length() > 0) {
					cnt_of_errors[i] = jsonObject.get("cnt_of_errors").toString();
				}

				if (jsonObject.get("scores").toString() != null) {
					scores[i] = jsonObject.get("scores").toString();
				}

				if (jsonObject.get("comments").toString() != null) {
					comments[i] = jsonObject.get("comments").toString();
				}

				if (jsonObject.get("points").toString() != null
						&& jsonObject.get("points").toString().trim().length() > 0) {
					points[i] = jsonObject.get("points").toString();
				}

				if (jsonObject.get("applicablity").toString() != null) {
					applicablity[i] = jsonObject.get("applicablity").toString();
				}

				if (jsonObject.get("select").toString() != null) {
					select[i] = jsonObject.get("select").toString();
				}
			}

			if (category.length > 0) {
				;
			}

			reviewScoreSheetVO.setCategory(category);
			if (categoryId.length > 0) {
				reviewScoreSheetVO.setCategoryId(categoryId);
			}

			if (sub_category.length > 0) {
				reviewScoreSheetVO.setSub_category(sub_category);
			}

			if (sub_categoryId.length > 0) {
				reviewScoreSheetVO.setSub_categoryId(sub_categoryId);
			}

			if (scores.length > 0) {
				reviewScoreSheetVO.setScores(scores);
			}

			if (cnt_of_errors.length > 0) {
				reviewScoreSheetVO.setCnt_of_errors(cnt_of_errors);
			}

			if (applicablity.length > 0) {
				reviewScoreSheetVO.setApplicablity(applicablity);
			}

			if (select.length > 0) {
				reviewScoreSheetVO.setSelect(select);
			}

			if (comments.length > 0) {
				reviewScoreSheetVO.setComments(comments);
			}

			if (points.length > 0) {
				reviewScoreSheetVO.setPoints(points);
			}

			this.logger.debug("Categories :" + reviewScoreSheetVO.getCategory().length);
			this.logger.debug("Comments:" + reviewScoreSheetVO.getComments().length);
			UserDetailsBean userDetailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			reviewScoreSheetVO.setUpdated_by(userDetailsBean.getLoginUserId());
			this.reviewTaskManagementService.updateReviewSSDetails(reviewScoreSheetVO);
			String[] categories = reviewScoreSheetVO.getCategory();
			String[] subCategories = reviewScoreSheetVO.getSub_category();
			String tempCategory = "";

			int i;
			for (i = 0; i < categories.length; ++i) {
				ReviewScoreSheetCategoryVO categoryVO = new ReviewScoreSheetCategoryVO();
				if (!tempCategory.equals(categories[i])) {
					tempCategory = categories[i];
					categoryVO.setReview_ss_categ_id(Integer.parseInt(reviewScoreSheetVO.getCategoryId()[i].trim()));
					categoryVO.setComments(reviewScoreSheetVO.getComments()[i]);
					categoryVO.setUpdated_by(reviewScoreSheetVO.getUpdated_by());
					this.reviewTaskManagementService.updateCategoryDetails(categoryVO);
				}
			}

			for (i = 0; i < subCategories.length; ++i) {
				ReviewScoreSheetSubCatgVO subCategoryVO = new ReviewScoreSheetSubCatgVO();
				subCategoryVO.setApplicability(reviewScoreSheetVO.getApplicablity()[i].trim());
				subCategoryVO.setCnt_of_errors(reviewScoreSheetVO.getCnt_of_errors()[i].trim());
				subCategoryVO.setPoints(reviewScoreSheetVO.getPoints()[i].trim());
				subCategoryVO.setScore(reviewScoreSheetVO.getScores()[i].trim());
				subCategoryVO.setSelect(reviewScoreSheetVO.getSelect()[i].trim());
				subCategoryVO.setUpdated_by(reviewScoreSheetVO.getUpdated_by().trim());
				subCategoryVO
						.setReview_ss_subcateg_id(Integer.parseInt(reviewScoreSheetVO.getSub_categoryId()[i].trim()));
				this.reviewTaskManagementService.updateSubCategoryDetails(subCategoryVO);
			}

			this.saveCaseHistory("Updated", request);
			return modelAndView;
		} catch (Exception var25) {
			return AtlasUtils.getJsonExceptionView(this.logger, var25, response);
		}
	}

	private void saveCaseHistory(String action, HttpServletRequest request) throws CMSException {
		try {
			this.logger.debug("I am in saveCaseHistory");
			CaseHistory caseHistoryOtherParam = new CaseHistory();
			Long pid = 0L;
			UserDetailsBean detailsBean = (UserDetailsBean) request.getSession().getAttribute("userDetailsBean");
			String crn = request.getParameter("crn");
			caseHistoryOtherParam.setCRN(crn);
			caseHistoryOtherParam.setProcessCycle("");
			if (crn != null && !crn.isEmpty()) {
				pid = ResourceLocator.self().getOfficeAssignmentService().getPIDForCRN(crn);
			}

			caseHistoryOtherParam.setPid("" + pid);
			caseHistoryOtherParam.setTaskName("");
			caseHistoryOtherParam.setTaskStatus("");
			if (request.getSession().getAttribute("loginLevel") != null) {
				caseHistoryOtherParam.setPerformer((String) request.getSession().getAttribute("performedBy"));
			} else {
				caseHistoryOtherParam.setPerformer(detailsBean.getLoginUserId());
			}

			this.logger.debug("CRN:" + caseHistoryOtherParam.getCRN());
			this.logger.debug("processCycle:" + caseHistoryOtherParam.getProcessCycle());
			this.logger.debug("PID:" + caseHistoryOtherParam.getPid());
			this.logger.debug("Performer:" + caseHistoryOtherParam.getPerformer());
			ResourceLocator.self().getCaseHistoryService().setCaseHistoryForReviewScoresheet(action,
					caseHistoryOtherParam);
		} catch (CMSException var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	private void saveCaseHistoryForTaskApproved(HttpServletRequest request, String performer) throws CMSException {
		try {
			this.logger.debug("In saveCaseHistoryForTaskApproved");
			CaseHistory caseHistory = new CaseHistory();
			String crn = request.getParameter("crn");
			caseHistory.setCRN(crn);
			caseHistory.setProcessCycle("");
			caseHistory.setPid(request.getParameter("PID").trim());
			caseHistory.setTaskName(request.getParameter("workItemName"));
			caseHistory.setTaskStatus("Approved");
			caseHistory.setPerformer(performer);
			this.logger.debug("CRN:" + caseHistory.getCRN());
			this.logger.debug("processCycle:" + caseHistory.getProcessCycle());
			this.logger.debug("PID:" + caseHistory.getPid());
			this.logger.debug("Performer:" + caseHistory.getPerformer());
			ResourceLocator.self().getCaseHistoryService().setCaseHistoryForTaskApproved(caseHistory);
		} catch (CMSException var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public ModelAndView getLBRTableReport(HttpServletRequest request, HttpServletResponse response)
			throws CMSException {
		try {
			String subjectType = request.getParameter("subjectType");
			String countryIds = request.getParameter("countryIds");
			this.logger.debug("subjectType " + subjectType);
			this.logger.debug("countryIds " + countryIds);
			this.logger.debug("countryIds " + StringUtils.commaSeparatedStringToIntList(countryIds));
			ReportGeneratorService service = new ReportGeneratorServiceLocator();
			ReportGeneratorServiceSoap port = service.getReportGeneratorServiceSoap();
			List<Integer> countryIdList = StringUtils.commaSeparatedStringToIntList(countryIds);
			int[] countryIdArray = new int[countryIdList.size()];

			for (int i = 0; i < countryIdList.size(); ++i) {
				countryIdArray[i] = (Integer) countryIdList.get(i);
			}

			this.logger.debug("countryIdArray " + countryIdArray);
			String path = port.generateReportForAtlas(countryIdArray, subjectType, this.getRandomFileName(), "DOC");
			String fileName = path.substring(path.lastIndexOf("\\") + 1);
			this.logger.debug("connectionPath" + this.connectionPath);
			path = this.connectionPath + fileName.replaceAll("\\s", "%20");
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream is;
			ServletOutputStream os;
			byte[] bytes;
			int read;
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				response.setContentType("application/ms-word");
				response.addHeader("Content-Disposition", "attachment; filename=\"LBR Table Data.doc\"");
				os = response.getOutputStream();
				bytes = new byte[1024];

				while ((read = is.read(bytes)) != -1) {
					os.write(bytes, 0, read);
				}
			} else {
				is = conn.getErrorStream();
				os = response.getOutputStream();
				os.write("<html><body>".getBytes());
				bytes = new byte[1024];

				while ((read = is.read(bytes)) != -1) {
					os.write(bytes, 0, read);
				}

				os.write("</body></html>".getBytes());
			}

			return null;
		} catch (Exception var17) {
			throw new CMSException(this.logger, var17);
		}
	}

	private String getRandomFileName() {
		return (new BigInteger(130, new SecureRandom())).toString(32).toString();
	}
}