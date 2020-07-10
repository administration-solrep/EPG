package fr.dila.solonepg.core.dto.activitenormative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.cases.typescomplexe.LigneProgrammationHabilitation;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.cases.typescomplexe.LigneProgrammationHabilitationImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.core.service.STServiceLocator;

public class TableauDeProgrammationHabilitationDTO implements Serializable {

	private static final long						serialVersionUID	= 4171700236032022191L;

	private List<LigneProgrammationHabilitationDTO>	listProgrammation;

	private Boolean									locked;

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
	public TableauDeProgrammationHabilitationDTO(ActiviteNormativeProgrammation activiteNormativeProgrammation,
			CoreSession session, Boolean isTableauProgrammation, Boolean masquerApplique) throws ClientException {

		listProgrammation = new ArrayList<LigneProgrammationHabilitationDTO>();
		HashMap<Integer, LigneProgrammationHabilitationDTO> listHabilitationMap = new HashMap<Integer, LigneProgrammationHabilitationDTO>();

		List<LigneProgrammationHabilitation> lignesProgrammations = new ArrayList<LigneProgrammationHabilitation>();

		if (isTableauProgrammation) {
			lignesProgrammations = activiteNormativeProgrammation.getLigneProgrammationHabilitation();
		}

		if (lignesProgrammations.isEmpty()) {

			setLocked(Boolean.FALSE);

			TexteMaitre texteMaitre = activiteNormativeProgrammation.getDocument().getAdapter(TexteMaitre.class);

			List<Habilitation> listHabilitation = SolonEpgServiceLocator.getActiviteNormativeService()
					.fetchListHabilitation(texteMaitre.getHabilitationIds(), session);
			int count = -1;

			for (Habilitation habilitation : listHabilitation) {

				if (!isTableauProgrammation) {
					// tableau de suivi

					if (VocabularyConstants.TYPE_HABILITATION_ACTIVE.equals(habilitation.getTypeHabilitation())) {

						List<Ordonnance> listOrdonnance = SolonEpgServiceLocator.getActiviteNormativeService()
								.fetchListOrdonnance(habilitation.getOrdonnanceIds(), session);

						Boolean ordonnanceAffiche = Boolean.FALSE;

						if (listOrdonnance != null && !listOrdonnance.isEmpty()) {
							for (Ordonnance ordonnance : listOrdonnance) {

								if (ordonnance.hasValidation()) {
									LigneProgrammationHabilitationDTO ligne = createLigne(habilitation);

									ligne.setNumeroNorOrdo(ordonnance.getNumeroNor());
									ligne.setObjetOrdo(ordonnance.getObjet());
									ligne.setMinisterePiloteOrdo(getNor(ordonnance.getMinisterePilote()));
									ligne.setLegislatureOrdo(ordonnance.getLegislature());
									ligne.setDateSaisineCEOrdo(ordonnance.getDateSaisineCE() != null ? ordonnance
											.getDateSaisineCE().getTime() : null);
									ligne.setDatePassageCMOrdo(ordonnance.getDatePassageCM() != null ? ordonnance
											.getDatePassageCM().getTime() : null);
									ligne.setDatePublicationOrdo(ordonnance.getDatePublication() != null ? ordonnance
											.getDatePublication().getTime() : null);
									ligne.setTitreOfficielOrdo(ordonnance.getTitreOfficiel());
									ligne.setNumeroOrdo(ordonnance.getNumero());
									ligne.setConventionDepotOrdo(ordonnance.getConventionDepot());
									ligne.setDateLimiteDepotOrdo(ordonnance.getDateLimiteDepot() != null ? ordonnance
											.getDateLimiteDepot().getTime() : null);

									listHabilitationMap
											.put((ligne.getNumeroOrdre() != null && !ligne.getNumeroOrdre().equals("") ? Integer
													.valueOf(ligne.getNumeroOrdre()) : count--), ligne);
									ordonnanceAffiche = Boolean.TRUE;
								}
							}
						}

						if (listOrdonnance == null || listOrdonnance.isEmpty() || !ordonnanceAffiche) {
							LigneProgrammationHabilitationDTO ligne = createLigne(habilitation);
							listHabilitationMap.put(
									(ligne.getNumeroOrdre() != null && !ligne.getNumeroOrdre().equals("") ? Integer
											.valueOf(ligne.getNumeroOrdre()) : count--), ligne);
						}
					}

				} else {
					LigneProgrammationHabilitationDTO ligne = createLigne(habilitation);
					listHabilitationMap.put(
							(ligne.getNumeroOrdre() != null && !ligne.getNumeroOrdre().equals("") ? Integer
									.valueOf(ligne.getNumeroOrdre()) : count--), ligne);
				}
			}
		} else {

			setLocked(Boolean.TRUE);
			int count = -1;
			for (LigneProgrammationHabilitation ligneProgrammation : lignesProgrammations) {
				listHabilitationMap
						.put((ligneProgrammation.getNumeroOrdre() != null
								&& !ligneProgrammation.getNumeroOrdre().equals("") ? Integer.valueOf(ligneProgrammation
								.getNumeroOrdre()) : count--), createLigne(ligneProgrammation));
			}
		}
		// Trier par nb d'ordre
		SortedSet<Integer> keys = new TreeSet<Integer>(listHabilitationMap.keySet());
		for (Integer key : keys) {
			listProgrammation.add(listHabilitationMap.get(key));
		}
	}

