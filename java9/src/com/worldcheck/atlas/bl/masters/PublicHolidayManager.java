package com.worldcheck.atlas.bl.masters;

import com.worldcheck.atlas.bl.interfaces.IPublicHolidayMaster;
import com.worldcheck.atlas.dao.masters.PublicHolidayDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.masters.PublicHolidayMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class PublicHolidayManager implements IPublicHolidayMaster {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.masters.PublicHolidayManager");
	private PublicHolidayDAO publicHolidayDAO;
	private String START = "start";
	private String LIMIT = "limit";

	public void setPublicHolidayDAO(PublicHolidayDAO publicHolidayDAO) {
		this.publicHolidayDAO = publicHolidayDAO;
	}

	public List<PublicHolidayMasterVO> getPublicHolidayGrid(PublicHolidayMasterVO publicHolidayMasterVO, int start,
			int limit, String sortColumnName, String sortType) throws CMSException {
		publicHolidayMasterVO.setStart(new Integer(start + 1));
		publicHolidayMasterVO.setLimit(new Integer(start + limit));
		publicHolidayMasterVO.setSortColumnName(sortColumnName);
		publicHolidayMasterVO.setSortType(sortType);
		List<PublicHolidayMasterVO> publicHolidayList = this.publicHolidayDAO
				.getPublicHolidayGrid(publicHolidayMasterVO);
		return publicHolidayList;
	}

	public int getPublicHolidayGridCount(PublicHolidayMasterVO publicHolidayMasterVO) throws CMSException {
		return this.publicHolidayDAO.getPublicHolidayGridCount(publicHolidayMasterVO);
	}

	public List<PublicHolidayMasterVO> getYearList(String years) {
		String[] yearList = years.split(",");
		List<PublicHolidayMasterVO> publicHolidayVOList = new ArrayList();

		for (int i = 0; i < yearList.length; ++i) {
			PublicHolidayMasterVO publicHolidayMasterVO = new PublicHolidayMasterVO();
			publicHolidayMasterVO.setHolidayYear(yearList[i]);
			publicHolidayVOList.add(publicHolidayMasterVO);
		}

		return publicHolidayVOList;
	}

	public int deletePublicHoliday(String holidayId) throws CMSException {
		return this.publicHolidayDAO.deletePublicHoliday(holidayId);
	}

	public PublicHolidayMasterVO getPublicHolidayInfo(String publicHolidayId) throws CMSException {
		PublicHolidayMasterVO publicHolidayMasterVO = this.publicHolidayDAO.getPublicHolidayInfo(publicHolidayId);
		return publicHolidayMasterVO;
	}

	public int updatePublicHoliday(PublicHolidayMasterVO publicHolidayMasterVO) throws CMSException {
		return this.publicHolidayDAO.updatePublicHoliday(publicHolidayMasterVO);
	}

	public int addPublicHoliday(PublicHolidayMasterVO publicHolidayMasterVO) throws CMSException {
		return this.publicHolidayDAO.addPublicHoliday(publicHolidayMasterVO);
	}

	public List<PublicHolidayMasterVO> isPublicHolidayExist(PublicHolidayMasterVO publicHolidayMasterVO)
			throws CMSException {
		List<PublicHolidayMasterVO> publicHolidayList = this.publicHolidayDAO
				.isPublicHolidayExist(publicHolidayMasterVO);
		return publicHolidayList;
	}

	public List<PublicHolidayMasterVO> getCurrentAndNextMonthHolidays(HttpServletRequest request) throws CMSException {
		List holidayList = null;

		try {
			int start = Integer.parseInt(request.getParameter(this.START));
			int limit = Integer.parseInt(request.getParameter(this.LIMIT));
			HashMap<String, Object> paramMap = new HashMap();
			paramMap.put(this.START, start + 1);
			paramMap.put(this.LIMIT, start + limit);
			holidayList = this.publicHolidayDAO.getCurrentAndNextMonthHolidays(paramMap);
			return holidayList;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int getCurrentAndNextMonthHolidaysCount() throws CMSException {
		return this.publicHolidayDAO.getCurrentAndNextMonthHolidaysCount();
	}

	public List<PublicHolidayMasterVO> getReport(Map<String, Object> excelParamMap) throws CMSException {
		return this.publicHolidayDAO.getReport(excelParamMap);
	}
}