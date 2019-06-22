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
    <?cocoon-format type="xslfo/rtf"?>
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="1cm" margin-bottom="1cm" margin-left="1cm" margin-right="2cm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
          <fo:block>
            <fo:table table-layout="fixed" width="100%" border-collapse="collapse">
              <xsl:attribute name="background-image"><xsl:value-of select="$background-image-url"/></xsl:attribute>	
              <fo:table-column column-width="78mm"/>
              <fo:table-column column-width="118mm"/>
              <fo:table-body>
                <fo:table-row>
					<fo:table-cell padding="3pt" border-color="#336699" border-style="solid" border-width="0.2mm" border-right="0mm">
						<fo:block text-align="left" font-size="10pt" font-weight="bold">
							<xsl:value-of select="java:getResourceString($docgen,string('REPORT'),string($language),string($country))"/> :  <xsl:value-of select="//project-name"/>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="3pt" border-color="#336699" border-style="solid" border-width="0.2mm" border-left="0mm">
						<fo:block text-align="left" font-size="10pt" font-weight="bold">
							<xsl:value-of select="java:getResourceString($docgen,string('application'),string($language),string($country))"/> :  <xsl:value-of select="//application-name"/>
						</fo:block>
					</fo:table-cell>
			    </fo:table-row>
              </fo:table-body>
            </fo:table>
          </fo:block>
          <fo:block>
            <fo:table table-layout="fixed" width="100%" border-collapse="collapse" >
              <fo:table-column column-width="196mm"/>
              <fo:table-body>
				<fo:table-row>
    				<fo:table-cell border-color="#336699" border-style="solid" border-width="0.1mm" padding="2pt">
      					<fo:block text-align="right" font-size="10pt" font-weight="bold">
      						<xsl:value-of select="java:getResourceString($docgen,string('TOTAL'),string($language),string($country))"/> : <xsl:value-of select="count(//data-row)" />
      					</fo:block>
    				</fo:table-cell>
				 </fo:table-row>
              </fo:table-body>
            </fo:table>
          </fo:block>
          <fo:block font-size="8pt">
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
								<fo:block text-align="center" font-size="8pt" font-weight="bold">
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
              <fo:table-column column-width="196mm"/>
              <fo:table-body>
				<fo:table-row>
    				<fo:table-cell border-color="#336699" border-style="solid" border-width="0.1mm" padding="2pt">
      					<fo:block text-align="right" font-size="10pt" font-weight="bold">
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
					<xsl:value-of select="."/>
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
					<xsl:value-of select="."/>
				  </fo:block>
				</fo:table-cell>
		  </xsl:for-each>
      	 </fo:table-row>
      	</xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
  </xsl:template>
  
  <xsl:template match="//headers-row">
    <xsl:variable name="total-headers" select="count(//header-name) - 1"></xsl:variable>
    <xsl:variable name="avg-column-width" select="186 div $total-headers"></xsl:variable>
    <xsl:variable name="floor-column-width" select="floor($avg-column-width)"></xsl:variable>
	<xsl:variable name="ceil-column-width" select="ceiling($avg-column-width)"></xsl:variable>    
	<xsl:variable name="total_till_now" select="(floor(($total-headers div 2)) * $floor-column-width) +  (floor(($total-headers div 2)) * $ceil-column-width) "/>
    <fo:table-row>
      <xsl:for-each select="//header-name">
		 <fo:table-cell border-color="#336699" border-style="solid" border-width="0.1mm"  padding="3pt">
			<xsl:if test="not(position() = last())">
				 <xsl:attribute name="border-right">0</xsl:attribute>
			</xsl:if>
			<xsl:choose>
				<xsl:when test=". = 'No.'">
				    <xsl:attribute name="width">10mm</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
				    <xsl:choose>
				       <xsl:when test="(position() mod 2 = 0)">
						 <xsl:attribute name="width"><xsl:value-of select="$floor-column-width" />mm</xsl:attribute>
					   </xsl:when>
					   <xsl:otherwise>
					     <xsl:attribute name="width"><xsl:value-of select="$ceil-column-width" />mm</xsl:attribute>
					   </xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="position() = last()">
			    <xsl:choose>
			      <xsl:when test="($total-headers mod 2 = 0)">
					<xsl:choose>
					   <xsl:when test="$total_till_now > 186">
						 <xsl:attribute name="width"><xsl:value-of select="$ceil-column-width - ($total_till_now - 186)" />mm</xsl:attribute>				
					   </xsl:when>
					   <xsl:otherwise>
					     <xsl:attribute name="width"><xsl:value-of select="$ceil-column-width + (186 - $total_till_now)" />mm</xsl:attribute>				
					   </xsl:otherwise>
				    </xsl:choose>
			        </xsl:when>	
			      <xsl:otherwise>
					 <xsl:choose>
					   <xsl:when test="($total_till_now + $floor-column-width) > 186">
						 <xsl:attribute name="width"><xsl:value-of select="$floor-column-width - (($total_till_now + $floor-column-width) - 186)" />mm</xsl:attribute>				
					   </xsl:when> 	
					   <xsl:otherwise>
					     <xsl:attribute name="width"><xsl:value-of select="$floor-column-width + (186 - ($total_till_now + $floor-column-width))" />mm</xsl:attribute>				
					   </xsl:otherwise>
					</xsl:choose>
			      </xsl:otherwise>
			    </xsl:choose>
			</xsl:if>
		    <fo:block font-size="9pt" font-weight="bold" text-align="center">
			    <xsl:value-of select="."/>
		    </fo:block>
		 </fo:table-cell>
      </xsl:for-each> 
    </fo:table-row>
  </xsl:template>
  
</xsl:stylesheet>
