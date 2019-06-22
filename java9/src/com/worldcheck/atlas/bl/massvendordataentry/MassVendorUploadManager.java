package com.worldcheck.atlas.bl.massvendordataentry;

import com.worldcheck.atlas.bl.interfaces.IMassVendorUpload;
import com.worldcheck.atlas.bl.interfaces.IMasterDataValidator;
import com.worldcheck.atlas.dao.massvendordataentry.MassVendorUploadDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.sbm.util.BizSoloTagStringUtil;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.utils.StringUtils;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.massvendordataentry.MassVendorUploadVO;
import com.worldcheck.atlas.vo.masters.CountryMasterVO;
import com.worldcheck.atlas.vo.masters.CurrencyMasterVO;
import com.worldcheck.atlas.vo.masters.VendorMasterVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class MassVendorUploadManager implements IMassVendorUpload {
	private static final String VENDOR_DETAILS_UPDATED = "Vendor Details Updated.";
	private static final String VENDOR_DETAILS_UPDATED_FOR_THIS_CASE_FROM_MASS_VENDOR_DATA_ENTRY_UPLOAD_USING_FILE = "Vendor details updated for this case from Mass Vendor data entry upload using File";
	private static final String SINGLE_QUOTE = "'";
	private static final String NULL_REPLACEMENT = "";
	private static final String NULL_STRING = "null";
	private static final String DEFAULT_SHEET_NAME = "Sheet 1";
	private static final String EXCEL_FILE_PREFIX = "Worldcheck_";
	private static final String EXCEL_FILE_SUFFIX = ".xls";
	private static final String UNDERSCORE = "_";
	String ALL_VALID = "";
	private static final String INVALID_DATA_IN_EXCEL_MSG = "Invalid Data in Excel.";
	private static final String SUCCESS_MSG = "Successfully uploaded.";
	private static final String DATE_UI_PATTERN = "[0-9]{4}+[/-]+[0-9]{2}+[/-]+[0-9]{2}+[T]+[0-9]{2}+[/:]+[0-9]{2}+[/:]+[0-9]{2}";
	private static final String HASH_STRING_DB = "\\";
	private static final String HASH_STRING_UI = "\\\\";
	private static final String MM_DD_YYYY = "MM/dd/yyyy";
	private static final String T = "T";
	private static final String VENDOR_INVOICE_ID = "vendorInvoiceId";
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.massvendordataentry.MassVendorUploadManager");
	private MassVendorUploadDAO massVendorUploadDAO;
	private PropertyReaderUtil propertyReader = null;
	private IMasterDataValidator masterDataValidator = null;
	List<String> countryNameList = new ArrayList();
	List<String> vendorNameList = new ArrayList();
	List<String> currencyNameList = new ArrayList();
	public static final String UPDATED_BY = "updatedBy";
	private static final String STATUS_CODE = "statusCode";

	public void setMassVendorUploadDAO(MassVendorUploadDAO massVendorUploadDAO) {
		this.massVendorUploadDAO = massVendorUploadDAO;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public void setMasterDataValidator(IMasterDataValidator masterDataValidator) {
		this.masterDataValidator = masterDataValidator;
	}

	public List<String> processExcel(File excelFile) throws CMSException {
		this.logger.debug("here will process the excel");
		List<HashMap> massVendorUploadList = new ArrayList();
		InputStream excelFileStream = null;
		HSSFRow row = null;
		Object jsonObject = new ArrayList();

		try {
			excelFileStream = new FileInputStream(excelFile);
			this.logger.debug("file input stream found  ");
			HSSFWorkbook wb = new HSSFWorkbook(excelFileStream);
			this.logger.debug("work book found  ");
			HSSFSheet sheet = wb.getSheetAt(0);
			int rowCount = sheet.getLastRowNum();
			List headerNames = StringUtils
					.commaSeparatedStringToList(this.propertyReader.getMassVendorUploadExcelHeader());
			HashMap<String, Object> massVendorUploadRowDataMap = new HashMap();
			this.logger.debug(" number of rows " + rowCount);
			int j = 1;

			while (true) {
				if (j > rowCount) {
					this.logger.debug("number of rows in row map list " + massVendorUploadList.size());
					if (massVendorUploadList.size() > 0) {
						jsonObject = this.getJsonObject(massVendorUploadList, headerNames);
					} else {
						((List) jsonObject).add(0, "");
						((List) jsonObject).add(1, "false");
					}
					break;
				}

				boolean isValidRow = false;
				row = sheet.getRow(j);
				if (row != null && row.getPhysicalNumberOfCells() > 0) {
					this.logger.debug("row " + j + " has atleast one deifned cell");
					isValidRow = true;
					massVendorUploadRowDataMap = new HashMap();

					for (int i = 0; i < headerNames.size(); ++i) {
						HSSFCell cell = row.getCell((short) i);
						if (i < headerNames.size() - 1 && cell != null) {
							if (i == 0) {
								if (cell.getCellType() == 0) {
									massVendorUploadRowDataMap.put((String) headerNames.get(i),
											cell.getDateCellValue());
								} else if (cell.getCellType() == 1) {
									massVendorUploadRowDataMap.put((String) headerNames.get(i),
											cell.getStringCellValue());
								}
							} else if (i == 6) {
								if (cell.getCellType() == 0) {
									massVendorUploadRowDataMap.put((String) headerNames.get(i),
											cell.getNumericCellValue());
								} else if (cell.getCellType() == 1) {
									massVendorUploadRowDataMap.put((String) headerNames.get(i),
											cell.getStringCellValue());
								}
							} else if (cell.getCellType() == 0) {
								massVendorUploadRowDataMap.put((String) headerNames.get(i), cell.getNumericCellValue());
							} else {
								massVendorUploadRowDataMap.put((String) headerNames.get(i),
										cell.getStringCellValue().toString());
							}
						} else if (i < headerNames.size() - 1 && cell == null) {
							massVendorUploadRowDataMap.put((String) headerNames.get(i), "Blank Cell Not Allowed");
						} else if (cell != null) {
							if (cell.getCellType() == 0) {
								massVendorUploadRowDataMap.put((String) headerNames.get(i),
										String.valueOf(cell.getNumericCellValue()));
							} else if (cell.getCellType() == 1) {
								massVendorUploadRowDataMap.put((String) headerNames.get(i),
										String.valueOf(cell.getStringCellValue().toString()));
							}
						} else {
							massVendorUploadRowDataMap.put((String) headerNames.get(i), "");
						}
					}
				}

				if (isValidRow) {
					this.logger.debug("number of rows in map " + massVendorUploadRowDataMap);
					Collection c = massVendorUploadRowDataMap.values();
					boolean isCellBlank = true;
					Iterator itr = c.iterator();

					while (itr.hasNext()) {
						String itrObjValue = itr.next().toString();
						if (!itrObjValue.isEmpty() && !itrObjValue.equalsIgnoreCase("Blank Cell Not Allowed")
								&& !itrObjValue.matches("\\s{1," + itrObjValue.length() + "}")) {
							isCellBlank = false;
							this.logger.debug("row: " + j + ", values: " + itrObjValue);
							break;
						}
					}

					if (!isCellBlank) {
						this.logger.debug("row " + j + " is defined and atlease a  cell is non-blank");
						massVendorUploadList.add(massVendorUploadRowDataMap);
					} else {
						this.logger.debug("row " + j + " is  defined  but has all blank cell");
					}
				} else {
					this.logger.debug("row:" + j + ":is not defined ");
				}

				++j;
			}
		} catch (FileNotFoundException var30) {
			if (excelFileStream != null) {
				try {
					excelFileStream.close();
				} catch (IOException var28) {
					throw new CMSException(this.logger, var28);
				}
			}

			throw new CMSException(this.logger, var30);
		} catch (IOException var31) {
			if (excelFileStream != null) {
				try {
					excelFileStream.close();
				} catch (IOException var27) {
					throw new CMSException(this.logger, var27);
				}
			}

			throw new CMSException(this.logger, var31);
		} catch (Exception var32) {
			this.logger.error(" Exception " + var32.getMessage());
			throw new CMSException(this.logger, var32);
		} finally {
			try {
				if (excelFileStream != null) {
					excelFileStream.close();
				}
			} catch (IOException var29) {
				throw new CMSException(this.logger, var29);
			}

		}

		return (List) jsonObject;
	}

	private List<String> getJsonObject(List<HashMap> massVendorUploadList, List<String> headerNames)
			throws CMSException {
		this.initCacheList();
		String isValidData = "true";
		List<String> dataList = new ArrayList();
		StringBuffer jsonObject = new StringBuffer("[");

		try {
			String getValidateDataList = this.validateCRNSubject(massVendorUploadList);

			for (int i = 0; i < massVendorUploadList.size(); ++i) {
				HashMap hashMap = (HashMap) massVendorUploadList.get(i);
				jsonObject.append("[");

				for (int j = 0; j < headerNames.size(); ++j) {
					jsonObject.append("'");
					if (((String) headerNames.get(j)).equalsIgnoreCase("Commissioning Date")) {
						if (hashMap.get(headerNames.get(j)) instanceof Date) {
							Date commDate = (Date) hashMap.get(headerNames.get(j));
							SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
							jsonObject.append(format.format(commDate));
						} else {
							this.logger.debug("setting valid data flag date ");
							isValidData = "false";
							jsonObject.append(hashMap.get(headerNames.get(j)) + "_invalid");
						}
					} else {
						String currency;
						if (((String) headerNames.get(j)).equalsIgnoreCase("crn")) {
							currency = (String) hashMap.get(headerNames.get(j));
							jsonObject.append(currency.replace("\\", "\\\\"));
						} else if (((String) headerNames.get(j)).equalsIgnoreCase("Country")) {
							currency = (String) hashMap.get(headerNames.get(j));
							if (!this.countryNameList.contains(currency)) {
								currency = currency.concat("_invalid");
								this.logger.debug("setting valid data flag country");
								isValidData = "false";
							}

							jsonObject.append(BizSoloTagStringUtil.getDisplayTag(currency));
						} else if (((String) headerNames.get(j)).equalsIgnoreCase("Vendor Name")) {
							currency = String.valueOf(hashMap.get(headerNames.get(j)));
							if (!this.vendorNameList.contains(currency)) {
								currency = currency.concat("_invalid");
								this.logger.debug("setting valid data flag vendor ");
								isValidData = "false";
							}

							jsonObject.append(BizSoloTagStringUtil.getDisplayTag(currency));
						} else if (((String) headerNames.get(j)).equalsIgnoreCase("Currency")) {
							currency = String.valueOf(hashMap.get(headerNames.get(j)));
							if (!this.currencyNameList.contains(currency)) {
								currency = currency.concat("_invalid");
								isValidData = "false";
								this.logger.debug("setting valid data flag currency ");
							}

							jsonObject.append(BizSoloTagStringUtil.getDisplayTag(currency));
						} else if (((String) headerNames.get(j)).equalsIgnoreCase("Amount")) {
							if (hashMap.get(headerNames.get(j)) instanceof Double) {
								jsonObject.append(hashMap.get(headerNames.get(j)));
							} else {
								isValidData = "false";
								this.logger.debug("setting valid data flag amount ");
								jsonObject.append(hashMap.get(headerNames.get(j)) + "_invalid");
							}
						} else if (hashMap.get(headerNames.get(j)) instanceof String) {
							jsonObject.append(BizSoloTagStringUtil
									.getDisplayTag(String.valueOf(hashMap.get(headerNames.get(j)))));
						} else {
							jsonObject.append(hashMap.get(headerNames.get(j)));
						}
					}

					jsonObject.append("'");
					if (j != headerNames.size() - 1) {
						jsonObject.append(",");
					}
				}

				jsonObject.append("]");
				if (i != massVendorUploadList.size() - 1) {
					jsonObject.append(",");
				}
			}

			jsonObject.append("]");
			dataList.add(jsonObject.toString());
			if (this.ALL_VALID.equalsIgnoreCase("false")) {
				isValidData = "false";
			}

			dataList.add(isValidData);
			if (getValidateDataList != null && !getValidateDataList.equals("")) {
				dataList.add(getValidateDataList);
			}

			return dataList;
		} catch (Exception var12) {
			throw new CMSException(this.logger, var12);
		}
	}

	private String validateCRNSubject(List<HashMap> massVendorUploadList) throws CMSException {
		List<String> crnNumbers = new ArrayList();
		String isValidData = "true";
		this.ALL_VALID = "true";
		int subCount = false;
		String validDataList = null;
		List<MassVendorUploadVO> crnDataList = new ArrayList();
		boolean isValidCRN = false;
		boolean isValidSubject = false;
		boolean isValidCountry = false;
		String isSubCount = "false";

		try {
			int k;
			for (k = 0; k < massVendorUploadList.size(); ++k) {
				HashMap hashMap = (HashMap) massVendorUploadList.get(k);
				crnNumbers.add(String.valueOf(hashMap.get("crn".toUpperCase())));
			}

			this.logger.debug("CRN list " + crnNumbers.size());
			int i;
			if (crnNumbers.size() < 1000 && crnNumbers.size() > 0) {
				crnDataList = this.massVendorUploadDAO.getCRNData(crnNumbers);
			} else {
				k = 0;

				while (k < crnNumbers.size()) {
					i = k;
					int end = k + 1000;
					this.logger.debug("start is before " + k + " end is " + end);
					if (end >= crnNumbers.size()) {
						end = crnNumbers.size() + 1;
					}

					this.logger.debug("k is " + k + " end is " + end);
					this.logger.debug("start is after " + k + " end is " + end);
					List<MassVendorUploadVO> tempCrnDataList = this.massVendorUploadDAO
							.getCRNData(crnNumbers.subList(k, end - 1));
					k += 999;
					this.logger.debug("start is after k updated " + i + " end is " + end);
					((List) crnDataList).addAll(tempCrnDataList);
				}
			}

			List<String> dataValid = new ArrayList();
			Iterator iterator = massVendorUploadList.iterator();

			while (true) {
				while (true) {
					String crn;
					do {
						if (!iterator.hasNext()) {
							for (i = 0; i < dataValid.size(); ++i) {
								if (((String) dataValid.get(i)).equalsIgnoreCase("false")) {
									this.ALL_VALID = "false";
									break;
								}
							}

							return validDataList;
						}

						HashMap rowData = (HashMap) iterator.next();
						crn = String.valueOf(rowData.get("crn".toUpperCase()));
						String nameSearched = String.valueOf(rowData.get("Name Searched"));
						String country = String.valueOf(rowData.get("Country"));
						isValidCRN = false;
						isValidSubject = false;
						isValidCountry = false;
						isSubCount = "false";
						Iterator var18 = ((List) crnDataList).iterator();

						while (var18.hasNext()) {
							MassVendorUploadVO massVendorUploadVO = (MassVendorUploadVO) var18.next();
							if (massVendorUploadVO.getCRN().equalsIgnoreCase(crn)) {
								isValidCRN = true;
								if (massVendorUploadVO.getNameSearched().equalsIgnoreCase(nameSearched)) {
									isValidSubject = true;
									if (massVendorUploadVO.getCountry().equalsIgnoreCase(country)) {
										isValidCountry = true;
									}
								}
							}
						}

						if (!isValidCRN) {
							isValidData = "false";
							rowData.put("crn".toUpperCase(), crn + "_invalid");
						}

						if (!isValidSubject) {
							isValidData = "false";
							rowData.put("Name Searched", nameSearched + "_invalid");
						}

						if (!isValidCountry) {
							isValidData = "false";
							rowData.put("Country", country + "_invalid");
						}

						dataValid.add(isValidData);
						if (isValidCRN && isValidSubject && isValidCountry) {
							int subCount = this.massVendorUploadDAO.getSubjectCount(rowData);
							this.logger.debug("The Subject count is -----> " + subCount);
							if (subCount > 1) {
								isValidData = "false";
								isSubCount = "true";
							}
						}
					} while (!isSubCount.equals("true"));

					if (validDataList != null && !validDataList.isEmpty()) {
						validDataList = validDataList + "," + crn;
					} else {
						validDataList = crn;
					}
				}
			}
		} catch (Exception var19) {
			throw new CMSException(this.logger, var19);
		}
	}

	private void initCacheList() throws CMSException {
		try {
			List<CountryMasterVO> countryList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("COUNTRY_MASTER");
			Iterator iterator = countryList.iterator();

			while (iterator.hasNext()) {
				CountryMasterVO countryMasterVO = (CountryMasterVO) iterator.next();
				this.countryNameList.add(countryMasterVO.getCountry());
			}

			this.logger.debug("country names " + this.countryNameList.size());
			List<VendorMasterVO> vendorList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("VENDOR_MASTER");
			Iterator iterator = vendorList.iterator();

			while (iterator.hasNext()) {
				VendorMasterVO vendorMasterVO = (VendorMasterVO) iterator.next();
				this.vendorNameList.add(vendorMasterVO.getVendorName());
			}

			this.logger.debug("vendorNameList names " + this.vendorNameList.size());
			List<CurrencyMasterVO> currencyList = ResourceLocator.self().getCacheService()
					.getCacheItemsList("CURRENCY_MASTER");
			Iterator iterator = currencyList.iterator();

			while (iterator.hasNext()) {
				CurrencyMasterVO currencyMasterVO = (CurrencyMasterVO) iterator.next();
				this.currencyNameList.add(currencyMasterVO.getCurrencyCode());
			}

			this.logger.debug("currencyNameList names " + this.currencyNameList.size());
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	public void addData(List<HashMap> massVendorUploadList, String userName) throws CMSException {
		HashMap hashMap = null;
		List idList = null;

		try {
			Iterator iterator = massVendorUploadList.iterator();

			while (iterator.hasNext()) {
				hashMap = (HashMap) iterator.next();
				idList = this.getListOfIds(hashMap);
				hashMap.put("updatedBy", userName);
				hashMap.put("statusCode", 1);
				if (idList.size() == 0) {
					this.logger.debug(" to be inserted ");
					int id = this.massVendorUploadDAO.insertData(hashMap);
					hashMap.put("vendorInvoiceId", id);
					this.massVendorUploadDAO.insertMapData(hashMap);
				} else {
					this.logger.debug(" to be updated " + hashMap);
					this.massVendorUploadDAO.updateData(hashMap);
				}
			}

		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	private List<Integer> getListOfIds(HashMap hashMap) throws CMSException {
		new ArrayList();
		List<Integer> idList = this.massVendorUploadDAO.getExistingIds(hashMap);
		return idList;
	}

	public List<String> getModifiedDataList(List<Map> modifiedRecords, String userName, String caseHistoryPerformer)
			throws CMSException {
		new HashMap();
		new HashMap();
		new ArrayList();

		List headerNames;
		try {
			headerNames = StringUtils.commaSeparatedStringToList(this.propertyReader.getMassVendorUploadExcelHeader());
		} catch (Exception var10) {
			throw new CMSException(this.logger, var10);
		}

		List<HashMap> massVendorUploadList = new ArrayList();

		for (int j = 0; j < modifiedRecords.size(); ++j) {
			HashMap rowData = (HashMap) modifiedRecords.get(j);
			HashMap massVendorUploadRowDataMap = new HashMap();

			try {
				for (int i = 0; i < headerNames.size(); ++i) {
					if (headerNames.get(i).equals("Commissioning Date")) {
						this.processCommisioningDate(massVendorUploadRowDataMap, (String) headerNames.get(i),
								(String) rowData.get(headerNames.get(i)));
					} else if (headerNames.get(i).equals("Amount")) {
						this.processAmount(massVendorUploadRowDataMap, (String) headerNames.get(i),
								(String) rowData.get(headerNames.get(i)));
					} else if (headerNames.get(i).equals("crn".toUpperCase())) {
						this.processCRN(massVendorUploadRowDataMap, (String) headerNames.get(i),
								(String) rowData.get(headerNames.get(i)));
					} else {
						this.processColumnData(massVendorUploadRowDataMap, (String) headerNames.get(i),
								(String) rowData.get(headerNames.get(i)));
					}
				}
			} catch (Exception var11) {
				throw new CMSException(this.logger, var11);
			}

			this.logger.debug("record map " + massVendorUploadRowDataMap);
			massVendorUploadList.add(massVendorUploadRowDataMap);
		}

		this.logger.debug("number of records " + massVendorUploadList.size());
		List<String> dataList = this.getJsonObject(massVendorUploadList, headerNames);
		this.logger.debug("is valid data " + (String) dataList.get(1));
		this.logger.debug("data list size " + dataList.size());
		if (dataList.size() == 2 && ((String) dataList.get(1)).equalsIgnoreCase("true")) {
			this.logger.info(" file is valid going to insert the data in DB ");
			this.addData(massVendorUploadList, userName);
			this.logger.info(" data inserted successfully");
			String fileName = this.writeToExcel(massVendorUploadList, userName);
			this.logger.info(" excel file created with name " + fileName);
			this.insertCaseHistory(massVendorUploadList, userName, fileName, caseHistoryPerformer);
			this.logger.info(" case history inserted successfully");
			dataList.add("Successfully uploaded.");
		} else {
			dataList.add("Invalid Data in Excel.");
		}

		return dataList;
	}

	private String writeToExcel(List<HashMap> massVendorUploadList, String userName) throws CMSException {
		String fileName = "";

		try {
			List<String> lstHeader = StringUtils
					.commaSeparatedStringToList(this.propertyReader.getMassVendorUploadExcelHeader());
			List<HashMap> dataList = new ArrayList();
			Iterator iterator = massVendorUploadList.iterator();

			while (iterator.hasNext()) {
				HashMap datamap = (HashMap) iterator.next();
				dataList.add(datamap);
			}

			fileName = this.storeFile(lstHeader, dataList, "MassVendorUpload", (short) 0, (short) 1, userName);
			return fileName;
		} catch (Exception var8) {
			throw new CMSException(this.logger, var8);
		}
	}

	private String storeFile(List<String> lstHeader, List<HashMap> dataList, String sheetName, short hssfForeColorIndex,
			short hssfBackgroundColourIndex, String userName) throws CMSException {
		this.logger.debug("in ExcelDownloader::writeToExcel");
		FileOutputStream fos = null;
		String fileName = "";
		String tempExcelFilePath = "";
		String tempDate = null;

		try {
			int rowCount = 0;
			int columnCount = 0;
			HSSFSheet sht = null;
			HSSFWorkbook workbook = this.createWorkBook();
			HSSFCellStyle headerCellStyle;
			if (hssfForeColorIndex == 0 && hssfBackgroundColourIndex == 0) {
				headerCellStyle = this.createHeaderStyle(workbook);
			} else {
				headerCellStyle = this.createHeaderStyle(workbook, hssfForeColorIndex, hssfBackgroundColourIndex);
			}

			if (sheetName != null && !sheetName.isEmpty()) {
				sht = workbook.createSheet(sheetName);
			} else {
				sht = workbook.createSheet("Sheet 1");
			}

			int var38 = rowCount + 1;
			HSSFRow row = sht.createRow(rowCount);
			HashMap hashMap = null;
			Iterator dataListIterator = lstHeader.iterator();

			while (dataListIterator.hasNext()) {
				String headerName = (String) dataListIterator.next();
				sht.setColumnWidth((short) columnCount, (short) 5000);
				HSSFCell cel = row.createCell((short) (columnCount++), 1);
				cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(headerName)));
				cel.setCellStyle(headerCellStyle);
			}

			this.logger.debug("iterating datalist");
			HSSFFont font = workbook.createFont();
			font.setFontName("Arial Unicode MS");
			dataListIterator = dataList.iterator();

			label160 : while (true) {
				HSSFRow rowNext;
				int cellCount;
				do {
					if (!dataListIterator.hasNext()) {
						tempExcelFilePath = this.propertyReader.getTempExcelFilePath();
						File dir = new File(tempExcelFilePath);
						if (!dir.exists()) {
							(new File(tempExcelFilePath)).mkdirs();
						}

						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss.SSS");
						tempDate = sdf.format(new Date());
						fileName = tempExcelFilePath + File.separator + "Worldcheck_" + "TestFile" + "_" + tempDate
								+ ".xls";
						File excelFile = new File(fileName);
						fos = new FileOutputStream(excelFile);
						workbook.write(fos);
						break label160;
					}

					cellCount = 0;
					hashMap = (HashMap) dataListIterator.next();
					rowNext = sht.createRow(var38++);
				} while (hashMap == null);

				Iterator var23 = lstHeader.iterator();

				while (var23.hasNext()) {
					String headerName = (String) var23.next();
					HSSFCell cel = rowNext.createCell((short) (cellCount++));
					String cellValue = String.valueOf(hashMap.get(headerName));
					HSSFCellStyle cellStyle = workbook.createCellStyle();
					cellStyle.setFont(font);
					cel.setCellStyle(cellStyle);
					cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(cellValue)));
				}
			}
		} catch (FileNotFoundException var35) {
			throw new CMSException(this.logger, var35);
		} catch (IOException var36) {
			throw new CMSException(this.logger, var36);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException var34) {
					throw new CMSException(this.logger, var34);
				}
			}

		}

		this.logger.debug("out ExcelDownloader::writeToExcel");
		return "Worldcheck_TestFile_" + tempDate + ".xls";
	}

	private HSSFCellStyle createHeaderStyle(HSSFWorkbook workbook) {
		this.createHeaderStyle(workbook, (short) 0, (short) 1);
		return null;
	}

	private HSSFWorkbook createWorkBook() {
		HSSFWorkbook workbook = new HSSFWorkbook();
		return workbook;
	}

	private static String getNullReplacedValue(String cellValue) {
		return cellValue != null && !cellValue.equalsIgnoreCase("null") ? cellValue : "";
	}

	private HSSFCellStyle createHeaderStyle(HSSFWorkbook workbook, short hssfForeColorIndex,
			short hssfBackgroundColourIndex) {
		this.logger.debug("creating HeaderStyle");
		HSSFCellStyle headerCellStyle = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontName("Arial Unicode MS");
		headerCellStyle.setFillPattern((short) 1);
		headerCellStyle.setFillForegroundColor(hssfBackgroundColourIndex);
		headerCellStyle.setBorderBottom((short) 1);
		headerCellStyle.setBottomBorderColor((short) 8);
		headerCellStyle.setBorderLeft((short) 1);
		headerCellStyle.setLeftBorderColor((short) 8);
		headerCellStyle.setBorderRight((short) 1);
		headerCellStyle.setRightBorderColor((short) 8);
		headerCellStyle.setBorderTop((short) 1);
		headerCellStyle.setTopBorderColor((short) 8);
		headerCellStyle.setWrapText(true);
		font.setBoldweight((short) 700);
		font.setColor(hssfForeColorIndex);
		headerCellStyle.setFont(font);
		this.logger.debug("created HeaderStyle");
		return headerCellStyle;
	}

	private void insertCaseHistory(List<HashMap> massVendorUploadList, String userName, String fileName,
			String caseHistoryPerformer) throws CMSException {
		List<String> crnNumbers = new ArrayList();
		List<CaseHistory> caseHistoryList = new ArrayList();
		List<MassVendorUploadVO> crnDataList = new ArrayList();

		int k;
		for (k = 0; k < massVendorUploadList.size(); ++k) {
			HashMap hashMap = (HashMap) massVendorUploadList.get(k);
			if (!crnNumbers.contains((String) hashMap.get("crn".toUpperCase()))) {
				crnNumbers.add((String) hashMap.get("crn".toUpperCase()));
			}
		}

		this.logger.debug("CRN list " + crnNumbers.size());
		if (crnNumbers.size() < 1000 && crnNumbers.size() > 0) {
			crnDataList = this.massVendorUploadDAO.getCRNPIDData(crnNumbers);
		} else {
			k = 0;

			while (k < crnNumbers.size()) {
				int start = k;
				int end = k + 1000;
				this.logger.debug("start is before " + k + " end is " + end);
				if (end >= crnNumbers.size()) {
					end = crnNumbers.size() + 1;
				}

				this.logger.debug("k is " + k + " end is " + end);
				this.logger.debug("start is after " + k + " end is " + end);
				List<MassVendorUploadVO> tempCrnDataList = this.massVendorUploadDAO
						.getCRNPIDData(crnNumbers.subList(k, end - 1));
				k += 999;
				this.logger.debug("start is after k updated " + start + " end is " + end);
				((List) crnDataList).addAll(tempCrnDataList);
			}
		}

		Iterator iterator = ((List) crnDataList).iterator();

		while (iterator.hasNext()) {
			MassVendorUploadVO massVendorUploadVO = (MassVendorUploadVO) iterator.next();
			CaseHistory caseHistory = new CaseHistory();
			caseHistory.setCRN(massVendorUploadVO.getCRN());
			caseHistory.setPerformer(caseHistoryPerformer);
			caseHistory.setPid(massVendorUploadVO.getPid());
			caseHistory.setNewInfo(
					"Vendor details updated for this case from Mass Vendor data entry upload using File" + fileName);
			caseHistory.setAction("Vendor Details Updated.");
			caseHistoryList.add(caseHistory);
		}

		ResourceLocator.self().getCaseHistoryService().setCaseHistoryForMassVendorUpload(caseHistoryList);
	}

	private void processCRN(HashMap<String, Object> massVendorUploadRowDataMap, String key, String crn) {
		if (crn.indexOf("_invalid") > -1) {
			crn = crn.substring(0, crn.indexOf("_invalid"));
		}

		crn = crn.replace("\\\\", "\\");
		massVendorUploadRowDataMap.put(key, crn);
	}

	private void processColumnData(HashMap<String, Object> massVendorUploadRowDataMap, String key, String data) {
		if (data.indexOf("_invalid") > -1) {
			data = data.substring(0, data.indexOf("_invalid"));
		}

		massVendorUploadRowDataMap.put(key, data);
	}

	private void processAmount(HashMap<String, Object> massVendorUploadRowDataMap, String key, String amount) {
		if (amount.indexOf("_invalid") > -1) {
			amount = amount.substring(0, amount.indexOf("_invalid"));
			massVendorUploadRowDataMap.put(key, amount);
		} else {
			double amt = 0.0D;

			try {
				amt = Double.parseDouble(amount);
				massVendorUploadRowDataMap.put(key, amt);
			} catch (Exception var7) {
				this.logger.error("in error " + var7.getMessage());
				massVendorUploadRowDataMap.put(key, amount);
			}
		}

	}

	private void processCommisioningDate(HashMap<String, Object> massVendorUploadRowDataMap, String key,
			String commisioningDate) {
		this.logger.debug("In processing Commissioning date");

		try {
			this.logger.debug("commissioningdate is:" + commisioningDate);
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			new Date();
			if (commisioningDate.indexOf("_invalid") > -1) {
				commisioningDate = commisioningDate.substring(0, commisioningDate.indexOf("_invalid"));
			}

			Pattern pattern1 = Pattern
					.compile("[0-9]{4}+[/-]+[0-9]{2}+[/-]+[0-9]{2}+[T]+[0-9]{2}+[/:]+[0-9]{2}+[/:]+[0-9]{2}");
			Matcher matcher1 = pattern1.matcher(commisioningDate);
			Date commDate;
			if (matcher1.find()) {
				commisioningDate = commisioningDate.substring(0, commisioningDate.indexOf("T"));
				commDate = format2.parse(commisioningDate);
				this.logger.debug("match find with pattern and commDate is " + commDate);
			} else {
				commDate = format.parse(commisioningDate);
				this.logger.debug("match does not find with pattern and commDate is :" + commDate);
			}

			if (("" + (commDate.getYear() + 1900)).length() > 4) {
				massVendorUploadRowDataMap.put(key, commisioningDate);
			} else {
				massVendorUploadRowDataMap.put(key, commDate);
			}
		} catch (Exception var9) {
			this.logger.error("in error " + var9.getMessage());
			massVendorUploadRowDataMap.put(key, commisioningDate);
		}

	}
}