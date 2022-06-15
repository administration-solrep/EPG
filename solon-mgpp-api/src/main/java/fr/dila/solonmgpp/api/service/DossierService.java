package fr.dila.solonmgpp.api.service;

import fr.dila.solonepg.api.cases.Dossier;
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
import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.HistoriqueDossierDTO;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EvenementType;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface DossierService extends fr.dila.solonepg.api.service.DossierService {
    HistoriqueDossierDTO findDossier(String idsDossiersEPPLies, CoreSession session);

    /**
     * recherche une {@link FicheLoi}, creation si non trouvée
     *
     * @param session
     * @param idDossier
     * @return
     */
    FicheLoi findOrCreateFicheLoi(CoreSession session, String idDossier);

    /**
     * Sauvegarde de la {@link FicheLoi}
     *
     * @param session
     * @param ficheDossier
     * @return
     */
    FicheLoi saveFicheLoi(CoreSession session, DocumentModel ficheDossier);

    /**
     * Publication de la {@link FicheLoi}
     *
     * @param session
     * @param ficheDossier
     * @return
     */
    FicheLoi publierFicheLoi(final CoreSession session, DocumentModel ficheLoi);

    /**
     * Creation de la fiche dossier
     *
     * @param session
     * @param evenementDTO
     */
    void createFicheLoi(CoreSession session, EvenementDTO evenementDTO);

    /**
     * Rattache un dossier EPG au MGPP via idDossier
     *
     * @param session
     * @param dossier
     * @param idDossier
     */
    void attachIdDossierToDosier(CoreSession session, Dossier dossier, String idDossier);

    /**
     * Mise a jour d'une {@link FicheLoi} à partir d'un evenement
     *
     * @param session
     * @param evenementDTO
     */
    void updateFicheLoi(CoreSession session, EvenementDTO evenementDTO);

    /**
     * Mise a jour du bordereau à partir d'un evenement
     *
     * @param session
     * @param evenementDTO
     * @param dossier
     */
    void updateBordereau(CoreSession session, EvenementDTO evenementDTO, Dossier dossier);

    /**
     * Mise a jour du FDD (dossier SGG) à partir d'un evenement
     *
     * @param session
     * @param evenementDTO
     * @param dossier
     */
    void updateFondDossier(CoreSession session, EvenementDTO evenementDTO, Dossier dossier);

    /**
     * recherche d'une {@link FichePresentationDR}, creation si non trouvée
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationDR findOrCreateFicheDR(CoreSession session, String idDossier);

    /**
     * recherche d'une {@link FichePresentationDPG}, creation si non trouvée
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationDPG findOrCreateFicheDPG(CoreSession session, String idDossier);

    /**
     * recherche d'une {@link FichePresentationSD}, creation si non trouvée
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationSD findOrCreateFicheSD(CoreSession session, String idDossier);

    /**
     * recherche d'une {@link FichePresentationJSS}, creation si non trouvée
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationJSS findOrCreateFicheJSS(CoreSession session, String idDossier);

    /**
     * recherche d'une {@link FichePresentationDOC}, creation si non trouvée
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationDOC findOrCreateFicheDOC(CoreSession session, String idDossier);

    /**
     * recherche d'une {@link FichePresentationAUD}, creation si non trouvée
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationAUD findOrCreateFicheAUD(CoreSession session, String idDossier);

    /**
     * publication d'un {@link FichePresentationDR}
     *
     * @param session
     * @param ficheDR
     * @return
     */
    FichePresentationDR publierFicheDR(CoreSession session, DocumentModel ficheDR);

    /**
     * Sauvegarde d'un {@link FichePresentationDR}
     *
     * @param session
     * @param ficheDR
     * @return
     */
    FichePresentationDR saveFicheDR(CoreSession session, DocumentModel ficheDR);

    /**
     * Sauvegarde d'un {@link FichePresentationDPG}
     *
     * @param session
     * @param ficheDPG
     * @return
     */
    FichePresentationDPG saveFicheDPG(CoreSession session, DocumentModel ficheDPG);

    /**
     * Sauvegarde d'un {@link FichePresentationSD}
     *
     * @param session
     * @param ficheSD
     * @return
     */
    FichePresentationSD saveFicheSD(CoreSession session, DocumentModel ficheSD);

    /**
     * Sauvegarde d'un {@link FichePresentationJSS}
     *
     * @param session
     * @param ficheJSS
     * @return
     */
    FichePresentationJSS saveFicheJSS(CoreSession session, DocumentModel ficheJSS);

    /**
     * Creation d'une {@link FichePresentationOEP} completer avec les info de l'evenement si non null
     *
     * @param session
     * @param evenementDTO
     * @return
     */
    FichePresentationOEP createFicheRepresentationOEP(CoreSession session, EvenementDTO evenementDTO, Boolean save);

    FichePresentationOEP findOrCreateFicheRepresentationOEP(CoreSession session, EvenementDTO evenementDTO);

    /**
     * Creation d'une {@link FichePresentationIE} vide
     *
     * @param evenementDTO
     *
     * @param session
     * @return
     */
    FichePresentationIE createEmptyFicheIE(EvenementDTO evenementDTO, CoreSession session);

    /**
     * Creation d'une {@link FichePresentationIE}
     *
     * @param session
     * @param fichePresentationIE
     */
    void createFicheIE(CoreSession session, FichePresentationIE fichePresentationIE);

    /**
     * Sauvegarde d'une {@link FichePresentationIE}
     *
     * @param session
     * @param fichePresentationIE
     * @return
     */
    FichePresentationIE saveFicheIE(CoreSession session, FichePresentationIE fichePresentationIE);

    /**
     * Recherche d'une {@link FichePresentationIE}
     *
     * @param session
     * @param identifiantProposition
     * @return
     */
    FichePresentationIE findFicheIE(CoreSession session, String identifiantProposition);

    /**
     * Recherche d'une {@link FichePresentationDPG}
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationDPG findFichePresentationDPG(CoreSession session, String idDossier);

    /**
     * Recherche d'une {@link FichePresentationSD}
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationSD findFichePresentationSD(CoreSession session, String idDossier);

    /**
     * Recherche d'une {@link FichePresentationJSS}
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationJSS findFichePresentationJSS(CoreSession session, String idDossier);

    /**
     * Recherche d'une {@link FichePresentation341}
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentation341 find341(CoreSession session, String idDossier);

    /**
     * Sauvegarde d'une {@link FichePresentation341}
     *
     * @param session
     * @param fiche341
     * @return
     */
    FichePresentation341 saveFiche341(CoreSession session, FichePresentation341 fiche341);

    /**
     * Creation d'une {@link FichePresentation341}
     *
     * @param session
     * @param idEvenement
     * @param evenementDTO
     * @return
     */
    FichePresentation341 createFiche341(CoreSession session, String idEvenement, EvenementDTO evenementDTO);

    /**
     * Creation d'une {@link FichePresentation341}
     *
     * @param session
     * @param fichePresentation341
     */
    void createFiche341(CoreSession session, FichePresentation341 fichePresentation341);

    /**
     * Creation d'une {@link FichePresentation341} vide
     *
     * @param evenementDTO
     * @param session
     * @return
     */
    FichePresentation341 createEmptyFiche341(EvenementDTO evenementDTO, CoreSession session);

    /**
     * Fetch {@link RepresentantOEP} from {@link FichePresentationOEP}
     *
     * @param session
     * @param typeRepresentant
     * @param idFicheOEP
     * @return
     */
    List<DocumentModel> fetchRepresentantOEP(CoreSession session, String typeRepresentant, String idFicheOEP);

    /**
     *
     * @param session
     * @param ficheOEPList
     */
    void diffuserFicheOEP(CoreSession session, List<DocumentModel> ficheOEPList);

    /**
     *
     * @param session
     * @param ficheOEPList
     */
    FichePresentationOEP diffuserFicheOEP(CoreSession session, DocumentModel ficheOEP);

    /**
     *
     * @param session
     * @param ficheOEPList
     */
    void annulerDiffusionFicheOEP(CoreSession session, List<DocumentModel> ficheOEPList);

    /**
     *
     * @param session
     * @param ficheOEPList
     */
    FichePresentationOEP annulerDiffusionFicheOEP(CoreSession session, DocumentModel ficheOEP);

    /**
     * recherche d'une {@link FichePresentationOEP} par son idDossier
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationOEP findFicheOEP(CoreSession session, String idDossier);

    /**
     * recherche d'une {@link FichePresentationOEP} par son idDossier
     *
     * @param session
     * @param idDossier
     *            l'id du dossier EPP
     * @return
     */
    FichePresentationOEP findFicheOEPbyIdDossierEPP(CoreSession session, String idDossierEPP);

    /**
     * Sauvegarde d'une {@link FichePresentationOEP}
     *
     * @param session
     * @param ficheOEP
     * @param listRepresentantSE
     * @param listRepresentantAN
     * @return
     */
    FichePresentationOEP saveFicheOEP(
        CoreSession session,
        DocumentModel ficheOEP,
        List<DocumentModel> listRepresentantAN,
        List<DocumentModel> listRepresentantSE
    );

    List<DocumentModel> fetchRepresentantOEP(
        String typeRepresentant,
        EvenementDTO currentEvenement,
        CoreSession session
    );

    /**
     * Sauvegarde de la creation d'une {@link FichePresentationOEP}
     *
     * @param session
     * @param ficheOEP
     * @param listAN
     * @param list2
     */
    void createFicheRepresentationOEP(
        CoreSession session,
        DocumentModel ficheOEP,
        List<DocumentModel> listAN,
        List<DocumentModel> listSE
    );

    void gererFichePresentation(EppEvtContainer eppEvtContainer, boolean publie, CoreSession session);

    /**
     * recherche d'une {@link FichePresentationAVI}
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationAVI findFicheAVI(CoreSession session, String idDossier);

    /**
     * creation d'une {@link FichePresentationAVI}
     *
     * @param session
     * @param evenementDTO
     * @return
     */
    FichePresentationAVI createFicheRepresentationAVI(CoreSession session, EvenementDTO evenementDTO);

    /**
     * creation d'une {@link FichePresentationAVI}
     *
     * @param session
     * @param ficheAVI
     * @param listRepresentant
     */
    void createFicheRepresentationAVI(
        CoreSession session,
        DocumentModel ficheAVI,
        List<DocumentModel> listRepresentant
    );

    /**
     * fetch des nominés d'une {@link FichePresentationAVI}
     *
     * @param session
     * @param id
     * @return
     */
    List<DocumentModel> fetchNomineAVI(CoreSession session, String id);

    /**
     * sauvegarde d'une {@link FichePresentationAVI}
     *
     * @param session
     * @param ficheAVI
     * @param listRepresentant
     * @return
     */
    FichePresentationAVI saveFicheAVI(
        CoreSession session,
        DocumentModel ficheAVI,
        List<DocumentModel> listRepresentant
    );

    /**
     * Positionne la fiche de decret par rapport a l'etape courante de la feuille de route du décret
     *
     * @param numeroNor
     * @param session
     * @param actif
     */
    void notifierDecret(String numeroNor, CoreSession session, Boolean actif);

    /**
     * Recherche d'une {@link FichePresentationDecret}, creation si non trouvée
     *
     * @param session
     * @param nor
     * @return
     */
    FichePresentationDecret findOrCreateFicheDecret(CoreSession session, String nor);

    /**
     * Sauvegarde d'une {@link FichePresentationDecret}
     *
     * @param session
     * @param ficheDecret
     * @return
     */
    FichePresentationDecret saveFicheDecret(CoreSession session, DocumentModel ficheDecret);

    /**
     * Mise a jour de la fiche loi suivant une communication
     *
     * @param session
     * @param idEvenement
     * @param typeEvenement
     */
    void updateFicheLoi(CoreSession session, String idEvenement, EvenementType typeEvenement);

    /**
     * recherche d'une {@link FichePresentationDR} par son idDossier
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationDR findFicheDR(CoreSession session, String idDossier);

    /**
     * recherche d'une {@link FichePresentationDR} par son idDossier
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationDecret findFicheDecret(CoreSession session, String idDossier);

    /**
     * Gets the list OEP Diffuse List
     *
     * @param session
     *            the session
     * @return the list OEP Diffuse List
     */
    List<DocumentModel> getFichePresentationOEPDiffuseList(final CoreSession session);

    /**
     * Updates la liste de toutes les fiches OEP publiees dans EPG
     *
     * @param session
     *            the session
     */
    void updateListeOEPPubliee(final CoreSession session);

    /**
     *
     * @return le path des fichiers des activites parlementaires
     */
    String getPathDirAPPublie();

    /**
     *
     * @return le path du répertoire qui va contenir les fichiers publies de l'activite parlementaire
     */
    String getPathAPPublieRepertoire();

    /**
     * recherche d'une {@link FichePresentationAUD}
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationAUD findFicheAUD(CoreSession session, String idDossier);

    /**
     * creation d'une {@link FichePresentationAUD}
     *
     * @param session
     * @param evenementDTO
     * @return
     */
    FichePresentationAUD createFicheRepresentationAUD(CoreSession session, EvenementDTO evenementDTO);

    /**
     * creation d'une {@link FichePresentationAUD}
     *
     * @param session
     * @param ficheAUD
     * @param listRepresentant
     */
    void createFicheRepresentationAUD(
        CoreSession session,
        DocumentModel ficheAUD,
        List<DocumentModel> listRepresentant
    );

    /**
     * fetch des nominés d'une {@link FichePresentationAUD}
     *
     * @param session
     * @param id
     * @return
     */
    List<DocumentModel> fetchPersonneAUD(CoreSession session, String id);

    /**
     * sauvegarde d'une {@link FichePresentationAUD}
     *
     * @param session
     * @param ficheAUD
     * @param listRepresentant
     * @return
     */
    FichePresentationAUD saveFicheAUD(
        CoreSession session,
        DocumentModel ficheAUD,
        List<DocumentModel> listRepresentant
    );

    /**
     * recherche d'une {@link FichePresentationDOC}
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationDOC findFicheDOC(CoreSession session, String idDossier);

    /**
     * creation d'une {@link FichePresentationDOC}
     *
     * @param session
     * @param evenementDTO
     * @return
     */
    FichePresentationDOC createFicheRepresentationDOC(CoreSession session, EvenementDTO evenementDTO);

    /**
     * creation d'une {@link FichePresentationDOC}
     *
     * @param session
     * @param idDossier
     * @return
     */
    FichePresentationDOC createFicheDoc(final CoreSession session, final String idDossier);

    /**
     * creation d'une {@link FichePresentationDOC}
     *
     * @param session
     * @param ficheDOC
     * @param listRepresentant
     */
    void createFicheRepresentationDOC(CoreSession session, DocumentModel ficheDOC);

    /**
     * sauvegarde d'une {@link FichePresentationDOC}
     *
     * @param session
     * @param ficheDOC
     * @return
     */
    FichePresentationDOC saveFicheDOC(CoreSession session, DocumentModel ficheDOC);

    FichePresentationAVI findOrCreateFicheAVI(final CoreSession session, final String idDossier);

    /**
     * Permet de récupérer l'ensemble des fiches loi qui n'ont pas de nature
     *
     * @param session
     * @return
     */
    List<FicheLoi> findFicheLoiWithoutNature(CoreSession session);

    FichePresentationOEP findFicheOEPinANATLies(CoreSession session, String idDossier);

    /**
     * Retourne la fiche loi qui possède la propriété floi:numeroNor = numeroNor en paramètre
     *
     * @param session
     * @param numeroNor
     * @return la première fiche loi trouvée
     */
    DocumentModel findFicheLoiDocumentFromNor(CoreSession session, String numeroNor);
}
