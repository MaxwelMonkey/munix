
<%@ page import="com.munix.PriceAdjustment" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="priceAdjustment.show" default="Show Price Adjustment" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="priceAdjustment.list" default="Price Adjustment List" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="priceAdjustment.new" default="New Price Adjustment" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="priceAdjustment.show" default="Show Price Adjustment" /></h1>
            <g:if test="${flash.message}">
              <div class="message">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
              <div class="errors">${flash.error}</div>
            </g:if>
            <g:form>
                <g:hiddenField name="id" value="${priceAdjustmentInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="priceAdjustment.id" default="ID" /></td>
                                <td valign="top" class="value" width="300px">${priceAdjustmentInstance}</td>
                            </tr>
                                                       
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="priceAdjustment.effectiveDate" default="Effective Date" /></td>
                                <td valign="top" class="value" width="300px"><g:formatDate date="${priceAdjustmentInstance?.effectiveDate}" format="MM/dd/yyyy" /></td>
                                <td valign="top" class="name"><g:message code="priceAdjustment.preparedBy" default="Prepared By" /></td>
                                <td valign="top" class="value" width="500px">${fieldValue(bean: priceAdjustmentInstance, field: "preparedBy")}</td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="priceAdjustment.priceType" default="Price Type" /></td>
                                <td valign="top" class="value" width="300px">${priceAdjustmentInstance?.priceType?.encodeAsHTML()}</td>
                                <td valign="top" class="name"><g:message code="priceAdjustment.approvedBy" default="Approved By" /></td>
                                <td valign="top" class="value" width="500px">${fieldValue(bean: priceAdjustmentInstance, field: "approvedBy")}</td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="priceAdjustment.itemType" default="Item Type" /></td>
                                <td valign="top" class="value" width="300px">${fieldValue(bean: priceAdjustmentInstance, field: "itemType")}</td>
                                <td valign="top" class="name"><g:message code="priceAdjustment.cancelledBy" default="Cancelled By" /></td>
                                <td valign="top" class="value" width="500px">${fieldValue(bean: priceAdjustmentInstance, field: "cancelledBy")}</td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="priceAdjustment.remark" default="Remarks" /></td>
                                <td valign="top" class="value" width="300px">${fieldValue(bean: priceAdjustmentInstance, field: "remark")}</td>
                                <td valign="top" class="name"><g:message code="priceAdjustment.status" default="Status" /></td>
                                <td valign="top" class="value" width="500px">${fieldValue(bean: priceAdjustmentInstance, field: "status")}</td>
                            </tr>                            
                        </tbody>
                    </table>
                </div>
                <div class="subTable">
    				<table>
        				<thead>
          					<th width="5%">Identifier</th>
          					<th>Description</th>
          					<th width="7%">Net</th>
          					<th width="15%">
          					<g:if test="${priceAdjustmentInstance?.priceType.toString() == 'Retail'}">
          						Old Price(Retail)
          					</g:if>
          					<g:else>
          						Old Price(Wholesale)
          					</g:else>
          					</th>
          					<th width="13%">New Price</th>
          					<th width="13%">Margin</th>
        				</thead>
        				<tbody>
        					<g:each in="${priceAdjustmentInstance?.items}" status="i" var="item">
          						<tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}product/show/${item?.product?.id}'">
            						<td>${item?.product?.identifier}</td>
            						<td>${item?.product?.description}</td>
            						<td>${item?.product?.isNet.encodeAsHTML()}</td>
                                    <td class="right">PHP <g:formatNumber number="${item?.oldPrice}" format="###,##0.00" /></td>
            						<td class="right">PHP <g:formatNumber number="${item?.newPrice}" format="###,##0.00" /></td>
            						<td class="right"><g:formatNumber number="${item?.margin}" format="###,##0.00" /> %</td>
          						</tr>
        					</g:each>
        				</tbody>
    				</table>
				</div>
				<g:if test="${!priceAdjustmentInstance?.isCancelled()}">
                	<div class="buttons">
        				<g:form>
          					<g:hiddenField name="priceAdjustmentInstance.id" value="${priceAdjustmentInstance?.id}" />
          					
            				<g:if test="${priceAdjustmentInstance?.isUnapproved()}">
               					<span class="button"><g:link class="cancel" controller="priceAdjustment" action="cancel" id="${priceAdjustmentInstance?.id}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');">Cancel</g:link></span>
          						<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
              					<span class="button"><g:link class="approve" controller="priceAdjustment" action="approve" id="${priceAdjustmentInstance?.id}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');">Approve</g:link></span>
            				</g:if>
            				
            				<g:if test="${priceAdjustmentInstance?.isApproved()}">
              					<span class="button"><g:link class="unapprove" controller="priceAdjustment" action="unapprove" id="${priceAdjustmentInstance?.id}" onclick="return confirm('${message(code: 'default.button.approve.confirm.message', default: 'Are you sure?')}');">Unapprove</g:link></span>
            				</g:if>
            				
            				<g:if test="${priceAdjustmentInstance?.isApproved() || priceAdjustmentInstance?.isApplied()}">
            					<span class="button"><g:link class="print" controller="print" action="priceAdjustment" taget="priceAdjustment" id="${priceAdjustmentInstance?.id}" target="priceAdjustment">Print</g:link></span>
							</g:if>
          				</g:form>
	                </div>
                </g:if>
            </g:form>
        </div>
    </body>
</html>
