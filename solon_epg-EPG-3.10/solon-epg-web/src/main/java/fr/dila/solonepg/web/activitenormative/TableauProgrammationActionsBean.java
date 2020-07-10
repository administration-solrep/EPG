package fr.dila.solonepg.web.activitenormative;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.common.utils.i18n.I18NUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.cases.LigneProgrammation;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.core.dto.activitenormative.LigneProgrammationDTO;
import fr.dila.solonepg.core.dto.activitenormative.TableauDeProgrammationLoiDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean Seam de gestion du tableau de programmation et de suivi de l'activite normative
 * 
 * @author asatre
 */
@Name("tableauProgrammationActions")
@Scope(ScopeType.CONVERSATION)
public class TableauProgrammationActionsBean implements Serializable {

	private static final long					serialVersionUID	= 2776349205588506388L;

	@In(create = true, required = false)
	protected transient NavigationContextBean	navigationContext;

	@In(create = true, required = true)
	protected transient CoreSession				documentManager;

	@In(required = true, create = true)
	protected SSPrincipal						ssPrincipal;

	@In(create = true, required = false)
	protected transient ResourcesAccessor		resourcesAccessor;

	private TableauDeProgrammationLoiDTO		tableauDeProgrammationLoiDTO;

	private Boolean								masquerApplique		= Boolean.TRUE;
	
	@In(create = true, required = false)
    protected FacesMessages facesMessages;
	
	private static final String BUNDLE = "messages";

	/**
	 * Ajouter pour f:convertDateTime
	 * 
	 * @return
	 */
	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	/**
	 * Construction du tableau de programmation de la loi
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<LigneProgrammationDTO> getCurrentListProgrammationLoi() throws ClientException {
		ActiviteNormativeProgrammation activiteNormativeProgrammation = navigationContext.getCurrentDocument()
				.getAdapter(ActiviteNormativeProgrammation.class);
		tableauDeProgrammationLoiDTO = new TableauDeProgrammationLoiDTO(activiteNormativeProgrammation,
				documentManager, Boolean.TRUE, Boolean.FALSE);
		return tableauDeProgrammationLoiDTO.getListProgrammation();

	}

	/**
	 * Construction du tableau de suivi de la loi
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<LigneProgrammationDTO> getCurrentListSuiviLoi() throws ClientException {
		ActiviteNormativeProgrammation activiteNormativeProgrammation = navigationContext.getCurrentDocument()
				.getAdapter(ActiviteNormativeProgrammation.class);
		tableauDeProgrammationLoiDTO = new TableauDeProgrammationLoiDTO(activiteNormativeProgrammation,
				documentManager, Boolean.FALSE, masquerApplique);
		return tableauDeProgrammationLoiDTO.getListProgrammation();

	}

	/**
	 * Verouillage du tableau de programmation
	 * 
	 * @return
	 */
	public String lockCurrentProgrammationLoi() throws ClientException {
		if (tableauDeProgrammationLoiDTO != null) {
			DocumentModel activiteNormativeDoc = navigationContext.getCurrentDocument();
			List<LigneProgrammation> lignesProgrammations = tableauDeProgrammationLoiDTO.remapField(documentManager,
					activiteNormativeDoc);
			SolonEpgServiceLocator.getActiviteNormativeService().saveCurrentProgrammationLoi(lignesProgrammations,
					activiteNormativeDoc.getAdapter(ActiviteNormative.class), documentManager);
		}

		return null;
	}

	/**
	 * Deverouillage du tableau de programmation
	 * 
	 * @return
	 */
	public String unlockCurrentProgrammationLoi() throws ClientException {
		if (tableauDeProgrammationLoiDTO != null) {
			SolonEpgServiceLocator.getActiviteNormativeService().removeCurrentProgrammationLoi(
					navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class), documentManager);
		}

