<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Supplier Items')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>

	 <link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
    <g:javascript src="generalmethods.js" />
    <g:javascript src="numbervalidation.js" />
    <g:javascript src="jquery.calculation.js" />
	<g:javascript src="table/jquery.dataTables.js" />
	<g:javascript src="table/jquery.dataTables.forceReload.js" />
	<g:javascript src="table/jquery.dataTables.filteringDelay.js" />
	<g:javascript src="editSupplierItems.js" />

</head>
<body >
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
  <g:hasErrors bean="${supplierInstance}">
    <div class="errors">
      <g:renderErrors bean="${supplierInstance}" as="list" />
    </div>
  </g:hasErrors>
    <div class="dialog">
      <table>
        <tbody>
		%{--<input type='hidden' name="product.id" id="productId" value="${productInstance.id}" />--}%
        <g:each in="${com.munix.SupplierItem.Status}" var="status">
            <input type='hidden' name="status" id="itemStatus" class ="itemStatus" value="${status}" />
        </g:each>


          <tr class="prop">
            <td valign="top" class="name"><g:message code="supplier.identifier.label" default="Identifier" /></td>
            <td valign="top" class="value">${supplierInstance?.identifier}</td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="supplier.name.label" default="Name" /></td>
            <td valign="top" class="value">${supplierInstance?.name}</td>
        </tr>

        </tbody>
      </table>
    </div>

	<div class="list">
		<table id="productTable">
		<h2>Available Products</h2>
			<thead>
				<th>ProductId</th>
				<th class="center">Identifier</th>
				<th class="center">Description</th>
			</thead>
			<tbody>

			</tbody>
		</table>
	</div>
	<g:form>
	
	<div class="subTable">
      <table id="supplierItemTable">
        <h2>Supplier Items</h2>
        <thead class="componentsTable">
          <tr>
            <th class="center">Identifier</th>
            <th class="center">Supplier Code</th>
            <th class="center">Description</th>
            <th class="center">Status</th>
            <th class="center">Remove</th>
          </tr>
        </thead>
        <tbody class="editable components" >
        <g:each in="${supplierInstance.items}" var="item" status="idx">
          <tr class="${(idx % 2) == 0 ? 'odd' : 'even'}">
           	<g:hiddenField class="productId" name="supplierItemList[${idx}].prdouct.id" value="${item.product.id}" />
            <td>${item?.product?.identifier}</td>
            <td>
                <input type="text" class="supplierCode" name="supplierItemList[${idx}].productCode" id="supplierItemList[${idx}].productCode" value="${item?.productCode}" />
            </td>
            <td>${item?.product?.description}</td>
            <td>
                <g:select name="supplierItemList[${idx}].status" id="supplierItemList[${idx}].status" value="${item?.status.key}" optionKey="key" from="${com.munix.SupplierItem.Status}"/>
            </td>
            <td class="center" id="cancelExisting">
				<img src="../images/cancel.png" class="remove">
				<g:hiddenField class="deleted" name="supplierItemList[${idx}].isDeleted" value="${item.isDeleted}" />
			</td>
          </tr>

        </g:each>
        </tbody>
      </table>
    </div>

    <div class="buttons">
      <g:hiddenField name="id" value="${supplierInstance?.id}" />
      <span class="button"><g:actionSubmit class="update" action="updateSupplierItems" value="${message(code: 'default.button.update.label', default: 'Update')}" onclick="return confirm('${message(code : 'default.prompt')}')" /></span>
    </div>
      </g:form>
  </div>

</body>
</html>