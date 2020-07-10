<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="fr.dila.solonepg.api.service.ActiviteNormativeService"%>
<%@page import="fr.dila.st.core.util.DateUtil"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page language="java"%>
<%@page import="fr.dila.solonepg.api.administration.ParametrageAN"%>
<%@page import="fr.dila.st.core.util.SessionUtil"%>
<%@page import="javax.security.auth.login.LoginContext"%>
<%@page import="org.nuxeo.ecm.core.api.CoreSession"%>
<%@ page import="org.nuxeo.runtime.api.Framework"%>
<%@ page import="java.util.Locale"%>
<%@ page import="fr.dila.solonepg.core.service.SolonEpgServiceLocator"%>
<%@ page import="fr.dila.solonepg.api.service.SolonEpgParametreService"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
String context = request.getContextPath();
Locale locale = request.getLocale();
String legislatureType = request.getParameter("legislature");
LoginContext loginContext = Framework.login();
CoreSession coreSession = SessionUtil.getCoreSession();
ActiviteNormativeService anService = SolonEpgServiceLocator.getActiviteNormativeService();
SolonEpgParametreService paramService = SolonEpgServiceLocator.getSolonEpgParametreService();

String language = locale == null ? "en" : locale.getLanguage();
String country = locale == null ? "US" : locale.getCountry();

ParametrageAN parametrage = paramService.getDocAnParametre(coreSession);
String legislatureCourante="";
String urlLegis = "application_lois.jsp";
String urlAppLoi = "application_lois.jsp";
if (legislatureType==null) {
	legislatureType = "courante";
}

if (parametrage !=null) {
	legislatureCourante = parametrage.getLegislaturePublication();
	if(legislatureType==null || legislatureType.equalsIgnoreCase("courante")) {
		urlLegis = urlLegis + "?legislature=precedente";
	} else {
		legislatureCourante = parametrage.getLegislaturePrecPublication();
		urlLegis = urlLegis + "?legislature=courante";
	}
}

try {
  urlAppLoi = paramService.getUrlSuiviApplicationLois();
} catch (Exception e) {
    urlAppLoi = "#";
}

loginContext.logout();

