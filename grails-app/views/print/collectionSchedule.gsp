<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <link rel="stylesheet" href="${resource(dir:'css',file:'report.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:'form.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css/reports',file:type+'.css')}" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
  </head>
  <body>
    <h2>${title}</h2>
    <table class="main">
    	<thead>
    	<tr>
    		<td>
		    <div id="head">
		      <table>
		        <g:each in="${header}" var="value">
		        <tr class="prop">
		          <td class="name col1">${value[0]}</td>
		          <td class="value col2">${value[1]}</td>
		          <td class="name col3">${value[2]}</td>
		          <td class="value col4">${value[3]}</td>
		        </tr>
		        </g:each>
		      </table>
		    </div>
		  	</td>
		</tr>
		</thead>
		


    	<tbody>
    	<tr>
    		<td>
		    <div id="body">
		      <table class="form">
		      	<g:if test="${columns}">
			        <thead>
			        <g:if test="${tableTitle}">
			        <tr>
			        <th class="title" colspan="99">${tableTitle}</th>
			        </tr>
			        </g:if>
			        <tr>
			        <g:each in="${columns}" var="column" status="i">
			        <th class="column${i}">${column}</th>
			        </g:each>
			        </tr>
			        </thead>
		        </g:if>
		        <g:if test="${list}">
			        <tbody>
			        <g:each in="${list}" var="row" status="i">
			          <tr class="row${i}" style="border-top:solid 1px #cccccc;">
				        <g:each in="${row}" var="cell" status="j">
			            <td class="cell${j}">${cell}</td>
			            </g:each>
			          </tr>
			        </g:each>
			        </tbody>
		        </g:if>
		        <g:if test="${totals}">
			          <g:each in="${totals}" var="totalRow">
			          <tr class="totals">
				        <g:each in="${totalRow}" var="total">
			            <td>${total}</td>
			            </g:each>
			          </tr>
			          </g:each>
		        </g:if>
		      </table>
			<br>
		      <table class="form">
		        <g:if test="${list2}">
			        <thead>
			        <g:if test="${tableTitle2}">
			        <tr>
			        <th class="title" colspan="99">${tableTitle2}</th>
			        </tr>
			        </g:if>
			        <tr>
			        <g:each in="${columns2}" var="column" status="i">
			        <th class="column${i}">${column}</th>
			        </g:each>
			        </tr>
			        </thead>
			        <tbody>
			        <g:each in="${list2}" var="row" status="i">
			          <tr class="row${i}" style="border-top:solid 1px #cccccc;">
				        <g:each in="${row}" var="cell" status="j">
			            <td class="cell${j}">${cell}</td>
			            </g:each>
			          </tr>
			        </g:each>
			        </tbody>
		        </g:if>
		        <g:if test="${totals2}">
			          <g:each in="${totals2}" var="totalRow">
			          <tr class="totals">
				        <g:each in="${totalRow}" var="total">
			            <td>${total}</td>
			            </g:each>
			          </tr>
			          </g:each>
		        </g:if>
		      </table>
			<br>
		      <table class="form">
		        <g:if test="${list3}">
			        <thead>
			        <g:if test="${tableTitle3}">
			        <tr>
			        <th class="title" colspan="99">${tableTitle3}</th>
			        </tr>
			        </g:if>
			        <tr>
			        <g:each in="${columns3}" var="column" status="i">
			        <th class="column${i}">${column}</th>
			        </g:each>
			        </tr>
			        </thead>
			        <tbody>
			        <g:each in="${list3}" var="row" status="i">
			          <tr class="row${i}">
				        <g:each in="${row}" var="cell" status="j">
			            <td class="cell${j}">${cell}</td>
			            </g:each>
			          </tr>
			        </g:each>
			        </tbody>
		        </g:if>
		        <g:if test="${totals3}">
			          <g:each in="${totals3}" var="totalRow">
			          <tr class="totals">
				        <g:each in="${totalRow}" var="total">
			            <td>${total}</td>
			            </g:each>
			          </tr>
			          </g:each>
		        </g:if>
		      </table>
		    </div>

		    <div id="footer">
		      <table>
		        <g:each in="${footer}" var="value">
		        <tr class="prop">
		          <td class="name col1">${value[0]}</td>
		          <td class="value col2">${value[1]}</td>
		          <td class="name col3">${value[2]}</td>
		          <td class="value col4">${value[3]}</td>
		        </tr>
		        </g:each>
		      </table>
		    </div>
		  	</td>
		</tr>
		</tbody>
	</table>
  </body>
</html>
