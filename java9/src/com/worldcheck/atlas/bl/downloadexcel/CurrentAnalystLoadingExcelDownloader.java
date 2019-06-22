package com.worldcheck.atlas.bl.downloadexcel;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

public class CurrentAnalystLoadingExcelDownloader {
	private static ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.downloadexcel.CurrentAnalystLoadingExcelDownloader");
	private static final String DEFAULT_SHEET_NAME = "Sheet 1";
	private static final String EXCEL_FILE_PREFIX = "Worldcheck_";
	private static final String EXCEL_FILE_SUFFIX = ".xls";
	private static final String UNDERSCORE = "_";
	private static PropertyReaderUtil propertyReader;
	private static final String DECIMAL_FORMAT = "0.#########################";
	private static final String NULL_REPLACEMENT = "";
	private static final String NULL_STRING = "null";
	private static String ITRM1 = "Itrm1";
	private static String ITRM2 = "Itrm2";
	private static String FINALDATE = "FinalDate";
	private static String LEAVEDATE = "leaveDate";
	private static String LEAVETYPE = "leaveType";
	private static String PRIMARYSUBJECT = "Primary Subject";
	private static String TASK = "Task";
	private static String PRIMARYSUBCOUNTRY = "Primary Subject Country";
	private static String SAT = "SAT";
	private static String SATSeprator = "#SAT#";
	private static String CASESTATUS = "caseStatus";
	public static Date datVal;
	public static String dupCRN = "";
	public static String dupTask = "";
	public static String Dollar = "$";
	public static Date dupDate;
	public static int rowFlag = 0;

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public static String writeToExcel(List<String> lstHeader, List<String> lstSubHeader1, List<String> lstSubHeader2,
			List<HashMap<String, String>> dataList, String sheetName, short hssfForeColorIndex,
			short hssfBackgroundColourIndex, HttpServletResponse response, String excelFileName, int subCellCount,
			String startDate, String endDate, String phd) throws CMSException, ParseException {
		Date d = new Date();
		logger.debug("in ExcelDownloader::writeToExcel" + d.getTime());
		FileOutputStream fos = null;
		String fileName = "";
		String tempExcelFilePath = "";
		SimpleDateFormat datFormat = new SimpleDateFormat("dd MMM yy");
		SimpleDateFormat datFormat2 = new SimpleDateFormat("dd-MMM-yy");
		int MILLIS_IN_DAY = 86400000;
		Date date = datFormat.parse(startDate);
		datFormat.parse(endDate);
		dupDate = new Date(date.getTime() - (long) MILLIS_IN_DAY);
		logger.debug("DuplicateDate--------------" + dupDate);

		try {
			int rowCount = 0;
			int columnCount = 0;
			HSSFSheet sht = null;
			HSSFWorkbook workbook = createWorkBook();
			HSSFCellStyle headerCellStyle2 = createHeaderStyle(workbook, (short) 9, (short) 40);
			HSSFCellStyle cellStyleIntrim1 = createStyle(workbook, (short) 9, (short) 13);
			HSSFCellStyle cellStyleIntrim2 = createStyle(workbook, (short) 9, (short) 53);
			HSSFCellStyle cellStyleFinal = createStyle(workbook, (short) 9, (short) 11);
			HSSFCellStyle onHoldType = createStyle(workbook, (short) 9, (short) 23);
			HSSFCellStyle headerCellStyle;
			if (hssfForeColorIndex == 0 && hssfBackgroundColourIndex == 0) {
				headerCellStyle = createHeaderStyle(workbook);
			} else {
				headerCellStyle = createHeaderStyle(workbook, hssfForeColorIndex, hssfBackgroundColourIndex);
			}

			if (sheetName != null && !sheetName.isEmpty()) {
				sht = workbook.createSheet(sheetName);
			} else {
				sht = workbook.createSheet("Sheet 1");
			}

			sht.createFreezePane(3, 3, 4, 4);
			int rowCount = rowCount + 1;
			HSSFRow row = sht.createRow(rowCount);
			HashMap<String, String> hashMap = null;
			Iterator var35 = lstHeader.iterator();

			while (var35.hasNext()) {
				String headerName = (String) var35.next();
				sht.setColumnWidth((short) columnCount, (short) 5000);
				HSSFCell cel = row.createCell((short) (columnCount++), 1);
				cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(headerName)));
				cel.setCellStyle(headerCellStyle);
			}

