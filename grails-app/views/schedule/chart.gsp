<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.munix.Schedule" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sample title</title>
    <link rel="stylesheet" href="${resource(dir:'css',file:'jsgantt.css')}"/>
  <g:javascript src="jsgantt.js" />
</head>
<body>
  <div style="position:relative" class="gantt" id="GanttChartDIV"></div>
<g:javascript>
  var g = new JSGantt.GanttChart('g',document.getElementById('GanttChartDIV'), 'day');
  g.setShowRes(1); // Show/Hide Responsible (0/1)
  g.setShowDur(1); // Show/Hide Duration (0/1)
  g.setShowComp(1); // Show/Hide % Complete(0/1)
  g.setCaptionType('Resource');  // Set to Show Caption (None,Caption,Resource,Duration,Complete)
  if( g ) {
  // Parameters (pID, pName,pStart,pEnd,pColor,pLink,pMile, pRes,  pComp, pGroup, pParent, pOpen, pDepend, pCaption)
  g.AddTaskItem(new JSGantt.TaskItem(1,'Define Chart API','','','ff0000','http://help.com', 0,'Brian',0, 1, 0, 1));

  g.AddTaskItem(new JSGantt.TaskItem(11,'Chart Object','7/20/2008','7/20/2008','ff00ff','http://www.yahoo.com', 1,'Shlomy',100, 0, 1, 1));
  g.Draw();
  }

  else
  {
  alert("not defined");
  }

</g:javascript>

</body>
</html>
