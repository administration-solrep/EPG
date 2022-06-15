package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgParametrageApplicationConstants;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.service.LogDocumentUpdateServiceImpl;
import fr.dila.st.core.service.STServiceLocator;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * Implémentation du service de gestion des paramètres de l'applciation (voir écran ECR_ADM_PAR_APP).
 *
 * @author arolin
 */
public class ParametreApplicationServiceImpl
    extends LogDocumentUpdateServiceImpl
    implements ParametreApplicationService {
    private static final long serialVersionUID = -5425270639646781913L;

    /**
     * Requete permettant de récupérer le document contenant les informations sur le paramètrage de l'application.
     */
    private static final String GET_PARAMETRAGE_APPLICATION_DOCUMENT_QUERY =
        "select * from " +
        SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE +
        " WHERE " +
        "ecm:isProxy = 0 ";

    private static final Set<String> UNLOGGABLE_ENTRY_LIST = Collections.unmodifiableSet(initUnloggableEntryList());

    private static Map<String, String> INIT_ENTRY_LABEL_LIST = new HashMap<>();

    static {
        INIT_ENTRY_LABEL_LIST.put(
            SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_DOSSIER_PAGE_PROPERTY,
            "Nombre de dossiers par page"
        );
        INIT_ENTRY_LABEL_LIST.put(
            SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DELAI_RAFRAICHISSEMENT_CORBEILLE_PROPERTY,
            "Délai de rafraichissement des corbeilles (mn)"
        );
        INIT_ENTRY_LABEL_LIST.put(
            SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_META_DISPO_CORBEILLE_PROPERTY,
            "Métadonnées actuellement disponible pour les utilisateurs"
        );
        INIT_ENTRY_LABEL_LIST.put(
            SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_DERNIERS_RESULTATS_PROPERTY,
            "Nombre de derniers résultats consultés"
        );
        INIT_ENTRY_LABEL_LIST.put(
            SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_FAVORIS_CONSULTATION_PROPERTY,
            "Nombre de favoris de consultation"
        );
        INIT_ENTRY_LABEL_LIST.put(
            SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_FAVORIS_RECHERCHE_PROPERTY,
            "Nombre de favoris de recherche"
        );
        INIT_ENTRY_LABEL_LIST.put(
            SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_DOSSIERS_SIGNALES_PROPERTY,
            "Nombre de dossiers signalés"
        );
        INIT_ENTRY_LABEL_LIST.put(
            SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_NB_TABLEAUX_DYNAMIQUES_PROPERTY,
            "Nombre de tableaux dynamiques"
        );
        INIT_ENTRY_LABEL_LIST.put(
            SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DELAI_ENVOI_MAIL_ALERTE_PROPERTY,
            "Délai avant envoi du mél de maintien d'une alerte (jours)"
        );
    }

    /**
     * Liste des valeurs xpath des champs qui n'ont pas à être loggé.
     *
     * @return
     */
    private static Set<String> initUnloggableEntryList() {
        return new HashSet<>();
    }

    private String paramAppliDocId = null;

    @Override
    public synchronized ParametrageApplication getParametreApplicationDocument(final CoreSession session) {
        DocumentModel parametreApplicationDocument = null;
        synchronized (this) {
            if (paramAppliDocId == null) {
                final DocumentModelList dml = session.query(GET_PARAMETRAGE_APPLICATION_DOCUMENT_QUERY);
                if (dml == null || dml.isEmpty()) {
                    throw new NuxeoException(
                        "Document " +
                        SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE +
                        "introuvable"
                    );
                } else if (dml.size() > 1) {
                    throw new NuxeoException(
                        "Plusieurs instances pour " +
                        SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE
                    );
                }
                parametreApplicationDocument = dml.get(0);
                paramAppliDocId = parametreApplicationDocument.getId();
            }
        }

        if (parametreApplicationDocument == null) {
            // id connu : charger la dernière vesion du document
            parametreApplicationDocument = session.getDocument(new IdRef(paramAppliDocId));
        }

        if (parametreApplicationDocument != null) {
            return parametreApplicationDocument.getAdapter(ParametrageApplication.class);
        } else {
            return null;
        }
    }

    @Override
    public List<DocumentModel> getAllNonAvailableColumnsDocument(final DocumentModel parametreApplication) {
        // récupération des identifiants des colonnes disponibles
        final List<String> metaDisponibleKeyList = getAvailablesColumnsIds(parametreApplication);
        // récupération de toutes les données
        final SolonEpgVocabularyService vocabularyService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
        return vocabularyService.getDocumentModelListFromDirectoryExceptFromIdList(
            STVocabularyConstants.BORDEREAU_LABEL,
            metaDisponibleKeyList
        );
    }

    @Override
    public List<DocumentModel> getAvailablesColumnsDocument(final DocumentModel parametreApplication) {
        // récupération des identifiants des colonnes disponibles
        final List<String> metaDisponibleKeyList = getAvailablesColumnsIds(parametreApplication);
        // récupérations des document model lié aux identifiants
        final SolonEpgVocabularyService solonEpgVocabularyService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
        return solonEpgVocabularyService.getDocumentModelListFromIdsList(
            STVocabularyConstants.BORDEREAU_LABEL,
            metaDisponibleKeyList
        );
    }

    @Override
    public List<String> getAvailablesColumnsIds(final DocumentModel parametreApplication) {
        // récupération des identifiants des colonnes disponibles
        final ParametrageApplication parametreApplicationDoc = parametreApplication.getAdapter(
            ParametrageApplication.class
        );
        return parametreApplicationDoc.getMetadonneDisponibleColonneCorbeille();
    }

    @Override
    protected void fireEvent(
        final CoreSession session,
        final DocumentModel ancienDossier,
        final Entry<String, Object> entry,
        final Object nouveauDocumentValue,
        final String ancienDossierValueLabel
    ) {
        final String entryKey = entry.getKey();
        final String propertyName = entryKey.substring(
            SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_PREFIX.length() + 1
        );
        // journalisation de l'action dans les logs
        fireEvent(session, propertyName, nouveauDocumentValue, ancienDossierValueLabel);
    }

    private static void fireEvent(
        final CoreSession session,
        final String propertyName,
        final Object nouveauDocumentValue,
        final String ancienDossierValueLabel
    ) {
        // journalisation de l'action dans les logs
        final JournalService journalService = STServiceLocator.getJournalService();
        // récupération du label de l'élément modifié pour le commentaire
        final String bordereauLabel = INIT_ENTRY_LABEL_LIST.get(propertyName);
        String comment =
            bordereauLabel +
            " : '" +
            (nouveauDocumentValue != null ? nouveauDocumentValue : "") +
            "' remplace '" +
            (ancienDossierValueLabel != null ? ancienDossierValueLabel : "") +
            "'";

        // envoie l'événement
        journalService.journaliserActionAdministration(
            session,
            SolonEpgEventConstant.PARAM_APPLI_UPDATE_EVENT,
            comment
        );
    }

    @Override
    protected Map<String, Object> getMap(final DocumentModel dossierDocument) {
        // on récupère toutes les propriétés liées au paramètrage de l'application
        return dossierDocument.getProperties(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA);
    }

    @Override
    protected Set<String> getUnLoggableEntry() {
        return UNLOGGABLE_ENTRY_LIST;
    }

    @Override
    public ParametrageApplication saveParametrageApplication(
        final CoreSession session,
        final ParametrageApplication parametrageApplication
    ) {
        final DocumentModel doc = session.saveDocument(parametrageApplication.getDocument());
        session.save();
        return doc.getAdapter(ParametrageApplication.class);
    }
}
