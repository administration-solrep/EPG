package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DATE_01_01_1900;
import static fr.dila.st.core.util.ObjectHelper.requireNonNullElse;

import com.google.common.base.Predicates;
import fr.dila.solonepg.api.administration.BulletinOfficiel;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.typescomplexe.DossierTransposition;
import fr.dila.solonepg.api.cases.typescomplexe.TranspositionsContainer;
import fr.dila.solonepg.api.constant.ActeVisibilityConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.dto.VecteurPublicationDTO;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.solonepg.core.cases.typescomplexe.DossierTranspositionImpl;
import fr.dila.solonepg.core.cases.typescomplexe.TranspositionsContainerImpl;
import fr.dila.solonepg.core.dto.activitenormative.VecteurPublicationDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.dossier.bordereau.BaseLegaleDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.BordereauSaveForm;
import fr.dila.solonepg.ui.bean.dossier.bordereau.ConseilEtatDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.DonneesIndexationDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.EpgBordereauDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.EpgDossierSimilaireBordereauDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.InformationsActeDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.ParutionDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.PeriodiciteDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.ResponsablesActeDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.TranspositionApplicationDTO;
import fr.dila.solonepg.ui.bean.dossier.bordereau.TranspositionApplicationDetailDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.bordereau.BordereauCourrierForm;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgBordereauUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.actions.SolonEpgActionServiceLocator;
import fr.dila.solonmgpp.api.node.MinistereNode;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.core.util.UnrestrictedCreateOrSaveDocumentRunner;
import fr.dila.st.ui.bean.DocumentDTO;
import fr.dila.st.ui.bean.DossierDTO;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.services.actions.STActionsServiceLocator;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.platform.usermanager.UserManager;

