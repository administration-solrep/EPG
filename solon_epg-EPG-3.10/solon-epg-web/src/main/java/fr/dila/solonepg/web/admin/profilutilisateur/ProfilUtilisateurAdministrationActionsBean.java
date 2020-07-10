package fr.dila.solonepg.web.admin.profilutilisateur;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.convert.Converter;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.ui.web.directory.VocabularyEntry;

import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgProfilUtilisateurConstants;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.converter.VocabularyEntryConverter;
import fr.dila.solonepg.web.recherche.SolonEpgVocSugUI;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean seam de gestion du profil utilisateur : edition des propriétés du profil utilisateur .
 * 
 * @author arolin
 */
@Name("profilUtilisateurAdministrationActions")
@Scope(CONVERSATION)
public class ProfilUtilisateurAdministrationActionsBean implements Serializable {

	private static final long					serialVersionUID	= 8000877213568214374L;

	@In(create = true, required = false)
	protected transient CoreSession				documentManager;

	@In(create = true, required = false)
	protected transient NavigationContextBean	navigationContext;

	protected String							errorName;

	protected Boolean							noPageError;

	@In(create = true, required = false)
	protected NuxeoPrincipal					currentUser;

	/**
	 * Liste des métadonnées proposées au utilisateurs comme colonnes dans l'espace de traitement
	 */
	protected List<VocabularyEntry>				sourceValueEspaceTraitement;

	/**
	 * Liste des métadonnées utilisées comme colonnes dans l'espace de traitement
	 */
	protected List<VocabularyEntry>				targetValueEspaceTraitement;

	/**
	 * Liste des métadonnées proposées au utilisateurs comme colonnes dans les instances de feuilles de route
	 */
	protected List<VocabularyEntry>				sourceValueInstanceFeuilleRoute;

	/**
	 * Liste des métadonnées utilisées comme colonnes dans les instances de feuilles de route
	 */
	protected List<VocabularyEntry>				targetValueInstanceFeuilleRoute;

	/**
	 * ProfilUtilisateur contenant les informations sur le profil utilisateur
	 */
	protected ProfilUtilisateur					profilUtilisateur;

	/**
	 * Converter de l'espace de traitement
	 */
	protected VocabularyEntryConverter			espaceTraitementConverter;

	/**
	 * Converter des instances de feuilles de route
	 */
	protected VocabularyEntryConverter			feuilleRouteConverter;

	/**
	 * Vrai si la popup de gestion du profil utilisateur doit être affichée
	 */
	protected Boolean							displayAdministrationProfil;

	/**
	 * Vrai si la popup de modification du mot de passe doit être affichée
	 */
	protected Boolean							displayResetPassword;

	public void resetProfilUtilisateur() {
		displayResetPassword = null;
		displayAdministrationProfil = null;
		profilUtilisateur = null;
		sourceValueEspaceTraitement = null;
		targetValueEspaceTraitement = null;
		targetValueInstanceFeuilleRoute = null;
		sourceValueInstanceFeuilleRoute = null;
		espaceTraitementConverter = null;
		feuilleRouteConverter = null;
	}

	/**
	 * Ajout d'un type d'acte dans la liste des types d'actes sélectionné pour la notification dans le profil
	 * utilisateur.
	 */
	public void addTypeActe(SolonEpgVocSugUI suggestion) throws ClientException {
		// on récupère le type d'acte sélectionné
		String typeActe = suggestion.getMotCleId();
		if (typeActe == null || typeActe.isEmpty()) {
			return;
		}
		List<String> listTypeActe = getProfilUtilisateur().getNotificationTypeActes();
		// on ajoute le format à la liste des formats autorisés si il n'est pas déjà contenu dans la liste
		if (!listTypeActe.contains(typeActe)) {
			listTypeActe.add(typeActe);
			profilUtilisateur.setNotificationTypeActes(listTypeActe);
		}
	}

