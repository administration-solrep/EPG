package fr.dila.solonmgpp.api.service;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;
import fr.sword.xsd.solon.epp.PieceJointeType;

/**
 * Service permettant de gérer les pieces jointes.
 * 
 * @author asatre
 * 
 */
public interface PieceJointeService {

    /**
     * set le contenu du {@link PieceJointeFichierDTO} à partir de EPP
     * 
     * @param pieceJointeFichierDTO
     * @param evenementDTO
     * @throws ClientException
     */
    void setContentFromEpp(PieceJointeFichierDTO pieceJointeFichierDTO, EvenementDTO evenementDTO, PieceJointeDTO pieceJointeDTO, CoreSession session)
            throws ClientException;

    /**
     * Creation d'une {@link PieceJointeDTO}
     * 
     * @param pieceJointeType
     * @return
     * @throws ClientException
     */
    PieceJointeDTO createPieceJointe(String pieceJointeType) throws ClientException;

    /**
     * Creation d'un {@link PieceJointeFichierDTO} à partir d'un {@link Blob}
     * 
     * @param blob
     * @param session
     * @return
     * @throws ClientException
     */
    PieceJointeFichierDTO createPieceJointeFichierFromBlob(Blob blob, CoreSession session) throws ClientException;

    /**
     * Recherche de la bonne enum par rapport au type de piece jointe
     * 
     * @param type
     * @return
     */
    PieceJointeType getCorrectPieceJointeType(String type);

}
