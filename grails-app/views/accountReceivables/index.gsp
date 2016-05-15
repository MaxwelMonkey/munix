<%@ page import="com.munix.Product" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <title>Age of Receivables</title>
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
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
  </style>
</head>
<body>
	<div class="body">
	<a href="#" onclick="$('#filters, #showFilters, #hideFilters').toggle('slow');" id="showFilters">Show Filters [+]</a>
	<a href="#" onclick="$('#filters, #showFilters, #hideFilters').toggle('slow');" id="hideFilters">Hide Filters [-]</a>
	    <div class="dialog" id="filters">
      <g:form name="searchForm" method="post">
      <h2>Filters</h2>
      
        <table>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Customer','name':'customer','field':'customer.id','list':com.munix.Customer.findAll([sort:'name'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Sales Agent','name':'salesAgent','field':'salesAgent.id','list':com.munix.SalesAgent.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Customer Type','name':'type','field':'type.id','list':com.munix.CustomerType.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Business City','name':'city','field':'city.id','list':com.munix.City.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Business Region','name':'region','field':'region.id','list':com.munix.Region.findAll([sort:'identifier'])]}"/>
          <tr class="prop">
            <td class="name">Status</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('status', this.checked)" id="statusCheckAll"> <label for="statusCheckAll">Check All</label>
            	<div class="multicheckbox">
        			<div class="checkbox"><input class="statusCheckbox" type="checkbox" name="status" id="statusActive" value="ACTIVE"> <label for="statusActive">ACTIVE</label></div>
        			<div class="checkbox"><input class="statusCheckbox" type="checkbox" name="status" id="statusInactive" value="INACTIVE"> <label for="statusInactive">INACTIVE</label></div>
        			<div class="checkbox"><input class="statusCheckbox" type="checkbox" name="status" id="statusOnHold" value="ONHOLD"> <label for="statusOnHold">ONHOLD</label></div>
        			<div class="checkbox"><input class="statusCheckbox" type="checkbox" name="status" id="statusBadAccount" value="BADACCOUNT"> <label for="statusBadAccount">BADACCOUNT</label></div>
            	</div>
            </td>
          </tr>

        </table>
      
        <div>
          <input type="submit" class="button" value="Run"/>
        </div>

      </g:form>
    </div>
    <div class="list">
      <table>
        <thead>
          <tr>	
	        <th>Identifier</th>
	        <th>Name</th>
	        <th>Sales Agent</th>
	        <th>Credit Limit</th>
	        <th>Customer Charges</th>
	        <th>Bounced Checks</th>
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
		        	<td>
          			<g:set var="unpaidCc" value="${unpaidCcs[bean?.id]?unpaidCcs[bean?.id]:0}"/>
          			<g:link controller="customerCharge" action="unpaidList" params="['customerId':bean?.id]">PHP <g:formatNumber number="${unpaidCc}" format="###,##0.00" /></g:link>
          			</td>
		        	<td>
          			<g:set var="unpaidBc" value="${unpaidBcs[bean?.id]?unpaidBcs[bean?.id]:0}"/>
          			<g:link controller="bouncedCheck" action="unpaidList" params="['customerId':bean?.id]">PHP <g:formatNumber number="${unpaidBc}" format="###,##0.00" /></g:link>
          			</td>
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
