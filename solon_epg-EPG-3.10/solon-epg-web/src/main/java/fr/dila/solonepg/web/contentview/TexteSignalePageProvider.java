package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.directory.DirectoryException;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.ecm.platform.usermanager.UserManager;

import fr.dila.ecm.platform.routing.api.DocumentRouteElement;
import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteSignale;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.TexteSignaleConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.service.TexteSignaleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.client.TexteSignaleDTO;
import fr.dila.solonepg.web.client.TexteSignaleDTOImpl;
import fr.dila.solonepg.web.suivi.textessignales.TextesSignalesActionsBean;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.web.contentview.ColumnBeanManager;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.contentview.AbstractDTOPageProvider;
import fr.dila.st.web.contentview.HiddenColumnPageProvider;

/**
 * Provider pour les textes signales
 * 
 * @author asatre
 * 
 */
public class TexteSignalePageProvider extends AbstractDTOPageProvider implements HiddenColumnPageProvider {

    private static final long serialVersionUID = 1L;

    private Map<String, TexteSignaleDTO> currentMap;

    private static final String SELECT = " SELECT d.ecm:uuid as id FROM Dossier as d, TexteSignale as t ";
    private static final String WHERE = " WHERE d.dos:dateVersementTS IS NOT NULL AND d.dos:" //
            + DossierSolonEpgConstants.ARCHIVE_DOSSIER_PROPERTY //
            + " = 0 AND t.ts:idDossier = d.ecm:uuid  AND t.ts:" // 
            + TexteSignaleConstants.TYPE + " = ? ";
    private static final String ORDER_BY = " ORDER BY t.ts:" //
            + TexteSignaleConstants.DATE_DEMANDE_PUBLICATION_TS //
            + " desc, d.dos:" //
            + DossierSolonEpgConstants.DOSSIER_DATE_VERSEMENT_TS //
            + " desc ";

