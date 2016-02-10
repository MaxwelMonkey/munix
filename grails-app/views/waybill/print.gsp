<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <link rel="stylesheet" href="${resource(dir:'css',file:'report.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'waybill.css')}" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sample title</title>
  </head>
  <body onload="" class="waybill">
    <h2>Waybill</h2>
    <div id="head">
      <table>
        <tr class="prop">
          <td class="name col1">Customer</td>
          <td class="value col2">${waybillInstance?.customer?.name}</td>
          <td class="name col3">Reference</td>
          <td class="value col4">${waybillInstance}</td>
        </tr>
        <tr class="prop">
          <td class="name">Address</td>
          <td class="value">${waybillInstance?.customer?.formatBusinessAddress()}</td>
          <td class="name">Delivery Date</td>
          <td class="value"><g:formatDate date="${new Date()}" format="MM/dd/yyyy"/></td>
        </tr>
        <tr class="prop">
          <td class="name">Forwarder</td>
          <td class="value">${waybillInstance?.forwarder}</td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>
        <tr class="prop">
          <td class="name">Forwarder Address</td>
          <td class="value">${waybillInstance?.forwarder?.formatAddress()}; ${waybillInstance?.forwarder?.contact}</td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>
      </table>
    </div>

    <div id="body">
      <table>
        <thead>
        <tr>
        <th class="center qty">Qty</th>
        <th class="center packaging">Unit</th>
        <th class="description">Description</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${waybillInstance.packages.sort{it.id}}" var="i">
          <tr>
            <td class="center">${i?.formatQty()}</td>
            <td class="center">${i?.packaging}</td>
            <td>${i?.description}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <div id="footer">
      <table>
        <tr class="prop">
          <td class="name">SD/s</td>
          <td class="value">
        <g:each in="${waybillInstance.deliveries.sort{it.id}}" var="i" status="j">
           <g:if test="${j>0}">, </g:if>${i}
        </g:each>
        </td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>

        <tr class="prop">
          <td class="name">Total</td>
          <td class="value"><strong>${waybillInstance?.formatDeclaredValueTotal()}</strong></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

      </table>
    </div>
	<script>window.onload = function(){window.print();};</script>
  </body>
</html>
