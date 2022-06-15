package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.cases.typescomplexe.InfoHistoriqueAmpliation;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants;
import fr.dila.solonepg.api.constant.SolonEpgTraitementPapierConstants;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.solonepg.api.documentmodel.ListeTraitementPapier;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.AmpliationService;
import fr.dila.solonepg.api.service.ListeTraitementPapierService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.service.TraitementPapierService;
import fr.dila.solonepg.api.service.vocabulary.DelaiPublicationService;
import fr.dila.solonepg.api.service.vocabulary.TypeAvisTraitementPapierService;
import fr.dila.solonepg.api.service.vocabulary.TypeSignataireTraitementPapierService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.solonepg.core.cases.typescomplexe.InfoEpreuveImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.ampliation.AmpliationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.ampliation.HistoriqueAmpliation;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication.CommunicationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication.DestinataireCommunicationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.epreuve.BackEpreuvesDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.epreuve.EpreuveDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.publication.PublicationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.reference.ComplementDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.retour.RetourDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.signature.ListeSignatureDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.signature.SignatureDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgTraitementPapierUIService;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.SSBirtUIService;
import fr.dila.ss.ui.services.SSUIServiceLocator;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.BlobUtils;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.LockUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.platform.usermanager.UserManager;

public class EpgTraitementPapierUIServiceImpl implements EpgTraitementPapierUIService {
    private static final String SIGNATAIRE_FONCTION_PARAM = "SIGNATAIRE_FONCTION_PARAM";
    private static final String SIGNATAIRE_PARAM = "SIGNATAIRE_PARAM";
    private static final String DOSSIERID_PARAM = "DOSSIERID_PARAM";

    @Override
    public void saveCommunicationDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        CommunicationDTO communicationDTO = Objects.requireNonNull(
            context.getFromContextData(EpgContextDataKey.TRAITEMENT_PAPIER_COMMUNICATION)
        );

