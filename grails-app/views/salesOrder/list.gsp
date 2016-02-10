
<%@ page import="com.munix.CustomerType; com.munix.PriceType" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  <g:javascript src="generalmethods.js" />
  <script>
    var $=jQuery.noConflict();
    $(document).ready(function(){
    	  $("#searchIdentifier").ForceNumericOnlyEnterAllowed()
    });
  </script>
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
  	<g:set var="maxlength" value="16"/>
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <div id="search">
      <g:form controller="salesOrder" action="filter" >
        <table>
          <tr>
            <td class="name">ID</td>
            <td class="value"><g:textField id="searchIdentifier" name="searchIdentifier" maxlength="${maxlength}" value="${params?.searchIdentifier}" /></td>
          </tr>
          <tr>
            <td class="name">Discount Type</td>
            <td class="value"><g:select name="searchDiscountType" noSelection="${['':'Select One...']}" from="${com.munix.DiscountType.list().sort{it.id}}" optionKey="id" optionValue="description" value="${params?.searchDiscountType}"/></td>
          </tr>
          <tr>
            <td class="name">Customer</td>
            <td class="value"><g:textField name="searchCustomer" value="${params?.searchCustomer}"/></td>
          </tr>
          <tr>
            <td class="name">Remarks</td>
            <td class="value"><g:textField name="searchRemark" value="${params?.searchRemark}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="searchStatus" noSelection="${['':'All statuses except Complete and Cancelled']}" from="${['All statuses','Unapproved','Second Approval Pending','Approved','Complete','Cancelled']}" value="${params?.searchStatus}"/></td>
          </tr>
		  <tr>
            <td class="name">Price Type</td>
            <td class="value"><g:select name="searchPriceType" noSelection="${['':'Select One...']}" from="${PriceType}" optionKey="description" value="${params?.searchPriceType}"/></td>
          </tr>
          <tr>
            <td class="name">Customer Type</td>
            <td class="value"><g:select name="searchCustomerType" noSelection="${['':'Select One...']}" optionKey="id" optionValue="description" from="${CustomerType.list()}" value="${params?.searchCustomerType}"/></td>
          </tr>
        </table>
        <div>
<!--          <input type="button" class="button" value="Reset" onClick="window.location = '${createLink(uri:'/')}salesOrder/list'"/>-->
          <input type="submit" class="button" value="Search"/>
        </div>

      </g:form>
    </div>

    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <table>
        <thead>
          <tr>
            <g:sortableColumn class="left" property="id" title="${message(code: 'salesOrder.id.label', default: 'Id')}" params="${params}"/>
            <th class="left">Customer Type</th>
            <g:sortableColumn class="left" property="customer" title="${message(code: 'salesOrder.customer.label', default: 'Customer')}" params="${params}"/>
            <g:sortableColumn class="left" property="discountType" title="${message(code: 'salesOrder.discountType.label', default: 'Discount Type')}" params="${params}"/>
            <g:sortableColumn class="left" property="priceType" title="${message(code: 'salesOrder.priceType.label', default: 'Price Type')}" params="${params}"/>
            <th class="left" >Remarks</th>
            <th class="left">Deliveries</th>
            <g:sortableColumn class="left" property="status" title="${message(code: 'salesOrder.status.label', default: 'Status')}" params="${params}"/>
            <g:sortableColumn class="left" property="date" title="${message(code: 'salesOrder.date.label', default: 'Date')}" params="${params}" />
            <g:sortableColumn class="left" property="deliveryDate" title="${message(code: 'salesOrder.deliveryDate.label', default: 'Delivery Date')}" params="${params}" />
            <th class="left">Print Count Price</th>
            <th class="left">Print Count No Price</th>
          </tr>
        </thead>
        <tbody>
        <g:each in="${salesOrderInstanceList}" status="i" var="salesOrderInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}salesOrder/show/${salesOrderInstance.id}'">
            <td id="rowSalesOrderId${i}" class="center">${salesOrderInstance}</td>
            <td>${salesOrderInstance?.customer?.type?.description}</td>
            <td>${fieldValue(bean: salesOrderInstance, field: "customer")}</td>
            <td>${fieldValue(bean: salesOrderInstance, field: "discountType")}</td>
            <td>${fieldValue(bean: salesOrderInstance, field: "priceType")}</td>
            <td>${fieldValue(bean: salesOrderInstance, field: "remark")}</td>
            <td>
                <g:each in="${salesOrderInstance?.deliveries}" var="delivery">
                  <g:link controller="salesDelivery" action="show" id="${delivery?.id}">[${delivery}]</g:link>
                </g:each>
            </td>
            <td>${fieldValue(bean: salesOrderInstance, field: "status")}</td>
            <td class="center"><g:formatDate date="${salesOrderInstance.date}" format="MM/dd/yyyy" /></td>
            <td class="center"><g:formatDate date="${salesOrderInstance.deliveryDate}" format="MM/dd/yyyy" /></td>
            <td>${salesOrderInstance.getPricePrintCount()}</td>
            <td>${salesOrderInstance.getNoPricePrintCount()}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${salesOrderInstanceTotal}" params="${params}" />
    </div>
  </div>
</body>
</html>
