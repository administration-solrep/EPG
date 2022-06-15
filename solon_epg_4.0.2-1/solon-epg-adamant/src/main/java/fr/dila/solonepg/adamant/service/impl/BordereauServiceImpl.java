package fr.dila.solonepg.adamant.service.impl;

import static fr.dila.solonepg.api.constant.VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ARCHIVE;
import static fr.dila.solonepg.api.constant.VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ERREUR_ARCHIVAGE;
import static fr.dila.solonepg.api.constant.VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ERREUR_LIVRAISON;
import static fr.dila.solonepg.api.constant.VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_LIVRE;
import static fr.dila.solonepg.api.constant.VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME;

import fr.dila.solonepg.adamant.SolonEpgAdamantConstant;
import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.adamant.model.BordereauPK;
import fr.dila.solonepg.adamant.model.DossierExtractionBordereau;
import fr.dila.solonepg.adamant.model.DossierExtractionLot;
import fr.dila.solonepg.adamant.service.BordereauService;
import fr.dila.solonepg.adamant.service.DossierExtractionService;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.core.dto.DossierArchivageStatDTO;
import fr.dila.solonepg.core.dto.DossierArchivageStatDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.service.AbstractPersistenceDefaultComponent;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SHA512Util;
import fr.dila.st.core.util.SolonDateConverter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Query;
import org.apache.commons.io.FileUtils;
import org.nuxeo.ecm.core.api.CoreSession;

public class BordereauServiceImpl extends AbstractPersistenceDefaultComponent implements BordereauService {
    private static final String PARAM_STATUT_AFTER = "statutAfter";
    private static final String PARAM_DATE_DEBUT = "dateDebut";
    private static final String PARAM_DATE_FIN = "dateFin";
    private static final String PARAM_OFFSET = "offset";
    private static final String PARAM_LIMIT = "limit";

    /**
     * Séparateur de cellule du fichier CSV
     */
    private static final String CSV_CELL_SEP = ",";

    /**
     * Séparateur de ligne du fichier CSV
     */
    private static final String CSV_LINE_SEP = "\r\n";

    private static final String PAGINATE_QUERY =
        "SELECT outer.* FROM (SELECT ROWNUM rn, inner.* FROM ( %s " +
        ") inner) outer WHERE outer.rn > :" +
        PARAM_OFFSET +
        " AND outer.rn <= :" +
        PARAM_LIMIT;

    private static final String WHERE = " WHERE ";
    private static final String AND = " AND ";
    private static final String STATUT_ARCHIVAGE_FILTER = "b.STATUT_ARCHIVAGE_AFTER = :" + PARAM_STATUT_AFTER;
    private static final String EXTRACTION_DATE = "b.EXTRACTION_DATE";
    private static final String DATE_LIVRAISON = "b.DATE_LIVRAISON";
    private static final String DATE_VERSEMENT = "b.DATE_VERSEMENT";
    private static final String DATE_FORMAT = "YYYY-MM-DD HH24:MI:SS";
    private static final String ORDER_BY = " ORDER BY %s";

    private static final String QUERY_STAT_STATUT_EXTRACTION =
        "SELECT TO_CHAR(d.ID) as id, TO_CHAR(d.NUMERONOR) as nor, " +
        "TO_CHAR(d.TITREACTE) as titreActe, TO_CHAR(b.STATUT_ARCHIVAGE_AFTER) as statutPeriode, " +
        "b.EXTRACTION_DATE as dateExt, TO_CHAR(d.STATUTARCHIVAGE) as statut, TO_CHAR(b.MESSAGE) as erreur " +
        "FROM DOSSIER_SOLON_EPG d INNER JOIN DOSSIER_EXTRACTION_BORDEREAU b ON d.id=b.ID_DOSSIER" +
        WHERE +
        STATUT_ARCHIVAGE_FILTER +
        AND +
        addFilterDateToQuery(EXTRACTION_DATE) +
        ORDER_BY;

