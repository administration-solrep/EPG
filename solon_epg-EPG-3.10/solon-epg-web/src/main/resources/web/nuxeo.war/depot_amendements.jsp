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
    <title>S.O.L.O.N. / Dépôt des amendements</title>
    <style type="text/css">
    <!--
    .mainTbl, .mainTbl td {
    	font-family: 'Lucida Grande', sans-serif;
    	font-size: 12px;
    }
    p {
		text-indent: 50px;
    }
    ul {
    	margin-left: 30px;
    }
    li {
    	padding-left: 20px;
    }
    -->
    </style>
    <script type="text/javascript" src="technique/jquery.min.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
	   if($.browser.mozilla)$("#bkgImg").css("position", "fixed");
	   else window.onscroll = function(){
			document.getElementById("bkgImg").style.top = document.body.scrollTop+5;
			document.getElementById("bkgImg").style.left = document.body.scrollLeft+5;
		}
	  });
	</script>
  </head>
  <body>
		<img id="bkgImg" style="position:absolute;top:1px;left:1px;margin:1px;width:99%;height:99%;z-index:-1;opacity:.3;filter:alpha(opacity=30);" src="<%=context%>/img/theme_page_accueil/fond_sceaux.png">
		<table class="mainTbl" width="100%" height="100%" cellpadding="0" cellspacing="0">
		<tr height="30px">
			<td>
				<a href="<%=context%>/login.jsp">S.O.L.O.N.</a>
				&nbsp;/&nbsp;
				<i>Dépôt des amendements</i>
				<br/><br/>
			</td>
		</tr><tr><td>
		<table cellspacing="0" cellpadding="0" border="0" width="80%" height="100%" align="center">
			<tr>
				<td valign="top">
					<p>Le premier alinéa de l’article 44 de la Constitution dispose que : <i>« Les membres du Parlement et le Gouvernement ont le droit d’amendement. Ce droit s’exerce en séance ou en commission selon les conditions fixées par les règlements des assemblées, dans le cadre déterminé par une loi organique »</i>. Le chapitre III de la <a href="http://www.legifrance.gouv.fr/affichTexte.do?cidTexte=JORFTEXT000020521873&fastPos=1&fastReqId=1318435320&categorieLien=cid&oldAction=rechTexte">loi organique du 15 avril 2009</a> en précise les conditions d’application.</p>
					<p>Les amendements ont pour objet de modifier un texte soumis à la délibération d’une assemblée. Ainsi, ils portent suppression, nouvelle rédaction, substitution ou insertion. Ils peuvent également introduire des dispositions additionnelles sous réserve qu’elles présentent, en première lecture, un lien, même indirect, avec le texte déposé ou transmis et, après la première lecture, en application de la « règle de l’entonnoir », une relation directe avec les dispositions restant en discussion<sup><small>1</small></sup>.</p>
					<p>Un amendement ne peut porter que sur un seul article. Si les modifications envisagées portent sur plusieurs articles, il convient de déposer autant d'amendements qu'il y a d'articles. Par ailleurs, lorsque plusieurs modifications d'un même article sont envisagées, sauf si elles sont de pure conséquence, il convient également de déposer plusieurs amendements.</p>
					<p>Les amendements faisant l’objet d’un accord interministériel doivent être déposés, selon le stade de la procédure auquel on se situe, auprès de la commission ou du service de la séance de l’assemblée devant laquelle se déroule l’examen du texte. Le dépôt s’effectue au secrétariat de la commission saisie au fond au stade de l’examen en commission.</p>
					<p><b>Les assemblées ont dématérialisé la procédure de dépôt des amendements</b>. On veillera par conséquent à effectuer ce dépôt par transmission de la version électronique de l’amendement :</p>
					<p>- <u><b>à l’Assemblée nationale</b></u>, via un portail dédié :</p>
					<p>Dès le début de la XIVème législature, le dépôt des amendements pour l’examen en séance ne pourra plus se faire par courrier électronique. Il conviendra donc d’utiliser l’application suivante :</p> 
					<p><center><a href="https://portail.assemblee-nationale.fr">https://portail.assemblee-nationale.fr</a></center></p>
					<p>Pour accéder à ce portail et déposer des amendements, il est nécessaire de disposer d’identifiants, en envoyant une demande en ce sens à l’adresse <a href="mailto:seance.admin@assemblee-nationale.fr">seance.admin@assemblee-nationale.fr</a>. Merci de préciser dans votre demande vos nom/prénom/fonction occupée/numéro de téléphone (portable de préférence).</p>
					<p>Il serait préférable que chaque ministère désigne un interlocuteur unique en charge du dépôt des amendements.</p>
					<p>Ces mêmes modalités de dépôt s’appliqueront dès le mois d’octobre en commission.</p>
					<p>L’Assemblée proposera donc dès juin 2012 une formation courte pour les personnels des ministères concernés (si vous êtes intéressés, merci d’envoyer une demande en ce sens à l’adresse électronique ci-dessus).</p>
					<p>- <u><b>au Sénat</b></u>, en se connectant à l’application <a href="https://ameli.senat.fr/ameli/">Ameli</a> accessible au bas de chaque page du <a href="http://www.senat.fr">site du Sénat</a>. Le dépôt via Ameli nécessite l’obtention préalable d’un identifiant et d’un mot de passe. Merci d’adresser votre demande à l’adresse amendements@senat.fr.</p>
					<p>Il serait préférable que chaque ministère désigne un interlocuteur unique en charge du dépôt des amendements, tant à l’Assemblée nationale qu’au Sénat.</p>
					<br/>
					<p>Tout amendement doit comporter les éléments suivants :</p>
					<p>
						<ul>
							<li>le titre du texte et, si possible, son numéro d’impression dans l’assemblée et pour le niveau de lecture concernés ;</li>
							<li>l’article amendé ou, le cas échéant, l’amendement sous-amendé ;</li>
							<li>le dispositif de l’amendement ;</li>
							<li>l’exposé des motifs.</li>
						</ul>
					</p>
					<br/>				
				</td>
			</tr>
			<tr>
				<td>
					<hr/>
					<sup><small>1</small></sup> <i>Sauf si l’amendement assure le respect de la Constitution, opère une coordination avec des textes en cours de discussion ou corrige une erreur matérielle.</i>
				</td>
			</tr>
	 	</table>
	 	</td></tr></table>
  </body>
</html>