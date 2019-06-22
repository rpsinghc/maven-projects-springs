package com.worldcheck.atlas.bl.interfaces;

import com.worldcheck.atlas.bl.search.SearchCriteria;
import com.worldcheck.atlas.exception.CMSException;
import java.util.List;

public interface ISearch {
	List search(SearchCriteria var1) throws CMSException;
}