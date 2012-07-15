<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Edited by XMLSpy® -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/solutions">
		<html>
			<head>
				<style type="text/css">
					table.wikitable {
						margin: 1em 1em 1em 0;
						background-color: #F9F9F9;
						color: black;
						font-family: sans-serif;
						line-height: 1.5em;
						font-size: 13px;
						border: 1px #AAA solid;
						border-collapse: collapse;
					}

					tr, th, td {
						border: 1px #AAA solid;
					}

					table.wikitable > tr > th, table.wikitable > * > tr > th {
						background-color: #F2F2F2;
					}
				</style>
			</head>
			<body>
				<xsl:for-each select="solution">
					<table class="wikitable">
						<tr>
							<th>House</th>
							<xsl:for-each select="house">
								<th>
									<xsl:value-of select="@position" />
								</th>
							</xsl:for-each>
						</tr>
						<tr>
							<th>Color</th>
							<xsl:for-each select="house">
								<td>
									<xsl:value-of select="@color" />
								</td>
							</xsl:for-each>
						</tr>
						<tr>
							<th>Nationality</th>
							<xsl:for-each select="house">
								<td>
									<xsl:value-of select="@nationality" />
								</td>
							</xsl:for-each>
						</tr>
						<tr>
							<th>Drink</th>
							<xsl:for-each select="house">
								<td>
									<xsl:value-of select="@drink" />
								</td>
							</xsl:for-each>
						</tr>
						<tr>
							<th>Smoke</th>
							<xsl:for-each select="house">
								<td>
									<xsl:value-of select="@smoke" />
								</td>
							</xsl:for-each>
						</tr>
						<tr>
							<th>Pet</th>
							<xsl:for-each select="house">
								<td>
									<xsl:value-of select="@pet" />
								</td>
							</xsl:for-each>
						</tr>
					</table>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>