package com.worldcheck.atlas.bl.updateCTDataEntry;

import com.savvion.sbm.bpmportal.bean.UserBean;
import com.worldcheck.atlas.dao.updateCTDataEntry.UpdateCTDataDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.validation.bl.CTDataValidator;
import com.worldcheck.atlas.vo.UpdateCTDataExcelVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class UpdateCTDataManager {
	private static final String FAIL = "Fail";
	private static final String PASS = "Pass";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.updateCTDataEntry.UpdateCTDataManager");
	private static final String DD_MM_YY = "dd/MM/yy";
	private static final String DD_MM_YYYY = "dd/MM/yyyy";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	private static final String DATA_INVALID_EXCEL_MESSAGE = "Data are in invalid form in excel";
	private static final String UPDATEDCTDATASTATUS = "UpdatedCTDataStatus";
	private PropertyReaderUtil propertyReader = null;
	private CTDataValidator ctDataValidator;
	private UpdateCTDataDAO updateCTDataDAO;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public void setCtDataValidator(CTDataValidator ctDataValidator) {
		this.ctDataValidator = ctDataValidator;
	}

	public void setUpdateCTDataDAO(UpdateCTDataDAO updateCTDataDAO) {
		this.updateCTDataDAO = updateCTDataDAO;
	}

	public String processExcel(HSSFWorkbook wb, HttpServletRequest request) throws CMSException {
		this.logger.debug("In UpdateCTDataManager : processExcel");
		if (request.getSession().getAttribute("UpdatedCTDataStatus") != null) {
			request.getSession().removeAttribute("UpdatedCTDataStatus");
		}

		new ArrayList();
		HSSFRow row = null;

		try {
			UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
			String userName = userBean.getUserName();
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowCount = sheet.getLastRowNum();
			if (rowCount == 0) {
				return "";
			} else {
				List headerNames = StringUtils.commaSeparatedStringToList(this.propertyReader.getCtDataExcelHeader());
				List<UpdateCTDataExcelVO> updateCTDataStatusList = new ArrayList();
				HashMap<String, String> updateCTDataStatus = new HashMap();
				HashMap<String, UpdateCTDataExcelVO> updateCTDataMap = new HashMap();
				this.logger.debug(" number of rows " + rowCount);
				List<String> crnInputList = new ArrayList();
				List<String> crnOutputList = new ArrayList();

				int k;
				for (k = 1; k <= rowCount; ++k) {
					row = sheet.getRow(k);
					HSSFCell crnCell = row.getCell((short) 0);
					if (crnCell != null && !crnCell.getStringCellValue().equalsIgnoreCase("")) {
						crnInputList.add(crnCell.getStringCellValue().toUpperCase());
					}
				}

				this.logger.debug("list of crn for crn validation in system" + crnInputList.size());
				List tempCrnOutputList;
				if (crnInputList.size() < 1000 && crnInputList.size() > 0) {
					crnOutputList = this.updateCTDataDAO.getCRNListfromClientCase(crnInputList);
				} else {
					k = 0;

					while (k < crnInputList.size()) {
						int end = k + 1000;
						if (end >= crnInputList.size()) {
							end = crnInputList.size() + 1;
						}

						tempCrnOutputList = this.updateCTDataDAO
								.getCRNListfromClientCase(crnInputList.subList(k, end - 1));
						k += 999;
						((List) crnOutputList).addAll(tempCrnOutputList);
					}
				}

				this.logger.debug("crn list from DB after clarify by cms_ClientCase:" + ((List) crnOutputList).size());
				new ArrayList();
				List<String> allActiveCurrencyCodeList = this.updateCTDataDAO.getAllActiveCurrencyCode();
				this.logger.debug("currency code list from DB " + allActiveCurrencyCodeList.size());

				for (int j = 1; j <= rowCount; ++j) {
					row = sheet.getRow(j);
					boolean flag = true;
					tempCrnOutputList = null;
					UpdateCTDataExcelVO ctDataExcelVOforStatus = null;
					UpdateCTDataExcelVO ctDataExcelVO = new UpdateCTDataExcelVO();
					ctDataExcelVO.setUpdatedBy(userName);
					ctDataExcelVOforStatus = new UpdateCTDataExcelVO();
					HSSFCell crnCell = row.getCell((short) 0);
					if (crnCell == null || crnCell.getStringCellValue().equalsIgnoreCase("")) {
						return "Data are in invalid form in excel";
					}

					flag = this.setDataVO(row, ctDataExcelVO, (List) crnOutputList, allActiveCurrencyCodeList);
					if (!flag) {
						ctDataExcelVOforStatus.setCrn(crnCell.getStringCellValue());
						ctDataExcelVOforStatus.setStatus("Fail");
						updateCTDataStatus.put(crnCell.getStringCellValue(), "Fail");
					} else {
						ctDataExcelVOforStatus.setCrn(crnCell.getStringCellValue());
						ctDataExcelVOforStatus.setStatus("Pass");
						updateCTDataStatus.put(crnCell.getStringCellValue(), "Pass");
						updateCTDataMap.put(crnCell.getStringCellValue(), ctDataExcelVO);
					}

					updateCTDataStatusList.add(ctDataExcelVOforStatus);
				}

				request.getSession().setAttribute("UpdatedCTDataStatus", updateCTDataStatusList);
				List<String> crnNumbers = new ArrayList();
				List<String> crnDataList = new ArrayList();
				if (updateCTDataMap.size() > 0) {
					Set keySetforCrnNumber = updateCTDataMap.keySet();
					Iterator itrforCrnNumber = keySetforCrnNumber.iterator();

					while (itrforCrnNumber.hasNext()) {
						crnNumbers.add(itrforCrnNumber.next().toString().toUpperCase());
					}

					this.logger.debug("CRN list " + crnNumbers.size());
					if (crnNumbers.size() < 1000 && crnNumbers.size() > 0) {
						crnDataList = this.updateCTDataDAO.getCrnDataList(crnNumbers);
					} else {
						int k = 0;

						while (k < crnNumbers.size()) {
							int end = k + 1000;
							if (end >= crnNumbers.size()) {
								end = crnNumbers.size() + 1;
							}

							List<String> tempCrnDataList = this.updateCTDataDAO
									.getCrnDataList(crnNumbers.subList(k, end - 1));
							k += 999;
							((List) crnDataList).addAll(tempCrnDataList);
						}
					}

					this.logger
							.debug("crn list from DB after clarify by to_capetown_info:" + ((List) crnDataList).size());
					Set keySet = updateCTDataMap.keySet();
					Iterator itr = keySet.iterator();

					while (itr.hasNext()) {
						String key = itr.next().toString();

						try {
							if (((List) crnDataList)
									.contains(((UpdateCTDataExcelVO) updateCTDataMap.get(key)).getCrn())) {
								this.updateCTDataDAO.updateCRNAccounts((UpdateCTDataExcelVO) updateCTDataMap.get(key),
										true);
							} else {
								this.updateCTDataDAO.updateCRNAccounts((UpdateCTDataExcelVO) updateCTDataMap.get(key),
										false);
							}
						} catch (CMSException var27) {
							if (request.getSession().getAttribute("UpdatedCTDataStatus") != null) {
								List<UpdateCTDataExcelVO> sessionList = (ArrayList) request.getSession()
										.getAttribute("UpdatedCTDataStatus");

								for (int index = 0; index < sessionList.size(); ++index) {
									if (((UpdateCTDataExcelVO) sessionList.get(index)).getCrn().equalsIgnoreCase(
											((UpdateCTDataExcelVO) updateCTDataMap.get(key)).getCrn())) {
										UpdateCTDataExcelVO sessionVO = (UpdateCTDataExcelVO) sessionList.get(index);
										sessionVO.setStatus("Fail");
										sessionList.set(index, sessionVO);
										request.getSession().setAttribute("UpdatedCTDataStatus", sessionList);
									}
								}
							}
						}
					}
				}

				return "true";
			}
		} catch (CMSException var28) {
			throw var28;
		} catch (Exception var29) {
			this.logger.error(" Exception " + var29.getMessage());
			throw new CMSException(this.logger, var29);
		}
	}

	private boolean setDataVO(HSSFRow row, UpdateCTDataExcelVO ctDataExcelVO, List<String> crnOutputList,
			List<String> allActiveCurrencyCodeList) throws CMSException {
		boolean flag = true;
		HSSFCell crnCell = row.getCell((short) 0);
		HSSFCell ctDateCell = row.getCell((short) 19);
		HSSFCell invNumberCell = row.getCell((short) 20);
		HSSFCell invCurrencyCell = row.getCell((short) 21);
		HSSFCell invAmountCell = row.getCell((short) 22);
		HSSFCell invDateCell = row.getCell((short) 23);

		try {
			String crn = crnCell.getStringCellValue();
			if (crnOutputList.contains(crn.toUpperCase())) {
				ctDataExcelVO.setCrn(crn);
				if (ctDateCell != null) {
					if (this.processDate(ctDateCell)) {
						this.logger.debug("ct date validated for DB");
						ctDataExcelVO.setCtDate(sdf.format(ctDateCell.getDateCellValue()));
					} else {
						flag = false;
					}
				}

				if (null != invNumberCell) {
					if (invNumberCell.getCellType() == 0) {
						this.logger.debug("inv number is validated for db");
						ctDataExcelVO.setInvNumber((float) invNumberCell.getNumericCellValue());
					} else {
						flag = false;
					}
				}

				if (null != invCurrencyCell) {
					if (invCurrencyCell.getCellType() == 1) {
						String currencyCode = invCurrencyCell.getStringCellValue();
						if (allActiveCurrencyCodeList.contains(currencyCode.toUpperCase())) {
							this.logger.debug("currency is validated for db");
							ctDataExcelVO.setInvCurrency(invCurrencyCell.getStringCellValue());
						} else {
							this.logger
									.debug("Update failed for CRN:" + crn + " due to inactive/no currency in system");
							flag = false;
						}
					} else {
						flag = false;
					}
				}

				if (null != invAmountCell) {
					if (invAmountCell.getCellType() == 0) {
						this.logger.debug("inv amount is validated for db");
						ctDataExcelVO.setInvAmount((float) invAmountCell.getNumericCellValue());
					} else {
						flag = false;
					}
				}

				if (null != invDateCell) {
					if (this.processDate(invDateCell)) {
						this.logger.debug("inv date is validated for db");
						ctDataExcelVO.setInvDate(sdf.format(invDateCell.getDateCellValue()));
					} else {
						flag = false;
					}
				}
			} else {
				this.logger.debug("CRN not found in system for update :" + crn);
				flag = false;
			}

			return flag;
		} catch (Exception var14) {
			throw new CMSException(this.logger, var14);
		}
	}

	private boolean processDate(HSSFCell cell) {
		this.logger.debug("In UpdateCTDataManager : processDate");
		boolean isValid = false;
		if (cell.getCellType() == 0) {
			isValid = true;
		} else {
			isValid = false;
		}

		this.logger.debug("processDate:" + isValid);
		return isValid;
	}
}