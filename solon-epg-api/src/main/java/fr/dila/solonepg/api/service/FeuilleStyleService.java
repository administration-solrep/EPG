package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.typescomplexe.InfoFeuilleStyleFile;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Classe de service pour la gestion des feuilles de style.
 *
 * @author ARN
 */
public interface FeuilleStyleService extends Serializable {
    /**
     * Vérifie que le fichier possède les paramètres d'une feuille de style, ajoute dans le fichier une métadonnée pour identifier la feuille de style et renvoie le fichier modifié.
     *
     * @param feuilleStyleFile fichier de feuille de style
     * @param session session
     * @return File renvoie le fichier modifie si
     */
    File checkFeuilleStyleValidAndTag(File feuilleStyleFile, CoreSession session, boolean isActeIntegral);

    /**
     * Vérifie que le fichier possède les paramètres d'une feuille de style, ajoute dans le fichier une métadonnée pour l'identifier la feuille de style et renvoie cet identifiant.
     *
     * @param feuilleStyleFile fichier de feuille de style
     * @param session session
     * @param isActeIntegral vrai si le répertoire parent est celui de l'acte intégral
     * @return String identifiant
     */
    String validateFeuilleStyle(File feuilleStyleFile, CoreSession session, boolean isActeIntegral);

    /**
     * Vérifie que le fichier possède les paramètres d'une feuille de style valide.
     *
     * @param feuilleStyleFile fichier de feuille de style
     * @param feuilleStyleName nom du fichier de style
     * @param session session
     * @return Boolean renvoie vrai si la feuille de stye est valide
     */
    Boolean checkFeuilleStyleValid(
        File feuilleStyleFile,
        String feuilleStyleName,
        CoreSession session,
        boolean isActeIntegral
    );

    /**
     * Vérifie que le fichier récupéré est valide (applique les règles de gestion liées au parapheur et fond de dossier).
     *
     * @param fichier fichier téléchargé
     * @param fileName nom du fichier
     * @param documentId identifiant du répertoire où l'on va ajouter le fichier ou fichier dont on va rajouter une version
     * @param session session
     * @param dossier dossier
     * @return String renvoie null si le fichier est accepté au vu des règles de gestion, renvoi un message d'erreur sinon
     */
    String checkFichierParapheurValid(
        File fichier,
        String fileName,
        String documentId,
        CoreSession session,
        DocumentModel dossier
    );

    /**
     * Met à jour les métadonnées du dossier à partir des métadonnées du fichier uploadé dans le parapheur (extrait ou type d'acte) si une feuille de style est présente
     * @param session session
     * @param fichierStream fichier téléchargé
     * @param fileName nom du fichier
     * @param repertoireDoc répertoire où l'on va ajouter le fichier ou fichier dont on va rajouter une version
     * @param dossierDoc dossier
     *
     * @return null si la mise à jour de la feuille de style s'est effectué correctement, message d'information sur l'erreur sinon.
     * @throws Exception
     */
    String updateDossierMetadataFromParapheurFile(
        CoreSession session,
        InputStream fichierStream,
        String fileName,
        DocumentModel repertoireDoc,
        DocumentModel dossierDoc
    );

    /**
     * Retourne les feuilles de styles correspondant au type acte envoyé en paramètre sous forme de liste.
     *
     * @param typeActe type d'acte
     *
     * @return List<InfoFeuilleStyleFile> feuilles de styles
     */
    List<InfoFeuilleStyleFile> getFeuilleStyleListFromTypeActe(String typeActe) throws Exception;

    /**
     * Récupère les informations d'un fichier en vue de son affichage
     *
     * @param docId
     * @param filename
     * @return
     * @throws Exception
     */
    InfoFeuilleStyleFile getFeuilleStyleInfo(String docId, String filename) throws Exception;
}
