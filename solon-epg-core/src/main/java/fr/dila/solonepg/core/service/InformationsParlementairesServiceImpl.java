package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.InformationsParlementairesService;
import fr.dila.solonepg.api.service.ParametreApplicationService;
import fr.dila.solonepp.rest.api.WSEpp;
import fr.dila.solonepp.rest.api.WSEvenement;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.rest.client.WSProxyFactory;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.AccuserReceptionRequest;
import fr.sword.xsd.solon.epp.AccuserReceptionResponse;
import fr.sword.xsd.solon.epp.CreationType;
import fr.sword.xsd.solon.epp.CreerVersionRequest;
import fr.sword.xsd.solon.epp.CreerVersionResponse;
import fr.sword.xsd.solon.epp.EppEvt48;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.EvtId;
import fr.sword.xsd.solon.epp.Institution;
import fr.sword.xsd.solon.epp.TransmissionDatePublicationJORequest;
import fr.sword.xsd.solon.epp.TransmissionDatePublicationJOResponse;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;

public class InformationsParlementairesServiceImpl implements InformationsParlementairesService {
    private static final STLogger LOGGER = STLogFactory.getLog(InformationsParlementairesServiceImpl.class);

    @Override
    public void callWsTransmissionDatePublicationJOEpp(CoreSession session, Dossier dossier) throws Exception {
        LOGGER.debug(session, STLogEnumImpl.LOG_DEBUG_TEC, "###APPEL TRANSMISSION DATE PUBLICATION JO####");
        WSEpp webservice = getWsEpp(session);
        if (webservice != null) {
            TransmissionDatePublicationJORequest request = new TransmissionDatePublicationJORequest();

            String idDossier = dossier.getNumeroNor();
            RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
            XMLGregorianCalendar datePublication = DateUtil.calendarToXMLGregorianCalendar(
                retourDila.getDateParutionJorf()
            );
            request.setIdDossier(idDossier);
            request.setDatePublication(datePublication);
            TransmissionDatePublicationJOResponse response = webservice.transmissionDatePublicationJO(request);
            if (TraitementStatut.OK.equals(response.getStatut())) {
                LOGGER.debug(session, STLogEnumImpl.LOG_DEBUG_TEC, "Retour OK du ws TransmissionDatePublicationJO");
            } else {
                // Statut KO
                LOGGER.error(
                    session,
                    EpgLogEnumImpl.FAIL_GET_WS_EPP_TEC,
                    "Appel ws transmissionDatePublication epp KO" + response.getMessageErreur()
                );
                throw new NuxeoException("Retour KO du ws TransmissionDatePublicationJO");
            }
        } else {
            LOGGER.error(
                session,
                EpgLogEnumImpl.FAIL_GET_WS_EPP_TEC,
                "Impossible de se connecter à EPP pour appeler TransmissionDatePublicationJO"
            );
            throw new NuxeoException("Impossible de se connecter à EPP pour appeler TransmissionDatePublicationJO");
        }
    }

    @Override
    public void callWsAccuserReception(CoreSession session, Dossier dossier, Object wsEvenement) throws Exception {
        LOGGER.info(STLogEnumImpl.LOG_INFO_TEC, "###APPEL ACCUSER RECEPTION####");
        WSEvenement webservice = (WSEvenement) wsEvenement;
        if (webservice != null) {
            AccuserReceptionRequest request = new AccuserReceptionRequest();

            EvtId evtId = new EvtId();
            evtId.setId(dossier.getIdEvenement());

            request.setIdEvenement(evtId);

            AccuserReceptionResponse response = webservice.accuserReception(request);
            if (TraitementStatut.OK.equals(response.getStatut())) {
                LOGGER.info(STLogEnumImpl.LOG_INFO_TEC, "Retour OK du ws AccuserReception");
            } else {
                // Statut KO
                LOGGER.info(
                    EpgLogEnumImpl.FAIL_GET_WS_EPP_TEC,
                    "Appel ws AccuserReception epp KO : " + response.getMessageErreur()
                );
                throw new NuxeoException("Retour KO du ws AccuserReception");
            }
        } else {
            LOGGER.info(
                EpgLogEnumImpl.FAIL_GET_WS_EPP_TEC,
                "Impossible de se connecter à EPP pour appeler AccuserReception"
            );
            throw new NuxeoException("Impossible de se connecter à EPP pour appeler AccuserReception");
        }
    }

