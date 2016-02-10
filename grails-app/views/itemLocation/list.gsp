
<%@ page import="com.munix.ItemLocation" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="itemLocation.list" default="Item Location List" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="itemLocation.new" default="New Item Location" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="itemLocation.list" default="Item Location List" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <div class="list">
                <table id="itemLocationListTable">
                    <thead>
                        <tr>
                        
                   	    <g:sortableColumn property="description" title="Description" titleKey="itemLocation.description" />
                        
                   	    <th><g:message code="itemLocation.warehouse" default="Warehouse" /></th>
                   	    
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${itemLocationInstanceList}" status="i" var="itemLocationInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}itemLocation/show/${itemLocationInstance.id}'">
                        
                            <td id="rowItemLocation${i}">${fieldValue(bean: itemLocationInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: itemLocationInstance, field: "warehouse")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${itemLocationInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
