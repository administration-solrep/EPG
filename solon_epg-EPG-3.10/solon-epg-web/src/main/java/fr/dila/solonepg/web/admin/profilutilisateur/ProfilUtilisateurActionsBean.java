package fr.dila.solonepg.web.admin.profilutilisateur;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.constant.SolonEpgLayoutConstant;
import fr.dila.solonepg.api.constant.SolonEpgProfilUtilisateurConstants;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean seam de gestion du profil utilisateur (IHM) : gestion de l'affichage des colonnes dans les layouts de l'espace
 * de traitement et de création.
 * 
 * @author arolin
 */
@Name("profilUtilisateurActions")
@Scope(CONVERSATION)
public class ProfilUtilisateurActionsBean implements Serializable {

	private static final long										serialVersionUID	= 8000877213568214374L;

	@In(create = true, required = false)
	protected transient CoreSession									documentManager;

	@In(create = true, required = false)
	protected transient NavigationContextBean						navigationContext;

	@In(create = true, required = false)
	protected transient ProfilUtilisateurAdministrationActionsBean	profilUtilisateurAdministrationActions;

	private static final Log										LOG					= LogFactory
																								.getLog(ProfilUtilisateurActionsBean.class);

	private static final Set<String>								LAYOUTS_DOSSIER		= new HashSet<String>();

	static {
		LAYOUTS_DOSSIER.add(SolonEpgLayoutConstant.ESPACE_TRAITEMENT_LAYOUT);
		LAYOUTS_DOSSIER.add(SolonEpgLayoutConstant.DOSSIER_LISTING_LAYOUT);
		LAYOUTS_DOSSIER.add(SolonEpgLayoutConstant.DOSSIER_LISTING_DTO_LAYOUT);
		LAYOUTS_DOSSIER.add(SolonEpgLayoutConstant.DOSSIER_LISTING_ADMIN_LAYOUT);
		LAYOUTS_DOSSIER.add(SolonEpgLayoutConstant.ESPACE_CREATION_LAYOUT);
		LAYOUTS_DOSSIER.add(SolonEpgLayoutConstant.RECHERCHE_DOSSIER_LAYOUT);
	}

	/**
	 * Contient les identifiants des colonnes à afficher de l'espace de traitement.
	 */
	private Set<String>												espaceTraitementColumnsToDisplayIds;

	/**
	 * Contient les identifiants des colonnes à afficher des instances de feiulle de route.
	 */
	private Set<String>												feuilleRouteInstanceColumnsToDisplayIds;

	/**
	 * Définit si l'on affiche ou non la colonne.
	 * 
	 * @param columnId
	 * @return boolean
	 * @throws ClientException
	 */
	public boolean hideColumn(String nomLayout, String columnId) throws ClientException {
		// si le layout n'est pas défini, on masque la colonne
		if (nomLayout == null || nomLayout.isEmpty()) {
			return true;
		}

		// récupération de la liste des colonnes visibles
		Set<String> columnsToDisplayIds = null;
		if (isLayoutDossier(nomLayout)) {
			columnsToDisplayIds = getEspaceTraitementColumnsToDisplayIds();
		} else if (isLayoutFdr(nomLayout)) {
			columnsToDisplayIds = getFeuilleRouteInstanceColumnsToDisplayIds();
		} else {
			// si le layout n'est pas un layout pour dossier ou instance de feuille de route, on affiche la colonne
			return false;
		}

		// vérifie que l'identifiant de la colonne appartient à la liste des colonnes visibles
		if (columnsToDisplayIds != null && !columnsToDisplayIds.isEmpty()) {
			if (columnId == null || columnId.isEmpty() || columnsToDisplayIds.contains(columnId)) {
				return false;
			}
		}
		return true;
	}

	@Observer(SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_CHANGED_EVENT)
	public void resetProfilUtilisateur() {
		espaceTraitementColumnsToDisplayIds = null;
		feuilleRouteInstanceColumnsToDisplayIds = null;
	}

	public Set<String> getEspaceTraitementColumnsToDisplayIds() throws ClientException {
		if (espaceTraitementColumnsToDisplayIds == null) {
			ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
			List<String> columns = profilUtilisateurService.getUserEspaceTraitementColumn(documentManager);
			if (columns == null) {
				columns = new ArrayList<String>();
			}
			espaceTraitementColumnsToDisplayIds = new HashSet<String>(columns);
		}
		return espaceTraitementColumnsToDisplayIds;
	}

	public Set<String> getFeuilleRouteInstanceColumnsToDisplayIds() throws ClientException {
		if (feuilleRouteInstanceColumnsToDisplayIds == null) {
			ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
			feuilleRouteInstanceColumnsToDisplayIds = new HashSet<String>(
					profilUtilisateurService.getUserFeuilleRouteInstanceColumn(documentManager));
		}
		return feuilleRouteInstanceColumnsToDisplayIds;
	}

	/**
	 * Renvoie vrai si le layout s'applique à un dossier
	 */
	protected boolean isLayoutDossier(String nomLayout) {
		return LAYOUTS_DOSSIER.contains(nomLayout);
	}

	/**
	 * Renvoie vrai si le layout s'applique à une instance de feuille de route
	 */
	protected boolean isLayoutFdr(String nomLayout) {
		return SolonEpgLayoutConstant.FEUILLE_ROUTE_LAYOUT.equals(nomLayout);
	}

	/**
	 * Récupère la liste des derniers dossiers traités par l'utilisateur sous forme de liste de documentModel
	 */
	public List<DocumentModel> getListeDerniersDossiersIntervention() {
		List<DocumentModel> listeDerniersDocumentIntervention = new ArrayList<DocumentModel>();
		try {
			ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
			final DocumentModel userProfilDoc = profilUtilisateurService.getOrCreateCurrentUserProfil(documentManager);
			ProfilUtilisateur userProfil = userProfilDoc.getAdapter(ProfilUtilisateur.class);
			// Récupération de la liste des ids des documents
			String dossiersIds = userProfil.getDerniersDossiersIntervention();
			if (StringUtil.isNotBlank(dossiersIds)) {
				String[] dossiersIdsArray = dossiersIds.split(",");
				for (String idDossier : dossiersIdsArray) {
					if (StringUtil.isNotBlank(idDossier)) {
						// On recherche le documentModel à partir de son id
						DocumentModel dossierDoc = documentManager.getDocument(new IdRef(idDossier));
						if (dossierDoc != null) {
							listeDerniersDocumentIntervention.add(dossierDoc);
						}
					}
				}
			}
		} catch (ClientException e) {
			// plantage lors de la récupération du profil utilisateur. On logge l'erreur
			LOG.error("Erreur lors de la récupération du profil utilisateur. " + e.getMessage());
			if (LOG.isDebugEnabled()) {
				LOG.debug(e);
			}
		}
		return listeDerniersDocumentIntervention;
	}

	/**
	 * Récupère le nombre dossiers qui, par défaut, doivent s'afficher dans les corbeilles de l'utilisateur. La valeur retournée est 10 par
	 * défaut au cas où elle n'est pas trouvée dans la base.
	 */
	public Long getNbDossiersVisiblesMax() {
		Long userPageSize = 10L;

		ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();

		try {
			userPageSize = profilUtilisateurService.getUtilisateurPageSize(documentManager);
		} catch (ClientException e) {
			LOG.error("Erreur lors de la récupération du nombre de dossiers à afficher pour l'utilisateur. " + e.getMessage());
			if (LOG.isDebugEnabled()) {
				LOG.debug(e);
			}
		}

		return userPageSize;
	}
}
