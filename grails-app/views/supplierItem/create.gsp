
<%@ page import="com.munix.SupplierItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'supplierItem.label', default: 'SupplierItem')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${supplierItemInstance}">
            <div class="errors">
                <g:renderErrors bean="${supplierItemInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="productCode"><g:message code="supplierItem.productCode.label" default="Product Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: supplierItemInstance, field: 'productCode', 'errors')}">
                                    <g:textField name="productCode" value="${supplierItemInstance?.productCode}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="product"><g:message code="supplierItem.product.label" default="Product" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: supplierItemInstance, field: 'product', 'errors')}">
                                    <g:select name="product.id" from="${com.munix.Product.list()}" optionKey="id" value="${supplierItemInstance?.product?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="price"><g:message code="supplierItem.price.label" default="Price" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: supplierItemInstance, field: 'price', 'errors')}">
                                    <g:textField name="price" value="${fieldValue(bean: supplierItemInstance, field: 'price')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="supplier"><g:message code="supplierItem.supplier.label" default="Supplier" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: supplierItemInstance, field: 'supplier', 'errors')}">
                                    <g:select name="supplier.id" from="${com.munix.Supplier.list()}" optionKey="id" value="${supplierItemInstance?.supplier?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                            <tr class="prop">
                            <td valign="top" class="name">
                              <label for="cost"><g:message code="supplierItem.status.label" default="Status" /></label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: supplierItemInstance, field: 'cost', 'errors')}">
                            <g:select from="${com.munix.SupplierItem.Status}" name="status" optionValue="description" value="${supplierItemInstance.status}"/>
                          </td>
                          </tr>
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
