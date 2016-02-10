
<%@ page import="com.munix.Product; com.munix.PriceAdjustment; com.munix.PriceAdjustmentItem"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="priceAdjustment.create" default="Create Price Adjustment" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="priceAdjustment.list" default="Price Adjustment List" /></g:link></span>
        </div>
        <div class="body">
            <calendar:resources lang="en" theme="aqua"/>
            <h1><g:message code="priceAdjustment.create" default="Create Price Adjustment" /></h1>
            <g:if test="${flash.message}">
              <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${priceAdjustmentInstance}">
            <div class="errors">
                <g:renderErrors bean="${priceAdjustmentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
					        <tr class="prop">
					            <td valign="top" class="name">
					              <label for="effectiveDate"><g:message code="priceAdjustment.effectiveDate.label" default="Effective Date" /></label>
					            </td>
					            <td valign="top" class="value ${hasErrors(bean: priceAdjustmentInstance, field: 'effectiveDate', 'errors')}">
					          		<calendar:datePicker name="effectiveDate"  years="2009,2030" value="${priceAdjustmentInstance?.effectiveDate}"/>
					        	</td>
					        </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="priceType"><g:message code="priceAdjustment.priceType" default="Price Type" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: priceAdjustmentInstance, field: 'priceType', 'errors')}">
                                    <g:select name="priceType" id="priceType" from="${com.munix.PriceType?.values()}" value="${priceAdjustmentInstance?.priceType}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="itemType"><g:message code="priceAdjustment.itemType" default="Item Type" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: priceAdjustmentInstance, field: 'itemType', 'errors')}">
                                    <g:select name="itemType.id" id="itemType" from="${com.munix.ItemType.list().sort{it.id}}" optionKey="id" value="${priceAdjustmentInstance?.itemType?.id}"  />
                                </td>
                            </tr>
                        
        					<tr class="prop">
         						<td valign="top" class="name">
          							<label for="remark"><g:message code="priceAdjustment.remark.label" default="Remarks" /></label>
         						</td>
        	 					<td valign="top" class="value ${hasErrors(bean: priceAdjustmentInstance, field: 'remark', 'errors')}">
          							<g:textArea name="remark" value="${priceAdjustmentInstance?.remark}" />
         						</td>
        					</tr>
                        </tbody>
                    </table>
                </div>
                
                <table height=10px><tr></tr>
                </table>
                
                <g:render template="items" model="['priceAdjustmentInstance': priceAdjustmentInstance, 'warehouses': warehouses]" />
				<div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'create', 'default': 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
