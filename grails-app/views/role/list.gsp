<%@ page import="com.munix.Role" %>
<head>
  <meta name="layout" content="main" />
  <title>Role List</title>
</head>

<body>

  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
    <span class="menuButton"><g:link class="create" action="create">New Role</g:link></span>
  </div>

  <div class="body">
    <h1>Role List</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
      <table id="roleListTable">
        <thead>
          <tr>
        <g:sortableColumn property="authority" title="Role Name" />
        <g:sortableColumn property="description" title="Description" />
        </tr>
        </thead>
        <tbody>
        <g:each in="${authorityList}" status="i" var="authority">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}role/show/${authority.id}'">
            <td id="rowRoleName${i}">${authority.authority?.encodeAsHTML()}</td>
            <td id="rowRoleDescription${i}">${authority.description?.encodeAsHTML()}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>

  </div>
</body>
