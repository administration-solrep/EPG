package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.parapheur.ParapheurFolder;
import fr.dila.solonepg.api.service.ParametrageParapheurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgParametrageParapheurUIService;
import fr.dila.solonepg.ui.th.bean.ParametreParapheurFormConsultation;
import fr.dila.solonepg.ui.th.bean.ParametreParapheurFormCreation;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.List;

public class EpgParametrageParapheurUIServiceImpl implements EpgParametrageParapheurUIService {

    @Override
    public void save(SpecificContext context) {
        List<ParametreParapheurFormCreation> list = context.getFromContextData(
            EpgContextDataKey.PARAMETRE_PARAPHEUR_FORM_LIST
        );
        String typeActe = context.getFromContextData(EpgContextDataKey.TYPE_ACTE);

        ParametrageParapheurService parametrageParapheurService = SolonEpgServiceLocator.getParametrageParapheurService();
        for (ParametreParapheurFormCreation parametreParapheurFormCreation : list) {
            ParapheurFolder parapheurFolder = parametrageParapheurService.getParapheurFolder(
                context.getSession(),
                typeActe,
                parametreParapheurFormCreation.getParapheurFolderId()
            );

            MapDoc2Bean.beanToDoc(parametreParapheurFormCreation, parapheurFolder.getDocument());
            context.getSession().saveDocument(parapheurFolder.getDocument());
        }
    }

    @Override
    public List<ParametreParapheurFormConsultation> getAllParametreParapheurDocument(SpecificContext context) {
        ParametrageParapheurService parametrageParapheurService = SolonEpgServiceLocator.getParametrageParapheurService();
        List<ParametreParapheurFormConsultation> parametreParapheurFormList = new ArrayList<>();
        String typeActe = context.getFromContextData(EpgContextDataKey.TYPE_ACTE);
        List<ParapheurFolder> parapheurFolderList = parametrageParapheurService.getAllParapheurFolders(
            context.getSession(),
            typeActe
        );

        for (ParapheurFolder parapheurFolder : parapheurFolderList) {
            ParametreParapheurFormConsultation parametreParapheurForm = MapDoc2Bean.docToBean(
                parapheurFolder.getDocument(),
                ParametreParapheurFormConsultation.class
            );
            parametreParapheurForm.setParapheurFolderId(parapheurFolder.getId());
            parametreParapheurForm.setParapheurFolderLabel(parapheurFolder.getName());
            parametreParapheurFormList.add(parametreParapheurForm);
        }
        return parametreParapheurFormList;
    }
}
