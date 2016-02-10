
<%@ page import="com.munix.MaterialRelease" %>
<html>

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'materialRelease.label', default: 'MaterialRelease')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
  <g:javascript src="generalmethods.js" />
  <g:javascript>
      function computeBalance(idx,releaseQty,dbBalance){
      		var releaseVal = $('#releaseValue'+idx).val()
      		dbRemainingBalance = dbBalance + releaseQty
      		$('#releaseValue'+idx).ForceNumericOnly(true)
      		
      		if(dbRemainingBalance < releaseVal) {
  		        $('#releaseValue'+idx).val(releaseQty)
  		        $('#computedBalance'+idx).text(addCommas(dbBalance))
  		        $('#releaseValue'+idx).focus()
  		        alert('The entered quantity is above the remaining balance')
      		} 
      		else {
      			var computedVal = dbRemainingBalance - releaseVal
  		        $('#computedBalance'+idx).text(addCommas(computedVal))
			}
		}
		function checkEmptyComputeBalance(idx,releaseQty,dbRemainingBalance) {
			var releaseVal = $('#releaseValue'+idx).val()
			if(releaseVal == ''){
          		$('#releaseValue'+idx).val('0')
		    }
		    else {
		    computeBalance(idx,releaseQty,dbRemainingBalance)
		    }
		}
  </g:javascript>  
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if> 
    <g:hasErrors bean="${materialReleaseInstance}">
      <div class="errors">
        <g:renderErrors bean="${materialReleaseInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
      <g:hiddenField name="id" value="${materialReleaseInstance?.id}" />
      <g:hiddenField name="version" value="${materialReleaseInstance?.version}" />
      <div class="dialog">
        <table>
          <tbody>
          
        	<tr class="prop">
          	  <td valign="top" class="name"><g:message code="materialRelease.id.label" default="ID" /></td>
              <td valign="top" class="value">${materialReleaseInstance}</td>        	
	          <td valign="top" class="name"><g:message code="materialRelease.preparedBy.label" default="Prepared By" /></td>
    	      <td valign="top" class="value">${fieldValue(bean: materialReleaseInstance, field: "preparedBy")}</td>
        	</tr>

        	<tr class="prop">
        	  <td valign="top" class="name"><g:message code="materialRelease.jobOrder.label" default="Job Order" /></td>
			  <td valign="top" class="value">
            	<g:link controller="jobOrder" action="show" params="[jobOrderId: materialReleaseInstance?.jobOrder?.id]">${fieldValue(bean: materialReleaseInstance, field: "jobOrder")}</g:link>
        	  </td>
          	  <td valign="top" class="name"><g:message code="materialRelease.status.label" default="Status" /></td>
          	  <td valign="top" class="value">${fieldValue(bean: materialReleaseInstance, field: "status")}</td>
        	</tr>
        	
        	<tr class="prop">
              <td valign="top" class="name"><g:message code="materialRelease.warehouse.label" default="Warehouse" /></td>
              <td valign="top" class="value ${hasErrors(bean: materialReleaseInstance, field: 'warehouse', 'errors')}">
          		<g:select name="warehouse.id" from="${com.munix.Warehouse.list().sort{it.toString()}}" optionKey="id" value="${materialReleaseInstance?.warehouse?.id}"  />
          	  </td>
              <td class="name"></td>
              <td class="value"></td>          	  
        	</tr>
        	
          </tbody>
        </table>
      </div>
      
    <div class="subTable release">
      <table>
        <thead>
          <tr>
            <th class="center">Identifier</th>
            <th class="center">Description</th>
            <th class="center">Remaining Balance</th>
            <th class="center">Release</th>
          </tr>
        </thead>
        <tbody>
        <g:each in="${materialReleaseInstance?.items}" var="i" status="idx">
        <tr <g:if test="${i?.qty==0 && remainingBalanceList[idx]==0}"> class="removed" style="display:none"</g:if>>
        
            <td>${i?.materialRequisitionItem?.component}</td>
            <td>${i?.materialRequisitionItem?.component.description}</td>
            <td class="right">            
	            <span id="computedBalance${idx}"><g:formatNumber number="${remainingBalanceList[idx]}" format="#,##0.00" /></span>
            </td>
            <td class="right"><input onchange="computeBalance(${idx},${i?.qty},${remainingBalanceList[idx]})" onclick="computeBalance(${idx},${i?.qty},${remainingBalanceList[idx]})" onblur="checkEmptyComputeBalance(${idx},${i?.qty},${remainingBalanceList[idx]})" onkeyup="computeBalance(${idx},${i?.qty},${remainingBalanceList[idx]})" onkeypress="computeBalance(${idx},${i?.qty},${remainingBalanceList[idx]})" class="right" type="text" name="releaseItemList[${idx}].qty" id="releaseValue${idx}" value="${i?.qty}"/></td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
      
      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
