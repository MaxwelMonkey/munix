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
    <g:each in="${modelInstanceList}" status="i" var="creditMemo">
        <table>
            <thead>
            <tr>
                <th colspan="99">
                    ${creditMemo.customer.toString()}<br/>
                    <g:formatDate format="MMMM d, yyyy" date="${params?.dateFrom}"/> - <g:formatDate format="MMMM d, yyyy" date="${params?.dateTo}"/>
                </th>
            </tr>
            <tr>
                <th>Date</th>
                <th>Ref #</th>
                <th>Remarks</th>
                <th>Old Qty.</th>
                <th>Old Price</th>
                <th>New Qty</th>
                <th>New Price</th>
                <th>Diff. Qty</th>
                <th>Diff. Price</th>
            </tr>
            </thead>
            <tbody>
            <g:set var="totalOldQty" value="${0}"/>
            <g:set var="totalOldPrice" value="${0}"/>
            <g:set var="totalNewQty" value="${0}"/>
            <g:set var="totalNewPrice" value="${0}"/>
            <g:set var="totalDiffQty" value="${0}"/>
            <g:set var="totalDiffPrice" value="${0}"/>
            <g:each in="${creditMemo.items}" var="bean">
                <g:set var="totalOldQty" value="${totalOldQty+bean.oldQty}"/>
                <g:set var="totalOldPrice" value="${totalOldPrice+bean.oldPrice}"/>
                <g:set var="totalNewQty" value="${totalNewQty+bean.newQty}"/>
                <g:set var="totalNewPrice" value="${totalNewPrice+bean.newPrice}"/>
                <g:set var="totalDiffQty" value="${totalDiffQty+(bean.oldQty-bean.newQty)}"/>
                <g:set var="totalDiffPrice" value="${totalDiffPrice+(bean.oldPrice-bean.newPrice)}"/>
                <tr>
                    <td><g:formatDate date="${bean['date']}" format="MM/dd/yyyy"/></td>
                    <td>${bean.deliveryItem.delivery}</td>
                    <td>${bean.remark}</td>
                    <td class="right">${bean.oldQty}</td>
                    <td class="right">${bean.oldPrice}</td>
                    <td class="right">${bean.newQty}</td>
                    <td class="right">${bean.newPrice}</td>
                    <td class="right">${bean.oldQty-bean.newQty}</td>
                    <td class="right">${bean.oldPrice-bean.newPrice}</td>
                </tr>
            </g:each>
            </tbody>
            %{--<g:if test="${params.totalHidden != 'Y'}">--}%
            <tr>
                <td></td><td></td>
                <th class="right">Total</th>
                <th class="right">${String.format('%,.2f',totalOldQty)}</th>
                <th class="right">${String.format('%,.2f',totalOldPrice)}</th>
                <th class="right">${String.format('%,.2f',totalNewQty)}</th>
                <th class="right">${String.format('%,.2f',totalNewPrice)}</th>
                <th class="right">${String.format('%,.2f',totalDiffQty)}</th>
                <th class="right">${String.format('%,.2f',totalDiffPrice)}</th>
            </tr>
            %{--</g:if>--}%
        </table>
    </g:each>
</div>
</body>
</html>
