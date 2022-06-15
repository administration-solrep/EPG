package fr.dila.solonepg.api.service;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.birt.BirtResultatFichier;
import fr.dila.st.api.event.batch.BatchLoggerModel;
import java.io.Serializable;
import java.util.Map;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface StatsGenerationResultatService extends Serializable {
    /**
     * generer tous les rapport  birt (html,pdf,xls )et sauvegarder dans la base.
     *
     * @param session
     * @param reportName  le nom du rappoert a generer
     * @param reportFile  le nom du fichhier rptdesign du fichier a generer
     * @param inputValues les parametres d'entree du rapport
     */
    void generateAllReportResult(
        CoreSession session,
        String reportName,
        String reportFile,
        Map<String, Serializable> inputValues,
        ANReportEnum report,
        final BatchLoggerModel batchLoggerModel,
        boolean curLegis
    );

    /**
     * recuperer le documment qui conient le resultat du rapport
     *
     * @param session
     * @param reportName le nim du report
     * @return
     */
    DocumentModel getBirtResultatFichier(CoreSession session, String reportName);

    /**
     * recupere la liste de ministere du pan pour les passer en parametres pour les rapports qui en ont besoin (on recupere ces infos de ldap)
     *
     * @param session : la session permettant d'effectuer la requête sql
     * @return
     */
    String getMinisteresListBirtReportParam(CoreSession session);

    /**
     * recupere la liste de ministeres du gouvernement en cours pour les passer en parametres pour les rapports qui en ont besoin (on recupere ces infos de ldap)
     *
     * @return
     */
    String getMinisteresCourantListBirtReportParam();

    void publierReportResulat(
        String reportName,
        String idMinistere,
        String nor,
        Blob blobHtml,
        boolean curLegis,
        CoreSession session
    );

    /**
     * publier le resultat du report
     *
     * @param session
     * @param reportName le nom du rapport ba publier
     * @param blobHtml   le contenu html du rapport
     */
    void publierReportResulat(String reportName, Blob blobHtml, boolean curLegis, CoreSession session);

    /**
     * publier le resultat du report
     *
     * @param reportName le nom du rapport ba publier
     * @param blobHtml   le contenu html du rapport
     * @param path       le chemin de rapport
     */
    void publierReportResulat(String reportName, Blob blobHtml, String path);

    /**
     * publier le resultat du report
     *
     * @param reportName le nom du rapport ba publier
     * @param blobHtml   le contenu html du rapport
     * @param path       le chemin de rapport
     */
    void publierReportResulat(String reportName, Blob blobHtml, String path, String extension);

    /**
     * sauvegarder le resultat du report
     *
     * @param session
     * @param reportName le nom du rapport ba publier
     * @param blobHtml   le contenu html du rapport
     * @param blobPdf    le contenu pdf du rapport
     * @param blobXls    le contenu xls du rapport
     * @return
     */
    BirtResultatFichier saveReportResulat(
        CoreSession session,
        String reportName,
        Blob blobHtml,
        Blob blobPdf,
        Blob blobXls
    );

    /**
     * genrer un rapport birt (en format outPutFormat)sans le sauvegarde de la base
     *
     * @param reportName   le nom du rapport
     * @param reportFile   le fichier rptdesign du rapport
     * @param inputValues  les parametre d'entrer pour le rapport birt
     * @param outPutFormat le format du rapport generer (html, pdf, ou excel)
     * @return
     */
    Blob generateReportResult(
        String reportName,
        String reportFile,
        Map<String, Serializable> inputValues,
        BirtOutputFormat outPutFormat
    );

    /**
     * @return
     */
    String getDirectionsListBirtReportParam();
}
