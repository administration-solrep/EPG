package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.core.event.helper.DocumentSearchEventHelper;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.EpgModeleFdrFicheUIService;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonepg.ui.th.model.bean.EpgModeleFdrForm;
import fr.dila.ss.ui.services.impl.SSAbstractModeleFdrFicheUIService;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;

public class EpgModeleFdrFicheUIServiceImpl
    extends SSAbstractModeleFdrFicheUIService<EpgModeleFdrForm, SolonEpgFeuilleRoute>
    implements EpgModeleFdrFicheUIService {

    @Override
    protected EpgModeleFdrForm convertDocToModeleForm(
        SpecificContext context,
        DocumentModel doc,
        EpgModeleFdrForm modeleForm
    ) {
        OrganigrammeService organigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
        SolonEpgFeuilleRoute modele = doc.getAdapter(SolonEpgFeuilleRoute.class);
        super.convertDocToModeleForm(context, doc, modeleForm);

        EpgModeleFdrForm epgModeleForm = modeleForm;

        epgModeleForm.setNumero(modele.getNumero().intValue());
        epgModeleForm.setTypeActe(modele.getTypeActe());
        VocabularyService vocService = ServiceUtil.getRequiredService(VocabularyService.class);
        epgModeleForm.setTypeActeLibelle(
            modele.getTypeActe() != null
                ? vocService.getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY, modele.getTypeActe())
                : ""
        );
        epgModeleForm.setIdDirection(modele.getDirection());
        if (modeleForm.getIdDirection() != null) {
            modeleForm.setMapDirection(
                (
                    Collections.singletonMap(
                        modeleForm.getIdDirection(),
                        STServiceLocator
                            .getOrganigrammeService()
                            .getOrganigrammeNodeById(modeleForm.getIdDirection(), OrganigrammeType.DIRECTION)
                            .getLabel()
                    )
                )
            );
        }
        epgModeleForm.setLibelleDirection(
            StringUtils.isNotEmpty(modele.getDirection())
                ? organigrammeService
                    .getOrganigrammeNodeById(modele.getDirection(), OrganigrammeType.DIRECTION)
                    .getLabel()
                : ""
        );
        epgModeleForm.setIntituleComplet(epgModeleForm.getIntitule());
        String[] intituleTab = SolonEpgActionServiceLocator
            .getEpgModeleFeuilleRouteActionService()
            .splitIntitule(
                epgModeleForm.getIntitule(),
                epgModeleForm.getIdMinistere(),
                epgModeleForm.getIdDirection(),
                epgModeleForm.getTypeActe()
            );
        epgModeleForm.setIntitule(intituleTab[0]);
        epgModeleForm.setIntituleLibre(intituleTab[1]);
        epgModeleForm.setIsFeuilleRouteDefautGlobal(modele.isFeuilleRouteDefautGlobal());

        DocumentSearchEventHelper.raiseEvent(context.getSession(), doc);
        return epgModeleForm;
    }

    @Override
    public SolonEpgFeuilleRoute convertFormToFeuilleRoute(EpgModeleFdrForm modeleForm, SolonEpgFeuilleRoute modele) {
        MapDoc2Bean.beanToDoc(modeleForm, modele.getDocument());
        modele.setTitle(
            SolonEpgServiceLocator
                .getFeuilleRouteModelService()
                .creeTitleModeleFDR(
                    modeleForm.getIdMinistere(),
                    modeleForm.getIdDirection(),
                    modeleForm.getTypeActe(),
                    modeleForm.getIntituleLibre()
                )
        );
        modele.setTypeActe(modeleForm.getTypeActe());
        modele.setDirection(modeleForm.getIdDirection());
        return modele;
    }
}