    private static final String QUERY_COUNT_STAT_STATUT_EXTRACTION =
        "SELECT COUNT(*) FROM DOSSIER_SOLON_EPG d INNER JOIN DOSSIER_EXTRACTION_BORDEREAU b ON d.id=b.ID_DOSSIER" +
        WHERE +
        STATUT_ARCHIVAGE_FILTER +
        AND +
        addFilterDateToQuery(EXTRACTION_DATE);

    private static final String QUERY_STAT_STATUT_LIVRAISON =
        "SELECT TO_CHAR(d.ID) as id, TO_CHAR(b.NUMERONOR) as nor, TO_CHAR(d.TITREACTE) as titreActe, TO_CHAR(b.STATUT_ARCHIVAGE_AFTER) as statutPeriode, " +
        "b.DATE_LIVRAISON as dateExt, TO_CHAR(d.STATUTARCHIVAGE) as statut FROM DOSSIER_SOLON_EPG d INNER JOIN RETOUR_VITAM_BORDEREAU_LIVRAISON b " +
        "ON d.NUMERONOR = b.NUMERONOR" +
        WHERE +
        STATUT_ARCHIVAGE_FILTER +
        AND +
        addFilterDateToQuery(DATE_LIVRAISON) +
        ORDER_BY;

    private static final String QUERY_COUNT_STAT_STATUT_LIVRAISON =
        "SELECT COUNT(*) FROM DOSSIER_SOLON_EPG d INNER JOIN RETOUR_VITAM_BORDEREAU_LIVRAISON b ON d.NUMERONOR = b.NUMERONOR" +
        WHERE +
        STATUT_ARCHIVAGE_FILTER +
        AND +
        addFilterDateToQuery(DATE_LIVRAISON);

    private static final String QUERY_STAT_STATUT_VERSEMENT =
        "SELECT TO_CHAR(d.ID) as id, TO_CHAR(b.NUMERONOR) as nor, TO_CHAR(d.TITREACTE) as titreActe, TO_CHAR(b.STATUT_ARCHIVAGE_AFTER) as statutPeriode, " +
        "b.DATE_VERSEMENT as dateExt, TO_CHAR(d.STATUTARCHIVAGE) as statut, TO_CHAR(b.MESSAGE_REJET) as erreur FROM DOSSIER_SOLON_EPG d " +
        "INNER JOIN RETOUR_VITAM_RAPPORT_VERSEMENT b ON d.NUMERONOR = b.NUMERONOR" +
        WHERE +
        STATUT_ARCHIVAGE_FILTER +
        AND +
        addFilterDateToQuery(DATE_VERSEMENT) +
        ORDER_BY;

    private static final String QUERY_COUNT_STAT_STATUT_VERSEMENT =
        "SELECT COUNT(*) FROM DOSSIER_SOLON_EPG d INNER JOIN RETOUR_VITAM_RAPPORT_VERSEMENT b ON d.NUMERONOR = b.NUMERONOR" +
        WHERE +
        STATUT_ARCHIVAGE_FILTER +
        AND +
        addFilterDateToQuery(DATE_VERSEMENT);

    private static String addFilterDateToQuery(String columnName) {
        return (
            columnName +
            " BETWEEN TO_DATE(:" +
            PARAM_DATE_DEBUT +
            ",'" +
            DATE_FORMAT +
            "') AND TO_DATE(:" +
            PARAM_DATE_FIN +
            ",'" +
            DATE_FORMAT +
            "')"
        );
    }

    @Override
    public DossierExtractionBordereau initBordereau(Dossier dossier, DossierExtractionLot lot) {
        BordereauPK bordereauPK = new BordereauPK(dossier.getDocument().getId(), lot.getId());
        DossierExtractionBordereau bordereau = new DossierExtractionBordereau();
        bordereau.setBordereauPK(bordereauPK);

        bordereau.setTypeActe(SolonEpgServiceLocator.getActeService().getActe(dossier.getTypeActe()).getLabel());
        bordereau.setStatut(
            STServiceLocator.getVocabularyService().getEntryLabel(STATUT_DOSSIER_DIRECTORY_NAME, dossier.getStatut())
        );
        bordereau.setVersion(SolonEpgServiceLocator.getDossierService().isDossierReprise(dossier) ? "V1" : "V2");

        bordereau.setExtractionDate(new Date());
        bordereau.setIdPaquet(lot.getId().toString());

        return bordereau;
    }

