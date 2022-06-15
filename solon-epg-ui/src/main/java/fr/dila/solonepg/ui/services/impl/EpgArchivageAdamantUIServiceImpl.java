package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getTypeActeService;

import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.adamant.service.ExtractionLotService;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.ui.services.EpgArchivageAdamantUIService;
import fr.dila.st.core.util.SolonDateConverter;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.IterableQueryResult;

public class EpgArchivageAdamantUIServiceImpl implements EpgArchivageAdamantUIService {
    private static final String QUERY_SELECT_COUNT = "SELECT count(distinct d.id) FROM dossier_solon_epg d ";
    private static final String QUERY_SELECT = "SELECT distinct d.id FROM dossier_solon_epg d ";
    private static final String JOIN_VECTEUR = "LEFT OUTER JOIN DOS_VECTEURPUBLICATION v ON d.id = v.id ";
    private static final String VECTEUR_NOT_NULL = "AND v.id IS NOT NULL ";
    private static final String STATUT_ARCHIVAGE =
        "'" +
        VocabularyConstants.STATUT_ARCHIVAGE_AUCUN +
        "','" +
        VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE +
        "','" +
        VocabularyConstants.STATUT_ARCHIVAGE_BASE_INTERMEDIAIRE +
        "','" +
        VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE +
        "'";
    public static final String GET_VECTORS_QUERY = "SELECT v.ecm:uuid as id FROM VecteurPublication as v ";

    private static final String QUERY_PUBLIE =
        "INNER JOIN retour_dila r on d.id=r.id " +
        JOIN_VECTEUR +
        "WHERE d.statutarchivage IN " +
        "(" +
        STATUT_ARCHIVAGE +
        ")" +
        " AND d.id_extraction_lot IS NULL " +
        VECTEUR_NOT_NULL +
        " AND d.typeacte IN (%s) " +
        " AND v.item IN (%s) " +
        " AND d.statut = " +
        VocabularyConstants.STATUT_PUBLIE +
        " AND r.dateparutionjorf BETWEEN DATE '%s' AND DATE '%s'";

    private static final String QUERY_CLOS =
        "LEFT OUTER JOIN (SELECT MAX(lc.LOG_EVENT_DATE) AS EVENTDATE,	lc.LOG_DOC_UUID " +
        "FROM NXP_LOGS lc GROUP BY lc.LOG_DOC_UUID) llc ON llc.LOG_DOC_UUID = d.ID " +
        JOIN_VECTEUR +
        "WHERE d.STATUT = '" +
        VocabularyConstants.STATUT_CLOS +
        "' AND d.STATUTARCHIVAGE IN " +
        "(" +
        STATUT_ARCHIVAGE +
        ") " +
        VECTEUR_NOT_NULL +
        "AND d.typeacte IN (%s) " +
        "AND v.item IN (%s) " +
        "AND d.ID_EXTRACTION_LOT IS NULL AND llc.EVENTDATE BETWEEN DATE '%s' AND DATE '%s'";

    private static final String QUERY_TERMINE_SANS_PUBLICATION =
        "LEFT OUTER JOIN dos_rubriques u ON u.id = d.id AND u.item = 'REPRISE' " +
        JOIN_VECTEUR +
        "LEFT OUTER JOIN (SELECT MIN(lt.LOG_EVENT_DATE) AS EVENTDATETERMINE,	lt.LOG_DOC_UUID " +
        "FROM NXP_LOGS lt WHERE lt.LOG_EVENT_COMMENT = 'label.journal.comment.terminerDossierSansPublication' " +
        "GROUP BY lt.LOG_DOC_UUID) llt ON llt.LOG_DOC_UUID = d.ID " +
        "WHERE d.STATUT = '" +
        VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION +
        "' AND d.STATUTARCHIVAGE IN " +
        "(" +
        STATUT_ARCHIVAGE +
        ") " +
        VECTEUR_NOT_NULL +
        " AND d.typeacte IN (%s) " +
        " AND v.item IN (%s) " +
        "AND d.ID_EXTRACTION_LOT IS NULL AND ((u.ITEM IS NOT NULL " +
        "AND (llt.EVENTDATETERMINE IS NULL OR llt.EVENTDATETERMINE < d.CREATED) " +
        "AND d.created BETWEEN DATE '%s' AND DATE '%s') " +
        "OR (u.ITEM IS NULL AND llt.EVENTDATETERMINE BETWEEN DATE '%s' AND DATE '%s'))";

    @Override
    public int countDossierEligible(final CoreSession session, String statut, Date startDate, Date endDate) {
        int nbDocument = 0;
        if (StringUtils.isNotEmpty(statut)) {
            nbDocument = doQueryCountDosEligible(session, statut, startDate, endDate);
        } else {
            nbDocument += doQueryCountDosEligible(session, VocabularyConstants.STATUT_PUBLIE, startDate, endDate);
            nbDocument += doQueryCountDosEligible(session, VocabularyConstants.STATUT_CLOS, startDate, endDate);
            nbDocument +=
                doQueryCountDosEligible(
                    session,
                    VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION,
                    startDate,
                    endDate
                );
        }
        return nbDocument;
    }

