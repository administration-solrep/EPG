package fr.dila.solonepg.ui.enums;

import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_MINISTERE_RESPONSABLE_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_NOM_RESPONSABLE_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import fr.dila.cm.caselink.CaseLinkConstants;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum EpgFiltreEnum implements IFiltreEnum {
    NUMERO_NOR(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_XPATH),
    TITRE_ACTE(
        WidgetTypeEnum.INPUT_TEXT,
        DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_XPATH,
        null,
        value -> "*" + value + "*"
    ),
    BASE_LEGALE_ACTE(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_ACTE_XPATH),
    CATEGORIE_ACTE(
        WidgetTypeEnum.SELECT,
        DossierSolonEpgConstants.DOSSIER_CATEGORIE_ACTE_LABEL_XPATH,
        () -> EpgUIServiceLocator.getEpgSelectValueUIService().getCategorieActe(),
        null
    ),
    COMPLET(WidgetTypeEnum.RADIO, DossierSolonEpgConstants.DOSSIER_IS_PARAPHEUR_COMPLET_XPATH),
    DATE_AG_CE(WidgetTypeEnum.MULTIPLE_DATE, ConseilEtatConstants.CONSEIL_ETAT_DATE_AG_CE_XPATH),
    DATE_ARRIVEE_DOSSIER_LINK(WidgetTypeEnum.MULTIPLE_DATE, CaseLinkConstants.DATE_FIELD),
    DATE_CREATION_DOSSIER(WidgetTypeEnum.MULTIPLE_DATE, DossierSolonEpgConstants.DOSSIER_CREATED_XPATH),
    DATE_DERNIERE_MODIFICATION(WidgetTypeEnum.MULTIPLE_DATE, STSchemaConstant.DUBLINCORE_MODIFIED_XPATH),
    DATE_EFFET(WidgetTypeEnum.MULTIPLE_DATE, DossierSolonEpgConstants.DOSSIER_DATE_EFFET_XPATH),
    DATE_PARUTION_JORF(WidgetTypeEnum.DATE, DossierSolonEpgConstants.DOSSIER_RETDILA_DATE_PARUTION_JORF_XPATH),
    DATE_PUBLICATION(WidgetTypeEnum.MULTIPLE_DATE, DossierSolonEpgConstants.DOSSIER_DATE_PUBLICATION_XPATH),
    DATE_PUBLICATION_SOUHAITEE(WidgetTypeEnum.MULTIPLE_DATE, DossierSolonEpgConstants.DOSSIER_DATE_PUBLICATION_XPATH),
    DATE_PRECISEE_PUBLICATION(
        WidgetTypeEnum.MULTIPLE_DATE,
        DossierSolonEpgConstants.DOSSIER_DATE_PRECISEE_PUBLICATION_XPATH
    ),
    DATE_SECTION_CE(WidgetTypeEnum.MULTIPLE_DATE, ConseilEtatConstants.CONSEIL_ETAT_DATE_SECTION_CE_XPATH),
    DATE_SIGNATURE(WidgetTypeEnum.MULTIPLE_DATE, DossierSolonEpgConstants.DOSSIER_DATE_SIGNATURE_XPATH),
    DATE_TRANSMISSION_SECTION_CE(
        WidgetTypeEnum.MULTIPLE_DATE,
        ConseilEtatConstants.CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_XPATH
    ),
    DELAI_PUBLICATION(
        WidgetTypeEnum.SELECT,
        DossierSolonEpgConstants.DOSSIER_DELAI_PUBLICATION_XPATH,
        () -> EpgUIServiceLocator.getEpgSelectValueUIService().getDelaiPublication(),
        null
    ),
    DERNIER_CONTRIBUTEUR(WidgetTypeEnum.INPUT_TEXT, STSchemaConstant.DUBLINCORE_LAST_CONTRIBUTOR_XPATH),
    DIRECTION_ATTACHE(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_XPATH),
    DIRECTION_RESP(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_DIRECTION_RESPONSABLE_XPATH),
    HABILITATION_COMMENTAIRE(
        WidgetTypeEnum.INPUT_TEXT,
        DossierSolonEpgConstants.DOSSIER_HABILITATION_COMMENTAIRE_XPATH
    ),
    HABILITATION_NUMERO_ARTICLES(
        WidgetTypeEnum.INPUT_TEXT,
        DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ARTICLES_XPATH
    ),
    HABILITATION_NUMERO_ORDRE(
        WidgetTypeEnum.INPUT_TEXT,
        DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ORDRE_XPATH
    ),
    HABILITATION_REF_LOI(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_HABILITATION_REF_LOI_XPATH),
    ID_CREATEUR_DOSSIER(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_ID_CREATEUR_XPATH),
    MAIL_RESP_DOSSIER(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_MAIL_RESPONSABLE_XPATH),
    MINISTERE_ATTACHE(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_XPATH),
    MINISTERE_RESP_BORD(WidgetTypeEnum.INPUT_TEXT, DOSSIER_MINISTERE_RESPONSABLE_XPATH),
    MODE_PARUTION(
        WidgetTypeEnum.SELECT,
        RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_LABEL_XPATH,
        () -> EpgUIServiceLocator.getEpgSelectValueUIService().getModeParution(),
        null
    ),
    NOM_RESP_DOSSIER(WidgetTypeEnum.INPUT_TEXT, DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_NOM_RESPONSABLE_PROPERTY),
    NUMERO_I_S_A(WidgetTypeEnum.INPUT_TEXT, ConseilEtatConstants.CONSEIL_ETAT_NUMERO_ISA_XPATH),
    NUMERO_TEXTE_PARUTION_JORF(
        WidgetTypeEnum.INPUT_TEXT,
        RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_JORF_XPATH
    ),
    PAGE_PARUTION_JORF(WidgetTypeEnum.INPUT_TEXT, RetourDilaConstants.RETOUR_DILA_PAGE_PARUTION_JORF_XPATH),
    PRIORITE(
        WidgetTypeEnum.SELECT,
        ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_XPATH,
        () -> EpgUIServiceLocator.getEpgSelectValueUIService().getPrioriteCE(),
        null
    ),
    PUBLICATION_INTEGRALE_OU_EXTRAIT(
        WidgetTypeEnum.SELECT,
        DossierSolonEpgConstants.DOSSIER_PUBLICATION_INTEGRALE_OU_EXTRAIT_LABEL_XPATH,
        () -> EpgUIServiceLocator.getEpgSelectValueUIService().getTypePublication(),
        null
    ),
    PUBLICATION_RAPPORT_PRESENTATION(
        WidgetTypeEnum.INPUT_TEXT,
        DossierSolonEpgConstants.DOSSIER_PUBLICATION_RAPPORT_PRESENTATION_XPATH
    ),
    POUR_FOURNITURE_EPREUVE(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_POUR_FOURNITURE_EPREUVE_XPATH),
    PRENOM_RESP_DOSSIER(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_PRENOM_RESPONSABLE_XPATH),
    RAPPORTEUR_CE(WidgetTypeEnum.INPUT_TEXT, ConseilEtatConstants.CONSEIL_ETAT_RAPPORTEUR_CE_XPATH),
    QUALITE_RESP_DOSSIER(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_QUALITE_RESPONSABLE_XPATH),
    SECTION_CE(WidgetTypeEnum.INPUT_TEXT, ConseilEtatConstants.CONSEIL_ETAT_SECTION_CE_XPATH),
    STATUT(
        WidgetTypeEnum.SELECT,
        DossierSolonEpgConstants.DOSSIER_STATUT_XPATH,
        () -> EpgUIServiceLocator.getEpgSelectValueUIService().getStatutDossier(),
        null
    ),
    TEL_RESP_DOSSIER(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_TEL_RESPONSABLE_XPATH),
    TYPE_ACTE(
        WidgetTypeEnum.SELECT,
        DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_XPATH,
        () -> EpgUIServiceLocator.getEpgSelectValueUIService().getTypeActe(),
        null
    ),
    ZONE_COM_SGG_DILA(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_ZONE_COM_SGG_DILA_XPATH);

    /**
     * Préfixe à appliquer sur le camel case de l'élément de l'enum pour avoir la clé de traduction associée au widget
     */
    private static final String LABEL_EPG_DOSSIER = "label.content.header.";

    private final WidgetTypeEnum type;
    private final Supplier<List<SelectValueDTO>> supplier;
    private final String xpath;
    private final Function<Serializable, Serializable> valueTreatmentFct;

    private static final Map<String, EpgFiltreEnum> WIDGET_NAME_TO_ENUM = Stream
        .of(values())
        .collect(toMap(EpgFiltreEnum::getWidgetName, identity()));

    EpgFiltreEnum(WidgetTypeEnum type, String xpath) {
        this(type, xpath, null, null);
    }

    EpgFiltreEnum(
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
        return LABEL_EPG_DOSSIER + name;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    private static EpgFiltreEnum filter(Predicate<EpgFiltreEnum> predicate) {
        return Stream.of(values()).filter(predicate).findFirst().orElse(null);
    }

    public static EpgFiltreEnum fromWidgetName(String widgetName) {
        return WIDGET_NAME_TO_ENUM.get(widgetName);
    }

    public static EpgFiltreEnum fromXpath(String xpath) {
        return filter(filtre -> filtre.getXpath().equals(xpath));
    }

    @Override
    public Supplier<List<SelectValueDTO>> getSupplier() {
        return supplier;
    }

    @Override
    public Function<Serializable, Serializable> getValueTreatmentFct() {
        return valueTreatmentFct;
    }
}
