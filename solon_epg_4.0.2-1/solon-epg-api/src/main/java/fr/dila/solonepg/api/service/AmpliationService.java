package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.Dossier;
import java.util.List;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface AmpliationService {
    /**
     * ajouter un fichier ampliation
     *
     * @param repertoireParent le repertoire de parent
     * @param blob le contenuudu fichier
     * @param session la session
     * @param currentUser
     * @return
     */
    DocumentModel ajouterAmpliationFichier(DocumentModel repertoireParent, Blob blob, CoreSession session);

    void supprimerAmpliationFichiers(CoreSession session, final DocumentModel dossierDoc);

    void renommerAmpliationFichier(CoreSession session, DocumentModel dossierDoc, String nouveauNom);

    /**
     * recuperer le contenu du mail d'ampliation
     *
     * @param session la session
     * @param dossierDoc le dossier
     * @return
     */
    String getAmpliationMailText(CoreSession session, DocumentModel dossierDoc);

    /**
     * recuperer le sujet du mail d'ampliation
     *
     * @param session la session
     * @param dossierDoc le dossier
     * @return
     */
    String getAmpliationMailObject(CoreSession session, DocumentModel dossierDoc);

    /**
     * sendAmpliation Mail
     *
     * @param session
     * @param dossierDoc
     * @param objet
     * @param text
     */
    void sendAmpliationMail(
        CoreSession session,
        DocumentModel dossierDoc,
        String expediteur,
        List<String> destinatairesEmails,
        String objet,
        String text
    );

    /**
     * création du document Ampliation à partir du dossier
     *
     * @param dossier
     * @param session
     * @return
     */
    DocumentModel createAmpliationDossierDocument(Dossier dossier, CoreSession session);

    /**
     *
     * @param doc
     * @param session
     * @return
     */
    String getNomAmpliationFichier(DocumentModel doc, CoreSession session);

    /**
     *
     * @param doc
     * @param session
     * @return
     */
    DocumentModel getAmpliationFichier(DocumentModel doc, CoreSession session);
}