	/**
	 * Enleve un type d'acte dans la liste des types d'actes sélectionné pour la notification dans le profil
	 * utilisateur.
	 */
	public void deleteTypeActe(String idVocabulaire) throws ClientException {
		// on récupère le type d'acte sélectionné
		if (idVocabulaire == null || idVocabulaire.isEmpty()) {
			return;
		}
		// on l'enlève de la liste des types d'actes sélectionné
		List<String> listTypeActe = getProfilUtilisateur().getNotificationTypeActes();
		listTypeActe.remove(idVocabulaire);
		profilUtilisateur.setNotificationTypeActes(listTypeActe);
	}

	/**
	 * Enregistrement du nouveau profil utilisateur.
	 * 
	 * @throws ClientException
	 */
	public void valider() throws ClientException {
		// récupération des listes de colonnes et de la liste des types d'actes, puis sauvegarde du document

		// récupération des listes de colonnes de l'espace de traitement
		List<String> idsDisplayedColumnsEspaceTraitement = new ArrayList<String>();
		if (targetValueEspaceTraitement != null && targetValueEspaceTraitement.size() > 0) {
			for (VocabularyEntry vocabularyEntry : targetValueEspaceTraitement) {
				idsDisplayedColumnsEspaceTraitement.add(vocabularyEntry.getId());
			}
		}
		profilUtilisateur.setIdsDisplayedColumnsEspaceTraitement(idsDisplayedColumnsEspaceTraitement);

		// récupération des listes de colonnes de l'espace de traitement
		List<String> idsDisplayedColumnsInstanceFeuilleRoute = new ArrayList<String>();
		if (targetValueInstanceFeuilleRoute != null && targetValueInstanceFeuilleRoute.size() > 0) {
			for (VocabularyEntry vocabularyEntry : targetValueInstanceFeuilleRoute) {
				idsDisplayedColumnsInstanceFeuilleRoute.add(vocabularyEntry.getId());
			}
		}
		profilUtilisateur.setIdsDisplayedColumnsInstanceFeuilleRoute(idsDisplayedColumnsInstanceFeuilleRoute);

		documentManager.saveDocument(profilUtilisateur.getDocument());
		resetProfilUtilisateur();
		Events.instance().raiseEvent(SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_CHANGED_EVENT);
		// Reset de la content view pour présenter directement le nouveau layout
		Events.instance().raiseEvent(ProviderBean.RESET_CONTENT_VIEW_EVENT);
	}

	/**
	 * Annulation des modifcations effectuée sur le profil utilisateur.
	 * 
	 * @throws ClientException
	 */
	public void annuler() throws ClientException {
		resetProfilUtilisateur();
	}

	/**
	 * Signale à l'application que l'on doit afficher la popup du profil utilisateur.
	 */
	public void displayAdministrationProfil() {
		setDisplayAdministrationProfil(true);
	}

	/**
	 * Signale à l'application que l'on doit afficher la popup de reset du mot de passe.
	 */
	public void displayResetPassword() {
		setDisplayResetPassword(true);
	}

	/**
	 * Renvoie vrai si l'utilisateur a le droit de voir des mesures nominatives.
	 * 
	 * @return vrai si l'utilisateur a le droit de voir des mesures nominatives.
	 */
	public boolean isVisibleMesureNominative() {
		List<String> groups = currentUser.getGroups();
		// on envoie un mail sur les mesures nominatives uniquement si l'utilisateur a le doit de voir des mesures
		// nominatives
		return groups.contains(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER);
	}

	// Getter & setter

	public Boolean isDisplayAdministrationProfil() {
		return displayAdministrationProfil;
	}

	public void setDisplayAdministrationProfil(Boolean displayAdministrationProfil) {
		this.displayAdministrationProfil = displayAdministrationProfil;
	}

	public Boolean isDisplayResetPassword() {
		return displayResetPassword;
	}

	public void setDisplayResetPassword(Boolean displayResetPassword) {
		this.displayResetPassword = displayResetPassword;
	}

	public ProfilUtilisateur getProfilUtilisateur() throws ClientException {
		if (profilUtilisateur == null) {
			ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
			profilUtilisateur = profilUtilisateurService.getOrCreateCurrentUserProfil(documentManager).getAdapter(
					ProfilUtilisateur.class);
		}
		return profilUtilisateur;
	}

