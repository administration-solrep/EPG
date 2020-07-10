package fr.dila.solonepg.api.service;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

import fr.dila.solonepg.api.documentmodel.ListeTraitementPapier;

/**
 * service pour la gestion des liste de traitement papier
 * 
 * @author admin
 */

public interface ListeTraitementPapierService {

    /**
     * recuperer la liste de traitement papier an basant sur la liste dossiers et la type
     * 
     * @param session la session
     * @param docsId la liste des dossier Id
     * @param typeListe type de la liste
     * @return
     * @throws ClientException
     */
    DocumentModelList getListeTraitementPapierOfDossierListAndType(CoreSession session, List<String> docsId, String typeListe) throws ClientException;

    /**
     * recuperer la liste de traitement papier an basant sur le dossier et la type
     * 
     * @param session la session
     * @param docId le dossier Id
     * @param typeListe type de la liste
     * @return
     * @throws ClientException
     */
    DocumentModelList getListeTraitementPapierOfDossierAndType(CoreSession session, String docId, String typeListe) throws ClientException;

    /**
     * creer une Nouvelle Liste de Traitement Papier pour Mise en signature
     * 
     * @param session la session
     * @param documentIdsList la liste des dossiers de la lite a creer
     * @param typeSignature type de signataire
     * @throws ClientException
     */
    void creerNouvelleListeTraitementPapierMiseEnSignature(CoreSession session, String typeSignataire, List<DocumentModel> documentIdsList) throws ClientException;

    /**
     * creer une Nouvelle Liste de Traitement Papier pour Demandes D'epreuves
     * 
     * @param session la session
     * @param documentIdsList la liste des dossiers de la lite a creer
     * @throws ClientException
     */
    void creerNouvelleListeTraitementPapierDemandesEpreuve(CoreSession session, List<DocumentModel> documentIdsList) throws ClientException;

    /**
     * creer une Nouvelle Liste de Traitement Papier pour Demandes De Publication
     * 
     * @param session la session
     * @param documentIdsList la liste des dossiers de la lite a creer
     * @throws ClientException
     */
    void creerNouvelleListeTraitementPapierDemandesDePublication(CoreSession session, List<DocumentModel> documentIdsList) throws ClientException;

    /**
     * Traiter en serie les dates des dossiers pour la liste de traitement de papiers pour signataire SGG
     * 
     * @param session la session
     * @param docList the list de dossirs a traiter
     * @param property le nom de la propreiete a traiter en serie
     * @param value la nouvelle date
     * @throws ClientException
     */
    public void traiterEnSerieSignataireSGGUnrestricted(CoreSession session, List<DocumentModel> docList, Calendar value) throws ClientException;
    /**
     * Traiter en serie les dates des dossiers pour la liste de traitement de papiers pour signataire PM
     * 
     * @param session la session
     * @param docList the list de dossirs a traiter
     * @param property le nom de la propreiete a traiter en serie
     * @param value la nouvelle date
     * @throws ClientException
     */
    public void traiterEnSerieSignatairePMUnrestricted(CoreSession session, List<DocumentModel> docList, Calendar value) throws ClientException;
    /**
     * Traiter en serie les dates des dossiers pour la liste de traitement de papiers pour signataire PM
     * 
     * @param session la session
     * @param docList the list de dossirs a traiter
     * @param property le nom de la propreiete a traiter en serie
     * @param dateRetour la nouvelle date de retour
     * @param dateEnvoi la nouvelle date d'envoi
     * @throws ClientException
     */
    public void traiterEnSerieSignatairePRUnrestricted(CoreSession session, List<DocumentModel> docList, Calendar dateRetour,Calendar dateEnvoi) throws ClientException ;
    /**
     * Traiter en serie les dates des dossiers pour la liste de traitement de papiers pour demande d'epreuve
     * @param session la session
     * @param docList the list de dossirs a traiter
     * @param property le nom de la propreiete a traiter en serie
     * @param value valeur de date pour le
     * @throws ClientException
     */
    public void traiterEnSerieDemandeEpreuveUnrestricted(CoreSession session, List<DocumentModel> docList, Calendar value) throws ClientException;
    
    /**
     * Supprimer des dossiers de la liste, supprimer la liste si tous les dossiers sont supprimer
     * @param listeTraitementPapier
     * @param docs
     * @param session
     * @throws ClientException
     */
    public void supprimerDossiersDeLaListeUnrestricted(ListeTraitementPapier listeTraitementPapier, List<DocumentModel> docs, CoreSession session) throws ClientException;
}
