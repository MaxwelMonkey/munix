
<%@ page import="com.munix.CheckStatus" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="checkStatus.list" default="Check Status List" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="checkStatus.new" default="New Check Status" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="checkStatus.list" default="Check Status List" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <div class="list">
                <table id="chekStatusListTable">
                    <thead>
                        <tr>

                   	    <g:sortableColumn property="identifier" title="Identifier" titleKey="checkStatus.identifier" />
                        
                   	    <g:sortableColumn property="description" title="Description" titleKey="checkStatus.description" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${checkStatusInstanceList}" status="i" var="checkStatusInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}checkStatus/show/${checkStatusInstance?.id}'">
                        
                            <td id="rowCheckStatus${i}">${fieldValue(bean: checkStatusInstance, field: "identifier")}</td>
                        
                            <td>${fieldValue(bean: checkStatusInstance, field: "description")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${checkStatusInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