	public void setProfilUtilisateur(ProfilUtilisateur profilUtilisateur) {
		this.profilUtilisateur = profilUtilisateur;
	}

	public String getErrorName() {
		return errorName;
	}

	public List<VocabularyEntry> getSourceValueEspaceTraitement() throws ClientException {
		if (sourceValueEspaceTraitement == null) {
			ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
			sourceValueEspaceTraitement = profilUtilisateurService
					.getVocEntryAllowedEspaceTraitementColumn(documentManager);
		}
		return sourceValueEspaceTraitement;
	}

	public void setSourceValueEspaceTraitement(List<VocabularyEntry> sourceValueEspaceTraitement) {
		this.sourceValueEspaceTraitement = sourceValueEspaceTraitement;
	}

	public List<VocabularyEntry> getTargetValueEspaceTraitement() throws ClientException {
		if (targetValueEspaceTraitement == null) {
			ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
			targetValueEspaceTraitement = profilUtilisateurService
					.getVocEntryUserEspaceTraitementColumn(documentManager);
		}
		return targetValueEspaceTraitement;
	}

	public void setTargetValueEspaceTraitement(List<VocabularyEntry> targetValueEspaceTraitement) {
		this.targetValueEspaceTraitement = targetValueEspaceTraitement;
	}

	public List<VocabularyEntry> getSourceValueInstanceFeuilleRoute() throws ClientException {
		if (sourceValueInstanceFeuilleRoute == null) {
			ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
			sourceValueInstanceFeuilleRoute = new ArrayList<VocabularyEntry>();
			sourceValueInstanceFeuilleRoute.addAll(profilUtilisateurService
					.getVocEntryAllowedFeuilleRouteInstanceColumn(documentManager));
		}
		return sourceValueInstanceFeuilleRoute;
	}

	public void setSourceValueInstanceFeuilleRoute(List<VocabularyEntry> sourceValueInstanceFeuilleRoute) {
		this.sourceValueInstanceFeuilleRoute = sourceValueInstanceFeuilleRoute;
	}

	public List<VocabularyEntry> getTargetValueInstanceFeuilleRoute() throws ClientException {
		if (targetValueInstanceFeuilleRoute == null) {
			ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
			targetValueInstanceFeuilleRoute = profilUtilisateurService
					.getVocEntryUserFeuilleRouteInstanceColumn(documentManager);
		}
		return targetValueInstanceFeuilleRoute;
	}

	public void setTargetValueInstanceFeuilleRoute(List<VocabularyEntry> targetValueInstanceFeuilleRoute) {
		this.targetValueInstanceFeuilleRoute = targetValueInstanceFeuilleRoute;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	public Boolean getNoPageError() {
		return noPageError;
	}

	public void setNoPageError(Boolean noPageError) {
		this.noPageError = noPageError;
	}

	public Converter getEspaceTraitementConverter() throws ClientException {
		if (espaceTraitementConverter == null) {
			List<VocabularyEntry> espaceTraitementVocabularyList = new ArrayList<VocabularyEntry>();
			espaceTraitementVocabularyList.addAll(getTargetValueEspaceTraitement());
			espaceTraitementVocabularyList.addAll(getSourceValueEspaceTraitement());
			espaceTraitementConverter = new VocabularyEntryConverter(espaceTraitementVocabularyList);
		}
		return espaceTraitementConverter;
	}

	public Converter getFeuilleRouteConverter() throws ClientException {
		if (feuilleRouteConverter == null) {
			List<VocabularyEntry> feuilleRouteVocabularyList = new ArrayList<VocabularyEntry>();
			feuilleRouteVocabularyList.addAll(getTargetValueInstanceFeuilleRoute());
			feuilleRouteVocabularyList.addAll(getSourceValueInstanceFeuilleRoute());
			feuilleRouteConverter = new VocabularyEntryConverter(feuilleRouteVocabularyList);
		}
		return feuilleRouteConverter;
	}
}
