<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page language="java"%>
<%@ page import="org.nuxeo.runtime.api.Framework"%>
<%@ page import="java.util.Locale"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
String context = request.getContextPath();
Locale locale = request.getLocale();
String language = locale == null ? "en" : locale.getLanguage();
String country = locale == null ? "US" : locale.getCountry();
%>
<html>
<fmt:setLocale value="fr" scope="session"/>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>S.O.L.O.N. / Suivi des Ordonnances</title>
    <link rel="icon" type="image/png" href="<%=context%>/icons/favicon.png" />
    <style type="text/css">
    <!--
     body, td{
	  font: normal 12px "Lucida Grande", sans-serif;
	  color: #000000;
  	}
	.title{
		  width:100%;
		  height:30px;
		  text-align:center;
		  font: bold 12px "Lucida Grande", sans-serif;
	  }
	.topTitle{
		  width:100%;
		  height:30px;
		  text-align:center;
		  font: bold 14px "Lucida Grande", sans-serif;
	  }
	.mainTbl td{
		  text-align:center;
		  vertical-align: top;
		  font: normal 12px "Lucida Grande", sans-serif;
	  	  color: #343434;
		  border: 1px solid black;
	  }
	.listeMinBox {
	 	height:300px;
	 	overflow:auto;
	  }
	.listeMinBox span{
	 	color:#774743;
	 	text-decoration:underline;
	 	cursor:pointer;
	 	
	  }
	.listeMinBox table{
		  text-align:center;
		  vertical-align: top;
		  font: normal 12px "Lucida Grande", sans-serif;
	  	  color: #343434;
		  border: 1px solid black;
		  direction:rtl;
	  }
	.listeMinBox th{
		  font-weight:bold;
		  border-left:none;
		  border-top:none;
		  border-bottom:none;
		  border-right:1px solid black;
	  }
	.listeMinBox td{
		  border-left:none;
		  border-bottom:none;
		  border-right:1px solid black;
		  border-top:1px solid black;
	  }
	  a,  a:hover, a:visited{
	 color:#774743;
	 }
	 .titreSpan{
	 color:#774743;
	 cursor:pointer;
	 text-decoration:underline;
	 }
	 .listeBox
	 {
	 	width:700px;
	 	height:300px;
	 	overflow:auto;
	 }
	 .listeBox td{
	 	text-align:left;
	 	background: none;
	 	border:none;
	 }
    -->
    </style>
    
	<script type="text/javascript" src="../technique/jquery.min.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
	   //$("#listeBox").load("openDocument.jsp?fileName=listeDesLoisHabilitation");
	   $.post("openDocument.jsp", {fileName:"listeDesLoisHabilitation",typeLegislature:"courante"}, function(data){$("#listeBox").html(data);});
	   //$("#listeMinBox").load("openDocument.jsp?fileName=listeDesMinistereHabilitation");
	   $.post("openDocument.jsp", {fileName:"listeDesMinistereHabilitation",typeLegislature:"courante"}, function(data){$("#listeMinBox").html(data);});

	   /*var title = "autresElements.jsp?ratification=true&fileName=an_tableau_bord_ratification_pas_depose&title="+encodeURIComponent($("#nonDepose38").text());
	   $("#nonDepose38").attr("href", title);*/
	   $("#nonDepose38").click(function(){
		   setFilename("an_tableau_bord_ratification_pas_depose");
		   setTitle($("#nonDepose38").text());
		   document.forms[0].submit();
		   return false;
	   });
	   
	   /*title = "autresElements.jsp?ratification=true&fileName=an_tableau_bord_ratification_38c&title="+encodeURIComponent($("#suiviArticle38").text());
	   $("#suiviArticle38").attr("href", title);*/
	   $("#suiviArticle38").click(function(){
		   setFilename("an_tableau_bord_ratification_38c");
		   setTitle($("#suiviArticle38").text());
		   document.forms[0].submit();
		   return false;
	   });

	   
	   /*title = "autresElements.jsp?ratification=true&fileName=an_tableau_bord_ratification_741&title="+encodeURIComponent($("#suiviArticle74").text());
	   $("#suiviArticle74").attr("href", title);*/
	   $("#suiviArticle74").click(function(){
		   setFilename("an_tableau_bord_ratification_741");
		   setTitle($("#suiviArticle74").text());
		   document.forms[0].submit();
		   return false;
	   });
	   
	   /*title = "autresElements.jsp?ratification=true&fileName=an_tableau_bord_habilitation_ss_filtre&title="+encodeURIComponent($("#tblHabilitation").text());
	   $("#tblHabilitation").attr("href", title);*/
	   $("#tblHabilitation").click(function(){
		   setFilename("an_tableau_bord_habilitation_ss_filtre");
		   setTitle($("#tblHabilitation").text());
		   document.forms[0].submit();
		   return false;
	   });
	   
	  });
	  
	  function eltDeSuivi(loiNb, obj) {
		  //document.location.href = "autresElements.jsp?ratification=true&fileName=tableauDeSuiviHab-"+loiNb+"&title="+encodeURIComponent(obj.innerHTML)
		  setFilename("tableauDeSuiviHab-"+loiNb);
		  setTitle(obj.innerHTML);
		  document.forms[0].submit();
	  }
	  function eltDeSuiviParMin(ministere, obj) {
		  //document.location.href = "autresElements.jsp?ratification=true&fileName=tab_an_habilitation_ss_fltr_min_all-"+ministere+"&title="+encodeURIComponent(obj.innerHTML)
		  setFilename("tab_an_habilitation_ss_fltr_min_all-"+ministere);
		  setTitle(obj.innerHTML);
		  document.forms[0].submit();
	  }
	  function parMinRatification38c(ministere, obj) {
		  //document.location.href = "autresElements.jsp?ratification=true&fileName=tab_an_ratification_38c_min-"+ministere+"&title="+encodeURIComponent(obj.parentNode.parentNode.cells[0].innerHTML)
		  setFilename("tab_an_ratification_38c_min-"+ministere);
		  setTitle($(obj).closest("tr").find("td:first > span:first").html());
		  document.forms[0].submit();
	  }
	  function parMinRatification741(ministere, obj) {
		  //document.location.href = "autresElements.jsp?ratification=true&fileName=tab_an_ratification_741_min-"+ministere+"&title="+encodeURIComponent(obj.parentNode.parentNode.cells[0].innerHTML)
		  setFilename("tab_an_ratification_741_min-"+ministere);
		  setTitle($(obj).closest("tr").find("td:first > span:first").html());
		  document.forms[0].submit();
	  }

	  function setFilename(fileNameVal){
		  $("#fileName").val(fileNameVal);
	  }
	  function setTitle(titVal){
		  $("#title").val(titVal);
	  }
	  
	</script>

  </head>
  <body>
  <img style="saturate:100;position:absolute;top:5px;margin:1px;width:99%;height:99%;z-index:-1;opacity:.3;filter:alpha(opacity=30);" src="<%=context%>/img/theme_page_accueil/fond_sceaux.png">
	<table width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr height="30px">
			<td>
				&nbsp;<a href="<%=context%>/login.jsp">S.O.L.O.N.</a>
			</td>
		</tr>
		<tr>
			<td class="topTitle">
				OUTIL DE SUIVI DES ORDONNANCES<br/>(Habilitations et ratification)<br/>
			</td>
		</tr>
		<tr>
			<td align="center">
				<table border="0" cellpadding="10" cellspacing="10" class="mainTbl">
					<tr>
						<td>
							<span class="title"><a href="#" id="tblHabilitation">Liste des habilitations depuis la XIIIe législature</a><br/>
							Présentation par loi d’habilitation</span><br/><br/>
							<i>Cliquez sur le titre de la loi pour accéder à la liste de ses habilitations</i>
							<br/>
							<i>Cliquez sur l’icône pour accéder à la loi d’habilitation</i>
							<br/><br/>
							<div class="listeBox" id="listeBox">
							</div>
						</td>
						<td>
							<span class="title">Présentation par ministère</span><br/><br/>
							<div class="listeMinBox" id="listeMinBox"></div>
							<br/><br/>
							<span class="title">Ratification</span><br/><br/>
							<a id="nonDepose38" href="#">
								Ordonnances de l’article 38 C dont le projet de loi de ratification n’a pas été déposé
							</a><br/>
							<a id="suiviArticle38" href="#">
								Ordonnances prises sur le fondement de l’article 38 de la Constitution
							</a><br/>
							<a id="suiviArticle74" href="#">
								Ordonnances prises sur le fondement de l’article 74-1 de la Constitution
							</a><br/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="center">
				<a href="http://extraqual.pm.ader.gouv.fr/ordonnances/index.html" target="_blank">
					SUIVI DES ORDONNANCES SUR LE PORTAIL DE LA QUALITE ET DE LA SIMPLIFICATION DU DROIT
				</a><br/>
					Pour toute question sur les éléments de suivi présentés ici, merci de nous écrire <br/>
					à l'adresse : <a href="mailto:application.lois@pm.gouv.fr">application.lois@pm.gouv.fr</a>
			</td>
		</tr>
	</table>
	<form action="autresElements.jsp" method="post">
		<input type="hidden" id="fileName" name="fileName" value=""/>
		<input type="hidden" id="ratification" name="ratification" value="true"/>
		<input type="hidden" id="title" name="title" value=""/>
		<input type="hidden" id="typeLegislature" name="typeLegislature" value="courante">
	</form>
  </body>
</html>