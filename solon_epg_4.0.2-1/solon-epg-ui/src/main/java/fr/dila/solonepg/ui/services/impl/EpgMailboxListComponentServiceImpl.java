package fr.dila.solonepg.ui.services.impl;

import static fr.dila.st.core.util.ObjectHelper.requireNonNullElse;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.EpgMailboxListComponentService;
import fr.dila.ss.api.enums.TypeRegroupement;
import fr.dila.ss.ui.bean.MailboxListDTO;
import fr.dila.ss.ui.services.impl.SSMailboxListComponentServiceImpl;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.bean.TreeElementDTO;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.session.UserSession;

public class EpgMailboxListComponentServiceImpl
    extends SSMailboxListComponentServiceImpl
    implements EpgMailboxListComponentService {

    @Override
    public Map<String, Object> getData(SpecificContext context) {
        TypeRegroupement modeTri = null;
        final CoreSession session = context.getSession();
        DocumentModel profilUtilisateurDoc = SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getProfilUtilisateurForCurrentUser(session)
            .getDocument();

        final UserSession userSession = context.getWebcontext().getUserSession();

        // On récupère l'item actif en session puis on vérifie s'il a changé dans le contexte
        String activeKey = context.getFromContextData(ACTIVE_KEY);
        if (context.getFromContextData(ACTIVE_KEY) != null) {
            activeKey = context.getFromContextData(ACTIVE_KEY);
        }

        MailboxListDTO dto;
        if (userSession.containsKey(SESSION_KEY)) {
            // On récupère le DTO en session s'il existe
            dto = (MailboxListDTO) userSession.get(SESSION_KEY);
        } else {
            dto = MapDoc2Bean.docToBean(profilUtilisateurDoc, MailboxListDTO.class);
        }
        // On récupère le mode de tri dans le contexte
        modeTri = ObjectHelper.requireNonNullElse(context.getFromContextData(MODE_TRI_KEY), dto.getModeTri());
        boolean modeTriChanged = false;
        if (dto.getModeTri() != modeTri) {
            dto.setModeTri(modeTri);
            modeTriChanged = true;
        }

        Boolean refreshCorbeille = requireNonNullElse(context.getFromContextData(REFRESH_CORBEILLE_KEY), TRUE);

        // si le mode de tri sélectionné est différent de celui sauvegardé
        // On met à jour notre premier niveau
        if (isTrue(refreshCorbeille) || dto.getChilds().isEmpty() || modeTriChanged) {
            Map<String, Integer> map = SolonEpgServiceLocator
                .getSolonEpgCorbeilleTreeService()
                .getCorbeilleTree(session, modeTri);

            List<TreeElementDTO> childs = constructListElementFromMap(map, "", modeTri);
            if (TypeRegroupement.PAR_POSTE == modeTri) {
                String posteDefautProfilUtilisateur = profilUtilisateurDoc
                    .getAdapter(ProfilUtilisateur.class)
                    .getPosteDefaut();
                childs
                    .stream()
                    .filter(treeElement -> treeElement.getKey().equals(posteDefautProfilUtilisateur))
                    .findFirst()
                    .ifPresent(
                        posteDefaut -> {
                            childs.remove(posteDefaut);
                            childs.add(0, posteDefaut);
                        }
                    );
            }
            dto.setChilds(childs);
        }

        // On renvoie dans la map de données le DTO et l'item actif
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("mailboxListMap", dto);
        returnMap.put(
            ACTIVE_KEY,
            activeKey != null ? activeKey : UserSessionHelper.getUserSessionParameter(context, ACTIVE_KEY, String.class)
        );

        // On sauvegarde en session le DTO et l'item actif
        userSession.put(ACTIVE_KEY, activeKey);
        userSession.put(SESSION_KEY, dto);
        userSession.put(REFRESH_CORBEILLE_KEY, FALSE);
        return returnMap;
    }

    protected List<TreeElementDTO> constructListElementFromMap(
        Map<String, Integer> map,
        String parentKey,
        TypeRegroupement modeTri
    ) {
        return MapUtils.isNotEmpty(map)
            ? map
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() != 0 || TypeRegroupement.PAR_POSTE == modeTri)
                .map(
                    entry ->
                        new TreeElementDTO(
                            fetchLabel(entry.getKey(), modeTri) + " - (" + entry.getValue() + ")",
                            entry.getKey(),
                            parentKey + "__" + entry.getKey(),
                            fetchLink(entry.getKey(), modeTri),
                            true
                        )
                )
                .sorted(Comparator.comparing(TreeElementDTO::getLabel))
                .collect(Collectors.toList())
            : new ArrayList<>();
    }

    protected String fetchLabel(String key, TypeRegroupement modeTri) {
        String label = key;
        if (TypeRegroupement.PAR_POSTE == modeTri) {
            PosteNode poste = STServiceLocator
                .getOrganigrammeService()
                .getOrganigrammeNodeById(key, OrganigrammeType.POSTE);
            if (poste != null) {
                label = poste.getLabel();
            }
        } else if (TypeRegroupement.PAR_TYPE_ACTE == modeTri) {
            label =
                STServiceLocator.getVocabularyService().getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY, key);
        } else if (TypeRegroupement.PAR_TYPE_ETAPE == modeTri) {
            label =
                STServiceLocator
                    .getVocabularyService()
                    .getEntryLabel(STVocabularyConstants.ROUTING_TASK_TYPE_VOCABULARY, key);
        }
        return label;
    }

    protected String fetchLink(String parentKey, TypeRegroupement modeTri) {
        StringBuilder link = new StringBuilder();
        if (TypeRegroupement.PAR_POSTE == modeTri) {
            link.append(LINK_URL_POSTE + "?posteId=" + parentKey);
        } else if (TypeRegroupement.PAR_TYPE_ACTE == modeTri) {
            link.append(LINK_URL_TYPE_ACTE + "?typeActe=" + parentKey);
        } else if (TypeRegroupement.PAR_TYPE_ETAPE == modeTri) {
            link.append(LINK_URL_TYPE_ETAPE + "?typeEtape=" + parentKey);
        }
        return link.toString();
    }
}
