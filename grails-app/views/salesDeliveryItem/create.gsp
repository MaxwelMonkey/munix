
<%@ page import="com.munix.SalesDeliveryItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesDeliveryItem.label', default: 'SalesDeliveryItem')}" />
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
    document.getElementById('qty').value = arr[3];
    document.getElementById('remaining').value = arr[3];
    $('selector').hide();
    computeAmount();
    }

    //compute amount
    function computeAmount(){
    var qty = document.getElementById('qty').value;
    var finalPrice = document.getElementById('finalPrice').value;
    document.getElementById('amount').value = qty * finalPrice
    }

    function checkRemainingBalance(){
    var remainingBalance = parseFloat(document.getElementById('remaining').value);
    var qty = parseFloat(document.getElementById('qty').value);
    if(qty > remainingBalance){
    alert('The entered quantity is above the remaining balance');
    document.getElementById('qty').value = remainingBalance;
    return false;
    }
    else{
    return true;
    }
    }

  </g:javascript>
</head>
<body>
<g:set var="salesDeliveryInstance" value="${com.munix.SalesDelivery.get(params?.id)}" />
<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
</div>
<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]" /></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${salesDeliveryItemInstance}">
    <div class="errors">
      <g:renderErrors bean="${salesDeliveryItemInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:form action="save" method="post" >
    <div class="dialog">
      <table>
        <tbody>
          <tr class="prop">
            <td valign="top" class="name">
              <label for="delivery"><g:message code="salesDeliveryItem.delivery.label" default="Delivery" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: salesDeliveryItemInstance, field: 'delivery', 'errors')}">
        <g:link action="show" controller="salesDelivery" id="${salesDeliveryInstance?.id}">${salesDeliveryInstance}</g:link>
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="product"><g:message code="salesDeliveryItem.product.label" default="Product" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: salesDeliveryItemInstance, field: 'product', 'errors')}">
            <input type="hidden" name="product.id" id="product.id"/>
        <g:textField name="item" id="item" value="" readonly="readonly"/>
        <div id="selector">
          <table id="selector_table" class="mytable filterable">
            <thead>
              <tr>
                <th>Identifier</th>
                <th>Description</th>
                <th class="right">Price</th>
                <th class="right">Remaining</th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${salesDeliveryInstance?.invoice?.items?.sort{it.product.toString()}}" var="i">
              <tr onClick="fillData('${i?.product?.id}||${i?.product?.identifier}||${i?.finalPrice}||${i?.computeRemainingBalance()}')" id="items">
                <td>${i.product.identifier}</td>
                <td>${i.product.description}</td>
                <td class="right">${i.finalPrice}</td>
                <td class="right">${i.computeRemainingBalance()}</td>
              </tr>
            </g:each>
            </tbody>
          </table>
        </div>
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="price"><g:message code="salesDeliveryItem.price.label" default="Price" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: salesDeliveryItemInstance, field: 'price', 'errors')}">
        <g:textField name="price" readonly="readonly" value="${fieldValue(bean: salesDeliveryItemInstance, field: 'price')}" />
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="qty"><g:message code="salesDeliveryItem.qty.label" default="Qty" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: salesDeliveryItemInstance, field: 'qty', 'errors')}">
        <g:hiddenField name="remaining" />
        <g:textField onblur="checkRemainingBalance();" onkeyup="checkRemainingBalance();" onkeypress="checkRemainingBalance();" name="qty" value="${fieldValue(bean: salesDeliveryItemInstance, field: 'qty')}" />
        </td>
        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <input type="hidden" name="id" value="${salesDeliveryInstance?.id}"/>
      <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
