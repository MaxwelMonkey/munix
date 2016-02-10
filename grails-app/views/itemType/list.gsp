
<%@ page import="com.munix.ItemType" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="itemType.list" default="Item Type List" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="itemType.new" default="New Item Type" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="itemType.list" default="Item Type List" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <div class="list">
                <table id="itemTypeListTable">
                    <thead>
                        <tr>

                   	    <g:sortableColumn property="identifier" title="Identifier" titleKey="itemType.identifier" />
                        
                   	    <g:sortableColumn property="description" title="Description" titleKey="itemType.description" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${itemTypeInstanceList}" status="i" var="itemTypeInstance" >
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}itemType/show/${itemTypeInstance.id}'">

                            <td id="rowItemType${i}">${fieldValue(bean: itemTypeInstance, field: "identifier")}</td>
                        
                            <td>${fieldValue(bean: itemTypeInstance, field: "description")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${itemTypeInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
