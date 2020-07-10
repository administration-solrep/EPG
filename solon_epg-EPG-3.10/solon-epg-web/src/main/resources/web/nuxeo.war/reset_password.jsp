<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- Nuxeo Enterprise Platform, svn $Revision: 22925 $ -->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page language="java"%>
<%@ page import="org.nuxeo.runtime.api.Framework"%>
<%@ page import="java.util.Locale"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Map"%>
<%@ page import="fr.dila.st.api.service.STUserService"%>
<%@ page import="org.nuxeo.ecm.core.api.ClientException"%>
<%@ page import="fr.dila.st.core.service.STServiceLocator"%>
<%@ page import="fr.dila.st.api.service.EtatApplicationService"%>
<%@ page import="fr.dila.st.api.administration.EtatApplication"%>
<%@ page import="fr.dila.solonepg.api.service.SolonEpgUserManager"%>
<%@ page import="fr.dila.solonepg.api.service.SolonEpgParametreService"%>
<%@ page import="fr.dila.solonepg.core.service.SolonEpgServiceLocator"%>
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
  SolonEpgParametreService paramService = SolonEpgServiceLocator.getSolonEpgParametreService();
  pageRenseignement = paramService.getUrlRenseignement();
} catch (Exception e) {
    pageRenseignement = "#";
}

