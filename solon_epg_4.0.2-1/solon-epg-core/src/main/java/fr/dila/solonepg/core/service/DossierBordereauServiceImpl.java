package fr.dila.solonepg.core.service;

import static java.util.Arrays.asList;

import fr.dila.solonepg.api.administration.BulletinOfficiel;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.ActeVisibilityConstants;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.constant.STWebserviceConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.JetonService;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.LogDocumentUpdateServiceImpl;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.core.util.UnrestrictedCreateOrSaveDocumentRunner;
import fr.sword.xsd.solon.epg.TypeModification;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

/**
 * Implémentation du service de distribution des dossiers de SOLON EPG.
 *
 * @author asatre
 */
public class DossierBordereauServiceImpl extends LogDocumentUpdateServiceImpl implements DossierBordereauService {
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -8746593455899581354L;

    private static final Set<String> UNLOGGABLE_ENTRY_LIST = Collections.unmodifiableSet(initUnloggableEntryList());

    private static final STLogger LOGGER = STLogFactory.getLog(DossierBordereauServiceImpl.class);

    private Pattern formatReferenceLoiPattern;

    private Pattern formatNumeroOrdrePattern;

    private Pattern formatReferenceOrdonnancePattern;

    /**
     * Liste des valeurs xpath des champs qui font parti du dossier mais pas du bordereau.
     *
     * @return
     */
    private static Set<String> initUnloggableEntryList() {
        Set<String> unloggableEntryList = new HashSet<>();
        unloggableEntryList.add("dos:isUrgent");
        unloggableEntryList.add("dos:statutArchivage");
        unloggableEntryList.add("dos:publicationEpreuveEnRetour");
        unloggableEntryList.add("dos:publicationRapportPresentation");
        unloggableEntryList.add("dos:texteAPublier");
        unloggableEntryList.add("dos:isParapheurTypeActeUpdated");
        unloggableEntryList.add("dos:nbDossierRectifie");
        unloggableEntryList.add("dos:mesureNominative");
        unloggableEntryList.add("dos:isParapheurFichierInfoNonRecupere");
        unloggableEntryList.add("dos:decretNumerote");
        return unloggableEntryList;
    }

    private static final List<String> DATE_SLASH_PROPERTIES_XPATH = Collections.unmodifiableList(
        asList(ConseilEtatConstants.CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_XPATH)
    );

    @Override
    protected Map<String, Object> getMap(DocumentModel dossierDocument) {
        // on récupère toutes les propriétés liées au bordereau et au conseil d'Etat
        Map<String, Object> map = dossierDocument.getProperties(DossierSolonEpgConstants.DOSSIER_SCHEMA);
        map.putAll(dossierDocument.getProperties(ConseilEtatConstants.CONSEIL_ETAT_SCHEMA));
        map.putAll(dossierDocument.getProperties(RetourDilaConstants.RETOUR_DILA_SCHEMA));
        return map;
    }

    @Override
    protected void fireEvent(
        CoreSession session,
        DocumentModel dossierDocModel,
        Entry<String, Object> entry,
        Object newPropValue,
        String prevPropValue
    ) {
        String entryKey = entry.getKey();

        journaliserActionBordereau(session, dossierDocModel, newPropValue, prevPropValue, entryKey);

        if (
            ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_XPATH.equals(entryKey) &&
            isDossierInAvisCEStep(session, dossierDocModel)
        ) {
            // Création d'un nouveau jeton car on est dans étape pour Avis CE
            createPriorisationJeton(session, dossierDocModel, newPropValue);
        }
    }

    private void createPriorisationJeton(CoreSession session, DocumentModel documentModel, Object prioriteValue) {
        final JetonService jetonService = STServiceLocator.getJetonService();
        Dossier dossier = documentModel.getAdapter(Dossier.class);
        if (dossier != null) {
            String priorite = prioriteValue != null ? prioriteValue.toString() : "0";
            jetonService.addDocumentInBasket(
                session,
                STWebserviceConstant.CHERCHER_MODIFICATION_DOSSIER,
                TableReference.MINISTERE_CE,
                documentModel,
                dossier.getNumeroNor(),
                TypeModification.PRIORISATION.name(),
                Collections.singletonList(priorite)
            );
        }
    }

