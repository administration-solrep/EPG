package fr.dila.solonepg.web.activitenormative;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.actions.ejb.ActionManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.cm.exception.CaseManagementRuntimeException;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.exception.ActiviteNormativeException;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.web.action.NavigationWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean Seam de l'espace d'activite normative (Traites et Accords)
 * 
 * @author asatre
 */
@Name("activiteNormativeTraitesActions")
@Scope(ScopeType.CONVERSATION)
public class ActiviteNormativeTraitesActionsBean implements Serializable {

	private static final long						serialVersionUID	= 2776349205588506388L;

	private static final Log						log					= LogFactory
																				.getLog(ActiviteNormativeTraitesActionsBean.class);

	@In(create = true, required = false)
	protected transient ActionManager				actionManager;

	@In(create = true, required = false)
	protected transient NavigationWebActionsBean	navigationWebActions;

	@In(create = true, required = false)
	protected transient NavigationContextBean		navigationContext;

	@In(create = true, required = true)
	protected transient CoreSession					documentManager;

	@In(create = true, required = false)
	protected transient FacesMessages				facesMessages;

	@In(create = true, required = false)
	protected transient ResourcesAccessor			resourcesAccessor;

	@In(required = true, create = true)
	protected SSPrincipal							ssPrincipal;

	private String									titre;
	private Date									dateSignature;
	private Boolean									publication;

	public String addToActiviteNormative() throws ClientException {
		if (StringUtils.isNotEmpty(titre) && dateSignature != null) {
			try {
				SolonEpgServiceLocator.getActiviteNormativeService().addTraiteToTableauMaitre(titre.trim(),
						dateSignature, publication, documentManager);
				STServiceLocator.getJournalService().journaliserActionPAN(documentManager, null,
						SolonEpgEventConstant.AJOUT_TM_EVENT,
						SolonEpgEventConstant.AJOUT_TM_COMMENT + " [" + titre.trim() + "]",
						SolonEpgEventConstant.CATEGORY_LOG_PAN_TRAITES_ACCORD);
				titre = null;
				dateSignature = null;
				publication = null;
			} catch (ActiviteNormativeException e) {
				String message = resourcesAccessor.getMessages().get(e.getMessage());
				facesMessages.add(StatusMessage.Severity.WARN, message);
			} catch (CaseManagementRuntimeException e1) {
				log.error(e1.getMessage());
				String message = "Le dossier est verouillé par un autre utilisateur, ajout impossible";
				facesMessages.add(StatusMessage.Severity.WARN, message);
			}
		}

		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

		return null;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getTitre() {
		return titre;
	}

	public Date getDateSignature() {
		return DateUtil.copyDate(dateSignature);
	}

	public void setDateSignature(Date dateSignature) {
		this.dateSignature = DateUtil.copyDate(dateSignature);
	}

	public Boolean getPublication() {
		return publication;
	}

	public void setPublication(Boolean publication) {
		this.publication = publication;
	}

}
