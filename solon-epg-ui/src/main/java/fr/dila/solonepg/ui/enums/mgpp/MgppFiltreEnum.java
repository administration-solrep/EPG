package fr.dila.solonepg.ui.enums.mgpp;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.ui.helper.mgpp.MgppEditWidgetFactory;
import fr.dila.solonmgpp.api.constant.SolonMgppCorbeilleConstant;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationSupportOrganisme;
import fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName;
import fr.dila.solonmgpp.core.domain.FichePresentationAUDImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationDRImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationDecretImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.solonmgpp.core.dto.EvenementDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.requete.recherchechamp.Parametre;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.bean.WidgetDTO;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.text.CaseUtils;

public enum MgppFiltreEnum {
    BASE_LEGALE(WidgetTypeEnum.INPUT_TEXT, FichePresentationSupportOrganisme.BASE_LEGALE),
    CODE_LECTURE(WidgetTypeEnum.SELECT, "", MgppSelectValueType.NIVEAU_LECTURE),
    COPIE_EVENEMENT(WidgetTypeEnum.SELECT, "", MgppSelectValueType.INSTITUTION),
    DATE_ARRIVEE(WidgetTypeEnum.DATE, STSchemaConstant.DUBLINCORE_CREATED_PROPERTY),
    DATE_CREATION(WidgetTypeEnum.MULTIPLE_DATE, FicheLoi.DATE_CREATION),
    DATE_DEBUT(WidgetTypeEnum.MULTIPLE_DATE, FichePresentationAUDImpl.DATE),
    DATE_DEPOT(WidgetTypeEnum.DATE, FichePresentation341.DATE_DEPOT),
    DATE_DEPOT_DR(WidgetTypeEnum.DATE, FichePresentationDRImpl.DATE_DEPOT_EFFECTIF),
    DATE_EVENEMENT(WidgetTypeEnum.MULTIPLE_DATE, ""),
    DATE_FIN(WidgetTypeEnum.MULTIPLE_DATE, FichePresentationAUDImpl.DATE_FIN),
    DATE_PUB(WidgetTypeEnum.DATE, FichePresentationDecretImpl.DATE_JO),
    DATE_TRANS(WidgetTypeEnum.DATE, FichePresentationIE.DATE),
    DESTINATAIRE_EVENEMENT(WidgetTypeEnum.SELECT, "", MgppSelectValueType.INSTITUTION),
    EMETTEUR(WidgetTypeEnum.INPUT_TEXT, FichePresentation341.AUTEUR),
    EMETTEUR_EVENEMENT(WidgetTypeEnum.SELECT, "", MgppSelectValueType.INSTITUTION),
    ETAT_EVENEMENT(WidgetTypeEnum.SELECT, "", MgppSelectValueType.ETAT_EVENT),
    ETAT_MESSAGE(WidgetTypeEnum.SELECT, ""),
    ID_DOSSIER(WidgetTypeEnum.INPUT_TEXT, FichePresentationSupportOrganisme.ID_DOSSIER),
    ID_DOSSIER_FICHE(WidgetTypeEnum.INPUT_TEXT, FichePresentationSupportOrganisme.ID_DOSSIER),
    ID_EVENEMENT(WidgetTypeEnum.INPUT_TEXT, EvenementDTOImpl.ID_EVENEMENT),
    ID_NOR(WidgetTypeEnum.INPUT_TEXT, FichePresentationDecretImpl.NOR),
    ID_PROPOSITION(WidgetTypeEnum.INPUT_TEXT, FichePresentation341.IDENTIFIANT_PROPOSITION),
    INTITULE(WidgetTypeEnum.INPUT_TEXT, FicheLoi.INTITULE),
    MIN_RATTACH(WidgetTypeEnum.SELECT, FichePresentationOEPImpl.MINISTERE_RATTACHEMENT, MgppSelectValueType.MINISTERE),
    NOR(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY),
    OBJET(WidgetTypeEnum.INPUT_TEXT, FichePresentation341.OBJET),
    OBJET_IE(WidgetTypeEnum.INPUT_TEXT, FichePresentationIE.INTITULE),
    OEP_NOM(WidgetTypeEnum.INPUT_TEXT, FichePresentationOEPImpl.NOM_ORGANISME),
    ORG_NOM(WidgetTypeEnum.INPUT_TEXT, FichePresentationSupportOrganisme.NOM_ORGANISME),
    PRESENCE_PIECE_JOINTE(WidgetTypeEnum.RADIO, ""),
    STATUT(WidgetTypeEnum.SELECT, DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY, MgppSelectValueType.STATUT),
    TEXTE_REF(WidgetTypeEnum.INPUT_TEXT, FichePresentationOEPImpl.TEXTE_REF),
    TITRE_ACTE(WidgetTypeEnum.INPUT_TEXT, DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_PROPERTY),
    TYPE_ACTE(
        WidgetTypeEnum.SELECT,
        DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY,
        MgppSelectValueType.TYPE_ACTE
    ),
    TYPE_EVENEMENT(WidgetTypeEnum.SELECT, "", MgppSelectValueType.TYPE_COM),
    TYPE_RAPPORT(WidgetTypeEnum.SELECT, FichePresentationDRImpl.RAPPORT_PARLEMENT, MgppSelectValueType.TYPE_RAPPORT);

