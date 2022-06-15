package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suivi.pan;

import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.core.dto.activitenormative.AbstractMapTexteMaitreTableDTO;
import fr.dila.solonepg.core.exception.ActiviteNormativeException;
import fr.dila.solonepg.ui.bean.pan.ConsultTexteMaitreDTO;
import fr.dila.solonepg.ui.enums.pan.PanActionCategory;
import fr.dila.solonepg.ui.enums.pan.PanActionEnum;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanUserSessionKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.nuxeo.ecm.core.api.IdRef;

public abstract class PanAbstractTexteMaitreSecondTableauAjax<T extends AbstractMapTexteMaitreTableDTO>
    extends SolonWebObject {
    protected static final String ID_CONTEXTUEL = "idContextuel";
    protected static final String ID_PREMIER_TABLEAU = "idPremierTableau";
    protected static final String ID_SECOND_TABLEAU = "idSecondTableau";
    private static final String EXCEPTION_ELT_NOT_FOUND = "Element introuvable";

    public PanAbstractTexteMaitreSecondTableauAjax() {
        super();
    }

    @POST
    public ThTemplate afficherSecondTableauPost(
        @QueryParam(ID_CONTEXTUEL) String idContextuel,
        @QueryParam(ID_PREMIER_TABLEAU) String idPremierTableau,
        @SwBeanParam LoisSuiviesForm panListForm
    ) {
        if (panListForm != null) {
            UserSessionHelper.putUserSessionParameter(context, PanUserSessionKey.THIRD_TABLE, panListForm);
        }
        return afficherSecondTableau(idContextuel, idPremierTableau);
    }

    @GET
    public ThTemplate afficherSecondTableau(
        @QueryParam(ID_CONTEXTUEL) String idContextuel,
        @QueryParam(ID_PREMIER_TABLEAU) String idPremierTableau
    ) {
        if (idContextuel == null && idPremierTableau == null) {
            idContextuel = context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_ID);
            idPremierTableau = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ID);
        }
        context.setCurrentDocument(idContextuel);
        context.putInContextData(PanContextDataKey.RELOAD_FROM_DOSSIER, false);
        Map<String, T> elementsMap = recupererSecondTableau(idContextuel, idPremierTableau);

        return restituerSecondTableau(idContextuel, idPremierTableau, elementsMap);
    }

    @GET
    @Path("charger/{idSecondTableau}")
    public ThTemplate chargerElement(
        @PathParam(ID_SECOND_TABLEAU) String idSecondTableau,
        @SwRequired @QueryParam(ID_PREMIER_TABLEAU) String idPremierTableau,
        @SwRequired @QueryParam(ID_CONTEXTUEL) String idContextuel
    ) {
        template.setName(getEditSecondElementTemplate());
        context.putInContextData(PanContextDataKey.RELOAD_FROM_DOSSIER, false);
        context.setCurrentDocument(idContextuel);

        Map<String, T> elementMap = recupererSecondTableau(idContextuel, idPremierTableau);
        T element = retrieveElementFromMap(elementMap, idSecondTableau).orElse(null);

        chargerSpecificContent(element);

        template.getData().put(PanTemplateConstants.ID_CONTEXTUEL, idContextuel);
        template.getData().put(PanTemplateConstants.ID_FIRST_TABLE, idPremierTableau);
        template.getData().put("secondElement", element);
        template.getData().put("typeActe", EpgUIServiceLocator.getEpgSelectValueUIService().getTypeActe());

        return template;
    }

    protected abstract void chargerSpecificContent(T element);

    protected abstract String getEditSecondElementTemplate();

    @POST
    @Path("recharger")
    public ThTemplate rechargerSecondTableau(
        @SwRequired @FormParam(ID_PREMIER_TABLEAU) String idPremierTableau,
        @SwRequired @FormParam(ID_CONTEXTUEL) String idContextuel
    ) {
        context.setCurrentDocument(idContextuel);
        context.putInContextData(PanContextDataKey.RELOAD_FROM_DOSSIER, true);
        Map<String, T> elementMap = recupererSecondTableau(idContextuel, idPremierTableau);

        // une fois les infos rechargées, on les valide pour les enregistrer
        for (T d : elementMap.values()) {
            d.setValidate(true);
        }

        prepareForSave(idContextuel, idPremierTableau, elementMap);

        saveElement(context);

        return restituerSecondTableau(idContextuel, idPremierTableau, elementMap);
    }

    @POST
    @Path("ajouter")
    public ThTemplate ajouterElement(
        @SwRequired @FormParam(ID_PREMIER_TABLEAU) String idPremierTableau,
        @SwRequired @FormParam(ID_CONTEXTUEL) String idContextuel,
        @SwRequired @FormParam("nor") String nor
    ) {
        context.setCurrentDocument(idContextuel);
        context.putInContextData(PanContextDataKey.RELOAD_FROM_DOSSIER, false);
        Map<String, T> elementMap = recupererSecondTableau(idContextuel, idPremierTableau);
        if (!elementMap.containsKey(nor)) {
            context.putInContextData(PanContextDataKey.SECOND_TABLE_MAP, elementMap);
            context.putInContextData(PanContextDataKey.SECOND_TABLE_ELT_NOR, nor);
            addElement(context);
            prepareForSave(idContextuel, idPremierTableau, elementMap);
            saveElement(context);
        } else {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("pan.troisieme.tableau.ajouter.nor.existant", nor));
            addMessageQueueInSession();
        }
        return restituerSecondTableau(idContextuel, idPremierTableau, elementMap);
    }

    @POST
    @Path("supprimer")
    public ThTemplate supprimerElement(
        @SwRequired @FormParam(ID_SECOND_TABLEAU) String idSecondTableau,
        @SwRequired @FormParam(ID_PREMIER_TABLEAU) String idPremierTableau,
        @SwRequired @FormParam(ID_CONTEXTUEL) String idContextuel
    ) {
        context.setCurrentDocument(idContextuel);
        context.putInContextData(PanContextDataKey.RELOAD_FROM_DOSSIER, false);

        Map<String, T> elementMap = recupererSecondTableau(idContextuel, idPremierTableau);

        T element = retrieveElementFromMap(elementMap, idSecondTableau)
            .orElseThrow(() -> new ActiviteNormativeException(EXCEPTION_ELT_NOT_FOUND));
        context.putInContextData(PanContextDataKey.SECOND_TABLE_ELT_DTO, element);

        prepareForSave(idContextuel, idPremierTableau, elementMap);

        removeElement(context);
        saveElement(context);

        return restituerSecondTableau(idContextuel, idPremierTableau, elementMap);
    }

    protected Map<String, T> recupererSecondTableau(String idContextuel, String idPremierTableau) {
        context.putInContextData(PanContextDataKey.TEXTE_MAITRE_ID, idContextuel);
        context.setCurrentDocument(idContextuel);
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ID, idPremierTableau);
        context.putInContextData(
            PanContextDataKey.FIRST_TABLE_ELT,
            context.getSession().getDocument(new IdRef(idPremierTableau)).getAdapter(getFirstTableEltClass())
        );
        return reloadSecondTableau(context);
    }

    public ThTemplate editerElement(String idSecondTableau, String idPremierTableau, String idContextuel)
        throws ParseException {
        context.setCurrentDocument(idContextuel);
        context.putInContextData(PanContextDataKey.RELOAD_FROM_DOSSIER, false);
        Map<String, T> elementMap = recupererSecondTableau(idContextuel, idPremierTableau);

        T updateElement = retrieveElementFromMap(elementMap, idSecondTableau)
            .orElseThrow(() -> new ActiviteNormativeException(EXCEPTION_ELT_NOT_FOUND));

        prepareForSave(idContextuel, idPremierTableau, elementMap);

        context.putInContextData(PanContextDataKey.SECOND_TABLE_ELT_DTO, updateElement);

        updateElement(context);

        return restituerSecondTableau(idContextuel, idPremierTableau, elementMap);
    }

    private void prepareForSave(String idContextuel, String idPremierTableau, Map<String, T> elementMap) {
        context.putInContextData(PanContextDataKey.SECOND_TABLE_MAP, elementMap);
        context.setCurrentDocument(idContextuel);
        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);

        List<AbstractMapTexteMaitreTableDTO> firstTableList = getFirstTableList(texteMaitre);
        context.putInContextData(PanContextDataKey.FIRST_TABLE_LIST, firstTableList);

        AbstractMapDTO firstTableEltDto = firstTableList
            .stream()
            .filter(it -> it.getId().equals(idPremierTableau))
            .findFirst()
            .orElse(null);
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO, firstTableEltDto);

        context.putInContextData(
            PanContextDataKey.FIRST_TABLE_ELT,
            context.getSession().getDocument(new IdRef(idPremierTableau)).getAdapter(getFirstTableEltClass())
        );
    }

    protected abstract List<AbstractMapTexteMaitreTableDTO> getFirstTableList(TexteMaitre texteMaitre);

    protected abstract void restituerSecondTableauSpecificContent(
        String idContextuel,
        String idPremierTableau,
        Map<String, T> elementsMap
    );

    protected ThTemplate restituerSecondTableau(
        String idContextuel,
        String idPremierTableau,
        Map<String, T> elementsMap
    ) {
        LoisSuiviesForm sortPaginateForm = ObjectHelper.requireNonNullElseGet(
            UserSessionHelper.getUserSessionParameter(context, PanUserSessionKey.THIRD_TABLE),
            LoisSuiviesForm::new
        );
        restituerSecondTableauSpecificContent(idContextuel, idPremierTableau, elementsMap);

        ConsultTexteMaitreDTO texteMaitreDTO = PanUIServiceLocator.getPanUIService().getConsultTexteMaitreDTO(context);
        template.getData().put(PanTemplateConstants.CONSULT_TEXTE_MAITRE_DTO, texteMaitreDTO);
        context.putInContextData(PanContextDataKey.CONSULT_TEXTEM_DTO, texteMaitreDTO);

        template.getData().put(PanTemplateConstants.ID_FIRST_TABLE, idPremierTableau);
        template.getData().put("secondTableForm", sortPaginateForm);
        template.getData().put("secondTableList", context.getFromContextData(PanContextDataKey.SECOND_TABLE_LIST));
        template.getData().put("secondTableActions", context.getActions(PanActionCategory.SECOND_TABLE_ACTIONS));
        String secondTableType = context.getFromContextData(PanContextDataKey.SECOND_TABLE_TYPE);
        template.getData().put("secondTableType", secondTableType + "s");
        String secondTableTypeNor = context.getFromContextData(PanContextDataKey.SECOND_TABLE_TYPE_NOR);
        template.getData().put("secondTableTypeNor", secondTableTypeNor);
        template.getData().put("secondTableLineTemplate", "ligne-" + secondTableType);

        template
            .getData()
            .put("secondTableLineActions", context.getActions(PanActionCategory.SECOND_TABLE_LINE_ACTIONS));
        template.getData().put("ajouterSecondTableNorAction", context.getAction(PanActionEnum.ADD_SECOND_ELT_NOR));

        template.getData().put("typeActe", EpgUIServiceLocator.getEpgSelectValueUIService().getTypeActe());
        template
            .getData()
            .put(PanTemplateConstants.CURRENT_SECTION, context.getFromContextData(PanContextDataKey.CURRENT_SECTION));
        return template;
    }

    protected abstract Class<? extends TexteMaitre> getFirstTableEltClass();

    private Optional<T> retrieveElementFromMap(Map<String, T> elements, String id) {
        return elements.values().stream().filter(it -> it.getId().equals(id)).findFirst();
    }

    protected abstract Map<String, T> reloadSecondTableau(SpecificContext context);

    protected abstract void addElement(SpecificContext context);

    protected abstract void removeElement(SpecificContext context);

    protected abstract void updateElement(SpecificContext context) throws ParseException;

    protected abstract void saveElement(SpecificContext context);

    @Override
    public ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/pan/texte-maitre-second-tableau", getMyContext());
    }
}
