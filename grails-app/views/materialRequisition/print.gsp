<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <link rel="stylesheet" href="${resource(dir:'css',file:'report.css')}" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Material Requisition</title>
  </head>
  <body onload="print()">
    <h2>Material Requisition</h2>
    <div id="head">
      <table>
        <tr class="prop">
          <td class="name">Production For</td>
          <td class="value">[${materialRequisitionInstance?.jobOrder?.product}] ${materialRequisitionInstance?.jobOrder?.product?.description}</td>
          <td class="name">Job Order</td>
          <td class="value">${materialRequisitionInstance?.jobOrder}</td>
        </tr>
        <tr class="prop">
          <td class="name">Qty</td>
          <td class="value">${materialRequisitionInstance?.jobOrder?.formatQty()}</td>
          <td class="name">Reference</td>
          <td class="value">${materialRequisitionInstance}</td>
        </tr>
        <tr class="prop">
          <td class="name">Assigned To</td>
          <td class="value">${materialRequisitionInstance?.jobOrder?.assignedTo}</td>
          <td class="name">Start Date</td>
          <td class="value"><g:formatDate date="${materialRequisitionInstance?.jobOrder?.startDate}" format="MM/dd/yyyy" /></td>
        </tr>
        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name">Target Date</td>
          <td class="value"><g:formatDate date="${materialRequisitionInstance?.jobOrder?.targetDate}" format="MM/dd/yyyy" /></td>
        </tr>
      </table>
    </div>

    <div id="body">
      <table>
        <thead>
        <th class="right">Qty</th>
        <th class="center">Unit</th>
        <th>Identifier</th>
        <th>Description</th>
        </thead>
        <tbody>
        <g:each in="${materialRequisitionInstance?.items.sort{it.component?.description}}" var="i">
          <tr>
            <td class="right">${i?.formatQty()}</td>
            <td class="center">${i?.component?.unit}</td>
            <td>${i?.component?.identifier}</td>
            <td>${i?.component?.description}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <div id="footer">
      <table>
        <tr>
          <td class="center">_____________________</td>
          <td class="center">_____________________</td>
          <td class="center">_____________________</td>
          <td class="center">_____________________</td>
        </tr>
        <tr class="prop">
          <td class="center">Created By</td>
          <td class="center">Pre-checked By</td>
          <td class="center">Checked & Released By</td>
          <td class="center">Received By</td>
        </tr>
      </table>
    </div>
  </body>
</html>
