<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <link rel="stylesheet" href="${resource(dir:'css',file:'report.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'form.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'ewb.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:type+'.css')}" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
  </head>
  <body>
    <h2></h2>
    <table class="main">
    	<tr>
    		<td class="accountBreakdown">
		    <div id="body">
		      <table class="form">
		        <g:if test="${list}">
			        <tbody>
			        <g:each in="${list}" var="row" status="i">
			          <tr class="row${i}">
				        <g:each in="${row}" var="cell" status="j">
			            <td class="cell${j}">${cell}</td>
			            </g:each>
			          </tr>
			        </g:each>
			        </tbody>
		        </g:if>
		      </table>
		    </div>

		    <div id="footer">
		      <table>
		        <tr class="prop">
		          <td class="total1">${bean.checks.size()}</td>
		          <td class="total2">${bean.formatTotal()}</td>
		        </tr>
		      </table>
		    </div>
		  	</td>
    		<td class="accountDetails">
    		<div id="pad">
    			${formatDate(date:bean?.depositDate, format:"MMM. dd, yyyy")} ${bean}
    		</div>
		    <div id="head">
		      <table>
		        <tr class="prop">
		          <td class="value col1"><div id="accountNumber">${bean.account?.accountNumber?.substring(0,3)} ${bean.account?.accountNumber?.substring(3,5)} ${bean.account?.accountNumber?.substring(5,10)} ${bean.account?.accountNumber?.substring(10)}</div></td>
		        </tr>
		        <tr class="prop">
		          <td class="value"><div id="accountName">${bean.account?.accountName}</div></td>
		        </tr>
		        <tr class="prop">
		          <td class="value"><div id="totalAmount">${bean.formatTotal()}</div></td>
		        </tr>
		      </table>
		    </div>
		  	</td>
		</tr>
		</tbody>
	</table>
  </body>
</html>
