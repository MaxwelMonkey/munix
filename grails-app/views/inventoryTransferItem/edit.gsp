
<%@ page import="com.munix.InventoryTransferItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'inventoryTransferItem.label', default: 'InventoryTransferItem')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${inventoryTransferItemInstance}">
            <div class="errors">
                <g:renderErrors bean="${inventoryTransferItemInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${inventoryTransferItemInstance?.id}" />
                <g:hiddenField name="version" value="${inventoryTransferItemInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>

                          <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="transfer"><g:message code="inventoryTransferItem.transfer.label" default="Transfer" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: inventoryTransferItemInstance, field: 'transfer', 'errors')}">
                                    <g:link controller="inventoryTransfer" action="show" id="${inventoryTransferItemInstance?.transfer?.id}">${inventoryTransferItemInstance?.transfer}</g:link>
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="product"><g:message code="inventoryTransferItem.product.label" default="Product" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: inventoryTransferItemInstance, field: 'product', 'errors')}">
                                   ${inventoryTransferItemInstance?.product}
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
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
