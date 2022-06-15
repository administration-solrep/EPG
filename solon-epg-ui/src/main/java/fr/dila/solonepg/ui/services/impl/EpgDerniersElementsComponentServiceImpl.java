package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.core.dto.DossierLinkMinimalMapper;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.EpgDerniersElementsComponentService;
import fr.dila.ss.ui.bean.DernierElementDTO;
import fr.dila.ss.ui.services.impl.SSDerniersElementsComponentServiceImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.DocumentModel;

public class EpgDerniersElementsComponentServiceImpl
    extends SSDerniersElementsComponentServiceImpl
    implements EpgDerniersElementsComponentService {

    @Override
    public List<DernierElementDTO> getDernierElementsDTOFromDocs(SpecificContext context) {
        List<DocumentModel> docs = getListeDerniersDossiersIntervention(context);
        return docs.stream().map(doc -> getDernierElementDTOFromDoc(context, doc)).collect(Collectors.toList());
    }

    @Override
    protected List<DocumentModel> getListeDerniersDossiersIntervention(SpecificContext context) {
        return SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getListeDerniersDossiersIntervention(context.getSession());
    }

    private DernierElementDTO getDernierElementDTOFromDoc(SpecificContext context, DocumentModel doc) {
        DernierElementDTO dto = new DernierElementDTO();

        String numeroNor = PropertyUtil.getStringProperty(
            doc,
            DossierSolonEpgConstants.DOSSIER_SCHEMA,
            DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY
        );
        dto.setLabel(numeroNor);
        dto.setExposant(null);
        dto.setCategorie("document");

        String docId = doc.getId();

        dto.setCaseLinkIdsLabels(
            SolonEpgServiceLocator
                .getCorbeilleService()
                .findDossierLinkUnrestricted(context.getSession(), docId)
                .stream()
                .map(DossierLink::getAdapter)
                .map(DossierLinkMinimalMapper::getIdLabelFromDossierLink)
                .collect(Collectors.toList())
        );
        dto.setId(docId);
        return dto;
    }
}