        MapDoc2Bean.beanToDoc(communicationDTO, dossierDoc);
        saveTraitementPapier(context);
    }

    @Override
    public void saveComplementDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        ComplementDTO complementDTO = Objects.requireNonNull(
            context.getFromContextData(EpgContextDataKey.TRAITEMENT_PAPIER_REFERENCE),
            "un complement dto est requis"
        );

        MapDoc2Bean.beanToDoc(complementDTO, dossierDoc);
        saveTraitementPapier(context);
    }

    @Override
    public void savePublicationDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        PublicationDTO publicationDTO = Objects.requireNonNull(
            context.getFromContextData(EpgContextDataKey.TRAITEMENT_PAPIER_PUBLICATION),
            "un publication dto est requis"
        );

        MapDoc2Bean.beanToDoc(publicationDTO, dossierDoc);
        saveTraitementPapier(context);
    }

    @Override
    public void saveSignatureDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        SignatureDTO signatureDTO = Objects.requireNonNull(
            context.getFromContextData(EpgContextDataKey.TRAITEMENT_PAPIER_SIGNATURE),
            "un signature dto est requis"
        );

        MapDoc2Bean.beanToDoc(signatureDTO, dossierDoc);
        saveTraitementPapier(context);
    }

    @Override
    public void saveRetourDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        RetourDTO retourDTO = Objects.requireNonNull(
            context.getFromContextData(EpgContextDataKey.TRAITEMENT_PAPIER_RETOUR),
            "un retour dto est requis"
        );

        MapDoc2Bean.beanToDoc(retourDTO, dossierDoc);
        saveTraitementPapier(context);
    }

    @Override
    public void saveEpreuveDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        EpreuveDTO epreuveDTO = Objects.requireNonNull(
            context.getFromContextData(EpgContextDataKey.TRAITEMENT_PAPIER_EPREUVE),
            "un epreuve dto est requis"
        );

        MapDoc2Bean.beanToDoc(new BackEpreuvesDTO(epreuveDTO), dossierDoc);
        saveTraitementPapier(context);
    }

    @Override
    public boolean canEditTabs(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();
        NuxeoPrincipal principal = session.getPrincipal();
        return hasPermission(principal) && LockUtils.isLockedByCurrentUser(session, dossierDoc.getRef());
    }

    private boolean hasPermission(NuxeoPrincipal principal) {
        return principal.isMemberOf(SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_WRITER);
    }

    private void saveTraitementPapier(SpecificContext context) {
        CoreSession session = context.getSession();

        if (!hasPermission(session.getPrincipal())) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("dossier.traitement.papier.message.save.no.permission"));
            return;
        }

        DocumentModel dossierDoc = context.getCurrentDocument();
        if (!LockUtils.isLockedByCurrentUser(session, dossierDoc.getRef())) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString("epg.dossier.lock.not.locked"));
            return;
        }

        final TraitementPapierService traitementPapierService = SolonEpgServiceLocator.getTraitementPapierService();
        traitementPapierService.saveTraitementPapier(session, dossierDoc);
        context.getMessageQueue().addInfoToQueue(ResourceHelper.getString("dossier.traitement.papier.message.save.ok"));

        ListeTraitementPapierService listeTraitementPapierService = SolonEpgServiceLocator.getListeTraitementPapierService();
        listeTraitementPapierService.updateFichePresentationAfterUpdateBordereau(session, dossierDoc);
    }

    @Override
    public ComplementDTO getComplementDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        ComplementDTO complementDTO = MapDoc2Bean.docToBean(dossierDoc, ComplementDTO.class);

        TypeSignataireTraitementPapierService typeSignataireTraitementPapierService = SolonEpgServiceLocator.getTypeSignataireTraitementPapierService();
        SelectValueDTO signataireValue = typeSignataireTraitementPapierService
            .getEntry(complementDTO.getSignataire())
            .map(t -> new SelectValueDTO(t.getKey(), t.getValue()))
            .orElseGet(SelectValueDTO::new);
        complementDTO.setSignataireValue(signataireValue);

        return complementDTO;
    }

    @Override
    public CommunicationDTO getCommunicationDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        CommunicationDTO communicationDTO = MapDoc2Bean.docToBean(dossierDoc, CommunicationDTO.class);
        setLabelValues(communicationDTO.getAllDestinataires().stream());

        return communicationDTO;
    }

    @Override
    public void setLabelValues(Stream<DestinataireCommunicationDTO> destinataires) {
        TypeAvisTraitementPapierService typeAvisTraitementPapierService = SolonEpgServiceLocator.getTypeAvisTraitementPapierService();
        List<ImmutablePair<String, String>> types = typeAvisTraitementPapierService.getEntries();
        destinataires.forEach(
            dest -> {
                dest.setDestinataireCommunicationLabel(
                    STServiceLocator
                        .getSTUserService()
                        .getUserInfo(dest.getDestinataireCommunication(), STUserService.CIVILITE_ABREGEE_PRENOM_NOM)
                );
                dest.setSensAvisLabel(
                    types
                        .stream()
                        .filter(e -> e.getKey().equals(dest.getSensAvis()))
                        .findFirst()
                        .map(ImmutablePair::getValue)
                        .orElse(null)
                );
            }
        );
    }

    @Override
    public String getCommunicationDestinataireObjet(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
        Boolean texteAPublier = traitementPapier.getTexteAPublier();
        Boolean hasSignataire = !SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_AUCUN.equals(
            traitementPapier.getSignataire()
        );

        String objet;
        if (hasSignataire) {
            if (texteAPublier) {
                objet = SolonEpgTraitementPapierConstants.COMMUNICATION_OBJET_AVIS_AVANT_PUBLIC_SIGN;
            } else {
                objet = SolonEpgTraitementPapierConstants.COMMUNICATION_OBJET_AVIS_AVANT_SIGN;
            }
        } else {
            if (texteAPublier) {
                objet = SolonEpgTraitementPapierConstants.COMMUNICATION_OBJET_AVIS_AVANT_PUBLIC;
            } else {
                objet = SolonEpgTraitementPapierConstants.COMMUNICATION_OBJET_AVIS;
            }
        }
        return objet;
    }

    @Override
    public PublicationDTO getPublicationDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        PublicationDTO publication = MapDoc2Bean.docToBean(dossierDoc, PublicationDTO.class);

        CoreSession session = context.getSession();
        publication.setVecteurPublication(
            toSelectValueDTO(
                session,
                publication,
                PublicationDTO::getNomVecteurPublication,
                doc -> doc.getAdapter(VecteurPublication.class).getIntitule()
            )
        );
        publication.setModeParution(
            toSelectValueDTO(
                session,
                publication,
                PublicationDTO::getNomModeParution,
                doc -> doc.getAdapter(ModeParution.class).getMode()
            )
        );

        if (StringUtils.isNotEmpty(publication.getNomDelaiPublication())) {
            DelaiPublicationService delaiPublicationService = SolonEpgServiceLocator.getDelaiPublicationService();
            SelectValueDTO delaiPublication = delaiPublicationService
                .getEntries()
                .stream()
                .filter(d -> d.getKey().equals(publication.getNomDelaiPublication()))
                .findFirst()
                .map(d -> new SelectValueDTO(d.getKey(), d.getValue()))
                .orElseGet(SelectValueDTO::new);
            publication.setDelaiPublication(delaiPublication);
        }

        return publication;
    }

    @Override
    public AmpliationDTO getAmpliationDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
        AmpliationDTO ampliationDTO = new AmpliationDTO();

        ampliationDTO.setDossierPapierArchive(traitementPapier.getPapierArchive());

        CoreSession session = context.getSession();
        AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
        String nomFichier = ampliationService.getNomAmpliationFichier(dossierDoc, session);
        if (StringUtils.isNotBlank(nomFichier)) {
            ampliationDTO.setFileName(nomFichier);
        }

        STUserService stUserService = STServiceLocator.getSTUserService();
        List<String> ampliationDestinataireMails = traitementPapier.getAmpliationDestinataireMails();
        // Les destinataires sont stockés avec leurs logins et non leurs emails
        List<STUser> users = ampliationDestinataireMails
            .stream()
            .filter(email -> !StringHelper.isEmail(email))
            .map(stUserService::getOptionalUser)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        List<String> usernames = users.stream().map(STUser::getUsername).collect(Collectors.toList());
        ampliationDTO.setDestinataires(users.stream().map(STUser::getUsername).collect(Collectors.toList()));
        ampliationDestinataireMails.removeAll(usernames);
        ampliationDTO.setDestinatairesLibres(new ArrayList<>(ampliationDestinataireMails));

        ampliationDTO.setMapDestinataire(
            users.stream().collect(Collectors.toMap(STUser::getUsername, STUser::getFullNameWithUsername))
        );

        return ampliationDTO;
    }

    @Override
    public String getDefaultAmpliationMailText(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
        return ampliationService.getAmpliationMailText(context.getSession(), dossierDoc);
    }

    @Override
    public String getDefaultAmpliationMailObjet(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
        return ampliationService.getAmpliationMailObject(context.getSession(), dossierDoc);
    }

    @Override
    public void saveAmpliationDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
        AmpliationDTO ampliationDTO = context.getFromContextData(EpgContextDataKey.TRAITEMENT_PAPIER_AMPLIATION);

        traitementPapier.setPapierArchive(ampliationDTO.getDossierPapierArchive());

        checkDestinatairesEmails(context, ampliationDTO);

        traitementPapier.setAmpliationDestinataireMails(ampliationDTO.getAllDestinataires());
        if (context.getMessageQueue().hasMessageInQueue()) {
            return;
        }

        saveTraitementPapier(context);
    }

    private void checkDestinatairesEmails(SpecificContext context, AmpliationDTO ampliationDTO) {
        STUserService stUserService = STServiceLocator.getSTUserService();
        ampliationDTO
            .getDestinataires()
            .stream()
            .map(stUserService::getOptionalUser)
            .forEach(dest -> this.checkAmpliationDestinataireEmail(context, dest));
        ampliationDTO
            .getDestinatairesLibres()
            .stream()
            .forEach(email -> this.checkAmpliationDestinataireLibreEmail(context, email));
    }

    private List<String> getEmails(SpecificContext context, AmpliationDTO ampliationDTO) {
        STUserService stUserService = STServiceLocator.getSTUserService();

        HashSet<String> emails = new HashSet<>();

        emails.addAll(
            ampliationDTO
                .getDestinataires()
                .stream()
                .map(stUserService::getOptionalUser)
                .filter(dest -> checkAmpliationDestinataireEmail(context, dest))
                .map(Optional::get)
                .map(STUser::getEmail)
                .collect(Collectors.toList())
        );

        emails.addAll(
            ampliationDTO
                .getDestinatairesLibres()
                .stream()
                .filter(email -> this.checkAmpliationDestinataireLibreEmail(context, email))
                .collect(Collectors.toList())
        );

        return new ArrayList<>(emails);
    }

    private boolean checkAmpliationDestinataireEmail(SpecificContext context, Optional<STUser> user) {
        Optional<String> email = user.map(STUser::getEmail);
        if (!email.isPresent()) {
            context
                .getMessageQueue()
                .addErrorToQueue(
                    ResourceHelper.getString("dossier.traitement.papier.ampliation.no.destinataire", user)
                );
            return false;
        } else if (!StringHelper.isEmail(email.get())) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("dossier.traitement.papier.ampliation.no.email", user));
            return false;
        }
        return true;
    }

    private boolean checkAmpliationDestinataireLibreEmail(SpecificContext context, String email) {
        boolean emailCorrect = StringHelper.isEmail(email);
        if (!emailCorrect) {
            context
                .getMessageQueue()
                .addErrorToQueue(ResourceHelper.getString("dossier.traitement.papier.ampliation.not.email", email));
        }

        return emailCorrect;
    }

    @Override
    public void saveAmpliationFile(SpecificContext context) {
        AmpliationDTO ampliation = context.getFromContextData(EpgContextDataKey.TRAITEMENT_PAPIER_AMPLIATION);

        if (StringUtils.isNotBlank(ampliation.getFileName())) {
            processNouveauAmpliationFichier(context, ampliation);
        } else {
            removeFichierAmpliation(context);
        }
    }

    private void processNouveauAmpliationFichier(SpecificContext context, AmpliationDTO ampliation) {
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();

        AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
        DocumentModel actuelFichier = ampliationService.getAmpliationFichier(dossierDoc, session);

        String nouveauFileName = ampliation.getFileName();
        InputStream nouveauFichierContent = ampliation.getFile();
        Blob nouveauFichierBlob = null;
        if (nouveauFichierContent != null) {
            nouveauFichierBlob = BlobUtils.createSerializableBlob(nouveauFichierContent, nouveauFileName);
        }

        if (actuelFichier == null) {
            addFichierAmpliation(session, context.getCurrentDocument(), nouveauFichierBlob);
        } else {
            if (nouveauFichierBlob == null) {
                renommerFichierAmpliation(session, dossierDoc, nouveauFileName);
            } else {
                if (isEquals(actuelFichier, nouveauFichierBlob)) {
                    renommerFichierAmpliation(session, dossierDoc, nouveauFileName);
                } else {
                    removeFichierAmpliation(context);
                    addFichierAmpliation(session, context.getCurrentDocument(), nouveauFichierBlob);
                }
            }
        }
    }

    private boolean isEquals(DocumentModel actuelAmpliationFichier, Blob nouveauAmpliationFichier) {
        BlobHolder blobHolder = actuelAmpliationFichier.getAdapter(BlobHolder.class);
        Blob blob = blobHolder.getBlob();

        try {
            return IOUtils.contentEquals(blob.getStream(), nouveauAmpliationFichier.getStream());
        } catch (IOException e) {
            throw new NuxeoException(e);
        }
    }

    private void addFichierAmpliation(CoreSession session, DocumentModel dossierDoc, Blob nouveauFichierBlob) {
        AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
        ampliationService.ajouterAmpliationFichier(dossierDoc, nouveauFichierBlob, session);
    }

    private void renommerFichierAmpliation(CoreSession session, DocumentModel dossierDoc, String nouveauNom) {
        AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
        ampliationService.renommerAmpliationFichier(session, dossierDoc, nouveauNom);
    }

    private void removeFichierAmpliation(SpecificContext context) {
        AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
        ampliationService.supprimerAmpliationFichiers(context.getSession(), context.getCurrentDocument());
    }

    private SelectValueDTO toSelectValueDTO(
        CoreSession session,
        PublicationDTO publication,
        Function<PublicationDTO, String> idSupplier,
        Function<DocumentModel, String> valueSupplier
    ) {
        String id = idSupplier.apply(publication);
        IdRef ref = new IdRef(id);
        if (StringUtils.isNotEmpty(id) && session.exists(ref)) {
            DocumentModel doc = session.getDocument(ref);
            return new SelectValueDTO(id, valueSupplier.apply(doc));
        }

        return new SelectValueDTO();
    }

    @Override
    public EpreuveDTO getEpreuveDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        BackEpreuvesDTO backEpreuveDTO = MapDoc2Bean.docToBean(dossierDoc, BackEpreuvesDTO.class);
        return backEpreuveDTO.toEpreuveDTO();
    }

    @Override
    public SignatureDTO getSignatureDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        SignatureDTO signatureDto = MapDoc2Bean.docToBean(dossierDoc, SignatureDTO.class);
        signatureDto.setListes(getListeTraitementPapierMiseEnSinature(context));
        if (StringUtils.isNoneBlank(signatureDto.getSignatureDestinataireSGG())) {
            signatureDto.setSignatureDestinataireSGGValue(
                usrIdToSelectValueDTO(signatureDto.getSignatureDestinataireSGG())
            );
        }
        if (StringUtils.isNoneBlank(signatureDto.getSignatureDestinataireCPM())) {
            signatureDto.setSignatureDestinataireCPMValue(
                usrIdToSelectValueDTO(signatureDto.getSignatureDestinataireCPM())
            );
        }
        return signatureDto;
    }

    @Override
    public RetourDTO getRetourDTO(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        return MapDoc2Bean.docToBean(dossierDoc, RetourDTO.class);
    }

    @Override
    public Response editerBordereauEnvoi(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        CommunicationDTO communicationDTO = Objects.requireNonNull(
            context.getFromContextData(EpgContextDataKey.TRAITEMENT_PAPIER_COMMUNICATION),
            "un communication dto est requis"
        );

        StringBuilder dest = new StringBuilder();

        TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);

        List<DestinataireCommunication> cabinetPmCommunicationList = traitementPapier.getCabinetPmCommunication();
        List<DestinataireCommunication> chargeMissionCommunicationList = traitementPapier.getChargeMissionCommunication();
        List<DestinataireCommunication> autresDestinatairesCommunicationList = traitementPapier.getAutresDestinatairesCommunication();

        Calendar now = Calendar.getInstance();
        Consumer<DestinataireCommunication> updateDateEnvoiFunction = elt -> elt.setDateEnvoi(now);

        prepareDestinatairesForReport(
            cabinetPmCommunicationList,
            communicationDTO.getSelectedCabinetPm(),
            updateDateEnvoiFunction,
            elt -> this.appendDestinataire(dest, elt.getNomDestinataireCommunication(), elt.getObjet(), true, false, "")
        );
        prepareDestinatairesForReport(
            chargeMissionCommunicationList,
            communicationDTO.getSelectedChargeMission(),
            updateDateEnvoiFunction,
            elt -> this.appendDestinataire(dest, elt.getNomDestinataireCommunication(), elt.getObjet(), true, false, "")
        );
        prepareDestinatairesForReport(
            autresDestinatairesCommunicationList,
            communicationDTO.getSelectedAutresDestinataires(),
            updateDateEnvoiFunction,
            elt ->
                this.appendDestinataire(dest, elt.getNomDestinataireCommunication(), elt.getObjet(), false, false, "")
        );

        traitementPapier.setCabinetPmCommunication(cabinetPmCommunicationList);
        traitementPapier.setChargeMissionCommunication(chargeMissionCommunicationList);
        traitementPapier.setAutresDestinatairesCommunication(autresDestinatairesCommunicationList);

        return editerBordereau(context, traitementPapier, dest.toString(), false);
    }

    @Override
    public Response editerBordereauRelance(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        CommunicationDTO communicationDTO = Objects.requireNonNull(
            context.getFromContextData(EpgContextDataKey.TRAITEMENT_PAPIER_COMMUNICATION),
            "un communication dto est requis"
        );

        StringBuilder dest = new StringBuilder();
        TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);

        List<DestinataireCommunication> cabinetPmCommunicationList = traitementPapier.getCabinetPmCommunication();
        List<DestinataireCommunication> chargeMissionCommunicationList = traitementPapier.getChargeMissionCommunication();
        List<DestinataireCommunication> autresDestinatairesCommunicationList = traitementPapier.getAutresDestinatairesCommunication();

        Calendar now = Calendar.getInstance();
        Consumer<DestinataireCommunication> updateDateRelanceFunction = elt -> elt.setDateRelance(now);
        Consumer<DestinataireCommunication> appendReportDestinataireFunction = elt ->
            this.appendDestinataire(
                    dest,
                    elt.getNomDestinataireCommunication(),
                    elt.getObjet(),
                    true,
                    true,
                    Optional.ofNullable(elt.getDateEnvoi()).map(SolonDateConverter.DATE_SLASH::format).orElse(" ")
                );

        prepareDestinatairesForReport(
            cabinetPmCommunicationList,
            communicationDTO.getSelectedCabinetPm(),
            updateDateRelanceFunction,
            appendReportDestinataireFunction
        );
        prepareDestinatairesForReport(
            chargeMissionCommunicationList,
            communicationDTO.getSelectedChargeMission(),
            updateDateRelanceFunction,
            appendReportDestinataireFunction
        );
        prepareDestinatairesForReport(
            autresDestinatairesCommunicationList,
            communicationDTO.getSelectedAutresDestinataires(),
            updateDateRelanceFunction,
            appendReportDestinataireFunction
        );

        traitementPapier.setCabinetPmCommunication(cabinetPmCommunicationList);
        traitementPapier.setChargeMissionCommunication(chargeMissionCommunicationList);
        traitementPapier.setAutresDestinatairesCommunication(autresDestinatairesCommunicationList);

        return editerBordereau(context, traitementPapier, dest.toString(), true);
    }

    private Response editerBordereau(
        SpecificContext context,
        TraitementPapier traitementPapier,
        String dest,
        boolean isRelance
    ) {
        CoreSession session = context.getSession();

        String signataire = StringUtils.defaultIfEmpty(traitementPapier.getNomsSignatairesCommunication(), ".");
        String signataireFonction = getUserFonction(signataire);
        String resolvedSignataire = resolveName(signataire, false, false);
        if (resolvedSignataire != null) {
            signataire = resolvedSignataire;
        }

        traitementPapier.save(session);

        HashMap<String, String> inputValues = new HashMap<>();
        inputValues.put(DOSSIERID_PARAM, traitementPapier.getDocument().getId());
        inputValues.put("DESTINATAIRES_PARAM", dest);
        inputValues.put(SIGNATAIRE_PARAM, signataire);
        inputValues.put(SIGNATAIRE_FONCTION_PARAM, signataireFonction);
        inputValues.put("RELANCE_PARAM", String.valueOf(isRelance));

        SSBirtUIService ssBirtUIService = SSUIServiceLocator.getSSBirtUIService();
        context.putInContextData(SSContextDataKey.BIRT_REPORT_VALUES, inputValues);
        context.putInContextData(SSContextDataKey.BIRT_OUTPUT_FILENAME, "Bordereau_communication");
        String birtReportId = Boolean.TRUE.equals(traitementPapier.getTexteSoumisAValidation())
            ? SolonEpgConstant.BIRT_REPORT_BORDEREAU_COMMUNICATION_SOUMIS_VALIDATION
            : SolonEpgConstant.BIRT_REPORT_BORDEREAU_COMMUNICATION;
        context.putInContextData(SSContextDataKey.BIRT_REPORT_ID, birtReportId);
        return ssBirtUIService.generateDoc(context);
    }

    private void prepareDestinatairesForReport(
        List<DestinataireCommunication> destinataires,
        List<String> selectedDestinataires,
        Consumer<DestinataireCommunication> updateFunction,
        Consumer<DestinataireCommunication> reportDestinatairesBuilderFunction
    ) {
        destinataires
            .stream()
            .filter(
                elt ->
                    StringUtils.isNotBlank(elt.getIdDestinataireCommunication()) &&
                    selectedDestinataires.contains(elt.getIdDestinataireCommunication())
            )
            .forEach(
                elt -> {
                    updateFunction.accept(elt);
                    reportDestinatairesBuilderFunction.accept(elt);
                }
            );
    }

    @Override
    public Response editerBordereauCabinetSg(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        CoreSession session = context.getSession();

        TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);

        List<DestinataireCommunication> cabinetSgDestinataires = traitementPapier.getCabinetSgCommunication();

        cabinetSgDestinataires.forEach(elt -> elt.setDateEnvoi(Calendar.getInstance()));
        traitementPapier.save(session);

        String signataire = StringUtils.defaultIfEmpty(traitementPapier.getNomsSignatairesCommunication(), ".");
        String signataireFonction = getUserFonction(signataire);
        String resolvedSignataire = resolveName(signataire, false, false);
        if (resolvedSignataire != null) {
            signataire = resolvedSignataire;
        }

        HashMap<String, String> inputValues = new HashMap<>();
        inputValues.put(DOSSIERID_PARAM, traitementPapier.getDocument().getId());
        inputValues.put(SIGNATAIRE_PARAM, signataire);
        inputValues.put(SIGNATAIRE_FONCTION_PARAM, signataireFonction);

        SSBirtUIService ssBirtUIService = SSUIServiceLocator.getSSBirtUIService();
        context.putInContextData(SSContextDataKey.BIRT_REPORT_VALUES, inputValues);
        context.putInContextData(SSContextDataKey.BIRT_OUTPUT_FILENAME, "Bordereau_communication");
        context.putInContextData(
            SSContextDataKey.BIRT_REPORT_ID,
            SolonEpgConstant.BIRT_REPORT_BORDEREAU_COMMUNICATION_CABINET_SG
        );
        return ssBirtUIService.generateDoc(context);
    }

    private void appendDestinataire(
        StringBuilder builder,
        String username,
        String object,
        boolean includeCivilite,
        boolean appendOldDate,
        String oldDate
    ) {
        builder
            .append("$name$=")
            .append(resolveName(username, false, false))
            .append(";;$object$=")
            .append(object)
            .append(";;$title$=");
        if (includeCivilite) {
            builder.append(getCivilite(username));
        }
        builder.append(";;");

        if (appendOldDate) {
            builder.append("$oldDate$=").append(oldDate).append(";;");
        }

        builder.append("&");
    }

    /**
     * get user emplyee type (fonction)
     *
     * @param userName
     * @return
     */
    private String getUserFonction(String userName) {
        final UserManager userManager = STServiceLocator.getUserManager();
        DocumentModel docUser = userManager.getUserModel(userName);
        String result = "";
        if (docUser != null) {
            STUser stUser = docUser.getAdapter(STUser.class);
            result = stUser.getEmployeeType();
        }
        return result;
    }

    /**
     * retourner la civilite d'un utilisateur
     *
     * @param userName
     * @return
     * @throws ClientException
     */
    private String getCivilite(String userName) {
        final UserManager userManager = STServiceLocator.getUserManager();
        DocumentModel docUser = userManager.getUserModel(userName);
        String result = "";

        if (docUser != null) {
            STUser stUser = docUser.getAdapter(STUser.class);
            String civilite = stUser.getTitle();
            result = "";

            if (civilite != null && !civilite.trim().equals("")) {
                result = civilite;
            }
        }
        return result;
    }

    /**
     *
     * @param userName
     *            le nom Complet de l'utilistaur (avec ou sans civilite)
     * @param isWithCivilite
     *            flag : true => ajouter la civilite dans le nom otherwise false
     *            => retourner le nom sans civilite
     * @return
     */
    private String resolveName(String userName, boolean isWithCivilite, boolean abregee) {
        final UserManager userManager = STServiceLocator.getUserManager();
        DocumentModel docUser = userManager.getUserModel(userName);
        String result = "";
        if (docUser == null) {
            result = userName;
        } else {
            STUser stUser = docUser.getAdapter(STUser.class);
            String firstName = stUser.getFirstName();
            String lastName = stUser.getLastName();
            String civilite = stUser.getTitle();
            StringBuilder nameBuilder = new StringBuilder();

            if (isWithCivilite && civilite != null && !civilite.trim().equals("")) {
                nameBuilder.append(civilite).append(" ");
            }

            if (firstName != null && !firstName.trim().equals("")) {
                nameBuilder.append(firstName).append(" ");
            }

            if (lastName != null && !lastName.trim().equals("")) {
                nameBuilder.append(lastName);
            }
            result = nameBuilder.toString();
        }

        if (abregee) {
            result = result.replace("Monsieur", "M.");
            result = result.replace("Madame", "Mme.");
        }

        return result;
    }

    @Override
    public Response editerCheminDeCroix(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();

        final TraitementPapierService traitementPapierService = SolonEpgServiceLocator.getTraitementPapierService();
        traitementPapierService.saveTraitementPapier(context.getSession(), dossierDoc);
        TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
        String destSGG = traitementPapier.getSignatureDestinataireSGG();
        String destCPM = traitementPapier.getSignatureDestinataireCPM();

        if (StringUtils.isEmpty(destSGG)) {
            destSGG = ".";
        }

        if (StringUtils.isEmpty(destCPM)) {
            destCPM = ".";
        }

        String resolveddestSGG = resolveName(destSGG, true, true);
        String resolveddestdestCPM = resolveName(destCPM, true, true);

        if (resolveddestSGG != null) {
            destSGG = resolveddestSGG;
        }

        if (resolveddestdestCPM != null) {
            destCPM = resolveddestdestCPM;
        }

        Map<String, String> inputValues = new HashMap<>();
        inputValues.put("DOSSIERID_PARAM", traitementPapier.getDocument().getId());
        inputValues.put("SIGNATAIRE_PARAM", destSGG);
        inputValues.put("A_SIGNATAIRE_PARAM", destCPM);

        SSBirtUIService ssBirtUIService = SSUIServiceLocator.getSSBirtUIService();
        context.putInContextData(SSContextDataKey.BIRT_REPORT_VALUES, inputValues);
        context.putInContextData(SSContextDataKey.BIRT_OUTPUT_FILENAME, "Bordereau_retour_a_envoyeur");
        context.putInContextData(
            SSContextDataKey.BIRT_REPORT_ID,
            SolonEpgConstant.BIRT_REPORT_BORDEREAU_CHEMIN_DE_CROIX
        );
        return ssBirtUIService.generateDoc(context);
    }

    @Override
    public Response editerBordereauRetour(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();

        TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
        traitementPapier.setDateRetour(Calendar.getInstance());

        String dest = StringUtils.defaultIfEmpty(traitementPapier.getRetourA(), ".");
        String sig = StringUtils.defaultIfEmpty(traitementPapier.getNomsSignatairesRetour(), ".");

        String resolvedDest = resolveName(dest, true, false);
        String signataireFonction = getUserFonction(sig);
        String resolvedSignataire = resolveName(sig, false, false);

        if (resolvedDest != null) {
            dest = resolvedDest;
        }

        if (resolvedSignataire != null) {
            sig = resolvedSignataire;
        }

        HashMap<String, String> inputValues = new HashMap<>();
        inputValues.put(DOSSIERID_PARAM, traitementPapier.getDocument().getId());
        inputValues.put("DESTINATAIRE_PARAM", dest);
        inputValues.put(SIGNATAIRE_PARAM, sig);
        inputValues.put(SIGNATAIRE_FONCTION_PARAM, signataireFonction);

        SSBirtUIService ssBirtUIService = SSUIServiceLocator.getSSBirtUIService();
        context.putInContextData(SSContextDataKey.BIRT_REPORT_VALUES, inputValues);
        context.putInContextData(SSContextDataKey.BIRT_OUTPUT_FILENAME, "Bordereau_retour_a_envoyeur");
        context.putInContextData(
            SSContextDataKey.BIRT_REPORT_ID,
            SolonEpgConstant.BIRT_REPORT_BORDEREAU_ENVOI_DE_RETOUR
        );

        traitementPapier.save(context.getSession());
        return ssBirtUIService.generateDoc(context);
    }

    private List<ListeSignatureDTO> getListeTraitementPapierMiseEnSinature(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();
        final ListeTraitementPapierService listeTraitementPapierService = SolonEpgServiceLocator.getListeTraitementPapierService();
        List<DocumentModel> list = listeTraitementPapierService.getListeTraitementPapierOfDossierAndType(
            session,
            dossierDoc.getId(),
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE
        );

        return list
            .stream()
            .map(doc -> doc.getAdapter(ListeTraitementPapier.class))
            .map(ListeSignatureDTO::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<SelectValueDTO> getReferencesSecretariatSecretaireGeneral(SpecificContext context) {
        return getReferences(context.getSession(), TableReference::getSignatureSGG);
    }

    @Override
    public List<SelectValueDTO> getReferencesCabinetPremierMinistre(SpecificContext context) {
        return getReferences(context.getSession(), TableReference::getCabinetPM);
    }

    @Override
    public List<SelectValueDTO> getReferencesChargesMission(SpecificContext context) {
        return getReferences(context.getSession(), TableReference::getChargesMission);
    }

    @Override
    public List<SelectValueDTO> getReferencesCabinetSg(SpecificContext context) {
        return Arrays.asList(new SelectValueDTO("Directeur"));
    }

    @Override
    public List<SelectValueDTO> getReferencesSignataires(SpecificContext context) {
        return getReferences(context.getSession(), TableReference::getSignataires);
    }

    private List<SelectValueDTO> getReferences(
        CoreSession session,
        Function<TableReference, List<String>> supplierUserIds
    ) {
        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        DocumentModel tableReferenceDoc = tableReferenceService.getTableReference(session);
        TableReference tableReference = tableReferenceDoc.getAdapter(TableReference.class);

        List<String> userIds = supplierUserIds.apply(tableReference);
        if (userIds == null) {
            return Collections.emptyList();
        }

        return userIds
            .stream()
            .filter(StringUtils::isNotBlank)
            .map(this::usrIdToSelectValueDTO)
            .collect(Collectors.toList());
    }

    @Override
    public SelectValueDTO usrIdToSelectValueDTO(SpecificContext context) {
        String userId = context.getFromContextData(STContextDataKey.USER_ID);
        return usrIdToSelectValueDTO(userId);
    }

    private SelectValueDTO usrIdToSelectValueDTO(String userId) {
        STUserService stUserService = STServiceLocator.getSTUserService();
        String fullName = ObjectHelper.requireNonNullElse(
            stUserService.getUserInfo(userId, STUserService.CIVILITE_ABREGEE_PRENOM_NOM),
            userId
        );
        return new SelectValueDTO(userId, fullName);
    }

    @Override
    public boolean isReferencesPublicationVisible(SpecificContext context) {
        return isTypeActeValid(
            context,
            (acteService, typeActe) ->
                !(acteService.isRapportAuParlement(typeActe) || acteService.isActeTexteNonPublie(typeActe))
        );
    }

    @Override
    public boolean isReferencesComplementElementVisible(SpecificContext context) {
        return isTypeActeValid(context, (acteService, typeActe) -> !acteService.isRapportAuParlement(typeActe));
    }

    @Override
    public boolean isCommunicationSecretaireGeneralVisible(SpecificContext context) {
        return isTypeActeValid(context, (acteService, typeActe) -> acteService.isArrete(typeActe));
    }

    @Override
    public boolean isCommunicationComplementVisible(SpecificContext context) {
        return isTypeActeValid(context, (acteService, typeActe) -> !acteService.isRapportAuParlement(typeActe));
    }

    @Override
    public boolean isSignatureEditerCheminCroixVisible(SpecificContext context) {
        return isTypeActeValid(context, (acteService, typeActe) -> acteService.isCheminDeCroix(typeActe));
    }

    private boolean isTypeActeValid(SpecificContext context, BiFunction<ActeService, String, Boolean> function) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        ActeService acteService = SolonEpgServiceLocator.getActeService();
        return function.apply(acteService, dossier.getTypeActe());
    }

    @Override
    public String getEpreuveDestinataire(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        EntiteNode ministere = STServiceLocator.getSTMinisteresService().getEntiteNode(dossier.getMinistereResp());

        return Optional.ofNullable(ministere).map(EntiteNode::getFormule).orElse("");
    }

    @Override
    public Response editerDemandeEpreuve(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
        return editerBordereauEpreuve(
            context,
            traitementPapier::getEpreuve,
            traitementPapier::setEpreuve,
            "Bordereau_retour_a_envoyeur"
        );
    }

    @Override
    public Response editerNouvelleEpreuve(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
        return editerBordereauEpreuve(
            context,
            traitementPapier::getNouvelleDemandeEpreuves,
            traitementPapier::setNouvelleDemandeEpreuves,
            "Bordereau_retour_a_envoyeur_pour_relecture"
        );
    }

    private Response editerBordereauEpreuve(
        SpecificContext context,
        Supplier<InfoEpreuve> epreuveGetter,
        Consumer<InfoEpreuve> epreuveSetter,
        String filename
    ) {
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();
        TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
        InfoEpreuve infoEpreuve = ObjectHelper.requireNonNullElseGet(epreuveGetter.get(), InfoEpreuveImpl::new);
        String dest = StringUtils.defaultIfEmpty(infoEpreuve.getNomsSignataires(), ".");
        String signataire = StringUtils.defaultIfEmpty(infoEpreuve.getDestinataireEntete(), ".");

        String resolvedDest = resolveName(dest, true, false);
        String signataireFonction = getUserFonction(signataire);
        String resolvedSignataire = resolveName(signataire, false, false);
        if (resolvedDest != null) {
            dest = resolvedDest;
        }

        if (resolvedSignataire != null) {
            signataire = resolvedSignataire;
        }

        // RG-SUI-PAP-31 : les champs date « Envoi en relecture » et « Envoi en
        // relecture de la nouvelle demande » sont alimentés automatiquement
        // avec la date du jour d’édition des bordereaux
        Calendar dua = Calendar.getInstance();
        infoEpreuve.setEnvoiRelectureLe(dua);
        epreuveSetter.accept(infoEpreuve);
        session.saveDocument(dossierDoc);

        HashMap<String, String> inputValues = new HashMap<>();
        inputValues.put("DOSSIERID_PARAM", traitementPapier.getDocument().getId());
        inputValues.put("DESTINATAIRE_PARAM", dest);
        inputValues.put("SIGNATAIRE_PARAM", signataire);
        inputValues.put("SIGNATAIRE_FONCTION_PARAM", signataireFonction);

        SSBirtUIService ssBirtUIService = SSUIServiceLocator.getSSBirtUIService();
        context.putInContextData(SSContextDataKey.BIRT_REPORT_VALUES, inputValues);
        context.putInContextData(SSContextDataKey.BIRT_OUTPUT_FILENAME, filename);
        context.putInContextData(
            SSContextDataKey.BIRT_REPORT_ID,
            SolonEpgConstant.BIRT_REPORT_BORDEREAU_ENVOI_EPREUVE_EN_RELECTURE
        );
        return ssBirtUIService.generateDoc(context);
    }

    @Override
    public List<HistoriqueAmpliation> getInfoAmpilation(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
        List<InfoHistoriqueAmpliation> result = traitementPapier.getAmpliationHistorique();

        return result.stream().map(HistoriqueAmpliation::new).collect(Collectors.toList());
    }

    @Override
    public void sendAmpliation(SpecificContext context) {
        AmpliationDTO ampliation = context.getFromContextData(EpgContextDataKey.TRAITEMENT_PAPIER_AMPLIATION);
        List<String> emails = getEmails(context, ampliation);

        if (CollectionUtils.isEmpty(emails)) {
            context.getMessageQueue().addErrorToQueue("dossier.traitement.papier.ampliation.no.destinataires");
        }

        String expediteur = ampliation.getExpediteur();
        if (!StringHelper.isEmail(expediteur)) {
            context.getMessageQueue().addErrorToQueue("dossier.traitement.papier.ampliation.no.expediteur");
        }

        String objet = ampliation.getObjet();
        if (StringUtils.isBlank(objet)) {
            context.getMessageQueue().addErrorToQueue("dossier.traitement.papier.ampliation.no.objet");
        }

        String message = ampliation.getMessage();
        if (StringUtils.isBlank(message)) {
            context.getMessageQueue().addErrorToQueue("dossier.traitement.papier.ampliation.no.message");
        }

        if (emails != null && context.getMessageQueue().hasMessageInQueue()) {
            return;
        }

        DocumentModel dossierDoc = context.getCurrentDocument();

        CoreSession session = context.getSession();
        AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
        ampliationService.sendAmpliationMail(
            session,
            dossierDoc,
            expediteur,
            emails,
            ampliation.getObjet(),
            ampliation.getMessage()
        );
    }

    @Override
    public FileSolonEpg getAmpliationFile(SpecificContext context) {
        AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
        DocumentModel doc = ampliationService.getAmpliationFichier(context.getCurrentDocument(), context.getSession());

        return doc.getAdapter(FileSolonEpg.class);
    }
}
