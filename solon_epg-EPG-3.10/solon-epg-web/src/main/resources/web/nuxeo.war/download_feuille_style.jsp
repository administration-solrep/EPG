<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java"%>
<%@ page import="fr.dila.solonepg.web.feuillestyle.FeuilleStyleBean"%>
<%
boolean hasError = false;
String errorMsg = null;
	 	try {
	 	   FeuilleStyleBean.getFeuilleStyleFile(response,request.getParameter("idFeuilleStyle"),request.getParameter("pos"));
	 	} catch (Exception e) {
	 	    hasError = true;
	 	    errorMsg = e.getMessage();
	 	}
%>