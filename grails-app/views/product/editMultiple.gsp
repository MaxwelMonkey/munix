
<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.0/themes/smoothness/jquery-ui.css" />
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.0/jquery-ui.min.js"></script>
	<style>
		#productList, #fieldList{
			background-color: white;
			margin-bottom: 10px;
			padding: 10px;
			border: 1px solid lightcyan;
			border-radius: 10px;
		}
		
		#productList a, #fieldList a{
			font-size:80%;
		}
	</style>
	<script>
		
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
      <div class="dialog">
<div class="ui-widget">
<label for="term">Choose Products to Update: </label>
</div>
	<div id="productList">
      <input type="checkbox" class="checkAll" onclick="checkAll('product', this.checked)" id="productCheckAll"> <label for="productCheckAll">Check All</label>
    	<div class="multicheckbox">
    		<g:each in="${products}" var="product">
    		<div class="checkbox"><input class="productCheckbox" type="checkbox" name="product.id" id="product${product?.id}" value="${product?.id}"> <label for="product${product?.id}">${product}</label></div>
    		</g:each>
    	</div>
	</div>
<div class="ui-widget">
<label for="term">Choose the fields to update: </label>
</div>
	<div id="fieldList">
      <input type="checkbox" class="checkAll" onclick="checkAll('field', this.checked)" id="fieldCheckAll"> <label for="fieldCheckAll">Check All</label>
    	<div class="multicheckbox">
    		<g:each in="${['Discount Type', 'Item Type', 'Unit', 'Category', 'Subcategory', 'Brand', 'Model', 'Material', 'Color', 'Use as Component', 'For Assembly', 'For Sale', 'Status']}" var="field" index="i">
    		<div class="checkbox"><input class="fieldCheckbox" type="checkbox" name="field" id="field${i}" value="${field}"> <label for="field${i}">${field}</label></div>
    		</g:each>
    	</div>
	</div>
      <div class="buttons">
          <span class="button"><g:link controller="product" action="list">Cancel</g:link></span>
        <span class="button"><g:actionSubmit action="editMultiple2" value="${message(code: 'default.button.next.label', default: 'Next')}" /></span>
      </div>
    </g:form>
  </div>
</body>
</html>
