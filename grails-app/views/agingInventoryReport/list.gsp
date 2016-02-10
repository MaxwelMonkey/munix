<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
  <title>Aging of Inventory</title>
  <style type="text/css" media="print,screen">
	@media print {
	   thead {display: table-header-group;}
	}
	
	
	.right{
		text-align:right;
	}
	
	#filters, #hideFilters{
		display:none;
	}
	
	section {
  position: relative;
  border: 1px solid #000;
  padding-top: 37px;
  background: #267596;
}
section.positioned {
  position: absolute;
  top:100px;
  left:100px;
  width:800px;
  box-shadow: 0 0 15px #333;
}
	
	.list {
  overflow-y: auto;
  height: 500px;
}
.list table {
  border-spacing: 0;
  width:100%;
}
.list td + td {
  border-left:1px solid #eee;
}
.list td, .list th {
  border-bottom:1px solid #eee;
}
.list th {
  height: 0;
  line-height: 0;
  padding-top: 0;
  padding-bottom: 0;
  color: transparent;
  border: none;
  white-space: nowrap;
}
.list th div{
  position: absolute;
  background: transparent;
  color: #fff;
  padding: 9px 10px;
  top: 0;
  margin-left: -10px;
  line-height: normal;
  border-left: 1px solid #eee;
}
.list 	th:first-child div{
  border: none;
}
  </style>
</head>
<body>
	<div class="body">
	<h1>Aging of Inventory</h1>
	<h2>Filtered ${list.size()} records.</h2>
	<a href="#" onclick="$('#filters, #showFilters, #hideFilters').toggle('slow');" id="showFilters">Show Filters [+]</a>
	<a href="#" onclick="$('#filters, #showFilters, #hideFilters').toggle('slow');" id="hideFilters">Hide Filters [-]</a>
	<g:render template="/common/product_filters" model="${['reportType':'agingOfInventory', 'action':'list']}" />
<section class="">
      <div class="list">
      <table>
        <thead>
          <tr class="header">	
	        <th>Product Code<div>Product Code</div></th>
	        <th>Product Description<div>Product Description</div></th>
	        <th>Product Unit<div>Product Unit</div></th>
	        <th>Item Type<div>Item Type</div></th>
	        <th>Discount Type<div>Discount Type</div></th>
	        <th>30 Days<div>30 days</div></th>
	        <th>60 Days<div>60 days</div></th>
	        <th>90 Days<div>90 days</div></th>
	        <th>120 Days<div>120 days</div></th>
	        <th>&gt;120 Days<div>&gt; 120 days</div></th>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
		        	<td><g:link controller="product" action="show" id="${bean?.id}">${bean?.identifier}</g:link></td>
		        	<td>${bean?.category} ${bean?.subcategory} ${bean?.brand} ${bean?.model} ${bean?.model_number} ${bean?.material} ${bean?.size} ${bean?.added_description} ${bean?.color}</td>
		        	<td>${bean?.unit}</td>
		        	<td>${bean?.item_type}</td>
		        	<td>${bean?.type}</td>
		        	<td>${String.format('%,.2f',bean?.aoi30)}</td>
		        	<td>${String.format('%,.2f',bean?.aoi60)}</td>
		        	<td>${String.format('%,.2f',bean?.aoi90)}</td>
		        	<td>${String.format('%,.2f',bean?.aoi120)}</td>
		        	<td>${String.format('%,.2f',bean?.aoigt120)}</td>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
    </div>
    </section>
	</div>
</body>
</html>
