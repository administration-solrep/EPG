<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.io.FileReader"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="fr.dila.st.core.util.DateUtil"%>
<%//@ page import="fr.dila.st.api.service.ConfigService"%>
<%//@ page import="fr.dila.solonepg.api.constant.SolonEpgConfigConstant"%>
<%//@ page import="fr.dila.st.core.service.STServiceLocator"%>
<%@ page import="fr.dila.solonmgpp.core.service.SolonMgppServiceLocator"%>
<%@ page import="fr.dila.solonmgpp.api.service.DossierService"%>
		 
<%
try{
	String fileName = request.getParameter("fileName");
	fileName = fileName.replaceAll("~/","").replaceAll("\\.\\./","");
	String fileWidth = request.getParameter("fileWidth");
	String fileHeight = request.getParameter("fileHeight");
	//ConfigService configService = STServiceLocator.getConfigService();
	//String generatedReportPath = configService.getValue(SolonEpgConfigConstant.SOLONEPG_AP_STATS_PUBLIE_DIRECTORY);
	DossierService mgppDossierService = SolonMgppServiceLocator.getDossierService();
	String generatedReportPath = mgppDossierService.getPathDirAPPublie();
	String generatedReportPathRepertoire = mgppDossierService.getPathAPPublieRepertoire();
	String extension = "html";
	
	File f = new File(generatedReportPath+"/"+fileName+"."+extension);
	File f_inside = new File(generatedReportPathRepertoire+"/"+fileName+"."+extension);
	if(fileName!=null && (f.exists() || f_inside.exists())){
	  
		FileReader file = null;
		if(f.exists()){
		  file = new FileReader(generatedReportPath+"/"+fileName+"."+extension);
		} else {
		  file = new FileReader(generatedReportPathRepertoire+"/"+fileName+"."+extension);
		}
		BufferedReader br = new BufferedReader(file);
		String s;
		//out.print("<div id='cont-"+fileName+"' style='overflow:auto;width:1px;height:100px;'><div>");
		while ((s = br.readLine()) != null) {
		    out.println(s);
		}
		//out.print("</div></div>");
		//out.print("<p style=\"font:italic 12px 'Lucida Grande', sans-serif\">Tableau actualisé le "+DateUtil.formatWithHour(new Date(f.lastModified()))+"</p>");
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

