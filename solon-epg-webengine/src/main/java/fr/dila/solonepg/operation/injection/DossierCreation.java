package fr.dila.solonepg.operation.injection;

import com.google.common.collect.ImmutableMap;
import fr.dila.cm.mailbox.Mailbox;
import fr.dila.cm.mailbox.MailboxConstants;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DossierTransposition;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.TypeActe;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.core.cases.typescomplexe.DossierTranspositionImpl;
import fr.dila.solonepg.core.cases.typescomplexe.TranspositionsContainerImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.DocumentHelper;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

@Operation(
    id = DossierCreation.ID,
    category = Constants.CAT_DOCUMENT,
    label = "Create Dossier",
    description = "Create Dossier : Test."
)
public class DossierCreation {
    private static final Log repriseLog = LogFactory.getLog("reprise-log");

    public static final String ID = "EPG.CreateDossier";

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    // 30/12/2010
    private SimpleDateFormat formatter = null;

    @Context
    protected OperationContext ctx;

    @Context
    protected CoreSession session;

    @Param(name = "typeActe", required = true, order = 1)
    protected String typeActe;

    @Param(name = "numeroNor", required = true, order = 2)
    protected String numeroNor;

    @Param(name = "statut", required = false, order = 3)
    protected String statut;

    @Param(name = "loiDeRef", required = true, order = 4)
    protected String loiDeRef;

    @Param(name = "ordoDeRef", required = true, order = 5)
    protected String ordoDeRef;

    @Param(name = "dirDeRef", required = true, order = 6)
    protected String dirDeRef;

    @Param(name = "dateTransmissionSectionCe", required = false, order = 7)
    protected String dateTransmissionSectionCe;

    @Param(name = "dateSectionCe", required = false, order = 8)
    protected String dateSectionCe;

    @Param(name = "dateAgCe", required = false, order = 9)
    protected String dateAgCe;

    @Param(name = "dateSignature", required = false, order = 10)
    protected String dateSignature;

    @Param(name = "datePreciseePublication", required = false, order = 11)
    protected String datePreciseePublication;

    @Param(name = "publicationDate", required = false, order = 12)
    protected String publicationDate;

    @Param(name = "dateSignaturePm", required = false, order = 13)
    protected String dateSignaturePm;

    @Param(name = "dateSignaturePr", required = false, order = 14)
    protected String dateSignaturePr;

    @Param(name = "sectionCe", required = false, order = 15)
    protected String sectionCe;

    @Param(name = "nomCompletChargesMissions", required = false, order = 16)
    protected String nomCompletChargesMissions;

    @Param(name = "nomCompletConseillersPms", required = false, order = 17)
    protected String nomCompletConseillersPms;

    @Param(name = "properties")
    protected Properties properties;

