package fr.dila.solonepg.ui.contentview;

import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PATTERN;
import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.*;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
//ajout pour requette
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;

import fr.dila.ss.ui.query.pageprovider.SSJournalAdminPageProvider;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.CoreSessionUtil;
import fr.dila.st.core.util.ResourceHelper;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.persistence.NoResultException;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.audit.api.LogEntry;

/**
 * page provider du journal affiche dans l'espace d'administration
 *
 *
 */
public class EpgJournalAdminPageProvider extends SSJournalAdminPageProvider {
    private static final long serialVersionUID = 571377123273370359L;

    private static final STLogger LOG = STLogFactory.getLog(EpgJournalAdminPageProvider.class);

    protected transient Map<String, String> dossierOriginQuestion = new HashMap<>();

    /**
     * Récupération des identifiants du dossier à partir d'un numéro NOR
     */
    @Override
    protected void getDossierIdsList() {
        // récupération de la session
        CoreSession coreSession = getCoreSession();
        CoreSessionUtil.assertSessionNotNull(coreSession);

        Set<String> idsDos = new HashSet<>();
        Optional
            .ofNullable((String) getParameters()[4])
            .filter(StringUtils::isNotEmpty)
            .filter(numeroNor -> DOSSIER_NUMERO_NOR_PATTERN.matcher(numeroNor).matches())
            .map(
                numeroNor -> {
                    idsDos.addAll(getDossierService().getIdsDossiersSupprimes(numeroNor));
                    return getNORService().getDossierIdFromNOR(coreSession, numeroNor);
                }
            )
            .ifPresent(idsDos::add);

        dossierIdList = new LinkedList<>(idsDos);
    }

    @Override
    public String getDossierRef(CoreSession session, LogEntry entry) {
        String dossierRef = entry.getDocUUID();

        if (StringUtils.isBlank(dossierRef)) {
            return dossierRef;
        }

        String norDossier = (String) getParameters()[4];
        if (isNotEmpty(norDossier)) {
            return norDossier;
        }

        String ref;

        //pour le cas de suppression du repertoire dans le fond de dossier on recupere tout le texte qui vient après le dernier espace du commentaire
        // le texte correspond au nor (reference du dossier)

        if(entry.getEventId().equals(SolonEpgEventConstant.DELETE_FOLDER_FDD))
        {
            ref = StringUtils.substringAfterLast(entry.getComment(), " ");

            return ref;

        }

        try {
            String nor = getNORService()
                .getNorFromDossierId(session, dossierRef)
                .orElseGet(() -> getDossierService().getNorDossierSupprime(dossierRef));
            ref = isNotEmpty(nor) ? nor : dossierRef;
        } catch (NoResultException e) {
            LOG.debug(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, "NOR supprimé non retrouvé", e);
            ref = ResourceHelper.getString("label.journal.comment.dossier.supprime");
        }
        return ref;
    }
}
