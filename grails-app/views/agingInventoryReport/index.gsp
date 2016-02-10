<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title>Age of Receivables</title>
  <style type="text/css" media="print,screen">
	@media print {
	   thead {display: table-header-group;}
	}
	
	
	.right{
		text-align:right;
	}
  </style>
</head>
<body>
	<div class="body">
	<h1>Aging of Inventory</h1>
	<g:render template="/common/product_filters"/>
    <div class="list">
      <table>
        <thead>
          <tr>	
	        <th>Product Code</th>
	        <th>Product Description</th>
	        <th>Product Unit</th>
	        <th>Item Type</th>
	        <th>Discount Type</th>
	        <th>30 Days</th>
	        <th>60 Days</th>
	        <th>90 Days</th>
	        <th>120 Days</th>
	        <th>&gt;120 Days</th>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
		        	<td>${bean?.identifier}</td>
		        	<td>${bean?.category} ${bean?.subcategory} ${bean?.brand} ${bean?.model} ${bean?.model_number} ${bean?.material} ${bean?.size} ${bean?.added_description} ${bean?.color}</td>
		        	<td>${bean?.unit}</td>
		        	<td>${bean?.item_type}</td>
		        	<td>${bean?.type}</td>
		        	<td>${bean?.aoi30}</td>
		        	<td>${bean?.aoi60}</td>
		        	<td>${bean?.aoi90}</td>
		        	<td>${bean?.aoi120}</td>
		        	<td>${bean?.aoigt120}</td>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
    </div>
	</div>
</body>
</html>
