
<%@ page import="com.munix.SalesDelivery" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
  <g:set var="entityName" value="${message(code: 'purchaseOrder.label', default: 'PurchaseOrder')}" />
  <title>Pending Purchase Orders</title>
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
	}

	div.list table{
		border-left:0px;
		border-right:0px;
	}
	
	th.date{ width:15%;}
	th.po{ width:20%;}
	th.supplier{ width:20%;}
	th.qty{ width:15%;}
	th.finalPrice{ width:15%;}
	th.amount{ width:15%;}
	.right{
		text-align:right;
	}
  </style>
</head>
<body>
    <div class="list">
      <g:each in="${list.keySet()}" status="i" var="bean">
      <table>
        <thead>
          <tr>
	        <th colspan="6">${bean} - ${bean.description}</th>
        </tr>
    	<tr>
    		<th class="date">Date</th>
    		<th class="po">PO #</th>
    		<th class="supplier">Supplier</th>
    		<th class="qty right">Qty</th>
    		<th class="finalPrice right">Final Price</th>
    		<th class="amount right">Amount</th>
    	</tr>
        </thead>
        <tbody>
	        	<g:each in="${list[bean]}" var="record">
	    	    	<tr>
		       			<td><g:formatDate date="${record.po?.date}" format="MM/dd/yyyy"/></td>
		       			<td>${record.po?.formatId()}</td>
		       			<td>${record.po?.supplier}</td>
		       			<td class="right">${record.formatRemainingBalance()}</td>
		       			<td class="right">${String.format('%,.4f',record.finalPrice)}</td>
		       			<td class="right">${String.format('%,.4f',record.finalPrice * record.computeRemainingBalance())}</td>
			    	</tr>
			    </g:each>
        </tbody>
		<g:each in="${totals[bean].keySet()}" var="key">        
          <tr>
	        <th></th>
	        <th></th>
	        <th></th>
	        <th class="right">Total</th>
	        <th class="right">${key}</th>
	        <th class="right">${String.format('%,.4f',totals[bean][key])}</th>
        	</tr>
        </g:each>
      </table>
	  </g:each>
    </div>
</body>
</html>
