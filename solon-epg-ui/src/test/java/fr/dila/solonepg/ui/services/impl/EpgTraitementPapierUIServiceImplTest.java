package fr.dila.solonepg.ui.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.solonepg.api.service.ListeTraitementPapierService;
import fr.dila.solonepg.api.service.TraitementPapierService;
import fr.dila.solonepg.api.service.vocabulary.TypeAvisTraitementPapierService;
import fr.dila.solonepg.api.service.vocabulary.TypeSignataireTraitementPapierService;
import fr.dila.solonepg.core.cases.typescomplexe.DestinataireCommunicationImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication.CommunicationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication.DestinataireCommunicationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.reference.ComplementDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgTraitementPapierUIService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.feature.SolonMockitoFeature;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.runtime.test.runner.Features;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ LockUtils.class, SolonEpgServiceLocator.class, STServiceLocator.class })
@PowerMockIgnore("javax.management.*")
@Features(SolonMockitoFeature.class)
public class EpgTraitementPapierUIServiceImplTest {

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    private EpgTraitementPapierUIService service = new EpgTraitementPapierUIServiceImpl();

    @Mock
    private ListeTraitementPapierService listeTraitementPapierService;

    @Mock
    private TraitementPapierService traitementPapierService;

    @Mock
    private TypeAvisTraitementPapierService typeAvisTraitementPapierService;

    @Mock
    private TypeSignataireTraitementPapierService typeSignataireTraitementPapierService;

    @Mock
    private STUserService userService;

    @Mock
    private DocumentModel dossier;

    @Mock
    private CoreSession session;

    @Mock
    private NuxeoPrincipal principal;

    private IdRef dossierRefId = new IdRef(UUID.randomUUID().toString());

    @Captor
    private ArgumentCaptor<ArrayList<Map<String, Serializable>>> complexeTypeListCaptor;

    @Before
    public void setup() {
        when(dossier.getType()).thenReturn(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);

        PowerMockito.mockStatic(SolonEpgServiceLocator.class);
        when(SolonEpgServiceLocator.getListeTraitementPapierService()).thenReturn(listeTraitementPapierService);
        when(SolonEpgServiceLocator.getTraitementPapierService()).thenReturn(traitementPapierService);
        when(SolonEpgServiceLocator.getTypeSignataireTraitementPapierService())
            .thenReturn(typeSignataireTraitementPapierService);
        when(SolonEpgServiceLocator.getTypeAvisTraitementPapierService()).thenReturn(typeAvisTraitementPapierService);

        PowerMockito.mockStatic(STServiceLocator.class);
        when(STServiceLocator.getSTUserService()).thenReturn(userService);

        when(dossier.getRef()).thenReturn(dossierRefId);
        when(session.getPrincipal()).thenReturn(principal);
    }

    @Test
    public void testGetComplementDTO() {
        // Given
        SpecificContext context = new SpecificContext();
        context.setCurrentDocument(dossier);

        Calendar now = Calendar.getInstance();
        when(dossier.getPropertyValue(TraitementPapierConstants.TRAITEMENT_PAPIER_COMMENTAIRE_REFERENCE_XPATH))
            .thenReturn("commentaire");
        when(dossier.getPropertyValue(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ARRIVE_PAPIER_XPATH))
            .thenReturn(now);
        when(dossier.getPropertyValue(TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATAIRE_XPATH)).thenReturn("4");
        when(dossier.getPropertyValue(TraitementPapierConstants.TRAITEMENT_PAPIER_TEXTE_A_PUBLIER_XPATH))
            .thenReturn(Boolean.TRUE);
        when(dossier.getPropertyValue(TraitementPapierConstants.TRAITEMENT_PAPIER_TEXTE_SOUMIS_A_VALIDATION_XPATH))
            .thenReturn(Boolean.TRUE);
        when(typeSignataireTraitementPapierService.getEntry(any())).thenReturn(Optional.empty());

        // When
        ComplementDTO complementDTO = service.getComplementDTO(context);

        // Then
        assertThat(complementDTO.getCommentaire()).isEqualTo("commentaire");
        assertThat(complementDTO.getDateArriveePapier()).isEqualTo(now);
        assertThat(complementDTO.getSignataire()).isEqualTo("4");
        assertThat(complementDTO.getTexteAPublier()).isTrue();
        assertThat(complementDTO.getTexteSoumisAValidation()).isTrue();
    }

