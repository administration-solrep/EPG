<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- Nuxeo Enterprise Platform, svn $Revision: 22925 $ -->
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java"%>
<%@ page import="org.nuxeo.runtime.api.Framework"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Set"%>
<%@ page import="fr.dila.st.core.service.STServiceLocator"%>
<%@ page import="fr.dila.st.api.service.STUserService"%>
<%@ page import="fr.dila.solonepg.api.cases.typescomplexe.InfoFeuilleStyleFile"%>
<%@ page import="fr.dila.solonepg.api.service.FeuilleStyleService"%>
<%@ page import="fr.dila.solonepg.api.service.ActeService"%>
<%@ page import="fr.dila.solonepg.api.service.SolonEpgParametreService"%>
<%@ page import="fr.dila.solonepg.api.constant.TypeActe"%>
<%@ page import="fr.dila.solonepg.core.service.SolonEpgServiceLocator"%>
<%@ page import="fr.dila.solonepg.web.feuillestyle.FeuilleStyleBean"%>

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
    String pageRenseignement;
    try {
      SolonEpgParametreService paramService = SolonEpgServiceLocator.getSolonEpgParametreService();
      pageRenseignement = paramService.getUrlRenseignement();
    } catch (Exception e) {
        pageRenseignement = "#";
    }
%>
<html>
<fmt:setLocale value="fr" scope="session"/>
<fmt:setBundle basename="messages" var="messages"/>


<title>SOLON EPG</title>
<link rel="icon" type="image/png" href="<%=context%>/icons/favicon.png" />
<style type="text/css">
<!--
 body {
  font: normal 11px "Lucida Grande", sans-serif;
  color: #343434;
  }

table.loginForm {
  border-spacing:3px;
  padding:3px;
  }
  
.tdLogin {
  background-image: url("<%=context%>/img/theme_page_accueil/fond_sceaux.png");
  background-repeat:no-repeat;
  background-attachment:scroll;
  background-position:center center; 
  width:100%;
}


.login {
	  background:#fff url("<%=context%>/img/theme_reponses/marianne_fond.gif") bottom right no-repeat;
	  opacity:0.95;
	  filter : alpha(opacity=95);
	  border: 1px solid black;
	  padding:20px 0px 0px 0px;
	  width:350px;
  }

.login_label {
	  font:bold 12px "Lucida Grande", sans-serif;
	  text-align: right;
	  white-space: nowrap;
	  color: #005595;
	  margin:0 4px 0 0;

  }

.login_input {
	  border:1px inset #454545;
	  background: white;
	  padding:3px;
	  color: #454545;
	  margin:0 10px 5px 0px;
	  font:normal 10px "Lucida Grande", sans-serif;
  }

.login_title {
  text-align: center;
  font:bold 13px "Lucida Grande", sans-serif;
}

/* this class is a repeat because defined in nxthemes-setup.xml but
nxthemes css is not used in login.jsp */
.login_button {
  cursor:pointer;
  color: #454545;
  font-size: 10px;
  background: #CECFD1 url(<%=context%>/img/theme_galaxy/buttons.png) repeat-x scroll left top;
  border:1px solid #BFC5CB;
  padding: 2px 5px 2px 5px;
  margin: 5px 10px 10px 0;
  }

.login_button:hover {
  border:1px solid #92999E;
  color:#000000;
  }

.news_container {
  position: absolute;
  top: 36px;
  right: 360px;
  text-align:left;
}

.block_container {
  border:1px solid #aaaaaa;
  overflow:auto;
  height:650px;
  width:250px;
  opacity:0.90;
  filter : alpha(opacity=90);
  padding:0;
  overflow:auto;
  overflow-x:hidden;
}

.display_feuilles_styles {
  font:bold 11px "Lucida Grande", sans-serif;
}

.errorMessage {
  color:#000;
  font:bold 10px "Lucida Grande", sans-serif;
  border:1px solid #666;
  background: url(<%=context%>/icons/warning.gif) 2px 3px no-repeat #FFCC33;
  margin-bottom:12px;
  display:block;
  padding:5px 5px 5px 23px;
  text-align: center;
  }
  
 .footer {
 	width:100%;
 	text-align:center;
 	font-size:12px;
 	
 }
 
  a,  a:hover, a:visited{
 color:#0000FF;
 }
-->

</style>

</head>

<body style="margin:0;text-align:center;">
<%
    String typeActeSelected = request.getParameter("typeActeSelected");

    boolean hasError = false;
    String errorMsg = null;
    
    try {
        FeuilleStyleService feuilleStyleService = SolonEpgServiceLocator.getFeuilleStyleService();
        List<InfoFeuilleStyleFile> feuilleStyleList = feuilleStyleService.getFeuilleStyleListFromTypeActe(typeActeSelected);
        request.setAttribute("feuilleStyleList", feuilleStyleList);
    } catch (Exception e) {
        hasError = true;
        errorMsg = e.getMessage();
    }
%>
<table cellspacing="0" cellpadding="0" border="0" width="100%" height="100%">
  <tbody>
    <tr>
      <td colspan="2" style="height:100px;">


				<img src="<%=context%>/img/logopm.png" style="margin-left:10px;margin-top:10px;"/>
      </td>
    </tr>  
    <tr>
      <td class="tdLogin" align="center">
        <form name="display_feuilles_styles_form" method="post" action="display_feuilles_styles.jsp">
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
                <fmt:message bundle="${messages}" key="label.accueil.display.feuille.style.title" />
              </td>
             </tr>
             <tr>
              <td colspan="2" class="login_title">
                <select id="typeActeSelected" name ="typeActeSelected" onChange="this.form.submit();">
                	<option value="-">-</option>
									<%
									    try {
									        ActeService acteService = SolonEpgServiceLocator.getActeService();
									        List<TypeActe> typeActeList = acteService.getAllTypeActe();
									        for (TypeActe typeActe : typeActeList) {
									            String optionTag = "<OPTION VALUE=\"" + typeActe.getId() + "\"";
									            if (typeActe.getId().equals(typeActeSelected)) {
									                optionTag += " selected=\"selected\"";
									            }
									            //close the option tag
									            optionTag += ">" + typeActe.getLabel() + "</OPTION>";
									            out.println(optionTag);
									        }
									    } catch (Exception e) {
									        hasError = true;
									        errorMsg = e.getMessage();
									    }
									%>
									</select>
              </td>
             </tr>
								<c:forEach items="${feuilleStyleList}" var="item"
								varStatus="loop">
								<tr>
									<td colspan="2" class="login_title">
									<a href="download_feuille_style.jsp?idFeuilleStyle=${item.id}&pos=${item.pos}" >${item.fileName}</a>
									</td>
								</tr>
						 </c:forEach>
						 <tr>
                <td></td>
                <td>
                  <%
                      if (hasError) {
                          out.println("<div class='errorMessage'>" + errorMsg + "</div>");
                      }
                  %>
                </td>
              </tr>
             <tr>
            <!-- retour à la page de login --> 
              <td colspan="2" class="display_feuilles_styles">
                <fmt:message bundle="${messages}" key="label.accueil.display.retour.login" />
                <a href="login.jsp"><fmt:message bundle="${messages}" key="label.accueil.display.retour.login.clickhere" /></a>
              </td>
            </tr>
            </table>
          </div>
        </form>
      </td>
    </tr>
  <tr>
          <td class="footer" colspan="2">
            <a href="<%=pageRenseignement%>" target="_blank">Pour tout renseignement sur les conditions d'accès à l'outil</a>
        </td>
    </tr>

    
  </tbody>
</table>
<!--   Current User = <%=request.getRemoteUser()%> -->




</html>

