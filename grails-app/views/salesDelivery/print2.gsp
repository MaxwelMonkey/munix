<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <link rel="stylesheet" href="${resource(dir:'css',file:'report.css')}" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sales Delivery</title>
  </head>
  <body onload="print();">
    <h1>Delivery Receipt</h1>
    <div id="head">
      <table>
        <tr class="prop">
          <td class="name">Customer</td>
          <td class="value">${salesDeliveryInstance?.customer?.name}</td>
          <td class="name">Reference</td>
          <td class="value">${salesDeliveryInstance}</td>
        </tr>
        <tr class="prop">
          <td class="name">Address</td>
          <td class="value">${salesDeliveryInstance?.customer?.formatBusinessAddress()}</td>
          <td class="name">Delivery Date</td>
          <td class="value"><g:formatDate date="${new Date()}" format="MM/dd/yyyy"/></td>
        </tr>
        <!---tr class="prop">
          <td class="name">Warehouse</td>
          <td class="value">${salesDeliveryInstance?.warehouse}</td>
          <td class="name">Terms</td>
          <td class="value">${salesDeliveryInstance?.termDay}</td>
        </tr--->
      </table>
    </div>

    <div id="body">
      <table>
        <thead>
        <th class="right">Qty</th>
        <th class="center">Unit</th>
        <th>Identifier</th>
        <th>Description</th>
        <th class="right">Unit Price</th>
        <th class="right">Amount</th>
        </thead>
        <tbody>
        <g:each in="${salesDeliveryInstance?.items.sort{it?.product?.description}}" var="i">
          <tr>
            <td class="right">${i?.formatQty()}</td>
            <td class="center">${i?.product?.unit}</td>
            <td>${i?.product?.identifier}</td>
            <td>${i?.product?.description}</td>
            <td class="right">${i.price==0?"(FREE)":i?.formatPrice()}</td>
            <td class="right">${i?.formatAmount()}</td>
          </tr>
        </g:each>
        </tbody>
        <tfoot>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right">Gross Total</td>
            <td class="right">${salesDeliveryInstance?.formatGrossTotal()}</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right">Discount (${salesDeliveryInstance?.invoice?.discountGroup})</td>
            <td class="right">${salesDeliveryInstance?.formatDiscountAmount()}</td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="right">Net Total</td>
            <td class="right">${salesDeliveryInstance?.formatGrandTotal()}</td>
          </tr>
        </tfoot>
      </table>
    </div>

    <div id="footer">
      <table>
        <tr class="prop">
          <td class="name">Created By</td>
          <td class="value">${salesDeliveryInstance?.preparedBy}</td>
          <td class="name">Received By</td>
          <td class="value">________________________</td>
        </tr>
        <tr class="prop">
          <td class="name">Confirmed By</td>
          <td class="value">________________________</td>
          <td class="name">Date & Time</td>
          <td class="value">________________________</td>
        </tr>
      </table>
    </div>
  </body>
</html>
