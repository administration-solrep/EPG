package fr.dila.solonepg.api.service;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.Dossier;

public interface AmpliationService {
    /**
     * ajouter un fichier ampliation
     * 
     * @param repertoireParent le repertoire de parent
     * @param fileName le nom d file
     * @param blob le contenuudu fichier
     * @param session la session
     * @param currentUser
     * @return
     * @throws ClientException
     */
    DocumentModel ajouterAmpliationFichier(DocumentModel repertoireParent, String fileName, Blob blob, CoreSession session) throws ClientException;

    /**
     * supprimer un fichier d'ampliation
     * 
     * @param session la session
     * @param ampliationFileDoc le fichir d'ampliation
     * @throws ClientException
     */
    void supprimerAmpliationFichierUnrestricted(CoreSession session, final DocumentModel dossierDoc) throws ClientException;

    /**
     * recuperer le contenu du mail d'ampliation
     * 
     * @param session la session
     * @param dossierDoc le dossier
     * @return
     * @throws ClientException
     */
    String getAmpliationMailText(CoreSession session, DocumentModel dossierDoc) throws ClientException;

    /**
     * recuperer le sujet du mail d'ampliation
     * 
     * @param session la session
     * @param dossierDoc le dossier
     * @return
     * @throws ClientException
     */
    String getAmpliationMailObject(CoreSession session, DocumentModel dossierDoc) throws ClientException;

    /**
     * sendAmpliation Mail
     * 
     * @param session
     * @param dossierDoc
     * @param objet
     * @param text
     * @throws ClientException
     */
    void sendAmpliationMail(CoreSession session, DocumentModel dossierDoc, String objet, String text) throws ClientException;

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
     * @throws ClientException
     */
    String getNomAmpliationFichier(DocumentModel doc, CoreSession session) throws ClientException;

    /**
     * 
     * @param doc
     * @param session
     * @return
     * @throws ClientException
     */
    DocumentModel getAmpliationFichier(DocumentModel doc, CoreSession session) throws ClientException;

}
