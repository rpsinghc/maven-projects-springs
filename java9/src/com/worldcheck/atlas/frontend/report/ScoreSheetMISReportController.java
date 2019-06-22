package com.worldcheck.atlas.frontend.report;

import com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader;
import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ReportConstants;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.ScoreSheetMasterVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetCategoryVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetSubCatgVO;
import com.worldcheck.atlas.vo.task.review.ReviewScoreSheetVO;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class ScoreSheetMISReportController extends MultiActionController {
	private static final String SCORE_SHEET_MIS_REPORT = "scoreSheetMisReport";
	private static final String DD_MM_YYYY = "dd-MM-yyyy";
	private static final String SHEET_ID = "SHEET_ID";
	private static final String QUALITY_PERCENT = "QUALITY_PERCENT";
	private static final String SCORE = "SCORE";
	private static final String COMMENT = "- Comment";
	private static final String QUALITY = "QUALITY";
	private static final String SCORED = "SCORED";
	private static final String TOTAL_POINT = "Total Point";
	private static final String RATING = "RATING";
	private static final String REVIEW_TIME = "REVIEW TIME";
	private static final String COMPLEXITY = "COMPLEXITY";
	private static final String ANALYST = "ANALYST";
	private static final String CRN = "CRN";
	private static final String YEAR_VAL = "yearVal";
	private static final String MONTH_VAL = "monthVal";
	private static final String REDIRECT_REDIRECT_SCORE_SHEET_MIS_REPORT_DO = "redirect:redirectScoreSheetMISReport.do";
	private static final String HSCORE_SHEET_NAME_VAL = "hscoreSheetNameVal";
	private static final String NO_DATA = "NoData";
	private static final String SUBMIT_TYPE = "submitType";
	private static final String DATA_FOUND = "dataFound";
	private static final String REVIEWER = "REVIEWER";
	private static final String DATE_OF_REVIEW = "DATE_OF_REVIEW";
	private static final String JSP_NAME = "analystScoringPerf_custom";
	private static final String CURRENT_MONTH = "currentMonth";
	private static final String CURRENT_YEAR = "currentYear";
	private static final String MONTH_MAP = "monthMap";
	private static final String YEAR_LIST = "yearList";
	private static final String REPORT_TYPES = "reportTypes";
	private static final String COLUMN_COUNT = "columnCount";
	private static final String ROW_COUNT = "rowCount";
	private static final String OFFICE_VAL = "officeVal";
	private static final String DATA_LIST = "dataList";
	private static final String HEADER_LIST = "headerList";
	private static final String TOTAL_POINTS = "TOTAL_POINT";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.report.ScoreSheetMISReportController");
	private TabularReportDAO tabularReportDAO;
	private PropertyReaderUtil propertyReader;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public ModelAndView redirectScoreSheetMISReport(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) throws Exception {
		ModelAndView modelAndView = null;
		this.logger.debug("in ScoreSheetMISReportController");

		try {
			List<String> reportTypelist = StringUtils
					.commaSeparatedStringToList(this.propertyReader.getVendorExpenditureReportTypes());
			List<String> yearList = StringUtils.commaSeparatedStringToList(this.propertyReader.getYearList());
			int currentYear = Calendar.getInstance().get(1);
			int currentMonth = Calendar.getInstance().get(2) + 1;
			this.logger.debug(
					"reportTypelist size :: " + reportTypelist.size() + " :: yearList size :: " + yearList.size());
			modelAndView = new ModelAndView("analystScoringPerf_custom");
			modelAndView.addObject("reportTypes", reportTypelist);
			modelAndView.addObject("yearList", yearList);
			modelAndView.addObject("monthMap", ReportConstants.monthMap);
			modelAndView.addObject("currentYear", currentYear);
			modelAndView.addObject("currentMonth", currentMonth);
			this.logger.debug("Action:" + request.getSession().getAttribute("action") + " :: office ID :: "
					+ request.getSession().getAttribute("officeVal"));
			if (request.getSession().getAttribute("action") != null) {
				if (request.getSession().getAttribute("action").equals("dataFound")) {
					this.logger.debug("submitType:true");
					modelAndView.addObject("submitType", "true");
				}

				if (request.getSession().getAttribute("isZip") != null) {
					modelAndView.addObject("isZip", "true");
					request.getSession().removeAttribute("isZip");
					modelAndView.addObject("currentYear", request.getSession().getAttribute("currentYear"));
					modelAndView.addObject("currentMonth", request.getSession().getAttribute("currentMonth"));
					modelAndView.addObject("hscoreSheetNameVal",
							request.getSession().getAttribute("hscoreSheetNameVal"));
					modelAndView.addObject("officeVal", request.getSession().getAttribute("officeVal"));
					request.getSession().removeAttribute("currentYear");
					request.getSession().removeAttribute("currentMonth");
					request.getSession().removeAttribute("hscoreSheetNameVal");
					request.getSession().removeAttribute("officeVal");
				}

				if (request.getSession().getAttribute("action").equals("NoData")) {
					this.logger.debug("submitType:false");
					modelAndView.addObject("submitType", "false");
					modelAndView.addObject("currentYear", request.getSession().getAttribute("currentYear"));
					modelAndView.addObject("currentMonth", request.getSession().getAttribute("currentMonth"));
					modelAndView.addObject("hscoreSheetNameVal",
							request.getSession().getAttribute("hscoreSheetNameVal"));
					modelAndView.addObject("officeVal", request.getSession().getAttribute("officeVal"));
					request.getSession().removeAttribute("currentYear");
					request.getSession().removeAttribute("currentMonth");
					request.getSession().removeAttribute("hscoreSheetNameVal");
					request.getSession().removeAttribute("officeVal");
				}

				this.logger.debug("remove action from session");
				request.getSession().removeAttribute("action");
			}

			this.logger.debug("exiting ScoreSheetMISReportController");
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getExceptionView(this.logger, var9);
		} catch (Exception var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		}
	}

	public ModelAndView downloadScoreSheetMISReport(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) throws Exception {
		ModelAndView modelAndView = null;
		String scoreSheetName = null;
		List<HashMap> reviewFieldScoreSheetVOList = null;
		List<ReviewScoreSheetVO> reviewDataScoreSheetData = null;
		this.logger.debug(" in ScoreSheetMISReportController  downloadScoreSheetMISReport :: scoresheetName:: "
				+ request.getParameter("hscoreSheetNameVal"));
		request.getSession().setAttribute("excelGeneration", new Date());

		ModelAndView var17;
		try {
			this.logger.debug("reportTypes :: " + this.propertyReader);
			modelAndView = new ModelAndView("redirect:redirectScoreSheetMISReport.do");
			HttpSession session = request.getSession();
			if (request.getParameter("hscoreSheetNameVal") != null) {
				scoreSheetName = request.getParameter("hscoreSheetNameVal");
				String month = request.getParameter("monthVal");
				String year = request.getParameter("yearVal");
				String officeName = request.getParameter("officeVal");
				this.logger.debug(" Month :: " + month + " year : " + year + " :: officeName :: " + officeName);
				reviewFieldScoreSheetVOList = this.tabularReportDAO.getReviewFieldScoreSheetMISReport(scoreSheetName,
						month, year);
				reviewDataScoreSheetData = this.tabularReportDAO.getCat_SubCat_ScoreSheetMISReport(scoreSheetName,
						month, year);
				if (reviewFieldScoreSheetVOList.size() != 0 && reviewDataScoreSheetData.size() != 0) {
					Map<String, Object> countMap = this.getResultCount(reviewFieldScoreSheetVOList,
							reviewDataScoreSheetData);
					int rowsInResultset = (Integer) countMap.get("rowCount");
					int cellsInResultset = (Integer) countMap.get("columnCount") * rowsInResultset;
					session.setAttribute("action", "dataFound");
					if (cellsInResultset > 100000) {
						this.logger.debug("export as ZIP");
						request.getSession().setAttribute("isZip", "true");
						request.getSession().setAttribute("currentYear", request.getParameter("yearVal"));
						request.getSession().setAttribute("currentMonth", request.getParameter("monthVal"));
						request.getSession().setAttribute("hscoreSheetNameVal",
								request.getParameter("hscoreSheetNameVal"));
						request.getSession().setAttribute("officeVal", request.getParameter("officeVal"));
					} else {
						String fileName = this.writeToExcel(reviewFieldScoreSheetVOList, reviewDataScoreSheetData,
								response);
						modelAndView = new ModelAndView("misExcelDownloadPopup");
						modelAndView.addObject("fileName", fileName);
					}

					return modelAndView;
				} else {
					session.setAttribute("action", "NoData");
					request.getSession().setAttribute("currentYear", request.getParameter("yearVal"));
					request.getSession().setAttribute("currentMonth", request.getParameter("monthVal"));
					request.getSession().setAttribute("hscoreSheetNameVal", request.getParameter("hscoreSheetNameVal"));
					request.getSession().setAttribute("officeVal", request.getParameter("officeVal"));
					return modelAndView;
				}
			}

			return modelAndView;
		} catch (CMSException var21) {
			var17 = AtlasUtils.getExceptionView(this.logger, var21);
		} catch (Exception var22) {
			var17 = AtlasUtils.getExceptionView(this.logger, var22);
			return var17;
		} finally {
			if (request.getSession().getAttribute("excelGeneration") != null) {
				request.getSession().removeAttribute("excelGeneration");
			}

		}

		return var17;
	}

	public ModelAndView generateExcelZipForSSMIS(HttpServletRequest request, HttpServletResponse response,
			ScoreSheetMasterVO scoreSheetMasterVO) throws Exception {
		ModelAndView modelAndView = null;
		String scoreSheetName = null;
		List<HashMap> reviewFieldScoreSheetVOList = null;
		List<ReviewScoreSheetVO> reviewDataScoreSheetData = null;
		this.logger.debug("in ScoreSheetMISReportController  generateExcelZipForSSMIS :: scoresheetName :: "
				+ request.getParameter("hscoreSheetNameVal"));
		request.getSession().setAttribute("excelGeneration", new Date());

		ModelAndView var21;
		try {
			this.logger.debug("reportTypes :: " + this.propertyReader);
			List<List> partedList = new ArrayList();
			if (request.getParameter("hscoreSheetNameVal") != null) {
				scoreSheetName = request.getParameter("hscoreSheetNameVal");
				String month = request.getParameter("monthVal");
				String year = request.getParameter("yearVal");
				String officeName = request.getParameter("officeVal");
				this.logger.debug(" Month :: " + month + " :: year :: " + year + " :: officeName :: " + officeName);
				reviewFieldScoreSheetVOList = this.tabularReportDAO.getReviewFieldScoreSheetMISReport(scoreSheetName,
						month, year);
				reviewDataScoreSheetData = this.tabularReportDAO.getCat_SubCat_ScoreSheetMISReport(scoreSheetName,
						month, year);
				Map<String, Object> countMap = this.getResultCount(reviewFieldScoreSheetVOList,
						reviewDataScoreSheetData);
				int columnCount = (Integer) countMap.get("columnCount");
				List<List<String>> dataList = (List) countMap.get("dataList");
				List<String> headerList = (List) countMap.get("headerList");
				int rowsAccomodated = 100000 / columnCount;
				int N = (Integer) countMap.get("rowCount");
				int L = rowsAccomodated - 1;
				this.logger.debug("rowsAccomodated :: " + rowsAccomodated + " :: N : : " + N + " :: L :: " + L);

				for (int i = 0; i < N; i += L) {
					partedList.add(partedList.size(), dataList.subList(i, Math.min(N, i + L)));
				}

				this.logger.debug("partedList size ::  " + partedList.size());
				modelAndView = new ModelAndView("excelDownloadPopupZip");
				String zipFileName = this.writeToMultipleExcel(partedList, headerList, response);
				modelAndView.addObject("fileName", zipFileName);
			}

			return modelAndView;
		} catch (CMSException var25) {
			var21 = AtlasUtils.getExceptionView(this.logger, var25);
			return var21;
		} catch (Exception var26) {
			var21 = AtlasUtils.getExceptionView(this.logger, var26);
		} finally {
			if (request.getSession().getAttribute("excelGeneration") != null) {
				request.getSession().removeAttribute("excelGeneration");
			}

		}

		return var21;
	}

	public String writeToMultipleExcel(List<List> partedList, List<String> headerList, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("in  writeToMultipleExcel ");
		new ArrayList();
		String zipFileName = "";

		try {
			List<String> fileNamesList = new ArrayList();

			for (int i = 0; i < partedList.size(); ++i) {
				String partName = "Part" + (i + 1);
				new ArrayList();
				String fileName = ExcelDownloader.writeToExcel1(headerList, (List) partedList.get(i),
						"scoreSheetMisReport" + partName, (short) 0, (short) 1, response,
						"scoreSheetMisReport" + partName);
				fileNamesList.add(fileName);
			}

			zipFileName = ExcelDownloader.generateZipFile(fileNamesList, "scoreSheetMisReport", response);
			return zipFileName;
		} catch (UnsupportedOperationException var10) {
			throw new CMSException(this.logger, var10);
		} catch (ClassCastException var11) {
			throw new CMSException(this.logger, var11);
		} catch (NullPointerException var12) {
			throw new CMSException(this.logger, var12);
		} catch (IllegalArgumentException var13) {
			throw new CMSException(this.logger, var13);
		}
	}

	private String writeToExcel(List<HashMap> scoreSheetReviewMapList, List<ReviewScoreSheetVO> scoreSheetDataList,
			HttpServletResponse response) throws CMSException {
		try {
			this.logger.debug("inside the ScoresheetMisReport >> writeToExcel");
			List<String> lstHeader = new ArrayList();
			lstHeader.add("CRN");
			lstHeader.add("DATE_OF_REVIEW");
			lstHeader.add("ANALYST");
			lstHeader.add("REVIEWER");
			lstHeader.add("COMPLEXITY");
			lstHeader.add("REVIEW TIME");
			lstHeader.add("RATING");
			lstHeader.add("Total Point");
			lstHeader.add("SCORED");
			lstHeader.add("QUALITY");
			List<ReviewScoreSheetCategoryVO> categoryList = ((ReviewScoreSheetVO) scoreSheetDataList.get(0))
					.getReviewCategory();
			Iterator var7 = categoryList.iterator();

			while (var7.hasNext()) {
				ReviewScoreSheetCategoryVO categoryVO = (ReviewScoreSheetCategoryVO) var7.next();
				List<ReviewScoreSheetSubCatgVO> subCategory = categoryVO.getScoreSheetSubCategory();

				ReviewScoreSheetSubCatgVO subCategoryVO;
				List var11;
				for (Iterator var10 = subCategory.iterator(); var10
						.hasNext(); var11 = subCategoryVO.getGradingPoint()) {
					subCategoryVO = (ReviewScoreSheetSubCatgVO) var10.next();
					lstHeader.add(categoryVO.getCategory() + "-" + subCategoryVO.getSub_category());
				}

				lstHeader.add(categoryVO.getCategory() + "- Comment");
			}

			var7 = scoreSheetReviewMapList.iterator();
			if (var7.hasNext()) {
				HashMap hashMap = (HashMap) var7.next();
				Set keys = hashMap.keySet();
				Iterator iterator = keys.iterator();

				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if (!key.equalsIgnoreCase("CRN") && !key.equalsIgnoreCase("DATE_OF_REVIEW")
							&& !key.equalsIgnoreCase("ANALYST") && !key.equalsIgnoreCase("REVIEWER")
							&& !key.equalsIgnoreCase("COMPLEXITY") && !key.equalsIgnoreCase("REVIEW TIME")
							&& !key.equalsIgnoreCase("SCORE") && !key.equalsIgnoreCase("RATING")
							&& !key.equalsIgnoreCase("QUALITY_PERCENT") && !key.equalsIgnoreCase("SHEET_ID")
							&& !key.equalsIgnoreCase("TOTAL_POINT")) {
						lstHeader.add(key);
					}
				}
			}

			List<List<String>> dataList = new ArrayList();

			for (int i = 0; i < scoreSheetDataList.size(); ++i) {
				ReviewScoreSheetVO scoreSheetDataVO = (ReviewScoreSheetVO) scoreSheetDataList.get(i);
				categoryList = scoreSheetDataVO.getReviewCategory();
				HashMap hashMap = (HashMap) scoreSheetReviewMapList.get(i);
				List<String> datamap = new ArrayList();
				Set keys = hashMap.keySet();
				datamap.add((String) hashMap.get("CRN"));
				Date date = new Date(((Timestamp) hashMap.get("DATE_OF_REVIEW")).getTime());
				SimpleDateFormat targetSdf = new SimpleDateFormat("dd-MM-yyyy");
				datamap.add(targetSdf.format(date));
				datamap.add((String) hashMap.get("ANALYST"));
				datamap.add((String) hashMap.get("REVIEWER"));
				datamap.add((String) hashMap.get("COMPLEXITY"));
				datamap.add("" + hashMap.get("REVIEW TIME"));
				datamap.add("" + hashMap.get("RATING"));
				datamap.add("" + hashMap.get("TOTAL_POINT"));
				datamap.add("" + hashMap.get("SCORE"));
				datamap.add("" + hashMap.get("QUALITY_PERCENT"));
				Iterator var15 = categoryList.iterator();

				while (var15.hasNext()) {
					ReviewScoreSheetCategoryVO categoryVO = (ReviewScoreSheetCategoryVO) var15.next();
					List<ReviewScoreSheetSubCatgVO> subCategory = categoryVO.getScoreSheetSubCategory();
					Iterator var18 = subCategory.iterator();

					while (var18.hasNext()) {
						ReviewScoreSheetSubCatgVO subCategoryVO = (ReviewScoreSheetSubCatgVO) var18.next();
						List<ReviewScoreSheetSubCatgVO> score = subCategoryVO.getGradingPoint();
						Iterator var21 = score.iterator();

						while (var21.hasNext()) {
							ReviewScoreSheetSubCatgVO gradingPoint = (ReviewScoreSheetSubCatgVO) var21.next();
							datamap.add(gradingPoint.getScore());
						}
					}

					datamap.add(categoryVO.getComments());
				}

				Iterator iterator = keys.iterator();

				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if (!key.equalsIgnoreCase("CRN") && !key.equalsIgnoreCase("DATE_OF_REVIEW")
							&& !key.equalsIgnoreCase("ANALYST") && !key.equalsIgnoreCase("REVIEWER")
							&& !key.equalsIgnoreCase("COMPLEXITY") && !key.equalsIgnoreCase("REVIEW TIME")
							&& !key.equalsIgnoreCase("SCORE") && !key.equalsIgnoreCase("RATING")
							&& !key.equalsIgnoreCase("QUALITY_PERCENT") && !key.equalsIgnoreCase("SHEET_ID")
							&& !key.equalsIgnoreCase("TOTAL_POINT")) {
						boolean flag = hashMap.get("") != null ? this.checkDataType(hashMap.get("")) : false;
						if (flag) {
							date = new Date(((Timestamp) hashMap.get(key)).getTime());
							targetSdf = new SimpleDateFormat("dd-MM-yyyy");
							datamap.add(targetSdf.format(date));
						} else {
							datamap.add(String.valueOf(hashMap.get(key)));
						}
					}
				}

				dataList.add(datamap);
			}

			this.logger.debug("data return to downloadScoreSheetMISReport");
			return ExcelDownloader.writeToExcel1(lstHeader, dataList, "scoreSheetMisReport", (short) 0, (short) 1,
					response, "scoreSheetMisReport");
		} catch (ClassCastException var22) {
			throw new CMSException(this.logger, var22);
		} catch (NullPointerException var23) {
			throw new CMSException(this.logger, var23);
		}
	}

	private Map<String, Object> getResultCount(List<HashMap> scoreSheetReviewMapList,
			List<ReviewScoreSheetVO> scoreSheetDataList) throws CMSException {
		HashMap resultMap = new HashMap();

		try {
			this.logger.debug("inside the ScoresheetMisReport >> getResultCount");
			List<String> lstHeader = new ArrayList();
			lstHeader.add("CRN");
			lstHeader.add("DATE_OF_REVIEW");
			lstHeader.add("ANALYST");
			lstHeader.add("REVIEWER");
			lstHeader.add("COMPLEXITY");
			lstHeader.add("REVIEW TIME");
			lstHeader.add("RATING");
			lstHeader.add("Total Point");
			lstHeader.add("SCORED");
			lstHeader.add("QUALITY");
			List<ReviewScoreSheetCategoryVO> categoryList = ((ReviewScoreSheetVO) scoreSheetDataList.get(0))
					.getReviewCategory();
			Iterator var7 = categoryList.iterator();

			while (var7.hasNext()) {
				ReviewScoreSheetCategoryVO categoryVO = (ReviewScoreSheetCategoryVO) var7.next();
				List<ReviewScoreSheetSubCatgVO> subCategory = categoryVO.getScoreSheetSubCategory();
				Iterator var10 = subCategory.iterator();

				while (var10.hasNext()) {
					ReviewScoreSheetSubCatgVO subCategoryVO = (ReviewScoreSheetSubCatgVO) var10.next();
					lstHeader.add(categoryVO.getCategory() + "-" + subCategoryVO.getSub_category());
				}

				lstHeader.add(categoryVO.getCategory() + "- Comment");
			}

			var7 = scoreSheetReviewMapList.iterator();
			if (var7.hasNext()) {
				HashMap hashMap = (HashMap) var7.next();
				Set keys = hashMap.keySet();
				Iterator iterator = keys.iterator();

				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if (!key.equalsIgnoreCase("CRN") && !key.equalsIgnoreCase("DATE_OF_REVIEW")
							&& !key.equalsIgnoreCase("ANALYST") && !key.equalsIgnoreCase("REVIEWER")
							&& !key.equalsIgnoreCase("COMPLEXITY") && !key.equalsIgnoreCase("REVIEW TIME")
							&& !key.equalsIgnoreCase("SCORE") && !key.equalsIgnoreCase("RATING")
							&& !key.equalsIgnoreCase("QUALITY_PERCENT") && !key.equalsIgnoreCase("SHEET_ID")
							&& !key.equalsIgnoreCase("TOTAL_POINT")) {
						lstHeader.add(key);
					}
				}
			}

			List<List<String>> dataList = new ArrayList();

			for (int i = 0; i < scoreSheetDataList.size(); ++i) {
				ReviewScoreSheetVO scoreSheetDataVO = (ReviewScoreSheetVO) scoreSheetDataList.get(i);
				categoryList = scoreSheetDataVO.getReviewCategory();
				HashMap hashMap = (HashMap) scoreSheetReviewMapList.get(i);
				List<String> datamap = new ArrayList();
				Set keys = hashMap.keySet();
				datamap.add((String) hashMap.get("CRN"));
				Date date = new Date(((Timestamp) hashMap.get("DATE_OF_REVIEW")).getTime());
				SimpleDateFormat targetSdf = new SimpleDateFormat("dd-MM-yyyy");
				datamap.add(targetSdf.format(date));
				datamap.add((String) hashMap.get("ANALYST"));
				datamap.add((String) hashMap.get("REVIEWER"));
				datamap.add((String) hashMap.get("COMPLEXITY"));
				datamap.add("" + hashMap.get("REVIEW TIME"));
				datamap.add("" + hashMap.get("RATING"));
				datamap.add("" + hashMap.get("TOTAL_POINT"));
				datamap.add("" + hashMap.get("SCORE"));
				datamap.add("" + hashMap.get("QUALITY_PERCENT"));
				Iterator var15 = categoryList.iterator();

				while (var15.hasNext()) {
					ReviewScoreSheetCategoryVO categoryVO = (ReviewScoreSheetCategoryVO) var15.next();
					List<ReviewScoreSheetSubCatgVO> subCategory = categoryVO.getScoreSheetSubCategory();
					Iterator var18 = subCategory.iterator();

					while (var18.hasNext()) {
						ReviewScoreSheetSubCatgVO subCategoryVO = (ReviewScoreSheetSubCatgVO) var18.next();
						List<ReviewScoreSheetSubCatgVO> score = subCategoryVO.getGradingPoint();
						Iterator var21 = score.iterator();

						while (var21.hasNext()) {
							ReviewScoreSheetSubCatgVO gradingPoint = (ReviewScoreSheetSubCatgVO) var21.next();
							datamap.add(gradingPoint.getScore());
						}
					}

					datamap.add(categoryVO.getComments());
				}

				Iterator iterator = keys.iterator();

				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					if (!key.equalsIgnoreCase("CRN") && !key.equalsIgnoreCase("DATE_OF_REVIEW")
							&& !key.equalsIgnoreCase("ANALYST") && !key.equalsIgnoreCase("REVIEWER")
							&& !key.equalsIgnoreCase("COMPLEXITY") && !key.equalsIgnoreCase("REVIEW TIME")
							&& !key.equalsIgnoreCase("SCORE") && !key.equalsIgnoreCase("RATING")
							&& !key.equalsIgnoreCase("QUALITY_PERCENT") && !key.equalsIgnoreCase("SHEET_ID")
							&& !key.equalsIgnoreCase("TOTAL_POINT")) {
						boolean flag = hashMap.get("") != null ? this.checkDataType(hashMap.get("")) : false;
						if (flag) {
							date = new Date(((Timestamp) hashMap.get(key)).getTime());
							targetSdf = new SimpleDateFormat("dd-MM-yyyy");
							datamap.add(targetSdf.format(date));
						} else {
							datamap.add(String.valueOf(hashMap.get(key)));
						}
					}
				}

				dataList.add(datamap);
			}

			resultMap.put("columnCount", lstHeader.size());
			resultMap.put("rowCount", dataList.size());
			resultMap.put("dataList", dataList);
			resultMap.put("headerList", lstHeader);
			return resultMap;
		} catch (ClassCastException var22) {
			throw new CMSException(this.logger, var22);
		} catch (NullPointerException var23) {
			throw new CMSException(this.logger, var23);
		}
	}

	private boolean checkDataType(Object dataObj) {
		boolean isTimeStampDataType = false;
		isTimeStampDataType = dataObj.getClass() == Timestamp.class;
		return isTimeStampDataType;
	}
}