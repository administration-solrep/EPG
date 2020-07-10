<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- Nuxeo Enterprise Platform, svn $Revision: 22925 $ -->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page language="java"%>
<%@ page import="org.nuxeo.runtime.api.Framework"%>
<%@ page import="java.util.Locale"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Map"%>
<%@ page import="fr.dila.st.core.service.STServiceLocator"%>
<%@ page import="fr.dila.st.api.service.EtatApplicationService"%>
<%@ page import="fr.dila.st.api.administration.EtatApplication"%>
<%@ page import="fr.dila.solonepg.api.service.SolonEpgUserManager"%>
<%@ page import="fr.dila.solonepg.api.service.SolonEpgParametreService"%>
<%@ page import="fr.dila.solonepg.core.service.SolonEpgServiceLocator"%>
<%@ page import="fr.dila.st.api.service.ConfigService"%>
<%@ page import="fr.dila.st.api.constant.STConfigConstants"%>
<%@ page import="fr.dila.st.api.service.STParametreService"%>
<%
String productName = Framework.getProperty("org.nuxeo.ecm.product.name");
String productVersion = Framework.getProperty("org.nuxeo.ecm.product.version");
String productTag = Framework.getProperty("reponses.product.tag");
String context = request.getContextPath();
Locale locale = request.getLocale();
String language = locale == null ? "en" : locale.getLanguage();
String country = locale == null ? "US" : locale.getCountry();

String pageRenseignement;
try {
  STParametreService paramService = STServiceLocator.getSTParametreService();
  pageRenseignement = paramService.getUrlRenseignement();
} catch (Exception e) {
    pageRenseignement = "#";
}

Boolean restrictionAccess = false;
String restrictionDescription = "";
Boolean affichageBanniere = false;
String messageBanniere = "";
boolean isExistAndNotActive = false;
String  username = request.getParameter("user_name");
String identPlateformeCouleur = "000000";
String identPlateformeLibelle = "";
String identPlateformeBackground = "fond_sceaux.png";
try {
  EtatApplicationService etatApp = STServiceLocator.getEtatApplicationService();
  Map<String, Object> result = etatApp.getRestrictionAccesUnrestricted();
  restrictionAccess = (Boolean)result.get(EtatApplicationService.RESTRICTION_ACCESS);
  restrictionDescription = (String)result.get(EtatApplicationService.RESTRICTION_DESCRIPTION);
  SolonEpgUserManager userManger = (SolonEpgUserManager) STServiceLocator.getUserManager();
  isExistAndNotActive = userManger.isExistAndNotActive(username);
  affichageBanniere = (Boolean)result.get(EtatApplicationService.AFFICHAGE_RADIO);
  messageBanniere = (String)result.get(EtatApplicationService.MESSAGE);
  ConfigService configService =  STServiceLocator.getConfigService();
  identPlateformeCouleur = configService.getValue(STConfigConstants.SOLON_IDENTIFICATION_PLATEFORME_COULEUR);
  identPlateformeLibelle = configService.getValue(STConfigConstants.SOLON_IDENTIFICATION_PLATEFORME_LIBELLE);
  identPlateformeBackground = configService.getValue(STConfigConstants.SOLON_IDENTIFICATION_PLATEFORME_BACKGROUND);
} catch (Exception e) {
  restrictionDescription="Erreur d'accès à l'état de l'application";
}

%>
<html>
<fmt:setLocale value="fr" scope="session"/>
<fmt:setBundle basename="messages" var="messages"/>

