
<%@ page import="com.munix.JobOrder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'jobOrder.label', default: 'JobOrder')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
	<link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
    <g:javascript src="numbervalidation.js" />
</head>
<body >
  <div class="body">
    <h1><g:message code="default.balanceVsQuantity.label" default="Balance vs. Quantity"/></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.id.label" default="Id" /></td>
            <td valign="top" class="value">${jobOrderInstance}</td>
            <td valign="top" class="name"><g:message code="jobOrder.preparedBy.label" default="Prepared By" /></td>
            <td valign="top" class="value">${jobOrderInstance?.preparedBy?.encodeAsHTML()}</td>

          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.product.label" default="Identifier" /></td>
            <td valign="top" class="value"><g:link controller="product" action="show" id="${jobOrderInstance?.product?.id}" >${jobOrderInstance?.product?.encodeAsHTML()}</g:link></td>
            <td valign="top" class="name"></td>
            <td valign="top" class="value"></td>

          </tr>
        </tbody>
      </table>
    </div>
    <div class="subTable">
      <table>
        <h2>Requirements</h2>
        <thead>
          <tr>
            <th class="center">Identifier</th>
            <th class="center">Description</th>
            <th class="center">Units Required</th>
            <th class="center">Quantity</th>
	        <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="i">
	          <th class="right">${i?.identifier}</th>
	        </g:each>        
          </tr>
        </thead>
        <tbody>
        <g:each in="${jobOrderInstance?.requisition?.items?.sort{it?.component?.description}}" var="reqItem" status="idx">
          <tr class="${(idx % 2) == 0 ? 'odd' : 'even'}">
             <g:if test='${reqItem.getMaterialReleaseItems()}'>
				<g:each in="${reqItem.getMaterialReleaseItems()}" var="relItem" status="relItemIdx">  
				     <g:if test='${relItemIdx==0}'>
            			<td><a href="${createLink(uri:'/')}product/show/${reqItem?.component?.id}">${reqItem?.component?.identifier}</a></td>
            			<td>${reqItem?.component?.description}</td>
            			<td class="right">${reqItem?.unitsRequired}</td>
            			<td class="right">${reqItem?.computeQuantity()}</td>
				        <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="warehouse">
			            <td class="right">${reqItem?.component?.formatSOH(warehouse)}</td>
				        </g:each>        
            		</g:if>
            		<g:else>
            			</tr>
            			<tr class="${(idx % 2) == 0 ? 'odd' : 'even'}">
            				<td></td>
            				<td></td>
            				<td></td>
            				<td></td>
				        <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="warehouse">
			            <td class="right"></td>
				        </g:each>        
            		</g:else>
            	</g:each>            		
             </g:if>
             <g:else>
                <td><a href="${createLink(uri:'/')}product/show/${reqItem?.component?.id}">${reqItem?.component?.identifier}</a></td>
            	<td>${reqItem?.component?.description}</td>
            	<td class="right">${reqItem?.unitsRequired}</td>
            	<td class="right">${reqItem?.computeQuantity()}</td>
		        <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="warehouse">
	            <td class="right">${reqItem?.component?.formatSOH(warehouse)}</td>
		        </g:each>        
             </g:else>             
          </tr>
        </g:each>
		</tbody>
      </table>
      </div>

  </div>
</body>
</html>
