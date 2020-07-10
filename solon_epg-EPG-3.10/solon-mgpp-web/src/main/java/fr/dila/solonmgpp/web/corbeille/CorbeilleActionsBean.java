package fr.dila.solonmgpp.web.corbeille;

import java.io.Serializable;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.web.client.DossierListingDTO;
import fr.dila.solonepg.web.espace.NavigationWebActionsBean;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.espace.EspaceParlementaireActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FicheLoiActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentation341ActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationAUDActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationAVIActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationDOCActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationDPGActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationDRActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationDecretActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationIEActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationJSSActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationOEPActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationSDActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;

/**
 * bean de gestion des corbeilles
 * 
 * @author asatre
 * 
 */
@Name("corbeilleActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.FRAMEWORK + 2)
public class CorbeilleActionsBean extends fr.dila.solonepg.web.espace.CorbeilleActionsBean implements Serializable {

  private static final long serialVersionUID = -5413163318304878731L;

  @In(create = true, required = false)
  protected transient NavigationContextBean navigationContext;

  @In(create = true, required = false)
  protected transient CorbeilleTreeBean corbeilleTree;

  @In(create = true, required = false)
  protected transient FacesMessages facesMessages;

  @In(create = true, required = false)
  protected transient FicheLoiActionsBean ficheLoiActions;

  @In(create = true, required = false)
  protected transient FichePresentation341ActionsBean fichePresentation341Actions;
  
  @In(create = true, required = false)
  protected transient FichePresentationAUDActionsBean fichePresentationAUDActions;
  
  @In(create = true, required = false)
  protected transient FichePresentationDOCActionsBean fichePresentationDOCActions;
  
  @In(create = true, required = false)
  protected transient FichePresentationJSSActionsBean fichePresentationJSSActions;
  
  @In(create = true, required = false)
  protected transient FichePresentationSDActionsBean fichePresentationSDActions;

  @In(create = true, required = false)
  protected transient FichePresentationDRActionsBean fichePresentationDRActions;

  @In(create = true, required = false)
  protected transient FichePresentationDecretActionsBean fichePresentationDecretActions;

  @In(create = true, required = false)
  protected transient FichePresentationOEPActionsBean fichePresentationOEPActions;

  @In(create = true, required = false)
  protected transient FichePresentationIEActionsBean fichePresentationIEActions;

  @In(create = true, required = false)
  protected transient FichePresentationAVIActionsBean fichePresentationAVIActions;
  
  @In(create = true, required = false)
  protected transient FichePresentationDPGActionsBean fichePresentationDPGActions;

  @In(create = true, required = false)
  protected transient NavigationWebActionsBean navigationWebActions;

  /**
   * Logger surcouche socle de log4j
   */
  private static final STLogger LOGGER = STLogFactory.getLog(CorbeilleActionsBean.class);

  public static final String REFRESH_CORBEILLE = "REFRESH_CORBEILLE";

  private String currentTaskType;

  /**
   * rafaichissement de la corbeille
   */
  @Observer(REFRESH_CORBEILLE)
  public void refreshCorbeille() {
    corbeilleTree.forceRefresh();
  }

	public String refreshEvenement() throws ClientException {
		DocumentModel currentDoc = navigationContext.getCurrentDocument();
		if (currentDoc != null) {
			DocumentModel evenementDoc = documentManager.getDocument(currentDoc.getRef());
			navigationContext.setCurrentDocument(evenementDoc);
		}

		EvenementDTO evtDTO = navigationContext.getCurrentEvenement();
		MessageDTO msgDTO = navigationContext.getCurrentMessage();
		if (evtDTO != null && msgDTO != null) {
			int currentMajeur = evtDTO.getVersionCouranteMajeur();
			int currentMineur = evtDTO.getVersionCouranteMineur();
			EvenementDTO evenementDTO;
			try {
				evenementDTO = SolonMgppServiceLocator.getEvenementService().findEvenement(
						msgDTO.getIdEvenement(), currentMajeur + "." + currentMineur,
						documentManager, false);
				navigationContext.setCurrentEvenement(evenementDTO);
			} catch (ClientException e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_EVENT_TEC, e);
				facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
				TransactionHelper.setTransactionRollbackOnly();
			}
		}

		corbeilleTree.forceRefreshNoReset();
		contentViewActions.refresh("corbeille_message_list");
		return SolonMgppViewConstant.VIEW_CORBEILLE_MESSAGE;
	}

  public String navigateToMessage(MessageDTO currentMessage, String contentViewName) throws ClientException {
    return corbeilleTree.navigateToMessage(currentMessage, contentViewName, Boolean.TRUE);
  }

	public String navigateToMessage(MessageDTO currentMessage, String contentViewName, Boolean reset) throws ClientException {
		return corbeilleTree.navigateToMessage(currentMessage, contentViewName, reset);
	}

  public String setCurrentVersion(int majeur, int mineur) {
    int currentMajeur = navigationContext.getCurrentEvenement().getVersionCouranteMajeur();
    int currentMineur = navigationContext.getCurrentEvenement().getVersionCouranteMineur();
    if (currentMajeur != majeur || currentMineur != mineur) {
      // load Evenement from version
      EvenementDTO evenementDTO;
      try {
        evenementDTO = SolonMgppServiceLocator.getEvenementService().findEvenement(navigationContext.getCurrentMessage().getIdEvenement(), majeur + "." + mineur, documentManager,false);
        navigationContext.setCurrentEvenement(evenementDTO);
      } catch (ClientException e) {
        LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_EVENT_TEC, e);
        facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
        TransactionHelper.setTransactionRollbackOnly();
      }
    }

    return null;
  }

  @Observer(EspaceParlementaireActionsBean.REFRESH_PAGE)
  public void resetTaskType() {
    setCurrentTaskType(null);
  }

  public void setCurrentTaskType(String currentTaskType) {
    this.currentTaskType = currentTaskType;
  }

  public String getCurrentTaskType() {
    return currentTaskType;
  }

  @Override
  public String readDossierLink(String dossierLinkId, String dossierDocId, Boolean read, String contentViewName, DossierListingDTO dossierListingDTO) throws ClientException {
    navigationContext.resetMGPPDocs();
    return super.readDossierLink(dossierLinkId, dossierDocId, read, contentViewName, dossierListingDTO);
  }

  @Override
  protected String navigateToEntry(String contentViewName, PageProvider<?> pageProvider) throws ClientException {

    if (navigationWebActions != null && navigationWebActions.isInEspaceParlemetaire()) {

      Object currentEntry = pageProvider.getCurrentEntry();
      if (currentEntry == null) {
        return null;
      }

      if (currentEntry instanceof MessageDTO) {
        return corbeilleTree.navigateToMessage((MessageDTO) currentEntry, contentViewName);
      } else if (currentEntry instanceof DocumentModel) {
        DocumentModel doc = (DocumentModel) currentEntry;
        if (doc.getType().equals(FicheLoi.DOC_TYPE)) {
          return ficheLoiActions.navigateToFicheLoi(doc, contentViewName);
        } else if (doc.getType().equals(FichePresentationOEP.DOC_TYPE)) {
          return fichePresentationOEPActions.navigateToOep(doc, contentViewName);
        } else if (doc.getType().equals(FichePresentation341.DOC_TYPE)) {
          return fichePresentation341Actions.navigateTo341(doc, contentViewName);
        } else if (doc.getType().equals(FichePresentationAVI.DOC_TYPE)) {
          return fichePresentationAVIActions.navigateToAVI(doc, contentViewName);
        } else if (doc.getType().equals(FichePresentationDecret.DOC_TYPE)) {
          return fichePresentationDecretActions.navigateToDecret(doc, contentViewName);
        } else if (doc.getType().equals(FichePresentationDR.DOC_TYPE)) {
          return fichePresentationDRActions.navigateToDR(doc, contentViewName);
        } else if (doc.getType().equals(FichePresentationIE.DOC_TYPE)) {
          return fichePresentationIEActions.navigateToIE(doc, contentViewName);
        } else if (doc.getType().equals(FichePresentationDPG.DOC_TYPE)){
          return fichePresentationDPGActions.navigateToFicheDPG(doc, contentViewName);
        } else if (doc.getType().equals(FichePresentationAUD.DOC_TYPE)){
          return fichePresentationAUDActions.navigateToFicheAUD(doc, contentViewName);
        } else if (doc.getType().equals(FichePresentationDOC.DOC_TYPE)){
          return fichePresentationDOCActions.navigateToFicheDOC(doc, contentViewName);
        } else if (doc.getType().equals(FichePresentationJSS.DOC_TYPE)){
          return fichePresentationJSSActions.navigateToFicheJSS(doc, contentViewName);
        } else if (doc.getType().equals(FichePresentationSD.DOC_TYPE)){
          return fichePresentationSDActions.navigateToFicheSD(doc, contentViewName);
        }
        else {
          return super.navigateToEntry(contentViewName, pageProvider);
        }
      }
    } else {
      return super.navigateToEntry(contentViewName, pageProvider);
    }

    return super.navigateToEntry(contentViewName, pageProvider);
  }
}
