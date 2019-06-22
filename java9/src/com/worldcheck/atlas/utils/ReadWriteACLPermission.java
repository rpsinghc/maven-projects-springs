package com.worldcheck.atlas.utils;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import com.worldcheck.atlas.vo.sbm.UserDetailsBean;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class ReadWriteACLPermission extends TagSupport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.filters.acl.RolePermissionFilter");
	private static final String USER_DETAIL_BEAN = "userDetailsBean";
	private static final String PERMISSION_TYPE = "permissionType";
	private boolean component = true;
	private String key;

	public boolean isComponent() {
		return this.component;
	}

	public void setComponent(boolean component) {
		this.component = component;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int doEndTag() throws JspException {
		return 6;
	}

	public int doStartTag() throws JspException {
		PageContext context = this.pageContext;
		boolean processBody = false;

		try {
			HttpServletRequest request = (HttpServletRequest) context.getRequest();
			HttpSession session = request.getSession();
			UserDetailsBean userDetailsBean = (UserDetailsBean) session.getAttribute("userDetailsBean");
			if (userDetailsBean != null) {
				Map permissionMap = userDetailsBean.getPermissionMap();
				String permissionType = (String) permissionMap.get(this.key);
				context.setAttribute("permissionType", permissionType);
				if (this.component) {
					if ("A".equalsIgnoreCase(permissionType)) {
						processBody = false;
					} else {
						processBody = true;
					}
				}
			}
		} catch (Exception var8) {
			new CMSException(this.logger, var8);
		}

		return processBody ? 1 : 0;
	}
}