			mergeCustom(0, 2, 0, 0, sht);
			mergeCustom(0, 2, 1, 1, sht);
			mergeCustom(0, 2, 2, 2, sht);
			mergeCustom(0, 2, 3, 3, sht);
			mergeCustom(0, 2, 4, 4, sht);
			mergeCustom(0, 2, 5, 5, sht);
			mergeCustom(0, 0, 6, subCellCount + 5, sht);
			HSSFRow row2 = sht.createRow(rowCount++);
			columnCount = 6;
			Iterator var84 = lstSubHeader1.iterator();

			while (var84.hasNext()) {
				String headerName = (String) var84.next();
				sht.setColumnWidth((short) columnCount, (short) 5000);
				HSSFCell cel = row2.createCell((short) (columnCount++), 1);
				cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(headerName)));
				cel.setCellStyle(headerCellStyle);
			}

			HSSFRow row3 = sht.createRow(rowCount++);
			columnCount = 6;
			Iterator var87 = lstSubHeader2.iterator();

			while (var87.hasNext()) {
				String headerName = (String) var87.next();
				sht.setColumnWidth((short) columnCount, (short) 5000);
				HSSFCell cel = row3.createCell((short) (columnCount++), 1);
				cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(headerName)));
				if (headerName.equalsIgnoreCase("S")) {
					cel.setCellStyle(headerCellStyle2);
				} else {
					cel.setCellStyle(headerCellStyle);
				}
			}

			rowCount = 3;
			logger.debug("iterating datalist");
			HSSFFont font = workbook.createFont();
			font.setFontName("Arial Unicode MS");
			rowCount = rowCount + 1;
			HSSFRow rowNext = sht.createRow(rowCount);
			Iterator dataListIterator = dataList.iterator();

			label921 : while (true) {
				Date date2;
				int cellCount;
				do {
					if (!dataListIterator.hasNext()) {
						rowCount += 5;
						rowNext = sht.createRow(rowCount);
						int cellNum = 6;
						String value = "";

						for (int k = 0; k < 5; ++k) {
							HSSFCellStyle cellStyle = workbook.createCellStyle();
							if (k == 0) {
								value = "legend:";
							} else if (k == 1) {
								++cellNum;
								value = "Interim1";
								cellStyle = cellStyleIntrim1;
							} else if (k == 2) {
								++cellNum;
								value = "Interim2";
								cellStyle = cellStyleIntrim2;
							} else if (k == 3) {
								++cellNum;
								value = "Final";
								cellStyle = cellStyleFinal;
							} else if (k == 4) {
								++cellNum;
								value = "On-Hold";
								cellStyle = onHoldType;
							}

							HSSFCell cel = rowNext.createCell((short) (cellNum++));
							cellStyle.setFont(font);
							cel.setCellStyle(cellStyle);
							cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(value)));
						}

						tempExcelFilePath = propertyReader.getTempExcelFilePath();
						File dir = new File(tempExcelFilePath);
						if (!dir.exists()) {
							(new File(tempExcelFilePath)).mkdirs();
						}

						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss.SSS");
						fileName = tempExcelFilePath + File.separator + "Worldcheck_" + excelFileName + "_"
								+ sdf.format(new Date()) + ".xls";
						File excelFile = new File(fileName);
						if (excelFile.exists()) {
							excelFile.delete();
						}

						fos = new FileOutputStream(excelFile);
						workbook.write(fos);
						break label921;
					}

					cellCount = 0;
					hashMap = (HashMap) dataListIterator.next();
					dupCRN = "";
					dupTask = "";
					dupDate = new Date(date.getTime() - (long) MILLIS_IN_DAY);
					rowFlag = 0;
					date = datFormat.parse(startDate);
					date2 = datFormat.parse(endDate);
				} while (hashMap == null);

				Iterator mapIterator = hashMap.keySet().iterator();

				while (true) {
					String key;
					do {
						if (!mapIterator.hasNext()) {
							continue label921;
						}

						key = (String) mapIterator.next();
						if (key.equalsIgnoreCase("CRN")) {
							String[] phDay = (String[]) null;
							if (phd != null) {
								phDay = phd.split(",");
							}

							String[] crnval = ((String) hashMap.get(key)).split(",");
							String[] intrm1 = ((String) hashMap.get(ITRM1)).split(",");
							String[] intrm2 = ((String) hashMap.get(ITRM2)).split(",");
							String[] finalDate = ((String) hashMap.get(FINALDATE)).split(",");
							String[] leaveDate = ((String) hashMap.get(LEAVEDATE)).split(",");
							String[] leaveType = ((String) hashMap.get(LEAVETYPE)).split(",");
							String[] primarySubCountArr = ((String) hashMap.get(PRIMARYSUBCOUNTRY)).split("SP~@@");
							String[] primarySubArr = ((String) hashMap.get(PRIMARYSUBJECT)).split("SP~@@");
							String[] caseStatus = ((String) hashMap.get(CASESTATUS)).split(",");
							String[] activeTask = (String[]) null;
							if (hashMap.get(TASK) != null && !((String) hashMap.get(TASK)).isEmpty()) {
								activeTask = ((String) hashMap.get(TASK)).split(",");
							}

							int firstRow = rowCount - 1;
							int limit = crnval.length;
							int lastRow = firstRow + limit - 1;

							int i;
							for (i = 0; i < crnval.length; ++i) {
								HSSFCell cel;
								String cellValue;
								if (primarySubArr[i] != null && !primarySubArr[i].equalsIgnoreCase("null")) {
									cel = rowNext.createCell((short) (cellCount++));
									cellValue = primarySubArr[i];
									cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(cellValue)));
								} else {
									++cellCount;
								}

								if (primarySubCountArr[i] != null && !primarySubCountArr[i].equalsIgnoreCase("null")) {
									cel = rowNext.createCell((short) (cellCount++));
									cellValue = primarySubCountArr[i];
									cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(cellValue)));
								} else {
									++cellCount;
								}

								if (activeTask != null && activeTask[i] != null
										&& !activeTask[i].equalsIgnoreCase("null")) {
									cel = rowNext.createCell((short) (cellCount++));
									cellValue = activeTask[i];
									cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(cellValue)));
								} else {
									++cellCount;
								}

								date = datFormat.parse(startDate);

								for (String crnValue = ""; date.getTime() <= date2
										.getTime(); date = new Date(date.getTime() + (long) MILLIS_IN_DAY)) {
									Date itm1 = null;
									Date itm2 = null;
									Date fnlDate = null;
									if (i < intrm1.length && intrm1[i] != null && intrm1[i].length() > 0
											&& !"null".equals(intrm1[i])) {
										itm1 = datFormat2.parse(intrm1[i]);
									}

									if (i < intrm2.length && intrm2[i] != null && intrm2[i].length() > 0
											&& !"null".equals(intrm2[i])) {
										itm2 = datFormat2.parse(intrm2[i]);
									}

									if (i < finalDate.length && finalDate[i] != null && finalDate[i].length() > 0
											&& !"null".equals(finalDate[i])) {
										fnlDate = datFormat2.parse(finalDate[i]);
									}

									crnValue = "";
									if (phDay != null) {
										for (int phindex = 0; phindex < phDay.length; ++phindex) {
											if (date.compareTo(datFormat2.parse(phDay[phindex])) == 0) {
												crnValue = "PH";
												break;
											}
										}
									}

									int a;
									HSSFCell cel;
									if ((itm1 == null || date.compareTo(itm1) != 0)
											&& (itm2 == null || date.compareTo(itm2) != 0)
											&& (fnlDate == null || date.compareTo(fnlDate) != 0)) {
										boolean onlyCellCountFlag = true;

										for (a = 0; a < leaveDate.length; ++a) {
											boolean isRemove = true;
											if (!leaveDate[a].equalsIgnoreCase("null")
													&& !leaveDate[a].equalsIgnoreCase("")
													&& date.compareTo(datFormat2.parse(leaveDate[a])) == 0) {
												for (int datlen = 0; datlen < intrm1.length; ++datlen) {
													Date itrm1 = datFormat2.parse(intrm1[datlen]);
													Date itrm2 = datFormat2.parse(intrm2[datlen]);
													Date fnlDat = datFormat2.parse(finalDate[datlen]);
													if (datFormat2.parse(leaveDate[a]).compareTo(itrm1) == 0
															|| datFormat2.parse(leaveDate[a]).compareTo(itrm2) == 0
															|| datFormat2.parse(leaveDate[a]).compareTo(fnlDat) == 0) {
														isRemove = false;
													}
												}

												if (isRemove) {
													HSSFCell cel = rowNext.createCell((short) (cellCount++));
													HSSFCellStyle cellStyle = workbook.createCellStyle();
													cel.setCellStyle(cellStyle);
													if (crnValue.trim().length() > 0) {
														crnValue = crnValue + "\n" + leaveType[a];
													} else {
														crnValue = leaveType[a];
													}

													cel.setCellValue(
															new HSSFRichTextString(getNullReplacedValue(crnValue)));
													crnValue = "";
													onlyCellCountFlag = false;
													leaveDate[a] = "";
												} else {
													onlyCellCountFlag = true;
												}
												break;
											}
										}

										if (crnValue.trim().length() > 0) {
											cel = rowNext.createCell((short) (cellCount++));
											cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(crnValue)));
										} else if (onlyCellCountFlag) {
											++cellCount;
										}
									} else {
										HSSFCellStyle cellStyle = workbook.createCellStyle();
										if (dupCRN.equalsIgnoreCase("")
												|| crnval[i].equalsIgnoreCase(dupCRN) && date.compareTo(dupDate) != 0
														&& rowFlag == rowCount
												|| !crnval[i].equalsIgnoreCase(dupCRN) && date.compareTo(dupDate) == 0
														&& rowFlag != rowCount
												|| crnval[i].equalsIgnoreCase(dupCRN) && date.compareTo(dupDate) != 0
														&& rowFlag != rowCount
												|| !crnval[i].equalsIgnoreCase(dupCRN) && date.compareTo(dupDate) != 0
												|| activeTask != null && activeTask.length > 0 && activeTask[i] != null
														&& crnval[i].equalsIgnoreCase(dupCRN)
														&& date.compareTo(dupDate) == 0 && rowFlag != rowCount
														&& !activeTask[i].equalsIgnoreCase(dupTask)) {
											dupCRN = crnval[i];
											if (activeTask != null && activeTask.length > 0 && activeTask[i] != null) {
												dupTask = activeTask[i];
											}

											rowFlag = rowCount;
											if (crnval[i] != null && !crnval[i].isEmpty()
													&& date.compareTo(itm1) == 0) {
												dupDate = itm1;
												if (caseStatus[i] != null && caseStatus[i].equals("On Hold")) {
													cellStyle = onHoldType;
												} else {
													cellStyle = cellStyleIntrim1;
												}
											} else if (crnval[i] != null && !crnval[i].isEmpty()
													&& date.compareTo(itm2) == 0) {
												dupDate = itm2;
												if (caseStatus[i].equals("On Hold")) {
													cellStyle = onHoldType;
												} else {
													cellStyle = cellStyleIntrim2;
												}
											} else if (crnval[i] != null && !crnval[i].isEmpty()
													&& date.compareTo(fnlDate) == 0) {
												dupDate = fnlDate;
												if (caseStatus[i].equals("On Hold")) {
													cellStyle = onHoldType;
												} else {
													cellStyle = cellStyleFinal;
												}
											}

											if (crnValue.trim().length() > 0) {
												crnValue = crnValue + "\n" + crnval[i];
											} else {
												crnValue = crnval[i];
											}
										}

										for (a = 0; a < leaveDate.length; ++a) {
											if (!crnValue.equalsIgnoreCase("") && !"null".equals(leaveDate[a])
													&& leaveDate[a] != null && !leaveDate[a].equalsIgnoreCase("")
													&& date.compareTo(datFormat2.parse(leaveDate[a])) == 0) {
												crnValue = leaveType[a] + "\n" + crnValue;
												leaveDate[a] = "";
												break;
											}
										}

										cel = rowNext.createCell((short) (cellCount++));
										cellStyle.setFont(font);
										cellStyle.setWrapText(true);
										cel.setCellStyle(cellStyle);
										cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(crnValue)));
									}
								}

								rowNext = sht.createRow(rowCount++);
								cellCount = 3;
							}

							for (i = 0; i < cellCount; ++i) {
								mergeCustom(firstRow, lastRow, i, i, sht);
							}
						}
					} while (!key.equalsIgnoreCase(SAT));

					String cellValue = (String) hashMap.get(key);
					String[] satVal = cellValue.split(SATSeprator);

					for (int i = 0; i < satVal.length; ++i) {
						HSSFCell cel = rowNext.createCell((short) (cellCount++));
						HSSFCellStyle cellStyle = workbook.createCellStyle();
						cellStyle.setVerticalAlignment((short) 1);
						cel.setCellStyle(cellStyle);
						cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(satVal[i])));
					}
				}
			}
		} catch (FileNotFoundException var77) {
			throw new CMSException(logger, var77);
		} catch (IOException var78) {
			throw new CMSException(logger, var78);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException var76) {
					throw new CMSException(logger, var76);
				}
			}

		}

		logger.debug("out CurrentAnalystLoading ExcelDownloader::writeToExcel" + d.getTime());
		return fileName;
	}

	private static HSSFCellStyle createHeaderStyle(HSSFWorkbook workbook, short hssfForeColorIndex,
			short hssfBackgroundColourIndex) {
		HSSFCellStyle headerCellStyle = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontName("Arial Unicode MS");
		headerCellStyle.setFillPattern((short) 1);
		headerCellStyle.setFillForegroundColor(hssfBackgroundColourIndex);
		headerCellStyle.setFillBackgroundColor(hssfForeColorIndex);
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
		return headerCellStyle;
	}

	private static HSSFCellStyle createStyle(HSSFWorkbook workbook, short hssfForeColorIndex,
			short hssfBackgroundColourIndex) {
		HSSFCellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFillPattern((short) 1);
		headerCellStyle.setFillForegroundColor(hssfBackgroundColourIndex);
		headerCellStyle.setFillBackgroundColor(hssfForeColorIndex);
		headerCellStyle.setWrapText(true);
		return headerCellStyle;
	}

	public static String exponentConversion(Double value) {
		DecimalFormat f = new DecimalFormat("0.#########################");
		String tmp = f.format(value).toString();
		return tmp;
	}

	private static HSSFCellStyle createHeaderStyle(HSSFWorkbook workbook) {
		createHeaderStyle(workbook, (short) 0, (short) 1);
		return null;
	}

	private static HSSFWorkbook createWorkBook() {
		HSSFWorkbook workbook = new HSSFWorkbook();
		return workbook;
	}

	private static String getNullReplacedValue(String cellValue) {
		return cellValue != null && !cellValue.equalsIgnoreCase("null") ? cellValue : "";
	}

	public static void mergeCustom(int firstRow, int lastRow, int firstCol, int lastCol, HSSFSheet sheet) {
		sheet.addMergedRegion(new Region(firstRow, (short) firstCol, lastRow, (short) lastCol));
	}
}