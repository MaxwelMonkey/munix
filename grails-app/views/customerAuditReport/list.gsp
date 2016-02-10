<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title>Customer List</title>
  <style type="text/css" media="print,screen">
	@media print {
	   thead {display: table-header-group;}
	}
	
	div.body{
		width:98%;
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
	
	tr.totals td{
		font-weight:bold;
		text-align:right;
	}
  </style>
</head>
<body>
    <div class="list">
      <g:each in="${result.keySet()}" status="i" var="customer">
      <table>
        <thead>
          <tr>
	        <th colspan="99">Customer Name: ${customer}</th>
        </tr>
          <tr>	
	        <th width="20%">Date &amp; Time</th>
	        <th width="10%">Field</th>
	        <th width="30%">Old Value</th>
	        <th width="30%">New Value</th>
	        <th width="10%">Name</th>
        </tr>
        </thead>
        <tbody>
        	<g:each in="${result[customer]}" var="auditLog">
    	    	<tr>
	       			<td><g:formatDate date="${auditLog.date}" format="MM/dd/yyyy HH:mm:ss"/></td>
	       			<td>${auditLog.fieldName}</td>
	       			<td>${auditLog.previousEntry}</td>
	       			<td>${auditLog.newEntry}</td>
	       			<td>${auditLog.user.userRealName}</td>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
   	</g:each>
    </div>
</body>
</html>