Boolean restrictionAccess = false;
String restrictionDescription = "";
boolean isExistAndNotActive = false;
String  username = request.getParameter("user_name");
try {
  EtatApplicationService etatApp = STServiceLocator.getEtatApplicationService();
  Map<String, Object> result = etatApp.getRestrictionAccesUnrestricted();
  restrictionAccess = (Boolean)result.get(EtatApplicationService.RESTRICTION_ACCESS);
  restrictionDescription = (String)result.get(EtatApplicationService.RESTRICTION_DESCRIPTION);
  SolonEpgUserManager userManger = (SolonEpgUserManager) STServiceLocator.getUserManager();
  isExistAndNotActive = userManger.isExistAndNotActive(username);
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
body{margin:0px;padding:0px;text-align:center;font-family:"Arial";width:100%;height:100%;display:block;background: url("<%=context%>/img/theme_page_accueil/fond_sceaux.png") no-repeat scroll right 80px; -webkit-background-size: cover; /* pour Chrome et Safari */  -moz-background-size: cover; /* pour Firefox */  -o-background-size: cover; /* pour Opera */  background-size: cover; /* version standardisée */   /*min-width:900px;*/} 
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
#main_solon_login{width:600px;color: #635A4D;margin-left:auto;margin-right:auto;z-index:1000;}
#main_solon_login h1{font-size: 26px;margin:0px;padding-top:15px;}
#main_solon_login_box{  background-color: #F4F2EA;border: 3px solid #635A4D;margin: 15px 30px 15px 100px;    padding: 10px;    text-align: center;    width: 360px;margin-left:auto;margin-right:auto;font-size: 14px;-moz-box-shadow: 0 0 5px #888;-webkit-box-shadow: 0 0 5px#888;box-shadow: 0 0 5px #888;}
#main_solon_login_box h2{font-size: 14px;width: 170px;    margin-left:auto;margin-right:auto;margin-bottom:15px;}
.main_solon_login_field{text-align: left;margin-bottom:5px;}
#main_solon_login_box label{color: #635A4D;    font-family: Arial,Helvetica,sans-serif;    font-size: 12px;    font-weight: bold;    text-align: right;    margin-right:5px;    width:133px;    display:block;    float:left;    line-height:25px;}
#main_solon_login_box input[type=submit] {    background: none no-repeat fixed center center #DBDBDB;    border: 2px solid #666666;    color: #635A4D;    cursor: pointer;    font-family: Arial,Helvetica,sans-serif;    font-size: 12px;    font-style: normal;    font-weight: bold;    height: 25px;    line-height: 5px;    margin: 5px;    padding-bottom: 5px;    padding-left: 5px;    padding-right: 5px;    padding-top: 2px !important;    text-transform: none;    width: 40%;    margin-top:5px;    transition: all 0.5s;-moz-transition: all 0.5s; /* Firefox 4 */-webkit-transition: all 0.5s; /* Safari and Chrome */-o-transition: all 0.5s; /* Opera */}
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
#footer_solon{position:absolute;bottom:0px;right:0px;border-top: 2px solid #B92125;width:100%;text-align:right;background-color:#F4F2EA;}
#footer_solon ul{margin:0px;padding:0px;list-style:none;font-size:12px;float:right;}
#footer_solon li{float:left;font-weight: bold;line-height:35px;}
#footer_solon li:hover{}
#footer_solon li a{padding-right:5px;height:35px;display:block;border-left: 5px solid #F4F2EA;border-right: 5px solid #F4F2EA;border-bottom: 5px solid #F4F2EA; transition: all 0.5s;-moz-transition: all 0.5s; /* Firefox 4 */-webkit-transition: all 0.5s; /* Safari and Chrome */-o-transition: all 0.5s; /* Opera */}
* html #footer_solon li a{display:inline;}
#footer_solon li a:hover{border-left: 5px solid #B92125;border-right: 5px solid #B92125;border-bottom: 5px solid #B92125;padding-left:20px;padding-right:20px;        transition: all 0.5s;-moz-transition: all 0.5s; /* Firefox 4 */-webkit-transition: all 0.5s; /* Safari and Chrome */-o-transition: all 0.5s; /* Opera */}
.version{color:#F4F2EA;}
-->
</style>

</head>

<body style="margin:0;text-align:center;">

<%
    boolean hasError = false;
    String errorMsg = null;
	String form_submitted_marker = request.getParameter("form_submitted_marker");

	if ("true".equals(form_submitted_marker)) {
		
		STUserService userService = STServiceLocator.getSTUserService();
		
		String email=request.getParameter("user_email");
		
		try {
			userService.askResetPassword(username, email);
			
		} catch (Exception e) {
		    hasError = true;
		    errorMsg = e.getMessage();
		}
		
	}

%>
<script type="text/javascript">


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
        <!-- debut #main_solon_login -->
        <div id='main_solon_login'>
            <h1>Système d'Organisation en Ligne des Opérations Normatives</h1>
            <!-- debut #main_solon_login_box -->
            <div id='main_solon_login_box'>
        <%  if (!"true".equals(form_submitted_marker) || hasError) { %>
        <form name="reset_password_form" method="post" action="reset_password.jsp">
          <!-- To prevent caching -->
          <%
              response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
              response.setHeader("Pragma", "no-cache"); // HTTP 1.0
              response.setDateHeader("Expires", -1); // Prevents caching at the proxy server
          %>
          <!-- ;jsessionid=<%=request.getSession().getId()%> -->
          <!-- ImageReady Slices (login_cutted.psd) -->
          <div class="login">
            <table>
             <tr>
              <td colspan="2" class="login_title">
                <fmt:message bundle="${messages}" key="label.login.askPassword" />
              </td>
             </tr>
             <tr>
                <td class="login_label">
                  <label for="username">
                    <fmt:message bundle="${messages}" key="label.login.username" />
                  </label>
                </td>
                <td>
                  <input class="login_input" type="text"
                    name="user_name" id="username" size="22" >
                </td>
              </tr>
              <tr>
                <td class="login_label">
                  <label for="email">
                    <fmt:message bundle="${messages}" key="label.login.email" />
                  </label>
                </td>
                <td>
                  <input class="login_input" type="text"
                      name="user_email" id="email" size="22" >
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  <% // label.login.logIn %>
                  <input type="hidden" name="language"
                      id="language" value="fr">
                  <input id="requestedUrl" type="hidden" value="" name="requestedUrl">
                  <input type="hidden" name="form_submitted_marker"
                      id="form_submitted_marker" value="true">
                  <input class="login_button" type="submit" name="Submit" id="validateBtn"
                    value="<fmt:message bundle="${messages}" key="label.login.validate" />">
                  <input class="login_button" type="submit" name="Cancel"  onclick="document.reset_password_form.action='login.jsp'"
                    value="<fmt:message bundle="${messages}" key="label.login.return.logIn" />">
                  
                </td>
              </tr>
              <tr>
                <td></td>
                <td>
                  <% 
                  if (hasError) {
                      out.println("<div class='main_solon_error'>"+errorMsg+"</div>");
                  }
                  %>
                </td>
              </tr>
            </table>
          </div>
        </form>
        <% } else { %>
          <form method="post" action="login.jsp">
            <div class="login">
              <table>
                <tr>
                  <td>
                    <fmt:message bundle="${messages}" key="label.login.password.sent" />
                    <input class="login_button" type="submit" name="Submit"
                      value="<fmt:message bundle="${messages}" key="label.login.return.logIn" />">
                  </td>
                </tr>
              </table>
            </div>
          </form>
        <% } %>
	</div>
	</div>
	</div>
</body>
<!--   Current User = <%=request.getRemoteUser()%> -->
</html>

