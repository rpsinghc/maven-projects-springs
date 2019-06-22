package com.worldcheck.atlas.bl.search;

import com.worldcheck.atlas.bl.interfaces.ISearch;
import com.worldcheck.atlas.bl.interfaces.ITeamSearchManager;
import com.worldcheck.atlas.dao.search.TeamSearchDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.search.TeamSearchVO;
import com.worldcheck.atlas.vo.task.MyTaskPageVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TeamSearchManager implements ITeamSearchManager, ISearch {
	private static final String _BLANK = " ";
	private static final String _T = "T";
	private static final String END_CLIENT_DUE_DATE = "endClientDueDate";
	private static final String START_CLIENT_DUE_DATE = "startClientDueDate";
	private static final String END_CASE_RECV_DATE = "endCaseRecvDate";
	private static final String START_CASE_RECV_DATE = "startCaseRecvDate";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.bl.search.TeamSearchManager");
	PropertyReaderUtil propertyReader = null;
	TeamSearchDAO teamSearchDAO = null;

	public void setTeamSearchDAO(TeamSearchDAO teamSearchDAO) {
		this.teamSearchDAO = teamSearchDAO;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public List<UserMasterVO> getAllAnalyst() throws CMSException {
		List<UserMasterVO> analystList = this.teamSearchDAO.getAllAnalyst();
		return analystList;
	}

	public List<UserMasterVO> getAllReviewer() throws CMSException {
		List<UserMasterVO> reviewerList = this.teamSearchDAO.getAllReviewer();
		return reviewerList;
	}

	public List<UserMasterVO> getAllBiVendorMgr() throws CMSException {
		List<UserMasterVO> biVendorList = this.teamSearchDAO.getAllbiVendor();
		return biVendorList;
	}

	public List<UserMasterVO> vendorTeamSearchInfo() throws CMSException {
		List<UserMasterVO> biVendorList = this.teamSearchDAO.vendorTeamSearchInfo();
		return biVendorList;
	}

	public List<TeamSearchVO> getTeamSearchResult(TeamSearchVO teamSearchVO) throws CMSException {
		this.setDates(teamSearchVO);
		List<TeamSearchVO> teamSearchList = this.teamSearchDAO.getTeamSearchResult(teamSearchVO);
		return teamSearchList;
	}

	private void setDates(TeamSearchVO teamSearchVO) {
		if (teamSearchVO.getStartCaseRecvDate() != null && teamSearchVO.getStartCaseRecvDate().trim().length() > 0) {
			teamSearchVO.setStartCaseRecvDate(teamSearchVO.getStartCaseRecvDate().replace("T", " "));
		}

		if (teamSearchVO.getEndCaseRecvDate() != null && teamSearchVO.getEndCaseRecvDate().trim().length() > 0) {
			teamSearchVO.setEndCaseRecvDate(teamSearchVO.getEndCaseRecvDate().replace("T", " "));
		}

		if (teamSearchVO.getStartClientDueDate() != null && teamSearchVO.getStartClientDueDate().trim().length() > 0) {
			teamSearchVO.setStartClientDueDate(teamSearchVO.getStartClientDueDate().replace("T", " "));
		}

		if (teamSearchVO.getEndClientDueDate() != null && teamSearchVO.getEndClientDueDate().trim().length() > 0) {
			teamSearchVO.setEndClientDueDate(teamSearchVO.getEndClientDueDate().replace("T", " "));
		}

	}

	private void setDatesMap(Map<String, Object> excelParamMap) {
		if (excelParamMap.get("startCaseRecvDate") != null
				&& ((String) excelParamMap.get("startCaseRecvDate")).trim().length() > 0) {
			excelParamMap.put("startCaseRecvDate", ((String) excelParamMap.get("startCaseRecvDate")).replace("T", " "));
		}

		if (excelParamMap.get("endCaseRecvDate") != null
				&& ((String) excelParamMap.get("endCaseRecvDate")).trim().length() > 0) {
			excelParamMap.put("endCaseRecvDate", ((String) excelParamMap.get("endCaseRecvDate")).replace("T", " "));
		}

		if (excelParamMap.get("startClientDueDate") != null
				&& ((String) excelParamMap.get("startClientDueDate")).trim().length() > 0) {
			excelParamMap.put("startClientDueDate",
					((String) excelParamMap.get("startClientDueDate")).replace("T", " "));
		}

		if (excelParamMap.get("endClientDueDate") != null
				&& ((String) excelParamMap.get("endClientDueDate")).trim().length() > 0) {
			excelParamMap.put("endClientDueDate", ((String) excelParamMap.get("endClientDueDate")).replace("T", " "));
		}

	}

	public int resultCount(TeamSearchVO teamSearchVO) throws CMSException {
		this.setDates(teamSearchVO);
		int count = this.teamSearchDAO.resultCount(teamSearchVO);
		return count;
	}

	public List<TeamSearchVO> searchForExport(Map<String, Object> excelParamMap) throws CMSException {
		new ArrayList();
		ArrayList finalSearchResult = new ArrayList();

		try {
			this.setDatesMap(excelParamMap);
			List<TeamSearchVO> teamSearchVO = this.teamSearchDAO.searchForExport(excelParamMap);
			List<String> crnList = new ArrayList();
			Iterator iterator = teamSearchVO.iterator();

			while (iterator.hasNext()) {
				TeamSearchVO teamSearchVO2 = (TeamSearchVO) iterator.next();
				if (!crnList.contains(teamSearchVO2.getCrnNo())) {
					crnList.add(teamSearchVO2.getCrnNo());
				}
			}

			List<MyTaskPageVO> processData = this.getExportTeamSearchProcessData(crnList);
			this.logger.debug("process data size " + processData.size());
			HashMap<String, String> teamMap = new HashMap();
			Iterator iterator = processData.iterator();

			while (true) {
				MyTaskPageVO myTaskPageVO;
				String cycleName;
				String taskName;
				do {
					do {
						if (!iterator.hasNext()) {
							this.logger.debug("team map " + teamMap);
							Set keys = teamMap.keySet();
							myTaskPageVO = null;
							Iterator iterator = keys.iterator();

							while (iterator.hasNext()) {
								cycleName = (String) iterator.next();
								String[] maps = cycleName.split("::");
								taskName = maps[0];
								String teamName = maps[1];
								Iterator iteratorteam = teamSearchVO.iterator();

								while (iteratorteam.hasNext()) {
									TeamSearchVO teamSearchVO2 = (TeamSearchVO) iteratorteam.next();
									String crnteam = teamSearchVO2.getCrnNo();
									String teamNameSearch = teamSearchVO2.getTeamName();
									if (teamNameSearch.contains("Primary")) {
										teamNameSearch = "Primary";
									}

									if (taskName.equals(crnteam) && teamName.equalsIgnoreCase(teamNameSearch)) {
										teamSearchVO2.setActiveTask((String) teamMap.get(cycleName));
										teamSearchVO2.setProcessCycle(maps[2]);
										finalSearchResult.add(teamSearchVO2);
									}
								}
							}

							return finalSearchResult;
						}

						myTaskPageVO = (MyTaskPageVO) iterator.next();
					} while (null == myTaskPageVO.getTaskName());
				} while (myTaskPageVO.getTaskName().equalsIgnoreCase("Invoicing Task"));

				String teamName = "";
				if (null != myTaskPageVO.getTeamTypeList() && !myTaskPageVO.getTeamTypeList().contains("Primary")) {
					teamName = myTaskPageVO.getTeamTypeList();
				} else {
					teamName = "Primary";
				}

				cycleName = "";
				if (null != myTaskPageVO.getCurrentCycle() && !"".equalsIgnoreCase(myTaskPageVO.getCurrentCycle())) {
					cycleName = myTaskPageVO.getCurrentCycle();
				} else {
					cycleName = "-";
				}

				String key = myTaskPageVO.getCrn() + "::" + teamName + "::" + cycleName;
				taskName = "";
				if (null != teamMap.get(key)) {
					taskName = myTaskPageVO.getTaskName() + "," + (String) teamMap.get(key);
				} else {
					taskName = myTaskPageVO.getTaskName();
				}

				teamMap.put(key, taskName);
			}
		} catch (Exception var18) {
			throw new CMSException(this.logger, var18);
		}
	}

	public List search(SearchCriteria searchCriteria) throws CMSException {
		return null;
	}

	public List<MyTaskPageVO> getTeamSearchProcessData(String crnNo, String teamName) throws CMSException {
		new ArrayList();

		try {
			TeamSearchVO teamVo = new TeamSearchVO();
			teamVo.setCrnNo(crnNo);
			List<MyTaskPageVO> taskList = ResourceLocator.self().getSBMService().getAllTaskForCrn(crnNo);
			HashMap<String, String> map = new HashMap();
			Iterator iterator = taskList.iterator();

			while (true) {
				while (iterator.hasNext()) {
					MyTaskPageVO myTaskPageVO = (MyTaskPageVO) iterator.next();
					String cycle = "";
					if (null != myTaskPageVO.getCurrentCycle()
							&& !"".equalsIgnoreCase(myTaskPageVO.getCurrentCycle())) {
						cycle = myTaskPageVO.getCurrentCycle();
					} else {
						cycle = "-";
					}

					if (null != myTaskPageVO.getTeamTypeList()
							&& myTaskPageVO.getTeamTypeList().equalsIgnoreCase(teamName)) {
						if (null == map.get(cycle)) {
							this.logger.debug("process cycle is first time " + myTaskPageVO.getTaskName());
							if (null != myTaskPageVO.getTaskName()
									&& !"Invoicing Task".equalsIgnoreCase(myTaskPageVO.getTaskName())) {
								map.put(cycle, myTaskPageVO.getTaskName());
							}
						} else {
							this.logger.debug("process cycle is next time " + myTaskPageVO.getTaskName());
							if (null != myTaskPageVO.getTaskName()
									&& !"Invoicing Task".equalsIgnoreCase(myTaskPageVO.getTaskName())) {
								map.put(cycle,
										((String) map.get(cycle)).concat(",").concat(myTaskPageVO.getTaskName()));
							}
						}
					} else if (null == myTaskPageVO.getTeamTypeList() && teamName.contains("Primary")) {
						if (null == map.get(cycle)) {
							this.logger.debug("process cycle primary is first time " + myTaskPageVO.getTaskName());
							if (null != myTaskPageVO.getTaskName()
									&& !"Invoicing Task".equalsIgnoreCase(myTaskPageVO.getTaskName())) {
								map.put(cycle, myTaskPageVO.getTaskName());
							}
						} else {
							this.logger.debug("process cycle primary is next time " + myTaskPageVO.getTaskName());
							if (null != myTaskPageVO.getTaskName()
									&& !"Invoicing Task".equalsIgnoreCase(myTaskPageVO.getTaskName())) {
								map.put(cycle,
										((String) map.get(cycle)).concat(",").concat(myTaskPageVO.getTaskName()));
							}
						}
					}
				}

				Set<String> set = map.keySet();
				List<MyTaskPageVO> taskList = new ArrayList();

				MyTaskPageVO myTaskPageVO;
				for (Iterator iterator = set.iterator(); iterator.hasNext(); taskList.add(myTaskPageVO)) {
					myTaskPageVO = new MyTaskPageVO();
					String key = (String) iterator.next();
					myTaskPageVO.setCurrentCycle(key);
					myTaskPageVO.setTaskName((String) map.get(key));
					if (key.equalsIgnoreCase("interim1")) {
						myTaskPageVO.setPid(new Long(1L));
					} else if (key.equalsIgnoreCase("interim2")) {
						myTaskPageVO.setPid(new Long(2L));
					} else {
						myTaskPageVO.setPid(new Long(3L));
					}
				}

				return taskList;
			}
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}
	}

	public List<MyTaskPageVO> getExportTeamSearchProcessData(List<String> crnNo) throws CMSException {
		Object taskList = new ArrayList();

		try {
			if (crnNo.size() < 1000 && crnNo.size() > 0) {
				taskList = ResourceLocator.self().getSBMService().getAllTaskForCrnList(crnNo);
			} else {
				int k = 0;

				while (k < crnNo.size()) {
					int start = k;
					int end = k + 1000;
					this.logger.debug("start is before " + k + " end is " + end);
					if (end >= crnNo.size()) {
						end = crnNo.size() + 1;
					}

					this.logger.debug("k is " + k + " end is " + end);
					this.logger.debug("start is after " + k + " end is " + end);
					List<MyTaskPageVO> tempTaskList = ResourceLocator.self().getSBMService()
							.getAllTaskForCrnList(crnNo.subList(k, end - 1));
					k += 999;
					this.logger.debug("start is after k updated " + start + " end is " + end);
					((List) taskList).addAll(tempTaskList);
				}
			}

			return (List) taskList;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}
}