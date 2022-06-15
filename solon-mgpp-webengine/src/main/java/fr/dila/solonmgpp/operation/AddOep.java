package fr.dila.solonmgpp.operation;

import fr.dila.solonepp.rest.api.WSEvenement;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.core.dto.EvenementDTOImpl;
import fr.dila.solonmgpp.core.service.DossierServiceImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.core.service.SolonMgppWsLocator;
import fr.dila.solonmgpp.core.util.WSErrorHelper;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.core.util.SHA512Util;
import fr.dila.st.rest.client.HttpTransactionException;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.CompressionFichier;
import fr.sword.xsd.solon.epp.ContenuFichier;
import fr.sword.xsd.solon.epp.CreationType;
import fr.sword.xsd.solon.epp.CreerVersionRequest;
import fr.sword.xsd.solon.epp.CreerVersionResponse;
import fr.sword.xsd.solon.epp.CritereRechercheEvenement;
import fr.sword.xsd.solon.epp.EppEvt49;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.Institution;
import fr.sword.xsd.solon.epp.Mandat;
import fr.sword.xsd.solon.epp.Message;
import fr.sword.xsd.solon.epp.PieceJointe;
import fr.sword.xsd.solon.epp.PieceJointeType;
import fr.sword.xsd.solon.epp.RechercherEvenementRequest;
import fr.sword.xsd.solon.epp.RechercherEvenementResponse;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.platform.mimetype.interfaces.MimetypeRegistry;
import org.nuxeo.runtime.api.Framework;

@Operation(id = AddOep.ID, category = Constants.CAT_DOCUMENT, label = "Add Oep", description = "Add Oep")
public class AddOep {
    private static final String ID_COMMUN = "idCommun";
    private static final String NOM_ORGANISME = "nomOrganisme";
    private static final String MIN_RATTACHEMENT = "minRattachement";
    private static final String MIN_RATTACHEMENT2 = "minRattachement2";
    private static final String MIN_RATTACHEMENT3 = "minRattachement3";

    private static final String NB_DEPUTE = "nbDepute";
    private static final String NB_SENATEUR = "nbSenateur";

    private static final String DUREE_AN = "dureeAN";
    private static final String DUREE_SE = "dureeSE";

    private static final String BASE_LEGALE = "baseLegale";
    private static final String TEXTE_DUREE = "texteDuree";
    private static final String TEXTE_ORGANISME = "texteOrganisme";

    private static final String FILENAME = "fileName";
    private static final String FILE_CONTENT = "fileContent";

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(AddOep.class);

