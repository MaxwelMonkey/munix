
<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.0/themes/smoothness/jquery-ui.css" />
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.0/jquery-ui.min.js"></script>
	<style>
		#productList{
			background-color: white;
			margin-bottom: 10px;
			padding: 10px;
			border: 1px solid lightcyan;
			border-radius: 10px;
		}
		
		#productList a{
			font-size:80%;
		}
	</style>
	<script>
		$(document).ready(function(){
			$("#term").autocomplete({
				source:function( request, response ) {
                $.ajax({
                    url: "${resource(dir:'product',file:'productList')}",
                    dataType: "json",
                    data: {term: request.term},
                    success: function(data) {	
                                response($.map(data, function(item) {
                                return {
                                    label: item.identifier,
                                    id: item.id,
                                    abbrev: item.identifier
                                    };
                            }));
                        }
                    });
                },
                minLength: 1,
                select: function( event, ui ) {
                	addProduct(ui.item.id, ui.item.label);
                	$(this).val(''); return false;
                }
			})
		});
		
		function addProduct(id, identifier){
			$("#productList").append("<input type='hidden' id='product"+id+"' name='productId' value='"+id+"'>");
			$("#productList").append("<span id='label"+id+"'>"+identifier+"</span>&nbsp;");
			$("#productList").append("<a id='remove"+id+"' class='removeLink' href='#' onclick='removeLink(this)' productId='"+id+"'>Remove</a>");
			$("#productList").append("<br id='br"+id+"'>");
		}
		
		function removeLink(elem){
			var id = $(elem).attr("productId");
			$("#br"+id).remove()
			$("#label"+id).remove()
			$("#remove"+id).remove()
			$("#product"+id).remove()
		}
	</script>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.editMultiple.label" args="[entityName]" default="Edit Multiple Products"/></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${productInstance}">
      <div class="errors">
        <g:renderErrors bean="${productInstance}" as="list" />
      </div>
    </g:hasErrors>
    <g:form method="post" >
    	<g:each in="${products}" var="${product}">
    	<g:hiddenField name="product" value="${product.id}"/>
    	</g:each>
    	<g:each in="${fields}" var="${field}">
    	<g:hiddenField name="field" value="${field}"/>
    	</g:each>
      <div class="dialog">
	<div id="productList">
		<h4>Products Selected:
        	<g:each in="${products}" var="product">
			<br/>${product.identifier} - 
			${product.formatDescription()}
			</g:each>
		</h4>
	</div>
        <table class="list">
        	<thead>
        		<tr>
        			<th>Fields</th>
        			<th></th>
	              <g:each in="${products}" var="product">
	              <th>${product}</th>
	              </g:each>
        		</tr>
        	</thead>
          <tbody>
          	<g:each in="${fields}" var="field">
          	<g:if test="${field == 'Discount Type'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="type"><g:message code="product.type.label" default="Discount Type" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'type', 'errors')}">
                <g:select name="type.id" from="${com.munix.DiscountType.list().sort{it.id}}" optionKey="id" value="${productInstance?.type?.id}" noSelection="['null': '']" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.type}
              </td>
              </g:each>
            </tr>
            </g:if>

          	<g:if test="${field == 'Item Type'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="itemType"><g:message code="product.itemType.label" default="Item Type" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'itemType', 'errors')}">
                <g:select name="itemType.id" from="${com.munix.ItemType.list().sort{it.id}}" optionKey="id" value="${productInstance?.itemType?.id}" noSelection="['null': '']" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.itemType}
              </td>
              </g:each>
            </tr>
            </g:if>
            
          	<g:if test="${field == 'Unit'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="unit"><g:message code="product.unit.label" default="Unit" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'unit', 'errors')}">
                <g:select name="unit.id" from="${com.munix.ProductUnit.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.unit?.id}" noSelection="['null': '']" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.unit}
              </td>
              </g:each>
            </tr>
            </g:if>

          	<g:if test="${field == 'Category'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="category"><g:message code="product.category.label" default="Category" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'category', 'errors')}">
                <g:select name="category.id" from="${com.munix.ProductCategory.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.category?.id}" noSelection="['null': '']" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.category}
              </td>
              </g:each>
            </tr>
            </g:if>

          	<g:if test="${field == 'Subcategory'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="subcategory"><g:message code="product.subcategory.label" default="Subcategory" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'subcategory', 'errors')}">
                <g:select name="subcategory.id" from="${com.munix.ProductSubcategory.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.subcategory?.id}" noSelection="['null': '']" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.subcategory}
              </td>
              </g:each>
            </tr>
            </g:if>

          	<g:if test="${field == 'Brand'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="brand"><g:message code="product.brand.label" default="Brand" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'brand', 'errors')}">
                <g:select name="brand.id" from="${com.munix.ProductBrand.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.brand?.id}" noSelection="['null': '']" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.brand}
              </td>
              </g:each>
            </tr>
            </g:if>

          	<g:if test="${field == 'Model'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="model"><g:message code="product.model.label" default="Model" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'model', 'errors')}">
                <g:select name="model.id" from="${com.munix.ProductModel.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.model?.id}" noSelection="['null': '']" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.model}
              </td>
              </g:each>
            </tr>
            </g:if>

          	<g:if test="${field == 'Material'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="material"><g:message code="product.material.label" default="Material" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'material', 'errors')}">
                <g:select name="material.id" from="${com.munix.ProductMaterial.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.material?.id}" noSelection="['null': '']" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.material}
              </td>
              </g:each>
            </tr>
            </g:if>

          	<g:if test="${field == 'Color'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="color"><g:message code="product.color.label" default="Color" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'color', 'errors')}">
                <g:select name="color.id" from="${com.munix.ProductColor.list().sort{it.toString()}}" optionKey="id" value="${productInstance?.color?.id}" noSelection="['null': '']" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.color}
              </td>
              </g:each>
            </tr>
            </g:if>

          	<g:if test="${field == 'Use as Component'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="isComponent"><g:message code="product.isComponent.label" default="Use as Component" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: product, field: 'isComponent', 'errors')}">
                <g:checkBox name="isComponent" value="${productInstance?.isComponent}" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.isComponent}
              </td>
              </g:each>
            </tr>
            </g:if>

          	<g:if test="${field == 'For Assembly'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="isForAssembly"><g:message code="product.isForAssembly.label" default="For Assembly" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: product, field: 'isForAssembly', 'errors')}">
                <g:checkBox name="isForAssembly" value="${productInstance?.isForAssembly}" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.isForAssembly}
              </td>
              </g:each>
            </tr>
            </g:if>

          	<g:if test="${field == 'For Sale'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="isForSale"><g:message code="product.isForSale.label" default="For Sale" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: product, field: 'isForSale', 'errors')}">
                <g:checkBox name="isForSale" value="${productInstance?.isForSale}" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.isForSale}
              </td>
              </g:each>
            </tr>
            </g:if>

          	<g:if test="${field == 'Status'}">
            <tr class="prop">
              <td valign="top" class="name">
                <label for="status"><g:message code="product.status.label" default="Status" /></label>
              </td>
              <td valign="top" class="value ${hasErrors(bean: productInstance, field: 'status', 'errors')}">
                <g:select name="status" from="${['Active','Inactive']}" value="${productInstance?.status}" />
              </td>
              <g:each in="${products}" var="product">
              <td>
              	${product.status}
              </td>
              </g:each>
            </tr>
            </g:if>
            

            </g:each>
		  </tbody>
	    </table>
      </div>
      <div class="buttons">
          <span class="button"><g:link controller="product" action="list">Cancel</g:link></span>
        <span class="button"><g:actionSubmit class="save" action="updateMultiple" value="${message(code: 'default.button.update.label', default: 'Update')}" onclick="return confirm('Are you sure you want to update all the products in this list?')"/></span>
      </div>
    </g:form>
  </div>
</body>
</html>
