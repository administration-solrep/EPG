package fr.dila.solonepg.ui.contentview.mgpp;

import fr.dila.solonepg.ui.helper.mgpp.MgppMessageListProviderHelper;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.contentview.AbstractDTOPageProvider;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.nuxeo.ecm.core.api.CoreSession;

public class MgppMessageListPageProvider extends AbstractDTOPageProvider {
    private static final long serialVersionUID = 1L;
    public static final String CORBEILLE_NODE_PROPERTY = "corbeilleNode";
    public static final String CRITERE_RECHERCHE_PROPERTY = "critereRecherche";
    public static final String DOSSIER_PARLEMENTAIRE_PROPERTY = "dossierParlementaire";
    public static final String IS_RECHERCHE_RAPIDE_PROPERTY = "isRechercheRapide";
    public static final String MAP_SEARCH_PROPERTY = "mapSearch";

    @SuppressWarnings("unchecked")
    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) {
        CorbeilleNode corbeilleNode = (CorbeilleNode) properties.get(CORBEILLE_NODE_PROPERTY);
        String dossierParlementaire = (String) properties.get(DOSSIER_PARLEMENTAIRE_PROPERTY);
        boolean isRechercheRapide = (boolean) properties.get(IS_RECHERCHE_RAPIDE_PROPERTY);
        CritereRechercheDTO critereRecherche = (CritereRechercheDTO) properties.get(CRITERE_RECHERCHE_PROPERTY);
        Map<String, Serializable> mapSearch = (Map<String, Serializable>) properties.get(MAP_SEARCH_PROPERTY);

        List<fr.dila.solonmgpp.api.dto.MessageDTO> messages;
        if (isRechercheRapide) {
            critereRecherche.setSortInfos(sortInfos);
            messages = SolonMgppServiceLocator.getRechercheService().findMessage(critereRecherche, coreSession);
        } else {
            messages =
                SolonMgppServiceLocator
                    .getMessageService()
                    .filtrerCorbeille(
                        corbeilleNode,
                        coreSession,
                        mapSearch,
                        sortInfos,
                        (SSPrincipal) coreSession.getPrincipal(),
                        dossierParlementaire
                    );
        }

        resultsCount = messages.size();

        Map<String, String> mapNiveauLecture = STServiceLocator
            .getVocabularyService()
            .getAllEntries(VocabularyConstants.VOCABULARY_NIVEAU_LECTURE_DIRECTORY);

        currentItems =
            LongStream
                .range(offset, Math.min(offset + getPageSize(), messages.size()))
                .mapToObj(i -> messages.get((int) i))
                .map(message -> MgppMessageListProviderHelper.convertToMessageDTO(message, mapNiveauLecture))
                .collect(Collectors.toList());
    }
}
