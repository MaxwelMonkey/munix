<head>
  <title>Login</title>
  <link rel="stylesheet" href="${resource(dir:'css',file:'login.css')}" />
</head>

<body>
  <div class="logo">
    <a href="${createLink(uri:'/')}"><img src="${resource(dir:'images',file:'munix.png')}" alt="Grails" border="0" /></a>
    <label for='appVersion' style="color:white">v${appVersion}</label>
  </div>
  <div id='login'>
    <div class='inner'>
      <g:if test='${flash.message}'>
        <div class='login_message'>${flash.message}</div>
      </g:if>
      <g:if test='${session.userAccess=="F"}'>
        <div class='login_message'>Sorry, you don't have access to the system.</div>
      </g:if>
      <form action='${postUrl}' method='POST' id='loginForm' class='cssform'>
        <p>
          <label for='j_username'>Username</label><br />
          <input type='text' class='text_' name='j_username' id='j_username' value='${request.remoteUser}' /><br />
        </p>
        <p>
          <label for='j_password'>Password</label><br />
          <input type='password' class='text_' name='j_password' id='j_password' /><br />
        </p>
        <p>
          <button id="login" type="submit">Login</button>
        </p>
      </form>
    </div>
  </div>
  <script type='text/javascript'>
  <!--
  (function(){
          document.forms['loginForm'].elements['j_username'].focus();
  })();
  // -->
  </script>
</body>
