package fr.dila.solonmgpp.api.service;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
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
import org.nuxeo.ecm.core.api.CoreSession;

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
     */
    EvenementDTO findEvenement(
        String idEvenement,
        String numVersion,
        CoreSession session,
        boolean gererFichePresentation
    );

    /**
     * Creation d'un {@link EvenementDTO} à partir d'un type
     *
     * @param typeEvenement
     * @param session
     * @return
     */
    EvenementDTO createEmptyEvenementDTO(String typeEvenement, CoreSession session);

    /**
     * Creation d'un {@link EppEvtContainer} à partir d'un {@link EvenementDTO}
     *
     * @param evenementDTO
     * @param hasContenu : spécifie si les peices jointes on du contenu ou non
     * @param session
     * @return
     */
    EppEvtContainer createEppEvtContainerFromEvenementDTO(
        EvenementDTO evenementDTO,
        Boolean hasContenu,
        CoreSession session
    );

    /**
     * initialisation dans EPP d'un evenement
     *
     * @param currentTypeEvenement
     * @return
     */
    EvenementDTO initialiserEvenement(String currentTypeEvenement, CoreSession session);

    /**
     * initialisation dans EPP d'un evenement
     *
     * @param currentTypeEvenement
     * @param genererIdDossier
     * @return
     */
    EvenementDTO initialiserEvenement(String currentTypeEvenement, boolean genererIdDossier, CoreSession session);

    /**
     * creation d'une nouvelle communication
     *
     * @param evenementDTO
     * @param publie
     * @param session
     */
    void createEvenement(EvenementDTO evenementDTO, boolean publie, CoreSession session);

    /**
     * Rectification d'une communication
     *
     * @param evenementDTO
     * @param publier
     */
    void rectifierEvenement(EvenementDTO evenementDTO, boolean publier, CoreSession session);

    /**
     * Completion d'un evenement
     *
     * @param evenementDTO
     * @param publier
     */
    void completerEvenement(EvenementDTO evenementDTO, boolean publier, CoreSession session);

    /**
     * Initilisation d'un evenement a partir de son evenement precedent
     * @param currentTypeEvenement
     * @param evenementDTO
     *
     * @return
     */
    EvenementDTO initialiserEvenementSuccessif(
        EvenementDTO evenementDTOPrecedent,
        String currentTypeEvenement,
        CoreSession session
    );

    /**
     * Initilisation d'un evenement a partir de son evenement precedent
     * @param currentTypeEvenement
     * @param genererIdDossier
     * @param evenementDTO
     *
     * @return
     */
    EvenementDTO initialiserEvenementSuccessif(
        EvenementDTO evenementDTOPrecedent,
        String currentTypeEvenement,
        boolean genererIdDossier,
        CoreSession session
    );

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
     * @param etatEvenement
     *
     * @param evenementDTO
     * @param session
     * @param dossierLink
     */
    void traiterEvenement(
        String etatEvenement,
        EvenementDTO evenementDTO,
        CoreSession session,
        DossierLink dossierLink
    );

    /**
     * Evenement en cours de traitement
     * @param idEvenement
     * @param session
     */
    void enCoursDeTraitementEvenement(String idEvenement, CoreSession session);

    /**
     * Accepter une version
     *
     * @param idEvenement
     */
    void accepterVersion(String idEvenement, CoreSession session);

    /**
     * Rejeter une version
     *
     * @param idEvenement
     */
    void rejeterVersion(String idEvenement, CoreSession session);

    /**
     * AR d'une version
     *
     * @param evenementDTO
     */
    void accuserReceptionVersion(EvenementDTO evenementDTO, CoreSession session);

    /**
     * Abandon d'une version
     *
     * @param idEvenement
     */
    void abandonnerVersion(String idEvenement, CoreSession session);

    /**
     * Annulation d'un evenement
     *
     * @param idEvenement
     */
    void annulerEvenement(String idEvenement, CoreSession session);

    /**
     * Suppression d'un evenement
     *
     * @param evenementDTO
     */
    void supprimerEvenement(EvenementDTO evenementDTO, CoreSession session);

    /**
     * Modification d'un evenement
     *
     * @param evenementDTO
     * @param publier
     */
    void modifierEvenement(EvenementDTO evenementDTO, boolean publier, CoreSession session);

    /**
     * Creation d'un AR
     *
     * @param idEvenement
     * @param evenementType
     */
    void accuserReception(String idEvenement, EvenementType evenementType, CoreSession session);

    /**
     * Creation d'un {@link EppEvt01} a l'etat brouillon
     *
     * @param dossier
     * @param session
     */
    void createEvenementEppEvt01Brouillon(Dossier dossier, CoreSession session);

    /**
     * Creation d'un {@link EppEvt28} a l'etat brouillon
     *
     * @param dossier
     * @param session
     */
    void createEvenementEppEvt28Brouillon(Dossier dossier, CoreSession session);

    /**
     * Creation d'un {@link EppEvt44} a l'etat brouillon
     *
     * @param numeroNor
     * @param coreSession
     */
    void createEvenementEppEvt44Brouillon(final Dossier dossier, CoreSession session);

    /**
     * Creation d'un {@link EppEvt36} à l'etat brouillon
     *
     * @param session
     * @param fichePresentationIE
     */
    void createEvenementEppEvt36Brouillon(CoreSession session, FichePresentationIE fichePresentationIE);

    /**
     * Creation d'un {@link EppEvt49} à l'etat brouillon
     *
     * @param session
     * @param fichePresentationOEP
     */
    void createEvenementEppEvt49Brouillon(CoreSession session, FichePresentationOEP fichePresentationOEP);

    /**
     * Creation d'un {@link EppEvt32} à l'etat brouillon
     *
     * @param session
     * @param fichePresentationAVI
     */
    void createEvenementEppEvt32Brouillon(CoreSession session, FichePresentationAVI fichePresentationAVI);

    /**
     * Creation d'un {@link EppAUD01} à l'etat brouillon
     *
     * @param session
     * @param fichePresentationAUD
     */
    void createEvenementEppAUD01Brouillon(CoreSession session, FichePresentationAUD fichePresentationAUD);

    /**
     * Creation d'un {@link EppDOC01} à l'etat brouillon
     *
     * @param session
     * @param fichePresentationDOC
     */
    void createEvenementEppDOC01Brouillon(CoreSession session, FichePresentationDOC fichePresentationDOC);

    /**
     * Verification si la procédure
     *
     * @param session
     * @param evenementDTO
     */
    void checkProcedureAcceleree(CoreSession session, EvenementDTO evenementDTO);

    /**
     *
     * @param session
     * @param idDossier
     */
    void createEvenementEppEvt29Brouillon(CoreSession session, FichePresentationDecret fichePresentationDecret);

    /**
     *
     * @param session
     * @param idDossier
     */
    void createEvenementEppEvt30Brouillon(CoreSession session, FichePresentationDecret fichePresentationDecret);

    /**
     *
     * @param session
     * @param fichePresentationDecret
     */
    void createEvenementEppEvt31Brouillon(CoreSession session, FichePresentationDecret fichePresentationDecret);

    /**
     *
     * @param session
     * @param fichePresentationDecret
     */
    void createEvenementEppEvt35Brouillon(CoreSession session, FichePresentationDecret fichePresentationDecret);

    /**
     *
     * @param session
     * @param idEvenement
     * @param mettreEnAttente
     */
    void mettreEnAttenteRelancer(CoreSession session, String idEvenement, boolean mettreEnAttente);

    /**
     * @return id
     */
    String generateIdDossierDOC();

    EvenementDTO findEvenement(
        String idEvenement,
        String numVersion,
        CoreSession session,
        boolean gererFichePresentation,
        boolean returnErrorIfNull
    );
}
