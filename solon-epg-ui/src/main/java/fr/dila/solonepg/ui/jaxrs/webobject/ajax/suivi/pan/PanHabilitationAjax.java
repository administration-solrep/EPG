package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.dto.HabilitationDTO;
import fr.dila.solonepg.core.dto.activitenormative.AbstractMapTexteMaitreTableDTO;
import fr.dila.solonepg.core.dto.activitenormative.HabilitationDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.model.bean.pan.HabilitationForm;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.model.SpecificContext;
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

@WebObject(type = "PanHabilitationAjax")
public class PanHabilitationAjax extends PanAbstractTexteMaitrePremierTableauAjax {

    @Override
    public void chargerSpecificContent(String idPremierTableau) {
        HabilitationDTO habilitationDto = (HabilitationDTO) getElementFromId(idPremierTableau);

        if (StringUtils.isNotBlank(habilitationDto.getMinisterePilote())) {
            Map<String, String> mapMinisterePilote = Collections.singletonMap(
                habilitationDto.getMinisterePilote(),
                STServiceLocator
                    .getOrganigrammeService()
                    .getOrganigrammeNodeById(habilitationDto.getMinisterePilote(), OrganigrammeType.MINISTERE)
                    .getLabel()
            );
            template.getData().put("mapMinisterePilote", mapMinisterePilote);
        }
        template
            .getData()
            .put(
                "ordonnancesNor",
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .fetchListOrdonnance(habilitationDto.getOrdonnanceIds(), context.getSession())
                    .stream()
                    .map(d -> new SelectValueDTO(d.getNumeroNor(), d.getNumeroNor()))
                    .collect(Collectors.toList())
            );
        template.getData().put(EpgTemplateConstants.ITEM, habilitationDto);
    }

    @Override
    public AbstractMapTexteMaitreTableDTO getElementFromId(String id) {
        Habilitation habilitation = context.getSession().getDocument(new IdRef(id)).getAdapter(Habilitation.class);

        return new HabilitationDTOImpl(habilitation);
    }

    @Override
    protected void initFrontComponents() {
        template
            .getData()
            .put("typeHabilitationsList", EpgUIServiceLocator.getEpgSelectValueUIService().getTypeHabilitation());
    }

    @POST
    @Path("editer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editerHabilitation(
        @FormParam(ID_CONTEXTUEL) String idContextuel,
        @FormParam(ID_PREMIER_TABLEAU) String idPremierTableau,
        @SwBeanParam HabilitationForm habilitationForm
    )
        throws Exception {
        context.setCurrentDocument(idContextuel);
        putListHabilitationInContext();

        context.putInContextData(PanContextDataKey.FIRST_TABLE_ELT_FORM, habilitationForm);

        return editerElement(idContextuel, idPremierTableau);
    }

    private void putListHabilitationInContext() {
        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);
        List<HabilitationDTO> list = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .fetchListHabilitation(texteMaitre.getHabilitationIds(), context.getSession())
            .stream()
            .map(HabilitationDTOImpl::new)
            .collect(Collectors.toList());
        context.putInContextData(PanContextDataKey.FIRST_TABLE_LIST, list);
    }

    @Override
    protected String getEditElementPremierTableauTemplate() {
        return "fragments/pan/habilitations/panEditHabilitation";
    }

    @Override
    protected void updateElement(SpecificContext context) throws ParseException {
        PanUIServiceLocator.getTexteMaitreHabilitationUIService().updateHabilitation(context);
    }

    @Override
    protected void removeElement(SpecificContext context) {
        HabilitationDTO habilitationDto = (HabilitationDTO) getElementFromId(
            context.getFromContextData(PanContextDataKey.FIRST_TABLE_ID)
        );
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO, habilitationDto);
        PanUIServiceLocator.getTexteMaitreHabilitationUIService().removeHabilitation(context);
    }

    @Override
    protected AbstractMapTexteMaitreTableDTO newElementDTO() {
        putListHabilitationInContext();
        return new HabilitationDTOImpl();
    }
}
