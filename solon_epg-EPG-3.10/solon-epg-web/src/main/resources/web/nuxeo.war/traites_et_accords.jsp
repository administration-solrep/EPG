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
    <title>S.O.L.O.N. / Traités et accords</title>
    <style type="text/css">
    <!--
    h1, div {
    	text-align:center;
    	position:relative;
    }
    -->
    </style>
  </head>
  <body>
  	<table cellspacing="0" cellpadding="0" border="0" width="100%" height="100%">
			<tr>
				<td valign="top">
					<img src="<%=context%>/img/theme_page_accueil/fond_sceaux.png" style="position:absolute;margin:1px;width:99%;height:90%;"/>
  				<h1>S.O.L.O.N. / Traités et accords</h1>
					<div>En construction</div>
				</td>
			</tr>
		</table>
  </body>
</html>