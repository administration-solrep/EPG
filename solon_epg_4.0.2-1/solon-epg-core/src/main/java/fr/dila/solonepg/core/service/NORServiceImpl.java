package fr.dila.solonepg.core.service;

import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_SCHEMA;
import static fr.dila.st.api.constant.STSchemaConstant.DUBLINCORE_SCHEMA;

import com.google.common.collect.Lists;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.CoreSessionUtil;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.UnrestrictedGetDocumentRunner;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import fr.sword.naiad.nuxeo.commons.core.util.SessionUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.persistence.Query;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.persistence.PersistenceProvider;
import org.nuxeo.ecm.core.persistence.PersistenceProviderFactory;
import org.nuxeo.ecm.core.uidgen.UIDGeneratorService;
import org.nuxeo.ecm.core.uidgen.UIDSequencer;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

public class NORServiceImpl extends DefaultComponent implements NORService {
    private static final String WARNING_MSG_DUPLICATE_NOR = "Attention : au moins 2 dossiers ont le même NOR !";

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -427109452126538120L;

    private static final String SOLON_EPG_NOR_SEQUENCER = "SOLON_EPG_NOR_SEQUENCER_";

    private static final Log log = LogFactory.getLog(NORServiceImpl.class);

    private static final STLogger LOGGER = STLogFactory.getLog(NORServiceImpl.class);

    private static final String FIRST_LETTER_RECTIFICATIF = "Z";

    private static final String SECOND_LETTER_RECTIFICATIF = "F";

    private static final String THIRD_LETTER_RECTIFICATIF = "H";

    private static PersistenceProvider persistenceProvider;

    protected PersistenceProvider getOrCreatePersistenceProvider() {
        if (persistenceProvider == null) {
            synchronized (NORServiceImpl.class) {
                if (persistenceProvider == null) {
                    activatePersistenceProvider();
                }
            }
        }
        return persistenceProvider;
    }

    protected static void activatePersistenceProvider() {
        Thread thread = Thread.currentThread();
        ClassLoader last = thread.getContextClassLoader();
        try {
            thread.setContextClassLoader(PersistenceProvider.class.getClassLoader());
            PersistenceProviderFactory persistenceProviderFactory = ServiceUtil.getRequiredService(
                PersistenceProviderFactory.class
            );
            persistenceProvider = persistenceProviderFactory.newProvider("NXUIDSequencer");
            persistenceProvider.openPersistenceUnit();
        } finally {
            thread.setContextClassLoader(last);
        }
    }

    @Override
    public void deactivate(final ComponentContext context) {
        if (persistenceProvider != null) {
            synchronized (NORServiceImpl.class) {
                if (persistenceProvider != null) {
                    persistenceProvider.closePersistenceUnit();
                    persistenceProvider = null;
                }
            }
        }
    }

    @Override
    public String createNOR(final String norActe, final String norMinistere, final String norDirection) {
        // Vérification des arguments
        if (StringUtils.isBlank(norActe) || norActe.length() != 1) {
            throw new IllegalArgumentException("Lettres de NOR acte invalides");
        }
        checkNorMinistere(norMinistere);
        checkNorDirection(norDirection);

        // Année
        final String annee = Year.now().format(DateTimeFormatter.ofPattern("yy"));

        // Compteur
        String compteur;
        String nuxeoTemplates = Framework.getProperty("nuxeo.templates", "");
        // Si le template oracle n'est pas activé, on utilise le sequenceur nuxeo (utile
        // pour H2)
        if (nuxeoTemplates.contains("oracle")) {
            compteur = String.format("%05d", getNumeroChronoNor(annee));
        } else {
            final UIDGeneratorService uidGeneratorService = ServiceUtil.getRequiredService(UIDGeneratorService.class);
            final UIDSequencer sequencer = uidGeneratorService.getSequencer();
            compteur = String.format("%05d", sequencer.getNextLong(SOLON_EPG_NOR_SEQUENCER + annee) - 1);
        }

        return norMinistere + norDirection + annee + compteur + norActe;
    }

