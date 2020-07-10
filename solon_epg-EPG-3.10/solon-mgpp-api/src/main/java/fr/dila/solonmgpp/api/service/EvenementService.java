package fr.dila.solonmgpp.api.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FileSolonMgpp;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.sword.xsd.solon.epp.EppAUD01;
import fr.sword.xsd.solon.epp.EppDOC01;
import fr.sword.xsd.solon.epp.EppEvt01;
import fr.sword.xsd.solon.epp.EppEvt28;
import fr.sword.xsd.solon.epp.EppEvt32;
import fr.sword.xsd.solon.epp.EppEvt36;
import fr.sword.xsd.solon.epp.EppEvt44;
import fr.sword.xsd.solon.epp.EppEvt49;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EvenementType;

/**
 * Interface du service des evenements pour l'interaction epp/mgpp
 * 
 * @author asatre
 * 
 */
public interface EvenementService {

    /**
     * Recherche dans EPP l'evenement correspondant au idEvenement
     * 
     * @param idEvenement
     * @param version au format majeur.mineur
     * @return
     * @throws ClientException
     */
    EvenementDTO findEvenement(String idEvenement, String numVersion, CoreSession session,boolean gererFichePresentation) throws ClientException;

    /**
     * Creation d'un {@link EvenementDTO} à partir d'un type
     * 
     * @param typeEvenement
     * @param session
     * @return
     * @throws ClientException
     */
    EvenementDTO createEmptyEvenementDTO(String typeEvenement, CoreSession session) throws ClientException;

    /**
     * Creation d'un {@link EppEvtContainer} à partir d'un {@link EvenementDTO}
     * 
     * @param evenementDTO
     * @param hasContenu : spécifie si les peices jointes on du contenu ou non
     * @param session
     * @return
     * @throws ClientException
     */
    EppEvtContainer createEppEvtContainerFromEvenementDTO(EvenementDTO evenementDTO, Boolean hasContenu, CoreSession session) throws ClientException;

    /**
     * initialisation dans EPP d'un evenement
     * 
     * @param currentTypeEvenement
     * @return
     * @throws ClientException
     */
    EvenementDTO initialiserEvenement(String currentTypeEvenement, CoreSession session) throws ClientException;

    /**
     * creation d'une nouvelle communication
     * 
     * @param evenementDTO
     * @param publie
     * @param session
     * @throws ClientException
     */
    void createEvenement(EvenementDTO evenementDTO, boolean publie, CoreSession session) throws ClientException;

    /**
     * Rectification d'une communication
     * 
     * @param evenementDTO
     * @param publier
     * @throws ClientException
     */
    void rectifierEvenement(EvenementDTO evenementDTO, boolean publier, CoreSession session) throws ClientException;

    /**
     * Completion d'un evenement
     * 
     * @param evenementDTO
     * @param publier
     */
    void completerEvenement(EvenementDTO evenementDTO, boolean publier, CoreSession session) throws ClientException;

    /**
     * Initilisation d'un evenement a partir de son evenement precedent
     * 
     * @param evenementDTO
     * @param currentTypeEvenement
     * @return
     * @throws ClientException
     */
    EvenementDTO initialiserEvenementSuccessif(EvenementDTO evenementDTOPrecedent, String currentTypeEvenement, CoreSession session) throws ClientException;

    /**
     * Nettoyage de titreActe titreActe - "projet de loi" ou "projet de loi organique" ou "projet de loi constitutionnelle"
     * 
     * @param dossier
     * @return
     */
    String cleanTitreActe(Dossier dossier);

    /**
     * Traite un evenement
     * 
     * @param messageDTO
     * 
     * @param evenementDTO
     * @param session
     * @param dossierLink
     * @throws ClientException
     */
    void traiterEvenement(MessageDTO messageDTO, EvenementDTO evenementDTO, CoreSession session, DossierLink dossierLink) throws ClientException;
    
    /**
     * Evenement en cours de traitement
     * 
     * @param messageDTO
     * 
     * @param evenementDTO
     * @param session
     * @param dossierLink
     * @throws ClientException
     */
    void enCoursDeTraitementEvenement(MessageDTO messageDTO, EvenementDTO evenementDTO, CoreSession session, DossierLink dossierLink) throws ClientException;

    /**
     * Accepter une version
     * 
     * @param evenementDTO
     */
    void accepterVersion(EvenementDTO evenementDTO, CoreSession session) throws ClientException;

    /**
     * Rejeter une version
     * 
     * @param evenementDTO
     * @throws ClientException
     */
    void rejeterVersion(EvenementDTO evenementDTO, CoreSession session) throws ClientException;

    /**
     * AR d'une version
     * 
     * @param evenementDTO
     * @throws ClientException
     */
    void accuserReceptionVersion(EvenementDTO evenementDTO, CoreSession session) throws ClientException;

    /**
     * Abandon d'une version
     * 
     * @param evenementDTO
     * @throws ClientException
     */
    void abandonnerVersion(EvenementDTO evenementDTO, CoreSession session) throws ClientException;

    /**
     * Annulation d'un evenement
     * 
     * @param evenementDTO
     * @throws ClientException
     */
    void annulerEvenement(EvenementDTO evenementDTO, CoreSession session) throws ClientException;

