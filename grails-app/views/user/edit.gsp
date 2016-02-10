<%@ page import="com.munix.User" %>
<head>
  <meta name="layout" content="main" />
  <title>Edit User</title>
</head>

<body>

  <div class="nav">
    <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
    <span class="menuButton"><g:link class="list" action="list">User List</g:link></span>
    <span class="menuButton"><g:link class="create" action="create">New User</g:link></span>
  </div>

  <div class="body">
    <h1>Edit User</h1>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${person}">
      <div class="errors">
        <g:renderErrors bean="${person}" as="list" />
      </div>
    </g:hasErrors>

    <g:form>
      <input type="hidden" name="id" value="${person.id}" />
      <input type="hidden" name="version" value="${person.version}" />
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name"><label for="username">Login Name:</label></td>
              <td valign="top" class="value ${hasErrors(bean:person,field:'username','errors')}">
                <input type="text" id="username" name="username" value="${person.username?.encodeAsHTML()}"/>
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name"><label for="userRealName">Full Name:</label></td>
              <td valign="top" class="value ${hasErrors(bean:person,field:'userRealName','errors')}">
                <input type="text" id="userRealName" name="userRealName" value="${person.userRealName?.encodeAsHTML()}"/>
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name"><label for="passwd">Password:</label></td>
              <td valign="top" class="value ${hasErrors(bean:person,field:'passwd','errors')}">
                <input type="password" id="passwd" name="passwd" value="${person.passwd?.encodeAsHTML()}"/>
              </td>
            </tr>

            <tr class="prop">
              <td valign="top" class="name"><label for="enabled">Enabled:</label></td>
              <td valign="top" class="value ${hasErrors(bean:person,field:'enabled','errors')}">
          <g:checkBox name="enabled" value="${person.enabled}"/>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><label for="description">Description:</label></td>
            <td valign="top" class="value ${hasErrors(bean:person,field:'description','errors')}">
              <input type="text" id="description" name="description" value="${person.description?.encodeAsHTML()}"/>
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><label for="email">Email:</label></td>
            <td valign="top" class="value ${hasErrors(bean:person,field:'email','errors')}">
              <input type="text" id="email" name="email" value="${person?.email?.encodeAsHTML()}"/>
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><label for="emailShow">Show Email:</label></td>
            <td valign="top" class="value ${hasErrors(bean:person,field:'emailShow','errors')}">
          <g:checkBox name="emailShow" value="${person.emailShow}"/>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><label for="emailSalesReport">Email Sales Report:</label></td>
            <td valign="top" class="value ${hasErrors(bean:person,field:'emailSalesReport','errors')}">
          <g:checkBox name="emailSalesReport" value="${person.emailSalesReport}"/>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><label for="sdPrintoutReviewer">Sales Delivery Printout Reviewer:</label></td>
            <td valign="top" class="value ${hasErrors(bean:person,field:'sdPrintoutReviewer','errors')}">
          <g:checkBox name="sdPrintoutReviewer" value="${person.sdPrintoutReviewer}"/>
          </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><label for="email">Access Time:</label></td>
            <td valign="top" class="value ${hasErrors(bean:person,field:'email','errors')}">
              From: 
				<select name="startAccessTime" id="startAccessTime">
					<option value="00:00">12:00 AM</option>
					<option value="01:00">1:00 AM</option>
					<option value="02:00">2:00 AM</option>
					<option value="03:00">3:00 AM</option>
					<option value="04:00">4:00 AM</option>
					<option value="05:00">5:00 AM</option>
					<option value="06:00">6:00 AM</option>
					<option value="07:00">7:00 AM</option>
					<option value="08:00">8:00 AM</option>
					<option value="09:00">9:00 AM</option>
					<option value="10:00">10:00 AM</option>
					<option value="11:00">11:00 AM</option>
					<option value="12:00">12:00 PM</option>
					<option value="13:00">1:00 PM</option>
					<option value="14:00">2:00 PM</option>
					<option value="15:00">3:00 PM</option>
					<option value="16:00">4:00 PM</option>
					<option value="17:00">5:00 PM</option>
					<option value="18:00">6:00 PM</option>
					<option value="19:00">7:00 PM</option>
					<option value="20:00">8:00 PM</option>
					<option value="21:00">9:00 PM</option>
					<option value="22:00">10:00 PM</option>
					<option value="23:00">11:00 PM</option>
				</select>
              To: 
				<select name="endAccessTime" id="endAccessTime">
					<option value="00:00">12:00 AM</option>
					<option value="01:00">1:00 AM</option>
					<option value="02:00">2:00 AM</option>
					<option value="03:00">3:00 AM</option>
					<option value="04:00">4:00 AM</option>
					<option value="05:00">5:00 AM</option>
					<option value="06:00">6:00 AM</option>
					<option value="07:00">7:00 AM</option>
					<option value="08:00">8:00 AM</option>
					<option value="09:00">9:00 AM</option>
					<option value="10:00">10:00 AM</option>
					<option value="11:00">11:00 AM</option>
					<option value="12:00">12:00 PM</option>
					<option value="13:00">1:00 PM</option>
					<option value="14:00">2:00 PM</option>
					<option value="15:00">3:00 PM</option>
					<option value="16:00">4:00 PM</option>
					<option value="17:00">5:00 PM</option>
					<option value="18:00">6:00 PM</option>
					<option value="19:00">7:00 PM</option>
					<option value="20:00">8:00 PM</option>
					<option value="21:00">9:00 PM</option>
					<option value="22:00">10:00 PM</option>
					<option value="23:00">11:00 PM</option>
				</select><br/>
				<script>
					$("#startAccessTime").val("${formatDate(date:person.startAccess, format:"HH:mm")}");
					$("#endAccessTime").val("${formatDate(date:person.endAccess, format:"HH:mm")}");
				</script>
            </td>
          </tr>

          <tr class="prop">
            <td valign="top" class="name"><label for="authorities">Roles:</label></td>
            <td valign="top" class="value ${hasErrors(bean:person,field:'authorities','errors')}">
              <ul>
                <g:each var="entry" in="${roleMap}">
                  <li>${entry.key.authority.encodeAsHTML()}
                  <g:checkBox name="${entry.key.authority}" value="${entry.value}"/>
                  </li>
                </g:each>
              </ul>
            </td>
          </tr>

          </tbody>
        </table>
      </div>

      <div class="buttons">
        <span class="button"><g:actionSubmit class="save" value="Update" /></span>
      </div>

    </g:form>

  </div>
</body>
