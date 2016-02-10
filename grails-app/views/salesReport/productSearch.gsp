
<%@ page import="com.munix.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
  <g:set var="entityName" value="${message(code: 'salesInvoice.label', default: 'Sales Order')}" />
  <title>Product Report</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1>Product Report</h1>
    <div class="dialog">
      <g:form action="productList" method="post" onsubmit="return filterChecker()">
      <h2>Filters</h2>
      
        <table>
          <tr class="prop">
            <td class="name">Report Type</td>
            <td class="value">
	          <select name="reportType">
                <g:each in="${reportTypes}" status="i" var="reportType">
		          	<option value="${reportType}">${reportTypeLabels[i]}</option>
	          	</g:each>
	          </select>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Wholesale/Retail</td>
            <td class="value">
	          <select name="priceType">
	          	<option value="Wholesale" selected>Wholesale</option>
	          	<option value="Retail">Retail</option>
	          </select>
            </td>
          </tr>
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
          <%--
          <tr class="prop">
            <td class="name">Currency</td>
            <td class="value">
	          <g:select name="currency" from="${CurrencyType.list()}" optionKey="id" optionValue="${{it.identifier+ ' - ' +it.description}}"/>
            </td>
          </tr>
           --%>
          <tr class="prop">
            <td class="name">Balance</td>
            <td class="value">
            	<g:select name="balance" id="balance" from="${['<0','>0','=0'] }" noSelection="${['null':'']}"/>
            	&nbsp;Filter from Warehouse: <g:select name="warehouseBalanceFilter" id="warehouseBalanceFilter" optionKey="id" from="${com.munix.Warehouse.findAll([sort:'identifier'])}" noSelection="${['null':'']}"/>
            </td>
          </tr>
         <%--
         <tr class="prop">
            <td class="name">Balance</td>
            <td class="value">
	           <input type="checkbox" name="balance" value="Y">
            </td>
          </tr>
          --%>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Warehouse','name':'warehouse','field':'warehouse.id','list':com.munix.Warehouse.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Product','name':'product','field':'id','list':com.munix.Product.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Discount Type','name':'discountType','field':'type.id','list':com.munix.DiscountType.findAll([sort:'description'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Category','name':'category','field':'category.id','list':com.munix.ProductCategory.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Subcategory','name':'subcategory','field':'subcategory.id','list':com.munix.ProductSubcategory.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Brand','name':'brand','field':'brand.id','list':com.munix.ProductBrand.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Model','name':'model','field':'model.id','list':com.munix.ProductModel.findAll([sort:'identifier'])]}"/>
		<g:render template="/salesReport/multicheckFilter" model="${['label':'Item Type','name':'itemType','field':'itemType.id','list':com.munix.ItemType.findAll([sort:'identifier'])]}"/>
        </table>
      
        <div>
          <input type="submit" class="button" value="Run"/>
        </div>

      </g:form>
    </div>
  </div>
  <script>
  function filterChecker(){
	if($("#balance").val()!='null'){
		if($("#warehouseBalanceFilter").val()=='null'){
			alert("Please select warehouse filter for balance.")
			return false;
		}
		else
			return true; 
	}else if($("#warehouseBalanceFilter").val()!='null'){
		if($("#balance").val()=='null'){
			alert("Please select a balance range.")
			return false;
		}
		else
			return true; 
	}else
		return true;
  }
  </script>
</body>
</html>
