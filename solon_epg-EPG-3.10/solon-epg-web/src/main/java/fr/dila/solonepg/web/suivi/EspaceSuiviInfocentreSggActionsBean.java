package fr.dila.solonepg.web.suivi;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.documentmodel.ListeTraitementPapier;
import fr.dila.solonepg.api.service.ListeTraitementPapierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.web.birt.BirtReportActionsBean;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.organigramme.ProtocolarOrderComparator;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.web.context.NavigationContextBean;

@Name("espaceSuiviInfocentreSggActionsBean")
@Scope(ScopeType.CONVERSATION)
public class EspaceSuiviInfocentreSggActionsBean implements Serializable {
	private static final long					serialVersionUID	= 1L;

	@In(create = true, required = false)
	protected transient NavigationContextBean	navigationContext;

	@In(create = true)
	protected transient DocumentsListsManager	documentsListsManager;

	@In(create = true, required = true)
	protected transient BirtReportActionsBean	birtReportingActions;

	protected Date								dateDeCreationDeListe;

	protected ListeTraitementPapier				listeTraitementPapier;

	@In(create = true, required = true)
	protected transient CoreSession				documentManager;

	/**
	 * @return
	 */
	public String getCurrentListeTraitementPapierDossierIds() {
		final ListeTraitementPapier listeTraitementPapier = getListeTraitementPapier();
		final List<String> idsDossier = listeTraitementPapier.getIdsDossier();
		final StringBuffer buf = new StringBuffer();

		if (idsDossier != null && !idsDossier.isEmpty()) {
			for (int i = 0; i < idsDossier.size() - 1; i++) {
				buf.append("'" + idsDossier.get(i) + "',");
			}
			buf.append("'" + idsDossier.get(idsDossier.size() - 1) + "'");
		} else {
			buf.append("''");
			FacesMessages.instance().add(StatusMessage.Severity.WARN, "La liste est vide. Elle va être supprimé.");
		}

		return buf.toString();
	}

	/**
	 * Renvoie vrai si l'on doit afficher le bouton "à traiter en série"
	 * 
	 * @return vrai si l'on doit afficher le bouton "à traiter en série"
	 */
	public boolean isButtonToDisplay() {
		final ListeTraitementPapier listeTraitementPapier = getListeTraitementPapier();

		final List<DocumentModel> docs = documentsListsManager
				.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		if (docs != null
				&& !docs.isEmpty()
				&& listeTraitementPapier != null
				&& !listeTraitementPapier.getTypeListe().equals(
						SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION)) {
			return true;
		}
		return false;
	}

	public boolean isDeleteButtonToDisplay() {
		final ListeTraitementPapier listeTraitementPapier = getListeTraitementPapier();

		final List<DocumentModel> docs = documentsListsManager
				.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
		if (docs != null && !docs.isEmpty() && listeTraitementPapier != null) {
			return true;
		}
		return false;
	}

	public String supprimerDossiersDeLaliste() throws ClientException {
		final ListeTraitementPapier listeTraitementPapier = getListeTraitementPapier();
		final List<DocumentModel> docs = documentsListsManager
				.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);

		final ListeTraitementPapierService listeTraitementPapierService = SolonEpgServiceLocator
				.getListeTraitementPapierService();
		listeTraitementPapierService.supprimerDossiersDeLaListeUnrestricted(listeTraitementPapier, docs,
				documentManager);

