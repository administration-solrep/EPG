package fr.dila.solonepg.api.service;


import java.io.Serializable;
import java.util.Map;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.st.api.event.batch.BatchLoggerModel;


public interface StatsGenerationResultatService extends Serializable {
    
    /**
     * generer tous les rapport  birt (html,pdf,xls )et sauvegarder dans la base. 
     * 
     * @param session
     * @param reportName le nom du rappoert a generer
     * @param reportFile le nom du fichhier rptdesign du fichier a generer 
     * @param inputValues les parametres d'entree du rapport
     * @throws ClientException
     */
    void generateAllReportResult(CoreSession session, String reportName, String reportFile, Map<String, String> inputValues, String reportId, final BatchLoggerModel batchLoggerModel,boolean curLegis) throws ClientException;
    
    /**
     * recuperer le documment qui conient le resultat du rapport
     * @param session
     * @param reportName le nim du report
     * @return
     * @throws ClientException
     */
    DocumentModel getBirtResultatFichier(CoreSession session, String reportName) throws ClientException;
    
    /**
     * recupere la liste de ministere du pan pour les passer en parametres pour les rapports qui en ont besoin (on recupere ces infos de ldap)
     * @param session : la session permettant d'effectuer la requête sql 
     * @return
     * @throws ClientException
     */
    String getMinisteresListBirtReportParam(CoreSession session) throws ClientException;
    
    /**
     * recupere la liste de ministeres du gouvernement en cours pour les passer en parametres pour les rapports qui en ont besoin (on recupere ces infos de ldap)
     *  
     * @return
     * @throws ClientException
     */
    String getMinisteresCourantListBirtReportParam() throws ClientException;
    
     /**
      * publier le resultat du report
      * 
      * @param session
      * @param reportName le nom du rapport ba publier
      * @param blobHtml le contenu html du rapport
      * @param blobXls le contenu xls du rapport
      * @param blobPdf le contenu pdf du rapport
      * @throws ClientException
      */
    void publierReportResulat(String reportName, Blob blobHtml,boolean curLegis,CoreSession session) throws ClientException;
    
    /**
     * publier le resultat du report
     * 
     * @param session
     * @param reportName le nom du rapport ba publier
     * @param blobHtml le contenu html du rapport
     * @param blobXls le contenu xls du rapport
     * @param blobPdf le contenu pdf du rapport
     * @param path le chemin de rapport
     * @throws ClientException
     */
   void publierReportResulat(String reportName, Blob blobHtml, String path) throws ClientException;
    
   /**
    * publier le resultat du report
    * 
    * @param session
    * @param reportName le nom du rapport ba publier
    * @param blobHtml le contenu html du rapport
    * @param blobXls le contenu xls du rapport
    * @param blobPdf le contenu pdf du rapport
    * @param path le chemin de rapport
    * @param extesnion l'extension du rapport genere (html, pdf, ...)
    * @throws ClientException
    */
  void publierReportResulat(String reportName, Blob blobHtml, String path, String extension) throws ClientException;
   
    /**
     * sauvegarder le resultat du report
     * 
     * @param session
     * @param reportName le nom du rapport ba publier
     * @param blobHtml le contenu html du rapport
     * @param blobXls le contenu xls du rapport
     * @param blobPdf le contenu pdf du rapport
     * @throws ClientException
     */
    void saveReportResulat(CoreSession session, String reportName,  Blob blobHtml, Blob blobPdf, Blob blobXls) throws ClientException;
    
    /**
     * genrer un rapport birt (en format outPutFormat)sans le sauvegarde de la base
     * 
     * @param reportName le nom du rapport
     * @param reportFile le fichier rptdesign du rapport
     * @param inputValues les parametre d'entrer pour le rapport birt
     * @param outPutFormat le format du rapport generer (html, pdf, ou excel)
     * @return
     * @throws ClientException
     */
    Blob generateReportResult(String reportName, String reportFile, Map<String, String> inputValues, String outPutFormat) throws ClientException ;
    
    /**
     * 
     * @return
     * @throws ClientException
     */
    String getDirectionsListBirtReportParam() throws ClientException ;

	boolean isRapportPubliable(String anReportId);

}
