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
      	<g:set var="warehouses" value="${com.munix.Warehouse.list(sort:'identifier')}"/>
        <thead>
          <tr>	
	        <th onclick="sortReport('identifier')">Code</th>
	        <th>Description</th>
	        <th>Part Number</th>
	        <th>Unit</th>
	        <g:each in="${warehouses}" status="i" var="warehouse">
	        <th>${warehouse?.identifier}</th>
	        </g:each>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr>
		        	<td>${bean.identifier}</td>
		        	<td>${bean?.category} ${bean?.subcategory} ${bean?.brand} ${bean?.model} ${bean?.model_number} ${bean?.material} ${bean?.size} ${bean?.added_description} ${bean?.color}</td>
		        	<td>${bean?.part_number}</td>
		        	<td class="center">${bean.unit}</td>
	        		<g:set var="stocks" value="${com.munix.Stock.findAll('from Stock s where s.product.id = :productId',['productId':bean.id])}"/>
	        		<g:set var="stockMap" value="${[:]}"/>
	        		<g:each in="${stocks}" var="stock">
	        			<% stockMap[stock.warehouse?.id] = stock.qty; %>
	        		</g:each>
			        <g:each in="${warehouses}" var="warehouse">
		        	<td class="right">
		        		<g:set var="qty" value="${stockMap[warehouse?.id]}"/>
		        		${String.format('%,.0f',qty)}
		        	</td>
		        	</g:each>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
    </div>
</body>
</html>
