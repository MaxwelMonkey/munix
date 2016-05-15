<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
  <title>Aging of Inventory</title>
  <style type="text/css" media="print,screen">
	@media print {
	   thead {display: table-header-group;}
	}
	
	
	.right{
		text-align:right;
	}
	
	#filters, #hideFilters{
		display:none;
	}
	
	section {
  position: relative;
  border: 1px solid #000;
  padding-top: 37px;
  background: #267596;
}
section.positioned {
  position: absolute;
  top:100px;
  left:100px;
  width:800px;
  box-shadow: 0 0 15px #333;
}
	
	.list {
  overflow-y: auto;
  height: 500px;
}
.list table {
  border-spacing: 0;
  width:100%;
}
.list td + td {
  border-left:1px solid #eee;
}
.list td, .list th {
  border-bottom:1px solid #eee;
}
.list th {
  height: 0;
  line-height: 0;
  padding-top: 0;
  padding-bottom: 0;
  color: transparent;
  border: none;
  white-space: nowrap;
}
.list th div{
  position: absolute;
  background: transparent;
  color: #fff;
  padding: 9px 10px;
  top: 0;
  margin-left: -10px;
  line-height: normal;
  border-left: 1px solid #eee;
}
.list 	th:first-child div{
  border: none;
}
  </style>
</head>
<body>
	<div class="body">
	<h1>Aging of Inventory</h1>
	<h2>Filtered ${list.size()} records.</h2>
	<a href="#" onclick="$('#filters, #showFilters, #hideFilters').toggle('slow');" id="showFilters">Show Filters [+]</a>
	<a href="#" onclick="$('#filters, #showFilters, #hideFilters').toggle('slow');" id="hideFilters">Hide Filters [-]</a>
	<g:render template="/common/product_filters" model="${['reportType':'agingOfInventory', 'action':'list']}" />
