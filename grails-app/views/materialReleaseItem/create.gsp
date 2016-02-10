
<%@ page import="com.munix.MaterialReleaseItem" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'materialReleaseItem.label', default: 'MaterialReleaseItem')}" />
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
<g:set var="materialReleaseInstance" value="${com.munix.MaterialRelease.get(params.id)}"/>
<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
  <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
</div>
<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]" /></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${materialReleaseItemInstance}">
    <div class="errors">
      <g:renderErrors bean="${materialReleaseItemInstance}" as="list" />
    </div>
  </g:hasErrors>
  <g:form action="save" method="post" >
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name">
              <label for="materialRelease"><g:message code="materialReleaseItem.materialRelease.label" default="Material Release" /></label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: materialReleaseItemInstance, field: 'materialRelease', 'errors')}">
        <g:link action="show" conntroller="materialRelease" id="${materialReleaseItemInstance?.materialRelease?.id}">${materialReleaseInstance}</g:link>

        </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="component"><g:message code="materialReleaseItem.component.label" default="Component" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: materialReleaseItemInstance, field: 'component', 'errors')}">

            <input type="hidden" id="component.id" name="component.id" value=""/>
        <g:textField id="item" name="item" value="" readOnly="true"/>
        <div id="selector">
          <table id="selector_table" class="mytable filterable">
            <thead>
              <tr>
                <th>Identifier</th>
                <th>Description</th>

              </tr>
            </thead>
            <tbody>
            <g:each in="${com.munix.Product.list().sort{it.description}}" var="i">
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
            <label for="qty"><g:message code="materialReleaseItem.qty.label" default="Qty" /></label>
          </td>
          <td valign="top" class="value ${hasErrors(bean: materialReleaseItemInstance, field: 'qty', 'errors')}">
        <g:textField name="qty" value="${fieldValue(bean: materialReleaseItemInstance, field: 'qty')}" />
        </td>
        </tr>



        </tbody>
      </table>
    </div>
    <div class="buttons">
      <input type="hidden" id="id" name="id" value="${materialReleaseInstance.id}">
      <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
    </div>
  </g:form>
</div>
</body>
</html>
