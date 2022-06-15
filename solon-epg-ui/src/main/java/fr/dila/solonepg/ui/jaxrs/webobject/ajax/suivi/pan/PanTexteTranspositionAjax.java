package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.api.cases.TexteTransposition;
import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.api.dto.TexteTranspositionDTO;
import fr.dila.solonepg.core.dto.activitenormative.AbstractMapTexteMaitreTableDTO;
import fr.dila.solonepg.core.dto.activitenormative.TexteTranspositionDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.bean.pan.TexteTranspositionForm;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanTexteTranspositionAjax")
public class PanTexteTranspositionAjax extends PanAbstractTexteMaitrePremierTableauAjax {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("ajouter")
    public Response ajouterElement(
        @SwRequired @FormParam(ID_CONTEXTUEL) String idContextuel,
        @SwRequired @FormParam(ID_NOR) String nor
    ) {
        template.setName(getEditElementPremierTableauTemplate());
        context.setCurrentDocument(idContextuel);
        initFrontComponents();
        template.getData().put(EpgTemplateConstants.ITEM, newElementDTO());
        template.getData().put(PanTemplateConstants.ID_CONTEXTUEL, idContextuel);
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ID, idContextuel);
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ELT_NOR, nor);

        template
            .getData()
            .put(PanTemplateConstants.CURRENT_SECTION, context.getFromContextData(PanContextDataKey.CURRENT_SECTION));

        putTexteTranspositionDTOListInContext();
        PanUIServiceLocator.getTranspositionDirectiveUIService().addNewText(context);
        PanUIServiceLocator.getTranspositionDirectiveUIService().saveTexteTransposition(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @Override
    public void chargerSpecificContent(String idPremierTableau) {
        TexteTranspositionDTO texteDto = (TexteTranspositionDTO) getElementFromId(idPremierTableau);
        template.getData().put(EpgTemplateConstants.ITEM, texteDto);

        if (StringUtils.isNotBlank(texteDto.getMinisterePilote())) {
            Map<String, String> mapMinisterePilote = Collections.singletonMap(
                texteDto.getMinisterePilote(),
                STServiceLocator
                    .getOrganigrammeService()
                    .getOrganigrammeNodeById(texteDto.getMinisterePilote(), OrganigrammeType.MINISTERE)
                    .getLabel()
            );
            template.getData().put("mapMinisterePilote", mapMinisterePilote);
        }
    }

    @Override
    protected void initFrontComponents() {
        template
            .getData()
            .put(PanTemplateConstants.TYPE_ACTES_LIST, EpgUIServiceLocator.getEpgSelectValueUIService().getTypeActe());
    }

    @POST
    @Path("editer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editerTexteTransposition(
        @FormParam(ID_CONTEXTUEL) String idContextuel,
        @FormParam(ID_PREMIER_TABLEAU) String idPremierTableau,
        @SwBeanParam TexteTranspositionForm texteTranspositionform
    )
        throws Exception {
        context.setCurrentDocument(idContextuel);
        putTexteTranspositionDTOListInContext();
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ELT_FORM, texteTranspositionform);

        return editerElement(idContextuel, idPremierTableau);
    }

    private void putTexteTranspositionDTOListInContext() {
        TranspositionDirective transposition = context.getCurrentDocument().getAdapter(TranspositionDirective.class);

        List<TexteTranspositionDTOImpl> list = SolonEpgServiceLocator
            .getTranspositionDirectiveService()
            .fetchTexteTransposition(transposition, context.getSession())
            .stream()
            .map(TexteTranspositionDTOImpl::new)
            .collect(Collectors.toList());

        context.putInContextData(PanContextDataKey.FIRST_TABLE_LIST, list);
    }

    @Override
    protected String getEditElementPremierTableauTemplate() {
        return "fragments/pan/directives/panEditTexteTransposition";
    }

    @Override
    protected void updateElement(SpecificContext context) throws ParseException {
        PanUIServiceLocator.getTranspositionDirectiveUIService().updateTexteTransposition(context);
    }

    @Override
    protected void removeElement(SpecificContext context) {
        PanUIServiceLocator.getTranspositionDirectiveUIService().removeTexteTransposition(context);
    }

    @Override
    protected AbstractMapTexteMaitreTableDTO getElementFromId(String id) {
        TexteTransposition texteTransposition = context
            .getSession()
            .getDocument(new IdRef(id))
            .getAdapter(TexteTransposition.class);
        return new TexteTranspositionDTOImpl(texteTransposition);
    }

    @Override
    protected AbstractMapTexteMaitreTableDTO newElementDTO() {
        return new TexteTranspositionDTOImpl();
    }
}