<head>
<title>SOLON EPG</title>
<link rel="icon" type="image/png" href="<%=context%>/icons/favicon.png" />
<style type="text/css">
<!--
body{margin:0px;padding:0px;text-align:center;font-family:"Arial";width:100%;height:100%;display:block;background: url("<%=context%>/img/theme_page_accueil/<%=identPlateformeBackground%>") no-repeat scroll right 80px; -webkit-background-size: cover; /* pour Chrome et Safari */  -moz-background-size: cover; /* pour Firefox */  -o-background-size: cover; /* pour Opera */  background-size: cover; /* version standardisée */   /*min-width:900px;*/} 
a{text-decoration:none; color:#635A4D;} 
a:hover{color:#B92125;}
.header_solon{height:105px;width:100%;display:block;}
#header_solon_logo{position:absolute;top:7px;left:20px;display:block;z-index:1000;}
#header_solon_banniere{height:105px;width:100%;text-align:center;position:absolute;top:0px;left:0px;z-index:500;background: url("<%=context%>/img/header-reponses.png") no-repeat scroll center center; }
#header_solon_left{height:105px;width:50%;position:absolute;top:0px;left:0px;background-color:#516eaa;z-index:100;}
#header_solon_right{height:105px;width:50%;position:absolute;top:0px;right:0px;background-color:#bd4044;z-index:100;}
#main_solon_nav{width:180px;background-color: #F4F2EA;border: 3px solid #635A4D;margin:20px;padding:10px;display:block;text-align:center;float:left;-moz-box-shadow: 0 0 5px #888;-webkit-box-shadow: 0 0 5px#888;box-shadow: 0 0 5px #888;z-index:500;}
#main_solon_nav span{ color: #635A4D;    font-family: Arial,Helvetica,sans-serif;    font-size: 14px;    font-weight: bold;    margin-bottom: 5px;    text-align: center;    width:152px;    display:block;    text-align:center;    margin-left:auto;    margin-right:auto; } 
#main_solon_nav ul{margin:0px;padding:0px;list-style:none;font-size:12px;margin-left:12px;margin-right:12px;}
#main_solon_nav ul li{background: url("<%=context%>/img/theme_page_accueil/gauche_bouton_petit.png") no-repeat scroll center center;width:152px;padding:0px;height:50px;display:block;margin-bottom:5px;border: 2px solid #F4F2EA;transition: all 0.5s;-moz-transition: all 0.5s; /* Firefox 4 */-webkit-transition: all 0.5s; /* Safari and Chrome */-o-transition: all 0.5s; /* Opera */}
#main_solon_nav ul li a{text-decoration:none; color: #635A4D; padding-top:10px; height:40px; display:block; font-weight:bold;}#main_solon_nav ul li a:hover{color:#B92125;}
#main_solon_nav ul li:hover{border: 2px solid #B92125;transition: all 0.2s;-moz-transition: all 0.2s; /* Firefox 4 */-webkit-transition: all 0.2s; /* Safari and Chrome */-o-transition: all 0.2s; /* Opera */}
#main_solon_login{width:600px;color: #635A4D;margin-left:auto;margin-right:auto;z-index:1000;padding-bottom:60px;}
#main_solon_login h1{font-size: 26px;margin:0px;padding-top:15px;}
#main_solon_login_box{  background-color: #F4F2EA;border: 3px solid #635A4D;margin: 15px 30px 15px 100px;    padding: 10px;    text-align: center;    width: 360px;margin-left:auto;margin-right:auto;font-size: 14px;-moz-box-shadow: 0 0 5px #888;-webkit-box-shadow: 0 0 5px#888;box-shadow: 0 0 5px #888;}
#main_solon_login_box h2{font-size: 14px;width: 170px;    margin-left:auto;margin-right:auto;margin-bottom:15px;}
.main_solon_login_field{text-align: left;margin-bottom:5px;}
#main_solon_login_box label{color: #635A4D;    font-family: Arial,Helvetica,sans-serif;    font-size: 12px;    font-weight: bold;    text-align: right;    margin-right:5px;    width:133px;    display:block;    float:left;    line-height:25px;}
#main_solon_login_box input[type=submit] {    background: none no-repeat fixed center center #DBDBDB;    border: 2px solid #666666;    color: #635A4D;    cursor: pointer;    font-family: Arial,Helvetica,sans-serif;    font-size: 12px;    font-style: normal;    font-weight: bold;    height: 25px;    line-height: 5px;    margin: 5px;    padding-bottom: 5px;    padding-left: 5px;    padding-right: 5px;    padding-top: 2px !important;    text-transform: none;    width: 80px;    margin-top:5px;    transition: all 0.5s;-moz-transition: all 0.5s; /* Firefox 4 */-webkit-transition: all 0.5s; /* Safari and Chrome */-o-transition: all 0.5s; /* Opera */}
#main_solon_login_box input[type=submit]:hover {    border: 2px solid #B92125;    color: #B92125;    transition: all 0.2s;-moz-transition: all 0.2s; /* Firefox 4 */-webkit-transition: all 0.2s; /* Safari and Chrome */-o-transition: all 0.2s; /* Opera */}
#main_solon_login_box input[type=text]{    background: none repeat scroll 0 0 white;    border: 2px inset #635A4D;    color: #454545;    font-family: Arial,Helvetica,sans-serif;    font-size: 12px;    font-style: normal;    font-variant: normal;    font-weight: bold;    margin: 2px 12px 2px 2px;    padding: 2px;    width: 170px;        transition: all 0.5s;-moz-transition: all 0.5s; /* Firefox 4 */-webkit-transition: all 0.5s; /* Safari and Chrome */-o-transition: all 0.5s; /* Opera */}
#main_solon_login_box input[type=text]:focus{    border: 2px solid #B92125;        transition: all 0.2s;-moz-transition: all 0.2s; /* Firefox 4 */-webkit-transition: all 0.2s; /* Safari and Chrome */-o-transition: all 0.2s; /* Opera */}
#main_solon_login_box input[type=password]{    background: none repeat scroll 0 0 white;    border: 2px inset #635A4D;    color: #454545;    font-family: Arial,Helvetica,sans-serif;    font-size: 12px;    font-style: normal;    font-variant: normal;    font-weight: bold;    margin: 2px 12px 2px 2px;    padding: 2px;    width: 170px;            transition: all 0.5s;-moz-transition: all 0.5s; /* Firefox 4 */-webkit-transition: all 0.5s; /* Safari and Chrome */-o-transition: all 0.5s; /* Opera */}
#main_solon_login_box input[type=password]:focus{    border: 2px solid #B92125;        transition: all 0.2s;-moz-transition: all 0.2s; /* Firefox 4 */-webkit-transition: all 0.2s; /* Safari and Chrome */-o-transition: all 0.2s; /* Opera */}
.main_solon_error{color:#B92125;display:block;margin-botom:5px;}
#main_solon_login_box span{font-size: 11px;padding-top:10px;}
#main_solon_login_box a{text-decoration:none; color: #635A4D;}
#main_solon_login_box a:hover{color:#B92125;}
#main_solon_login_lib{ background-color: #F4F2EA;    border: 3px solid #635A4D;    font-size: 12px;    font-weight: bold;    height: 20px;    margin-left:auto;    margin-right:auto;    padding-top: 3px;    text-align: center;    width: 253px;    -moz-box-shadow: 0 0 2px #000;-webkit-box-shadow: 0 0 2px #000;box-shadow: 0 0 2px #000;   }   
#main_solon_login_lib a{text-decoration:none; color: #635A4D;}#main_solon_login_lib a:hover{color:#B92125;}
#main_solon_login_rules{margin-top:15px;font-size: 12px;}
#footer_solon{position:fixed;bottom:0px;right:0px;border-top: 2px solid #B92125;width:100%;text-align:right;background-color:#F4F2EA;}
#footer_solon ul{margin:0px;padding:0px;list-style:none;font-size:12px;float:right;}
#footer_solon li{float:left;font-weight: bold;line-height:35px;}
#footer_solon li:hover{}
#footer_solon li a{padding-right:5px;height:35px;display:block;border-left: 5px solid #F4F2EA;border-right: 5px solid #F4F2EA;border-bottom: 5px solid #F4F2EA; transition: all 0.5s;-moz-transition: all 0.5s; /* Firefox 4 */-webkit-transition: all 0.5s; /* Safari and Chrome */-o-transition: all 0.5s; /* Opera */}
* html #footer_solon li a{display:inline;}
#footer_solon li a:hover{border-left: 5px solid #B92125;border-right: 5px solid #B92125;border-bottom: 5px solid #B92125;padding-left:20px;padding-right:20px;        transition: all 0.5s;-moz-transition: all 0.5s; /* Firefox 4 */-webkit-transition: all 0.5s; /* Safari and Chrome */-o-transition: all 0.5s; /* Opera */}
.version{color:#F4F2EA;}
#solon_identification_plateforme_div{color:#<%=identPlateformeCouleur%>;font-size:20px;font-weight:bold;}
-->
</style>

</head>
<body>
    <script language="javascript">
    window.addEventListener("DOMContentLoaded", function (event) {
    	var inputScreen = document.getElementById('screenSize');
    	if(inputScreen){
    		inputScreen.value=screen.width + "x" + screen.height
    	}
      });
    </script>

    <!-- debut #header_solon -->
    <div class="header_solon">
        <div id='header_solon_logo'><img src="<%=context%>/img/theme_page_accueil/up_premierministre.png" alt="Premier ministre"/></div>
        <div id='header_solon_banniere'></div>
        <div id='header_solon_left'></div>
        <div id='header_solon_right'></div>
    </div>
    <!-- fin #header_solon -->
    
    <!-- debut #main_solon -->
    <div id='main_solon'>
        <!-- debut #main_solon_nav -->
        <div id='main_solon_nav'>
            <span><fmt:message bundle="${messages}" key="label.accueil.display.espace.suivi.title" /> </span>
            <ul>
                <li><a href="activite_parlementaire.jsp"><fmt:message bundle="${messages}" key="label.accueil.display.espace.suivi.activite.parlementaire" /></a></li>
                <li><a href="depot_amendements.jsp"><fmt:message bundle="${messages}" key="label.accueil.display.espace.suivi.depot.amendement" /></a></li>
                <li><a href="transposition_directives.jsp"><fmt:message bundle="${messages}" key="label.accueil.display.espace.suivi.transposition.directive" /></a></li>
                <li><a href="outildesuivi/application_lois.jsp"><fmt:message bundle="${messages}" key="label.accueil.display.espace.suivi.application.loi" /></a></li>
                <li><a href="outildesuivi/suivi_ordonnances.jsp"><fmt:message bundle="${messages}" key="label.accueil.display.espace.suivi.suivi.ordonnance" /></a></li>
                <li><a href="traites_et_accords.jsp"><fmt:message bundle="${messages}" key="label.accueil.display.espace.suivi.traite.accord" /></a></li>
                <li><a href="outildesuivi/application_ordonnances.jsp"><fmt:message bundle="${messages}" key="label.accueil.display.espace.suivi.application.ordonnances" /></a></li>
            </ul>
        </div>
        <!-- fin #main_solon_nav -->

        <!-- debut #main_solon_login -->
        <div id='main_solon_login'>
            <h1>Système d'Organisation en Ligne des Opérations Normatives</h1>
            <!-- debut #main_solon_login_box -->
            <div id='main_solon_login_box'>
                <form method="post" action="nxstartup.faces">
                 <!-- To prevent caching -->
					          <%
					              response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
					              response.setHeader("Pragma", "no-cache"); // HTTP 1.0
					              response.setDateHeader("Expires", -1); // Prevents caching at the proxy server
					          %>
					          <!-- ;jsessionid=<%=request.getSession().getId()%> -->
					          <!-- ImageReady Slices (login_cutted.psd) -->
                <h2><fmt:message bundle="${messages}" key="label.accueil.display.espace.connexion.title" /></h2>
                    <div class='main_solon_login_field'><label for="username"><fmt:message bundle="${messages}" key="label.login.username" /></label><input id="username" class="login_input" type="text" size="22" name="user_name"></div>
                    <div class='main_solon_login_field'><label for="password"><fmt:message bundle="${messages}" key="label.login.password" /></label><input type="password" size="22" id="password" name="user_password" class="login_input"></div>
                    <input id="language" type="hidden" value="fr" name="language">
                    <input id="requestedUrl" type="hidden" value="" name="requestedUrl">
                    <input id="screenSize" type="hidden" value="" name="screenSize">
                    <input id="form_submitted_marker" type="hidden" name="form_submitted_marker">
                    <input class="login_button" type="submit" value="<fmt:message bundle="${messages}" key="label.login.logIn" />" name="Submit">
                <c:if test="${param.loginFailed}">

	             <% if (isExistAndNotActive) {
                 %>
	             <div class="main_solon_error">
	             <fmt:message bundle="${messages}" key="message.user.inactif" />
	             </div>
	             <%
                } else {
                %>
	             <div class="main_solon_error"><fmt:message bundle="${messages}"
		         key="label.login.invalidUsernameOrPassword" /></div>
	            <%
                } %>
                 </c:if>
                 <!--  FEV 550 Message tentative de bruteforce -->
                 <c:if test="${param.loginWait}">
  					<div class="main_solon_error">
  						<fmt:message bundle="${messages}" key="message.login.bloque" /> ${param.loginMessage}
  					</div>
                 </c:if>
                
                
                
                
                <c:if test="${param.loginMissing}">
                <div class="main_solon_error">
                <fmt:message bundle="${messages}" key="label.login.missingUsername" />
                </div>
                </c:if>
                <c:if test="${param.securityError}">
                <div class="main_solon_error">
                <fmt:message bundle="${messages}" key="label.login.securityError" />
                </div>
                </c:if>
                <% if (restrictionAccess && restrictionDescription != null) { %> 
                <div class="main_solon_error">
                <%=restrictionDescription%>
                </div>
                <% } %>
               </form>
                <span><fmt:message bundle="${messages}" key="label.login.get.new.password" /> <a href="reset_password.jsp"><fmt:message bundle="${messages}" key="label.login.get.new.password.clickhere" /></a></span>
                
				
            </div>
            <!-- fin #main_solon_login_box -->
            
            <!--  debut #solon_identification_plateforme_div -->
            <div id="solon_identification_plateforme_div">
            	<%=identPlateformeLibelle%>
            </div>
            <!-- fin #solon_identification_plateforme_div -->
			

            <br/>
				<% if (affichageBanniere) { %>
				<div align="justify" style="width:350px;border:solid 3px #635a4d;padding-top:5px;padding-left:5px;padding-right:5px;background-color:white;margin:0 auto">
					<span>
					<%=messageBanniere%>
					</span>
				</div>
				<% } %>

            <!-- debut #main_solon_login_lib -->
            <div id='main_solon_login_lib'>
                <a href="http://extraqual.pm.ader.gouv.fr/publication/index.html"><fmt:message bundle="${messages}" key="label.accueil.display.feuille.style.link" /></a>
            </div>
            <!-- fin #main_solon_login_box -->
            <!-- debut #main_solon_login_rules -->
            <div id='main_solon_login_rules'>
                <a href="<%=pageRenseignement%>" target="_blank">Pour tout renseignement sur les conditions d'accès à l'outil</a>
            </div>
        </div>
        <!-- fin #main_solon_login -->
    </div>
    <!-- fin #main_solon -->
    
    <!-- debut #footer_solon -->
    <div id='footer_solon'>
        <ul>
            <li class='version'><%=productTag%></li>
            <li><a href="http://www.legifrance.gouv.fr/">Légifrance</a></li>
            <li><a href="http://extraqual.pm.ader.gouv.fr/legistique/index.html">Guide de légistique</a></li>
            <li><a href="http://extraqual.pm.ader.gouv.fr/index.html">Portail de la qualité et de la simplification du droit</a></li>
        </ul>
    </div>
<!--   Current User = null -->

</body></html>