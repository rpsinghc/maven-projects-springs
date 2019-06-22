package com.worldcheck.atlas.validation.bl;

import com.worldcheck.atlas.vo.CaseDetails;

public class TeamValidator {
	public boolean performPullbackValidation(CaseDetails newCaseDetails) {
		return false;
	}

	public boolean performDeleteTeamValidation(CaseDetails newCaseDetails) {
		return false;
	}

	public boolean performAddTeamValidation(CaseDetails newCaseDetails) {
		return false;
	}
}