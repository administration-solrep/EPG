<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page language="java"%>
<%@ page import="org.nuxeo.runtime.api.Framework"%>
<%@ page import="java.util.Locale"%>
<%@ page import="fr.dila.solonepg.core.service.SolonEpgServiceLocator"%>
<%@ page import="fr.dila.solonepg.api.service.EpgAlertService"%>
<%@ page import="org.nuxeo.ecm.core.api.ClientException"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
String productName = Framework.getProperty("org.nuxeo.ecm.product.name");
String productVersion = Framework.getProperty("org.nuxeo.ecm.product.version");
String productTag = Framework.getProperty("reponses.product.tag");
String context = request.getContextPath();
Locale locale = request.getLocale();
String language = locale == null ? "en" : locale.getLanguage();
String country = locale == null ? "US" : locale.getCountry();
%>
<html>
<fmt:setLocale value="fr" scope="session"/>
<fmt:setBundle basename="messages" var="messages"/>

<head>
<title>SOLON EPG</title>
<link rel="icon" type="image/png" href="<%=context%>/icons/favicon.png" />
<style type="text/css">
<!--
body{margin:0px;padding:0px;text-align:center;font-family:"Arial";width:100%;height:100%;display:block;background: url("<%=context%>/img/theme_page_accueil/fond_sceaux.png") no-repeat scroll right 80px; -webkit-background-size: cover; /* pour Chrome et Safari */  -moz-background-size: cover; /* pour Firefox */  -o-background-size: cover; /* pour Opera */  background-size: cover; /* version standardisée */   /*min-width:900px;*/} 
a{text-decoration:none; color:#635A4D;} 
a:hover{color:#B92125;}
.header_solon{height:105px;width:100%;display:block;}
#header_solon_logo{position:absolute;top:7px;left:20px;display:block;z-index:1000;}
#header_solon_banniere{height:105px;width:100%;text-align:center;position:absolute;top:0px;left:0px;z-index:500;background: url("<%=context%>/img/header-reponses.png") no-repeat scroll center center; }
#header_solon_left{height:105px;width:50%;position:absolute;top:0px;left:0px;background-color:#516eaa;z-index:100;}
#header_solon_right{height:105px;width:50%;position:absolute;top:0px;right:0px;background-color:#bd4044;z-index:100;}
#main_solon_maintien{width:600px;color: #635A4D;margin-left:auto;margin-right:auto;z-index:1200;}
#main_solon_maintien h1{font-size: 26px;margin:0px;padding-top:15px;}
#main_solon_maintien_box{  background-color: #F4F2EA;border: 3px solid #635A4D;margin: 100px 30px 15px 100px;    padding: 10px;    text-align: center;    width: 360px;margin-left:auto;margin-right:auto;font-size: 14px;-moz-box-shadow: 0 0 5px #888;-webkit-box-shadow: 0 0 5px#888;box-shadow: 0 0 5px #888;}
#main_solon_maintien_box h2{font-size: 14px;width: 170px;    margin-left:auto;margin-right:auto;margin-bottom:15px;}
#main_solon_maintien_box span{font-size: 11px;padding-top:10px;}
#main_solon_maintien_box a{text-decoration:none; color: #635A4D;}
#main_solon_maintien_box a:hover{color:#B92125;}
.version{color:#F4F2EA;}
-->
</style>

</head>

<body style="margin:0;text-align:center;">
	<!-- debut #header_solon -->
    <div class="header_solon">
        <div id='header_solon_logo'><img src="<%=context%>/img/theme_page_accueil/up_premierministre.png" alt="Premier ministre"/></div>
        <div id='header_solon_banniere'></div>
        <div id='header_solon_left'></div>
        <div id='header_solon_right'></div>
    </div>   
    <div id='main_solon'>        
		<div id='main_solon_maintien'>
            <h1>Système d'Organisation en Ligne des Opérations Normatives</h1>
            <!-- debut #main_solon_maintien_box -->
            <div id='main_solon_maintien_box'>				
				  <% 
						String alertId = request.getParameter("alertId");
						if (alertId == null) {
							out.println("Erreur dans la validation d'alerte : aucune alerte sélectionnée.");
						} else {  
							EpgAlertService alertService = SolonEpgServiceLocator.getAlertConfirmationSchedulerService();
							out.println(alertService.confirmationAlerte(alertId));        
						}
					%>
					<br /><br />
					<a href="../login.jsp">Retour à la page de connexion</a>				  
			</div>			
		</div>
</div>
</body>
<!--   Current User = <%=request.getRemoteUser()%> -->
</html>

