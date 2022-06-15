package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant.ADMIN_FONCTIONNEL_TABLEAU_DYNAMIQUE_DESINATAIRE_VIEWER;
import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getTableauDynamiqueService;
import static fr.dila.solonepg.ui.enums.EpgActionCategory.SUIVI_TD_ACTIONS;
import static fr.dila.solonepg.ui.th.constants.EpgURLConstants.URL_PAGE_TD;
import static fr.dila.st.core.util.ObjectHelper.requireNonNullElseGet;
import static fr.dila.st.core.util.ResourceHelper.getString;
import static fr.dila.st.core.util.StreamUtils.throwingMerger;
import static fr.dila.st.ui.th.constants.STURLConstants.MAIN_CONTENT_ID;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import fr.dila.solonepg.api.enums.FavorisRechercheType;
import fr.dila.solonepg.api.recherche.TableauDynamique;
import fr.dila.solonepg.api.service.TableauDynamiqueService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.TableauxDynamiquesDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.FavorisRechercheUIType;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.TableauDynamiqueUIService;
import fr.dila.solonepg.ui.th.bean.TableauDynamiqueForm;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.core.query.QueryHelper;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public class TableauDynamiqueUIServiceImpl implements TableauDynamiqueUIService {

    @Override
    public TableauDynamiqueForm getTableauDynamiqueForm(SpecificContext context) {
        DocumentModel tabDoc = context.getCurrentDocument();
        TableauDynamiqueForm td = MapDoc2Bean.docToBean(tabDoc, TableauDynamiqueForm.class);
        List<String> destinatairesId = tabDoc.getAdapter(TableauDynamique.class).getDestinatairesId();
        OrganigrammeService organigrammeService = STServiceLocator.getOrganigrammeService();
        HashMap<String, String> collect = requireNonNullElseGet(destinatairesId, LinkedList<String>::new)
            .stream()
            .map(organigrammeService::getOrganigrammeNodeById)
            .collect(toMap(OrganigrammeNode::getId, OrganigrammeNode::getLabel, throwingMerger(), HashMap::new));
        td.setMapDestinataires(collect);
        return td;
    }

    @Override
    public void createOrSaveTableauDynamique(SpecificContext context) {
        TableauDynamiqueForm form = context.getFromContextData(EpgContextDataKey.TABLEAU_DYNAMIQUE_FORM);
        String uiType = context.getFromContextData(EpgContextDataKey.TD_TYPE_RECHERCHE);

        String query;
        TableauDynamiqueService tableauDynamiqueService = SolonEpgServiceLocator.getTableauDynamiqueService();
        Map<Integer, String> result;
        String id = form.getId();
        ArrayList<String> destinataires = form.getDestinataires();
        String libelle = form.getLibelle();
        CoreSession session = context.getSession();
        String msgKeyOk;
        String msgKeyKo;
        if (StringUtils.isBlank(id)) {
            FavorisRechercheType type = FavorisRechercheUIType.getTypeFromUIValue(uiType);
            context.putInContextData(EpgContextDataKey.TYPE_RECHERCHE, type);
            query = EpgUIServiceLocator.getEpgRechercheUIService().getJsonQuery(context);
            result = tableauDynamiqueService.createTableauDynamique(session, type, query, destinataires, libelle);
            msgKeyOk = "tableau.dynamique.ajout.ok";
            msgKeyKo = "tableau.dynamique.ajout.ko";
        } else {
            DocumentModel tableauDoc = QueryHelper.getDocument(session, id);
            query = tableauDoc.getAdapter(TableauDynamique.class).getQuery();
            result = tableauDynamiqueService.updateTableauDynamique(session, tableauDoc, query, destinataires, libelle);
            msgKeyOk = "tableau.dynamique.modif.ok";
            msgKeyKo = "tableau.dynamique.modif.ko";
        }

        String ok = result.get(0);
        if (!StringUtils.isEmpty(ok)) {
            context.getMessageQueue().addToastSuccess(getString(msgKeyOk));
        }
        String ko = result.get(1);
        if (!StringUtils.isEmpty(ko)) {
            context.getMessageQueue().addWarnToQueue(getString(msgKeyKo, ko));
        }
    }

    @Override
    public boolean isDirectionViewer(NuxeoPrincipal ssPrincipal) {
        return (
            ssPrincipal.isAdministrator() ||
            ssPrincipal.isMemberOf(ADMIN_FONCTIONNEL_TABLEAU_DYNAMIQUE_DESINATAIRE_VIEWER)
        );
    }

    @Override
    public void deleteTableauDynamique(SpecificContext context) {
        CoreSession session = context.getSession();
        final DocumentModel documentTableauDynamique = context.getCurrentDocument();
        getTableauDynamiqueService().deleteTableauDynamique(session, documentTableauDynamique);

        final String nomTableauDynamique = documentTableauDynamique.getTitle();
        final String utilisateurCourant = session.getPrincipal().getName();
        context
            .getMessageQueue()
            .addToastSuccess(getString("tableaux.dynamiques.remove.success", nomTableauDynamique, utilisateurCourant));
    }

    /**
     * @return true si l'utilisateur courant a un nombre de tableaux dynamiques
     *         inférieur au nombre maximal autorisé.
     */
    @Override
    public boolean canAddNewTableauDynamique(SpecificContext context) {
        CoreSession session = context.getSession();
        return getTableauDynamiqueService().userIsUnderQuota(session, session.getPrincipal().getName());
    }

    @Override
    public TableauxDynamiquesDTO getTableauxDynamiqueDTO(SpecificContext context) {
        TableauDynamique tabDyn = context.getCurrentDocument().getAdapter(TableauDynamique.class);
        context.putInContextData(EpgContextDataKey.ID_TABLEAU_DYNAMIQUE, tabDyn.getId());
        context.putInContextData(EpgContextDataKey.LABEL_TABLEAU_DYNAMIQUE, tabDyn.getTitle());
        TableauxDynamiquesDTO dto = new TableauxDynamiquesDTO();
        dto.setId(tabDyn.getId());
        dto.setLabel(tabDyn.getTitle());
        dto.setLink(String.format(URL_PAGE_TD + MAIN_CONTENT_ID, tabDyn.getId()));
        dto.setActions(context.getActions(SUIVI_TD_ACTIONS));
        return dto;
    }

    @Override
    public EpgDossierList getDossierListForTableauxDynamique(SpecificContext context) {
        DocumentModel tabDynDoc = context.getCurrentDocument();
        Objects.requireNonNull(tabDynDoc);
        TableauDynamique tabDyn = tabDynDoc.getAdapter(TableauDynamique.class);
        context.putInContextData(EpgContextDataKey.TYPE_RECHERCHE, tabDyn.getType());
        context.putInContextData(EpgContextDataKey.JSON_QUERY, tabDyn.getQuery());
        return EpgUIServiceLocator.getEpgRechercheUIService().getResultsForJsonQuery(context);
    }

    @Override
    public List<TableauxDynamiquesDTO> getListTableauxDynamiqueDTO(SpecificContext context) {
        return getTableauDynamiqueService()
            .findAll(context.getSession())
            .stream()
            .map(
                d -> {
                    context.setCurrentDocument(d);
                    return getTableauxDynamiqueDTO(context);
                }
            )
            .collect(toList());
    }
}
