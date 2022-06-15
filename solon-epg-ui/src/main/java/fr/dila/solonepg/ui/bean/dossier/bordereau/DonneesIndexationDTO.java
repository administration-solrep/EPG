package fr.dila.solonepg.ui.bean.dossier.bordereau;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.st.ui.annot.NxProp;
import java.util.Calendar;
import java.util.List;

public class DonneesIndexationDTO {

    public DonneesIndexationDTO() {
        super();
    }

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_TEXTE_ENTREPRISE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Boolean isTexteRubriqueEntreprise;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_DATE_EFFET_SCHEMA,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private List<Calendar> datesEffetRubriqueEntreprise;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private List<String> rubriques;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_INDEXATION_MOTS_CLES_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private List<String> motsCles;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_INDEXATION_CHAMP_LIBRE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private List<String> champsLibres;

    private Boolean isVisibleTexteEntreprise = Boolean.FALSE;

    public Boolean getIsTexteRubriqueEntreprise() {
        return isTexteRubriqueEntreprise;
    }

    public void setIsTexteRubriqueEntreprise(Boolean isTexteRubriqueEntreprise) {
        this.isTexteRubriqueEntreprise = isTexteRubriqueEntreprise;
    }

    public List<Calendar> getDatesEffetRubriqueEntreprise() {
        return datesEffetRubriqueEntreprise;
    }

    public void setDatesEffetRubriqueEntreprise(List<Calendar> datesEffetRubriqueEntreprise) {
        this.datesEffetRubriqueEntreprise = datesEffetRubriqueEntreprise;
    }

    public List<String> getRubriques() {
        return rubriques;
    }

    public void setRubriques(List<String> rubriques) {
        this.rubriques = rubriques;
    }

    public List<String> getMotsCles() {
        return motsCles;
    }

    public void setMotsCles(List<String> motsCles) {
        this.motsCles = motsCles;
    }

    public List<String> getChampsLibres() {
        return champsLibres;
    }

    public void setChampsLibres(List<String> champsLibres) {
        this.champsLibres = champsLibres;
    }

    public Boolean getIsVisibleTexteEntreprise() {
        return isVisibleTexteEntreprise;
    }

    public void setIsVisibleTexteEntreprise(Boolean isVisibleTexteEntreprise) {
        this.isVisibleTexteEntreprise = isVisibleTexteEntreprise;
    }
}
