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

public class AuthorizedACLPermission extends TagSupport {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.filters.acl.RolePermissionFilter");
	private static final String USER_DETAIL_BEAN = "userDetailsBean";
	private static final String PERMISSION_NA = "NA";
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
		boolean processBody = false;

		try {
			PageContext context = this.pageContext;
			HttpServletRequest request = (HttpServletRequest) context.getRequest();
			HttpSession session = request.getSession();
			UserDetailsBean userDetailsBean = (UserDetailsBean) session.getAttribute("userDetailsBean");
			if (userDetailsBean != null) {
				Map permissionMap = userDetailsBean.getPermissionMap();
				String permissionType = (String) permissionMap.get(this.key);
				if (this.component) {
					if (!"NA".equalsIgnoreCase(permissionType)) {
						processBody = true;
					} else {
						processBody = false;
					}
				} else if (permissionType != null && !"NA".equalsIgnoreCase(permissionType)) {
					processBody = false;
				} else {
					processBody = true;
				}
			}
		} catch (NullPointerException var8) {
			new CMSException(this.logger, var8);
		} catch (Exception var9) {
			new CMSException(this.logger, var9);
		}

		return processBody ? 1 : 0;
	}
}