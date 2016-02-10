<%@ page import="com.munix.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  <style type="text/css" media="print,screen">
	@media print {
	   thead {display: table-header-group;}
	}
	
	div.long{
		padding:0px;
		margin:0px;
		overflow:auto;
		overflow-x:hidden;
	}
	
	.col0 div.long{
		width:100px;
	}
	
	.col1 div.long{
		width:150px;
	}
	
	div.long div{
		margin:0px;
		padding:0px;
		width:2000px;
	}
	
	.right{
		text-align:right;
	}
	
	div.scrollableHeader{
		overflow-y:hidden;
		overflow-x:none;
		padding:0px;
		margin:0px;
		height:50px;
	}
	div.scrollableHeader table{
		width:1303px;
	}

	div.scrollable{
		height:400px;
		overflow:auto;
		overflow-y:scroll;
		padding-top:0px;
		margin-top:0px;
	}

	.col0{
		width:9%;
	}	
	.col1{
		width:13%;
	}	
	.col2{
		width:10%;
	}	
	.col3{
		width:4%;
	}	
	.col4{
		width:5%;
	}	
	.col5{
		width:7%;
	}	
	.col6{
		width:5%;
	}	
	.col7{
		width:7%;
	}	
	.col8{
		width:7%;
	}	
	
	.col9{
		width:7%;
	}	
	.col10{
		width:7%;
	}	
	.col11{
		width:6%;
	}	
	.col12{
		width:7%;
	}	
	.col13{
		width:6%;
	}	
  </style>
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
    <script>
    $(document).ready(function(){
    	window.location.href="#scroller";
    });
    </script>