		return SolonEpgViewConstant.ESPACE_SUIVI_INFOCENTRE_SGG;
	}

	public String getDemandeEpreuve() {
		return SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE;
	}

	public String getDemandePublication() {
		return SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION;
	}

	/**
	 * imprimer la liste des
	 * 
	 * @param liste
	 * @throws Exception
	 */
	public void imprimer(final DocumentModel liste) throws Exception {
		final HashMap<String, String> inputValues = new HashMap<String, String>();
		StringBuilder ministereParm = new StringBuilder();
		final List<EntiteNode> ministereList = STServiceLocator.getSTMinisteresService().getCurrentMinisteres();
		Collections.sort(ministereList, new ProtocolarOrderComparator());
		// Récupération de la liste des ministères
		for (final OrganigrammeNode node : ministereList) {
			ministereParm.append("$id$=").append(node.getId()).append(";;$label$=").append(node.getLabel())
					.append(";;&");
		}

		final ListeTraitementPapier listeTraitementPapier = liste.getAdapter(ListeTraitementPapier.class);
		inputValues.put("LISTEID_PARAM", listeTraitementPapier.getDocument().getId());
		inputValues.put("MINISTERES_PARAM", ministereParm.toString());
		if (SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE
				.equals(listeTraitementPapier.getTypeListe())) {
			// La liste de traitement de papier est de type signature
			birtReportingActions.generateWord(SolonEpgConstant.BIRT_REPORT_LISTE_DE_GESTION_MISE_EN_SIGNATURE,
					inputValues, "Liste_de_traitement_papier-" + listeTraitementPapier.getNumeroListe());
		} else if (SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE
				.equals(listeTraitementPapier.getTypeListe())) {
			// La liste de traitement de papier est de type demande epreuve
			birtReportingActions.generateWord(SolonEpgConstant.BIRT_REPORT_LISTE_DE_GESTION_DEMANDE_EPREUVE,
					inputValues, "Liste_de_traitement_papier-" + listeTraitementPapier.getNumeroListe());
		} else if (SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION
				.equals(listeTraitementPapier.getTypeListe())) {
			// La liste de traitement de papier est de type demande publication
			birtReportingActions.generateWord(SolonEpgConstant.BIRT_REPORT_LISTE_DE_GESTION_DEMANDE_PUBLICATION,
					inputValues, "Liste_de_traitement_papier-" + listeTraitementPapier.getNumeroListe());
		}
	}

	@Observer(STEventConstant.CURRENT_DOCUMENT_CHANGED_EVENT)
	public void resetListeTraitementPapier() {
		listeTraitementPapier = null;

	}

	/**
	 * Getter / Setter
	 */

	public String getIntituleListeTraitementPapier() {
		final ListeTraitementPapier listeTraitementPapier = getListeTraitementPapier();
		final String typeListe = listeTraitementPapier.getTypeListe();
		StringBuilder intitule = new StringBuilder();
		if (SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE.equals(typeListe)) {
			intitule.append(listeTraitementPapier.getTypeSignature());
		} else {
			intitule.append(getListeLabelPrefix(typeListe));
		}

		intitule.append(listeTraitementPapier.getNumeroListe().toString());
		return intitule.toString();
	}

	public String getListeLabelPrefix(final String typeListe) {
		if (SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE.equals(typeListe)) {
			return SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE_LABEL;
		} else if (SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION.equals(typeListe)) {
			return SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION_LABEL;
		}
		return "";
	}

	public ListeTraitementPapier getListeTraitementPapier() {
		if (listeTraitementPapier == null) {
			final DocumentModel docModel = navigationContext.getCurrentDocument();
			if (docModel != null
					&& SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DOCUMENT_TYPE.equals(docModel
							.getType())) {
				listeTraitementPapier = docModel.getAdapter(ListeTraitementPapier.class);
			}
		}
		return listeTraitementPapier;
	}

	/**
	 * retourne la date selectionne a minuit utilise pour l'ecran gestion de listes
	 * 
	 * @return la date selectionne a minuit utilise
	 */
	public String getSelectedDate() {
		if (dateDeCreationDeListe == null) {
			dateDeCreationDeListe = new Date();
		}
		final Calendar dua = Calendar.getInstance();

		dua.setTime(dateDeCreationDeListe);

		return DateLiteral.dateFormatter.print(dua.getTimeInMillis());
	}

	/**
	 * retourne la date selectionne + 1 jour a minuit utilise pour l'ecran gestion de listes
	 * 
	 * @return la date selectionne + 1 a minuit utilise
	 */
	public String getDatePlusOne() {
		if (dateDeCreationDeListe == null) {
			dateDeCreationDeListe = new Date();
		}
		final Calendar dua = Calendar.getInstance();
		dua.setTime(dateDeCreationDeListe);
		dua.add(Calendar.DAY_OF_YEAR, 1);
		return DateLiteral.dateFormatter.print(dua.getTimeInMillis());
	}

	public Date getDateDeCreationDeListe() {
		return DateUtil.copyDate(dateDeCreationDeListe);
	}

	public void setDateDeCreationDeListe(final Date dateDeCreationDeListe) throws ClientException {
		this.dateDeCreationDeListe = DateUtil.copyDate(dateDeCreationDeListe);
		// Réinitialise la liste sélectionnée si il y en avait une
		navigationContext.resetCurrentDocument();
	}
}