    @Override
    public void creerLotArchivage(final CoreSession session, String statut, Date startDate, Date endDate) {
        List<String> listIds = new ArrayList<>();

        if (StringUtils.isNotEmpty(statut)) {
            listIds = doQueryListIdDosEligible(session, statut, startDate, endDate);
        } else {
            listIds.addAll(doQueryListIdDosEligible(session, VocabularyConstants.STATUT_PUBLIE, startDate, endDate));
            listIds.addAll(doQueryListIdDosEligible(session, VocabularyConstants.STATUT_CLOS, startDate, endDate));
            listIds.addAll(
                doQueryListIdDosEligible(
                    session,
                    VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION,
                    startDate,
                    endDate
                )
            );
        }

        ExtractionLotService extractionLotService = SolonEpgAdamantServiceLocator.getExtractionLotService();
        extractionLotService.createAndAssignLotToDocumentsByIds(listIds);
    }

    @Override
    public Integer doQueryCountDosEligible(final CoreSession session, String statut, Date startDate, Date endDate) {
        String query = buildDosEligibleQuery(session, QUERY_SELECT_COUNT, statut, startDate, endDate);

        try (
            IterableQueryResult resCount = QueryUtils.doSqlQuery(
                session,
                new String[] { FlexibleQueryMaker.COL_COUNT },
                query,
                new Object[] {}
            )
        ) {
            return StreamSupport
                .stream(resCount.spliterator(), false)
                .map(row -> ((Long) row.get(FlexibleQueryMaker.COL_COUNT)).intValue())
                .findFirst()
                .orElse(0);
        }
    }

    @Override
    public List<String> doQueryListIdDosEligible(
        final CoreSession session,
        String statut,
        Date startDate,
        Date endDate
    ) {
        String query = buildDosEligibleQuery(session, QUERY_SELECT, statut, startDate, endDate);

        try (
            IterableQueryResult res = QueryUtils.doSqlQuery(
                session,
                new String[] { FlexibleQueryMaker.COL_ID },
                query,
                new Object[] {}
            )
        ) {
            return StreamSupport
                .stream(res.spliterator(), false)
                .map(row -> (String) row.get(FlexibleQueryMaker.COL_ID))
                .collect(Collectors.toList());
        }
    }

    private String buildDosEligibleQuery(
        CoreSession session,
        String selectQuery,
        String statut,
        Date startDate,
        Date endDate
    ) {
        int delaisEligibilite = SolonEpgAdamantServiceLocator
            .getDossierExtractionService()
            .getDelaisEligibilite(session);
        String typeActes = getTypeActeToString(session);
        String vecteurs = getVecteurPublicationToString(session);

        String formatStartDate = SolonDateConverter.DATE_DASH_REVERSE.format(
            removeMonthToDate(startDate, delaisEligibilite)
        );
        String formatEndDate = SolonDateConverter.DATE_DASH_REVERSE.format(
            removeMonthToDate(endDate, delaisEligibilite)
        );

        String query = "";
        if (VocabularyConstants.STATUT_PUBLIE.equals(statut)) {
            query = selectQuery + String.format(QUERY_PUBLIE, typeActes, vecteurs, formatStartDate, formatEndDate);
        } else if (VocabularyConstants.STATUT_CLOS.equals(statut)) {
            query = selectQuery + String.format(QUERY_CLOS, typeActes, vecteurs, formatStartDate, formatEndDate);
        } else if (VocabularyConstants.STATUT_TERMINE_SANS_PUBLICATION.equals(statut)) {
            query =
                selectQuery +
                String.format(
                    QUERY_TERMINE_SANS_PUBLICATION,
                    typeActes,
                    vecteurs,
                    formatStartDate,
                    formatEndDate,
                    formatStartDate,
                    formatEndDate
                );
        }

        return query;
    }

    /**
     * Ajouter un nombre de mois a une date
     * @param date
     * @param month
     * @return
     */
    protected Date removeMonthToDate(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month * -1);
        return calendar.getTime();
    }

    /**
     * Converti un ensemble de nom de type d'acte en chaîne de caractère de leur id
     * @param session
     * @return chaîne de caractère de id de type d'acte
     */
    protected String getTypeActeToString(CoreSession session) {
        return SolonEpgAdamantServiceLocator
            .getDossierExtractionService()
            .getTypeActeEligibilite(session)
            .stream()
            .flatMap(
                s -> getTypeActeService().getEntries().stream().filter(e -> e.getValue().equals(s)).map(Pair::getKey)
            )
            .map(s -> "'" + s + "'")
            .collect(Collectors.joining(","));
    }

    /**
     * Converti un ensemble de nom de vecteurs de publication en chaîne de caractère de leur id
     * @param session
     * @return chaîne de caractère de id de vecteurs de publication
     */
    protected String getVecteurPublicationToString(CoreSession session) {
        return SolonEpgAdamantServiceLocator
            .getDossierExtractionService()
            .getVecteurPublicationEligibilite(session)
            .stream()
            .collect(Collectors.joining("','", "'", "'"));
    }
}
