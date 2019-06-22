package com.worldcheck.atlas.bl.createcase;

import com.worldcheck.atlas.utils.ResourceLocator;
import com.worldcheck.atlas.vo.CaseDetails;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class CreateCaseController extends SimpleFormController {
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
		HashMap<String, Object> hmAttributes = new HashMap();
		hmAttributes.put("PRIORITY", "medium");
		ResourceLocator resourceLocator = ResourceLocator.self();
		resourceLocator.getSBMService().createProcessInstance(hmAttributes);
		System.out.println("\n Process Created Sucessfully..");
		return new ModelAndView(this.getSuccessView());
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CaseDetails caseDetail = new CaseDetails();
		System.out.println("Case Create Method Called");
		return caseDetail;
	}
}