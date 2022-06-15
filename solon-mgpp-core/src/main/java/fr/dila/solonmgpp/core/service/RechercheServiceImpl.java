package fr.dila.solonmgpp.core.service;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepp.rest.api.WSEvenement;
import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.MessageService;
import fr.dila.solonmgpp.api.service.RechercheService;
import fr.dila.solonmgpp.core.builder.QueryBuilder;
import fr.dila.solonmgpp.core.domain.FichePresentationDRImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationDecretImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.solonmgpp.core.dto.RechercheDTO;
import fr.dila.solonmgpp.core.util.WSErrorHelper;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.rest.client.HttpTransactionException;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.Message;
import fr.sword.xsd.solon.epp.QueryRechercheEvenement;
import fr.sword.xsd.solon.epp.RechercherEvenementRequest;
import fr.sword.xsd.solon.epp.RechercherEvenementResponse;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;

public class RechercheServiceImpl implements RechercheService {
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(RechercheServiceImpl.class);

    @Override
    public CritereRechercheDTO initCritereRechercheDTO(final CritereRechercheDTO critereRechercheDTO) {
        if (SolonMgppActionConstant.PROCEDURE_LEGISLATIVE.equals(critereRechercheDTO.getMenu())) {
            // RG-PROC-LEG-41
            final List<String> list = critereRechercheDTO.getTypeEvenement();
            list.add(EvenementType.EVT_01.value());
            list.add(EvenementType.EVT_02.value());
            critereRechercheDTO.setTypeEvenement(list);
        } else if (SolonMgppActionConstant.PUBLICATION.equals(critereRechercheDTO.getMenu())) {
            // RG-PROC-LEG-41
            final List<String> list = critereRechercheDTO.getTypeActes();
            list.addAll(SolonEpgServiceLocator.getActeService().getLoiList());
            list.add(TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT);
            critereRechercheDTO.setTypeActes(list);
        } else if (SolonMgppActionConstant.DEPOT_DE_RAPPORT.equals(critereRechercheDTO.getMenu())) {
            // RG-PROC-RAP-11
            final List<String> list = critereRechercheDTO.getTypeActes();
            list.add(TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT);
            critereRechercheDTO.setTypeActes(list);
        } else if (SolonMgppActionConstant.INTERVENTION_EXTERIEURE.equals(critereRechercheDTO.getMenu())) {
            final List<String> list = critereRechercheDTO.getTypeEvenement();
            list.add(EvenementType.EVT_36.value());
            list.add(EvenementType.EVT_38.value());
            critereRechercheDTO.setTypeEvenement(list);
        } else if (SolonMgppActionConstant.RESOLUTION_ARTICLE_341.equals(critereRechercheDTO.getMenu())) {
            final List<String> list = critereRechercheDTO.getTypeEvenement();
            list.add(EvenementType.EVT_39.value());
            list.add(EvenementType.EVT_40.value());
            list.add(EvenementType.EVT_41.value());
            list.add(EvenementType.EVT_43.value());
            list.add(EvenementType.EVT_43_BIS.value());
            critereRechercheDTO.setTypeEvenement(list);
        } else if (SolonMgppActionConstant.DESIGNATION_OEP.equals(critereRechercheDTO.getMenu())) {
            // aucune limitation : recherche dans les ficheOEP
        } else if (this.allowSearch(critereRechercheDTO.getMenu())) {
            // aucune limitation : recherche dans les fiches d'avis de nomination
        } else if (SolonMgppActionConstant.DECRET.equals(critereRechercheDTO.getMenu())) {
            final List<String> list = critereRechercheDTO.getTypeActes();
            list.add(TypeActeConstants.TYPE_ACTE_DECRET_PR);
            list.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND);
        } else {
            throw new UnsupportedOperationException("Menu inconnu.");
        }