    @Override
    public void callWsCreerVersionEvt48(CoreSession session, Dossier dossier) throws Exception {
        LOGGER.info(STLogEnumImpl.LOG_INFO_TEC, "###APPEL CREER VERSION####");
        WSEvenement webservice = getWsEvenement(session);
        if (webservice != null) {
            CreerVersionRequest request = new CreerVersionRequest();
            RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);

            EppEvt48 evt = new EppEvt48();
            evt.setIdEvenementPrecedent(dossier.getIdEvenement());
            evt.setIdDossier(dossier.getNumeroNor());
            evt.setEmetteur(Institution.DILA);
            evt.setDestinataire(
                "Sénat".equals(dossier.getEmetteur()) ? Institution.SENAT : Institution.ASSEMBLEE_NATIONALE
            );
            evt.getCopie().add(Institution.GOUVERNEMENT);
            evt.setObjet(dossier.getRubrique());
            evt.setCommentaire(retourDila.getTitreOfficiel());
            XMLGregorianCalendar dateJo = DateUtil.calendarToXMLGregorianCalendar(retourDila.getDateParutionJorf());
            evt.setDateJo(dateJo);
            evt.setNor(dossier.getNumeroNor());
            Integer pageJo = retourDila.getPageParutionJorf() != null
                ? retourDila.getPageParutionJorf().intValue()
                : null;
            evt.setPageJo(pageJo);
            Integer numeroJo;
            try {
                numeroJo = Integer.parseInt(retourDila.getNumeroTexteParutionJorf());
            } catch (NumberFormatException e) {
                numeroJo = null;
            }
            evt.setNumeroJo(numeroJo);

            EppEvtContainer eppEvtContainer = new EppEvtContainer();
            eppEvtContainer.setType(EvenementType.EVT_48);
            eppEvtContainer.setEvt48(evt);

            request.setEvenement(eppEvtContainer);
            request.setModeCreation(CreationType.PUBLIER);

            CreerVersionResponse response = webservice.creerVersion(request);
            if (TraitementStatut.OK.equals(response.getStatut())) {
                LOGGER.info(STLogEnumImpl.LOG_INFO_TEC, "Retour OK du ws CreerVersion");
            } else {
                // Statut KO
                LOGGER.info(
                    EpgLogEnumImpl.FAIL_GET_WS_EPP_TEC,
                    "Appel ws CreerVersion epp KO : " + response.getMessageErreur()
                );
                LOGGER.info(
                    EpgLogEnumImpl.FAIL_GET_WS_EPP_TEC,
                    "Appel ws CreerVersion epp KO : " + response.getMessageErreur()
                );
                throw new NuxeoException("Retour KO du ws CreerVersion");
            }
        } else {
            LOGGER.info(
                EpgLogEnumImpl.FAIL_GET_WS_EPP_TEC,
                "Impossible de se connecter à EPP pour appeler CreerVersion"
            );
            throw new NuxeoException("Impossible de se connecter à EPP pour appeler CreerVersion");
        }
    }

    private WSEpp getWsEpp(CoreSession session) throws WSProxyFactoryException {
        String url = null;
        String username = null;
        String value = null;
        String keyAlias = null;

        ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
        ParametrageApplication parametrageApplication = parametreApplicationService.getParametreApplicationDocument(
            session
        );
        url = parametrageApplication.getUrlEppInfosParl();
        if (StringUtils.isNotBlank(url) && url.contains("/site/solonepp")) {
            username = parametrageApplication.getLoginEppInfosParl();
            value = parametrageApplication.getPassEppInfosParl();
        } else {
            url = null;
        }

        if (url == null) {
            return null;
        }

        WSProxyFactory factory = new WSProxyFactory(url, null, username, keyAlias);
        return factory.getService(WSEpp.class, value);
    }

    public WSEvenement getWsEvenement(CoreSession session) throws WSProxyFactoryException {
        String url = null;
        String username = null;
        String value = null;
        String keyAlias = null;

        ParametreApplicationService parametreApplicationService = SolonEpgServiceLocator.getParametreApplicationService();
        ParametrageApplication parametrageApplication = parametreApplicationService.getParametreApplicationDocument(
            session
        );
        url = parametrageApplication.getUrlEppInfosParl();
        if (StringUtils.isNotBlank(url) && url.contains("/site/solonepp")) {
            username = parametrageApplication.getLoginEppInfosParl();
            value = parametrageApplication.getPassEppInfosParl();
        } else {
            url = null;
        }

        if (url == null) {
            return null;
        }

        WSProxyFactory factory = new WSProxyFactory(url, null, username, keyAlias);
        return factory.getService(WSEvenement.class, value);
    }
}
