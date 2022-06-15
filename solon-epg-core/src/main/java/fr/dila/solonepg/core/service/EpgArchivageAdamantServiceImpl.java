package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.EpgArchivageAdamantService;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.IterableQueryResult;

public class EpgArchivageAdamantServiceImpl implements EpgArchivageAdamantService {

    @Override
    public int getNbDosExtracting(final CoreSession session) {
        String query;
        int nbDossier = 0;

        query =
            "SELECT count(*) FROM dossier_solon_epg d " +
            "WHERE d.statutarchivage = '" +
            VocabularyConstants.STATUT_ARCHIVAGE_EXTRACTION_EN_COURS +
            "'";

        IterableQueryResult resCount = QueryUtils.doSqlQuery(
            session,
            new String[] { FlexibleQueryMaker.COL_COUNT },
            query,
            new Object[] {}
        );

        if (resCount != null) {
            Iterator<Map<String, Serializable>> iterator = resCount.iterator();
            if (iterator.hasNext()) {
                Map<String, Serializable> row = iterator.next();
                nbDossier = ((Long) row.get(FlexibleQueryMaker.COL_COUNT)).intValue();
            }
            resCount.close();
        }
        return nbDossier;
    }
}
