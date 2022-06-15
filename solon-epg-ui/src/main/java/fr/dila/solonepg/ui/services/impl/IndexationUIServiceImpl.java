package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.IndexationEpgService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.IndexationUIService;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class IndexationUIServiceImpl implements IndexationUIService {

    @Override
    public void addIndexationToDossier(String currentIndexation, String indexationType, DocumentModel dossierDoc) {
        if (StringUtils.isEmpty(currentIndexation)) {
            return;
        }
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        List<String> indexations = getIndexations(indexationType, dossierDoc);
        if (!indexations.contains(currentIndexation)) {
            indexations.add(currentIndexation);
        }
        setIndexations(indexationType, dossier, indexations);
    }

    @Override
    public void removeIndexation(String indexationType, DocumentModel dossierDoc, String indexation) {
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        List<String> indexations = getIndexations(indexationType, dossierDoc);
        indexations.remove(indexation);
        setIndexations(indexationType, dossier, indexations);
    }

    @Override
    public List<String> getIndexations(String indexationType, DocumentModel dossierDoc) {
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY)) {
            return dossier.getIndexationRubrique();
        }
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_MOTS_CLES_PROPERTY)) {
            return dossier.getIndexationMotsCles();
        }
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_CHAMP_LIBRE_PROPERTY)) {
            return dossier.getIndexationChampLibre();
        }
        return Collections.emptyList();
    }

    private void setIndexations(String indexationType, Dossier dossier, List<String> indexations) {
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY)) {
            dossier.setIndexationRubrique(indexations);
        }
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_MOTS_CLES_PROPERTY)) {
            dossier.setIndexationMotsCles(indexations);
        }
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_CHAMP_LIBRE_PROPERTY)) {
            dossier.setIndexationChampLibre(indexations);
        }
    }

    @Override
    public List<String> findAllIndexation(String indexationType, CoreSession session, DocumentModel dossierDoc) {
        IndexationEpgService indexationEpgService = SolonEpgServiceLocator.getIndexationEpgService();
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY)) {
            Objects.requireNonNull(session);
            return indexationEpgService.findAllIndexationRubrique(session, "");
        }
        if (indexationType.equals(DossierSolonEpgConstants.DOSSIER_INDEXATION_MOTS_CLES_PROPERTY)) {
            Objects.requireNonNull(session);
            Objects.requireNonNull(dossierDoc);
            return indexationEpgService.findAllIndexationMotCleForDossier(session, dossierDoc, "");
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> findAllIndexationRubrique(CoreSession session) {
        return findAllIndexation(DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY, session, null);
    }

    @Override
    public List<SelectValueDTO> getAllIndexationRubriqueSelectValues(CoreSession session) {
        return findAllIndexationRubrique(session).stream().map(SelectValueDTO::new).collect(Collectors.toList());
    }
}