    @Test
    public void testSaveComplementDTO() {
        // Given
        SpecificContext context = new SpecificContext();
        context.setCurrentDocument(dossier);
        context.setSession(session);
        when(principal.isMemberOf(SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_WRITER)).thenReturn(true);

        PowerMockito.mockStatic(LockUtils.class);
        when(LockUtils.isLockedByCurrentUser(session, dossierRefId)).thenReturn(true);

        Calendar now = Calendar.getInstance();
        ComplementDTO complementDTO = new ComplementDTO();
        complementDTO.setCommentaire("commentaire");
        complementDTO.setDateArriveePapier(now);
        complementDTO.setSignataire("signataire");
        complementDTO.setTexteAPublier(true);
        complementDTO.setTexteSoumisAValidation(true);
        context.putInContextData(EpgContextDataKey.TRAITEMENT_PAPIER_REFERENCE, complementDTO);

        // When
        service.saveComplementDTO(context);

        // Then
        assertThat(context.getMessageQueue().getErrorQueue()).isEmpty();
        assertThat(context.getMessageQueue().getInfoQueue()).isNotEmpty();

        Mockito
            .verify(dossier)
            .setPropertyValue(TraitementPapierConstants.TRAITEMENT_PAPIER_COMMENTAIRE_REFERENCE_XPATH, "commentaire");
        Mockito
            .verify(dossier)
            .setPropertyValue(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ARRIVE_PAPIER_XPATH, now);
        Mockito
            .verify(dossier)
            .setPropertyValue(TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATAIRE_XPATH, "signataire");
        Mockito
            .verify(dossier)
            .setPropertyValue(TraitementPapierConstants.TRAITEMENT_PAPIER_TEXTE_A_PUBLIER_XPATH, true);
        Mockito
            .verify(dossier)
            .setPropertyValue(TraitementPapierConstants.TRAITEMENT_PAPIER_TEXTE_SOUMIS_A_VALIDATION_XPATH, true);

        Mockito.verify(traitementPapierService).saveTraitementPapier(session, dossier);
        Mockito.verify(listeTraitementPapierService).updateFichePresentationAfterUpdateBordereau(session, dossier);
    }

    @Test
    public void testGetCommunicationDTO() {
        // Given
        SpecificContext context = new SpecificContext();
        context.setCurrentDocument(dossier);
        context.setSession(session);

        DestinataireCommunicationImpl destinataireCommunication = new DestinataireCommunicationImpl();
        destinataireCommunication.setDateEnvoi(DateUtil.localDateToGregorianCalendar(LocalDate.of(2021, 1, 1)));
        destinataireCommunication.setDateRelance(DateUtil.localDateToGregorianCalendar(LocalDate.of(2021, 1, 2)));
        destinataireCommunication.setDateRetour(DateUtil.localDateToGregorianCalendar(LocalDate.of(2021, 1, 3)));
        destinataireCommunication.setNomDestinataireCommunication("nomDestinataire");
        destinataireCommunication.setObjet("objet");
        destinataireCommunication.setSensAvis("sens");

        ArrayList<Map<String, Serializable>> cabinetPmCommunication = new ArrayList<>();
        cabinetPmCommunication.add(destinataireCommunication.getSerializableMap());

        when(dossier.getPropertyValue(TraitementPapierConstants.TRAITEMENT_PAPIER_CABINET_PM_COMMUNICATION_XPATH))
            .thenReturn(cabinetPmCommunication);

        // When
        CommunicationDTO communicationDTO = service.getCommunicationDTO(context);

        // Then
        assertThat(communicationDTO).isNotNull();
        assertThat(communicationDTO.getCabinetPm()).hasSize(1);
        assertThat(communicationDTO.getCabinetPm().get(0).getDateEnvoi())
            .isEqualTo(DateUtil.localDateToGregorianCalendar(LocalDate.of(2021, 1, 1)));
        assertThat(communicationDTO.getCabinetPm().get(0).getDateRelance())
            .isEqualTo(DateUtil.localDateToGregorianCalendar(LocalDate.of(2021, 1, 2)));
        assertThat(communicationDTO.getCabinetPm().get(0).getDateRetour())
            .isEqualTo(DateUtil.localDateToGregorianCalendar(LocalDate.of(2021, 1, 3)));
        assertThat(communicationDTO.getCabinetPm().get(0).getDestinataireCommunication()).isEqualTo("nomDestinataire");
        assertThat(communicationDTO.getCabinetPm().get(0).getObjet()).isEqualTo("objet");
        assertThat(communicationDTO.getCabinetPm().get(0).getSensAvis()).isEqualTo("sens");
    }

