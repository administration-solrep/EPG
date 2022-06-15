package fr.dila.solonepg.ui.enums;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EpgPageProviders {
    DOSSIER_SIMILAIRE_PP("bordereau_dossiers_indexation_content", null, null),
    PROVIDER_CORBEILLE_POSTE(
        "corbeillePostePageProvider",
        ImmutableList.of(
            EpgColumnEnum.INFO_COMPLEMENTAIRE,
            EpgColumnEnum.DATE_ARRIVEE,
            EpgColumnEnum.DATE_PUBLICATION_SOUHAITEE
        ),
        asList(
            EpgPageProviders::putFromCorbeilleInMap,
            EpgPageProviders::putNoteActionsInMap,
            EpgPageProviders::putEspaceDeTravailActionsInMap
        )
    ),
    PROVIDER_CORBEILLE_TYPE_ACTE(
        "corbeilleTypeActePageProvider",
        ImmutableList.of(
            EpgColumnEnum.INFO_COMPLEMENTAIRE,
            EpgColumnEnum.DATE_ARRIVEE,
            EpgColumnEnum.DATE_PUBLICATION_SOUHAITEE
        ),
        asList(
            EpgPageProviders::putFromCorbeilleInMap,
            EpgPageProviders::putNoteActionsInMap,
            EpgPageProviders::putEspaceDeTravailActionsInMap
        )
    ),
    PROVIDER_CORBEILLE_TYPE_ETAPE(
        "corbeilleTypeEtapePageProvider",
        ImmutableList.of(
            EpgColumnEnum.INFO_COMPLEMENTAIRE,
            EpgColumnEnum.DATE_ARRIVEE,
            EpgColumnEnum.DATE_PUBLICATION_SOUHAITEE
        ),
        asList(
            EpgPageProviders::putFromCorbeilleInMap,
            EpgPageProviders::putNoteActionsInMap,
            EpgPageProviders::putEspaceDeTravailActionsInMap
        )
    ),
    PROVIDER_CORBEILLE_DOSSIERS_EN_CREATION(
        "corbeilleDossierEnCreationPageProvider",
        ImmutableList.of(
            EpgColumnEnum.INFO_COMPLEMENTAIRE,
            EpgColumnEnum.COMPLET,
            EpgColumnEnum.DATE_PUBLICATION_SOUHAITEE
        ),
        asList(
            EpgPageProviders::putGeneralsActionsInMap,
            EpgPageProviders::putNoteActionsInMap,
            EpgPageProviders::putEspaceDeTravailActionsInMap
        )
    ),
    PROVIDER_CORBEILLE_DOSSIERS_SUIVIS(
        "corbeilleDossiersSuivisPageProvider",
        ImmutableList.of(EpgColumnEnum.INFO_COMPLEMENTAIRE, EpgColumnEnum.TYPE_ACTE),
        asList(
            EpgPageProviders::putSuiviInMap,
            EpgPageProviders::putGeneralsActionsInMap,
            EpgPageProviders::putEspaceDeTravailActionsInMap
        )
    ),
    PROVIDER_DOSSIERS_ABANDON(
        "dossierAbandon",
        Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE),
        emptyList()
    ),
    PROVIDER_DOSSIERS_CANDIDATS_ELIMINATION(
        "dossiersCandidatElimination",
        Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE),
        emptyList()
    ),
    PROVIDER_DOSSIERS_CANDIDATS_ARCHIVAGE_DEFINITIF(
        "dossierCandidatToArchivageDefinitivePage",
        Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE),
        emptyList()
    ),
    PROVIDER_DOSSIERS_CANDIDATS_ARCHIVAGE_INTERMEDIAIRE(
        "dossierCandidatToArchivageIntermediairePage",
        Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE),
        emptyList()
    ),
    PROVIDER_DOSSIERS_A_TRAITER(
        "dossierTraitePage",
        ImmutableList.of(EpgColumnEnum.INFO_COMPLEMENTAIRE, EpgColumnEnum.TYPE_ACTE),
        asList(EpgPageProviders::putEspaceDeTravailActionsInMap)
    ),
    PROVIDER_DOSSIERS_FAVORIS(
        "dossiersById",
        Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE),
        emptyList()
    ),
    PROVIDER_DOSSIERS_CREES(
        "dossierCreePage",
        ImmutableList.of(EpgColumnEnum.INFO_COMPLEMENTAIRE, EpgColumnEnum.TYPE_ACTE),
        asList(EpgPageProviders::putEspaceDeTravailActionsInMap)
    ),
    PROVIDER_DOSSIERS_VALIDES_ELIMINATION(
        "dossiersValidesElimination",
        Collections.singletonList(EpgColumnEnum.INFO_COMPLEMENTAIRE),
        emptyList()
    ),
    PROVIDER_HISTORIQUE_VALIDATION(
        "corbeilleHistoriqueValidationPageProvider",
        ImmutableList.of(EpgColumnEnum.INFO_COMPLEMENTAIRE, EpgColumnEnum.TYPE_ACTE),
        asList(EpgPageProviders::putEspaceDeTravailActionsInMap)
    );

    /** Nom (Nuxeo) du content view */
    private final String cvName;

    /** Colonnes additionnelles (optionnelles) pour ce provider. */
    private final List<EpgColumnEnum> additionalColumns;

    /**
     * Consumer injectant les actions liées au provider dans la map à partir du
     * context.
     */
    private final List<BiConsumer<Map<String, Object>, SpecificContext>> actionConsumers;

    EpgPageProviders(
        String cvName,
        List<EpgColumnEnum> additionalColumns,
        List<BiConsumer<Map<String, Object>, SpecificContext>> actionConsumers
    ) {
        this.cvName = cvName;
        this.additionalColumns = additionalColumns;
        this.actionConsumers = actionConsumers;
    }

    public String getCvName() {
        return cvName;
    }

    public static EpgPageProviders fromContentViewName(String cvName) {
        return Stream.of(values()).filter(elt -> elt.getCvName().equals(cvName)).findFirst().orElse(null);
    }

    public List<String> getAdditionalColumns() {
        if (additionalColumns == null) {
            return new ArrayList<>();
        }
        return additionalColumns.stream().map(col -> col.getNxPropName()).collect(Collectors.toList());
    }

    public void putActionsInMap(Map<String, Object> map, SpecificContext context) {
        actionConsumers.forEach(action -> action.accept(map, context));
    }

    private static void putSuiviInMap(Map<String, Object> map, SpecificContext context) {
        context.putInContextData(EpgContextDataKey.IS_DOSSIER_SUIVI, true);
    }

    private static void putGeneralsActionsInMap(Map<String, Object> map, SpecificContext context) {
        map.put(
            STTemplateConstants.GENERALE_ACTIONS,
            context.getActions(EpgActionCategory.DOSSIER_CREATION_ACTIONS_GENERAL)
        );
    }

    private static void putFromCorbeilleInMap(Map<String, Object> map, SpecificContext context) {
        context.putInContextData(EpgContextDataKey.IS_FROM_CORBEILLE, true);
    }

    private static void putNoteActionsInMap(Map<String, Object> map, SpecificContext context) {
        map.put(
            SSTemplateConstants.NOTE_ACTIONS,
            context.getActions(EpgActionCategory.CORBEILLE_DOSSIERS_ACTIONS_NOTE)
        );
    }

    private static void putEspaceDeTravailActionsInMap(Map<String, Object> map, SpecificContext context) {
        map.put(
            EpgTemplateConstants.SELECTION_TOOL_DOSSIER_ACTIONS,
            context.getActions(EpgActionCategory.SELECTION_TOOL_DOSSIERS_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.CREATE_LIST_MISE_EN_SIGNATURE_ACTIONS,
            context.getActions(EpgActionCategory.CREATE_LIST_MISE_EN_SIGNATURE_MAILBOX_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.ADD_DOSSIERS_TO_FAVORIS_ACTIONS,
            context.getActions(EpgActionCategory.ADD_DOSSIERS_TO_FAVORIS_ACTIONS)
        );
    }
}
