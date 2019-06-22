package com.worldcheck.atlas.dao.rejection;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.SubTeamReMapVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class RejectionREDAO extends SqlMapClientTemplate {
	private static final String REJECTION_GET_PRIMARY_TEMA_INFO = "Rejection.getPrimaryTemaInfo";
	private static final String REJECTION_GET_NOTI_FICATION_SENDER = "Rejection.getNotiFicationSender";
	private static final String REJECTION_SAVE_REJECTED_RE_INFO = "Rejection.saveRejectedREInfo";
	private static final String REJECTION_GET_ALL_SUBJECT_INFO_BY_TEAM = "Rejection.getAllSubjectInfoByTeam";
	private static final String PROCESS_CYCLE = "processCycle";
	private static final String TEAM_NAME = "teamName";
	private static final String TEAM_ID = "teamId";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.rejection.RejectionREDAO");

	public List<SubTeamReMapVO> getAllSubjectInfoByTeam(String crn, String teamId, String teamName, String processCycle)
			throws CMSException {
		this.logger.debug("CRN:" + crn);
		List rejectionREVOList = null;

		try {
			this.logger.debug("Inside the RejectionREDAO getAllSubjectInfoByTeamCRN::" + crn + "teamName:" + teamName);
			HashMap<String, String> map = new HashMap();
			map.put("crn", crn);
			map.put("teamId", teamId);
			map.put("teamName", teamName);
			map.put("processCycle", processCycle);
			new ArrayList();
			rejectionREVOList = this.queryForList("Rejection.getAllSubjectInfoByTeam", map);
			return rejectionREVOList;
		} catch (DataAccessException var7) {
			throw new CMSException(this.logger, var7);
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	public int saveRejectedREInfo(List<SubTeamReMapVO> teamDetailsVOList) throws CMSException {
		int updateRejectRECount = 0;
		this.logger.debug("inside RejectionREDAO size of list:" + teamDetailsVOList.size());

		try {
			Iterator i$ = teamDetailsVOList.iterator();

			while (i$.hasNext()) {
				SubTeamReMapVO subTeamReMapVO = (SubTeamReMapVO) i$.next();
				this.logger.debug("CRN:" + subTeamReMapVO.getCrn() + "\tSubjectId:" + subTeamReMapVO.getSubjectId()
						+ "\tREID:" + subTeamReMapVO.getReId() + "\tTeamID:" + subTeamReMapVO.getTeamId()
						+ "\tPerformer:" + subTeamReMapVO.getPerformer());
				updateRejectRECount += this.update("Rejection.saveRejectedREInfo", subTeamReMapVO);
				this.logger.debug("updateRejectRECount::" + updateRejectRECount);
			}

			return updateRejectRECount;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public List<SubTeamReMapVO> getNotiFicationSender(String crn) throws CMSException {
		List<SubTeamReMapVO> notificationSender = null;
		this.logger.debug("inside the RejectionREDAO ****** getNotiFicationSender() & CRN::" + crn);

		try {
			notificationSender = this.queryForList("Rejection.getNotiFicationSender", crn);
			this.logger.debug("getNotiFicationSender::" + notificationSender);
			return notificationSender;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public SubTeamReMapVO getPrimaryTemaInfo(String crn) throws CMSException {
		SubTeamReMapVO subTeamReMapVO = null;

		try {
			subTeamReMapVO = (SubTeamReMapVO) this.queryForObject("Rejection.getPrimaryTemaInfo", crn);
			this.logger.debug("getNotiFicationSender::" + subTeamReMapVO);
			return subTeamReMapVO;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}