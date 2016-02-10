
<%@ page import="com.munix.*" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript library="reports/filter" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'filter.css')}" />
    <g:set var="entityName" value="${message(code: 'salesInvoice.label', default: 'Sales Order')}" />
    <title>Credit Memo Report</title>
</head>
<body>
<div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
</div>
<div class="body">
<h1>Credit Memo Report</h1>
<div class="dialog">
<g:form action="creditMemoList" method="post">
<h2>Filters</h2>
<table>
<tr class="prop">
    <td class="name">Date</td>
    <td class="value">
        From <g:datePicker name="dateFrom" precision="day" noSelection="['': '']" />
        To <g:datePicker name="dateTo" precision="day" noSelection="['': '']" />
    </td>
</tr>
<tr class="prop">
    <td class="name">Customer</td>
    <td class="value">
        <%--<g:select name="product.id" from="${products}" optionKey="id" noSelection="['':'All']"/> --%>
        <input type="checkbox" class="checkAll" onclick="checkAll('customer', this.checked)" id="customerCheckAll"> <label for="customerCheckAll">Check All</label>
        <div class="multicheckbox">
            <g:each in="${customers}" var="customer">
                <div class="checkbox"><input class="customerCheckbox" type="checkbox" name="customer.id" id="customer${customer?.id}" value="${customer?.id}"> <label for="customer${customer?.id}">${customer}</label></div>
            </g:each>
        </div>
    </td>
</tr>
<tr class="prop">
    <td class="name">Reason</td>
    <td class="value">
        <%--<g:select name="product.id" from="${products}" optionKey="id" noSelection="['':'All']"/> --%>
        <input type="checkbox" class="checkAll" onclick="checkAll('reason', this.checked)" id="reasonCheckAll"> <label for="reasonCheckAll">Check All</label>
        <div class="multicheckbox">
            <g:each in="${reasons}" var="reason">
                <div class="checkbox"><input class="reasonCheckbox" type="checkbox" name="reason.id" id="reason${reason?.id}" value="${reason?.id}"> <label for="reason${reason?.id}">${reason}</label></div>
            </g:each>
        </div>
    </td>
</tr>
%{--<tr class="prop">--}%
    %{--<td class="name">Outstanding Balance</td>--}%
    %{--<td class="value"><input type="checkbox" name="balance" value="Y"></td>--}%
%{--</tr>--}%
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
</table>

<div>
    <input type="submit" class="button" value="Run"/>
</div>

</g:form>
</div>
</div>
</body>
</html>
