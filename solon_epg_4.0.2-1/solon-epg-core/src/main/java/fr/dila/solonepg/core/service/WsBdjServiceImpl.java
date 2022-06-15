package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.dto.BilanSemestrielDTO;
import fr.dila.solonepg.api.dto.TexteBilanSemestrielDTO;
import fr.dila.solonepg.api.enums.BilanSemestrielType;
import fr.dila.solonepg.api.exception.WsBdjException;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.api.service.WsBdjService;
import fr.dila.solonepg.core.bdj.WsBdj;
import fr.dila.solonepg.core.bdj.WsBdjImpl;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.DecretApplicatifDTO;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.EcheancierLoisDTO;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.MesureApplicationDTO;
import fr.dila.solonepg.core.dto.activitenormative.injectionbdj.TexteMaitreDTO;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ProfileService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.core.util.StAXUtil;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class WsBdjServiceImpl implements WsBdjService {
    private static final STLogger LOGGER = STLogFactory.getLog(WsBdjServiceImpl.class);

    public WsBdjServiceImpl() {
        super();
    }

    @Override
    public boolean isPublicationEcheancierBdjActivated(CoreSession session) {
        STParametreService paramService = STServiceLocator.getSTParametreService();
        String flagAffichagePublierDossier = paramService.getParametreValue(
            session,
            SolonEpgParametreConstant.ACTIVATION_PUBLICATION_ECHEANCIER_BDJ
        );
        return BooleanUtils.toBoolean(flagAffichagePublierDossier);
    }

    protected WsBdj getWsBdj() {
        return new WsBdjImpl();
    }

    @Override
    public String publierEcheancierBDJ(DocumentModel currentDocument, CoreSession documentManager)
        throws WsBdjException, IOException, XMLStreamException {
        ArrayList<TexteMaitre> textesMaitres = new ArrayList<>();
        textesMaitres.add(currentDocument.getAdapter(TexteMaitre.class));

        return publierEcheancierBDJ(textesMaitres, documentManager);
    }

    @Override
    public String publierEcheancierBDJ(List<TexteMaitre> textesMaitres, CoreSession session)
        throws WsBdjException, IOException, XMLStreamException {
        // Générer le modèle objet injectionbdj
        ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();
        String echeancierXmlStr = buildEcheancierXmlStr(session, textesMaitres, activiteNormativeService);
        String status = StringUtils.EMPTY;

        if (echeancierXmlStr != null) {
            status = getWsBdj().publierEcheancierBDJ(session, echeancierXmlStr);

            for (TexteMaitre texteMaitre : textesMaitres) {
                // Mettre a jour la date d'injection du texte maitre
                texteMaitre.setDateInjection(Calendar.getInstance());
                texteMaitre.save(session);
            }
        }
        return status;
    }

    protected String buildEcheancierXmlStr(
        CoreSession documentManager,
        List<TexteMaitre> textesMaitres,
        ActiviteNormativeService activiteNormativeService
    )
        throws IOException, XMLStreamException {
        EcheancierLoisDTO echeancier = new EcheancierLoisDTO();

        // Récupération de la législature en cours et de la précédente
        SolonEpgParametreService paramService = SolonEpgServiceLocator.getSolonEpgParametreService();
        ParametrageAN param = paramService.getDocAnParametre(documentManager);
        String legislatureEnCours = param.getLegislatureEnCours();
        String legislaturePrecPublication = param.getLegislaturePrecPublication();

        for (TexteMaitre texteMaitre : textesMaitres) {
            // Filtrage des textes maitres selon la législature : ne sont
            // publiés que les textes maitres de la législature en cours ou de
            // la précédente
            String legislature = texteMaitre.getLegislaturePublication();
            if (
                StringUtils.isNotEmpty(legislature) &&
                (
                    legislature.equals(legislatureEnCours) ||
                    StringUtils.isNotEmpty(legislaturePrecPublication) &&
                    legislature.equals(legislaturePrecPublication)
                )
            ) {
                TexteMaitreDTO texteMaitreDTO = new TexteMaitreDTO(texteMaitre.getNumero(), texteMaitre.getNumeroNor());

                List<MesureApplicative> listMesures = activiteNormativeService.fetchMesure(
                    texteMaitre.getMesuresIds(),
                    documentManager
                );

                for (MesureApplicative mesure : listMesures) {
                    buildEcheancierXmlStrMesureApplicative(
                        documentManager,
                        activiteNormativeService,
                        texteMaitreDTO,
                        mesure
                    );
                }

                // Classer les mesures par ordre croissant du numéro d'ordre
                texteMaitreDTO.getMesures().sort(Comparator.comparing(MesureApplicationDTO::getNumero));

                echeancier.getTextes().add(texteMaitreDTO);
            }
        }

        if (echeancier.getTextes().isEmpty()) {
            return null;
        }

        // Générer le XML à partir du texte maitre
        return mapToXml(echeancier, documentManager);
    }

    private void buildEcheancierXmlStrMesureApplicative(
        CoreSession documentManager,
        ActiviteNormativeService activiteNormativeService,
        TexteMaitreDTO texteMaitreDTO,
        MesureApplicative mesure
    ) {
        String numOrdreStr = mesure.getNumeroOrdre();
        if (NumberUtils.isCreatable(numOrdreStr)) {
            int numOrdre = Integer.parseInt(numOrdreStr);

            if (numOrdre != 0) {
                MesureApplicationDTO mesureDTO = new MesureApplicationDTO();

                mesureDTO.setNumero(numOrdre);

                mesureDTO.setArticleTexte(mesure.getArticle());
                mesureDTO.setBaseLegale(mesure.getBaseLegale());

                Calendar dateObjPub = mesure.getDateObjectifPublication();
                if (dateObjPub != null) {
                    mesureDTO.setObjectif(SolonDateConverter.MONTH_YEAR.format(dateObjPub));
                }

                mesureDTO.setObjet(mesure.getObjetRIM());

                List<Decret> listDecret = activiteNormativeService.fetchDecrets(mesure.getDecretIds(), documentManager);
                for (Decret decret : listDecret) {
                    DecretApplicatifDTO decretDTO = new DecretApplicatifDTO();
                    decretDTO.setNor(decret.getNumeroNor());
                    decretDTO.setTitreLong(decret.getTitreOfficiel());

                    mesureDTO.getDecrets().add(decretDTO);
                }

                texteMaitreDTO.getMesures().add(mesureDTO);
            }
        }
    }

    @Override
    public void publierBilanSemestrielBDJ(
        BilanSemestrielDTO bilanSemestriel,
        String legislatureEnCours,
        CoreSession session
    ) {
        try {
            getWsBdj()
                .publierBilanSemestrielBDJ(session, buildBilanXmlStr(bilanSemestriel, legislatureEnCours, session));
        } catch (WsBdjException e) {
            // do nothing already logged by WsBdj
        } catch (IOException | XMLStreamException e) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_INJECTION_TM_BDJ_TEC, e);
        }
    }

    private void buildStAXEcheancier(
        XMLStreamWriter xMLStreamWriter,
        EcheancierLoisDTO echeancier,
        CoreSession session
    ) {
        try {
            xMLStreamWriter.writeStartElement("textes");

            StAXUtil.writeAttributeXmlnsXsi(xMLStreamWriter);

            for (TexteMaitreDTO texteMaitre : echeancier.getTextes()) {
                xMLStreamWriter.writeStartElement("Texte");
                xMLStreamWriter.writeAttribute("id", StringUtils.defaultString(texteMaitre.getId()));
                xMLStreamWriter.writeAttribute("nor", texteMaitre.getNor());

                for (MesureApplicationDTO mesure : texteMaitre.getMesures()) {
                    xMLStreamWriter.writeStartElement("mesure");
                    if (mesure.getNumero() != null) {
                        xMLStreamWriter.writeAttribute("num", Integer.toString(mesure.getNumero()));
                    }
                    StAXUtil.appendTextNode(
                        xMLStreamWriter,
                        "articleTexte",
                        StringUtils.defaultString(mesure.getArticleTexte())
                    );
                    StAXUtil.appendTextNode(
                        xMLStreamWriter,
                        "baseLegale",
                        StringUtils.defaultString(mesure.getBaseLegale())
                    );
                    StAXUtil.appendTextNode(xMLStreamWriter, "objet", StringUtils.defaultString(mesure.getObjet()));
                    StAXUtil.appendTextNode(
                        xMLStreamWriter,
                        "objectif",
                        StringUtils.defaultString(mesure.getObjectif())
                    );

                    for (DecretApplicatifDTO decret : mesure.getDecrets()) {
                        xMLStreamWriter.writeStartElement("decret");

                        StAXUtil.appendTextNode(xMLStreamWriter, "nor", decret.getNor());

                        if (decret.getTitreLong() != null) {
                            StAXUtil.appendTextNode(xMLStreamWriter, "titre_long", decret.getTitreLong());
                        } else {
                            xMLStreamWriter.writeEmptyElement("titre_long");
                        }

                        xMLStreamWriter.writeEndElement(); // </decret>
                    }

                    xMLStreamWriter.writeEndElement(); // </mesure>
                }

                xMLStreamWriter.writeEndElement(); // </Texte>
            }

            xMLStreamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_INJECTION_TM_BDJ_TEC, e);
        }
    }

    private String mapToXml(EcheancierLoisDTO echeancier, CoreSession session) throws IOException, XMLStreamException {
        return StAXUtil.buildXml(xMLStreamWriter -> buildStAXEcheancier(xMLStreamWriter, echeancier, session));
    }

    protected String buildBilanXmlStr(
        BilanSemestrielDTO bilanSemestriel,
        String legislatureEnCours,
        CoreSession session
    )
        throws IOException, XMLStreamException {
        return StAXUtil.buildXml(
            xMLStreamWriter -> buildStAXBilan(xMLStreamWriter, bilanSemestriel, legislatureEnCours, session)
        );
    }

    private void buildStAXBilan(
        XMLStreamWriter xMLStreamWriter,
        BilanSemestrielDTO bilanSemestriel,
        String legislatureEnCours,
        CoreSession session
    ) {
        String strXmlDateDebutLois = SolonDateConverter.DATE_DASH_REVERSE.format(
            bilanSemestriel.getDateDebutIntervalleTextesPublies()
        );
        String strXmlDatestrFinLois = SolonDateConverter.DATE_DASH_REVERSE.format(
            bilanSemestriel.getDateFinIntervalleTextesPublies()
        );
        String strXmlDateFinMesures = SolonDateConverter.DATE_DASH_REVERSE.format(
            bilanSemestriel.getDateFinIntervalleMesures()
        );

        String strHDateDebutLois = SolonDateConverter.DATE_SLASH.format(
            bilanSemestriel.getDateDebutIntervalleTextesPublies()
        );
        String strHDateDebutMesures = SolonDateConverter.DATE_SLASH.format(
            bilanSemestriel.getDateDebutIntervalleMesures()
        );
        String strHDatestrFinLois = SolonDateConverter.DATE_SLASH.format(
            bilanSemestriel.getDateFinIntervalleTextesPublies()
        );
        String strHDateFinMesures = SolonDateConverter.DATE_SLASH.format(bilanSemestriel.getDateFinIntervalleMesures());
        String strDateBilan = SolonDateConverter.DATE_SLASH.format(bilanSemestriel.getDateBilan());

        try {
            xMLStreamWriter.writeStartElement("bilan");

            xMLStreamWriter.writeAttribute("texte_type", bilanSemestriel.getType().getLabel());
            StAXUtil.writeAttributeXmlnsXsi(xMLStreamWriter);

            appendInjectionTitle(
                xMLStreamWriter,
                bilanSemestriel,
                strHDateDebutLois,
                strHDateDebutMesures,
                strHDatestrFinLois,
                strHDateFinMesures,
                strDateBilan
            );

            StAXUtil.appendTextNode(xMLStreamWriter, "date_debut_intervalle", strXmlDateDebutLois);
            StAXUtil.appendTextNode(xMLStreamWriter, "date_fin_intervalle_textes_publies", strXmlDatestrFinLois);
            StAXUtil.appendTextNode(xMLStreamWriter, "date_fin_intervalle_mesures", strXmlDateFinMesures);
            StAXUtil.appendTextNode(
                xMLStreamWriter,
                "titre_court_lgf",
                ResourceHelper.getString(
                    "wsbdj.injection.titre.court.lgf",
                    SolonDateConverter.getClientConverter().format(bilanSemestriel.getDateBilan())
                )
            );

            xMLStreamWriter.writeStartElement("texte_edito_lgf");
            if (bilanSemestriel.getType() == BilanSemestrielType.LOI) {
                buildTexteEditolgfLoi(
                    legislatureEnCours,
                    bilanSemestriel.getDateFinIntervalleMesures(),
                    bilanSemestriel.getDateDebutIntervalleMesures(),
                    bilanSemestriel.getDateFinIntervalleTextesPublies(),
                    xMLStreamWriter
                );
            } else {
                buildTexteEditolgfOrdonnance(
                    legislatureEnCours,
                    bilanSemestriel.getDateFinIntervalleMesures(),
                    bilanSemestriel.getDateDebutIntervalleMesures(),
                    bilanSemestriel.getDateFinIntervalleTextesPublies(),
                    xMLStreamWriter
                );
            }
            xMLStreamWriter.writeEndElement();

            if (bilanSemestriel.getType() == BilanSemestrielType.LOI) {
                StAXUtil.appendTextNode(
                    xMLStreamWriter,
                    "titre_col_1",
                    ResourceHelper.getString("wsbdj.injection.titre.col.1.type.loi")
                );
            } else {
                StAXUtil.appendTextNode(
                    xMLStreamWriter,
                    "titre_col_1",
                    ResourceHelper.getString("wsbdj.injection.titre.col.1.type.ordonnance")
                );
            }

            StAXUtil.appendTextNode(
                xMLStreamWriter,
                "titre_col_2",
                ResourceHelper.getString("wsbdj.injection.titre.col.2")
            );
            StAXUtil.appendTextNode(
                xMLStreamWriter,
                "titre_col_3",
                ResourceHelper.getString("wsbdj.injection.titre.col.3")
            );
            StAXUtil.appendTextNode(
                xMLStreamWriter,
                "titre_col_4",
                ResourceHelper.getString("wsbdj.injection.titre.col.4")
            );
            StAXUtil.appendTextNode(
                xMLStreamWriter,
                "titre_col_5",
                ResourceHelper.getString("wsbdj.injection.titre.col.5")
            );

            xMLStreamWriter.writeStartElement("textes");
            bilanSemestriel
                .getListeTextes()
                .forEach(
                    bilan -> {
                        try {
                            buildTexteXmlStr(bilan, xMLStreamWriter);
                        } catch (XMLStreamException e) {
                            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_INJECTION_TM_BDJ_TEC, e);
                        }
                    }
                );
            xMLStreamWriter.writeEndElement(); // </textes>

            xMLStreamWriter.writeEndElement(); // </bilan>
        } catch (XMLStreamException e) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_INJECTION_TM_BDJ_TEC, e);
        }
    }

    /** Injecte dans le writer l'élément
     *
     * <texte_pan><p> Bilan semestriel ...</p></texte_pan>
     */
    private void appendInjectionTitle(
        XMLStreamWriter xMLStreamWriter,
        BilanSemestrielDTO bilanSemestriel,
        String strHDateDebutLois,
        String strHDateDebutMesures,
        String strHDatestrFinLois,
        String strHDateFinMesures,
        String strDateBilan
    )
        throws XMLStreamException {
        String par;
        if (bilanSemestriel.getType() == BilanSemestrielType.LOI) {
            par =
                ResourceHelper.getString(
                    "wsbdj.injection.texte.pan.type.loi",
                    strDateBilan,
                    strHDateDebutLois,
                    strHDatestrFinLois,
                    strHDateDebutMesures,
                    strHDateFinMesures
                );
        } else {
            par =
                ResourceHelper.getString(
                    "wsbdj.injection.texte.pan.type.ordonnance",
                    strDateBilan,
                    strHDateDebutLois,
                    strHDatestrFinLois,
                    strHDateDebutMesures,
                    strHDateFinMesures
                );
        }

        xMLStreamWriter.writeStartElement("texte_pan");
        StAXUtil.appendTextNode(xMLStreamWriter, "p", " " + par);
        xMLStreamWriter.writeEndElement();
    }

    /**
     * Calcul le tag texte_edito_lgf pour l'export des bilans semestriels ordonnance
     * xml à la BDJ
     *
     * @throws XMLStreamException
     */
    private void buildTexteEditolgfOrdonnance(
        String numeroLegislatureCourante,
        Date dateFinIntervalleMesures,
        Date dateDebutIntervalle,
        Date dateFinIntervalleTextesPublies,
        XMLStreamWriter xMLStreamWriter
    )
        throws XMLStreamException {
        String rangBilan = calculRangBilanLegislature(dateDebutIntervalle, dateFinIntervalleTextesPublies);

        String texte1 = ResourceHelper.getString(
            "wsbdj.injection.texte.edito.lgf.1.type.ordonnance",
            rangBilan,
            numeroLegislatureCourante,
            SolonDateConverter.getClientConverter().format(dateFinIntervalleMesures),
            SolonDateConverter.getClientConverter().format(dateDebutIntervalle),
            SolonDateConverter.getClientConverter().format(dateFinIntervalleTextesPublies)
        );
        StAXUtil.appendTextNode(xMLStreamWriter, "p", texte1);

        String texte2 = ResourceHelper.getString(
            "wsbdj.injection.texte.edito.lgf.2.type.ordonnance",
            SolonDateConverter.getClientConverter().format(dateFinIntervalleMesures)
        );
        StAXUtil.appendTextNode(xMLStreamWriter, "p", texte2);

        String texte3 = ResourceHelper.getString(
            "wsbdj.injection.texte.edito.lgf.3.type.ordonnance",
            SolonDateConverter.getClientConverter().format(dateFinIntervalleMesures),
            numeroLegislatureCourante
        );
        StAXUtil.appendTextNode(xMLStreamWriter, "p", texte3);
    }

    /**
     * Calcul le tag texte_edito_lgf pour l'export des bilans semestriels lois xml à
     * la BDJ
     *
     * @throws XMLStreamException
     */
    private void buildTexteEditolgfLoi(
        String numeroLegislatureCourante,
        Date dateFinIntervalleMesures,
        Date dateDebutIntervalle,
        Date dateFinIntervalleTextesPublies,
        XMLStreamWriter xMLStreamWriter
    )
        throws XMLStreamException {
        String rangBilan = calculRangBilanLegislature(dateDebutIntervalle, dateFinIntervalleTextesPublies);

        String texte1 = ResourceHelper.getString(
            "wsbdj.injection.texte.edito.lgf.1.type.loi",
            rangBilan,
            numeroLegislatureCourante,
            SolonDateConverter.getClientConverter().format(dateFinIntervalleMesures),
            SolonDateConverter.getClientConverter().format(dateDebutIntervalle),
            SolonDateConverter.getClientConverter().format(dateFinIntervalleTextesPublies)
        );

        StAXUtil.appendTextNode(xMLStreamWriter, "p", texte1);

        String texte2 = ResourceHelper.getString(
            "wsbdj.injection.texte.edito.lgf.2.type.loi",
            SolonDateConverter.getClientConverter().format(dateFinIntervalleMesures)
        );

        StAXUtil.appendTextNode(xMLStreamWriter, "p", texte2);

        String texte3 = ResourceHelper.getString(
            "wsbdj.injection.texte.edito.lgf.3.type.loi",
            SolonDateConverter.getClientConverter().format(dateFinIntervalleMesures),
            numeroLegislatureCourante
        );

        StAXUtil.appendTextNode(xMLStreamWriter, "p", texte3);
    }

    private void buildTexteXmlStr(TexteBilanSemestrielDTO texteBilanSemestriel, XMLStreamWriter xMLStreamWriter)
        throws XMLStreamException {
        final NumberFormat tauxFormater = NumberFormat.getInstance(Locale.ENGLISH);
        tauxFormater.setMaximumFractionDigits(2);

        xMLStreamWriter.writeEmptyElement("texte");
        xMLStreamWriter.writeAttribute("texte_type", texteBilanSemestriel.getType());
        xMLStreamWriter.writeAttribute("titre", texteBilanSemestriel.getTitre());
        xMLStreamWriter.writeAttribute("mesures_attendues", Long.toString(texteBilanSemestriel.getMesuresAttendues()));
        xMLStreamWriter.writeAttribute(
            "mesures_appliquees",
            Long.toString(texteBilanSemestriel.getMesuresAppliquees())
        );
        xMLStreamWriter.writeAttribute("taux", tauxFormater.format(texteBilanSemestriel.getTaux()));
        xMLStreamWriter.writeAttribute("mesures_en_attente", Long.toString(texteBilanSemestriel.getMesuresEnAttente()));
    }

    /**
     * Calcule le rang de la législature.
     *
     * @param dateDebutIntervalle
     * @param dateFinIntervalleTextesPublies
     * @return
     */
    private String calculRangBilanLegislature(Date dateDebutIntervalle, Date dateFinIntervalleTextesPublies) {
        Calendar calDebutIntervalle = DateUtil.toCalendarFromNotNullDate(dateDebutIntervalle);
        calDebutIntervalle.add(Calendar.MONTH, 6);

        Calendar calFinIntervalleTextesPublies = DateUtil.toCalendarFromNotNullDate(dateFinIntervalleTextesPublies);

        for (int rangBilan = 0; rangBilan <= 9; rangBilan++) {
            if (calDebutIntervalle.after(calFinIntervalleTextesPublies)) {
                int rangReturn = rangBilan - 1;
                if (rangReturn == 1) {
                    return "1er";
                } else {
                    return String.valueOf(rangReturn) + "ème";
                }
            }
            calDebutIntervalle.add(Calendar.MONTH, 6);
        }

        return "0";
    }

    @Override
    public void sendTransferErrorMailToAdmins(Exception exc, CoreSession session) {
        try {
            final ProfileService profileService = STServiceLocator.getProfileService();
            final List<STUser> users = profileService.getUsersFromBaseFunction(
                STBaseFunctionConstant.ADMIN_FONCTIONNEL_EMAIL_RECEIVER
            );

            final String object = ResourceHelper.getString("wsbdj.injection.error.mail.object");
            final String text = ResourceHelper.getString("wsbdj.injection.error.mail.text", exc.getMessage());

            STServiceLocator.getSTMailService().sendMailToUserList(users, object, text);
        } catch (final Exception e) {
            LOGGER.error(
                session,
                STLogEnumImpl.FAIL_SEND_MAIL_TEC,
                "Erreur d'envoi du mail lors de l'injection BDJ",
                e
            );
        }
    }
}
