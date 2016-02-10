
<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Components')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>

	 <link rel="stylesheet" href="${resource(dir:'css',file:'scrollableTable.css')}" />
    <g:javascript src="generalmethods.js" />
    <g:javascript src="numbervalidation.js" />
    <g:javascript src="jquery.calculation.js" />
	<g:javascript src="table/jquery.dataTables.js" />
	<g:javascript src="table/jquery.dataTables.forceReload.js" />
	<g:javascript src="table/jquery.dataTables.filteringDelay.js" />
	<g:javascript src="editProductComponent.js" />

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
  <g:hasErrors bean="${productInstance}">
    <div class="errors">
      <g:renderErrors bean="${productInstance}" as="list" />
    </div>
  </g:hasErrors>
    <div class="dialog">
      <table>
        <tbody>
		<input type='hidden' name="product.id" id="productId" value="${productInstance.id}" />

          <tr class="prop">
            <td valign="top" class="name"><g:message code="product.id.label" default="ID" /></td>
            <td valign="top" class="value">${productInstance}</td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name"><g:message code="product.description.label" default="Description" /></td>
            <td valign="top" class="value">${productInstance?.description}</td>
        </tr>

        </tbody>
      </table>
    </div>

	<div class="list">
		<table id="searchProductComponentTable">
		<h2>Available Components</h2>
			<thead>
				<th>ProductId</th>
				<th class="center" width="200px">Identifier</th>
				<th class="center" width="700px">Description</th>
				<th>Units required per assembly</th>
				<th>Unit</th>
				<th>Id</th>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<g:form>
	
	<div class="subTable">
      <table id="componentsTable">
        <h2>Components</h2>
        <thead class="componentsTable">
          <tr>
            <th class="center">Identifier</th>
            <th class="center">Description</th>
            <th class="center">Units required per assembly</th>
            <th class="center">Unit</th>
            <th class="center">Remove</th>
          </tr>
        </thead>
        <tbody class="editable components" >
        <g:each in="${productInstance.components}" var="item" status="idx">
          <tr class="${(idx % 2) == 0 ? 'odd' : 'even'}">
           	<g:hiddenField class="componentId" name="componentList[${idx}].component.id" value="${item.component?.id}" />
           	<g:hiddenField class="productId" name="componentList[${idx}].prdouct.id" value="${productInstance.id}" />
            <td><a href="${createLink(uri:'/')}product/show/${item?.productId}">${item?.component?.identifier}</a></td>
            <td>${item?.component?.description}</td>
            <td class="right">
            	<input type="text" class="qty" name="componentList[${idx}].qty" id="componentList[${idx}].qty" value="${item?.qty}" />
            </td>
            <td>${item?.component?.unit}</td>
            <td class="center" id="cancelExisting">
				<img src="../images/cancel.png" class="remove">
				<g:hiddenField class="deleted" name="componentList[${idx}].isDeleted" value="${item.isDeleted}" />
			</td>
          </tr>
          
        </g:each>
        </tbody>
      </table>
    </div>

    <div class="buttons">
        <g:hiddenField name="id" value="${productInstance?.id}" />
          <span class="button"><g:actionSubmit class="update" action="updateProductComponent" value="${message(code: 'default.button.update.label', default: 'Update')}" onclick="return confirm('${message(code : 'default.prompt')}')" /></span>
    </div>
      </g:form>
  </div>

</body>
</html>
