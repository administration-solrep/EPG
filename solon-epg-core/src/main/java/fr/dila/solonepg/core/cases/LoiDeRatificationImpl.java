package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * {@link LoiDeRatification} Implementation.
 *
 * @author asatre
 *
 */
public class LoiDeRatificationImpl extends TexteMaitreImpl implements LoiDeRatification {
    private static final long serialVersionUID = -4402027281133053902L;

    public LoiDeRatificationImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getId() {
        return document.getId();
    }

    @Override
    public String getTermeDepot() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TERME_DEPOT);
    }

    @Override
    public void setTermeDepot(String termeDepot) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TERME_DEPOT, termeDepot);
    }

    @Override
    public Calendar getDateLimiteDepot() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_LIMITE_DEPOT);
    }

    @Override
    public void setDateLimiteDepot(Calendar dateLimiteDepot) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_LIMITE_DEPOT, dateLimiteDepot);
    }

    @Override
    public Calendar getDateLimitePublication() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_LIMITE_PUBLICATION);
    }

    @Override
    public void setDateLimitePublication(Calendar dateLimitePublication) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.DATE_LIMITE_PUBLICATION,
            dateLimitePublication
        );
    }

    @Override
    public Calendar getDateSaisineCE() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SAISINE_CE);
    }

    @Override
    public void setDateSaisineCE(Calendar dateSaisineCE) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SAISINE_CE, dateSaisineCE);
    }

    @Override
    public Calendar getDateExamenCE() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_EXAMEN_CE);
    }

    @Override
    public void setDateExamenCE(Calendar dateExamenCE) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_EXAMEN_CE, dateExamenCE);
    }

    @Override
    public String getSectionCE() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.SECTION_CE);
    }

    @Override
    public void setSectionCE(String sectionCE) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.SECTION_CE, sectionCE);
    }

    @Override
    public Calendar getDateExamenCM() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_EXAMEN_CM);
    }

    @Override
    public void setDateExamenCM(Calendar dateExamenCM) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_EXAMEN_CM, dateExamenCM);
    }

    @Override
    public Boolean isRenvoiDecret() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.RENVOI_DECRET);
    }

    @Override
    public void setRenvoiDecret(Boolean renvoiDecret) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.RENVOI_DECRET, renvoiDecret);
    }

    @Override
    public String getChambreDepot() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CHAMBRE_DEPOT);
    }

    @Override
    public void setChambreDepot(String chambreDepot) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CHAMBRE_DEPOT, chambreDepot);
    }

    @Override
    public Calendar getDateDepot() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_DEPOT);
    }

    @Override
    public void setDateDepot(Calendar dateDepot) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_DEPOT, dateDepot);
    }

    @Override
    public String getNumeroDepot() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_DEPOT);
    }

    @Override
    public void setNumeroDepot(String numeroDepot) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_DEPOT, numeroDepot);
    }

    @Override
    public Boolean isDateSaisineCELocked() {
        return getBooleanProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.DATE_SAISINE_CE_LOCKED
        );
    }

    @Override
    public void setDateSaisineCELocked(Boolean dateSaisineCELocked) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.DATE_SAISINE_CE_LOCKED,
            dateSaisineCELocked
        );
    }

    @Override
    public Boolean isDateExamenCELocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_EXAMEN_CE_LOCKED);
    }

    @Override
    public void setDateExamenCELocked(Boolean dateExamenCELocked) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.DATE_EXAMEN_CE_LOCKED,
            dateExamenCELocked
        );
    }

    @Override
    public Boolean isSectionCELocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.SECTION_CE_LOCKED);
    }

    @Override
    public void setSectionCELocked(Boolean sectionCELocked) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.SECTION_CE_LOCKED, sectionCELocked);
    }

    @Override
    public Boolean isChambreDepotLocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CHAMBRE_DEPOT_LOCKED);
    }

    @Override
    public void setChambreDepotLocked(Boolean chambreDepotLocked) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.CHAMBRE_DEPOT_LOCKED,
            chambreDepotLocked
        );
    }

    @Override
    public Boolean isDateDepotLocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_DEPOT_LOCKED);
    }

    @Override
    public void setDateDepotLocked(Boolean dateDepotLocked) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_DEPOT_LOCKED, dateDepotLocked);
    }

    @Override
    public Boolean isNumeroDepotLocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_DEPOT_LOCKED);
    }

    @Override
    public void setNumeroDepotLocked(Boolean numeroDepotLocked) {
        setProperty(
            TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
            TexteMaitreConstants.NUMERO_DEPOT_LOCKED,
            numeroDepotLocked
        );
    }

    @Override
    public String getIdDossier() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ID_DOSSIER);
    }

    @Override
    public void setIdDossier(String idDossier) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ID_DOSSIER, idDossier);
    }
}
