
<%@ page import="com.munix.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
  <g:set var="entityName" value="${message(code: 'salesInvoice.label', default: 'Sales Order')}" />
  <title>Sales Report</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1>Sales Order Summary Report</h1>
    <div class="dialog">
      <g:form action="pendingList" >
      <h2>Filters</h2>
      
      <input type="hidden" name="model" value="SalesOrder">
        <table>
          <tr class="prop">
            <td class="name">Report Type</td>
            <td class="value">
	          <select name="reportType">
	          	<option value="product">By Product</option>
	          	<option value="customer">By Customer</option>
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
          <tr class="prop">
            <td class="name">Customer</td>
            %{--<td class="value">--}%
	          %{--<g:select name="customer.id" from="${customers}" optionKey="id" value="${salesOrderInstance?.customer?.id}" noSelection="['':'All']"/>--}%
            %{--</td>--}%
            <td class="value">
              <input type="checkbox" class="checkAll" onclick="checkAll('customer', this.checked)" id="customerCheckAll"> <label for="customerCheckAll">Check All</label>
              <div class="multicheckbox">
                  <g:each in="${customers}" var="customer">
                      <div class="checkbox"><input class="customerCheckbox" type="checkbox" name="customer.id" id="customer${customer?.id}" value="${customer?.id}"> <label for="customer${customer?.id}">${customer}</label></div>
                  </g:each>
              </div>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Discount Type</td>
            %{--<td class="value">--}%
          		%{--<g:select name="discountType.id" from="${discountTypes}" optionKey="id" value="${salesOrderInstance?.discountType?.id}"  noSelection="['':'All']"/>--}%
          	%{--</td>--}%
            <td class="value">
              <input type="checkbox" class="checkAll" onclick="checkAll('discountType', this.checked)" id="discountTypeCheckAll"> <label for="discountTypeCheckAll">Check All</label>
              <div class="multicheckbox">
                  <g:each in="${discountTypes}" var="discountType">
                      <div class="checkbox"><input class="discountTypeCheckbox" type="checkbox" name="discountType.id" id="discountType${discountType?.id}" value="${discountType?.id}"> <label for="discountType${discountType?.id}">${discountType}</label></div>
                  </g:each>
              </div>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Customer Type</td>
            %{--<td class="value">--}%
          		%{--<g:select name="customer.type.id" from="${customerTypes}" optionKey="id" value="${salesOrderInstance?.customer?.type?.id}"  noSelection="['':'All']"/>--}%
          	%{--</td>--}%
            <td class="value">
              <input type="checkbox" class="checkAll" onclick="checkAll('customerType', this.checked)" id="customerTypeCheckAll"> <label for="customerTypeCheckAll">Check All</label>
              <div class="multicheckbox">
                  <g:each in="${customerTypes}" var="customerType">
                      <div class="checkbox"><input class="customerTypeCheckbox" type="checkbox" name="customerType.id" id="customerType${customerType.id}" value="${customerType.id}"> <label for="customerType${customerType.id}">${customerType}</label></div>
                  </g:each>
              </div>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Product</td>
            %{--<td class="value">--}%
	          %{--<g:select name="product.id" from="${products}" optionKey="id" noSelection="['':'All']"/>--}%
            %{--</td>--}%
            <td class="value">
              <input type="checkbox" class="checkAll" onclick="checkAll('product', this.checked)" id="productCheckAll"> <label for="productCheckAll">Check All</label>
              <div class="multicheckbox">
                  <g:each in="${products}" var="product">
                      <div class="checkbox"><input class="productCheckbox" type="checkbox" name="product.id" id="product${product.id}" value="${product.id}"> <label for="product${product.id}">${product}</label></div>
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
            	<input type="checkbox" class="checkAll" onclick="checkAll('category', this.checked)" id="categoryCheckAll"> <label for="categoryCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${itemTypes}" var="itemType">
	            		<div class="checkbox"><input type="checkbox" name="product.itemType.id" id="itemType${itemType.id}" value="${itemType.id}"> <label for="itemType${itemType.id}">${itemType}</label></div>
            		</g:each>
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
