package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.dto.LoiDeRatificationDTO;
import fr.dila.solonepg.core.dto.activitenormative.AbstractMapTexteMaitreTableDTO;
import fr.dila.solonepg.core.dto.activitenormative.LoiDeRatificationDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.bean.pan.LoiRatificationForm;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanLoiRatificationAjax")
public class PanLoiRatificationAjax extends PanAbstractTexteMaitrePremierTableauAjax {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("ajouter")
    public Response ajouterElement(
        @SwRequired @FormParam(ID_CONTEXTUEL) String idContextuel,
        @SwRequired @FormParam(ID_NOR) String nor
    ) {
        template.setName(getEditElementPremierTableauTemplate());
        context.setCurrentDocument(idContextuel);
        template.getData().put(EpgTemplateConstants.ITEM, newElementDTO());
        template.getData().put(PanTemplateConstants.ID_CONTEXTUEL, idContextuel);
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ELT_NOR, nor);

        template
            .getData()
            .put(PanTemplateConstants.CURRENT_SECTION, context.getFromContextData(PanContextDataKey.CURRENT_SECTION));

        putLoiRatificationDTOListInContext();
        PanUIServiceLocator.getTexteMaitreOrdonnanceUIService().addLoiRatification(context);
        PanUIServiceLocator.getTexteMaitreOrdonnanceUIService().saveLoiRatification(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    private List<DocumentModel> getAvailableDocs(List<String> ids) {
        return context
            .getSession()
            .getDocuments(ids.stream().map(IdRef::new).filter(context.getSession()::exists).toArray(IdRef[]::new));
    }

    private void putLoiRatificationDTOListInContext() {
        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);
        List<LoiDeRatificationDTO> loisRatificationList = getAvailableDocs(texteMaitre.getLoiRatificationIds())
            .stream()
            .map(doc -> new LoiDeRatificationDTOImpl(doc.getAdapter(LoiDeRatification.class)))
            .collect(Collectors.toList());
        context.putInContextData(PanContextDataKey.FIRST_TABLE_LIST, loisRatificationList);
    }

    @Override
    public void chargerSpecificContent(String idPremierTableau) {
        LoiDeRatificationDTO ratificationDto = (LoiDeRatificationDTO) getElementFromId(idPremierTableau);
        template.getData().put(EpgTemplateConstants.ITEM, ratificationDto);
    }

    @Override
    protected void initFrontComponents() {}

    @POST
    @Path("editer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editerLoiRatification(
        @FormParam(ID_CONTEXTUEL) String idContextuel,
        @FormParam(ID_PREMIER_TABLEAU) String idPremierTableau,
        @SwBeanParam LoiRatificationForm loiRatificationform
    )
        throws ParseException {
        context.setCurrentDocument(idContextuel);
        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);

        List<LoiDeRatificationDTO> list = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .fetchLoiDeRatification(texteMaitre.getLoiRatificationIds(), context.getSession())
            .stream()
            .map(LoiDeRatificationDTOImpl::new)
            .collect(Collectors.toList());

        context.putInContextData(PanContextDataKey.FIRST_TABLE_LIST, list);
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ELT_FORM, loiRatificationform);

        return editerElement(idContextuel, idPremierTableau);
    }

    @Override
    protected String getEditElementPremierTableauTemplate() {
        return "fragments/pan/ratification/panEditLoiRatification";
    }

    @Override
    protected void updateElement(SpecificContext context) throws ParseException {
        PanUIServiceLocator.getTexteMaitreOrdonnanceUIService().updateLoiDeRatification(context);
    }

    @Override
    protected void removeElement(SpecificContext context) {
        PanUIServiceLocator.getTexteMaitreOrdonnanceUIService().removeLoiDeRatification(context);
    }

    @Override
    protected AbstractMapTexteMaitreTableDTO getElementFromId(String id) {
        LoiDeRatification loiDeRatification = context
            .getSession()
            .getDocument(new IdRef(id))
            .getAdapter(LoiDeRatification.class);
        return new LoiDeRatificationDTOImpl(loiDeRatification);
    }

    @Override
    protected AbstractMapTexteMaitreTableDTO newElementDTO() {
        return new LoiDeRatificationDTOImpl();
    }
}
