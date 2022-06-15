package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.directives;

import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.core.dto.activitenormative.TranspositionDirectiveDTOImpl;
import fr.dila.solonepg.ui.enums.pan.PanActionEnum;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanAbstractTextemaitre;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.bean.pan.TranspositionDirectiveForm;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Map;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanDirectivesTextemaitre")
public class PanDirectivesTextemaitre extends PanAbstractTextemaitre {

    public PanDirectivesTextemaitre() {
        super();
    }

    @Override
    public void loadSpecificTexteMaitreContent() {
        context.putInContextData(PanContextDataKey.FIRST_TABLE_TYPE, "texte");

        TranspositionDirective transpositionDirective = context
            .getCurrentDocument()
            .getAdapter(TranspositionDirective.class);
        TranspositionDirectiveDTOImpl texteMaitreDTO = new TranspositionDirectiveDTOImpl(transpositionDirective);
        Map<String, Object> templateData = template.getData();
        templateData.put(PanTemplateConstants.TEXTE_MAITRE, texteMaitreDTO);

        templateData.put(
            PanTemplateConstants.FIRST_TABLE_LIST,
            PanUIServiceLocator.getTranspositionDirectiveUIService().getTextesTransposition(context)
        );

        templateData.put("etatsDirectivesList", EpgUIServiceLocator.getEpgSelectValueUIService().getEtatsDirectives());

        templateData.put(PanTemplateConstants.NOR_TYPE, "tous");
        templateData.put(
            PanTemplateConstants.AJOUTER_PREMIER_TABLE_NOR_ACTION,
            context.getAction(PanActionEnum.ADD_ELT_PREMIER_TABLEAU_NOR)
        );
        templateData.put(
            PanTemplateConstants.TYPE_ACTES_LIST,
            EpgUIServiceLocator.getEpgSelectValueUIService().getTypeActe()
        );
    }

    @Override
    public String getSecondTableauAjaxWebObject() {
        return "";
    }

    @Path("sauvegarder")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveTexteMaitre(@SwBeanParam TranspositionDirectiveForm texteMaitreFormDTO) {
        context.putInContextData(PanContextDataKey.TEXTE_MAITRE_FORM, texteMaitreFormDTO);
        return sauvegarderTexteMaitre();
    }

    @Override
    protected void saveTexteMaitre(SpecificContext context) {
        PanUIServiceLocator.getTranspositionDirectiveUIService().saveTransposition(context);
    }

    @Override
    protected AbstractMapDTO getTexteMaitreDto(SpecificContext context) {
        TranspositionDirective transpositionDirective = context
            .getCurrentDocument()
            .getAdapter(TranspositionDirective.class);
        return new TranspositionDirectiveDTOImpl(transpositionDirective);
    }
}
