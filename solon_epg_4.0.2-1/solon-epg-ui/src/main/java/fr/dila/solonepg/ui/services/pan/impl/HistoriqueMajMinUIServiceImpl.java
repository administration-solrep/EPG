package fr.dila.solonepg.ui.services.pan.impl;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TARGET;
import fr.dila.solonepg.api.service.HistoriqueMajMinisterielleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.LigneHistoriqueMAJMinisterielleDTO;
import fr.dila.solonepg.ui.bean.pan.LigneHistoriquePanUnsortedList;
import fr.dila.solonepg.ui.constants.pan.PanConstants;
import fr.dila.solonepg.ui.contentview.PanMajMinPageProvider;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.helper.PanHelper;
import fr.dila.solonepg.ui.services.pan.HistoriqueMajMinUIService;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.trash.TrashService;
import org.nuxeo.runtime.api.Framework;

/**
 * Bean pour l'historique des mises à jour ministérielles
 *
 * @author jgomez
 */
public class HistoriqueMajMinUIServiceImpl implements HistoriqueMajMinUIService {
    private static final String SUCCESS_MESSAGE_SUPPRESSION = "label.epg.document.supprime";

    @Override
    public MAJ_TARGET getTarget(SpecificContext context) {
        PanConstants.Section currentSection = PanConstants.Section.findByKey(
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION)
        );
        switch (Objects.requireNonNull(currentSection)) {
            case SECTION_RATIFICATION:
                return MAJ_TARGET.ORDONNANCE;
            case SECTION_LOIS:
                return MAJ_TARGET.APPLICATION_LOI;
            case SECTION_DIRECTIVES:
                return MAJ_TARGET.TRANSPOSITION;
            case SECTION_HABILITATIONS:
                return MAJ_TARGET.HABILITATION;
            case SECTION_ORDONNANCES:
                return MAJ_TARGET.APPLICATION_ORDONNANCE;
            default:
                return null;
        }
    }

    @Override
    public String targetedDelete(SpecificContext context) {
        return delete(context, context.getFromContextData(STContextDataKey.IDS));
    }

    @Override
    public String massDelete(SpecificContext context) {
        HistoriqueMajMinisterielleService service = SolonEpgServiceLocator.getHistoriqueMajMinisterielleService();
        List<DocumentModel> docs = service.getHistoriqueMaj(context.getSession(), getTarget(context));

        if (CollectionUtils.isNotEmpty(docs)) {
            TrashService trashService = Framework.getService(TrashService.class);
            trashService.trashDocuments(docs);
            return ResourceHelper.getString(SUCCESS_MESSAGE_SUPPRESSION, docs.size());
        } else {
            return "";
        }
    }

    private String delete(SpecificContext context, List<String> lineIds) {
        String successMessage = "";
        if (CollectionUtils.isNotEmpty(lineIds)) {
            successMessage = ResourceHelper.getString(SUCCESS_MESSAGE_SUPPRESSION, lineIds.size());
            context.getSession().removeDocuments(lineIds.stream().map(IdRef::new).toArray(DocumentRef[]::new));
            context.getMessageQueue().addSuccessToQueue(successMessage);
        }
        return successMessage;
    }

    @Override
    public LigneHistoriquePanUnsortedList getHistoriquePaginated(SpecificContext context, MAJ_TARGET majTarget) {
        LigneHistoriquePanUnsortedList results = new LigneHistoriquePanUnsortedList();
        results.buildColonnes();

        DocumentRef ref = SolonEpgServiceLocator.getHistoriqueMajMinisterielleService().getRef(majTarget);
        CoreSession session = context.getSession();
        if (session.exists(ref)) {
            String id = session.getDocument(ref).getId();

            Map<String, Serializable> mapSearch = context.getFromContextData(
                PanContextDataKey.JSON_SEARCH_HISTORIQUE_MAJ
            );
            LoisSuiviesForm loisSuiviForm = PanHelper.getPaginateFormFromUserSession(mapSearch, context);
            PanMajMinPageProvider provider = loisSuiviForm.getPageProvider(
                session,
                "panMajMinPageProvider",
                Collections.singletonList(id)
            );
            List<LigneHistoriqueMAJMinisterielleDTO> historique = provider
                .getCurrentPage()
                .stream()
                .map(LigneHistoriqueMAJMinisterielleDTO::new)
                .collect(Collectors.toList());

            results.setNbTotal(provider.getResultsCount());
            results.setListe(historique);
        }
        return results;
    }
}
