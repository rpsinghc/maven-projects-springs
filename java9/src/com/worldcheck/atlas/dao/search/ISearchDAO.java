package com.worldcheck.atlas.dao.search;

import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.exception.CMSException;
import java.util.List;

public interface ISearchDAO {
	List search(SearchCriteria var1) throws CMSException;
}