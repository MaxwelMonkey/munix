<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <title>Munix - Select Bank Account</title>
  </head>
  <body>
    <h2>Select Bank Account</h2>
    <g:form action="${submit}" method="post">
    	<input type="hidden" name="id" value="${id}">
      <div class="dialog">
        <table>
          <tbody>

            <tr class="prop">
              <td valign="top" class="name">
                <label for="bankAccount">Please select a Bank Account.</label>
              </td>
              <td valign="top" class="value">
			    <g:select name="bankAccount.id" from="${bankAccounts}" optionKey="id"/>
              </td>
            </tr>
          </tbody>
        </table>
      <div class="buttons">
        <span class="button"><g:submitButton name="create" class="save" value="Submit" /></span>
        <span class="button"><g:link class="cancel" controller="${controller}" action="${action}" id="${id}">Cancel</g:link></span>
      </div>
      </div>
     </g:form>
  </body>
</html>
