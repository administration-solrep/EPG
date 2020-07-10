package fr.dila.solonmgpp.core.service;

import fr.dila.solonmgpp.api.service.CorbeilleService;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.api.service.EvenementService;
import fr.dila.solonmgpp.api.service.EvenementTypeService;
import fr.dila.solonmgpp.api.service.FondDossierService;
import fr.dila.solonmgpp.api.service.InstitutionService;
import fr.dila.solonmgpp.api.service.MGPPInjectionGouvernementService;
import fr.dila.solonmgpp.api.service.MGPPJointureService;
import fr.dila.solonmgpp.api.service.MGPPRequeteurWidgetGeneratorService;
import fr.dila.solonmgpp.api.service.MailService;
import fr.dila.solonmgpp.api.service.MessageService;
import fr.dila.solonmgpp.api.service.MetaDonneesService;
import fr.dila.solonmgpp.api.service.NavetteService;
import fr.dila.solonmgpp.api.service.NotificationService;
import fr.dila.solonmgpp.api.service.ParametreMgppService;
import fr.dila.solonmgpp.api.service.PieceJointeService;
import fr.dila.solonmgpp.api.service.RechercheService;
import fr.dila.solonmgpp.api.service.SuiviService;
import fr.dila.solonmgpp.api.service.TableReferenceService;
import fr.dila.st.core.util.ServiceUtil;

/**
 * ServiceLocator de l'application SOLON MGPP : permet de retourner les services de l'application.
 * 
 * @author asatre
 */
public class SolonMgppServiceLocator {

    /**
     * Retourne le service de gestion des corbeilles.
     * 
     * @return {@link CorbeilleService}
     */
    public static CorbeilleService getCorbeilleService() {
        return ServiceUtil.getService(CorbeilleService.class);
    }

    /**
     * Retourne le service de gestion des evenements.
     * 
     * @return {@link EvenementService}
     */
    public static EvenementService getEvenementService() {
        return ServiceUtil.getService(EvenementService.class);
    }

    /**
     * Retourne le service de gestion des messages.
     * 
     * @return {@link MessageService}
     */
    public static MessageService getMessageService() {
        return ServiceUtil.getService(MessageService.class);
    }

    /**
     * Retourne le service de gestion des types d'evenements.
     * 
     * @return {@link EvenementTypeService}
     */
    public static EvenementTypeService getEvenementTypeService() {
        return ServiceUtil.getService(EvenementTypeService.class);
    }

    /**
     * Retourne le service de gestion des metadonnees
     * 
     * @return {@link MetaDonneesService}
     */
    public static MetaDonneesService getMetaDonneesService() {
        return ServiceUtil.getService(MetaDonneesService.class);
    }

    /**
     * Retourne le service de gestion des pieces jointes
     * 
     * @return {@link PieceJointeService}
     */
    public static PieceJointeService getPieceJointeService() {
        return ServiceUtil.getService(PieceJointeService.class);
    }

    /**
     * Retourne le service de gestion des tables de references.
     * 
     * @return {@link TableReferenceService}
     */
    public static TableReferenceService getTableReferenceService() {
        return ServiceUtil.getService(TableReferenceService.class);
    }

    /**
     * Retourne le service de gestion des institutions.
     * 
     * @return {@link InstitutionService}
     */
    public static InstitutionService getInstitutionService() {
        return ServiceUtil.getService(InstitutionService.class);
    }

    /**
     * Retourne le service de gestion des mails.
     * 
     * @return {@link MailService}
     */
    public static MailService getMailService() {
        return ServiceUtil.getService(MailService.class);
    }

    /**
     * Retourne le sservice de gestion de la recherche.
     * 
     * @return {@link RechercheService}
     */
    public static RechercheService getRechercheService() {
        return ServiceUtil.getService(RechercheService.class);
    }

    /**
     * Retourne le service de gestion de la recherche.
     * 
     * @return {@link DossierService}
     */
    public static DossierService getDossierService() {
        return ServiceUtil.getService(DossierService.class);
    }

    /**
     * Retourne le service de la suivi.
     * 
     * @return {@link DossierService}
     */
    public static SuiviService getSuiviService() {
        return ServiceUtil.getService(SuiviService.class);
    }

    /**
     * 
     * @return {@link FondDossierService}
     */
    public static FondDossierService getFondDossierService() {
        return ServiceUtil.getService(FondDossierService.class);
    }

    /**
     * 
     * @return {@link ParametreMgppService}
     */
    public static ParametreMgppService getParametreMgppService() {
        return ServiceUtil.getService(ParametreMgppService.class);
    }

    /**
     * Retourne le service de gestion des navettes.
     * 
     * @return {@link NavetteService}
     */
    public static NavetteService getNavetteService() {
        return ServiceUtil.getService(NavetteService.class);
    }
    
    /**
     * Retourne le service de gestion des recherches.
     * 
     * @return {@link MGPPRequeteurWidgetGeneratorService}
     */
    public static MGPPRequeteurWidgetGeneratorService getMGPPRequeteurWidgetGeneratorService() {
        return ServiceUtil.getService(MGPPRequeteurWidgetGeneratorService.class);
    }
    
    /**
     * 
     * @return {@link MGPPJointureService}
     */
    public static MGPPJointureService getMGPPJointureService() {
        return ServiceUtil.getService(MGPPJointureService.class);
    }
    
    /**
     * 
     * @return {@link NotificationService}
     */
    public static NotificationService getNotificationService() {
        return ServiceUtil.getService(NotificationService.class);
    }
    
    /**
     * 
     * @return {@link MGPPInjectionGouvernementService}
     */
    public static MGPPInjectionGouvernementService getMGPPInjectionGouvernementService() {
        return ServiceUtil.getService(MGPPInjectionGouvernementService.class);
    }
    
    private SolonMgppServiceLocator() {
        //Private default constructor
       }
}
