package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.masters.PublicHolidayDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.masters.PublicHolidayMasterVO;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public interface IPublicHolidayMaster {
	void setPublicHolidayDAO(PublicHolidayDAO var1);

	List<PublicHolidayMasterVO> getPublicHolidayGrid(PublicHolidayMasterVO var1, int var2, int var3, String var4,
			String var5) throws CMSException;

	int getPublicHolidayGridCount(PublicHolidayMasterVO var1) throws CMSException;

	List<PublicHolidayMasterVO> getYearList(String var1);

	int deletePublicHoliday(String var1) throws CMSException;

	PublicHolidayMasterVO getPublicHolidayInfo(String var1) throws CMSException;

	int updatePublicHoliday(PublicHolidayMasterVO var1) throws CMSException;

	int addPublicHoliday(PublicHolidayMasterVO var1) throws CMSException;

	List<PublicHolidayMasterVO> isPublicHolidayExist(PublicHolidayMasterVO var1) throws CMSException;

	List<PublicHolidayMasterVO> getCurrentAndNextMonthHolidays(HttpServletRequest var1) throws CMSException;

	int getCurrentAndNextMonthHolidaysCount() throws CMSException;

	List<PublicHolidayMasterVO> getReport(Map<String, Object> var1) throws CMSException;
}