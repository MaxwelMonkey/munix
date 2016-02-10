
<%@ page import="com.munix.Product" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="default.priceAdjustmentHistory.label" default="Price Adjustment History" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
        </div>
        <div class="body">
            <h1><g:message code="default.priceAdjustmentHistory.label" default="Price Adjustment History" /></h1>
            <g:if test="${flash.message}">
              <div class="message">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
              <div class="errors">${flash.error}</div>
            </g:if>
            <g:form>
                <g:set var="prod" value="${productInstance}" />
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="priceAdjustmentHistory.identifier.label" default="Identifier" /></td>
                                <td valign="top" class="value">${prod?.identifier}</td>
                            </tr>
							<tr class="prop">
                                <td valign="top" class="name"><g:message code="priceAdjustmentHistory.description.label" default="Description" /></td>
                                <td valign="top" class="value">${prod?.description}</td>
                            </tr>
                                            
                        </tbody>
                    </table>
                </div>
                <div class="subTable">
    				<table>
        				<thead>
          					<th class="center">Date Created</th>
          					<th class="center">Date Applied</th>
          					<th class="center">ID</th>
          					<th class="center">Old Price</th>
          					<th class="center">New Price</th>
          					<th class="center">Margin</th>
        				</thead>
        				<tbody>
        					<g:each in="${appliedPriceAdjustmentItems}" var="item">
          						<tr class="prop">
            						<td class="center"><g:formatDate date="${item?.priceAdjustment?.dateGenerated}" format="MMMM dd,yyyy" /></td>
            						<td class="center"><g:formatDate date="${item?.priceAdjustment?.effectiveDate}" format="MMMM dd,yyyy" /></td>
            						<td class="center"><g:link controller="priceAdjustment" action="show" id="${item?.priceAdjustment?.id}">${item?.priceAdjustment}</g:link></td>
           							<td class="right">PHP <g:formatNumber number="${item?.oldPrice}" format="###,##0.00" /></td>
           							<td class="right">PHP <g:formatNumber number="${item?.newPrice}" format="###,##0.00" /></td>
           							<td class="right"><g:formatNumber number="${item?.computeMargin()}" format="###,##0.00" /> %</td>
          						</tr>
        					</g:each>
        				</tbody>
    				</table>
				</div>
				<div>
					<input type="button" class="button" value="Back" onclick="window.location='${createLink(uri:'/')}product/show/${prod.id}'">
	            </div>
            </g:form>
        </div>
    </body>
</html>
