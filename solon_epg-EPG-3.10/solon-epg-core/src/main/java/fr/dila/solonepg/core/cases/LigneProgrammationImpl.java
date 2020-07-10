package fr.dila.solonepg.core.cases;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.LigneProgrammation;
import fr.dila.solonepg.api.constant.LigneProgrammationConstants;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.st.core.domain.STDomainObjectImpl;

/**
 * {@link LigneProgrammation} Implementation.
 * 
 * @author asatre
 * 
 */
public class LigneProgrammationImpl extends STDomainObjectImpl implements LigneProgrammation {

	private static final long	serialVersionUID	= -7026027771349205057L;
	private Calendar			dateMiseApplication;

	// private DocumentModel document;

	public LigneProgrammationImpl(DocumentModel doc) {
		// this.document = doc;
		super(doc);
	}

	@Override
	public String getNumeroOrdre() {
		return PropertyUtil.getStringProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.NUMERO_ORDRE);
	}

	@Override
	public void setNumeroOrdre(String numeroOrdre) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.NUMERO_ORDRE, numeroOrdre);
	}

	@Override
	public String getArticleLoi() {
		return PropertyUtil.getStringProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.ARTICLE_LOI);
	}

	@Override
	public void setArticleLoi(String articleLoi) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.ARTICLE_LOI, articleLoi);
	}

	@Override
	public String getBaseLegale() {
		return PropertyUtil.getStringProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.BASE_LOI);
	}

	@Override
	public void setBaseLegale(String baseLegale) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.BASE_LOI, baseLegale);
	}

	@Override
	public String getObjet() {
		return PropertyUtil.getStringProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.OBJET);
	}

	@Override
	public void setObjet(String objet) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.OBJET, objet);
	}

	@Override
	public String getMinisterePilote() {
		return PropertyUtil.getStringProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.MINISTERE_PILOTE);
	}

	@Override
	public void setMinisterePilote(String ministerePilote) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.MINISTERE_PILOTE, ministerePilote);
	}

	@Override
	public String getDirectionResponsable() {
		return PropertyUtil.getStringProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.DIRECTION_RESPONSABLE);
	}

	@Override
	public void setDirectionResponsable(String directionResponsable) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.DIRECTION_RESPONSABLE, directionResponsable);
	}

	@Override
	public String getCategorieTexte() {
		return PropertyUtil.getStringProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.CATEGORIE_TEXTE);
	}

	@Override
	public void setCategorieTexte(String categorieTexte) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.CATEGORIE_TEXTE, categorieTexte);
	}

	@Override
	public String getConsultationObligatoireHCE() {
		return PropertyUtil.getStringProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.CONSULTATION_OBLIGATOIRE_HCE);
	}

	@Override
	public void setConsultationObligatoireHCE(String consultationObligatoireHCE) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.CONSULTATION_OBLIGATOIRE_HCE, consultationObligatoireHCE);
	}

	@Override
	public String getCalendrierConsultationHCE() {
		return PropertyUtil.getStringProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.CALENDRIER_CONSULTATION_OBLIGATOIRE_HCE);
	}

	@Override
	public void setCalendrierConsultationObligatoireHCE(String calendrierConsultationObligatoireHCE) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.CALENDRIER_CONSULTATION_OBLIGATOIRE_HCE,
				calendrierConsultationObligatoireHCE);
	}

	@Override
	public Calendar getDateSaisineCE() {
		return PropertyUtil.getCalendarProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.DATE_SAISINE_CE);
	}

	@Override
	public void setDateSaisineCE(Calendar dateSaisineCE) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.DATE_SAISINE_CE, dateSaisineCE);
	}

	@Override
	public Calendar getObjectifPublication() {
		return PropertyUtil.getCalendarProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.OBJECTIF_PUBLICATION);
	}

	@Override
	public void setObjectifPublication(Calendar objectifPublication) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.OBJECTIF_PUBLICATION, objectifPublication);
	}

	@Override
	public String getObservation() {
		return PropertyUtil.getStringProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.OBSERVATION);
	}

	@Override
	public void setObservation(String observation) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.OBSERVATION, observation);
	}

	@Override
	public void setTypeMesureId(String typeMesureId) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.TYPE_MESURE, typeMesureId);
	}

	@Override
	public String getTypeMesureId() {
		return PropertyUtil.getStringProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.OBSERVATION);
	}

	@Override
	public Calendar getDatePrevisionnelleSaisineCE() {
		return PropertyUtil.getCalendarProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.DATE_PREVISIONELLE_SAISINE_CE);
	}

	@Override
	public void setDatePrevisionnelleSaisineCE(Calendar datePrevisionnelleSaisineCE) {
		PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.DATE_PREVISIONELLE_SAISINE_CE, datePrevisionnelleSaisineCE);
	}

	@Override
	public Calendar getDateMiseApplication() {
		return dateMiseApplication;
	}

	@Override
	public void setDateMiseApplication(Calendar dateMiseApplication) {
		this.dateMiseApplication = dateMiseApplication;
	}

	@Override
	public void setTypeTableau(String typeTableau) {
		if (LigneProgrammationConstants.TYPE_TABLEAU_VALUE_PROGRAMMATION.equals(typeTableau)
				|| LigneProgrammationConstants.TYPE_TABLEAU_VALUE_SUIVI.equals(typeTableau)) {
			PropertyUtil.setProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
					LigneProgrammationConstants.TYPE_TABLEAU, typeTableau);
		}

	}

	@Override
	public String getTypeTableau() {
		return PropertyUtil.getStringProperty(document, LigneProgrammationConstants.LIGNE_PROGRAMMATION_SCHEMA,
				LigneProgrammationConstants.TYPE_TABLEAU);
	}
}