    @Override
    protected void fillCurrentPageMapList(final CoreSession coreSession) throws ClientException {
        currentItems = new ArrayList<Map<String, Serializable>>();
        currentMap = new HashMap<String, TexteSignaleDTO>();

        final Map<String, Serializable> props = getProperties();
        final TextesSignalesActionsBean bean = (TextesSignalesActionsBean) props.get(COLUMN_BEAN_MANAGER);

        final String type = bean.getParams();
        final Object[] params = new Object[] { type };

        final String currentQuery = SELECT + WHERE + ORDER_BY;

        resultsCount = QueryUtils.doCountQuery(coreSession, QueryUtils.ufnxqlToFnxqlQuery(currentQuery), params);
        Long pagesize = getPageSize();
        if (pagesize < 1) {
            pagesize = maxPageSize;
        }

        if (resultsCount > 0) {

            final List<DocumentModel> dml = QueryUtils.doUFNXQLQueryAndFetchForDocuments(coreSession, "Dossier",
                    QueryUtils.ufnxqlToFnxqlQuery(currentQuery), params, pagesize, offset);

            final TexteSignaleService texteSignaleService = SolonEpgServiceLocator.getTexteSignaleService();
            if (!dml.isEmpty()) {

                for (final DocumentModel dm : dml) {

                    final Dossier dossier = dm.getAdapter(Dossier.class);
                    final TraitementPapier traitementPapier = dm.getAdapter(TraitementPapier.class);

                    final TexteSignale texteSignale = texteSignaleService.findTexteSignale(coreSession, dossier.getDocument().getId(), type);

                    final TexteSignaleDTO texteSignaleDTO = new TexteSignaleDTOImpl();
                    texteSignaleDTO.setDocIdForSelection(dm.getId());
                    texteSignaleDTO.setDossierid(dm.getId());
                    // RG-SUI-SGG-13
                    texteSignaleDTO.setTitre(dossier.getTitreActe());
                    texteSignaleDTO.setNor(dossier.getNumeroNor());
                    // RG-SUI-SGG-14
                    final Calendar cal = texteSignale.getDateDemandePublicationTS();
                    if (cal != null) {
                        texteSignaleDTO.setDateDemandePublicationTS(cal.getTime());
                    } else {
                        texteSignaleDTO.setDateDemandePublicationTS(null);
                    }
                    texteSignaleDTO.setVecteurPublicationTS(texteSignale.getVecteurPublicationTS());
                    // RG-SUI-SGG-15
                    texteSignaleDTO.setArriveeSolon(dossier.getArriveeSolonTS());
                    // RG-SUI-SGG-16
                    texteSignaleDTO.setAccordPM(computeAccordPM(dossier, coreSession));
                    // RG-SUI-SGG-17
                    texteSignaleDTO.setAccordSGG(computeAccordSGG(dossier, coreSession));
                    // RG-SUI-SGG-18
                    texteSignaleDTO.setArriveeOriginale(traitementPapier.getDateArrivePapier() != null);
                    // RG-SUI-SGG-19
                    texteSignaleDTO.setMiseEnSignature(computeMiseEnsignature(traitementPapier));
                    // RG-SUI-SGG-20
                    texteSignaleDTO.setRetourSignature(computeRetourSignature(traitementPapier));
                    // RG-SUI-SGG-21
                    texteSignaleDTO.setDecretPr(computeDecretPr(dossier));
                    // RG-SUI-SGG-22
                    texteSignaleDTO.setEnvoiPr(computeEnvoiPr(traitementPapier));
                    // RG-SUI-SGG-23
                    texteSignaleDTO.setRetourPr(computeRetourPr(traitementPapier));
                    // RG-SUI-SGG-24
                    texteSignaleDTO.setDateJo(computedateJo(dossier));
                    // RG-SUI-SGG-25
                    computeDelaiAndDatePublication(dossier, texteSignaleDTO);
                    // RG-SUI-SGG-26
                    texteSignaleDTO.setPublication(VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut()));
                    // observation
                    texteSignaleDTO.setObservation(texteSignale.getObservationTS());

                    currentItems.add(texteSignaleDTO);
                    currentMap.put(dm.getId(), texteSignaleDTO);
                }
            }
        }

    }

    // RG-SUI-SGG-25
    private void computeDelaiAndDatePublication(final Dossier dossier, final TexteSignaleDTO texteSignaleDTO) {
        final TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        String delai = dossier.getDelaiPublication();
        if (delai != null && !delai.isEmpty()) {
            texteSignaleDTO.setDelai(delai);
            final Calendar cal = dossier.getDatePreciseePublication();
            if (cal != null) {
                texteSignaleDTO.setDatePublication(cal.getTime());
            }
        } else {
            delai = traitementPapier.getPublicationDelai();
            if (delai != null && !delai.isEmpty()) {
                texteSignaleDTO.setDelai(delai);
                final Calendar cal = traitementPapier.getPublicationDateDemande();
                if (cal != null) {
                    texteSignaleDTO.setDatePublication(cal.getTime());
                }
            }
        }
    }

    // RG-SUI-SGG-24
    private Date computedateJo(final Dossier dossier) {
        final TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        Calendar cal = dossier.getDateEnvoiJOTS();
        if (cal != null) {
            return cal.getTime();
        }
        cal = traitementPapier.getPublicationDateEnvoiJo();
        if (cal != null) {
            return cal.getTime();
        }
        return null;
    }

    // RG-SUI-SGG-23
    private Boolean computeRetourPr(final TraitementPapier traitementPapier) {
        Boolean result = Boolean.FALSE;
        final DonneesSignataire donneesSignatairePr = traitementPapier.getSignaturePr();
        if (donneesSignatairePr != null && donneesSignatairePr.getDateRetourSignature() != null) {
            result = Boolean.TRUE;
        }
        return result;
    }

    // RG-SUI-SGG-22
    private Boolean computeEnvoiPr(final TraitementPapier traitementPapier) {
        Boolean result = Boolean.FALSE;
        final DonneesSignataire donneesSignatairePr = traitementPapier.getSignaturePr();
        if (donneesSignatairePr != null && donneesSignatairePr.getDateEnvoiSignature() != null) {
            result = Boolean.TRUE;
        }
        return result;
    }

    // RG-SUI-SGG-21
    private Boolean computeDecretPr(final Dossier dossier) {
        return TypeActeConstants.TYPE_ACTE_DECRET_PR.equals(dossier.getTypeActe());
    }

    // RG-SUI-SGG-20
    private Boolean computeRetourSignature(final TraitementPapier traitementPapier) {
        Boolean result = Boolean.FALSE;
        final DonneesSignataire donneesSignatairePm = traitementPapier.getSignaturePm();
        if (donneesSignatairePm != null && donneesSignatairePm.getDateRetourSignature() != null) {
            result = Boolean.TRUE;
        }
        final DonneesSignataire donneesSignataireSgg = traitementPapier.getSignatureSgg();
        if (donneesSignataireSgg != null && donneesSignataireSgg.getDateRetourSignature() != null) {
            result = Boolean.TRUE;
        }
        return result;
    }

    // RG-SUI-SGG-19
    private Boolean computeMiseEnsignature(final TraitementPapier traitementPapier) {
        Boolean result = Boolean.FALSE;
        final DonneesSignataire donneesSignatairePm = traitementPapier.getSignaturePm();
        if (donneesSignatairePm != null && donneesSignatairePm.getDateEnvoiSignature() != null) {
            result = Boolean.TRUE;
        }
        final DonneesSignataire donneesSignataireSgg = traitementPapier.getSignatureSgg();
        if (donneesSignataireSgg != null && donneesSignataireSgg.getDateEnvoiSignature() != null) {
            result = Boolean.TRUE;
        }
        return result;
    }

    // RG-SUI-SGG-17
    private Boolean computeAccordSGG(final Dossier dossier, final CoreSession coreSession) throws ClientException {
        Boolean result = Boolean.FALSE;
        final TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        final TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();

        final DocumentModel tableReferenceDoc = tableReferenceService.getTableReference(coreSession);
        TableReference tableReference = null;
        if (tableReferenceDoc != null) {
            tableReference = tableReferenceDoc.getAdapter(TableReference.class);
        }
        List<String> listChargesMission = new ArrayList<String>();
        if (tableReference != null) {
            listChargesMission = tableReference.getChargesMission();
        }
        if (listChargesMission != null && !listChargesMission.isEmpty()) {
            final List<DestinataireCommunication> listDC = traitementPapier.getChargeMissionCommunication();
            if (listDC != null && !listDC.isEmpty()) {
                // cas traitement papier
                for (final DestinataireCommunication destinataireCommunication : listDC) {
                    final String destinataire = destinataireCommunication.getNomDestinataireCommunication();

                    final UserManager userManager = STServiceLocator.getUserManager();

                    final DocumentModel user = userManager.getUserModel(destinataire);
                    if (user != null) {
                        if (listChargesMission.contains(user.getId())) {
                            final String avis = destinataireCommunication.getSensAvis();
                            // cf type_avis_traitement_papier.csv
                            if (avis == null || !isAvisFavorable(avis)) {
                                result = Boolean.FALSE;
                                break;
                            } else {
                                result = Boolean.TRUE;
                            }
                        }
                    }
                }
            }

            if (!result) {
                final UserManager userManager = STServiceLocator.getUserManager();
                final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();

                final Set<String> mailboxes = new HashSet<String>();
                for (final String userId : listChargesMission) {
                    final DocumentModel user = userManager.getUserModel(userId);
                    if (user != null) {
                        final STUser stUser = user.getAdapter(STUser.class);
                        mailboxes.addAll(mailboxPosteService.getMailboxPosteIdSetFromPosteIdSet(stUser.getPostes()));
                    }
                }

                if (mailboxes.isEmpty()) {
                    return Boolean.FALSE;
                }

                // recherche des pour avis
                final Object[] params = new Object[] { VocabularyConstants.ROUTING_TASK_TYPE_AVIS, dossier.getLastDocumentRoute() };
                final StringBuilder query = new StringBuilder();
                query.append(" SELECT rs.ecm:currentLifeCycleState AS life, rs.rtsk:validationStatus AS status, rs.rtsk:type AS typ FROM RouteStep AS rs ");
                query.append(" WHERE rs.rtsk:type = ? ");
                query.append(" AND rs.rtsk:distributionMailboxId IN (" + StringUtil.join(mailboxes, ", ", "'") + ") ");
                query.append(" AND rs.rtsk:documentRouteId = ? ");

                // cas feuille de route
                IterableQueryResult iterableQueryResult = null;
                try {
                    iterableQueryResult = QueryUtils.doUFNXQLQuery(coreSession, query.toString(), params);
                    if (iterableQueryResult != null) {
                        final Iterator<Map<String, Serializable>> iter = iterableQueryResult.iterator();
                        while (iter.hasNext()) {
                            final Map<String, Serializable> map = iter.next();
                            final String lifeCycleState = (String) map.get("life");
                            if (DocumentRouteElement.ElementLifeCycleState.done.name().equals(lifeCycleState)) {
                                final String status = (String) map.get("status");
                                if (STSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE.equals(status)) {
                                    // validation manuelle
                                    final String type = (String) map.get("typ");
                                    if ("2".equals(type) || "4".equals(type) || "5".equals(type) || "7".equals(type) || "8".equals(type)) {
                                        result = Boolean.TRUE;
                                    } else {
                                        result = Boolean.FALSE;
                                    }
                                } else if (SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_CORRECTION_VALUE.equals(status)) {
                                    // validation avec correction
                                    final String type = (String) map.get("typ");
                                    if ("2".equals(type) || "4".equals(type) || "5".equals(type) || "7".equals(type) || "8".equals(type)) {
                                        result = Boolean.TRUE;
                                    } else {
                                        result = Boolean.FALSE;
                                    }
                                } else {
                                    result = Boolean.FALSE;
                                }

                            } else {
                                result = Boolean.FALSE;
                            }
                        }

                    }
                } finally {
                    if (iterableQueryResult != null) {
                        iterableQueryResult.close();
                    }
                }
            }
        }

        return result;
    }

    // RG-SUI-SGG-16
    private Boolean computeAccordPM(final Dossier dossier, final CoreSession coreSession) throws ClientException {
        Boolean result = Boolean.FALSE;
        final TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        final TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        final DocumentModel tableReferenceDoc = tableReferenceService.getTableReference(coreSession);
        TableReference tableReference = null;
        if (tableReferenceDoc != null) {
            tableReference = tableReferenceDoc.getAdapter(TableReference.class);
        }
        List<String> listCabinetPM = new ArrayList<String>();
        if (tableReference != null) {
            listCabinetPM = tableReference.getCabinetPM();
        }
        if (listCabinetPM != null && !listCabinetPM.isEmpty()) {

            final List<DestinataireCommunication> listDC = traitementPapier.getCabinetPmCommunication();
            if (listDC != null && !listDC.isEmpty()) {
                // cas traitement papier
                for (final DestinataireCommunication destinataireCommunication : listDC) {
                    final String destinataire = destinataireCommunication.getNomDestinataireCommunication();

                    final UserManager userManager = STServiceLocator.getUserManager();

                    final DocumentModel user = userManager.getUserModel(destinataire);
                    if (user != null) {
                        if (listCabinetPM.contains(user.getId())) {
                            final String avis = destinataireCommunication.getSensAvis();
                            // cf type_avis_traitement_papier.csv
                            if (avis == null || !isAvisFavorable(avis)) {
                                result = Boolean.FALSE;
                                break;
                            } else {
                                result = Boolean.TRUE;
                            }
                        }
                    }
                }
            }

            if (!result) {
                final UserManager userManager = STServiceLocator.getUserManager();
                final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();

                final Set<String> mailboxes = new HashSet<String>();
                for (final String userId : listCabinetPM) {
                    final DocumentModel user = userManager.getUserModel(userId);
                    if (user != null) {
                        final STUser stUser = user.getAdapter(STUser.class);
                        mailboxes.addAll(mailboxPosteService.getMailboxPosteIdSetFromPosteIdSet(stUser.getPostes()));
                    }
                }

                if (mailboxes.isEmpty()) {
                    return Boolean.FALSE;
                }

                // recherche des pour avis
                final Object[] params = new Object[] { VocabularyConstants.ROUTING_TASK_TYPE_AVIS, dossier.getLastDocumentRoute() };
                final StringBuilder query = new StringBuilder();
                query.append(" SELECT rs.ecm:currentLifeCycleState AS life, rs.rtsk:validationStatus AS status, rs.rtsk:type AS typ FROM RouteStep AS rs ");
                query.append(" WHERE rs.rtsk:type = ? ");
                query.append(" AND rs.rtsk:distributionMailboxId IN (" + StringUtil.join(mailboxes, ", ", "'") + ") ");
                query.append(" AND rs.rtsk:documentRouteId = ? ");

                // cas feuille de route
                IterableQueryResult iterableQueryResult = null;
                try {
                    iterableQueryResult = QueryUtils.doUFNXQLQuery(coreSession, query.toString(), params);
                    if (iterableQueryResult != null) {
                        final Iterator<Map<String, Serializable>> iter = iterableQueryResult.iterator();
                        while (iter.hasNext()) {
                            final Map<String, Serializable> m = iter.next();
                            final String lifeCycleState = (String) m.get("life");
                            if (DocumentRouteElement.ElementLifeCycleState.done.name().equals(lifeCycleState)) {
                                final String status = (String) m.get("status");
                                if (STSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_VALUE.equals(status)) {
                                    // validation manuelle
                                    final String type = (String) m.get("typ");
                                    if ("2".equals(type) || "4".equals(type) || "5".equals(type) || "7".equals(type) || "8".equals(type)) {
                                        result = Boolean.TRUE;
                                    } else {
                                        result = Boolean.FALSE;
                                    }
                                } else if (SolonEpgSchemaConstant.ROUTING_TASK_VALIDATION_STATUS_AVIS_FAVORABLE_CORRECTION_VALUE.equals(status)) {
                                    // validation avec correction
                                    final String type = (String) m.get("typ");
                                    if ("2".equals(type) || "4".equals(type) || "5".equals(type) || "7".equals(type) || "8".equals(type)) {
                                        result = Boolean.TRUE;
                                    } else {
                                        result = Boolean.FALSE;
                                    }
                                } else {
                                    result = Boolean.FALSE;
                                }

                            } else {
                                result = Boolean.FALSE;
                            }
                        }
                    }
                } finally {
                    if (iterableQueryResult != null) {
                        iterableQueryResult.close();
                    }
                }
            }
        }

        return result;
    }

    public TexteSignaleDTO getTexteSignaleDTO(final String docIdForSelection) {
        return currentMap.get(docIdForSelection);
    }

    public Map<String, TexteSignaleDTO> getAllTexteSignaleDTO() {
        return currentMap;
    }

    /**
     * Avis à partir du vocabulaire (ie type_avis_traitement_papier.csv)
     * 
     * @param avis
     * @return
     * @throws ClientException
     */
    private Boolean isAvisFavorable(final String avis) throws ClientException {

        final DirectoryService directoryService = STServiceLocator.getDirectoryService();
        Session session = null;
        DocumentModel documentModel = null;
        try {
            session = directoryService.open(VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_DIRECTORY);
            documentModel = session.getEntry(avis);
            final String type = (String) documentModel.getProperty(VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_SCHEMA,
                    VocabularyConstants.VOCABULARY_TYPE_AVIS_TP_TYPE);
            return type != null && type.equals("FAV");
        } catch (final Exception e) {
            return false;
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (final DirectoryException e) {
                    // do nothing
                }
            }
        }
    }

    @Override
    public Boolean isHiddenColumn(final String isHidden) throws ClientException {
        if (!StringUtils.isEmpty(isHidden)) {
            final Map<String, Serializable> props = getProperties();
            final Object bean = props.get(COLUMN_BEAN_MANAGER);
            if (bean instanceof ColumnBeanManager) {
                return ((ColumnBeanManager) bean).isHiddenColumn(isHidden);
            }
        }
        return Boolean.FALSE;
    }
}
