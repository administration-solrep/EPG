package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.core.dto.activitenormative.AbstractMapTexteMaitreTableDTO;
import fr.dila.solonepg.core.dto.activitenormative.HabilitationDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.OrdonnanceHabilitationDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.OrdonnancesHabilitationsPanUnsortedList;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.bean.pan.OrdonnanceHabilitationForm;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanOrdonnanceHabilitationAjax")
public class PanOrdonnanceHabilitationAjax
    extends PanAbstractTexteMaitreSecondTableauAjax<OrdonnanceHabilitationDTOImpl> {

    public PanOrdonnanceHabilitationAjax() {
        super();
    }

    @POST
    @Path("editer")
    public ThTemplate editerOrdonnanceHabilitation(
        @SwRequired @FormParam(ID_SECOND_TABLEAU) String idSecondTableau,
        @SwRequired @FormParam(ID_PREMIER_TABLEAU) String idPremierTableau,
        @SwRequired @FormParam(ID_CONTEXTUEL) String idContextuel,
        @SwBeanParam OrdonnanceHabilitationForm ordonnanceHabilitationForm
    )
        throws ParseException {
        context.putInContextData(PanContextDataKey.SECOND_TABLE_ELT_FORM, ordonnanceHabilitationForm);
        return editerElement(idSecondTableau, idPremierTableau, idContextuel);
    }

    @Override
    protected List<AbstractMapTexteMaitreTableDTO> getFirstTableList(TexteMaitre texteMaitre) {
        return SolonEpgServiceLocator
            .getActiviteNormativeService()
            .fetchListHabilitation(texteMaitre.getHabilitationIds(), context.getSession())
            .stream()
            .map(HabilitationDTOImpl::new)
            .collect(Collectors.toList());
    }

    @Override
    protected void restituerSecondTableauSpecificContent(
        String idContextuel,
        String idPremierTableau,
        Map<String, OrdonnanceHabilitationDTOImpl> elementsMap
    ) {
        OrdonnancesHabilitationsPanUnsortedList ordoList = new OrdonnancesHabilitationsPanUnsortedList(
            context,
            new ArrayList<>(elementsMap.values())
        );
        context.putInContextData(PanContextDataKey.SECOND_TABLE_LIST, ordoList);
        context.putInContextData(PanContextDataKey.SECOND_TABLE_TYPE, "ordonnanceHabilitation");
        context.putInContextData(PanContextDataKey.SECOND_TABLE_TYPE_NOR, "ordonnance");

        Habilitation habilitation = context
            .getSession()
            .getDocument(new IdRef(idPremierTableau))
            .getAdapter(Habilitation.class);
        String numeroHabilitation = habilitation.getNumeroOrdre();
        String numeroLoi = context
            .getSession()
            .getDocument(new IdRef(idContextuel))
            .getAdapter(TexteMaitre.class)
            .getNumero();

        template
            .getData()
            .put(
                PanTemplateConstants.SECOND_TABLE_CAPTION,
                ResourceHelper.getString("pan.application.ordonnancesHabilitation.title", numeroLoi, numeroHabilitation)
            );
    }

    @Override
    protected void chargerSpecificContent(OrdonnanceHabilitationDTOImpl element) {
        if (StringUtils.isNotBlank(element.getMinisterePilote())) {
            Map<String, String> mapMinisterePilote = Collections.singletonMap(
                element.getMinisterePilote(),
                STServiceLocator
                    .getOrganigrammeService()
                    .getOrganigrammeNodeById(element.getMinisterePilote(), OrganigrammeType.MINISTERE)
                    .getLabel()
            );
            template.getData().put("mapMinisterePilote", mapMinisterePilote);
        }
    }

    @Override
    protected String getEditSecondElementTemplate() {
        return "fragments/pan/habilitations/panEditOrdonnanceHabilitation";
    }

    @Override
    protected Class<? extends TexteMaitre> getFirstTableEltClass() {
        return Habilitation.class;
    }

    @Override
    protected Map<String, OrdonnanceHabilitationDTOImpl> reloadSecondTableau(SpecificContext context) {
        context.putInContextData(
            PanContextDataKey.FIRST_TABLE_ELT_DTO,
            new HabilitationDTOImpl(context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT))
        );

        return PanUIServiceLocator
            .getTexteMaitreHabilitationUIService()
            .reloadOrdonnanceHabilitation(context)
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> (OrdonnanceHabilitationDTOImpl) e.getValue()));
    }

    @Override
    protected void addElement(SpecificContext context) {
        PanUIServiceLocator.getTexteMaitreHabilitationUIService().addNewOrdonnance(context);
    }

    @Override
    protected void removeElement(SpecificContext context) {
        PanUIServiceLocator.getTexteMaitreHabilitationUIService().removeOrdonnanceHabilitation(context);
    }

    @Override
    protected void updateElement(SpecificContext context) throws ParseException {
        PanUIServiceLocator.getTexteMaitreHabilitationUIService().updateOrdonnanceHabilitation(context);
    }

    @Override
    protected void saveElement(SpecificContext context) {
        PanUIServiceLocator.getTexteMaitreHabilitationUIService().saveOrdonnanceHabilitation(context);
    }
}
