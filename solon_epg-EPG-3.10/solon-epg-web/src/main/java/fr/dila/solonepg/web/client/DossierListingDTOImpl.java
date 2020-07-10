package fr.dila.solonepg.web.client;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.dto.IdLabel;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.core.client.AbstractMapDTO;

public class DossierListingDTOImpl extends AbstractMapDTO implements DossierListingDTO {

	private static final String	IS_COMPLET					= "isComplet";
	private static final String	IS_RETOUR_POUR_MODIFICATION	= "isRetourPourModification";
	private static final String	FROM_CASE_LINK				= "fromCaseLink";
	private static final long	serialVersionUID			= -6369089723223447444L;

	public DossierListingDTOImpl() {
		setFromCaseLink(Boolean.FALSE);
	}

	/**
	 * Rédéfinition de equal pour la méthode refreshCurrentEntry de CorbeilleActionsBean : permet de n'avoir à définir
	 * que l'id du dossier ou du dossierLink pour définir sa position dans une liste de DossierListingDTO.
	 * 
	 * nb : utilisé dans le currentPage.indexOf(entry) de la méthode setCurrentEntry de l'AbstractPageProvider.
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DossierListingDTO) {
			DossierListingDTO dossierListingDTO = (DossierListingDTO) obj;
			// on vérifie le caseLinkId
			if (StringUtils.isNotEmpty(dossierListingDTO.getCaseLinkId())
					&& StringUtils.isNotEmpty(this.getCaseLinkId())) {
				if (dossierListingDTO.getCaseLinkId().equals(this.getCaseLinkId())) {
					return true;
				} else {
					return false;
				}
			}

			// on vérifie le dossierId
			if (StringUtils.isNotEmpty(dossierListingDTO.getDossierId()) && StringUtils.isNotEmpty(this.getDossierId())) {
				if (dossierListingDTO.getDossierId().equals(this.getDossierId())) {
					return true;
				} else {
					return false;
				}
			}

		}
		return false;
	}

	@Override
	public int hashCode() {
		if (StringUtils.isNotEmpty(this.getCaseLinkId())) {
			return this.getCaseLinkId().hashCode();
		} else if (StringUtils.isNotEmpty(this.getDossierId())) {
			return this.getDossierId().hashCode();
		} else {
			return 0;
		}
	}

	@Override
	public IdLabel[] getCaseLinkIdsLabels() {
		return (IdLabel[]) get(CASE_LINK_IDS_LABELS);
	}

	@Override
	public String getDossierId() {
		return getString(DOSSIER_ID);
	}

	@Override
	public void setCaseLinkIdsLabels(IdLabel[] caseLinkIdsLabels) {
		put(CASE_LINK_IDS_LABELS, caseLinkIdsLabels);
	}

	@Override
	public void setDossierId(String dossierId) {
		put(DOSSIER_ID, dossierId);
	}

	@Override
	public String getDocIdForSelection() {
		return getString(DOC_ID_FOR_SELECTION);
	}

	@Override
	public void setDocIdForSelection(String docIdForSelection) {
		put(DOC_ID_FOR_SELECTION, docIdForSelection);
	}

	@Override
	public String getType() {
		return "Dossier";
	}

	@Override
	public Boolean isLocked() {
		return getBoolean(IS_LOCKED);
	}

	@Override
	public void setLocked(Boolean isLocked) {
		put(IS_LOCKED, isLocked);
	}

	@Override
	public String getLockOwner() {
		return getString(LOCK_OWNER);
	}

	@Override
	public void setDecretArrive(Boolean isDecretArrive) {
		put(IS_DECRET_ARRIVE, isDecretArrive);
	}

	@Override
	public Boolean isDecretArrive() {
		return getBoolean(IS_DECRET_ARRIVE);
	}

	@Override
	public void setLockOwner(String lockOwner) {
		put(LOCK_OWNER, lockOwner);
	}

	@Override
	public String getCaseLinkId() {
		return getString(CASE_LINK_ID);
	}

	@Override
	public Boolean getRead() {
		return getBoolean(IS_READ);
	}

	@Override
	public void setCaseLinkId(String caseLinkId) {
		put(CASE_LINK_ID, caseLinkId);
	}

	@Override
	public void setRead(Boolean read) {
		put(IS_READ, read);
	}

	@Override
	public String getStatut() {
		return getString(DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY);
	}

	@Override
	public void setStatut(String statut) {
		put(DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY, statut);
	}

	@Override
	public String getNumeroNor() {
		return getString(DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY);
	}

	@Override
	public void setNumeroNor(String numeroNor) {
		put(DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY, numeroNor);
	}

	@Override
	public String getTypeActe() {
		return getString(DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY);
	}

	@Override
	public void setTypeActe(String typeActe) {
		put(DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_PROPERTY, typeActe);
	}

	@Override
	public String getTitreActe() {
		return getString(DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_PROPERTY);
	}

	@Override
	public void setTitreActe(String titreActe) {
		put(DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_PROPERTY, titreActe);
	}

	@Override
	public String getMinistereResp() {
		return getString(DossierSolonEpgConstants.DOSSIER_MINISTERE_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setMinistereResp(String ministereResp) {
		put(DossierSolonEpgConstants.DOSSIER_MINISTERE_RESPONSABLE_PROPERTY, ministereResp);
	}

	@Override
	public String getDirectionResp() {
		return getString(DossierSolonEpgConstants.DOSSIER_DIRECTION_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setDirectionResp(String ministereResp) {
		put(DossierSolonEpgConstants.DOSSIER_DIRECTION_RESPONSABLE_PROPERTY, ministereResp);
	}

	@Override
	public String getMinistereAttache() {
		return getString(DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_PROPERTY);
	}

	@Override
	public void setMinistereAttache(String ministereAttache) {
		put(DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_PROPERTY, ministereAttache);
	}

	@Override
	public String getDirectionAttache() {
		return getString(DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_PROPERTY);
	}

	@Override
	public void setDirectionAttache(String ministereAttache) {
		put(DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_PROPERTY, ministereAttache);
	}

	@Override
	public String getNomRespDossier() {
		return getString(DossierSolonEpgConstants.DOSSIER_NOM_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setNomRespDossier(String nomRespDossier) {
		put(DossierSolonEpgConstants.DOSSIER_NOM_RESPONSABLE_PROPERTY, nomRespDossier);
	}

	@Override
	public String getPrenomRespDossier() {
		return getString(DossierSolonEpgConstants.DOSSIER_PRENOM_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setPrenomRespDossier(String prenomRespDossier) {
		put(DossierSolonEpgConstants.DOSSIER_PRENOM_RESPONSABLE_PROPERTY, prenomRespDossier);
	}

	@Override
	public String getQualiteRespDossier() {
		return getString(DossierSolonEpgConstants.DOSSIER_QUALITE_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setQualiteRespDossier(String qualite) {
		put(DossierSolonEpgConstants.DOSSIER_QUALITE_RESPONSABLE_PROPERTY, qualite);
	}

	@Override
	public String getTelephoneRespDossier() {
		return getString(DossierSolonEpgConstants.DOSSIER_TEL_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setTelephoneRespDossier(String telephoneRespDossier) {
		put(DossierSolonEpgConstants.DOSSIER_TEL_RESPONSABLE_PROPERTY, telephoneRespDossier);
	}

	@Override
	public String getMailRespDossier() {
		return getString(DossierSolonEpgConstants.DOSSIER_MAIL_RESPONSABLE_PROPERTY);
	}

	@Override
	public void setMailRespDossier(String mailRespDossier) {
		put(DossierSolonEpgConstants.DOSSIER_MAIL_RESPONSABLE_PROPERTY, mailRespDossier);
	}

	@Override
	public String getCategorieActe() {
		return getString(DossierSolonEpgConstants.DOSSIER_CATEGORIE_ACTE_PROPERTY);
	}

	@Override
	public void setCategorieActe(String categorieActe) {
		put(DossierSolonEpgConstants.DOSSIER_CATEGORIE_ACTE_PROPERTY, categorieActe);
	}

	@Override
	public String getBaseLegaleActe() {
		return getString(DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_ACTE_PROPERTY);
	}

	@Override
	public void setBaseLegaleActe(String baseLegaleActe) {
		put(DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_ACTE_PROPERTY, baseLegaleActe);
	}

	@Override
	public Date getDatePublication() {
		return getDate(DossierSolonEpgConstants.DOSSIER_DATE_PUBLICATION_PROPERTY);
	}

	@Override
	public void setDatePublication(Date datePublication) {
		put(DossierSolonEpgConstants.DOSSIER_DATE_PUBLICATION_PROPERTY, datePublication);
	}

	@Override
	public Boolean getPublicationRapportPresentation() {
		return getBoolean(DossierSolonEpgConstants.DOSSIER_PUBLICATION_RAPPORT_PRESENTATION_PROPERTY);
	}

	@Override
	public void setPublicationRapportPresentation(Boolean publicationRapportPresentation) {
		put(DossierSolonEpgConstants.DOSSIER_PUBLICATION_RAPPORT_PRESENTATION_PROPERTY, publicationRapportPresentation);
	}

	@Override
	public List<String> getNomCompletChargesMissions() {
		return getListString(DossierSolonEpgConstants.DOSSIER_NOM_COMPLET_CHARGES_MISSIONS_PROPERTY);
	}

	@Override
	public void setNomCompletChargesMissions(List<String> chargeMissionIds) {
		putListString(DossierSolonEpgConstants.DOSSIER_NOM_COMPLET_CHARGES_MISSIONS_PROPERTY, chargeMissionIds);
	}

	@Override
	public List<String> getNomCompletConseillersPms() {
		return getListString(DossierSolonEpgConstants.DOSSIER_NOM_COMPLET_CONSEILLERS_PMS_PROPERTY);
	}

	@Override
	public void setNomCompletConseillersPms(List<String> conseillerPmIds) {
		putListString(DossierSolonEpgConstants.DOSSIER_NOM_COMPLET_CONSEILLERS_PMS_PROPERTY, conseillerPmIds);
	}

	@Override
	public Date getDateSignature() {
		return getDate(DossierSolonEpgConstants.DOSSIER_DATE_SIGNATURE_PROPERTY);
	}

	@Override
	public void setDateSignature(Date dateSignature) {
		put(DossierSolonEpgConstants.DOSSIER_DATE_SIGNATURE_PROPERTY, dateSignature);
	}

	@Override
	public Date getDatePourFournitureEpreuve() {
		return getDate(DossierSolonEpgConstants.DOSSIER_POUR_FOURNITURE_EPREUVE_PROPERTY);
	}

	@Override
	public void setDatePourFournitureEpreuve(Date pourFournitureEpreuve) {
		put(DossierSolonEpgConstants.DOSSIER_POUR_FOURNITURE_EPREUVE_PROPERTY, pourFournitureEpreuve);
	}

	@Override
	public List<String> getVecteurPublication() {
		return getListString(DossierSolonEpgConstants.DOSSIER_VECTEUR_PUBLICATION_PROPERTY);
	}

	@Override
	public void setVecteurPublication(List<String> vecteurPublication) {
		putListString(DossierSolonEpgConstants.DOSSIER_VECTEUR_PUBLICATION_PROPERTY, vecteurPublication);
	}

	@Override
	public List<String> getPublicationsConjointes() {
		return getListString(DossierSolonEpgConstants.DOSSIER_PUBLICATION_CONJOINTE_PROPERTY);
	}

	@Override
	public void setPublicationsConjointes(List<String> publicationsConjointes) {
		putListString(DossierSolonEpgConstants.DOSSIER_PUBLICATION_CONJOINTE_PROPERTY, publicationsConjointes);
	}

	@Override
	public String getPublicationIntegraleOuExtrait() {
		return getString(DossierSolonEpgConstants.DOSSIER_PUBLICATION_INTEGRALE_OU_EXTRAIT_PROPERTY);
	}

	@Override
	public void setPublicationIntegraleOuExtrait(String publicationIntegraleOuExtrait) {
		put(DossierSolonEpgConstants.DOSSIER_PUBLICATION_INTEGRALE_OU_EXTRAIT_PROPERTY, publicationIntegraleOuExtrait);
	}

	@Override
	public Boolean getDecretNumerote() {
		return getBoolean(DossierSolonEpgConstants.DOSSIER_DECRET_NUMEROTE_PROPERTY);
	}

	@Override
	public void setDecretNumerote(Boolean decretNumerote) {
		put(DossierSolonEpgConstants.DOSSIER_DECRET_NUMEROTE_PROPERTY, decretNumerote);
	}

	@Override
	public String getDelaiPublication() {
		return getString(DossierSolonEpgConstants.DOSSIER_DELAI_PUBLICATION_PROPERTY);
	}

	@Override
	public void setDelaiPublication(String delaiPublication) {
		put(DossierSolonEpgConstants.DOSSIER_DELAI_PUBLICATION_PROPERTY, delaiPublication);
	}

	@Override
	public Date getDatePreciseePublication() {
		return getDate(DossierSolonEpgConstants.DOSSIER_DATE_PRECISEE_PUBLICATION_PROPERTY);
	}

	@Override
	public void setDatePreciseePublication(Date datePreciseePublication) {
		put(DossierSolonEpgConstants.DOSSIER_DATE_PRECISEE_PUBLICATION_PROPERTY, datePreciseePublication);
	}

	@Override
	public String getZoneComSggDila() {
		return getString(DossierSolonEpgConstants.DOSSIER_ZONE_COM_SGG_DILA_PROPERTY);
	}

	@Override
	public void setZoneComSggDila(String zoneComSggDila) {
		put(DossierSolonEpgConstants.DOSSIER_ZONE_COM_SGG_DILA_PROPERTY, zoneComSggDila);
	}

	@Override
	public List<String> getIndexationRubrique() {
		return getListString(DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY);
	}

	@Override
	public void setIndexationRubrique(List<String> indexationRubrique) {
		putListString(DossierSolonEpgConstants.DOSSIER_INDEXATION_RUBRIQUE_PROPERTY, indexationRubrique);
	}

	@Override
	public List<String> getIndexationMotsCles() {
		return getListString(DossierSolonEpgConstants.DOSSIER_INDEXATION_MOTS_CLES_PROPERTY);
	}

	@Override
	public void setIndexationMotsCles(List<String> indexationMotsCles) {
		putListString(DossierSolonEpgConstants.DOSSIER_INDEXATION_MOTS_CLES_PROPERTY, indexationMotsCles);
	}

	@Override
	public List<String> getIndexationChampLibre() {
		return getListString(DossierSolonEpgConstants.DOSSIER_INDEXATION_CHAMP_LIBRE_PROPERTY);
	}

	@Override
	public void setIndexationChampLibre(List<String> indexationChampLibre) {
		putListString(DossierSolonEpgConstants.DOSSIER_INDEXATION_CHAMP_LIBRE_PROPERTY, indexationChampLibre);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComplexeType> getApplicationLoi() {
		return (List<ComplexeType>) getComplexe(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY);
	}

	@Override
	public void setApplicationLoi(List<ComplexeType> loisAppliquees) {
		putComplexe(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY, loisAppliquees);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComplexeType> getTranspositionOrdonnance() {
		return (List<ComplexeType>) getComplexe(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY);
	}

	@Override
	public void setTranspositionOrdonnance(List<ComplexeType> applicationOrdonnances) {
		putComplexe(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY, applicationOrdonnances);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComplexeType> getTranspositionDirective() {
		return (List<ComplexeType>) getComplexe(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY);
	}

	@Override
	public void setTranspositionDirective(List<ComplexeType> directivesEuropeennes) {
		putComplexe(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY, directivesEuropeennes);
	}

	@Override
	public List<String> getApplicationLoiRef() {
		return getListString(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_REF_PROPERTY);
	}

	@Override
	public void setApplicationLoiRef(List<String> loisAppliquees) {
		putListString(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_REF_PROPERTY, loisAppliquees);
	}

	@Override
	public List<String> getTranspositionOrdonnanceRef() {
		return getListString(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_REF_PROPERTY);
	}

	@Override
	public void setTranspositionOrdonnanceRef(List<String> applicationOrdonnances) {
		putListString(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_REF_PROPERTY, applicationOrdonnances);
	}

	@Override
	public List<String> getTranspositionDirectiveRef() {
		return getListString(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_REF_PROPERTY);
	}

	@Override
	public void setTranspositionDirectiveRef(List<String> directivesEuropeennes) {
		putListString(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_REF_PROPERTY, directivesEuropeennes);
	}

	@Override
	public String getHabilitationRefLoi() {
		return getString(DossierSolonEpgConstants.DOSSIER_HABILITATION_REF_LOI_PROPERTY);
	}

	@Override
	public void setHabilitationRefLoi(String habilitationRefLoi) {
		put(DossierSolonEpgConstants.DOSSIER_HABILITATION_REF_LOI_PROPERTY, habilitationRefLoi);
	}

	@Override
	public String getHabilitationNumeroArticles() {
		return getString(DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ARTICLES_PROPERTY);
	}

	@Override
	public void setHabilitationNumeroArticles(String numeroArticles) {
		put(DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ARTICLES_PROPERTY, numeroArticles);
	}

	@Override
	public String getHabilitationCommentaire() {
		return getString(DossierSolonEpgConstants.DOSSIER_HABILITATION_COMMENTAIRE_PROPERTY);
	}

	@Override
	public void setHabilitationCommentaire(String habilitationCommentaire) {
		put(DossierSolonEpgConstants.DOSSIER_HABILITATION_COMMENTAIRE_PROPERTY, habilitationCommentaire);
	}

	@Override
	public Boolean getUrgent() {
		return getBoolean(DossierSolonEpgConstants.DOSSIER_IS_URGENT_PROPERTY);
	}

	@Override
	public void setUrgent(Boolean urgent) {
		put(DossierSolonEpgConstants.DOSSIER_IS_URGENT_PROPERTY, urgent);
	}

	@Override
	public Date getDateCreation() {
		return getDate(STSchemaConstant.DUBLINCORE_CREATED_PROPERTY);
	}

	@Override
	public String getLastContributor() {
		return getString(STSchemaConstant.DUBLINCORE_LAST_CONTRIBUTOR_PROPERTY);
	}

	@Override
	public String getAuthor() {
		return getString(STSchemaConstant.DUBLINCORE_CREATOR_PROPERTY);
	}

	@Override
	public void setDateCreation(Date date) {
		put(STSchemaConstant.DUBLINCORE_CREATED_PROPERTY, date);
	}

	@Override
	public void setLastContributor(String lastContributor) {
		put(STSchemaConstant.DUBLINCORE_LAST_CONTRIBUTOR_PROPERTY, lastContributor);
	}

	@Override
	public void setAuthor(String author) {
		put(STSchemaConstant.DUBLINCORE_CREATOR_PROPERTY, author);
	}

	@SuppressWarnings("unchecked")
	private List<? extends ComplexeType> getComplexe(String key) {
		return (List<? extends ComplexeType>) get(key);
	}

	private void putComplexe(String key, List<? extends ComplexeType> value) {
		put(key, (Serializable) value);
	}

	@Override
	public Date getDateParutionJorf() {
		return getDate(RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_JORF_PROPERTY);
	}

	@Override
	public void setDateParutionJorf(Date dateParutionJorf) {
		put(RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_JORF_PROPERTY, dateParutionJorf);
	}

	@Override
	public String getNumeroTexteParutionJorf() {
		return getString(RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_JORF_PROPERTY);
	}

	@Override
	public void setNumeroTexteParutionJorf(String numeroTexteParutionJorf) {
		put(RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_JORF_PROPERTY, numeroTexteParutionJorf);
	}

	@Override
	public Long getPageParutionJorf() {
		return getLong(RetourDilaConstants.RETOUR_DILA_PAGE_PARUTION_JORF_PROPERTY);
	}

	@Override
	public void setPageParutionJorf(Long pageParutionJorf) {
		put(RetourDilaConstants.RETOUR_DILA_PAGE_PARUTION_JORF_PROPERTY, pageParutionJorf);
	}

	@Override
	public String getModeParution() {
		return getString(RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_PROPERTY);
	}

	@Override
	public void setModeParution(String modeParution) {
		put(RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_PROPERTY, modeParution);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ParutionBo> getParutionBo() {
		return (List<ParutionBo>) getComplexe(RetourDilaConstants.RETOUR_DILA_PARUTION_BO_PROPERTY);
	}

	@Override
	public void setParutionBo(List<ParutionBo> parutionBoList) {
		putComplexe(RetourDilaConstants.RETOUR_DILA_PARUTION_BO_PROPERTY, parutionBoList);
	}

	@Override
	public String getPriorite() {
		return getString(ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_PROPERTY);
	}

	@Override
	public void setPriorite(String priorite) {
		put(ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_PROPERTY, priorite);
	}

	@Override
	public String getSectionCe() {
		return getString(ConseilEtatConstants.CONSEIL_ETAT_SECTION_CE_PROPERTY);
	}

	@Override
	public void setSectionCe(String sectionCe) {
		put(ConseilEtatConstants.CONSEIL_ETAT_SECTION_CE_PROPERTY, sectionCe);
	}

	@Override
	public String getRapporteurCe() {
		return getString(ConseilEtatConstants.CONSEIL_ETAT_RAPPORTEUR_CE_PROPERTY);
	}

	@Override
	public void setRapporteurCe(String rapporteurCe) {
		put(ConseilEtatConstants.CONSEIL_ETAT_RAPPORTEUR_CE_PROPERTY, rapporteurCe);
	}

	@Override
	public Date getDateTransmissionSectionCe() {
		return getDate(ConseilEtatConstants.CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_PROPERTY);
	}

	@Override
	public void setDateTransmissionSectionCe(Date dateTransmissionSectionCe) {
		put(ConseilEtatConstants.CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_PROPERTY, dateTransmissionSectionCe);
	}

	@Override
	public Date getDateSectionCe() {
		return getDate(ConseilEtatConstants.CONSEIL_ETAT_DATE_SECTION_CE_PROPERTY);
	}

	@Override
	public void setDateSectionCe(Date dateSectionCe) {
		put(ConseilEtatConstants.CONSEIL_ETAT_DATE_SECTION_CE_PROPERTY, dateSectionCe);
	}

	@Override
	public Date getDateAgCe() {
		return getDate(ConseilEtatConstants.CONSEIL_ETAT_DATE_AG_CE_PROPERTY);
	}

	@Override
	public void setDateAgCe(Date dateAgCe) {
		put(ConseilEtatConstants.CONSEIL_ETAT_DATE_AG_CE_PROPERTY, dateAgCe);
	}

	@Override
	public String getNumeroISA() {
		return getString(ConseilEtatConstants.CONSEIL_ETAT_NUMERO_ISA_PROPERTY);
	}

	@Override
	public void setNumeroISA(String numeroISA) {
		put(ConseilEtatConstants.CONSEIL_ETAT_NUMERO_ISA_PROPERTY, numeroISA);
	}

	@Override
	public Boolean isComplet() {
		return getBoolean(IS_COMPLET);
	}

	@Override
	public void setComplet(Boolean complet) {
		put(IS_COMPLET, complet);
	}

	@Override
	public Boolean isRetourPourModification() {
		return getBoolean(IS_RETOUR_POUR_MODIFICATION);
	}

	@Override
	public void setRetourPourModification(Boolean retourPourModification) {
		put(IS_RETOUR_POUR_MODIFICATION, retourPourModification);
	}

	@Override
	public void setFromCaseLink(Boolean fromCaseLink) {
		put(FROM_CASE_LINK, fromCaseLink);
	}

	@Override
	public Boolean isFromCaseLink() {
		return getBoolean(FROM_CASE_LINK);
	}

	@Override
	public List<Calendar> getDateEffet() {
		return getListCalendar(DossierSolonEpgConstants.DOSSIER_DATE_EFFET_PROPERTY);
	}

	@Override
	public void setDateEffet(List<Calendar> dateEffet) {
		putListCalendar(DossierSolonEpgConstants.DOSSIER_DATE_EFFET_PROPERTY, dateEffet);
	}

	@Override
	public Boolean isTexteEntreprise() {
		return getBoolean(DossierSolonEpgConstants.DOSSIER_TEXTE_ENTREPRISE_PROPERTY);
	}

	@Override
	public void setTexteEntreprise(Boolean te) {
		put(DossierSolonEpgConstants.DOSSIER_TEXTE_ENTREPRISE_PROPERTY,te);
	}
}
