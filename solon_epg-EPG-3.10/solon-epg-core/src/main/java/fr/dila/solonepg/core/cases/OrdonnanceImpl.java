package fr.dila.solonepg.core.cases;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;

/**
 * Implementation de {@link Ordonnance}
 * 
 * @author asatre
 * 
 */
public class OrdonnanceImpl extends TexteMaitreImpl implements Ordonnance {

    private static final long serialVersionUID = -3801345718539975375L;

    public OrdonnanceImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getConventionDepot() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CONVENTION_DEPOT);
    }

    @Override
    public void setConventionDepot(String convention) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CONVENTION_DEPOT, convention);
    }

    @Override
    public String getLegislature() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.LEGISLATURE);
    }

    @Override
    public void setLegislature(String legislature) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.LEGISLATURE, legislature);
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
    public Calendar getDatePassageCM() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PASSAGE_CM);
    }

    @Override
    public void setDatePassageCM(Calendar datePassageCM) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PASSAGE_CM, datePassageCM);
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
    public Boolean isDateSaisineCELocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SAISINE_CE_LOCKED);
    }

    @Override
    public void setDateSaisineCELocked(Boolean dateSaisineCELocked) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_SAISINE_CE_LOCKED, dateSaisineCELocked);
    }

    @Override
    public Boolean isDatePassageCMLocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PASSAGE_CM_LOCKED);
    }

    @Override
    public void setDatePassageCMLocked(Boolean datePassageCMLocked) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PASSAGE_CM_LOCKED, datePassageCMLocked);
    }

    @Override
    public Boolean isObjetLocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.OBJET_LOCKED);
    }

    @Override
    public void setObjetLocked(Boolean objetLocked) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.OBJET_LOCKED, objetLocked);
    }

    @Override
    public Boolean isMinisterePiloteLocked() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.MINISTERE_PILOTE_LOCKED);
    }

    @Override
    public void setMinisterePiloteLocked(Boolean ministerePiloteLocked) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.MINISTERE_PILOTE_LOCKED, ministerePiloteLocked);        
    }
    
    @Override
	public List<String> getDecretsIdsInvalidated() {
		return getListStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.IDS_INVALIDATED);
	}

	@Override
	public void setDecretsIdsInvalidated(List<String> decretsIdsNotValidated) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.IDS_INVALIDATED, decretsIdsNotValidated);
	}
}