%>
<html>
<fmt:setLocale value="fr" scope="session"/>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>S.O.L.O.N. / Applications des lois pour la législature courante</title>
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
		  font-variant: small-caps;
	  }
	.mainTbl {
		border: 1px solid black;
		width: 1100px;
		  
	}
	.mainTbl td{
		  
		  vertical-align: top;
		  padding:10;		  
		  font: normal 12px "Lucida Grande", sans-serif;
	  	  color: #343434;
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
	 	width:600px;
	 	height:400px;
	 	overflow:auto;
	 }
	 .listeBox td{
	 	text-align:left;
	 	background: none;
	 	border:none;
	 	padding: 0;
	 }
	 .listeMinBox {
	 	height:400px;
	 	width:100%;
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
	  .soustitre {
	  	font: normal 14px "Lucida Grande", sans-serif;
		font-variant: small-caps;
	  }
	  .stat_bas {
	  	vertical-align:bottom;
	  	overflow: auto;
	  }
	  .elemCentre {
	  	margin-right: auto;
		margin-left: auto;
		width: 1100px;
	  }
    -->
    </style>

	<script type="text/javascript" src="../technique/jquery.min.js"></script>
	<script type="text/javascript">
	
    window.onpageshow = function() {
    	// Dans le cas d'un 'Back button' avec Firefox, l'action de submit du formulaire
    	// reste à l'ancienne. Du coup, avec cette fonction, on la réinitialise.
		document.forms[0].action ='autresElements.jsp';
	};

	$(document).ready(function() {
	   
	   $.post("openDocument.jsp", {fileName:"listeDesLois",typeLegislature:"<%=legislatureType%>"}, function(data){$("#listeBox").html(data);});
	   
	   $.post("openDocument.jsp", {fileName:"listeDesMinistereLoi",typeLegislature:"<%=legislatureType%>"}, function(data){$("#listeMinBox").html(data);});

	   $("#mesureLink").click(function(){
		   setFilename("mesuresApplicaticationActiveNonPubliees");
		   setTitle($("#mesureLink").text());
		   document.forms[0].submit();
		   return false;
	   });
	  
	   $("#miseApplicationLoiLink").click(function(){
		   setFilename("delais_mise_application");
		   setTitle($("#miseApplicationLoiLink").text());
		   document.forms[0].submit();
		   return false;
	   });

	   $("#vigueurLink").click(function(){
		   setFilename("mesureApplicaticationDifferee");
		   setTitle($("#vigueurLink").text());
		   document.forms[0].submit();
		   return false;
	   });

	   $("#tauxMinLink").click(function(){
		   setFilename("taux_dapplication_fil_eau_taux_ministere_tous");
		   setTitle($("#tauxMinLink").text());
		   document.forms[0].submit();
		   return false;
	   });

	   $("#tauxGlbLink").click(function(){
		   setFilename("taux_dapplication_fil_eau_taux_global");
		   setTitle($("#tauxGlbLink").text());
		   document.forms[0].submit();
		   return false;
	   });
	   $("#miseApplicationMinLink").click(function(){
		   setFilename("delai_ministere_tous");
		   setTitle($("#miseApplicationMinLink").text());
		   document.forms[0].submit();
		   return false;
	   });
	   $("#bilanSemestLoi").click(function(){
		   setFilename("bilan_semestriels_loi_tous");
		   setTitle($("#bilanSemestLoi").text());
		   document.forms[0].submit();
		   return false;
	   });
	   
	  });
	  function eltDeSuivi(loiNb, obj) {
		  document.forms[0].action = "elementsDeSuivi.jsp";
		  setLoiNb(loiNb);
		  setTitle(obj.innerHTML);
		  document.forms[0].submit();
	  }
	  function eltDeSuiviParMin(ministere, obj) {
		  setFilename("taux_execution_lois_promulguees_debut_legislature_par_ministere-"+ministere);
		  setTitle(obj.innerHTML);
		  document.forms[0].submit();
	  }
	  function parMinMesure(ministere, obj) {
		  setFilename("mesuresApplicaticationParMinistereNonPubliees-"+ministere);
		  setTitle(obj.innerHTML);
		  document.forms[0].submit();
	  }

	  function setFilename(fileNameVal){
		  $("#fileName").val(fileNameVal);
	  }
	  function setTitle(titVal){
		  $("#title").val(titVal);
	  }
	  function setLoiNb(loiVal){
		  $("#loiNb").val(loiVal);
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
				outil de suivi<br/>de l'application des lois<br/>pour la <%=legislatureCourante%>ème législature
			</td>
		</tr>
		<tr>
			<td>
				<div class="elemCentre">
				<table class="mainTbl">
					<tr>
						<td>
							<span class="title">PRÉSENTATION PAR LOI<br/>
							APPELANT DES DÉCRETS D'APPLICATION</span><br/><br/>			
							<div class="listeBox" id="listeBox">
							</div>
							<br/><br/>
							<i>Cliquer sur le titre de la loi pour accéder aux éléments de suivi</i>
							<br/>
							<i>Cliquer sur l'icône pour accéder à la loi publiée</i>
						</td>
						<td width="440px">
							<span class="title">PRÉSENTATION PAR MINISTÈRE</span><br/><br/><br/>
							<div class="listeMinBox" id="listeMinBox"></div>
							<br/><br/>
							
						</td>
					</tr>
					<tr>
						<td>
							<div class="stat_bas">							
							<span class="soustitre">Autres éléments de suivi des lois</span><br/><br/>		
							
							<a id="bilanSemestLoi" href="#">
								Bilan semestriel, par loi
							</a><br/><br/>					
							<a id="miseApplicationLoiLink" href="#">
								Délai de mise en application, par loi
							</a><br/><br/>
							<a id="mesureLink" href="#">
								Mesures en attente de décret d'application, par loi
							</a><br/><br/>
							<a id="vigueurLink" href="#">
								Mesures avec entrée en vigueur différée, par loi
							</a><br/><br/>
							</div>
						</td>
						<td>
							<div class="stat_bas">
							<span class="soustitre">Statistiques</span><br/><br/>
							<a id="tauxGlbLink" href="#">
								Taux d'application au fil de l'eau global
							</a><br/><br/>
							<a id="tauxMinLink" href="#">
								Taux d'application au fil de l'eau par ministère
							</a><br/><br/>
							<a href="parMinistere.jsp?typeLegislature=<%=legislatureType%>">
								Bilan semestriel par ministère
							</a><br/><br/>
							<a id="miseApplicationMinLink" href="#">
								Délai de mise en application, par ministère
							</a><br/><br/>
							</div>
						</td>
					</tr>
				</table>
			<br/>
			<% 
			if(legislatureType.equalsIgnoreCase("courante")) { 
				out.print("<a href='"+urlLegis+"'>Accès aux données de la législature précédente.</a>");
			} else if(legislatureType.equalsIgnoreCase("precedente")) {
				out.print("<a href='"+urlLegis+"'>Accès aux données de la législature en cours.</a>");
			}
			%>
			<br/>
			<br/>
			</div>
			</td>
		</tr>			
		<tr>
			<td align="center">
				<a href="<%=urlAppLoi%>" target="_blank">
					SUIVI DE L'APPLICATION DES LOIS SUR LE PORTAIL DE LA QUALITE ET DE LA SIMPLIFICATION DU DROIT
				</a><br/>
					Pour toute observation sur les éléments de suivi présentés ici, merci de nous écrire <br/>
					à l'adresse : <a href="mailto:application.lois@pm.gouv.fr">application.lois@pm.gouv.fr</a>
			</td>
		</tr>
	</table>
	<form action="autresElements.jsp" method="post">
		<input type="hidden" id="fileName" name="fileName" value=""/>
		<input type="hidden" id="title" name="title" value=""/>
		<input type="hidden" id="loiNb" name="loiNb" value=""/>
		<input type="hidden" id="typeLegislature" name="typeLegislature" value="<%=legislatureType%>">
	</form>
  </body>
</html>