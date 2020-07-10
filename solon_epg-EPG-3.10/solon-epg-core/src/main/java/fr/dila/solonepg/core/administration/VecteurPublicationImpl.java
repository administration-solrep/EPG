/**
 * 
 */
package fr.dila.solonepg.core.administration;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.st.core.domain.STDomainObjectImpl;

/**
 * @author ahullot
 *
 */
public class VecteurPublicationImpl extends STDomainObjectImpl implements VecteurPublication {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VecteurPublicationImpl(DocumentModel document) {
		super(document);
	}
	
	/**
	 * Permet de retourner le nom du vecteur de publication
	 */
	@Override
	public String getIntitule() {
		
		return getStringProperty(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA, SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_INTITULE);
	}

	/**
	 * Permet d'instancier le nom du vecteur de publication
	 */
	@Override
	public void setIntitule(String intitule) {
		setProperty(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA, SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_INTITULE, intitule);
	}

	/**
	 * Permet de retourner le date de début du vecteur de publication
	 */
	@Override
	public Calendar getDateDebut() {
		
		return getDateProperty(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA, SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DEBUT);
	}

	/**
	 * Permet d'instancier le date de début du vecteur de publication
	 */
	@Override
	public void setDateDebut(Calendar debut) {
		setProperty(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA, SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_DEBUT, debut);

	}

	/**
	 * Permet de retourner le date de fin du vecteur de publication
	 */
	@Override
	public Calendar getDateFin() {
		return getDateProperty(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA, SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_FIN);
	}

	/**
	 * Permet d'instancier le date de fin du vecteur de publication
	 */
	@Override
	public void setDateFin(Calendar fin) {
		setProperty(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA, SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_FIN, fin);

	}
	

}