    /**
     * Suppression d'un evenement
     * 
     * @param evenementDTO
     * @throws ClientException
     */
    void supprimerEvenement(EvenementDTO evenementDTO, CoreSession session) throws ClientException;

    /**
     * Modification d'un evenement
     * 
     * @param evenementDTO
     * @param publier
     * @throws ClientException
     */
    void modifierEvenement(EvenementDTO evenementDTO, boolean publier, CoreSession session) throws ClientException;

    /**
     * Creation d'un AR
     * 
     * @param idEvenement
     * @param evenementType
     * @throws ClientException
     */
    void accuserReception(String idEvenement, EvenementType evenementType, CoreSession session) throws ClientException;

    /**
     * Creation d'un {@link EppEvt01} a l'etat brouillon
     * 
     * @param dossier
     * @param session
     * @throws ClientException
     */
    void createEvenementEppEvt01Brouillon(Dossier dossier, CoreSession session) throws ClientException;

    /**
     * Creation d'un {@link EppEvt28} a l'etat brouillon
     * 
     * @param dossier
     * @param session
     * @throws ClientException
     */
    void createEvenementEppEvt28Brouillon(Dossier dossier, CoreSession session) throws ClientException;

    /**
     * Creation d'un {@link EppEvt44} a l'etat brouillon
     * 
     * @param numeroNor
     * @param coreSession
     */
    void createEvenementEppEvt44Brouillon(final Dossier dossier, CoreSession session) throws ClientException;

    /**
     * Creation d'un {@link EppEvt36} à l'etat brouillon
     * 
     * @param session
     * @param fichePresentationIE
     */
    void createEvenementEppEvt36Brouillon(CoreSession session, FichePresentationIE fichePresentationIE) throws ClientException;

    /**
     * Creation d'un {@link EppEvt49} à l'etat brouillon
     * 
     * @param session
     * @param file
     * @param fichePresentationOEP
     * @param evenementDTO
     * @throws ClientException
     */
    void createEvenementEppEvt49Brouillon(CoreSession session, FileSolonMgpp file, FichePresentationOEP fichePresentationOEP, EvenementDTO evenementDTO) throws ClientException;

    /**
     * Creation d'un {@link EppEvt32} à l'etat brouillon
     * 
     * @param session
     * @param file
     * @param fichePresentationAVI
     * @param evenementDTO
     * @throws ClientException
     */
    void createEvenementEppEvt32Brouillon(CoreSession session, FileSolonMgpp file, FichePresentationAVI fichePresentationAVI, EvenementDTO evenementDTO) throws ClientException;

    /**
     * Creation d'un {@link EppAUD01} à l'etat brouillon
     * 
     * @param session
     * @param file
     * @param fichePresentationAUD
     * @param evenementDTO
     * @throws ClientException
     */
    void createEvenementEppAUD01Brouillon(CoreSession session, FileSolonMgpp file, FichePresentationAUD fichePresentationAUD, EvenementDTO evenementDTO) throws ClientException;

    
    
    /**
     * Creation d'un {@link EppDOC01} à l'etat brouillon
     * 
     * @param session
     * @param file
     * @param fichePresentationDOC
     * @param evenementDTO
     * @throws ClientException
     */
    void createEvenementEppDOC01Brouillon(CoreSession session, FileSolonMgpp file, FichePresentationDOC fichePresentationDOC, EvenementDTO evenementDTO) throws ClientException;
    
    
    /**
     * Verification si la procédure
     * 
     * @param session
     * @param evenementDTO
     * @throws ClientException
     */
    void checkProcedureAcceleree(CoreSession session, EvenementDTO evenementDTO) throws ClientException;

    /**
     * 
     * @param session
     * @param idDossier
     * @throws ClientException
     */
    void createEvenementEppEvt29Brouillon(CoreSession session, FichePresentationDecret fichePresentationDecret) throws ClientException;

    /**
     * 
     * @param session
     * @param idDossier
     * @throws ClientException
     */
    void createEvenementEppEvt30Brouillon(CoreSession session, FichePresentationDecret fichePresentationDecret) throws ClientException;

    /**
     * 
     * @param session
     * @param fichePresentationDecret
     * @throws ClientException
     */
    public void createEvenementEppEvt31Brouillon(CoreSession session, FichePresentationDecret fichePresentationDecret) throws ClientException;

    /**
     * 
     * @param session
     * @param fichePresentationDecret
     */
    public void createEvenementEppEvt35Brouillon(CoreSession session, FichePresentationDecret fichePresentationDecret) throws ClientException;

    /**
     * 
     * @param session
     * @param idEvenement
     * @param mettreEnAttente
     * @throws ClientException
     */
    public void mettreEnAttenteRelancer(CoreSession session, String idEvenement, boolean mettreEnAttente) throws ClientException;

    /**
     * @return id
     */
    public String generateIdDossierDOC();

	EvenementDTO findEvenement(String idEvenement, String numVersion,
			CoreSession session, boolean gererFichePresentation,
			boolean returnErrorIfNull) throws ClientException;
}
