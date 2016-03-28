
<%@ page import="com.munix.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
  <g:set var="entityName" value="${message(code: 'salesInvoice.label', default: 'Sales Order')}" />
  <title>Assembly Report</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1>Assembly Report</h1>
    <div class="dialog">
      <g:form name="searchForm" method="post" action="list">
      <h2>Filters</h2>
      
        <table>
          <tr class="prop">
            <td class="name">Date</td>
            <td class="value">
	          From <g:datePicker name="dateFrom" precision="day" noSelection="['': '']" />
	          To <g:datePicker name="dateTo" precision="day" noSelection="['': '']" />
            </td>
          </tr>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Product','name':'product','field':'product.id','list':com.munix.Product.findAll([sort:'identifier'])]}"/>
          <tr class="prop">
            <td class="name">Status</td>
            <td class="value">
	          <select name="status">
	          	<option value="">All</option>
	          	<option value="Active">Active</option>
	          	<option value="Inactive">Inactive</option>
	          </select>
            </td>
          </tr>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Item Type','name':'itemType','field':'itemType.id','list':com.munix.ItemType.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Brand','name':'brand','field':'brand.id','list':com.munix.ProductBrand.findAll([sort:'identifier'])]}"/>
        </table>
      
        <div>
          <input type="submit" class="button" value="Run"/>
        </div>

      </g:form>
    </div>
  </div>
</body>
</html>
