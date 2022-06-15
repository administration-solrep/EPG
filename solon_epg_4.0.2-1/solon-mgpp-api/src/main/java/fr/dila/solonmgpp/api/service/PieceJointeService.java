package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.MgppPieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;
import fr.sword.xsd.solon.epp.PieceJointeType;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;

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
     */
    void setContentFromEpp(
        PieceJointeFichierDTO pieceJointeFichierDTO,
        EvenementDTO evenementDTO,
        MgppPieceJointeDTO pieceJointeDTO,
        CoreSession session
    );

    /**
     * Creation d'une {@link MgppPieceJointeDTO}
     *
     * @param pieceJointeType
     * @return
     */
    MgppPieceJointeDTO createPieceJointe(String pieceJointeType);

    /**
     * Creation d'un {@link PieceJointeFichierDTO} à partir d'un {@link Blob}
     *
     * @param blob
     * @param session
     * @return
     */
    PieceJointeFichierDTO createPieceJointeFichierFromBlob(Blob blob, CoreSession session);

    /**
     * Recherche de la bonne enum par rapport au type de piece jointe
     *
     * @param type
     * @return
     */
    PieceJointeType getCorrectPieceJointeType(String type);
}