    private static final String LABEL_MGPP_EVENEMENT = "label.mgpp.evenement.";
    private final WidgetTypeEnum type;
    private final MgppSelectValueType selecteur;
    private final String fieldName;

    MgppFiltreEnum(WidgetTypeEnum type, String fieldName) {
        this(type, fieldName, null);
    }

    MgppFiltreEnum(WidgetTypeEnum type, String fieldName, MgppSelectValueType selecteur) {
        this.type = type;
        this.fieldName = fieldName;
        this.selecteur = selecteur;
    }

    public WidgetTypeEnum getType() {
        return type;
    }

    public WidgetDTO initWidget(SpecificContext context) {
        String name = getWidgetName();
        String label = getWidgetLabel();
        WidgetDTO widget = new WidgetDTO(name, label, type.toString());
        List<SelectValueDTO> lstSelectValues = null;

        if (selecteur != null) {
            lstSelectValues = selecteur.getSelectValues();
        } else if (this == ETAT_MESSAGE) {
            MgppCorbeilleName curCorbeille = context.getFromContextData(MgppContextDataKey.CORBEILLE_ID);
            boolean isInHistoriqueCorbeille =
                (
                    curCorbeille != null &&
                    curCorbeille.getIdCorbeille().endsWith(SolonMgppCorbeilleConstant.HISTORIQUE_SUFFIXE)
                );

            lstSelectValues =
                SolonMgppServiceLocator
                    .getMetaDonneesService()
                    .findAllEtatMessage(isInHistoriqueCorbeille)
                    .stream()
                    .map(etat -> new SelectValueDTO(etat, "label.mgpp.etat." + etat))
                    .collect(Collectors.toList());
        }

        if (CollectionUtils.isNotEmpty(lstSelectValues)) {
            widget.getParametres().add(new Parametre(MgppEditWidgetFactory.LST_VALUES_PARAM_NAME, lstSelectValues));
        }

        return widget;
    }

    public String getWidgetName() {
        return CaseUtils.toCamelCase(name().toLowerCase(), false, '_');
    }

    public String getSortName() {
        return getWidgetName() + "Tri";
    }

    public String getWidgetLabel() {
        String name = getWidgetName();
        return ResourceHelper.getString(LABEL_MGPP_EVENEMENT + name).equals(LABEL_MGPP_EVENEMENT + name)
            ? "label.mgpp.fichedossier." + name
            : LABEL_MGPP_EVENEMENT + name;
    }

    public ColonneInfo buildColonne(String value) {
        ColonneInfo colonne = new ColonneInfo(getWidgetLabel(), true, true, false, true);

        colonne.setSortName(getSortName());
        colonne.setSortId(getWidgetName() + "Header");
        colonne.setSortValue(value);

        return colonne;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static MgppFiltreEnum fromWidgetName(String widgetName) {
        return Stream.of(values()).filter(filtre -> filtre.getWidgetName().equals(widgetName)).findFirst().orElse(null);
    }
}
