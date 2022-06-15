package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.feuilleroute.SqueletteStepTypeDestinataire;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.EpgSelectValueUIService;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.ui.services.impl.SSSelectValueUIServiceImpl;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;

public class EpgSelectValueUIServiceImpl extends SSSelectValueUIServiceImpl implements EpgSelectValueUIService {

    @Override
    public List<SelectValueDTO> getTypeActe() {
        return getSelectValues(SolonEpgServiceLocator.getTypeActeService(), Object::toString)
            .stream()
            .sorted(Comparator.comparing(SelectValueDTO::getLabel))
            .collect(Collectors.toList());
    }

    @Override
    public List<SelectValueDTO> getUnaryTypeActe() {
        return getUnarySelectValues(SolonEpgServiceLocator.getTypeActeService());
    }

    @Override
    public List<SelectValueDTO> getCategorieActe() {
        return getSelectValues(SolonEpgServiceLocator.getCategorieActeService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getBordereauLabel() {
        return getSelectValues(SolonEpgServiceLocator.getBordereauLabelService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getUnaryCategorieActe() {
        return getUnarySelectValues(SolonEpgServiceLocator.getCategorieActeService());
    }

    @Override
    public List<SelectValueDTO> getPrioriteCE() {
        return getSelectValues(SolonEpgServiceLocator.getPrioriteCEService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getDelaiPublication() {
        List<SelectValueDTO> delaiPublication = getSelectValues(
            SolonEpgServiceLocator.getDelaiPublicationService(),
            Object::toString
        );

        delaiPublication
            .stream()
            .filter(e -> VocabularyConstants.DELAI_PUBLICATION_AUCUN.equals(e.getId()))
            .findFirst()
            .ifPresent(
                item -> {
                    delaiPublication.remove(item);
                    delaiPublication.add(0, item);
                }
            );

        return delaiPublication;
    }

    @Override
    public List<SelectValueDTO> getDelaiPublicationFiltered() {
        return getDelaiPublication()
            .stream()
            .filter(v -> !VocabularyConstants.DELAI_PUBLICATION_SOUS_EMBARGO.equals(v.getKey()))
            .collect(Collectors.toList());
    }

    @Override
    public List<SelectValueDTO> getUnaryDelaiPublication() {
        return getUnarySelectValues(SolonEpgServiceLocator.getDelaiPublicationService());
    }

    @Override
    public List<SelectValueDTO> getTypeMesures() {
        return getSelectValues(SolonEpgServiceLocator.getTypeMesureService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getStatutDossier() {
        return getSelectValuesFromVocabulary(VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME);
    }

    @Override
    public List<SelectValueDTO> getStatutArchivageForStatistiques() {
        int statutCandidatDefinitif = Integer.parseInt(VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE);
        return getSelectValuesFromVocabulary(VocabularyConstants.VOCABULARY_STATUS_ARCHIVAGE)
            .stream()
            .filter(select -> Integer.parseInt(select.getId()) > statutCandidatDefinitif)
            .sorted(Comparator.comparing(SelectValueDTO::getId))
            .collect(Collectors.toList());
    }

    @Override
    public List<SelectValueDTO> getUnaryStatutDossier() {
        return getUnarySelectValuesFromVocabulary(VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME);
    }

    @Override
    public List<SelectValueDTO> getStatutValidation() {
        return getSelectValuesFromVocabulary(VocabularyConstants.STATUT_VALIDATION_DIRECTORY_NAME);
    }

    @Override
    public List<SelectValueDTO> getRoutingTaskTypesFiltered() {
        return getSelectValues(SolonEpgServiceLocator.getEpgRoutingTaskTypeService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getRoutingTaskTypesFilteredByIdFdr(CoreSession session, String idFdr) {
        List<SelectValueDTO> selectValueDTOList;
        String typeActe;
        List<String> attachedDocumentIds = session
            .getDocument(new IdRef(idFdr))
            .getAdapter(SSFeuilleRoute.class)
            .getAttachedDocuments();
        /*
         *  Si attached document non vide => La feuille de route est rattaché a un dossier donc je l'utilise pour récupérer le type d'acte.
         *
         *  si attached document est vide => La feuille de route n'est pas rattaché a un dossier,
         *  je récupère le type d'acte directement sur le fdr (modèle ou squelette)
         */
        if (CollectionUtils.isNotEmpty(attachedDocumentIds)) {
            Dossier dossier = session.getDocument(new IdRef(attachedDocumentIds.get(0))).getAdapter(Dossier.class);
            typeActe = dossier.getTypeActe();
        } else {
            SolonEpgFeuilleRoute feuilleRoute = session
                .getDocument(new IdRef(idFdr))
                .getAdapter(SolonEpgFeuilleRoute.class);
            typeActe = feuilleRoute.getTypeActe();
        }

        selectValueDTOList =
            getSelectValues(
                SolonEpgServiceLocator
                    .getEpgRoutingTaskTypeService()
                    .getFilteredEntries(session.getPrincipal(), typeActe),
                pair -> String.valueOf(pair.getKey()),
                ImmutablePair::getValue
            );

        return selectValueDTOList;
    }

    @Override
    public List<SelectValueDTO> getResponsableAmendement() {
        return getSelectValues(SolonEpgServiceLocator.getResponsableAmendementService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getPoleChargeMission() {
        return getSelectValues(SolonEpgServiceLocator.getPoleChargeMissionService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getNatureTexte() {
        return getSelectValues(SolonEpgServiceLocator.getNatureTexteService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getProcedureVote() {
        return getSelectValues(SolonEpgServiceLocator.getProcedureVoteService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getNatureTexteBaseLegale() {
        return getSelectValues(SolonEpgServiceLocator.getNatureTexteBaseLegaleService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getTypePublication() {
        return getSelectValues(SolonEpgServiceLocator.getTypePublicationService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getStatutArchivageFacet() {
        List<SelectValueDTO> values = getUnarySelectValues(SolonEpgServiceLocator.getStatutArchivageFacetService());
        // remove duplicates
        return new ArrayList<>(new HashSet<>(values));
    }

    @Override
    public List<SelectValueDTO> getVecteurPublication() {
        WebContext activeContext = WebEngine.getActiveContext();
        if (activeContext == null || activeContext.getCoreSession() == null) {
            return Collections.emptyList();
        }

        BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        Map<String, String> vecteurs = bulletinOfficielService.getAllVecteurPublicationIdIntitulePair(
            activeContext.getCoreSession()
        );
        return getSelectValues(new ArrayList<>(vecteurs.entrySet()), Map.Entry::getKey, Map.Entry::getValue);
    }

    @Override
    public List<SelectValueDTO> getActifVecteurPublication() {
        WebContext activeContext = WebEngine.getActiveContext();
        if (activeContext == null || activeContext.getCoreSession() == null) {
            return Collections.emptyList();
        }

        BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        Map<String, String> vecteurs = bulletinOfficielService.getAllActifVecteurPublicationIdIntitulePair(
            activeContext.getCoreSession()
        );
        return getSelectValues(new ArrayList<>(vecteurs.entrySet()), Map.Entry::getKey, Map.Entry::getValue);
    }

    @Override
    public List<SelectValueDTO> getUnaryVecteurPublication() {
        WebContext activeContext = WebEngine.getActiveContext();
        if (activeContext == null || activeContext.getCoreSession() == null) {
            return Collections.emptyList();
        }

        BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        Map<String, String> vecteurs = bulletinOfficielService.getAllVecteurPublicationIdIntitulePair(
            activeContext.getCoreSession()
        );
        return getUnarySelectValues(new ArrayList<>(vecteurs.entrySet()), Map.Entry::getValue);
    }

    @Override
    public List<SelectValueDTO> getVecteurPublicationTS() {
        return getSelectValuesFromVocabulary(VocabularyConstants.VECTEUR_PUBLICATION_TS);
    }

    @Override
    public List<SelectValueDTO> getModeParution() {
        return getModeParution((tableReferenceService, session) -> tableReferenceService.getModesParutionList(session));
    }

    @Override
    public List<SelectValueDTO> getActifModeParution() {
        return getModeParution(
            (tableReferenceService, session) -> tableReferenceService.getModesParutionActifsList(session)
        );
    }

    private List<SelectValueDTO> getModeParution(
        BiFunction<TableReferenceService, CoreSession, List<DocumentModel>> supplierModes
    ) {
        WebContext activeContext = WebEngine.getActiveContext();
        if (activeContext == null || activeContext.getCoreSession() == null) {
            return Collections.emptyList();
        }

        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        List<DocumentModel> docs = supplierModes.apply(tableReferenceService, activeContext.getCoreSession());
        List<ModeParution> modes = docs
            .stream()
            .map(d -> d.getAdapter(ModeParution.class))
            .collect(Collectors.toList());

        return getSelectValues(modes, ModeParution::getId, ModeParution::getMode);
    }

    @Override
    public List<SelectValueDTO> getEtatsDirectives() {
        return getSelectValues(SolonEpgServiceLocator.getDirectiveEtatService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getTypeHabilitation() {
        return getSelectValuesFromVocabulary(VocabularyConstants.VOC_TYPE_HABILITATION);
    }

    @Override
    public List<SelectValueDTO> getTraitementPapierTypeAvis() {
        return getSelectValuesFromVocabulary(
            VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_DIRECTORY,
            VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_SCHEMA
        );
    }

    @Override
    public List<SelectValueDTO> getTypeSignataireTraitementPapier() {
        return getSelectValues(SolonEpgServiceLocator.getTypeSignataireTraitementPapierService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getPeriodiciteRapport() {
        return getSelectValues(SolonEpgServiceLocator.getPeriodiciteRapportService(), Object::toString);
    }

    @Override
    public List<SelectValueDTO> getSqueletteTypeDestinataire() {
        return Stream
            .of(SqueletteStepTypeDestinataire.values())
            .map(type -> new SelectValueDTO(type.name(), ResourceHelper.getString(type.getValue())))
            .collect(Collectors.toList());
    }

    @Override
    public List<SelectValueDTO> getDispositionHabilitation() {
        return getSelectValues(SolonEpgServiceLocator.getDispositionHabilitationService(), Object::toString);
    }
}
