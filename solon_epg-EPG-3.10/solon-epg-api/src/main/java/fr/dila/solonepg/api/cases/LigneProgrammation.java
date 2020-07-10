package fr.dila.solonepg.api.cases;

import java.io.Serializable;
import java.util.Calendar;

import fr.dila.st.api.domain.STDomainObject;

/**
 * type complexe representant une ligne dans le tableau de programmation de l'activite normative
 * 
 * @author asatre
 * 
 */
public interface LigneProgrammation extends Serializable, STDomainObject {

	String getNumeroOrdre();

	void setNumeroOrdre(String numeroOrdre);

	String getArticleLoi();

	void setArticleLoi(String articleLoi);

	String getBaseLegale();

	void setBaseLegale(String baseLegale);

	String getObjet();

	void setObjet(String objet);

	String getMinisterePilote();

	void setMinisterePilote(String ministerePilote);

	String getDirectionResponsable();

	void setDirectionResponsable(String directionResponsable);

	String getCategorieTexte();

	void setCategorieTexte(String categorieTexte);

	String getConsultationObligatoireHCE();

	void setConsultationObligatoireHCE(String consultationObligatoireHCE);

	String getCalendrierConsultationHCE();

	void setCalendrierConsultationObligatoireHCE(String calendrierConsultationObligatoireHCE);

	Calendar getDateSaisineCE();

	void setDateSaisineCE(Calendar dateSaisineCE);

	Calendar getObjectifPublication();

	void setObjectifPublication(Calendar objectifPublication);

	String getObservation();

	void setObservation(String observation);

	void setTypeMesureId(String typeMesureId);

	String getTypeMesureId();

	void setDatePrevisionnelleSaisineCE(Calendar datePrevisionnelleSaisineCE);

	Calendar getDatePrevisionnelleSaisineCE();

	void setDateMiseApplication(Calendar dateDateMiseApplication);

	Calendar getDateMiseApplication();

	void setTypeTableau(String typeTableau);

	String getTypeTableau();
}
