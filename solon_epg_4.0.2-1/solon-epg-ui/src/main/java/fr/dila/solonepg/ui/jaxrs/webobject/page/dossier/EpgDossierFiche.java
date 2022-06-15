package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.ui.bean.MgppDossierCommunicationConsultationFiche;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.MgppTemplateConstants;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliDossierFiche")
public class EpgDossierFiche extends SolonWebObject {

    public EpgDossierFiche() {
        super();
    }

    @GET
    public ThTemplate getFiche() {
        if (template.getData() == null) {
            Map<String, Object> map = new HashMap<>();
            template.setData(map);
        }

        // Le current document peut être une fiche ou un dossier
        boolean isFicheVerrouillee = false;
        if (context.getCurrentDocument() != null) {
            isFicheVerrouillee =
                LockUtils.isLockedByCurrentUser(context.getSession(), context.getCurrentDocument().getRef());
        }

        context.putInContextData(MgppContextDataKey.IS_FICHE_VERROUILLEE, isFicheVerrouillee);
        // Setter la fiche comme current document
        Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
        FichePresentationDR fichePresentation = SolonMgppServiceLocator
            .getDossierService()
            .findOrCreateFicheDR(context.getSession(), dossier.getNumeroNor());
        context.setCurrentDocument(fichePresentation.getDocument());

        MgppDossierCommunicationConsultationFiche fiche = MgppUIServiceLocator
            .getMgppFicheUIService()
            .getFicheRemplie(context);

        if (isFicheVerrouillee) {
            List<SelectValueDTO> courrierSelect = MgppUIServiceLocator
                .getModeleCourrierUIService()
                .getAvailableModels(context);
            template.getData().put("courrierSelect", courrierSelect);
            template
                .getData()
                .put(
                    MgppTemplateConstants.MGPP_GENERALE_ACTIONS,
                    context.getActions(MgppActionCategory.COMMUNICATION_FICHE_EDIT_ACTION)
                );
        } else {
            template
                .getData()
                .put(
                    MgppTemplateConstants.MGPP_GENERALE_ACTIONS,
                    context.getActions(MgppActionCategory.COMMUNICATION_FICHE_ACTION)
                );
        }
        template
            .getData()
            .put(
                MgppTemplateConstants.MGPP_PRINT_ACTIONS,
                context.getActions(MgppActionCategory.COMMUNICATION_FICHE_PRINT_ACTION)
            );

        template.getData().put(MgppTemplateConstants.ID_FICHE, context.getCurrentDocument().getId());
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_PRESENTATION, fiche.getLstWidgetsPresentation());
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_FICHE, fiche.getLstWidgetsFiche());
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_DEPOT, fiche.getLstWidgetsDepot());
        template.getData().put(MgppTemplateConstants.LST_NAVETTES, fiche.getLstNavettes());
        template.getData().put(MgppTemplateConstants.LST_WIDGETS_LOI_VOTEE, fiche.getLstWidgetsLoiVotee());
        template.getData().put(MgppTemplateConstants.LST_TABLES_REPRESENTANTS, fiche.getLstTablesRepresentants());
        template.getData().put(MgppTemplateConstants.IS_DIFFUSION_BUTTON_VISIBLE, false);
        template.getData().put(MgppTemplateConstants.CAN_GENERER_COURRIER, true);

        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/mgpp/dossier/onglets/fichePresentation", context);
    }
}
