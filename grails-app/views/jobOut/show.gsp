
<%@ page import="com.munix.JobOut" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'jobOut.label', default: 'jobOut')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
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
            <td valign="top" class="name"><g:message code="jobOut.id.label" default="ID" /></td>
            <td valign="top" class="value">${jobOutInstance}</td>  
            <td valign="top" class="name"><g:message code="jobOut.preparedBy.label" default="Prepared By" /></td>
            <td valign="top" class="value">${fieldValue(bean: jobOutInstance, field: "preparedBy")}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOut.jobOrder.label" default="Job Order" /></td>
            <td valign="top" class="value"><g:link controller="jobOrder" action="show" params="[jobOrderId: jobOutInstance?.jobOrder?.id]" id="${jobOutInstance?.jobOrder?.id}">${jobOutInstance?.jobOrder?.encodeAsHTML()}</g:link></td>
            <td valign="top" class="name"><g:message code="jobOut.approvedBy.label" default="Approved By" /></td>
            <td valign="top" class="value">${fieldValue(bean: jobOutInstance, field: "approvedBy")}</td>
          </tr>

          <tr class="prop">
            <td class="name"></td>
            <td class="value"></td>
            <td valign="top" class="name"><g:message code="jobOut.cancelledBy.label" default="Cancelled By" /></td>
            <td valign="top" class="value">${fieldValue(bean: jobOutInstance, field: "cancelledBy")}</td>
          </tr>

          <tr class="prop">
            <td class="name"></td>
            <td class="value"></td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOut.product.label" default="Identifier" /></td>
            <td valign="top" class="value">${jobOutInstance?.jobOrder?.product}</td>
           	<td valign="top" class="name"><g:message code="jobOut.status.label" default="Status" /></td>
            <td valign="top" class="value">${fieldValue(bean: jobOutInstance, field: "status")}</td>
          </tr>

		  <tr class="prop">
			<td valign="top" class="name"><g:message code="jobOut.description.label" default="Description" /></td>
            <td valign="top" class="value">${jobOutInstance?.jobOrder?.product?.description}</td>
            <td valign="top" class="name"></td>
			<td valign="top" class="value"></td>
		  </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOut.qty.label" default="Quantity" /></td>
            <td valign="top" class="value">${fieldValue(bean: jobOutInstance, field: "qty")}</td>
            <td class="name"></td>
            <td class="value"></td> 
          </tr>
  		  <tr class="prop">
  		    <td valign="top" class="name"><g:message code="jobOut.assignedTo.label" default="Assigned To" /></td>
            <td valign="top" class="value">${jobOutInstance?.jobOrder?.assignedTo}</td>
            <td class="name"></td>
            <td class="value"></td> 
  		  </tr>
          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOut.warehouse.label" default="Warehouse" /></td>
            <td valign="top" class="value">${fieldValue(bean: jobOutInstance, field: "warehouse")}</td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>
        </tbody>
      </table>
	  <g:ifAnyGranted role="ROLE_SUPER">
	  <table>
        <h2>Costing Summary</h2>	  
	    <tbody>	
		  <tr>
            <td  class="name"><g:message code="jobOut.componentsCost.label" default="Components Cost per Assembly" /></td>
            <td valign="top" class="right"><g:formatNumber number="${componentsCost}" format="#,##0.0000"/></td>
          </tr>

          <tr>
            <td class="name"><g:message code="jobOut.laborCost.label" default="Labor Cost per Assembly " />(${jobOutInstance?.laborCost?.type})</td>
            <td valign="top" class="right"><g:formatNumber number="${jobOutInstance?.laborCost?.amount}" format="#,##0.0000"/></td>
          </tr>

 		  <tr>
            <td class="name"><g:message code="jobOut.totalCost.label" default="Total Cost per Assembly" /></td>
            <td class="right"><g:formatNumber number="${totalCostPerAssembly}" format="#,##0.0000"/></td>
          </tr>
         </tbody>   
         <tfoot class="total"> 
          <tr>
            <td>Job Out Total Cost</td>
            <td class="right"><strong>PHP <g:formatNumber number="${jobOutTotalCost}" format="#,##0.0000" /></strong></td>
          </tr>
         </tfoot>
      </table>
     </g:ifAnyGranted>
    </div>
    <div class="buttons">
      <g:form>
		<g:hiddenField name="id" value="${jobOutInstance.id}"/>
          <g:ifAnyGranted role="ROLE_MANAGER_PRODUCTION,ROLE_PRODUCTION">
			<g:if test="${jobOutInstance.isUnapproved()}">
			  <span class="button"><g:actionSubmit class="cancel" action="cancel" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" /></span>
	          <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
	          <span class="button"><g:actionSubmit class="approve" value="Approve" action="approve" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
	        </g:if>
	        <g:if test="${jobOutInstance.isApproved()}">
	          <span class="button"><g:actionSubmit class="unapprove" value="Unapprove" action="unapprove" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
	        </g:if>
		  <span class="button"><g:link class="print" controller="print" action="jobOut" target="jobOut" value="${message(code: 'default.button.print.label', default: 'Print')}" params="[id:params.id]"> Print</g:link></span>
	    </g:ifAnyGranted>
      </g:form>
    </div>
  </div>
</body>
</html>
