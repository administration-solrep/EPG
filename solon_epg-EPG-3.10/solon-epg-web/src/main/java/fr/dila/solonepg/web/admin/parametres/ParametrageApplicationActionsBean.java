package fr.dila.solonepg.web.admin.parametres;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.contentview.jsf.ContentView;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.constant.SolonEpgParametrageApplicationConstants;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean seam de gestion du paramètrage de l'application (IHM).
 * 
 * @author arolin
 */
@Name("parametrageApplicationActions")
@Scope(CONVERSATION)
public class ParametrageApplicationActionsBean implements Serializable {

	private static final long											serialVersionUID	= 8000877213568214374L;

	@In(create = true, required = true)
	protected transient CoreSession										documentManager;

	@In(create = true, required = false)
	protected transient NavigationContextBean							navigationContext;

	@In(create = true, required = false)
	protected transient ParametrageApplicationAdministrationActionsBean	parametrageApplicationAdministrationActions;

	@In(create = true, required = false)
	protected transient ContentViewActions								contentViewActions;

	private static final Log											log					= LogFactory
																									.getLog(ParametrageApplicationActionsBean.class);

	/**
	 * Contient le nombre de dossiers par page à afficher par défaut. Cette valeur dépend du profil utilisateur.
	 */
	private Long														defaultDossierPageSize;

	/**
	 * Contient le nombre maximum de dossiers par page à afficher par défaut. Cette valeur dépend du paramétrage.
	 */
	private Long														maxDossierPageSize;

	/**
	 * Contient le délai de rafraichissement des corbeilles (en ms).
	 */
	private Long														delaiRafraichissementCorbeille;

	private Long														delaiAffichageMessage;

	/**
	 * remet à jour les paramètres de l'application.
	 */
	@Observer(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_CHANGED_EVENT)
	public void resetParametrageApplication() {
		delaiRafraichissementCorbeille = null;
		delaiAffichageMessage = null;
		init();
	}

	/**
	 * Initialisation du paramétrage profil utilisateur et paramétrage application (nombre de dossiers par page).
	 */
	@Create
	public void init() {
		defaultDossierPageSize = getUtilisateurPageSize();
		maxDossierPageSize = getMaxDossierPageSize();
	}

	/**
	 * Indique le nombre maximum d'item par page ; utilisé pour limiter l'affichage du widget de sélection du nombre
	 * d'éléments par page.
	 * 
	 * Pour les useGlobalPageSize : utilisation du paramétrage du nombre de dossier
	 * Pour les contentView avec un provider fournissant un maxPageSize : contentProvider.maxPageSize
	 * Sinon 20
	 * 
	 * Méthode utilisée dans page_number_list.xhtml et content_view_result_layout_selector.xhtml (pages surchargées
	 * dans EPG) pour contrôler l'affichage du sélecteur du nombre d'éléments par page.
	 * 
	 * Le contentView est passé en paramètre de la méthode car la valeur est disponible dans la JSP au moment de
	 * l'appel, alors {@link ContentViewActions#getCurrentContentView()} n'est pas encore défini.
	 */
	public Long getMaxPageSize(ContentView contentView) {
		if (contentView.getUseGlobalPageSize()) {
			return maxDossierPageSize;
		} else if (contentView.getCurrentPageProvider() != null) {
			return contentView.getCurrentPageProvider().getMaxPageSize();
		} else {
			return 20l;
		}
	}

