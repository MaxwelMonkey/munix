<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
    <g:set var="entityName" value="${message(code: 'purchases.label', default: 'Purchases')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <style type="text/css" media="print,screen">
    @media print {
        thead {display: table-header-group;}
    }

    div.list table tr td{
        border-left:0px;
        border-right:0px;
    }

    div.list table tr th{
        background-color:white;
        color:black;
        border-top:0px;
        border-left:0px;
        border-right:0px;
    }

    div.list table{
        border-left:0px;
        border-right:0px;
    }
    .right{
        text-align:right;
    }
    </style>
</head>
<body>
<div class="list">
<table>
    <g:set var="grandTotalCurr" value="${[:]}"/>
    <g:set var="grandTotalPHP" value="${0}"/>
    <g:each in="${suppliers}" var="supplier">
    <tr>
        <th colspan="99">
            ${supplier}
        </th>
    </tr>
    <tr>
        <th>Date (Invoice)</th>
        <th>Date (Delivery)</th>
        <th>Reference #</th>
        <th>Supplier Reference #</th>
        <th class="right">Amount in currency</th>
        <th class="right">Ex. Rate</th>
        <th class="right">Amount in PHP</th>
    </tr>
    <g:set var="totalAmountCurr" value="${0}"/>
    <g:set var="totalAmountPHP" value="${0}"/>
        <g:each in="${result[supplier]}" var="purchaseInvoice">
            <g:set var="totalAmountCurr" value="${totalAmountCurr+purchaseInvoice?.computeForeignAmountTotal()}"/>
            <g:set var="totalAmountPHP" value="${totalAmountPHP+purchaseInvoice?.computePurchaseInvoicePhpTotal()}"/>
            <tr>
                <td><g:formatDate date="${purchaseInvoice?.invoiceDate}" format="MM/dd/yyyy"/></td>
                <td><g:formatDate date="${purchaseInvoice?.deliveryDate}" format="MM/dd/yyyy"/></td>
                <td>${purchaseInvoice?.reference}</td>
                <td>${purchaseInvoice?.supplierReference}</td>
                <td class="right">${purchaseInvoice?.supplier?.currency} <g:formatNumber number="${purchaseInvoice?.computeForeignAmountTotal()}" format="###,##0.00" /></td>
                <td class="right">${purchaseInvoice?.exchangeRate}</td>
                <td class="right"><g:formatNumber number="${purchaseInvoice?.computePurchaseInvoicePhpTotal()}" format="PHP ###,##0.00" /></td>
            </tr>
	        <%
	        	Double currencyTotal = grandTotalCurr[purchaseInvoice?.supplier?.currency];
	        	if(!currencyTotal) currencyTotal = 0;
	        	currencyTotal += purchaseInvoice?.computeForeignAmountTotal();
	        	grandTotalCurr[purchaseInvoice?.supplier?.currency] = currencyTotal;
	        %>
        </g:each>
        <g:set var="grandTotalPHP" value="${grandTotalPHP+totalAmountPHP}"/>
        <tr>
            <td></td><td></td><td></td>
            <th class="right">Total</th>
            <th class="right"><g:formatNumber number="${totalAmountCurr}" format="###,##0.00" /></th>
            <th></th>
            <th class="right"><g:formatNumber number="${totalAmountPHP}" format="PHP ###,##0.00" /></th>
        </tr>
    </g:each>
        <tr>
            <td></td><td></td><td></td>
            <th class="right">Grand Total</th>
            <th class="right">
            <g:each in="${grandTotalCurr.keySet()}" var="key">
            ${key} <g:formatNumber number="${grandTotalCurr[key]}" format="###,##0.00" /><br>
            </g:each>
            </th>
            <th></th>
            <th class="right"><g:formatNumber number="${grandTotalPHP}" format="PHP ###,##0.00" /></th>
        </tr>
        </table>
</div>
</body>
</html>