    @OperationMethod
    public DocumentModel run() throws Exception {
        repriseLog.debug("Creation du dossier " + numeroNor);
        DocumentModel result = null;
        try {
            Mailbox currentMailbox = null;

            String posteId;
            String username = ctx.getPrincipal().getName();

            Mailbox personalMailbox = null;

            List<Mailbox> mailboxes = STServiceLocator.getMailboxService().getUserMailboxes(session);
            if (mailboxes != null && !mailboxes.isEmpty()) {
                for (Iterator<Mailbox> it = mailboxes.iterator(); it.hasNext();) {
                    Mailbox mbox = it.next();
                    if (
                        MailboxConstants.type.personal.name().equals(mbox.getType()) && username.equals(mbox.getOwner())
                    ) {
                        personalMailbox = mbox;
                        // it.remove();
                        break;
                    }
                }
            }

            if (personalMailbox == null) {
                throw new Exception("mailBox non trouve pour l'utilisateur " + username);
            }

            currentMailbox = personalMailbox;

            // Création du dossier
            DocumentModel dossierDoc = session.createDocumentModel(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
            DocumentHelper.setProperties(session, dossierDoc, properties);
            DublincoreSchemaUtils.setTitle(dossierDoc, numeroNor);

            Dossier dossier = SSServiceLocator
                .getCaseDistributionService()
                .createEmptyCase(session, dossierDoc, currentMailbox, Dossier.class);

            final ActeService acteService = SolonEpgServiceLocator.getActeService();
            dossier.setIsDossierIssuInjectionP(true);
            if (typeActe.equalsIgnoreCase("Décret en CE")) {
                dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE);
            } else if (typeActe.equalsIgnoreCase("Décret en CM")) {
                dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CM);
            } else if (typeActe.equalsIgnoreCase("Décret en CE - art. 37-2")) {
                dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37);
            } else if (typeActe.equalsIgnoreCase("Décret du PR")) {
                dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_DECRET_PR);
            } else if (typeActe.equalsIgnoreCase("Décret en CE et en CM")) {
                dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM);
            } else if (typeActe.equalsIgnoreCase("Arrêté du PM")) {
                dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_PM);
            } else if (typeActe.equalsIgnoreCase("Arrêté du PR")) {
                dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_PR);
            } else if (typeActe.equalsIgnoreCase("Note de service")) {
                dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_NOTE);
            } else if (typeActe.equalsIgnoreCase("Demande d#avis au CE")) {
                dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE);
            } else {
                // Ajout des informations de création
                TypeActe type = acteService.getActeByLabel(typeActe);
                if (null != type) {
                    dossier.setTypeActe(type.getId());
                } else {
                    repriseLog.error("type d'acte non trouve -> " + typeActe);
                    throw new Exception("type d'acte non trouve -> " + typeActe);
                }
            }

            ConseilEtat conseilEtat = dossierDoc.getAdapter(ConseilEtat.class);

            try {
                if (dateTransmissionSectionCe != null && !dateTransmissionSectionCe.trim().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(this.getFormatter().parse(dateTransmissionSectionCe));
                    conseilEtat.setDateTransmissionSectionCe(calendar);
                }
                if (dateSectionCe != null && !dateSectionCe.trim().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(this.getFormatter().parse(dateSectionCe));
                    conseilEtat.setDateSectionCe(calendar);
                }
                if (dateAgCe != null && !dateAgCe.trim().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(this.getFormatter().parse(dateAgCe));
                    conseilEtat.setDateAgCe(calendar);
                }

                if (sectionCe != null && !sectionCe.trim().equals("")) {
                    OrganigrammeNode uniteStructurelleNode = STServiceLocator
                        .getSTUsAndDirectionService()
                        .getUniteStructurelleNode(sectionCe);
                    if (uniteStructurelleNode != null) {
                        conseilEtat.setSectionCe(uniteStructurelleNode.getLabel());
                    } else {
                        repriseLog.warn("section CE avec id " + sectionCe + " non trouve");
                    }
                }

                conseilEtat.save(session);

                if (dateSignature != null && !dateSignature.trim().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(this.getFormatter().parse(dateSignature));
                    dossier.setDateSignature(calendar);
                }
                if (datePreciseePublication != null && !datePreciseePublication.trim().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(this.getFormatter().parse(datePreciseePublication));
                    dossier.setDatePreciseePublication(calendar);
                }
                if (publicationDate != null && !publicationDate.trim().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(this.getFormatter().parse(publicationDate));
                    RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
                    retourDila.setDateParutionJorf(calendar);
                    retourDila.save(session);
                }
                if (dateSignaturePm != null && !dateSignaturePm.trim().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(this.getFormatter().parse(dateSignaturePm));
                    dossier.setDateSignature(calendar);
                }
                if (dateSignaturePr != null && !dateSignaturePr.trim().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(this.getFormatter().parse(dateSignaturePr));
                    dossier.setDateSignature(calendar);
                }
            } catch (ParseException e) {
                repriseLog.warn(
                    "la date est mal parsee: " +
                    System.getProperty("line.separator") +
                    "dateTransmissionSectionCe = " +
                    dateTransmissionSectionCe +
                    System.getProperty("line.separator") +
                    "dateSectionCe = " +
                    dateSectionCe +
                    System.getProperty("line.separator") +
                    "dateAgCe = " +
                    dateAgCe +
                    System.getProperty("line.separator") +
                    " dateSignature = " +
                    dateSignature +
                    System.getProperty("line.separator") +
                    " datePreciseePublication = " +
                    datePreciseePublication +
                    System.getProperty("line.separator") +
                    " publicationDate = " +
                    publicationDate +
                    System.getProperty("line.separator") +
                    " dateSignaturePm = " +
                    dateSignaturePm +
                    System.getProperty("line.separator") +
                    " dateSignaturePr = " +
                    dateSignaturePr
                );
            }

            List<String> indexationRubriqueList = new ArrayList<>();
            indexationRubriqueList.add("REPRISE");
            dossier.setIndexationRubrique(indexationRubriqueList);

            dossier.setNumeroNor(numeroNor);

            if (nomCompletChargesMissions != null && !nomCompletChargesMissions.trim().equals("")) {
                List<String> nomCompletChargesMissionsList = new ArrayList<>();
                nomCompletChargesMissionsList.add(nomCompletChargesMissions);
                dossier.setNomCompletChargesMissions(nomCompletChargesMissionsList);
            }
            if (nomCompletConseillersPms != null && !nomCompletConseillersPms.trim().equals("")) {
                List<String> nomCompletConseillersPmsList = new ArrayList<>();
                nomCompletConseillersPmsList.add(nomCompletConseillersPms);
                dossier.setNomCompletConseillersPms(nomCompletConseillersPmsList);
            }

            if (statut != null) {
                dossier.setStatut(statut);
            }

            posteId = dossier.getDirectionResp();

            String idCreateurDossier = dossier.getIdCreateurDossier();
            List<String> vecteurPublication = dossier.getVecteurPublication();

            // Crée le dossier
            dossier = createDossier(session, currentMailbox, dossier, posteId);
            dossier.setIdCreateurDossier(idCreateurDossier);
            dossier.setVecteurPublication(vecteurPublication);

            if (StringUtils.isNotBlank(loiDeRef)) {
                String[] loiDeRefList = loiDeRef.split("||");
                for (String autreLoiDeRef : loiDeRefList) {
                    DossierTransposition transposition = new DossierTranspositionImpl();
                    transposition.setRef(autreLoiDeRef);
                    dossier.setApplicationLoi(new TranspositionsContainerImpl(Arrays.asList(transposition)));
                }
            }

            if (StringUtils.isNotBlank(ordoDeRef)) {
                String[] transpositionOrdonnanceListRef = ordoDeRef.split("||");
                for (String transpositionOrdonnanceRef : transpositionOrdonnanceListRef) {
                    DossierTransposition transposition = new DossierTranspositionImpl();
                    transposition.setRef(transpositionOrdonnanceRef);
                    dossier.setTranspositionOrdonnance(new TranspositionsContainerImpl(Arrays.asList(transposition)));
                }
            }

            if (StringUtils.isNotBlank(dirDeRef)) {
                String[] transpositionDirectiveListRefList = dirDeRef.split("||");
                for (String transpositionDirectiveRef : transpositionDirectiveListRefList) {
                    DossierTransposition transposition = new DossierTranspositionImpl();
                    transposition.setRef(transpositionDirectiveRef);
                    dossier.setTranspositionDirective(new TranspositionsContainerImpl(Arrays.asList(transposition)));
                }
            }

            dossier.save(session);
            result = dossier.getDocument();

            // Event de rattachement de l'activite normative (post commit)
            EventProducer eventProducer = STServiceLocator.getEventProducer();
            Map<String, Serializable> eventProperties = new HashMap<>();
            eventProperties.put(
                SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_DOSSIER_DOCID_PARAM,
                dossier.getDocument().getId()
            );
            InlineEventContext inlineEventContext = new InlineEventContext(
                session,
                session.getPrincipal(),
                eventProperties
            );
            eventProducer.fireEvent(
                inlineEventContext.newEvent(SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_EVENT)
            );

            repriseLog.debug("Creation du dossier " + numeroNor + "-> OK");
        } catch (Exception e) {
            repriseLog.error("Creation du dossier " + numeroNor + "-> KO", e);
            throw new Exception("Erreur lors de la creation du dossier " + numeroNor, e);
        }
        return result;
    }

    /**
     * ceration du dossier
     *
     * @param session
     *            the curent session
     * @param currentMailbox
     *            le mailBox courant
     * @param dossier
     *            le dossier a creer
     * @param posteId
     *            le poste Id
     * @return
     */
    public Dossier createDossier(CoreSession session, Mailbox currentMailbox, Dossier dossier, String posteId) {
        // Initialise les ministère / direction de rattachement
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);

        dossier.setMinistereAttache(dossier.getMinistereResp());
        dossier.setDirectionAttache(dossier.getDirectionResp());

        try {
            EntiteNode ministere = SolonEpgServiceLocator
                .getEpgOrganigrammeService()
                .getMinistereFromNor(dossier.getNumeroNor().substring(0, 3));
            if (ministere != null) {
                traitementPapier.setRetourA(ministere.getFormule());
                traitementPapier.save(session);
            }
        } catch (Exception e) {
            repriseLog.warn("le champ 'retour a' n'a pas pu être rempli correctement");
        }

        dossier.setPublicationIntegraleOuExtrait(DossierSolonEpgConstants.DOSSIER_INTEGRAL_PROPERTY_VALUE);

        // Crée le parapheur (methode spécifique à l'injection)
        SolonEpgServiceLocator.getParapheurService().createAndFillParapheurInjection(dossier, session);

        // Crée le fond de dossier
        SolonEpgServiceLocator.getFondDeDossierService().createAndFillFondDossier(dossier, session);

        // Sauvegarde le dossier
        dossier.save(session);

        // Lève un événement de fin de création de dossier
        Map<String, Serializable> eventProperties = ImmutableMap.of(
            SolonEpgEventConstant.DOSSIER_EVENT_PARAM,
            dossier,
            SolonEpgEventConstant.POSTE_ID_EVENT_PARAM,
            posteId
        );
        InlineEventContext inlineEventContext = new InlineEventContext(
            session,
            session.getPrincipal(),
            eventProperties
        );

        EventProducer eventProducer = STServiceLocator.getEventProducer();
        eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgEventConstant.INJECTION_AFTER_DOSSIER_CREATED));

        return dossier;
    }

    public SimpleDateFormat getFormatter() {
        if (this.formatter == null) {
            this.formatter = new SimpleDateFormat(DATE_FORMAT);
        }
        return this.formatter;
    }
}
