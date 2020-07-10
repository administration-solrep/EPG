<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.io.FileReader"%>
<%@ page import="java.io.BufferedReader"%>
<%@ page import="java.io.BufferedInputStream"%>
<%@ page import="java.io.BufferedOutputStream"%>
<%@ page import="java.io.FileInputStream"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.io.IOException"%>
<%@ page import="org.apache.commons.io.IOUtils"%>
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
	//ConfigService configService = STServiceLocator.getConfigService();
	//String generatedReportPath = configService.getValue(SolonEpgConfigConstant.SOLONEPG_AP_STATS_PUBLIE_DIRECTORY);
	DossierService mgppDossierService = SolonMgppServiceLocator.getDossierService();
	String generatedReportPath = mgppDossierService.getPathAPPublieRepertoire();

	File f = new File(generatedReportPath+"/"+fileName+".pdf");
	String filename = fileName+".pdf";
	if(fileName!=null && f.exists()){
	  
		 response.setContentType ("application/pdf");
		 //set the header and also the Name by which user will be prompted to save
		 response.setHeader ("Content-Disposition", "attachment;filename=\""+ f.getName() +"\"");
		 
		  InputStream in = new FileInputStream(f);
		  ServletOutputStream outs = response.getOutputStream();
		  
		  int bit = 256;
		  int i = 0;
		  try {
		   while ((bit) >= 0) {
		    bit = in.read();
		    outs.write(bit);
		   }

		  } catch (IOException ioe) {
		   ioe.printStackTrace(System.out);
		  }

		  outs.flush();
		  outs.close();
		  in.close();
		
		}
		
		
	else {
	    out.print("<h4>Ce tableau n'a pas encore été publié</h4>");
	}
}catch(Exception e){
  e.printStackTrace();
}
%>

