package fr.dila.solonepg.ui.services.actions.impl;

import static fr.dila.st.core.util.ResourceHelper.getString;

import fr.dila.solonepg.api.administration.IndexationMotCle;
import fr.dila.solonepg.api.administration.IndexationRubrique;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgIndextionConstants;
import fr.dila.solonepg.api.exception.IndexationException;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.IndexationEpgService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgIndexationMotCleForm;
import fr.dila.solonepg.ui.bean.EpgIndexationMotCleListDTO;
import fr.dila.solonepg.ui.bean.EpgIndexationRubriqueDTO;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.actions.EpgGestionIndexationActionService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.operation.utils.UpdateListOperation;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

public class EpgGestionIndexationActionServiceImpl implements EpgGestionIndexationActionService {
    private static final String ITEM = "item";
    private static final String TOUS = "Tous";

    private static final STLogger LOG = STLogFactory.getLog(EpgGestionIndexationActionServiceImpl.class);

    private static final String ADD_RUBRIQUE_ERROR = "admin.indexation.action.add.rubrique.error";

    public Boolean isUpdater(SpecificContext context) {
        return context
            .getSession()
            .getPrincipal()
            .isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_INDEXATION_UPDATER);
    }

    @Override
    public void addRubrique(SpecificContext context) {
        CoreSession session = context.getSession();
        String indexationRubrique = context.getFromContextData(EpgContextDataKey.INDEXATION_RUBRIQUE);
        IndexationEpgService indexationEpgService = SolonEpgServiceLocator.getIndexationEpgService();
        for (String rubrique : indexationRubrique.split(UpdateListOperation.LIST_SEPARATOR)) {
            if (StringUtils.isNotBlank(rubrique)) {
                try {
                    indexationEpgService.createIndexationRubrique(session, rubrique);
                } catch (IndexationException e) {
                    // catch des messages du services en cas d'erreur car problème de renseignement utilisateur
                    LOG.error(EpgLogEnumImpl.ERROR_ADD_INDEXATION_TEC, e, getString(ADD_RUBRIQUE_ERROR, rubrique));
                    context.getMessageQueue().addErrorToQueue(getString(ADD_RUBRIQUE_ERROR, rubrique));
                }
            }
        }
        context.getMessageQueue().addToastSuccess(getString("admin.indexation.action.add.rubrique.success"));
    }

    @Override
    public EpgIndexationMotCleListDTO getMotCleList(SpecificContext context) {
        DocumentModel motCleListDoc = context.getCurrentDocument();
        EpgIndexationMotCleListDTO liste = MapDoc2Bean.docToBean(motCleListDoc, EpgIndexationMotCleListDTO.class);

        Map<String, String> mapMinistere = liste
            .getMinistereIds()
            .stream()
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    id ->
                        STServiceLocator
                            .getOrganigrammeService()
                            .getOrganigrammeNodeById(id, OrganigrammeType.MINISTERE)
                            .getLabel()
                )
            );

        liste.setMapMinistere(mapMinistere);
        liste.setAction(context.getAction(EpgActionEnum.ADD_MOTS_CLES));
        return liste;
    }

    @Override
    public void addMotCle(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel currentIndexationMotCleDoc = context.getCurrentDocument();
        String indexationMotCle = context.getFromContextData(EpgContextDataKey.INDEXATION_MOT_CLE_LIST);
        if (currentIndexationMotCleDoc != null) {
            if (StringUtils.isEmpty(indexationMotCle)) {
                context.getMessageQueue().addErrorToQueue(getString("admin.indexation.mot.cle.empty"));
            } else {
                IndexationMotCle indexation = currentIndexationMotCleDoc.getAdapter(IndexationMotCle.class);
                List<String> listMotCle = indexation.getMotsCles();
                boolean update = false;
                for (String motCle : indexationMotCle.split(UpdateListOperation.LIST_SEPARATOR)) {
                    if (StringUtils.isNotBlank(motCle)) {
                        if (!listMotCle.contains(motCle)) {
                            listMotCle.add(motCle);
                            update = true;
                        } else {
                            context.getMessageQueue().addErrorToQueue(getString("admin.indexation.mot.cle.present"));
                        }
                    }
                }
                if (update) {
                    indexation.setMotsCles(listMotCle);
                    IndexationEpgService indexationEpgService = SolonEpgServiceLocator.getIndexationEpgService();
                    indexationEpgService.updateIndexationMotCle(session, indexation);
                    context.getMessageQueue().addToastSuccess(getString("admin.indexation.action.add.mot.cle.success"));
                }
            }
        }
    }

    @Override
    public void addMotCleList(SpecificContext context) {
        CoreSession session = context.getSession();
        EpgIndexationMotCleForm form = context.getFromContextData(EpgContextDataKey.INDEXATION_MOT_CLE_FORM);
        IndexationEpgService indexationEpgService = SolonEpgServiceLocator.getIndexationEpgService();

        if (StringUtils.isEmpty(form.getMinistereId())) {
            context.getMessageQueue().addErrorToQueue(getString("admin.indexation.ministere.empty"));
        } else {
            List<String> idMinisteres = new ArrayList<>();
            if (TOUS.equals(form.getMinistereId())) {
                // L'utilisateur a selectionné "Tous les ministères" qui renvoie "tous" en non un identifiant de node
                STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
                for (OrganigrammeNode node : ministeresService.getCurrentMinisteres()) {
                    idMinisteres.add(node.getId());
                }
            } else {
                idMinisteres.add(form.getMinistereId());
            }

            try {
                indexationEpgService.createIndexationMotCle(session, form.getLabel(), idMinisteres);
                context
                    .getMessageQueue()
                    .addToastSuccess(getString("admin.indexation.action.add.list.mot.cle.success"));
            } catch (IndexationException e) {
                LOG.error(EpgLogEnumImpl.ERROR_ADD_INDEXATION_TEC, e);
                context.getMessageQueue().addErrorToQueue(getString("admin.indexation.action.add.list.mot.cle.error"));
            }
        }
    }

    @Override
    public void deleteIndexation(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel indexationDoc = context.getCurrentDocument();
        if (indexationDoc != null) {
            if (indexationDoc.hasSchema(SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_SCHEMA)) {
                SolonEpgServiceLocator
                    .getIndexationEpgService()
                    .deleteIndexationRubrique(session, indexationDoc.getAdapter(IndexationRubrique.class));
                context.getMessageQueue().addToastSuccess(getString("admin.indexation.action.delete.rubrique.success"));
            } else if (indexationDoc.hasSchema(SolonEpgIndextionConstants.INDEXATION_MOT_CLE_SCHEMA)) {
                SolonEpgServiceLocator
                    .getIndexationEpgService()
                    .deleteIndexationMotCle(session, indexationDoc.getAdapter(IndexationMotCle.class));
                context
                    .getMessageQueue()
                    .addToastSuccess(getString("admin.indexation.action.delete.mot.cle.list.success"));
            }
        }
    }

    @Override
    public List<EpgIndexationMotCleListDTO> getListIndexationMotCle(SpecificContext context) {
        DocumentModelList docListMotCle = SolonEpgServiceLocator
            .getIndexationEpgService()
            .findAllIndexationMotCle(context.getSession());
        return docListMotCle
            .stream()
            .map(
                doc -> {
                    EpgIndexationMotCleListDTO motCle = MapDoc2Bean.docToBean(doc, EpgIndexationMotCleListDTO.class);
                    context.putInContextData(ITEM, motCle);
                    motCle.setGeneralesActions(
                        context.getActions(EpgActionCategory.PARAMETRES_INDEXATION_MOTS_CLES_LIST)
                    );
                    motCle.setAction(context.getAction(EpgActionEnum.REMOVE_MOTS_CLES));
                    return motCle;
                }
            )
            .collect(Collectors.toList());
    }

    @Override
    public List<EpgIndexationRubriqueDTO> getListIndexationRubrique(SpecificContext context) {
        DocumentModelList docListRubrique = SolonEpgServiceLocator
            .getIndexationEpgService()
            .findAllIndexationRubrique(context.getSession());
        return docListRubrique
            .stream()
            .map(
                doc -> {
                    EpgIndexationRubriqueDTO rubrique = MapDoc2Bean.docToBean(doc, EpgIndexationRubriqueDTO.class);
                    context.putInContextData(ITEM, rubrique);
                    rubrique.setAction(context.getAction(EpgActionEnum.REMOVE_RUBRIQUE));
                    return rubrique;
                }
            )
            .collect(Collectors.toList());
    }

    @Override
    public void removeMotCle(SpecificContext context) {
        DocumentModel currentIndexationMotCle = context.getCurrentDocument();
        String motCle = context.getFromContextData(EpgContextDataKey.INDEXATION_MOT_CLE);
        if (currentIndexationMotCle != null && motCle != null) {
            IndexationMotCle indexation = currentIndexationMotCle.getAdapter(IndexationMotCle.class);
            if (indexation != null) {
                List<String> listMotCle = indexation.getMotsCles();
                if (listMotCle.contains(motCle)) {
                    listMotCle.remove(motCle);
                    indexation.setMotsCles(listMotCle);
                    SolonEpgServiceLocator
                        .getIndexationEpgService()
                        .updateIndexationMotCle(context.getSession(), indexation);
                }
            }
            context.getMessageQueue().addToastSuccess(getString("admin.indexation.action.delete.mot.cle.success"));
        }
    }

    @Override
    public void changeMotCleList(SpecificContext context) {
        DocumentModel currentIndexationMotCle = context.getCurrentDocument();
        String idMinistere = context.getFromContextData(STContextDataKey.MINISTERE_ID);
        try {
            if (idMinistere == null) {
                context.getMessageQueue().addErrorToQueue(getString("indexation.ministere.empty"));
            } else {
                IndexationMotCle indexationMC = currentIndexationMotCle.getAdapter(IndexationMotCle.class);
                if (indexationMC != null) {
                    List<String> idMinisteres = new ArrayList<>();
                    String[] title = indexationMC.getIntitule().split(" - ");
                    if (TOUS.equals(idMinistere)) {
                        // L'utilisateur a selectionné "Tous les ministères" qui renvoi "Tous" en non un identifiant de node
                        STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
                        for (OrganigrammeNode node : ministeresService.getCurrentMinisteres()) {
                            idMinisteres.add(node.getId());
                        }
                        indexationMC.setIntitule(TOUS + " - " + title[1]);
                    } else {
                        idMinisteres.add(idMinistere);
                        EntiteNode node = STServiceLocator.getSTMinisteresService().getEntiteNode(idMinistere);
                        indexationMC.setIntitule(node.getNorMinistere() + " - " + title[1]);
                    }

                    indexationMC.setMinistereIds(idMinisteres);
                    SolonEpgServiceLocator
                        .getIndexationEpgService()
                        .updateIndexationMotCleAfterChange(context.getSession(), indexationMC);
                    context
                        .getMessageQueue()
                        .addToastSuccess("admin.indexation.action.reattribution.list.mot.cle.success");
                }
            }
        } catch (IndexationException e) {
            // catch des messages du services en cas d'erreur car problème de renseignement utilisateur
            LOG.error(EpgLogEnumImpl.ERROR_ADD_INDEXATION_TEC, e);
            context.getMessageQueue().addErrorToQueue(getString(ADD_RUBRIQUE_ERROR));
        }
    }
}
