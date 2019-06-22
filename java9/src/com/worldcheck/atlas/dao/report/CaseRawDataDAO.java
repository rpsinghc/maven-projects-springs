package com.worldcheck.atlas.dao.report;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.MaterializedViewVO;
import com.worldcheck.atlas.vo.masters.ReportTypeMasterVO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import com.worldcheck.atlas.vo.report.CaseRawTableVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class CaseRawDataDAO extends SqlMapClientTemplate {
	private static final String CASE_RAW_DATA_TEMPLATE_GET_INFO_LIST_CASE_RAW_DATA_TEMPLATE = "CaseRawDataTemplate.getInfoListCaseRawDataTemplate";
	private static final String CASE_RAW_DATA_TEMPLATE_SEARCH_EXIST_TEMPLATE_NAME = "CaseRawDataTemplate.searchExistTemplateName";
	private static final String GET_ALL_TEMPLATE_CREATOR = "CaseRawDataTemplate.getAllTemplateCreator";
	private static final String SEARCH_CASE_RAW_DATA = "CaseRawDataTemplate.searchCaseRawDataGridList";
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.dao.report.CaseRawDataDAO");

	public List<CaseRawTableVO> getCaseRawDataGrid(CaseRawTableVO caseRawTableVO) throws CMSException {
		try {
			new ArrayList();
			List<CaseRawTableVO> caseRawDataGridList = this
					.queryForList("CaseRawDataTemplate.searchCaseRawDataGridList", caseRawTableVO);
			this.logger.debug("CaseRawTableVO size :" + caseRawDataGridList.size());
			return caseRawDataGridList;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public int getCaseRawDataGridCount(CaseRawTableVO caseRawTableVO) throws CMSException {
		try {
			return (Integer) this.queryForObject("CaseRawDataTemplate.searchCaseRawDataGridListCount", caseRawTableVO);
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public List<UserMasterVO> getAllTemplateCreator() throws CMSException {
		this.logger.debug("In CaseRawDataDAO : getAllTemplateCreator");
		new ArrayList();

		try {
			List<UserMasterVO> templateCreatorList = this.queryForList("CaseRawDataTemplate.getAllTemplateCreator");
			this.logger.debug("templateCreatorList:" + templateCreatorList.size());
			return templateCreatorList;
		} catch (DataAccessException var3) {
			throw new CMSException(this.logger, var3);
		} catch (Exception var4) {
			throw new CMSException(this.logger, var4);
		}
	}

	public int getCountExistTemplateName(String templateName) throws CMSException {
		try {
			int count = Integer.parseInt(
					this.queryForObject("CaseRawDataTemplate.searchExistTemplateName", templateName).toString());
			this.logger.debug("TemplateName: " + templateName + "\tGetCount :" + count);
			return count;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<CaseRawTableVO> getInfoListCaseRawDataTemplate() throws CMSException {
		try {
			List<CaseRawTableVO> infoList = this.queryForList("CaseRawDataTemplate.getInfoListCaseRawDataTemplate");
			return infoList;
		} catch (DataAccessException var2) {
			throw new CMSException(this.logger, var2);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public int saveCaseRawDataTemplate(CaseRawTableVO caseRawTableVO) throws CMSException {
		int caseRawDataTemplateId = false;
		int infoFieldMapId = false;
		int selectOptionMapId = false;
		this.logger.debug("inside caseRawDataDAO deleteCaseRawDataTemplate");
		String selectedInfoId = null;
		boolean var10 = false;

		try {
			int caseRawDataTemplateId = (Integer) this.insert("CaseRawDataTemplate.saveCaseRawDataTemplate",
					caseRawTableVO);
			this.logger.debug("****caseRawDataTemplateId::" + caseRawDataTemplateId);
			if (!caseRawTableVO.getSelectedInfo().contains(",")) {
				caseRawTableVO.setSelectedInfo(caseRawTableVO.getSelectedInfo() + ",");
			}

			if (!caseRawTableVO.getSelectedOption().contains(",")) {
				caseRawTableVO.setSelectedOption(caseRawTableVO.getSelectedOption() + ",");
			}

			String[] info_arr = caseRawTableVO.getSelectedInfo().split(",");
			String[] option_arr = caseRawTableVO.getSelectedOption().split(",");
			this.logger.debug("*****InfoOption::" + caseRawTableVO.getSelectedInfo());
			this.logger.debug("*****selectedOption::" + caseRawTableVO.getSelectedOption());
			int i = 0;
			String[] var19 = info_arr;
			int var18 = info_arr.length;

			for (int var17 = 0; var17 < var18; ++var17) {
				String string = var19[var17];
				++i;
				String infoName = string.split("#")[0];
				selectedInfoId = string.split("#")[1];
				HashMap<String, String> selInfoMap = new HashMap();
				selInfoMap.put("fieldId", selectedInfoId);
				selInfoMap.put("templateId", String.valueOf(caseRawDataTemplateId));
				selInfoMap.put("updated_by", caseRawTableVO.getUpdated_by());
				if (option_arr.length > 0) {
					selInfoMap.put("optionStatus", "1");
				} else {
					selInfoMap.put("optionStatus", "0");
				}

				this.logger.debug("##########fieldId:" + selectedInfoId + "templateId:" + caseRawDataTemplateId
						+ "updated_by:" + caseRawTableVO.getUpdated_by());
				this.logger.debug("\t . Insert Info .");
				this.logger.debug(i + " infoName::" + infoName + "\t infoId::" + selectedInfoId);
				int infoFieldMapId = (Integer) this.insert("CaseRawDataTemplate.save_Case_Raw_Data_Field_Map",
						selInfoMap);
				this.logger.debug("after insert infoFieldMapId::" + infoFieldMapId);
				if (caseRawTableVO.getSelectedInfo().contains(infoName)) {
					int j = 0;
					String[] var24 = option_arr;
					int var23 = option_arr.length;

					for (int var22 = 0; var22 < var23; ++var22) {
						String string2 = var24[var22];
						if (string2.contains(infoName)) {
							++j;
							String selectedOptionName = string2.split("#")[0];
							String selectedOptionId = string2.split("#")[1];
							this.logger.debug(j + " selectedOptionName::" + selectedOptionName + "\t selectedOptionId::"
									+ selectedOptionId);
							HashMap<String, String> selOptionMap = new HashMap();
							selOptionMap.put("fieldId", String.valueOf(infoFieldMapId));
							selOptionMap.put("seletedOptionId", selectedOptionId);
							selOptionMap.put("updated_by", caseRawTableVO.getUpdated_by());
							int selectionMapId = (Integer) this.insert("CaseRawDataTemplate.save_CASE_RAW_OPTION_INFO",
									selOptionMap);
							this.logger.debug("optionId from cms_case_option_info::" + selectionMapId);
						}
					}
				}
			}

			return caseRawDataTemplateId;
		} catch (DataAccessException var28) {
			throw new CMSException(this.logger, var28);
		} catch (Exception var29) {
			throw new CMSException(this.logger, var29);
		}
	}

	public int deleteCaseRawDataTemplate(long caseRawDataTemplateId) throws CMSException {
		this.logger.debug("inside caseRawDataDAO deleteCaseRawDataTemplate");
		this.logger.debug("caseRawDataTemplateId:" + caseRawDataTemplateId);

		try {
			int deletedRow = Integer
					.valueOf(this.delete("CaseRawDataTemplate.deleteCaseRawDataTemplate", caseRawDataTemplateId));
			return deletedRow;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public CaseRawTableVO getCaseRawDataTemplateInfo(long caseRawDataTemplateId) throws CMSException {
		this.logger.debug("inside caseRawDataDAO getCaseRawDataTemplateInfo");
		this.logger.debug("caseRawDataTemplateId:" + caseRawDataTemplateId);

		try {
			this.getSelectedInfoFieldsListCaseRawDataTemplate(caseRawDataTemplateId);
			CaseRawTableVO caseRawTableVO = new CaseRawTableVO();
			return caseRawTableVO;
		} catch (DataAccessException var5) {
			throw new CMSException(this.logger, var5);
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public int updateCaseRawDataTemplate(CaseRawTableVO caseRawTableVO) throws CMSException {
		this.logger.debug("inside caseRawDataDAO updateCaseRawDataTemplate");
		this.logger.debug("caseRawTableVO:: Id" + caseRawTableVO.getTemplateId());

		try {
			int caseRawDataTemplateId = Integer
					.valueOf(this.update("CaseRawDataTemplate.updateCaseRawDataTemplate", caseRawTableVO));
			return caseRawDataTemplateId;
		} catch (DataAccessException var4) {
			throw new CMSException(this.logger, var4);
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public List<ReportTypeMasterVO> getSubReportList() throws CMSException {
		try {
			List<ReportTypeMasterVO> subReportTypes = this.queryForList("CaseRawDataTemplate.getSubReportTypesList");
			this.logger.debug("after dao call::" + subReportTypes.size());
			return subReportTypes;
		} catch (DataAccessException var2) {
			throw new CMSException(this.logger, var2);
		} catch (Exception var3) {
			throw new CMSException(this.logger, var3);
		}
	}

	public long setDefaultCaseRawDataTemplate(long caseRawDataTemplateId, long status, String updated_by)
			throws CMSException {
		this.logger.debug("inside caseRawDataDAO setDefaultCaseRawDataTemplate");
		this.logger.debug("caseRawDataTemplateId:" + caseRawDataTemplateId);
		HashMap setDefaultMap = null;

		int updatedRow;
		try {
			setDefaultMap = new HashMap();
			setDefaultMap.put("caseRawDataTemplateId", String.valueOf(caseRawDataTemplateId));
			setDefaultMap.put("status", String.valueOf(status));
			setDefaultMap.put("updated_by", updated_by);
			updatedRow = Integer
					.valueOf(this.update("CaseRawDataTemplate.setDefaultCaseRawDataTemplate", setDefaultMap));
		} catch (DataAccessException var9) {
			throw new CMSException(this.logger, var9);
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}

		return (long) updatedRow;
	}

	public List<CaseRawTableVO> getSelectedInfoFieldsListCaseRawDataTemplate(long caseRawDataTemplateId)
			throws CMSException {
		try {
			List<CaseRawTableVO> infoList = this.queryForList(
					"CaseRawDataTemplate.get_Selected_Info_Fields_ListCaseRawDataTemplate", caseRawDataTemplateId);
			this.logger.debug("************start************");
			String selectedInfo = "";
			String selectedOption = "";
			Iterator var7 = infoList.iterator();

			while (true) {
				List sel;
				do {
					do {
						CaseRawTableVO caseRawTableVO;
						if (!var7.hasNext()) {
							this.logger.debug("************End************");
							this.logger.debug("Selected Info::" + selectedInfo);
							this.logger.debug("Selected Option::" + selectedOption);
							caseRawTableVO = new CaseRawTableVO();
							caseRawTableVO.setSelectedInfo(selectedInfo);
							caseRawTableVO.setSelectedOption(selectedOption);
							infoList.add(caseRawTableVO);
							return infoList;
						}

						caseRawTableVO = (CaseRawTableVO) var7.next();
						this.logger.debug("SelectedField:" + caseRawTableVO.getSelectedInfo() + "selectedInfoId:"
								+ caseRawTableVO.getSelectedInfoId() + "\t FieldMapId:"
								+ caseRawTableVO.getFieldMapId());
						this.logger.debug(caseRawTableVO.getSelectedInfo() + "#" + caseRawTableVO.getSelectedInfoId());
						if ("".equals(selectedInfo)) {
							selectedInfo = caseRawTableVO.getSelectedInfo() + "#" + caseRawTableVO.getSelectedInfoId();
						} else {
							selectedInfo = selectedInfo + "," + caseRawTableVO.getSelectedInfo() + "#"
									+ caseRawTableVO.getSelectedInfoId();
						}

						sel = this.getSelectedOption(caseRawTableVO.getSelectedInfo(),
								Long.parseLong(caseRawTableVO.getFieldMapId()));
					} while (sel == null);
				} while (sel.size() == 0);

				Iterator var10 = sel.iterator();

				while (var10.hasNext()) {
					CaseRawTableVO caseRawTableVO2 = (CaseRawTableVO) var10.next();
					this.logger.debug(caseRawTableVO2.getSearchType() + "#" + caseRawTableVO2.getFieldMapId() + "#"
							+ caseRawTableVO2.getSelectedOption());
					if ("".equals(selectedOption)) {
						selectedOption = caseRawTableVO2.getSearchType() + "#" + caseRawTableVO2.getFieldMapId() + "#"
								+ caseRawTableVO2.getSelectedOption();
					} else {
						selectedOption = selectedOption + "," + caseRawTableVO2.getSearchType() + "#"
								+ caseRawTableVO2.getFieldMapId() + "#" + caseRawTableVO2.getSelectedOption();
					}
				}
			}
		} catch (DataAccessException var11) {
			throw new CMSException(this.logger, var11);
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	public List<CaseRawTableVO> getSelectedOption(String listType, long selectedInfoId) throws CMSException {
		List optionList = null;

		try {
			if (listType.equals("REPORT_TYPE")) {
				optionList = this.queryForList("CaseRawDataTemplate.get_Selected_Option_Fields_List_FOR_REPORT_TYPE",
						selectedInfoId);
			} else if (!listType.equals("PRIMARY_TEAM_OFFICE") && !listType.equals("SUPPORTING_1_TEAM_OFFICE")
					&& !listType.equals("SUPPORTING_2_TEAM_OFFICE") && !listType.equals("SUPPORTING_3_TEAM_OFFICE")) {
				if (listType.equals("CASE_MANAGER")) {
					optionList = this.queryForList(
							"CaseRawDataTemplate.get_Selected_Option_Fields_List_FOR_CASEMANAGER", selectedInfoId);
				} else if (listType.equals("CLIENT_CODE")) {
					optionList = this.queryForList("CaseRawDataTemplate.get_Selected_Option_Fields_List_FOR_CLIENTCODE",
							selectedInfoId);
				} else if (listType.equals("CASE_STATUS")) {
					optionList = this.queryForList("CaseRawDataTemplate.get_Selected_Option_Fields_List_FOR_CASESTATUS",
							selectedInfoId);
				} else if (listType.equals("SUB_REPORT_TYPE")) {
					optionList = this.queryForList(
							"CaseRawDataTemplate.get_Selected_Option_Fields_List_FOR_SUB_REPORT_TYPE", selectedInfoId);
				}
			} else {
				optionList = this.queryForList("CaseRawDataTemplate.get_Selected_Option_Fields_List_FOR_OFFICE",
						selectedInfoId);
			}

			return optionList;
		} catch (DataAccessException var6) {
			throw new CMSException(this.logger, var6);
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public boolean getSubjectButtonValue(CaseRawTableVO excelFilter) throws CMSException {
		boolean flag = false;

		try {
			List<String> columnNameList = excelFilter.getFieldName();
			List<String> columnList = new ArrayList();
			columnList.add("OTHER_SUBJECTS");
			columnList.add("OTHER_SUBJECT_COUNTRIES");

			for (int i = 0; i < columnNameList.size(); ++i) {
				for (int j = 0; j < columnList.size(); ++j) {
					if (((String) columnList.get(j)).equals(columnNameList.get(i))) {
						flag = true;
					}
				}
			}

			return flag;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	public List<HashMap> getExportToExcelData(CaseRawTableVO excelFilter) throws CMSException {
		ArrayList optionList = new ArrayList();

		try {
			this.logger.debug("inside the caseRawDataDAO ::getExportToExcelData");
			String clientCodeQueryString = "";
			String caseManagerQueryString = "";
			boolean flag = false;
			String caseManagers = excelFilter.getSelectedCaseManager();
			String clientCodes = excelFilter.getSelectedClientCode();
			String subjectButtonName = excelFilter.getButtonName();
			List<String> cmList = new ArrayList();
			List<String> clientCodeList = new ArrayList();
			if (caseManagers != null && !caseManagers.isEmpty()) {
				cmList = StringUtils.commaSeparatedStringToList(caseManagers);
			}

			if (clientCodes != null && !clientCodes.isEmpty()) {
				clientCodeList = StringUtils.commaSeparatedStringToList(clientCodes);
			}

			int k;
			int k1;
			int end;
			if (((List) cmList).size() < 1000) {
				clientCodeQueryString = "";
				caseManagerQueryString = "";
				if (((List) cmList).size() > 0) {
					excelFilter.setSelectedCaseManager(
							"CASE_MANAGER NOT IN (" + excelFilter.getSelectedCaseManager() + ")");
				}

				if (((List) clientCodeList).size() < 1000) {
					this.logger.debug("Case Manager and Client Code both are less than 1000");
					this.logger.debug("client code set value in if:: " + excelFilter.getSelectedClientCode());
					if (((List) clientCodeList).size() > 0) {
						excelFilter.setSelectedClientCode(
								"CLIENT_CODE NOT IN (" + excelFilter.getSelectedClientCode() + ")");
					}

					if (subjectButtonName.equals("crdSubjectButton")) {
						flag = this.getSubjectButtonValue(excelFilter);
						if (flag) {
							optionList.addAll(
									this.queryForList("CaseRawDataTemplate.getExportToExcelSubjectData", excelFilter));
						} else {
							optionList
									.addAll(this.queryForList("CaseRawDataTemplate.getExportToExcelData", excelFilter));
						}
					}

					if (subjectButtonName.equals("crdExcelButton")) {
						optionList.addAll(this.queryForList("CaseRawDataTemplate.getExportToExcelData", excelFilter));
					}

					if (subjectButtonName.equals("crdJlpButton")) {
						optionList
								.addAll(this.queryForList("CaseRawDataTemplate.getExportToExcelJlpData", excelFilter));
					}
				} else {
					this.logger.debug("Case Manager is less than 1000 but client Code is > 1000");
					k = 0;
					this.logger.debug("client code set value in else: " + excelFilter.getSelectedClientCode());

					for (; k < ((List) clientCodeList).size(); this.logger
							.debug("Button Name in getExportToExcelData meyhod in Dao:::"
									+ excelFilter.getButtonName().toString())) {
						k1 = k;
						end = k + 1000;
						if (end >= ((List) clientCodeList).size()) {
							end = ((List) clientCodeList).size() + 1;
						}

						k += 999;
						this.logger.debug("after k updated k is :: " + k + " :: start is  " + k1 + " end is " + end);
						clientCodeQueryString = clientCodeQueryString + "CLIENT_CODE NOT IN (" + StringUtils
								.listToCommaSeparatedStringObject(((List) clientCodeList).subList(k1, end - 1)) + ")";
						if (k < ((List) clientCodeList).size()) {
							clientCodeQueryString = clientCodeQueryString + " AND ";
						}
					}

					excelFilter.setSelectedClientCode(clientCodeQueryString);
					this.logger.debug("client code query" + clientCodeQueryString);
					if (subjectButtonName.equals("crdSubjectButton")) {
						flag = this.getSubjectButtonValue(excelFilter);
						if (flag) {
							optionList.addAll(
									this.queryForList("CaseRawDataTemplate.getExportToExcelSubjectData", excelFilter));
						} else {
							optionList
									.addAll(this.queryForList("CaseRawDataTemplate.getExportToExcelData", excelFilter));
						}
					}

					if (subjectButtonName.equals("crdExcelButton")) {
						optionList.addAll(this.queryForList("CaseRawDataTemplate.getExportToExcelData", excelFilter));
					}

					if (subjectButtonName.equals("crdJlpButton")) {
						optionList
								.addAll(this.queryForList("CaseRawDataTemplate.getExportToExcelJlpData", excelFilter));
					}
				}
			} else {
				clientCodeQueryString = "";
				caseManagerQueryString = "";
				k = 0;

				while (k < ((List) cmList).size()) {
					k1 = k;
					end = k + 1000;
					if (end >= ((List) cmList).size()) {
						end = ((List) cmList).size() + 1;
					}

					k += 999;
					this.logger.debug("after k updated k is :: " + k + " :: start is  " + k1 + " end is " + end);
					caseManagerQueryString = caseManagerQueryString + "CASE_MANAGER NOT IN ("
							+ StringUtils.listToCommaSeparatedStringObject(((List) cmList).subList(k1, end - 1)) + ")";
					if (k < ((List) cmList).size()) {
						caseManagerQueryString = caseManagerQueryString + " AND ";
					}
				}

				excelFilter.setSelectedCaseManager(caseManagerQueryString);
				if (((List) clientCodeList).size() < 1000) {
					if (((List) clientCodeList).size() > 0) {
						excelFilter.setSelectedClientCode(
								"CLIENT_CODE NOT IN (" + excelFilter.getSelectedClientCode() + ")");
					}

					this.logger.debug("Case Manager > 1000 and Client Code is less than 1000");
					if (subjectButtonName.equals("crdSubjectButton")) {
						flag = this.getSubjectButtonValue(excelFilter);
						if (flag) {
							optionList.addAll(
									this.queryForList("CaseRawDataTemplate.getExportToExcelSubjectData", excelFilter));
						} else {
							optionList
									.addAll(this.queryForList("CaseRawDataTemplate.getExportToExcelData", excelFilter));
						}
					}

					if (subjectButtonName.equals("crdExcelButton")) {
						optionList.addAll(this.queryForList("CaseRawDataTemplate.getExportToExcelData", excelFilter));
					}

					if (subjectButtonName.equals("crdJlpButton")) {
						optionList
								.addAll(this.queryForList("CaseRawDataTemplate.getExportToExcelJlpData", excelFilter));
					}
				} else {
					this.logger.debug("Both Case Manager and client Code are > 1000");

					for (k1 = 0; k1 < ((List) clientCodeList).size(); this.logger
							.debug("Button Name in getExportToExcelData meyhod in Dao:::"
									+ excelFilter.getButtonName().toString())) {
						end = k1;
						int end1 = k1 + 1000;
						if (end1 >= ((List) clientCodeList).size()) {
							end1 = ((List) clientCodeList).size() + 1;
						}

						k1 += 999;
						this.logger.debug(
								"after k1 updated k1 is :: " + k1 + " :: start1 is  " + end + " end1 is " + end1);
						clientCodeQueryString = clientCodeQueryString + "CLIENT_CODE NOT IN (" + StringUtils
								.listToCommaSeparatedStringObject(((List) clientCodeList).subList(end, end1 - 1)) + ")";
						if (k1 < ((List) clientCodeList).size()) {
							clientCodeQueryString = clientCodeQueryString + " AND ";
						}
					}

					excelFilter.setSelectedClientCode(clientCodeQueryString);
					this.logger.debug("client code query in else" + clientCodeQueryString);
					if (subjectButtonName.equals("crdSubjectButton")) {
						flag = this.getSubjectButtonValue(excelFilter);
						if (flag) {
							optionList.addAll(
									this.queryForList("CaseRawDataTemplate.getExportToExcelSubjectData", excelFilter));
						} else {
							optionList
									.addAll(this.queryForList("CaseRawDataTemplate.getExportToExcelData", excelFilter));
						}
					}

					if (subjectButtonName.equals("crdExcelButton")) {
						optionList.addAll(this.queryForList("CaseRawDataTemplate.getExportToExcelData", excelFilter));
					}

					if (subjectButtonName.equals("crdJlpButton")) {
						optionList
								.addAll(this.queryForList("CaseRawDataTemplate.getExportToExcelJlpData", excelFilter));
					}
				}
			}

			return optionList;
		} catch (DataAccessException var15) {
			throw new CMSException(this.logger, var15);
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}
	}

	public int getExportToExcelDataCount(CaseRawTableVO excelFilter) throws CMSException {
		String clientCodeQueryString = "";
		String caseManagerQueryString = "";

		try {
			this.logger.debug("inside the caseRawDataDAO ::getExportToExcelDataCount");
			int resultCount = 0;
			boolean flag = false;
			String subjectButtonName = excelFilter.getButtonName();
			String caseManagers = excelFilter.getSelectedCaseManager();
			String clientCodes = excelFilter.getSelectedClientCode();
			List<String> cmList = new ArrayList();
			List<String> clientCodeList = new ArrayList();
			if (caseManagers != null && !caseManagers.isEmpty()) {
				cmList = StringUtils.commaSeparatedStringToList(caseManagers);
			}

			if (clientCodes != null && !clientCodes.isEmpty()) {
				clientCodeList = StringUtils.commaSeparatedStringToList(clientCodes);
			}

			this.logger.debug("Button Name in getExportToExcelData meyhod in Dao:::" + subjectButtonName);
			int k;
			int k1;
			int end;
			if (((List) cmList).size() < 1000) {
				clientCodeQueryString = "";
				caseManagerQueryString = "";
				if (((List) cmList).size() > 0) {
					excelFilter.setSelectedCaseManager(
							"CASE_MANAGER NOT IN (" + excelFilter.getSelectedCaseManager() + ")");
				}

				if (((List) clientCodeList).size() < 1000) {
					this.logger.debug("Case Manager and Client Code both are less than 1000");
					if (((List) clientCodeList).size() > 0) {
						excelFilter.setSelectedClientCode(
								"CLIENT_CODE NOT IN (" + excelFilter.getSelectedClientCode() + ")");
					}

					if (subjectButtonName.equals("crdSubjectButton")) {
						flag = this.getSubjectButtonValue(excelFilter);
						if (flag) {
							resultCount = (Integer) this.queryForObject(
									"CaseRawDataTemplate.getExportToExcelSubjectDataCount", excelFilter);
						} else {
							resultCount = (Integer) this.queryForObject("CaseRawDataTemplate.getExportToExcelDataCount",
									excelFilter);
						}
					}

					if (subjectButtonName.equals("crdExcelButton")) {
						resultCount = (Integer) this.queryForObject("CaseRawDataTemplate.getExportToExcelDataCount",
								excelFilter);
					}

					if (subjectButtonName.equals("crdJlpButton")) {
						resultCount = (Integer) this.queryForObject("CaseRawDataTemplate.getExportToExcelJlpDataCount",
								excelFilter);
					}
				} else {
					this.logger.debug("Case Manager is less than 1000 but client Code is > 1000");
					k = 0;

					while (k < ((List) clientCodeList).size()) {
						k1 = k;
						end = k + 1000;
						if (end >= ((List) clientCodeList).size()) {
							end = ((List) clientCodeList).size() + 1;
						}

						k += 999;
						this.logger.debug("after k updated k is :: " + k + " :: start is  " + k1 + " end is " + end);
						clientCodeQueryString = clientCodeQueryString + "CLIENT_CODE NOT IN (" + StringUtils
								.listToCommaSeparatedStringObject(((List) clientCodeList).subList(k1, end - 1)) + ")";
						if (k < ((List) clientCodeList).size()) {
							clientCodeQueryString = clientCodeQueryString + " AND ";
						}
					}

					excelFilter.setSelectedClientCode(clientCodeQueryString);
					if (subjectButtonName.equals("crdSubjectButton")) {
						flag = this.getSubjectButtonValue(excelFilter);
						if (flag) {
							resultCount = (Integer) this.queryForObject(
									"CaseRawDataTemplate.getExportToExcelSubjectDataCount", excelFilter);
						} else {
							resultCount = (Integer) this.queryForObject("CaseRawDataTemplate.getExportToExcelDataCount",
									excelFilter);
						}
					}

					if (subjectButtonName.equals("crdExcelButton")) {
						resultCount = (Integer) this.queryForObject("CaseRawDataTemplate.getExportToExcelDataCount",
								excelFilter);
					}

					if (subjectButtonName.equals("crdJlpButton")) {
						resultCount = (Integer) this.queryForObject("CaseRawDataTemplate.getExportToExcelJlpDataCount",
								excelFilter);
					}
				}
			} else {
				clientCodeQueryString = "";
				caseManagerQueryString = "";
				k = 0;

				while (k < ((List) cmList).size()) {
					k1 = k;
					end = k + 1000;
					if (end >= ((List) cmList).size()) {
						end = ((List) cmList).size() + 1;
					}

					k += 999;
					this.logger.debug("after k updated k is :: " + k + " :: start is  " + k1 + " end is " + end);
					caseManagerQueryString = caseManagerQueryString + "CASE_MANAGER NOT IN ("
							+ StringUtils.listToCommaSeparatedStringObject(((List) cmList).subList(k1, end - 1)) + ")";
					if (k < ((List) cmList).size()) {
						caseManagerQueryString = caseManagerQueryString + " AND ";
					}
				}

				excelFilter.setSelectedCaseManager(caseManagerQueryString);
				if (((List) clientCodeList).size() < 1000) {
					if (((List) clientCodeList).size() > 0) {
						excelFilter.setSelectedClientCode(
								"CLIENT_CODE NOT IN (" + excelFilter.getSelectedClientCode() + ")");
					}

					this.logger.debug("Case Manager > 1000 and Client Code is less than 1000");
					if (subjectButtonName.equals("crdSubjectButton")) {
						flag = this.getSubjectButtonValue(excelFilter);
						if (flag) {
							resultCount = (Integer) this.queryForObject(
									"CaseRawDataTemplate.getExportToExcelSubjectDataCount", excelFilter);
						} else {
							resultCount = (Integer) this.queryForObject("CaseRawDataTemplate.getExportToExcelDataCount",
									excelFilter);
						}
					}

					if (subjectButtonName.equals("crdExcelButton")) {
						resultCount = (Integer) this.queryForObject("CaseRawDataTemplate.getExportToExcelDataCount",
								excelFilter);
					}

					if (subjectButtonName.equals("crdJlpButton")) {
						resultCount = (Integer) this.queryForObject("CaseRawDataTemplate.getExportToExcelJlpDataCount",
								excelFilter);
					}
				} else {
					this.logger.debug("Both Case Manager and client Code are > 1000");
					k1 = 0;

					while (k1 < ((List) clientCodeList).size()) {
						end = k1;
						int end1 = k1 + 1000;
						if (end1 >= ((List) clientCodeList).size()) {
							end1 = ((List) clientCodeList).size() + 1;
						}

						k1 += 999;
						this.logger.debug(
								"after k1 updated k1 is :: " + k1 + " :: start1 is  " + end + " end1 is " + end1);
						clientCodeQueryString = clientCodeQueryString + "CLIENT_CODE NOT IN (" + StringUtils
								.listToCommaSeparatedStringObject(((List) clientCodeList).subList(end, end1 - 1)) + ")";
						if (k1 < ((List) clientCodeList).size()) {
							clientCodeQueryString = clientCodeQueryString + " AND ";
						}
					}

					excelFilter.setSelectedClientCode(clientCodeQueryString);
					if (subjectButtonName.equals("crdSubjectButton")) {
						flag = this.getSubjectButtonValue(excelFilter);
						if (flag) {
							resultCount = (Integer) this.queryForObject(
									"CaseRawDataTemplate.getExportToExcelSubjectDataCount", excelFilter);
						} else {
							resultCount = (Integer) this.queryForObject("CaseRawDataTemplate.getExportToExcelDataCount",
									excelFilter);
						}
					}

					if (subjectButtonName.equals("crdExcelButton")) {
						resultCount = (Integer) this.queryForObject("CaseRawDataTemplate.getExportToExcelDataCount",
								excelFilter);
					}

					if (subjectButtonName.equals("crdJlpButton")) {
						resultCount = (Integer) this.queryForObject("CaseRawDataTemplate.getExportToExcelJlpDataCount",
								excelFilter);
					}
				}
			}

			return resultCount;
		} catch (DataAccessException var15) {
			throw new CMSException(this.logger, var15);
		} catch (Exception var16) {
			throw new CMSException(this.logger, var16);
		}
	}

	public List<MaterializedViewVO> getMaterializedViewRefreshTime(String mvName) throws CMSException {
		try {
			List<String> tempList = new ArrayList();
			String[] tempArray = mvName.split(",");
			String[] var7 = tempArray;
			int var6 = tempArray.length;

			for (int var5 = 0; var5 < var6; ++var5) {
				String str = var7[var5];
				tempList.add(str);
			}

			MaterializedViewVO mv = new MaterializedViewVO();
			mv.setMvList(tempList);
			List<MaterializedViewVO> mvRefreshList = this
					.queryForList("CaseRawDataTemplate.getMaterializedViewRefreshTime", mv);
			this.logger.debug("after dao call MV refresh::" + mvRefreshList.size());
			return mvRefreshList;
		} catch (DataAccessException var8) {
			throw new CMSException(this.logger, var8);
		} catch (Exception var9) {
			throw new CMSException(this.logger, var9);
		}
	}
}