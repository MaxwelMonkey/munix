<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <link rel="stylesheet" href="${resource(dir:'css',file:'report.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'tripTicket.css')}" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Trip Ticket</title>
  </head>
  <body onload="" class="tripTicket">
    <h2>Trip Ticket</h2>
    <div id="head">
      <table>
        <tr class="prop">
          <td class="name col1">Plate Number</td>
          <td class="value col2">${tripTicketInstance?.truck}</td>
          <td class="name col3">Reference</td>
          <td class="value col4">${tripTicketInstance}</td>
        </tr>
        <tr class="prop">
          <td class="name">Driver</td>
          <td class="value">${tripTicketInstance?.driver}</td>
          <td class="name">Delivery Date</td>
          <td class="value"><g:formatDate date="${new Date()}" format="MM/dd/yyyy"/></td>
        </tr>
        <tr class="prop">
          <td class="name">Helpers</td>
          <td class="value">
        <g:each in="${tripTicketInstance?.helpers?.sort{it.toString()}}" var="i">
${i} <br/>
        </g:each>
        </td>
        <td class="name"></td>
        <td class="value"></td>
        </tr>
      </table>
    </div>

    <div id="body">
      <table>
        <thead>
        <tr>
        <th class="customer">Customer</th>
        <th class="wb">WB</th>
        <th class="sd">SD</th>
        <th class="package">Package/s</th>
        <th class="timeIn">Time In</th>
        <th class="timeOut">Time Out</th>
        <th class="signature">Signature</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${tripTicketInstance?.items.sort{it.priority}}" var="i">
          <tr>
            <td>
	        <g:if test="${i?.type == 'Waybill'}">
				<b>${i?.item?.customer?.name}</b> <br/>
				<i>${i?.item?.forwarder}</i> <br/>
				${i?.item?.forwarder?.formatAddress()}
			</g:if>
			<g:else>
				<b>${i?.item?.customer?.name}</b> <br/>
				${i?.item?.customer?.formatBusinessAddress()}
			</g:else>
            </td>
            <td>
          <g:if test="${i?.type == 'Waybill'}">
${i?.item}
          </g:if>
          </td>
          <td>
<g:each in="${i?.item?.deliveries}" var="deliveryInstance">
  ${deliveryInstance} <br/>
</g:each>

          </td>
          <td>
          <g:each in="${i?.item?.packages}" var="packageInstance">
            ${packageInstance?.formatQty()} ${packageInstance?.packaging}<br/>
          </g:each>
          </td>
          <td></td>
          <td></td>
          <td></td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

    <div id="footer">
      <table>
        <tr class="prop">
          <td class="name">Created By</td>
          <td class="value">${tripTicketInstance?.preparedBy}, <g:formatDate date="${tripTicketInstance?.date}" format="MM/dd/yyyy hh:mm a"/></td>
        </tr>
        <tr class="prop">
          <td class="name">Released By</td>
          <td class="value">________________________</td>
          <td class="name">Date & Time In</td>
          <td class="value">________________________</td>
        </tr>
        <tr class="prop">
          <td class="name">Guard On Duty</td>
          <td class="value">________________________</td>
          <td class="name">Date & Time Out</td>
          <td class="value">________________________</td>
        </tr>
      </table>
    </div>
	<script>window.onload = function(){window.print();};</script>
  </body>
</html>
