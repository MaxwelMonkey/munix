<%@ page import="com.munix.StockCard" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:javascript src="jquery.ui.core.js" />
        <g:set var="entityName" value="${message(code: 'stockCostHistory.label', default: 'Stock Cost History')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
        </div>
        <div class="body">
            <h1><g:message code="stockCard.show" default="Stock Cost History" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            
            <div class="dialog">
      			<table>
        			<tbody>

          				<tr class="prop">
            				<td valign="top" class="name"><g:message code="product.identifier.label" default="Identifier" /></td>
        					<td valign="top" class="value">${product}</td>
        				</tr>
        				<tr class="prop">
            				<td valign="top" class="name"><g:message code="product.description.label" default="Description" /></td>
        					<td valign="top" class="value">${product?.description}</td>
        				</tr>
        				
       				</tbody>
    			</table>
   			</div>
       
            <g:form action="show" method="post">
                <div class="subTable">
                    <table id="stockCostItemTable">
                        <thead>
                            <th style="width:150px">Date</th>
                            <th>Supplier</th>
                            <th>Reference #</th>
                            <th>Supplier Reference #</th>
                            <th>Cost (Foreign)</th>
                            <th>Exchange Rate</th>
                            <th>Cost (Peso)</th>
                            <th>Quantity</th>
                        </thead>
                        <tbody>
                            <g:each in="${stockCostItems}" var="item">
                            	<tr>
                                	<td><g:formatDate date="${item.date}" format="MM/dd/yyyy" /></td>
                                    <td>${item.supplier}</td>
                                    <td>
                                    	<g:if test="${item.purchaseInvoice}">
                                    	<g:link controller="purchaseInvoice" action="show" id="${item.purchaseInvoice?.id}">${item.reference}</g:link>
                                    	</g:if>
                                    	<g:else>
                                    	<g:link controller="jobOut" action="show" id="${item.id}">${item.reference}</g:link>
                                    	</g:else>
                                    </td>
									<td>${item.supplierReference}</td>
                                    <td class="right">${item.costForeign}</td>
                                    <td class="right"><g:formatNumber number="${item.exchangeRate}" format="###,##0.00"/></td>
                                    <td class="right"><g:formatNumber number="${item.costLocal}" format="###,##0.0000"/></td>
                                    <td class="right"><g:formatNumber number="${item.qty}" format="###,##0.00"/></td>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                	<span class="button"><g:link controller="product" action="show" id="${product?.id}">Back</g:link></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
