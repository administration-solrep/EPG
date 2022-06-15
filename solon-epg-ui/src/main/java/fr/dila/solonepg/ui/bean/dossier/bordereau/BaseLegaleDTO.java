package fr.dila.solonepg.ui.bean.dossier.bordereau;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.st.ui.annot.NxProp;
import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;

public class BaseLegaleDTO {

    public BaseLegaleDTO() {
        super();
    }

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_ACTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String baseLegale;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_NATURE_TEXTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String natureTexte;

    private String natureTexteLibelle;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_NUMERO_TEXTE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String numeroTexte;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_DATE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private Calendar dateBaseLegale;

    private Boolean isVisibleBaseLegale = Boolean.FALSE;

    public String getBaseLegale() {
        return baseLegale;
    }

    public void setBaseLegale(String baseLegale) {
        this.baseLegale = baseLegale;
    }

    public String getNatureTexte() {
        return natureTexte;
    }

    public void setNatureTexte(String natureTexte) {
        this.natureTexte = natureTexte;
    }

    public String getNatureTexteLibelle() {
        return natureTexteLibelle;
    }

    public void setNatureTexteLibelle(String natureTexteLibelle) {
        this.natureTexteLibelle = natureTexteLibelle;
    }

    public String getNumeroTexte() {
        return numeroTexte;
    }

    public void setNumeroTexte(String numeroTexte) {
        this.numeroTexte = numeroTexte;
    }

    public Calendar getDateBaseLegale() {
        return dateBaseLegale;
    }

    public void setDateBaseLegale(Calendar dateBaseLegale) {
        this.dateBaseLegale = dateBaseLegale;
    }

    public Boolean getIsVisibleBaseLegale() {
        return isVisibleBaseLegale;
    }

    public void setIsVisibleBaseLegale(Boolean isVisibleBaseLegale) {
        this.isVisibleBaseLegale = isVisibleBaseLegale;
    }

    public boolean isEmpty() {
        return (
            StringUtils.isAllBlank(this.baseLegale, this.natureTexte, this.natureTexteLibelle, this.numeroTexte) &&
            dateBaseLegale == null
        );
    }
}