    /**
     * On va rechercher le numero chrono dans le sequenceur oracle si le séquenceur
     * n'existe pas (nouvelle année par exemple), on appelle la procédure stockée de
     * création d'un nouveau séquenceur
     *
     * @param annee
     * @return un numero chrono unique
     */
    private int getNumeroChronoNor(final String annee) {
        final int[] nextVal = new int[1];
        nextVal[0] = -1;
        getOrCreatePersistenceProvider()
            .run(
                true,
                entityManager -> {
                    // génération du nom de séquence à aller chercher en fonction de l'année
                    String sequenceName = SOLON_EPG_NOR_SEQUENCER + annee;

                    // On vérifie que la séquence existe

                    final String checkSequence = String.format(
                        "SELECT count(1) FROM user_sequences WHERE sequence_name = '%s'",
                        sequenceName
                    );

                    final Query checkSequenceQuery = entityManager.createNativeQuery(checkSequence);
                    int count = ((Number) checkSequenceQuery.getSingleResult()).intValue();
                    // Si elle n'existe pas, on la créée
                    if (count == 0) {
                        // Si la sequence n'existe pas, on génère une nouvelle séquence
                        try (CloseableCoreSession session = SessionUtil.openSession()) {
                            // Appel de la procédure stockée CREATE_NEW_SEQUENCE qui prend en paramètre le
                            // nom de la
                            // nouvelle séquence
                            String createSequence = String.format("CREATE_NEW_SEQUENCE('%s')", sequenceName);
                            QueryUtils.execSqlFunction(session, createSequence, null);
                        }
                    }
                    final String selectNextVal = String.format("SELECT %s.nextval from dual", sequenceName);
                    // récupération de la prochaine valeur
                    final Query nextValQuery = entityManager.createNativeQuery(selectNextVal);
                    @SuppressWarnings("unchecked")
                    final List<Number> resultList = nextValQuery.getResultList();
                    if (CollectionUtils.isNotEmpty(resultList)) {
                        nextVal[0] = resultList.get(0).intValue();
                    }
                }
            );
        if (nextVal[0] == -1) {
            throw new EPGException("Impossible de générer le numéro chrono du NOR");
        }
        return nextVal[0];
    }

    @Override
    public String createRectificatifNOR(final Dossier dossierRectifie) {
        final String ancienNor = dossierRectifie.getNumeroNor();
        final StringBuilder nouveauNor = new StringBuilder(ancienNor.substring(0, 11));
        final Long nbDossierRectifie = dossierRectifie.getNbDossierRectifie();
        // on ne peut pas créér plus de 3 rectificatif !
        if (nbDossierRectifie > 2L) {
            throw new IllegalArgumentException(
                "Plus de 3 rectifications sur le même dossier ! Nouvelle rectification interdite !"
            );
        }
        // on ajoute 1 au nb de dossier rectifie pour prendre en compte le rectificatif
        // que l'on est en train de creer
        nouveauNor.append(getLettreFromNbRectificatif(nbDossierRectifie + 1));
        return nouveauNor.toString();
    }

    @Override
    public String getLettreFromNbRectificatif(final Long nbRectificatif) {
        if (nbRectificatif == 1L) {
            return FIRST_LETTER_RECTIFICATIF;
        } else if (nbRectificatif == 2L) {
            return SECOND_LETTER_RECTIFICATIF;
        } else if (nbRectificatif == 3L) {
            return THIRD_LETTER_RECTIFICATIF;
        }
        return null;
    }

    /**
     * Méthode permettant de retrouver un dossier ou un objet associé à partir du
     * NOR. Cette méthode applique la requête NXQL "SELECT d.ecm:uuid FROM Dossier
     * as d WHERE d.dos:numeroNor=?"
     *
     * @param dossierFetcher Function qui, à partir de la requête NXQL, renvoie le
     *                       dossier sous sa forme souhaitée
     */
    private <T> T getDossierWithNor(final CoreSession session, final String nor, Function<String, T> dossierFetcher) {
        if (StringUtils.isBlank(nor)) {
            return null;
        }
        CoreSessionUtil.assertSessionNotNull(session);

        final String query = String.format(
            " SELECT d.ecm:uuid as id FROM %s as d WHERE d.%s:%s = ?",
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX,
            DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY
        );

        return dossierFetcher.apply(query);
    }

