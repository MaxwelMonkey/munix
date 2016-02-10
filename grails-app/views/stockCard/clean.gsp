<%@ page import="com.munix.StockCard; com.munix.StockCardItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:javascript src="jquery.ui.core.js" />
        <title>Clean All Stock Cards</title>
        <script>
        	var stockCards = new Array();
        	<g:each in="${stockCards}" status="i" var="bean">
        		stockCards[stockCards.length]=${bean};
        	</g:each>
        	
        	var index = 0;
        	
        	function clean(){
        		$("#iframe").attr("src","${resource(dir:'stockCard',file:'recalculate')}/"+stockCards[index]);
        		$("#loading").show();
        		$("#current").html(index+1);
        	}
        	
        	function nextStockCard(){
        		index++;
        		clean();
        	}
        	
        	$(document).ready(function(){
        		$("#iframe").attr("onload", "nextStockCard();");
        	});
        </script>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
        </div>
        <div class="body">
        	<h1>Clean All Stock Cards</h1>
            <div class="dialog">
            	<input type="button" onclick="clean();" value="Start"/>
            	<span id="loading" style="display:none">
            		<img src="${resource(dir:'images',file:'spinner.gif')}" />
            		Processing <span id="current">0</span> out of ${stockCards.size()} Stock Cards... Please Wait..
            	</span><br/>
            	<iframe id="iframe" height="100" width="1000">
            	</iframe>
            </div>
        </div>
    </body>
</html>
