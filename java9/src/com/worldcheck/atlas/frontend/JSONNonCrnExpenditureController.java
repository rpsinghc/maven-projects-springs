package com.worldcheck.atlas.frontend;

import com.worldcheck.atlas.bl.interfaces.INonCrnExpenditure;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.frontend.commoncontroller.JSONMultiActionController;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.JsonBeanUtil;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.VendorMasterVO;
import com.worldcheck.atlas.vo.noncrnexpenditure.NonCRNExpenditureVO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class JSONNonCrnExpenditureController extends JSONMultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.JSONNonCrnExpenditureController");
	private PropertyReaderUtil propertyReader = null;
	private final String COUNTRY_ID = "countryId";
	private final String VENDORE_MASTER_LIST = "vendorMasterList";
	private final String PAYMENT_CYCLE_LIST = "paymentCycleList";
	private INonCrnExpenditure nonCrnExpenditureService = null;

	public void setNonCrnExpenditureService(INonCrnExpenditure nonCrnExpenditureService) {
		this.nonCrnExpenditureService = nonCrnExpenditureService;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReaderUtil) {
		this.propertyReader = propertyReaderUtil;
	}

	public ModelAndView getPaymentCycle(HttpServletRequest request, HttpServletResponse response,
			NonCRNExpenditureVO nonCRNExpenditureVO) {
		ModelAndView viewForJson = new ModelAndView("jsonView");

		try {
			String paymentCycle = this.propertyReader.getPaymentCycle();
			List<String> paymentList = StringUtils.commaSeparatedStringToList(paymentCycle);
			viewForJson.addObject("paymentCycleList", JsonBeanUtil.toJsonBean(paymentList));
			return viewForJson;
		} catch (Exception var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}

	public ModelAndView getCountryVendorMaster(HttpServletRequest request, HttpServletResponse response,
			NonCRNExpenditureVO nonCRNExpenditureVO) {
		new ArrayList();
		ModelAndView viewForJson = new ModelAndView("jsonView");

		try {
			this.logger.debug(" country id is " + request.getParameter("countryId"));
			String countryId = request.getParameter("countryId");
			List<VendorMasterVO> vendorList = this.nonCrnExpenditureService.getCountryVendorMaster(countryId);
			viewForJson.addObject("vendorMasterList", vendorList);
			return viewForJson;
		} catch (CMSException var7) {
			return AtlasUtils.getJsonExceptionView(this.logger, var7, response);
		}
	}
}