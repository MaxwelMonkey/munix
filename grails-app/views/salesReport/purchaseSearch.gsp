
<%@ page import="com.munix.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
  <g:set var="entityName" value="${message(code: 'salesInvoice.label', default: 'Purchase Order')}" />
  <title>Pending Purchase Orders Report</title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  </div>
  <div class="body">
    <h1>Pending Purchase Orders</h1>
    <div class="dialog">
      <g:form action="purchaseList" >
      <h2>Filters</h2>
      
      <input type="hidden" name="model" value="PurchaseOrder">
        <table>
          <tr class="prop">
            <td class="name">Report Type</td>
            <td class="value">
	          <select name="reportType">
	          	<option value="purchaseProduct">By Product</option>
	          	<option value="purchaseSupplier">By Supplier</option>
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
            <td class="name">Supplier</td>
            <td class="value">
            	<input type="checkbox" class="checkAll" onclick="checkAll('supplier', this.checked)" id="supplierCheckAll"> <label for="supplierCheckAll">Check All</label>
            	<div class="multicheckbox">
            		<g:each in="${suppliers}" var="supplier">
            		<div class="checkbox"><input class="supplierCheckbox" type="checkbox" name="supplier.id" id="supplier${supplier.id}" value="${supplier.id}"> <label for="supplier${supplier.id}">${supplier}</label></div>
            		</g:each>
            	</div>
            </td>
          </tr>
          <tr class="prop">
            <td class="name">Product</td>
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
