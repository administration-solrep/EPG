package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.core.dto.activitenormative.AbstractMapTexteMaitreTableDTO;
import fr.dila.solonepg.core.exception.ActiviteNormativeException;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.text.ParseException;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class PanAbstractTexteMaitrePremierTableauAjax extends SolonWebObject {
    private static final String EXCEPTION_MESURE_NOT_FOUND = "Mesure introuvable";
    protected static final String ID_NOR = "nor";
    protected static final String ID_CONTEXTUEL = "idContextuel";
    protected static final String ID_PREMIER_TABLEAU = "idPremierTableau";

    @GET
    @Path("charger/{idPremierTableau}")
    public ThTemplate chargerElement(
        @PathParam(ID_PREMIER_TABLEAU) String idPremierTableau,
        @SwRequired @QueryParam(ID_CONTEXTUEL) String idContextuel,
        @QueryParam("hideColumns") Boolean hideColumns
    ) {
        template.setName(getEditElementPremierTableauTemplate());
        context.setCurrentDocument(idContextuel);
        chargerSpecificContent(idPremierTableau);
        template.getData().put(PanTemplateConstants.ID_CONTEXTUEL, idContextuel);
        template.getData().put("hideColumns", BooleanUtils.toBoolean(hideColumns));
        template
            .getData()
            .put(PanTemplateConstants.CURRENT_SECTION, context.getFromContextData(PanContextDataKey.CURRENT_SECTION));
        initFrontComponents();

        return template;
    }

    protected abstract void chargerSpecificContent(String idPremierTableau);

    protected abstract void initFrontComponents();

    @GET
    @Path("creer")
    public Object ajouterElement(@SwRequired @QueryParam(ID_CONTEXTUEL) String idContextuel) {
        template.setName(getEditElementPremierTableauTemplate());
        context.setCurrentDocument(idContextuel);
        initFrontComponents();
        template.getData().put(EpgTemplateConstants.ITEM, newElementDTO());
        template.getData().put(PanTemplateConstants.ID_CONTEXTUEL, idContextuel);
        template
            .getData()
            .put(PanTemplateConstants.CURRENT_SECTION, context.getFromContextData(PanContextDataKey.CURRENT_SECTION));
        return template;
    }

    public Response editerElement(String idContextuel, String idPremierTableau) throws ParseException {
        context.setCurrentDocument(idContextuel);
        List<AbstractMapTexteMaitreTableDTO> list = context.getFromContextData(PanContextDataKey.FIRST_TABLE_LIST);

        AbstractMapTexteMaitreTableDTO element;

        // si idPremierTableau n'est pas valorisé, on est dans le cas de création
        // sinon on met à jour la mesure correspondante
        if (StringUtils.isBlank(idPremierTableau)) {
            element = newElementDTO();
            list.add(element);
        } else {
            element =
                list
                    .stream()
                    .filter(l -> l.getId().equals(idPremierTableau))
                    .findFirst()
                    .orElseThrow(() -> new ActiviteNormativeException(EXCEPTION_MESURE_NOT_FOUND));
        }
        context.putInContextData(PanContextDataKey.FIRST_TABLE_LIST, list);
        context.putInContextData(PanContextDataKey.TEXTE_MAITRE_ID, idContextuel);
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO, element);
        updateElement(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Path("supprimer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response supprimerElement(
        @SwRequired @FormParam(ID_PREMIER_TABLEAU) String id,
        @SwRequired @FormParam(ID_CONTEXTUEL) String idContextuel
    ) {
        context.setCurrentDocument(idContextuel);
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ID, id);
        context.putInContextData(PanContextDataKey.TEXTE_MAITRE_ID, idContextuel);
        removeElement(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    protected abstract AbstractMapTexteMaitreTableDTO newElementDTO();

    protected abstract String getEditElementPremierTableauTemplate();

    protected abstract void updateElement(SpecificContext context) throws ParseException;

    protected abstract void removeElement(SpecificContext context);

    protected abstract AbstractMapTexteMaitreTableDTO getElementFromId(String i);

    @Override
    public ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("", getMyContext());
    }
}
