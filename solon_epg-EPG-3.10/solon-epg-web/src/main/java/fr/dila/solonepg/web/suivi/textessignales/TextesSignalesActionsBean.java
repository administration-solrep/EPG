package fr.dila.solonepg.web.suivi.textessignales;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.contentview.jsf.ContentView;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.exception.TextesSignalesException;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.api.service.TexteSignaleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.client.TexteSignaleDTO;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.contentview.TexteSignalePageProvider;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.contentview.ColumnBeanManager;
import fr.dila.st.web.lock.STLockActionsBean;

/**
 * bean de gestion des textes signalés
 * 
 * @author asatre
 */
@Name("textesSignalesActions")
@Scope(ScopeType.CONVERSATION)
public class TextesSignalesActionsBean implements Serializable, ColumnBeanManager {

    private static final String TAB1 = "1";
    private static final String TAB2 = "0";

    private static final long serialVersionUID = 1L;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(create = true)
    protected transient NavigationContext navigationContext;

    @In(create = true, required = true)
    protected ContentViewActions contentViewActions;

    @In(create = true)
    protected transient DocumentsListsManager documentsListsManager;

    @In(create = true, required = false)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true, required = false)
    protected transient STLockActionsBean stLockActions;

    @In(required = true, create = true)
    protected SSPrincipal ssPrincipal;

    private String currentUserSubTab = TAB1;

    private String contentViewName = SolonEpgViewConstant.ESPACE_SUIVI_TEXTES_SIGNALES_CONTENT_VIEW;

    private String type;
    private Boolean showPopup;

    public void setCurrentUserSubTab(String currentUserSubTab) throws ClientException {
        navigationContext.setCurrentDocument(null);
        ContentView contentView = contentViewActions.getContentViewWithProvider(contentViewName);
        documentsListsManager.resetWorkingList(contentView.getSelectionListName());
        this.currentUserSubTab = currentUserSubTab;
        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
    }

    public String getCurrentUserSubTab() {
        return currentUserSubTab;
    }

    public void setContentViewName(String contentViewName) {
        this.contentViewName = contentViewName;
    }

    public String getContentViewName() {
        return contentViewName;
    }

    public String getParams() {

        if (TAB1.equals(currentUserSubTab)) {
            return TAB1;
        } else {
            return TAB2;
        }

    }

    public void verserDossierDansTextesSignales() throws ClientException {
        DocumentModel doc = navigationContext.getCurrentDocument();
        if (doc != null) {

            // Vérifie si le document est verrouillé par l'utilisateur en cours
            if (!stLockActions.isDocumentLockedByCurrentUser(doc)) {
                String errorMessage = resourcesAccessor.getMessages().get(STLockActionsBean.LOCK_LOST_ERROR_MSG);
                facesMessages.add(StatusMessage.Severity.WARN, errorMessage);
                return;
            }

            if (StringUtils.isBlank(type)) {
                SolonEpgVocabularyService vocabularyService = SolonEpgServiceLocator.getSolonEpgVocabularyService();

                DocumentModel voc = vocabularyService.getDocumentModelFromId(VocabularyConstants.VOCABULARY_TYPE_ACTE_DIRECTORY,
                        doc.getAdapter(Dossier.class).getTypeActe());
                String classification = (String) voc.getProperty(VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA,
                        VocabularyConstants.VOCABULARY_TYPE_ACTE_CLASSIFICATION);

                if ("0".equals(classification)) {
                    showPopup = Boolean.TRUE;
                    return;
                } else {
                    type = TAB1;
                }
            }
            TexteSignaleService texteSignaleService = SolonEpgServiceLocator.getTexteSignaleService();
            texteSignaleService.verser(documentManager, doc.getAdapter(Dossier.class), type);
        }

        cancelverserDossierDansTextesSignales();
    }

    public void retirerDossierDesTextesSignales(DocumentModel doc) throws ClientException {
        if (doc != null) {
            TexteSignaleService texteSignaleService = SolonEpgServiceLocator.getTexteSignaleService();
            texteSignaleService.retirer(documentManager, doc.getAdapter(Dossier.class));
        }
    }

    public void enregistrerTexteSignale(String idDossier, TexteSignaleDTO texteSignaleDTO) throws ClientException {
        TexteSignaleService texteSignaleService = SolonEpgServiceLocator.getTexteSignaleService();
        try {
            texteSignaleService.save(documentManager, idDossier, texteSignaleDTO.getDateDemandePublicationTS(),
                    texteSignaleDTO.getVecteurPublicationTS(), texteSignaleDTO.getObservation(), currentUserSubTab);
        } catch (TextesSignalesException e) {
            String message = resourcesAccessor.getMessages().get("texte.signale.droit.insuffisant");
            facesMessages.add(StatusMessage.Severity.INFO, MessageFormat.format(message, texteSignaleDTO.getNor()));
        }
    }

    public void saveAllTextesSignales() throws ClientException {
        ContentView contentView = contentViewActions.getContentViewWithProvider(contentViewName);
        TexteSignalePageProvider provider = (TexteSignalePageProvider) contentView.getPageProvider();
        Map<String, TexteSignaleDTO> textesSignalesDTOs = provider.getAllTexteSignaleDTO();
        for (Entry<String, TexteSignaleDTO> entry : textesSignalesDTOs.entrySet()) {
            enregistrerTexteSignale(entry.getKey(), textesSignalesDTOs.get(entry.getKey()));
        }
        documentsListsManager.resetWorkingList(contentView.getSelectionListName());
        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
    }

    public Boolean isInTextesSignales() {
        return contentViewActions.getCurrentContentView() != null
                && SolonEpgViewConstant.ESPACE_SUIVI_TEXTES_SIGNALES_CONTENT_VIEW.equals(contentViewActions.getCurrentContentView().getName());
    }

    public void removeSelectedTextesSignales() throws ClientException {
        String selectionListName = contentViewActions.getCurrentContentView().getSelectionListName();

        if (documentsListsManager.isWorkingListEmpty(selectionListName)) {
            return;
        }

        List<DocumentModel> docsList = documentsListsManager.getWorkingList(selectionListName);
        for (DocumentModel doc : docsList) {
            retirerDossierDesTextesSignales(doc);
        }
        documentsListsManager.resetWorkingList(selectionListName);
        navigationContext.setCurrentDocument(null);

        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

    }

    @Override
    public Boolean isHiddenColumn(String isHidden) {
        if (isHidden != null && !isHidden.isEmpty()) {
            return !TAB1.equals(currentUserSubTab);
        }
        return Boolean.FALSE;
    }

    public void setShowPopup(Boolean showPopup) {
        this.showPopup = showPopup;
    }

    public Boolean getShowPopup() {
        return showPopup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String cancelverserDossierDansTextesSignales() {
        type = null;
        showPopup = Boolean.FALSE;
        return null;
    }
}
