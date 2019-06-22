package com.worldcheck.atlas.services.updatepassword;

import com.worldcheck.atlas.bl.interfaces.IMasterDataValidator;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;

public class UpdatePasswordService {
	private ILogProducer logger = LogProducerImpl
			.getLogger("com.worldcheck.atlas.services.updatepassword.UpdatePasswordService");
	private IMasterDataValidator masterDataValidator;

	public void setMasterDataValidator(IMasterDataValidator masterDataValidator) {
		this.masterDataValidator = masterDataValidator;
	}

	public boolean performPasswordValidation(String userId, String passwordEncr) throws CMSException {
		boolean isValidPassword = this.masterDataValidator.performPasswordValidation(userId, passwordEncr);
		return isValidPassword;
	}
}