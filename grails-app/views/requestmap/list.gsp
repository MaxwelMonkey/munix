<%@ page import="com.munix.Requestmap" %>
<head>
  <meta name="layout" content="main" />
  <title>Requestmap List</title>
</head>

<body>

  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create">New Requestmap</g:link></span>
  </div>

  <div class="body">
    <h1>Requestmap List</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <table id="requestmapListTable">
        <thead>
          <tr>
        <g:sortableColumn property="url" title="URL Pattern" />
        <g:sortableColumn property="configAttribute" title="Roles" />
        </tr>
        </thead>
        <tbody>
        <g:each in="${requestmapList}" status="i" var="requestmap">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}requestmap/show/${requestmap.id}'">
            <td id="rowUrl${i}">${requestmap.url?.encodeAsHTML()}</td>
            <td id="rowRoles${i}">${requestmap.configAttribute}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

  </div>
</body>
