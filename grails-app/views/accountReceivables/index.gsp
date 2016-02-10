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
    <div class="list">
      <table>
        <thead>
          <tr>	
	        <th>Identifier</th>
	        <th>Name</th>
	        <th>Sales Agent</th>
	        <th>Credit Limit</th>
	        <th>Terms</th>
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
		        	<td><g:link controller="customer" action="show" id="${bean?.id}">${bean?.identifier}</g:link></td>
		        	<td><g:link controller="customer" action="show" id="${bean?.id}">${bean?.name}</g:link></td>
		        	<td>${bean?.sales_agent}</td>
		        	<td>${bean.credit_limit?String.format('%,.2f',bean?.credit_limit):""}</td>
		        	<td>${bean.term}</td>
		        	<td><g:link controller="accountReceivables" action="list" id="${bean?.id}" params="${[from:0, to:30]}">${bean.aor30}</g:link></td>
		        	<td><g:link controller="accountReceivables" action="list" id="${bean?.id}" params="${[from:30, to:60]}">${bean.aor60}</g:link></td>
		        	<td><g:link controller="accountReceivables" action="list" id="${bean?.id}" params="${[from:60, to:90]}">${bean.aor90}</g:link></td>
		        	<td><g:link controller="accountReceivables" action="list" id="${bean?.id}" params="${[from:90, to:120]}">${bean.aor120}</g:link></td>
		        	<td><g:link controller="accountReceivables" action="list" id="${bean?.id}" params="${[from:120, to:9999]}">${bean.aorgt120}</g:link></td>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
    </div>
	</div>
</body>
</html>