	/**
	 * Récupère le délai de rafraichissement des corbeilles (en ms).
	 * 
	 * @return le délai de rafraichissement des corbeilles (en ms).
	 */
	public Long getDelaiRafraichissementCorbeille() {
		if (delaiRafraichissementCorbeille == null) {
			delaiRafraichissementCorbeille = 3000000L;
			ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator
					.getParametreApplicationService();
			try {
				ParametrageApplication parametrageApplicationDoc = parametreApplicationService
						.getParametreApplicationDocument(documentManager);
				delaiRafraichissementCorbeille = parametrageApplicationDoc.getDateRafraichissementCorbeille();
				if (delaiRafraichissementCorbeille != null) {
					// conversion minute => milli-seconde
					delaiRafraichissementCorbeille = delaiRafraichissementCorbeille * 60 * 1000;
				}
			} catch (ClientException e) {
				log.warn("erreur lors de la récupération du délai de rafraichissement des corbeilles", e);
			}
		}
		return delaiRafraichissementCorbeille;
	}

	/**
	 * Récupère le délai de rafraichissement des corbeilles (en ms).
	 * 
	 * @return le délai de rafraichissement des corbeilles (en ms).
	 */
	public Long getDelaiAffichageMessage() {
		if (delaiAffichageMessage == null) {
			ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator
					.getParametreApplicationService();
			try {
				ParametrageApplication parametrageApplicationDoc = parametreApplicationService
						.getParametreApplicationDocument(documentManager);
				delaiAffichageMessage = parametrageApplicationDoc.getDelaiAffichageMessage();
			} catch (ClientException e) {
				log.warn("erreur lors de la récupération du délai d'affichage des messages", e);
			}
		}
		return delaiAffichageMessage == null ? 5000L : delaiAffichageMessage * 1000;
	}

	/**
	 * Attribut cible du select:value dans page_number_list.xhtml et content_view_result_layout_selector.xhtml.
	 * 
	 * La cible dépend du paramétrage la contentView : si useGlobalPageSize, on set la valeur du globalPageSize, sinon
	 * on configure seulement la contentView.
	 */
	public void setCurrentPageSize(Long currentPageSize) {
		ContentView contentView = this.contentViewActions.getCurrentContentView();
		if (contentView != null) {
			contentView.setCurrentPageSize(currentPageSize);
			if (contentView.getUseGlobalPageSize()) {
				contentViewActions.setGlobalPageSize(currentPageSize);
			}
		}
	}

	/**
	 * Attribut cible du select:value dans page_number_list.xhtml et content_view_result_layout_selector.xhtml.
	 * 
	 * Point particulier : si contentView.globalPageSize est null et contentView.useGlobalPageSize == true, alors
	 * on initialise la valeur avec le paramétrage applicatif du nombre de dossiers maximum par page.
	 */
	public Long getCurrentPageSize() {
		if (contentViewActions.getCurrentContentView().getUseGlobalPageSize()
				// contentViewActions#getGlobalPageSize is null before it is explicitly set
				&& contentViewActions.getGlobalPageSize() == null) {
			contentViewActions.setGlobalPageSize(defaultDossierPageSize);
			return contentViewActions.getGlobalPageSize();
		} else {
			return contentViewActions.getCurrentContentView().getCurrentPageSize();
		}
	}

	/**
	 * Récupère le nombre de dossiers par défault à afficher par page pour l'utilisateur courant
	 */
	private Long getUtilisateurPageSize() {
		Long userPageSize = 10L;
		ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
		try {
			userPageSize = profilUtilisateurService.getUtilisateurPageSize(documentManager);
		} catch (ClientException e) {
			log.warn("erreur lors de la récupération du nb de dossier affiché pour l'utilisateur", e);
		}
		return userPageSize;
	}

	/**
	 * Récupère le nombre de dossier maximum par page, issu du paramétrage applicatif
	 */
	public Long getMaxDossierPageSize() {
		Long dossierDefaultPageSize = 20L;
		ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator
				.getParametreApplicationService();
		try {
			ParametrageApplication parametrageApplicationDoc = parametreApplicationService
					.getParametreApplicationDocument(documentManager);
			dossierDefaultPageSize = parametrageApplicationDoc.getNbDossierPage();
		} catch (ClientException e) {
			log.warn("erreur lors de la récupération du nb de dossier affiché par défaut", e);
		}
		return dossierDefaultPageSize;
	}
}
