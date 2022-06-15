package fr.dila.solonepg.ui.helper;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteSignale;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.AttenteSignatureDTO;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement;
import fr.sword.naiad.nuxeo.commons.core.util.StringUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.ecm.platform.usermanager.UserManager;

public class EpgAttenteSignatureHelper {

    private EpgAttenteSignatureHelper() {}

    public static AttenteSignatureDTO dossierDocToAttenteSignatureDTO(
        CoreSession session,
        DocumentModel dossierDoc,
        String type
    ) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);

        final TexteSignale texteSignale = SolonEpgServiceLocator
            .getTexteSignaleService()
            .findTexteSignale(session, dossier.getDocument().getId(), type);

        final AttenteSignatureDTO attenteSignatureDTO = new AttenteSignatureDTO();
        attenteSignatureDTO.setDocIdForSelection(dossierDoc.getId());
        attenteSignatureDTO.setDossierId(dossierDoc.getId());
        // RG-SUI-SGG-13
        attenteSignatureDTO.setTitre(dossier.getTitreActe());
        attenteSignatureDTO.setNor(dossier.getNumeroNor());
        // RG-SUI-SGG-14
        attenteSignatureDTO.setDateDemandePublicationTS(DateUtil.toDate(texteSignale.getDateDemandePublicationTS()));
        attenteSignatureDTO.setVecteurPublicationTS(texteSignale.getVecteurPublicationTS());
        if (StringUtils.isNotEmpty(attenteSignatureDTO.getVecteurPublicationTS())) {
            attenteSignatureDTO.setVecteurPublicationTSLibelle(
                STServiceLocator
                    .getVocabularyService()
                    .getEntryLabel(
                        VocabularyConstants.VECTEUR_PUBLICATION_TS,
                        attenteSignatureDTO.getVecteurPublicationTS()
                    )
            );
        }
        // RG-SUI-SGG-15
        attenteSignatureDTO.setArriveeSolon(dossier.getArriveeSolonTS());
        // RG-SUI-SGG-16
        attenteSignatureDTO.setAccordPM(computeAccordPM(dossier, session));
        // RG-SUI-SGG-17
        attenteSignatureDTO.setAccordSGG(computeAccordSGG(dossier, session));
        // RG-SUI-SGG-18
        attenteSignatureDTO.setArriveeOriginale(traitementPapier.getDateArrivePapier() != null);
        // RG-SUI-SGG-19
        attenteSignatureDTO.setMiseEnSignature(computeMiseEnsignature(traitementPapier));
        // RG-SUI-SGG-20
        attenteSignatureDTO.setRetourSignature(computeRetourSignature(traitementPapier));
        // RG-SUI-SGG-21
        attenteSignatureDTO.setDecretPr(computeDecretPr(dossier));
        // RG-SUI-SGG-22
        attenteSignatureDTO.setEnvoiPr(computeEnvoiPr(traitementPapier));
        // RG-SUI-SGG-23
        attenteSignatureDTO.setRetourPr(computeRetourPr(traitementPapier));
        // RG-SUI-SGG-24
        attenteSignatureDTO.setDateJo(computedateJo(dossier));
        // RG-SUI-SGG-25
        computeDelaiAndDatePublication(dossier, attenteSignatureDTO);
        // RG-SUI-SGG-26
        attenteSignatureDTO.setPublication(VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut()));
        // observation
        attenteSignatureDTO.setObservation(texteSignale.getObservationTS());
        return attenteSignatureDTO;
    }

    // RG-SUI-SGG-25
    private static void computeDelaiAndDatePublication(
        final Dossier dossier,
        final AttenteSignatureDTO attenteSignatureDTO
    ) {
        final TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        String delai = dossier.getDelaiPublication();
        if (StringUtils.isNotEmpty(delai)) {
            attenteSignatureDTO.setDelai(delai);
            attenteSignatureDTO.setDatePublication(DateUtil.toDate(dossier.getDatePreciseePublication()));
        } else {
            delai = traitementPapier.getPublicationDelai();
            if (StringUtils.isNotEmpty(delai)) {
                attenteSignatureDTO.setDelai(delai);
                attenteSignatureDTO.setDatePublication(DateUtil.toDate(traitementPapier.getPublicationDateDemande()));
            }
        }
        if (StringUtils.isNotEmpty(attenteSignatureDTO.getDelai())) {
            attenteSignatureDTO.setDelaiLibelle(
                STServiceLocator
                    .getVocabularyService()
                    .getEntryLabel(VocabularyConstants.DELAI_PUBLICATION, attenteSignatureDTO.getDelai())
            );
        }
    }

    // RG-SUI-SGG-24
    private static Date computedateJo(final Dossier dossier) {
        Calendar cal = dossier.getDateEnvoiJOTS();
        if (cal != null) {
            return cal.getTime();
        }
        final TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        return DateUtil.toDate(traitementPapier.getPublicationDateEnvoiJo());
    }

    // RG-SUI-SGG-23
    private static boolean computeRetourPr(final TraitementPapier traitementPapier) {
        boolean result = false;
        final DonneesSignataire donneesSignatairePr = traitementPapier.getSignaturePr();
        if (donneesSignatairePr != null && donneesSignatairePr.getDateRetourSignature() != null) {
            result = true;
        }
        return result;
    }

    // RG-SUI-SGG-22
    private static boolean computeEnvoiPr(final TraitementPapier traitementPapier) {
        boolean result = false;
        final DonneesSignataire donneesSignatairePr = traitementPapier.getSignaturePr();
        if (donneesSignatairePr != null && donneesSignatairePr.getDateEnvoiSignature() != null) {
            result = true;
        }
        return result;
    }

    // RG-SUI-SGG-21
    private static boolean computeDecretPr(final Dossier dossier) {
        return TypeActeConstants.TYPE_ACTE_DECRET_PR.equals(dossier.getTypeActe());
    }

    // RG-SUI-SGG-20
    private static boolean computeRetourSignature(final TraitementPapier traitementPapier) {
        boolean result = false;
        final DonneesSignataire donneesSignatairePm = traitementPapier.getSignaturePm();
        if (donneesSignatairePm != null && donneesSignatairePm.getDateRetourSignature() != null) {
            result = true;
        }
        final DonneesSignataire donneesSignataireSgg = traitementPapier.getSignatureSgg();
        if (donneesSignataireSgg != null && donneesSignataireSgg.getDateRetourSignature() != null) {
            result = true;
        }
        return result;
    }

    // RG-SUI-SGG-19
    private static boolean computeMiseEnsignature(final TraitementPapier traitementPapier) {
        boolean result = false;
        final DonneesSignataire donneesSignatairePm = traitementPapier.getSignaturePm();
        if (donneesSignatairePm != null && donneesSignatairePm.getDateEnvoiSignature() != null) {
            result = true;
        }
        final DonneesSignataire donneesSignataireSgg = traitementPapier.getSignatureSgg();
        if (donneesSignataireSgg != null && donneesSignataireSgg.getDateEnvoiSignature() != null) {
            result = true;
        }
        return result;
    }

    // RG-SUI-SGG-17
    private static boolean computeAccordSGG(final Dossier dossier, final CoreSession coreSession) {
        boolean result = false;
        final TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        final TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();

        final DocumentModel tableReferenceDoc = tableReferenceService.getTableReference(coreSession);
        TableReference tableReference = null;
        if (tableReferenceDoc != null) {
            tableReference = tableReferenceDoc.getAdapter(TableReference.class);
        }

        List<String> listChargesMission = Optional
            .ofNullable(tableReference)
            .map(TableReference::getChargesMission)
            .orElse(Collections.emptyList());
        if (CollectionUtils.isNotEmpty(listChargesMission)) {
            result =
                checkAvisDestinatairesCommunication(
                    listChargesMission,
                    traitementPapier.getChargeMissionCommunication()
                );

            if (!result) {
                result = checkAvisFeuilleDeRoute(coreSession, dossier, listChargesMission);
            }
        }

        return result;
    }

    // RG-SUI-SGG-16
    private static boolean computeAccordPM(final Dossier dossier, final CoreSession coreSession) {
        boolean result = false;
        final TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        final TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        final DocumentModel tableReferenceDoc = tableReferenceService.getTableReference(coreSession);
        TableReference tableReference = null;
        if (tableReferenceDoc != null) {
            tableReference = tableReferenceDoc.getAdapter(TableReference.class);
        }

        List<String> listCabinetPM = Optional
            .ofNullable(tableReference)
            .map(TableReference::getCabinetPM)
            .orElse(Collections.emptyList());
        if (CollectionUtils.isNotEmpty(listCabinetPM)) {
            result = checkAvisDestinatairesCommunication(listCabinetPM, traitementPapier.getCabinetPmCommunication());

            if (!result) {
                result = checkAvisFeuilleDeRoute(coreSession, dossier, listCabinetPM);
            }
        }

        return result;
    }

    private static boolean checkAvisDestinatairesCommunication(
        List<String> listUsers,
        final List<DestinataireCommunication> listDC
    ) {
        boolean result = false;
        if (CollectionUtils.isNotEmpty(listDC)) {
            // cas traitement papier
            for (final DestinataireCommunication destinataireCommunication : listDC) {
                final String destinataire = destinataireCommunication.getNomDestinataireCommunication();

                final UserManager userManager = STServiceLocator.getUserManager();

                final DocumentModel user = userManager.getUserModel(destinataire);
                if (user != null && listUsers.contains(user.getId())) {
                    final String avis = destinataireCommunication.getSensAvis();
                    // cf type_avis_traitement_papier.csv
                    if (avis == null || !isAvisFavorable(avis)) {
                        return false;
                    } else {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    private static boolean checkAvisFeuilleDeRoute(
        final CoreSession session,
        final Dossier dossier,
        List<String> listUsers
    ) {
        final UserManager userManager = STServiceLocator.getUserManager();
        final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();

        final Set<String> mailboxes = new HashSet<>();
        for (final String userId : listUsers) {
            final DocumentModel user = userManager.getUserModel(userId);
            if (user != null) {
                final STUser stUser = user.getAdapter(STUser.class);
                mailboxes.addAll(mailboxPosteService.getMailboxPosteIdSetFromPosteIdSet(stUser.getPostes()));
            }
        }

        if (mailboxes.isEmpty()) {
            return false;
        }

        // recherche des pour avis
        final Object[] params = { VocabularyConstants.ROUTING_TASK_TYPE_AVIS, dossier.getLastDocumentRoute() };
        String query =
            " SELECT rs.ecm:currentLifeCycleState AS life, rs.rtsk:validationStatus AS status FROM RouteStep AS rs " +
            " WHERE rs.rtsk:type = ? " +
            " AND rs.rtsk:distributionMailboxId IN (" +
            StringUtil.join(mailboxes, ", ", "'") +
            ") " +
            " AND rs.rtsk:documentRouteId = ? ";

        // cas feuille de route
        try (IterableQueryResult iterableQueryResult = QueryUtils.doUFNXQLQuery(session, query, params)) {
            return StreamSupport
                .stream(iterableQueryResult.spliterator(), false)
                .allMatch(
                    map -> isValidationManuelleOuAvecCorrection((String) map.get("life"), (String) map.get("status"))
                );
        }
    }

    private static boolean isValidationManuelleOuAvecCorrection(String lifeCycleState, String status) {
        return (
            FeuilleRouteElement.ElementLifeCycleState.done.name().equals(lifeCycleState) &&
            (
                SSFeuilleRouteConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE.equals(status) ||
                SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_CORRECTION_VALUE.equals(status)
            )
        );
    }

    /**
     * Avis à partir du vocabulaire (ie type_avis_traitement_papier.csv)
     *
     * @param avis
     * @return
     */
    private static boolean isAvisFavorable(final String avis) {
        final DirectoryService directoryService = STServiceLocator.getDirectoryService();
        try (Session session = directoryService.open(VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_DIRECTORY)) {
            DocumentModel documentModel = session.getEntry(avis);
            final String type = (String) documentModel.getProperty(
                VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_SCHEMA,
                VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_TYPE
            );
            return "FAV".equals(type);
        }
    }
}
