<html>
  <head>
    <title>Munix - Home</title>
    <meta name="layout" content="main" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'home.css')}" />
  </head>
  <body>
    <div id="pageBody">
    	<ul>
    	<g:each in="${sections}" var="section">
    		<li><a href="#${section.class}">${section.title}</a></li>
    	</g:each>
    	</ul>
    	<br>
      <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    	<g:each in="${sections}" var="section">
      	<div class="section ${section.class}">
      		<a name="${section.class}"></a>
			<h3>${section.title}</h3>
		    <div class="list">
			<table>
				<thead>
					<tr>
						<g:each in="${section.columns}" var="column">
						<th>${column}</th>
						</g:each>
					</tr>
				</thead>
				<tbody>
					<g:each in="${section.list}" var="row" status="i">
			          	<tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location='${createLink(uri:'/')}${section.object}/show/${row[0]}'">
							<g:each in="${row}" var="cell" status="j">
							<g:if test="${j>0}">
							<td>${cell}</td>
							</g:if>
							</g:each>
						</tr>
					</g:each>
				</tbody>
			</table>
			</div>
      	</div>
      	</g:each>

    </div>
  </body>
</html>