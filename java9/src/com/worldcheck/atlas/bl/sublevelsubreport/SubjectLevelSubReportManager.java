package com.worldcheck.atlas.bl.sublevelsubreport;

import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ExposePropertyPlaceholderConfigurer;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.ClientMasterVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.report.SubReportTypeVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SubjectLevelSubReportManager {
	private static ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.sublevelsubreport.SubjectLevelSubReportManager");
	String propertyName = "";
	ExposePropertyPlaceholderConfigurer propertyConfigurer = null;

	public void setPropertyConfigurer(ExposePropertyPlaceholderConfigurer propertyConfigurer) {
		this.propertyConfigurer = propertyConfigurer;
	}

	public List getReportTypeMasterForSubjectLevel(String code_Name) throws Exception {
		logger.debug("In getReportTypeMasterForSubjectLevel method");
		List<ReportTypeMasterVO> reportTypeMasterList = null;
		this.propertyName = (String) this.propertyConfigurer.getResolvedProps().get("atlas.report.type.table");
		reportTypeMasterList = ResourceLocator.self().getCacheService().getCacheItemsList(this.propertyName);
		Iterator<ReportTypeMasterVO> itr = ((List) reportTypeMasterList).iterator();
		logger.debug("out getReportTypeMasterForSubjectLevel before filter" + reportTypeMasterList);
		this.propertyName = (String) this.propertyConfigurer.getResolvedProps().get("atlas.report.type.name");
		String[] keyValuePairs = this.propertyName.split(":");
		Map map = new HashMap();
		String[] var9 = keyValuePairs;
		int var8 = keyValuePairs.length;

		for (int var7 = 0; var7 < var8; ++var7) {
			String pair = var9[var7];
			logger.debug("out getReportTypeMasterForSubjectLevel -- pair:" + pair);
			if (pair != null && pair.contains("=")) {
				String[] entry = pair.split("=");
				map.put(entry[0].trim(), entry[1].trim());
			}
		}

		if (map != null) {
			if (map.get(code_Name) != null) {
				this.propertyName = (String) map.get(code_Name);
			} else if (map.get("DEFAULT") != null) {
				this.propertyName = (String) map.get("DEFAULT");
			}
		}

		logger.debug("out getReportTypeMasterForSubjectLevel before loop:: propertyName " + this.propertyName);
		List<ReportTypeMasterVO> reportTypeMaster = new ArrayList();
		if (!this.propertyName.trim().equalsIgnoreCase("all") && !this.propertyName.trim().equalsIgnoreCase("")) {
			String[] aryReportType = this.propertyName.split(",");

			while (itr.hasNext()) {
				ReportTypeMasterVO obj = (ReportTypeMasterVO) itr.next();
				logger.debug("out getReportTypeMasterForSubjectLevel aryReportType" + aryReportType);
				if (aryReportType == null && obj.getReportType() != null
						&& obj.getReportType().equalsIgnoreCase(this.propertyName)) {
					logger.debug("out getReportTypeMasterForSubjectLevel in loop1 ==" + obj.getReportType());
					reportTypeMaster.add(obj);
				}

				String[] var12 = aryReportType;
				int var11 = aryReportType.length;

				for (int var17 = 0; var17 < var11; ++var17) {
					String rPT = var12[var17];
					this.propertyName = rPT;
					if (obj.getReportType() != null && obj.getReportType().equalsIgnoreCase(this.propertyName)) {
						logger.debug("out getReportTypeMasterForSubjectLevel in loop2 ==" + obj.getReportType());
						reportTypeMaster.add(obj);
					}
				}
			}

			if (reportTypeMaster != null) {
				reportTypeMasterList = reportTypeMaster;
			}
		}

		logger.debug("out getReportTypeMasterForSubjectLevel method" + reportTypeMasterList);
		return (List) reportTypeMasterList;
	}

	public List<SubReportTypeVO> getSubReportTypeMasterForSubj(String reportType) throws Exception {
		this.propertyName = (String) this.propertyConfigurer.getResolvedProps().get("atlas.subreport.type.table");
		List<SubReportTypeVO> subReportTypeMasterList = ResourceLocator.self().getCacheService()
				.getCacheItemsList(this.propertyName);
		return subReportTypeMasterList;
	}

	public List<ClientMasterVO> getClientMasterForSubjLevelConfig(String clientCode) throws Exception {
		this.propertyName = (String) this.propertyConfigurer.getResolvedProps().get("atlas.client.master.table");
		List<ClientMasterVO> clientMasterList = ResourceLocator.self().getCacheService()
				.getCacheItemsList(this.propertyName);
		return clientMasterList;
	}
}