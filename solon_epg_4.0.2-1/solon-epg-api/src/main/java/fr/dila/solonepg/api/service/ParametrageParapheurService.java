package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public interface ParametrageParapheurService {
    /**
     * Récupère un répertoire spécifique contenant les informations sur le paramètrage parapheur.
     *
     * @param session
     * @param typeActe
     * @param typeFolderParapheur
     * @return
     */
    ParapheurFolder getParapheurFolder(CoreSession session, String typeActe, String typeFolderParapheur);

    /**
     * Récupère les répértoires d'un parapheur.
     *
     * @param session
     * @param typeActe
     * @return
     */
    List<ParapheurFolder> getAllParapheurFolders(CoreSession session, String typeActe);
}
