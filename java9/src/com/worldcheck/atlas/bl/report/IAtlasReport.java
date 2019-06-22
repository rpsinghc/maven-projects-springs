package com.worldcheck.atlas.bl.report;

import com.worldcheck.atlas.exception.CMSException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IAtlasReport {
	List<Object> fetchReport(HttpServletRequest var1, HttpServletResponse var2) throws CMSException;

	int fetchTotalCount(HttpServletRequest var1, HttpServletResponse var2) throws CMSException;
}