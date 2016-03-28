<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesDelivery.label', default: 'SalesDelivery')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesDelivery.id.label" default="Id" /></td>
        <td valign="top" class="value">${salesDeliveryInstance}</td>
        <td valign="top" class="name"><g:message code="salesDelivery.date.label" default="Date" /></td>
        <td valign="top" class="value"><g:formatDate date="${salesDeliveryInstance?.date}" format="MMM. dd, yyyy"/></td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesDelivery.invoice.label" default="Order" /></td>
        <td valign="top" class="value"><g:link elementId="salesOrderShowLink" action="show" controller="salesOrder" id="${salesDeliveryInstance?.invoice?.id}">${salesDeliveryInstance?.invoice}</g:link></td>
        <td valign="top" class="name"><g:message code="salesDelivery.preparedBy.label" default="Prepared By" /></td>
        <td valign="top" class="value">${salesDeliveryInstance?.preparedBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
	      <td valign="top" class="name">Sales Invoice #</td>
	      <td valign="top" class="value">${salesDeliveryInstance?.salesDeliveryNumber}</td>
        <td valign="top" class="name"><g:message code="salesDelivery.approvedBy.label" default="Approved By" /></td>
        <td valign="top" class="value">${salesDeliveryInstance?.approvedBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
	        <td valign="top" class="name"><g:message code="salesDelivery.warehouse.label" default="Warehouse" /></td>
	        <td valign="top" class="value">${salesDeliveryInstance?.warehouse?.description?.encodeAsHTML()}</td>

	        <td valign="top" class="name"><g:message code="salesDelivery.autoApprovedBy.label" default="Auto-approved By" /></td>
            <td valign="top" class="value">${salesDeliveryInstance?.autoApprovedBy?.encodeAsHTML()}</td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesDelivery.deliveryType.label" default="Delivery Type" /></td>
          <td valign="top" class="value">${salesDeliveryInstance?.deliveryType?.encodeAsHTML()}</td>
	      <td valign="top" class="name">Delivery Receipt #</td>
	      <td valign="top" class="value">${salesDeliveryInstance?.deliveryReceiptNumber}</td>
        </tr>

          <tr class="prop">
	        <td valign="top" class="name"><g:message code="salesDelivery.remark.label" default="Remarks" /></td>
	        <td valign="top" class="value">${fieldValue(bean: salesDeliveryInstance, field: "remark")}</td>
	        <td class="name"></td>
	        <td class="value"></td>
          </tr>

        <tr class="prop">
          <td valign="top" class="name"><g:message code="salesDelivery.customer.label" default="Customer" /></td>
        <td valign="top" class="value"><g:link elementId="customerShowLink" controller="customer" action="show" id="${salesDeliveryInstance?.customer?.id}">${salesDeliveryInstance?.customer}</g:link></td>
        <td valign="top" class="name"><g:message code="salesDelivery.salesAgent.label" default="Sales Agent" /></td>
        <td valign="top" class="value"><g:link controller="salesAgent" action="show" id="${salesDeliveryInstance?.salesAgent?.id}">${salesDeliveryInstance?.salesAgent?.firstName} ${salesDeliveryInstance?.salesAgent?.lastName}</g:link></td>
        </tr>

        <g:ifAnyGranted role="ROLE_ACCOUNTING">
          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesDelivery.termDay.label" default="Term Days" /></td>
          <td valign="top" class="value">${salesDeliveryInstance?.termDay?.encodeAsHTML()}</td>
          <td class="name">Counter Receipt</td>
          <td class="value">
              <g:each in="${salesDeliveryInstance.counterReceipts?.findAll{it?.isApproved()}.sort{it.id}}" var="counterReceipt">
                <g:link controller="counterReceipt" action="show" id="${counterReceipt?.id}">${counterReceipt}</g:link>
              </g:each>
          </td>
          </tr>
        </g:ifAnyGranted>

        <tr class="prop">
          <td class="name"></td>
          <td class="value"></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>

        <tr class="prop">


        <td valign="top" class="name"><g:message code="salesDelivery.tripTicket.label" default="Trip Ticket" /></td>
        <g:if test="${salesDeliveryInstance?.directDelivery != null}">
          <td valign="top" class="value"><g:link action="show" controller="tripTicket" id="${salesDeliveryInstance?.directDelivery?.tripTicket?.tripTicket?.id}">${salesDeliveryInstance?.directDelivery?.tripTicket}</g:link></td>
        </g:if>
        <g:elseif test="${salesDeliveryInstance?.waybill != null}">
          <td valign="top" class="value"><g:link action="show" controller="tripTicket" id="${salesDeliveryInstance?.waybill?.tripTicket?.tripTicket?.id}">${salesDeliveryInstance?.waybill?.tripTicket}</g:link></td>
        </g:elseif>
        <g:else>
          <td class="value"></td>
        </g:else>
          <td class="name"></td>
          <td class="value"></td>
        </tr>
        <tr class="prop">
          <td valign="top" class="name">Waybill</td>
      	  <td valign="top" class="value"><g:link action="show" controller="waybill" id="${salesDeliveryInstance?.waybill?.tripTicket?.id}">${salesDeliveryInstance?.waybill}</g:link></td>
          <td class="name"></td>
          <td class="value"></td>
        </tr>
        <tr class="prop">
        <td valign="top" class="name"><g:message code="salesDelivery.delivery.label" default="Delivery" /></td>

        <g:if test="${salesDeliveryInstance?.directDelivery != null}">
          <td valign="top" class="value"><g:link action="show" controller="directDelivery" id="${salesDeliveryInstance?.directDelivery?.id}">${salesDeliveryInstance?.directDelivery}</g:link></td>
        </g:if>
        <g:else>
          <td></td>
        </g:else>
          <td valign="top" class="name"><g:message code="salesDelivery.status.label" default="Status" /></td>
        <td valign="top" class="value">${salesDeliveryInstance?.status}</td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="subTable">
      <table>
        <h2>Discounted Price Items</h2>

        <thead>
          <tr>
            <th class="center">Identifier</th>
            <th class="center">Description</th>
            <th class="center">Qty</th>
            <g:if test="${salesDeliveryInstance?.isUnapproved()}">
              <th class="center">Running Cost</th>
            </g:if> <g:else>
              <th class="center">Cost</th>
            </g:else>
            <th class="center">Price</th>
            <th class="center">Margin</th>
            <th class="center">Margin Percentage</th>
            <th></th>
          </tr>
        </thead>
          <tbody class="uneditable">
        	<g:each in="${salesDeliveryInstance?.items?.findAll {!it?.orderItem.isNet}?.sort{it?.product?.description}}" var="i" status="colors">
              <g:if test="${i?.qty?.intValue() > 0}">
                <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
	          	  <td><g:link controller="product" action="show" id="${i?.product?.id}">${i?.product}</g:link></td>
                  <td>${i?.product?.description}</td>
                  <td class="right"><g:formatNumber number="${i?.qty}" format="#,##0.00" /></td>
                  <td class="right">
                    <g:if test="${salesDeliveryInstance?.isUnapproved()}">
                        Php <g:formatNumber number="${i?.product?.runningCost}" format="#,##0.00" />
                    </g:if> <g:else>
                        Php <g:formatNumber number="${i?.cost}" format="#,##0.00" />
                    </g:else>
                  </td>
                  <td class="right">Php <g:formatNumber number="${i?.discountedPrice}" format="#,##0.00" /></td>
                  <td class="right">Php <g:formatNumber number="${i?.margin}" format="#,##0.00" /></td>
                  <td class="right"><g:formatNumber number="${i?.getPriceMargin()}" format="#,##0.0000"/>%</td>
                  <td><g:link controller="stockCostHistory" action="show" id="${i?.product?.id}">View Stock Cost History</g:link></td>
                </tr>
               </g:if>
        	</g:each>
        </tbody>
      </table>
    </div>


      <div class="subTable">
        <table>
          <h2>Net Price Items</h2>

          <thead>
            <tr>
              <th class="center">Identifier</th>
              <th class="center">Description</th>
              <th class="center">Qty</th>
              <g:if test="${salesDeliveryInstance?.isUnapproved()}">
                <th class="center">Running Cost</th>
              </g:if> <g:else>
                <th class="center">Cost</th>
              </g:else>
              <th class="center">Price</th>
              <th class="center">Margin</th>
              <th class="center">Margin Percentage</th>
              <th></th>
            </tr>
          </thead>
          <g:if test="${salesDeliveryInstance?.isUnapproved()}">
            <tbody class="editable">
          </g:if>

          <g:else>
            <tbody class="uneditable">
          </g:else>

          <g:each in="${salesDeliveryInstance?.items?.findAll {it?.orderItem.isNet}?.sort{it?.product?.description}}" var="i" status="colors">
            <g:if test="${i?.qty?.intValue() > 0}">
              <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
	          	  <td><g:link controller="product" action="show" id="${i?.product?.id}">${i?.product}</g:link></td>
                  <td>${i?.product?.description}</td>
                  <td class="right"><g:formatNumber number="${i?.qty}" format="#,##0.00" /></td>
                  <td class="right">
                    <g:if test="${salesDeliveryInstance?.isUnapproved()}">
                        Php <g:formatNumber number="${i?.product?.runningCost}" format="#,##0.0000" />
                    </g:if> <g:else>
                        Php <g:formatNumber number="${i?.cost}" format="#,##0.0000" />
                    </g:else>
                  </td>
                  <td class="right">Php <g:formatNumber number="${i?.discountedPrice}" format="#,##0.0000" /></td>
                  <td class="right">Php <g:formatNumber number="${i?.margin}" format="#,##0.0000" /></td>
                  <td class="right"><g:formatNumber number="${i?.getPriceMargin()}" format="#,##0.0000"/>%</td>
                  <td><g:link controller="stockCostHistory" action="show" id="${i?.product?.id}">View Stock Cost History</g:link></td>
                </tr>
            </g:if>
          </g:each>
          </tbody>
        </table>
      </div>

      <div class="buttons">
        <g:form>
            <span class="button"><g:link controller="salesDelivery" action="show" id="${salesDeliveryInstance?.id}">Back</g:link></span>
        </g:form>
      </div>
  </div>
</body>
</html>
