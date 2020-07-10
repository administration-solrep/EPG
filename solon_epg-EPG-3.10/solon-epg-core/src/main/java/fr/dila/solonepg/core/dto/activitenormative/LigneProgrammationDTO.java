package fr.dila.solonepg.core.dto.activitenormative;

import java.io.Serializable;
import java.util.Date;

import fr.dila.st.core.util.DateUtil;

/**
 * representation d'un decret dans le tableau de programmation
 * 
 * @author asatre
 * 
 */
public class LigneProgrammationDTO implements Serializable {

	private static final long	serialVersionUID	= -7165345773142018923L;

	private String				id;
	private String				numeroOrdre;
	private String				article;
	private String				baseLegale;
	private String				objet;
	private String				ministerePilote;
	private String				directionResponsable;
	private String				categorieTexte;
	private String				consultationObligatoireHCE;
	private String				calendrierConsultationObligatoireHCE;
	private Date				dateSaisineCE;
	private Date				objectifPublication;
	private String				observation;

	private Date				dateSortieCE;
	private String				typeMesure;
	private String				norDecret;
	private String				titreDecret;
	private Date				datePublicationDecret;
	private String				typeMesureId;

	private Date				datePrevisionnelleSaisineCE;
	private Date				dateMiseApplication;
	private String				typeTableau;

	public LigneProgrammationDTO() {

	}

	public String getNumeroOrdre() {
		return numeroOrdre;
	}

	public void setNumeroOrdre(String numeroOrdre) {
		this.numeroOrdre = numeroOrdre;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getBaseLegale() {
		return baseLegale;
	}

	public void setBaseLegale(String baseLegale) {
		this.baseLegale = baseLegale;
	}

	public String getObjet() {
		return objet;
	}

	public void setObjet(String objet) {
		this.objet = objet;
	}

	public String getMinisterePilote() {
		return ministerePilote;
	}

	public void setMinisterePilote(String ministerePilote) {
		this.ministerePilote = ministerePilote;
	}

	public String getDirectionResponsable() {
		return directionResponsable;
	}

	public void setDirectionResponsable(String directionResponsable) {
		this.directionResponsable = directionResponsable;
	}

	public String getCategorieTexte() {
		return categorieTexte;
	}

	public void setCategorieTexte(String categorieTexte) {
		this.categorieTexte = categorieTexte;
	}

	public String getConsultationObligatoireHCE() {
		return consultationObligatoireHCE;
	}

	public void setConsultationObligatoireHCE(String consultationObligatoireHCE) {
		this.consultationObligatoireHCE = consultationObligatoireHCE;
	}

	public String getCalendrierConsultationObligatoireHCE() {
		return calendrierConsultationObligatoireHCE;
	}

	public void setCalendrierConsultationObligatoireHCE(String calendrierConsultationObligatoireHCE) {
		this.calendrierConsultationObligatoireHCE = calendrierConsultationObligatoireHCE;
	}

	public Date getDateSaisineCE() {
		return dateSaisineCE;
	}

	public void setDateSaisineCE(Date dateSaisineCE) {
		this.dateSaisineCE = DateUtil.copyDate(dateSaisineCE);
	}

	public Date getObjectifPublication() {
		return DateUtil.copyDate(objectifPublication);
	}

	public void setObjectifPublication(Date objectifPublication) {
		this.objectifPublication = DateUtil.copyDate(objectifPublication);
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public Date getDateSortieCE() {
		return DateUtil.copyDate(dateSortieCE);
	}

	public void setDateSortieCE(Date dateSortieCE) {
		this.dateSortieCE = DateUtil.copyDate(dateSortieCE);
	}

	public String getTypeMesure() {
		return typeMesure;
	}

	public void setTypeMesure(String typeMesure) {
		this.typeMesure = typeMesure;
	}

	public String getNorDecret() {
		return norDecret;
	}

	public void setNorDecret(String norDecret) {
		this.norDecret = norDecret;
	}

	public String getTitreDecret() {
		return titreDecret;
	}

	public void setTitreDecret(String titreDecret) {
		this.titreDecret = titreDecret;
	}

	public Date getDatePublicationDecret() {
		return DateUtil.copyDate(datePublicationDecret);
	}

	public void setDatePublicationDecret(Date datePublicationDecret) {
		this.datePublicationDecret = DateUtil.copyDate(datePublicationDecret);
	}

	public void setTypeMesureId(String typeMesureId) {
		this.typeMesureId = typeMesureId;
	}

	public String getTypeMesureId() {
		return typeMesureId;
	}

	public void setDatePrevisionnelleSaisineCE(Date datePrevisionnelleSaisineCE) {
		this.datePrevisionnelleSaisineCE = datePrevisionnelleSaisineCE;
	}

	public Date getDatePrevisionnelleSaisineCE() {
		return DateUtil.copyDate(datePrevisionnelleSaisineCE);
	}

	public void setDateMiseApplication(Date dateMiseApplication) {
		this.dateMiseApplication = DateUtil.copyDate(dateMiseApplication);
	}

	public Date getDateMiseApplication() {
		return DateUtil.copyDate(dateMiseApplication);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the typeTableau
	 */
	public String getTypeTableau() {
		return typeTableau;
	}

	/**
	 * @param typeTableau
	 *            the typeTableau to set
	 */
	public void setTypeTableau(String typeTableau) {
		this.typeTableau = typeTableau;
	}

}