    private boolean isDossierInAvisCEStep(CoreSession session, DocumentModel dossierDocModel) {
        final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
        boolean existsAvisCEStep = false;
        try {
            existsAvisCEStep = corbeilleService.existsPourAvisCEStep(session, dossierDocModel.getId());
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, dossierDocModel, e);
        }
        return existsAvisCEStep;
    }

    private void journaliserActionBordereau(
        CoreSession session,
        DocumentModel dossierDocModel,
        Object newPropValue,
        String oldPropValue,
        String entryKey
    ) {
        // récupération du nom de la métadonnée modifiée
        int labelCodeIndex = entryKey.indexOf(":");
        String labelCode = entryKey.substring(labelCodeIndex + 1);
        SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
        String bordereauLabel = vocService.getLabelFromId(
            STVocabularyConstants.BORDEREAU_LABEL,
            labelCode,
            STVocabularyConstants.COLUMN_LABEL
        );
        // récupération de la valeur des enumération grâce au vocabulaire
        String ancienDossierValueLabel = getVocabularyValue(session, entryKey, oldPropValue, vocService);
        String nouveauDossierValueLabel = getVocabularyValue(session, entryKey, newPropValue, vocService);
        String comment =
            StringUtils.defaultString(bordereauLabel, labelCode) +
            " : '" +
            StringUtils.defaultString(nouveauDossierValueLabel) +
            "' remplace '" +
            StringUtils.defaultString(ancienDossierValueLabel) +
            "'";
        STServiceLocator
            .getJournalService()
            .journaliserActionBordereau(session, dossierDocModel, STEventConstant.BORDEREAU_UPDATE, comment);
    }

    /**
     * Récupére le label associé à l'id du vocabulaire ou des autres références
     */
    private String getVocabularyValue(
        final CoreSession session,
        String entryKey,
        Object id,
        SolonEpgVocabularyService vocService
    ) {
        String idStr = null;
        if (id != null) {
            idStr = id.toString();
        }
        String value = idStr;
        if (StringUtils.isNotEmpty(idStr)) {
            if (DossierSolonEpgConstants.DOSSIER_PUBLICATION_INTEGRALE_OU_EXTRAIT_XPATH.equals(entryKey)) {
                value =
                    vocService.getLabelFromId(
                        VocabularyConstants.TYPE_PUBLICATION,
                        idStr,
                        STVocabularyConstants.COLUMN_LABEL
                    );
            } else if (DossierSolonEpgConstants.DOSSIER_DELAI_PUBLICATION_XPATH.equals(entryKey)) {
                value =
                    vocService.getLabelFromId(
                        VocabularyConstants.DELAI_PUBLICATION,
                        idStr,
                        STVocabularyConstants.COLUMN_LABEL
                    );
            } else if (DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_XPATH.equals(entryKey)) {
                value =
                    vocService.getLabelFromId(
                        VocabularyConstants.TYPE_ACTE_VOCABULARY,
                        idStr,
                        STVocabularyConstants.COLUMN_LABEL
                    );
            } else if (DossierSolonEpgConstants.DOSSIER_CATEGORIE_ACTE_XPATH.equals(entryKey)) {
                value =
                    vocService.getLabelFromId(VocabularyConstants.NATURE, idStr, STVocabularyConstants.COLUMN_LABEL);
            } else if (ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_XPATH.equals(entryKey)) {
                value =
                    vocService.getLabelFromId(
                        VocabularyConstants.VOC_PRIORITE,
                        idStr,
                        STVocabularyConstants.COLUMN_LABEL
                    );
            } else if (RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_XPATH.equals(entryKey)) {
                final IdRef docRef = new IdRef(idStr);
                try {
                    if (session.exists(docRef)) {
                        ModeParution mode = session.getDocument(docRef).getAdapter(ModeParution.class);
                        value = mode.getMode();
                    } else if (StringUtils.isNotEmpty(idStr)) {
                        value = "**Mode inconnu**";
                    }
                } catch (NuxeoException exc) {
                    LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, id, exc);
                }
            } else if (DossierSolonEpgConstants.DOSSIER_VECTEUR_PUBLICATION_XPATH.equals(entryKey)) {
                value = RegExUtils.removeAll(value, "\\[|\\]");
                String[] listIds = value.split(",");
                value =
                    Arrays
                        .stream(listIds)
                        .map(String::trim)
                        .map(elem -> getVecteurPublicationLabel(session, id, elem))
                        .collect(Collectors.joining(", ", "[", "]"));
            }
        }
        return value;
    }

    private String getVecteurPublicationLabel(CoreSession session, Object id, String elem) {
        String vecteurPublicationLabel = elem;

        try {
            IdRef docRef = new IdRef(elem);
            if (session.exists(docRef)) {
                DocumentModel doc = session.getDocument(docRef);
                if (doc.hasSchema(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA)) {
                    VecteurPublication vecteur = doc.getAdapter(VecteurPublication.class);
                    vecteurPublicationLabel = vecteur.getIntitule();
                } else if (doc.hasSchema(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA)) {
                    BulletinOfficiel bulletin = doc.getAdapter(BulletinOfficiel.class);
                    vecteurPublicationLabel = bulletin.getIntitule();
                }
            }
        } catch (NuxeoException exc) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, id, exc);
        }

        return vecteurPublicationLabel;
    }

    @Override
    public void publierDossierStub(DocumentModel dossier, CoreSession session) {
        Dossier dossierDoc = dossier.getAdapter(Dossier.class);
        // passe le dossier à l'état publié
        dossierDoc.setStatut(VocabularyConstants.STATUT_PUBLIE);
        /*
         * RetourDila retourDila = dossier.getAdapter(RetourDila.class); // met des valeurs par défault dans les
         * propriétés JORF retourDila.setDateParutionJorf(Calendar.getInstance());
         * retourDila.setNumeroTexteParutionJorf("2010-123455"); retourDila.setPageParutionJorf(123L);
         */
        dossierDoc.save(session);

        // Met à jour les droits d'indexation du dossier
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        dossierService.initDossierIndexationAclUnrestricted(session, Collections.singletonList(dossierDoc));
    }

    @Override
    protected Set<String> getUnLoggableEntry() {
        return UNLOGGABLE_ENTRY_LIST;
    }

    @Override
    public void terminerDossierSansPublication(DocumentModel dossierDoc, CoreSession session) {
        final JournalService journalService = STServiceLocator.getJournalService();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        SolonEpgServiceLocator.getDossierDistributionService().terminateFeuilleRoute(session, dossierDoc);
        // passe le dossier à l'état "terminé sans publication"
        dossier.setStatut(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION);
        session.saveDocument(dossierDoc);
        // évenement de passage à l'état lancé
        final EventProducer eventProducer = STServiceLocator.getEventProducer();
        final Map<String, Serializable> eventProperties = new HashMap<>();
        eventProperties.put(SolonEpgEventConstant.DOSSIER_EVENT_PARAM, dossier);
        final InlineEventContext inlineEventContext = new InlineEventContext(
            session,
            session.getPrincipal(),
            eventProperties
        );
        eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgEventConstant.DOSSIER_STATUT_CHANGED));

        // journalisation de l'action dans les logs du bordereau
        journalService.journaliserActionBordereau(
            session,
            dossierDoc,
            SolonEpgEventConstant.DOSSIER_STATUT_CHANGED,
            SolonEpgEventConstant.TERMINER_DOSSIER_SANS_PUBLICATION_COMMENT
        );
    }

    @Override
    public boolean isFieldReferenceLoiValid(CoreSession session, String value) {
        if (formatReferenceLoiPattern == null) {
            STParametreService paramService = STServiceLocator.getSTParametreService();
            String formatReferenceLoi = paramService.getParametreValue(
                session,
                SolonEpgParametreConstant.FORMAT_REFERENCE_LOI
            );
            formatReferenceLoiPattern = Pattern.compile(formatReferenceLoi, Pattern.CANON_EQ);
        }
        return formatReferenceLoiPattern.matcher(value).matches();
    }

    @Override
    public boolean isFieldNumeroOrdreValid(CoreSession session, String value) {
        if (formatNumeroOrdrePattern == null) {
            STParametreService paramService = STServiceLocator.getSTParametreService();
            String formatNumeroOrdre = paramService.getParametreValue(
                session,
                SolonEpgParametreConstant.FORMAT_NUMERO_ORDRE
            );
            formatNumeroOrdrePattern = Pattern.compile(formatNumeroOrdre, Pattern.CANON_EQ);
        }
        return formatNumeroOrdrePattern.matcher(value).matches();
    }

    @Override
    public boolean isFieldReferenceOrdonnanceValid(CoreSession session, String value) {
        if (formatReferenceOrdonnancePattern == null) {
            STParametreService paramService = STServiceLocator.getSTParametreService();
            String formatReferenceOrdonnance = paramService.getParametreValue(
                session,
                SolonEpgParametreConstant.FORMAT_REFERENCE_ORDONNANCE
            );
            formatReferenceOrdonnancePattern = Pattern.compile(formatReferenceOrdonnance, Pattern.CANON_EQ);
        }
        return formatReferenceOrdonnancePattern.matcher(value).matches();
    }

    @Override
    public void reset() {
        formatReferenceLoiPattern = null;
        formatNumeroOrdrePattern = null;
        formatReferenceOrdonnancePattern = null;
    }

    @Override
    public boolean isBordereauComplet(Dossier dossier) {
        final ActeService acteService = SolonEpgServiceLocator.getActeService();

        final String typeActeId = dossier.getTypeActe();
        final Map<String, Boolean> mapVisibility = acteService.getActeVisibility(typeActeId);

        boolean isComplet = true;

        // Mantis #38727
        if (isPropertyEmpty(dossier.getTitreActe())) {
            isComplet = false;
        }

        // Nom resp dossier
        if (isPropertyEmpty(dossier.getNomRespDossier())) {
            isComplet = false;
        }

        // Qualité resp dossier
        if (isPropertyEmpty(dossier.getQualiteRespDossier())) {
            isComplet = false;
        }

        if (mapVisibility.get(ActeVisibilityConstants.PUBLICATION)) {
            // Vecteur publication
            if (dossier.getVecteurPublication().isEmpty()) {
                isComplet = false;
            }

            // Publication à date précisée
            if ("1".equals(dossier.getDelaiPublication()) && dossier.getDatePreciseePublication() == null) {
                isComplet = false;
            }
        }

        // Indexation : Rubrique
        if (
            mapVisibility.get(ActeVisibilityConstants.INDEXATION) &&
            dossier.getTexteEntreprise() &&
            CollectionUtils.isEmpty(dossier.getIndexationRubrique())
        ) {
            isComplet = false;
        }

        // Raport Au Parlement
        String baseLegaleNatureTexte = dossier.getBaseLegaleNatureTexte();
        if (
            acteService.isRapportAuParlement(dossier.getTypeActe()) &&
            (baseLegaleNatureTexte == null || baseLegaleNatureTexte.isEmpty())
        ) {
            isComplet = false;
        }
        return isComplet;
    }

    private boolean isPropertyEmpty(final String property) {
        return StringUtils.isBlank(property);
    }

    @Override
    public String getBordereauMetadonnesObligatoiresManquantes(final DocumentModel dossierDoc) {
        final ActeService acteService = SolonEpgServiceLocator.getActeService();
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        final String typeActeId = dossier.getTypeActe();
        final Map<String, Boolean> mapVisibility = acteService.getActeVisibility(typeActeId);

        final List<String> bordereauMetadonnesObligatoiresManquantes = new ArrayList<>();
        final SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();

        // Mantis #38727
        if (isPropertyEmpty(dossier.getTitreActe())) {
            bordereauMetadonnesObligatoiresManquantes.add(
                vocService.getLabelFromId(
                    STVocabularyConstants.BORDEREAU_LABEL,
                    DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_PROPERTY,
                    STVocabularyConstants.COLUMN_LABEL
                )
            );
        }

        // Nom resp dossier
        if (isPropertyEmpty(dossier.getNomRespDossier())) {
            bordereauMetadonnesObligatoiresManquantes.add(
                vocService.getLabelFromId(
                    STVocabularyConstants.BORDEREAU_LABEL,
                    DossierSolonEpgConstants.DOSSIER_NOM_RESPONSABLE_PROPERTY,
                    STVocabularyConstants.COLUMN_LABEL
                )
            );
        }

        // Qualité resp dossier
        if (isPropertyEmpty(dossier.getQualiteRespDossier())) {
            bordereauMetadonnesObligatoiresManquantes.add(
                vocService.getLabelFromId(
                    STVocabularyConstants.BORDEREAU_LABEL,
                    DossierSolonEpgConstants.DOSSIER_QUALITE_RESPONSABLE_PROPERTY,
                    STVocabularyConstants.COLUMN_LABEL
                )
            );
        }

        if (mapVisibility.get(ActeVisibilityConstants.PUBLICATION)) {
            // Vecteur publication
            if (dossier.getVecteurPublication().size() < 1) {
                bordereauMetadonnesObligatoiresManquantes.add(
                    vocService.getLabelFromId(
                        STVocabularyConstants.BORDEREAU_LABEL,
                        DossierSolonEpgConstants.DOSSIER_VECTEUR_PUBLICATION_PROPERTY,
                        STVocabularyConstants.COLUMN_LABEL
                    )
                );
            }

            // Publication à date précisée
            if ("1".equals(dossier.getDelaiPublication()) && dossier.getDatePreciseePublication() == null) {
                bordereauMetadonnesObligatoiresManquantes.add(
                    vocService.getLabelFromId(
                        STVocabularyConstants.BORDEREAU_LABEL,
                        DossierSolonEpgConstants.DOSSIER_DATE_PRECISEE_PUBLICATION_PROPERTY,
                        STVocabularyConstants.COLUMN_LABEL
                    )
                );
            }
        }

        // FEV547 - Date d'effet si texte entreprise
        if (dossier.getTexteEntreprise() && dossier.getDateEffet().isEmpty()) {
            bordereauMetadonnesObligatoiresManquantes.add(
                vocService.getLabelFromId(
                    STVocabularyConstants.BORDEREAU_LABEL,
                    DossierSolonEpgConstants.DOSSIER_DATE_EFFET_PROPERTY,
                    STVocabularyConstants.COLUMN_LABEL
                )
            );
        }

        if (dossier.getTexteEntreprise() && CollectionUtils.isEmpty(dossier.getIndexationRubrique())) {
            bordereauMetadonnesObligatoiresManquantes.add(
                vocService.getLabelFromId(
                    STVocabularyConstants.BORDEREAU_LABEL,
                    DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY,
                    STVocabularyConstants.COLUMN_LABEL
                )
            );
        }

        return StringHelper.join(bordereauMetadonnesObligatoiresManquantes, ",", "\"");
    }

    @Override
    public boolean addPublicationsConjointes(
        final CoreSession session,
        final Dossier dossier,
        final List<String> norsAdded,
        final List<String> norsUnknown,
        final List<String> norsAlreadyPublished
    ) {
        final String dossierNor = dossier.getNumeroNor();

        final NORService norService = SolonEpgServiceLocator.getNORService();

        final List<String> norToCheckList = new ArrayList<>();
        norToCheckList.addAll(norsAdded);
        norToCheckList.addAll(dossier.getPublicationsConjointes());
        final Map<String, Dossier> allDossiersConjointsMap = new HashMap<>();
        allDossiersConjointsMap.put(dossierNor, dossier);
        do {
            final String norToCheck = norToCheckList.remove(0);
            Dossier dossierConjoint = allDossiersConjointsMap.get(norToCheck);
            if (dossierConjoint == null) {
                DocumentModel dossierConjointDoc = norService.getDossierFromNOR(session, norToCheck);
                if (dossierConjointDoc == null) {
                    norsUnknown.add(norToCheck);
                    continue;
                }
                dossierConjoint = dossierConjointDoc.getAdapter(Dossier.class);
                // Le dossier ne doit pas être à l'état publié
                if (dossierConjoint.isDone() || VocabularyConstants.STATUT_PUBLIE.equals(dossierConjoint.getStatut())) {
                    norsAlreadyPublished.add(norToCheck);
                    continue;
                }
                allDossiersConjointsMap.put(norToCheck, dossierConjoint);
                norToCheckList.addAll(dossierConjoint.getPublicationsConjointesUnrestricted(session));
            }
        } while (norToCheckList.size() > 0);

        // Récupération des données à propager
        // Délai de publication
        final String delaiPublication = dossier.getDelaiPublication();
        // Publication à date précisée
        final Calendar datePreciseePublication = dossier.getDatePreciseePublication();
        // Mode de parution
        final RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
        final String modeParution = retourDila.getModeParution();

        // Parcours de la liste de nors conjoints pour modifier chaque dossier
        final Set<String> norConjoints = new HashSet<>(allDossiersConjointsMap.keySet());
        for (Entry<String, Dossier> conjointEntry : allDossiersConjointsMap.entrySet()) {
            norConjoints.remove(conjointEntry.getKey());
            final List<String> norConjointsList = new ArrayList<>(norConjoints);
            norConjoints.add(conjointEntry.getKey());

            final Dossier dossierConjoint = conjointEntry.getValue();
            if (!dossierNor.equals(conjointEntry.getKey())) {
                // Propagation des métadonnées du dossier
                propagatePublicationData(dossierConjoint, delaiPublication, datePreciseePublication, modeParution);
            }
            dossierConjoint.setPublicationsConjointesUnrestricted(session, norConjointsList);
            new UnrestrictedCreateOrSaveDocumentRunner(session).saveDocument(dossierConjoint.getDocument());
        }

        return norsAlreadyPublished.isEmpty() && norsUnknown.isEmpty();
    }

    @Override
    public void removePublicationConjointe(CoreSession session, Dossier dossier, String norRemoved) {
        // Récupération du dossier conjoint
        final NORService norService = SolonEpgServiceLocator.getNORService();
        DocumentModel dossierConjointDoc = norService.getDossierFromNOR(
            session,
            norRemoved,
            RetourDilaConstants.RETOUR_DILA_SCHEMA
        );
        // Suppression dans le dossier courant et dans le dossier conjoint
        List<String> publications = dossier.getPublicationsConjointes();
        publications.remove(norRemoved);
        dossier.setPublicationsConjointes(publications);
        if (dossierConjointDoc != null) {
            final Dossier dossierConjoint = dossierConjointDoc.getAdapter(Dossier.class);
            // utilisation de methode unrestricted car l'utilisateur peut mettre un dossier comme conjoint sans avoir le
            // droit de le voir
            List<String> publicationsDossierASuppr = dossierConjoint.getPublicationsConjointesUnrestricted(session);
            publicationsDossierASuppr.remove(dossier.getNumeroNor());
            // Parcours des publications conjointes du dossier courant
            for (String pubNor : publications) {
                DocumentModel pubDossierDoc = norService.getDossierFromNOR(
                    session,
                    pubNor,
                    RetourDilaConstants.RETOUR_DILA_SCHEMA
                );
                Dossier pubDossierConjoint = pubDossierDoc.getAdapter(Dossier.class);
                if (pubDossierDoc != null) {
                    List<String> publicationsDossierPubConjointe = pubDossierConjoint.getPublicationsConjointesUnrestricted(
                        session
                    );
                    publicationsDossierPubConjointe.remove(norRemoved);
                    pubDossierConjoint.setPublicationsConjointesUnrestricted(session, publicationsDossierPubConjointe);
                    new UnrestrictedCreateOrSaveDocumentRunner(session).saveDocument(pubDossierDoc);
                }
                // On supprime également les publications conjointes du dossier conjoint
                if (publicationsDossierASuppr.contains(pubNor)) {
                    publicationsDossierASuppr.remove(pubNor);
                }
            }
            dossierConjoint.setPublicationsConjointesUnrestricted(session, publicationsDossierASuppr);
            new UnrestrictedCreateOrSaveDocumentRunner(session).saveDocument(dossierConjointDoc);
        }
    }

    @Override
    public void propagatePublicationData(
        Dossier dossierDest,
        String delaiPublication,
        Calendar datePreciseePublication,
        String modeParution
    ) {
        // Propagation des métadonnées du dossier
        // Délai de publication
        dossierDest.setDelaiPublication(delaiPublication);
        // Publication à date précisée
        dossierDest.setDatePreciseePublication(datePreciseePublication);
        // Mode de parution
        final RetourDila retourDilaDossierConjoint = dossierDest.getDocument().getAdapter(RetourDila.class);
        if (retourDilaDossierConjoint != null) {
            retourDilaDossierConjoint.setModeParution(modeParution);
        }
    }

    @Override
    protected List<String> getDateSlashPropertiesXpath() {
        return DATE_SLASH_PROPERTIES_XPATH;
    }
}
