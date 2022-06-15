package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.documentmodel.ListeTraitementPapier;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

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
     */
    DocumentModelList getListeTraitementPapierOfDossierListAndType(
        CoreSession session,
        List<String> docsId,
        String typeListe
    );

    /*
     * Récupérer la liste des Traitement papier par date et type de liste
     */
    DocumentModelList getListeTraitementPapierFromTypeAndDate(
        final CoreSession session,
        final String dateDebut,
        final String dateFin,
        final List<String> typeListe
    );

    /**
     * recuperer la liste de traitement papier an basant sur le dossier et la type
     *
     * @param session la session
     * @param docId le dossier Id
     * @param typeListe type de la liste
     * @return
     */
    DocumentModelList getListeTraitementPapierOfDossierAndType(CoreSession session, String docId, String typeListe);

    /**
     * creer une Nouvelle Liste de Traitement Papier pour Mise en signature
     *
     * @param session la session
     * @param documentIdsList la liste des dossiers de la lite a creer
     * @param typeSignature type de signataire
     */
    ListeTraitementPapier creerNouvelleListeTraitementPapierMiseEnSignature(
        CoreSession session,
        String typeSignataire,
        List<DocumentModel> documentIdsList
    );

    /**
     * creer une Nouvelle Liste de Traitement Papier pour Demandes D'epreuves
     *
     * @param session la session
     * @param documentIdsList la liste des dossiers de la lite a creer
     */
    void creerNouvelleListeTraitementPapierDemandesEpreuve(CoreSession session, List<DocumentModel> documentIdsList);

    /**
     * creer une Nouvelle Liste de Traitement Papier pour Demandes De Publication
     *
     * @param session la session
     * @param documentIdsList la liste des dossiers de la lite a creer
     */
    void creerNouvelleListeTraitementPapierDemandesDePublication(
        CoreSession session,
        List<DocumentModel> documentIdsList
    );

    /**
     * Traiter en serie les dates des dossiers pour la liste de traitement de papiers pour signataire SGG
     *
     * @param session la session
     * @param docList the list de dossirs a traiter
     * @param property le nom de la propreiete a traiter en serie
     * @param value la nouvelle date
     */
    void traiterEnSerieSignataireSGGUnrestricted(CoreSession session, List<DocumentModel> docList, Calendar value);

    /**
     * Traiter en serie les dates des dossiers pour la liste de traitement de papiers pour signataire PM
     *
     * @param session la session
     * @param docList the list de dossirs a traiter
     * @param property le nom de la propreiete a traiter en serie
     * @param value la nouvelle date
     */
    void traiterEnSerieSignatairePMUnrestricted(CoreSession session, List<DocumentModel> docList, Calendar value);

    /**
     * Traiter en serie les dates des dossiers pour la liste de traitement de papiers pour signataire PM
     *
     * @param session la session
     * @param docList the list de dossirs a traiter
     * @param property le nom de la propreiete a traiter en serie
     * @param dateRetour la nouvelle date de retour
     * @param dateEnvoi la nouvelle date d'envoi
     */
    void traiterEnSerieSignatairePRUnrestricted(
        CoreSession session,
        List<DocumentModel> docList,
        Calendar dateRetour,
        Calendar dateEnvoi
    );

    /**
     * Traiter en serie les dates des dossiers pour la liste de traitement de papiers pour demande d'epreuve
     * @param session la session
     * @param docList the list de dossirs a traiter
     * @param property le nom de la propreiete a traiter en serie
     * @param value valeur de date pour le
     */
    void traiterEnSerieDemandeEpreuveUnrestricted(CoreSession session, List<DocumentModel> docList, Calendar value);

    void updateFichePresentationAfterUpdateBordereau(CoreSession session, DocumentModel doc);

    /*
     * Exporter la liste au format pdf
     */
    File exportPdfListe(CoreSession session, DocumentModel listeDoc) throws IOException;
}
