<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="report" />
    <g:set var="entityName" value="${message(code: 'creditMemo.label', default: 'CreditMemo')}" />
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
    <g:each in="${modelInstanceList}" status="k" var="creditMemoList">
    <table>
        <tr>
            <th colspan="99">
                ${creditMemoList.getKey().toString() as String}
            </th>
        </tr>
        <tr>
        <table>
            <thead>
            <tr>
                <th>Date</th>
                <th>CM Ref #</th>
                <th>SD Ref #</th>
                <th>SD Date</th>
                <th>Customer</th>
                <th>Product Code</th>
                <th>Product Description</th>
                <th>Remarks</th>
                <th>Quantity</th>
                <th>Old Price</th>
                <th>New Price</th>
                <th>Total</th>
                <th>Discount</th>
                <th>Net Total</th>
            </tr>
            </thead>
            <tbody>
            <g:set var="totalOldQty" value="${0}"/>
            <g:set var="totalOldPrice" value="${0}"/>
            <g:set var="totalNewQty" value="${0}"/>
            <g:set var="totalNewPrice" value="${0}"/>
            <g:set var="totalDiffQty" value="${0}"/>
            <g:set var="totalDiffPrice" value="${0}"/>
            <g:set var="totalTotal" value="${0}"/>
            <g:set var="totalDiscount" value="${0}"/>
            <g:set var="totalNetTotal" value="${0}"/>
		    <g:each in="${creditMemoList.getValue()}" status="i" var="creditMemo">
            <g:each in="${creditMemo?.items}" var="bean">
                <g:set var="totalOldQty" value="${totalOldQty+bean?.oldQty}"/>
                <g:set var="totalOldPrice" value="${totalOldPrice+bean?.oldPrice}"/>
                <g:set var="totalNewQty" value="${totalNewQty+bean?.newQty}"/>
                <g:set var="totalNewPrice" value="${totalNewPrice+bean?.newPrice}"/>
                <g:set var="totalDiffQty" value="${totalDiffQty+(bean?.oldQty?(bean?.oldQty-bean?.newQty):bean?.newQty)}"/>
                <g:set var="totalDiffPrice" value="${totalDiffPrice+(bean?.oldPrice?(bean?.oldPrice-bean?.newPrice):bean?.newPrice)}"/>
                <g:set var="totalTotal" value="${totalTotal+bean?.computeFinalAmount()}"/>
                <g:set var="totalDiscount" value="${totalDiscount+bean?.computeDiscountAmount()}"/>
                <g:set var="totalNetTotal" value="${totalNetTotal+bean?.computeDiscountedAmount()}"/>
                <tr>
                    <td><g:formatDate date="${bean['date']}" format="MM/dd/yyyy"/></td>
                    <td>${bean?.creditMemo}</td>
                    <td>${bean?.deliveryItem?.delivery}</td>
                    <td><g:formatDate date="${bean.deliveryItem?.delivery?.date}" format="MM/dd/yyyy"/></td>
                    <td>${bean?.creditMemo?.customer}</td>
                    <td>${bean?.deliveryItem?.product?.identifier}</td>
                    <td>${bean?.deliveryItem?.product?.description}</td>
                    <td>${bean?.remark}</td>
                    <td class="right">${(bean?.oldQty?(bean?.oldQty-bean?.newQty):bean?.newQty)?String.format('%,.0f',(bean?.oldQty?(bean?.oldQty-bean?.newQty):bean?.newQty)):0}</td>
                    <td class="right">${bean?.oldPrice?String.format('%,.2f',bean.oldPrice):0.00}</td>
                    <td class="right">${bean?.newPrice?String.format('%,.2f',bean.newPrice):0.00}</td>
                    <td class="right">${String.format('%,.2f',bean.computeFinalAmount())}</td>
                    <td class="right">${String.format('%,.2f',bean.computeDiscountAmount())}</td>
                    <td class="right">${String.format('%,.2f',bean.computeDiscountedAmount())}</td>
                </tr>
            </g:each>
            </g:each>
            </tbody>
            %{--<g:if test="${params.totalHidden != 'Y'}">--}%
                <tr>
                    <td></td><td></td>
                    <td></td><td></td>
                    <td></td><td></td><td></td>
                    <th class="right">Total</th>
                    <th class="right">${totalDiffQty?String.format('%,.0f',totalDiffQty):0}</th>
                    <th class="right">${totalOldPrice?String.format('%,.2f',totalOldPrice):0.00}</th>
                    <th class="right">${totalNewPrice?String.format('%,.2f',totalNewPrice):0.00}</th>
                    <th class="right">${totalTotal?String.format('%,.2f',totalTotal):0.00}</th>
                    <th class="right">${totalDiscount?String.format('%,.2f',totalDiscount):0.00}</th>
                    <th class="right">${totalNetTotal?String.format('%,.2f',totalNetTotal):0.00}</th>
                </tr>
            %{--</g:if>--}%
        </table>
        </tr>
    </table>
    </g:each>
</div>
</body>
</html>
