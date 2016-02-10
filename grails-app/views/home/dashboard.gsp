<html>
  <head>
    <title>Munix - Home</title>
    <meta name="layout" content="main" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'home.css')}" />
  </head>
  <body>
    <div id="pageBody">
      <div>
          <ul>
			<g:ifAnyGranted role="ROLE_PURCHASING">
                <li><a href="${createLink(controller:'home',action:'purchasing')}">Purchasing Home</a></li>
			</g:ifAnyGranted>
			<g:ifAnyGranted role="ROLE_SALES">
                <li><a href="${createLink(controller:'home',action:'sales')}">Sales Home</a></li>
			</g:ifAnyGranted>
			<g:ifAnyGranted role="ROLE_PRODUCTION">
                <li><a href="${createLink(controller:'home',action:'production')}">Production Home</a></li>
			</g:ifAnyGranted>
			<g:ifAnyGranted role="ROLE_ACCOUNTING">
                <li><a href="${createLink(controller:'home',action:'accounting')}">Accounting Home</a></li>
			</g:ifAnyGranted>
			<g:ifAnyGranted role="ROLE_DELIVERY">
                <li><a href="${createLink(controller:'home',action:'delivery')}">Delivery Home</a></li>
			</g:ifAnyGranted>
	      </ul>
      </div>

    </div>
  </body>
</html>