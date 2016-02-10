
<%@ page import="com.munix.JobOrder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'jobOrder.label', default: 'JobOrder')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
	 <link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
    <g:javascript src="generalmethods.js" />
    <g:javascript src="numbervalidation.js" />
    <g:javascript src="jquery.calculation.js" />
	<g:javascript src="table/jquery.dataTables.js" />
	<g:javascript src="table/jquery.dataTables.forceReload.js" />
	<g:javascript src="table/jquery.dataTables.filteringDelay.js" />

	<script>
    var columnsSettings = new Array()
    columnsSettings.push({"bVisible": false}, null, {"bVisible": false}, {"bVisible": false}, null<g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="warehouse">, null</g:each>);
    </script>
	<g:javascript src="jobOrderMaterialsTemplate.js" />

</head>
<body >
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:if test="${flash.errors}">
      <div class="errors">${flash.errors}</div>
    </g:if>
  <g:hasErrors bean="${jobOrderInstance}">
    <div class="errors">
      <g:renderErrors bean="${jobOrderInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:hasErrors bean="${jobOrderInstance?.requisition?.items}">
    <div class="errors">
      <g:renderErrors bean="${jobOrderInstance?.requisition?.items}" as="list" />
    </div>
  </g:hasErrors>
    <div class="dialog">
      <table>
        <tbody>
		<input type='hidden' name="product.id" id="productId" value="${jobOrderInstance.product.id}" />
		<input type='hidden' name="jobOrderQty" id="jobOrderQty" value="${jobOrderInstance.qty}" />

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.id.label" default="Id" /></td>
            <td valign="top" class="value">${jobOrderInstance}</td>
            <td valign="top" class="name"><g:message code="jobOrder.preparedBy.label" default="Prepared By" /></td>
            <td valign="top" class="value">${jobOrderInstance?.preparedBy?.encodeAsHTML()}</td>
        </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.requisition.label" default="Requisition" /></td>
            <td valign="top" class="value"><g:link controller="materialRequisition" action="show" id="${jobOrderInstance?.requisition?.id}">${jobOrderInstance?.requisition?.encodeAsHTML()}</g:link></td>
            <td valign="top" class="name"><g:message code="jobOrder.approvedBy.label" default="Job Order Approved By" /></td>
            <td valign="top" class="value">${jobOrderInstance?.approvedBy?.encodeAsHTML()}</td>

          </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="jobOrder.product.label" default="Identifier" /></td>
          <td valign="top" class="value">${jobOrderInstance?.product?.encodeAsHTML()}</td>
          <td valign="top" class="name"><g:message code="jobOrder.materialReleasesApproveBy.label" default="Material Releases Approved By" /></td>
          <td valign="top" class="value">${jobOrderInstance?.approvedBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.description.label" default="Description" /></td>
            <td valign="top" class="value">${jobOrderInstance?.product?.description}</td>
            <td valign="top" class="name"><g:message code="jobOrder.markAsCompletedBy.label" default="Mark as Completed By" /></td>
            <td valign="top" class="value">${jobOrderInstance?.markAsCompleteBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.qty.label" default="Quantity" /></td>
            <td valign="top" class="value">${jobOrderInstance?.formatQty()}</td>
            <td valign="top" class="name"><g:message code="jobOrder.assignedTo.label" default="Assigned To" /></td>
            <td valign="top" class="value">${jobOrderInstance?.assignedTo?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.startDate.label" default="Start Date" /></td>
            <td valign="top" class="value"><g:formatDate date="${jobOrderInstance?.startDate}" format="MM/dd/yyyy hh:mm aaa" /></td>
            <td valign="top" class="name"><g:message code="jobOrder.status.label" default="Status" /></td>
            <td valign="top" class="value">${fieldValue(bean: jobOrderInstance, field: "status")}</td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.targetDate.label" default="Target Date" /></td>
            <td valign="top" class="value"><g:formatDate date="${jobOrderInstance?.targetDate}" format="MM/dd/yyyy hh:mm aaa" /></td>
            <td class="name"></td>
            <td class="value"></td>
        </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.endDate.label" default="End Date" /></td>
            <td valign="top" class="value"><g:formatDate date="${jobOrderInstance?.endDate}" format="MM/dd/yyyy hh:mm aaa" /></td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>
          <tr class="prop">
            <td valign="top" class="name"><g:message code="jobOrder.remark.label" default="Remarks" /></td>
            <td valign="top" class="value">${fieldValue(bean: jobOrderInstance, field: "remark")}</td>
            <td class="name"></td>
            <td class="value"></td>
          </tr>

        </tbody>
      </table>
    </div>

	<div class="list">
		<table id="searchJobOrderMaterialsTable">
		<h1>Replacements</h1>
			<thead>
				<th>Product Id</th>
				<th width="20%">Identifier</th>
				<th>Quantity per Assembly</th>
				<th>Quantity Required</th>
				<th width="50%">Description</th>
		        <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="i">
		          <th class="right">${i?.identifier}</th>
		        </g:each>        
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<g:form>

    <div class="subTable">
      <table id="componentsTable">
        <h2>Requirements</h2>
        <thead class="componentsTable">
          <tr>
            <th class="center">Identifier</th>
            <th class="center">Description</th>
            <th class="center" style="width:100px">Units required per assembly</th>
            <th class="center">Total units required</th>
            <th class="center">Remove</th>
          </tr>
        </thead>
        <tbody class="editable components" >
        <g:each in="${components}" var="item" status="idx">
          <tr class="${(idx % 2) == 0 ? 'odd' : 'even'}">
          	<g:hiddenField class="productId" name="materialList[${item.index}].component.id" value="${item.productId}" />
			<g:hiddenField class="totalUnits" name="totalUnits" value="${item?.totalUnits}"/>
			<g:hiddenField class="defaultUnitsRequired" name="defaultUnitsRequired" value="${item?.unitsRequired}" />
			<g:hiddenField class="defaultTotalUnitsRequired" name="defaultTotalUnitsRequired" value="${item?.totalUnitsRequired}" />
            <td><a href="${createLink(uri:'/')}product/show/${item?.productId}">${item?.identifier}</a></td>
            <td>${item?.description}</td>
            <td class="value ${hasErrors(bean: jobOrderInstance.requisition.items[idx], field: 'unitsRequired', 'errors')}">
            	<input type="text" class="unitsRequired" name="materialList[${item.index}].unitsRequired" id="materialList[${item.index}].unitsRequired" value="${item?.unitsRequired}" />
            </td>
            <td class="right amount">
            	${item?.totalUnitsRequired}
            </td>
            <td class="right" id="cancelExisting">
				<img src="../images/cancel.png" class="remove">
				<g:hiddenField class="deleted" name="materialList[${item.index}].isDeleted" value="${item.isDeleted}" />
			</td>
          </tr>
          
        </g:each>
        </tbody>
      </table>
    </div>

    <div class="buttons">
        <g:hiddenField name="id" value="${jobOrderInstance?.id}" />
        <g:if test="${jobOrderInstance?.isUnapproved()}">
          <span class="button"><g:actionSubmit class="update" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" onclick="return confirm('${message(code : 'default.prompt')}')" /></span>
        </g:if>
    </div>
      </g:form>
  </div>

</body>
</html>
