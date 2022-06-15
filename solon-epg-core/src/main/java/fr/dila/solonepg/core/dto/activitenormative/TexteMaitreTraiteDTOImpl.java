package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.cases.Traite;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.TexteMaitreTraiteDTO;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.util.DateUtil;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class TexteMaitreTraiteDTOImpl extends AbstractMapDTO implements TexteMaitreTraiteDTO {
    private static final long serialVersionUID = 78416213510924898L;
    private static final Log log = LogFactory.getLog(TexteMaitreTraiteDTOImpl.class);

    @Override
    public String getType() {
        return TexteMaitre.class.getSimpleName();
    }

    @Override
    public String getDocIdForSelection() {
        return getId();
    }

    public class DecretTraite implements Serializable {
        private String numeroNor;
        private String etapeSolon;
        private String numero;
        private Date datePublication;
        private String titreOfficiel;

        public DecretTraite(String norDecret, CoreSession session) {
            if (StringUtils.isNotBlank(norDecret)) {
                ActiviteNormative activiteNormative = SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .findOrCreateActiviteNormativeByNor(session, norDecret);
                TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
                numeroNor = texteMaitre.getNumeroNor();
                etapeSolon = texteMaitre.getEtapeSolon();
                numero = texteMaitre.getNumero();
                datePublication = DateUtil.toDate(texteMaitre.getDatePublication());
                titreOfficiel = texteMaitre.getTitreOfficiel();
            }
        }

        public void setNumeroNor(String numeroNor) {
            this.numeroNor = numeroNor;
        }

        public String getNumeroNor() {
            return numeroNor;
        }

        public String getEtapeSolon() {
            return etapeSolon;
        }

        public void setEtapeSolon(String etapeSolon) {
            this.etapeSolon = etapeSolon;
        }

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public Calendar getDatePublication() {
            return DateUtil.toCalendar(this.datePublication);
        }

        public void setDatePublication(Date datePublication) {
            this.datePublication = DateUtil.copyDate(datePublication);
        }

        public String getTitreOfficiel() {
            return titreOfficiel;
        }

        public void setTitreOfficiel(String titreOfficiel) {
            this.titreOfficiel = titreOfficiel;
        }
    }

    public class RatificationTraite implements Serializable {
        private String numeroNor;
        private String etapeSolon;
        private String numero;
        private Date dateCreation;
        private String titreOfficiel;
        private Date dateSaisineCE;
        private Date datePublication;

        public RatificationTraite(String norLoiRatification, CoreSession session) {
            if (StringUtils.isNotBlank(norLoiRatification)) {
                ActiviteNormative activiteNormative = SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .findOrCreateActiviteNormativeByNor(session, norLoiRatification);
                TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);

                etapeSolon = texteMaitre.getEtapeSolon();
                numero = texteMaitre.getNumero();
                dateCreation = DateUtil.toDate(texteMaitre.getDateCreation());

                LoiDeRatification loiDeRatification = activiteNormative.getAdapter(LoiDeRatification.class);
                numeroNor = loiDeRatification.getNumeroNor();
                titreOfficiel = texteMaitre.getTitreOfficiel();
                dateSaisineCE = DateUtil.toDate(loiDeRatification.getDateSaisineCE());
                datePublication = DateUtil.toDate(loiDeRatification.getDatePublication());
            }
        }

        public String getNumeroNor() {
            return numeroNor;
        }

        public void setNumeroNor(String numeroNor) {
            this.numeroNor = numeroNor;
        }

        public String getEtapeSolon() {
            return etapeSolon;
        }

        public void setEtapeSolon(String etapeSolon) {
            this.etapeSolon = etapeSolon;
        }

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public Date getDateCreation() {
            return this.dateCreation;
        }

        public void setDateCreation(Date dateCreation) {
            this.dateCreation = DateUtil.copyDate(dateCreation);
        }

        public String getTitreOfficiel() {
            return titreOfficiel;
        }

        public void setTitreOfficiel(String titreOfficiel) {
            this.titreOfficiel = titreOfficiel;
        }

        public Date getDateSaisineCE() {
            return dateSaisineCE;
        }

        public void setDateSaisineCE(Date dateSaisineCE) {
            this.dateSaisineCE = DateUtil.copyDate(dateSaisineCE);
        }

        public Date getDatePublication() {
            return datePublication;
        }

        public void setDatePublication(Date datePublication) {
            this.datePublication = DateUtil.copyDate(datePublication);
        }
    }

    public TexteMaitreTraiteDTOImpl(ActiviteNormative activiteNormative, CoreSession session) {
        if (activiteNormative == null) {
            return;
        }

        setId(activiteNormative.getDocument().getId());

        Traite traite = activiteNormative.getAdapter(Traite.class);

        this.setCategorie(traite.getCategorie());
        this.setOrganisation(traite.getOrganisation());
        this.setThematique(traite.getThematique());
        this.setTitreActe(traite.getTitreActe());
        this.setDateSignature(DateUtil.toDate(traite.getDateSignature()));
        this.setDateEntreeEnVigueur(DateUtil.toDate(traite.getDateEntreeEnVigueur()));
        this.setAutorisationRatification(traite.hasAutorisationRatification());
        this.setPublication(traite.isPublication());
        this.setObservation(traite.getObservation());

        this.setMinisterePilote(traite.getMinisterePilote());

        this.setDegrePriorite(traite.getDegrePriorite());
        this.setDatePJL(DateUtil.toDate(traite.getDatePJL()));
        this.setEtudeImpact(traite.hasEtudeImpact());
        this.setDispoEtudeImpact(traite.isDispoEtudeImpact());

        // decret
        setDecretTraite(new DecretTraite(traite.getNorDecret(), session));

        // ratification
        setRatificationTraite(new RatificationTraite(traite.getNorLoiRatification(), session));

        EPGFeuilleRouteService feuilleRouteService = SolonEpgServiceLocator.getEPGFeuilleRouteService();

        String numeroNor = this.getAutorisationRatification()
            ? getRatificationTraite().numeroNor
            : getDecretTraite().numeroNor;
        Dossier dossier = numeroNor != null
            ? SolonEpgServiceLocator.getNORService().findDossierFromNOR(session, numeroNor)
            : null;

        if (dossier != null) {
            DocumentModel etapeDoc = feuilleRouteService.getEtapeInitialisationDossier(session, dossier.getDocument());
            if (etapeDoc != null) {
                SolonEpgRouteStep epgRouteStep = etapeDoc.getAdapter(SolonEpgRouteStep.class);
                this.setDateDepotRatification(DateUtil.toDate(epgRouteStep.getDateFinEtape()));
            }
        }
    }

    public void setDecretTraite(DecretTraite decretTraite) {
        put("decretTraite", decretTraite);
    }

    public DecretTraite getDecretTraite() {
        return (DecretTraite) get("decretTraite");
    }

    public void setRatificationTraite(RatificationTraite ratificationTraite) {
        put("ratificationTraite", ratificationTraite);
    }

    public RatificationTraite getRatificationTraite() {
        return (RatificationTraite) get("ratificationTraite");
    }

    @Override
    public String getId() {
        return getString(TexteMaitreConstants.ID);
    }

    @Override
    public void setId(String id) {
        put(TexteMaitreConstants.ID, id);
    }

    @Override
    public String getCategorie() {
        return getString(TexteMaitreConstants.CATEGORIE);
    }

    @Override
    public void setCategorie(String categorie) {
        put(TexteMaitreConstants.CATEGORIE, categorie);
    }

    @Override
    public String getOrganisation() {
        return getString(TexteMaitreConstants.ORGANISATION);
    }

    @Override
    public void setOrganisation(String organisation) {
        put(TexteMaitreConstants.ORGANISATION, organisation);
    }

    @Override
    public String getThematique() {
        return getString(TexteMaitreConstants.THEMATIQUE);
    }

    @Override
    public void setThematique(String thematique) {
        put(TexteMaitreConstants.THEMATIQUE, thematique);
    }

    @Override
    public String getTitreActe() {
        return getString(TexteMaitreConstants.TITRE_ACTE);
    }

    @Override
    public void setTitreActe(String titreActe) {
        put(TexteMaitreConstants.TITRE_ACTE, titreActe);
    }

    @Override
    public Date getDateSignature() {
        return getDate(TexteMaitreConstants.DATE_SIGNATURE);
    }

    @Override
    public void setDateSignature(Date dateSignature) {
        put(TexteMaitreConstants.DATE_SIGNATURE, dateSignature);
    }

    @Override
    public Date getDateEntreeEnVigueur() {
        return getDate(TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR);
    }

    @Override
    public void setDateEntreeEnVigueur(Date dateEntreeEnVigueur) {
        put(TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR, dateEntreeEnVigueur);
    }

    @Override
    public Boolean getAutorisationRatification() {
        return getBoolean(TexteMaitreConstants.AUTORISATION_RATIFICATION);
    }

    @Override
    public void setAutorisationRatification(Boolean autorisationRatification) {
        put(TexteMaitreConstants.AUTORISATION_RATIFICATION, autorisationRatification);
    }

    @Override
    public Boolean getPublication() {
        return getBoolean(TexteMaitreConstants.PUBLICATION);
    }

    @Override
    public void setPublication(Boolean publication) {
        put(TexteMaitreConstants.PUBLICATION, publication);
    }

    @Override
    public String getObservation() {
        return getString(TexteMaitreConstants.OBSERVATION);
    }

    @Override
    public void setObservation(String observation) {
        put(TexteMaitreConstants.OBSERVATION, observation);
    }

    @Override
    public String getMinisterePilote() {
        return getString(TexteMaitreConstants.MINISTERE_PILOTE);
    }

    @Override
    public void setMinisterePilote(String ministerePilote) {
        put(TexteMaitreConstants.MINISTERE_PILOTE, ministerePilote);
    }

    @Override
    public String getDegrePriorite() {
        return getString(TexteMaitreConstants.DEGRE_PRIORITE);
    }

    @Override
    public void setDegrePriorite(String degrePriorite) {
        put(TexteMaitreConstants.DEGRE_PRIORITE, degrePriorite);
    }

    @Override
    public Date getDatePJL() {
        return getDate(TexteMaitreConstants.DATE_PJL);
    }

    @Override
    public void setDatePJL(Date datePJL) {
        put(TexteMaitreConstants.DATE_PJL, datePJL);
    }

    @Override
    public Boolean getEtudeImpact() {
        return getBoolean(TexteMaitreConstants.ETUDE_IMPACT);
    }

    @Override
    public void setEtudeImpact(Boolean etudeImpact) {
        put(TexteMaitreConstants.ETUDE_IMPACT, etudeImpact);
    }

    @Override
    public Boolean getDispoEtudeImpact() {
        return getBoolean(TexteMaitreConstants.DISPO_ETUDE_IMPACT);
    }

    @Override
    public void setDispoEtudeImpact(Boolean dispoEtudeImpact) {
        put(TexteMaitreConstants.DISPO_ETUDE_IMPACT, dispoEtudeImpact);
    }

    @Override
    public Date getDateDepotRatification() {
        return getDate(TexteMaitreConstants.DATE_DEPOT_RATIFICATION);
    }

    @Override
    public void setDateDepotRatification(Date dateDepotRatification) {
        put(TexteMaitreConstants.DATE_DEPOT_RATIFICATION, dateDepotRatification);
    }

    /**
     * refresh du DTO (decret et loi de ratification lié via le nor)
     *
     * @param session
     */

    @Override
    public void refresh(CoreSession session) {
        DecretTraite decretTraite = getDecretTraite();
        RatificationTraite ratificationTraite = getRatificationTraite();
        if (StringUtils.isNotBlank(decretTraite.numeroNor)) {
            Dossier dossier = SolonEpgServiceLocator
                .getNORService()
                .findDossierFromNOR(session, decretTraite.numeroNor);
            if (dossier != null) {
                RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);

                decretTraite.numeroNor = dossier.getNumeroNor();
                decretTraite.etapeSolon = setEtapeSolon(dossier, session);
                decretTraite.numero = retourDila.getNumeroTexteParutionJorf();
                decretTraite.datePublication = DateUtil.toDate(retourDila.getDateParutionJorf());
                decretTraite.titreOfficiel = retourDila.getTitreOfficiel();
            }
        }

        if (StringUtils.isNotBlank(ratificationTraite.numeroNor)) {
            Dossier dossier = SolonEpgServiceLocator
                .getNORService()
                .findDossierFromNOR(session, ratificationTraite.numeroNor);
            if (dossier != null) {
                RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);

                ratificationTraite.etapeSolon = setEtapeSolon(dossier, session);
                ratificationTraite.dateCreation = DateUtil.toDate(dossier.getDateCreation());
                ratificationTraite.numeroNor = dossier.getNumeroNor();
                ratificationTraite.titreOfficiel = retourDila.getTitreOfficiel();

                ratificationTraite.numero = retourDila.getNumeroTexteParutionJorf();
                ratificationTraite.datePublication = DateUtil.toDate(retourDila.getDateParutionJorf());

                ConseilEtat conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);

                ratificationTraite.dateSaisineCE = DateUtil.toDate(conseilEtat.getDateSaisineCE());
            }
        }
    }

    private String setEtapeSolon(Dossier dossier, CoreSession session) {
        try {
            List<String> listStepLabel = SolonEpgServiceLocator
                .getCorbeilleService()
                .findCurrentStepsLabel(session, dossier.getDocument().getId());
            return StringUtils.join(listStepLabel, ", ");
        } catch (Exception e) {
            log.error(
                "Erreur lors de la construction de l'etapeSolon pour le dossier " + dossier.getDocument().getId()
            );
        }

        return null;
    }

    @Override
    public DocumentModel remapField(TexteMaitre texteMaitre, CoreSession session) {
        Traite traite = texteMaitre.getAdapter(Traite.class);

        traite.setCategorie(this.getCategorie());
        traite.setOrganisation(this.getOrganisation());
        traite.setThematique(this.getThematique());
        traite.setTitreActe(this.getTitreActe());

        traite.setDateSignature(DateUtil.toCalendar(this.getDateSignature()));
        traite.setDateEntreeEnVigueur(DateUtil.toCalendar(this.getDateEntreeEnVigueur()));

        traite.setAutorisationRatification(this.getAutorisationRatification());
        traite.setPublication(this.getPublication());
        traite.setObservation(this.getObservation());

        traite.setMinisterePilote(this.getMinisterePilote());

        traite.setDegrePriorite(this.getDegrePriorite());

        traite.setDatePJL(DateUtil.toCalendar(this.getDatePJL()));

        traite.setEtudeImpact(this.getEtudeImpact());
        traite.setDispoEtudeImpact(this.getDispoEtudeImpact());

        traite.setDateDepotRatification(DateUtil.toCalendar(this.getDateDepotRatification()));

        traite.setNorDecret(getDecretTraite().numeroNor);

        traite.setNorLoiRatification(getRatificationTraite().numeroNor);

        return traite.getDocument();
    }

    @Override
    public DocumentModel remapDecret(CoreSession session) {
        DecretTraite decretTraite = getDecretTraite();
        if (StringUtils.isNotBlank(decretTraite.numeroNor)) {
            ActiviteNormative activiteNormative = SolonEpgServiceLocator
                .getActiviteNormativeService()
                .findOrCreateActiviteNormativeByNor(session, decretTraite.numeroNor);
            TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
            texteMaitre.setNumeroNor(decretTraite.numeroNor);
            texteMaitre.setEtapeSolon(decretTraite.etapeSolon);
            texteMaitre.setNumero(decretTraite.numero);
            texteMaitre.setDatePublication(DateUtil.toCalendar(decretTraite.datePublication));
            texteMaitre.setTitreOfficiel(decretTraite.getTitreOfficiel());
            return texteMaitre.getDocument();
        }

        return null;
    }

    @Override
    public DocumentModel remapRatification(CoreSession session) {
        RatificationTraite ratificationTraite = getRatificationTraite();
        if (StringUtils.isNotBlank(ratificationTraite.numeroNor)) {
            ActiviteNormative activiteNormative = SolonEpgServiceLocator
                .getActiviteNormativeService()
                .findOrCreateActiviteNormativeByNor(session, ratificationTraite.numeroNor);
            TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
            LoiDeRatification loiDeRatification = activiteNormative.getAdapter(LoiDeRatification.class);

            loiDeRatification.setNumeroNor(ratificationTraite.numeroNor);
            texteMaitre.setEtapeSolon(ratificationTraite.etapeSolon);
            texteMaitre.setNumero(ratificationTraite.numero);
            texteMaitre.setDateCreation(DateUtil.toCalendar(ratificationTraite.dateCreation));

            loiDeRatification.setTitreOfficiel(ratificationTraite.getTitreOfficiel());
            loiDeRatification.setDateSaisineCE(DateUtil.toCalendar(ratificationTraite.dateCreation));
            loiDeRatification.setDatePublication(DateUtil.toCalendar(ratificationTraite.datePublication));

            return loiDeRatification.getDocument();
        }

        return null;
    }
}
