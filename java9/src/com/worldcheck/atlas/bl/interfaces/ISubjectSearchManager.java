package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.vo.SubjectDetails;
import java.util.List;

public interface ISubjectSearchManager {
	List<SubjectDetails> getSubjectTypeList() throws CMSException;

	List<SubjectDetails> getAssociateCasesForSub(SearchCriteria var1) throws CMSException;

	int getAsscciateCaseCount(SearchCriteria var1) throws CMSException;
}