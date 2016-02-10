<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  <style type="text/css" media="print,screen">
	@media print {
	   thead {display: table-header-group;}
	}
	
	div.list table tr td{
		border-left:0px;
		border-right:0px;
	}

	div.list table tr th{
		background-color:white;
		color:black;
		border-top:0px;
		border-left:0px;
		border-right:0px;
		cursor:pointer;
	}

	div.list table{
		border-left:0px;
		border-right:0px;
	}
	
	.right{
		text-align:right;
	}
  </style>
</head>
<body>
    <div class="list">
      <table>
        <thead>
          <tr>	
	        <th onclick="sortReport('identifier')">Code</th>
	        <th onclick="sortReport('formatDescription()')">Item Description</th>
	        <th onclick="sortReport('unit')">Unit</th>
	        <th onclick="sortReport('packageDetails')"></th>
	        <th onclick="sortReport(<g:if test="${priceType=='Retail'}">'retailPrice'</g:if><g:else>'wholeSalePrice'</g:else>)">Price</th>
	        <th></th>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr>
		        	<td>${bean.identifier}</td>
		        	<td>${bean?.category} ${bean?.subcategory} ${bean?.brand} ${bean?.model} ${bean?.model_number} ${bean?.material} ${bean?.size} ${bean?.added_description} ${bean?.color}</td>
		        	<td class="center">${bean.unit}</td>
		        	<td>${bean.package_details}</td>
		        	<td class="right">${String.format('%,.2f',params.priceType=='Retail'?bean.retail_price:bean.whole_sale_price)}</td>
		        	<td>${bean.is_net?"NET":""}</td>
		    	</tr>
	    	</g:each>
	    	<tr>
	    		<td colspan="5">Prices are subject to change to change without prior notice.</td>
	    	</tr>
        </tbody>
      </table>
    </div>
</body>
</html>
