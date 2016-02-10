
<%@ page import="com.munix.PurchaseInvoice" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
  <title><g:message code="default.list.label" args="[entityName]" /></title>
  <link rel="stylesheet" href="${resource(dir:'css',file:'jquery.ui.all.css')}" />
  <g:javascript src="numbervalidation.js" />
  <g:javascript src="jquery.ui.core.js" />
  <g:javascript src="jquery.ui.datepicker.js" />
  <g:javascript src="generalmethods.js" />
  <g:javascript src="calendarStructTemplate.js" />
  <g:javascript>
    var $=jQuery.noConflict();
    $(document).ready(function(){
        setCalendarStruct($("#searchInvoiceDateFrom"), $("#searchInvoiceDateFrom_value"))
        setCalendarStruct($("#searchInvoiceDateTo"), $("#searchInvoiceDateTo_value"))
        setCalendarStruct($("#searchDeliveryDateFrom"), $("#searchDeliveryDateFrom_value"))
        setCalendarStruct($("#searchDeliveryDateTo"), $("#searchDeliveryDateTo_value"))
    	Event.observe(window, 'load', function() {
    		$('search').hide();
    		Event.observe('searchButton', 'click', function(){
    			$('search').toggle();
    		});
    	});
    });
  </g:javascript>
</head>
<body>
  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="filter"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
  </div>
  <div class="body">
  	<calendar:resources lang="en" theme="aqua"/>
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div id="search">
      <g:form controller="purchaseInvoice" action="list" >
        <table>
          <tr>
            <td class="name">Reference</td>
            <td class="value"><g:textField name="reference" id="reference" value="${params?.reference}"/></td>
          </tr>
          <tr>
            <td class="name">Supplier Reference</td>
            <td class="value"><g:textField name="supplierReference" id="supplierReference" value="${params?.supplierReference}"/></td>
          </tr>
          <tr>
            <td class="name">Supplier</td>
            <td class="value"><g:textField name="supplier" id="supplier" value="${params?.supplier}"/></td>
          </tr>
          <tr>
            <td class="name">Type</td>
            <td class="value"><g:select name="type" from="${['','Foreign','Local']}" value="${params?.type}"/></td>
          </tr>
          <tr>
            <td class="name">Status</td>
            <td class="value"><g:select name="status" noSelection="${['':'']}" from="${['Approved', 'Unapproved', 'Cancelled', 'Paid']}" id="status" value="${params?.status}"/></td>
          </tr>
          <tr>
          	
            <td class="name">Invoice Date</td>
            <td class="value"">
              <calendar:datePicker name="searchInvoiceDateFrom"  years="2009,2030" value="${dateMap.searchInvoiceDateFrom}"/>
           	  to
           	  <calendar:datePicker name="searchInvoiceDateTo"  years="2009,2030" value="${dateMap.searchInvoiceDateTo}"/>
           	</td>
          </tr>
          <tr>
            <td class="name">Delivery Date</td>
            <td class="value"">
              <calendar:datePicker name="searchDeliveryDateFrom"  years="2009,2030" value="${dateMap.searchDeliveryDateFrom}"/>
           	  to
           	  <calendar:datePicker name="searchDeliveryDateTo"  years="2009,2030" value="${dateMap.searchDeliveryDateTo}"/>
           	</td>
          </tr>
          <tr>
            <td colspan="2" ><input type="submit" class="button" value="Go"/></td>
          </tr>

        </table>
      </g:form>
    </div>
    <div class="list">
      <table>
        <thead>
          <tr>

        <g:sortableColumn property="reference" title="${message(code: 'purchaseInvoice.reference.label', default: 'Reference')}" params="${params}" />
        <g:sortableColumn class="left" property="supplierReference" title="${message(code: 'purchaseInvoice.supplierReference.label', default: 'Supplier Reference')}" params="${params}"/>
        <g:sortableColumn property="supplier" title="${message(code: 'purchaseInvoice.supplier.label', default: 'Supplier')}" params="${params}" />
        <g:sortableColumn property="type" title="${message(code: 'purchaseInvoice.type.label', default: 'Type')}" params="${params}" />
        <th class="right">Amount (Foreign Currency)</th>
        <th class="right">Amount (PHP)</th>
        <g:sortableColumn property="status" title="${message(code: 'purchaseInvoice.status.label', default: 'Status')}" params="${params}" />
        <g:sortableColumn class="center" property="invoiceDate" title="${message(code: 'purchaseInvoice.invoiceDate.label', default: 'Invoice Date')}" params="${params}"/>
        <g:sortableColumn class="center" property="deliveryDate" title="${message(code: 'purchaseInvoice.deliveryDate.label', default: 'Delivery Date')}" params="${params}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${purchaseInvoiceInstanceList}" status="i" var="purchaseInvoiceInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}purchaseInvoice/show/${purchaseInvoiceInstance.id}'">

            <td id="rowPurchaseInvoiceReference${i}">${fieldValue(bean: purchaseInvoiceInstance, field: "reference")}</td>
            <td class="left">${fieldValue(bean: purchaseInvoiceInstance, field: "supplierReference")}</td>
            <td>${purchaseInvoiceInstance?.supplier?.name}</td>
            <td>${fieldValue(bean: purchaseInvoiceInstance, field: "type")}</td>
            <td class="right">${purchaseInvoiceInstance?.formatPurchaseInvoiceDiscountedForeignTotal()}</td>
            <td class="right">${purchaseInvoiceInstance?.formatPurchaseInvoiceDiscountedPhpTotal()}</td>
            <td>${fieldValue(bean: purchaseInvoiceInstance, field: "status")}</td>
            <td><g:formatDate date="${purchaseInvoiceInstance.invoiceDate}" format="MM/dd/yyyy"/></td>
            <td><g:formatDate date="${purchaseInvoiceInstance.deliveryDate}" format="MM/dd/yyyy"/></td>

          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <div class="paginateButtons">
      <g:paginate total="${purchaseInvoiceInstanceTotal}" params="${params}"/>
    </div>
  </div>
</body>
</html>
