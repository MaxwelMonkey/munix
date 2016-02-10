
<%@ page import="com.munix.InventoryTransferItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'inventoryTransferItem.label', default: 'InventoryTransferItem')}" />
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
    $('selector').hide();
    }
  </g:javascript>
</head>
<body>
<g:set var="inventoryTransferInstance" value="${com.munix.InventoryTransfer.get(params?.id)}" />
<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
</div>
<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]" /></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${inventoryTransferItemInstance}">
    <div class="errors">
      <g:renderErrors bean="${inventoryTransferItemInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:form action="save" method="post" >
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="transfer"><g:message code="inventoryTransferItem.transfer.label" default="Transfer" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: inventoryTransferItemInstance, field: 'transfer', 'errors')}">
        <g:link action="show" controller="inventoryTransfer" id="${inventoryTransferInstance?.id}">${inventoryTransferInstance}</g:link>
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="product"><g:message code="inventoryTransferItem.product.label" default="Product" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: inventoryTransferItemInstance, field: 'product', 'errors')}">

            <input type="hidden" id="product.id" name="product.id" value=""/>
        <g:textField id="item" name="item" value="" readOnly="true"/>
        <div id="selector">
          <table id="selector_table" class="mytable filterable">
            <thead>
              <tr>
                <th>Identifier</th>
                <th>Description</th>
                <th>SOH</th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${com.munix.Product.list().sort{it.toString()}}" var="i">
              <g:if test="${i.getStock(inventoryTransferInstance?.originWarehouse)?.qty > 0}">
                <tr onClick="fillData('${i.id}||${i.identifier}')" id="items">
                  <td>${i.identifier}</td>
                  <td>${i.description}</td>
                  <td>${i.formatSOH(inventoryTransferInstance?.originWarehouse)}</td>
                </tr>
              </g:if>
            </g:each>
            </tbody>
          </table>
        </div>
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="qty"><g:message code="inventoryTransferItem.qty.label" default="Qty" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: inventoryTransferItemInstance, field: 'qty', 'errors')}">
        <g:textField name="qty" value="${fieldValue(bean: inventoryTransferItemInstance, field: 'qty')}" />
        </td>
        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <input type="hidden" name="id" value="${inventoryTransferInstance?.id}"/>
      <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
