
<%@ page import="com.munix.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
  <g:set var="entityName" value="${message(code: 'salesInvoice.label', default: 'Sales Order')}" />
  <title>Year End Accounting Report</title>
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
    <h1>Year End Accounting Report</h1>
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
          <tr class="prop">
            <td class="name">Date</td>
            <td class="value">
	          From <g:datePicker name="dateFrom" precision="day" noSelection="['': '']" />
	          To <g:datePicker name="dateTo" precision="day" noSelection="['': '']" />
            </td>
          </tr>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Customer Name','name':'customer','field':'customer.id','list':com.munix.Customer.findAll([sort:'name'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Customer Type','name':'type','field':'type.id','list':com.munix.CustomerType.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Discount Type','name':'discountType','field':'discountType.id','list':com.munix.DiscountType.findAll([sort:'identifier'])]}"/>
          <tr class="prop">
            <td class="name">Sorting</td>
            <td class="value">
	          <select name="sortBy">
		          <option value="name">Customer Name</option>
		          <option value="balance">Balance</option>
		          <option value="pending_checks">Pending Checks</option>
		          <option value="net_balance">Net Balance</option>
	          </select>
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
