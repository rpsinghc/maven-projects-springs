package com.worldcheck.atlas.retrieverclient;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.EnumDeserializer;
import org.apache.axis.encoding.ser.EnumSerializer;

public class OutputFormat implements Serializable {
	private String _value_;
	private static HashMap _table_ = new HashMap();
	public static final String _HTML = "HTML";
	public static final String _DOCX = "DOCX";
	public static final String _DOC = "DOC";
	public static final String _PDF = "PDF";
	public static final OutputFormat HTML = new OutputFormat("HTML");
	public static final OutputFormat DOCX = new OutputFormat("DOCX");
	public static final OutputFormat DOC = new OutputFormat("DOC");
	public static final OutputFormat PDF = new OutputFormat("PDF");
	private static TypeDesc typeDesc = new TypeDesc(OutputFormat.class);

	protected OutputFormat(String value) {
		this._value_ = value;
		_table_.put(this._value_, this);
	}

	public String getValue() {
		return this._value_;
	}

	public static OutputFormat fromValue(String value) throws IllegalArgumentException {
		OutputFormat enumeration = (OutputFormat) _table_.get(value);
		if (enumeration == null) {
			throw new IllegalArgumentException();
		} else {
			return enumeration;
		}
	}

	public static OutputFormat fromString(String value) throws IllegalArgumentException {
		return fromValue(value);
	}

	public boolean equals(Object obj) {
		return obj == this;
	}

	public int hashCode() {
		return this.toString().hashCode();
	}

	public String toString() {
		return this._value_;
	}

	public Object readResolve() throws ObjectStreamException {
		return fromValue(this._value_);
	}

	public static Serializer getSerializer(String mechType, Class _javaType, QName _xmlType) {
		return new EnumSerializer(_javaType, _xmlType);
	}

	public static Deserializer getDeserializer(String mechType, Class _javaType, QName _xmlType) {
		return new EnumDeserializer(_javaType, _xmlType);
	}

	public static TypeDesc getTypeDesc() {
		return typeDesc;
	}

	static {
		typeDesc.setXmlType(new QName("http://tempuri.org/", "OutputFormat"));
	}
}