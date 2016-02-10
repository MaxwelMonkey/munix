
<%@ page import="com.munix.MaterialReleaseItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'materialReleaseItem.label', default: 'MaterialReleaseItem')}" />
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
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'materialReleaseItem.id.label', default: 'Id')}" />
                        
                            <th><g:message code="materialReleaseItem.component.label" default="Component" /></th>
                   	    
                            <g:sortableColumn property="qty" title="${message(code: 'materialReleaseItem.qty.label', default: 'Qty')}" />
                        
                            <th><g:message code="materialReleaseItem.materialRelease.label" default="Material Release" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${materialReleaseItemInstanceList}" status="i" var="materialReleaseItemInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${materialReleaseItemInstance.id}">${fieldValue(bean: materialReleaseItemInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: materialReleaseItemInstance, field: "component")}</td>
                        
                            <td>${fieldValue(bean: materialReleaseItemInstance, field: "qty")}</td>
                        
                            <td>${fieldValue(bean: materialReleaseItemInstance, field: "materialRelease")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${materialReleaseItemInstanceTotal}" />
            </div>
        </div>
    </body>
</html>