package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.recherche.FavorisConsultation;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.EPGFavorisConsultationEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgFavorisConsultationUIService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

public class EpgFavorisConsultationUIServiceImpl implements EpgFavorisConsultationUIService {

    private FavorisConsultation getFavorisConsultation(SpecificContext context) {
        EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
        String path = getUserWorkspacePath(context);
        DocumentModel doc = espaceRechercheService.getFavorisConsultation(path, context.getSession());
        return doc.getAdapter(FavorisConsultation.class);
    }

    private String getUserWorkspacePath(SpecificContext context) {
        final DocumentModel docModel = STServiceLocator
            .getUserWorkspaceService()
            .getCurrentUserPersonalWorkspace(context.getSession());
        return docModel.getPathAsString();
    }

    @Override
    public List<String> getFavorisConsultationDossiersId(SpecificContext context) {
        return getFavorisConsultation(context).getDossiersId();
    }

    @Override
    public List<String> getFavorisConsultationFdrsId(SpecificContext context) {
        return getFavorisConsultation(context).getFdrsId();
    }

    @Override
    public List<String> getFavorisConsultationUsers(SpecificContext context) {
        return getFavorisConsultation(context).getUsers();
    }

    @Override
    public void removeFavorisConsultation(SpecificContext context) {
        CoreSession session = context.getSession();
        EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
        String path = getUserWorkspacePath(context);
        List<String> documentModelIdList = context.getFromContextData(EpgContextDataKey.LIST_FAVORIS);
        List<DocumentModel> documentModelList = documentModelIdList
            .stream()
            .map(id -> context.getSession().getDocument(new IdRef(id)))
            .collect(Collectors.toList());

        espaceRechercheService.removeFromFavorisConsultation(session, path, documentModelList);
    }

    @Override
    public void removeFavorisConsultationUser(SpecificContext context) {
        CoreSession session = context.getSession();
        EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
        String path = getUserWorkspacePath(context);
        List<String> documentModelIdList = context.getFromContextData(EpgContextDataKey.LIST_FAVORIS);
        List<DocumentModel> documentModelList = documentModelIdList
            .stream()
            .map(id -> STServiceLocator.getUserManager().getUserModel(id))
            .collect(Collectors.toList());

        espaceRechercheService.removeFromFavorisConsultation(session, path, documentModelList);
    }

    private boolean addFavorisConsultation(
        SpecificContext context,
        List<DocumentModel> documentModelList,
        int nbFavorisAct,
        Long nbFavorisDef
    ) {
        CoreSession session = context.getSession();
        EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
        String path = getUserWorkspacePath(context);

        if (documentModelList.size() + nbFavorisAct > nbFavorisDef) {
            // Pas de remplissage
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("rechercher.favoris.ajout.impossible"));
            return false;
        } else {
            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("rechercher.favoris.ajout"));
            espaceRechercheService.addToFavorisConsultation(session, path, documentModelList);
        }
        return true;
    }

    @Override
    public boolean addDossiersToFavorisConsultation(SpecificContext context) {
        return addToFavorisConsultation(context, EPGFavorisConsultationEnum.DOSSIER);
    }

    @Override
    public boolean addUtilisateursToFavorisConsultation(SpecificContext context) {
        return addToFavorisConsultation(context, EPGFavorisConsultationEnum.USER);
    }

    @Override
    public boolean addFDRToFavorisConsultation(SpecificContext context) {
        return addToFavorisConsultation(context, EPGFavorisConsultationEnum.FDR);
    }

    private boolean addToFavorisConsultation(
        SpecificContext context,
        EPGFavorisConsultationEnum epgFavorisConsultationEnum
    ) {
        ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
        ParametrageApplication parametrageApplication = parametreApplicationService.getParametreApplicationDocument(
            context.getSession()
        );
        List<String> documentModelIdList = context.getFromContextData(EpgContextDataKey.LIST_FAVORIS);
        int nbFavorisAct = 0;
        List<DocumentModel> documentModelList;
        if (epgFavorisConsultationEnum == null) {
            documentModelList = context.getSession().getDocuments(documentModelIdList, null);
        } else {
            nbFavorisAct = epgFavorisConsultationEnum.getNbFavoris(getFavorisConsultation(context));
            documentModelList = epgFavorisConsultationEnum.getDocumentModelList(context, documentModelIdList);
        }
        Long nbFavorisDef = parametrageApplication.getNbFavorisConsultation();
        return addFavorisConsultation(context, documentModelList, nbFavorisAct, nbFavorisDef);
    }
}
