package fr.dila.solonmgpp.web.filter;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.ui.web.api.WebActions;

import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.constant.SolonMgppBaseFunctionConstant;
import fr.dila.solonmgpp.api.constant.TypeEvenementConstants;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.HistoriqueDossierDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.espace.NavigationWebActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.sword.xsd.solon.epp.EtatMessage;

/**
 * Bean des filtres d'afichage
 * 
 * @author asatre
 * 
 */
@Name("filterActions")
@Scope(CONVERSATION)
public class FilterActionsBean implements Serializable {

	private static final long						serialVersionUID	= 7638406241296668526L;

	private static final STLogger					LOGGER				= STLogFactory.getLog(FilterActionsBean.class);

	@In(create = true)
	protected transient NavigationWebActionsBean	navigationWebActions;

	@In(create = true, required = false)
	protected transient NavigationContextBean		navigationContext;

	@In(create = true, required = false)
	protected transient WebActions					webActions;

	@In(create = true)
	protected SSPrincipal							ssPrincipal;

	@In(create = true, required = true)
	protected transient CoreSession					documentManager;

	public Boolean isFicheLoiVisible() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& (SolonMgppActionConstant.PROCEDURE_LEGISLATIVE.equals(navigationWebActions
						.getCurrentSecondMenuAction().getId()) || SolonMgppActionConstant.PUBLICATION
						.equals(navigationWebActions.getCurrentSecondMenuAction().getId()));
	}

	public Boolean isFichePresentationDepotRapportVisible() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.DEPOT_DE_RAPPORT.equals(navigationWebActions.getCurrentSecondMenuAction()
						.getId());
	}

	public Boolean isFichePresentationDPGVisible() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.DECLARATION_DE_POLITIQUE_GENERALE.equals(navigationWebActions
						.getCurrentSecondMenuAction().getId());
	}

	public Boolean isFichePresentationDOCVisible() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES.equals(navigationWebActions
						.getCurrentSecondMenuAction().getId());
	}

	public Boolean isFichePresentationAUDVisible() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.DEMANDE_AUDITION.equals(navigationWebActions.getCurrentSecondMenuAction()
						.getId());
	}

	public Boolean isFichePresentationIntervExtVisible() {
		return isInInterventionExterieure();
	}

	public Boolean isInInterventionExterieure() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.INTERVENTION_EXTERIEURE.equals(navigationWebActions
						.getCurrentSecondMenuAction().getId());
	}

	public Boolean isInOEP() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.DESIGNATION_OEP.equals(navigationWebActions.getCurrentSecondMenuAction()
						.getId());
	}

	public Boolean isInAUD() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.DEMANDE_AUDITION.equals(navigationWebActions.getCurrentSecondMenuAction()
						.getId());
	}

	public Boolean isInDOC() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES.equals(navigationWebActions
						.getCurrentSecondMenuAction().getId());
	}

	public Boolean isInDPG() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.DECLARATION_DE_POLITIQUE_GENERALE.equals(navigationWebActions
						.getCurrentSecondMenuAction().getId());
	}

	public Boolean isInSD() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.DECLARATION_SUR_UN_SUJET_DETERMINE.equals(navigationWebActions
						.getCurrentSecondMenuAction().getId());
	}

	public Boolean isInJSS() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C.equals(navigationWebActions
						.getCurrentSecondMenuAction().getId());
	}

	public Boolean isUpdater() {
		return ssPrincipal != null && ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.CORBEILLE_MGPP_UPDATER);
	}

	public Boolean isFondDossierVisible() {
		return isInOEP() || isInAVI() || isInInterventionExterieure() || isInAUD() || isInDOC() || isInDPG()
				|| isInSD() || isInJSS();
	}

	public Boolean isIn341() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RESOLUTION_ARTICLE_341.equals(navigationWebActions
						.getCurrentSecondMenuAction().getId());
	}

	public Boolean isInAVI() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.AVIS_NOMINATION.equals(navigationWebActions.getCurrentSecondMenuAction()
						.getId());
	}

	public Boolean isInDecret() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.DECRET.equals(navigationWebActions.getCurrentSecondMenuAction().getId());
	}

	public Boolean isInRechercheLoi() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RECHERCHE.equals(navigationWebActions.getCurrentSecondMenuAction().getId())
				&& SolonMgppActionConstant.RECHERCHE_FICHE_LOI.equals(navigationWebActions
						.getCurrentRechercheCategory());
	}

	public Boolean isInRechercheOEP() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RECHERCHE.equals(navigationWebActions.getCurrentSecondMenuAction().getId())
				&& SolonMgppActionConstant.RECHERCHE_FICHE_OEP.equals(navigationWebActions
						.getCurrentRechercheCategory());
	}

	public Boolean isInRechercheAVI() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RECHERCHE.equals(navigationWebActions.getCurrentSecondMenuAction().getId())
				&& SolonMgppActionConstant.RECHERCHE_FICHE_AVI.equals(navigationWebActions
						.getCurrentRechercheCategory());
	}

	public Boolean isInRechercheDR() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RECHERCHE.equals(navigationWebActions.getCurrentSecondMenuAction().getId())
				&& SolonMgppActionConstant.RECHERCHE_FICHE_DR
						.equals(navigationWebActions.getCurrentRechercheCategory());
	}

	public Boolean isInRechercheDPG() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RECHERCHE.equals(navigationWebActions.getCurrentSecondMenuAction().getId())
				&& SolonMgppActionConstant.RECHERCHE_FICHE_DPG.equals(navigationWebActions
						.getCurrentRechercheCategory());
	}

	public Boolean isInRechercheSD() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RECHERCHE.equals(navigationWebActions.getCurrentSecondMenuAction().getId())
				&& SolonMgppActionConstant.RECHERCHE_FICHE_SD
						.equals(navigationWebActions.getCurrentRechercheCategory());
	}

	public Boolean isInRechercheJSS() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RECHERCHE.equals(navigationWebActions.getCurrentSecondMenuAction().getId())
				&& SolonMgppActionConstant.RECHERCHE_FICHE_JSS.equals(navigationWebActions
						.getCurrentRechercheCategory());
	}

	public Boolean isInRechercheAUD() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RECHERCHE.equals(navigationWebActions.getCurrentSecondMenuAction().getId())
				&& SolonMgppActionConstant.RECHERCHE_FICHE_AUD.equals(navigationWebActions
						.getCurrentRechercheCategory());
	}

	public Boolean isInRechercheDOC() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RECHERCHE.equals(navigationWebActions.getCurrentSecondMenuAction().getId())
				&& SolonMgppActionConstant.RECHERCHE_FICHE_DOC.equals(navigationWebActions
						.getCurrentRechercheCategory());
	}

	public Boolean isInRechercheDecret() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RECHERCHE.equals(navigationWebActions.getCurrentSecondMenuAction().getId())
				&& SolonMgppActionConstant.RECHERCHE_FICHE_DECRET.equals(navigationWebActions
						.getCurrentRechercheCategory());
	}

	public Boolean isInRecherche341() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RECHERCHE.equals(navigationWebActions.getCurrentSecondMenuAction().getId())
				&& SolonMgppActionConstant.RECHERCHE_FICHE_341.equals(navigationWebActions
						.getCurrentRechercheCategory());
	}

	public Boolean isInRechercheIE() {
		return navigationWebActions.getCurrentSecondMenuAction() != null
				&& SolonMgppActionConstant.RECHERCHE.equals(navigationWebActions.getCurrentSecondMenuAction().getId())
				&& SolonMgppActionConstant.RECHERCHE_FICHE_IE
						.equals(navigationWebActions.getCurrentRechercheCategory());
	}

	public Boolean isHistoVisible() {
		return StringUtils.isNotBlank(navigationContext.getCurrentIdDossier()) && isUpdater();
	}

	public Boolean isNonTraite() {
		return (navigationContext.getCurrentMessage() != null)
				&& EtatMessage.NON_TRAITE.value().equals(navigationContext.getCurrentMessage().getEtatMessage());
	}

	public Boolean isTraiterVisible() {
		return (navigationContext.getCurrentMessage() != null)
				&& (EtatMessage.EN_COURS_TRAITEMENT.value().equals(
						navigationContext.getCurrentMessage().getEtatMessage()) || EtatMessage.NON_TRAITE.value()
						.equals(navigationContext.getCurrentMessage().getEtatMessage()));
	}

	public Boolean peutMettreEnAttente() throws ClientException {
		return (navigationContext.getCurrentEvenement() != null)
				&& (TypeEvenementConstants.TYPE_EVENEMENT_EVT02.equals(navigationContext.getCurrentEvenement()
						.getTypeEvenementName()) || (TypeEvenementConstants.TYPE_EVENEMENT_EVT04BIS
						.equals(navigationContext.getCurrentEvenement().getTypeEvenementName()) && isPropositionLoi()))
				&& navigationContext.getCurrentMessage() != null
				&& !navigationContext.getCurrentMessage().isEnAttente();
	}

	public Boolean isEnAttente() {
		return navigationContext.getCurrentMessage() != null && navigationContext.getCurrentMessage().isEnAttente();
	}

	public Boolean peutLierUnOEP() {
		if (navigationContext.getCurrentMessage() != null
				&& TypeEvenementConstants.isEvenementOEP(navigationContext.getCurrentMessage().getTypeEvenement())) {

			try {
				FichePresentationOEP fpOEPSelected = SolonMgppServiceLocator.getDossierService().findFicheOEP(
						documentManager, navigationContext.getCurrentMessage().getIdDossier());

				// on check dans les dossiers liées
				if (fpOEPSelected == null) {
					fpOEPSelected = SolonMgppServiceLocator.getDossierService().findFicheOEPbyIdDossierEPP(
							documentManager, navigationContext.getCurrentMessage().getIdDossier());
				}
				// on arrête les bêtises et on affiche le bouton. Une fiche OEP n'a peut pas pu être créée donc il faut
				// pouvoir lier la communication
				if (fpOEPSelected == null) {
					return true;
				}

				// s'il l'identifiant commun n'est composé que de chiffre, alors on n'affiche pas le bouton. Il s'agit
				// probablement d'une communication "bien renseignée".
				if (fpOEPSelected.getIdCommun().matches("\\d*")) {
					return false;
				}

				List<String> idDossiersEPPLies = Arrays.asList(fpOEPSelected.getIdsANATLies().split(";"));
				// on est sûr que ce n'est pas une fiche créée automatiquement donc on n'affiche pas le bouton
				if (idDossiersEPPLies.size() >= 2) {
					return false;
				}

				return true;

			} catch (ClientException e1) {
				LOGGER.info(documentManager, MgppLogEnumImpl.FAIL_GET_FICHE_PRESENTATION_TEC, e1);
				return true;
			}
		}
		return false;
	}

	private boolean isPropositionLoi() throws ClientException {
		boolean isProp = false;
		String idDossier = navigationContext.getCurrentIdDossier();
		HistoriqueDossierDTO historiqueDTO = SolonMgppServiceLocator.getDossierService().findDossier(idDossier,
				documentManager);
		if (historiqueDTO.getRootEvents() != null) {
			for (EvenementDTO evenementDTO : historiqueDTO.getRootEvents().values()) {
				if (TypeEvenementConstants.TYPE_EVENEMENT_EVT02.equals(evenementDTO.getTypeEvenementName())) {
					return true;
				}
			}
		}
		return isProp;
	}

}
