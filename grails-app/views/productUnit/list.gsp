
<%@ page import="com.munix.ProductUnit" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'productUnit.label', default: 'ProductUnit')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table id="unitListTable">
                    <thead>
                        <tr>

                            <g:sortableColumn property="identifier" title="${message(code: 'productUnit.identifier.label', default: 'Identifier')}" />
                        
                            <g:sortableColumn property="description" title="${message(code: 'productUnit.description.label', default: 'Description')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${productUnitInstanceList}" status="i" var="productUnitInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}productUnit/show/${productUnitInstance.id}'">

                            <td id="rowUnit${i}">${fieldValue(bean: productUnitInstance, field: "identifier")}</td>
                        
                            <td>${fieldValue(bean: productUnitInstance, field: "description")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${productUnitInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
