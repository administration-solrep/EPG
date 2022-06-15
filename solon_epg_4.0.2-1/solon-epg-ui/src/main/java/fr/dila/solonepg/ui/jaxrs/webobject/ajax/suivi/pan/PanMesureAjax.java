package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.dto.MesureApplicativeDTO;
import fr.dila.solonepg.core.dto.activitenormative.AbstractMapTexteMaitreTableDTO;
import fr.dila.solonepg.core.dto.activitenormative.DecretDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.MesureApplicativeDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.PanActionsDTO;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.bean.pan.MesureForm;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

@WebObject(type = "PanMesureAjax")
public class PanMesureAjax extends PanAbstractTexteMaitrePremierTableauAjax {

    @Override
    public void chargerSpecificContent(String idPremierTableau) {
        MesureApplicativeDTO mesureDto = (MesureApplicativeDTO) getElementFromId(idPremierTableau);

        if (StringUtils.isNotBlank(mesureDto.getPoleChargeMission())) {
            Optional<SelectValueDTO> select = EpgUIServiceLocator
                .getEpgSelectValueUIService()
                .getPoleChargeMission()
                .stream()
                .filter(it -> it.getLabel().equals(mesureDto.getPoleChargeMission()))
                .findFirst();
            select.ifPresent(selectValueDTO -> template.getData().put("poleChargeMissionKey", selectValueDTO.getKey()));
        }

        if (StringUtils.isNotBlank(mesureDto.getMinisterePilote())) {
            Map<String, String> mapMinisterePilote = Collections.singletonMap(
                mesureDto.getMinisterePilote(),
                STServiceLocator
                    .getOrganigrammeService()
                    .getOrganigrammeNodeById(mesureDto.getMinisterePilote(), OrganigrammeType.MINISTERE)
                    .getLabel()
            );
            template.getData().put("mapMinisterePilote", mapMinisterePilote);
        }
        template
            .getData()
            .put(
                "decretsNor",
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .fetchDecrets(mesureDto.getDecretIds(), context.getSession())
                    .stream()
                    .map(d -> new SelectValueDTO(d.getNumeroNor(), d.getNumeroNor()))
                    .collect(Collectors.toList())
            );
        context = PanUIServiceLocator.getPanUIService().putPanActionDTOInContext(context);
        template
            .getData()
            .put(
                PanTemplateConstants.PROFIL_ONLY_MINISTERIEL,
                (
                    (PanActionsDTO) context.getFromContextData(PanContextDataKey.PAN_ACTION_DTO)
                ).getIsOnlyUtilisateurMinistereLoiOrOrdonnance()
            );
        template.getData().put(EpgTemplateConstants.ITEM, mesureDto);
    }

    @Override
    protected void initFrontComponents() {
        template
            .getData()
            .put(
                PanTemplateConstants.TYPE_MESURES_LIST,
                EpgUIServiceLocator.getEpgSelectValueUIService().getTypeMesures()
            );
        List<SelectValueDTO> lstSelectValues = EpgUIServiceLocator.getEpgSelectValueUIService().getPoleChargeMission();
        lstSelectValues.sort(SelectValueDTO.getLabelComparator());
        template.getData().put(PanTemplateConstants.POLE_CHARGE_MISSION_LIST, lstSelectValues);
        template
            .getData()
            .put(
                PanTemplateConstants.RESPONSABLE_AMENDEMENT_LIST,
                EpgUIServiceLocator.getEpgSelectValueUIService().getResponsableAmendement()
            );
    }

    @POST
    @Path("editer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editerMesure(
        @FormParam(ID_CONTEXTUEL) String idContextuel,
        @FormParam(ID_PREMIER_TABLEAU) String idPremierTableau,
        @SwBeanParam MesureForm mesureform
    )
        throws ParseException {
        context.setCurrentDocument(idContextuel);
        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);

        List<MesureApplicativeDTO> list = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .fetchMesure(texteMaitre.getMesuresIds(), context.getSession())
            .stream()
            .map(mesure -> new MesureApplicativeDTOImpl(mesure, texteMaitre))
            .collect(Collectors.toList());

        context.putInContextData(PanContextDataKey.FIRST_TABLE_LIST, list);
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ELT_FORM, mesureform);
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ID, idContextuel);
        context.putInContextData(
            PanContextDataKey.FIRST_TABLE_ELT,
            context.getSession().getDocument(new IdRef(idContextuel)).getAdapter(MesureApplicative.class)
        );
        context.putInContextData(PanContextDataKey.RELOAD_FROM_DOSSIER, false);
        Map<String, DecretDTOImpl> decretsMap = PanUIServiceLocator
            .getTexteMaitreUIService()
            .reloadDecrets(context)
            .entrySet()
            .stream()
            .collect(Collectors.toMap(e -> e.getKey(), e -> (DecretDTOImpl) e.getValue()));
        context.putInContextData(PanContextDataKey.SECOND_TABLE_MAP, decretsMap); // pour l'éventuelle màj de la liste des décrets

        return editerElement(idContextuel, idPremierTableau);
    }

    @Override
    public ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("", getMyContext());
    }

    @Override
    protected String getEditElementPremierTableauTemplate() {
        return "fragments/pan/components/panEditMesure";
    }

    @Override
    protected void updateElement(SpecificContext context) throws ParseException {
        PanUIServiceLocator.getTexteMaitreUIService().updateMesure(context);
    }

    @Override
    protected void removeElement(SpecificContext context) {
        PanUIServiceLocator.getTexteMaitreUIService().removeMesure(context);
    }

    @Override
    protected AbstractMapTexteMaitreTableDTO getElementFromId(String id) {
        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);
        MesureApplicative mesure = context.getSession().getDocument(new IdRef(id)).getAdapter(MesureApplicative.class);

        return new MesureApplicativeDTOImpl(mesure, texteMaitre);
    }

    @Override
    protected AbstractMapTexteMaitreTableDTO newElementDTO() {
        return new MesureApplicativeDTOImpl();
    }
}