    @Test
    public void testSaveCommunicationDTO() {
        // Given
        SpecificContext context = new SpecificContext();
        context.setCurrentDocument(dossier);
        context.setSession(session);

        DestinataireCommunicationDTO destinataire = new DestinataireCommunicationDTO();
        destinataire.setDateEnvoi(DateUtil.localDateToGregorianCalendar(LocalDate.of(2021, 1, 1)));
        destinataire.setDateRelance(DateUtil.localDateToGregorianCalendar(LocalDate.of(2021, 1, 2)));
        destinataire.setDateRetour(DateUtil.localDateToGregorianCalendar(LocalDate.of(2021, 1, 3)));
        destinataire.setDestinataireCommunication("nomDestinataire");
        destinataire.setObjet("objet");
        destinataire.setSensAvis("sens");

        List<DestinataireCommunicationDTO> cabinetPm = new ArrayList<>();
        cabinetPm.add(destinataire);

        CommunicationDTO communicationDTO = new CommunicationDTO();
        communicationDTO.setCabinetPm(cabinetPm);

        context.putInContextData(EpgContextDataKey.TRAITEMENT_PAPIER_COMMUNICATION, communicationDTO);

        PowerMockito.mockStatic(LockUtils.class);
        when(LockUtils.isLockedByCurrentUser(session, dossierRefId)).thenReturn(true);

        when(principal.isMemberOf(SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_WRITER)).thenReturn(true);

        // When
        service.saveCommunicationDTO(context);

        // Then
        assertThat(context.getMessageQueue().getErrorQueue()).isEmpty();
        assertThat(context.getMessageQueue().getInfoQueue()).isNotEmpty();

        Mockito
            .verify(dossier)
            .setPropertyValue(
                Mockito.eq(TraitementPapierConstants.TRAITEMENT_PAPIER_CABINET_PM_COMMUNICATION_XPATH),
                complexeTypeListCaptor.capture()
            );

        ArrayList<Map<String, Serializable>> complexeTypeList = complexeTypeListCaptor.getValue();

        assertThat(complexeTypeList).hasSize(1);
        DestinataireCommunicationImpl destinataireCommunicationImpl = new DestinataireCommunicationImpl(
            complexeTypeList.get(0)
        );

        assertThat(destinataireCommunicationImpl.getDateEnvoi())
            .isEqualTo(DateUtil.localDateToGregorianCalendar(LocalDate.of(2021, 1, 1)));
        assertThat(destinataireCommunicationImpl.getDateRelance())
            .isEqualTo(DateUtil.localDateToGregorianCalendar(LocalDate.of(2021, 1, 2)));
        assertThat(destinataireCommunicationImpl.getDateRetour())
            .isEqualTo(DateUtil.localDateToGregorianCalendar(LocalDate.of(2021, 1, 3)));
        assertThat(destinataireCommunicationImpl.getNomDestinataireCommunication()).isEqualTo("nomDestinataire");
        assertThat(destinataireCommunicationImpl.getObjet()).isEqualTo("objet");
        assertThat(destinataireCommunicationImpl.getSensAvis()).isEqualTo("sens");

        Mockito.verify(traitementPapierService).saveTraitementPapier(session, dossier);
        Mockito.verify(listeTraitementPapierService).updateFichePresentationAfterUpdateBordereau(session, dossier);
    }
}
