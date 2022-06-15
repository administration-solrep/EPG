package fr.dila.solonepg.ui.jaxrs.webobject.page.travail;

import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.travail.DossierCreationForm;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgDossierCreationUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.ProfilParametersForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.constants.STURLConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliTravailDossier")
public class EpgTravailDossier extends SolonWebObject {

    public EpgTravailDossier() {
        super();
    }

    @GET
    @Path("creation")
    public ThTemplate getDossierCreation() {
        context.setNavigationContextTitle(
            new Breadcrumb(
                "Créer un dossier",
                EpgURLConstants.URL_DOSSIER_CREATION + STURLConstants.MAIN_CONTENT_ID,
                Breadcrumb.TITLE_ORDER,
                context.getWebcontext().getRequest()
            )
        );
        template.setName("pages/travail/creation-dossier");
        template.setContext(context);
        template.setData(buildDossierCreationMap(context));

        return template;
    }

    public static Map<String, Object> buildDossierCreationMap(SpecificContext context) {
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());

        EpgDossierCreationUIService epgDossierCreationUIService = EpgUIServiceLocator.getEpgDossierCreationUIService();
        ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
        DocumentModel profilUser = profilUtilisateurService
            .getProfilUtilisateurForCurrentUser(context.getSession())
            .getDocument();
        ProfilParametersForm dto = MapDoc2Bean.docToBean(profilUser, ProfilParametersForm.class);
        map.put(EpgTemplateConstants.NOR_PRM_LIST, epgDossierCreationUIService.getNorPrmList(context));
        map.put(EpgTemplateConstants.POSTES_LIST, epgDossierCreationUIService.getUserPostes(context));
        String posteDefaut = dto.getDefaultPoste();
        map.put(EpgTemplateConstants.POSTE_SELECTED, posteDefaut);
        context.putInContextData(EpgContextDataKey.POSTE_DEFAUT, posteDefaut);

        DossierCreationForm dossier = new DossierCreationForm();
        dossier.setEntite(epgDossierCreationUIService.getDefaultMinistere(context));
        if (StringUtils.isNotEmpty(dossier.getEntite())) {
            dossier.setMapEntite(
                Collections.singletonMap(
                    dossier.getEntite(),
                    STServiceLocator
                        .getOrganigrammeService()
                        .getOrganigrammeNodeById(dossier.getEntite(), OrganigrammeType.MINISTERE)
                        .getLabel()
                )
            );
        }
        dossier.setDirection(epgDossierCreationUIService.getDefaultDirection(context));
        if (StringUtils.isNotEmpty(dossier.getDirection())) {
            dossier.setMapDirection(
                Collections.singletonMap(
                    dossier.getDirection(),
                    STServiceLocator
                        .getOrganigrammeService()
                        .getOrganigrammeNodeById(dossier.getDirection(), OrganigrammeType.DIRECTION)
                        .getLabel()
                )
            );
        }

        map.put(EpgTemplateConstants.DTO, dossier);
        return map;
    }
}
