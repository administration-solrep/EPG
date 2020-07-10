package fr.dila.solonepg.core.cases;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.core.util.PropertyUtil;

/**
 * {@link MesureApplicative} Implementation.
 * 
 * @author asatre
 * 
 */
public class MesureApplicativeImpl extends TexteMaitreImpl implements MesureApplicative {

    private static final long serialVersionUID = -4402027281133053902L;

    public MesureApplicativeImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getArticle() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ARTICLE);
    }

    @Override
    public void setArticle(String article) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ARTICLE, article);
    }

    @Override
    public String getCodeModifie() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CODE_MODIFIE);
    }

    @Override
    public void setCodeModifie(String codeModifie) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CODE_MODIFIE, codeModifie);
    }

    @Override
    public String getBaseLegale() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.BASE_LEGALE);
    }

    @Override
    public void setBaseLegale(String baseLegale) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.BASE_LEGALE, baseLegale);
    }

    @Override
    public String getObjetRIM() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.OBJET_RIM);
    }

    @Override
    public void setObjetRIM(String objetRIM) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.OBJET_RIM, objetRIM);
    }

    @Override
    public String getTypeMesure() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TYPE_MESURE);
    }

    @Override
    public void setTypeMesure(String typeMesure) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TYPE_MESURE, typeMesure);
    }

    @Override
    public Calendar getDateObjectifPublication() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_OBJECTIF_PUBLICATION);
    }

    @Override
    public void setDateObjectifPublication(Calendar dateObjectifPublication) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_OBJECTIF_PUBLICATION, dateObjectifPublication);
    }

    @Override
    public String getDirectionResponsable() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DIRECTION_RESPONSABLE);
    }

    @Override
    public void setDirectionResponsable(String directionResponsable) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DIRECTION_RESPONSABLE, directionResponsable);
    }

    @Override
    public Boolean getApplicationRecu() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.APPLICATION_RECU);
    }

    @Override
    public void setApplicationRecu(Boolean applicationRecu) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.APPLICATION_RECU, applicationRecu);
    }

    @Override
    public String getConsultationsHCE() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CONSULTATIONS_HCE);
    }

    @Override
    public void setConsultationsHCE(String consultationsHCE) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CONSULTATIONS_HCE, consultationsHCE);
    }

    @Override
    public String getCalendrierConsultationsHCE() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CALENDRIER_CONSULTATIONS_HCE);
    }

    @Override
    public void setCalendrierConsultationsHCE(String calendrierConsultationsHCE) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CALENDRIER_CONSULTATIONS_HCE, calendrierConsultationsHCE);
    }

    @Override
    public Calendar getDateDisponnibiliteAvantProjet() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_DISPONIBILITE_AVANT_PROJET);
    }

    @Override
    public void setDateDisponnibiliteAvantProjet(Calendar dateDisponibiliteAvantProjet) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_DISPONIBILITE_AVANT_PROJET, dateDisponibiliteAvantProjet);
    }

    @Override
    public Calendar getDatePrevisionnelleSaisineCE() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PREVISIONNELLE_SAISINE_CE);
    }

    @Override
    public void setDatePrevisionnelleSaisineCE(Calendar  datePrevisionnelleSaisineCE) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PREVISIONNELLE_SAISINE_CE, datePrevisionnelleSaisineCE);
    }

    @Override
    public String getPoleChargeMission() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.POLE_CHARGE_MISSION);
    }

    @Override
    public void setPoleChargeMission(String poleChargeMission) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.POLE_CHARGE_MISSION, poleChargeMission);
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
    public Boolean getApplicationLoi() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.APPLICATION_LOI);
    }

    @Override
    public void setApplicationLoi(Boolean applicationLoi) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.APPLICATION_LOI, applicationLoi);
    }

    @Override
    public Boolean isFromAmendement() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.FROM_AMENDEMENT);
    }

    @Override
    public void setFromAmendement(Boolean fromAmendement) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.FROM_AMENDEMENT, fromAmendement);
    }

    @Override
    public Boolean isDiffere() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DIFFERE);
    }

    @Override
    public void setDiffere(Boolean differe) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DIFFERE, differe);
    }

    @Override
    public String getNumeroQuestion() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_QUESTION);
    }

    @Override
    public void setNumeroQuestion(String numeroQuestion) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_QUESTION, numeroQuestion);
    }

    @Override
    public Boolean isAmendement() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.AMENDEMENT);
    }

    @Override
    public void setAmendement(Boolean amendement) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.AMENDEMENT, amendement);
    }

    @Override
    public String getResponsableAmendement() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.RESPONSABLE_AMENDEMENT);
    }

    @Override
    public void setResponsableAmendement(String responsableAmendement) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.RESPONSABLE_AMENDEMENT, responsableAmendement);
    }

    @Override
    public List<String> getDecretIds() {
        return getListStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DECRET_IDS);
    }

    @Override
    public void setDecretIds(List<String> decretIds) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DECRET_IDS, decretIds);
    }
    
    @Override
    public String toString() {
        return "[MesureApplicative : Id :" + getId() + ", NumeroOrdre : " + getNumeroOrdre() + "]";
    }
    
    @Override
    public Boolean getMesureApplication() {
        return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.MESURE_APPLICATION);
    }

    @Override
    public void setMesureApplication(Boolean mesureApplication) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.MESURE_APPLICATION, mesureApplication);
    }

    @Override
    public Calendar getDateCirculationCompteRendu() {
        return PropertyUtil.getCalendarProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_CIRCULATION_COMPTE_RENDU);
    }

    @Override
    public void setDateCirculationCompteRendu(Calendar dateCirculationCompteRendu) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_CIRCULATION_COMPTE_RENDU, dateCirculationCompteRendu);
    }
    
    @Override
    public Calendar getDateMiseApplication() {
        return PropertyUtil.getCalendarProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_MISE_APPLICATION);
    }

    @Override
    public void setDateMiseApplication(Calendar dateMiseApplication) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_MISE_APPLICATION, dateMiseApplication);
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
