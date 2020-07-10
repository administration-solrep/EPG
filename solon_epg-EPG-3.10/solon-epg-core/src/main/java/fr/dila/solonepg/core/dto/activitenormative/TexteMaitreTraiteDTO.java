package fr.dila.solonepg.core.dto.activitenormative;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.cases.Traite;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.service.FeuilleRouteService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;

public class TexteMaitreTraiteDTO implements Serializable {

    private static final long serialVersionUID = 78416213510924898L;
    private static final Log log = LogFactory.getLog(TexteMaitreTraiteDTO.class);

    private String id;
    private String categorie;
    private String organisation;
    private String thematique;
    private String titreActe;
    private Date dateSignature;
    private Date dateEntreeEnVigueur;
    private Boolean autorisationRatification;
    private Boolean publication;
    private String observation;

    private String ministerePilote;
    private String degrePriorite;
    private Date datePJL;
    private Boolean etudeImpact;
    private Boolean dispoEtudeImpact;
    private Date dateDepotRatification;

    private RatificationTraite ratificationTraite;

    private DecretTraite decretTraite;

    public class DecretTraite {
        private String numeroNor;
        private String etapeSolon;
        private String numero;
        private Date datePublication;
        private String titreOfficiel;

        public DecretTraite(String norDecret, CoreSession session) throws ClientException {
            if (StringUtils.isNotBlank(norDecret)) {
                ActiviteNormative activiteNormative = SolonEpgServiceLocator.getActiviteNormativeService().findOrCreateActiviteNormativeByNor(
                        session, norDecret);
                TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
                numeroNor = texteMaitre.getNumeroNor();
                etapeSolon = texteMaitre.getEtapeSolon();
                numero = texteMaitre.getNumero();
                datePublication = texteMaitre.getDatePublication()!= null ? texteMaitre.getDatePublication().getTime() : null;
                titreOfficiel = texteMaitre.getTitreOfficiel()  ;
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

        public Date getDatePublication() {
            return DateUtil.copyDate(this.datePublication);
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

    public class RatificationTraite {
        private String numeroNor;
        private String etapeSolon;
        private String numero;
        private Date dateCreation;
        private String titreOfficiel;
        private Date dateSaisineCE;
        private Date datePublication;

        public RatificationTraite(String norLoiRatification, CoreSession session) throws ClientException {
            if (StringUtils.isNotBlank(norLoiRatification)) {
                ActiviteNormative activiteNormative = SolonEpgServiceLocator.getActiviteNormativeService().findOrCreateActiviteNormativeByNor(
                        session, norLoiRatification);
                TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);

                etapeSolon = texteMaitre.getEtapeSolon();
                numero = texteMaitre.getNumero();
                dateCreation = texteMaitre.getDateCreation() != null ? texteMaitre.getDateCreation().getTime() : null;

                LoiDeRatification loiDeRatification = activiteNormative.getAdapter(LoiDeRatification.class);
                numeroNor = loiDeRatification.getNumeroNor();
                titreOfficiel = texteMaitre.getTitreOfficiel()  ;
                dateSaisineCE = loiDeRatification.getDateSaisineCE() != null ? loiDeRatification.getDateSaisineCE().getTime() : null;
                datePublication = loiDeRatification.getDatePublication() != null ? loiDeRatification.getDatePublication().getTime() : null;
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
            return DateUtil.copyDate(this.dateCreation);
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
            return DateUtil.copyDate(dateSaisineCE);
        }

        public void setDateSaisineCE(Date dateSaisineCE) {
            this.dateSaisineCE = DateUtil.copyDate(dateSaisineCE);
        }

        public Date getDatePublication() {
            return DateUtil.copyDate(datePublication);
        }

        public void setDatePublication(Date datePublication) {
            this.datePublication = DateUtil.copyDate(datePublication);
        }
    }

    public TexteMaitreTraiteDTO(ActiviteNormative activiteNormative, CoreSession session) throws ClientException {
        if (activiteNormative == null) {
            return;
        }

        setId(activiteNormative.getDocument().getId());

        Traite traite = activiteNormative.getAdapter(Traite.class);
                
        this.categorie = traite.getCategorie();
        this.organisation = traite.getOrganisation();
        this.thematique = traite.getThematique();
        this.titreActe = traite.getTitreActe();
        this.dateSignature = traite.getDateSignature() != null ? traite.getDateSignature().getTime() : null;
        this.dateEntreeEnVigueur = traite.getDateEntreeEnVigueur() != null ? traite.getDateEntreeEnVigueur().getTime() : null;
        this.autorisationRatification = traite.hasAutorisationRatification();
        this.publication = traite.isPublication();
        this.observation = traite.getObservation();

        this.ministerePilote = traite.getMinisterePilote();

        this.degrePriorite = traite.getDegrePriorite();
        this.datePJL = traite.getDatePJL() != null ? traite.getDatePJL().getTime() : null;
        this.etudeImpact = traite.hasEtudeImpact();
        this.dispoEtudeImpact = traite.isDispoEtudeImpact();
        
        
        
        // decret
        decretTraite = new DecretTraite(traite.getNorDecret(), session);

        // ratification
        ratificationTraite = new RatificationTraite(traite.getNorLoiRatification(), session);
        
        
        FeuilleRouteService feuilleRouteService  = SolonEpgServiceLocator.getFeuilleRouteService() ;
        
        Dossier dossier = null ;
        String numeroNor  = this.autorisationRatification ?ratificationTraite.numeroNor :   decretTraite.numeroNor ;
        dossier = numeroNor != null ?  SolonEpgServiceLocator.getNORService().findDossierFromNOR(session, numeroNor) : null;

        if(dossier != null){
            DocumentModel  etapeDoc  = feuilleRouteService.getEtapeInitialisationDossier(session, dossier.getDocument());
            if(etapeDoc != null){                
                SolonEpgRouteStep epgRouteStep  = etapeDoc.getAdapter(SolonEpgRouteStep.class) ;
                Calendar dateFin= epgRouteStep.getDateFinEtape() ;
                this.dateDepotRatification =    dateFin != null ? dateFin.getTime() : null ;                
            }
        }

    }

    /**
     * refresh du DTO (decret et loi de ratification lié via le nor)
     * 
     * @param session
     * @throws ClientException
     */
    public void refresh(CoreSession session) throws ClientException {
        if (StringUtils.isNotBlank(decretTraite.numeroNor)) {
            Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(session, decretTraite.numeroNor);
            if (dossier != null) {
                RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
                
                decretTraite.numeroNor = dossier.getNumeroNor();
                decretTraite.etapeSolon = setEtapeSolon(dossier, session);
                decretTraite.numero = retourDila.getNumeroTexteParutionJorf();
                decretTraite.datePublication = retourDila.getDateParutionJorf() != null ? retourDila.getDateParutionJorf().getTime() : null;
                decretTraite.titreOfficiel = retourDila.getTitreOfficiel() ;
            }
        }

        if (StringUtils.isNotBlank(ratificationTraite.numeroNor)) {
            Dossier dossier = SolonEpgServiceLocator.getNORService().findDossierFromNOR(session, ratificationTraite.numeroNor);
            if (dossier != null) {

              
               RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
              
                ratificationTraite.etapeSolon = setEtapeSolon(dossier, session);
                ratificationTraite.dateCreation = dossier.getDateCreation() != null ? dossier.getDateCreation().getTime() : null;
                ratificationTraite.numeroNor = dossier.getNumeroNor();
                ratificationTraite.titreOfficiel = retourDila.getTitreOfficiel()  ;
                
                ratificationTraite.numero = retourDila.getNumeroTexteParutionJorf();
                ratificationTraite.datePublication = retourDila.getDateParutionJorf() != null ? retourDila.getDateParutionJorf().getTime() : null;
                
                ConseilEtat conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
                
                ratificationTraite.dateSaisineCE = conseilEtat.getDateSaisineCE() != null ? conseilEtat.getDateSaisineCE().getTime() : null;
                
            }
        }

    }
    
    private String setEtapeSolon(Dossier dossier, CoreSession session) {
        
        try {
            List<String> listStepLabel = SolonEpgServiceLocator.getCorbeilleService().findCurrentStepsLabel(session, dossier.getDocument().getId());
            return StringUtil.join(listStepLabel, ", ", "");
        } catch (Exception e) {
            log.error("Erreur lors de la construction de l'etapeSolon pour le dossier " + dossier.getDocument().getId());
        }
        
        return null;
    }

    public DocumentModel remapField(TexteMaitre texteMaitre, CoreSession session) {

        Traite traite = texteMaitre.getAdapter(Traite.class);

        traite.setCategorie(categorie);
        traite.setOrganisation(organisation);
        traite.setThematique(thematique);
        traite.setTitreActe(titreActe);

        if (dateSignature != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateSignature);
            traite.setDateSignature(cal);
        } else {
            traite.setDateSignature(null);
        }

        if (dateEntreeEnVigueur != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateEntreeEnVigueur);
            traite.setDateEntreeEnVigueur(cal);
        } else {
            traite.setDateEntreeEnVigueur(null);
        }

        traite.setAutorisationRatification(autorisationRatification);
        traite.setPublication(publication);
        traite.setObservation(observation);

        traite.setMinisterePilote(ministerePilote);

        traite.setDegrePriorite(degrePriorite);

        if (datePJL != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(datePJL);
            traite.setDatePJL(cal);
        } else {
            traite.setDatePJL(null);
        }

        traite.setEtudeImpact(etudeImpact);
        traite.setDispoEtudeImpact(dispoEtudeImpact);

        if (dateDepotRatification != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateDepotRatification);
            traite.setDateDepotRatification(cal);
        } else {
            traite.setDateDepotRatification(null);
        }

        traite.setNorDecret(decretTraite.numeroNor);

        traite.setNorLoiRatification(ratificationTraite.numeroNor);

        return traite.getDocument();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getThematique() {
        return thematique;
    }

    public void setThematique(String thematique) {
        this.thematique = thematique;
    }

    public String getTitreActe() {
        return titreActe;
    }

    public void setTitreActe(String titreActe) {
        this.titreActe = titreActe;
    }

    public Date getDateSignature() {
        return DateUtil.copyDate(dateSignature);
    }

    public void setDateSignature(Date dateSignature) {
        this.dateSignature = DateUtil.copyDate(dateSignature);
    }

    public Date getDateEntreeEnVigueur() {
        return DateUtil.copyDate(this.dateEntreeEnVigueur);
    }

    public void setDateEntreeEnVigueur(Date dateEntreeEnVigueur) {
        this.dateEntreeEnVigueur = DateUtil.copyDate(dateEntreeEnVigueur);
    }

    public Boolean getAutorisationRatification() {
        return autorisationRatification;
    }

    public void setAutorisationRatification(Boolean autorisationRatification) {
        this.autorisationRatification = autorisationRatification;
    }

    public Boolean getPublication() {
        return publication;
    }

    public void setPublication(Boolean publication) {
        this.publication = publication;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getMinisterePilote() {
        return ministerePilote;
    }

    public void setMinisterePilote(String ministerePilote) {
        this.ministerePilote = ministerePilote;
    }

    public String getDegrePriorite() {
        return degrePriorite;
    }

    public void setDegrePriorite(String degrePriorite) {
        this.degrePriorite = degrePriorite;
    }

    public Date getDatePJL() {
        return DateUtil.copyDate(datePJL);
    }

    public void setDatePJL(Date datePJL) {
        this.datePJL = DateUtil.copyDate(datePJL);
    }

    public Boolean getEtudeImpact() {
        return etudeImpact;
    }

    public void setEtudeImpact(Boolean etudeImpact) {
        this.etudeImpact = etudeImpact;
    }

    public Boolean getDispoEtudeImpact() {
        return dispoEtudeImpact;
    }

    public void setDispoEtudeImpact(Boolean dispoEtudeImpact) {
        this.dispoEtudeImpact = dispoEtudeImpact;
    }

    public Date getDateDepotRatification() {
        return dateDepotRatification;
    }

    public void setDateDepotRatification(Date dateDepotRatification) {
        this.dateDepotRatification = dateDepotRatification;
    }

    public RatificationTraite getRatificationTraite() {
        return ratificationTraite;
    }

    public DecretTraite getDecretTraite() {
        return decretTraite;
    }

    public DocumentModel remapDecret(CoreSession session) throws ClientException {
        if (StringUtils.isNotBlank(decretTraite.numeroNor)) {
            ActiviteNormative activiteNormative = SolonEpgServiceLocator.getActiviteNormativeService().findOrCreateActiviteNormativeByNor(session,
                    decretTraite.numeroNor);
            TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
            texteMaitre.setNumeroNor(decretTraite.numeroNor);
            texteMaitre.setEtapeSolon(decretTraite.etapeSolon);
            texteMaitre.setNumero(decretTraite.numero);
            
            if (decretTraite.datePublication != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(decretTraite.datePublication);
                texteMaitre.setDatePublication(cal);
            } else {
                texteMaitre.setDatePublication(null);
            }
            
            texteMaitre.setTitreOfficiel(decretTraite.getTitreOfficiel()) ;
            return texteMaitre.getDocument();
        }
        
        return null;
    }

    public DocumentModel remapRatification(CoreSession session) throws ClientException {
        if (StringUtils.isNotBlank(ratificationTraite.numeroNor)) {
            ActiviteNormative activiteNormative = SolonEpgServiceLocator.getActiviteNormativeService().findOrCreateActiviteNormativeByNor(session,
                    ratificationTraite.numeroNor);
            TexteMaitre texteMaitre = activiteNormative.getAdapter(TexteMaitre.class);
            LoiDeRatification loiDeRatification = activiteNormative.getAdapter(LoiDeRatification.class);

            loiDeRatification.setNumeroNor(ratificationTraite.numeroNor);
            texteMaitre.setEtapeSolon(ratificationTraite.etapeSolon);
            texteMaitre.setNumero(ratificationTraite.numero);

            if (ratificationTraite.dateCreation != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(ratificationTraite.dateCreation);
                texteMaitre.setDateCreation(cal);
            } else {
                texteMaitre.setDateCreation(null);
            }
            
            loiDeRatification.setTitreOfficiel(ratificationTraite.getTitreOfficiel()) ;
            if (ratificationTraite.dateCreation != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(ratificationTraite.dateCreation);
                loiDeRatification.setDateSaisineCE(cal);
            } else {
                loiDeRatification.setDateSaisineCE(null);
            }

            if (ratificationTraite.datePublication != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(ratificationTraite.datePublication);
                loiDeRatification.setDatePublication(cal);
            } else {
                loiDeRatification.setDatePublication(null);
            }

            return loiDeRatification.getDocument();

        }

        return null;
    }

}