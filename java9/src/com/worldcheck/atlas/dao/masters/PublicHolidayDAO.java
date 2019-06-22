package com.worldcheck.atlas.dao.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.masters.PublicHolidayMasterVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class PublicHolidayDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.PublicHolidayDAO");
	String SUCCESS = "success";
	String ADD_PUBLIC_HOLIDAY = "PublicHoliday_Master.addPublicHoliday";
	String UPDATE_PUBLIC_HOLIDAY = "PublicHoliday_Master.updatePublicHoliday";
	String DELETE_PUBLIC_HOLIDAT = "PublicHoliday_Master.deletePublicHoliday";
	String GET_PUBLIC_HOLIDAY_INFO = "PublicHoliday_Master.getPublicHolidayInfo";
	String GET_PUBLIC_HOLIDAY_LIST = "PublicHoliday_Master.getPublicHolidayGridList";
	String GET_PUBLIC_HOLIDAY_SQL_ID = "OfficeUtilization.getPublicHoliday";
	String GET_PUBLIC_HOLIDAY_COUNT_SQL_ID = "OfficeUtilization.getPublicHolidayCount";
	private String COUNT_PUBLIC_HOLIDAY = "PublicHoliday_Master.getPublicHolidayGridListCount";
	private String IS_PUBLIC_HOLIDAY_EXIST = "PublicHoliday_Master.isExistPublicHolidayInfo";
	private String PUBLIC_HOLIDAY_EXPORT_TO_EXCEL = "PublicHoliday_Master.exportTo_XLS";
	private String OFFICE_ID = "officeId";
	private String HOLIDAY_DATE = "holidayDate";

	public List<PublicHolidayMasterVO> getCurrentAndNextMonthHolidays(HashMap<String, Object> paramMap)
			throws CMSException {
		try {
			return this.queryForList(this.GET_PUBLIC_HOLIDAY_SQL_ID, paramMap);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int getCurrentAndNextMonthHolidaysCount() throws CMSException {
		try {
			return (Integer) this.queryForObject(this.GET_PUBLIC_HOLIDAY_COUNT_SQL_ID);
		} catch (DataAccessException var2) {
			throw new CMSException(this.logger, var2);
		}
	}

	public List<PublicHolidayMasterVO> getPublicHolidayGrid(PublicHolidayMasterVO publicHolidayMasterVO)
			throws CMSException {
		try {
			this.logger.debug("office:" + publicHolidayMasterVO.getOffice() + "holiday Year:"
					+ publicHolidayMasterVO.getHolidayYear());
			new ArrayList();
			List<PublicHolidayMasterVO> publicHolidayGridList = this.queryForList(this.GET_PUBLIC_HOLIDAY_LIST,
					publicHolidayMasterVO);
			this.logger.debug("Total Record::" + publicHolidayGridList.size());
			return publicHolidayGridList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getPublicHolidayGridCount(PublicHolidayMasterVO publicHolidayMasterVO) throws CMSException {
		this.logger.debug("count>>>>>>>>>>>>>>>>>>>>>>inside dao:" + publicHolidayMasterVO.getStart());

		try {
			return (Integer) this.queryForObject(this.COUNT_PUBLIC_HOLIDAY, publicHolidayMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int deletePublicHoliday(String holidayId) throws CMSException {
		List holidayIdList = null;

		try {
			this.logger.debug("Inside the PublicHoliday Dao ::" + holidayId);
			holidayIdList = StringUtils.commaSeparatedStringToList(holidayId);
			this.logger.debug("Inside the PublicHoliday Dao ::" + holidayId);
			int count = this.delete(this.DELETE_PUBLIC_HOLIDAT, holidayIdList);
			return count;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public PublicHolidayMasterVO getPublicHolidayInfo(String publicHolidayId) throws CMSException {
		PublicHolidayMasterVO publicHoliday = null;

		try {
			this.logger.debug("\n*****\nPublicHolidayId:" + publicHolidayId);
			publicHoliday = (PublicHolidayMasterVO) this.queryForObject(this.GET_PUBLIC_HOLIDAY_INFO, publicHolidayId);
			return publicHoliday;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int updatePublicHoliday(PublicHolidayMasterVO publicHolidayMasterVO) throws CMSException {
		try {
			this.logger.debug("Update PublicHoliday.... from PublicHolidayDAO");
			int count = this.update(this.UPDATE_PUBLIC_HOLIDAY, publicHolidayMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int addPublicHoliday(PublicHolidayMasterVO publicHolidayMasterVO) throws CMSException {
		try {
			this.logger.debug("Insert new Public Holiday.... from PublicHolidayDAO");
			int count = (Integer) this.insert(this.ADD_PUBLIC_HOLIDAY, publicHolidayMasterVO);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<PublicHolidayMasterVO> isPublicHolidayExist(PublicHolidayMasterVO publicHolidayMasterVO)
			throws CMSException {
		try {
			new ArrayList();
			this.logger.debug("officeid :" + publicHolidayMasterVO.getOfficeId() + "holiday Date:"
					+ publicHolidayMasterVO.getHolidayDate());
			List<PublicHolidayMasterVO> publicHolidayGridList = this.queryForList(this.IS_PUBLIC_HOLIDAY_EXIST,
					publicHolidayMasterVO);
			this.logger.debug("Size:" + publicHolidayGridList.size() + " public Holiday:" + publicHolidayGridList);
			return publicHolidayGridList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<PublicHolidayMasterVO> getReport(Map<String, Object> excelParamMap) throws CMSException {
		List mv = null;

		try {
			mv = this.queryForList(this.PUBLIC_HOLIDAY_EXPORT_TO_EXCEL, excelParamMap);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}