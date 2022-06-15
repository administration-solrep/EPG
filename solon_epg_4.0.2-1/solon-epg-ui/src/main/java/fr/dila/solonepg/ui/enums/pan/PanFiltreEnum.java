package fr.dila.solonepg.ui.enums.pan;

import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.ui.enums.IFiltreEnum;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum PanFiltreEnum implements IFiltreEnum {
    NUMERO_NOR(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.NUMERO_NOR_XPATH, null, null),
    TITRE(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.TITRE_OFFICIEL_XPATH, null, PanFiltreEnum::wrapWithStar),
    TITRE_DIRECTIVE(
        WidgetTypeEnum.INPUT_TEXT,
        TexteMaitreConstants.TITRE_ACTE_XPATH,
        null,
        PanFiltreEnum::wrapWithStar
    ),
    NUMERO(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.NUMERO_XPATH, null, null),
    MINISTERE_PILOTE(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.MINISTERE_PILOTE_XPATH, null, null),
    PROMULGATION(WidgetTypeEnum.MULTIPLE_DATE, TexteMaitreConstants.DATE_PROMULGATION_XPATH, null, null),
    DATE_PUBLICATION(WidgetTypeEnum.MULTIPLE_DATE, TexteMaitreConstants.DATE_PUBLICATION_XPATH, null, null),
    PROCEDURE_VOTE(
        WidgetTypeEnum.SELECT,
        TexteMaitreConstants.PROCEDURE_VOTE_XPATH,
        () -> EpgUIServiceLocator.getEpgSelectValueUIService().getProcedureVote(),
        null
    ),
    NATURE_TEXTE(
        WidgetTypeEnum.SELECT,
        TexteMaitreConstants.NATURE_TEXTE_XPATH,
        () -> EpgUIServiceLocator.getEpgSelectValueUIService().getNatureTexte(),
        null
    ),
    LEGISLATURE_PUBLICATION(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.LEGISLATURE_PUBLICATION_XPATH, null, null),
    COMMISSION_AN(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.COMMISSION_ASS_NATIONALE_XPATH, null, null),
    COMMISSION_SENAT(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.COMMISSION_SENAT_XPATH, null, null),
    NUMERO_INTERNE(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.NUMERO_INTERNE_XPATH, null, null),
    MOT_CLE(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.MOT_CLE_XPATH, null, PanFiltreEnum::wrapWithStar),
    FONDEMENT_CONSTITUTIONNEL(
        WidgetTypeEnum.SELECT,
        TexteMaitreConstants.DISPOSITION_HABILITATION_XPATH,
        () -> EpgUIServiceLocator.getEpgSelectValueUIService().getDispositionHabilitation(),
        null
    ),
    CHAMP_LIBRE(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.CHAMP_LIBRE_XPATH, null, PanFiltreEnum::wrapWithStar),
    CATEGORIE(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.CATEGORIE_XPATH, null, PanFiltreEnum::wrapWithStar),
    ORGANISATION(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.ORGANISATION_XPATH, null, PanFiltreEnum::wrapWithStar),
    THEMATIQUE(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.THEMATIQUE_XPATH, null, PanFiltreEnum::wrapWithStar),
    INTITULE(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.INTITULE_XPATH, null, PanFiltreEnum::wrapWithStar),
    DATE_SIGNATURE(WidgetTypeEnum.MULTIPLE_DATE, TexteMaitreConstants.DATE_SIGNATURE_XPATH, null, null),
    DATE_ENTREE_VIGUEUR(WidgetTypeEnum.MULTIPLE_DATE, TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR_XPATH, null, null),
    OBSERVATIONS(WidgetTypeEnum.INPUT_TEXT, TexteMaitreConstants.OBSERVATION_XPATH, null, PanFiltreEnum::wrapWithStar);

    /**
     * Préfixe à appliquer sur le camel case de l'élément de l'enum pour avoir la
     * clé de traduction associée au widget
     */
    private static final String LABEL_PAN_DOSSIER = "label.content.header.";

    /**
     * Type de filtre
     */
    private final WidgetTypeEnum type;

    /**
     * Xpath du champ nuxeo associé
     */
    private final String xpath;

    private final Supplier<List<SelectValueDTO>> supplier;

    private final Function<Serializable, Serializable> valueTreatmentFct;

    PanFiltreEnum(
        WidgetTypeEnum type,
        String xpath,
        Supplier<List<SelectValueDTO>> supplier,
        Function<Serializable, Serializable> valueTreatmentFct
    ) {
        this.type = type;
        this.xpath = xpath;
        this.supplier = supplier;
        this.valueTreatmentFct = valueTreatmentFct;
    }

    @Override
    public WidgetTypeEnum getType() {
        return type;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getWidgetLabel() {
        String name = getWidgetName();
        return LABEL_PAN_DOSSIER + name;
    }

    @Override
    public Supplier<List<SelectValueDTO>> getSupplier() {
        return supplier;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    @Override
    public Function<Serializable, Serializable> getValueTreatmentFct() {
        return valueTreatmentFct;
    }

    private static PanFiltreEnum filter(Predicate<PanFiltreEnum> predicate) {
        return Stream.of(values()).filter(predicate).findFirst().orElse(null);
    }

    public static PanFiltreEnum fromWidgetName(String widgetName) {
        return filter(filtre -> filtre.getWidgetName().equals(widgetName));
    }

    public static PanFiltreEnum fromXpath(String xpath) {
        return filter(filtre -> filtre.getXpath().equals(xpath));
    }

    private static String wrapWithStar(Serializable value) {
        return "*" + value + "*";
    }
}
