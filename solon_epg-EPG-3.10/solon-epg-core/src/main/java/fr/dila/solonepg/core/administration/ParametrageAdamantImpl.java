package fr.dila.solonepg.core.administration;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.administration.ParametrageAdamant;
import fr.dila.solonepg.api.constant.SolonEpgParametrageAdamantConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.PropertyUtil;

/**
 * Parametrage Application Implémentation.
 * 
 * @author arolin
 */
public class ParametrageAdamantImpl extends STDomainObjectImpl implements ParametrageAdamant {

	private static final long serialVersionUID = 1L;

	private static final STLogger	LOGGER				= STLogFactory.getLog(ParametrageAdamantImpl.class);

	/**
	 * @param doc
	 */
	public ParametrageAdamantImpl(DocumentModel doc) {
		super(doc);
	}

	//*************************************************************
	// Profil Utilisateur favoris de l'utilisateurs property
	//*************************************************************

	@Override
	public Long getNumeroSolon() {
		return PropertyUtil.getLongProperty(document, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_NUMERO_SOLON_PROPERTY);
	}

	@Override
	public void setNumeroSolon(Integer numeroSolon) {
		setProperty(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_NUMERO_SOLON_PROPERTY, numeroSolon);
	}

	@Override
	public String getOriginatingAgencyBlocIdentifier() {
		return PropertyUtil.getStringProperty(document, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_ORIGINATING_AGENCY_BLOC_IDENTIFIER_PROPERTY);
	}

	@Override
	public void setOriginatingAgencyBlocIdentifier(String originatingAgencyBlocIdentifier) {
		setProperty(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_ORIGINATING_AGENCY_BLOC_IDENTIFIER_PROPERTY, originatingAgencyBlocIdentifier);
	}

	@Override
	public String getSubmissionAgencyBlocIdentifier() {
		return PropertyUtil.getStringProperty(document, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SUBMISSION_AGENCY_BLOC_IDENTIFIER_PROPERTY);
	}

	@Override
	public void setSubmissionAgencyBlocIdentifier(String submissionAgencyBlocIdentifier) {
		setProperty(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SUBMISSION_AGENCY_BLOC_IDENTIFIER_PROPERTY, submissionAgencyBlocIdentifier);
	}

	@Override
	public String getArchivalProfile() {
		return PropertyUtil.getStringProperty(document, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_ARCHIVAL_PROFILE_PROPERTY);
	}

	@Override
	public void setArchivalProfile(String archivalProfile) {
		setProperty(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_ARCHIVAL_PROFILE_PROPERTY, archivalProfile);
	}

	@Override
	public String getOriginatingAgencyIdentifier() {
		return PropertyUtil.getStringProperty(document, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_ORIGINATING_AGENCY_IDENTIFIER_PROPERTY);
	}

	@Override
	public void setOriginatingAgencyIdentifier(String originatingAgencyIdentifier) {
		setProperty(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_ORIGINATING_AGENCY_IDENTIFIER_PROPERTY, originatingAgencyIdentifier);
	}

	@Override
	public String getSubmissionAgencyIdentifier() {
		return PropertyUtil.getStringProperty(document, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SUBMISSION_AGENCY_IDENTIFIER_PROPERTY);
	}

	@Override
	public void setSubmissionAgencyIdentifier(String submissionAgencyIdentifier) {
		setProperty(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SUBMISSION_AGENCY_IDENTIFIER_PROPERTY, submissionAgencyIdentifier);
	}

	@Override
	public Long getDelaiEligibilite() {
		return PropertyUtil.getLongProperty(document, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DELAI_ELIGIBILITE_PROPERTY);
	}

	@Override
	public void setDelaiEligibilite(Integer delaiEligibilite) {
		setProperty(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DELAI_ELIGIBILITE_PROPERTY, delaiEligibilite);
	}

	@Override
	public Long getNbDossierExtraction() {
		return PropertyUtil.getLongProperty(document, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_NB_DOSSIER_EXTRACTION_PROPERTY);
	}

	@Override
	public void setNbDossierExtraction(Integer nbDossierExtraction) {
		setProperty(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA, SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_NB_DOSSIER_EXTRACTION_PROPERTY, nbDossierExtraction);
	}

}