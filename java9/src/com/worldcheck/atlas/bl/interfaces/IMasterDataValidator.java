package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.utils.PropertyReaderUtil;
import com.worldcheck.atlas.validation.dao.ValidationDAO;
import com.worldcheck.atlas.vo.masters.UserMasterVO;
import java.io.File;

public interface IMasterDataValidator {
	void setValidationDAO(ValidationDAO var1);

	void setPropertyReader(PropertyReaderUtil var1);

	boolean performPasswordValidation(String var1, String var2) throws CMSException;

	boolean performBackUpValidation(UserMasterVO var1);

	boolean performUniqueClientNameValidation(String var1);

	boolean performCRNValidation(String var1) throws CMSException;

	boolean performExcelValidation(File var1) throws CMSException;

	boolean isMacroEnabledInMassVendorUploadFile(File var1) throws CMSException;

	boolean performResetPasswordValidation(String var1, String var2) throws CMSException;

	boolean performCurrentPasswordValidation(String var1, String var2) throws CMSException;
}