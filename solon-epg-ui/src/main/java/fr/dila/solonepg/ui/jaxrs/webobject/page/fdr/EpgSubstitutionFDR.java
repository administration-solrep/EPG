package fr.dila.solonepg.ui.jaxrs.webobject.page.fdr;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.ui.services.EpgModeleFdrListUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonepg.ui.th.model.EpgLayoutThTemplate;
import fr.dila.solonepg.ui.th.model.bean.EpgModeleFdrForm;
import fr.dila.ss.api.criteria.SubstitutionCriteria;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.enums.SSActionEnum;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.enums.SSUserSessionKey;
import fr.dila.ss.ui.jaxrs.webobject.page.dossier.SSSubstitutionFDR;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Collections;
import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "SubstitutionFDR")
public class EpgSubstitutionFDR extends SSSubstitutionFDR {

    public EpgSubstitutionFDR() {
        super();
    }

    @GET
    @Path("/liste")
    public ThTemplate getListModeleSubstitution() throws IllegalAccessException, InstantiationException {
        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        SubstitutionCriteria criteria = (SubstitutionCriteria) Optional
            .ofNullable(UserSessionHelper.getUserSessionParameter(context, SSUserSessionKey.SUBSTITUTION_CRITERIA))
            .orElse(
                new SubstitutionCriteria(Collections.singletonList(dossier.getMinistereResp()), dossier.getTypeActe())
            );

        context.putInContextData(
            SSContextDataKey.LIST_MODELE_FDR,
            UserSessionHelper.getUserSessionParameter(context, SSUserSessionKey.MODELE_FDR_LIST_FORM)
        );
        context.putInContextData(SSContextDataKey.SUBSTITUTION_CRITERIA, criteria);

        // Récupération de la liste des modèles disponnible pour la substitution
        EpgModeleFdrListUIService modeleFDRListUIService = EpgUIServiceLocator.getEpgModeleFdrListUIService();
        ModeleFDRList lstResults = modeleFDRListUIService.getModelesFDRSubstitution(context);

        ThTemplate template = buildTemplateListModeleSubstitution(lstResults, dossier.getNumeroNor());
        template.getData().put(STTemplateConstants.HAS_FILTER, true);
        template.getData().put(STTemplateConstants.CRITERIA, criteria);
        return template;
    }

    @GET
    @Path("/consult")
    public ThTemplate getModeleSubstitutionConsult(@QueryParam("idModele") String idModele)
        throws IllegalAccessException, InstantiationException {
        verifyAction(SSActionEnum.DOSSIER_SUBSTITUER_FDR_VALIDER, "/consult");

        DocumentModel dossierDoc = context.getCurrentDocument();
        context.setCurrentDocument(idModele);
        EpgModeleFdrForm modeleForm = new EpgModeleFdrForm();
        SolonEpgUIServiceLocator.getEpgModeleFdrFicheUIService().consultModeleSubstitution(context, modeleForm);

        return buildTemplateSubstitutionConsult(dossierDoc, modeleForm);
    }

    @GET
    @Path("/valider")
    public Response validerSubstitutionModele(@QueryParam("idModele") String idModele) {
        context.putInContextData(SSContextDataKey.ID_MODELE, idModele);

        SolonEpgActionServiceLocator.getEpgDossierDistributionActionService().substituerRoute(context);

        UserSessionHelper.putUserSessionParameter(context, SSUserSessionKey.MODELE_FDR_LIST_FORM, null);
        UserSessionHelper.putUserSessionParameter(context, SSUserSessionKey.SUBSTITUTION_CRITERIA, null);
        UserSessionHelper.putUserSessionParameter(context, SpecificContext.MESSAGE_QUEUE, context.getMessageQueue());
        return redirect(getValiderSubstitutionUrl(context.getNavigationContext()));
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgLayoutThTemplate();
    }
}
