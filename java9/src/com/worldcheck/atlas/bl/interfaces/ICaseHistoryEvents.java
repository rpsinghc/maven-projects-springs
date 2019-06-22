package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.dao.audit.CaseHistoryDAO;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.CaseDetails;
import com.worldcheck.atlas.vo.TeamDetails;
import com.worldcheck.atlas.vo.audit.CaseHistory;
import com.worldcheck.atlas.vo.task.invoice.UnconfimredBudgetVO;
import java.util.List;

public interface ICaseHistoryEvents {
	void setCaseHistoryDAO(CaseHistoryDAO var1);

	void setCaseHistory(CaseDetails var1, CaseDetails var2, CaseHistory var3, String var4) throws CMSException;

	void setCaseHistoryForTaskStatusChange(CaseHistory var1) throws CMSException;

	void setCaseHistoryForCaseStatusChange(String var1, String var2, CaseHistory var3) throws CMSException;

	List<CaseHistory> setCaseHistoryForReviewScoresheet(String var1, CaseHistory var2) throws CMSException;

	List<CaseHistory> getCaseHistory(String var1, int var2, int var3, String var4, String var5, List<String> var6)
			throws CMSException;

	int getCaseHistoryCountForCRN(String var1, List<String> var2) throws CMSException;

	void setCaseHistoryForRejection(CaseHistory var1) throws CMSException;

	void setCaseHistoryForComments(String var1, CaseHistory var2) throws CMSException;

	void setCaseHistoryForTaskCompleted(String var1, String var2, String var3, String var4, String var5, String var6,
			String var7) throws CMSException;

	void setCaseHistoryForConfirmBudgetAndDueDate(List<UnconfimredBudgetVO> var1) throws CMSException;

	void setCaseHistoryForTaskApproved(CaseHistory var1) throws CMSException;

	void setCaseHistoryForUploadFinalDocument(CaseHistory var1) throws CMSException;

	long getTimeStampOfCCS(String var1) throws CMSException;

	void setCaseHistoryForBulkOfficeAssignment(List<CaseDetails> var1, List<CaseDetails> var2) throws CMSException;

	void setCaseHistoryForBulkTeamAssignment(List<CaseDetails> var1, List<CaseDetails> var2) throws CMSException;

	void setCaseHistoryForMassVendorUpload(List<CaseHistory> var1) throws CMSException;

	void setCaseHistoryForCaseCreated(CaseHistory var1) throws CMSException;

	void setCaseHistoryForTeamManagerReassign(TeamDetails var1, TeamDetails var2, CaseHistory var3) throws CMSException;

	void setCaseHistoryForBulkTeamManagerReassign(List<CaseDetails> var1, List<CaseDetails> var2) throws CMSException;
}