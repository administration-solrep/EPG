<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
String context = request.getContextPath();
String loiNb = request.getParameter("loiNb");
String titre = request.getParameter("title");
String legisType = request.getParameter("typeLegislature");
if (legisType == null || legisType.equals("null")) {
	legisType = "courante";
}
%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Elements De Suivi</title>
<link rel="icon" type="image/png" href="<%=context%>/icons/favicon.png" />
<style>
  .mainTbl .mainTd{
  	font: normal 12px "Lucida Grande", sans-serif;
	color: #000000;
  }
  .mainTd{
  	padding-left:40px;
  }
  .title {
  	font: normal 14px "Lucida Grande", sans-serif;
  	vertical-align: bottom;
  }
  a, a:hover, a:visited{
	font: normal 12px "Lucida Grande", sans-serif;
 	color:#774743;
  }
  .isLink{
	font-weight: bold;
  	cursor: pointer;
  }
</style>
<script type="text/javascript" src="../technique/jquery.min.js"></script>
<script type="text/javascript">
var fileHgt=0;
$(document).ready(function() {
   //$("#tauxAppDiv").load("openDocument.jsp?fileName=tauxDapplicationAuFilDeLeauParLoiAffichageAvencee-<%//=loiNb%>");
   $.post("openDocument.jsp", {fileName:"tauxDapplicationAuFilDeLeauParLoiAffichageAvencee-<%=loiNb%>",typeLegislature:"<%=legisType%>"}, function(data){$("#tauxAppDiv").html(data);});
   //$("#repartitionDiv").load("openDocument.jsp?fileName=repartitionParMinistere-<%//=loiNb%>");
   $.post("openDocument.jsp", {fileName:"repartitionParMinistere-<%=loiNb%>",typeLegislature:"<%=legisType%>"}, function(data){$("#repartitionDiv").html(data);});
   
   $('#reportsDiv') .css({'width': (($(window).width()) - 40)+'px'});
   $(window).resize(function(){
       $('#reportsDiv') .css({'width': (($(window).width()) - 40)+'px'});
       fileHgt=(($(window).height()) - 110);
   });
  fileHgt=(($(window).height()) - 100);
   if($.browser.mozilla)$("#bkgImg").css("position", "fixed");
   else window.onscroll = function(){
		document.getElementById("bkgImg").style.top = document.body.scrollTop+5;
		document.getElementById("bkgImg").style.left = document.body.scrollLeft+5;
	}
  });
function reportsDivLoad(fileName) {
	/*$("#reportsDiv").load("openDocument.jsp?fileName="+fileName+"&fileHeight="+fileHgt, function(){
		$('html,body').animate({scrollTop: $('#reportsDiv').offset().top}, 1000);
		});*/
	$.post("openDocument.jsp", {fileName:fileName, fileHeight: fileHgt,typeLegislature:"<%=legisType%>"}, function(data){
		$("#reportsDiv").html(data);
		$('html,body').animate({scrollTop: $('#reportsDiv').offset().top}, 1000);
	});
}
</script>
</head>

<body>
<img id="bkgImg" style="position:absolute;top:1px;left:1px;margin:1px;width:99%;height:99%;z-index:-1;opacity:.3;filter:alpha(opacity=30);" src="<%=context%>/img/theme_page_accueil/fond_sceaux.png">
	<table class="mainTbl" width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr height="30px">
			<td>
				<a href="<%=context%>/login.jsp">S.O.L.O.N.</a>
				&nbsp;/&nbsp; 
				<a href="application_lois.jsp?legislature=<%=legisType%>">Suivi de l'application des lois</a>
				&nbsp;/&nbsp;
				<span style="font: normal 12px 'Lucida Grande', sans-serif;"><%=titre%></span>
			</td>
		</tr>
		<tr height="150px">
			<td id="tauxAppDiv" align="center">
			</td>
		</tr>
		<tr height="50px">
			<td class="title" align="center">
				R&#233;partition des mesures par minist&#232;re
			</td>
		</tr>
		<tr height="80px">
			<td align="center">
				<div id="repartitionDiv"></div>
			</td>
		</tr>
		<tr height="60px">
			<td  class="mainTd">
			
				<span class="isLink" onclick="reportsDivLoad('tableauDeSuivi-<%=loiNb%>');">
					&gt;&nbsp;Consulter le tableau de suivi des mesures d'application de la loi
				</span>
				<br/>
				<span class="isLink" onclick="reportsDivLoad('mesuresApplicaticationParLoiNonPubliees-<%=loiNb%>');">
					&gt;&nbsp;Consulter le tableau de suivi des seules mesures restant &#224; prendre
				</span>
				<br/>
				<span class="isLink" onclick="reportsDivLoad('delai_loi_tous');">
					&gt;&nbsp;D&#233;lai de publication des d&#233;crets par loi
				</span>
			</td>
		</tr>
		<tr height="100%">
			<td>
				<div id="reportsDiv" style="overflow-x:auto;overflow-y:visible"></div>
			</td>
		</tr>
	</table>
	<style>.style_5{border:none;}.style_6{border:none;}</style>
</body>
</html>