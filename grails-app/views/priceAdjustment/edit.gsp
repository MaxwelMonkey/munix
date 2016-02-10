
<%@ page import="com.munix.PriceAdjustment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="priceAdjustment.edit" default="Edit Price Adjustment" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
        <g:javascript src="table/jquery.dataTables.js" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="priceAdjustment.list" default="Price Adjustment List" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="priceAdjustment.new" default="New Price Adjustment" /></g:link></span>
        </div>
        <div class="body">
            <calendar:resources lang="en" theme="aqua"/>
            <h1><g:message code="priceAdjustment.edit" default="Edit PriceAdjustment" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${priceAdjustmentInstance}">
            <div class="errors">
                <g:renderErrors bean="${priceAdjustmentInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${priceAdjustmentInstance?.id}" />
                <g:hiddenField name="version" value="${priceAdjustmentInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                        	<tr class="prop">
                                <td valign="top" class="name"><g:message code="priceAdjustment.id" default="ID" /></td>
                                <td valign="top" class="value" width="300px">${priceAdjustmentInstance}</td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name"><label for="itemType"><g:message code="priceAdjustment.itemType" default="Item Type" />:</label></td>
                                <td valign="top">${priceAdjustmentInstance?.itemType}</td>
			                    <g:hiddenField name="itemType.id" id="itemType" value="${priceAdjustmentInstance?.itemType?.id}" />
                            </tr>
                            
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
          							<label for="remark"><g:message code="priceAdjustment.remark.label" default="Remarks" /></label>
         						</td>
        	 					<td valign="top" class="value ${hasErrors(bean: priceAdjustmentInstance, field: 'remark', 'errors')}">
          							<g:textArea name="remark" value="${priceAdjustmentInstance?.remark}" />
         						</td>
        					</tr>                        
                        </tbody>
                    </table>
                </div>
                <g:render template="items" model="['priceAdjustmentInstance': priceAdjustmentInstance, 'warehouses': warehouses]" />
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'update', 'default': 'Update')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
