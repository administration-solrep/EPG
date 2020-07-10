package fr.dila.solonepg.core.dto.activitenormative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.LigneProgrammation;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.LigneProgrammationConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.service.VocabularyServiceImpl;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;

public class TableauDeProgrammationLoiDTO implements Serializable {

	private static final long			serialVersionUID	= 4171700236032022191L;

	private List<LigneProgrammationDTO>	listProgrammation;

	private Boolean						locked;

	/**
	 * 
	 * @param dossier
	 * @param session
	 * @param checkBDD
	 *            true verifie si le tableau est en base (ie tableau de programmation), false pour tableau de suivi
	 * @param masquerApplique
	 *            true pour masquer les mesures appliquées
	 * @throws ClientException
	 */
	public TableauDeProgrammationLoiDTO(ActiviteNormativeProgrammation activiteNormativeProgrammation,
			CoreSession session, Boolean isTableauProgrammation, Boolean masquerApplique) throws ClientException {

		listProgrammation = new ArrayList<LigneProgrammationDTO>();

		List<LigneProgrammation> lignesProgrammations = new ArrayList<LigneProgrammation>();

		if (isTableauProgrammation) {
			lignesProgrammations = activiteNormativeProgrammation.getLigneProgrammation(session);

		}

		if (lignesProgrammations.isEmpty()) {

			setLocked(Boolean.FALSE);

			TexteMaitre texteMaitre = activiteNormativeProgrammation.getDocument().getAdapter(TexteMaitre.class);

			final ActiviteNormativeService actNormService = SolonEpgServiceLocator.getActiviteNormativeService();
			List<MesureApplicative> listMesure = actNormService.fetchMesure(texteMaitre.getMesuresIds(), session);

			final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
			for (MesureApplicative mesureApplicative : listMesure) {

				if (!isTableauProgrammation
						&& (StringUtils.isBlank(mesureApplicative.getNumeroOrdre()) || "0"
								.equalsIgnoreCase(mesureApplicative.getNumeroOrdre().trim()))) {
					// Mantis #39252
					continue;
				}

				if (!isTableauProgrammation && masquerApplique && mesureApplicative.getApplicationRecu()) {
					continue;
				}

				List<Decret> listDecret = actNormService.fetchDecrets(mesureApplicative.getDecretIds(), session);

				Boolean decretAffiche = Boolean.FALSE;
				if (listDecret != null && !listDecret.isEmpty()) {

					for (Decret decret : listDecret) {

						if (!isTableauProgrammation && !decret.hasValidation()) {
							// Mantis #39252
							continue;
						}

						LigneProgrammationDTO programmationDecret = new LigneProgrammationDTO();
						programmationDecret.setNumeroOrdre(mesureApplicative.getNumeroOrdre());
						programmationDecret.setArticle(mesureApplicative.getArticle());
						programmationDecret.setBaseLegale(mesureApplicative.getBaseLegale());
						programmationDecret.setObjet(mesureApplicative.getObjetRIM());
						programmationDecret.setMinisterePilote(getNor(mesureApplicative.getMinisterePilote()));
						programmationDecret.setDirectionResponsable(getDirection(mesureApplicative
								.getDirectionResponsable()));
						programmationDecret.setCategorieTexte(decret.getTypeActe() != null ? getTypeActe(decret
								.getTypeActe()) : null);
						programmationDecret.setConsultationObligatoireHCE(mesureApplicative.getConsultationsHCE());
						programmationDecret.setCalendrierConsultationObligatoireHCE(mesureApplicative
								.getCalendrierConsultationsHCE());
						programmationDecret.setDateSaisineCE(decret.getDateSaisineCE() != null ? decret
								.getDateSaisineCE().getTime() : null);
						programmationDecret.setDatePrevisionnelleSaisineCE(mesureApplicative
								.getDatePrevisionnelleSaisineCE() != null ? mesureApplicative
								.getDatePrevisionnelleSaisineCE().getTime() : null);
						programmationDecret
								.setObjectifPublication(mesureApplicative.getDateObjectifPublication() != null ? mesureApplicative
										.getDateObjectifPublication().getTime() : null);
						programmationDecret.setObservation(mesureApplicative.getObservation());

						programmationDecret.setDateSortieCE(decret.getDateSortieCE() != null ? decret.getDateSortieCE()
								.getTime() : null);
						programmationDecret.setDatePublicationDecret(decret.getDatePublication() != null ? decret
								.getDatePublication().getTime() : null);
						programmationDecret.setNorDecret(decret.getNumeroNor());
						programmationDecret.setTitreDecret(decret.getTitreOfficiel());

						if (StringUtils.isNotEmpty(mesureApplicative.getTypeMesure())) {
							programmationDecret.setTypeMesure(vocabularyService.getEntryLabel(
									VocabularyConstants.TYPE_MESURE_VOCABULARY, mesureApplicative.getTypeMesure()));
						} else {
							programmationDecret.setTypeMesure(null);
						}

						programmationDecret.setTypeMesureId(mesureApplicative.getTypeMesure());

						programmationDecret
								.setDateMiseApplication(mesureApplicative.getDateMiseApplication() != null ? mesureApplicative
										.getDateMiseApplication().getTime() : null);

						listProgrammation.add(programmationDecret);
						decretAffiche = Boolean.TRUE;

					}
				}
				if (listDecret == null || listDecret.isEmpty() || !decretAffiche) {

					LigneProgrammationDTO programmationDecret = new LigneProgrammationDTO();
					programmationDecret.setNumeroOrdre(mesureApplicative.getNumeroOrdre());
					programmationDecret.setArticle(mesureApplicative.getArticle());
					programmationDecret.setBaseLegale(mesureApplicative.getBaseLegale());
					programmationDecret.setObjet(mesureApplicative.getObjetRIM());
					programmationDecret.setMinisterePilote(getNor(mesureApplicative.getMinisterePilote()));
					programmationDecret.setDirectionResponsable(getDirection(mesureApplicative
							.getDirectionResponsable()));
					programmationDecret.setConsultationObligatoireHCE(mesureApplicative.getConsultationsHCE());
					programmationDecret.setCalendrierConsultationObligatoireHCE(mesureApplicative
							.getCalendrierConsultationsHCE());
					programmationDecret.setDatePrevisionnelleSaisineCE(mesureApplicative
							.getDatePrevisionnelleSaisineCE() != null ? mesureApplicative
							.getDatePrevisionnelleSaisineCE().getTime() : null);
					programmationDecret
							.setObjectifPublication(mesureApplicative.getDateObjectifPublication() != null ? mesureApplicative
									.getDateObjectifPublication().getTime() : null);
					programmationDecret.setObservation(mesureApplicative.getObservation());

					if (StringUtils.isNotEmpty(mesureApplicative.getTypeMesure())) {
						programmationDecret.setTypeMesure(vocabularyService.getEntryLabel(
								VocabularyConstants.TYPE_MESURE_VOCABULARY, mesureApplicative.getTypeMesure()));
					} else {
						programmationDecret.setTypeMesure(null);
					}

					programmationDecret.setTypeMesureId(mesureApplicative.getTypeMesure());

					programmationDecret
							.setDateMiseApplication(mesureApplicative.getDateMiseApplication() != null ? mesureApplicative
									.getDateMiseApplication().getTime() : null);
					if (!isTableauProgrammation) {
						programmationDecret.setTypeTableau(LigneProgrammationConstants.TYPE_TABLEAU_VALUE_SUIVI);
					}

					listProgrammation.add(programmationDecret);

				}

			}
		} else {
			setLocked(Boolean.TRUE);

			for (LigneProgrammation ligneProgrammation : lignesProgrammations) {
				LigneProgrammationDTO programmationDecret = new LigneProgrammationDTO();
				programmationDecret.setId(ligneProgrammation.getId());
				programmationDecret.setNumeroOrdre(ligneProgrammation.getNumeroOrdre());
				programmationDecret.setArticle(ligneProgrammation.getArticleLoi());
				programmationDecret.setBaseLegale(ligneProgrammation.getBaseLegale());
				programmationDecret.setObjet(ligneProgrammation.getObjet());
				programmationDecret.setMinisterePilote(ligneProgrammation.getMinisterePilote());
				programmationDecret.setDirectionResponsable(ligneProgrammation.getDirectionResponsable());
				programmationDecret.setCategorieTexte(ligneProgrammation.getCategorieTexte());
				programmationDecret.setConsultationObligatoireHCE(ligneProgrammation.getConsultationObligatoireHCE());
				programmationDecret.setCalendrierConsultationObligatoireHCE(ligneProgrammation
						.getCalendrierConsultationHCE());
				programmationDecret.setDateSaisineCE(ligneProgrammation.getDateSaisineCE() != null ? ligneProgrammation
						.getDateSaisineCE().getTime() : null);
				programmationDecret
						.setDatePrevisionnelleSaisineCE(ligneProgrammation.getDatePrevisionnelleSaisineCE() != null ? ligneProgrammation
								.getDatePrevisionnelleSaisineCE().getTime() : null);
				programmationDecret
						.setObjectifPublication(ligneProgrammation.getObjectifPublication() != null ? ligneProgrammation
								.getObjectifPublication().getTime() : null);
				programmationDecret.setObservation(ligneProgrammation.getObservation());
				programmationDecret.setTypeMesureId(ligneProgrammation.getTypeMesureId());
				programmationDecret
						.setDateMiseApplication(ligneProgrammation.getDateMiseApplication() != null ? ligneProgrammation
								.getDateMiseApplication().getTime() : null);

				listProgrammation.add(programmationDecret);
			}
		}
		Collections.sort(listProgrammation, new Comparator<LigneProgrammationDTO>() {
			@Override
			public int compare(LigneProgrammationDTO obj1, LigneProgrammationDTO obj2) {
				if (obj1.getNumeroOrdre() != null && obj2.getNumeroOrdre() != null) {
					String str1 = String.format("%9s", obj1.getNumeroOrdre());
					String str2 = String.format("%9s", obj2.getNumeroOrdre());
					return str1.compareTo(str2);
				}
				return -1;

			}
		});
	}

