package com.worldcheck.atlas.validation.bl;

import com.worldcheck.atlas.vo.CaseDetails;

public class SubjectValidator {
	public boolean performCaseStatusValidation(String crn) {
		return false;
	}

	public boolean performPullbackValidation(CaseDetails newCaseDetails) {
		return false;
	}

	public boolean performDeleteTeamValidation(CaseDetails newCaseDetails) {
		return false;
	}
}