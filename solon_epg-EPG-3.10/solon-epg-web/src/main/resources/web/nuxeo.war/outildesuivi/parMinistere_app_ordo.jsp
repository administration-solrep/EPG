<%
String context = request.getContextPath();
String legisType = request.getParameter("typeLegislature");
if (legisType == null || legisType.equals("null")) {
	legisType = "courante";
}
%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Par Minist&#232;re</title>
<link rel="icon" type="image/png" href="<%=context%>/icons/favicon.png" />
<style>
 body, td {
  font: normal 12px "Lucida Grande", sans-serif;
  color: #000000;
  }
  a,  a:hover, a:visited{
 	color:#774743;
  }
  .isLink{
  	cursor: pointer;
  	font-weight: bold;
  }
</style>
<script type="text/javascript" src="../technique/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
   //$("#bilanDiv").load("openDocument.jsp?fileName=bilan_semestriels_ministere_tous_ordonnance");
   $.post("openDocument.jsp", {fileName:"bilan_semestriels_ministere_tous_ordonnance",typeLegislature:"<%=legisType%>"}, function(data){$("#bilanDiv").html(data);});
   //$("#delaiDiv").load("openDocument.jsp?fileName=delai_ministere_tous_app_ordo");
   $.post("openDocument.jsp", {fileName:"delai_ministere_tous_app_ordo",typeLegislature:"<%=legisType%>"}, function(data){$("#delaiDiv").html(data);});
   if($.browser.mozilla)$("#bkgImg").css("position", "fixed");
   else window.onscroll = function(){
		document.getElementById("bkgImg").style.top = document.body.scrollTop+5;
		document.getElementById("bkgImg").style.left = document.body.scrollLeft+5;
	}
   jQuery('#bilanSpan').click(function() {
	    var fooOffset = jQuery('#bilanDiv').offset(),
	        destination = fooOffset.top;
	    jQuery(document).scrollTop(destination);
	});
	jQuery('#delaiSpan').click(function() {
    var fooOffset = jQuery('#delaiDiv').offset(),
        destination = fooOffset.top;
    jQuery(document).scrollTop(destination);
});
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
				<a href="application_ordonnances.jsp?legislature=<%=legisType%>">Suivi de l'application des ordonnances</a>
				&nbsp;/&nbsp;
				Situation par minist&#232;re
			</td>
		</tr>
		<tr height="60px">
			<td>
				<span class="isLink" id="bilanSpan">>&nbsp;Dernier bilan semestriel</span>
				<br/>
				<span class="isLink" id="delaiSpan">>&nbsp;D&#233;lais de publication des mesures d'application par minist&#232;re</span>
			</td>
		</tr>
		<tr>
			<td>
				<div id="bilanDiv" style="width:100%;height:100%;overflow:visible"></div>
			</td>
		</tr>
		<tr>
			<td>
				<div id="delaiDiv" style="width:100%;height:100%;overflow:visible"></div>
			</td>
		</tr>
	</table>
	<style>.style_7 { white-space: normal;}</style>
</body>

</html>