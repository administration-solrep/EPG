package fr.dila.solonepg.ui.enums;

import fr.dila.ss.ui.enums.EtapeValidationStatus;
import java.util.Objects;
import java.util.stream.Stream;

public enum EpgEtapeValidationStatusEnum implements EtapeValidationStatus {
    VALIDMANUEL(
        "1",
        "label.reponses.feuilleRoute.etape.valide.manuellement",
        "icon--check-circle base-paragraph--success"
    ),
    AVIS_FAVORABLE(
        "1",
        "2",
        "label.epg.feuilleRoute.etape.valide.avis.favorable",
        "icon--check-circle base-paragraph--success"
    ),
    DOSSIER_LANCE(
        "1",
        "1",
        "label.epg.feuilleRoute.etape.valide.dossier.lance",
        "icon--check-circle base-paragraph--success"
    ),
    REFUS("2", "label.reponses.feuilleRoute.etape.valide.refusValidation", "icon--times-circle base-paragraph--danger"),
    AVIS_DEFAVORABLE(
        "2",
        "2",
        "label.epg.feuilleRoute.etape.valide.avis.defavorable",
        "icon--times-circle base-paragraph--danger"
    ),
    REFUS_MODIFICATION(
        "2",
        "15",
        "label.epg.feuilleRoute.etape.valide.refus.modification",
        "icon--times-circle base-paragraph--danger"
    ),
    VALIDAUTO(
        "3",
        "label.reponses.feuilleRoute.etape.valide.automatiquement",
        "icon--check-bubble-cog base-paragraph--success"
    ),
    NONCONCERNE(
        "4",
        "label.reponses.feuilleRoute.etape.nonConcerne",
        "icon--user-bubble-times base-paragraph--default"
    ),
    FAVORABLE_CORRECTION(
        "10",
        "2",
        "label.epg.feuilleRoute.etape.valide.avisFavorableCorrection",
        "icon--check-circle-bubble-eye base-paragraph--success"
    ),
    VALIDER_CORRECTION(
        "10",
        "label.epg.feuilleRoute.etape.valide.etapeValiderAvecCorrection",
        "icon--check-circle-bubble-eye base-paragraph--success"
    ),
    RETOUR_MODIF(
        "15",
        "label.epg.feuilleRoute.etape.valide.retourModification",
        "icon--arrow-up-left-circle base-paragraph--default"
    ),
    FAVORABLE_AVIS_RENDU(
        "20",
        "label.epg.feuilleRoute.etape.valide.manuellement.avisRendu",
        "icon--check-circle base-paragraph--success"
    ),
    DEFAVORABLE_DESSAISESSEMENT(
        "22",
        "label.epg.feuilleRoute.etape.valide.refusValidation.22",
        "icon--times-circle base-paragraph--danger"
    ),
    DEFAVORABLE_RETRAIT(
        "23",
        "label.epg.feuilleRoute.etape.valide.refusValidation.23",
        "icon--times-circle base-paragraph--danger"
    ),
    DEFAVORABLE_REFUS(
        "24",
        "label.epg.feuilleRoute.etape.valide.refusValidation.24",
        "icon--times-circle base-paragraph--danger"
    ),
    DEFAVORABLE_RENVOI(
        "25",
        "label.epg.feuilleRoute.etape.valide.refusValidation.25",
        "icon--times-circle base-paragraph--danger"
    );

    private final String key;
    private final String typeEtape;
    private final String labelKey;
    private final String icon;

    EpgEtapeValidationStatusEnum(String key, String labelKey, String icon) {
        this(key, null, labelKey, icon);
    }

    EpgEtapeValidationStatusEnum(String key, String typeEtape, String labelKey, String icon) {
        this.key = key;
        this.typeEtape = typeEtape;
        this.labelKey = labelKey;
        this.icon = icon;
    }

    public static EpgEtapeValidationStatusEnum getEnumFromKey(String key, String typeEtape) {
        return Stream
            .of(values())
            .filter(elem -> Objects.equals(elem.getKey(), key) && Objects.equals(elem.getTypeEtape(), typeEtape))
            .findFirst()
            .orElse(
                Stream
                    .of(values())
                    .filter(elem -> Objects.equals(elem.getKey(), key))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Valeur inattendue '" + key + "'"))
            );
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getTypeEtape() {
        return typeEtape;
    }

    @Override
    public String getLabelKey() {
        return labelKey;
    }

    @Override
    public String getIcon() {
        return icon;
    }
}
