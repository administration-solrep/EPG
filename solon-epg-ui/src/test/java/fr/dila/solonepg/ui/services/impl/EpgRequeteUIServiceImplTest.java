package fr.dila.solonepg.ui.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.enumeration.QueryType;
import fr.dila.solonepg.api.requeteur.EpgRequeteExperte;
import fr.dila.solonepg.elastic.models.search.ClauseEt;
import fr.dila.solonepg.elastic.models.search.ClauseOu;
import fr.dila.solonepg.elastic.models.search.RechercheLibre;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaExp;
import fr.dila.solonepg.elastic.models.search.enums.ElasticOperatorEnum;
import fr.dila.solonepg.ui.services.EpgRechercheUIService;
import fr.dila.solonepg.ui.services.EpgRequeteUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.ss.ui.bean.AlerteForm;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.api.constant.STRequeteConstants;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.service.SecurityService;
import fr.dila.st.core.feature.SolonMockitoFeature;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.LinkedHashMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;
import org.nuxeo.runtime.test.runner.Features;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ STServiceLocator.class, EpgUIServiceLocator.class })
@PowerMockIgnore("javax.management.*")
@Features(SolonMockitoFeature.class)
public class EpgRequeteUIServiceImplTest {
    private static final String SEARCHED_TEXT = "plafond";
    private static final String USERNAME = "pmontier";
    private static final String PARENT_PATH = "/case-management/UserWorkspaces/" + USERNAME;
    private static final String DOC_ID = "1c9d5185-8067-4c89-b74c-07cf04b19498";

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private EpgRequeteUIService service;

    private AlerteForm alerteForm;

    @Mock
    private SpecificContext context;

    @Mock
    private CoreSession session;

    @Mock
    private DocumentModel doc;

    @Mock
    private DocumentModel workspace;

    @Mock
    private NuxeoPrincipal principal;

    @Mock
    private UserWorkspaceService userWorkspaceService;

    @Mock
    private EpgRechercheUIService rechercheUIService;

    @Mock
    private SecurityService securityService;

    @Mock
    private DocumentModel savedDoc;

    @Mock
    private EpgRequeteExperte requeteExperte;

    @Before
    public void setup() {
        service = new EpgRequeteUIServiceImpl();

        PowerMockito.mockStatic(STServiceLocator.class, EpgUIServiceLocator.class);
        when(STServiceLocator.getUserWorkspaceService()).thenReturn(userWorkspaceService);
        when(EpgUIServiceLocator.getEpgRechercheUIService()).thenReturn(rechercheUIService);
        when(STServiceLocator.getSecurityService()).thenReturn(securityService);

        when(context.getSession()).thenReturn(session);
        when(context.getCurrentDocument()).thenReturn(null);

        alerteForm = new AlerteForm();
        when(context.getFromContextData(SSContextDataKey.ALERTE_FORM)).thenReturn(alerteForm);

        when(doc.getAdapter(EpgRequeteExperte.class)).thenReturn(requeteExperte);

        when(session.createDocument(doc)).thenReturn(savedDoc);
        when(session.getPrincipal()).thenReturn(principal);

        when(principal.getName()).thenReturn(USERNAME);

        when(savedDoc.getId()).thenReturn(DOC_ID);

        when(userWorkspaceService.getCurrentUserPersonalWorkspace(session)).thenReturn(workspace);
        when(workspace.getPathAsString()).thenReturn(PARENT_PATH);
    }

