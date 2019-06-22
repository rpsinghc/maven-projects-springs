<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" 
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
 xmlns:fo="http://www.w3.org/1999/XSL/Format" 
 xmlns:xsltc="http://xml.apache.org/xalan/xsltc"
 xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">
  <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
  <xsl:param name="background-image-url"/>
  <xsl:param name="language"/>
  <xsl:param name="country"/>
  <xsl:variable name="docgen" select="java:com.savvion.sbm.bpmportal.service.report.adhoc.AdhocResourceBundle.new()"/>
  
  <xsl:template match="adhoc-report">
      <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="1cm" margin-left="1cm" margin-right="1cm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
          <fo:block>
            <fo:table table-layout="fixed" width="100%" border-collapse="collapse">
              <xsl:attribute name="background-image"><xsl:value-of select="$background-image-url"/></xsl:attribute>	
              <fo:table-body>
                <fo:table-row>
              	  <fo:table-cell border-color="#336699" border-style="solid" border-width="0.2mm" >
	                <fo:table table-layout="fixed" width="100%">
	 			      <fo:table-column column-width="7%"/>
				  	  <fo:table-column column-width="30%"/>
					  <fo:table-column column-width="11%"/>
					  <fo:table-column column-width="52%"/>
                      <fo:table-body>
					  <fo:table-row>
	    				<fo:table-cell padding="3pt">
	      					<fo:block text-align="left" font-size="6pt" font-weight="bold">
	      						<xsl:value-of select="java:getResourceString($docgen,string('REPORT'),string($language),string($country))"/> :
	      					</fo:block>
	    				</fo:table-cell>
	    				<fo:table-cell padding="3pt">
	      					<fo:block font-size="6pt">
	      						<xsl:value-of select="//project-name"/>
	      					</fo:block>
	    				</fo:table-cell>
	    				<fo:table-cell padding="3pt">
	      					<fo:block text-align="left" font-size="6pt" font-weight="bold">
	      						<xsl:value-of select="java:getResourceString($docgen,string('application'),string($language),string($country))"/> :
	      					</fo:block>
	    				</fo:table-cell>
	    				<fo:table-cell padding="3pt">
	      					<fo:block font-size="6pt">
	      						<xsl:value-of select="//application-name"/>
	      					</fo:block>
    					</fo:table-cell>
				   	  </fo:table-row>
				   	</fo:table-body>
				  </fo:table>
			     </fo:table-cell>
			    </fo:table-row>
              </fo:table-body>
            </fo:table>
          </fo:block>
          <fo:block font-size="5pt">
            <fo:table table-layout="fixed" width="100%" border-collapse="collapse" >
              <fo:table-body>
				<fo:table-row>
    				<fo:table-cell border-color="#336699" border-style="solid" border-width="0.1mm" padding="2pt">
      					<fo:block text-align="right" font-size="6pt" font-weight="bold">
      						<xsl:value-of select="java:getResourceString($docgen,string('TOTAL'),string($language),string($country))"/> : <xsl:value-of select="count(//data-row)" />
      					</fo:block>
    				</fo:table-cell>
				 </fo:table-row>
              </fo:table-body>
            </fo:table>
          </fo:block>
          <fo:block font-size="5pt">
            <fo:table table-layout="fixed" width="100%" border-collapse="collapse">
              <xsl:attribute name="background-image"><xsl:value-of select="$background-image-url"/></xsl:attribute>	
              <fo:table-header>
              	<xsl:apply-templates select="headers-row"/>
              </fo:table-header>
              <fo:table-body>
			  	<xsl:choose>
			  		<xsl:when test="count(//data-row) = 0">
						<fo:table-row background-color="#EEF0F2">
							<fo:table-cell border-color="#336699" border-style="solid" border-width="0.1mm" padding="2pt">
								<xsl:attribute name="number-columns-spanned">
									<xsl:value-of select="count(//header-name)"/>
								</xsl:attribute>
								<fo:block text-align="center" font-size="6pt" font-weight="bold">
									<xsl:value-of select="java:getResourceString($docgen,string('noSearchResult'),string($language),string($country))"/>
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:when>
					<xsl:otherwise>
                <xsl:apply-templates select="data"/>
					</xsl:otherwise>
			  	</xsl:choose>
              </fo:table-body>
            </fo:table>
          </fo:block>
          <fo:block font-size="5pt">
            <fo:table table-layout="fixed" width="100%" border-collapse="separate">
              <fo:table-body>
				<fo:table-row>
    				<fo:table-cell border-color="#336699" border-style="solid" border-width="0.1mm" padding="2pt">
      					<fo:block text-align="right" font-size="6pt" font-weight="bold">
      						<xsl:value-of select="java:getResourceString($docgen,string('TOTAL'),string($language),string($country))"/> : <xsl:value-of select="count(//data-row)" />
      					</fo:block>
    				</fo:table-cell>
				 </fo:table-row>
              </fo:table-body>
            </fo:table>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <!-- ========================= -->
  <!-- child element: member     -->
  <!-- ========================= -->
  <xsl:template match="//data">
    <xsl:for-each select="//data-row">
      <xsl:choose>	
        <xsl:when test="position() mod 2 = 0">
          <fo:table-row background-color="#EEF0F2">
            <xsl:for-each select="./datavalue">
				 <fo:table-cell border-color="#336699" border-style="solid" border-width="0.1mm" padding="2pt">
					<xsl:if test="not(position() = last())">
						<xsl:attribute name="border-right">0</xsl:attribute>
					</xsl:if>
				   <fo:block>
				   <xsl:if test="normalize-space(.) != ''">
					     <xsl:call-template name="zero_width_space_1">
                  <xsl:with-param name="data" select="normalize-space(.)"/>
               </xsl:call-template>
           </xsl:if>
               <!-- <xsl:value-of select="."/> -->
				   </fo:block>
				 </fo:table-cell>
		  </xsl:for-each>
      	 </fo:table-row>
      	</xsl:when>
      	<xsl:otherwise>
          <fo:table-row background-color="#FFFFFF">
           <xsl:for-each select="./datavalue">
				<fo:table-cell border-color="#336699" border-style="solid" border-width="0.1mm" padding="2pt">
				  <xsl:if test="not(position() = last())">
					<xsl:attribute name="border-right">0</xsl:attribute>
				  </xsl:if>
				  <fo:block>
				  <xsl:if test="normalize-space(.) != ''">
					  <xsl:call-template name="zero_width_space_1">
              <xsl:with-param name="data" select="normalize-space(.)"/>
           </xsl:call-template>
          </xsl:if>
           <!-- <xsl:value-of select="."/>  -->
				  </fo:block>
				</fo:table-cell>
		  </xsl:for-each>
      	 </fo:table-row>
      	</xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template match="//headers-row">
    <fo:table-row>
      <xsl:for-each select="//header-name">
		 <fo:table-cell border-color="#336699" border-style="solid" border-width="0.1mm"  padding="3pt">
			<xsl:if test="not(position() = last())">
				 <xsl:attribute name="border-right">0</xsl:attribute>
			</xsl:if>
			<xsl:if test=". = 'No.'">
				 <xsl:attribute name="width">10mm</xsl:attribute>
			</xsl:if>
		    <fo:block font-size="6pt" font-weight="bold" text-align="center" hyphenate="true">
		      <xsl:if test="normalize-space(.) != ''">
			     <xsl:call-template name="zero_width_space_1">
              <xsl:with-param name="data" select="normalize-space(.)"/>
           </xsl:call-template>
          </xsl:if>
           <!-- <xsl:value-of select="."/> -->
		    </fo:block>
		 </fo:table-cell>
      </xsl:for-each> 
    </fo:table-row>
  </xsl:template>
  
  <!-- Templates meant for zero-width-space wrapping so as to wrap longer strings -->
  
  <xsl:template name="zero_width_space_1">
    <xsl:param name="data"/>
    <xsl:param name="counter" select="0"/>
    <xsl:choose>
        <xsl:when test="$counter &lt;= string-length($data)">
          <xsl:value-of select='concat(substring($data,$counter,1),"&#8203;")'/>
           <xsl:call-template name="zero_width_space_2">
                   <xsl:with-param name="data" select="$data"/>
                   <xsl:with-param name="counter" select="$counter+1"/>
              </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
        </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="zero_width_space_2">
     <xsl:param name="data"/>
     <xsl:param name="counter"/>
     <xsl:value-of select='concat(substring($data,$counter,1),"&#8203;")'/>
     <xsl:call-template name="zero_width_space_1">
      <xsl:with-param name="data" select="$data"/>
      <xsl:with-param name="counter" select="$counter+1"/>
     </xsl:call-template>
  </xsl:template>
  
</xsl:stylesheet>
