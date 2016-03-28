
<%@ page import="com.munix.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
  <g:set var="entityName" value="${message(code: 'salesInvoice.label', default: 'Sales Order')}" />
  <title>Sales Report</title>
  <script>
  	function changeReportType(reportType){
  		if(reportType=="salesReportByMonthComparison"){
  			$("#customerTypeRow").show()
  		}else{
  			$("#customerTypeRow").hide()
  		}
  	}
  </script>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1>Sales Report</h1>
    <div class="dialog">
      <g:form action="list" method="get">
      <h2>Filters</h2>
      
      <input type="hidden" name="model" value="SalesDelivery">
          <g:ifNotGranted role="ROLE_MANAGER_ACCOUNTING">
              <input type="hidden" name="totalHidden" value="Y">
          </g:ifNotGranted>
        <table>
          <tr class="prop">
            <td class="name">Report Type</td>
            <td class="value">
	          <select name="reportType" onchange="changeReportType(this.value);">
	          	<g:ifAllGranted role="ROLE_ACCOUNTING">
	          		<option value="series">By Series</option>
	          	</g:ifAllGranted>
	          	<g:ifAllGranted role="ROLE_MANAGER_ACCOUNTING">
	          		<option value="itemsSold">By Items Sold</option>
	          	</g:ifAllGranted>
	          	<g:ifAllGranted role="ROLE_MANAGER_SALES">
	          		<option value="itemsSoldDetailed">By Items Sold (Detailed)</option>
	          	</g:ifAllGranted>
	          	<g:ifAllGranted role="ROLE_MANAGER_ACCOUNTING">
	          		<%--<option value="date">By Date</option> --%>
	          		<option value="salesReportCustomerType">Customer Type</option>
	          	</g:ifAllGranted>
	          	<g:ifAllGranted role="ROLE_MANAGER_ACCOUNTING">
	          		<option value="salesReportDiscountType">Discount Type</option>
	          	</g:ifAllGranted>
	          	<g:ifAllGranted role="ROLE_MANAGER_ACCOUNTING">
	          		<option value="salesReportCustomerDiscountType">Customer Type + Discount Type</option>
	          	</g:ifAllGranted>
	          	<g:ifAllGranted role="ROLE_MANAGER_ACCOUNTING">
	          		<option value="salesReportByMonthComparison">By Month Comparison</option>
	          	</g:ifAllGranted>
	          	<g:ifAllGranted role="ROLE_MANAGER_ACCOUNTING">
	          		<option value="salesDeliveryMargin">SD Margin (Summary)</option>
	          	</g:ifAllGranted>
	          	<g:ifAllGranted role="ROLE_MANAGER_ACCOUNTING">
	          		<option value="salesDeliveryMarginDetailed">SD Margin (Detailed)</option>
	          	</g:ifAllGranted>
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
          <tr class="prop">
            <td class="name">Outstanding Balance</td>
            <td class="value"><input type="checkbox" name="balance" value="Y"></td>
          </tr>
          <tr class="prop">
            <td class="name">Net</td>
            <td class="value"><input type="checkbox" name="isNet" value="Y"></td>
          </tr>
          <tr class="prop">
            <td class="name">For Sale</td>
            <td class="value"><input type="checkbox" name="isForSale" value="Y"></td>
          </tr>
          <tr class="prop">
            <td class="name">Component</td>
            <td class="value"><input type="checkbox" name="isComponent" value="Y"></td>
          </tr>
          <tr class="prop">
            <td class="name">Assembly</td>
            <td class="value"><input type="checkbox" name="isForAssembly" value="Y"></td>
          </tr>
          <tr class="prop">
            <td class="name">Status</td>
            <td class="value">
          		<!--<select id="searchStatus" name="searchStatus">
					<option value="">Select One...</option>
					<option value="Unapproved">Unapproved</option>
					<option value="Unpaid">Unpaid</option>
					<option value="Taken">Taken</option>
					<option value="Closed">Closed</option>
					<option value="Cancelled">Cancelled</option>
					<option value="Paid">Paid</option>
				</select>-->
				<input type="checkbox" class="checkAll" onclick="checkAll('searchStatus', this.checked)" id="searchStatusCheckAll"> <label for="searchStatusCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<%--<g:each in="${productTypes}" var="productType"> --%>
            		<div class="checkbox"><input class="searchStatusCheckbox" type="checkbox" name="searchStatus" id="searchStatus1" value="Unapproved"> <label for="searchStatus1">Unapproved</label></div>
            		<div class="checkbox"><input class="searchStatusCheckbox" type="checkbox" name="searchStatus" id="searchStatus2" value="Unpaid"> <label for="searchStatus2">Unpaid</label></div>
            		<div class="checkbox"><input class="searchStatusCheckbox" type="checkbox" name="searchStatus" id="searchStatus3" value="Taken"> <label for="searchStatus3">Taken</label></div>
            		<div class="checkbox"><input class="searchStatusCheckbox" type="checkbox" name="searchStatus" id="searchStatus4" value="Closed"> <label for="searchStatus4">Closed</label></div>
            		<div class="checkbox"><input class="searchStatusCheckbox" type="checkbox" name="searchStatus" id="searchStatus4" value="Cancelled"> <label for="searchStatus4">Cancelled</label></div>
            		<div class="checkbox"><input class="searchStatusCheckbox" type="checkbox" name="searchStatus" id="searchStatus4" value="Paid"> <label for="searchStatus4">Paid</label></div>
            		<%--</g:each> --%>
            	</div>
          	</td>
          </tr>
          <tr class="prop" id="customerTypeRow" style="display:none">
            <td class="name">Customer Type</td>
            <td class="value">
          		<input type="checkbox" class="checkAll" onclick="checkAll('customerType', this.checked)" id="customerTypeCheckAll"> <label for="customerTypeCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${com.munix.CustomerType.list().sort{it.description}}" var="customerType">
            		<div class="checkbox"><input class="customerTypeCheckbox" type="checkbox" name="customerType.id" id="customerType${customerType?.id}" value="${customerType?.id}"> <label for="customerType${customerType?.id}">${customerType.description}</label></div>
            		</g:each>
            	</div>
          	</td>
          </tr>
          <tr class="prop">
            <td class="name">Discount Type</td>
            <td class="value">
          		<%--<g:select name="productType.id" from="${productTypes}" optionKey="id" value="${salesInvoiceInstance?.productType?.id}"  noSelection="['':'All']"/> --%>
          		<input type="checkbox" class="checkAll" onclick="checkAll('productType', this.checked)" id="productTypeCheckAll"> <label for="productTypeCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${productTypes}" var="productType">
            		<div class="checkbox"><input class="productTypeCheckbox" type="checkbox" name="productType.id" id="productType${productType?.id}" value="${productType?.id}"> <label for="productType${productType?.id}">${productType.identifier}</label></div>
            		</g:each>
            	</div>
          	</td>
          </tr>
          <tr class="prop">
            <td class="name">Customer</td>
            <td class="value">
	          <%-- <g:select name="customer.id" from="${customers}" optionKey="id" value="${salesInvoiceInstance?.customer?.id}" noSelection="['':'All']"/>--%>
            	<input type="checkbox" class="checkAll" onclick="checkAll('customer', this.checked)" id="customerCheckAll"> <label for="customerCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${customers}" var="customer">
            		<div class="checkbox"><input class="customerCheckbox" type="checkbox" name="customer.id" id="customer${customer?.id}" value="${customer?.id}"> <label for="customer${customer?.id}">${customer.name}</label></div>
            		</g:each>
            	</div>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Salesman</td>
            <td class="value">
	          <%-- <g:select name="salesAgent.id" from="${salesAgents}" optionKey="id" noSelection="['':'All']"/>--%>
	          <input type="checkbox" class="checkAll" onclick="checkAll('salesAgent', this.checked)" id="salesAgentCheckAll"> <label for="salesAgentCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${salesAgents}" var="salesAgent">
            		<div class="checkbox"><input class="salesAgentCheckbox" type="checkbox" name="salesAgent.id" id="salesAgent${salesAgent?.id}" value="${salesAgent?.id}"> <label for="salesAgent${salesAgent?.id}">${salesAgent.identifier}</label></div>
            		</g:each>
            	</div>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Product</td>
            <td class="value">
	          <%--<g:select name="product.id" from="${products}" optionKey="id" noSelection="['':'All']"/> --%>
	          <input type="checkbox" class="checkAll" onclick="checkAll('product', this.checked)" id="productCheckAll"> <label for="productCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${products}" var="product">
            		<div class="checkbox"><input class="productCheckbox" type="checkbox" name="product.id" id="product${product?.id}" value="${product?.id}"> <label for="product${product?.id}">${product}</label></div>
            		</g:each>
            	</div>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Category</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('category', this.checked)" id="categoryCheckAll"> <label for="categoryCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${categories}" var="category">
            		<div class="checkbox"><input class="categoryCheckbox" type="checkbox" name="product.category.id" id="category${category.id}" value="${category.id}"> <label for="category${category.id}">${category}</label></div>
            		</g:each>
            	</div>
            </td>
          </tr>

          <tr class="prop">
            <td class="name">Subcategory</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('subcategory', this.checked)" id="categoryCheckAll"> <label for="categoryCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${subcategories}" var="subcategory">
            		<div class="checkbox"><input class="subcategoryCheckbox" type="checkbox" name="product.subcategory.id" id="subcategory${subcategory.id}" value="${subcategory.id}"> <label for="subcategory${subcategory.id}">${subcategory}</label></div>
            		</g:each>
            	</div>
            </td>
          </tr>

          <tr class="prop">
            <td class="name">Brand</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('brand', this.checked)" id="categoryCheckAll"> <label for="categoryCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${brands}" var="brand">
	            		<div class="checkbox"><input class="brandCheckbox" type="checkbox" name="product.brand.id" id="brand${brand.id}" value="${brand.id}"> <label for="brand${brand.id}">${brand}</label></div>
            		</g:each>
            	</div>
            </td>
          </tr>

          <tr class="prop">
            <td class="name">Model</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('model', this.checked)" id="categoryCheckAll"> <label for="categoryCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${models}" var="model">
	            		<div class="checkbox"><input class="modelCheckbox" type="checkbox" name="product.model.id" id="model${model.id}" value="${model.id}"> <label for="model${model.id}">${model}</label></div>
            		</g:each>
            	</div>
            </td>
          </tr>

          <tr class="prop">
            <td class="name">Item Type</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('itemType', this.checked)" id="itemTypeCheckAll"> <label for="itemTypeCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${itemTypes}" var="itemType">
	            		<div class="checkbox"><input class="itemTypeCheckbox" type="checkbox" name="product.itemType.id" id="itemType${itemType.id}" value="${itemType.id}"> <label for="itemType${itemType.id}">${itemType}</label></div>
            		</g:each>
            	</div>
            </td>
          </tr>
          
          <tr class="prop">
            <td class="name">Material</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('material', this.checked)" id="materialCheckAll"> <label for="materialCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${materials}" var="material">
	            		<div class="checkbox"><input class="materialCheckbox" type="checkbox" name="material.id" id="material${material.id}" value="${material.id}"> <label for="itemType${material.id}">${material}</label></div>
            		</g:each>
            	</div>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Color</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('color', this.checked)" id="colorCheckAll"> <label for="colorCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${colors}" var="color">
	            		<div class="checkbox"><input class="colorCheckbox" type="checkbox" name="color.id" id="color${color.id}" value="${color.id}"> <label for="itemType${color.id}">${color}</label></div>
            		</g:each>
            	</div>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Price Type</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('priceType', this.checked)" id="priceTypeCheckAll"> <label for="priceTypeCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<div class="checkbox"><input class="priceTypeCheckbox" type="checkbox" name="priceType" id="priceType1" value="RETAIL"> <label for="priceType1">Retail</label></div>
            		<div class="checkbox"><input class="priceTypeCheckbox" type="checkbox" name="priceType" id="priceType2" value="WHOLESALE"> <label for="priceType2">Wholesale</label></div>
            	</div>
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
