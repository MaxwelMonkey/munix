<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <g:javascript src="jquery-1.4.1.min.js" />
  <g:javascript src="generalmethods.js" />
</head>
<body>
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
    <g:if test="${flash.error}">
      <div class="errors">${flash.error}</div>
    </g:if> 
    <g:hasErrors bean="${productComponentInstance}">
      <div class="errors">
        <g:renderErrors bean="${productComponentInstance}" as="list" />
      </div>
    </g:hasErrors>
    
    <div class="dialog">  
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="product.photo.label" default="Photo" /></td>
        <td valign="top" class="value">
          <g:link elementId="showPhoto" action="viewPhoto" id="${productInstance.id}"><img src="${createLink(action:'viewImage', id:productInstance.id)}" height="100" width="150" onclick="window.location='${createLink(uri:'/')}product/viewPhoto/${productInstance?.id}'" alt="${fieldValue(bean:productInstance, field:'identifier')}"/></g:link>
        </td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.identifier.label" default="Identifier" /></td>
        <td valign="top" class="value">${fieldValue(bean: productInstance, field: "identifier")}</td>
        <td valign="top" class="name"><g:message code="product.type.label" default="Discount Type" /></td>
        <td valign="top" class="value">${productInstance?.type?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.description.label" default="Description" /></td>
        <td valign="top" class="value">${productInstance?.description}</td>
        <td valign="top" class="name"><g:message code="product.unit.label" default="Unit" /></td>
        <td valign="top" class="value">${productInstance?.unit?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.itemType.label" default="Item Type" /></td>
          <td valign="top" class="value">${productInstance?.itemType?.description}</td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.category.label" default="Category" /></td>
        <td valign="top" class="value">${productInstance?.category?.encodeAsHTML()}</td>
        <td valign="top" class="name"><g:message code="product.material.label" default="Material" /></td>
        <td valign="top" class="value">${productInstance?.material?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.subcategory.label" default="Subcategory" /></td>
        <td valign="top" class="value">${productInstance?.subcategory?.encodeAsHTML()}</td>
        <td valign="top" class="name"><g:message code="product.size.label" default="Size" /></td>
        <td valign="top" class="value">${productInstance?.size?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.brand.label" default="Brand" /></td>
        <td valign="top" class="value">${productInstance?.brand?.encodeAsHTML()}</td>
        <td valign="top" class="name"><g:message code="product.addedDescription.label" default="Added Description" /></td>
        <td valign="top" class="value">${fieldValue(bean: productInstance, field: "addedDescription")}</td>
        </tr>
        
        <tr class="prop">
        	<td valign="top" class="name"><g:message code="product.productDetails.label" default="Package Details" /></td>
        	<td valign="top" class="value">${fieldValue(bean: productInstance, field: "packageDetails")}</td>
        	<td class="name"></td>
        	<td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="productInstance.warehouses.itemLocations.label" default="Item Locations" /></td>

        <td valign="top" style="text-align: left;" class="value">
          <ul>
            <g:each in="${warehouses}" var="warehouse">
              <g:if test="${warehouse?.selectedItemLocation}">
              	<li>${warehouse?.itemLocationWarehouse?.description} - ${warehouse?.selectedItemLocation}</li>
              </g:if>
            </g:each>
          </ul>
        </td>
            <td class="name"></td>
        	<td class="value"></td>
        
		</tr>
		
        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.model.label" default="Model" /></td>
        <td valign="top" class="value">${productInstance?.model?.encodeAsHTML()}</td>
        <td valign="top" class="name"><g:message code="product.color.label" default="Color" /></td>
        <td valign="top" class="value">${productInstance?.color?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.modelNumber.label" default="Model Number" /></td>
        <td valign="top" class="value">${fieldValue(bean: productInstance, field: "modelNumber")}</td>
        <td valign="top" class="name"><g:message code="product.partNumber.label" default="Part Number" /></td>
        <td valign="top" class="value">${fieldValue(bean: productInstance, field: "partNumber")}</td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <g:ifAnyGranted role = "ROLE_SALES,ROLE_PURCHASING">
        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.retail.label" default="Price (Retail)" /></td>
        <td valign="top" class="value">${productInstance?.formatRetailPrice()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
         <td valign="top" class="name"><g:message code="product.wholeSale.label" default="Price (Wholesale)" /></td>
        <td valign="top" class="value">${productInstance?.formatWholeSalePrice()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>
        </g:ifAnyGranted>


        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.reorderPoint.label" default="Reorder Point" /></td>
        <td valign="top" class="value">${productInstance?.formatReorderPoint()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.eoq.label" default="EOQ" /></td>
        <td valign="top" class="value">${productInstance?.formatEoq()}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.isNet.label" default="Use Net Price" /></td>
        <td valign="top" class="value"><g:formatBoolean boolean="${productInstance?.isNet}" /></td>
        <td valign="top" class="name"><g:message code="product.isForAssembly.label" default="For Assembly" /></td>
        <td valign="top" class="value"><g:formatBoolean boolean="${productInstance?.isForAssembly}" /></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.isNet.label" default="Use as Component" /></td>
        <td valign="top" class="value"><g:formatBoolean boolean="${productInstance?.isComponent}" /></td>
        <td valign="top" class="name"><g:message code="product.isForSale.label" default="For Sale" /></td>
        <td valign="top" class="value"><g:formatBoolean boolean="${productInstance?.isForSale}" /></td>
        </tr>

        <tr class="prop">
			<td valign="top" class="name"><g:message code="product.remark.label" default="Remarks" /></td>
			<td valign="top" class="value">${productInstance?.remark?.encodeAsHTML()}</td>
			<td class="name"></td>
			<td class="value"></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="product.status.label" default="Status" /></td>
        <td valign="top" class="value">${productInstance?.status}</td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="dialog">  
      <table>
        <tbody>
          <g:ifAnyGranted role = "ROLE_SUPER">
          	<tr class="prop">
          		<td valign="top" class="name"><g:message code="product.runningCost.label" default="Running Cost" /></td>
          		<td class="value">PHP <g:formatNumber number="${productInstance?.runningCost}" format="#,##0.0000"/></td>
        	</tr>
        	<tr class="prop">
          		<td valign="top" class="name"><g:message code="product.wholeSalePriceMargin.label" default="Wholesale Price Margin" /></td>
          		<td class="value">PHP <g:formatNumber number="${productInstance?.getWholeSalePriceMargin()}" format="#,##0.00"/></td>
        	</tr>
        	<tr class="prop">
          		<td valign="top" class="name"><g:message code="product.retailPriceMargin.label" default="Retail Price Margin" /></td>
          		<td class="value">PHP <g:formatNumber number="${productInstance?.getRetailPriceMargin()}" format="#,##0.00"/></td>
        	</tr>
          </g:ifAnyGranted>
        	<tr class="prop">
          		<td valign="top" class="name"><g:message code="product.totalStock.label" default="Total Stock" /></td>
          		<td class="value">${productInstance?.getTotalStock()}</td>
        	</tr>
          </tbody>
        </table>
	</div>

	<div class="dialog">
	  <h2>Selling Price</h2>  
      <table>
        <tbody>
          	<tr class="prop">
          		<td valign="top" class="name"><g:message code="product.wholeSaleSellingPriceMargin.label" default="Wholesale Selling Price Margin" /></td>
          		<td class="value">${productInstance?.formatWholeSaleSellingPriceMargin()}</td>
        	</tr>
        	<tr class="prop">
          		<td valign="top" class="name"><g:message code="product.retailSellingPriceMargin.label" default="Retail Selling Price Margin" /></td>
          		<td class="value">${productInstance?.formatRetailSellingPriceMargin()}</td>
        	</tr>
        	<tr class="prop">
          		<td valign="top" class="name"><g:message code="product.sellingPriceMargin.label" default="Multiplier" /></td>
          		<td class="value"><g:formatNumber number="${productInstance?.formatSellingPriceMargin()}" format="#,##0.00"/>%</td>
        	</tr>
          </tbody>
        </table>
	</div>

    <div class="subTable">
    	<h2>Stock Breakdown</h2>
       	<table>
        	<g:each in="${stocks}" var="stock">
              	<tr class="prop">
               		<td valign="top" class="name">${stock.warehouse}</td>
               		<td valign="top" class="value">${stock.qty}</td>
               	</tr>
            </g:each>
       	</table>
    </div>
            
    <g:if test="${productInstance?.isForAssembly}">
      <div class="subTable">
        <table>
	<h2>Components</h2>  
	<g:form>
	 <g:hiddenField name="id" value="${productInstance?.id}" />
	 <h2><span class="button"><g:actionSubmit class="addItems" id="addItems" controller="product" action="editProductComponent" value="Edit Components"/></span></h2>
	</g:form>
          <thead>
            <tr>
              <th>Identifier</th>
              <th>Description</th>
              <th class="right">Quantity</th>
              <th>Unit</th>
		    <g:ifAnyGranted role="ROLE_ADMIN_PURCHASING">
              <th class="right">Foreign Cost</th>
              <th class="right">Exchange Rate</th>
              <th class="right">Peso Cost</th>
            </g:ifAnyGranted>
            </tr>
          </thead>
          <tbody>
          <g:each in="${productInstance?.components.sort{it?.component?.description}}" var="i" status="colors">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onClick="window.location='${createLink(uri:'/')}stockCostHistory/show?id=${i?.component?.id}'">
              <td>${i?.component}</td>
              <td>${i?.component?.description}</td>
              <td class="right">${i?.qty}</td>
              <td>${i?.component?.unit}</td>
		    <g:ifAnyGranted role="ROLE_ADMIN_PURCHASING">
              <td class="right">${i?.component?.currency} <g:formatNumber number="${i?.component?.runningCostInForeignCurrency}" format="#,##0.0000"/></td>
              <td>${i?.component?.exchangeRate}</td>
              <td class="right">PHP <g:formatNumber number="${i?.component?.runningCost}" format="#,##0.0000"/></td>
            </g:ifAnyGranted>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
    </g:if>
    
      <div class="subTable">
        <table>
          <g:link elementId="addLaborCost" class="addItem" controller="laborCost" action="create" id="${productInstance?.id}">Add Labor Cost</g:link>
          <thead>
            <tr>
              <th>Type</th>
              <th>Amount</th>
            </tr>
          </thead>
          <tbody class="editable">
          <g:each in="${productInstance.laborCosts.sort{it.type}}" var="laborCostEntry" status="colors">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}laborCost/edit/${laborCostEntry?.id}'">
              <td>${laborCostEntry?.type}</td>
              <td>PHP <g:formatNumber number="${laborCostEntry?.amount}" format="#,##0.00" /></td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      
    <div class="buttons">
      <g:form>
        <g:hiddenField name="id" value="${productInstance?.id}" />
        <g:ifAnyGranted role="ROLE_PURCHASING">
       	  <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
       	  <span class="button"><g:actionSubmit class="viewTable" action="auditLogs" value="${message(code: 'default.button.auditLogs.label', default: 'View Audit Logs')}" /></span>
        </g:ifAnyGranted>
          <span class="button"><g:actionSubmit id="viewPriceAdjustmentHistory" class="viewTable" action="viewPriceAdjustmentHistory" value="${message(code: 'default.button.viewPriceAdjustmentHistory.label', default: 'View Price Adjustment History')}" /></span>
          <span class="button"><g:link class="viewTable" controller="stockCard" action="show" id="${productInstance?.stockCard?.id}">View Stock Card</g:link></span>
          <span class="button"><g:link class="viewTable" controller="stockCostHistory" action="show" id="${productInstance?.id}">View Stock Cost History</g:link></span>
        <g:ifAnyGranted role="ROLE_SALES,ROLE_ACCOUNTING">
          <span class="button"><g:actionSubmit id="viewSellingHistory" class="viewTable" action="viewSellingHistory" value="${message(code: 'default.button.viewSellingHistory.label', default: 'View Selling History')}" /></span>
        </g:ifAnyGranted>
      </g:form>
    </div>
  </div>
</body>
</html>
