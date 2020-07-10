<%@page import="fr.dila.solonepg.api.service.SolonEpgParametreService"%>
<%@page import="fr.dila.solonepg.core.service.SolonEpgServiceLocator"%>
<%@page import="fr.dila.solonepg.api.service.ActiviteNormativeService"%>
<%@page import="fr.dila.st.core.util.SessionUtil"%>
<%@page import="org.nuxeo.ecm.core.api.CoreSession"%>
<%@page import="org.nuxeo.runtime.api.Framework"%>
<%@page import="javax.security.auth.login.LoginContext"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.io.FileReader"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="fr.dila.st.core.util.DateUtil"%>
<%@ page import="fr.dila.solonepg.api.constant.SolonEpgConfigConstant"%>
<%
try{
	String fileName = request.getParameter("fileName");
	fileName = fileName.replaceAll("~/","").replaceAll("\\.\\./","");
	String fileWidth = request.getParameter("fileWidth");
	String fileHeight = request.getParameter("fileHeight");
	String legisType = request.getParameter("typeLegislature");
	
	ActiviteNormativeService anService = SolonEpgServiceLocator.getActiviteNormativeService();
	String generatedReportPath;
	if(legisType==null || legisType.equalsIgnoreCase("courante") || legisType.equalsIgnoreCase("null")) {
		generatedReportPath = anService.getPathDirANStatistiquesPublie();
	} else {
		LoginContext loginContext = Framework.login();
		CoreSession coreSession = SessionUtil.getCoreSession();
		generatedReportPath = anService.getPathDirANStatistiquesLegisPrecPublie(coreSession);
		loginContext.logout();
	}

	
	File f = new File(generatedReportPath+"/"+fileName+".html");
	if(fileName!=null && f.exists()){
		FileReader file = new FileReader(generatedReportPath+"/"+fileName+".html");
		BufferedReader br = new BufferedReader(file);
		String s;
		out.print("<div id='cont-"+fileName+"' style='overflow:auto;width:1px;height:100px;'><div>");
		while ((s = br.readLine()) != null) {
		    out.println(s);
		}
		out.print("</div></div>");
		if (fileName.equals("listeDesLois") || fileName.equals("listeDesOrdonnances")) {
			out.print("<p style=\"font:italic 12px 'Lucida Grande', sans-serif;text-align:right;\">Tableau actualisé le "+DateUtil.formatWithHour(new Date(f.lastModified()))+"</p>");
		}
		out.print("<script>");
		if(fileWidth!=null && fileWidth!="null") {
			out.print("var wdt = "+fileWidth+";");
		}
		else {
			out.print("var wdt = $('#cont-"+fileName+"').parent().width()-10;");
		}
		if(fileHeight!=null && fileHeight!="null") {
			out.print("var hgt = "+fileHeight+";");
		}
		else {
			out.print("var hgt = $('#cont-"+fileName+"').parent().height()-40;");
		}
		out.print("var repHgt=$('#cont-"+fileName+" div:first-child').height()+30;if(repHgt<hgt){hgt=repHgt};");
		out.print("var repWdt=$('#cont-"+fileName+" div:first-child').width();");
		out.print("$('#cont-"+fileName+"').css({'height': hgt});");
		out.print("if(repWdt>wdt){$('#cont-"+fileName+"').css({'width': wdt});}else{$('#cont-"+fileName+"').css({'width': ''});}");
		out.print("</script>");
		out.flush();
	}
	else {
	    out.print("<h4>Ce tableau n'a pas encore été publié</h4>");
	}
}catch(Exception e){}
%>

