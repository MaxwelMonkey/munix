
<%@ page import="com.munix.WaybillPackage" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'waybillPackage.label', default: 'WaybillPackage')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'waybillPackage.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="description" title="${message(code: 'waybillPackage.description.label', default: 'Description')}" />
                        
                            <th><g:message code="waybillPackage.packaging.label" default="Packaging" /></th>
                   	    
                            <g:sortableColumn property="qty" title="${message(code: 'waybillPackage.qty.label', default: 'Qty')}" />
                        
                            <th><g:message code="waybillPackage.waybill.label" default="Waybill" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${waybillPackageInstanceList}" status="i" var="waybillPackageInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${waybillPackageInstance.id}">${fieldValue(bean: waybillPackageInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: waybillPackageInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: waybillPackageInstance, field: "packaging")}</td>
                        
                            <td>${fieldValue(bean: waybillPackageInstance, field: "qty")}</td>
                        
                            <td>${fieldValue(bean: waybillPackageInstance, field: "waybill")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${waybillPackageInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
