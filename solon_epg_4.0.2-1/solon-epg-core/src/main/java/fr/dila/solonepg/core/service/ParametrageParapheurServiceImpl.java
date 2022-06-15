package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.ParametrageParapheurService;
import java.util.ArrayList;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ParametrageParapheurServiceImpl implements ParametrageParapheurService {

    @Override
    public ParapheurFolder getParapheurFolder(CoreSession session, String typeActe, String typeFolderParapheur) {
        String idTypeActe = SolonEpgServiceLocator.getTypeActeService().getId(typeActe);
        DocumentModel parapheur = SolonEpgServiceLocator
            .getParapheurModelService()
            .getParapheurModelFromTypeActe(session, idTypeActe);
        if (parapheur != null) {
            List<ParapheurFolder> list = SolonEpgServiceLocator
                .getParapheurService()
                .getParapheurRootNode(session, parapheur);
            return list.stream().filter(pf -> typeFolderParapheur.equals(pf.getId())).findFirst().orElse(null);
        }

        return null;
    }

    @Override
    public List<ParapheurFolder> getAllParapheurFolders(CoreSession session, String typeActe) {
        List<ParapheurFolder> folderList = new ArrayList<>();
        String idTypeActe = SolonEpgServiceLocator.getTypeActeService().getId(typeActe);
        DocumentModel parapheur = SolonEpgServiceLocator
            .getParapheurModelService()
            .getParapheurModelFromTypeActe(session, idTypeActe);
        if (parapheur != null) {
            folderList = SolonEpgServiceLocator.getParapheurService().getParapheurRootNode(session, parapheur);
        }

        return folderList;
    }
}
