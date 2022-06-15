package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.Dossier;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Service de gestion du bordereau du dossier SOLON EPG.
 *
 * @author asatre
 */
public interface DossierBordereauService extends fr.dila.st.api.service.LogDocumentUpdateService {
    /**
     * passe un dossier au statut publié et remplit par défault les valeurs concernant la publication JORF. utilisé pour
     * tester l'état publié des dossiers sans passer par les webservices
     *
     * @param dossier
     * @param session
     */
    void publierDossierStub(DocumentModel dossier, CoreSession session);

    void terminerDossierSansPublication(DocumentModel dossier, CoreSession session);

    /**
     * Renvoie vrai si la référence de l'application de la loi respecte l'expression règulière défini dans le
     * paramétrage technique.
     *
     * @return vrai si la référence de l'application de la loi respecte l'expression règulière défini dans le
     *         paramétrage technique.
     */
    boolean isFieldReferenceLoiValid(CoreSession session, String value);

    /**
     * Renvoie vrai si le numéro d'ordre respecte l'expression règulière défini dans le paramétrage technique.
     *
     * @return vrai si le numéro d'ordre respecte l'expression règulière défini dans le paramétrage technique.
     */
    boolean isFieldNumeroOrdreValid(CoreSession session, String value);

    /**
     * Renvoie vrai si la référence de l'application de l'ordonnance respecte l'expression règulière défini dans le
     * paramétrage technique.
     *
     * @return vrai si la référence de l'application de l'ordonnance respecte l'expression règulière défini dans le
     *         paramétrage technique.
     */
    boolean isFieldReferenceOrdonnanceValid(CoreSession session, String value);

    /**
     * Réinitialise les expressions régulières qui vérifient le numéro d'ordre et les références.
     */
    void reset();

    /**
     * Vérifie la complétude du bordereau du dossier
     *
     * @param session
     * @param dossier
     * @return
     */
    boolean isBordereauComplet(Dossier dossier);

    /**
     * Retourne les métadonnées obligatoires qui ne sont pas renseignées dans le bordereau du dossier
     *
     * @param dossierDoc
     * @return
     */
    String getBordereauMetadonnesObligatoiresManquantes(DocumentModel dossierDoc);

    /**
     * ajoute les publications conjointes au dossier et des autres dossiers conjoints
     *
     * @param session
     * @param dossier
     * @param norsAdded
     *            : les nouveaux nors à ajouter aux dossiers conjoints
     * @param norsUnknown
     *            : la liste de retour des nors non reconnus
     * @param norsAlreadyPublished
     *            : la liste de retour des nors à l'état publiés et donc non ajoutables
     * @return true si tous les nors ont pu être ajoutés, false sinon.
     */
    boolean addPublicationsConjointes(
        CoreSession session,
        Dossier dossier,
        List<String> norsAdded,
        List<String> norsUnknown,
        List<String> norsAlreadyPublished
    );

    /**
     * retire le nor des publications conjointes au dossier et des autres dossiers conjoints
     *
     * @param session
     * @param dossier
     * @param norRemoved
     */
    void removePublicationConjointe(CoreSession session, Dossier dossier, String norRemoved);

    /**
     * Propage les infos de publication conjointe dans un dossier destination
     *
     * @param dossierDest
     * @param delaiPublication
     * @param datePreciseePublication
     * @param modeParution
     */
    void propagatePublicationData(
        Dossier dossierDest,
        String delaiPublication,
        Calendar datePreciseePublication,
        String modeParution
    );
}
