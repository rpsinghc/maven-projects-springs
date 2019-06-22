package com.integrascreen.orders;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class RiskAggregation implements Serializable {
	private int totalCRNRiskSummary;
	private int totalCRNRiskScore;
	private String totalCRNRAGColour;
	private int totalCaseLevelRiskSummary;
	private int totalCaseLevelRiskScore;
	private String totalCaseLevelRAGColour;
	private int categoryRiskSummary;
	private int categoryRiskScore;
	private String categoryRAGColour;
	private int totalSubjectRiskSummary;
	private int totalSubjectRiskScore;
	private String totalSubjectLevelRAGColour;
	private int individualSubjectRiskSummary;
	private String individualSubjectRAGColour;
	private int subjectCategoryRiskSummary;
	private int subjectRiskScore;
	private int howmanyRiskCountriesMedium;
	private int howmanyRiskCountriesHigh;
	private int howManyRiskCountriesVeryHigh;
	private int countryRiskSummaryCount;
	private int totalCategoryScore;
	private String countryRiskSummaryValue;
	private Object __equalsCalc = null;
	private boolean __hashCodeCalc = false;
	private static TypeDesc typeDesc = new TypeDesc(RiskAggregation.class, true);

	static {
		typeDesc.setXmlType(new QName("http://orders.integrascreen.com", "RiskAggregation"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("totalCRNRiskSummary");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "TotalCRNRiskSummary"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("totalCRNRiskScore");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "TotalCRNRiskScore"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("totalCRNRAGColour");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "TotalCRNRAGColour"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("totalCaseLevelRiskSummary");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "TotalCaseLevelRiskSummary"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("totalCaseLevelRiskScore");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "TotalCaseLevelRiskScore"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("totalCaseLevelRAGColour");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "TotalCaseLevelRAGColour"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("categoryRiskSummary");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CategoryRiskSummary"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("categoryRiskScore");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CategoryRiskScore"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("categoryRAGColour");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CategoryRAGColour"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("totalSubjectRiskSummary");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "TotalSubjectRiskSummary"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("totalSubjectRiskScore");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "TotalSubjectRiskScore"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("totalSubjectLevelRAGColour");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "TotalSubjectLevelRAGColour"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("individualSubjectRiskSummary");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "IndividualSubjectRiskSummary"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("individualSubjectRAGColour");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "individualSubjectRAGColour"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectCategoryRiskSummary");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectCategoryRiskSummary"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("subjectRiskScore");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "SubjectRiskScore"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("howmanyRiskCountriesMedium");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "HowmanyRiskCountriesMedium"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("howmanyRiskCountriesHigh");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "HowmanyRiskCountriesHigh"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("howManyRiskCountriesVeryHigh");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "HowManyRiskCountriesVeryHigh"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("countryRiskSummaryCount");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CountryRiskSummaryCount"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("totalCategoryScore");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "TotalCategoryScore"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("countryRiskSummaryValue");
		elemField.setXmlName(new QName("http://orders.integrascreen.com", "CountryRiskSummaryValue"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	public RiskAggregation() {
	}

	public RiskAggregation(int totalCRNRiskSummary, int totalCRNRiskScore, String totalCRNRAGColour,
			int totalCaseLevelRiskSummary, int totalCaseLevelRiskScore, String totalCaseLevelRAGColour,
			int categoryRiskSummary, int categoryRiskScore, String categoryRAGColour, int totalSubjectRiskSummary,
			int totalSubjectRiskScore, String totalSubjectLevelRAGColour, int individualSubjectRiskSummary,
			String individualSubjectRAGColour, int subjectCategoryRiskSummary, int subjectRiskScore,
			int howmanyRiskCountriesMedium, int howmanyRiskCountriesHigh, int howManyRiskCountriesVeryHigh,
			int countryRiskSummaryCount, int totalCategoryScore, String countryRiskSummaryValue) {
		this.totalCRNRiskSummary = totalCRNRiskSummary;
		this.totalCRNRiskScore = totalCRNRiskScore;
		this.totalCRNRAGColour = totalCRNRAGColour;
		this.totalCaseLevelRiskSummary = totalCaseLevelRiskSummary;
		this.totalCaseLevelRiskScore = totalCaseLevelRiskScore;
		this.totalCaseLevelRAGColour = totalCaseLevelRAGColour;
		this.categoryRiskSummary = categoryRiskSummary;
		this.categoryRiskScore = categoryRiskScore;
		this.categoryRAGColour = categoryRAGColour;
		this.totalSubjectRiskSummary = totalSubjectRiskSummary;
		this.totalSubjectRiskScore = totalSubjectRiskScore;
		this.totalSubjectLevelRAGColour = totalSubjectLevelRAGColour;
		this.individualSubjectRiskSummary = individualSubjectRiskSummary;
		this.individualSubjectRAGColour = individualSubjectRAGColour;
		this.subjectCategoryRiskSummary = subjectCategoryRiskSummary;
		this.subjectRiskScore = subjectRiskScore;
		this.howmanyRiskCountriesMedium = howmanyRiskCountriesMedium;
		this.howmanyRiskCountriesHigh = howmanyRiskCountriesHigh;
		this.howManyRiskCountriesVeryHigh = howManyRiskCountriesVeryHigh;
		this.countryRiskSummaryCount = countryRiskSummaryCount;
		this.totalCategoryScore = totalCategoryScore;
		this.countryRiskSummaryValue = countryRiskSummaryValue;
	}

	public int getTotalCRNRiskSummary() {
		return this.totalCRNRiskSummary;
	}

	public void setTotalCRNRiskSummary(int totalCRNRiskSummary) {
		this.totalCRNRiskSummary = totalCRNRiskSummary;
	}

	public int getTotalCRNRiskScore() {
		return this.totalCRNRiskScore;
	}

	public void setTotalCRNRiskScore(int totalCRNRiskScore) {
		this.totalCRNRiskScore = totalCRNRiskScore;
	}

	public String getTotalCRNRAGColour() {
		return this.totalCRNRAGColour;
	}

	public void setTotalCRNRAGColour(String totalCRNRAGColour) {
		this.totalCRNRAGColour = totalCRNRAGColour;
	}

	public int getTotalCaseLevelRiskSummary() {
		return this.totalCaseLevelRiskSummary;
	}

	public void setTotalCaseLevelRiskSummary(int totalCaseLevelRiskSummary) {
		this.totalCaseLevelRiskSummary = totalCaseLevelRiskSummary;
	}

	public int getTotalCaseLevelRiskScore() {
		return this.totalCaseLevelRiskScore;
	}

	public void setTotalCaseLevelRiskScore(int totalCaseLevelRiskScore) {
		this.totalCaseLevelRiskScore = totalCaseLevelRiskScore;
	}

	public String getTotalCaseLevelRAGColour() {
		return this.totalCaseLevelRAGColour;
	}

	public void setTotalCaseLevelRAGColour(String totalCaseLevelRAGColour) {
		this.totalCaseLevelRAGColour = totalCaseLevelRAGColour;
	}

	public int getCategoryRiskSummary() {
		return this.categoryRiskSummary;
	}

	public void setCategoryRiskSummary(int categoryRiskSummary) {
		this.categoryRiskSummary = categoryRiskSummary;
	}

	public int getCategoryRiskScore() {
		return this.categoryRiskScore;
	}

	public void setCategoryRiskScore(int categoryRiskScore) {
		this.categoryRiskScore = categoryRiskScore;
	}

	public String getCategoryRAGColour() {
		return this.categoryRAGColour;
	}

	public void setCategoryRAGColour(String categoryRAGColour) {
		this.categoryRAGColour = categoryRAGColour;
	}

	public int getTotalSubjectRiskSummary() {
		return this.totalSubjectRiskSummary;
	}

	public void setTotalSubjectRiskSummary(int totalSubjectRiskSummary) {
		this.totalSubjectRiskSummary = totalSubjectRiskSummary;
	}

	public int getTotalSubjectRiskScore() {
		return this.totalSubjectRiskScore;
	}

	public void setTotalSubjectRiskScore(int totalSubjectRiskScore) {
		this.totalSubjectRiskScore = totalSubjectRiskScore;
	}

	public String getTotalSubjectLevelRAGColour() {
		return this.totalSubjectLevelRAGColour;
	}

	public void setTotalSubjectLevelRAGColour(String totalSubjectLevelRAGColour) {
		this.totalSubjectLevelRAGColour = totalSubjectLevelRAGColour;
	}

	public int getIndividualSubjectRiskSummary() {
		return this.individualSubjectRiskSummary;
	}

	public void setIndividualSubjectRiskSummary(int individualSubjectRiskSummary) {
		this.individualSubjectRiskSummary = individualSubjectRiskSummary;
	}

	public String getIndividualSubjectRAGColour() {
		return this.individualSubjectRAGColour;
	}

	public void setIndividualSubjectRAGColour(String individualSubjectRAGColour) {
		this.individualSubjectRAGColour = individualSubjectRAGColour;
	}

	public int getSubjectCategoryRiskSummary() {
		return this.subjectCategoryRiskSummary;
	}

	public void setSubjectCategoryRiskSummary(int subjectCategoryRiskSummary) {
		this.subjectCategoryRiskSummary = subjectCategoryRiskSummary;
	}

	public int getSubjectRiskScore() {
		return this.subjectRiskScore;
	}

	public void setSubjectRiskScore(int subjectRiskScore) {
		this.subjectRiskScore = subjectRiskScore;
	}

	public int getHowmanyRiskCountriesMedium() {
		return this.howmanyRiskCountriesMedium;
	}

	public void setHowmanyRiskCountriesMedium(int howmanyRiskCountriesMedium) {
		this.howmanyRiskCountriesMedium = howmanyRiskCountriesMedium;
	}

	public int getHowmanyRiskCountriesHigh() {
		return this.howmanyRiskCountriesHigh;
	}

	public void setHowmanyRiskCountriesHigh(int howmanyRiskCountriesHigh) {
		this.howmanyRiskCountriesHigh = howmanyRiskCountriesHigh;
	}

	public int getHowManyRiskCountriesVeryHigh() {
		return this.howManyRiskCountriesVeryHigh;
	}

	public void setHowManyRiskCountriesVeryHigh(int howManyRiskCountriesVeryHigh) {
		this.howManyRiskCountriesVeryHigh = howManyRiskCountriesVeryHigh;
	}

	public int getCountryRiskSummaryCount() {
		return this.countryRiskSummaryCount;
	}

	public void setCountryRiskSummaryCount(int countryRiskSummaryCount) {
		this.countryRiskSummaryCount = countryRiskSummaryCount;
	}

	public int getTotalCategoryScore() {
		return this.totalCategoryScore;
	}

	public void setTotalCategoryScore(int totalCategoryScore) {
		this.totalCategoryScore = totalCategoryScore;
	}

	public String getCountryRiskSummaryValue() {
		return this.countryRiskSummaryValue;
	}

	public void setCountryRiskSummaryValue(String countryRiskSummaryValue) {
		this.countryRiskSummaryValue = countryRiskSummaryValue;
	}

	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof RiskAggregation)) {
			return false;
		} else {
			RiskAggregation other = (RiskAggregation) obj;
			if (obj == null) {
				return false;
			} else if (this == obj) {
				return true;
			} else if (this.__equalsCalc != null) {
				return this.__equalsCalc == obj;
			} else {
				this.__equalsCalc = obj;
				boolean _equals = this.totalCRNRiskSummary == other.getTotalCRNRiskSummary()
						&& this.totalCRNRiskScore == other.getTotalCRNRiskScore()
						&& (this.totalCRNRAGColour == null && other.getTotalCRNRAGColour() == null
								|| this.totalCRNRAGColour != null
										&& this.totalCRNRAGColour.equals(other.getTotalCRNRAGColour()))
						&& this.totalCaseLevelRiskSummary == other.getTotalCaseLevelRiskSummary()
						&& this.totalCaseLevelRiskScore == other.getTotalCaseLevelRiskScore()
						&& (this.totalCaseLevelRAGColour == null && other.getTotalCaseLevelRAGColour() == null
								|| this.totalCaseLevelRAGColour != null
										&& this.totalCaseLevelRAGColour.equals(other.getTotalCaseLevelRAGColour()))
						&& this.categoryRiskSummary == other.getCategoryRiskSummary()
						&& this.categoryRiskScore == other.getCategoryRiskScore()
						&& (this.categoryRAGColour == null && other.getCategoryRAGColour() == null
								|| this.categoryRAGColour != null
										&& this.categoryRAGColour.equals(other.getCategoryRAGColour()))
						&& this.totalSubjectRiskSummary == other.getTotalSubjectRiskSummary()
						&& this.totalSubjectRiskScore == other.getTotalSubjectRiskScore()
						&& (this.totalSubjectLevelRAGColour == null && other.getTotalSubjectLevelRAGColour() == null
								|| this.totalSubjectLevelRAGColour != null && this.totalSubjectLevelRAGColour
										.equals(other.getTotalSubjectLevelRAGColour()))
						&& this.individualSubjectRiskSummary == other.getIndividualSubjectRiskSummary()
						&& (this.individualSubjectRAGColour == null && other.getIndividualSubjectRAGColour() == null
								|| this.individualSubjectRAGColour != null && this.individualSubjectRAGColour
										.equals(other.getIndividualSubjectRAGColour()))
						&& this.subjectCategoryRiskSummary == other.getSubjectCategoryRiskSummary()
						&& this.subjectRiskScore == other.getSubjectRiskScore()
						&& this.howmanyRiskCountriesMedium == other.getHowmanyRiskCountriesMedium()
						&& this.howmanyRiskCountriesHigh == other.getHowmanyRiskCountriesHigh()
						&& this.howManyRiskCountriesVeryHigh == other.getHowManyRiskCountriesVeryHigh()
						&& this.countryRiskSummaryCount == other.getCountryRiskSummaryCount()
						&& this.totalCategoryScore == other.getTotalCategoryScore()
						&& (this.countryRiskSummaryValue == null && other.getCountryRiskSummaryValue() == null
								|| this.countryRiskSummaryValue != null
										&& this.countryRiskSummaryValue.equals(other.getCountryRiskSummaryValue()));
				this.__equalsCalc = null;
				return _equals;
			}
		}
	}

	public synchronized int hashCode() {
		if (this.__hashCodeCalc) {
			return 0;
		} else {
			this.__hashCodeCalc = true;
			int _hashCode = 1;
			int _hashCode = _hashCode + this.getTotalCRNRiskSummary();
			_hashCode += this.getTotalCRNRiskScore();
			if (this.getTotalCRNRAGColour() != null) {
				_hashCode += this.getTotalCRNRAGColour().hashCode();
			}

			_hashCode += this.getTotalCaseLevelRiskSummary();
			_hashCode += this.getTotalCaseLevelRiskScore();
			if (this.getTotalCaseLevelRAGColour() != null) {
				_hashCode += this.getTotalCaseLevelRAGColour().hashCode();
			}

			_hashCode += this.getCategoryRiskSummary();
			_hashCode += this.getCategoryRiskScore();
			if (this.getCategoryRAGColour() != null) {
				_hashCode += this.getCategoryRAGColour().hashCode();
			}

			_hashCode += this.getTotalSubjectRiskSummary();
			_hashCode += this.getTotalSubjectRiskScore();
			if (this.getTotalSubjectLevelRAGColour() != null) {
				_hashCode += this.getTotalSubjectLevelRAGColour().hashCode();
			}

			_hashCode += this.getIndividualSubjectRiskSummary();
			if (this.getIndividualSubjectRAGColour() != null) {
				_hashCode += this.getIndividualSubjectRAGColour().hashCode();
			}

			_hashCode += this.getSubjectCategoryRiskSummary();
			_hashCode += this.getSubjectRiskScore();
			_hashCode += this.getHowmanyRiskCountriesMedium();
			_hashCode += this.getHowmanyRiskCountriesHigh();
			_hashCode += this.getHowManyRiskCountriesVeryHigh();
			_hashCode += this.getCountryRiskSummaryCount();
			_hashCode += this.getTotalCategoryScore();
			if (this.getCountryRiskSummaryValue() != null) {
				_hashCode += this.getCountryRiskSummaryValue().hashCode();
			}

			this.__hashCodeCalc = false;
			return _hashCode;
		}
	}

	public static TypeDesc getTypeDesc() {
		return typeDesc;
	}

	public static Serializer getSerializer(String mechType, Class _javaType, QName _xmlType) {
		return new BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	public static Deserializer getDeserializer(String mechType, Class _javaType, QName _xmlType) {
		return new BeanDeserializer(_javaType, _xmlType, typeDesc);
	}
}