    @Override
    public void completeBordereau(DossierExtractionBordereau bordereau, File sipFile, String idPaquet)
        throws IOException {
        bordereau.setPoids(sipFile.length());
        bordereau.setEmpreinte(SHA512Util.getSHA512Hash(sipFile));
        bordereau.setIdPaquet(idPaquet);
    }

    @Override
    public void saveBordereau(DossierExtractionBordereau bordereau) {
        SolonEpgAdamantServiceLocator.getDossierExtractionDao().saveBordereau(bordereau);
    }

    @Override
    public void generateBordereauFile(CoreSession session, DossierExtractionLot lot, File folder) throws IOException {
        // Récupération des informations bordereau de chaque dossier du lot.
        Collection<DossierExtractionBordereau> bordereaux = SolonEpgAdamantServiceLocator
            .getDossierExtractionDao()
            .getBordereaux(lot);

        StringBuilder builder = new StringBuilder(generateBordereauFileHeaders());
        bordereaux.forEach(bordereau -> builder.append(generateBordereauFileLine(bordereau)));

        File bordereauFile = new File(
            folder.getAbsolutePath() + SolonEpgAdamantConstant.FILE_SEPARATOR + computeBordereauFilename(session)
        );
        FileUtils.writeStringToFile(bordereauFile, builder.toString(), StandardCharsets.UTF_8);
    }

    /**
     * Génère le nom du fichier bordereau à produire selon le format suivant :
     * numéro d’entrée SOLON (fourni par les Archives nationales), suivi d’un
     * underscore et de la date d’extraction au format AAAA-MM-JJ-hh-mm, suivi
     * d’un underscore et du numéro du lot d’extraction Exemple :
     * 20190082_2018-04-17-12-30_123.csv
     *
     * @param lot
     * @return
     */
    private String computeBordereauFilename(CoreSession session) {
        final DossierExtractionService dossierExtractionService = SolonEpgAdamantServiceLocator.getDossierExtractionService();

        return (
            "BG_" +
            dossierExtractionService.getNumeroSolon(session) +
            "_" +
            SolonDateConverter.DATETIME_DASH_REVERSE_SECOND_DASH.formatNow() +
            ".csv"
        );
    }

    /**
     * Génération des headers du fichier.
     */
    private String generateBordereauFileHeaders() {
        return Stream
            .of("Nom", "Numéro de paquet", "Empreinte", "Poids", "Type Acte", "Statut", "Version")
            .collect(Collectors.joining(CSV_CELL_SEP, "", CSV_LINE_SEP));
    }

