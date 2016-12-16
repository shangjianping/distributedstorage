<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="UTF-8" version="1.0" />

	<xsl:template match="/IDR">
	<IDR xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance">
		<xsl:for-each select="IDR_IMAGE">
		<IDR_IMAGE>
			<SCANTIME>
				<xsl:value-of select="SCANTIME"/>
			</SCANTIME>
			<PICNO>
				<xsl:value-of select="PICNO"/>
			</PICNO>
			<xsl:for-each select="IDR_CHECK_UNIT">
			<IDR_CHECK_UNIT>
				<CHECKINTIME>
					<xsl:value-of select="CHECKINTIME"/>
				</CHECKINTIME>
				<xsl:for-each select="IDR_SIIG">
					<xsl:call-template name="IDR_SIIG"/>
				</xsl:for-each>
				<xsl:for-each select="IDR_CONCLUSION">
					<xsl:call-template name="IDR_CONCLUSION"/>
				</xsl:for-each>
			</IDR_CHECK_UNIT>
			</xsl:for-each>
		</IDR_IMAGE>
		</xsl:for-each>
	</IDR>
	</xsl:template>

	<xsl:template match="IDR_SIIG" name="IDR_SIIG">
		<xsl:choose>
			<xsl:when test="TYPE = 'EDI'">
				<IDR_SIIG>
					<TYPE>EDI</TYPE>
					<xsl:call-template name="EDI"/>
				</IDR_SIIG>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="EDI">
		<IDR_SII_EDI_TOTAL>
			<xsl:for-each select="./EDI/Entry">
				<IDR_SII_EDI_ENTRY>
					<ENTRY_ID>
						<xsl:value-of select="./ENTRY_ID"/>
					</ENTRY_ID>
					<IMP_C>
						<xsl:value-of select="./IMP_C"/>
					</IMP_C>
					<PACK_MARK>
						<xsl:value-of select="./PACK_MARK"/>
					</PACK_MARK>
					<GROSS_WT>
						<xsl:value-of select="./GROSS_WT"/>
					</GROSS_WT>
					<LADING_BILL_NO>
						<xsl:value-of select="./LADING_BILL_NO"/>
					</LADING_BILL_NO>
					<IDR_SII_EDI_ENTRY_MANINFO>
						<Name>
							<xsl:value-of select="./BUYER_INFO/Name"/>
						</Name>
						<Type>BUYER_INFO</Type>
					</IDR_SII_EDI_ENTRY_MANINFO>
					<IDR_SII_EDI_ENTRY_MANINFO>
						<Name>
							<xsl:value-of select="./AGENT_INFO/Name"/>
						</Name>
						<Type>AGENT_INFO</Type>
					</IDR_SII_EDI_ENTRY_MANINFO>
					<IDR_SII_EDI_ENTRY_MANINFO>
						<Name>
							<xsl:value-of select="./SELLER_INFO/Name"/>
						</Name>
						<Type>SELLER_INFO</Type>
					</IDR_SII_EDI_ENTRY_MANINFO>
					<xsl:for-each select="./EntryItem">
						<IDR_SII_EDI_ENTRY_ITEM>
							<HS_C>
								<xsl:value-of select="./HS_C"/>
							</HS_C>
							<GOODS_D>
								<xsl:value-of select="./GOODS_D"/>
							</GOODS_D>
						</IDR_SII_EDI_ENTRY_ITEM>
					</xsl:for-each>
				</IDR_SII_EDI_ENTRY>
			</xsl:for-each>
		</IDR_SII_EDI_TOTAL>
	</xsl:template>

	<xsl:template name="IDR_CONCLUSION">
		<IDR_CONCLUSION>
			<TYPE>
				<xsl:value-of select="./TYPE"/>
			</TYPE>
			<IDR_CONCLUSION_CONTENT>
				<CONTENT>
					<xsl:value-of select="./CONTENT/CONTENT"/>
				</CONTENT>
			</IDR_CONCLUSION_CONTENT>
		</IDR_CONCLUSION>
	</xsl:template>

</xsl:stylesheet>
