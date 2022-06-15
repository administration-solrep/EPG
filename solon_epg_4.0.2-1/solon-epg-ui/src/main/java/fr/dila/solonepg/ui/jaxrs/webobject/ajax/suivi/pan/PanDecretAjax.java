package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.core.dto.activitenormative.AbstractMapTexteMaitreTableDTO;
import fr.dila.solonepg.core.dto.activitenormative.DecretDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.MesureApplicativeDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.DecretsPanUnsortedList;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.bean.pan.DecretForm;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanDecretAjax")
public class PanDecretAjax extends PanAbstractTexteMaitreSecondTableauAjax<DecretDTOImpl> {

    public PanDecretAjax() {
        super();
    }

    @Override
    protected void chargerSpecificContent(DecretDTOImpl decret) {}

    @POST
    @Path("editer")
    public ThTemplate editerDecret(
        @SwRequired @FormParam(ID_SECOND_TABLEAU) String idSecondTableau,
        @SwRequired @FormParam(ID_PREMIER_TABLEAU) String idPremierTableau,
        @SwRequired @FormParam(ID_CONTEXTUEL) String idContextuel,
        @SwBeanParam DecretForm decretform
    )
        throws ParseException {
        context.putInContextData(PanContextDataKey.SECOND_TABLE_ELT_FORM, decretform);
        context.putInContextData(PanContextDataKey.SECOND_TABLE_ELT_ID, idSecondTableau);
        return editerElement(idSecondTableau, idPremierTableau, idContextuel);
    }

    @Override
    protected List<AbstractMapTexteMaitreTableDTO> getFirstTableList(TexteMaitre texteMaitre) {
        return SolonEpgServiceLocator
            .getActiviteNormativeService()
            .fetchMesure(texteMaitre.getMesuresIds(), context.getSession())
            .stream()
            .map(it -> new MesureApplicativeDTOImpl(it, texteMaitre))
            .collect(Collectors.toList());
    }

    @Override
    protected void restituerSecondTableauSpecificContent(
        String idContextuel,
        String idPremierTableau,
        Map<String, DecretDTOImpl> elementsMap
    ) {
        DecretsPanUnsortedList decretsList = PanUIServiceLocator.getPanUIService().getDecrets(context);
        context.putInContextData(PanContextDataKey.SECOND_TABLE_LIST, decretsList);
        context.putInContextData(PanContextDataKey.SECOND_TABLE_TYPE, "decret");
        context.putInContextData(PanContextDataKey.SECOND_TABLE_TYPE_NOR, "decret");
        MesureApplicative mesureApplicative = context
            .getSession()
            .getDocument(new IdRef(idPremierTableau))
            .getAdapter(MesureApplicative.class);
        context.putInContextData(
            PanContextDataKey.HAS_SURBRILLANCE,
            !mesureApplicative.getDecretsIdsInvalidated().isEmpty()
        );

        template
            .getData()
            .put(
                "mesureApplication",
                context
                    .getSession()
                    .getDocument(new IdRef(idPremierTableau))
                    .getAdapter(MesureApplicative.class)
                    .getMesureApplication()
            );

        MesureApplicative mesure = context
            .getSession()
            .getDocument(new IdRef(idPremierTableau))
            .getAdapter(MesureApplicative.class);
        String numeroMesure = mesure.getNumeroOrdre();
        String numeroLoi = context
            .getSession()
            .getDocument(new IdRef(idContextuel))
            .getAdapter(TexteMaitre.class)
            .getNumero();

        template
            .getData()
            .put(
                PanTemplateConstants.SECOND_TABLE_CAPTION,
                ResourceHelper.getString(
                    "pan.application." +
                    context.getFromContextData(PanContextDataKey.CURRENT_SECTION) +
                    ".decrets.title",
                    numeroLoi,
                    numeroMesure
                )
            );
    }

    @Override
    protected String getEditSecondElementTemplate() {
        return "fragments/pan/components/panEditDecret";
    }

    @Override
    protected Class<? extends TexteMaitre> getFirstTableEltClass() {
        return MesureApplicative.class;
    }

    @Override
    protected Map<String, DecretDTOImpl> reloadSecondTableau(SpecificContext context) {
        return PanUIServiceLocator
            .getTexteMaitreUIService()
            .reloadDecrets(context)
            .entrySet()
            .stream()
            .collect(Collectors.toMap(e -> e.getKey(), e -> (DecretDTOImpl) e.getValue()));
    }

    @Override
    protected void addElement(SpecificContext context) {
        PanUIServiceLocator.getTexteMaitreUIService().addNewDecret(context);
    }

    @Override
    protected void removeElement(SpecificContext context) {
        PanUIServiceLocator.getTexteMaitreUIService().removeDecret(context);
    }

    @Override
    protected void updateElement(SpecificContext context) throws ParseException {
        PanUIServiceLocator.getTexteMaitreUIService().updateDecret(context);
    }

    @Override
    protected void saveElement(SpecificContext context) {
        PanUIServiceLocator.getTexteMaitreUIService().saveDecret(context);
    }
}
