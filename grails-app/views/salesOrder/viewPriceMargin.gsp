<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
    <span class="menuButton"><g:link class="create" action="create">Create</g:link></span>
    <span class="menuButton"><g:link class="create" action="upload">Create (from SO Form)</g:link></span>
    <span class="menuButton"><g:link class="create" action="excelForm">Download SO Form</g:link></span>
  </div>
  <div class="body">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesOrder.id.label" default="Id" /></td>
	        <td valign="top" class="value">${salesOrderInstance}</td>
	        <td valign="top" class="name"><g:message code="salesOrder.date.label" default="Date" /></td>
	        <td valign="top" class="value"><g:formatDate date="${salesOrderInstance?.date}" format="MMM. dd, yyyy" /></td>
          </tr>

       	  <tr class="prop">
            <g:set var="printBadAccount" value=""></g:set>
            <g:if test="${salesOrderInstance.customer.isBadAccount()}">
                <g:set var="printBadAccount" value="Bad Account"></g:set>
            </g:if>
       	    <td valign="top" class="name"><g:message code="salesOrder.customer.label" default="Customer" /></td>
	        <td valign="top" class="value"><g:link elementId="customerShowLink" controller="customer" action="show" id="${salesOrderInstance?.customer?.id}">${salesOrderInstance?.customer?.encodeAsHTML()}</g:link> <font color="red"><strong>${printBadAccount}</strong></font></td>
		    <td valign="top" class="name"><g:message code="salesOrder.deliveryDate.label" default="Delivery Date" /></td>
		    <td valign="top" class="value"><g:formatDate date="${salesOrderInstance?.deliveryDate}"  format="MMM. dd, yyyy"/></td>
          </tr>
          
          <tr class="prop">
       	    <td valign="top" class="name"><g:message code="salesOrder.customer.label" default="Customer Remaining Credit" /></td>
	        <td valign="top" class="value"><g:formatNumber number="${salesOrderInstance?.customer?.remainingCredit}" format="#,##0.00"/></td>
		    <td valign="top" class="name"></td>
		    <td valign="top" class="value"></td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesOrder.discountGroup.label" default="Discount Group" /></td>
            <td valign="top" class="value">
                <g:if test="${boldDiscountGroup}">
                  <strong>${salesOrderInstance?.discountGroup?.encodeAsHTML()}</strong>
                </g:if>
                <g:else>
                  ${salesOrderInstance?.discountGroup?.encodeAsHTML()}
                </g:else>
            </td>
            <td valign="top" class="name"><g:message code="salesOrder.netDiscountGroup.label" default="Net Discount Group" /></td>
            <td valign="top" class="value">
                <g:if test="${boldNetDiscountGroup}">
                  <strong>${salesOrderInstance?.netDiscountGroup?.encodeAsHTML()}</strong>
                </g:if>
                <g:else>
                  ${salesOrderInstance?.netDiscountGroup?.encodeAsHTML()}
                </g:else>
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesOrder.type.label" default="Customer Type" /></td>
		 	<td valign="top" class="value">${salesOrderInstance?.customer?.type?.description}</td>
		 	<td valign="top" class="name"><g:message code="salesOrder.preparedBy.label" default="Prepared By" /></td>
	        <td valign="top" class="value"> ${salesOrderInstance?.preparedBy?.encodeAsHTML()} </td>
          </tr>

          <tr class="prop">
	        <td valign="top" class="name"><g:message code="salesOrder.discountType.label" default="Discount Type" /></td>
     	    <td valign="top" class="value">${salesOrderInstance?.discountType}</td>
     	    <td valign="top" class="name"><g:message code="salesOrder.approvedBy.label" default="1st Approval" /></td>
	        <td valign="top" class="value">${salesOrderInstance?.approvedBy?.encodeAsHTML()}</td>
          </tr>


          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesOrder.salesAgent.label" default="Sales Agent" /></td>
            <td valign="top" class="value"><g:link controller="salesAgent" action="show" id="${salesOrderInstance?.customer?.salesAgent?.id}">${salesOrderInstance?.customer?.salesAgent?.formatName()?.encodeAsHTML()}</g:link></td>
            <td valign="top" class="name"><g:message code="salesOrder.approvedTwoBy.label" default="2nd Approval" /></td>
	        <td valign="top" class="value">${salesOrderInstance?.approvedTwoBy?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><g:message code="salesOrder.forwarder.label" default="Forwarder" /></td>
            <td valign="top" class="value"><g:link controller="forwarder" action="show" id="${salesOrderInstance?.customer?.forwarder?.id}">${salesOrderInstance?.customer?.forwarder}</g:link></td>
            <td valign="top" class="name"><g:message code="salesOrder.status.label" default="Status" /></td>
	        <td valign="top" class="value">${fieldValue(bean: salesOrderInstance, field: "status")}</td>
          </tr>

          <tr class="prop">
	        <td valign="top" class="name"><g:message code="salesOrder.remark.label" default="Remarks" /></td>
	        <td valign="top" class="value">${fieldValue(bean: salesOrderInstance, field: "remark")}</td>
	        <td valign="top" class="name"><g:message code="salesOrder.closedBy.label" default="Closed By" /></td>
	        <td valign="top" class="value">${fieldValue(bean: salesOrderInstance, field: "closedBy")}</td>
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
            <g:if test="${salesOrderInstance?.status == 'Unapproved'}">
              <th class="center">Running Cost</th>
            </g:if> <g:else>
              <th class="center">Cost</th>
            </g:else>
            <th class="center">Final Price</th>
            <th class="center">Margin</th>
            <th class="center">Margin Percentage</th>
              <th></th>
          </tr>
        </thead>
          <tbody class="uneditable">
        	<g:each in="${salesOrderInstance?.items?.findAll {!it?.isNet}?.sort{it?.product?.description}}" var="i" status="colors">
            <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
          	  <td><g:link controller="product" action="show" id="${i?.product?.id}">${i?.product}</g:link></td>
          	  <td>${i?.product?.description}</td>
          	  <td class="right"> Php
	            <g:if test="${salesOrderInstance?.status == 'Unapproved' || salesOrderInstance?.status == 'Second Approval Pending'}">
	          		<g:formatNumber number="${i?.product?.runningCost}" format="#,##0.0000" />
	          	</g:if> <g:else>
	          		<g:formatNumber number="${i?.cost}" format="#,##0.0000" />
	          	</g:else>
	          </td>
	          <td class="right">Php <g:formatNumber number="${i?.discountedFinalPrice()}" format="#,##0.0000" /></td>
	          <td class="right">Php <g:formatNumber number="${i?.margin}" format="#,##0.0000" /></td>
	          <td class="right"><g:formatNumber number="${i?.getPriceMargin()}" format="#,##0.0000"/>%</td>
              <td><g:link controller="stockCostHistory" action="show" id="${i?.product?.id}">View Stock Cost History</g:link></td>
          	</tr>
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
              <g:if test="${salesOrderInstance?.status == 'Unapproved'}">
                <th class="center">Running Cost</th>
              </g:if> <g:else>
                <th class="center">Cost</th>
              </g:else>
              <th class="center">Final Price</th>
              <th class="center">Margin</th>
              <th class="center">Margin Percentage</th>
              <th></th>
            </tr>
          </thead>
          <g:if test="${salesOrderInstance?.status == 'Unapproved'}">
            <tbody class="editable">
          </g:if>

          <g:else>
            <tbody class="uneditable">
          </g:else>

          <g:each in="${salesOrderInstance?.items?.findAll {it?.isNet}?.sort{it?.product?.description}}" var="i" status="colors">
              <tr class="${(colors % 2) == 0 ? 'odd' : 'even'}">
          	  <td><g:link controller="product" action="show" id="${i?.product?.id}">${i?.product}</g:link></td>
          	  <td>${i?.product?.description}</td>
          	  <td class="right">Php
	            <g:if test="${salesOrderInstance?.status == 'Unapproved' || salesOrderInstance?.status == 'Second Approval Pending'}">
	          		<g:formatNumber number="${i?.product?.runningCost}" format="#,##0.00" />
	          	</g:if> <g:else>
	          		<g:formatNumber number="${i?.cost}" format="#,##0.00" />
	          	</g:else>
	          </td>
	          <td class="right">Php <g:formatNumber number="${i?.discountedFinalPrice()}" format="#,##0.0000" /></td>
	          <td class="right">Php <g:formatNumber number="${i?.margin}" format="#,##0.0000" /></td>
	          <td class="right"><g:formatNumber number="${i?.getPriceMargin()}" format="#,##0.0000"/>%</td>
              <td><g:link controller="stockCostHistory" action="show" id="${i?.product?.id}">View Stock Cost History</g:link></td>
          	</tr>
          </g:each>
          </tbody>
        </table>
      </div>

      <div class="buttons">
        <g:form>
            <span class="button"><g:link controller="salesOrder" action="show" id="${salesOrderInstance?.id}">Back</g:link></span>
        </g:form>
      </div>
  </div>
</body>
</html>
