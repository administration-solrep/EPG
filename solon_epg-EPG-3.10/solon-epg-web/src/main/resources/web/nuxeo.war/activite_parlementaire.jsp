<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page language="java"%>
<%@ page import="org.nuxeo.runtime.api.Framework"%>
<%@ page import="java.util.Locale"%>
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
    <title>S.O.L.O.N. / Applications des lois</title>
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
	  }
	.mainTbl td{
		  text-align:center;
		  vertical-align: top;
		  font: normal 12px "Lucida Grande", sans-serif;
	  	  color: #343434;
		  border: 1px solid black;
	}
	a,  a:hover, a:visited{
	 	color:#774743;
	}
	
	.listeBox{
	 	width:100%;
	 	height:300px;
	 	overflow:auto;
	 	padding:10px;
	 	max-width:500px;
	}
	.listeBox td{
	 	text-align:left;
	 	background: none;
	 	border:none;
	}
	
	.listeMinBox {
	 	height:300px;
	 	overflow:auto;
	 	padding: 10px;
	 	max-width:500px;
	  }
	.listeMinBox td{
	 	text-align:left;
	 	background: none;
	 	border:none;
	}
	.listeMinBox table{
		  text-align:center;
		  vertical-align: top;
		  font: normal 12px "Lucida Grande", sans-serif;
	  	  color: #343434;
		  /*border: 1px solid black;*/
	}
	.titreOep, .titreOepMin {
		font-weight: bold;
		margin: 2px 10px;
		cursor: pointer;
	}
	.dateOep, .dateOepMin {
		margin-right: 10px;
	}
	.listeDiv{
		border-bottom:1px solid black;
		padding:10px;
	}
	.minTitle{
		text-decoration:underline;
		padding-right: 5px;
	}
	.minTd{
		align:left;
	}
	.titreOepMin{
		padding-right: 15px;
	}
    -->
    </style>
    
	<script type="text/javascript" src="technique/jquery.min.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		/*$("#listeBox").load("openActiviteParlementaire.jsp?fileName=listeDesFichesOEP", function( response, status, xhr ) {
			   $("#listeMinBox").html( $(".minTable").clone() );
			   $(".minTable", "#listeBox").remove();

			   $(".txtLibre").html( $(".texteLibre") );
		   });*/

		$.post("openActiviteParlementaire.jsp", {fileName:"listeDesFichesOEP"}, function(data){
			$("#listeBox").html(data);
			$("#listeMinBox").html( $(".minTable").clone() );
			$(".minTable", "#listeBox").remove();

			$(".txtLibre").html( $(".texteLibre") );
		});
	}); 

	function openHtmlFile(fileName){
		//$(".globalTbl > tbody:last > tr:first > td").load("openActiviteParlementaire.jsp?fileName="+encodeURIComponent(fileName));
		$.post("openActiviteParlementaire.jsp", {fileName:fileName}, function(data){ 
			$(".globalTbl > tbody:last > tr:first > td").html(data);
		});
		$(".breadcrumb").append(" / <a href=\"<%=context%>/activite_parlementaire.jsp\">Activité Parlementaire</a>");
	}  

	</script>

  </head>
  <body>
  <img style="saturate:100;position:absolute;top:5px;margin:1px;width:99%;height:99%;z-index:-1;opacity:.3;filter:alpha(opacity=30);" src="<%=context%>/img/theme_page_accueil/fond_sceaux.png">
	<table width="100%" height="100%" cellpadding="0" cellspacing="0" class="globalTbl">
		<tr height="30px">
			<td class="breadcrumb">
				&nbsp;<a href="<%=context%>/login.jsp">S.O.L.O.N.</a>
			</td>
		</tr>
		<tr>
			<td class="topTitle">
				LISTE OEP
			</td>
		</tr>
		<tr>
			<td class="txtLibre">
				text libre
			</td>
		</tr>
		<tbody>
		<tr>
			<td align="center">

				<table border="0" cellpadding="0" cellspacing="10" class="mainTbl">
					<tr>
						<td>
							<div class="listeDiv">
								<span class="title">Liste des organismes extraparlementaires</span>
								<br/>
								<span>(classés par ordre alphabétique)</span>
							</div>
							<div class="listeBox" id="listeBox"></div>
						</td>
						<td>
							<div class="listeDiv">
								<span class="title">Liste des organismes extraparlementaires</span>
								<br/>
								<span>(classés par ministère)</span>
							</div>
							<div class="listeMinBox" id="listeMinBox"></div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		</tbody>
	</table>
	<span class="trg"></span>
  </body>
</html>