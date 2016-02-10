
<%@ page import="com.munix.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
  <g:set var="entityName" value="${message(code: 'salesInvoice.label', default: 'Sales Order')}" />
  <title>Customer Report</title>
  <script>
  	function updateReportType(reportType){
  		document.searchForm.action=reportType;
  	}
  	
  	$(document).ready(function(){
  		updateReportType(document.searchForm.reportType.value);
  	});
  </script>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1>Customer Report</h1>
    <div class="dialog">
      <g:form name="searchForm" method="post">
      <h2>Filters</h2>
      
        <table>
          <tr class="prop">
            <td class="name">Report Type</td>
            <td class="value">
	          <select name="reportType" onchange="updateReportType(this.value)">
                <g:each in="${reportTypes}" status="i" var="reportType">
		          	<option value="${reportType}">${reportTypeLabels[i]}</option>
	          	</g:each>
	          </select>
            </td>
          </tr>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Customer','name':'customer','field':'id','list':com.munix.Customer.findAll([sort:'name'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Sales Agent','name':'salesAgent','field':'salesAgent.id','list':com.munix.SalesAgent.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Customer Type','name':'type','field':'type.id','list':com.munix.CustomerType.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Discount Type','name':'discountType','field':'discountType.id','list':com.munix.DiscountType.findAll([sort:'identifier'])]}"/>
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
          <tr class="prop">
            <td class="name">Top customer until</td>
            <td class="value">
	          From <g:datePicker name="dateFrom" precision="day" noSelection="['': '']" />
	          To <g:datePicker name="dateTo" precision="day" noSelection="['': '']" />
            </td>
          </tr>
        </table>
      
        <div>
          <input type="submit" class="button" value="Run"/>
        </div>

      </g:form>
    </div>
  </div>
</body>
</html>
