package fr.dila.solonepg.core.cases;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DataModel;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.core.schema.SchemaManager;
import org.nuxeo.ecm.core.schema.TypeProvider;
import org.nuxeo.runtime.api.Framework;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.fonddossier.FondDeDossierInstance;
import fr.dila.solonepg.api.parapheur.ParapheurInstance;
import fr.dila.solonepg.api.service.FeuilleRouteService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.util.ComplexTypeUtil;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.core.dossier.STDossierImpl;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.util.UnrestrictedGetOrSetPropertyDocumentRunner;

/**
 * Implémentation du dossier SOLON EPG.
 * 
 * @author jtremeaux
 */
public class DossierImpl extends STDossierImpl implements Dossier {

	private static final long	serialVersionUID	= 6160682333116646611L;

	/**
	 * Constructeur de DossierImpl.
	 * 
	 * @param dossierDoc
	 *            Document dossier
	 */
	public DossierImpl(DocumentModel dossierDoc) {
		super(dossierDoc);
	}

	@Override
	public String getLastDocumentRoute() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				STSchemaConstant.DOSSIER_LAST_DOCUMENT_ROUTE_PROPERTY);
	}

	@Override
	public void setLastDocumentRoute(String lastDocumentRoute) {
		PropertyUtil.setProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				STSchemaConstant.DOSSIER_LAST_DOCUMENT_ROUTE_PROPERTY, lastDocumentRoute);
	}

	@Override
	public FondDeDossierInstance getFondDeDossier(CoreSession session) {
		try {
			String fddid = getDocumentFddId();
			DocumentModel docModel = session.getDocument(new IdRef(fddid));
			return docModel.getAdapter(FondDeDossierInstance.class);
		} catch (ClientException e) {
			throw new CaseManagementRuntimeException(e);
		}
	}

	@Override
	public ParapheurInstance getParapheur(CoreSession session) {
		try {
			String parapheurId = getDocumentParapheurId();
			DocumentModel docModel = session.getDocument(new IdRef(parapheurId));
			return docModel.getAdapter(ParapheurInstance.class);
		} catch (ClientException e) {
			throw new CaseManagementRuntimeException(e);
		}
	}

	// /////////////////
	// get Property helper
	// ////////////////

	protected List<String> getListStringProperty(String schema, String value) {
		return PropertyUtil.getStringListProperty(document, schema, value);
	}

	protected void setProperty(String schema, String property, Object value) {
		PropertyUtil.setProperty(document, schema, property, value);
	}

	protected List<ComplexeType> getTranspositionProperty(String schema, String value) {
		return PropertyUtil.getListSerializableProperty(document, schema, value);
	}

	// /////////////////
	// Dossier method
	// ////////////////

	@Override
	public DocumentModel getDossierMetadata(Dossier dossier) throws ClientException {
		copySchema(dossier.getDocument(), document, DossierSolonEpgConstants.DOSSIER_SCHEMA);

		return document;
	}

	/**
	 * Copie tous les propriété du schéma d'un documentModel donné dans un autre documentModel
	 * 
	 * @param src
	 * @param dst
	 * @param schema
	 * @throws ClientException
	 */
	private void copySchema(DocumentModel src, DocumentModel dst, String schema) throws ClientException {
		DataModel dataModelSrc = src.getDataModel(schema);
		TypeProvider typeProvider = Framework.getLocalService(SchemaManager.class);
		DataModel newDataModel = DocumentModelImpl.cloneDataModel(typeProvider.getSchema(dataModelSrc.getSchema()),
				dataModelSrc);
		dst.setProperties(schema, newDataModel.getMap());
	}

	// /////////////////
	// getter/setter implementation
	// ////////////////

	@Override
	public String getDocumentParapheurId() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DOCUMENT_PARAPHEUR_ID);
	}

	@Override
	public void setDocumentParapheurId(String documentParapheurId) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_DOCUMENT_PARAPHEUR_ID,
				documentParapheurId);
	}

	@Override
	public String getDocumentFddId() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DOCUMENT_FOND_DOSSIER_ID);
	}

	@Override
	public void setDocumentFddId(String documentFddId) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_DOCUMENT_FOND_DOSSIER_ID,
				documentFddId);
	}

	// /////////////////
	// getter/setter Dossier
	// ////////////////

	@Override
	public String getStatut() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY);
	}

	@Override
	public void setStatut(String statut) {
		// dénormalisation
		setPublie(VocabularyConstants.STATUT_PUBLIE.equals(statut));
		setNorAttribue(VocabularyConstants.STATUT_NOR_ATTRIBUE.equals(statut));

		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY, statut);
	}

	@Override
	public String getNumeroNor() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY);
	}

	@Override
	public void setNumeroNor(String numeroNor) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY,
				numeroNor);
	}

	@Override
	public Long getNbDossierRectifie() {
		return PropertyUtil.getLongProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_NB_DOSSIER_RECTIFIE_PROPERTY);
	}

	@Override
	public void setNbDossierRectifie(Long nbDossierRectifie) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_NB_DOSSIER_RECTIFIE_PROPERTY, nbDossierRectifie);
	}

	@Override
	public String getTypeActe() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY);
	}

	@Override
	public void setTypeActe(String typeActe) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY,
				typeActe);

	}

	@Override
	public String getTitreActe() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_PROPERTY);
	}

	@Override
	public void setTitreActe(String titreActe) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_PROPERTY,
				titreActe);

	}

	@Override
	public String getMinistereResp() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_MINISTERE_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setMinistereResp(String ministereResp) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_MINISTERE_RESPONSABLE_PROPERTY, ministereResp);
	}

	@Override
	public String getDirectionResp() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DIRECTION_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setDirectionResp(String ministereResp) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DIRECTION_RESPONSABLE_PROPERTY, ministereResp);
	}

	@Override
	public String getMinistereAttache() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_PROPERTY);
	}

	@Override
	public void setMinistereAttache(String ministereAttache) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_PROPERTY, ministereAttache);
	}

	@Override
	public String getDirectionAttache() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_PROPERTY);
	}

	@Override
	public void setIsParapheurComplet(Boolean isParapheurComplet) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_IS_PARAPHEUR_COMPLET_PROPERTY, isParapheurComplet);
	}

	@Override
	public Boolean getIsParapheurComplet() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_IS_PARAPHEUR_COMPLET_PROPERTY);
	}

	@Override
	public Boolean getIsParapheurFichierInfoNonRecupere() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_IS_PARAPHEUR_FICHIER_INFO_NON_RECUPERE_PROPERTY);
	}

	@Override
	public void setIsParapheurFichierInfoNonRecupere(Boolean isParapheurFichierInfoNonRecupere) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_IS_PARAPHEUR_FICHIER_INFO_NON_RECUPERE_PROPERTY,
				isParapheurFichierInfoNonRecupere);
	}

	@Override
	public Boolean getIsParapheurTypeActeUpdated() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_IS_PARAPHEUR_TYPE_ACTE_UPDATED_PROPERTY);
	}

	@Override
	public void setIsParapheurTypeActeUpdated(Boolean isParapheurTypeActeUpdated) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_IS_PARAPHEUR_TYPE_ACTE_UPDATED_PROPERTY, isParapheurTypeActeUpdated);
	}

	@Override
	public boolean isMesureNominative() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_MESURE_NOMINATIVE_PROPERTY);
	}

	@Override
	public void setMesureNominative(boolean mesureNominative) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_MESURE_NOMINATIVE_PROPERTY, mesureNominative);
	}

	@Override
	public Long getNumeroVersionActeOuExtrait() {
		return PropertyUtil.getLongProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_NUMERO_VERSION_ACTE_OU_EXTRAIT_PROPERTY);
	}

	@Override
	public void setNumeroVersionActeOuExtrait(Long numeroVersionActeOuExtrait) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_NUMERO_VERSION_ACTE_OU_EXTRAIT_PROPERTY, numeroVersionActeOuExtrait);
	}

	@Override
	public Boolean getIsActeReferenceForNumeroVersion() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_IS_ACTE_REFERENCE_FOR_NUMERO_VERSION_PROPERTY);
	}

	@Override
	public void setIsActeReferenceForNumeroVersion(Boolean isActeReferenceForNumeroVersion) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_IS_ACTE_REFERENCE_FOR_NUMERO_VERSION_PROPERTY,
				isActeReferenceForNumeroVersion);
	}

	@Override
	public void setDirectionAttache(String ministereAttache) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_PROPERTY, ministereAttache);
	}

	@Override
	public String getNomRespDossier() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_NOM_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setNomRespDossier(String nomRespDossier) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_NOM_RESPONSABLE_PROPERTY,
				nomRespDossier);
	}

	@Override
	public String getPrenomRespDossier() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_PRENOM_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setPrenomRespDossier(String prenomRespDossier) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_PRENOM_RESPONSABLE_PROPERTY, prenomRespDossier);
	}

	@Override
	public String getQualiteRespDossier() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_QUALITE_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setQualiteRespDossier(String qualite) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_QUALITE_RESPONSABLE_PROPERTY, qualite);
	}

	@Override
	public String getTelephoneRespDossier() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_TEL_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setTelephoneRespDossier(String telephoneRespDossier) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_TEL_RESPONSABLE_PROPERTY,
				telephoneRespDossier);
	}

	@Override
	public String getMailRespDossier() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_MAIL_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setMailRespDossier(String mailRespDossier) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_MAIL_RESPONSABLE_PROPERTY, mailRespDossier);
	}

	@Override
	public String getNomCompletRespDossier() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_NOM_COMPLET_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setNomCompletRespDossier(String nomCompletRespDossier) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_NOM_COMPLET_RESPONSABLE_PROPERTY, nomCompletRespDossier);
	}

	@Override
	public String getIdCreateurDossier() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_ID_CREATEUR_PROPERTY);
	}

	@Override
	public void setIdCreateurDossier(String idCreateurDossier) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_ID_CREATEUR_PROPERTY,
				idCreateurDossier);
	}

	@Override
	public String getCategorieActe() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_CATEGORIE_ACTE_PROPERTY);
	}

	@Override
	public void setCategorieActe(String categorieActe) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_CATEGORIE_ACTE_PROPERTY,
				categorieActe);
	}

	@Override
	public String getBaseLegaleActe() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_ACTE_PROPERTY);
	}

	@Override
	public void setBaseLegaleActe(String baseLegaleActe) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_ACTE_PROPERTY, baseLegaleActe);
	}

	@Override
	public String getBaseLegaleNatureTexte() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_NATURE_TEXTE_PROPERTY);
	}

	@Override
	public void setBaseLegaleNatureTexte(String baseLegaleNatureTexte) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_NATURE_TEXTE_PROPERTY, baseLegaleNatureTexte);
	}

	@Override
	public String getBaseLegaleNumeroTexte() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_NUMERO_TEXTE_PROPERTY);
	}

	@Override
	public void setBaseLegaleNumeroTexte(String baseLegaleNumeroTexte) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_NUMERO_TEXTE_PROPERTY, baseLegaleNumeroTexte);
	}

	@Override
	public Calendar getBaseLegaleDate() {
		return PropertyUtil.getCalendarProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_DATE_PROPERTY);
	}

	@Override
	public void setBaseLegaleDate(Calendar baseLegaleDate) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_DATE_PROPERTY, baseLegaleDate);
	}

	@Override
	public Calendar getDatePublication() {
		return PropertyUtil.getCalendarProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_PUBLICATION_PROPERTY);
	}

	@Override
	public void setDatePublication(Calendar datePublication) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_PUBLICATION_PROPERTY, datePublication);
	}

	@Override
	public Boolean getPublicationRapportPresentation() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_PUBLICATION_RAPPORT_PRESENTATION_PROPERTY);
	}

	@Override
	public void setPublicationRapportPresentation(Boolean publicationRapportPresentation) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_PUBLICATION_RAPPORT_PRESENTATION_PROPERTY,
				publicationRapportPresentation);
	}

	@Override
	public List<String> getNomCompletChargesMissions() {
		return getListStringProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_NOM_COMPLET_CHARGES_MISSIONS_PROPERTY);
	}

	@Override
	public void setNomCompletChargesMissions(List<String> chargeMissionIds) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_NOM_COMPLET_CHARGES_MISSIONS_PROPERTY, chargeMissionIds);
	}

	@Override
	public List<String> getNomCompletConseillersPms() {
		return getListStringProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_NOM_COMPLET_CONSEILLERS_PMS_PROPERTY);
	}

	@Override
	public void setNomCompletConseillersPms(List<String> conseillerPmIds) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_NOM_COMPLET_CONSEILLERS_PMS_PROPERTY, conseillerPmIds);
	}

	@Override
	public Calendar getDateSignature() {
		return PropertyUtil.getCalendarProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_SIGNATURE_PROPERTY);
	}

	@Override
	public void setDateSignature(Calendar dateSignature) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_DATE_SIGNATURE_PROPERTY,
				dateSignature);
	}

	@Override
	public Calendar getDatePourFournitureEpreuve() {
		return PropertyUtil.getCalendarProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_POUR_FOURNITURE_EPREUVE_PROPERTY);
	}

	@Override
	public void setDatePourFournitureEpreuve(Calendar pourFournitureEpreuve) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_POUR_FOURNITURE_EPREUVE_PROPERTY, pourFournitureEpreuve);
	}

	@Override
	public List<String> getVecteurPublication() {
		return getListStringProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_VECTEUR_PUBLICATION_PROPERTY);
	}

	@Override
	public void setVecteurPublication(List<String> vecteurPublication) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_VECTEUR_PUBLICATION_PROPERTY, vecteurPublication);
	}

	@Override
	public List<String> getPublicationsConjointes() {
		return getListStringProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_PUBLICATION_CONJOINTE_PROPERTY);
	}

	@Override
	public void setPublicationsConjointes(List<String> publicationsConjointes) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_PUBLICATION_CONJOINTE_PROPERTY, publicationsConjointes);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPublicationsConjointesUnrestricted(CoreSession session) {
		List<String> publicationsConjointes = null;
		try {
			publicationsConjointes = (List<String>) new UnrestrictedGetOrSetPropertyDocumentRunner(session, document)
					.getProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
							DossierSolonEpgConstants.DOSSIER_PUBLICATION_CONJOINTE_PROPERTY);
		} catch (ClientException e) {
			throw new RuntimeException("Erreur lors de de la récupération des publicationsConjointes", e);
		}
		return publicationsConjointes;
	}

	@Override
	public void setPublicationsConjointesUnrestricted(CoreSession session, List<String> publicationsConjointes) {
		try {
			new UnrestrictedGetOrSetPropertyDocumentRunner(session, document).setProperty(
					DossierSolonEpgConstants.DOSSIER_SCHEMA,
					DossierSolonEpgConstants.DOSSIER_PUBLICATION_CONJOINTE_PROPERTY, publicationsConjointes);
		} catch (ClientException e) {
			throw new RuntimeException("Erreur lors de de la récupération des publicationsConjointes", e);
		}
	}

	@Override
	public String getPublicationIntegraleOuExtrait() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_PUBLICATION_INTEGRALE_OU_EXTRAIT_PROPERTY);
	}

	@Override
	public void setPublicationIntegraleOuExtrait(String publicationIntegraleOuExtrait) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_PUBLICATION_INTEGRALE_OU_EXTRAIT_PROPERTY,
				publicationIntegraleOuExtrait);
	}

	@Override
	public Boolean getDecretNumerote() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DECRET_NUMEROTE_PROPERTY);
	}

	@Override
	public void setDecretNumerote(Boolean decretNumerote) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_DECRET_NUMEROTE_PROPERTY,
				decretNumerote);
	}

	@Override
	public String getDelaiPublication() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DELAI_PUBLICATION_PROPERTY);
	}

	@Override
	public void setDelaiPublication(String delaiPublication) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DELAI_PUBLICATION_PROPERTY, delaiPublication);
	}

	@Override
	public Calendar getDatePreciseePublication() {
		return PropertyUtil.getCalendarProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_PRECISEE_PUBLICATION_PROPERTY);
	}

	@Override
	public void setDatePreciseePublication(Calendar datePreciseePublication) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_PRECISEE_PUBLICATION_PROPERTY, datePreciseePublication);
	}

	@Override
	public String getZoneComSggDila() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_ZONE_COM_SGG_DILA_PROPERTY);
	}

	@Override
	public void setZoneComSggDila(String zoneComSggDila) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_ZONE_COM_SGG_DILA_PROPERTY, zoneComSggDila);
	}

	@Override
	public List<String> getIndexationRubrique() {
		return getListStringProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY);
	}

	@Override
	public void setIndexationRubrique(List<String> indexationRubrique) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY, indexationRubrique);
	}

	@Override
	public List<String> getIndexationMotsCles() {
		return getListStringProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_MOTS_CLES_PROPERTY);
	}

	@Override
	public void setIndexationMotsCles(List<String> indexationMotsCles) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_MOTS_CLES_PROPERTY, indexationMotsCles);
	}

	@Override
	public List<String> getIndexationChampLibre() {
		return getListStringProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_CHAMP_LIBRE_PROPERTY);
	}

	@Override
	public void setIndexationChampLibre(List<String> indexationChampLibre) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_CHAMP_LIBRE_PROPERTY, indexationChampLibre);
	}

	@Override
	public boolean isIndexationSgg() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_SGG_PROPERTY);
	}

	@Override
	public void setIndexationSgg(boolean indexationSgg) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_INDEXATION_SGG_PROPERTY,
				indexationSgg);
	}

	@Override
	public boolean isIndexationSggPub() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_SGG_PUB_PROPERTY);
	}

	@Override
	public void setIndexationSggPub(boolean indexationSggPub) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_SGG_PUB_PROPERTY, indexationSggPub);
	}

	@Override
	public boolean isIndexationMin() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_MIN_PROPERTY);
	}

	@Override
	public void setIndexationMin(boolean indexationMin) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_INDEXATION_MIN_PROPERTY,
				indexationMin);
	}

	@Override
	public boolean isIndexationMinPub() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_MIN_PUB_PROPERTY);
	}

	@Override
	public void setIndexationMinPub(boolean indexationMinPub) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_MIN_PUB_PROPERTY, indexationMinPub);
	}

	@Override
	public boolean isIndexationDir() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_DIR_PROPERTY);
	}

	@Override
	public void setIndexationDir(boolean indexationDir) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_INDEXATION_DIR_PROPERTY,
				indexationDir);
	}

	@Override
	public boolean isIndexationDirPub() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_DIR_PUB_PROPERTY);
	}

	@Override
	public void setIndexationDirPub(boolean indexationDirPub) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_INDEXATION_DIR_PUB_PROPERTY, indexationDirPub);
	}

	@Override
	public List<ComplexeType> getApplicationLoi() {
		return getTranspositionProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY);
	}

	@Override
	public void setApplicationLoi(List<ComplexeType> loisAppliquees) {
		// Dénormalisation des références des application lois
		setApplicationLoiRefs(ComplexTypeUtil.getListeComplexeTypeRef(loisAppliquees));

		PropertyUtil.setListSerializableProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY, loisAppliquees);
	}

	@Override
	public List<ComplexeType> getTranspositionOrdonnance() {
		return getTranspositionProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY);
	}

	@Override
	public void setTranspositionOrdonnance(List<ComplexeType> applicationOrdonnances) {
		// Dénormalisation des références des transpositions d'ordonnance
		setTranspositionOrdonnanceRefs(ComplexTypeUtil.getListeComplexeTypeRef(applicationOrdonnances));

		PropertyUtil.setListSerializableProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY, applicationOrdonnances);
	}

	@Override
	public List<ComplexeType> getTranspositionDirective() {
		return getTranspositionProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY);
	}

	@Override
	public void setTranspositionDirective(List<ComplexeType> directivesEuropeennes) {
		PropertyUtil.setListSerializableProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY, directivesEuropeennes);
	}

	@Override
	public String getHabilitationRefLoi() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_REF_LOI_PROPERTY);
	}

	@Override
	public void setHabilitationRefLoi(String habilitationRefLoi) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_REF_LOI_PROPERTY, habilitationRefLoi);
	}

	@Override
	public String getHabilitationNumeroArticles() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ARTICLES_PROPERTY);
	}

	@Override
	public void setHabilitationNumeroArticles(String numeroArticles) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ARTICLES_PROPERTY, numeroArticles);
	}

	@Override
	public String getHabilitationNumeroOrdre() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ORDRE_PROPERTY);
	}

	@Override
	public void setHabilitationNumeroOrdre(String habilitationNumeroOrdre) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ORDRE_PROPERTY, habilitationNumeroOrdre);
	}

	@Override
	public String getHabilitationCommentaire() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_COMMENTAIRE_PROPERTY);
	}

	@Override
	public void setHabilitationCommentaire(String habilitationCommentaire) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_COMMENTAIRE_PROPERTY, habilitationCommentaire);
	}

	@Override
	public Calendar getHabilitationDateTerme() {
		return PropertyUtil.getCalendarProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_DATE_TERME_PROPERTY);
	}

	@Override
	public void setHabilitationDateTerme(Calendar habilitationDateTerme) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_DATE_TERME_PROPERTY, habilitationDateTerme);
	}

	@Override
	public String getHabilitationTerme() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_TERME_PROPERTY);
	}

	@Override
	public void setHabilitationTerme(String habilitationTerme) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_TERME_PROPERTY, habilitationTerme);
	}

	@Override
	public String getCandidat() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_CANDIDAT_PROPERTY);
	}

	@Override
	public void setCandidat(String candidat) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_CANDIDAT_PROPERTY,
				candidat);
	}

	@Override
	public Boolean getIsDossierIssuInjection() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_IS_DOSSIER_ISSU_INJECTION_PROPERTY);
	}

	@Override
	public void setIsDossierIssuInjectionP(Boolean isDossierIssuInjection) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_IS_DOSSIER_ISSU_INJECTION_PROPERTY, isDossierIssuInjection);
	}

	@Override
	public Calendar getDateCandidature() {
		return PropertyUtil.getCalendarProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_CANDIDATURE_PROPERTY);
	}

	@Override
	public void setDateCandidature(Calendar dateCandidature) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_CANDIDATURE_PROPERTY, dateCandidature);
	}

	@Override
	public Boolean getIsUrgent() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_IS_URGENT_PROPERTY);
	}

	@Override
	public void setIsUrgent(Boolean isUrgent) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_IS_URGENT_PROPERTY,
				isUrgent);
	}

	@Override
	public String getStatutArchivage() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_STATUT_ARCHIVAGE);
	}

	@Override
	public void setStatutArchivage(String statutArchivage) {
		// dénormalisation
		setArchive( VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_GENERE.equals(statutArchivage)
				|| VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_LIVRE.equals(statutArchivage)
				|| VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ARCHIVE.equals(statutArchivage));
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_STATUT_ARCHIVAGE,
				statutArchivage);
	}

	@Override
	public Calendar getDateDeMaintienEnProduction() {
		return PropertyUtil.getCalendarProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_DE_MAINTIEN_EN_PRODCUTION);
	}

	@Override
	public void setDateDeMaintienEnProduction(Calendar dateDeMaintienEnProduction) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_DE_MAINTIEN_EN_PRODCUTION, dateDeMaintienEnProduction);
	}

	@Override
	public Calendar getDateVersementArchivageIntermediaire() {
		return PropertyUtil.getCalendarProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_VERSEMENT_ARCHIVAGE_INTERMEDIAIRE);
	}

	@Override
	public void setDateVersementArchivageIntermediaire(Calendar dateVersementArchivageIntermediaire) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_VERSEMENT_ARCHIVAGE_INTERMEDIAIRE,
				dateVersementArchivageIntermediaire);
	}

	@Override
	public void setTitle(String title) {
		DublincoreSchemaUtils.setTitle(document, title);
	}

	@Override
	public Boolean getArriveeSolonTS() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_ARRIVEE_SOLON_TS);
	}

	@Override
	public void setArriveeSolonTS(Boolean arriveeSolon) {
		PropertyUtil.setProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_ARRIVEE_SOLON_TS, arriveeSolon);
	}

	@Override
	public Calendar getDateVersementTS() {
		return PropertyUtil.getCalendarProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_VERSEMENT_TS);
	}

	@Override
	public Calendar getDateEnvoiJOTS() {
		return PropertyUtil.getCalendarProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_ENVOI_JO_TS);
	}

	@Override
	public void setDateEnvoiJOTS(Calendar dateEnvoiJOTS) {
		PropertyUtil.setProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_ENVOI_JO_TS, dateEnvoiJOTS);
	}

	@Override
	public void setDateVersementTS(Calendar dateVersementTS) {
		PropertyUtil.setProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_DATE_VERSEMENT_TS, dateVersementTS);
	}

	@Override
	public Boolean isDispositionHabilitation() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_DISPOSITION_PROPERTY);
	}

	@Override
	public void setDispositionHabilitation(Boolean dispositionHabilitation) {
		PropertyUtil.setProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_HABILITATION_DISPOSITION_PROPERTY, dispositionHabilitation);
	}

	@Override
	public String getCreatorPoste() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_POSTE_CREATOR_PROPERTY);
	}

	@Override
	public void setCreatorPoste(String creatorPoste) {
		PropertyUtil.setProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_POSTE_CREATOR_PROPERTY, creatorPoste);
	}

	@Override
	public Calendar getDateCreation() {
		return PropertyUtil.getCalendarProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				STSchemaConstant.DUBLINCORE_CREATED_PROPERTY);
	}

	@Override
	public void setDateCreation(Calendar dateCreation) {
		PropertyUtil.setProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				STSchemaConstant.DUBLINCORE_CREATED_PROPERTY, dateCreation);
	}

	@Override
	public String getLastContributor() {
		return DublincoreSchemaUtils.getLastContributor(document);
	}

	@Override
	public String getAuthor() {
		return DublincoreSchemaUtils.getCreator(document);
	}

	@Override
	public List<String> getTranspositionDirectiveNumero() {
		return PropertyUtil.getStringListProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.TRANSPOSITION_DIRECTIVE_NUMERO);
	}

	@Override
	public void setTranspositionDirectiveNumero(List<String> directivesEuropeennesNumero) {
		PropertyUtil.setProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.TRANSPOSITION_DIRECTIVE_NUMERO, directivesEuropeennesNumero);
	}

	@Override
	public List<String> getApplicationLoiRefs() {
		return PropertyUtil.getStringListProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.APPLICATION_LOI_REFS);
	}

	@Override
	public void setApplicationLoiRefs(List<String> applicationLoiRefs) {
		PropertyUtil.setProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.APPLICATION_LOI_REFS, applicationLoiRefs);
	}

	@Override
	public List<String> getTranspositionOrdonnanceRefs() {
		return PropertyUtil.getStringListProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.TRANSPOSITION_ORDONNANCE_REFS);
	}

	@Override
	public void setTranspositionOrdonnanceRefs(List<String> applicationLoiRefs) {
		PropertyUtil.setProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.TRANSPOSITION_ORDONNANCE_REFS, applicationLoiRefs);
	}

	@Override
	public String getIdDossier() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_ID_DOSSIER_PROPERTY);
	}

	@Override
	public void setIdDossier(String idDossier) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_ID_DOSSIER_PROPERTY,
				idDossier);
	}

	@Override
	public void setNorAttribue(Boolean norAttribue) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.NOR_ATTRIBUE_DOSSIER_PROPERTY,
				norAttribue);
	}

	@Override
	public void setPublie(Boolean publie) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.PUBLIE_DOSSIER_PROPERTY, publie);
	}

	@Override
	public void setArchive(Boolean archive) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.ARCHIVE_DOSSIER_PROPERTY, archive);
	}

	@Override
	public Boolean isArchive() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.ARCHIVE_DOSSIER_PROPERTY);
	}

	@Override
	public String getPeriodiciteRapport() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.PERIODICITE_RAPPORT);
	}

	@Override
	public void setPeriodiciteRapport(String periodiciteRapport) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.PERIODICITE_RAPPORT,
				periodiciteRapport);
	}

	@Override
	public String getPeriodicite() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.PERIODICITE);
	}

	@Override
	public void setPeriodicite(String periodicite) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.PERIODICITE, periodicite);
	}

	@Override
	public Boolean isAfterDemandePublication() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.IS_AFTER_DEMANDE_PUBLICATION);
	}

	@Override
	public void setIsAfterDemandePublication(Boolean value) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.IS_AFTER_DEMANDE_PUBLICATION,
				value);
	}

	@Override
	public void setAdoption(Boolean adoption) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.ADOPTION, adoption);
	}

	@Override
	public String getLastChargeMission() throws ClientException, Exception {
		FeuilleRouteService fdrs = SolonEpgServiceLocator.getFeuilleRouteService();
		return fdrs.getLastChargeMission(getCoreSession(), document);
	}

	@Override
	public Boolean isDossierDeleted() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DELETED);
	}

	@Override
	public void setIsDossierDeleted(Boolean isDossierDeleted) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DELETED, isDossierDeleted);
	}

	@Override
	public Boolean getTexteEntreprise() {
		return PropertyUtil.getBooleanProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_TEXTE_ENTREPRISE_PROPERTY);
	}

	@Override
	public void setTexteEntreprise(Boolean texteEntreprise) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_TEXTE_ENTREPRISE_PROPERTY, texteEntreprise);
	}

	@Override
	public List<Calendar> getDateEffet() {
		return PropertyUtil.getCalendarListProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_DATE_EFFET_PROPERTY);
	}

	@Override
	public void setDateEffet(List<Calendar> dateEffet) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_DATE_EFFET_PROPERTY,
				dateEffet);
	}

	@Override
	public String getEmetteur() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_EMETTEUR_PROPERTY);
	}

	@Override
	public void setEmetteur(String emetteur) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_EMETTEUR_PROPERTY,
				emetteur);
	}

	@Override
	public String getRubrique() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_RUBRIQUE_PROPERTY);
	}

	@Override
	public void setRubrique(String rubrique) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_RUBRIQUE_PROPERTY,
				rubrique);
	}

	@Override
	public String getCommentaire() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_COMMENTAIRE_PROPERTY);
	}

	@Override
	public void setCommentaire(String commentaire) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_COMMENTAIRE_PROPERTY,
				commentaire);
	}

	@Override
	public String getIdEvenement() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_ID_EVENEMENT_PROPERTY);
	}

	@Override
	public void setIdEvenement(String idEvenement) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_ID_EVENEMENT_PROPERTY,
				idEvenement);
	}

	@Override
	public String getIdExtractionLot() {
		return PropertyUtil.getStringProperty(document, DossierSolonEpgConstants.DOSSIER_SCHEMA,
				DossierSolonEpgConstants.DOSSIER_ID_EXTRACTION_LOT);
	}

	@Override
	public void setIdExtractionLot(String idExtractionLot) {
		setProperty(DossierSolonEpgConstants.DOSSIER_SCHEMA, DossierSolonEpgConstants.DOSSIER_ID_EXTRACTION_LOT,
				idExtractionLot);
	}
}
