package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier;

import static fr.dila.solonepg.ui.enums.EpgActionCategory.TRAITEMENT_PAPIER_SOUS_ONGLET;

import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Map;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliDossierTraitement")
public class EpgDossierTraitementPapier extends SolonWebObject {
    private static final String TRAITEMENT_PAPIER_WEBOBJECT = "AppliDossierTraitementPapier";
    private static final String DEFAULT_SOUS_ONGLET = "references";

    public EpgDossierTraitementPapier() {
        super();
    }

    @Path("papier")
    public Object getHome() {
        Map<String, Object> map = template.getData();

        String tab = DEFAULT_SOUS_ONGLET;

        EpgUIServiceLocator.getEpgDossierUIService().setActionsInContext(context);
        map.put(
            EpgTemplateConstants.SUBTABS,
            OngletConteneur.actionsToTabs(context, TRAITEMENT_PAPIER_SOUS_ONGLET, tab)
        );

        return newObject(TRAITEMENT_PAPIER_WEBOBJECT + StringUtils.capitalize(tab), context, template);
    }

    /**
     * Charger un sous-onglet
     *
     * @param tab
     * @return
     */
    @Path("papier/{tab}")
    public Object getSousOnglet(@PathParam("tab") String tab) {
        Map<String, Object> map = template.getData();

        map.put(
            EpgTemplateConstants.SUBTABS,
            OngletConteneur.actionsToTabs(context, TRAITEMENT_PAPIER_SOUS_ONGLET, tab)
        );

        // on envoie le context avec le template (lorsqu'on rafraichit la page par exemple)
        return newObject(TRAITEMENT_PAPIER_WEBOBJECT + StringUtils.capitalize(tab), context, template);
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/dossier/onglets/traitementPapier", context);
    }
}
