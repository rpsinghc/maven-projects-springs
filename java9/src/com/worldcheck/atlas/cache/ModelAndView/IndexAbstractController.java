package com.worldcheck.atlas.cache.ModelAndView;

import com.worldcheck.atlas.cache.service.CacheService;
import com.worldcheck.atlas.cache.vo.ResultSetVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;

public class IndexAbstractController extends AbstractFormController {
	protected ModelAndView showForm(HttpServletRequest arg0, HttpServletResponse arg1, BindException arg2)
			throws Exception {
		String Mess = "Abstract Controller Test";
		CacheService cacheService = new CacheService();
		ModelAndView modelAndView = new ModelAndView("indexAbstractController");
		modelAndView.addObject("aList", cacheService.getCacheItemsList("BRANCH_OFFICE_MASTER"));
		modelAndView.addObject("message", Mess);
		return modelAndView;
	}

	protected ModelAndView processFormSubmission(HttpServletRequest arg0, HttpServletResponse arg1, Object rsultSetVO,
			BindException arg3) throws Exception {
		ResultSetVO resVO = (ResultSetVO) rsultSetVO;
		CacheService cacheService = new CacheService();
		ModelAndView modelAndView = new ModelAndView("indexAbstractController");
		modelAndView.addObject("aList", cacheService.getCacheItemsList("BRANCH_OFFICE_MASTER"));
		modelAndView.addObject("id", resVO.getId());
		modelAndView.addObject("name", resVO.getName());
		return modelAndView;
	}
}