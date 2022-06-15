package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.security.principal.STPrincipal;
import fr.dila.st.core.util.DirSessionUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.query.sql.model.QueryBuilder;
import org.nuxeo.ecm.directory.Session;

/**
 * Surcharge du profilService
 * @author Fabio Esposito
 *
 */
public class ProfileServiceImpl extends fr.dila.st.core.service.ProfileServiceImpl {
    private static final long serialVersionUID = -5863032927051461627L;

    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(ProfileServiceImpl.class);

    @Override
    public List<DocumentModel> getProfilListForUserCreate(STPrincipal principal) {
        if (log.isDebugEnabled()) {
            log.debug("Génération de la liste des profils");
        }

        List<DocumentModel> profilDocList = new ArrayList<>();
        List<DocumentModel> docList = null;
        try (Session session = DirSessionUtil.getSession(STConstant.ORGANIGRAMME_PROFILE_DIR);) {
            docList = session.query(new QueryBuilder(), false);
        }

        for (DocumentModel profilDoc : docList) {
            if (profilDoc != null) {
                Set<String> baseFunctionSet = getBaseFunctionFromProfil(profilDoc.getId());

                boolean filter1 =
                    !baseFunctionSet.contains(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER) ||
                    principal.isMemberOf(STBaseFunctionConstant.PROFIL_CREATOR);
                //filtre sur les profils sgg
                boolean filter2 =
                    !baseFunctionSet.contains(STBaseFunctionConstant.PROFIL_SGG) ||
                    principal.isMemberOf(STBaseFunctionConstant.PROFIL_SGG);
                //filtre sur le profil webservice : seul les profils webservices n'ont pas eccès à l'application
                boolean filter3 =
                    baseFunctionSet.contains(STBaseFunctionConstant.INTERFACE_ACCESS) ||
                    principal.isMemberOf(SolonEpgBaseFunctionConstant.PROFIL_WEBSERVICE_UPDATER);

                if (filter1 && filter2 && filter3) {
                    profilDocList.add(profilDoc);
                }
            }
        }

        Collections.sort(
            profilDocList,
            new Comparator<DocumentModel>() {

                @Override
                public int compare(DocumentModel p1, DocumentModel p2) {
                    return p1.getId().compareTo(p2.getId());
                }
            }
        );

        return profilDocList;
    }
}