<section class="">
      <div class="list">
      <table>
        <thead>
          <tr class="header">	
	        <th>Product Code<div>Product Code</div></th>
	        <th>Product Description<div>Product Description</div></th>
	        <th>Product Unit<div>Product Unit</div></th>
	        <th>Item Type<div>Item Type</div></th>
	        <th>Discount Type<div>Discount Type</div></th>
	        <th>0-90 Days<div>0-90 days</div></th>
	        <th>91-180 Days<div>91-180 days</div></th>
	        <th>181-270 Days<div>181-270 days</div></th>
	        <th>271-360 Days<div>271-360 days</div></th>
	        <th>&gt;360 Days<div>&gt; 360 days</div></th>
        </tr>
        </thead>
        <tbody>
	        <g:each in="${list}" status="i" var="bean">
	        	<g:set var="aoi30" value="${bean?.aoi30}"/>
	        	<g:set var="aoi60" value="${bean?.aoi60}"/>
	        	<g:set var="aoi90" value="${bean?.aoi90}"/>
	        	<g:set var="aoi120" value="${bean?.aoi120}"/>
	        	<g:set var="aoigt120" value="${bean?.aoigt120}"/>
	        	<g:set var="aoiia" value="${bean?.aoiia}"/>
    	    	<tr class="${(i % 2) == 0 ? 'odd' : 'even'}" >
	        	<g:if test="${aoiia<0}">
	    	    	<g:while test="${aoiia<0}">
		        		<g:if test="${aoigt120>0}">
		        			<g:set var="aoiiatemp" value="${aoiia+aoigt120}"/>
		        			<g:if test="${aoiiatemp>0}">
		        				<g:set var="aoiiatemp" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoigt120" value="${aoigt120+aoiia}"/>
		        			<g:if test="${aoigt120<0}">
		        				<g:set var="aoigt120" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoiia" value="${aoiiatemp}"/>
		        		</g:if>
		        		<g:if test="${aoi120>0}">
		        			<g:set var="aoiiatemp" value="${aoiia+aoi120}"/>
		        			<g:if test="${aoiiatemp>0}">
		        				<g:set var="aoiiatemp" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoi120" value="${aoi120+aoiia}"/>
		        			<g:if test="${aoi120<0}">
		        				<g:set var="aoi120" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoiia" value="${aoiiatemp}"/>
		        		</g:if>
		        		<g:if test="${aoi90>0}">
		        			<g:set var="aoiiatemp" value="${aoiia+aoi90}"/>
		        			<g:if test="${aoiiatemp>0}">
		        				<g:set var="aoiiatemp" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoi90" value="${aoi90+aoiia}"/>
		        			<g:if test="${aoi90<0}">
		        				<g:set var="aoi90" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoiia" value="${aoiiatemp}"/>
		        		</g:if>
		        		<g:if test="${aoi60>0}">
		        			<g:set var="aoiiatemp" value="${aoiia+aoi60}"/>
		        			<g:if test="${aoiiatemp>0}">
		        				<g:set var="aoiiatemp" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoi60" value="${aoi60+aoiia}"/>
		        			<g:if test="${aoi60<0}">
		        				<g:set var="aoi60" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoiia" value="${aoiiatemp}"/>
		        		</g:if>
		        		<g:if test="${aoi30>0}">
		        			<g:set var="aoiiatemp" value="${aoiia+aoi30}"/>
		        			<g:if test="${aoiiatemp>0}">
		        				<g:set var="aoiiatemp" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoi30" value="${aoi30+aoiia}"/>
		        			<g:if test="${aoi30<0}">
		        				<g:set var="aoi30" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoiia" value="${aoiiatemp}"/>
		        		</g:if>
		        			<g:set var="aoiia" value="${0.00}"/>
		        	</g:while>
	        	</g:if>
	        	<g:if test="${aoiia>0}">
	    	    	<g:while test="${aoiia>0}">
		        		<g:if test="${aoigt120<0}">
		        			<g:set var="aoiiatemp" value="${aoiia+aoigt120}"/>
		        			<g:if test="${aoiiatemp<0}">
		        				<g:set var="aoiiatemp" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoigt120" value="${aoigt120+aoiia}"/>
		        			<g:if test="${aoigt120>0}">
		        				<g:set var="aoigt120" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoiia" value="${aoiiatemp}"/>
		        		</g:if>
		        		<g:if test="${aoi120<0}">
		        			<g:set var="aoiiatemp" value="${aoiia+aoi120}"/>
		        			<g:if test="${aoiiatemp<0}">
		        				<g:set var="aoiiatemp" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoi120" value="${aoi120+aoiia}"/>
		        			<g:if test="${aoi120>0}">
		        				<g:set var="aoi120" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoiia" value="${aoiiatemp}"/>
		        		</g:if>
		        		<g:if test="${aoi90<0}">
		        			<g:set var="aoiiatemp" value="${aoiia+aoi90}"/>
		        			<g:if test="${aoiiatemp<0}">
		        				<g:set var="aoiiatemp" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoi90" value="${aoi90+aoiia}"/>
		        			<g:if test="${aoi90>0}">
		        				<g:set var="aoi90" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoiia" value="${aoiiatemp}"/>
		        		</g:if>
		        		<g:if test="${aoi60<0}">
		        			<g:set var="aoiiatemp" value="${aoiia+aoi60}"/>
		        			<g:if test="${aoiiatemp<0}">
		        				<g:set var="aoiiatemp" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoi60" value="${aoi60+aoiia}"/>
		        			<g:if test="${aoi60>0}">
		        				<g:set var="aoi60" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoiia" value="${aoiiatemp}"/>
		        		</g:if>
		        		<g:if test="${aoi30<0}">
		        			<g:set var="aoiiatemp" value="${aoiia+aoi30}"/>
		        			<g:if test="${aoiiatemp<0}">
		        				<g:set var="aoiiatemp" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoi30" value="${aoi30+aoiia}"/>
		        			<g:if test="${aoi30>0}">
		        				<g:set var="aoi30" value="${0.00}"/>
		        			</g:if>
		        			<g:set var="aoiia" value="${aoiiatemp}"/>
		        		</g:if>
		        			<g:set var="aoiia" value="${0.00}"/>
		        	</g:while>
	        	</g:if>
	        	
		        	<td><g:link controller="product" action="show" id="${bean?.id}">${bean?.identifier}</g:link></td>
		        	<td>${bean?.category} ${bean?.subcategory} ${bean?.brand} ${bean?.model} ${bean?.model_number} ${bean?.material} ${bean?.size} ${bean?.added_description} ${bean?.color}</td>
		        	<td>${bean?.unit}</td>
		        	<td>${bean?.item_type}</td>
		        	<td>${bean?.type}</td>
						<g:set var="positive" value="${''}"/>
		        		<g:if test="${aoi30<0}">
			        		<g:set var="positive" value="${'negative'}"/>
		        		</g:if>
		        		<g:if test="${aoi30>0}">
			        		<g:set var="positive" value="${'positive'}"/>
		        		</g:if>
		        	<td class="${positive}">${String.format('%,.2f',aoi30)}</td>
						<g:set var="positive" value="${''}"/>
		        		<g:if test="${aoi60<0}">
			        		<g:set var="positive" value="${'negative'}"/>
		        		</g:if>
		        		<g:if test="${aoi60>0}">
			        		<g:set var="positive" value="${'positive'}"/>
		        		</g:if>
		        	<td class="${positive}">${String.format('%,.2f',aoi60)}</td>
						<g:set var="positive" value="${''}"/>
		        		<g:if test="${aoi90<0}">
			        		<g:set var="positive" value="${'negative'}"/>
		        		</g:if>
		        		<g:if test="${aoi90>0}">
			        		<g:set var="positive" value="${'positive'}"/>
		        		</g:if>
		        	<td class="${positive}">${String.format('%,.2f',aoi90)}</td>
						<g:set var="positive" value="${''}"/>
		        		<g:if test="${aoi120<0}">
			        		<g:set var="positive" value="${'negative'}"/>
		        		</g:if>
		        		<g:if test="${aoi120>0}">
			        		<g:set var="positive" value="${'positive'}"/>
		        		</g:if>
		        	<td class="${positive}">${String.format('%,.2f',aoi120)}</td>
						<g:set var="positive" value="${''}"/>
		        		<g:if test="${aoigt120<0}">
			        		<g:set var="positive" value="${'negative'}"/>
		        		</g:if>
		        		<g:if test="${aoigt120>0}">
			        		<g:set var="positive" value="${'positive'}"/>
		        		</g:if>
		        	<td class="${positive}">${String.format('%,.2f',aoigt120)}</td>
		    	</tr>
	    	</g:each>
        </tbody>
      </table>
    </div>
    </section>
	</div>
</body>
</html>
