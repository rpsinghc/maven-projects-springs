package com.worldcheck.atlas.bl.cacheUtil;

import com.worldcheck.atlas.cache.service.CacheService;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.BranchOfficeMasterVO;
import com.worldcheck.atlas.vo.masters.ClientGroupMasterVO;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.CountryDBCommonVO;
import com.worldcheck.atlas.vo.masters.CountryDatabaseMasterVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import com.worldcheck.atlas.vo.masters.IndustryMasterVO;
import com.worldcheck.atlas.vo.masters.LeaveTypeMasterVO;
import com.worldcheck.atlas.vo.masters.REMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.masters.RisksMasterVO;
import com.worldcheck.atlas.vo.masters.ScoreSheetMasterVO;
import com.worldcheck.atlas.vo.masters.VendorMasterVO;
import com.worldcheck.atlas.vo.report.SubReportTypeVO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONEhCacheUtilController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.cacheUtil.JSONEhCacheUtilController");
	private CacheService cacheService;
	private static final String JSONVIEW = "jsonView";

	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	public ModelAndView getClientMaster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<ClientMasterVO> clientMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("CLIENT_MASTER");
			modelAndView.addObject("clientMasterList", clientMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getVendorMaster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<VendorMasterVO> vendorMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("VENDOR_MASTER");
			modelAndView.addObject("vendorMasterList", vendorMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getCountryMaster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<CountryDatabaseMasterVO> countryMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("COUNTRY_MASTER");
			modelAndView.addObject("countryMasterList", countryMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getIndustryMaster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");
		List<IndustryMasterVO> industryMasterList = new ArrayList();
		IndustryMasterVO indVoforOthers = null;

		try {
			List<IndustryMasterVO> industryMasterResultList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("INDUSTRY_MASTER");
			Iterator industryMasterVOIterator = industryMasterResultList.iterator();

			while (industryMasterVOIterator.hasNext()) {
				IndustryMasterVO indvo = (IndustryMasterVO) industryMasterVOIterator.next();
				if (!indvo.getIndustry().equalsIgnoreCase("Others")) {
					industryMasterList.add(indvo);
				} else {
					indVoforOthers = indvo;
				}
			}

			if (indVoforOthers != null) {
				industryMasterList.add(indVoforOthers);
			}

			modelAndView.addObject("industryMasterList", industryMasterList);
			return modelAndView;
		} catch (CMSException var9) {
			return AtlasUtils.getJsonExceptionView(this.logger, var9, response);
		} catch (Exception var10) {
			return AtlasUtils.getJsonExceptionView(this.logger, var10, response);
		}
	}

	public ModelAndView getRiskMaster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<RisksMasterVO> riskMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("RISK_MASTER");
			modelAndView.addObject("riskMasterList", riskMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getCurrencyMaster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<CurrencyMasterVO> currencyMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("CURRENCY_MASTER");
			modelAndView.addObject("currencyMasterList", currencyMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getReportTypeMaster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<ReportTypeMasterVO> reportTypeMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("REPORT_TYPE_MASTER");
			modelAndView.addObject("reportTypeMasterList", reportTypeMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getReportTypeMasterAll(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.logger.debug("In cache util controller");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<ReportTypeMasterVO> reportTypeMasterListAll = ResourceLocator.self().getCacheService()
					.getCacheItemsList("REPORT_TYPE_MASTER_ALL");
			modelAndView.addObject("reportTypeMasterListAll", reportTypeMasterListAll);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getLeaveTypeMaster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<LeaveTypeMasterVO> leaveTypeMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("LEAVE_TYPE_MASTER");
			modelAndView.addObject("leaveTypeMasterList", leaveTypeMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getOfficeMaster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<BranchOfficeMasterVO> officeMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("OFFICE_MASTER");
			modelAndView.addObject("officeMasterList", officeMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getREMaster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<REMasterVO> rEMasterList = ResourceLocator.self().getCacheService().getCacheItemsList("RE_MASTER");
			modelAndView.addObject("rEMasterList", rEMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getREMasterIndividual(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<REMasterVO> rEMasterIndividualList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("RE_MASTER_INDIVIDUAL");
			modelAndView.addObject("rEMasterIndividualList", rEMasterIndividualList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getREMasterCompany(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<REMasterVO> rEMasterCompanyList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("RE_MASTER_COMPANY");
			modelAndView.addObject("rEMasterCompanyList", rEMasterCompanyList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getSubReportTypeMaster(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<SubReportTypeVO> subReportTypeMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("SUBREPORT_TYPE_MASTER");
			modelAndView.addObject("subReportTypeMasterList", subReportTypeMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getScoreSheetFieldsMaster(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<ScoreSheetMasterVO> scoreSheetFieldsMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("SCORESHEET_FLDS_MASTER");
			modelAndView.addObject("scoreSheetFieldsMasterList", scoreSheetFieldsMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getCaseStatusMaster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<ScoreSheetMasterVO> caseStatusMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("CASE_STATUS_MASTER");
			modelAndView.addObject("caseStatusMasterList", caseStatusMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getCntryDbCategMaster(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<CountryDBCommonVO> cntryDbCategMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("CNTRY_DB_CATEG_MASTER");
			modelAndView.addObject("cntryDbCategMasterList", cntryDbCategMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getCntryDbLbrMaster(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<CountryDBCommonVO> cntryDbLbrMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("CNTRY_DB_LBR_MASTER");
			modelAndView.addObject("cntryDbLbrMasterList", cntryDbLbrMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getCntryDbTypeMaster(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<CountryDBCommonVO> cntryDbTypeMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("CNTRY_DB_TYPE_MASTER");
			modelAndView.addObject("cntryDbTypeMasterList", cntryDbTypeMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getRiskLevel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<CountryMasterVO> riskLevelList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("RISK_LEVEL");
			modelAndView.addObject("riskLevelList", riskLevelList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getRegion(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			List<CountryMasterVO> regionList = ResourceLocator.self().getCacheService().getCacheItemsList("REGION");
			modelAndView.addObject("regionList", regionList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getBudgetCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.logger.debug("in getBudgetCount");
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			int budgetCount = ResourceLocator.self().getCacheService().getBudgetCount();
			modelAndView.addObject("budgetCount", budgetCount);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}

	public ModelAndView getClientGroupMaster(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView("jsonView");

		try {
			this.logger.debug("in getClientGroupMaster");
			List<ClientGroupMasterVO> clientGroupMasterList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("CLIENT_GROUP_MASTER");
			this.logger.debug("in getClientGroupMaster" + clientGroupMasterList.size());
			modelAndView.addObject("clientGroupMasterList", clientGroupMasterList);
			return modelAndView;
		} catch (CMSException var5) {
			return AtlasUtils.getJsonExceptionView(this.logger, var5, response);
		} catch (Exception var6) {
			return AtlasUtils.getJsonExceptionView(this.logger, var6, response);
		}
	}
}