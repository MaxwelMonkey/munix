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
      <table>
        <thead>
          <tr>	
	        <th>Code</th>
	        <th>Name</th>
	        <th>Sales Agent</th>
	        <th>Business City, Region</th>
	        <th>Landline</th>
	        <th>Credit Limit</th>
	        <th>Terms</th>
	        <th>Status</th>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr>
    	    		<td>${bean.identifier}</td>
    	    		<td>${bean.name}</td>
    	    		<td>${bean.first_name} ${bean.last_name}</td>
    	    		<td>${bean.city}, ${bean.region}</td>
    	    		<td>${bean.landline}</td>
    	    		<td class="right">${String.format('%,.2f',bean.credit_limit)}</td>
    	    		<td>${bean.terms}</td>
    	    		<td>${bean.status=='BADACCOUNT'?'Bad Account':bean.status=='ONHOLD'?'On Hold':bean.status=='INACTIVE'?'Inactive':'Active'}</td>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
    </div>
</body>
</html>
