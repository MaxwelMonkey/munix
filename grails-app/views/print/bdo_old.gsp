<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <link rel="stylesheet" href="${resource(dir:'css',file:'report.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'form.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'bdo.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:type+'.css')}" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
  </head>
  <body>
    <h2></h2>
    <table class="main">
    	<tr>
    		<td>
    		<div id="pad">
    			${bean}<br>
    			${bean.billsPurchase?"BP":""}
    		</div>
		    <div id="head">
		      <table>
		        <tr class="prop">
		          <td class="value col1">${bean.account?.accountNumber}</td>
		          <td class="value col2">${formatDate(date:bean?.depositDate, format:"MMM. dd, yyyy")}</td>
		        </tr>
		        <tr class="prop">
		          <td class="value" colspan="2"><div id="accountName">${bean.account?.accountName}</div></td>
		        </tr>
		      </table>
		    </div>
		  	</td>
		</tr>
		


    	<tbody>
    	<tr>
    		<td>
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
		</tr>
		</tbody>
	</table>
  </body>
</html>