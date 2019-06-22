package com.worldcheck.atlas.dao.masters;

import com.integrascreen.orders.REMasterVO;
import com.integrascreen.orders.UPMasterVO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.isis.vo.ISISResponseVO;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.masters.ResearchElementGroupMasterVO;
import com.worldcheck.atlas.vo.masters.ResearchElementMasterVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;
import org.json.simple.JSONValue;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class REMultiActionDAO extends SqlMapClientTemplate {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.masters.REMultiActionDAO");
	private String USER_NAME = "userName";
	private String RE_MASTER_ID = "rEMasterId";
	private String RE_NAME_VAL = "reNameval";
	private String RE_CODE_VAL = "reCodeval";
	private String RE_STATUS_VAL = "reStatusval";
	private String RE_ENTITYTYPE_ID_VAL = "reEntityTypeIdval";
	private String RE_REMOVE_ENGLISH = "reRemEng";
	private String RE_BI_TEAM = "reBITeam";
	private String RE_SUPVEN_TEAM = "reSubVenTeam";
	private String RE_POINT_VAL = "rePointsval";
	private String RE_NAME = "reName";
	private String RE_ID = "reId";
	private String SUBJECT_TYPE = "subjectType";
	private String INDIVIDUAL = "Individual";
	private String COMPANY = "Company";
	private String REMASTER_GET_REGROUP_ID_SQL_ID = "REMaster.getGroupId";
	private String REMASTER_ADD_GROUPRE_SQL_ID = "REMaster.addNewREGroup";
	private String REMASTER_GET_REINFO_SQL_ID = "REMaster.getREInfo";
	private String REMASTER_ADD_RE_SQL_ID = "REMaster.addNewResearchElement";
	private String REMASTER_STATUSUPDATE_SQL_ID = "REMaster.statusUpdate";
	private String REMASTER_ISSUBJECTTYPERE_UNIQUE_SQL_ID = "REMaster.isSubjectTypeREUnique";
	private String REMASTER_GET_GROUP_INFO_SQL_ID = "REMaster.getGroupInfo";
	private String REMASTER_ISGROUPNAME_UNIQUE_SQL_ID = "REMaster.isGroupNameUnique";
	private String REMASTER_UPDATERE_SQL_ID = "REMaster.updateRE";
	private String REMASTER_UPDATE_GROUPRE_SQL_ID = "REMaster.updateGroupRE";
	private String REMASTER_SEARCH_SQL_ID = "REMaster.searchRE";
	private String REMASTER_SEARCH_COUNT_SQL_ID = "REMaster.searchRECount";
	private String REMASTER_UPDATE_CASE_REPOINTS_SQL_ID = "REMaster.updateCaseREPoints";
	private String REMASTER_UPDATE_CASE_GRPREPOINTS_SQL_ID = "REMaster.updateCaseGrpREPoints";
	private String REMASTER_CAN_RES_DEACTIVATED_SQL_ID = "REMaster.canResDeactivated";
	private String GET_RE_INFO_SQL_ID = "REMaster.getReInfoDetails";
	private String REMASTER_ISWIPCASEIMPACTED_SQL_ID = "REMaster.isWIPCaseImpacted";
	private String REMASTER_WIPIMPACTEDCASECOUNT_SQL_ID = "REMaster.wipImpactedCaseCount";
	private String DELETE_RE_DETAILS_SQL_ID = "REMaster.deleteREDetails";
	private String RE_MASTER_EXPORT_TO_EXCEL = "REMaster.exportToXl_RE";

	public List<ResearchElementMasterVO> searchRE(ResearchElementMasterVO researchElementMasterVO) throws CMSException {
		this.logger.debug("Inside the Dao for searchRE");

		try {
			return this.queryForList(this.REMASTER_SEARCH_SQL_ID, researchElementMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int searchRECount(ResearchElementMasterVO researchElementMasterVO) throws CMSException {
		this.logger.debug("Inside the Dao for searchRECount");

		try {
			return (Integer) this.queryForObject(this.REMASTER_SEARCH_COUNT_SQL_ID, researchElementMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public String updateREStatus(String rEMasterId, int statusVal, String userName) throws CMSException {
		String message = "";

		try {
			int count = false;
			String status = "";
			if (statusVal == 0) {
				status = "D";
			} else {
				status = "A";
			}

			this.logger.debug("inside updateREStatus DAO :: reMasterId :: " + rEMasterId + " ::  statusVal :: "
					+ statusVal + " :: userName :: " + userName);
			List<ResearchElementMasterVO> reList = this.queryForList(this.GET_RE_INFO_SQL_ID, rEMasterId);
			ResearchElementMasterVO vo = null;

			for (int i = 0; i < reList.size(); ++i) {
				message = "";
				vo = (ResearchElementMasterVO) reList.get(i);

				try {
					UPMasterVO upMasterVO = new UPMasterVO();
					upMasterVO.setMaster("RE");
					upMasterVO.setUpdateType("Update");
					REMasterVO masterObject = new REMasterVO();
					this.logger.debug("Re Code/id :: " + vo.getrEMasterId() + " :: RE name :: "
							+ vo.getResearchElementName() + " Subject Type :: " + vo.getReEntityTypeId() + " Status :: "
							+ vo.getResearchElementStatus());
					masterObject.setCode(vo.getrEMasterId() + "");
					masterObject.setDescription(vo.getResearchElementName());
					masterObject.setSubjectType(vo.getReEntityTypeId());
					masterObject.setStatus(status);
					upMasterVO.setREMaster(masterObject);
					ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient()
							.updateMaster(upMasterVO);
					if (!isisResponseVO.isSuccess()) {
						(new StringBuilder()).append(isisResponseVO.getResponseVO().getMessage()).append(" for ")
								.append(vo.getResearchElementName()).toString();
						message = isisResponseVO.getResponseVO().getMessage();
						break;
					}

					message = "success";
				} catch (CMSException var13) {
					throw new CMSException(this.logger, var13);
				}
			}

			if (message.trim().length() > 0 && message.equals("success")) {
				HashMap<String, Object> paramMap = new HashMap();
				paramMap.put(this.RE_MASTER_ID, rEMasterId);
				paramMap.put(this.USER_NAME, userName);
				paramMap.put(this.RE_STATUS_VAL, statusVal);
				int count = this.update(this.REMASTER_STATUSUPDATE_SQL_ID, paramMap);
				this.logger.debug("update count :: " + count);
			} else {
				this.logger.debug("not updated as failure at ISIS :: " + message);
			}

			return message;
		} catch (DataAccessException var14) {
			throw new CMSException(this.logger, var14);
		}
	}

	public int addResearchElement(ResearchElementMasterVO researchElementMasterVO) throws CMSException {
		try {
			return (Integer) this.insert(this.REMASTER_ADD_RE_SQL_ID, researchElementMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public ResearchElementMasterVO getREInfo(ResearchElementMasterVO researchElementMasterVO) throws CMSException {
		try {
			this.logger.debug("in  getREInfo DAO");
			return (ResearchElementMasterVO) this.queryForObject(this.REMASTER_GET_REINFO_SQL_ID,
					researchElementMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int addResearchElementGroup(ResearchElementMasterVO researchElementMasterVO) throws CMSException {
		try {
			return (Integer) this.insert(this.REMASTER_ADD_GROUPRE_SQL_ID, researchElementMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int getGroupId(String groupName) throws CMSException {
		try {
			return (Integer) this.queryForObject(this.REMASTER_GET_REGROUP_ID_SQL_ID, groupName);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public String updateRE(ResearchElementMasterVO researchElementMasterVO, int groupId) throws CMSException {
		this.logger.debug("in updateRE in DAO");
		String message = "";

		try {
			int count = false;
			int updateCount = false;
			int updateCount;
			if (Integer.parseInt(researchElementMasterVO.getResearchElementType()) == 0 && groupId != 0) {
				this.logger.debug("group RE .. groupId :: " + groupId);
				String[] modifiedRecords = researchElementMasterVO.getModifiedRecords();

				for (int i = 0; i < modifiedRecords.length; ++i) {
					message = "";
					String JSONstring = modifiedRecords[i];
					Map jsonObject = (Map) JSONValue.parse(JSONstring);
					researchElementMasterVO.setResearchElementName((String) jsonObject.get(this.RE_NAME_VAL));
					researchElementMasterVO.setReGroupId(groupId);
					this.logger.debug("re name :: " + (String) jsonObject.get(this.RE_NAME_VAL) + " :: re points :: "
							+ Float.parseFloat(jsonObject.get(this.RE_POINT_VAL).toString()));
					researchElementMasterVO.setPointval(Float.parseFloat(jsonObject.get(this.RE_POINT_VAL).toString()));
					researchElementMasterVO
							.setSupportingVendorTeam((String) ((String) jsonObject.get(this.RE_SUPVEN_TEAM)));
					this.logger.debug("re supvenTeam :: " + (String) ((String) jsonObject.get(this.RE_SUPVEN_TEAM))
							+ " :: re BITeam :: " + (String) jsonObject.get(this.RE_BI_TEAM));
					researchElementMasterVO.setBiTeam((String) jsonObject.get(this.RE_BI_TEAM));
					researchElementMasterVO
							.setRemoveForEnglishCountry((String) ((String) jsonObject.get(this.RE_REMOVE_ENGLISH)));
					researchElementMasterVO.setReEntityTypeId(jsonObject.get(this.RE_ENTITYTYPE_ID_VAL).toString());
					this.logger.debug("re code val :: " + jsonObject.get(this.RE_CODE_VAL) + " :: status val :: "
							+ jsonObject.get(this.RE_STATUS_VAL) + " :: "
							+ researchElementMasterVO.getResearchElementStatus());
					researchElementMasterVO.setResearchElementStatus(jsonObject.get(this.RE_STATUS_VAL).toString());
					researchElementMasterVO.setIsIndividual(0);
					UPMasterVO upMasterVO;
					REMasterVO masterObject;
					ISISResponseVO isisResponseVO;
					if (null != jsonObject.get(this.RE_CODE_VAL)
							&& ((String) jsonObject.get(this.RE_CODE_VAL)).trim().length() > 0) {
						researchElementMasterVO.setResearchElementTypeId(jsonObject.get(this.RE_CODE_VAL).toString());

						try {
							upMasterVO = new UPMasterVO();
							upMasterVO.setMaster("RE");
							upMasterVO.setUpdateType("Update");
							masterObject = new REMasterVO();
							this.logger.debug("Re Code/id :: " + researchElementMasterVO.getResearchElementTypeId()
									+ " :: RE name :: " + researchElementMasterVO.getResearchElementName()
									+ " Subject Type :: " + researchElementMasterVO.getReEntityTypeId() + " Status :: "
									+ researchElementMasterVO.getResearchElementStatus());
							masterObject.setCode(researchElementMasterVO.getResearchElementTypeId());
							masterObject.setDescription(researchElementMasterVO.getResearchElementName());
							masterObject.setSubjectType(researchElementMasterVO.getReEntityTypeId());
							if (Integer.parseInt(researchElementMasterVO.getResearchElementStatus()) == 1) {
								masterObject.setStatus("A");
							} else {
								masterObject.setStatus("D");
							}

							upMasterVO.setREMaster(masterObject);
							isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
							if (isisResponseVO.isSuccess()) {
								this.update(this.REMASTER_UPDATE_GROUPRE_SQL_ID, researchElementMasterVO);
								updateCount = this.update(this.REMASTER_UPDATE_CASE_GRPREPOINTS_SQL_ID,
										researchElementMasterVO);
								this.logger.debug("number of rows updated in CMS_SUB_TEAM_RE_MAP :: " + updateCount);
								message = "success";
							} else {
								message = isisResponseVO.getResponseVO().getMessage();
							}
						} catch (CMSException var14) {
							throw new CMSException(this.logger, var14);
						}
					} else {
						int count = (Integer) this.insert(this.REMASTER_ADD_GROUPRE_SQL_ID, researchElementMasterVO);

						try {
							upMasterVO = new UPMasterVO();
							upMasterVO.setMaster("RE");
							upMasterVO.setUpdateType("Insert");
							masterObject = new REMasterVO();
							this.logger.debug("Re Code/id :: " + count + " :: RE name :: "
									+ researchElementMasterVO.getResearchElementName() + " Subject Type :: "
									+ researchElementMasterVO.getReEntityTypeId() + " Status :: "
									+ researchElementMasterVO.getResearchElementStatus());
							masterObject.setCode(count + "");
							masterObject.setDescription(researchElementMasterVO.getResearchElementName());
							masterObject.setSubjectType(researchElementMasterVO.getReEntityTypeId());
							masterObject.setStatus("A");
							upMasterVO.setREMaster(masterObject);
							isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient().updateMaster(upMasterVO);
							if (!isisResponseVO.isSuccess()) {
								this.deleteREDetails(count + "");
								message = isisResponseVO.getResponseVO().getMessage();
								break;
							}

							message = "success";
						} catch (CMSException var15) {
							throw new CMSException(this.logger, var15);
						}
					}
				}
			} else {
				this.logger.debug("individual RE : getPoints :" + researchElementMasterVO.getPoints());
				researchElementMasterVO.setReGroupId(0);
				researchElementMasterVO.setResearchElementGroupName((String) null);

				try {
					UPMasterVO upMasterVO = new UPMasterVO();
					upMasterVO.setMaster("RE");
					upMasterVO.setUpdateType("Update");
					REMasterVO masterObject = new REMasterVO();
					this.logger.debug("Re Code/id :: " + researchElementMasterVO.getResearchElementTypeId()
							+ " :: RE name :: " + researchElementMasterVO.getResearchElementName() + " Subject Type :: "
							+ researchElementMasterVO.getReEntityTypeId() + " Status :: "
							+ researchElementMasterVO.getResearchElementStatus());
					masterObject.setCode(researchElementMasterVO.getResearchElementTypeId());
					masterObject.setDescription(researchElementMasterVO.getResearchElementName());
					masterObject.setSubjectType(researchElementMasterVO.getReEntityTypeId());
					if (Integer.parseInt(researchElementMasterVO.getResearchElementStatus()) == 1) {
						masterObject.setStatus("A");
					} else {
						masterObject.setStatus("D");
					}

					upMasterVO.setREMaster(masterObject);
					ISISResponseVO isisResponseVO = ResourceLocator.self().getAtlasWebServiceClient()
							.updateMaster(upMasterVO);
					if (isisResponseVO.isSuccess()) {
						this.update(this.REMASTER_UPDATERE_SQL_ID, researchElementMasterVO);
						updateCount = this.update(this.REMASTER_UPDATE_CASE_REPOINTS_SQL_ID, researchElementMasterVO);
						this.logger.debug("number of rows updated in CMS_SUB_TEAM_RE_MAP :: " + updateCount);
						message = "success";
					} else {
						message = isisResponseVO.getResponseVO().getMessage();
					}
				} catch (CMSException var13) {
					throw new CMSException(this.logger, var13);
				}
			}

			if (message.trim().length() > 0 && message.equals("success")) {
				this.logger.debug("Updated successfully");
			} else {
				this.logger.debug("Failure as ISIS side :: " + message);
			}

			return message;
		} catch (DataAccessException var16) {
			throw new CMSException(this.logger, var16);
		} catch (Exception var17) {
			throw new CMSException(this.logger, var17);
		}
	}

	public List<ResearchElementGroupMasterVO> getGroupInfo(ResearchElementMasterVO researchElementMasterVO)
			throws CMSException {
		try {
			this.logger.debug("Inside getGroupInfo DAO :: " + researchElementMasterVO.getrEMasterId());
			return this.queryForList(this.REMASTER_GET_GROUP_INFO_SQL_ID, researchElementMasterVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int isGroupNameUnique(HashMap<String, Object> paramMap) throws CMSException {
		try {
			return Integer.parseInt(this.queryForObject(this.REMASTER_ISGROUPNAME_UNIQUE_SQL_ID, paramMap).toString());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (NumberFormatException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int canREsDeactivated(String reIds) throws CMSException {
		try {
			return Integer.parseInt(this.queryForObject(this.REMASTER_CAN_RES_DEACTIVATED_SQL_ID, reIds).toString());
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (NumberFormatException var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public String isSubjectTypeREUnique(String reInfo) throws CMSException {
		try {
			String duplicateReInfo = "";
			HashMap<String, Object> paramMap = null;
			if (null != reInfo && reInfo.trim().length() > 0) {
				String[] reInfoArray = reInfo.split(",");

				for (int i = 0; i < reInfoArray.length; ++i) {
					paramMap = new HashMap();
					this.logger.debug("reInfoArray[i] :: " + reInfoArray[i]);
					String temp = reInfoArray[i].replaceAll("##&&", ",");
					this.logger.debug("temp :: " + temp);
					String[] reArray = temp.split("::");
					paramMap.put(this.SUBJECT_TYPE, reArray[0]);
					paramMap.put(this.RE_NAME, reArray[1]);
					if (reArray.length == 3) {
						paramMap.put(this.RE_ID, reArray[2]);
					} else {
						paramMap.put(this.RE_ID, "");
					}

					int count = Integer.parseInt(
							this.queryForObject(this.REMASTER_ISSUBJECTTYPERE_UNIQUE_SQL_ID, paramMap).toString());
					this.logger.debug("count :: " + count);
					if (count > 0) {
						if (Integer.parseInt(reArray[0]) == 1) {
							reArray[0] = this.INDIVIDUAL;
						} else {
							reArray[0] = this.COMPANY;
						}

						duplicateReInfo = duplicateReInfo + reArray[1] + " (" + reArray[0] + ") " + ",";
					}
				}

				if (duplicateReInfo.trim().length() > 0) {
					duplicateReInfo = duplicateReInfo.substring(0, duplicateReInfo.length() - 1);
				}

				this.logger.debug("duplicateReInfo :: " + duplicateReInfo);
			}

			return duplicateReInfo;
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (NumberFormatException var10) {
			throw new CMSException(this.logger, var10);
		} catch (PatternSyntaxException var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public String isWIPCaseImpacted(String reInfo) throws CMSException {
		try {
			String message = "";
			String reIds = "";
			HashMap<String, Object> paramMap = null;
			if (null != reInfo && reInfo.trim().length() > 0) {
				String[] reInfoArray = reInfo.split(",");

				int count;
				for (count = 0; count < reInfoArray.length; ++count) {
					paramMap = new HashMap();
					this.logger.debug("reInfoArray[i] :: " + reInfoArray[count]);
					String[] reArray = reInfoArray[count].split("::");
					if (reArray.length == 2) {
						paramMap.put(this.RE_BI_TEAM, reArray[0]);
						paramMap.put(this.RE_ID, reArray[1]);
						int count = Integer.parseInt(
								this.queryForObject(this.REMASTER_ISWIPCASEIMPACTED_SQL_ID, paramMap).toString());
						this.logger.debug("count :: " + count);
						if (count == 0) {
							reIds = reIds + reArray[1] + ",";
						}
					}
				}

				if (reIds.trim().length() > 0) {
					reIds = reIds.substring(0, reIds.length() - 1);
					count = Integer
							.parseInt(this.queryForObject(this.REMASTER_WIPIMPACTEDCASECOUNT_SQL_ID, reIds).toString());
					if (count > 0) {
						message = "success";
					}
				}

				this.logger.debug("message :: " + message);
			}

			return message;
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (NumberFormatException var10) {
			throw new CMSException(this.logger, var10);
		} catch (PatternSyntaxException var11) {
			throw new CMSException(this.logger, var11);
		}
	}

	public int deleteREDetails(String reId) throws CMSException {
		try {
			return this.delete(this.DELETE_RE_DETAILS_SQL_ID, reId);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public List<ResearchElementMasterVO> getReport(Map<String, Object> excelParamMap) throws CMSException {
		List mv = null;

		try {
			mv = this.queryForList(this.RE_MASTER_EXPORT_TO_EXCEL, excelParamMap);
			return mv;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}
}