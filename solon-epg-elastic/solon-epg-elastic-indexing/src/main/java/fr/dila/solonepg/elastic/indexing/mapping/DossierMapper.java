package fr.dila.solonepg.elastic.indexing.mapping;

import static java.util.stream.Collectors.toList;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.typescomplexe.DossierTransposition;
import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.cases.typescomplexe.TranspositionsContainer;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.vocabulary.PrioriteCEService;
import fr.dila.solonepg.api.service.vocabulary.TypePublicationService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.MappingException;
import fr.dila.solonepg.elastic.models.ElasticApplicationLoi;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticParutionBo;
import fr.dila.solonepg.elastic.models.ElasticStep;
import fr.dila.solonepg.elastic.models.ElasticTransposition;
import fr.dila.solonepg.elastic.models.ElasticTranspositionDirective;
import fr.dila.solonepg.elastic.models.ElasticTranspositionOrdonnance;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.service.SSFeuilleRouteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.runtime.model.DefaultComponent;

public class DossierMapper extends DefaultComponent implements IDossierMapper {
    private static final String UUID_PATTERN =
        "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}";

    @Override
    public ElasticDossier from(
        DocumentModel documentModel,
        CoreSession session,
        Map<String, String> vecteurPublications
    )
        throws MappingException {
        try {
            return doFrom(documentModel, session, vecteurPublications);
        } catch (NuxeoException e) {
            throw new MappingException(String.format("Erreur de mapping du dossier %s", documentModel.getId()), e);
        }
    }