        return critereRechercheDTO;
    }

    private boolean allowSearch(String menu) {
        return (
            SolonMgppActionConstant.AVIS_NOMINATION.equals(menu) ||
            SolonMgppActionConstant.DECLARATION_DE_POLITIQUE_GENERALE.equals(menu) ||
            SolonMgppActionConstant.DECLARATION_SUR_UN_SUJET_DETERMINE.equals(menu) ||
            SolonMgppActionConstant.DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C.equals(menu) ||
            SolonMgppActionConstant.DEMANDE_AUDITION.equals(menu) ||
            SolonMgppActionConstant.AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES.equals(menu)
        );
    }

    @Override
    public List<MessageDTO> findMessage(final CritereRechercheDTO critereRechercheDTO, final CoreSession session) {
        final List<MessageDTO> listMessage = new ArrayList<MessageDTO>();

        final RechercherEvenementResponse rechercherEvenementResponse = findMessageWS(critereRechercheDTO, session);

        final MessageService messageService = SolonMgppServiceLocator.getMessageService();

        for (final Message message : rechercherEvenementResponse.getMessage()) {
            listMessage.add(messageService.buildMessageDTOFromMessage(message, session));
        }

        return listMessage;
    }

    @Override
    public RechercherEvenementResponse findMessageWS(
        final CritereRechercheDTO critereRechercheDTO,
        final CoreSession session
    ) {
        // corbeille EPP
        WSEvenement wsEvenement = null;

        try {
            wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
        } catch (final WSProxyFactoryException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(e);
        }

        final RechercheDTO rechercheDTO = QueryBuilder.getInstance().buildQuery(critereRechercheDTO);
        final QueryRechercheEvenement queryRechercheEvenement = new QueryRechercheEvenement();
        queryRechercheEvenement.setWhereClause(rechercheDTO.getWhereClause().toString());
        queryRechercheEvenement.getParametres().addAll(rechercheDTO.getParamList());

        final RechercherEvenementRequest rechercherEvenementRequest = new RechercherEvenementRequest();
        rechercherEvenementRequest.setParRequeteXsd(queryRechercheEvenement);

        RechercherEvenementResponse rechercherEvenementResponse = null;

        try {
            rechercherEvenementResponse = wsEvenement.rechercherEvenement(rechercherEvenementRequest);
        } catch (final HttpTransactionException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            throw new NuxeoException(SolonMgppWsLocator.getConnexionFailureMessage(session));
        } catch (final Exception e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_MESSAGE_TEC, e);
            throw new NuxeoException(e);
        }

        if (rechercherEvenementResponse == null) {
            throw new NuxeoException("Erreur de communication avec SOLON EPP, la récupération des messages a échoué.");
        } else if (
            rechercherEvenementResponse.getStatut() == null ||
            !TraitementStatut.OK.equals(rechercherEvenementResponse.getStatut())
        ) {
            final NuxeoException clientException = new NuxeoException(
                "Erreur de communication avec SOLON EPP, la récupération des messages a échoué." +
                WSErrorHelper.buildCleanMessage(rechercherEvenementResponse.getMessageErreur())
            );

            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_MESSAGE_TEC, clientException);
            throw clientException;
        }
        return rechercherEvenementResponse;
    }

    @Override
    public List<DocumentModel> findDossier(
        final CoreSession coreSession,
        final CritereRechercheDTO critereRechercheDTO
    ) {
        final RechercheDTO rechercheDTO = QueryBuilder.getInstance().buildQueryDossier(critereRechercheDTO);

        final StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        query.append(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        query.append(" as d WHERE ");

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            coreSession,
            DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
            query.append(rechercheDTO.getWhereClause()).toString(),
            rechercheDTO.getParamList().toArray()
        );
    }

    @Override
    public List<DocumentModel> findOEP(final CoreSession coreSession, final CritereRechercheDTO critereRechercheDTO) {
        final RechercheDTO rechercheDTO = QueryBuilder.getInstance().buildQueryOEP(critereRechercheDTO);

        final StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        query.append(FichePresentationOEPImpl.DOC_TYPE);
        query.append(" as d ");

        if (StringUtils.isNotBlank(rechercheDTO.getWhereClause().toString())) {
            query.append(" WHERE ");
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            coreSession,
            FichePresentationOEPImpl.DOC_TYPE,
            query.append(rechercheDTO.getWhereClause()).toString(),
            rechercheDTO.getParamList().toArray()
        );
    }

    @Override
    public List<DocumentModel> findAVI(final CoreSession coreSession, final CritereRechercheDTO critereRechercheDTO) {
        final RechercheDTO rechercheDTO = QueryBuilder.getInstance().buildQueryAVI(critereRechercheDTO);

        final StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        query.append(FichePresentationAVI.DOC_TYPE);
        query.append(" as d ");

        if (StringUtils.isNotBlank(rechercheDTO.getWhereClause().toString())) {
            query.append(" WHERE ");
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            coreSession,
            FichePresentationAVI.DOC_TYPE,
            query.append(rechercheDTO.getWhereClause()).toString(),
            rechercheDTO.getParamList().toArray()
        );
    }

    @Override
    public List<DocumentModel> findDecret(
        final CoreSession coreSession,
        final CritereRechercheDTO critereRechercheDTO
    ) {
        final RechercheDTO rechercheDTO = QueryBuilder.getInstance().buildQueryDecret(critereRechercheDTO);

        final StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        query.append(FichePresentationDecretImpl.DOC_TYPE);
        query.append(" as d ");

        if (StringUtils.isNotBlank(rechercheDTO.getWhereClause().toString())) {
            query.append(" WHERE ");
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            coreSession,
            FichePresentationDecretImpl.DOC_TYPE,
            query.append(rechercheDTO.getWhereClause()).toString(),
            rechercheDTO.getParamList().toArray()
        );
    }

    @Override
    public List<DocumentModel> findDOC(final CoreSession coreSession, final CritereRechercheDTO critereRechercheDTO) {
        final RechercheDTO rechercheDTO = QueryBuilder.getInstance().buildQueryDOC(critereRechercheDTO);

        final StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        query.append(FichePresentationDOC.DOC_TYPE);
        query.append(" as d ");

        if (StringUtils.isNotBlank(rechercheDTO.getWhereClause().toString())) {
            query.append(" WHERE ");
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            coreSession,
            FichePresentationDOC.DOC_TYPE,
            query.append(rechercheDTO.getWhereClause()).toString(),
            rechercheDTO.getParamList().toArray()
        );
    }

    @Override
    public List<DocumentModel> findDPG(final CoreSession coreSession, final CritereRechercheDTO critereRechercheDTO) {
        final RechercheDTO rechercheDTO = QueryBuilder.getInstance().buildQueryDPG(critereRechercheDTO);

        final StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        query.append(FichePresentationDPG.DOC_TYPE);
        query.append(" as d ");

        if (StringUtils.isNotBlank(rechercheDTO.getWhereClause().toString())) {
            query.append(" WHERE ");
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            coreSession,
            FichePresentationDPG.DOC_TYPE,
            query.append(rechercheDTO.getWhereClause()).toString(),
            rechercheDTO.getParamList().toArray()
        );
    }

    @Override
    public List<DocumentModel> findSD(final CoreSession coreSession, final CritereRechercheDTO critereRechercheDTO) {
        final RechercheDTO rechercheDTO = QueryBuilder.getInstance().buildQuerySD(critereRechercheDTO);

        final StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        query.append(FichePresentationSD.DOC_TYPE);
        query.append(" as d ");

        if (StringUtils.isNotBlank(rechercheDTO.getWhereClause().toString())) {
            query.append(" WHERE ");
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            coreSession,
            FichePresentationSD.DOC_TYPE,
            query.append(rechercheDTO.getWhereClause()).toString(),
            rechercheDTO.getParamList().toArray()
        );
    }

    @Override
    public List<DocumentModel> findJSS(final CoreSession coreSession, final CritereRechercheDTO critereRechercheDTO) {
        final RechercheDTO rechercheDTO = QueryBuilder.getInstance().buildQueryJSS(critereRechercheDTO);

        final StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        query.append(FichePresentationJSS.DOC_TYPE);
        query.append(" as d ");

        if (StringUtils.isNotBlank(rechercheDTO.getWhereClause().toString())) {
            query.append(" WHERE ");
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            coreSession,
            FichePresentationJSS.DOC_TYPE,
            query.append(rechercheDTO.getWhereClause()).toString(),
            rechercheDTO.getParamList().toArray()
        );
    }

    @Override
    public List<DocumentModel> findAUD(final CoreSession coreSession, final CritereRechercheDTO critereRechercheDTO) {
        final RechercheDTO rechercheDTO = QueryBuilder.getInstance().buildQueryAUD(critereRechercheDTO);

        final StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        query.append(FichePresentationAUD.DOC_TYPE);
        query.append(" as d ");

        if (StringUtils.isNotBlank(rechercheDTO.getWhereClause().toString())) {
            query.append(" WHERE ");
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            coreSession,
            FichePresentationAUD.DOC_TYPE,
            query.append(rechercheDTO.getWhereClause()).toString(),
            rechercheDTO.getParamList().toArray()
        );
    }

    @Override
    public List<DocumentModel> findDR(final CoreSession coreSession, final CritereRechercheDTO critereRechercheDTO) {
        final RechercheDTO rechercheDTO = QueryBuilder.getInstance().buildQueryDR(critereRechercheDTO);

        final StringBuilder query = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        query.append(FichePresentationDRImpl.DOC_TYPE);
        query.append(" as d ");

        if (StringUtils.isNotBlank(rechercheDTO.getWhereClause().toString())) {
            query.append(" WHERE ");
        }

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            coreSession,
            FichePresentationDRImpl.DOC_TYPE,
            query.append(rechercheDTO.getWhereClause()).toString(),
            rechercheDTO.getParamList().toArray()
        );
    }
}
