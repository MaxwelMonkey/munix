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
        <th>Product Code</th>
        <th>Product Description</th>
        <th class="right">Quantity</th>
        <th class="right">Unit Price</th>
        <th class="right">Amount</th>
        <th class="right">Ex. Rate</th>
        <th class="right">Amount in PHP</th>
    </tr>
    <g:set var="totalAmountCurr" value="${0}"/>
    <g:set var="totalAmountPHP" value="${0}"/>
        <g:each in="${result[supplier]}" var="purchaseInvoiceItem">
            <g:set var="totalAmountCurr" value="${totalAmountCurr+=purchaseInvoiceItem.finalPrice * purchaseInvoiceItem.qty}"/>
            <g:set var="totalAmountPHP" value="${totalAmountPHP+=purchaseInvoiceItem.finalPrice * purchaseInvoiceItem.qty * (purchaseInvoiceItem.purchaseInvoice.exchangeRate?:0)}"/>
            <tr>
                <td><g:formatDate date="${purchaseInvoiceItem.purchaseInvoice?.invoiceDate}" format="MM/dd/yyyy"/></td>
                <td><g:formatDate date="${purchaseInvoiceItem.purchaseInvoice?.deliveryDate}" format="MM/dd/yyyy"/></td>
                <td>${purchaseInvoiceItem.purchaseInvoice?.reference}</td>
                <td>${purchaseInvoiceItem.purchaseInvoice?.supplierReference}</td>
                <td>${purchaseInvoiceItem.purchaseOrderItem?.product?.identifier}</td>
                <td>${purchaseInvoiceItem.purchaseOrderItem?.product?.getDescription()}</td>
                <td><g:formatNumber number="${purchaseInvoiceItem.qty}" format="###,##0" /></td>
                <td><g:formatNumber number="${purchaseInvoiceItem.finalPrice}" format="###,##0.00" /></td>
                <td class="right">${purchaseInvoiceItem.purchaseInvoice?.supplier?.currency} <g:formatNumber number="${purchaseInvoiceItem.finalPrice * purchaseInvoiceItem.qty}" format="###,##0.00" /></td>
                <td class="right">${purchaseInvoiceItem.purchaseInvoice?.exchangeRate}</td>
                <td class="right"><g:formatNumber number="${purchaseInvoiceItem.finalPrice * purchaseInvoiceItem.qty * (purchaseInvoiceItem.purchaseInvoice.exchangeRate?:0)}" format="PHP ###,##0.00" /></td>
            </tr>
	        <%
	        	Double currencyTotal = grandTotalCurr[purchaseInvoiceItem?.purchaseInvoice?.supplier?.currency];
	        	if(!currencyTotal) currencyTotal = 0;
	        	currencyTotal += (purchaseInvoiceItem.finalPrice * purchaseInvoiceItem.qty);
	        	grandTotalCurr[purchaseInvoiceItem?.purchaseInvoice?.supplier?.currency] = currencyTotal;
	        %>
        </g:each>
        <g:set var="grandTotalPHP" value="${grandTotalPHP+totalAmountPHP}"/>
        <tr>
            <td></td><td></td><td></td><td></td><td></td><td></td><td></td>
            <th class="right">Total</th>
            <th class="right"><g:formatNumber number="${totalAmountCurr}" format="###,##0.00" /></th>
            <th></th>
            <th class="right"><g:formatNumber number="${totalAmountPHP}" format="PHP ###,##0.00" /></th>
        </tr>
    </g:each>
        <tr>
            <td></td><td></td><td></td><td></td><td></td><td></td><td></td>
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