</head>
<body>
	<div class="body">
	<a href="#" onclick="$('#filters').show(); $(this).hide(); $('#hideFilters').show();" id="showFilters">Show Filters</a>
	<a href="#" onclick="$('#filters').hide(); $(this).hide(); $('#showFilters').show();" id="hideFilters">Hide Filters</a>
    <div class="dialog" id="filters">
      <g:form name="searchForm" action="productCostVsSelling" method="post" onsubmit="return filterChecker()">
      <h2>Filters</h2>
      
        <input type="hidden" name="reportType" value="${productCostVsSelling}">
        <table>
          <tr class="prop">
            <td class="name">Wholesale/Retail</td>
            <td class="value">
	          <select name="priceType">
	          	<option value="Wholesale" selected>Wholesale</option>
	          	<option value="Retail">Retail</option>
	          </select>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Status</td>
            <td class="value">
	          <select name="status">
	          	<option value="">All</option>
	          	<option value="Active">Active</option>
	          	<option value="Inactive">Inactive</option>
	          </select>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Currency</td>
            <td class="value">
	          <select name="currency">
	          	<option value="">All</option>
	          	<g:each in="${CurrencyType.list()}" var="currency">
	          	<option value="${currency.identifier}">${currency.identifier + '-' + currency.description}</option>
	          	</g:each>
	          </select>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Component</td>
            <td class="value">
            	<input type="checkbox" name="component" value="Y"/>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Net Price</td>
            <td class="value">
            	<input type="checkbox" name="isNet" value="Y"/>
            </td>
          </tr>
          <g:set var="productList" value="${com.munix.Product.findAll()}"/>
          <tr class="prop">
            <td class="name">For Sale only</td>
            <td class="value">
            	<input type="checkbox" name="forSale" value="Y"/>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Is For Assembly</td>
            <td class="value">
            	<input type="checkbox" name="forAssembly" value="Y"/>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Balance</td>
            <td class="value">
            	<g:select name="balance" id="balance" from="${['<0','>0','=0'] }" noSelection="${['null':'']}"/>
            </td>
          </tr>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Filter from Warehouse','name':'warehouse','field':'warehouse','list':com.munix.Warehouse.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Product','name':'product','field':'id','list':productList]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Supplier','name':'supplier','field':'supplier','list':com.munix.Supplier.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Discount Type','name':'discountType','field':'type','list':com.munix.DiscountType.findAll([sort:'description'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Category','name':'category','field':'category','list':com.munix.ProductCategory.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Subcategory','name':'subcategory','field':'subcategory','list':com.munix.ProductSubcategory.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Brand','name':'brand','field':'brand','list':com.munix.ProductBrand.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Model','name':'model','field':'model','list':com.munix.ProductModel.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Item Type','name':'itemType','field':'itemType','list':com.munix.ItemType.findAll([sort:'identifier'])]}"/>
        </table>
      
        <div>
          <input type="submit" class="button" value="Run"/>
        </div>

      </g:form>
    </div>
	<script>$('#hideFilters').hide(); $('#filters').hide(); </script>
    <div class="list" style="width:1320px">
    	<div class="scrollableHeader">
      <table id="header">
        <thead>
          <tr>	
	        <th class="col0">Item Code</th>
	        <th class="col1">Description</th>
	        <th class="col2">Part Number</th>
	        <th class="col3">Unit</th>
	        <th class="col4">Item Type</th>
	        <th class="col5">Discount Type</th>
	        <th class="col6">Net Price</th>
	        <th class="col7">Currency</th>
	        <th class="col8">Foreign Cost</th>
	        <th class="col9">Local Cost</th>
	        <th class="col10">Wholesale Price</th>
	        <th class="col11">% Margin</th>
	        <th class="col12">Retail Price</th>
	        <th class="col13">% Margin</th>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
		        	<td class="col0">
		        		<g:link controller="product" action="show" id="${bean?.id}">${bean?.identifier}</g:link>
		        	</td>
		        	<td class="col1">
		        		${bean?.category} ${bean?.subcategory} ${bean?.brand} ${bean?.model} ${bean?.model_number} ${bean?.material} ${bean?.size} ${bean?.added_description} ${bean?.color}
		        	</td>
		        	<td class="col2">${bean?.part_number}</td>
		        	<td class="col3">${bean?.unit}</td>
		        	<td class="col4">${bean?.item_type}</td>
		        	<td class="col5">${bean?.type}</td>
		        	<td class="col6">${bean?.is_net?"X":""}</td>
		        	<td class="col7">${bean.currency}</td>
		        	<td class="col8">${bean?.cost_foreign?String.format('%,.4f',bean?.cost_foreign):''}</td>
		        	<td class="col9">${bean?.cost_local?String.format('%,.2f',bean?.cost_local):''}</td>
		        	<g:set var="margin" value="${bean?.is_net?marginsMap[bean.type_id].netItemMargin:marginsMap[bean.type_id].discountedItemMargin}"/>
		        	<td class="col10">${bean?.whole_sale_price?String.format('%,.2f',bean?.whole_sale_price):''}</td>
		        	<td class="col11">${bean.running_cost==0?"0.00":String.format('%,.2f',(((bean.whole_sale_price * (1-margin))/bean.running_cost)-1)*100)}%</td>
		        	<td class="col12">${bean?.retail_price?String.format('%,.2f',bean?.retail_price):''}</td>
		        	<td class="col13">${bean.running_cost==0?"0.00":String.format('%,.2f',(((bean.retail_price * (1-margin))/bean.running_cost)-1)*100)}%</td>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
      </div>
      <div class="scrollable">
      	<a name="scroller"></a>
      <table>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
		        	<td class="col0">
		        		<g:link controller="product" action="show" id="${bean?.id}">${bean?.identifier}</g:link>
		        	</td>
		        	<td class="col1">
		        		${bean?.category} ${bean?.subcategory} ${bean?.brand} ${bean?.model} ${bean?.model_number} ${bean?.material} ${bean?.size} ${bean?.added_description} ${bean?.color}
		        	</td>
		        	<td class="col2">${bean?.part_number}</td>
		        	<td class="col3">${bean?.unit}</td>
		        	<td class="col4">${bean?.item_type}</td>
		        	<td class="col5">${bean?.type}</td>
		        	<td class="col6">${bean?.is_net?"X":""}</td>
		        	<td class="col7">${bean.currency}</td>
		        	<td class="col8">${bean?.cost_foreign?String.format('%,.4f',bean?.cost_foreign):''}</td>
		        	<td class="col9">${bean?.cost_local?String.format('%,.2f',bean?.cost_local):''}</td>
		        	<g:set var="margin" value="${bean?.is_net?marginsMap[bean.type_id].netItemMargin:marginsMap[bean.type_id].discountedItemMargin}"/>
		        	<td class="col10">${bean?.whole_sale_price?String.format('%,.2f',bean?.whole_sale_price):''}</td>
		        	<td class="col11">${bean.running_cost==0?"0.00":String.format('%,.2f',(((bean.whole_sale_price * (1-margin))/bean.running_cost)-1)*100)}%</td>
		        	<td class="col12">${bean?.retail_price?String.format('%,.2f',bean?.retail_price):''}</td>
		        	<td class="col13">${bean.running_cost==0?"0.00":String.format('%,.2f',(((bean.retail_price * (1-margin))/bean.running_cost)-1)*100)}%</td>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
      </div>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${listSize}" params="${params}" />
    </div>
	</div>
</body>
</html>