    @Override
    public DocumentModel getDossierFromNOR(final CoreSession session, final String nor, String... schemas) {
        List<String> schemasList = Lists.newArrayList(ObjectHelper.requireNonNullElseGet(schemas, () -> new String[0]));
        schemasList.add(DOSSIER_SCHEMA);
        schemasList.add(DUBLINCORE_SCHEMA);

        return Optional
            .ofNullable(getDossierIdFromNOR(session, nor))
            .map(id -> new UnrestrictedGetDocumentRunner(session, schemasList.toArray(new String[0])).getById(id))
            .orElse(null);
    }

    @Override
    public String getDossierIdFromNOR(final CoreSession session, final String nor) {
        return getDossierWithNor(
            session,
            nor,
            query -> {
                List<String> ids = QueryUtils.doUFNXQLQueryForIdsList(session, query, new Object[] { nor }, 2, 0);
                if (ids.size() > 1) {
                    LOGGER.warn(STLogEnumImpl.WARNING_TEC, WARNING_MSG_DUPLICATE_NOR);
                }
                return ids.stream().findFirst().orElse(null);
            }
        );
    }

    @Override
    public Optional<String> getNorFromDossierId(final CoreSession session, final String dossierId) {
        String queryNor = "Select d.dos:numeroNor as nor From Dossier as d where d.ecm:uuid = ?";
        List<String> nors = QueryUtils.doUFNXQLQueryAndMapping(
            session,
            queryNor,
            new Object[] { dossierId },
            (Map<String, Serializable> rowData) -> (String) rowData.get("nor")
        );
        return nors.isEmpty() ? Optional.empty() : Optional.of(nors.get(0));
    }

