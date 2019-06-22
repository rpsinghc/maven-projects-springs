package com.worldcheck.atlas.bl.interfaces;

import com.savvion.sbm.bizlogic.util.Session;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.task.invoice.UnconfimredBudgetVO;
import java.util.List;

public interface IUnconfirmBudget {
	UnconfimredBudgetVO getBudgetDetails(String var1) throws CMSException;

	int saveISISDetails(UnconfimredBudgetVO var1, Session var2) throws CMSException;

	List<UnconfimredBudgetVO> getBudgetRecords(UnconfimredBudgetVO var1) throws CMSException;

	int getBudgetRecordsCount(String var1) throws CMSException;

	String saveUnconfirmDetails(UnconfimredBudgetVO var1, String var2, String var3, Session var4) throws CMSException;

	String getCrnOffice(String var1) throws CMSException;
}