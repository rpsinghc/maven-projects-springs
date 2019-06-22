package com.worldcheck.atlas.validation.bl;

import com.worldcheck.atlas.bl.interfaces.IMasterDataValidator;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.utils.ExposePropertyPlaceholderConfigurer;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.validation.dao.ValidationDAO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class MasterDataValidator implements IMasterDataValidator {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.validation.bl.MasterDataValidator");
	private ValidationDAO validationDAO = null;
	private PropertyReaderUtil propertyReader = null;
	ExposePropertyPlaceholderConfigurer propertyConfigurer = null;

	public void setPropertyConfigurer(ExposePropertyPlaceholderConfigurer propertyConfigurer) {
		this.propertyConfigurer = propertyConfigurer;
	}

	public void setValidationDAO(ValidationDAO validationDAO) {
		this.validationDAO = validationDAO;
	}

	public void setPropertyReader(PropertyReaderUtil propertyReader) {
		this.propertyReader = propertyReader;
	}

	public boolean performPasswordValidation(String userName, String passwordEncr) throws CMSException {
		boolean isValidPassword = false;

		try {
			String passwordHistory = this.validationDAO.getPasswordHistory(userName);
			this.logger.debug("password history " + passwordHistory);
			String[] passwords = passwordHistory.split(",");
			this.logger.debug("passwords  " + passwords);
			isValidPassword = this.validatePassword(passwords, passwordEncr);
			return isValidPassword;
		} catch (Exception var6) {
			throw new CMSException(this.logger, var6);
		}
	}

	private boolean validatePassword(String[] passwords, String entredPassword) {
		boolean checkFlag = true;
		int historyCount = Integer
				.parseInt((String) this.propertyConfigurer.getResolvedProps().get("atlas.password.history.count"));
		if (passwords != null && passwords.length > 0 && passwords.length <= Integer
				.parseInt((String) this.propertyConfigurer.getResolvedProps().get("atlas.password.history.count"))) {
			historyCount = passwords.length;
		}

		for (int i = 0; i < historyCount; ++i) {
			if (passwords[i].equals(entredPassword)) {
				checkFlag = false;
				i = historyCount;
			}
		}

		return checkFlag;
	}

	public boolean performBackUpValidation(UserMasterVO user) {
		return false;
	}

	public boolean performUniqueClientNameValidation(String clientName) {
		return false;
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

	public boolean performExcelValidation(File excelFile) throws CMSException {
		String headerNames = this.propertyReader.getMassVendorUploadExcelHeader();
		String[] headerList = (String[]) null;
		InputStream excelFileStream = null;
		HSSFRow row = null;
		boolean isValidHeader = false;
		if (headerNames != null) {
			headerList = headerNames.split(",");
		}

		try {
			excelFileStream = new FileInputStream(excelFile);
			this.logger.debug("file input stream found  ");
			HSSFWorkbook wb = new HSSFWorkbook(excelFileStream);
			this.logger.debug("work book found  ");
			HSSFSheet sheet = wb.getSheetAt(0);
			row = sheet.getRow(0);
			if (row != null) {
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

	private boolean isValidHeader(HSSFRow row, String[] headerNames) {
		boolean flag = true;
		this.logger.debug("headers are " + headerNames.length);

		for (int j = 0; j < headerNames.length; ++j) {
			HSSFCell cell = row.getCell((short) j);
			this.logger.debug("cell is " + cell);
			this.logger.debug("header value  " + headerNames[j]);
			if (cell == null || !cell.getStringCellValue().toString().trim().equals(headerNames[j].trim())) {
				flag = false;
				break;
			}

			this.logger.debug("cell.getStringCellValue().toString() " + cell.getStringCellValue().toString());
			this.logger.debug("header value  " + headerNames[j]);
		}

		return flag;
	}

	public boolean isMacroEnabledInMassVendorUploadFile(File excelFile) throws CMSException {
		this.logger.debug("In isMacroEnabledInMassVendorUploadFile ");
		InputStream excelFileStream = null;
		boolean macroEnableflag = false;

		try {
			excelFileStream = new FileInputStream(excelFile);
			this.logger.debug("file input stream found  ");
			HSSFWorkbook wb = new HSSFWorkbook(excelFileStream);
			this.logger.debug("work book found  ");
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow macroCheckRow = sheet.getRow(0);
			if (macroCheckRow != null) {
				HSSFCell macroCheckerCell = macroCheckRow.getCell((short) 25);
				if (macroCheckerCell != null) {
					if (macroCheckerCell.getCellType() == 0) {
						if (macroCheckerCell.getNumericCellValue() == 1.0D) {
							macroEnableflag = true;
						} else if (macroCheckerCell.getNumericCellValue() == 0.0D) {
							macroEnableflag = false;
						}
					} else {
						if (macroCheckerCell.getStringCellValue().trim().equalsIgnoreCase("1")) {
							macroEnableflag = true;
						}

						if (macroCheckerCell.getStringCellValue().trim().equalsIgnoreCase("0")) {
							macroEnableflag = false;
						}
					}
				}
			}
		} catch (FileNotFoundException var22) {
			if (excelFileStream != null) {
				try {
					excelFileStream.close();
				} catch (IOException var20) {
					throw new CMSException(this.logger, var22);
				}
			}

			this.logger.error("File Not Found " + var22.getMessage());
		} catch (IOException var23) {
			if (excelFileStream != null) {
				try {
					excelFileStream.close();
				} catch (IOException var19) {
					throw new CMSException(this.logger, var23);
				}
			}

			this.logger.error("IO Exception " + var23.getMessage());
		} finally {
			try {
				if (excelFileStream != null) {
					excelFileStream.close();
				}
			} catch (IOException var21) {
				throw new CMSException(this.logger, var21);
			}

		}

		return macroEnableflag;
	}

	public boolean performResetPasswordValidation(String userName, String passwordEncr) throws CMSException {
		this.logger.debug("Entring MasterDataValidator:performResetPasswordValidation");
		boolean isValidPassword = true;

		try {
			String passwords = this.validationDAO.getTempPassword(userName);
			if (!passwords.equals(passwordEncr)) {
				isValidPassword = false;
			}

			return isValidPassword;
		} catch (Exception var5) {
			throw new CMSException(this.logger, var5);
		}
	}

	public boolean performCurrentPasswordValidation(String UserCurrentPassword, String currentPassEncrypted)
			throws CMSException {
		return UserCurrentPassword.equalsIgnoreCase(currentPassEncrypted);
	}
}