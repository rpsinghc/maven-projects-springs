package com.worldcheck.atlas.validation.bl;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.validation.dao.ValidationDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CTDataValidator {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.validation.bl.CTDataValidator");
	private ValidationDAO validationDAO = null;
	private PropertyReaderUtil propertyReader = null;

	public void setValidationDAO(ValidationDAO validationDAO) {
		this.validationDAO = validationDAO;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public boolean performExcelFormatValidation(File excelFile) throws CMSException {
		String headerNames = this.propertyReader.getCtDataExcelHeader();
		String[] headerList = null;
		InputStream excelFileStream = null;
		HSSFRow row = null;
		boolean isValidHeader = false;
		if (null != headerNames) {
			headerList = headerNames.split(",");
		}

		try {
			excelFileStream = new FileInputStream(excelFile);
			this.logger.debug("file input stream found  ");
			HSSFWorkbook wb = new HSSFWorkbook(excelFileStream);
			this.logger.debug("work book found  ");
			HSSFSheet sheet = wb.getSheetAt(0);
			row = sheet.getRow(0);
			if (null != row) {
				isValidHeader = this.isValidHeader(row, headerList);
			}
		} catch (FileNotFoundException var23) {
			if (excelFileStream != null) {
				try {
					excelFileStream.close();
				} catch (IOException var21) {
					throw new CMSException(this.logger, var23);
				}
			}

			this.logger.error("File Not Found " + var23.getMessage());
		} catch (IOException var24) {
			if (excelFileStream != null) {
				try {
					excelFileStream.close();
				} catch (IOException var20) {
					throw new CMSException(this.logger, var24);
				}
			}

			this.logger.error("IO Exception " + var24.getMessage());
			var24.printStackTrace();
		} finally {
			try {
				if (excelFileStream != null) {
					excelFileStream.close();
				}
			} catch (IOException var22) {
				throw new CMSException(this.logger, var22);
			}

		}

		return isValidHeader;
	}

	public boolean performExcelFormatValidation(HSSFWorkbook wb) throws CMSException {
		String headerNames = this.propertyReader.getCtDataExcelHeader();
		String[] headerList = null;
		HSSFRow row = null;
		boolean isValidHeader = false;
		if (null != headerNames) {
			headerList = headerNames.split(",");
		}

		try {
			HSSFSheet sheet = wb.getSheetAt(0);
			row = sheet.getRow(0);
			if (null != row) {
				isValidHeader = this.isValidHeader(row, headerList);
			}

			return isValidHeader;
		} catch (Exception var7) {
			throw new CMSException(this.logger, var7);
		}
	}

	private boolean isValidHeader(HSSFRow row, String[] headerNames) {
		boolean flag = true;
		this.logger.debug("# of headers:" + headerNames.length);

		for (int j = 0; j < headerNames.length; ++j) {
			HSSFCell cell = row.getCell((short) j);
			if (cell == null || !cell.getStringCellValue().toString().trim().equals(headerNames[j].trim())) {
				flag = false;
				break;
			}

			this.logger.debug("matched cell header name: " + cell.getStringCellValue().toString());
			this.logger.debug(" with header value  " + headerNames[j]);
		}

		return flag;
	}

	public boolean performCRNValidation(String crn) throws CMSException {
		boolean isCRNExist = false;
		int countCRN = false;
		int countCRN = this.validationDAO.checkCRN(crn);
		if (countCRN > 0) {
			isCRNExist = true;
		}

		return isCRNExist;
	}
}