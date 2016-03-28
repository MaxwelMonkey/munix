
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
		#fieldList{
			background-color: white;
			margin-bottom: 10px;
			padding: 10px;
			border: 1px solid lightcyan;
			border-radius: 10px;
		}
		
		#productList{
			padding-top: 10px;
			padding-bottom: 10px;
		}
		
		#productList a, #fieldList a{
			font-size:80%;
		}
		
		.field_col{
			display:none;
		}
	</style>
	<script>
		<g:set var="warehouses" value="${com.munix.Warehouse.list()}"/>
		var addedProducts = new Object();
		
		function addProduct(id, identifier, type, itemType, unit, category, subcategory, brand, model, material, color, isComponent, isForAssembly, isForSale, status<g:each in="${warehouses}" var="warehouse">, warehouse${warehouse.id}</g:each>){
			addedProducts[identifier] = 1;
			$("#productList table").append("<tr><td><input type='checkbox' class='checkbox' name='product.id' value='"+id+"'></td><td><span id='label"+id+"'>"+identifier+"</span></td><td class='field_col0 field_col'>"+type+"</td><td class='field_col1 field_col'>"+itemType+"</td><td class='field_col2 field_col'>"+unit+"</td><td class='field_col3 field_col'>"+category+"</td><td class='field_col4 field_col'>"+subcategory+"</td><td class='field_col5 field_col'>"+brand+"</td><td class='field_col6 field_col'>"+model+"</td><td class='field_col7 field_col'>"+material+"</td><td class='field_col8 field_col'>"+color+"</td><td class='field_col9 field_col'>"+isComponent+"</td><td class='field_col10 field_col'>"+isForAssembly+"</td><td class='field_col11 field_col'>"+isForSale+"</td><td class='field_col12 field_col'>"+status+"</td><g:each in="${warehouses}" var="warehouse" index="i"><td>"+warehouse${warehouse.id}+"</td></g:each></tr>");
			checkFields();
		}
		
		function checkAllProducts(checkbox){
			if(checkbox.checked)
				$("#productList table .checkbox").attr("checked","true");
			else
				$("#productList table .checkbox").removeAttr("checked");
		}
		
		function clearProductTable(){			
			$("#productList table tr td").each(function(){
				$(this).parent().remove();
			});
			$("#productList table").append("<tr id='loadingRow'><td colspan='99'>Loading, please wait..<img src='${resource(dir:'images',file:'spinner.gif')}' alt='loading' border='0' /></td></tr>");
		}
		
		function removeLink(elem){
			$(elem).parent().parent().remove();
		}
		
		$(document).ready(function(){
			$("#term2").autocomplete({
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
                                    type: item.type?item.type:"",
                                    itemType: item.itemType?item.itemType:"",
                                    unit: item.unit?item.unit:"",
                                    category: item.category?item.category:"",
                                    subcategory: item.subcategory?item.subcategory:"",
                                    brand: item.brand?item.brand:"",
                                    model: item.model?item.model:"",
                                    material: item.material?item.material:"",
                                    color: item.color?item.color:"",
                                    isComponent: item.isComponent,
                                    isForAssembly: item.isForAssembly,
                                    isForSale: item.isForSale,
                                    status: item.status
                                    };
                            }));
                        }
                    });
                },
                minLength: 1,
                select: function( event, ui ) {
                	addProduct(ui.item.id, ui.item.label, ui.item.type, ui.item.itemType, ui.item.unit, ui.item.category, ui.item.subcategory, ui.item.brand, ui.item.model, ui.item.material, ui.item.color, ui.item.isComponent, ui.item.isForAssembly, ui.item.isForSale, ui.item.status);
                	$(this).val(''); return false;
                }
			})
		});
		
		function getProductsByKeyword(){
			clearProductTable();
			$.ajax({
            	url: "${resource(dir:'product',file:'productListByKeyword')}",
                dataType: "json",
                data: {term: $("#term").val()},
                success: function(data) {	
                	$("#loadingRow").hide();
                        $.map(data, function(item) {
                        addItem({
                            label: item.identifier,
                            id: item.id,
                            type: item.type?item.type:"",
                            itemType: item.itemType?item.itemType:"",
                            unit: item.unit?item.unit:"",
                            category: item.category?item.category:"",
                            subcategory: item.subcategory?item.subcategory:"",
                            brand: item.brand?item.brand:"",
                            model: item.model?item.model:"",
                            material: item.material?item.material:"",
                            color: item.color?item.color:"",
                            isComponent: item.isComponent,
                            isForAssembly: item.isForAssembly,
                            isForSale: item.isForSale,
						    <g:each in="${warehouses}" var="warehouse">
						    warehouse${warehouse.id}: item["warehouse${warehouse.id}"],
						    </g:each>
                            status: item.status
                            });
                    });
                }
            });
		}
		
		function addItem(data){
			addProduct(data.id, data.label, data.type, data.itemType, data.unit, data.category, data.subcategory, data.brand, data.model, data.material, data.color, data.isComponent, data.isForAssembly, data.isForSale, data.status<g:each in="${warehouses}" var="warehouse">, data.warehouse${warehouse.id}</g:each>);	
		}
		
		function checkFields(){
			$(".fieldCheckbox").each(function(){
				var checkbox = this
				var index = $(checkbox).attr("ind");
				if(checkbox.checked){
					$(".field_col"+index).show();
				}else{
					$(".field_col"+index).hide();
				}
			});
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
<label for="term"><strong>Step 1: Choose the fields to update: </strong></label>
</div>
	<div id="fieldList">
      <input type="checkbox" class="checkAll" onclick="checkAll('field', this.checked); checkFields();" id="fieldCheckAll"> <label for="fieldCheckAll">Check All</label>
    	<div class="multicheckbox">
    		<g:each in="${['Discount Type', 'Item Type', 'Unit', 'Category', 'Subcategory', 'Brand', 'Model', 'Material', 'Color', 'Use as Component', 'For Assembly', 'For Sale', 'Status']}" var="field" status="i">
    		<div class="checkbox"><input class="fieldCheckbox" type="checkbox" class="fieldCheckbox" name="field" id="field${i}" value="${field}" ind="${i}" onclick="checkFields()"> <label for="field${i}">${field}</label></div>
    		</g:each>
    	</div>
	</div>
  <div class="ui-widget">
<label for="term"><strong>Step 2:  Type the Keyword to search for products: </strong></label> <input type="text" id="term" name="term"> <input class="button" type="button" value="Search" onclick="getProductsByKeyword();">
</div>
	<div id="productList">
    		<table class="list">
    			<tr>
    				<th style="width:1%"><input type="checkbox" onclick="checkAllProducts(this);"></th><th>Identifier</th>
    				<th class="field_col0 field_col">Discount Type</th>
    				<th class="field_col1 field_col">Item Type</th>
    				<th class="field_col2 field_col">Unit</th>
    				<th class="field_col3 field_col">Category</th>
    				<th class="field_col4 field_col">Subcategory</th>
    				<th class="field_col5 field_col">Brand</th>
    				<th class="field_col6 field_col">Model</th>
    				<th class="field_col7 field_col">Material</th>
    				<th class="field_col8 field_col">Color</th>
    				<th class="field_col9 field_col">Component</th>
    				<th class="field_col10 field_col">For Assembly</th>
    				<th class="field_col11 field_col">For Sale</th>
    				<th class="field_col12 field_col">Status</th>
					<g:each in="${warehouses}" var="warehouse" index="i">
    				<th>${warehouse.identifier}</th>
					</g:each>
    			</tr>

    		</table>
	</div>
      <div class="buttons">
          <span class="button"><g:link controller="product" action="list">Cancel</g:link></span>
        <span class="button"><g:actionSubmit action="editMultiple2" value="${message(code: 'default.button.next.label', default: 'Next')}" /></span>
      </div>
    </g:form>
  </div>

</body>
</html>
