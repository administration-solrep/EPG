package fr.dila.solonepg.ui.services.mgpp.impl;

import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.MgppHistoriqueTreeUIService;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.HistoriqueDossierDTO;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.enums.EtatEvenementEPPEnum;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.ui.bean.DossierHistoriqueEPP;
import fr.dila.st.ui.bean.MessageVersion;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.NuxeoException;

public class MgppHistoriqueTreeUIServiceImpl implements MgppHistoriqueTreeUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(MgppHistoriqueTreeUIServiceImpl.class);

    @Override
    public DossierHistoriqueEPP getHistoriqueEPP(SpecificContext context) {
        String idCurrentEvent = context.getFromContextData(MgppContextDataKey.COMMUNICATION_ID);

        String idDossier = MgppUIServiceLocator
            .getMgppDossierUIService()
            .getCurrentEvenementDTO(context)
            .getIdDossier();

        FichePresentationOEP fichePresentationOEP = SolonMgppServiceLocator
            .getDossierService()
            .findFicheOEP(context.getSession(), idDossier);
        if (fichePresentationOEP != null) {
            idDossier = fichePresentationOEP.getIdsANATLies();
        }

        return buildHistoriqueEPP(context, idDossier, idCurrentEvent);
    }

    private DossierHistoriqueEPP buildHistoriqueEPP(SpecificContext context, String idDossier, String idCurrentEvent) {
        DossierHistoriqueEPP historique = new DossierHistoriqueEPP();
        try {
            HistoriqueDossierDTO historiqueDto = SolonMgppServiceLocator
                .getDossierService()
                .findDossier(idDossier, context.getSession());

            List<MessageVersion> rootMessageVersion = historiqueDto
                .getRootEvents()
                .values()
                .stream()
                .map(rootEvt -> buildMessageVersion(context, rootEvt, historiqueDto.getMapSuivant(), idCurrentEvent))
                .collect(Collectors.toList());

            historique.setLstVersions(rootMessageVersion);
        } catch (NuxeoException e) {
            LOGGER.error(
                context.getSession(),
                STLogEnumImpl.FAIL_GET_DOSSIER_TEC,
                "Erreur de recuperation de l'historique du dossier EPP " + idDossier,
                e
            );
        }
        if (CollectionUtils.isEmpty(historique.getLstVersions())) {
            context
                .getMessageQueue()
                .addErrorToQueue("Aucun historique EPP trouvé pour : " + idDossier, "historiqueEPP_content");
        }
        return historique;
    }

    @Override
    public DossierHistoriqueEPP getHistoriqueEPPFiche(SpecificContext context) {
        String idDossier = MgppUIServiceLocator.getMgppFicheUIService().getIdDossierFromFiche(context);
        if (FichePresentationOEPImpl.DOC_TYPE.equals(context.getCurrentDocument().getType())) {
            FichePresentationOEP fpOEP = context.getCurrentDocument().getAdapter(FichePresentationOEP.class);
            idDossier = fpOEP.getIdsANATLies();
        }
        if (idDossier == null) {
            return new DossierHistoriqueEPP();
        }
        return buildHistoriqueEPP(context, idDossier, "");
    }

    private MessageVersion buildMessageVersion(
        SpecificContext context,
        EvenementDTO evtDto,
        Map<String, List<EvenementDTO>> nextEventMap,
        String idCurrentEvent
    ) {
        MessageVersion messageVersion = new MessageVersion(
            evtDto.getIdEvenement(),
            evtDto.getTypeEvenementLabel(),
            evtDto.getTypeEvenementName(),
            idCurrentEvent.equals(evtDto.getIdEvenement()),
            EtatEvenementEPPEnum.ANNULER.name().equals(evtDto.getEtat())
        );

        List<MessageVersion> childs = new ArrayList<>();
        nextEventMap
            .values()
            .forEach(
                value ->
                    value
                        .stream()
                        .filter(nextEvt -> isEvenementPrecedentEqualsTo(nextEvt, evtDto))
                        .map(nextEvt -> buildMessageVersion(context, nextEvt, nextEventMap, idCurrentEvent))
                        .forEachOrdered(childs::add)
            );
        messageVersion.setLstChilds(childs);
        return messageVersion;
    }

    private static boolean isEvenementPrecedentEqualsTo(EvenementDTO event, EvenementDTO eventDto) {
        return (
            StringUtils.isNotBlank(event.getIdEvenementPrecedent()) &&
            event.getIdEvenementPrecedent().equals(eventDto.getIdEvenement())
        );
    }
}
