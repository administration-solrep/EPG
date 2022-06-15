package fr.dila.solonepg.ui.services.actions.impl;

import fr.dila.cm.core.dao.FeuilleRouteDao;
import fr.dila.solonepg.api.criteria.EpgFeuilleRouteCriteria;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.core.dao.EpgFeuilleRouteDao;
import fr.dila.solonepg.ui.bean.fdr.EpgFeuilleRouteDTO;
import fr.dila.solonepg.ui.bean.fdr.EpgModeleFDRList;
import fr.dila.solonepg.ui.services.actions.EpgRechercheModeleFeuilleRouteActionService;
import fr.dila.ss.ui.bean.fdr.FeuilleRouteDTO;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.actions.impl.SSRechercheModeleFeuilleRouteActionServiceImpl;
import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.ss.ui.th.bean.RechercheModeleFdrForm;
import fr.dila.st.core.helper.PaginationHelper;
import fr.dila.st.ui.contentview.UfnxqlPageDocumentProvider;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class EpgRechercheModeleFeuilleRouteActionServiceImpl
    extends SSRechercheModeleFeuilleRouteActionServiceImpl
    implements EpgRechercheModeleFeuilleRouteActionService {

    @Override
    protected String getSearchQueryString(SpecificContext context) {
        EpgFeuilleRouteCriteria criteria = getFeuilleRouteCriteria(context);
        FeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(context.getSession(), criteria);

        return feuilleRouteDao.getQueryString();
    }

    /**
     * Retourne les critères de recherche des modèles de feuille de route sous
     * forme textuelle.
     *
     * @return Critères de recherche des modèles de feuille de route sous forme
     *         textuelle
     */
    @Override
    protected List<Object> getSearchQueryParameter(SpecificContext context) {
        EpgFeuilleRouteCriteria criteria = getFeuilleRouteCriteria(context);
        FeuilleRouteDao feuilleRouteDao = new EpgFeuilleRouteDao(context.getSession(), criteria);

        return feuilleRouteDao.getParamList();
    }

    @Override
    protected EpgFeuilleRouteCriteria getFeuilleRouteCriteria(SpecificContext context) {
        RechercheModeleFdrForm form = context.getFromContextData(SSContextDataKey.SEARCH_MODELEFDR_FORM);
        EpgFeuilleRouteCriteria criteria = new EpgFeuilleRouteCriteria();
        context.putInContextData(SSContextDataKey.FEUILLE_ROUTE_CRITERIA, criteria);

        criteria = (EpgFeuilleRouteCriteria) super.getFeuilleRouteCriteria(context);

        criteria.setNumero(form.getNumero());
        criteria.setTypeActe(form.getTypeActe());
        criteria.setDirection(form.getDirection());

        return criteria;
    }

    @Override
    public ModeleFDRList getModeles(SpecificContext context) {
        ModeleFDRListForm listForm = context.getFromContextData(SSContextDataKey.LIST_MODELE_FDR);

        UfnxqlPageDocumentProvider provider = getProvider(context, listForm);
        List<DocumentModel> docs = provider.getCurrentPage();

        EpgModeleFDRList listResult = new EpgModeleFDRList();
        listResult.setListe(
            docs
                .stream()
                .map(doc -> this.getFeuilleRouteDTOFromDoc(doc, context.getSession()))
                .collect(Collectors.toList())
        );
        listResult.setNbTotal((int) provider.getResultsCount());
        listResult.buildColonnes(listForm);
        listResult.setHasSelect(true);
        listResult.setHasPagination(true);

        //Récupérer les actions des modèles
        getActionFDRList(context, listResult);

        listForm.setPage(PaginationHelper.getPageFromCurrentIndex(provider.getCurrentPageIndex()));

        return listResult;
    }

    @Override
    public EpgFeuilleRouteDTO getFeuilleRouteDTOFromDoc(DocumentModel doc, CoreSession session) {
        FeuilleRouteDTO dto = super.getFeuilleRouteDTOFromDoc(doc, session);

        SolonEpgFeuilleRoute fdr = doc.getAdapter(SolonEpgFeuilleRoute.class);

        EpgFeuilleRouteDTO epgDto = new EpgFeuilleRouteDTO(dto);
        epgDto.setNumero(fdr.getNumero());
        return epgDto;
    }
}