	private LigneProgrammationHabilitationDTO createLigne(LigneProgrammationHabilitation ligneProgrammation) {
		LigneProgrammationHabilitationDTO ligne = new LigneProgrammationHabilitationDTO();

		ligne.setArticle(ligneProgrammation.getArticle());
		ligne.setCodification(ligneProgrammation.getCodification());
		ligne.setConvention(ligneProgrammation.getConvention());
		ligne.setConventionDepot(ligneProgrammation.getConventionDepot());
		ligne.setDatePrevisionnelleCM(ligneProgrammation.getDatePrevisionnelleCM() != null ? ligneProgrammation
				.getDatePrevisionnelleCM().getTime() : null);
		ligne.setDatePrevisionnelleSaisineCE(ligneProgrammation.getDatePrevisionnelleSaisineCE() != null ? ligneProgrammation
				.getDatePrevisionnelleSaisineCE().getTime() : null);
		ligne.setDateTerme(ligneProgrammation.getDateTerme() != null ? ligneProgrammation.getDateTerme().getTime()
				: null);
		ligne.setLegislature(ligneProgrammation.getLegislature());
		ligne.setMinisterePilote(getNor(ligneProgrammation.getMinisterePilote()));
		ligne.setNumeroOrdre(ligneProgrammation.getNumeroOrdre());
		ligne.setObjetRIM(ligneProgrammation.getObjetRIM());
		ligne.setObservation(ligneProgrammation.getObservation());

		return ligne;
	}

	private LigneProgrammationHabilitationDTO createLigne(Habilitation habilitation) {
		LigneProgrammationHabilitationDTO ligne = new LigneProgrammationHabilitationDTO();

		ligne.setArticle(habilitation.getArticle());
		ligne.setCodification(habilitation.getCodification());
		ligne.setConvention(habilitation.getConvention());
		ligne.setConventionDepot(habilitation.getConventionDepot());
		ligne.setDatePrevisionnelleCM(habilitation.getDatePrevisionnelleCM() != null ? habilitation
				.getDatePrevisionnelleCM().getTime() : null);
		ligne.setDatePrevisionnelleSaisineCE(habilitation.getDatePrevisionnelleSaisineCE() != null ? habilitation
				.getDatePrevisionnelleSaisineCE().getTime() : null);
		ligne.setDateTerme(habilitation.getDateTerme() != null ? habilitation.getDateTerme().getTime() : null);
		ligne.setLegislature(habilitation.getLegislature());
		ligne.setMinisterePilote(getNor(habilitation.getMinisterePilote()));
		ligne.setNumeroOrdre(habilitation.getNumeroOrdre());
		ligne.setObjetRIM(habilitation.getObjetRIM());
		ligne.setObservation(habilitation.getObservation());

		return ligne;
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

	public void setListProgrammation(List<LigneProgrammationHabilitationDTO> listProgrammation) {
		this.listProgrammation = listProgrammation;
	}

	public List<LigneProgrammationHabilitationDTO> getListProgrammation() {
		return listProgrammation;
	}

	public List<LigneProgrammationHabilitation> remapField(List<LigneProgrammationHabilitation> list) {
		for (LigneProgrammationHabilitationDTO ligneProgrammationHabilitationDTO : getListProgrammation()) {
			LigneProgrammationHabilitation ligneProgrammation = new LigneProgrammationHabilitationImpl();
			ligneProgrammation.setArticle(ligneProgrammationHabilitationDTO.getArticle());

			if (ligneProgrammationHabilitationDTO.getDatePrevisionnelleSaisineCE() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(ligneProgrammationHabilitationDTO.getDatePrevisionnelleSaisineCE());
				ligneProgrammation.setDatePrevisionnelleSaisineCE(cal);
			}
			if (ligneProgrammationHabilitationDTO.getDatePrevisionnelleCM() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(ligneProgrammationHabilitationDTO.getDatePrevisionnelleCM());
				ligneProgrammation.setDatePrevisionnelleCM(cal);
			}
			if (ligneProgrammationHabilitationDTO.getDateTerme() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(ligneProgrammationHabilitationDTO.getDateTerme());
				ligneProgrammation.setDateTerme(cal);
			}

			ligneProgrammation.setMinisterePilote(ligneProgrammationHabilitationDTO.getMinisterePilote());
			ligneProgrammation.setNumeroOrdre(ligneProgrammationHabilitationDTO.getNumeroOrdre());

			ligneProgrammation.setObjetRIM(ligneProgrammationHabilitationDTO.getObjetRIM());
			ligneProgrammation.setObservation(ligneProgrammationHabilitationDTO.getObservation());
			ligneProgrammation.setConvention(ligneProgrammationHabilitationDTO.getConvention());
			ligneProgrammation.setConventionDepot(ligneProgrammationHabilitationDTO.getConventionDepot());

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
