
<%@ page import="com.munix.MaterialRelease" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'materialRelease.label', default: 'MaterialRelease')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript src="jquery-1.4.1.min.js" />
  <g:javascript src="generalmethods.js" />
  <g:javascript>
      function computeBalance(idx,dbRemainingBalance){
      		var releaseVal = $('#releaseValue'+idx).val()
      		$('#releaseValue'+idx).ForceNumericOnly(true)
      		
      		if(dbRemainingBalance < releaseVal) {
  		        $('#releaseValue'+idx).val(dbRemainingBalance)
  		        $('#releaseValue'+idx).focus()
				alert('The entered quantity is above the remaining balance')  		        
      		} 
      		else {
      			var computedVal = dbRemainingBalance - releaseVal
  		        $('#computedBalance'+idx).text(addCommas(computedVal))
			}
		}
		function checkEmptyComputeBalance(idx,dbRemainingBalance) {
			var releaseVal = $('#releaseValue'+idx).val()
			if(releaseVal == ''){
          		$('#releaseValue'+idx).val('0')
		    }
		    else {
		    computeBalance(idx,dbRemainingBalance)
		    }
		}
		
  </g:javascript>
</head>
<body>
<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
</div>
<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]" /></h1>
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
  
  <g:form action="save" method="post" >
   <g:hiddenField name="materialReleaseInstance" id='materialReleaseInstance' value="${materialReleaseInstance}"/>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="jobOrder"><g:message code="materialRelease.jobOrder.label" default="Job Order" /></label>
            </td>
            <td valign="top" class="value">
            	<g:link controller="jobOrder" action="show" params="[jobOrderId: jobOrder?.id]">${jobOrder}</g:link>
        	</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="warehouse"><g:message code="materialRelease.warehouse.label" default="Warehouse" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: materialReleaseInstance, field: 'warehouse', 'errors')}">
        <g:select name="warehouse.id" class="warehouse" from="${com.munix.Warehouse.list().sort{it.toString()}}" optionValue="description" optionKey="id" value="${materialReleaseInstance?.warehouse?.id}"  />
        </td>
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
        <g:each in="${materialRequisitionItems}" var="i" status="idx">
          <tr <g:if test="${i?.remainingBalance==0}"> class="removed" style="display:none"</g:if>>

            <td>${i?.item?.component}</td>
            <td>${i?.item?.component?.description}</td>
            <td class="right">            
	            <span id="computedBalance${idx}">0.00</span>
            </td>            
            <td class="right"><input onchange="computeBalance(${idx},${i?.remainingBalance})" onclick="computeBalance(${idx},${i?.remainingBalance})" onblur="checkEmptyComputeBalance(${idx},${i?.remainingBalance})" onkeyup="computeBalance(${idx},${i?.remainingBalance})" onkeypress="computeBalance(${idx},${i?.remainingBalance})" class="right" type="text" name="releaseItemList[${idx}].qty" id="releaseValue${idx}" value="${i?.remainingBalance}"/></td>
            	<g:hiddenField name="releaseItemList[${idx}].materialRequisitionItem.id" id="releaseItemList${idx}" value="${i?.item?.id}"/>
            	<g:hiddenField name="releaseItemList[${idx}].cost" id="releaseItemList${idx}" value="${i?.item?.component?.runningCost}"/>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <div class="buttons">
      		<input type="hidden" id="id" name="id" value="${params?.id}"/>
      <span class="button"><g:submitButton name="create" class="save" onclick="return confirm('Are You Sure?')" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
