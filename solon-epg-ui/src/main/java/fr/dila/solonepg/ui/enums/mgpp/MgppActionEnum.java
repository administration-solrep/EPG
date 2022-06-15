package fr.dila.solonepg.ui.enums.mgpp;

import fr.dila.st.ui.enums.ActionEnum;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public enum MgppActionEnum implements ActionEnum {
    ADMIN_MENU_ESPACE_PARL_GESTION_MODELE_COURRIERS,
    ADMIN_MENU_PARAM_PARAM_MGPP,
    ADMIN_MENU_PARAM_REFERENCE_EPP,
    ADMIN_MODELE_COURRIER_CREATE,
    ADMIN_MODELE_COURRIER_DELETE,
    DOSSIER_AUD("mgpp_demandeAudition"),
    DOSSIER_AUTRE_DOCUMENT("mgpp_autresDocumentsTransmisAuxAssemblees"),
    DOSSIER_AVI("mgpp_avisNomination"),
    DOSSIER_DECRET("mgpp_decret"),
    DOSSIER_IE("mgpp_interventionExterieure"),
    DOSSIER_JSS("mgpp_demandeMiseEnOeuvreArticle283C"),
    DOSSIER_OEP("mgpp_designationOEP"),
    DOSSIER_POL_GEN("mgpp_declarationDePolitiqueGenerale"),
    DOSSIER_PROC_LEGIS("mgpp_procedureLegislative"),
    DOSSIER_PUBLI("mgpp_publication"),
    DOSSIER_RAPPORT("mgpp_depotDeRapport"),
    DOSSIER_RESOL("mgpp_resolutionArticle341"),
    DOSSIER_SD("mgpp_declarationSurUnSujetDetermine"),
    EXPORT_LISTE_DOSSIER,
    MGPP_CALENDRIERS,
    MGPP_CALENDRIER_PROMULGATION,
    MGPP_CALENDRIER_SUIVI,
    MGPP_DOSSIERS;

    private final String value;

    MgppActionEnum() {
        this(null);
    }

    MgppActionEnum(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return StringUtils.defaultIfBlank(value, name());
    }

    public static MgppActionEnum fromValue(String value) {
        return fromValue(value, null);
    }

    public static MgppActionEnum fromValue(String value, MgppActionEnum defaultValue) {
        return Stream.of(values()).filter(action -> action.getName().equals(value)).findFirst().orElse(defaultValue);
    }

    public String getValue() {
        return value;
    }
}
