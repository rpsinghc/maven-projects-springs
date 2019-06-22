package com.worldcheck.atlas.utils;

import com.worldcheck.atlas.sbm.util.BizSoloTagStringUtil;
import javax.servlet.jsp.JspException;
import org.apache.taglibs.standard.tag.common.core.NullAttributeException;
import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

public class AtlasOutTag extends OutSupport {
	private String value_;
	private String default_;
	private String escapeXml_;

	public AtlasOutTag() {
		this.init();
	}

	public int doStartTag() throws JspException {
		this.evaluateExpressions();
		return super.doStartTag();
	}

	public void release() {
		super.release();
		this.init();
	}

	public void setValue(String value_) {
		this.value_ = value_;
	}

	public void setDefault(String default_) {
		this.default_ = default_;
	}

	public void setEscapeXml(String escapeXml_) {
		this.escapeXml_ = escapeXml_;
	}

	private void init() {
		this.value_ = this.default_ = null;
		this.escapeXml_ = "false";
	}

	private void evaluateExpressions() throws JspException {
		try {
			this.value = ExpressionUtil.evalNotNull("out", "value", this.value_, Object.class, this, this.pageContext);
			if (this.value != null) {
				this.value = BizSoloTagStringUtil.getDisplayTag(this.value.toString());
			}
		} catch (NullAttributeException var3) {
			this.value = null;
		}

		try {
			this.def = (String) ExpressionUtil.evalNotNull("out", "default", this.default_, String.class, this,
					this.pageContext);
			if (this.def != null) {
				this.def = BizSoloTagStringUtil.getDisplayTag(this.def.toString());
			}
		} catch (NullAttributeException var2) {
			this.def = null;
		}

		this.escapeXml = true;
		Boolean escape = (Boolean) ExpressionUtil.evalNotNull("out", "escapeXml", this.escapeXml_, Boolean.class, this,
				this.pageContext);
		if (escape != null) {
			this.escapeXml = escape;
		}

	}
}