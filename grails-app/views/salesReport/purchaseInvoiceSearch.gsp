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
    <h1>Purchases</h1>
    <div class="dialog">
        <g:form action="purchaseInvoiceList" >
            <h2>Filters</h2>

            <input type="hidden" name="model" value="PurchaseOrder">
            <table>
                <tr class="prop">
                    <td class="name">Report Type</td>
                    <td class="value">
                        <select name="reportType">
                            <option value="purchaseInvoiceSupplier">By Supplier</option>
                            <option value="purchaseInvoiceSupplierDetailed">By Supplier (Detailed)</option>
                        </select>
                    </td>
                </tr>
                <tr class="prop">
                    <td class="name"><g:radio name="dateType" value="invoice" checked="checked"/> Invoice Date</td>
                    <td class="value">
                        From <g:datePicker name="invoiceDateFrom" precision="day" noSelection="['': '']" />
                        To <g:datePicker name="invoiceDateTo" precision="day" noSelection="['': '']" />
                    </td>
                </tr>
                <tr class="prop">
                    <td class="name"><g:radio  name="dateType" value="delivery"/> Delivery Date</td>
                    <td class="value">
                        From <g:datePicker name="deliveryDateFrom" precision="day" noSelection="['': '']" />
                        To <g:datePicker name="deliveryDateTo" precision="day" noSelection="['': '']" />
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
                    <td class="name">Warehouse</td>
                    <td class="value">
                        <input type="checkbox" class="checkAll" onclick="checkAll('warehouse', this.checked)" id="warehouseCheckAll"> <label for="warehouseCheckAll">Check All</label>
                        <div class="multicheckbox">
                            <g:each in="${warehouses}" var="warehouse">
                                <div class="checkbox"><input class="warehouseCheckbox" type="checkbox" name="warehouse.id" id="warehouse${warehouse.id}" value="${warehouse.id}"> <label for="warehouse${warehouse.id}">${warehouse}</label></div>
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

            </table>

            <div>
                <input type="submit" class="button" value="Run"/>
            </div>

        </g:form>
    </div>
</div>
</body>
</html>