    @Test
    public void saveRequeteLibreES() {
        String alerteTitre = "test alerte epg recherche libre";
        alerteForm.setTitre(alerteTitre);
        when(session.createDocumentModel(PARENT_PATH, alerteTitre, STRequeteConstants.SMART_FOLDER_DOCUMENT_TYPE))
            .thenReturn(doc);

        RechercheLibre rechercheLibre = new RechercheLibre();
        rechercheLibre.setFulltext(SEARCHED_TEXT);
        rechercheLibre.setExpressionExacte(true);
        rechercheLibre.setArchive(true);
        rechercheLibre.setProd(true);

        when(rechercheUIService.toRechercheLibre(context)).thenReturn(rechercheLibre);

        String createdRequeteId = service.saveRequeteLibreES(context);

        assertThat(createdRequeteId).isEqualTo(DOC_ID);

        String expectedWhereClause =
            "{\"fulltext\":\"plafond\",\"directionAttache\":[],\"ministereAttache\":[],\"vecteurPublication\":[],\"datePublicationMin\":\"\",\"datePublicationMax\":\"\",\"datePublicationJoMin\":\"\",\"datePublicationJoMax\":\"\",\"dateSignatureMin\":\"\",\"dateSignatureMax\":\"\",\"dateCreationMin\":\"\",\"dateCreationMax\":\"\",\"statut\":[],\"categorieActe\":[],\"statutArchivage\":[],\"sortType\":\"RELEVANCE_DESC\",\"poste\":\"\",\"expressionExacte\":true,\"docCurrentVersion\":true,\"archive\":true,\"prod\":true,\"typeActe\":[],\"hasDroitsNomination\":false,\"page\":1,\"pageSize\":0}";
        verify(requeteExperte).setWhereClause(expectedWhereClause);
        verify(requeteExperte).setQueryType(QueryType.ES_LIBRE);
        verify(session).createDocumentModel(PARENT_PATH, alerteTitre, STRequeteConstants.SMART_FOLDER_DOCUMENT_TYPE);
        verify(session).createDocument(doc);
        verify(securityService).addAceToAcl(savedDoc, ACL.LOCAL_ACL, USERNAME, SecurityConstants.EVERYTHING);
    }

    @Test
    public void saveRequeteExperteES() {
        String alerteTitre = "test alerte epg recherche experte";
        alerteForm.setTitre(alerteTitre);
        when(session.createDocumentModel(PARENT_PATH, alerteTitre, STRequeteConstants.SMART_FOLDER_DOCUMENT_TYPE))
            .thenReturn(doc);

        SearchCriteriaExp searchCriteriaExp = new SearchCriteriaExp();
        LinkedHashMap<String, String> tris = new LinkedHashMap<>(1);
        tris.put(
            STSchemaConstant.DOSSIER_SCHEMA_PREFIX + ":" + DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PROPERTY,
            SortOrder.ASC.getValue()
        );
        searchCriteriaExp.setTris(tris);

        ClauseEt clauseEt = new ClauseEt();
        clauseEt.setField(
            STSchemaConstant.DOSSIER_SCHEMA_PREFIX +
            ":" +
            DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY +
            ".titre"
        );
        clauseEt.setValeur(SEARCHED_TEXT);
        clauseEt.setOperator(ElasticOperatorEnum.CONTIENT_PHRASE);

        ClauseOu clauseOu = new ClauseOu();
        clauseOu.setClausesEt(Lists.newArrayList(clauseEt));
        searchCriteriaExp.setClausesOu(Lists.newArrayList(clauseOu));

        searchCriteriaExp.setPageSize(10);

        when(rechercheUIService.toSearchCriteriaExp(context)).thenReturn(searchCriteriaExp);

        String createdRequeteId = service.saveRequeteExperteES(context);

        assertThat(createdRequeteId).isEqualTo(DOC_ID);

        String expectedWhereClause =
            "{\"tris\":{\"dos:numeroNor\":\"asc\"},\"clausesOu\":[{\"clausesEt\":[{\"field\":\"dos:applicationLoi.titre\",\"valeur\":\"plafond\",\"operator\":\"CONTIENT_PHRASE\"}]}],\"hasDroitsNomination\":false,\"page\":1,\"pageSize\":0}";
        verify(requeteExperte).setWhereClause(expectedWhereClause);
        verify(requeteExperte).setQueryType(QueryType.ES_EXPERTE);
        verify(session).createDocumentModel(PARENT_PATH, alerteTitre, STRequeteConstants.SMART_FOLDER_DOCUMENT_TYPE);
        verify(session).createDocument(doc);
        verify(securityService).addAceToAcl(savedDoc, ACL.LOCAL_ACL, USERNAME, SecurityConstants.EVERYTHING);
    }
}
