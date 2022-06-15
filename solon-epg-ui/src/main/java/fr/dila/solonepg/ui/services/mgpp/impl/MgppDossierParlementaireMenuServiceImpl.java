package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.DOSSIERS_PARLEMENTAIRES_SELECTED;

import fr.dila.solonepg.ui.bean.DossiersParlementairesMenu;
import fr.dila.solonepg.ui.enums.mgpp.CritereRechercheDossiersParlementaires;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppUserSessionKey;
import fr.dila.solonepg.ui.services.mgpp.MgppDossierParlementaireMenuService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.MgppTemplateConstants;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.bean.SelectValueGroupDTO;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.services.impl.FragmentServiceImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.platform.actions.Action;

public class MgppDossierParlementaireMenuServiceImpl
    extends FragmentServiceImpl
    implements MgppDossierParlementaireMenuService {
    public static final String ACTIVE_KEY = "corbeilleActivated";

    public MgppDossierParlementaireMenuServiceImpl() {
        super();
    }

    @Override
    public Map<String, Object> getData(SpecificContext context) {
        Map<String, Object> mapData = new HashMap<>();

        List<SelectValueDTO> dossiersParlementaires = context
            .getActions(MgppActionCategory.DOSSIER_PARLEMENTAIRE_ACTIONS)
            .stream()
            .map(action -> createSelectValueDTOFromAction(context, action))
            .collect(Collectors.toList());
        mapData.put("dossiersParlementaires", dossiersParlementaires);

        String dossiersParlementairesSelected = UserSessionHelper.getUserSessionParameter(
            context,
            MgppUserSessionKey.DOSSIER_PARLEMENTAIRE
        );
        if (StringUtils.isNotBlank(context.getFromContextData(DOSSIERS_PARLEMENTAIRES_SELECTED))) {
            dossiersParlementairesSelected = context.getFromContextData(DOSSIERS_PARLEMENTAIRES_SELECTED);
        }
        if (StringUtils.isBlank(dossiersParlementairesSelected) && CollectionUtils.isNotEmpty(dossiersParlementaires)) {
            SelectValueDTO firstDossierParlementaire = dossiersParlementaires.get(0);
            dossiersParlementairesSelected =
                firstDossierParlementaire instanceof SelectValueGroupDTO
                    ? ((SelectValueGroupDTO) firstDossierParlementaire).getSelectValues().get(0).getId()
                    : firstDossierParlementaire.getId();
        }

        mapData.put("dossiersParlementairesSelected", dossiersParlementairesSelected);
        context.putInContextData(DOSSIERS_PARLEMENTAIRES_SELECTED, dossiersParlementairesSelected);
        UserSessionHelper.putUserSessionParameter(
            context,
            MgppUserSessionKey.DOSSIER_PARLEMENTAIRE,
            dossiersParlementairesSelected
        );

        DossiersParlementairesMenu dto = getDossiersParlementairesMenu(context);
        mapData.put("corbeilles", dto.getCorbeilles());
        mapData.put(ACTIVE_KEY, UserSessionHelper.getUserSessionParameter(context, ACTIVE_KEY, String.class));
        mapData.put("criteresRecherche", dto.getCriteres());
        // data du select (création com créatrice)
        List<SelectValueDTO> evenementTypes = SolonMgppServiceLocator
            .getEvenementTypeService()
            .findEvenementTypeCreateur(dossiersParlementairesSelected)
            .stream()
            .map(type -> new SelectValueDTO(type.getName(), type.getLabel()))
            .sorted(Comparator.comparing(SelectValueDTO::getLabel))
            .collect(Collectors.toList());
        mapData.put("evenementTypes", evenementTypes);
        mapData.put(
            MgppTemplateConstants.CREATE_DOSSIER_MGPP_ACTIONS,
            context.getActions(MgppActionCategory.CREATE_DOSSIER_MGPP)
        );

        return mapData;
    }

    private SelectValueDTO createSelectValueDTOFromAction(SpecificContext context, Action action) {
        List<Action> subActions;
        switch (action.getId()) {
            case SolonMgppViewConstant.CATEGORY_DECLARATION_ID:
                subActions = context.getActions(MgppActionCategory.MGPP_DECLARATION_CATEGORY);
                break;
            case SolonMgppViewConstant.CATEGORY_NOMINATION_ID:
                subActions = context.getActions(MgppActionCategory.MGPP_NOMINATION_CATEGORY);
                break;
            case SolonMgppViewConstant.CATEGORY_ORGANISATION_ID:
                subActions = context.getActions(MgppActionCategory.MGPP_ORGANISATION_CATEGORY);
                break;
            case SolonMgppViewConstant.CATEGORY_RAPORT_ID:
                subActions = context.getActions(MgppActionCategory.MGPP_RAPORT_CATEGORY);
                break;
            default:
                subActions = Collections.emptyList();
                break;
        }
        return subActions.isEmpty()
            ? new SelectValueDTO(action.getId(), action.getLabel())
            : new SelectValueGroupDTO(
                action.getLabel(),
                subActions
                    .stream()
                    .map(subAct -> createSelectValueDTOFromAction(context, subAct))
                    .collect(Collectors.toList())
            );
    }

    private DossiersParlementairesMenu getDossiersParlementairesMenu(SpecificContext context) {
        String dossiersParlementairesSelected = context.getFromContextData(DOSSIERS_PARLEMENTAIRES_SELECTED);
        DossiersParlementairesMenu dto = new DossiersParlementairesMenu();
        dto.setCorbeilles(MgppUIServiceLocator.getMgppCorbeilleUIService().getCorbeilles(context));
        dto.setCriteres(
            CritereRechercheDossiersParlementaires.getCriteresRechercheDossiersParlementaires(
                dossiersParlementairesSelected
            )
        );
        return dto;
    }
}
