package fr.dila.solonmgpp.web.dossier;

import static org.jboss.seam.annotations.Install.FRAMEWORK;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonmgpp.api.constant.SolonMgppI18nConstant;
import fr.dila.solonmgpp.api.constant.TypeEvenementConstants;
import fr.dila.solonmgpp.api.descriptor.PropertyDescriptor;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.espace.EspaceParlementaireActionsBean;
import fr.dila.solonmgpp.web.espace.NavigationWebActionsBean;
import fr.dila.solonmgpp.web.evenement.EvenementCreationActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FicheLoiActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentation341ActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationAVIActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationIEActionsBean;
import fr.dila.solonmgpp.web.fichepresentation.FichePresentationOEPActionsBean;

@Name("dossierCreationActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = FRAMEWORK + 1)
public class DossierCreationActionsBean extends fr.dila.solonepg.web.dossier.DossierCreationActionsBean {

    private static final String MODIFICATION = "modification";

    private static final long serialVersionUID = 142065522223542215L;

    @In(create = true, required = false)
    protected transient EspaceParlementaireActionsBean espaceParlementaireActions;

    @In(create = true, required = false)
    protected transient NavigationWebActionsBean navigationWebActions;

    @In(create = true, required = false)
    protected transient EvenementCreationActionsBean evenementCreationActions;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = true)
    protected transient FicheLoiActionsBean ficheLoiActions;

    @In(create = true, required = false)
    protected transient FichePresentationIEActionsBean fichePresentationIEActions;

    @In(create = true, required = false)
    protected transient FichePresentation341ActionsBean fichePresentation341Actions;

    @In(create = true, required = false)
    protected transient FichePresentationOEPActionsBean fichePresentationOEPActions;

    @In(create = true, required = false)
    protected transient FichePresentationAVIActionsBean fichePresentationAVIActions;

    private Boolean caseTraiter;
    private Boolean casePublier;
    private String from;
    private String norRattachement;

    @Override
    protected void fullReset() {
        this.casePublier = Boolean.FALSE;
        this.caseTraiter = Boolean.FALSE;
        this.from = null;
    }

    public String suivreTransitionTraite() throws ClientException {
        this.caseTraiter = Boolean.TRUE;
        this.casePublier = Boolean.FALSE;
        this.from = null;

        String result = null ;
        
        EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
        if (evenementDTO == null || StringUtils.isBlank(evenementDTO.getTypeEvenementName())) {
            facesMessages.add(StatusMessage.Severity.WARN, "Impossible d'effectuer cette action sans communication.");
            TransactionHelper.setTransactionRollbackOnly();
            
        }else if (TypeEvenementConstants.TYPE_EVENEMENT_EVT02.equals(evenementDTO.getTypeEvenementName())) {
            // il n'y a que les evenement Evt02 concerné par la creation de dossier
            // recherche du dossier rattaché a la communication
            Dossier dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(documentManager, evenementDTO.getIdDossier());
            if (dossier != null) {
                // un dossier existe deja on traite normalement l'evenement
                this.casePublier = Boolean.FALSE;
                this.caseTraiter = Boolean.FALSE;
                this.from = null;
                result = evenementCreationActions.suivreTransitionTraite();
            } else {
                // creation du dossier EPG
                result = super.navigateTo();
            }
        } else if (TypeEvenementConstants.TYPE_EVENEMENT_EVT34.equals(evenementDTO.getTypeEvenementName())) {
            this.casePublier = Boolean.FALSE;
            this.caseTraiter = Boolean.FALSE;
            this.from = null;
            // verification si la fiche existe deja
            if (fichePresentationAVIActions.getFicheAVI() == null) {
                // creation organisme si non existant
                result = fichePresentationAVIActions.navigateToCreationAVI(evenementDTO, Boolean.TRUE);
            } else {
                result = evenementCreationActions.suivreTransitionTraite();
            }
        } else if (TypeEvenementConstants.TYPE_EVENEMENT_EVT49_0.equals(evenementDTO.getTypeEvenementName()) || TypeEvenementConstants.TYPE_EVENEMENT_EVT49.equals(evenementDTO.getTypeEvenementName()) || TypeEvenementConstants.TYPE_EVENEMENT_EVT51.equals(evenementDTO.getTypeEvenementName())) {
            this.casePublier = Boolean.FALSE;
            this.caseTraiter = Boolean.FALSE;
            this.from = null;
            // verifions si la fiche existe deja
            if (fichePresentationOEPActions.getFicheOEP() == null) {
                // creation oep si non existante
                result = fichePresentationOEPActions.navigateToCreationOEP(evenementDTO, Boolean.TRUE);
            } else {
                result = evenementCreationActions.suivreTransitionTraite();
            }
        } else if (TypeEvenementConstants.TYPE_EVENEMENT_EVT39.equals(evenementDTO.getTypeEvenementName()) || TypeEvenementConstants.TYPE_EVENEMENT_EVT43BIS.equals(evenementDTO.getTypeEvenementName()) || TypeEvenementConstants.TYPE_EVENEMENT_EVT43.equals(evenementDTO.getTypeEvenementName()) || TypeEvenementConstants.TYPE_EVENEMENT_EVT40.equals(evenementDTO.getTypeEvenementName())) {
            return fichePresentation341Actions.navigateToCreation341(evenementDTO, Boolean.TRUE);
        } else {
            this.casePublier = Boolean.FALSE;
            this.caseTraiter = Boolean.FALSE;
            this.from = null;
            result = evenementCreationActions.suivreTransitionTraite();
        }
        
        return result ;
    }
    
    public String suivreTransitionEnCoursDeTraitement() throws ClientException {

        String result = null ;
        
        EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
        if (evenementDTO == null || StringUtils.isBlank(evenementDTO.getTypeEvenementName())) {
            facesMessages.add(StatusMessage.Severity.WARN, "Impossible d'effectuer cette action sans communication.");
            TransactionHelper.setTransactionRollbackOnly();
            
        } else {
            result = evenementCreationActions.suivreTransitionEnCoursDeTraitement();
        }
        
        return result ;
    }

    public String publierEvenement(String from) throws ClientException {
        this.from = from;
        return publierEvenement();
    }

    public String publierEvenement() throws ClientException {

        EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();       
        if (evenementDTO == null || StringUtils.isBlank(evenementDTO.getTypeEvenementName())) {
            facesMessages.add(StatusMessage.Severity.WARN, "Impossible d'effectuer cette action sans communication.");
            TransactionHelper.setTransactionRollbackOnly();
            return null;
        }

        // check meta
        if (!evenementCreationActions.checkMetaDonnee(evenementDTO, Boolean.TRUE)) {
        	return null;
        }

        this.casePublier = Boolean.TRUE;
        this.caseTraiter = Boolean.FALSE;

        if (TypeEvenementConstants.TYPE_EVENEMENT_EVT28.equals(evenementDTO.getTypeEvenementName())) {
            
            //int version = evenementDTO.getVersionCouranteMajeur();
            this.casePublier = Boolean.FALSE;
            this.caseTraiter = Boolean.FALSE;
            //String viewResult = "";
            if (MODIFICATION.equals(from)) {
                return evenementCreationActions.saveModifierEvenement(true);
            } else {
                return evenementCreationActions.saveCreationEvenement(true);
            }
            
//            if (version == 0) {
//                // creation du dossier EPG
//                return super.navigateTo();
//            } else {
//                return viewResult;
//            }
        } else {
            this.casePublier = Boolean.FALSE;
            this.caseTraiter = Boolean.FALSE;
            return evenementCreationActions.saveCreationEvenement(true);
        }
    }

    @Override
    public String goTerminer() throws Exception {
        String view = super.goTerminer();
        if (StringUtils.isEmpty(view)) {
            return view;
        }

        if (VIEW_SELECT_POSTE.equals(view)) {
            return view;
        }

        if (isCaseTraiter()) {
            evenementCreationActions.suivreTransitionTraite();
        } else if (isCasePublier()) {

            EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
            if (evenementDTO == null || StringUtils.isBlank(evenementDTO.getTypeEvenementName()) || !TypeEvenementConstants.TYPE_EVENEMENT_EVT28.equals(evenementDTO.getTypeEvenementName())) {
                evenementCreationActions.saveCreationEvenement(true);
            }

        }

        return navigateBack(view);
    }

    @Override
    protected Dossier createDossier(final DossierService dossierService, Dossier dossier, String norDossierCopieFdr) throws ClientException {
        final fr.dila.solonmgpp.api.service.DossierService dossierServiceMgpp = SolonMgppServiceLocator.getDossierService();
        EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
        dossier = super.createDossier(dossierService, dossier, norDossierCopieFdr);

        if (isCaseTraiter()) {
            if (evenementDTO == null || StringUtils.isBlank(evenementDTO.getIdDossier()) || StringUtils.isBlank(evenementDTO.getTypeEvenementName())) {
                facesMessages.add(StatusMessage.Severity.WARN, "Impossible d'effectuer cette action sans communication.");
                TransactionHelper.setTransactionRollbackOnly();
                return null;
            }
            dossierServiceMgpp.attachIdDossierToDosier(documentManager, dossier, evenementDTO.getIdDossier());
        }
        if (evenementDTO != null && TypeEvenementConstants.TYPE_EVENEMENT_EVT02.equals(evenementDTO.getTypeEvenementName())) {
            dossierServiceMgpp.updateFicheLoi(documentManager, evenementDTO);
        }
        return dossier;
    }

    private String navigateBack(String view) throws ClientException {
        if (!isCaseTraiter() && !isCasePublier()) {
            caseTraiter = Boolean.FALSE;
            casePublier = Boolean.FALSE;
            return view;
        } else {
            caseTraiter = Boolean.FALSE;
            casePublier = Boolean.FALSE;

            // reset du provider
            Events.instance().raiseEvent(ProviderBean.RESET_CONTENT_VIEW_EVENT);

            // refresh du provider
            Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

            return espaceParlementaireActions.navigateTo(navigationWebActions.getCurrentSecondMenuAction());
        }
    }

    @Override
    public String goAnnuler() throws ClientException {
        return navigateBack(super.goAnnuler());
    }

    public String rattacher() {
        try {

            if (isCaseTraiter()) {
                // try to find dossier from nor
                Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(documentManager, norRattachement);
                if (dossier == null) {
                    facesMessages.add(StatusMessage.Severity.WARN, "Aucun dossier trouvé pour le NOR " + norRattachement);
                    TransactionHelper.setTransactionRollbackOnly();
                    return null;
                } else {
                    if (StringUtils.isNotBlank(dossier.getIdDossier())) {
                        facesMessages.add(StatusMessage.Severity.WARN, "Le dossier " + norRattachement + " est déjà rattaché");
                        TransactionHelper.setTransactionRollbackOnly();
                        return null;
                    }
                }

                EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
                if (evenementDTO == null || StringUtils.isBlank(evenementDTO.getIdDossier()) || StringUtils.isBlank(evenementDTO.getTypeEvenementName())) {
                    facesMessages.add(StatusMessage.Severity.WARN, "Impossible d'effectuer cette action sans communication.");
                    TransactionHelper.setTransactionRollbackOnly();
                    return null;
                }

                SolonMgppServiceLocator.getDossierService().attachIdDossierToDosier(documentManager, dossier, evenementDTO.getIdDossier());

                evenementCreationActions.suivreTransitionTraite();

            }

            norRattachement = null;
            return navigateBack(null);

        } catch (ClientException e) {
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
            return null;
        }

    }
    
    public Boolean traiterSansDossier() {
        return navigationContext.getCurrentEvenement() != null &&
          TypeEvenementConstants.TYPE_EVENEMENT_EVT02.equals(navigationContext.getCurrentEvenement().getTypeEvenementName());
    }

    public Boolean isCasePublier() {
        return casePublier != null && casePublier;
    }

    public Boolean isCaseTraiter() {
        return caseTraiter != null && caseTraiter;
    }

    public void setNorRattachement(String norRattachement) {
        this.norRattachement = norRattachement;
    }

    public String getNorRattachement() {
        return norRattachement;
    }
}
