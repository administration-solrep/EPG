package fr.dila.solonepg.elastic.batch;

import com.google.gson.Gson;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.enumeration.QueryType;
import fr.dila.solonepg.api.requeteur.EpgRequeteExperte;
import fr.dila.solonepg.core.export.dto.ExportDossierDTO;
import fr.dila.solonepg.core.util.EpgExcelUtil;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.search.RechercheLibre;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaExp;
import fr.dila.solonepg.elastic.services.RequeteurService;
import fr.dila.ss.core.service.AbstractAlertService;
import fr.dila.st.api.alert.Alert;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.CoreSessionUtil;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.activation.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.IdRef;

/**
 * @author arolin
 */
public class EpgAlertServiceImpl
    extends AbstractAlertService<EpgRequeteExperte, ExportDossierDTO>
    implements EpgAlertService {
    private static final long serialVersionUID = -7549168607732149204L;

    private static final STLogger LOGGER = STLogFactory.getLog(EpgAlertServiceImpl.class);

    @Override
    protected EpgRequeteExperte getRequeteExperte(CoreSession session, Alert alert) {
        return session.getDocument(new IdRef(alert.getRequeteId())).getAdapter(EpgRequeteExperte.class);
    }

    @Override
    protected List<String> getRecipients(Alert alert) {
        List<String> recipients = super.getRecipients(alert);
        recipients.addAll(alert.getExternalRecipients());
        return recipients;
    }

    @Override
    protected long countResultsFromRequete(CoreSession session, EpgRequeteExperte requeteExperte, String username)
        throws IOException, URISyntaxException {
        long results = 0;
        String whereClause = requeteExperte.getWhereClause();
        if (StringUtils.isBlank(whereClause)) {
            LOGGER.info(
                session,
                STLogEnumImpl.LOG_INFO_TEC,
                "La requête ayant pour id [" + requeteExperte.getDocument().getId() + "] est vide"
            );
        } else {
            // pour avoir les mêmes résultats lors de l'exécution de la requête, il faut créer une session avec le
            // créateur de l'alerte et ne pas utiliser la session en cours car c'est l'utilisateur administrateur qui
            // lance le batch
            try (CloseableCoreSession requeteUserSession = CoreSessionUtil.openSession(session, username)) {
                Gson gson = new Gson();
                RequeteurService requeteurService = ServiceUtil.getRequiredService(RequeteurService.class);

                if (requeteExperte.getQueryType() == QueryType.ES_LIBRE) {
                    RechercheLibre rechercheLibre = gson.fromJson(whereClause, RechercheLibre.class);
                    rechercheLibre.setPageSize(0);
                    results = requeteurService.getCountResults(rechercheLibre, requeteUserSession);
                } else if (requeteExperte.getQueryType() == QueryType.ES_EXPERTE) {
                    SearchCriteriaExp searchCriteriaExp = gson.fromJson(whereClause, SearchCriteriaExp.class);
                    searchCriteriaExp.setPageSize(0);
                    results = requeteurService.getCountResults(searchCriteriaExp, requeteUserSession);
                }
            }
        }

        return results;
    }

    @Override
    protected String getDefaultEmailSubject(CoreSession session, STParametreService paramService) {
        return paramService.getParametreValue(session, SolonEpgParametreConstant.OBJET_MAIL_ALERT);
    }

    @Override
    protected String getDefaultEmailContent(CoreSession session, STParametreService paramService, long nbResults) {
        return paramService.getParametreValue(session, SolonEpgParametreConstant.TEXTE_MAIL_ALERT);
    }

    @Override
    protected List<ExportDossierDTO> getDossiersFromRequete(
        CoreSession session,
        EpgRequeteExperte requeteExperte,
        String username,
        long nombreDossiers
    )
        throws IOException, URISyntaxException {
        List<ExportDossierDTO> dossiers = Collections.emptyList();
        String whereClause = requeteExperte.getWhereClause();
        if (StringUtils.isBlank(whereClause)) {
            LOGGER.info(
                session,
                STLogEnumImpl.LOG_INFO_TEC,
                "La requête ayant pour id [" + requeteExperte.getDocument().getId() + "] est vide"
            );
        } else {
            Collection<ElasticDossier> results = Collections.emptyList();

            // pour avoir les mêmes résultats lors de l'exécution de la requête, il faut créer une session avec le
            // créateur de l'alerte et ne pas utiliser la session en cours car c'est l'utilisateur administrateur qui
            // lance le batch
            try (CloseableCoreSession requeteUserSession = CoreSessionUtil.openSession(session, username)) {
                Gson gson = new Gson();
                RequeteurService requeteurService = ServiceUtil.getRequiredService(RequeteurService.class);

                if (requeteExperte.getQueryType() == QueryType.ES_LIBRE) {
                    RechercheLibre rechercheLibre = gson.fromJson(whereClause, RechercheLibre.class);
                    rechercheLibre.setPageSize((int) nombreDossiers);
                    rechercheLibre.setColonnes(
                        Arrays.asList(
                            ElasticDossier.DOS_NUMERO_NOR,
                            ElasticDossier.DOS_TITRE_ACTE,
                            ElasticDossier.DOS_MINISTERE_RESP,
                            ElasticDossier.DOS_MINISTERE_ATTACHE,
                            ElasticDossier.DOS_PRENOM_RESP_DOSSIER,
                            ElasticDossier.DOS_NOM_RESP_DOSSIER
                        )
                    );
                    results =
                        requeteurService.getResultsWithoutHighlight(rechercheLibre, requeteUserSession).getResults();
                } else if (requeteExperte.getQueryType() == QueryType.ES_EXPERTE) {
                    SearchCriteriaExp searchCriteriaExp = gson.fromJson(whereClause, SearchCriteriaExp.class);
                    searchCriteriaExp.setPageSize((int) nombreDossiers);
                    results =
                        requeteurService.getResults(searchCriteriaExp, requeteUserSession, true).getResults().values();
                }

                dossiers = convertTo(results);
            }
        }

        return dossiers;
    }

    @Override
    protected DataSource getDataSource(CoreSession session, List<ExportDossierDTO> dossiers) {
        return EpgExcelUtil.exportDossiers(dossiers);
    }

    private static List<ExportDossierDTO> convertTo(Collection<ElasticDossier> dossiers) {
        return dossiers.stream().map(EpgAlertServiceImpl::convertTo).collect(Collectors.toList());
    }

    private static ExportDossierDTO convertTo(ElasticDossier dossier) {
        return new ExportDossierDTO(
            dossier.getDosNumeroNor(),
            dossier.getDosTitreActe(),
            dossier.getDosMinistereResp(),
            dossier.getDosMinistereAttache(),
            dossier.getDosPrenomRespDossier(),
            dossier.getDosNomRespDossier()
        );
    }
}
