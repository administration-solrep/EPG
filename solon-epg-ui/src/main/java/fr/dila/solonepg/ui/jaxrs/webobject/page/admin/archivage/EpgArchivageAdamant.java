package fr.dila.solonepg.ui.jaxrs.webobject.page.admin.archivage;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.th.bean.ArchivageAdamantForm;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliAdamant")
public class EpgArchivageAdamant extends SolonWebObject {

    @GET
    public ThTemplate getAdamant() {
        verifyAction(EpgActionEnum.ADMIN_MENU_ARCHIVAGE_ADAMANT, EpgURLConstants.URL_ARCHIVAGE_ADAMANT);

        template.setName("pages/admin/archivage/adamant");
        context.setNavigationContextTitle(
            new Breadcrumb(
                ResourceHelper.getString("archivage.adamant.title"),
                EpgURLConstants.URL_ARCHIVAGE_ADAMANT,
                Breadcrumb.TITLE_ORDER
            )
        );
        template.setContext(context);

        ArchivageAdamantForm archivageAdamant = new ArchivageAdamantForm();
        archivageAdamant.setDossierExtraction(getNbDossierEnCoursExtraction(context.getSession()));

        // Map pour mon contenu spécifique
        Map<String, Object> map = new HashMap<>();
        map.put(STTemplateConstants.RESULT_FORM, archivageAdamant);
        map.put(STTemplateConstants.EDIT_ACTIONS, context.getActions(EpgActionCategory.ARCHIVAGE_ADAMANT));
        map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        map.put("statuts", getStatuts());
        template.setData(map);

        return template;
    }

    private int getNbDossierEnCoursExtraction(CoreSession session) {
        return SolonEpgServiceLocator.getArchivageAdamantService().getNbDosExtracting(session);
    }

    private List<SelectValueDTO> getStatuts() {
        List<SelectValueDTO> status = new ArrayList<>();
        status.add(new SelectValueDTO(VocabularyConstants.STATUT_CLOS, "Clos"));
        status.add(new SelectValueDTO(VocabularyConstants.STATUT_PUBLIE, "Publié"));
        status.add(new SelectValueDTO(VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION, "Terminé sans publication"));

        return status;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgAdminTemplate();
    }
}
