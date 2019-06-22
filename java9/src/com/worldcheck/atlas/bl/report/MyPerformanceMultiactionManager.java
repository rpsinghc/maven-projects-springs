package com.worldcheck.atlas.bl.report;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.dao.report.TabularReportDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.report.MyPerformanceReportVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyPerformanceMultiactionManager implements IAtlasReport {
	private static final String myPerformanceKey = "myPerformanceKey";
	private String YEAR = "year";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.frontend.masters.OverdueReportMultiactionManager");
	private TabularReportDAO tabularReportDAO;

	public void setTabularReportDAO(TabularReportDAO tabularReportDAO) {
		this.tabularReportDAO = tabularReportDAO;
	}

	public List fetchReport(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		HashMap<String, String> hmapValue = null;
		HashMap<String, HashMap<String, String>> hmapList = null;
		List<HashMap<String, HashMap<String, String>>> finalList = new ArrayList();
		MyPerformanceReportVO myPerformanceReportVO = new MyPerformanceReportVO();
		myPerformanceReportVO.setYear(request.getParameter(this.YEAR));
		myPerformanceReportVO.setAnalyst(((UserBean) request.getSession().getAttribute("userBean")).getUserName());
		this.logger.debug("the username" + ((UserBean) request.getSession().getAttribute("userBean")).getUserName());
		this.logger.debug("==========IN Manager of REport========>" + request.getParameter(this.YEAR));
		List<MyPerformanceReportVO> myPerformanceReportVO2 = this.tabularReportDAO
				.fetchMyPerformance(myPerformanceReportVO);
		Iterator<MyPerformanceReportVO> itr = myPerformanceReportVO2.iterator();
		this.logger.debug("List :" + myPerformanceReportVO2);

		while (itr.hasNext()) {
			MyPerformanceReportVO itrVO = (MyPerformanceReportVO) itr.next();
			this.logger.debug("itrVO :" + itrVO.getMonth() + ":" + itrVO.getListMyPerformanceReportVO());
			List<MyPerformanceReportVO> innerLst = itrVO.getListMyPerformanceReportVO();
			this.logger.debug("Inner List size:" + innerLst.size());
			hmapValue = new HashMap();
			Iterator inItr = innerLst.iterator();

			while (inItr.hasNext()) {
				hmapList = new HashMap();
				MyPerformanceReportVO inItrVO = (MyPerformanceReportVO) inItr.next();
				hmapValue.put(inItrVO.getReportType(), inItrVO.getNumOfCases());
				hmapValue.put("month", inItrVO.getMonth());
			}

			hmapList.put("myPerformanceKey", hmapValue);
			finalList.add(hmapList);
		}

		return finalList;
	}

	public int fetchTotalCount(HttpServletRequest request, HttpServletResponse response) throws CMSException {
		return 0;
	}

	public List<MyPerformanceReportVO> getCRNS(HashMap hm) throws CMSException {
		this.logger.debug("Inside Manager for CRNS");
		return this.tabularReportDAO.getCRNS(hm);
	}
}