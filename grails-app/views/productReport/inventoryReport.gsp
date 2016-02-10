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
	
	tr.totals td{
		font-weight:bold;
		text-align:right;
	}
  </style>
  <script>
	jQuery(document).ready(function(){
	  	 jQuery(".balanceSpan").each(function(){
	  		var productId = jQuery(this).attr("productId");
	  		jQuery.ajax({
			type: "POST",
			url: "${resource(dir:'productReport',file:'retrieveRunningBalance')}",
			data: { date: "<g:formatDate date="${params.asOfDate}" format="MM/dd/yyyy"/>", productId: productId }
			})
			.done(function( msg ) {
				jQuery("#balanceSpan_"+productId).html(msg);
			});
			
	  	});

	  	 jQuery(".warehouseBalanceSpan").each(function(){
	  		var warehouse = jQuery(this).attr("warehouse");
	  		var warehouseId = jQuery(this).attr("warehouseId");
	  		var productId = jQuery(this).attr("productId");
	  		jQuery.ajax({
			type: "POST",
			url: "${resource(dir:'productReport',file:'retrieveRunningWarehouseBalance')}",
			data: { date: "<g:formatDate date="${params.asOfDate}" format="MM/dd/yyyy"/>", productId: productId, warehouse: warehouse }
			})
			.done(function( msg ) {
				jQuery("#balanceSpan_"+warehouseId+"_"+productId).html(msg);
			});
			
	  	});
  	});
  </script>
</head>
<body>
    <div class="list">
      <table>
      	<g:set var="warehouses" value="${com.munix.Warehouse.list(sort:'identifier')}"/>
      	<g:set var="totalForeign" value="${0}"/>
      	<g:set var="totalPeso" value="${0}"/>
      	<g:set var="totalForeign" value="${totalForeign + totalPeso}"/>
      	<g:set var="currencyTotals" value="${[:]}"/>
      	<g:set var="currencyExchange" value="${[:]}"/>
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
	        <th>Running Balance</th>
	        <g:each in="${warehouses}" status="i" var="warehouse">
	        <th>${warehouse?.identifier}</th>
	        </g:each>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
    	    	<tr>
		        	<td>${bean?.identifier}</td>
		        	<td>${bean?.category} ${bean?.subcategory} ${bean?.brand} ${bean?.model} ${bean?.model_number} ${bean?.material} ${bean?.size} ${bean?.added_description} ${bean?.color}</td>
		        	<td>${bean?.part_number}</td>
		        	<td>${bean?.unit}</td>
		        	<td class="right">${bean?.currency}</td>
		        	<td class="right">${bean?.cost_foreign?String.format('%,.4f',bean?.cost_foreign):''}</td>
		        	<td class="right">${bean?.exchange_rate}</td>
		        	<td class="right">${bean?.cost_local?String.format('%,.2f',bean?.cost_local):''}</td>
	        		<%-- g:set var="stocks" value="${com.munix.Stock.findAll('from Stock s where s.product.id = :productId',['productId':bean.id])}"/>
	        		<g:set var="stockMap" value="${[:]}"/>
	        		<g:each in="${stocks}" var="stock">
	        			<% stockMap[stock.warehouse?.id] = stock.qty; %>
	        		</g:each  --%>
		        	<td class="right"><span id="balanceSpan_${bean.id}" class="balanceSpan" productId="${bean.id}"><img src="${resource(dir:'images',file:'spinner.gif')}"></span></td>
			        <g:each in="${warehouses}" var="warehouse">
		        	<td class="right">
		        		<%-- g:set var="qty" value="${stockMap[warehouse?.id]}"/>
		        		<g:if test="${qty}">
	        				<g:set var="costForeign" value="${(bean?.cost_foreign?bean.cost_foreign*qty:0)}"/>
	        				<g:set var="totalForeign" value="${totalForeign+costForeign}"/>
	        				<g:set var="totalPeso" value="${totalPeso+(bean?.cost_local?bean.cost_local*qty:0)}"/>
	        				<g:if test="${bean.currency}">
		        				<g:if test="${!currencyTotals[bean?.currency]}">
			        				<% currencyTotals[bean?.currency] = 0 %>
		        				</g:if>
		        				<% currencyTotals[bean?.currency] += costForeign %>
		        				<g:if test="${!currencyExchange[bean?.currency]}">
			        				<% currencyExchange[bean?.currency] = bean.exchange_rate %>
		        				</g:if>
	        				</g:if>
	        				${String.format('%,.0f',qty)}
	        			</g:if --%>
	        			<span id="balanceSpan_${warehouse?.id}_${bean.id}" class="warehouseBalanceSpan" productId="${bean.id}" warehouse="${warehouse?.identifier}" warehouseId="${warehouse?.id}"><img src="${resource(dir:'images',file:'spinner.gif')}"></span>
		        	</td>
		        	</g:each>
		    	</tr>
	    	</g:each>
	    	<tr class="totals">
	    		<td colspan="4">Total Peso Amount</td>
	    		<td colspan="3"></td>
	    		<td>${totalPeso==0?"0.00":String.format('%,.2f',totalPeso)}</td>
	    	</tr>
	    	<g:set var="displayGrandTotal" value="${0}"/>
	    	<g:each in="${currencyTotals.keySet()}" var="currency">
	    	<tr class="totals">
	    		<td colspan="4"><g:if test="${displayGrandTotal==0}">Grand Total</g:if></td>
	    		<td></td>
	    		<td>${currency}</td>
	    		<td>${currencyExchange[currency]}</td>
	    		<td>${String.format('%,.2f',currencyTotals[currency])}</td>
	    	</tr>
		    	<g:set var="displayGrandTotal" value="${1}"/>
	    	</g:each>
        </tbody>
      </table>
    </div>
</body>
</html>
