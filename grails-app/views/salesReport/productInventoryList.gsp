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
	        <th>Unit</th>
	        <g:each in="${params.warehouse?.id}" status="i" var="warehouse">
	        <th>${com.munix.Warehouse.get(warehouse)?.identifier}</th>
	        </g:each>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr>
		        	<td>${bean}</td>
		        	<td>${bean.formatDescription()}</td>
		        	<td class="center">${bean.unit}</td>
			        <g:each in="${params.warehouse?.id}" var="warehouse">
		        	<td class="right">
		        		<g:each in="${bean.stocks}" var="stock">
		        			<g:if test="${stock.warehouse?.id==warehouse.toInteger()}">
		        				${String.format('%,.2f',stock.qty)}
		        			</g:if>
		        		</g:each>
		        	</td>
		        	</g:each>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
    </div>
</body>
</html>
