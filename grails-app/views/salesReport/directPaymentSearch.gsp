
<%@ page import="com.munix.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
  <g:set var="entityName" value="${message(code: 'salesInvoice.label', default: 'Direct Payment')}" />
  <title>Direct Payment Report</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1>Direct Payment Report</h1>
    <div class="dialog">
      <g:form action="directPaymentList" >
      <h2>Filters</h2>
      
      <input type="hidden" name="model" value="DirectPayment">
        <table>
          <tr class="prop">
            <td class="name">Report Type</td>
            <td class="value">
	          <select name="reportType">
	          	<option value="directPaymentSeries">By Series</option>
	          	<option value="directPaymentCustomer">By Customer</option>
	          	<option value="directPaymentType">By Payment Type</option>
	          	<option value="directPaymentSalesAgent">By Salesman</option>
	          	<%--<option value="directPaymentSalesAgentDetailed">By Salesman (Detailed)</option> --%>
	          </select>
            </td>
          </tr>
          <g:ifAnyGranted role="ROLE_MANAGER_ACCOUNTING">
          <tr class="prop">
            <td class="name">Hide Total</td>
            <td class="value"><input type="checkbox" name="totalHidden" value="Y" checked="checked"></td>
          </tr>
          </g:ifAnyGranted>
          <tr class="prop">
            <td class="name">Date</td>
            <td class="value">
	          From <g:datePicker name="dateFrom" precision="day" noSelection="['': '']" />
	          To <g:datePicker name="dateTo" precision="day" noSelection="['': '']" />
            </td>
          </tr>
			<g:render template="/salesReport/multicheckFilter" model="${['label':'Customer','name':'customer','field':'customer.id','list':customers]}"/>
			<g:render template="/salesReport/multicheckFilter" model="${['label':'Discount Type','name':'discountType','field':'discountType.id','list':discountTypes]}"/>
			<g:render template="/salesReport/multicheckFilter" model="${['label':'Customer Type','name':'customerType','field':'customerType.id','list':customerTypes]}"/>
			<g:render template="/salesReport/multicheckFilter" model="${['label':'Payment Type','name':'paymentType','field':'paymentType.id','list':paymentTypes]}"/>
			<g:render template="/salesReport/multicheckFilter" model="${['label':'Sales Agent','name':'salesAgent','field':'salesAgent.id','list':salesAgents]}"/>
        </table>
      
        <div>
          <input type="submit" class="button" value="Run"/>
        </div>

      </g:form>
    </div>
  </div>
</body>
</html>
