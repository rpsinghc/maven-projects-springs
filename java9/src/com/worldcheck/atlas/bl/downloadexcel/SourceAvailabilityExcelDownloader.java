package com.worldcheck.atlas.bl.downloadexcel;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddress;

public class SourceAvailabilityExcelDownloader {
	private static ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.downloadexcel.SourceAvailabilityExcelDownloader");
	private static final String DEFAULT_SHEET_NAME = "Sheet 1";
	private static final String EXCEL_FILE_PREFIX = "Worldcheck_";
	private static final String EXCEL_FILE_SUFFIX = ".xls";
	private static final String UNDERSCORE = "_";
	private static PropertyReaderUtil propertyReader;
	private static final String DECIMAL_FORMAT = "0.#########################";
	private static final String CONTENT_TYPE = "application/xls; charset=UTF-8";
	private static final String NULL_REPLACEMENT = "";
	private static final String NULL_STRING = "null";

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public static void writeToExcel(List<String> lstFirstHeader, List<String> lstHeader,
			List<LinkedHashMap<String, String>> dataList, String sheetName, short hssfForeColorIndex,
			short hssfBackgroundColourIndex, HttpServletResponse response, String excelFileName) throws CMSException {
		logger.debug("in SourceAvailabilityExcelDownloader::writeToExcel");
		FileOutputStream fos = null;
		String fileName = "";
		String tempExcelFilePath = "";

		try {
			int rowCount = 0;
			int columnCount = 0;
			HSSFSheet sht = null;
			HSSFWorkbook workbook = createWorkBook();
			HSSFCellStyle headerCellStyle;
			if (hssfForeColorIndex == 0 && hssfBackgroundColourIndex == 0) {
				headerCellStyle = createHeaderStyle(workbook);
			} else {
				createHeaderStyle(workbook, hssfForeColorIndex, hssfBackgroundColourIndex);
			}

			if (sheetName != null && !sheetName.isEmpty()) {
				sht = workbook.createSheet(sheetName);
			} else {
				sht = workbook.createSheet("Sheet 1");
			}

			int var40 = rowCount + 1;
			HSSFRow row = sht.createRow(rowCount);
			LinkedHashMap<String, String> hashMap = null;
			logger.debug("Iterate through first headerList. lstFirstHeader :: " + lstFirstHeader.size());
			HSSFCellStyle headerCellStyle2 = createHeaderStyle(workbook, (short) 8, (short) 41);
			Iterator var20 = lstFirstHeader.iterator();

			while (var20.hasNext()) {
				String headerName = (String) var20.next();
				sht.setColumnWidth((short) columnCount, (short) 5000);
				HSSFCell cel = row.createCell((short) (columnCount++), 1);
				cel.setCellValue(getNullReplacedValue(headerName));
				cel.setCellStyle(headerCellStyle2);
			}

			logger.debug("merging header now.");
			mergeCustom(0, 0, 0, 0, sht);
			mergeCustom(0, 0, 1, 1, sht);
			mergeCustom(0, 0, 2, 2, sht);
			mergeCustom(0, 0, 3, 6, sht);
			mergeCustom(0, 0, 7, 9, sht);
			mergeCustom(0, 0, 10, 10, sht);
			mergeCustom(0, 0, 11, 11, sht);
			mergeCustom(0, 0, 12, 15, sht);
			mergeCustom(0, 0, 16, 16, sht);
			HSSFRow row1 = sht.createRow(var40++);
			columnCount = 0;
			logger.debug("Iterate through second headerList.");
			Iterator var45 = lstHeader.iterator();

			while (var45.hasNext()) {
				String headerName = (String) var45.next();
				if (!headerName.equals("Country") && !headerName.equals("Name Of Database")) {
					headerCellStyle = createHeaderStyle(workbook, (short) 8, (short) 52);
				} else {
					headerCellStyle = createHeaderStyle(workbook, (short) 9, (short) 8);
				}

				sht.setColumnWidth((short) columnCount, (short) 5000);
				HSSFCell cel = row1.createCell((short) (columnCount++), 1);
				cel.setCellValue(getNullReplacedValue(headerName));
				cel.setCellStyle(headerCellStyle);
			}

			logger.debug("iterating datalist");
			HSSFFont font = workbook.createFont();
			font.setFontName("Arial");
			String key = "";
			String cellValue = "";
			HSSFCell cel = null;
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFont(font);
			Iterator dataListIterator = dataList.iterator();

			while (true) {
				if (!dataListIterator.hasNext()) {
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
					break;
				}

				int cellCount = 0;
				hashMap = (LinkedHashMap) dataListIterator.next();
				HSSFRow rowNext = sht.createRow(var40++);
				if (hashMap != null) {
					Iterator mapIterator = hashMap.keySet().iterator();

					while (mapIterator.hasNext()) {
						key = (String) mapIterator.next();
						cel = rowNext.createCell(cellCount++);
						cellValue = (String) hashMap.get(key);
						cel.setCellStyle(cellStyle);
						cel.setCellValue(getNullReplacedValue(cellValue));
					}
				}
			}
		} catch (FileNotFoundException var37) {
			throw new CMSException(logger, var37);
		} catch (IOException var38) {
			throw new CMSException(logger, var38);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException var36) {
					throw new CMSException(logger, var36);
				}
			}

		}

		openFile(response, fileName);
		logger.debug("out SourceAvailabilityExcelDownloader::writeToExcel");
	}

	public static void mergeCustom(int firstRow, int lastRow, int firstCol, int lastCol, HSSFSheet sheet) {
		sheet.addMergedRegion(new Region(firstRow, (short) firstCol, lastRow, (short) lastCol));
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
	}

	private static HSSFCellStyle createHeaderStyle(HSSFWorkbook workbook, short hssfForeColorIndex,
			short hssfBackgroundColourIndex) {
		logger.debug("creating HeaderStyle");
		HSSFCellStyle headerCellStyle = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
		font.setFontName("Arial");
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
		headerCellStyle.setAlignment((short) 2);
		font.setBoldweight((short) 400);
		font.setColor(hssfForeColorIndex);
		headerCellStyle.setFont(font);
		logger.debug("created HeaderStyle");
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

	private static void openFile(HttpServletResponse response, String fileName) throws CMSException {
		logger.debug("in SourceAvailabilityExcelDownloader::openFile");
		InputStream is = null;
		OutputStream os = null;
		FileInputStream fis = null;
		File file = null;

		try {
			file = new File(fileName);
			response.setContentType("application/xls; charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			if (file.exists()) {
				fis = new FileInputStream(fileName);
				is = new BufferedInputStream(fis);
				os = response.getOutputStream();
				byte[] buffer = new byte[1024];

				for (int read = is.read(buffer); read >= 0; read = is.read(buffer)) {
					if (read > 0) {
						os.write(buffer, 0, read);
					}
				}

				os.flush();
			}
		} catch (IOException var22) {
			throw new CMSException(logger, var22);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException var21) {
					throw new CMSException(logger, var21);
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException var20) {
					throw new CMSException(logger, var20);
				}
			}

			if (os != null) {
				try {
					os.close();
				} catch (IOException var19) {
					throw new CMSException(logger, var19);
				}
			}

		}

		file.delete();
		logger.debug("out SourceAvailabilityExcelDownloader::openFile");
	}

	private static String getNullReplacedValue(String cellValue) {
		return cellValue != null && !cellValue.equalsIgnoreCase("null") ? cellValue : "";
	}
}