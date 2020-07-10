package fr.dila.solonmgpp.core.service;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepp.rest.api.WSEvenement;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.PieceJointeService;
import fr.dila.solonmgpp.core.dto.PieceJointeDTOImpl;
import fr.dila.solonmgpp.core.dto.PieceJointeFichierDTOImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.SHA512Util;
import fr.dila.st.rest.client.HttpTransactionException;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.ChercherPieceJointeRequest;
import fr.sword.xsd.solon.epp.ChercherPieceJointeResponse;
import fr.sword.xsd.solon.epp.CompressionFichier;
import fr.sword.xsd.solon.epp.ContenuFichier;
import fr.sword.xsd.solon.epp.EvtId;
import fr.sword.xsd.solon.epp.PieceJointeType;
import fr.sword.xsd.solon.epp.Version;

/**
 * Implementation de {@link PieceJointeService}
 * 
 * @author asatre
 * 
 */
public class PieceJointeServiceImpl implements PieceJointeService {
  
    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOGGER = STLogFactory.getLog(PieceJointeServiceImpl.class);
    

    @Override
    public void setContentFromEpp(PieceJointeFichierDTO pieceJointeFichierDTO, EvenementDTO evenementDTO, PieceJointeDTO pieceJointeDTO,
            CoreSession session) throws ClientException {
        WSEvenement wsEvenement = null;

        try {
            wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
        } catch (WSProxyFactoryException e) {
            LOGGER.error(session,MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new ClientException(e);
        }

        ChercherPieceJointeRequest chercherPieceJointeRequest = new ChercherPieceJointeRequest();
        chercherPieceJointeRequest.setCompression(pieceJointeFichierDTO.getCompression() == null ? CompressionFichier.AUCUNE : CompressionFichier.valueOf(pieceJointeFichierDTO
                .getCompression()));
        chercherPieceJointeRequest.setIdDossier(evenementDTO.getIdDossier());

        EvtId evtId = new EvtId();
        evtId.setId(evenementDTO.getIdEvenement());

        Version version = new Version();
        version.setMajeur(evenementDTO.getVersionCouranteMajeur());
        version.setMineur(evenementDTO.getVersionCouranteMineur());
        evtId.setVersion(version);

        chercherPieceJointeRequest.setIdEvt(evtId);
        chercherPieceJointeRequest.setNomFichier(pieceJointeFichierDTO.getNomFichier());

        try {
            PieceJointeType pieceJointeType = getCorrectPieceJointeType(pieceJointeDTO.getType());
            chercherPieceJointeRequest.setTypePieceJointe(pieceJointeType);
        } catch (Exception e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_PIECE_JOINTE_TEC, e);
            throw new ClientException(e.getMessage());
        }

        ChercherPieceJointeResponse chercherPieceJointeResponse = null;

        try {
            chercherPieceJointeResponse = wsEvenement.chercherPieceJointe(chercherPieceJointeRequest);
        } catch (HttpTransactionException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            throw new ClientException(SolonMgppWsLocator.getConnexionFailureMessage(session));
        } catch (Exception e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_PIECE_JOINTE_TEC, e);            
            throw new ClientException(e);
        }

        if (chercherPieceJointeResponse == null || !TraitementStatut.OK.equals(chercherPieceJointeResponse.getStatut())) {
            throw new ClientException("Erreur de communication avec SOLON EPP, la récupération de la piece jointe a échoué.");
        }

        for (ContenuFichier contenuFichier : chercherPieceJointeResponse.getPieceJointe().getFichier()) {
            pieceJointeFichierDTO.setContenu(contenuFichier.getContenu());
            break;
        }

    }

    @Override
    public PieceJointeType getCorrectPieceJointeType(String type) {

        try {
            return PieceJointeType.fromValue(type);
        } catch (Exception e) {
            // retry sans "S"
            return PieceJointeType.fromValue(type.substring(0, type.length() - 1));
        }
    }

    @Override
    public PieceJointeDTO createPieceJointe(String pieceJointeType) throws ClientException {
        if (StringUtils.isBlank(pieceJointeType)) {
            throw new ClientException("Le type de la pièce jointe est obligatoire.");
        }

        PieceJointeDTO pieceJointeDTO = new PieceJointeDTOImpl();
        pieceJointeDTO.setType(pieceJointeType);
        return pieceJointeDTO;
    }

    @Override
    public PieceJointeFichierDTO createPieceJointeFichierFromBlob(Blob blob, CoreSession session) throws ClientException {
        PieceJointeFichierDTO pieceJointeFichierDTO = new PieceJointeFichierDTOImpl();
        try {
            byte[] content = blob.getByteArray();
            pieceJointeFichierDTO.setContenu(content);
            pieceJointeFichierDTO.setSha512(SHA512Util.getSHA512Hash(content));
        } catch (IOException e) {
            ClientException clientException = new ClientException("Erreur lors de la lecture du fichier.");
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CREATE_PIECE_JOINTE_FROM_BLOB_TEC, clientException) ;
            throw clientException ;
        }
        pieceJointeFichierDTO.setNomFichier(blob.getFilename());
        pieceJointeFichierDTO.setMimeType(blob.getMimeType());
        pieceJointeFichierDTO.setCompression(blob.getMimeType().toLowerCase().contains("zip") ? "ZIP" : "AUCUNE");

        return pieceJointeFichierDTO;
    }

}
