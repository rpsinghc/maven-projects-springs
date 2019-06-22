package com.worldcheck.atlas.bl.downloadexcel;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.report.RevenueSummaryVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ChartExcelDownloader {
	private static ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader");
	private static PropertyReaderUtil propertyReader;
	private static final String NULL_REPLACEMENT = "";
	private static final String NULL_STRING = "null";
	private static final String EXCEL_FILE_PREFIX = "Worldcheck_";
	private static final String EXCEL_FILE_SUFFIX = ".xlsx";
	private static final String UNDERSCORE = "_";
	private static String FILE_DIR = "";
	private static String FILE_NAME_BY_MONTH = "ChartSample-ByMonth.xlsx";
	private static String FILE_NAME_BY_STATUS = "ChartSample-ByStatus.xlsx";
	private static String FILE_NAME_BY_REPORT = "ChartSample-ByReport.xlsx";
	private static String FILE_NAME_BY_CLIENT = "ChartSample-ByClient.xlsx";
	private static String FILE_NAME_BY_TOP_COUNTRY = "ChartSample-ByTopCountry.xlsx";
	private static String FILE_NAME_BY_DELIVERY = "ChartSample-ByDelivery.xlsx";
	private static String FILE_NAME_BY_TAT_HISTOGRAM = "ChartSample-ByTAT.xlsx";
	private static String SHEET_BY_CASES = "Cases";
	private static String SHEET_BY_REVENUES = "Revenues";
	private static String SHEET_SUFFIX_BY_MONTH = "_BY_MONTH";
	private static String SHEET_SUFFIX_BY_STATUS = "_BY_STATUS";
	private static String SHEET_SUFFIX_BY_REPORT = "_BY_REPORT";
	private static String SHEET_SUFFIX_BY_CLIENT = "_BY_CLIENTS";
	private static String SHEET_SUFFIX_BY_TOP_COUNTRY = "_BY_TOP_COUNTRY";
	private static String SHEET_SUFFIX_BY_TAT = "_BY_TAT";
	private static String SHEET_SUFFIX_BY_DELIVERY = "_BY_DELIVERY";
	private static Name rangeCellCases = null;
	private static String referenceCases = null;
	private static Name rangeCellRevenue = null;
	private static String referenceRevenue = null;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	private static String getNullReplacedValue(String cellValue) {
		return cellValue != null && !cellValue.equalsIgnoreCase("null") ? cellValue : "";
	}

	public static String exportChartToExcel(List<String> headerCaseList, List<String> headerRevenueList,
			List<List<String>> dataCaseList, List<List<String>> dataRevenueList, List<String> crdHeaderList,
			List<List<String>> crddataList, HttpServletResponse response, String excelFileName,
			RevenueSummaryVO revenueSummaryVO, int sizeOfReport, int tabType)
			throws CMSException, IOException, InvalidFormatException {
		logger.debug("in ChartExcelDownloader::writeToExcel1::" + new Date());
		logger.debug("in ChartExcelDownloader::writeToExcel1::dataList;" + dataCaseList);
		FileOutputStream fos = null;
		FileInputStream fis = null;
		String fileName = "";
		String tempExcelFilePath = "";
		byte deface = 1;

		try {
			int rowCount = 0;
			int rowCount2 = 0;
			int firstRowCount = 0;
			int firstColumnCount = 0;
			FILE_DIR = propertyReader.getRevenueSummaryTemplatePath();
			logger.debug("File dir::" + FILE_DIR);
			if (tabType == 1) {
				logger.debug("##ChartExcelDownloader##exportChartToExcel## first tab");
				fis = new FileInputStream(new File(FILE_DIR, FILE_NAME_BY_MONTH));
			} else if (tabType == 2) {
				logger.debug("##ChartExcelDownloader##exportChartToExcel## second tab");
				fis = new FileInputStream(new File(FILE_DIR, FILE_NAME_BY_STATUS));
			} else if (tabType == 3) {
				logger.debug("##ChartExcelDownloader##exportChartToExcel## third tab");
				fis = new FileInputStream(new File(FILE_DIR, FILE_NAME_BY_REPORT));
			} else if (tabType == 4) {
				logger.debug("##ChartExcelDownloader##exportChartToExcel## fourth tab");
				fis = new FileInputStream(new File(FILE_DIR, FILE_NAME_BY_CLIENT));
			} else if (tabType == 5) {
				logger.debug("##ChartExcelDownloader##exportChartToExcel## fifth tab");
				fis = new FileInputStream(new File(FILE_DIR, FILE_NAME_BY_TOP_COUNTRY));
			}

			Workbook workbook = new XSSFWorkbook(OPCPackage.open(fis));
			renameSheet(workbook, sizeOfReport);
			writeDataInFirstSheet(workbook, firstRowCount, firstColumnCount, crdHeaderList, crddataList);
			String sheetNameCases = SHEET_BY_CASES + sizeOfReport;
			Sheet sh = workbook.getSheet(sheetNameCases);
			if (tabType == 1) {
				sheetNameCases = sheetNameCases.substring(0, sheetNameCases.lastIndexOf("s") + 1)
						+ SHEET_SUFFIX_BY_MONTH;
			} else if (tabType == 2) {
				sheetNameCases = sheetNameCases.substring(0, sheetNameCases.lastIndexOf("s") + 1)
						+ SHEET_SUFFIX_BY_STATUS;
			} else if (tabType == 3) {
				sheetNameCases = sheetNameCases.substring(0, sheetNameCases.lastIndexOf("s") + 1)
						+ SHEET_SUFFIX_BY_REPORT;
			} else if (tabType == 4) {
				sheetNameCases = sheetNameCases.substring(0, sheetNameCases.lastIndexOf("s") + 1)
						+ SHEET_SUFFIX_BY_CLIENT;
			} else if (tabType == 5) {
				sheetNameCases = sheetNameCases.substring(0, sheetNameCases.lastIndexOf("s") + 1)
						+ SHEET_SUFFIX_BY_TOP_COUNTRY;
			}

			workbook.setSheetName(workbook.getSheetIndex(sh), sheetNameCases);
			String sheetNameRevenue = SHEET_BY_REVENUES + sizeOfReport;
			Sheet sh2 = workbook.getSheet(sheetNameRevenue);
			if (tabType == 1) {
				sheetNameRevenue = sheetNameRevenue.substring(0, sheetNameRevenue.lastIndexOf("s"))
						+ SHEET_SUFFIX_BY_MONTH;
			} else if (tabType == 2) {
				sheetNameRevenue = sheetNameRevenue.substring(0, sheetNameRevenue.lastIndexOf("s"))
						+ SHEET_SUFFIX_BY_STATUS;
			} else if (tabType == 3) {
				sheetNameRevenue = sheetNameRevenue.substring(0, sheetNameRevenue.lastIndexOf("s"))
						+ SHEET_SUFFIX_BY_REPORT;
			} else if (tabType == 4) {
				sheetNameRevenue = sheetNameRevenue.substring(0, sheetNameRevenue.lastIndexOf("s"))
						+ SHEET_SUFFIX_BY_CLIENT;
			} else if (tabType == 5) {
				sheetNameRevenue = sheetNameRevenue.substring(0, sheetNameRevenue.lastIndexOf("s"))
						+ SHEET_SUFFIX_BY_TOP_COUNTRY;
			}

			workbook.setSheetName(workbook.getSheetIndex(sh2), sheetNameRevenue);

			for (int x = 0; x <= sizeOfReport; ++x) {
				sh.getRow(1).getCell(x).setCellValue("");
				sh2.getRow(1).getCell(x).setCellValue("");
			}

			logger.debug("in ChartExcelDownloader::writing second sheet");
			int rowCount = rowCount + 1;
			Row row = sh.createRow(rowCount);
			int var45 = rowCount2 + 1;
			Row row1 = sh2.createRow(rowCount2);
			if (tabType != 2 && tabType != 3) {
				if (tabType != 1 && tabType != 4) {
					if (tabType == 5) {
						sh.getRow(0).createCell(3).setCellValue(revenueSummaryVO.getHeaderFirstGraphField());
						sh2.getRow(0).createCell(3).setCellValue(revenueSummaryVO.getHeaderSecondGraphField());
					}
				} else {
					sh.createRow(54).createCell(0).setCellValue(revenueSummaryVO.getYaxisFirstGraphField());
					sh.createRow(55).createCell(0).setCellValue(revenueSummaryVO.getHeaderFirstGraphField());
					sh2.createRow(54).createCell(0).setCellValue(revenueSummaryVO.getYaxisSecondGraphField());
					sh2.createRow(55).createCell(0).setCellValue(revenueSummaryVO.getHeaderSecondGraphField());
				}
			} else {
				sh.createRow(19).createCell(0).setCellValue(revenueSummaryVO.getYaxisFirstGraphField());
				sh.createRow(20).createCell(0).setCellValue(revenueSummaryVO.getHeaderFirstGraphField());
				sh2.createRow(19).createCell(0).setCellValue(revenueSummaryVO.getYaxisSecondGraphField());
				sh2.createRow(20).createCell(0).setCellValue(revenueSummaryVO.getHeaderSecondGraphField());
			}

			rowCount = writeDataInSecondSheet(workbook, sh, row, headerCaseList, dataCaseList);
			writeDataInThirdSheet(workbook, sh2, row1, headerRevenueList, dataRevenueList);
			removeSheet(workbook);
			tempExcelFilePath = propertyReader.getTempExcelFilePath();
			File dir = new File(tempExcelFilePath);
			if (!dir.exists()) {
				(new File(tempExcelFilePath)).mkdirs();
			}

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss.SSS");
			fileName = tempExcelFilePath + File.separator + "Worldcheck_" + excelFileName + "_" + sdf.format(new Date())
					+ ".xlsx";
			File excelFile = new File(fileName);
			if (excelFile.exists()) {
				excelFile.delete();
			}

			logger.debug("rowCount::" + rowCount);
			if (tabType != 4 && tabType != 5) {
				plotGraph(workbook, sizeOfReport, sheetNameCases, sheetNameRevenue, deface, rowCount);
			} else {
				plotGraphForClientAndCountry(workbook, sizeOfReport, sheetNameCases, sheetNameRevenue, deface,
						rowCount);
			}

			logger.debug("end row count::" + new Date());
			fos = new FileOutputStream(excelFile);
			workbook.write(fos);
			fos.close();
		} catch (FileNotFoundException var41) {
			throw new CMSException(logger, var41);
		} catch (IOException var42) {
			throw new CMSException(logger, var42);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException var40) {
					throw new CMSException(logger, var40);
				}
			}

			if (fis != null) {
				try {
					fis.close();
				} catch (IOException var39) {
					throw new CMSException(logger, var39);
				}
			}

		}

		logger.debug("out ExcelDownloader::exportChartToExcelByCountry");
		return fileName;
	}

	public static String exportChartToExcelForTAT(List<String> headerCaseList, List<List<String>> dataCaseList,
			List<String> crdHeaderList, List<List<String>> crddataList, HttpServletResponse response,
			String excelFileName, RevenueSummaryVO revenueSummaryVO, int sizeOfReport)
			throws CMSException, IOException, InvalidFormatException {
		logger.debug("in ChartExcelDownloader::exportChartToExcelForTAT::" + new Date());
		logger.debug("in ChartExcelDownloader::exportChartToExcelForTAT::dataList;" + dataCaseList);
		FileOutputStream fos = null;
		FileInputStream fis = null;
		String fileName = "";
		String tempExcelFilePath = "";
		byte deface = 1;

		try {
			int rowCount = 0;
			int firstRowCount = 0;
			int firstColumnCount = 0;
			int columnCount = 0;
			FILE_DIR = propertyReader.getRevenueSummaryTemplatePath();
			logger.debug("File dir::" + FILE_DIR);
			fis = new FileInputStream(new File(FILE_DIR, FILE_NAME_BY_TAT_HISTOGRAM));
			Workbook workbook = new XSSFWorkbook(OPCPackage.open(fis));
			CreationHelper createHelperYAxisCases = workbook.getCreationHelper();
			CellStyle cellStyleYAxisCases = workbook.createCellStyle();
			cellStyleYAxisCases.setDataFormat(createHelperYAxisCases.createDataFormat().getFormat("#,##0"));

			String temp;
			for (int i = 1; i < workbook.getNumberOfSheets(); ++i) {
				temp = workbook.getSheetAt(i).getSheetName();
				if (!temp.equalsIgnoreCase(SHEET_BY_CASES + sizeOfReport)
						&& !temp.equalsIgnoreCase(SHEET_BY_REVENUES + sizeOfReport)) {
					workbook.setSheetHidden(i, true);
					workbook.setSheetName(i, workbook.getSheetName(i) + i);
				}
			}

			logger.debug("in ChartExcelDownloader::writing first sheet");
			Sheet firstSheet = workbook.getSheetAt(0);
			temp = firstSheet.getSheetName();
			logger.debug("firstSheetname::" + temp);
			int var55 = firstRowCount + 1;
			Row rowFirstSheet = firstSheet.createRow(firstRowCount);
			List<String> subCrdDataList = null;
			logger.debug("Time before execution::" + new Date());
			Iterator var26 = crdHeaderList.iterator();

			while (var26.hasNext()) {
				String headerName = (String) var26.next();
				Cell cel = rowFirstSheet.createCell(firstColumnCount++, 1);
				cel.setCellValue(getNullReplacedValue(headerName));
			}

			logger.debug("iterating CRD datalist");
			int cellCount = false;
			Iterator dataListIterator = crddataList.iterator();

			label290 : while (true) {
				Row rowNext;
				int cellCount;
				do {
					if (!dataListIterator.hasNext()) {
						crddataList = null;
						logger.debug("iterating Cases And Revenue");
						String sheetNameCases = SHEET_BY_CASES + sizeOfReport;
						Sheet sh = workbook.getSheet(sheetNameCases);
						sheetNameCases = sheetNameCases.substring(0, sheetNameCases.length() - 1) + SHEET_SUFFIX_BY_TAT;
						workbook.setSheetName(workbook.getSheetIndex(sh), sheetNameCases);

						for (int x = 0; x <= sizeOfReport; ++x) {
							sh.getRow(1).getCell(x).setCellValue("");
						}

						logger.debug("in ChartExcelDownloader::writing second sheet");
						int rowCount = rowCount + 1;
						Row row = sh.createRow(rowCount);
						sh.getRow(0).createCell(3).setCellValue(revenueSummaryVO.getYaxisFirstGraphField());
						List<String> subDataList = null;
						Iterator var34 = headerCaseList.iterator();

						while (var34.hasNext()) {
							String headerName = (String) var34.next();
							Cell cel2 = row.createCell(columnCount++, 1);
							cel2.setCellValue(getNullReplacedValue(headerName));
						}

						logger.debug("iterating second datalist");
						Iterator dataListIterator = dataCaseList.iterator();

						while (true) {
							Row rowNext1;
							do {
								if (!dataListIterator.hasNext()) {
									dataCaseList = null;
									logger.debug("iterating third datalist");

									for (int i = 1; i < workbook.getNumberOfSheets(); ++i) {
										if (workbook.isSheetHidden(i)) {
											workbook.removeSheetAt(i);
											--i;
										}
									}

									tempExcelFilePath = propertyReader.getTempExcelFilePath();
									File dir = new File(tempExcelFilePath);
									if (!dir.exists()) {
										(new File(tempExcelFilePath)).mkdirs();
									}

									SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss.SSS");
									fileName = tempExcelFilePath + File.separator + "Worldcheck_" + excelFileName + "_"
											+ sdf.format(new Date()) + ".xlsx";
									File excelFile = new File(fileName);
									if (excelFile.exists()) {
										excelFile.delete();
									}

									logger.debug("rowCount::" + rowCount);
									int x = 65;

									for (int i = 0; i <= sizeOfReport; ++i) {
										if (i == 0) {
											rangeCellCases = workbook.getName("Date");
											referenceCases = sheetNameCases + "!$" + (char) x + "$" + (deface + 1)
													+ ":$" + (char) x + "$" + rowCount;
											rangeCellCases.setRefersToFormula(referenceCases);
											++x;
										} else {
											rangeCellCases = workbook.getName("Case" + i);
											referenceCases = sheetNameCases + "!$" + (char) x + "$" + (deface + 1)
													+ ":$" + (char) x + "$" + rowCount;
											rangeCellCases.setRefersToFormula(referenceCases);
											++x;
										}
									}

									logger.debug("end row count::" + new Date());
									fos = new FileOutputStream(excelFile);
									workbook.write(fos);
									fos.close();
									break label290;
								}

								cellCount = 0;
								subDataList = (List) dataListIterator.next();
								rowNext1 = sh.createRow(rowCount++);
							} while (subDataList == null);

							Iterator iterator = subDataList.iterator();

							while (iterator.hasNext()) {
								Cell cel4 = rowNext1.createCell((short) (cellCount++));
								if (cellCount == 1) {
									cel4.setCellValue(getNullReplacedValue((String) iterator.next()));
								} else {
									cel4.setCellValue(
											(double) Integer.parseInt(getNullReplacedValue((String) iterator.next())));
									cel4.setCellStyle(cellStyleYAxisCases);
								}
							}
						}
					}

					cellCount = 0;
					subCrdDataList = (List) dataListIterator.next();
					rowNext = firstSheet.createRow(var55++);
				} while (subCrdDataList == null);

				Iterator iterator = subCrdDataList.iterator();

				while (iterator.hasNext()) {
					Cell cel1 = rowNext.createCell(cellCount++);
					cel1.setCellValue(getNullReplacedValue((String) iterator.next()));
				}
			}
		} catch (FileNotFoundException var51) {
			throw new CMSException(logger, var51);
		} catch (IOException var52) {
			throw new CMSException(logger, var52);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException var50) {
					throw new CMSException(logger, var50);
				}
			}

			if (fis != null) {
				try {
					fis.close();
				} catch (IOException var49) {
					throw new CMSException(logger, var49);
				}
			}

		}

		logger.debug("out ExcelDownloader::exportChartToExcelForTAT");
		return fileName;
	}

	public static String writeToExcelForDelivery(List<String> headerCaseList, List<List<String>> dataCaseList,
			List<String> crdHeaderList, List<List<String>> crddataList, HttpServletResponse response,
			String excelFileName, RevenueSummaryVO revenueSummaryVO, int sizeOfReport)
			throws CMSException, IOException, InvalidFormatException {
		logger.debug("in ChartExcelDownloader::writeToExcel1::" + new Date());
		logger.debug("in ChartExcelDownloader::writeToExcel1::dataList;" + dataCaseList);
		logger.debug("Size of report for first tab::" + sizeOfReport);
		FileOutputStream fos = null;
		FileInputStream fis = null;
		String fileName = "";
		String tempExcelFilePath = "";
		byte deface = 1;

		try {
			int rowCount = 0;
			int firstRowCount = 0;
			int firstColumnCount = 0;
			int columnCount = 0;
			FILE_DIR = propertyReader.getRevenueSummaryTemplatePath();
			logger.debug("File dir::" + FILE_DIR);
			fis = new FileInputStream(new File(FILE_DIR, FILE_NAME_BY_DELIVERY));
			Workbook workbook = new XSSFWorkbook(OPCPackage.open(fis));
			CreationHelper createHelperYAxisCases = workbook.getCreationHelper();
			CellStyle cellStyleYAxisCases = workbook.createCellStyle();
			cellStyleYAxisCases.setDataFormat(createHelperYAxisCases.createDataFormat().getFormat("0.0%"));

			String temp;
			for (int i = 1; i < workbook.getNumberOfSheets(); ++i) {
				temp = workbook.getSheetAt(i).getSheetName();
				if (!temp.equalsIgnoreCase(SHEET_BY_CASES + sizeOfReport)
						&& !temp.equalsIgnoreCase(SHEET_BY_REVENUES + sizeOfReport)) {
					logger.debug("Sheet Name before edit##" + workbook.getSheetName(i));
					workbook.setSheetHidden(i, true);
					workbook.setSheetName(i, workbook.getSheetName(i) + "demo");
					logger.debug("Sheet Name after edit##" + workbook.getSheetName(i));
				}
			}

			logger.debug("in ChartExcelDownloader::writing first sheet");
			Sheet firstSheet = workbook.getSheetAt(0);
			temp = firstSheet.getSheetName();
			logger.debug("firstSheetname::" + temp);
			int var55 = firstRowCount + 1;
			Row rowFirstSheet = firstSheet.createRow(firstRowCount);
			List<String> subCrdDataList = null;
			logger.debug("Time before execution::" + new Date());
			Iterator var26 = crdHeaderList.iterator();

			while (var26.hasNext()) {
				String headerName = (String) var26.next();
				Cell cel = rowFirstSheet.createCell(firstColumnCount++, 1);
				cel.setCellValue(getNullReplacedValue(headerName));
			}

			logger.debug("iterating CRD datalist");
			int cellCount = false;
			Iterator dataListIterator = crddataList.iterator();

			label290 : while (true) {
				Row rowNext;
				int cellCount;
				do {
					if (!dataListIterator.hasNext()) {
						crddataList = null;
						String sheetNameCases = SHEET_BY_CASES + sizeOfReport;
						Sheet sh = workbook.getSheet(sheetNameCases);
						sheetNameCases = sheetNameCases.substring(0, sheetNameCases.lastIndexOf("s") + 1)
								+ SHEET_SUFFIX_BY_DELIVERY;
						workbook.setSheetName(workbook.getSheetIndex(sh), sheetNameCases);

						for (int x = 0; x <= sizeOfReport; ++x) {
							sh.getRow(1).getCell(x).setCellValue("");
						}

						sh.createRow(19).createCell(0).setCellValue(revenueSummaryVO.getYaxisFirstGraphField());
						sh.createRow(20).createCell(0).setCellValue(revenueSummaryVO.getHeaderFirstGraphField());
						logger.debug("in ChartExcelDownloader::writing second sheet");
						int rowCount = rowCount + 1;
						Row row = sh.createRow(rowCount);
						List<String> subDataList = null;
						Iterator var34 = headerCaseList.iterator();

						while (var34.hasNext()) {
							String headerName = (String) var34.next();
							Cell cel2 = row.createCell(columnCount++, 1);
							cel2.setCellValue(getNullReplacedValue(headerName));
						}

						logger.debug("iterating second datalist");
						Iterator dataListIterator = dataCaseList.iterator();

						while (true) {
							Row rowNext1;
							do {
								if (!dataListIterator.hasNext()) {
									dataCaseList = null;
									logger.debug("iterating third datalist");

									for (int i = 1; i < workbook.getNumberOfSheets(); ++i) {
										if (workbook.isSheetHidden(i)) {
											workbook.removeSheetAt(i);
											--i;
										}
									}

									tempExcelFilePath = propertyReader.getTempExcelFilePath();
									File dir = new File(tempExcelFilePath);
									if (!dir.exists()) {
										(new File(tempExcelFilePath)).mkdirs();
									}

									SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss.SSS");
									fileName = tempExcelFilePath + File.separator + "Worldcheck_" + excelFileName + "_"
											+ sdf.format(new Date()) + ".xlsx";
									File excelFile = new File(fileName);
									if (excelFile.exists()) {
										excelFile.delete();
									}

									logger.debug("rowCount::" + rowCount);
									int x = 65;

									for (int i = 0; i <= sizeOfReport; ++i) {
										if (i == 0) {
											rangeCellCases = workbook.getName("Date");
											referenceCases = sheetNameCases + "!$" + (char) x + "$" + (deface + 1)
													+ ":$" + (char) x + "$" + rowCount;
											rangeCellCases.setRefersToFormula(referenceCases);
											++x;
										} else {
											rangeCellCases = workbook.getName("Case" + i);
											referenceCases = sheetNameCases + "!$" + (char) x + "$" + (deface + 1)
													+ ":$" + (char) x + "$" + rowCount;
											rangeCellCases.setRefersToFormula(referenceCases);
											++x;
										}
									}

									logger.debug("end row count::" + new Date());
									fos = new FileOutputStream(excelFile);
									workbook.write(fos);
									fos.close();
									break label290;
								}

								cellCount = 0;
								subDataList = (List) dataListIterator.next();
								rowNext1 = sh.createRow(rowCount++);
							} while (subDataList == null);

							Iterator iterator = subDataList.iterator();

							while (iterator.hasNext()) {
								Cell cel4 = rowNext1.createCell((short) (cellCount++));
								if (cellCount == 1) {
									cel4.setCellValue(getNullReplacedValue((String) iterator.next()));
								} else {
									cel4.setCellValue(
											(double) Float.parseFloat(getNullReplacedValue((String) iterator.next())));
									cel4.setCellStyle(cellStyleYAxisCases);
								}
							}
						}
					}

					cellCount = 0;
					subCrdDataList = (List) dataListIterator.next();
					rowNext = firstSheet.createRow(var55++);
				} while (subCrdDataList == null);

				Iterator iterator = subCrdDataList.iterator();

				while (iterator.hasNext()) {
					Cell cel1 = rowNext.createCell(cellCount++);
					cel1.setCellValue(getNullReplacedValue((String) iterator.next()));
				}
			}
		} catch (FileNotFoundException var51) {
			throw new CMSException(logger, var51);
		} catch (IOException var52) {
			throw new CMSException(logger, var52);
		} finally {
			if (fos != null) {
				try {
					fos.close();
					logger.debug("FOS close");
				} catch (IOException var50) {
					throw new CMSException(logger, var50);
				}
			}

			if (fis != null) {
				try {
					fis.close();
					logger.debug("FIS close");
				} catch (IOException var49) {
					throw new CMSException(logger, var49);
				}
			}

		}

		logger.debug("out ExcelDownloader::writeToExcelForDelivery");
		return fileName;
	}

	private static void removeSheet(Workbook workbook) {
		for (int i = 1; i < workbook.getNumberOfSheets(); ++i) {
			if (workbook.isSheetHidden(i)) {
				workbook.removeSheetAt(i);
				--i;
			}
		}

	}

	private static void renameSheet(Workbook workbook, int sizeOfReport) {
		for (int i = 1; i < workbook.getNumberOfSheets(); ++i) {
			String temp = workbook.getSheetAt(i).getSheetName();
			if (!temp.equalsIgnoreCase(SHEET_BY_CASES + sizeOfReport)
					&& !temp.equalsIgnoreCase(SHEET_BY_REVENUES + sizeOfReport)) {
				workbook.setSheetHidden(i, true);
				workbook.setSheetName(i, workbook.getSheetName(i) + "demo");
			}
		}

	}

	private static void writeDataInFirstSheet(Workbook workbook, int firstRowCount, int firstColumnCount,
			List<String> crdHeaderList, List<List<String>> crddataList) {
		logger.debug("in ChartExcelDownloader::writing first sheet");
		Sheet firstSheet = workbook.getSheetAt(0);
		String firstSheetname = firstSheet.getSheetName();
		logger.debug("firstSheetname::" + firstSheetname);
		Row rowFirstSheet = firstSheet.createRow(firstRowCount++);
		List<String> subCrdDataList = null;
		logger.debug("Time before execution::" + new Date());
		Iterator var11 = crdHeaderList.iterator();

		while (var11.hasNext()) {
			String headerName = (String) var11.next();
			Cell cel = rowFirstSheet.createCell(firstColumnCount++, 1);
			cel.setCellValue(getNullReplacedValue(headerName));
		}

		logger.debug("iterating CRD datalist");
		int cellCount = false;
		Iterator dataListIterator = crddataList.iterator();

		while (true) {
			Row rowNext;
			int cellCount;
			do {
				if (!dataListIterator.hasNext()) {
					crddataList = null;
					return;
				}

				cellCount = 0;
				subCrdDataList = (List) dataListIterator.next();
				rowNext = firstSheet.createRow(firstRowCount++);
			} while (subCrdDataList == null);

			Iterator iterator = subCrdDataList.iterator();

			while (iterator.hasNext()) {
				Cell cel1 = rowNext.createCell(cellCount++);
				cel1.setCellValue(getNullReplacedValue((String) iterator.next()));
			}
		}
	}

	private static int writeDataInSecondSheet(Workbook workbook, Sheet sh, Row row, List<String> headerCaseList,
			List<List<String>> dataCaseList) {
		List<String> subDataList = null;
		int cellCount = false;
		int columnCount = 0;
		int rowCount = 1;
		CreationHelper createHelperYAxisCases = workbook.getCreationHelper();
		CellStyle cellStyleYAxisCases = workbook.createCellStyle();
		cellStyleYAxisCases.setDataFormat(createHelperYAxisCases.createDataFormat().getFormat("#,##0"));
		Iterator var13 = headerCaseList.iterator();

		while (var13.hasNext()) {
			String headerName = (String) var13.next();
			Cell cel2 = row.createCell(columnCount++, 1);
			cel2.setCellValue(getNullReplacedValue(headerName));
		}

		logger.debug("iterating second datalist");
		Iterator dataListIterator = dataCaseList.iterator();

		while (true) {
			int cellCount;
			Row rowNext1;
			do {
				if (!dataListIterator.hasNext()) {
					dataCaseList = null;
					return rowCount;
				}

				cellCount = 0;
				subDataList = (List) dataListIterator.next();
				rowNext1 = sh.createRow(rowCount++);
			} while (subDataList == null);

			Iterator iterator = subDataList.iterator();

			while (iterator.hasNext()) {
				Cell cel4 = rowNext1.createCell((short) (cellCount++));
				if (cellCount == 1) {
					cel4.setCellValue(getNullReplacedValue((String) iterator.next()));
				} else {
					cel4.setCellValue((double) Integer.parseInt(getNullReplacedValue((String) iterator.next())));
					cel4.setCellStyle(cellStyleYAxisCases);
				}
			}
		}
	}

	private static void writeDataInThirdSheet(Workbook workbook, Sheet sh2, Row row1, List<String> headerRevenueList,
			List<List<String>> dataRevenueList) {
		List<String> subDataList = null;
		logger.debug("iterating third datalist");
		int columnCount = 0;
		int cellCount = false;
		int rowCount2 = 1;
		CreationHelper createHelperYAxisRevenue = workbook.getCreationHelper();
		CellStyle cellStyleYAxisRevenue = workbook.createCellStyle();
		cellStyleYAxisRevenue.setDataFormat(createHelperYAxisRevenue.createDataFormat().getFormat("$#,##0"));
		Iterator iterator = headerRevenueList.iterator();

		while (iterator.hasNext()) {
			String headerName = (String) iterator.next();
			Cell cel3 = row1.createCell(columnCount++, 1);
			cel3.setCellValue(getNullReplacedValue(headerName));
		}

		Iterator dataListIterator = dataRevenueList.iterator();

		while (true) {
			Row rowNext2;
			int cellCount;
			do {
				if (!dataListIterator.hasNext()) {
					dataRevenueList = null;
					return;
				}

				cellCount = 0;
				subDataList = (List) dataListIterator.next();
				rowNext2 = sh2.createRow(rowCount2++);
			} while (subDataList == null);

			iterator = subDataList.iterator();

			while (iterator.hasNext()) {
				Cell cel5 = rowNext2.createCell(cellCount++);
				if (cellCount == 1) {
					cel5.setCellValue(getNullReplacedValue((String) iterator.next()));
				} else {
					cel5.setCellValue((double) Float.parseFloat(getNullReplacedValue((String) iterator.next())));
					cel5.setCellStyle(cellStyleYAxisRevenue);
				}
			}
		}
	}

	private static void plotGraph(Workbook workbook, int sizeOfReport, String sheetNameCases, String sheetNameRevenue,
			int deface, int rowCount) {
		int x = 65;

		int i;
		for (i = 0; i <= sizeOfReport; ++i) {
			if (i == 0) {
				rangeCellCases = workbook.getName("Date");
				referenceCases = sheetNameCases + "!$" + (char) x + "$" + (deface + 1) + ":$" + (char) x + "$"
						+ rowCount;
				rangeCellCases.setRefersToFormula(referenceCases);
				++x;
			} else {
				rangeCellCases = workbook.getName("Case" + i);
				referenceCases = sheetNameCases + "!$" + (char) x + "$" + (deface + 1) + ":$" + (char) x + "$"
						+ rowCount;
				rangeCellCases.setRefersToFormula(referenceCases);
				++x;
			}
		}

		x = 65;

		for (i = 0; i <= sizeOfReport; ++i) {
			if (i == 0) {
				rangeCellRevenue = workbook.getName("Date");
				referenceRevenue = sheetNameRevenue + "!$" + (char) x + "$" + (deface + 1) + ":$" + (char) x + "$"
						+ rowCount;
				rangeCellRevenue.setRefersToFormula(referenceRevenue);
				++x;
			} else {
				rangeCellRevenue = workbook.getName("Revenue" + i);
				referenceRevenue = sheetNameRevenue + "!$" + (char) x + "$" + (deface + 1) + ":$" + (char) x + "$"
						+ rowCount;
				rangeCellRevenue.setRefersToFormula(referenceRevenue);
				++x;
			}
		}

	}

	private static void plotGraphForClientAndCountry(Workbook workbook, int sizeOfReport, String sheetNameCases,
			String sheetNameRevenue, int deface, int rowCount) {
		int x = 65;

		int i;
		for (i = 0; i <= sizeOfReport; ++i) {
			if (i == 0) {
				rangeCellCases = workbook.getName("Date");
				referenceCases = sheetNameCases + "!$" + (char) x + "$" + (deface + 1) + ":$" + (char) x + "$"
						+ rowCount;
				rangeCellCases.setRefersToFormula(referenceCases);
				++x;
			} else {
				rangeCellCases = workbook.getName("Case" + i);
				referenceCases = sheetNameCases + "!$" + (char) x + "$" + (deface + 1) + ":$" + (char) x + "$"
						+ rowCount;
				rangeCellCases.setRefersToFormula(referenceCases);
				++x;
			}
		}

		x = 65;

		for (i = 0; i <= sizeOfReport; ++i) {
			if (i == 0) {
				rangeCellRevenue = workbook.getName("DateRevenue");
				referenceRevenue = sheetNameRevenue + "!$" + (char) x + "$" + (deface + 1) + ":$" + (char) x + "$"
						+ rowCount;
				rangeCellRevenue.setRefersToFormula(referenceRevenue);
				++x;
			} else {
				rangeCellRevenue = workbook.getName("Revenue" + i);
				referenceRevenue = sheetNameRevenue + "!$" + (char) x + "$" + (deface + 1) + ":$" + (char) x + "$"
						+ rowCount;
				rangeCellRevenue.setRefersToFormula(referenceRevenue);
				++x;
			}
		}

	}
}