public class EpgBordereauUIServiceImpl implements EpgBordereauUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(EpgBordereauUIServiceImpl.class);

    private static final String OJ_URL = "oj";
    private static final String EURLEX_URL = " http://data.europa.eu/eli/dir/";

    private static final String TYPE_APPLICATION_LOI = "LOI";
    private static final String TYPE_APPLICATION_ORDONNANCE = "ORD";
    private static final String TYPE_APPLICATION_DIRECTIVE = "DIR";

    @Override
    public EpgBordereauDTO getBordereau(SpecificContext context) {
        EpgBordereauDTO bordereauDto = new EpgBordereauDTO();
        return fillBordereau(context, bordereauDto);
    }

    private EpgBordereauDTO fillBordereau(SpecificContext context, EpgBordereauDTO bordereauDto) {
        CoreSession session = context.getSession();
        NuxeoPrincipal principal = session.getPrincipal();
        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        String typeActe = dossier.getTypeActe();

        InformationsActeDTO infosDto = MapDoc2Bean.docToBean(dossierDoc, InformationsActeDTO.class);
        String statut = infosDto.getStatut();
        if (statut != null) {
            infosDto.setStatutLibelle(
                STServiceLocator
                    .getVocabularyService()
                    .getEntryLabel(VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME, statut)
            );
        }
        if (typeActe != null) {
            infosDto.setTypeActeLibelle(
                STServiceLocator
                    .getVocabularyService()
                    .getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY, typeActe)
            );
        }
        String categorieActe = infosDto.getCategorieActe();
        if (categorieActe != null) {
            infosDto.setCategorieActeLibelle(
                STServiceLocator
                    .getVocabularyService()
                    .getEntryLabel(VocabularyConstants.CATEGORY_ACTE_CONVENTION_COLLECTIVE, categorieActe)
            );
        }
        infosDto.setStatutArchivageLibelle(getLibelleStatutArchivage(dossier));
        infosDto.setIsEditableTypeActe(isEditableTypeActe(principal, dossier));
        infosDto.setIsVisibleCategorieActe(isVisibleCategorieActe(typeActe));
        infosDto.setIsVisiblePublicationRapport(isVisiblePublicationRapport(typeActe));

        boolean hasStepPublicationBO = SolonEpgActionServiceLocator
            .getEpgDocumentRoutingActionService()
            .hasStepPublicationBO(context);
        if (hasStepPublicationBO) {
            infosDto.setIsModifiableDateSignature(true);
        } else {
            infosDto.setIsModifiableDateSignature(isModifiableDateSignature(principal));
        }

        infosDto.setIsVisibleStatutArchivage(isVisibleStatutArchivage(dossier));
        infosDto.setIdBordereauVersement(getBordereauVersementId(context));
        bordereauDto.setInformationsActe(infosDto);

        ResponsablesActeDTO respDto = MapDoc2Bean.docToBean(dossierDoc, ResponsablesActeDTO.class);
        respDto.setIntituleMinistereResp(
            STServiceLocator.getSTMinisteresService().getEntiteNode(dossier.getMinistereResp()).getLabel()
        );
        respDto.setIntituleMinistereRattach(
            STServiceLocator.getSTMinisteresService().getEntiteNode(dossier.getMinistereAttache()).getLabel()
        );
        respDto.setIntituleDirectionResp(
            STServiceLocator
                .getSTUsAndDirectionService()
                .getUniteStructurelleNode(dossier.getDirectionResp())
                .getLabel()
        );
        respDto.setIntituleDirectionRattach(
            STServiceLocator
                .getSTUsAndDirectionService()
                .getUniteStructurelleNode(dossier.getDirectionAttache())
                .getLabel()
        );
        respDto.setCreeParLibelle(getCreeParLibelle(respDto.getCreePar()));
        respDto.setCreeParEmail(getCreeParMail(respDto.getCreePar()));
        bordereauDto.setResponsablesActe(respDto);

        ConseilEtatDTO conseilEtatDto = MapDoc2Bean.docToBean(dossierDoc, ConseilEtatDTO.class);
        String prioriteCE = conseilEtatDto.getPrioriteCE();
        if (StringUtils.isNotBlank(prioriteCE)) {
            conseilEtatDto.setPrioriteCELibelle(
                STServiceLocator.getVocabularyService().getEntryLabel(VocabularyConstants.VOC_PRIORITE, prioriteCE)
            );
        }
        conseilEtatDto.setIsEditableCE(isEditableCE(principal));
        bordereauDto.setConseilEtat(conseilEtatDto);

        ParutionDTO parutionDto = MapDoc2Bean.docToBean(dossierDoc, ParutionDTO.class);
        parutionDto.setVecteurPublicationLibelle(
            getVecteurPublicationDossier(session, parutionDto.getVecteurPublication())
                .stream()
                .map(VecteurPublicationDTO::getIntitule)
                .collect(Collectors.toList())
        );
        if (parutionDto.getModeParution() != null && session.exists(new IdRef(parutionDto.getModeParution()))) {
            parutionDto.setModeParutionLibelle(
                session.getDocument(new IdRef(parutionDto.getModeParution())).getAdapter(ModeParution.class).getMode()
            );
        }

        if (parutionDto.getDelaiPublication() != null) {
            parutionDto.setDelaiPublicationLibelle(
                STServiceLocator
                    .getVocabularyService()
                    .getEntryLabel(VocabularyConstants.DELAI_PUBLICATION, parutionDto.getDelaiPublication())
            );
            parutionDto.setIsDatePreciseeVisible(
                VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE.equals(parutionDto.getDelaiPublication())
            );
        }
        parutionDto.setIsModifiableZoneComSggDila(isModifiableZoneComSggDila(principal));
        if (CollectionUtils.isNotEmpty(parutionDto.getPublicationsConjointes())) {
            parutionDto.setMapPublicationConjointes(
                parutionDto
                    .getPublicationsConjointes()
                    .stream()
                    .collect(Collectors.toMap(Function.identity(), nor -> getIdDossier(session, nor)))
            );
        }
        if (parutionDto.getIntegraleOuExtraitPublication() != null) {
            parutionDto.setIntegraleOuExtraitPublicationLibelle(
                STServiceLocator
                    .getVocabularyService()
                    .getEntryLabel(VocabularyConstants.TYPE_PUBLICATION, parutionDto.getIntegraleOuExtraitPublication())
            );
        }
        bordereauDto.setParution(parutionDto);

        TranspositionApplicationDTO transpositionApplicationDto = getTranspositionApplicationDTO(dossier);
        bordereauDto.setTranspositionApplication(transpositionApplicationDto);

        BaseLegaleDTO baseLegale = MapDoc2Bean.docToBean(dossierDoc, BaseLegaleDTO.class);
        baseLegale.setIsVisibleBaseLegale(isVisibleBaseLegale(typeActe));
        String natureTexte = baseLegale.getNatureTexte();
        if (natureTexte != null) {
            baseLegale.setNatureTexteLibelle(
                STServiceLocator
                    .getVocabularyService()
                    .getEntryLabel(VocabularyConstants.BASE_LEGALE_NATURE_TEXTE_DIRECTORY_NAME, natureTexte)
            );
        }
        bordereauDto.setBaseLegale(baseLegale);

        DonneesIndexationDTO donneesIndexation = MapDoc2Bean.docToBean(dossierDoc, DonneesIndexationDTO.class);
        donneesIndexation.setIsVisibleTexteEntreprise(isVisibleTexteEntreprise(typeActe));
        bordereauDto.setDonneesIndexation(donneesIndexation);

        PeriodiciteDTO periodicite = MapDoc2Bean.docToBean(dossierDoc, PeriodiciteDTO.class);
        SolonEpgVocabularyService vocService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
        if (StringUtils.isNotEmpty(periodicite.getPeriodiciteRapport())) {
            periodicite.setPeriodiciteRapportLabel(
                vocService.getEntryLabel(
                    VocabularyConstants.PERIODICITE_RAPPORT_DIRECTORY_NAME,
                    periodicite.getPeriodiciteRapport()
                )
            );
        }
        bordereauDto.setPeriodicite(periodicite);

        bordereauDto.setIsEdit(
            STActionsServiceLocator.getDossierLockActionService().getCanUnlockCurrentDossier(context)
        );
        bordereauDto.setActeVisibility(getActeVisibility(typeActe));
        bordereauDto.setIsBaseLegaleEmpty(isBaseLegaleEmpty(dossier));

        return bordereauDto;
    }

    @Override
    public EpgDossierSimilaireBordereauDTO getBordereauSimilaire(SpecificContext context) {
        EpgDossierSimilaireBordereauDTO bordereauDto = new EpgDossierSimilaireBordereauDTO();
        return (EpgDossierSimilaireBordereauDTO) fillBordereau(context, bordereauDto);
    }

    private String getIdDossier(CoreSession session, String nor) {
        return Optional
            .ofNullable(SolonEpgServiceLocator.getNORService().getDossierIdFromNOR(session, nor))
            .orElse(StringUtils.EMPTY);
    }

    private boolean isEditableTypeActe(NuxeoPrincipal principal, Dossier dossier) {
        ActeService acteService = SolonEpgServiceLocator.getActeService();
        // on ne peut pas changer le type d'acte des dossiers publiés
        return (
            acteService.isDecret(dossier.getTypeActe()) &&
            principal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_TYPE_ACTE_DECRET_UPDATER) ||
            acteService.isArrete(dossier.getTypeActe()) &&
            principal.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_TYPE_ACTE_ARRETE_UPDATER) &&
            !VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut())
        );
    }

    private boolean isVisibleCategorieActe(String typeActeId) {
        return !SolonEpgServiceLocator.getActeService().isRapportAuParlement(typeActeId);
    }

    private boolean isVisibleStatutArchivage(Dossier dossier) {
        return !dossier.getStatutArchivage().equals(VocabularyConstants.STATUT_ARCHIVAGE_AUCUN);
    }

    private boolean isVisiblePublicationRapport(String typeActeId) {
        return SolonEpgServiceLocator.getActeService().isVisiblePublicationRapport(typeActeId);
    }

    /**
     * Retourne vrai si le champ "date signature" est modifiable
     *
     * @return boolean
     */
    public boolean isModifiableDateSignature(NuxeoPrincipal principal) {
        return principal.isMemberOf(SolonEpgBaseFunctionConstant.BORDEREAU_DATE_SIGNATURE_UPDATER);
    }

    @Override
    public boolean isVisibleCategorieActeConventionCollective(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        String typeActeId = dossier.getTypeActe();
        // On détermine si on affice ou pas 'Convention collective' dans la
        // liste déroulante du type d'acte
        // FEV 501
        List<String> typesActes = Arrays.asList(
            TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND,
            TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL,
            TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL,
            TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND,
            TypeActeConstants.TYPE_ACTE_ARRETE_PM,
            TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND,
            TypeActeConstants.TYPE_ACTE_ARRETE_PR,
            TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND,
            TypeActeConstants.TYPE_ACTE_ARRETE_CE,
            TypeActeConstants.TYPE_ACTE_AVIS
        );
        return typesActes.contains(typeActeId);
    }

    private String getLibelleStatutArchivage(Dossier dossier) {
        final Map<String, String> libelleStatutArchivage = VocabularyConstants.LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID;
        return libelleStatutArchivage.get(dossier.getStatutArchivage());
    }

    private Map<String, Boolean> getActeVisibility(String typeActe) {
        return SolonEpgServiceLocator.getActeService().getActeVisibility(typeActe);
    }

    private boolean isEditableCE(NuxeoPrincipal principal) {
        return principal.isMemberOf(SolonEpgBaseFunctionConstant.CONSEIL_ETAT_UPDATER);
    }

    private List<VecteurPublicationDTO> getVecteurPublicationDossier(CoreSession session, List<String> idsVecteur) {
        List<VecteurPublicationDTO> lstIntituleVecteurs = new ArrayList<>();
        for (String idVect : idsVecteur) {
            final IdRef docRef = new IdRef(idVect);
            try {
                // Si ça n'est pas un vecteur, c'est le libellé d'un bulletin
                if (session.exists(docRef)) {
                    DocumentModel doc = session.getDocument(docRef);
                    if (doc.hasSchema(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA)) {
                        VecteurPublication vecteur = doc.getAdapter(VecteurPublication.class);
                        lstIntituleVecteurs.add(new VecteurPublicationDTOImpl(vecteur));
                    } else if (doc.hasSchema(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA)) {
                        BulletinOfficiel bo = doc.getAdapter(BulletinOfficiel.class);
                        lstIntituleVecteurs.add(new VecteurPublicationDTOImpl(bo.getIntitule()));
                    }
                } else {
                    lstIntituleVecteurs.add(new VecteurPublicationDTOImpl(idVect));
                }
            } catch (NuxeoException e) {
                LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOCUMENT_FONC, idVect, e);
            }
        }

        return lstIntituleVecteurs;
    }

    /**
     * Retourne vrai si le champ "commentaire sgg dila" est modifiable
     *
     * @return boolean
     */
    private boolean isModifiableZoneComSggDila(NuxeoPrincipal principal) {
        return principal.isMemberOf(SolonEpgBaseFunctionConstant.ZONE_COMMENTAIRE_SGG_DILA_UPDATER);
    }

    @Override
    public List<SelectValueDTO> getVecteurPublicationList(SpecificContext context, List<String> vecteurPublication) {
        BulletinOfficielService bulletinService = SolonEpgServiceLocator.getBulletinOfficielService();
        List<SelectValueDTO> vecteurs = new ArrayList<>();
        CoreSession session = context.getSession();
        List<DocumentModel> allBulletinOrdered = bulletinService.getAllBulletinOfficielOrdered(
            context.getCurrentDocument(),
            session,
            vecteurPublication
        );
        List<DocumentModel> lstVecteurs = bulletinService.getAllActifVecteurPublication(session);
        SelectValueDTO vpJournalOff = null;
        String joIdLibelle = StringUtils.EMPTY;
        try {
            joIdLibelle = bulletinService.getLibelleForJO(session);
        } catch (NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOCUMENT_FONC, bulletinService, e);
        }

        // récupère la liste des vecteurs de publication
        for (DocumentModel doc : lstVecteurs) {
            VecteurPublication vect = doc.getAdapter(VecteurPublication.class);

            if (!joIdLibelle.equals(doc.getId())) {
                vecteurs.add(new SelectValueDTO(vect.getId(), vect.getIntitule()));
            } else {
                vpJournalOff = new SelectValueDTO(vect.getId(), vect.getIntitule());
            }
        }

        for (final DocumentModel docModel : allBulletinOrdered) {
            BulletinOfficiel bulletin = docModel.getAdapter(BulletinOfficiel.class);

            vecteurs.add(new SelectValueDTO(bulletin.getId(), bulletin.getIntitule()));
        }

        // Si un des vecteurs a un libellé à la place de l'id, on remplace l'id par un libellé dans les options
        ObjectHelper
            .requireNonNullElseGet(vecteurPublication, ArrayList<String>::new)
            .stream()
            .filter(id -> vecteurs.removeIf(vec -> id.equals(vec.getLabel())))
            .map(SelectValueDTO::new)
            .forEach(vecteurs::add);

        // On trie dans l'ordre alphabétique
        Collections.sort(vecteurs, Comparator.comparing(SelectValueDTO::getLabel));
        // on place la ligne du journal officiel en première position
        vecteurs.add(0, vpJournalOff);

        return vecteurs;
    }

    @Override
    public List<SelectValueDTO> getModesParution(CoreSession session) {
        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        return tableReferenceService
            .getModesParutionActifsList(session)
            .stream()
            .map(doc -> doc.getAdapter(ModeParution.class))
            .map(mp -> new SelectValueDTO(mp.getId(), mp.getMode()))
            .collect(Collectors.toList());
    }

    private boolean isBaseLegaleEmpty(Dossier dossier) {
        return (
            dossier != null &&
            StringUtils.isEmpty(dossier.getBaseLegaleActe()) &&
            dossier.getBaseLegaleDate() == null &&
            StringUtils.isEmpty(dossier.getBaseLegaleNatureTexte()) &&
            StringUtils.isEmpty(dossier.getBaseLegaleNumeroTexte())
        );
    }

    private boolean isVisibleBaseLegale(String typeActeId) {
        return SolonEpgServiceLocator.getActeService().isVisibleBaseLegale(typeActeId);
    }

    public boolean isVisibleTexteEntreprise(String typeActeId) {
        // On détermine si on affice ou pas 'Texte entreprise' dans le bordereau
        // - FEV547
        List<String> typesActeTexteEntreprise = Arrays.asList(
            TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND,
            TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL,
            TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL,
            TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND,
            TypeActeConstants.TYPE_ACTE_ARRETE_PM,
            TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND,
            TypeActeConstants.TYPE_ACTE_ARRETE_PR,
            TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND,
            TypeActeConstants.TYPE_ACTE_ARRETE_CE,
            TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37,
            TypeActeConstants.TYPE_ACTE_DECRET_CE,
            TypeActeConstants.TYPE_ACTE_DECRET_CE_IND,
            TypeActeConstants.TYPE_ACTE_DECRET_CE_CM,
            TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND,
            TypeActeConstants.TYPE_ACTE_DECRET_CM,
            TypeActeConstants.TYPE_ACTE_DECRET_CM_IND,
            TypeActeConstants.TYPE_ACTE_DECRET_PR,
            TypeActeConstants.TYPE_ACTE_DECRET_PR_IND,
            TypeActeConstants.TYPE_ACTE_DECRET_PR_CE,
            TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD,
            TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE,
            TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND,
            TypeActeConstants.TYPE_ACTE_ORDONNANCE
        );

        return typesActeTexteEntreprise.contains(typeActeId);
    }

    private String getCreeParLibelle(String userName) {
        UserManager userManager = STServiceLocator.getUserManager();
        DocumentModel userDoc = userManager.getUserModel(userName);
        return Optional
            .ofNullable(userDoc)
            .map(doc -> doc.getAdapter(STUser.class))
            .map(this::getCreeParUserInfo)
            .orElse(userName);
    }

    private String getCreeParUserInfo(STUser user) {
        return Stream
            .of(user.getTitle(), user.getFirstName(), user.getLastName(), user.getTelephoneNumber())
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.joining(" "));
    }

    private String getCreeParMail(String userName) {
        UserManager userManager = STServiceLocator.getUserManager();
        DocumentModel userDoc = userManager.getUserModel(userName);
        return Optional.ofNullable(userDoc).map(doc -> doc.getAdapter(STUser.class)).map(STUser::getEmail).orElse("");
    }

    private TranspositionApplicationDTO getTranspositionApplicationDTO(Dossier dossier) {
        TranspositionApplicationDTO dto = MapDoc2Bean.docToBean(
            dossier.getDocument(),
            TranspositionApplicationDTO.class
        );

        if (dossier.getTranspositionDirective() != null) {
            dto.setTranspositionDirectives(
                dossier
                    .getTranspositionDirective()
                    .getTranspositions()
                    .stream()
                    .map(transposition -> mapTranspositionApplication(transposition, true, false))
                    .collect(Collectors.toList())
            );
        } else {
            dto.setTranspositionDirectives(
                Arrays.asList(new TranspositionApplicationDetailDTO(TYPE_APPLICATION_DIRECTIVE))
            );
        }
        if (dossier.getTranspositionOrdonnance() != null || dossier.getApplicationLoi() != null) {
            dto.setApplicationLoisOrdonnances(
                dossier
                    .getTranspositionOrdonnance()
                    .getTranspositions()
                    .stream()
                    .map(transposition -> mapTranspositionApplication(transposition, false, true))
                    .collect(Collectors.toList())
            );
            dto
                .getApplicationLoisOrdonnances()
                .addAll(
                    dossier
                        .getApplicationLoi()
                        .getTranspositions()
                        .stream()
                        .map(transposition -> mapTranspositionApplication(transposition, false, false))
                        .collect(Collectors.toList())
                );
        } else {
            dto.setApplicationLoisOrdonnances(
                Arrays.asList(new TranspositionApplicationDetailDTO(TYPE_APPLICATION_LOI))
            );
        }

        return dto;
    }

    private TranspositionApplicationDetailDTO mapTranspositionApplication(
        DossierTransposition transposition,
        boolean isDirective,
        boolean isOrdonnance
    ) {
        TranspositionApplicationDetailDTO dto = new TranspositionApplicationDetailDTO();
        String ref = transposition.getRef();
        Integer annee = null;
        if (isDirective) {
            String[] refDirective = StringUtils.split(ref, "/");
            if (CollectionUtils.size(refDirective) == 2) {
                annee = Integer.parseInt(refDirective[0]);
                ref = refDirective[1];
            }
            dto.setType(TYPE_APPLICATION_DIRECTIVE);
        } else if (isOrdonnance) {
            dto.setType(TYPE_APPLICATION_ORDONNANCE);
        } else {
            dto.setType(TYPE_APPLICATION_LOI);
        }
        dto.setCommentaire(transposition.getCommentaire());
        dto.setNumeroArticle(transposition.getNumeroArticles());
        dto.setNumeroOrdre(transposition.getRefMesure());
        dto.setReference(ref);
        dto.setTitre(transposition.getTitre());
        dto.setAnnee(annee);

        return dto;
    }

    @Override
    public void saveBordereau(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        CoreSession session = context.getSession();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        List<String> publicationsConjointesOld = new ArrayList<>(dossier.getPublicationsConjointes());

        BordereauSaveForm form = context.getFromContextData(EpgContextDataKey.BORDEREAU_SAVE_FORM);
        if (!VocabularyConstants.STATUT_NOR_ATTRIBUE.equals(dossier.getStatut())) {
            MapDoc2Bean.beanToDoc(form, dossierDoc);

            checkBordereauBeforeSaving(context, form);

            if (CollectionUtils.isNotEmpty(context.getMessageQueue().getErrorQueue())) {
                return;
            }

            final DossierBordereauService dbs = SolonEpgServiceLocator.getDossierBordereauService();
            // on logge les modifications effectuées sur le bordereau.
            dbs.logAllDocumentUpdate(session, dossierDoc);

            // on regarde si les modifications ont eu lieu sur les applications des
            // lois, les transpositions ou les
            // ordonnances
            // afin de mettre à jour l'historique des maj ministérielles
            SolonEpgServiceLocator.getHistoriqueMajMinisterielleService().registerMajDossier(session, dossierDoc);

            final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
            // mise à jour du dossier si le type d'acte change
            Boolean typeActeUpdated = dossierService.updateDossierWhenTypeActeUpdated(dossierDoc, session);

            LOGGER.info(session, STLogEnumImpl.SAVE_DOSSIER_FONC, dossierDoc);

            dossierDoc = session.saveDocument(dossierDoc);
            // Event de rattachement de l'activite normative (post commit)
            final EventProducer eventProducer = STServiceLocator.getEventProducer();

            final Map<String, Serializable> eventProperties = new HashMap<>();
            eventProperties.put(
                SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_DOSSIER_DOCID_PARAM,
                dossier.getDocument().getId()
            );
            final InlineEventContext inlineEventContext = new InlineEventContext(
                session,
                session.getPrincipal(),
                eventProperties
            );
            eventProducer.fireEvent(
                inlineEventContext.newEvent(SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_EVENT)
            );
            // mise à jour du dossier de la fiche dr dans le cas de rapport au
            // parlement et màj du de la fiche loi dans le
            // cas des types d'actes de type loi
            ActeService acteService = SolonEpgServiceLocator.getActeService();

            String typeActe = dossier.getTypeActe();
            if (BooleanUtils.isTrue(acteService.isRapportAuParlement(typeActe)) || acteService.isLoi(typeActe)) {
                final Map<String, Serializable> eventPropertiesFicheDR = new HashMap<>();
                eventPropertiesFicheDR.put(SolonEpgEventConstant.DOSSIER_EVENT_PARAM, dossier.getDocument());

                if (BooleanUtils.isTrue(acteService.isRapportAuParlement(typeActe))) {
                    eventPropertiesFicheDR.put(
                        SolonEpgEventConstant.TYPE_ACTE_EVENT_PARAM,
                        SolonEpgEventConstant.TYPE_ACTE_DR_EVENT_VALUE
                    );
                } else if (acteService.isLoi(typeActe)) {
                    eventPropertiesFicheDR.put(
                        SolonEpgEventConstant.TYPE_ACTE_EVENT_PARAM,
                        SolonEpgEventConstant.TYPE_ACTE_FL_EVENT_VALUE
                    );
                }

                final InlineEventContext inlineEventContextFP = new InlineEventContext(
                    session,
                    session.getPrincipal(),
                    eventPropertiesFicheDR
                );
                eventProducer.fireEvent(
                    inlineEventContextFP.newEvent(SolonEpgEventConstant.BATCH_EVENT_UPDATE_FICHE_AFTER_UPDATE_BORDEREAU)
                );
            }
            // Propagation des modifications aux publications conjointes du dossier
            List<String> publications = getPublicationsConjointes(dossierDoc);

            List<String> publicationsToRemove = ListUtils.removeAll(
                publicationsConjointesOld,
                getPublicationsConjointesList(form.getPublicationsConjointes())
            );
            publicationsToRemove.forEach(p -> dbs.removePublicationConjointe(session, dossier, p));
            // Récupération des données à propager
            // Délai de publication
            final String delaiPublication = dossier.getDelaiPublication();

            // Publication à date précisée
            final Calendar datePreciseePublication = dossier.getDatePreciseePublication();
            // Mode de parution
            final RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
            final String modeParution = retourDila.getModeParution();
            // Parcours des publications conjointes du dossier courrant
            final NORService norService = SolonEpgServiceLocator.getNORService();
            List<String> allPublications = new ArrayList<>(publications);
            if (CollectionUtils.isNotEmpty(allPublications)) {
                allPublications.add(dossier.getNumeroNor());
            }
            for (String pubNor : publications) {
                DocumentModel pubDossierDoc = norService.getDossierFromNOR(
                    session,
                    pubNor,
                    RetourDilaConstants.RETOUR_DILA_SCHEMA,
                    STSchemaConstant.DUBLINCORE_SCHEMA,
                    STSchemaConstant.COMMON_SCHEMA
                );
                if (pubDossierDoc == null) {
                    String message = ResourceHelper.getString(
                        "feedback.solonepg.document.bordereau.publicationconjointe",
                        pubNor
                    );
                    context.getMessageQueue().addErrorToQueue(message);
                    allPublications.removeIf(pubNor::equals);
                    continue;
                }
                Dossier pubDossierConjoint = pubDossierDoc.getAdapter(Dossier.class);
                List<String> publicationsToBeAdded = allPublications
                    .stream()
                    .filter(Predicates.not(pubNor::equals))
                    .collect(Collectors.toList());
                pubDossierConjoint.setPublicationsConjointes(publicationsToBeAdded);
                dbs.propagatePublicationData(
                    pubDossierConjoint,
                    delaiPublication,
                    datePreciseePublication,
                    modeParution
                );
                new UnrestrictedCreateOrSaveDocumentRunner(session).saveDocument(pubDossierConjoint.getDocument());
            }

            // FEV509 - On ne déverouille plus le dossier lorsqu'on enregistre le
            // bordereau

            context
                .getMessageQueue()
                .addToastSuccess(
                    ResourceHelper.getString(
                        BooleanUtils.isTrue(typeActeUpdated)
                            ? "dossier.bordereau.sauvegarde.type.acte.modifie"
                            : "dossier.bordereau.sauvegarde"
                    )
                );
        } else {
            // Si dossier au statut NOR Attribué on ne sauvegarde que les données d'indexation
            dossier.setIndexationRubrique(form.getRubriques());
            dossier.setIndexationMotsCles(form.getMotsCles());
            dossier.setIndexationChampLibre(form.getChampsLibres());
            session.saveDocument(dossierDoc);

            context.getMessageQueue().addToastSuccess(ResourceHelper.getString("dossier.bordereau.sauvegarde"));
        }
    }

    private List<String> getPublicationsConjointesList(String publicationsConjointes) {
        return Stream
            .of(requireNonNullElse(publicationsConjointes, "").split(","))
            .map(String::trim)
            .filter(StringUtils::isNotEmpty)
            .collect(Collectors.toList());
    }

    private void checkBordereauBeforeSaving(SpecificContext context, BordereauSaveForm form) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        CoreSession session = context.getSession();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        List<String> publicationsConjointesList = Optional
            .ofNullable(form.getPublicationsConjointes())
            .map(this::getPublicationsConjointesList)
            .orElseGet(ArrayList::new);
        dossier.setPublicationsConjointes(publicationsConjointesList);
        List<Calendar> datesEffetRubriqueEntreprise = ObjectHelper
            .requireNonNullElseGet(form.getDatesEffetRubriqueEntreprise(), ArrayList::new)
            .stream()
            .map(String.class::cast)
            .map(SolonDateConverter.DATE_SLASH::parseToCalendar)
            .collect(Collectors.toList());
        dossier.setDateEffet(datesEffetRubriqueEntreprise);
        if (form.getTranspositionDirectives() == null) {
            dossier.setTranspositionDirective(new TranspositionsContainerImpl());
        } else {
            dossier.setTranspositionDirective(mapTranspositionDtoToContainer(form.getTranspositionDirectives()));
        }
        if (form.getApplicationLoiOrdonnances() == null) {
            dossier.setApplicationLoi(new TranspositionsContainerImpl());
            dossier.setTranspositionOrdonnance(new TranspositionsContainerImpl());
        } else {
            dossier.setApplicationLoi(mapLoiOrdonnanceToDto(form, TYPE_APPLICATION_LOI));
            dossier.setTranspositionOrdonnance(mapLoiOrdonnanceToDto(form, TYPE_APPLICATION_ORDONNANCE));
        }

        LOGGER.info(context.getSession(), STLogEnumImpl.UPDATE_DOSSIER_FONC, dossierDoc);
        // vérification des champs si le dossier est lancé
        if (
            STDossier.DossierState.running.toString().equals(dossierDoc.getCurrentLifeCycleState()) &&
            !isBordereauComplet(dossierDoc, form)
        ) {
            final DossierBordereauService dbs = SolonEpgServiceLocator.getDossierBordereauService();
            String donneesManquantes = dbs.getBordereauMetadonnesObligatoiresManquantes(dossierDoc);
            context
                .getMessageQueue()
                .addErrorToQueue(
                    ResourceHelper.getString("bordereau.champs.obligatoires.manquants", donneesManquantes)
                );
        }

        workaroundOra00060Deadlock(session, dossierDoc);

        //Vérifier que les publications conjointes ne contiennent pas des points virgules
        String pubConjointesFromForm = form.getPublicationsConjointes();
        String[] pubConjointesSplitted = pubConjointesFromForm.split(";");

        if(pubConjointesSplitted.length > 1){
            context
                    .getMessageQueue()
                    .addErrorToQueue(
                            ResourceHelper.getString("feedback.solonepg.document.bordereau.publicationconjointeAvecPointVirgule")
                    );
        }

        // vérifier que la date de signature est valide: > 01/01/1900
        Calendar dateSignature = dossier.getDateSignature();
        if (dateSignature != null) {
            Calendar dateCreation = dossier.getDateCreation();

            if (dateSignature.before(DATE_01_01_1900) || dateSignature.after(Calendar.getInstance())) {
                context
                    .getMessageQueue()
                    .addErrorToQueue(
                        ResourceHelper.getString("feedback.solonepg.document.bordereau.dateSignatureIncorrecte")
                    );
            } else if (dateSignature.before(dateCreation) && !DateUtils.isSameDay(dateSignature, dateCreation)) {
                context
                    .getMessageQueue()
                    .addWarnToQueue(
                        ResourceHelper.getString("feedback.solonepg.document.bordereau.dateSignature.warning")
                    );
            }
        }

        // Message erreur si texte entreprise et aucune date d'effet renseignée
        if (BooleanUtils.isTrue(dossier.getTexteEntreprise()) && dossier.getDateEffet().isEmpty()) {
            context
                .getMessageQueue()
                .addErrorToQueue(
                    ResourceHelper.getString("feedback.solonepg.bordereau.date.effet.obligatoire.si.texte.entreprise")
                );
        }
        // suppression du champ "publication date précisée" si le délai n'est
        // pas "a date précisée"
        if (!"1".equals(dossier.getDelaiPublication())) {
            LOGGER.info(
                session,
                STLogEnumImpl.UPDATE_DOSSIER_FONC,
                ResourceHelper.getString("dossier.bordereau.suppression.date.publication.precisee")
            );
            dossier.setDatePreciseePublication(null);
        }
        // periodicite seulement dans le cas où PeriodiciteRapport = periodique
        if (!DossierSolonEpgConstants.RAPPORT_PERIODIQUE_PERIODICITE_ID.equals(dossier.getPeriodiciteRapport())) {
            dossier.setPeriodicite(null);
        }
    }

    /**
     * M158580 - ORA-00060 workaround La mise à jour d'un dossier avec
     * publication conjointe met à jour le dossier et les champs
     * delaiPublication, DatePreciseePublication et modeParution des dossiers en
     * publication conjointe.
     * <p>
     * Cela peut entrainer un deadlock en cas de modification simultanée car on
     * aura pour une modification de A et B en modification conjointe.
     * <p>
     * saveBordereau(A): modif. A (lock) -> ... B (deadlock) -> ...
     * saveBordereau(B): modif. B (lock) -> ... A (deadlock) -> ...
     * <p>
     * Le contournement mis en oeuvre consiste à provoquer la modification de
     * dc:modified sur le dossier et ses publications conjointes dans un ordre
     * déterministe.
     * <p>
     * saveBordereau(A): dc:modified A (lock) -> ... dc:modified B -> ... ;
     * modif. A -> ... B -> ... (release all) saveBordereau(B): dc:modified A
     * (wait) -> ... dc:modified B -> ... ; modif. B -> ... A -> ... (release
     * all)
     * <p>
     * Pour que le contournement fonctionne, il ne faut pas que l'appel à
     * saveBordereau(...) soit précédé d'un session.save(...) qui déclenche le
     * lock.
     *
     * @param dossierDoc
     * @param session
     */
    private void workaroundOra00060Deadlock(CoreSession session, DocumentModel dossierDoc) {
        final NORService norService = SolonEpgServiceLocator.getNORService();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        List<String> publicationsConjointesWa = getPublicationsConjointes(dossierDoc);
        if (dossier.getNumeroNor() != null) {
            publicationsConjointesWa.add(dossier.getNumeroNor());
        }
        Collections.sort(publicationsConjointesWa);
        for (String pubNor : publicationsConjointesWa) {
            DocumentModel pubDossierDoc = norService.getDossierFromNOR(
                session,
                pubNor,
                RetourDilaConstants.RETOUR_DILA_SCHEMA,
                STSchemaConstant.DUBLINCORE_SCHEMA,
                STSchemaConstant.COMMON_SCHEMA
            );
            if (pubDossierDoc != null) {
                Dossier pubDossierConjoint = pubDossierDoc.getAdapter(Dossier.class);
                DublincoreSchemaUtils.setModifiedDate(pubDossierDoc, Calendar.getInstance());
                if (pubNor.equals(dossier.getNumeroNor())) {
                    // unrestricted n'est pas nécessaire sur le dossier cible
                    session.saveDocument(pubDossierDoc);
                } else {
                    new UnrestrictedCreateOrSaveDocumentRunner(session).saveDocument(pubDossierConjoint.getDocument());
                }
            }
        }
    }

    public boolean isBordereauComplet(DocumentModel dossierDoc, BordereauSaveForm form) {
        final ActeService acteService = SolonEpgServiceLocator.getActeService();
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final String typeActeId = dossier.getTypeActe();
        final Map<String, Boolean> mapVisibility = acteService.getActeVisibility(typeActeId);

        boolean isNotEmptyPublication =
            !mapVisibility.get(ActeVisibilityConstants.PUBLICATION) || isNotEmptyPublication(form);
        boolean areNotEmptyRubriques =
            !dossier.getTexteEntreprise() ||
            !mapVisibility.get(ActeVisibilityConstants.INDEXATION) ||
            CollectionUtils.isNotEmpty(form.getRubriques());
        boolean isNotEmptyBaseLegale =
            !isRapportAuParlement(dossierDoc) || StringUtils.isNotEmpty(form.getBaseLegale());
        boolean isNotEmptyDateEffetTexteEntreprise =
            !dossier.getTexteEntreprise() || CollectionUtils.isNotEmpty(dossier.getDateEffet());

        return (
            StringUtils.isNoneEmpty(form.getTitreActe(), form.getNomResponsable(), form.getQualiteResponsable()) &&
            isNotEmptyPublication &&
            areNotEmptyRubriques &&
            isNotEmptyBaseLegale &&
            isNotEmptyDateEffetTexteEntreprise
        );
    }

    private Boolean isRapportAuParlement(DocumentModel currentDoc) {
        if (currentDoc != null && currentDoc.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
            Dossier dossier = currentDoc.getAdapter(Dossier.class);
            if (dossier != null) {
                ActeService acteService = SolonEpgServiceLocator.getActeService();
                return acteService.isRapportAuParlement(dossier.getTypeActe());
            }
        }
        return Boolean.FALSE;
    }

    private Boolean isNotEmptyPublication(BordereauSaveForm form) {
        return (
            CollectionUtils.isNotEmpty(form.getVecteurPublication()) ||
            VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE.equals(form.getDelaiPublication()) &&
            form.getDatePreciseePublication() == null
        );
    }

    public List<String> getPublicationsConjointes(DocumentModel dossierDoc) {
        final List<String> list = new ArrayList<>();
        if (dossierDoc != null) {
            final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            list.addAll(dossier.getPublicationsConjointes());
        }
        return list;
    }

    private DossierTransposition mapTranspositionDtoToDossierTransposition(TranspositionApplicationDetailDTO dto) {
        DossierTransposition transposition = new DossierTranspositionImpl();
        String ref = Optional.ofNullable(dto.getReference()).orElse(StringUtils.EMPTY);
        Integer annee = dto.getAnnee();
        if (annee != null) {
            ref = annee + "/" + ref;
        }
        transposition.setCommentaire(Optional.ofNullable(dto.getCommentaire()).orElse(StringUtils.EMPTY));
        transposition.setNumeroArticles(Optional.ofNullable(dto.getNumeroArticle()).orElse(StringUtils.EMPTY));
        transposition.setRef(ref);
        transposition.setRefMesure(Optional.ofNullable(dto.getNumeroOrdre()).orElse(StringUtils.EMPTY));
        transposition.setTitre(Optional.ofNullable(dto.getTitre()).orElse(StringUtils.EMPTY));
        return transposition;
    }

    private TranspositionsContainer mapTranspositionDtoToContainer(List<TranspositionApplicationDetailDTO> dtos) {
        return new TranspositionsContainerImpl(
            dtos.stream().map(this::mapTranspositionDtoToDossierTransposition).collect(Collectors.toList())
        );
    }

    @Override
    public String getTitreDirectiveFromEurlex(SpecificContext context) {
        TranspositionApplicationDetailDTO directive = context.getFromContextData(
            EpgContextDataKey.TRANSPOSITION_APPLICATION_DETAIL
        );

        String titre = directive.getTitre();
        Integer annee = directive.getAnnee();
        String reference = directive.getReference();
        String titreDirective = SolonEpgServiceLocator
            .getTranspositionDirectiveService()
            .findDirectiveEurlexWS(reference, annee, titre);

        if (titreDirective == null) {
            // Erreur -> Directive non trouvée
            context
                .getMessageQueue()
                .addInfoToQueue(
                    ResourceHelper.getString("dossier.bordereau.directive.inexistante", annee, reference),
                    "add-directive"
                );
            if (StringUtils.isNotBlank(titre)) {
                titreDirective = titre + getDirectiveUrl(annee, reference);
            }
        } else {
            titreDirective += getDirectiveUrl(annee, reference);
        }
        return titreDirective;
    }

    private String getDirectiveUrl(Integer annee, String reference) {
        return EURLEX_URL + annee + '/' + reference + '/' + OJ_URL;
    }

    private String getBordereauVersementId(SpecificContext context) {
        DossierDTO dossierDto = EpgUIServiceLocator.getEpgFondDeDossierUIService().getBordereauVersementDTO(context);
        return dossierDto.getLstDocuments().stream().map(DocumentDTO::getId).findFirst().orElse(null);
    }

    @Override
    public List<SelectValueDTO> getTypeActeOptions(SpecificContext context) {
        List<SelectValueDTO> typeActeOptions = EpgUIServiceLocator.getEpgSelectValueUIService().getTypeActe();
        ActeService acteService = SolonEpgServiceLocator.getActeService();
        String typeActe = context.getCurrentDocument().getAdapter(Dossier.class).getTypeActe();
        return typeActeOptions
            .stream()
            .filter(
                option ->
                    isNeitherArreteNorDecret(acteService, typeActe) ||
                    filterDecrets(context, acteService, typeActe, option) ||
                    filterArretes(acteService, typeActe, option)
            )
            .sorted(Comparator.comparing(SelectValueDTO::getLabel))
            .collect(Collectors.toList());
    }

    private boolean isNeitherArreteNorDecret(ActeService acteService, String typeActe) {
        return !acteService.isDecret(typeActe) && !acteService.isArrete(typeActe);
    }

    private boolean filterArretes(ActeService acteService, String typeActe, SelectValueDTO option) {
        return acteService.isArrete(option.getKey()) && acteService.isArrete(typeActe);
    }

    private boolean filterDecrets(
        SpecificContext context,
        ActeService acteService,
        String typeActe,
        SelectValueDTO option
    ) {
        return (
            acteService.isDecret(option.getKey()) &&
            acteService.isDecret(typeActe) &&
            SolonEpgServiceLocator
                .getTypeActeService()
                .isDecretCMIndividuelDisplayed(context.getSession(), option.getKey())
        );
    }

    @Override
    public BordereauCourrierForm getBordereauEtCourrier(SpecificContext context) {
        Dossier doss = context.getCurrentDocument().getAdapter(Dossier.class);
        BordereauCourrierForm form = new BordereauCourrierForm();
        try {
            EntiteNode ministereEpg = STServiceLocator
                .getOrganigrammeService()
                .getOrganigrammeNodeById(doss.getMinistereResp(), OrganigrammeType.MINISTERE);
            MinistereNode node = SolonMgppServiceLocator
                .getTableReferenceService()
                .getMinistereNode(
                    context.getSession(),
                    ministereEpg.getFormule(),
                    ministereEpg.getLabel(),
                    ministereEpg.getLabel(),
                    ministereEpg.getEdition()
                );

            if (node != null) {
                form.setAuteur(node.getLibelle());
            }
        } catch (EPGException e) {
            LOGGER.error(context.getSession(), EpgLogEnumImpl.FAIL_GET_WS_EPP_TEC, e);
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("tableref.epp.error"));
        }

        return form;
    }

    private TranspositionsContainer mapLoiOrdonnanceToDto(BordereauSaveForm form, String type) {
        return mapTranspositionDtoToContainer(
            form
                .getApplicationLoiOrdonnances()
                .stream()
                .filter(app -> type.equals(app.getType()))
                .collect(Collectors.toList())
        );
    }
}
