
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
	
	th.date{ width:10%;}
	th.po{ width:20%;}
	th.supplier{ width:36%;}
	th.qty{ width:10%;}
	th.finalPrice{ width:12%;}
	th.amount{ width:12%;}
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
	        <th colspan="7">${bean}</th>
        </tr>
        	<tr>
	    		<th class="date">Date</th>
	    		<th class="po">PO #</th>
	    		<th class="product">Product Code</th>
	    		<th class="description">Product Description</th>
	    		<th class="qty right">Qty</th>
	    		<th class="finalPrice right">Final Price</th>
	    		<th class="amount right">Amount</th>
        	</tr>
        </thead>
        <tbody>
        	<g:set var="total" value="${0}"/>
	        	<g:each in="${list[bean]}" var="record">
		        	<g:set var="total" value="${total+record.qty}"/>
	    	    	<tr>
		       			<td><g:formatDate date="${record.po?.date}" format="MM/dd/yyyy"/></td>
		       			<td>${record.po.formatId()}</td>
		       			<td>${record.product}</td>
						<td>${record.product?.description}</td>
		       			<td class="right">${record.formatQty()}</td>
		       			<td class="right">${String.format('%,.4f',record.finalPrice)}</td>
		       			<td class="right">${String.format('%,.4f',record.computeAmount())}</td>
			    	</tr>
			    </g:each>
        </tbody>
		<g:each in="${totals[bean].keySet()}" var="key">        
          <tr>
	        <th></th>
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