    public static final String ID = "EPG.AddOep";

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "properties")
    protected Properties properties;

    @OperationMethod
    public void run() throws Exception {
        String idCommun = properties.get(ID_COMMUN);

        LOGGER.debug(session, MgppLogEnumImpl.ADD_OEP_PROCESS_TEC, "debut Oep - idCommun = " + idCommun);
        try {
            if (StringUtils.isNotBlank(idCommun)) {
                String nomOrganisme = properties.get(NOM_ORGANISME);
                String minRattachement = properties.get(MIN_RATTACHEMENT);
                String minRattachement2 = properties.get(MIN_RATTACHEMENT2);
                String minRattachement3 = properties.get(MIN_RATTACHEMENT3);
                String nbDepute = properties.get(NB_DEPUTE);
                String nbSenateur = properties.get(NB_SENATEUR);
                String dureeAN = properties.get(DUREE_AN);
                String dureeSE = properties.get(DUREE_SE);
                String baseLegale = properties.get(BASE_LEGALE);
                String texteDuree = properties.get(TEXTE_DUREE);
                String texteOrganisme = properties.get(TEXTE_ORGANISME);
                String fileName = properties.get(FILENAME);
                String fileContent = properties.get(FILE_CONTENT);

                String[] fileContentArray = fileContent.split("#");

                byte[] content = new byte[fileContentArray.length];

                for (int i = 0; i < fileContentArray.length; i++) {
                    content[i] = Integer.valueOf(fileContentArray[i]).byteValue();
                }

                String idDossier = DossierServiceImpl.generateIdDossierOEP(idCommun);

                EvenementDTO evenementDTO = new EvenementDTOImpl();
                evenementDTO.setIdDossier(idDossier);

                FichePresentationOEP fiche = SolonMgppServiceLocator
                    .getDossierService()
                    .findOrCreateFicheRepresentationOEP(session, evenementDTO);

                fiche.setIdCommun(idCommun);
                fiche.setIdDossier(idDossier);

                fiche.setNomOrganisme(nomOrganisme);
                fiche.setMinistereRattachement(minRattachement);
                fiche.setMinistereRattachement2(minRattachement2);
                fiche.setMinistereRattachement3(minRattachement3);

                try {
                    fiche.setNbDepute(Long.parseLong(nbDepute));
                } catch (NumberFormatException e) {
                    fiche.setNbDepute(0L);
                }

                try {
                    fiche.setNbSenateur(Long.parseLong(nbSenateur));
                } catch (NumberFormatException e) {
                    fiche.setNbSenateur(0L);
                }

                fiche.setDureeMandatAN(dureeAN);
                fiche.setDureeMandatSE(dureeSE);
                fiche.setTexteRef(baseLegale);
                fiche.setTexteDuree(texteDuree);
                fiche.setCommentaire(texteOrganisme);

                SolonMgppServiceLocator
                    .getDossierService()
                    .createFicheRepresentationOEP(
                        session,
                        fiche.getDocument(),
                        new ArrayList<DocumentModel>(),
                        new ArrayList<DocumentModel>()
                    );

                LOGGER.debug(session, MgppLogEnumImpl.ADD_OEP_PROCESS_TEC, "Add Oep OK - idCommun = " + idCommun);

                LOGGER.debug(
                    session,
                    MgppLogEnumImpl.ADD_OEP_PROCESS_TEC,
                    "Add Oep publication communication - idCommun = " + idCommun
                );

                // reload fiche
                FichePresentationOEP fichePresentationOEP = SolonMgppServiceLocator
                    .getDossierService()
                    .findFicheOEP(session, idDossier);

                try {
                    // publication de 2 EVT49 a l'injection d'un oep
                    publierEVT49(fichePresentationOEP, fileName, content);
                } catch (Exception e) {
                    LOGGER.debug(
                        session,
                        MgppLogEnumImpl.FAIL_ADD_OEP_PROCESS_TEC,
                        "Add Oep publication communication - idCommun = " + idCommun,
                        e
                    );
                }
            } else {
                LOGGER.warn(session, MgppLogEnumImpl.ADD_OEP_PROCESS_TEC, "numero is empty ");
            }
        } catch (Exception e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_ADD_OEP_PROCESS_TEC, "Add Oep KO - idCommun = " + idCommun, e);
            throw e;
        }
    }

    private void publierEVT49(FichePresentationOEP fichePresentationOEP, String fileName, byte[] content)
        throws Exception {
        WSEvenement wsEvenement = null;

        try {
            wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
        } catch (WSProxyFactoryException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            throw new NuxeoException(e);
        }

        RechercherEvenementRequest rechercherEvenementRequest = new RechercherEvenementRequest();

        CritereRechercheEvenement critereRechercheEvenement = new CritereRechercheEvenement();
        critereRechercheEvenement.setIdDossier(fichePresentationOEP.getIdDossier());
        critereRechercheEvenement.setTypeEvenement(EvenementType.EVT_49);

        rechercherEvenementRequest.setParCritere(critereRechercheEvenement);

        RechercherEvenementResponse rechercherEvenementResponse = null;

        try {
            rechercherEvenementResponse = wsEvenement.rechercherEvenement(rechercherEvenementRequest);
        } catch (HttpTransactionException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(SolonMgppWsLocator.getConnexionFailureMessage(session));
        } catch (Exception e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(e);
        }

        if (rechercherEvenementResponse == null) {
            throw new NuxeoException("Erreur de communication avec SOLON EPP, la récupération des messages a échoué.");
        } else if (
            rechercherEvenementResponse.getStatut() == null ||
            !TraitementStatut.OK.equals(rechercherEvenementResponse.getStatut())
        ) {
            NuxeoException NuxeoException = new NuxeoException(
                "Erreur de communication avec SOLON EPP, la récupération des messages a échoué." +
                WSErrorHelper.buildCleanMessage(rechercherEvenementResponse.getMessageErreur())
            );
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, NuxeoException);
            throw NuxeoException;
        }

        Message last = null;

        for (Message message : rechercherEvenementResponse.getMessage()) {
            if (
                last == null ||
                last.getDateEvenement() != null &&
                message.getDateEvenement() != null &&
                DateUtil
                    .xmlGregorianCalendarToDate(last.getDateEvenement())
                    .before(DateUtil.xmlGregorianCalendarToDate(message.getDateEvenement()))
            ) {
                last = message;
            }
        }

        CreerVersionRequest creerVersionRequest = new CreerVersionRequest();
        creerVersionRequest.setModeCreation(CreationType.PUBLIER);

        EppEvtContainer eppEvtContainer = new EppEvtContainer();
        EppEvt49 eppEvt49 = new EppEvt49();
        eppEvt49.setIdEvenementPrecedent(last == null ? null : last.getIdEvenement());
        eppEvt49.setIdDossier(fichePresentationOEP.getIdDossier());
        eppEvt49.setObjet(fichePresentationOEP.getNomOrganisme());
        eppEvt49.setBaseLegale(fichePresentationOEP.getTexteRef());

        // ASSEMBLEE_NATIONALE
        eppEvt49.setDestinataire(Institution.ASSEMBLEE_NATIONALE);
        eppEvt49.getCopie().clear();

        PieceJointe pieceJointe = new PieceJointe();
        pieceJointe.setType(PieceJointeType.LETTRE_PM);

        ContenuFichier contenuFichier = new ContenuFichier();
        contenuFichier.setADuContenu(Boolean.TRUE);
        contenuFichier.setCompression(CompressionFichier.AUCUNE);
        contenuFichier.setContenu(content);

        fileName = FileUtils.sanitizePathTraversal(fileName);

        String computedSha512 = SHA512Util.getSHA512Hash(content);

        contenuFichier.setSha512(computedSha512);

        MimetypeRegistry mimeService = Framework.getService(MimetypeRegistry.class);

        try {
            String mimeType = mimeService.getMimetypeFromFilenameAndBlobWithDefault(fileName, null, null);
            contenuFichier.setMimeType(mimeType);
        } catch (Exception e) {
            contenuFichier.setMimeType("application/octet-stream");
        }

        contenuFichier.setNomFichier(fileName);

        pieceJointe.getFichier().add(contenuFichier);

        eppEvt49.setLettrePm(pieceJointe);

        // recupération des représentants
        DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        List<DocumentModel> listRepresentantAN = dossierService.fetchRepresentantOEP(
            session,
            VocabularyConstants.REPRESENTANT_TYPE_AN,
            fichePresentationOEP.getDocument().getId()
        );

        for (DocumentModel documentModel : listRepresentantAN) {
            RepresentantOEP representantOEP = documentModel.getAdapter(RepresentantOEP.class);
            if (representantOEP != null) {
                Mandat mandat = new Mandat();
                mandat.setId(representantOEP.getRepresentant());
                if (VocabularyConstants.FONCTION_TITULAIRE_ID.equals(representantOEP.getFonction())) {
                    eppEvt49.getTitulaires().add(mandat);
                } else if (VocabularyConstants.FONCTION_SUPPLEANT_ID.equals(representantOEP.getFonction())) {
                    eppEvt49.getSuppleant().add(mandat);
                }
            }
        }

        eppEvtContainer.setEvt49(eppEvt49);
        eppEvtContainer.setType(EvenementType.EVT_49);
        creerVersionRequest.setEvenement(eppEvtContainer);

        CreerVersionResponse creerVersionResponseAN = null;
        try {
            creerVersionResponseAN = wsEvenement.creerVersion(creerVersionRequest);
        } catch (HttpTransactionException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            throw new NuxeoException(SolonMgppWsLocator.getConnexionFailureMessage(session));
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
            throw new NuxeoException(e);
        }

        if (creerVersionResponseAN == null) {
            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la creation de la communication a échoué."
            );
        } else if (
            creerVersionResponseAN.getStatut() == null ||
            !TraitementStatut.OK.equals(creerVersionResponseAN.getStatut())
        ) {
            NuxeoException clientExc = new NuxeoException(
                "Erreur de communication avec SOLON EPP, la creation de la communication a échoué." +
                WSErrorHelper.buildCleanMessage(creerVersionResponseAN.getMessageErreur())
            );
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
            throw clientExc;
        }

        // SENAT
        eppEvt49.setIdEvenementPrecedent(creerVersionResponseAN.getEvenement().getEvt49().getIdEvenement());
        eppEvt49.setDestinataire(Institution.SENAT);
        eppEvt49.getCopie().clear();

        List<DocumentModel> listRepresentantSE = dossierService.fetchRepresentantOEP(
            session,
            VocabularyConstants.REPRESENTANT_TYPE_SE,
            fichePresentationOEP.getDocument().getId()
        );
        eppEvt49.getTitulaires().clear();
        eppEvt49.getSuppleant().clear();

        for (DocumentModel documentModel : listRepresentantSE) {
            RepresentantOEP representantOEP = documentModel.getAdapter(RepresentantOEP.class);
            if (representantOEP != null) {
                Mandat mandat = new Mandat();
                mandat.setId(representantOEP.getRepresentant());
                if (VocabularyConstants.FONCTION_TITULAIRE_ID.equals(representantOEP.getFonction())) {
                    eppEvt49.getTitulaires().add(mandat);
                } else if (VocabularyConstants.FONCTION_SUPPLEANT_ID.equals(representantOEP.getFonction())) {
                    eppEvt49.getSuppleant().add(mandat);
                }
            }
        }

        eppEvtContainer.setEvt49(eppEvt49);
        eppEvtContainer.setType(EvenementType.EVT_49);

        creerVersionRequest.setEvenement(eppEvtContainer);

        CreerVersionResponse creerVersionResponseSE = null;
        try {
            creerVersionResponseSE = wsEvenement.creerVersion(creerVersionRequest);
        } catch (HttpTransactionException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            throw new NuxeoException(SolonMgppWsLocator.getConnexionFailureMessage(session));
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, e);
            throw new NuxeoException(e);
        }

        if (creerVersionResponseSE == null) {
            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la creation de la communication a échoué."
            );
        } else if (
            creerVersionResponseSE.getStatut() == null ||
            !TraitementStatut.OK.equals(creerVersionResponseSE.getStatut())
        ) {
            NuxeoException clientExc = new NuxeoException(
                "Erreur de communication avec SOLON EPP, la creation de la communication a échoué." +
                WSErrorHelper.buildCleanMessage(creerVersionResponseSE.getMessageErreur())
            );
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_COMM_TEC, clientExc);
            throw clientExc;
        }
    }
}
