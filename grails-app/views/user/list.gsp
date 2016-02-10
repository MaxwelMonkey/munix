<%@ page import="com.munix.User" %>
<head>
	<meta name="layout" content="main" />
	<title>User List</title>
</head>

<body>

	<div class="nav">
		<span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
		<span class="menuButton"><g:link class="create" action="create">New User</g:link></span>
	</div>

	<div class="body">
		<h1>User List</h1>
		<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
		</g:if>
		<div class="list">
			<table id="userListTable">
			<thead>
				<tr>
					<g:sortableColumn property="username" title="Login Name" />
					<g:sortableColumn property="userRealName" title="Full Name" />
					<g:sortableColumn property="enabled" title="Enabled" />
					<g:sortableColumn property="description" title="Description" />
				</tr>
			</thead>
			<tbody>
			<g:each in="${personList}" status="i" var="person">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}user/show/${person.id}'">
					<td id="rowUserName${i}">${person.username?.encodeAsHTML()}</td>
					<td id="rowRealName${i}">${person.userRealName?.encodeAsHTML()}</td>
					<td id="rowEnabled${i}">${person.enabled?.encodeAsHTML()}</td>
					<td id="rowDescription${i}">${person.description?.encodeAsHTML()}</td>
				</tr>
			</g:each>
			</tbody>
			</table>
		</div>

	</div>
</body>