	private String getTypeActe(String idTypeActe) {
		String label = STServiceLocator.getVocabularyService().getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY,
				idTypeActe);
		return label.replace(VocabularyServiceImpl.UNKNOWN_ENTRY, "");
	}

	private String getDirection(String idDirection) {
		OrganigrammeNode node;
		try {
			node = STServiceLocator.getSTUsAndDirectionService().getUniteStructurelleNode(idDirection);
		} catch (ClientException e) {
			// node non trouvé
			node = null;
		}
		return node != null ? node.getLabel() : idDirection;
	}

	private String getNor(String idMinistere) {
		EntiteNode node;
		try {
			node = STServiceLocator.getSTMinisteresService().getEntiteNode(idMinistere);
		} catch (ClientException e) {
			// node non trouvé
			node = null;
		}
		return node != null ? node.getNorMinistere() : idMinistere;
	}

	public void setListProgrammation(List<LigneProgrammationDTO> listProgrammation) {
		this.listProgrammation = listProgrammation;
	}

	public List<LigneProgrammationDTO> getListProgrammation() {
		return listProgrammation;
	}

	public List<LigneProgrammation> remapField(CoreSession documentManager, DocumentModel activiteNormativeDoc)
			throws ClientException {
		List<LigneProgrammation> list = new ArrayList<LigneProgrammation>();
		ActiviteNormativeProgrammation actiNormProgrammation = activiteNormativeDoc
				.getAdapter(ActiviteNormativeProgrammation.class);
		actiNormProgrammation.emptyLigneProgrammation(documentManager,
				LigneProgrammationConstants.TYPE_TABLEAU_VALUE_PROGRAMMATION);

		for (LigneProgrammationDTO ligneProgrammationDTO : getListProgrammation()) {
			LigneProgrammation ligneProgrammation;

			if (StringUtil.isNotEmpty(ligneProgrammationDTO.getId())) {
				DocumentModel doc = documentManager.getDocument(new IdRef(ligneProgrammationDTO.getId()));
				ligneProgrammation = doc.getAdapter(LigneProgrammation.class);

			} else {
				String titreDoc = "LigneProgrammation-" + DateUtil.formatDDMMYYYY(Calendar.getInstance());
				DocumentModel doc = documentManager.createDocumentModel(activiteNormativeDoc.getPathAsString(),
						titreDoc, LigneProgrammationConstants.LIGNE_PROGRAMMATION_DOC_TYPE);

				ligneProgrammation = doc.getAdapter(LigneProgrammation.class);
			}
			ligneProgrammation.setArticleLoi(ligneProgrammationDTO.getArticle());
			ligneProgrammation.setBaseLegale(ligneProgrammationDTO.getBaseLegale());

			ligneProgrammation.setCalendrierConsultationObligatoireHCE(ligneProgrammationDTO
					.getCalendrierConsultationObligatoireHCE());

			ligneProgrammation.setCategorieTexte(ligneProgrammationDTO.getCategorieTexte());

			if (ligneProgrammationDTO.getDateSaisineCE() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(ligneProgrammationDTO.getDateSaisineCE());
				ligneProgrammation.setDateSaisineCE(cal);
			}

			ligneProgrammation.setDirectionResponsable(ligneProgrammationDTO.getDirectionResponsable());
			ligneProgrammation.setMinisterePilote(ligneProgrammationDTO.getMinisterePilote());
			ligneProgrammation.setNumeroOrdre(ligneProgrammationDTO.getNumeroOrdre());

			if (ligneProgrammationDTO.getObjectifPublication() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(ligneProgrammationDTO.getObjectifPublication());
				ligneProgrammation.setObjectifPublication(cal);
			}

			if (ligneProgrammationDTO.getDatePrevisionnelleSaisineCE() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(ligneProgrammationDTO.getDatePrevisionnelleSaisineCE());
				ligneProgrammation.setDatePrevisionnelleSaisineCE(cal);
			}

			ligneProgrammation.setObjet(ligneProgrammationDTO.getObjet());
			ligneProgrammation.setObservation(ligneProgrammationDTO.getObservation());
			ligneProgrammation.setTypeMesureId(ligneProgrammationDTO.getTypeMesureId());
			ligneProgrammation.setConsultationObligatoireHCE(ligneProgrammationDTO.getConsultationObligatoireHCE());
			ligneProgrammation.setTypeTableau(LigneProgrammationConstants.TYPE_TABLEAU_VALUE_PROGRAMMATION);

			if (StringUtil.isNotEmpty(ligneProgrammationDTO.getId())) {
				documentManager.saveDocument(ligneProgrammation.getDocument());
			} else {
				documentManager.createDocument(ligneProgrammation.getDocument());
			}
			documentManager.save();
			list.add(ligneProgrammation);
		}

		return list;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean isLocked() {
		return locked;
	}

}
