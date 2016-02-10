
<%@ page import="com.munix.SalesOrderItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrderItem.label', default: 'SalesOrderItem')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <g:javascript>
    <munix:selectorConfig />
    //Selector
    Event.observe(window, 'load', function() {
    $('selector').hide();
    Event.observe('item', 'click', function(){
    $('selector').toggle();
    });
    });
    //Autocompletion
    function fillData(data){
	    var arr = data.split('||');
	    document.getElementById('product.id').value = arr[0];
	    document.getElementById('item').value = arr[1];
	    document.getElementById('price').value = arr[2];
	    document.getElementById('finalPrice').value = arr[2];
	    $('selector').hide();
	    computeAmount();
    }

    //compute amount
    function computeAmount(){
      var qty = document.getElementById('qty').value;
      var finalPrice = document.getElementById('finalPrice').value;
      document.getElementById('amount').value = qty * finalPrice
    }

  </g:javascript>
</head>
<body>
<g:set var="salesOrderInstance" value="${com.munix.SalesOrder.get(params?.id)}"/>
<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
</div>
<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]" /></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${salesOrderItemInstance}">
    <div class="errors">
      <g:renderErrors bean="${salesOrderItemInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:form action="save" method="post" >
    <div class="dialog">
      <table>
        <tbody>
          <tr class="prop">
            <td valign="top" class="name">
              <label for="invoice"><g:message code="salesOrderItem.invoice.label" default="Order" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesOrderItemInstance, field: 'invoice', 'errors')}">
        <g:link action="show" controller="salesOrder" id="${salesOrderInstance?.id}">${salesOrderInstance} </g:link>
        </td>
        </tr>
        <tr class="prop">
          <td valign="top" class="name">
            <label for="product"><g:message code="salesOrderItem.product.label" default="Product" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: salesOrderItemInstance, field: 'product', 'errors')}">
            <input type="hidden" name="product.id" id="product.id"/>
        <g:textField name="item" id="item" value="${salesOrderItemInstance?.product?.id}" readonly="readonly"/>
        <div id="selector">
          <table id="selector_table" class="mytable filterable">
            <thead>
              <tr>
                <th>Identifier</th>
                <th>Description</th>
                <th class="right">Price</th>
                <th class="right">${salesOrderInstance?.warehouse}</th>
              </tr>
            </thead>
            <tbody>
            
            <g:each in="${com.munix.Product.list().findAll{it.isForSale == true; it.type == salesOrderInstance?.discountType;}.sort{it.toString()}}" var="i">
              <tr onClick="fillData('${i.id}||${i.identifier}||${i.getProductPrice(salesOrderInstance.priceType)}')" id="items">
                <td>${i.identifier}</td>
                <td>${i.description}</td>
                <td class="right">${i.getProductPrice(salesOrderInstance.priceType)}</td>
                <td class="right">${i.getStock(salesOrderInstance?.warehouse)}</td>

              </tr>
            </g:each>
            </tbody>
          </table>
        </div>
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="price"><g:message code="salesOrderItem.price.label" default="Price" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: salesOrderItemInstance, field: 'price', 'errors')}">
        <g:textField readonly="readonly" name="price" value="${fieldValue(bean: salesOrderItemInstance, field: 'price')}" />
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="finalPrice"><g:message code="salesOrderItem.finalPrice.label" default="Final Price" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: salesOrderItemInstance, field: 'finalPrice', 'errors')}">
        <g:textField id="finalPrice" onKeydown="computeAmount();" onkeyup="computeAmount();" onblur="computeAmount();" name="finalPrice" value="${fieldValue(bean: salesOrderItemInstance, field: 'finalPrice')}" />
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="qty"><g:message code="salesOrderItem.qty.label" default="Qty" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: salesOrderItemInstance, field: 'qty', 'errors')}">
        <g:textField id="qty" onKeydown="computeAmount();" onkeyup="computeAmount();" onblur="computeAmount();" name="qty" value="${fieldValue(bean: salesOrderItemInstance, field: 'qty')}" />
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="amount"><g:message code="salesOrderItem.amount.label" default="Amount" /></label>
          </td>
          <td valign="top" class="value">
        <g:textField name="amount" readonly="readonly" value="" />
        </td>
        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <input type="hidden" name="id" value="${params?.id}"/>
      <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
