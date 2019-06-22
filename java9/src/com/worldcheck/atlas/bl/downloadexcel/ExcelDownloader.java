package com.worldcheck.atlas.bl.downloadexcel;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.vo.ExcelDownloadMultiTabVO;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelDownloader {
	private static ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.bl.downloadexcel.ExcelDownloader");
	private static final String DEFAULT_SHEET_NAME = "Sheet 1";
	private static final String EXCEL_FILE_PREFIX = "Worldcheck_";
	private static final String EXCEL_FILE_SUFFIX = ".xls";
	private static final String ZIP_FILE_SUFFIX = ".zip";
	private static final String UNDERSCORE = "_";
	private static PropertyReaderUtil propertyReader;
	private static final String DECIMAL_FORMAT = "0.#########################";
	private static final String CONTENT_TYPE = "application/xls; charset=UTF-8";
	private static final String CONTENT_TYPE_ZIP = "application/zip";
	private static final String NULL_REPLACEMENT = "";
	private static final String NULL_STRING = "null";

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		propertyReader = propertyReader;
	}

	public static Map<String, Object> writeToExcel(List<String> lstHeader, List<LinkedHashMap<String, String>> dataList,
			String sheetName, short hssfForeColorIndex, short hssfBackgroundColourIndex, HttpServletResponse response,
			String excelFileName) throws CMSException {
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
				headerCellStyle = createHeaderStyle(workbook, hssfForeColorIndex, hssfBackgroundColourIndex);
			}

			if (sheetName != null && !sheetName.isEmpty()) {
				sht = workbook.createSheet(sheetName);
			} else {
				sht = workbook.createSheet("Sheet 1");
			}

			int var38 = rowCount + 1;
			HSSFRow row = sht.createRow(rowCount);
			LinkedHashMap<String, String> hashMap = null;
			Iterator var18 = lstHeader.iterator();

			while (var18.hasNext()) {
				String headerName = (String) var18.next();
				sht.setColumnWidth((short) columnCount, (short) 5000);
				HSSFCell cel = row.createCell((short) (columnCount++), 1);
				cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(headerName)));
				cel.setCellStyle(headerCellStyle);
			}

			logger.debug("iterating datalist");
			HSSFFont font = workbook.createFont();
			font.setFontName("Arial Unicode MS");
			HSSFDataFormat fmt = workbook.createDataFormat();
			HSSFCellStyle textStyle = workbook.createCellStyle();
			textStyle.setDataFormat(fmt.getFormat("@"));
			sht.setDefaultColumnStyle(3, textStyle);
			Iterator dataListIterator = dataList.iterator();

			label168 : while (true) {
				int cellCount;
				HSSFRow rowNext;
				do {
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
						break label168;
					}

					cellCount = 0;
					hashMap = (LinkedHashMap) dataListIterator.next();
					rowNext = sht.createRow(var38++);
				} while (hashMap == null);

				Iterator mapIterator = hashMap.keySet().iterator();

				while (mapIterator.hasNext()) {
					String key = (String) mapIterator.next();
					HSSFCell cel = rowNext.createCell((short) (cellCount++));
					String cellValue = (String) hashMap.get(key);
					cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(cellValue)));
				}
			}
		} catch (FileNotFoundException var35) {
			throw new CMSException(logger, var35);
		} catch (IOException var36) {
			throw new CMSException(logger, var36);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException var34) {
					throw new CMSException(logger, var34);
				}
			}

		}

		logger.debug("out ExcelDownloader::writeToExcel");
		return openFile(fileName);
	}

	public static String writeToExcel1(List<String> lstHeader, List<List<String>> dataList, String sheetName,
			short hssfForeColorIndex, short hssfBackgroundColourIndex, HttpServletResponse response,
			String excelFileName) throws CMSException {
		logger.debug("in ExcelDownloader::writeToExcel1");
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
				headerCellStyle = createHeaderStyle(workbook, hssfForeColorIndex, hssfBackgroundColourIndex);
			}

			if (sheetName != null && !sheetName.isEmpty()) {
				sht = workbook.createSheet(sheetName);
			} else {
				sht = workbook.createSheet("Sheet 1");
			}

			int var35 = rowCount + 1;
			HSSFRow row = sht.createRow(rowCount);
			List<String> subDataList = null;
			Iterator dataListIterator = lstHeader.iterator();

			while (dataListIterator.hasNext()) {
				String headerName = (String) dataListIterator.next();
				sht.setColumnWidth((short) columnCount, (short) 5000);
				HSSFCell cel = row.createCell((short) (columnCount++), 1);
				cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(headerName)));
				cel.setCellStyle(headerCellStyle);
			}

			logger.debug("iterating datalist");
			HSSFFont font = workbook.createFont();
			font.setFontName("Arial Unicode MS");
			dataListIterator = dataList.iterator();

			label168 : while (true) {
				HSSFRow rowNext;
				int cellCount;
				do {
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
						break label168;
					}

					cellCount = 0;
					subDataList = (List) dataListIterator.next();
					rowNext = sht.createRow(var35++);
				} while (subDataList == null);

				Iterator iterator = subDataList.iterator();

				while (iterator.hasNext()) {
					HSSFCell cel = rowNext.createCell((short) (cellCount++));
					String cellValue = (String) iterator.next();
					cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(cellValue)));
				}
			}
		} catch (FileNotFoundException var32) {
			throw new CMSException(logger, var32);
		} catch (IOException var33) {
			throw new CMSException(logger, var33);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException var31) {
					throw new CMSException(logger, var31);
				}
			}

		}

		logger.debug("out ExcelDownloader::writeToExcel1");
		return fileName;
	}

	private static HSSFCellStyle createHeaderStyle(HSSFWorkbook workbook, short hssfForeColorIndex,
			short hssfBackgroundColourIndex) {
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

	public static String writeToMultiTabExcel(List<ExcelDownloadMultiTabVO> lstExcelDownloadMultiTabVO,
			short hssfForeColorIndex, short hssfBackgroundColourIndex, HttpServletResponse response,
			String excelFileName) throws CMSException {
		logger.debug("in ExcelDownloader::writeToMultiTabExcel");
		String fileName = "";
		FileOutputStream fos = null;
		String tempExcelFilePath = "";

		try {
			HSSFWorkbook workbook = createWorkBook();
			HSSFCellStyle headerCellStyle;
			if (hssfForeColorIndex == 0 && hssfBackgroundColourIndex == 0) {
				headerCellStyle = createHeaderStyle(workbook);
			} else {
				headerCellStyle = createHeaderStyle(workbook, hssfForeColorIndex, hssfBackgroundColourIndex);
			}

			createWorkSheet(lstExcelDownloadMultiTabVO, headerCellStyle, workbook);
			tempExcelFilePath = propertyReader.getTempExcelFilePath();
			File dir = new File(tempExcelFilePath);
			if (!dir.exists()) {
				(new File(tempExcelFilePath)).mkdirs();
			}

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss.SSS");
			fileName = tempExcelFilePath + File.separator + "Worldcheck_" + excelFileName + "_" + sdf.format(new Date())
					+ ".xls";
			File excelFile = new File(fileName);
			if (excelFile.exists()) {
				excelFile.delete();
			}

			fos = new FileOutputStream(excelFile);
			workbook.write(fos);
		} catch (FileNotFoundException var21) {
			throw new CMSException(logger, var21);
		} catch (IOException var22) {
			throw new CMSException(logger, var22);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException var20) {
					throw new CMSException(logger, var20);
				}
			}

		}

		logger.debug("out ExcelDownloader::writeToMultiTabExcel");
		return fileName;
	}

	private static HSSFWorkbook createWorkBook() {
		HSSFWorkbook workbook = new HSSFWorkbook();
		return workbook;
	}

	private static HSSFWorkbook createWorkSheet(List<ExcelDownloadMultiTabVO> lstExcelDownloadMultiTabVO,
			HSSFCellStyle headerCellStyle, HSSFWorkbook workbook) {
		logger.debug("in ExcelDownloader::createWorkSheet");
		String sheetName = "";
		HSSFSheet sht = null;
		int i = 0;
		Iterator iterator = lstExcelDownloadMultiTabVO.iterator();

		label47 : while (iterator.hasNext()) {
			int rowCount = 0;
			int columnCount = 0;
			ExcelDownloadMultiTabVO excelDownloadMultiTabVO = (ExcelDownloadMultiTabVO) iterator.next();
			sheetName = excelDownloadMultiTabVO.getSheetName();
			List<String> lstHeader = excelDownloadMultiTabVO.getLstHeader();
			List<List<String>> dataList = excelDownloadMultiTabVO.getDataList();
			if (sheetName != null && !sheetName.isEmpty()) {
				sht = workbook.createSheet(sheetName);
			} else {
				++i;
				sht = workbook.createSheet("Sheet 1" + i);
			}

			int var22 = rowCount + 1;
			HSSFRow row = sht.createRow(rowCount);
			LinkedHashMap<String, String> hashMap = null;
			Iterator dataListIterator = lstHeader.iterator();

			String subDataList;
			while (dataListIterator.hasNext()) {
				subDataList = (String) dataListIterator.next();
				sht.setColumnWidth((short) columnCount, (short) 5000);
				HSSFCell cel = row.createCell((short) (columnCount++), 1);
				cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(subDataList)));
				cel.setCellStyle(headerCellStyle);
			}

			subDataList = null;
			dataListIterator = dataList.iterator();

			while (true) {
				HSSFRow rowNext;
				List subDataList;
				int cellCount;
				do {
					if (!dataListIterator.hasNext()) {
						continue label47;
					}

					cellCount = 0;
					subDataList = (List) dataListIterator.next();
					rowNext = sht.createRow(var22++);
				} while (subDataList == null);

				Iterator iterator1 = subDataList.iterator();

				while (iterator1.hasNext()) {
					HSSFCell cel = rowNext.createCell((short) (cellCount++));
					String cellValue = (String) iterator1.next();
					HSSFCellStyle cellStyle = workbook.createCellStyle();
					cel.setCellStyle(cellStyle);
					cel.setCellValue(new HSSFRichTextString(getNullReplacedValue(cellValue)));
				}
			}
		}

		logger.debug("out ExcelDownloader::createWorkSheet");
		return workbook;
	}

	private static Map<String, Object> openFile(String fileName) throws CMSException {
		logger.debug("in ExcelDownloader::openFile");
		InputStream is = null;
		FileInputStream fis = null;
		File file = null;
		byte[] buffer = (byte[]) null;

		try {
			file = new File(fileName);
			if (file.exists()) {
				fis = new FileInputStream(fileName);
				is = new BufferedInputStream(fis);
				buffer = new byte[(int) file.length()];
				is.read(buffer);
			}
		} catch (IOException var16) {
			throw new CMSException(logger, var16);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException var15) {
					throw new CMSException(logger, var15);
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException var14) {
					throw new CMSException(logger, var14);
				}
			}

		}

		file.delete();
		Map<String, Object> resultMap = new HashMap();
		resultMap.put("fileName", file.getName());
		resultMap.put("fileBytes", buffer);
		logger.debug("out ExcelDownloader::openFile");
		return resultMap;
	}

	private static String getNullReplacedValue(String cellValue) {
		return cellValue != null && !cellValue.equalsIgnoreCase("null") ? cellValue : "";
	}

	public static String generateZipFile(List<String> sourceFiles, String fileName, HttpServletResponse response)
			throws CMSException {
		String zipFile = "";

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss.SSS");
			zipFile = propertyReader.getTempExcelFilePath() + File.separator + "Worldcheck_" + fileName + "_"
					+ sdf.format(new Date()) + ".zip";
			byte[] buffer = new byte[1024];
			FileOutputStream fout = new FileOutputStream(zipFile);
			ZipOutputStream zout = new ZipOutputStream(fout);
			String exactFileName = "";
			String completeFileName = "";

			int i;
			for (i = 0; i < sourceFiles.size(); ++i) {
				FileInputStream fin = new FileInputStream((String) sourceFiles.get(i));
				completeFileName = (String) sourceFiles.get(i);
				exactFileName = completeFileName.substring(completeFileName.lastIndexOf("\\") + 1,
						completeFileName.length());
				zout.putNextEntry(new ZipEntry(exactFileName));

				int length;
				while ((length = fin.read(buffer)) > 0) {
					zout.write(buffer, 0, length);
				}

				zout.closeEntry();
				fin.close();
			}

			for (i = 0; i < sourceFiles.size(); ++i) {
				File srcFile = new File((String) sourceFiles.get(i));
				srcFile.delete();
			}

			zout.close();
			return zipFile;
		} catch (IOException var13) {
			throw new CMSException(logger, var13);
		}
	}

	public static void openZipFile(String fileName, HttpServletResponse response) throws CMSException {
		logger.debug("in ExcelDownloader::openZipFile");
		InputStream is = null;
		OutputStream os = null;
		FileInputStream fis = null;
		File file = null;
		String exactFileName = "";
		byte[] buffer = (byte[]) null;

		try {
			file = new File(fileName);
			exactFileName = file.getName();
			response.setContentType("application/zip");
			exactFileName = "\"" + exactFileName + "\"";
			logger.debug("exactFileName " + exactFileName);
			response.setHeader("Content-Disposition", "attachment; filename=" + exactFileName);
			if (file.exists()) {
				fis = new FileInputStream(fileName);
				is = new BufferedInputStream(fis);
				os = response.getOutputStream();
				buffer = new byte[1024];

				for (int read = is.read(buffer); read >= 0; read = is.read(buffer)) {
					if (read > 0) {
						os.write(buffer, 0, read);
					}
				}
			}
		} catch (IOException var22) {
			throw new CMSException(logger, var22);
		} finally {
			logger.debug("before file delete");
			if (file != null && file.exists()) {
				file.delete();
				logger.debug("file deleted");
			}

			if (fis != null) {
				try {
					fis.close();
				} catch (IOException var21) {
					logger.error("There is some error while closing the file input stream :: " + var21.getMessage());
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException var20) {
					logger.error(
							"There is some error while closing the buffered input stream :: " + var20.getMessage());
				}
			}

			if (os != null) {
				try {
					os.close();
				} catch (IOException var19) {
					logger.error("There is some error while closing the output stream :: " + var19.getMessage());
				}
			}

		}

		logger.debug("out ExcelDownloader::openZipFile");
	}
}