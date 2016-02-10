<%@ page import="com.munix.PurchaseOrder" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript src="generalmethods.js" />
    <g:javascript src="numbervalidation.js" />
    <g:javascript src="jquery.calculation.js" />
    <g:javascript src="table/jquery.dataTables.js" />
	<g:javascript src="table/jquery.dataTables.forceReload.js" />
	<g:javascript src="table/jquery.dataTables.filteringDelay.js" />
	<script>
    var columnsSettings = new Array()
    columnsSettings.push({"bVisible": false}, null, null, null, null, {"bVisible": false}, null, {"bVisible": false}<g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="warehouse">, null</g:each>)
    
    var currency = "${purchaseOrderInstance?.supplier?.currency?.identifier}";
    </script>
    <g:javascript src="purchaseOrderTemplate.js" />
    
    <g:set var="entityName" value="${message(code: 'purchaseOrder.label', default: 'PurchaseOrder')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
	<link rel="stylesheet" href="${resource(dir:'js/table/css',file:'demo_table.css')}" />
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if>
    <g:hasErrors bean="${purchaseOrderInstance}">
      <div class="errors">
        <g:renderErrors bean="${purchaseOrderInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form action="save" method="post" onSubmit="getCounter()" >
      <div class="dialog">
        <table>
          <tbody>
            <tr class="prop">
              <td valign="top" class="name">
                <label for="supplier"><g:message code="purchaseOrder.supplier.label" default="Supplier" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: purchaseOrderInstance, field: 'supplier', 'errors')}">
                <g:set var="suppliersSortedByIdentifier" value="${com.munix.Supplier.list().sort{it.identifier.toLowerCase()}}" />
          		<g:select class="supSelect" noSelection="${['':'Select One...']}" name="supplier.id" id="supplierId" from="${suppliersSortedByIdentifier}" optionKey="id" optionValue="identifier" value="${purchaseOrderInstance?.supplier?.id}"/>
          		<g:set var="suppliersSortedByName" value="${com.munix.Supplier.list().sort{it.name.toLowerCase()}}" />
          		<g:select class="supSelectName" noSelection="${['':'Select One...']}" name="supplierName" id="supplierName" from="${suppliersSortedByName}" optionKey="id" optionValue="name" />
          	  </td>
          	</tr>
          	</tbody>
         </table>
         <table>
         	<tbody>
         	<tr class="prop">
              <td valign="top" class="name">
                <label for="remark"><g:message code="purchaseOrder.remark.label" default="Remarks" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: purchaseOrderInstance, field: 'remark', 'errors')}">
                <g:textArea name="remark" value="${purchaseOrderInstance?.remark}" maxlength="255"/>
              </td>
            </tr>
          	<tr class="prop">
            	<td valign="top" class="name"><g:message code="purchaseOrder.discountRate.label" default="Discount Rate" /></td>
            	<td valign="top" class="value"><g:textField name="discountRate" id="discountRate" value="${purchaseOrderInstance?.discountRate}" />%</td>
	       	</tr>
          	<tr class="prop">
            	<td valign="top" class="name"><g:message code="purchaseOrder.shipBy.label" default="Ship By" /></td>
            	<td valign="top" class="value">
			          <g:datePicker name="shipBy" precision="day" value="${purchaseOrderInstance?.shipBy}" />
				</td>
	       	</tr>
          </tbody>
        </table>
         
   	<div class="list">
		<table id="searchPurchaseOrderItemsTable">
      	  <thead>
      	  	<th>ID</th>
      	  	<th width="200px">Name</th>
      	  	<th>Part Number</th>
      	  	<th width="200px">Supplier Code</th>
      	  	<th width="400px">Description</th>
      	  	<th width="200px">Running Cost</th>
      	  	<th width="200px">Running Cost</th>
      	  	<th width="200px">Supplier Currency</th>
          <g:each in="${com.munix.Warehouse.list().sort{it.identifier}}" var="warehouse">
          	<th>${warehouse}</th>
          </g:each>
      	  </thead>
      </table>
   </div>
   <br>
   <div class="subTable">
   	  <table id="componentsTable">
      	<thead class="componentsTable">
          <tr>
          	<th>Cancel</th>
            <th>Identifier</th>
            <th>Part Number</th>
            <th>Supplier Code</th>
            <th>Description</th>
            <th class="right">Price</th>
            <th class="right">Final Price</th>
            <th class="right">Quantity</th>
            <th class="right">Amount</th>
          </tr>
        </thead>
        <tbody class="editable components" >
         	<g:each in="${items}" var="item" status="idx">
          		<tr class="${(idx % 2) == 0 ? 'odd' : 'even'}">
          		<g:hiddenField class="productId" name="itemList[${item.index}].product.id" value="${item.productId}" />
				<td class="right" id="cancelExisting">
					<img src="../images/cancel.png" class="remove">
					<g:hiddenField class="deleted" name="itemList[${item.index}].isDeleted" value="${item.isDeleted}" />
				</td>
            	<td><a href="${createLink(uri:'/')}product/show/${item?.productId}">${item?.identifier}</a></td>
            	<td>${item?.partNumber}</td>
            	<td>${item?.productCode}</td>
            	<td>${item?.description}</td>
            	<td>${item?.price}</td>
            	<td class="right">
            		<input type="text" class="finalPrice" maxlength="7" name="itemList[${item.index}].finalPrice" id="itemList[${item.index}].finalPrice" value="${item?.finalPrice}" />
            	</td>
            	<td class="right">
            		<input type="text" class="qty" maxlength="7" name="itemList[${item.index}].qty" id="itemList[${item.index}].qty" value="${item?.qty}" />
            	</td>
				<td class="right amount">
            		${item?.amount}
            	</td>
          		</tr>
        	</g:each>
        </tbody>
        <tfoot class="total">
          <tr>
            <td> <strong>Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
			<td></td>
            <td class="right"><span id="amountTotalText">0.0000</span></td>
          </tr>
          <tr>
            <td> <strong>Discount</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
			<td></td>
            <td class="right"><span id="discount">0.00%</span></td>
          </tr>
          <tr>
            <td> <strong>Grand Total</strong></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
			<td></td>
            <td class="right"><span id="grandTotal">0.0000</span></td>
          </tr>
        </tfoot>
      </table>
    </div>
    <g:hiddenField name="amountTotal" />
      <div class="buttons">
        <g:form>
          <g:hiddenField name="id" value="${purchaseOrderInstance?.id}" />
			<g:ifAnyGranted role="ROLE_MANAGER_PURCHASING,ROLE_PURCHASING">
              	<span class="button"><g:actionSubmit class="markAsComplete" action="save" value="Create" onclick="isDirty=false;return confirm('${message(code: 'default.button.markAsComplete.confirm.message', default: 'Are you sure?')}');" /></span>
           	</g:ifAnyGranted>
        </g:form>
    </g:form>
  </div>
</body>
</html>
