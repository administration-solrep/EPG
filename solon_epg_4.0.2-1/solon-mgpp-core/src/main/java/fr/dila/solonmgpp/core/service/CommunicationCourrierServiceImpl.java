package fr.dila.solonmgpp.core.service;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getTableReferenceService;
import static fr.dila.solonmgpp.api.constant.MgppDocTypeConstants.COMMUNICATION_COURRIER_TYPE;
import static fr.dila.solonmgpp.core.service.SolonMgppServiceLocator.getParametreMgppService;
import static fr.dila.st.core.service.STServiceLocator.getSTUserService;
import static fr.dila.st.core.util.SolonDateConverter.getClientConverter;
import static java.util.Optional.ofNullable;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.domain.CommunicationCourrier;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.Navette;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.HistoriqueDossierDTO;
import fr.dila.solonmgpp.api.node.MandatNode;
import fr.dila.solonmgpp.api.service.CommunicationCourrierService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.sword.xsd.solon.epp.EtatVersion;
import fr.sword.xsd.solon.epp.NiveauLectureCode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public class CommunicationCourrierServiceImpl implements CommunicationCourrierService {
    private static final STLogger LOGGER = STLogFactory.getLog(CommunicationCourrierServiceImpl.class);

    @Override
    public CommunicationCourrier createCommunicationCourrier(
        CoreSession session,
        Dossier dossier,
        DocumentModel fiche,
        NuxeoPrincipal actingUser
    ) {
        LOGGER.info(session, STLogEnumImpl.LOG_INFO_TEC, "Initialisation des métadonnées du courrier");
        DocumentModel commCourrierDoc = session.createDocumentModel(COMMUNICATION_COURRIER_TYPE);
        CommunicationCourrier commCourrier = commCourrierDoc.getAdapter(CommunicationCourrier.class);

        ParametrageMgpp param = getParametreMgppService().findParametrageMgpp(session);

        if (param != null) {
            MandatNode auteurLex01 = SolonMgppServiceLocator
                .getTableReferenceService()
                .getMandat(param.getAuteurLex01(), session);

            if (auteurLex01 != null && auteurLex01.getIdentite() != null) {
                commCourrier.setAuteurLex01(auteurLex01.getIdentite().getNomComplet());
            }
            commCourrier.setNomAN(param.getNomAN());
            commCourrier.setNomSenat(param.getNomSenat());
            commCourrier.setSignataireAdjointSgg(param.getNomDirecteurAdjointSGG());
            commCourrier.setSignataireSgg(param.getNomSGG());
        }

        setSignatures(session, commCourrier);

        commCourrier.setAuteur(getSTUserService().getUserFullName(actingUser.getModel()));
        commCourrier.setDateDuJour(SolonDateConverter.getClientConverter().formatNow());

        if (dossier != null) {
            commCourrier.setNor(dossier.getNumeroNor());
            commCourrier.setTitreActe(dossier.getTitreActe());
            EntiteNode min = STServiceLocator.getSTMinisteresService().getEntiteNode(dossier.getMinistereResp());
            commCourrier.setMinistereResponsableDossier(min.getLabel());
        }

        if (fiche != null) {
            setInfoFromFicheLoi(commCourrier, fiche, session);
        } else if (dossier != null) {
            DocumentModel ficheLoiDoc = SolonMgppServiceLocator
                .getDossierService()
                .findFicheLoiDocumentFromNor(session, dossier.getNumeroNor());
            setInfoFromFicheLoi(commCourrier, ficheLoiDoc, session);
        }

        LOGGER.info(session, STLogEnumImpl.LOG_INFO_TEC, "Fin d'initialisation des métadonnées du courrier");
        return commCourrier;
    }

    private void setSignatures(CoreSession session, CommunicationCourrier commCourrier) {
        DocumentModel tabRefDoc = getTableReferenceService().getTableReference(session);

        List<String> signatures = ofNullable(tabRefDoc)
            .map(d -> d.getAdapter(TableReference.class))
            .map(TableReference::getSignatureSGG)
            .orElse(new ArrayList<>());

        commCourrier.setSignatureSGG(
            signatures
                .stream()
                .map(signature -> getSTUserService().getUserInfo(signature, STUserService.CIVILITE_ABREGEE_PRENOM_NOM))
                .collect(Collectors.toList())
        );
    }

    private void setInfoFromFicheLoi(
        CommunicationCourrier commCourrier,
        DocumentModel ficheLoiDoc,
        CoreSession session
    ) {
        if (ficheLoiDoc != null) {
            FicheLoi ficheLoi = ficheLoiDoc.getAdapter(FicheLoi.class);

            if (StringUtils.isNotBlank(ficheLoi.getAssembleeDepot())) {
                commCourrier.setAssembleDepot(ResourceHelper.getString(ficheLoi.getAssembleeDepot()));
                commCourrier.setDestinataireCourrier(ResourceHelper.getString(ficheLoi.getAssembleeDepot()));
            }

            if (ficheLoi.getDateCM() != null) {
                commCourrier.setDateConseilMinistres(
                    SolonDateConverter.getClientConverter().format(ficheLoi.getDateCM())
                );
            }

            if (ficheLoi.getDateDepot() != null) {
                commCourrier.setDateDepotFicheLoi(
                    SolonDateConverter.getClientConverter().format(ficheLoi.getDateDepot())
                );
            }

            setInfoFromCommunication(commCourrier, ficheLoi.getIdDossier(), session);
            setInfoFromNavettes(commCourrier, ficheLoiDoc.getId(), session);
        }
    }

    private Map<Date, String> getOrderedTitle(HistoriqueDossierDTO historiqueDossier) {
        List<EvenementDTO> lstEvent = getAllPublishedEvenementDTO(historiqueDossier);

        return lstEvent
            .stream()
            .collect(
                Collectors.toMap(
                    this::getDateKeyFromEvenement,
                    this::getStringValueFromEvenement,
                    (title1, title2) -> title2,
                    TreeMap::new
                )
            );
    }

    private List<EvenementDTO> getAllPublishedEvenementDTO(HistoriqueDossierDTO historiqueDossierDTO) {
        List<EvenementDTO> lstEvent = new ArrayList<>(
            historiqueDossierDTO
                .getRootEvents()
                .values()
                .stream()
                .filter(this::isEvenementElligible)
                .collect(Collectors.toList())
        );

        lstEvent.addAll(
            historiqueDossierDTO
                .getMapSuivant()
                .values()
                .stream()
                .flatMap(List::stream)
                .filter(this::isEvenementElligible)
                .collect(Collectors.toList())
        );

        return lstEvent;
    }

    private boolean isEvenementElligible(EvenementDTO event) {
        return !EtatVersion.ABANDONNE.value().equals(event.getVersionCouranteEtat());
    }

    private Date getDateKeyFromEvenement(EvenementDTO event) {
        Date date = new Date();
        if (event != null && event.get("horodatage") != null) {
            date = (Date) event.get("horodatage");
        }

        return date;
    }

    private String getStringValueFromEvenement(EvenementDTO event) {
        return event == null ? "" : StringUtils.defaultString(event.getIntitule());
    }

    private void setInfoFromCommunication(CommunicationCourrier commCourrier, String idDossier, CoreSession session) {
        HistoriqueDossierDTO historiqueDossier = SolonMgppServiceLocator
            .getDossierService()
            .findDossier(idDossier, session);
        Map<Date, String> orderedTitles = getOrderedTitle(historiqueDossier);

        commCourrier.setIntitulePremiereCommunication(orderedTitles.values().stream().findFirst().orElse(null));
        commCourrier.setIntituleDerniereCommunication(
            new TreeMap<>(orderedTitles).descendingMap().values().stream().findFirst().orElse(null)
        );
    }

    private Stream<Navette> getFilteredStreamFromListNavetteDocs(
        List<DocumentModel> lstNavettesDocs,
        NiveauLectureCode code
    ) {
        return lstNavettesDocs
            .stream()
            .map(doc -> doc.getAdapter(Navette.class))
            .filter(nav -> code.name().equals(nav.getCodeLecture()));
    }

    private void setInfoFromNavettes(CommunicationCourrier commCourrier, String ficheLoiId, CoreSession session) {
        List<DocumentModel> navettesDocs = SolonMgppServiceLocator
            .getNavetteService()
            .fetchNavette(session, ficheLoiId);

        // Cas des CMPs
        getFilteredStreamFromListNavetteDocs(navettesDocs, NiveauLectureCode.CMP)
            .reduce((nav1, nav2) -> nav2)
            .ifPresent(
                navettesCMP -> {
                    if (StringUtils.isNotBlank(navettesCMP.getSortAdoptionAN())) {
                        commCourrier.setSortAdoptionAN(
                            SolonEpgServiceLocator
                                .getSolonEpgVocabularyService()
                                .getEntryLabel(
                                    VocabularyConstants.VOCABULARY_SORT_ADOPTION_DIRECTORY,
                                    navettesCMP.getSortAdoptionAN()
                                )
                        );
                    }
                }
            );

        // Cas nouvelle lecture de l'AN
        getFilteredStreamFromListNavetteDocs(navettesDocs, NiveauLectureCode.NOUVELLE_LECTURE_AN)
            .reduce((nav1, nav2) -> nav2)
            .ifPresent(
                navetteAN -> {
                    commCourrier.setAdopteLeNouvelleLectureAN(
                        SolonDateConverter.getClientConverter().format(navetteAN.getDateAdoption())
                    );
                    commCourrier.setRejeteLeNouvelleLectureAN(
                        SolonDateConverter.getClientConverter().format(navetteAN.getDateAdoption())
                    );
                    commCourrier.setTransmisLeNouvelleLectureAN(
                        SolonDateConverter.getClientConverter().format(navetteAN.getDateTransmission())
                    );
                }
            );

        // Cas nouvvelle lecture du Sénat
        getFilteredStreamFromListNavetteDocs(navettesDocs, NiveauLectureCode.NOUVELLE_LECTURE_SENAT)
            .reduce((nav1, nav2) -> nav2)
            .ifPresent(
                navetteSenat -> {
                    commCourrier.setAdopteLeNouvelleLectureSenat(
                        SolonDateConverter.getClientConverter().format(navetteSenat.getDateAdoption())
                    );
                    commCourrier.setRejeteLeNouvelleLectureSenat(
                        SolonDateConverter.getClientConverter().format(navetteSenat.getDateAdoption())
                    );
                    commCourrier.setTransmisLeNouvelleLectureSenat(
                        SolonDateConverter.getClientConverter().format(navetteSenat.getDateTransmission())
                    );
                }
            );

        List<Navette> lstNavetteSimpleAN = getFilteredStreamFromListNavetteDocs(navettesDocs, NiveauLectureCode.AN)
            .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(lstNavetteSimpleAN)) {
            commCourrier.setAdopteLe1ereLectureAN(
                SolonDateConverter.getClientConverter().format(lstNavetteSimpleAN.get(0).getDateAdoption())
            );
            if (lstNavetteSimpleAN.size() > 1 && lstNavetteSimpleAN.get(1).getDateAdoption() != null) {
                commCourrier.setAdopteLe2emeLectureAN(
                    SolonDateConverter.getClientConverter().format(lstNavetteSimpleAN.get(1).getDateAdoption())
                );
                commCourrier.setRejeteLe2emeLectureAN(
                    SolonDateConverter.getClientConverter().format(lstNavetteSimpleAN.get(1).getDateAdoption())
                );
            }

            commCourrier.setRejeteLe1ereLectureAN(
                SolonDateConverter.getClientConverter().format(lstNavetteSimpleAN.get(0).getDateAdoption())
            );

            commCourrier.setNavetteLe1ereLectureAN(
                SolonDateConverter.getClientConverter().format(lstNavetteSimpleAN.get(0).getDateNavette())
            );
        }

        List<Navette> lstNavetteSimpleSENAT = getFilteredStreamFromListNavetteDocs(
                navettesDocs,
                NiveauLectureCode.SENAT
            )
            .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(lstNavetteSimpleSENAT)) {
            commCourrier.setAdopteLe1ereLectureSenat(
                getClientConverter().format(lstNavetteSimpleSENAT.get(0).getDateAdoption())
            );

            commCourrier.setRejeteLe1ereLectureSenat(
                getClientConverter().format(lstNavetteSimpleSENAT.get(0).getDateAdoption())
            );

            commCourrier.setNavetteLe1ereLectureSenat(
                getClientConverter().format(lstNavetteSimpleSENAT.get(0).getDateNavette())
            );

            if (lstNavetteSimpleSENAT.size() > 1 && lstNavetteSimpleSENAT.get(1).getDateAdoption() != null) {
                commCourrier.setAdopteLe2emeLectureSenat(
                    getClientConverter().format(lstNavetteSimpleSENAT.get(1).getDateAdoption())
                );
                commCourrier.setRejeteLe2emeLectureSenat(
                    getClientConverter().format(lstNavetteSimpleSENAT.get(1).getDateAdoption())
                );
            }
        }
    }
}
