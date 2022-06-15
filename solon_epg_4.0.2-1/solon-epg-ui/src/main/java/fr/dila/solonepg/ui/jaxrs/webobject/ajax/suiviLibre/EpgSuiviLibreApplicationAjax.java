package fr.dila.solonepg.ui.jaxrs.webobject.ajax.suiviLibre;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.AbstractPanSortedList;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.jaxrs.webobject.AbstractEpgSuiviLibrePage;
import fr.dila.solonepg.ui.services.pan.PanStatistiquesUIService;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.bean.OrganigrammeElementDTO;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.services.STUIServiceLocator;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang3.BooleanUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "SuiviLibreApplicationAjax")
public class EpgSuiviLibreApplicationAjax extends AbstractEpgSuiviLibrePage {
    private static final String LEGISLATURE_EN_COURS = "legislatureEnCours";
    private static final String TYPE_ACTE = "typeActe";
    private static final String TYPE_APPLICATION = "typeApplication";
    private static final List<String> TYPES_ACTE = ImmutableList.of("lois", "ordonnances", "habilitations");
    private static final List<String> TYPES_APPLICATION = ImmutableList.of(
        "applicationLois",
        "applicationOrdonnances",
        "suiviOrdonnances"
    );

    public EpgSuiviLibreApplicationAjax() {
        super();
    }

    @GET
    public ThTemplate get(
        @QueryParam(LEGISLATURE_EN_COURS) Boolean legislatureEnCours,
        @QueryParam(TYPE_ACTE) String typeActe,
        @QueryParam(TYPE_APPLICATION) String typeApplication
    ) {
        if (legislatureEnCours == null) {
            legislatureEnCours = context.getFromContextData(PanContextDataKey.LEGISLATURE_EN_COURS);
        }
        if (typeActe == null) {
            typeActe = context.getFromContextData(PanContextDataKey.SUIVI_TYPE_ACTE);
        }
        if (typeApplication == null) {
            typeApplication = context.getFromContextData(PanContextDataKey.SUIVI_CURRENT_SECTION);
        }
        return load(legislatureEnCours, typeActe, typeApplication);
    }

    @GET
    @Path("charger")
    public ThTemplate load(
        @SwRequired @QueryParam(LEGISLATURE_EN_COURS) Boolean legislatureEnCours,
        @SwRequired @QueryParam(TYPE_ACTE) String typeActe,
        @SwRequired @QueryParam(TYPE_APPLICATION) String typeApplication
    ) {
        if (!TYPES_ACTE.contains((typeActe)) || !TYPES_APPLICATION.contains(typeApplication)) {
            throw new IllegalArgumentException("Valeur invalide pour typeActe ou typeApplication");
        }

        Map<String, Object> map = template.getData();
        boolean finalLegislatureEnCours = BooleanUtils.toBooleanDefaultIfNull(legislatureEnCours, true);

        PanStatistiquesUIService statService = PanUIServiceLocator.getPanStatistiquesUIService();
        logAndDo(
            () -> {
                ActiviteNormativeEnum currentMenu = ActiviteNormativeEnum.getById(typeActe);
                context.putInContextData(PanContextDataKey.CURRENT_SECTION_ENUM, currentMenu);
                String legislature = SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .getLegislatureEnCoursOuPrec(context.getSession(), finalLegislatureEnCours);
                context.putInContextData(PanContextDataKey.LEGISLATURE_INPUT, legislature);
                context.putInContextData(PanContextDataKey.PAN_FORM, new LoisSuiviesForm());
                context.putInContextData(PanContextDataKey.CURRENT_SECTION, typeActe);
                context.putInContextData(PanContextDataKey.LEGISLATURE_EN_COURS, finalLegislatureEnCours);
                context.putInContextData(PanContextDataKey.FORCE_NON_PAGINATED_TABLEAU_MAITRE, true);
                AbstractPanSortedList<AbstractMapDTO> textesMaitres = PanUIServiceLocator
                    .getPanUIService()
                    .getTableauMaitre(context);
                map.put(PanTemplateConstants.TEXTE_MAITRE, textesMaitres);
                List<OrganigrammeElementDTO> organigramme = STUIServiceLocator
                    .getOrganigrammeTreeService()
                    .getOrganigramme(context);
                List<SelectValueDTO> ministeresList = new ArrayList<>();
                if (!organigramme.isEmpty()) {
                    List<String> ministeres = SolonEpgServiceLocator
                        .getActiviteNormativeService()
                        .getAllAplicationMinisteresList(context.getSession(), typeActe, finalLegislatureEnCours);
                    ministeresList =
                        organigramme
                            .get(0)
                            .getChilds()
                            .stream()
                            .map(OrganigrammeElementDTO.class::cast)
                            .filter(node -> ministeres.contains(node.getMinistereId()))
                            .map(
                                it ->
                                    new SelectValueDTO(
                                        it.getMinistereId(),
                                        STServiceLocator
                                            .getOrganigrammeService()
                                            .getOrganigrammeNodeById(it.getMinistereId(), OrganigrammeType.MINISTERE)
                                            .getLabel()
                                    )
                            )
                            .collect(Collectors.toList());
                }
                map.put(PanTemplateConstants.MINISTERES, ministeresList);
                context.putInContextData(PanContextDataKey.SUIVI_CURRENT_SECTION, typeApplication);
                Map<String, List<ANReportEnum>> otherData = statService.getAdditionnalReportsSuiviLibre(context);
                map.put(PanTemplateConstants.EXTRA, otherData);
                map.put(PanTemplateConstants.TYPE_APPLICATION, typeApplication);
                map.put(EpgTemplateConstants.TYPE_ACTE, typeActe);
                map.put(PanTemplateConstants.LEGISLATURE_EN_COURS, finalLegislatureEnCours);
                map.put(PanTemplateConstants.DATE_ACTUALISATION, statService.getSuiviDateActualisation(context));
            }
        );
        template.setContext(context);
        return template;
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/suiviLibre/applicationLegislature", context);
    }
}
