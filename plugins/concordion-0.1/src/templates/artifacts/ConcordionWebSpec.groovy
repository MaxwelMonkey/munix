<html xmlns:concordion="http://www.concordion.org/2007/concordion" xmlns:ext="urn:concordion-extensions:2010">
<head>
    <title>@artifact.capitalizedName@</title>
</head>
<body>

    <h1>@artifact.capitalizedName@</h1>

    <div style="background-color: #aaa; padding: 1em;">
        <p>
            Note that the following is a template example for the "@artifact.name@" specification that has been kindly 
            generated by the <a href="http://grails.org/plugin/concordion">Grails Concordion plugin</a>.
            You should change this example to adapt it conveniently to your application.
	</p>
    </div>

    <hr/>

    <h2>Grails homepage title</h2>

    <p>
       The Grails homepage's title is 
       "<span concordion:assertEquals="grailsHomePageTitle()">Grails - The search is over.</span>".
    </p>

    <h2>Further Details</h2>
    <ul>
        <li><a href="#">Other active specification</a></li>
        <li><a href="#">Another one specification</a></li>
    </ul>

</body>
</html>
