
<%@ page import="com.munix.Customer" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:javascript src="jquery.dataTables.min.js" />
  <g:javascript src="FixedHeader.min.js" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
    <g:set var="entityName" value="${message(code: 'customerAccountsSummary.label', default: 'Customer Accounts Summary')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
    <style>
		th{
		    border-left: 1px solid #ddd;
		    border: 1px solid #E5EFF8;
		    padding: 8px;
		    background: #267596;
		}
		
		th:hover{
		    background: #4B9BBD;
		}
		
		div.fixedHeader table{
    		margin-top:-10px !important;
		}
    </style>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
  	<h1><g:message code="customerAccountsSummary.list" default="Customer Accounts Summary" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>

	    <div id="search">
      <g:form controller="customer" action="accountsSummary" >
        <table>
          <tr>
            <td class="name">Customer Identifier</td>
            <td class="value"><g:textField name="customerIdentifier" value="${params?.customerIdentifier}"/></td>
          </tr>
          <tr>
            <td class="name">Customer Name</td>
            <td class="value"><g:textField name="customerName" value="${params?.customerName}"/></td>
          </tr>
          <tr>
          	<td class="name">Has Balance</td>
			<td class="buttons">
          		<g:checkBox name="hasBalanceSD" value="${params?.hasBalanceSD}"/> Sales Delivery <br><br>
          		<g:checkBox name="hasBalanceCC" value="${params?.hasBalanceCC}"/> Customer Charge <br><br>
          		<g:checkBox name="hasBalanceDM" value="${params?.hasBalanceDM}"/> Debit Memo <br><br>
          		<g:checkBox name="hasBalanceBC" value="${params?.hasBalanceBC}"/> Bounced Check <br><br>
          		<g:checkBox name="hasBalancePC" value="${params?.hasBalancePC}"/> Pending Checks
          	</td>
          </tr>
          <tr>
            <td class="name">Over credit limit</td>
            <td class="value"><g:select name="overCreditLimit" noSelection="${['':'']}" from="${['True', 'False']}" id="overCreditLimit" value="${params?.overCreditLimit}"/></td>
          </tr>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Terms','name':'terms','field':'term.id','list':com.munix.Term.findAll()]}"/>
          <tr>
            <td class="name">Period</td>
            <td class="value">
            	<input type="radio" name="dateRange" id="dateAll" value="all" onclick="$('#dateRangeRow').find('select').attr('disabled','true')" <g:if test="${!params.dateRange||params.dateRange=='all'}">checked</g:if>>All
            	<input type="radio" name="dateRange" id="dateRange" value="range" onclick="$('#dateRangeRow').find('select').removeAttr('disabled')" <g:if test="${params.dateRange=='range'}">checked</g:if>>Date Range
            </td>
          </tr>
          <tr id="dateRangeRow">
            <td class="name">Date</td>
            <td class="value">
	          From <g:datePicker name="dateFrom" precision="day" noSelection="['': '']" value="${params.dateFrom}"/>
	          To <g:datePicker name="dateTo" precision="day" noSelection="['': '']" value="${params.dateTo}"/>
	          <g:if test="${!params.dateRange||params.dateRange=='all'}"><script>$('#dateRangeRow').find('select').attr('disabled','true')</script></g:if>
            </td>
          </tr>
          <tr>
            <td class="name">With Total</td>
            <td class="value"><input type="checkbox" name="withTotal" value="Y" <g:if test="${!params.dateRange || params.withTotal=='Y'}">checked</g:if>/></td>
          </tr>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Sales Agent','name':'salesAgent','field':'salesAgent.id','list':com.munix.SalesAgent.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Business City','name':'salesAgent','field':'busAddrCity.id','list':com.munix.City.findAll([sort:'description'])]}"/>
        </table>
        <div>
          <input type="submit" class="button" value="Search"/>
        </div>

      </g:form>
    </div>

    <div class="list">
      <table id="list">
        <thead>
          <tr>
          	<th class="center">Identifier</th>
          	<th class="center">Name</th>
          	<th class="center">Credit Limit</th>
          	<th class="center">Terms</th>
          	<th class="center">Sales Deliveries</th>
          	<th class="center">Customer Charges</th>
          	<th class="center">Debit Memos</th>
          	<th class="center">Bounced Checks</th>
          	<th class="center">Pending Checks</th>
          	<th class="center">Balance</th>
          </tr>
        </thead>
        <tbody>
        <g:set var="sdTotal" value="${0}"/>
        <g:set var="ccTotal" value="${0}"/>
        <g:set var="dmTotal" value="${0}"/>
        <g:set var="bcTotal" value="${0}"/>
        <g:set var="pcTotal" value="${0}"/>
        <g:set var="bTotal" value="${0}"/>
        <g:each in="${customerAccounts}" status="bc" var="customerAccountInstance">
          <tr class="${(bc % 2) == 0 ? 'odd' : 'even'}" >
          	<g:set var="redText" value=""/>
            <g:if test="${customerAccountInstance?.isOverLimit()}">
                <g:set var="redText" value="redText"/>
            </g:if>
          	
          	<td class="left ${redText}"><g:link action="show" controller="customer" id="${customerAccountInstance?.customer?.id}">${customerAccountInstance?.customer?.identifier}</g:link></td>
          	<td class="left ${redText}">${customerAccountInstance?.customer?.name}</td>
          	<td class="left ${redText}">PHP <g:formatNumber number="${customerAccountInstance?.customer?.creditLimit}" format="###,##0.00" /></td>
          	<td class="left ${redText}">${customerAccountInstance?.customer?.term}</td>
          	<td class="right ${redText}" onclick="window.location='${createLink(uri:'/')}salesDelivery/unpaidList?customerId=${customerAccountInstance?.customer?.id}'">
        		<g:set var="unpaidSd" value="${unpaidSds[customerAccountInstance.customer?.id]?unpaidSds[customerAccountInstance.customer?.id]:0}"/>
          		PHP <g:formatNumber number="${unpaidSd}" format="###,##0.00" />
		        <g:set var="sdTotal" value="${sdTotal+unpaidSd}"/>
          	</td>
          	<td class="right ${redText}" onclick="window.location='${createLink(uri:'/')}customerCharge/unpaidList?customerId=${customerAccountInstance?.customer?.id}'">
          		<g:set var="unpaidCc" value="${unpaidCcs[customerAccountInstance.customer?.id]?unpaidCcs[customerAccountInstance.customer?.id]:0}"/>
          		PHP <g:formatNumber number="${unpaidCc}" format="###,##0.00" />
		        <g:set var="ccTotal" value="${ccTotal+unpaidCc}"/>
          	</td>
          	<td class="right ${redText}" onclick="window.location='${createLink(uri:'/')}creditMemo/unpaidList?customerId=${customerAccountInstance?.customer?.id}'">
          		<g:set var="unpaidDm" value="${unpaidDms[customerAccountInstance.customer?.id]?unpaidDms[customerAccountInstance.customer?.id]:0}"/>
          		PHP <g:formatNumber number="${unpaidDm}" format="###,##0.00" />
		        <g:set var="dmTotal" value="${dmTotal+unpaidDm}"/>
          	</td>
          	<td class="right ${redText}" onclick="window.location='${createLink(uri:'/')}bouncedCheck/unpaidList?customerId=${customerAccountInstance?.customer?.id}'">
          		<g:set var="unpaidBc" value="${unpaidBcs[customerAccountInstance.customer?.id]?unpaidBcs[customerAccountInstance.customer?.id]:0}"/>
          		PHP <g:formatNumber number="${unpaidBc}" format="###,##0.00" />
		        <g:set var="bcTotal" value="${bcTotal+unpaidBc}"/>
          	</td>
          	<td class="right ${redText}" onclick="window.location='${createLink(uri:'/')}checkPayment/unpaidList?customerId=${customerAccountInstance?.customer?.id}'">
          		<g:set var="unpaidCp" value="${unpaidCps[customerAccountInstance.customer?.id]?unpaidCps[customerAccountInstance.customer?.id]:0}"/>
          		PHP <g:formatNumber number="${unpaidCp}" format="###,##0.00" />
		        <g:set var="pcTotal" value="${pcTotal+unpaidCp}"/>
          	</td>
          	<td class="right ${redText}" >
          		PHP <g:formatNumber number="${unpaidSd + unpaidCc + unpaidDm + unpaidBc + unpaidCp}" format="###,##0.00" />
            </td>
          </tr>
	        <g:set var="bTotal" value="${bTotal+unpaidSd + unpaidCc + unpaidDm + unpaidBc + unpaidCp}"/>
        </g:each>
        <g:ifAnyGranted role="ROLE_SUPER,ROLE_MANAGER_ACCOUNTING">
        	<g:if test="${!params.dateRange || params.withTotal=='Y'}">
          <tr>
          	<td><strong>Total</strong></td>
          	<td></td>
          	<td></td>
          	<td></td>
          	<td class="right"><strong>PHP <g:formatNumber number="${sdTotal}" format="###,##0.00" /></strong></td>
          	<td class="right"><strong>PHP <g:formatNumber number="${ccTotal}" format="###,##0.00" /></strong></td>
          	<td class="right"><strong>PHP <g:formatNumber number="${dmTotal}" format="###,##0.00" /></strong></td>
          	<td class="right"><strong>PHP <g:formatNumber number="${bcTotal}" format="###,##0.00" /></strong></td>
          	<td class="right"><strong>PHP <g:formatNumber number="${pcTotal}" format="###,##0.00" /></strong></td>
          	<td class="right"><strong>PHP <g:formatNumber number="${bTotal}" format="###,##0.00" /></strong></td>
          </tr>
          	</g:if>
        </g:ifAnyGranted>
        </tbody>
      </table>
    </div>
  </div>
  <script>
		$(document).ready( function () {
			var oTable = $('#list').dataTable({
		        "bPaginate": false,
		        "bLengthChange": false,
		        "bFilter": false,
		        "bSort": false,
		        "bInfo": false,
		        "bAutoWidth": false
		    } );
			new FixedHeader( oTable );
		} );
  </script>
</body>
</html>