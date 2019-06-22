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

public class ReportType implements Serializable {
	private String _value_;
	private static HashMap _table_ = new HashMap();
	public static final String _IS1 = "IS1";
	public static final String _IS2 = "IS2";
	public static final String _IS3 = "IS3";
	public static final String _FCP = "FCP";
	public static final ReportType IS1 = new ReportType("IS1");
	public static final ReportType IS2 = new ReportType("IS2");
	public static final ReportType IS3 = new ReportType("IS3");
	public static final ReportType FCP = new ReportType("FCP");
	private static TypeDesc typeDesc = new TypeDesc(ReportType.class);

	protected ReportType(String value) {
		this._value_ = value;
		_table_.put(this._value_, this);
	}

	public String getValue() {
		return this._value_;
	}

	public static ReportType fromValue(String value) throws IllegalArgumentException {
		ReportType enumeration = (ReportType) _table_.get(value);
		if (enumeration == null) {
			throw new IllegalArgumentException();
		} else {
			return enumeration;
		}
	}

	public static ReportType fromString(String value) throws IllegalArgumentException {
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
		typeDesc.setXmlType(new QName("http://tempuri.org/", "ReportType"));
	}
}