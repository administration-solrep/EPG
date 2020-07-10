<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
String context = request.getContextPath();
String fileName = request.getParameter("fileName");
String title = request.getParameter("title");
String legisType = request.getParameter("typeLegislature");
if (legisType==null || legisType.equals("null")) {
	legisType = "courante";
}

String parentLink = "<a href='application_lois.jsp?legislature="+legisType+"'>Suivi de l'application des lois</a>";
if(request.getParameter("ratification")!=null && "true".equals(request.getParameter("ratification"))){
  parentLink = "<a href='suivi_ordonnances.jsp'>Suivi des ordonnances</a>";
}
if(request.getParameter("applicationOrdo")!=null && "true".equals(request.getParameter("applicationOrdo"))){
  parentLink = "<a href='application_ordonnances.jsp'>Application des ordonnances</a>";
}
%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Autres Elements</title>
<link rel="icon" type="image/png" href="<%=context%>/icons/favicon.png" />
<style>
 body, td {
  font: normal 12px "Lucida Grande", sans-serif;
  color: #000000;
  }
  a,  a:hover, a:visited{
 	color:#774743;
  }
</style>
<script type="text/javascript" src="../technique/jquery.min.js"></script>
<script type="text/javascript">

$(document).ready(function() {
	
   $.post("openDocument.jsp", {fileName:"<%=fileName%>",typeLegislature:"<%=legisType%>"}, function(data){
	   $("#mainDiv").html(data);
   });
   
   if($.browser.mozilla)$("#bkgImg").css("position", "fixed");
   else window.onscroll = function(){
		document.getElementById("bkgImg").style.top = document.body.scrollTop+5;
		document.getElementById("bkgImg").style.left = document.body.scrollLeft+5;
	}
  });
</script>
</head>

<body>
<img id="bkgImg" style="position:absolute;top:1px;left:1px;margin:1px;width:99%;height:99%;z-index:-1;opacity:.3;filter:alpha(opacity=30);" src="<%=context%>/img/theme_page_accueil/fond_sceaux.png">
	<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr height="30px">
			<td>
				<a href="<%=context%>/login.jsp">S.O.L.O.N.</a>
				&nbsp;/&nbsp;
				<%=parentLink%>
				&nbsp;/&nbsp;
				<%=title%>
			</td>
		</tr>
		<tr>
			<td>
				<div id="mainDiv" class="mainDiv" style="width:100%;height:100%;overflow:visible"></div>
			</td>
		</tr>
	</table>
</body>

</html>