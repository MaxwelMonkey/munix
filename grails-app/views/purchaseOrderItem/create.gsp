
<%@ page import="com.munix.PurchaseOrderItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseOrderItem.label', default: 'PurchaseOrderItem')}" />
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
    document.getElementById('code').value = arr[3];
    $('selector').hide();
    }
  </g:javascript>
</head>
<body>
<g:set value="${com.munix.PurchaseOrder.get(params?.id)}" var="purchaseOrderInstance" />

<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
</div>
<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]" /></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${purchaseOrderItemInstance}">
    <div class="errors">
      <g:renderErrors bean="${purchaseOrderItemInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:form action="save" method="post" >
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="po"><g:message code="purchaseOrderItem.po.label" default="PO" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'po', 'errors')}">

        <g:link action="show" controller="purchaseOrder" id="${purchaseOrderInstance?.id}">${purchaseOrderInstance}</g:link>

        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="product"><g:message code="purchaseOrderItem.product.label" default="Product" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'product', 'errors')}">
            <input type="hidden" id="product.id" name="product.id" value=""/>
        <g:textField id="item" name="item" value="" readOnly="true"/>
        <div id="selector">
          <table id="selector_table" class="mytable filterable">
            <thead>
              <tr>
                <th>Identifier</th>
                <th>Code</th>
                <th>Description</th>
                <th class="right">Price</th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${purchaseOrderInstance.supplier.items.sort{it.toString()}}" var="i">
              <tr onClick="fillData('${i.product.id}||${i.product.identifier}||${i.cost}||${i.productCode}')" id="items">
                <td>${i.product?.identifier}</td>
                <td>${i.productCode}</td>
                <td>${i.product?.description}</td>
                <td class="right">${i.cost}</td>

              </tr>
            </g:each>
            </tbody>
          </table>
        </div>
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="code"><g:message code="purchaseOrderItem.code.label" default="Supplier Code" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'code', 'errors')}">
        <g:textField readonly="readonly" name="code" value="" />
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="price"><g:message code="purchaseOrderItem.price.label" default="Price" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'price', 'errors')}">
        <g:textField readonly="readonly" name="price" value="${fieldValue(bean: purchaseOrderItemInstance, field: 'price')}" />
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="finalPrice"><g:message code="purchaseOrderItem.finalPrice.label" default="Final Price" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'finalPrice', 'errors')}">
        <g:textField name="finalPrice" value="${fieldValue(bean: purchaseOrderItemInstance, field: 'finalPrice')}" />
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="qty"><g:message code="purchaseOrderItem.qty.label" default="Qty" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: purchaseOrderItemInstance, field: 'qty', 'errors')}">
        <g:textField name="qty" value="${fieldValue(bean: purchaseOrderItemInstance, field: 'qty')}" />
        </td>
        </tr>
        </tbody>
      </table>
    </div>

    <div class="buttons">
      <input type="hidden" name="id" value="${purchaseOrderInstance?.id}"/>
      <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