    /**
     * Génération d'une ligne du fichier bordereau correspondant à un lot.
     *
     * @param bordereau
     *            un bordereau persisté
     */
    private String generateBordereauFileLine(DossierExtractionBordereau bordereau) {
        return Stream
            .of(
                bordereau.getNomSip(),
                bordereau.getIdPaquet(),
                bordereau.getEmpreinte(),
                String.valueOf(bordereau.getPoids()),
                bordereau.getTypeActe(),
                bordereau.getStatut(),
                bordereau.getVersion()
            )
            .collect(Collectors.joining(CSV_CELL_SEP, "", CSV_LINE_SEP));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DossierArchivageStatDTO> getDossierArchivageStatResultList(
        CoreSession session,
        final String statutAfter,
        final String dateDebut,
        final String dateFin,
        final Long offset,
        final Long nbDos,
        final String sortInfo
    ) {
        final Long limit = offset + nbDos;
        String query = getQuery(statutAfter, true, false, sortInfo);
        List<DossierArchivageStatDTO> result = new ArrayList<>();
        accept(
            true,
            entityManager -> {
                Query nativeQuery = entityManager.createNativeQuery(query);
                nativeQuery.setParameter(PARAM_STATUT_AFTER, statutAfter);
                nativeQuery.setParameter(PARAM_DATE_DEBUT, dateDebut);
                nativeQuery.setParameter(PARAM_DATE_FIN, dateFin);
                nativeQuery.setParameter(PARAM_OFFSET, offset);
                nativeQuery.setParameter(PARAM_LIMIT, limit);
                for (Object[] dosObj : (List<Object[]>) nativeQuery.getResultList()) {
                    DossierArchivageStatDTO dto = new DossierArchivageStatDTOImpl(
                        (String) dosObj[1],
                        (String) dosObj[2],
                        (String) dosObj[3],
                        (String) dosObj[4],
                        (Date) dosObj[5],
                        (String) dosObj[6],
                        dosObj.length > 7 ? (String) dosObj[7] : ""
                    );
                    result.add(dto);
                }
            }
        );
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DossierArchivageStatDTO> getDossierArchivageStatFullResultList(
        CoreSession session,
        final String statutAfter,
        final String dateDebut,
        final String dateFin,
        final String sortInfo
    ) {
        String query = getQuery(statutAfter, false, false, sortInfo);

        List<DossierArchivageStatDTO> result = new ArrayList<>();
        accept(
            true,
            entityManager -> {
                Query nativeQuery = entityManager.createNativeQuery(query);
                nativeQuery.setParameter(PARAM_STATUT_AFTER, statutAfter);
                nativeQuery.setParameter(PARAM_DATE_DEBUT, dateDebut);
                nativeQuery.setParameter(PARAM_DATE_FIN, dateFin);
                for (Object[] dosObj : (List<Object[]>) nativeQuery.getResultList()) {
                    DossierArchivageStatDTO dto = new DossierArchivageStatDTOImpl(
                        (String) dosObj[0],
                        (String) dosObj[1],
                        (String) dosObj[2],
                        (String) dosObj[3],
                        (Date) dosObj[4],
                        (String) dosObj[5],
                        dosObj.length > 6 ? (String) dosObj[6] : ""
                    );
                    result.add(dto);
                }
            }
        );
        return result;
    }

    @Override
    public Integer getCountDossierArchivageStatResult(
        final String statutAfter,
        final String dateDebut,
        final String dateFin
    ) {
        String query = getQuery(statutAfter, false, true, null);
        return apply(
            true,
            entityManager -> {
                Query nativeQuery = entityManager.createNativeQuery(query);
                nativeQuery.setParameter(PARAM_STATUT_AFTER, statutAfter);
                nativeQuery.setParameter(PARAM_DATE_DEBUT, dateDebut);
                nativeQuery.setParameter(PARAM_DATE_FIN, dateFin);
                Object result = nativeQuery.getSingleResult();
                return ((BigDecimal) result).intValue();
            }
        );
    }

    private String getQuery(String statutAfter, boolean isPaginated, boolean isCount, String sortInfo) {
        String query;
        if (
            STATUT_ARCHIVAGE_DOSSIER_LIVRE.equals(statutAfter) ||
            STATUT_ARCHIVAGE_DOSSIER_ERREUR_LIVRAISON.equals(statutAfter)
        ) {
            query = isCount ? QUERY_COUNT_STAT_STATUT_LIVRAISON : QUERY_STAT_STATUT_LIVRAISON;
        } else if (
            STATUT_ARCHIVAGE_DOSSIER_ARCHIVE.equals(statutAfter) ||
            STATUT_ARCHIVAGE_DOSSIER_ERREUR_ARCHIVAGE.equals(statutAfter)
        ) {
            query = isCount ? QUERY_COUNT_STAT_STATUT_VERSEMENT : QUERY_STAT_STATUT_VERSEMENT;
        } else {
            query = isCount ? QUERY_COUNT_STAT_STATUT_EXTRACTION : QUERY_STAT_STATUT_EXTRACTION;
        }
        if (!isCount) {
            query = String.format(query, sortInfo);
        }
        return isPaginated ? String.format(PAGINATE_QUERY, query) : query;
    }
}