		return null;
	}

	public String getTableauProgrammationLockInfo() {
		ActiviteNormativeProgrammation activiteNormativeProgrammation = navigationContext.getCurrentDocument()
				.getAdapter(ActiviteNormativeProgrammation.class);
		if (StringUtils.isNotEmpty(activiteNormativeProgrammation.getLockUser())) {
			return "Figé le " + DateUtil.formatForClient(activiteNormativeProgrammation.getLockDate().getTime())
					+ ", par " + activiteNormativeProgrammation.getLockUser();
		}
		return "";
	}

	public Boolean isCurrentProgrammationLoiLocked() {
		ActiviteNormativeProgrammation activiteNormativeProgrammation = navigationContext.getCurrentDocument()
				.getAdapter(ActiviteNormativeProgrammation.class);
		return StringUtils.isEmpty(activiteNormativeProgrammation.getLockUser());
	}

	/**
	 * Verouillage du tableau de programmation
	 * 
	 * @return
	 */
	public String sauvegarderTableauSuivi() throws ClientException {
		if (tableauDeProgrammationLoiDTO != null) {

			DocumentModel activiteNormativeDoc = navigationContext.getCurrentDocument();
			List<LigneProgrammation> lignesProgrammations = tableauDeProgrammationLoiDTO.remapField(documentManager,
					activiteNormativeDoc);
			SolonEpgServiceLocator.getActiviteNormativeService().publierTableauSuivi(lignesProgrammations,
					activiteNormativeDoc.getAdapter(ActiviteNormative.class), documentManager);
		}

		return null;
	}
	 
	public void publierTableauSuivi() throws ClientException {
		sauvegarderTableauSuivi();

		ActiviteNormativeService actNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();

		actNormativeService.publierTableauSuiviHTML(navigationContext.getCurrentDocument(), documentManager,
				masquerApplique, false, true);

		if (actNormativeService.isPublicationEcheancierBdjActivated(documentManager)) {
			DocumentModel currentDocument = navigationContext.getCurrentDocument();
			
			// S'il n'y a pas de législature de publication, on affiche un message
			if (StringUtil.isEmpty(currentDocument.getAdapter(TexteMaitre.class).getLegislaturePublication())) {
				final Locale locale = Locale.FRENCH;
				facesMessages.add(StatusMessage.Severity.INFO, I18NUtils.getMessageString(BUNDLE,
						"activite.normative.tableau.suivi.publier.alerte.legislature", null, locale));
			}
			
			// Récupération de la législature en cours et de la précédente
			SolonEpgParametreService paramService = SolonEpgServiceLocator.getSolonEpgParametreService();
			ParametrageAN param = paramService.getDocAnParametre(documentManager);
			String legislatureEnCours = param.getLegislatureEnCours();
			String legislaturePrecPublication = param.getLegislaturePrecPublication();

			// Si la législature du document courant est celle en court ou la précédente
			String legislature = currentDocument.getAdapter(TexteMaitre.class).getLegislaturePublication();
			if (StringUtil.isNotEmpty(legislature) && !legislature.equals(legislatureEnCours)
					&& !legislature.equals(legislaturePrecPublication)) {
				final Locale locale = Locale.FRENCH;
				facesMessages.add(StatusMessage.Severity.INFO, I18NUtils.getMessageString(BUNDLE,
						"activite.normative.tableau.suivi.publier.alerte.invalidLegislature", null, locale));
			}
			actNormativeService.publierEcheancierBDJ(currentDocument, documentManager);
		}
	}
	
	public String getTableauSuiviPublicationInfo() {
		ActiviteNormativeProgrammation activiteNormativeProgrammation = navigationContext.getCurrentDocument()
				.getAdapter(ActiviteNormativeProgrammation.class);
		if (StringUtils.isNotEmpty(activiteNormativeProgrammation.getTableauSuiviPublicationUser())) {
			return "Publié le "
					+ DateUtil.formatForClient(activiteNormativeProgrammation.getTableauSuiviPublicationDate()
							.getTime()) + ", par " + activiteNormativeProgrammation.getTableauSuiviPublicationUser();
		}
		return "";
	}

	public String refreshCurrentProgrammationLoi() {
		return null;
	}

	public Boolean getMasquerApplique() {
		return masquerApplique;
	}

	public void setMasquerApplique(Boolean masquerApplique) {
		this.masquerApplique = masquerApplique;
	}

	/**
	 * inversion de l'affichage des mesures appliquées
	 * 
	 * @return
	 */
	public String invertMasquerApplique() {
		setMasquerApplique(!getMasquerApplique());
		return null;
	}

}
