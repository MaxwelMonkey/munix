
<%@ page import="com.munix.MaterialRequisitionItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'materialRequisitionItem.label', default: 'MaterialRequisitionItem')}" />
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
    document.getElementById('component.id').value = arr[0];
    document.getElementById('item').value = arr[1];
    $('selector').hide();
    }

  </g:javascript>
</head>
<body>
<g:set var="materialRequisitionInstance" value="${com.munix.MaterialRequisition.get(params.id)}" />
<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
</div>
<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]" /></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${materialRequisitionItemInstance}">
    <div class="errors">
      <g:renderErrors bean="${materialRequisitionItemInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:form action="save" method="post" >
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="requisition"><g:message code="materialRequisitionItem.requisition.label" default="Requisition" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: materialRequisitionItemInstance, field: 'requisition', 'errors')}">
        <g:link action="show" controller="materialRequisition" id="${materialRequisitionInstance?.id}">${materialRequisitionInstance}</g:link>
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="component"><g:message code="materialRequisitionItem.component.label" default="Component" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: materialRequisitionItemInstance, field: 'component', 'errors')}">
            <input type="hidden" name="component.id" id="component.id"/>
        <g:textField name="item" id="item" value="${salesInvoiceItemInstance?.product?.id}" readonly="readonly"/>
        <div id="selector">
          <table id="selector_table" class="mytable filterable">
            <thead>
              <tr>
                <th>Identifier</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${com.munix.Product.list().findAll{it.isComponent == true}.sort{it.toString()}}" var="i">
              <tr onClick="fillData('${i.id}||${i.identifier}')" id="items">
                <td>${i.identifier}</td>
                <td>${i.description}</td>
              </tr>
            </g:each>
            </tbody>
          </table>
        </div>
        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="qty"><g:message code="materialRequisitionItem.qty.label" default="Qty" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: materialRequisitionItemInstance, field: 'qty', 'errors')}">
        <g:textField name="qty" value="${fieldValue(bean: materialRequisitionItemInstance, field: 'qty')}" />
        </td>
        </tr>

        </tbody>
      </table>
    </div>
    <div class="buttons">
      <input type="hidden" name="id" id="id" value="${params?.id}"/>
      <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
