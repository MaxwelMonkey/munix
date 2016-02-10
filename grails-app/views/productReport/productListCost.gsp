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
	        <th>Description</th>
	        <th>Part Number</th>
	        <th>Unit</th>
	        <th>Currency</th>
	        <th>Unit Price</th>
	        <th>Exchange Rate</th>
	        <th>Peso Amount</th>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr>
		        	<td>${bean?.identifier}</td>
		        	<td>${bean?.category} ${bean?.subcategory} ${bean?.brand} ${bean?.model} ${bean?.model_number} ${bean?.material} ${bean?.size} ${bean?.added_description} ${bean?.color}</td>
		        	<td>${bean?.part_number}</td>
		        	<td>${bean?.unit}</td>
		        	<td>${bean?.currency}</td>
		        	<td>${bean?.cost_foreign?String.format('%,.4f',bean?.cost_foreign):''}</td>
		        	<td>${bean?.exchange_rate}</td>
		        	<td>${bean?.cost_local?String.format('%,.2f',bean?.cost_local):''}</td>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
    </div>
</body>
</html>
