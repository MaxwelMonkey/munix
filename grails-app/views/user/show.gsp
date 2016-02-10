<%@ page import="com.munix.User" %>
<head>
  <meta name="layout" content="main" />
  <title>Show User</title>
</head>

<body>

  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list">User List</g:link></span>
    <span class="menuButton"><g:link class="create" action="create">New User</g:link></span>
  </div>

  <div class="body">
    <h1>Show User</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <div class="dialog">
      <table>
        <tbody>

          <tr class="prop">
            <td valign="top" class="name">Login Name:</td>
            <td id="loginName" valign="top" class="value">${person.username?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Full Name:</td>
            <td id="fullName" valign="top" class="value">${person.userRealName?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Enabled:</td>
            <td id="enabledCheckbox" valign="top" class="value">${person.enabled}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Description:</td>
            <td id="userDescription" valign="top" class="value">${person.description?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Email:</td>
            <td id="userEmail" valign="top" class="value">${person.email?.encodeAsHTML()}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Show Email:</td>
            <td id="showEmail" valign="top" class="value">${person.emailShow}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Email Sales Report:</td>
            <td id="emailSalesReport" valign="top" class="value">${person.emailSalesReport}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Sales Delivery Printout Reviewer:</td>
            <td id="emailSalesReport" valign="top" class="value">${person.sdPrintoutReviewer}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Access Time:</td>
            <td id="accessTime" valign="top" class="value">${formatDate(date:person.startAccess, format:"h:mm a")} - ${formatDate(date:person.endAccess, format:"h:mm a")}</td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name">Roles:</td>
            <td id="userRoles" valign="top" class="value">
              <ul>
                <g:each in="${roleNames}" var='name'>
                  <li>${name}</li>
                </g:each>
              </ul>
            </td>
          </tr>

        </tbody>
      </table>
    </div>

    <div class="buttons">
      <g:form>
        <input type="hidden" name="id" value="${person.id}" />
        <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
      </g:form>
    </div>

  </div>
</body>