    @SuppressWarnings("unchecked")
    private ElasticDossier doFrom(
        DocumentModel documentModel,
        CoreSession session,
        Map<String, String> vecteurPublications
    ) {
        ElasticDossier exportableDossier = new ElasticDossier();
        Dossier dossier = documentModel.getAdapter(Dossier.class);

        /* UID */
        exportableDossier.setUid(documentModel.getId()); // Identifiant du dossier

        /* consetat:... => Champs de la section CE */
        exportableDossier.setConsetatDateAgCe(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.CONSETAT_DATE_AG_CE))
        );
        exportableDossier.setConsetatDateSectionCe(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.CONSETAT_DATE_SECTION_CE))
        );
        exportableDossier.setConsetatDateTransmissionSectionCe(
            ElasticUtils.toDate(
                (Calendar) documentModel.getPropertyValue(ElasticDossier.CONSETAT_DATE_TRANSMISSION_SECTION_CE)
            )
        );
        exportableDossier.setConsetatNumeroISA(
            (String) documentModel.getPropertyValue(ElasticDossier.CONSETAT_NUMERO_ISA)
        );

        PrioriteCEService prioriteCEService = SolonEpgServiceLocator.getPrioriteCEService();
        String priorite = (String) documentModel.getPropertyValue(ElasticDossier.CONSETAT_PRIORITE);
        String prioriteLabel = StringUtils.isNotBlank(priorite)
            ? prioriteCEService.getEntry(priorite).map(ImmutablePair::getValue).orElse("")
            : "";
        exportableDossier.setConsetatPriorite(prioriteLabel);
        exportableDossier.setConsetatRapporteurCe(
            (String) documentModel.getPropertyValue(ElasticDossier.CONSETAT_RAPPORTEUR_CE)
        );
        exportableDossier.setConsetatReceptionAvisCE(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.CONSETAT_RECEPTION_AVIS_CE))
        );
        exportableDossier.setConsetatSectionCe(
            (String) documentModel.getPropertyValue(ElasticDossier.CONSETAT_SECTION_CE)
        );

        exportableDossier.setDcLastContributor(dossier.getLastContributor());
        exportableDossier.setDcLocked(LockUtils.isLocked(session, documentModel.getRef()));
        exportableDossier.setDcLockOwner(
            STActionsServiceLocator.getSTLockActionService().getLockOwnerName(documentModel, session)
        );

        exportableDossier.setDcTitle(documentModel.getTitle());

        exportableDossier.setDcModified(DateUtil.toDate(DublincoreSchemaUtils.getModifiedDate(documentModel)));

        /* dos:... */
        exportableDossier.setDosApplicationLoi(
            fromTranspositionsContainer(dossier.getApplicationLoi(), ElasticApplicationLoi::new)
        );

        exportableDossier.setDosArchive((Boolean) documentModel.getPropertyValue(ElasticDossier.DOS_ARCHIVE));
        exportableDossier.setDosArriveeSolonTS(
            (Boolean) documentModel.getPropertyValue(ElasticDossier.DOS_ARRIVEE_SOLON_TS)
        );
        exportableDossier.setDosBaseLegaleActe(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_BASE_LEGALE_ACTE)
        ); // Base légale
        exportableDossier.setDosBaseLegaleNumeroTexte(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_BASE_LEGALE_NUMERO_TEXTE)
        );

        VocabularyService vocabularyService = STServiceLocator.getVocabularyService();

        String categorieActe = dossier.getCategorieActe();
        if (categorieActe != null) {
            exportableDossier.setDosCategorieActe(
                vocabularyService.getEntryLabel(VocabularyConstants.CATEGORY_ACTE_CONVENTION_COLLECTIVE, categorieActe)
            );
        }

        exportableDossier.setDosCreated(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.DOS_CREATED))
        ); // Date de création
        exportableDossier.setDosDateEnvoiJoTS(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.DOS_DATE_ENVOI_JO_TS))
        );
        exportableDossier.setDosDatePreciseePublication(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.DOS_DATE_PRECISEE_PUBLICATION))
        ); // Date de publication souhaitée
        exportableDossier.setDosDatePublication(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.DOS_DATE_PUBLICATION))
        ); // Date de publication
        exportableDossier.setDosDateSignature(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.DOS_DATE_SIGNATURE))
        ); // Date de signature
        exportableDossier.setDosDateVersementTS(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.DOS_DATE_VERSEMENT_TS))
        );
        exportableDossier.setDosDecretNumerote(
            (Boolean) documentModel.getPropertyValue(ElasticDossier.DOS_DECRET_NUMEROTE)
        );

        String delaiPublication = dossier.getDelaiPublication();
        if (delaiPublication != null) {
            exportableDossier.setDosDelaiPublication(
                vocabularyService.getEntryLabel(VocabularyConstants.DELAI_PUBLICATION, delaiPublication)
            );
        }

        // Direction responsable
        String directionRespId = dossier.getDirectionResp();
        exportableDossier.setDosDirectionRespId(directionRespId);
        STUsAndDirectionService stUsAndDirectionService = STServiceLocator.getSTUsAndDirectionService();
        exportableDossier.setDosDirectionResp(
            getLabel(
                stUsAndDirectionService::getUniteStructurelleNode,
                UniteStructurelleNode::getLabel,
                directionRespId
            )
        );

        String directionAttacheId = dossier.getDirectionAttache();
        exportableDossier.setDosDirectionAttacheId(directionAttacheId);
        exportableDossier.setDosDirectionAttache(
            getLabel(
                stUsAndDirectionService::getUniteStructurelleNode,
                UniteStructurelleNode::getLabel,
                directionAttacheId
            )
        );

        // responsable
        exportableDossier.setDosEmetteur((String) documentModel.getPropertyValue(ElasticDossier.DOS_EMETTEUR));
        exportableDossier.setDosHabilitationCommentaire(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_HABILITATION_COMMENTAIRE)
        );
        exportableDossier.setDosHabilitationNumeroArticles(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_HABILITATION_NUMERO_ARTICLES)
        );
        exportableDossier.setDosHabilitationNumeroOrdre(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_HABILITATION_NUMERO_ORDRE)
        );
        exportableDossier.setDosHabilitationRefLoi(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_HABILITATION_REF_LOI)
        );
        exportableDossier.setDosIdCreateurDossier(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_ID_CREATEUR_DOSSIER)
        );
        exportableDossier.setDosIdDocumentParapheur(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_ID_DOCUMENT_PARAPHEUR)
        );
        exportableDossier.setDosIsUrgent(dossier.getIsUrgent());
        exportableDossier.setDosLibre((List<String>) documentModel.getPropertyValue(ElasticDossier.DOS_LIBRE)); // Champs
        // libres
        exportableDossier.setDosMailRespDossier(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_MAIL_RESP_DOSSIER)
        );
        exportableDossier.setDosMesureNominative(
            (Boolean) documentModel.getPropertyValue(ElasticDossier.DOS_MESURE_NOMINATIVE)
        );

        String ministereAttacheId = (String) documentModel.getPropertyValue(ElasticDossier.DOS_MINISTERE_ATTACHE);
        exportableDossier.setDosMinistereAttacheId(ministereAttacheId); // Ministère de rattachement
        STMinisteresService stMinisteresService = STServiceLocator.getSTMinisteresService();
        exportableDossier.setDosMinistereAttache(
            getLabel(stMinisteresService::getEntiteNode, EntiteNode::getLabel, ministereAttacheId)
        );

        // Ministère responsable
        String ministereRespId = dossier.getMinistereResp();
        exportableDossier.setDosMinistereRespId(ministereRespId);
        exportableDossier.setDosMinistereResp(
            getLabel(stMinisteresService::getEntiteNode, EntiteNode::getLabel, ministereRespId)
        );

        exportableDossier.setDosMotscles((List<String>) documentModel.getPropertyValue(ElasticDossier.DOS_MOTSCLES)); // Mots
        // clés
        exportableDossier.setDosNomCompletChargesMissions(
            (List<String>) documentModel.getPropertyValue(ElasticDossier.DOS_NOM_COMPLET_CHARGES_MISSIONS)
        ); // Chargés de mission
        exportableDossier.setDosNomCompletConseillersPms(
            (List<String>) documentModel.getPropertyValue(ElasticDossier.DOS_NOM_COMPLET_CONSEILLERS_PMS)
        ); // Conseillers PMs
        exportableDossier.setDosNomRespDossier(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_NOM_RESP_DOSSIER)
        );
        exportableDossier.setDosNumeroNor((String) documentModel.getPropertyValue(ElasticDossier.DOS_NUMERO_NOR)); // Numéro
        // NOR
        exportableDossier.setDosPourFournitureEpreuve(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.DOS_POUR_FOURNITURE_EPREUVE))
        );
        exportableDossier.setDosPrenomRespDossier(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_PRENOM_RESP_DOSSIER)
        );
        TypePublicationService typePublicationService = SolonEpgServiceLocator.getTypePublicationService();
        String typePublicationId = (String) documentModel.getPropertyValue(
            ElasticDossier.DOS_PUBLICATION_INTEGRALE_OU_EXTRAIT
        );
        String typePublicationLabel = StringUtils.isNotBlank(typePublicationId)
            ? typePublicationService.getEntry(typePublicationId).map(ImmutablePair::getValue).orElse("")
            : "";
        exportableDossier.setDosPublicationIntegraleOuExtrait(typePublicationLabel);
        exportableDossier.setDosPublicationRapportPresentation(
            (Boolean) documentModel.getPropertyValue(ElasticDossier.DOS_PUBLICATION_RAPPORT_PRESENTATION)
        );
        exportableDossier.setDosPublicationsConjointes(
            (List<String>) documentModel.getPropertyValue(ElasticDossier.DOS_PUBLICATIONS_CONJOINTES)
        );
        exportableDossier.setDosQualiteRespDossier(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_QUALITE_RESP_DOSSIER)
        );
        exportableDossier.setDosRubriques((List<String>) documentModel.getPropertyValue(ElasticDossier.DOS_RUBRIQUES)); // Rubriques

        String statut = dossier.getStatut();
        if (statut != null) {
            exportableDossier.setDosStatut(
                vocabularyService.getEntryLabel(VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME, statut)
            ); // Statut du dossier
        }

        String statutArchivage = dossier.getStatutArchivage();
        if (statutArchivage != null) {
            exportableDossier.setDosStatutArchivageId(statutArchivage);
            exportableDossier.setDosStatutArchivage(
                vocabularyService.getEntryLabel(VocabularyConstants.STATUT_ARCHIVAGE_FACET_VOCABULARY, statutArchivage)
            );
        }

        exportableDossier.setDosTelRespDossier(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_TEL_RESP_DOSSIER)
        );
        exportableDossier.setDosTitreActe((String) documentModel.getPropertyValue(ElasticDossier.DOS_TITRE_ACTE)); // Titre

        exportableDossier.setDosTranspositionDirective(
            fromTranspositionsContainer(dossier.getTranspositionDirective(), ElasticTranspositionDirective::new)
        );

        exportableDossier.setDosTranspositionOrdonnance(
            fromTranspositionsContainer(dossier.getTranspositionOrdonnance(), ElasticTranspositionOrdonnance::new)
        );

        String typeActe = dossier.getTypeActe();
        if (typeActe != null) {
            exportableDossier.setDosTypeActe(
                vocabularyService.getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY, typeActe)
            );
        }
        exportableDossier.setDosVecteurPublication(getVecteurPublications(vecteurPublications, dossier)); // Vecteur de publication

        exportableDossier.setDosZoneComSggDila(
            (String) documentModel.getPropertyValue(ElasticDossier.DOS_ZONE_COM_SGG_DILA)
        );

        exportableDossier.setDosTexteEntreprise(
            (Boolean) documentModel.getPropertyValue(ElasticDossier.DOS_TEXTE_ENTREPRISE)
        );
        exportableDossier.setDosDateEffet(
            ElasticUtils.toDates((List<Calendar>) documentModel.getPropertyValue(ElasticDossier.DOS_DATE_EFFET))
        );

        exportableDossier.setEcmCurrentLifeCycleState(documentModel.getCurrentLifeCycleState());

        exportableDossier.setRetdilaDateParutionJorf(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.RETDILA_DATE_PARUTION_JORF))
        );

        String modeParutionId = (String) documentModel.getPropertyValue(ElasticDossier.RETDILA_MODE_PARUTION);
        exportableDossier.setRetdilaModeParution(modeParutionId);

        exportableDossier.setRetdilaNumeroTexteParutionJorf(
            (String) documentModel.getPropertyValue(ElasticDossier.RETDILA_NUMERO_TEXTE_PARUTION_JORF)
        );
        exportableDossier.setRetdilaPageParutionJorf(
            (Long) documentModel.getPropertyValue(ElasticDossier.RETDILA_PAGE_PARUTION_JORF)
        );
        RetourDila retourDila = documentModel.getAdapter(RetourDila.class);
        List<ElasticParutionBo> elasticParutionsBo = retourDila
            .getParutionBo()
            .stream()
            .map(this::toParutionBo)
            .collect(Collectors.toList());
        exportableDossier.setRetdilaParutionBo(elasticParutionsBo);

        exportableDossier.setRetdilaTitreOfficiel(
            (String) documentModel.getPropertyValue(ElasticDossier.RETDILA_TITRE_OFFICIEL)
        );

        String feuilleRouteId = dossier.getLastDocumentRoute();
        if (StringUtils.isNotEmpty(feuilleRouteId)) {
            SSFeuilleRouteService ssFeuilleRouteService = SSServiceLocator.getSSFeuilleRouteService();
            List<DocumentModel> steps = ssFeuilleRouteService.findAllStepsIndexation(session, feuilleRouteId);
            List<ElasticStep> elasticEtapes = steps
                .stream()
                .map(doc -> doc.getAdapter(SSRouteStep.class))
                .map(e -> this.toEtape(e, session))
                .collect(Collectors.toList());
            exportableDossier.setSteps(elasticEtapes);
        }

        exportableDossier.setTpAmpliationDestinataireMails(
            (List<String>) documentModel.getPropertyValue(ElasticDossier.TP_AMPLIATION_DESTINATAIRE_MAILS)
        );
        exportableDossier.setTpCheminCroix((Boolean) documentModel.getPropertyValue(ElasticDossier.TP_CHEMIN_CROIX));
        exportableDossier.setTpCommentaireReference(
            (String) documentModel.getPropertyValue(ElasticDossier.TP_COMMENTAIRE_REFERENCE)
        );
        exportableDossier.setTpDateArrivePapier(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.TP_DATE_ARRIVE_PAPIER))
        );
        exportableDossier.setTpDateRetour(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.TP_DATE_RETOUR))
        );

        exportableDossier.setTpMotifRetour((String) documentModel.getPropertyValue(ElasticDossier.TP_MOTIF_RETOUR));
        exportableDossier.setTpNomsSignatairesCommunication(
            (String) documentModel.getPropertyValue(ElasticDossier.TP_NOMS_SIGNATAIRES_COMMUNICATION)
        );
        exportableDossier.setTpNomsSignatairesRetour(
            (String) documentModel.getPropertyValue(ElasticDossier.TP_NOMS_SIGNATAIRES_RETOUR)
        );
        exportableDossier.setTpPriorite((String) documentModel.getPropertyValue(ElasticDossier.TP_PRIORITE));
        exportableDossier.setTpPublicationDate(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.TP_PUBLICATION_DATE))
        );
        exportableDossier.setTpPublicationDateDemande(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.TP_PUBLICATION_DATE_DEMANDE))
        );
        exportableDossier.setTpPublicationDateEnvoiJo(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.TP_PUBLICATION_DATE_ENVOI_JO))
        );
        exportableDossier.setTpPublicationDelai(
            (String) documentModel.getPropertyValue(ElasticDossier.TP_PUBLICATION_DELAI)
        );
        exportableDossier.setTpPublicationEpreuveEnRetour(
            (Boolean) documentModel.getPropertyValue(ElasticDossier.TP_PUBLICATION_EPREUVE_EN_RETOUR)
        );
        exportableDossier.setTpPublicationModePublication(
            (String) documentModel.getPropertyValue(ElasticDossier.TP_PUBLICATION_MODE_PUBLICATION)
        );
        exportableDossier.setTpPublicationNomPublication(
            (String) documentModel.getPropertyValue(ElasticDossier.TP_PUBLICATION_NOM_PUBLICATION)
        );
        exportableDossier.setTpPublicationNumeroListe(
            (String) documentModel.getPropertyValue(ElasticDossier.TP_PUBLICATION_NUMERO_LISTE)
        );
        exportableDossier.setTpRetourA((String) documentModel.getPropertyValue(ElasticDossier.TP_RETOUR_A));
        exportableDossier.setTpRetourDuBonaTitrerLe(
            ElasticUtils.toDate((Calendar) documentModel.getPropertyValue(ElasticDossier.TP_RETOUR_DU_BONA_TITRER_LE))
        );
        exportableDossier.setTpSignataire((String) documentModel.getPropertyValue(ElasticDossier.TP_SIGNATAIRE));

        exportableDossier.setTpTexteAPublier(
            (Boolean) documentModel.getPropertyValue(ElasticDossier.TP_TEXTE_APUBLIER)
        );

        // droits
        exportableDossier.setPerms(getDossierPermsAsString(documentModel));

        return exportableDossier;
    }

    private <E> String getLabel(Function<String, E> entiteSupplier, Function<E, String> labelGetter, String id) {
        return StringUtils.isNotBlank(id)
            ? Optional.ofNullable(entiteSupplier.apply(id)).map(labelGetter).orElse(id)
            : "";
    }

    private List<String> getVecteurPublications(Map<String, String> mapVecteurPublications, Dossier dossier) {
        List<String> vecteurPublications = dossier.getVecteurPublication();
        vecteurPublications.replaceAll(elt -> this.getVecteurPublicationLabel(mapVecteurPublications, elt));
        return vecteurPublications;
    }

    private String getVecteurPublicationLabel(Map<String, String> vecteurPublications, String uuidOrLabel) {
        if (uuidOrLabel.matches(UUID_PATTERN)) {
            return vecteurPublications.get(uuidOrLabel);
        }

        return uuidOrLabel;
    }

    @SuppressWarnings("unchecked")
    private <T extends ElasticTransposition> List<T> fromTranspositionsContainer(
        TranspositionsContainer transpositionsContainer,
        Supplier<T> supplier
    ) {
        if (transpositionsContainer == null) {
            return Collections.emptyList();
        }

        List<DossierTransposition> transpositions = transpositionsContainer.getTranspositions();
        return (List<T>) transpositions
            .stream()
            .map(t -> supplier.get().mapTransposition(t))
            .collect(Collectors.toList());
    }

    private ElasticParutionBo toParutionBo(ParutionBo parutionBo) {
        ElasticParutionBo elasticParutionBo = new ElasticParutionBo();
        elasticParutionBo.setDateParutionBo(ElasticUtils.toDate(parutionBo.getDateParutionBo()));
        elasticParutionBo.setNumeroTexteParutionBo(parutionBo.getNumeroTexteParutionBo());

        return elasticParutionBo;
    }

    private ElasticStep toEtape(SSRouteStep step, CoreSession session) {
        ElasticStep elasticStep = new ElasticStep();
        elasticStep.setEcmCurrentLifeCycleState(step.getCurrentLifeCycleState());
        elasticStep.setAutomaticValidated(step.isAutomaticValidated());
        elasticStep.setAutomaticValidation(step.isAutomaticValidation());
        elasticStep.setDateDebutEtape(ElasticUtils.toDate(step.getDateDebutEtape()));
        elasticStep.setDateFinEtape(ElasticUtils.toDate(step.getDateFinEtape()));
        elasticStep.setDistributionmailboxId(step.getDistributionMailboxId());
        elasticStep.setDueDate(ElasticUtils.toDate(step.getDueDate()));
        elasticStep.setMinistereID(step.getMinistereId());
        elasticStep.setObligatoireMinistere(step.isObligatoireMinistere());
        elasticStep.setObligatoireSgg(step.isObligatoireSGG());
        elasticStep.setType(step.getType());
        elasticStep.setValidationStatus(step.getValidationStatus());
        elasticStep.setValidationUserId(step.getValidationUserId());

        String findCommentsText = String.format(
            "SELECT " +
            SolonEpgSchemaConstant.COMMENT_SCHEMA_PREFIX +
            ":" +
            SolonEpgSchemaConstant.COMMENT_TEXT_PROPERTY +
            " FROM " +
            STConstant.COMMENT_DOCUMENT_TYPE +
            " WHERE ecm:parentId = '%s' ",
            step.getDocument().getId()
        );

        try (IterableQueryResult res = session.queryAndFetch(findCommentsText, NXQL.NXQL)) {
            elasticStep.setComments(
                StreamSupport
                    .stream(res.spliterator(), false)
                    .map(m -> (String) m.get("comment:text"))
                    .collect(Collectors.toList())
            );
        }

        return elasticStep;
    }

    private Collection<String> getDossierPermsAsString(DocumentModel dossier) {
        return Arrays.stream(dossier.getACP().getACLs()).flatMap(List::stream).map(ACE::getUsername).collect(toList());
    }
}