    @Override
    public DocumentModel getDossierFromNORWithACL(final CoreSession session, final String nor) {
        if (StringUtils.isBlank(nor)) {
            return null;
        }
        CoreSessionUtil.assertSessionNotNull(session);

        final String query = String.format(
            "SELECT d.ecm:uuid as id FROM %s as d WHERE d.%s:%s = ? ",
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX,
            DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY
        );

        final List<DocumentModel> listDoc = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            query,
            new Object[] { nor },
            1,
            0
        );
        if (!listDoc.isEmpty()) {
            return listDoc.get(0);
        }
        return null;
    }

    @Override
    public Dossier getRectificatifFromNORAndNumRect(
        final CoreSession session,
        final String nor,
        final Long nbRectificatif
    ) {
        if (nor == null || nor.trim().length() == 0) {
            return null;
        }

        if (nbRectificatif > 3L) {
            throw new IllegalArgumentException("Un dossier ne peut pas avoir plus de 3 rectificatifs");
        }

        // récupération du nor du rectificatif à partir du nor du dossier
        final StringBuilder norRectificatif = new StringBuilder(nor.substring(0, 11));
        norRectificatif.append(getLettreFromNbRectificatif(nbRectificatif));

        // récupération du rectificatif
        return findDossierFromNOR(session, norRectificatif.toString());
    }

    @Override
    public Dossier findDossierFromNOR(final CoreSession session, final String nor, String... schemas) {
        final DocumentModel doc = getDossierFromNOR(session, nor, schemas);
        if (doc != null) {
            return doc.getAdapter(Dossier.class);
        }
        return null;
    }

    @Override
    public Dossier findDossierFromNORWithACL(CoreSession session, String nor) {
        final DocumentModel doc = getDossierFromNORWithACL(session, nor);
        if (doc != null) {
            return doc.getAdapter(Dossier.class);
        }
        return null;
    }

    protected void checkNorMinistere(final String norMinistere) {
        if (norMinistere == null || norMinistere.length() != 3) {
            throw new NuxeoException("Lettres de NOR ministère invalides : " + norMinistere);
        }
    }

    protected void checkNorDirection(final String norDirection) {
        if (norDirection == null || norDirection.length() != 1) {
            throw new NuxeoException("Lettres de NOR direction invalides : " + norDirection);
        }
    }

    @Override
    public String reattributionMinistereNOR(final Dossier dossierReattribue, final String norMinistere) {
        checkNorMinistere(norMinistere);
        String nouveauNor = dossierReattribue.getNumeroNor();
        nouveauNor = norMinistere + nouveauNor.substring(3);
        return nouveauNor;
    }

    @Override
    public String reattributionDirectionNOR(
        final Dossier dossierReattribue,
        final String norMinistere,
        final String norDirection
    ) {
        checkNorMinistere(norMinistere);
        checkNorDirection(norDirection);
        String nouveauNor = dossierReattribue.getNumeroNor();
        nouveauNor = norMinistere + norDirection + nouveauNor.substring(4);
        return nouveauNor;
    }

    @Override
    public List<String> findDossierIdsFromNOR(final CoreSession coreSession, final String nor) {
        final String query = String.format(
            "SELECT d.ecm:uuid as id FROM %1$s as d WHERE d.%2$s:%3$s LIKE ? AND d.%2$s:%4$s = ? AND testDossierAcl(d.ecm:uuid) = 1 ",
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX,
            DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY,
            DossierSolonEpgConstants.ARCHIVE_DOSSIER_PROPERTY
        );

        return QueryUtils.doUFNXQLQueryForIdsList(coreSession, query, new Object[] { nor, 0 });
    }

    @Override
    public Set<String> retrieveAvailableNorList(final CoreSession session) {
        final Set<String> availableNor = new HashSet<>();

        final String query = "select dos.numeroNor from dossier_solon_epg dos";

        try (
            IterableQueryResult res = QueryUtils.doSqlQuery(
                session,
                new String[] { "dos:numeroNor" },
                query,
                new Object[] {}
            )
        ) {
            final Iterator<Map<String, Serializable>> iterator = res.iterator();
            while (iterator.hasNext()) {
                final Map<String, Serializable> row = iterator.next();
                final String numeroNor = (String) row.get("dos:numeroNor");
                availableNor.add(numeroNor);
            }
        } catch (final NuxeoException e) {
            log.error("Erreur lors de la récupération des numéros des nors", e);
        }
        return availableNor;
    }

    @Override
    public boolean checkNorUnicity(final CoreSession session, String nor) {
        boolean exist = false;

        final String query = "select dos.id from dossier_solon_epg dos where dos.numeroNor = ?";

        try (
            IterableQueryResult res = QueryUtils.doSqlQuery(
                session,
                new String[] { FlexibleQueryMaker.COL_ID },
                query,
                new Object[] { nor }
            )
        ) {
            final Iterator<Map<String, Serializable>> iterator = res.iterator();
            while (iterator.hasNext()) {
                final Map<String, Serializable> row = iterator.next();
                final String docid = (String) row.get(FlexibleQueryMaker.COL_ID);
                log.warn(String.format("NOR[%s] used by Doc[%s]", nor, docid));
                exist = true;
            }
        } catch (final NuxeoException e) {
            log.error("Erreur lors de la récupération des numéros des nors", e);
        }
        return exist;
    }

    private List<String> getDossierIdsFromISA(final CoreSession coreSession, final String isa) {
        final String query = String.format(
            " SELECT d.ecm:uuid as id FROM %s as d WHERE d.%s:%s = ? ",
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA_PREFIX,
            ConseilEtatConstants.CONSEIL_ETAT_NUMERO_ISA_PROPERTY
        );

        return QueryUtils.doUFNXQLQueryForIdsList(coreSession, query, new Object[] { isa });
    }

    /**
     * Récupération du dossier via son numéro ISA
     */
    @Override
    public DocumentModel getDossierFromISA(final CoreSession session, final String isa, String... schemas) {
        List<String> schemasList = Lists.newArrayList(ObjectHelper.requireNonNullElseGet(schemas, () -> new String[0]));
        schemasList.add(DOSSIER_SCHEMA);
        schemasList.add(DUBLINCORE_SCHEMA);

        final List<String> listDoc = getDossierIdsFromISA(session, isa);

        if (listDoc.size() > 1) {
            throw new NuxeoException("Plusieurs dossiers correspondent à ce numéro isa");
        }
        if (!listDoc.isEmpty() && listDoc.size() < 2) {
            return new UnrestrictedGetDocumentRunner(session, schemasList.toArray(new String[0]))
            .getById(listDoc.get(0));
        }

        return null;
    }

    @Override
    public boolean isStructNorValide(String nor) {
        String regex = "^[a-zA-Z]{4}[0-9]{7}[a-zA-Z]{1}$";
        return nor.matches(regex);
    }

    @Override
    public boolean isStructNumeroISAValid(String numeroISA) {
        String regex = "^[0-9a-zA-Z_\\-]+$";
        return numeroISA.matches(regex);
    }
}
