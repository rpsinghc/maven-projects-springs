package com.worldcheck.atlas.frontend.masters;

import com.worldcheck.atlas.bl.downloadexcel.CSVDownloader;
import com.worldcheck.atlas.bl.invoicemis.InvoiceMISManager;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.AtlasUtils;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.report.InvoiceMISVO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class InvoiceMISController extends MultiActionController {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.InvoiceMISController");
	private static final String INVOICE_MIS_DATA = "INVOICE_MIS";
	private static final String CASE_REFERENCE_NO = "CASE REFERENCE NO";
	private static final String Client_REFERENCE_NO = "Client REFERENCE NO";
	private static final String SUBJECT_NAME = "SUBJECT_NAME";
	private static final String SUBJECT_COUNTRY = "SUBJECT_COUNTRY";
	private static final String ENTITY_TYPE = "Entity Tpe";
	private static final String REPORT_LEVEL = "REPORT_LEVEL";
	private static final String SUBJECT_FEE = "SUBJECT_FEE";
	private static final String CASE_FEE = "CASE FEE";
	private static final String CASE_CREATION_DATE = "CASE CREATION DATE";
	private static final String FINAL_REPORT_SENT_DATE = "FINAL REPORT SENT DATE";
	private static final String REGISTER_NUMBER = "REGISTER NUMBER";
	private static final String REGISTER_DATE = "REGISTER DATE";
	private String INVOICE_MIS_JSP = "invoiceMIS";
	InvoiceMISManager invoiceMISManager = null;
	PropertyReaderUtil propertyReader = null;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public void setInvoiceMISManager(InvoiceMISManager invoiceMISManager) {
		this.invoiceMISManager = invoiceMISManager;
	}

	public ModelAndView exportToExcelInvoiceMIS(HttpServletRequest request, HttpServletResponse response,
			InvoiceMISVO invoicemisvo) {
		this.logger.debug("in export to excel");
		ModelAndView modelandview = null;

		try {
			String excelParams = request.getParameter("excelParams");
			String dataType = request.getParameter("dataType");
			this.logger.debug("dataType :: " + dataType);
			Map<String, Object> excelParamMap = (Map) JSONValue.parse(excelParams);
			this.logger.debug("parameters :: " + excelParamMap);
			this.logger.debug("dataType :: " + dataType);
			new ArrayList();
			List<InvoiceMISVO> dataForExport = this.invoiceMISManager.getInvoiceMISDataForExport(excelParamMap);
			this.logger.debug("number of records " + dataForExport.size());
			String fileName = this.writeToExcel(dataForExport, dataType, response);
			modelandview = new ModelAndView("misExcelDownloadPopup");
			modelandview.addObject("fileName", fileName);
			return modelandview;
		} catch (CMSException var10) {
			return AtlasUtils.getExceptionView(this.logger, var10);
		} catch (Exception var11) {
			return AtlasUtils.getExceptionView(this.logger, var11);
		}
	}

	private String writeToExcel(List<InvoiceMISVO> resultList, String dataType, HttpServletResponse response)
			throws CMSException {
		this.logger.debug("Entring InvoiceMISController:writeToExcel ");
		List<String> lstHeader = this.getHeaderList(dataType);
		List<List<String>> dataList = new ArrayList();
		InvoiceMISVO invoiceMISVO = null;

		try {
			Iterator iterator = resultList.iterator();

			while (iterator.hasNext()) {
				List<String> datamap = new ArrayList();
				invoiceMISVO = (InvoiceMISVO) iterator.next();
				this.populateDataMap(invoiceMISVO, dataType, datamap);
				dataList.add(datamap);
			}

			return CSVDownloader.exportCSV(lstHeader, dataList, dataType, response);
		} catch (UnsupportedOperationException var9) {
			throw new CMSException(this.logger, var9);
		} catch (ClassCastException var10) {
			throw new CMSException(this.logger, var10);
		} catch (NullPointerException var11) {
			throw new CMSException(this.logger, var11);
		} catch (IllegalArgumentException var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	private List<String> getHeaderList(String dataType) throws CMSException {
		this.logger.debug("Entring InvoiceMISController:getHeaderList");
		ArrayList lstHeader = new ArrayList();

		try {
			if (dataType.equals("INVOICE_MIS")) {
				lstHeader.add("CASE REFERENCE NO");
				lstHeader.add("Client REFERENCE NO");
				lstHeader.add("SUBJECT_NAME");
				lstHeader.add("SUBJECT_COUNTRY");
				lstHeader.add("Entity Tpe");
				lstHeader.add("REPORT_LEVEL");
				lstHeader.add("SUBJECT_FEE");
				lstHeader.add("CASE FEE");
				lstHeader.add("CASE CREATION DATE");
				lstHeader.add("FINAL REPORT SENT DATE");
				lstHeader.add("REGISTER NUMBER");
				lstHeader.add("REGISTER DATE");
			}
		} catch (UnsupportedOperationException var4) {
			throw new CMSException(this.logger, var4);
		} catch (ClassCastException var5) {
			throw new CMSException(this.logger, var5);
		} catch (NullPointerException var6) {
			throw new CMSException(this.logger, var6);
		} catch (IllegalArgumentException var7) {
			throw new CMSException(this.logger, var7);
		}

		this.logger.debug("Existing InvoiceMISController:getHeaderList");
		return lstHeader;
	}

	private void populateDataMap(InvoiceMISVO invoiceMISVO, String dataType, List<String> datamap) {
		this.logger.debug("Entring InvoiceMISController:populateDataMap");
		if (dataType.equalsIgnoreCase("INVOICE_MIS")) {
			datamap.add(String.valueOf(invoiceMISVO.getCrn() != null ? invoiceMISVO.getCrn() : ""));
			datamap.add(String
					.valueOf(invoiceMISVO.getClientReferenceNo() != null ? invoiceMISVO.getClientReferenceNo() : ""));
			datamap.add(String.valueOf(invoiceMISVO.getSubjectName() != null ? invoiceMISVO.getSubjectName() : ""));
			datamap.add(
					String.valueOf(invoiceMISVO.getSubjectCountry() != null ? invoiceMISVO.getSubjectCountry() : ""));
			datamap.add(String.valueOf(invoiceMISVO.getEntityType() != null ? invoiceMISVO.getEntityType() : ""));
			datamap.add(String.valueOf(invoiceMISVO.getReportLevel() != null ? invoiceMISVO.getReportLevel() : ""));
			datamap.add(String.valueOf(invoiceMISVO.getSubjectFee() != null ? invoiceMISVO.getSubjectFee() : ""));
			datamap.add(String.valueOf(invoiceMISVO.getCaseFee() != null ? invoiceMISVO.getCaseFee() : ""));
			datamap.add(String.valueOf(invoiceMISVO.getCaseCreationDate() != null
					? invoiceMISVO.getCaseCreationDate().split(" ")[0]
					: ""));
			datamap.add(String.valueOf(invoiceMISVO.getFinalaReportSentdate() != null
					? invoiceMISVO.getFinalaReportSentdate().split(" ")[0]
					: ""));
			datamap.add(
					String.valueOf(invoiceMISVO.getRegisterNumber() != null ? invoiceMISVO.getRegisterNumber() : ""));
			datamap.add(String.valueOf(
					invoiceMISVO.getRegisterDate() != null ? invoiceMISVO.getRegisterDate().split(" ")[0] : ""));
			this.logger.debug("Existing InvoiceMISController:populateDataMap");
		}

	}
}