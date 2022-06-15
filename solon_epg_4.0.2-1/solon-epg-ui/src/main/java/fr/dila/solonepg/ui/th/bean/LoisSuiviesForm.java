package fr.dila.solonepg.ui.th.bean;

import static fr.dila.solonepg.api.constant.TexteMaitreConstants.ACHEVEE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.APPLICATION_DIRECTE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.CATEGORIE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.CHAMP_LIBRE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.COMMISSION_ASS_NATIONALE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.COMMISSION_SENAT;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.DATE_ECHEANCE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.DATE_PROCHAIN_TAB_AFFICHAGE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.DATE_PROMULGATION;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.DATE_PUBLICATION;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.DATE_SIGNATURE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.DISPOSITION_HABILITATION;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.INTITULE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.LEGISLATURE_PUBLICATION;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.MINISTERE_PILOTE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.MOT_CLE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.NATURE_TEXTE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.NUMERO;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.NUMERO_INTERNE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.NUMERO_NOR;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.ORGANISATION;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.PROCEDURE_VOTE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.TEXTE_MAITRE_PREFIX;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.THEMATIQUE;
import static fr.dila.solonepg.api.constant.TexteMaitreConstants.TITRE_OFFICIEL;
import static fr.dila.st.api.constant.STConstant.SORT_ORDER_SUFFIX;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.FormSort;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang3.StringUtils;

@SwBean(keepdefaultValue = true)
public class LoisSuiviesForm extends AbstractSortablePaginationForm {
    private static final String MINISTERE_PARAM = "ministerePilote";
    private static final String NOR_PARAM = "nor";
    private static final String NUMERO_PARAM = "numero";
    private static final String TITRE_PARAM = "titre";
    private static final String KEYWORD_PARAM = "motCle";
    private static final String PROMULGATION_PARAM = "promulgation";
    private static final String APPLICATION_PARAM = "application";
    private static final String NATURE_TEXTE_PARAM = "natureTexte";
    private static final String ACHEVEE_PARAM = "achevee";
    private static final String CHAMP_LIBRE_PARAM = "champLibre";
    private static final String COMMISSION_ASS_NATIONALE_PARAM = "commissionAssNationale";
    private static final String COMMISSION_SENAT_PARAM = "commissionSenat";
    private static final String DATE_ADOPTION_PARAM = "dateAdoption";
    private static final String DATE_ECHEANCE_PARAM = "dateEcheance";
    private static final String DATE_PROCHAIN_TAB_AFFICHAGE_PARAM = "dateProchainTabAffichage";
    private static final String DATE_PUBLICATION_PARAM = "datePublication";
    private static final String DROIT_CONFORME_PARAM = "droitConforme";
    private static final String FONDEMENT_CONSTITUTIONNEL_PARAM = "fondementConstitutionnel";
    private static final String INTITULE_PARAM = "intitule";
    private static final String LEGISLATURE_PUBLICATION_PARAM = "legislaturePublication";
    private static final String NUMERO_INTERNE_PARAM = "numeroInterne";
    private static final String PROCEDURE_VOTE_PARAM = "procedureVote";
    public static final String CATEGORIE_PARAM = "categorie";
    public static final String PAYS_PARAM = "pays";
    public static final String THEMATIQUE_PARAM = "thematique";
    public static final String TITRE_ACTE_PARAM = "titreActe";
    public static final String DATE_SIGNATURE_PARAM = "dateSignature";
    public static final String DATE_ENTREE_EN_VIGUEUR_PARAM = "dateEntreeEnVigueur";
    public static final String OBSERVATION_PARAM = "observation";

    @SuppressWarnings("unchecked")
    public LoisSuiviesForm(Map<String, Serializable> mapSearch) {
        super();
        if (mapSearch != null) {
            setMinistere(SortOrder.fromValue((String) mapSearch.get(MINISTERE_PARAM)));
            setNor(SortOrder.fromValue((String) mapSearch.get(NOR_PARAM)));
            setNumero(SortOrder.fromValue((String) mapSearch.get(NUMERO_PARAM)));
            setTitre(SortOrder.fromValue((String) mapSearch.get(TITRE_PARAM)));
            setPromulgation(SortOrder.fromValue((String) mapSearch.get(PROMULGATION_PARAM)));
            setApplication(SortOrder.fromValue((String) mapSearch.get(APPLICATION_PARAM)));
            setDatePublication(SortOrder.fromValue((String) mapSearch.get(DATE_PUBLICATION_PARAM)));
            setIntitule(SortOrder.fromValue((String) mapSearch.get(INTITULE_PARAM)));
            setMotCle(SortOrder.fromValue((String) mapSearch.get(KEYWORD_PARAM)));
            setNatureTexte(SortOrder.fromValue((String) mapSearch.get(NATURE_TEXTE_PARAM)));
            if (StringUtils.isNotEmpty((String) mapSearch.get(PAGE_PARAM_NAME))) {
                setPage((String) mapSearch.get(PAGE_PARAM_NAME));
            }
            if (StringUtils.isNotEmpty((String) mapSearch.get(SIZE_PARAM_NAME))) {
                setSize((String) mapSearch.get(SIZE_PARAM_NAME));
            }
        }
    }

    @QueryParam(MINISTERE_PARAM)
    @FormParam(MINISTERE_PARAM)
    private SortOrder ministere;

    @QueryParam(NOR_PARAM)
    @FormParam(NOR_PARAM)
    private SortOrder nor;

    @QueryParam(NUMERO_PARAM)
    @FormParam(NUMERO_PARAM)
    private SortOrder numero;

    @QueryParam(TITRE_PARAM)
    @FormParam(TITRE_PARAM)
    private SortOrder titre;

    @QueryParam(KEYWORD_PARAM)
    @FormParam(KEYWORD_PARAM)
    private SortOrder motCle;

    @QueryParam(PROMULGATION_PARAM)
    @FormParam(PROMULGATION_PARAM)
    private SortOrder promulgation;

    @QueryParam(APPLICATION_PARAM)
    @FormParam(APPLICATION_PARAM)
    private SortOrder application;

    @QueryParam(NATURE_TEXTE_PARAM)
    @FormParam(NATURE_TEXTE_PARAM)
    private SortOrder natureTexte;

    @QueryParam(DATE_PUBLICATION_PARAM)
    @FormParam(DATE_PUBLICATION_PARAM)
    private SortOrder datePublication;

    @QueryParam(INTITULE_PARAM)
    @FormParam(INTITULE_PARAM)
    private SortOrder intitule;

    @QueryParam(FONDEMENT_CONSTITUTIONNEL_PARAM)
    @FormParam(FONDEMENT_CONSTITUTIONNEL_PARAM)
    private SortOrder fondementConstitutionnel;

    @QueryParam(DATE_ADOPTION_PARAM)
    @FormParam(DATE_ADOPTION_PARAM)
    private SortOrder dateAdoption;

    @QueryParam(DATE_ECHEANCE_PARAM)
    @FormParam(DATE_ECHEANCE_PARAM)
    private SortOrder dateEcheance;

    @QueryParam(DROIT_CONFORME_PARAM)
    @FormParam(DROIT_CONFORME_PARAM)
    private SortOrder droitConforme;

    @QueryParam(DATE_PROCHAIN_TAB_AFFICHAGE_PARAM)
    @FormParam(DATE_PROCHAIN_TAB_AFFICHAGE_PARAM)
    private SortOrder dateProchainTabAffichage;

    @QueryParam(ACHEVEE_PARAM)
    @FormParam(ACHEVEE_PARAM)
    private SortOrder achevee;

    @QueryParam(OBSERVATION_PARAM)
    @FormParam(OBSERVATION_PARAM)
    private SortOrder observation;

    @QueryParam(PROCEDURE_VOTE_PARAM)
    @FormParam(PROCEDURE_VOTE_PARAM)
    private SortOrder procedureVote;

    @QueryParam(LEGISLATURE_PUBLICATION_PARAM)
    @FormParam(LEGISLATURE_PUBLICATION_PARAM)
    private SortOrder legislaturePublication;

    @QueryParam(COMMISSION_ASS_NATIONALE_PARAM)
    @FormParam(COMMISSION_ASS_NATIONALE_PARAM)
    private SortOrder commissionAN;

    @QueryParam(COMMISSION_SENAT_PARAM)
    @FormParam(COMMISSION_SENAT_PARAM)
    private SortOrder commissionSenat;

    @QueryParam(NUMERO_INTERNE_PARAM)
    @FormParam(NUMERO_INTERNE_PARAM)
    private SortOrder numeroInterne;

    @QueryParam(CHAMP_LIBRE_PARAM)
    @FormParam(CHAMP_LIBRE_PARAM)
    private SortOrder champLibre;

    @QueryParam(MINISTERE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(MINISTERE_PARAM + SORT_ORDER_SUFFIX)
    private Integer ministereOrdre;

    @QueryParam(NOR_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(NOR_PARAM + SORT_ORDER_SUFFIX)
    private Integer norOrdre;

    @QueryParam(NUMERO_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(NUMERO_PARAM + SORT_ORDER_SUFFIX)
    private Integer numeroOrdre;

    @QueryParam(TITRE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(TITRE_PARAM + SORT_ORDER_SUFFIX)
    private Integer titreOrdre;

    @QueryParam(KEYWORD_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(KEYWORD_PARAM + SORT_ORDER_SUFFIX)
    private Integer motCleOrdre;

    @QueryParam(PROMULGATION_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(PROMULGATION_PARAM + SORT_ORDER_SUFFIX)
    private Integer promulgationOrdre;

    @QueryParam(APPLICATION_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(APPLICATION_PARAM + SORT_ORDER_SUFFIX)
    private Integer applicationOrdre;

    @QueryParam(NATURE_TEXTE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(NATURE_TEXTE_PARAM + SORT_ORDER_SUFFIX)
    private Integer natureTexteOrdre;

    @QueryParam(DATE_PUBLICATION_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(DATE_PUBLICATION_PARAM + SORT_ORDER_SUFFIX)
    private Integer datePublicationOrdre;

    @QueryParam(INTITULE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(INTITULE_PARAM + SORT_ORDER_SUFFIX)
    private Integer intituleOrdre;

    @QueryParam(FONDEMENT_CONSTITUTIONNEL_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(FONDEMENT_CONSTITUTIONNEL_PARAM + SORT_ORDER_SUFFIX)
    private Integer fondementConstitutionnelOrdre;

    @QueryParam(DATE_ADOPTION_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(DATE_ADOPTION_PARAM + SORT_ORDER_SUFFIX)
    private Integer dateAdoptionOrdre;

    @QueryParam(DATE_ECHEANCE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(DATE_ECHEANCE_PARAM + SORT_ORDER_SUFFIX)
    private Integer dateEcheanceOrdre;

    @QueryParam(DROIT_CONFORME_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(DROIT_CONFORME_PARAM + SORT_ORDER_SUFFIX)
    private Integer droitConformeOrdre;

    @QueryParam(DATE_PROCHAIN_TAB_AFFICHAGE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(DATE_PROCHAIN_TAB_AFFICHAGE_PARAM + SORT_ORDER_SUFFIX)
    private Integer dateProchainTabAffichageOrdre;

    @QueryParam(ACHEVEE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(ACHEVEE_PARAM + SORT_ORDER_SUFFIX)
    private Integer acheveeOrdre;

    @QueryParam(OBSERVATION_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(OBSERVATION_PARAM + SORT_ORDER_SUFFIX)
    private Integer observationOrdre;

    @QueryParam(PROCEDURE_VOTE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(PROCEDURE_VOTE_PARAM + SORT_ORDER_SUFFIX)
    private Integer procedureVoteOrdre;

    @QueryParam(LEGISLATURE_PUBLICATION_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(LEGISLATURE_PUBLICATION_PARAM + SORT_ORDER_SUFFIX)
    private Integer legislaturePublicationOrdre;

    @QueryParam(COMMISSION_ASS_NATIONALE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(COMMISSION_ASS_NATIONALE_PARAM + SORT_ORDER_SUFFIX)
    private Integer commissionANOrdre;

    @QueryParam(COMMISSION_SENAT_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(COMMISSION_SENAT_PARAM + SORT_ORDER_SUFFIX)
    private Integer commissionSenatOrdre;

    @QueryParam(NUMERO_INTERNE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(NUMERO_INTERNE_PARAM + SORT_ORDER_SUFFIX)
    private Integer numeroInterneOrdre;

    @QueryParam(CHAMP_LIBRE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(CHAMP_LIBRE_PARAM + SORT_ORDER_SUFFIX)
    private Integer champLibreOrdre;

    @QueryParam(CATEGORIE_PARAM)
    @FormParam(CATEGORIE_PARAM)
    private SortOrder categorie;

    @QueryParam(CATEGORIE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(CATEGORIE_PARAM + SORT_ORDER_SUFFIX)
    private Integer categorieOrdre;

    @QueryParam(PAYS_PARAM)
    @FormParam(PAYS_PARAM)
    private SortOrder pays;

    @QueryParam(PAYS_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(PAYS_PARAM + SORT_ORDER_SUFFIX)
    private Integer paysOrdre;

    @QueryParam(TITRE_ACTE_PARAM)
    @FormParam(TITRE_ACTE_PARAM)
    private SortOrder titreActe;

    @QueryParam(TITRE_ACTE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(TITRE_ACTE_PARAM + SORT_ORDER_SUFFIX)
    private Integer titreActeOrdre;

    @QueryParam(DATE_SIGNATURE_PARAM)
    @FormParam(DATE_SIGNATURE_PARAM)
    private SortOrder dateSignature;

    @QueryParam(DATE_SIGNATURE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(DATE_SIGNATURE_PARAM + SORT_ORDER_SUFFIX)
    private Integer dateSignatureOrdre;

    @QueryParam(DATE_ENTREE_EN_VIGUEUR_PARAM)
    @FormParam(DATE_ENTREE_EN_VIGUEUR_PARAM)
    private SortOrder dateEntreeEnVigueur;

    @QueryParam(DATE_ENTREE_EN_VIGUEUR_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(DATE_ENTREE_EN_VIGUEUR_PARAM + SORT_ORDER_SUFFIX)
    private Integer dateEntreeEnVigueurOrdre;

    @QueryParam(THEMATIQUE_PARAM)
    @FormParam(THEMATIQUE_PARAM)
    private SortOrder thematique;

    @QueryParam(THEMATIQUE_PARAM + SORT_ORDER_SUFFIX)
    @FormParam(THEMATIQUE_PARAM + SORT_ORDER_SUFFIX)
    private Integer thematiqueOrdre;

    public LoisSuiviesForm() {
        super();
    }

    public SortOrder getMinistere() {
        return ministere;
    }

    public void setMinistere(SortOrder ministere) {
        checkChangeEvent(ministere);
        this.ministere = ministere;
    }

    public SortOrder getNor() {
        return nor;
    }

    public void setNor(SortOrder nor) {
        checkChangeEvent(nor);
        this.nor = nor;
    }

    public SortOrder getNumero() {
        return numero;
    }

    public void setNumero(SortOrder numero) {
        checkChangeEvent(numero);
        this.numero = numero;
    }

    public SortOrder getTitre() {
        return titre;
    }

    public void setTitre(SortOrder titre) {
        checkChangeEvent(titre);
        this.titre = titre;
    }

    public SortOrder getMotCle() {
        return motCle;
    }

    public void setMotCle(SortOrder motCle) {
        checkChangeEvent(motCle);
        this.motCle = motCle;
    }

    public SortOrder getPromulgation() {
        return promulgation;
    }

    public void setPromulgation(SortOrder promulgation) {
        checkChangeEvent(promulgation);
        this.promulgation = promulgation;
    }

    public SortOrder getApplication() {
        return application;
    }

    public void setApplication(SortOrder application) {
        checkChangeEvent(application);
        this.application = application;
    }

    public SortOrder getNatureTexte() {
        return natureTexte;
    }

    public void setNatureTexte(SortOrder natureTexte) {
        checkChangeEvent(natureTexte);
        this.natureTexte = natureTexte;
    }

    public SortOrder getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(SortOrder datePublication) {
        checkChangeEvent(datePublication);
        this.datePublication = datePublication;
    }

    public SortOrder getIntitule() {
        return intitule;
    }

    public void setIntitule(SortOrder intitule) {
        checkChangeEvent(intitule);
        this.intitule = intitule;
    }

    public SortOrder getFondementConstitutionnel() {
        return fondementConstitutionnel;
    }

    public void setFondementConstitutionnel(SortOrder fondementConstitutionnel) {
        checkChangeEvent(fondementConstitutionnel);
        this.fondementConstitutionnel = fondementConstitutionnel;
    }

    public SortOrder getDateAdoption() {
        return dateAdoption;
    }

    public void setDateAdoption(SortOrder dateAdoption) {
        checkChangeEvent(dateAdoption);
        this.dateAdoption = dateAdoption;
    }

    public SortOrder getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(SortOrder dateEcheance) {
        checkChangeEvent(dateEcheance);
        this.dateEcheance = dateEcheance;
    }

    public SortOrder getDroitConforme() {
        return droitConforme;
    }

    public void setDroitConforme(SortOrder droitConforme) {
        checkChangeEvent(droitConforme);
        this.droitConforme = droitConforme;
    }

    public SortOrder getDateProchainTabAffichage() {
        return dateProchainTabAffichage;
    }

    public void setDateProchainTabAffichage(SortOrder dateProchainTabAffichage) {
        checkChangeEvent(dateProchainTabAffichage);
        this.dateProchainTabAffichage = dateProchainTabAffichage;
    }

    public SortOrder getAchevee() {
        return achevee;
    }

    public void setAchevee(SortOrder achevee) {
        checkChangeEvent(achevee);
        this.achevee = achevee;
    }

    public SortOrder getObservation() {
        return observation;
    }

    public void setObservation(SortOrder observation) {
        checkChangeEvent(observation);
        this.observation = observation;
    }

    public SortOrder getProcedureVote() {
        return procedureVote;
    }

    public void setProcedureVote(SortOrder procedureVote) {
        checkChangeEvent(procedureVote);
        this.procedureVote = procedureVote;
    }

    public SortOrder getLegislaturePublication() {
        return legislaturePublication;
    }

    public void setLegislaturePublication(SortOrder legislaturePublication) {
        checkChangeEvent(legislaturePublication);
        this.legislaturePublication = legislaturePublication;
    }

    public SortOrder getCommissionAN() {
        return commissionAN;
    }

    public void setCommissionAN(SortOrder commissionAN) {
        checkChangeEvent(commissionAN);
        this.commissionAN = commissionAN;
    }

    public SortOrder getCommissionSenat() {
        return commissionSenat;
    }

    public void setCommissionSenat(SortOrder commissionSenat) {
        checkChangeEvent(commissionSenat);
        this.commissionSenat = commissionSenat;
    }

    public SortOrder getNumeroInterne() {
        return numeroInterne;
    }

    public void setNumeroInterne(SortOrder numeroInterne) {
        checkChangeEvent(numeroInterne);
        this.numeroInterne = numeroInterne;
    }

    public SortOrder getChampLibre() {
        return champLibre;
    }

    public void setChampLibre(SortOrder champLibre) {
        checkChangeEvent(champLibre);
        this.champLibre = champLibre;
    }

    public Integer getMinistereOrdre() {
        return ministereOrdre;
    }

    public void setMinistereOrdre(Integer ministereOrdre) {
        this.ministereOrdre = ministereOrdre;
    }

    public Integer getNorOrdre() {
        return norOrdre;
    }

    public void setNorOrdre(Integer norOrdre) {
        this.norOrdre = norOrdre;
    }

    public Integer getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(Integer numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public Integer getTitreOrdre() {
        return titreOrdre;
    }

    public void setTitreOrdre(Integer titreOrdre) {
        this.titreOrdre = titreOrdre;
    }

    public Integer getMotCleOrdre() {
        return motCleOrdre;
    }

    public void setMotCleOrdre(Integer motCleOrdre) {
        this.motCleOrdre = motCleOrdre;
    }

    public Integer getPromulgationOrdre() {
        return promulgationOrdre;
    }

    public void setPromulgationOrdre(Integer promulgationOrdre) {
        this.promulgationOrdre = promulgationOrdre;
    }

    public Integer getApplicationOrdre() {
        return applicationOrdre;
    }

    public void setApplicationOrdre(Integer applicationOrdre) {
        this.applicationOrdre = applicationOrdre;
    }

    public Integer getNatureTexteOrdre() {
        return natureTexteOrdre;
    }

    public void setNatureTexteOrdre(Integer natureTexteOrdre) {
        this.natureTexteOrdre = natureTexteOrdre;
    }

    public Integer getDatePublicationOrdre() {
        return datePublicationOrdre;
    }

    public void setDatePublicationOrdre(Integer datePublicationOrdre) {
        this.datePublicationOrdre = datePublicationOrdre;
    }

    public Integer getIntituleOrdre() {
        return intituleOrdre;
    }

    public void setIntituleOrdre(Integer intituleOrdre) {
        this.intituleOrdre = intituleOrdre;
    }

    public Integer getFondementConstitutionnelOrdre() {
        return fondementConstitutionnelOrdre;
    }

    public void setFondementConstitutionnelOrdre(Integer fondementConstitutionnelOrdre) {
        this.fondementConstitutionnelOrdre = fondementConstitutionnelOrdre;
    }

    public Integer getDateAdoptionOrdre() {
        return dateAdoptionOrdre;
    }

    public void setDateAdoptionOrdre(Integer dateAdoptionOrdre) {
        this.dateAdoptionOrdre = dateAdoptionOrdre;
    }

    public Integer getDateEcheanceOrdre() {
        return dateEcheanceOrdre;
    }

    public void setDateEcheanceOrdre(Integer dateEcheanceOrdre) {
        this.dateEcheanceOrdre = dateEcheanceOrdre;
    }

    public Integer getDroitConformeOrdre() {
        return droitConformeOrdre;
    }

    public void setDroitConformeOrdre(Integer droitConformeOrdre) {
        this.droitConformeOrdre = droitConformeOrdre;
    }

    public Integer getDateProchainTabAffichageOrdre() {
        return dateProchainTabAffichageOrdre;
    }

    public void setDateProchainTabAffichageOrdre(Integer dateProchainTabAffichageOrdre) {
        this.dateProchainTabAffichageOrdre = dateProchainTabAffichageOrdre;
    }

    public Integer getAcheveeOrdre() {
        return acheveeOrdre;
    }

    public void setAcheveeOrdre(Integer acheveeOrdre) {
        this.acheveeOrdre = acheveeOrdre;
    }

    public Integer getObservationOrdre() {
        return observationOrdre;
    }

    public void setObservationOrdre(Integer observationOrdre) {
        this.observationOrdre = observationOrdre;
    }

    public Integer getProcedureVoteOrdre() {
        return procedureVoteOrdre;
    }

    public void setProcedureVoteOrdre(Integer procedureVoteOrdre) {
        this.procedureVoteOrdre = procedureVoteOrdre;
    }

    public Integer getLegislaturePublicationOrdre() {
        return legislaturePublicationOrdre;
    }

    public void setLegislaturePublicationOrdre(Integer legislaturePublicationOrdre) {
        this.legislaturePublicationOrdre = legislaturePublicationOrdre;
    }

    public Integer getCommissionANOrdre() {
        return commissionANOrdre;
    }

    public void setCommissionANOrdre(Integer commissionANOrdre) {
        this.commissionANOrdre = commissionANOrdre;
    }

    public Integer getCommissionSenatOrdre() {
        return commissionSenatOrdre;
    }

    public void setCommissionSenatOrdre(Integer commissionSenatOrdre) {
        this.commissionSenatOrdre = commissionSenatOrdre;
    }

    public Integer getNumeroInterneOrdre() {
        return numeroInterneOrdre;
    }

    public void setNumeroInterneOrdre(Integer numeroInterneOrdre) {
        this.numeroInterneOrdre = numeroInterneOrdre;
    }

    public Integer getChampLibreOrdre() {
        return champLibreOrdre;
    }

    public void setChampLibreOrdre(Integer champLibreOrdre) {
        this.champLibreOrdre = champLibreOrdre;
    }

    public SortOrder getCategorie() {
        return categorie;
    }

    public void setCategorie(SortOrder categorie) {
        checkChangeEvent(categorie);
        this.categorie = categorie;
    }

    public Integer getCategorieOrdre() {
        return categorieOrdre;
    }

    public void setCategorieOrdre(Integer categorieOrdre) {
        this.categorieOrdre = categorieOrdre;
    }

    public SortOrder getPays() {
        return pays;
    }

    public void setPays(SortOrder pays) {
        checkChangeEvent(pays);
        this.pays = pays;
    }

    public Integer getPaysOrdre() {
        return paysOrdre;
    }

    public void setPaysOrdre(Integer paysOrdre) {
        this.paysOrdre = paysOrdre;
    }

    public SortOrder getThematique() {
        return thematique;
    }

    public void setThematique(SortOrder thematique) {
        checkChangeEvent(thematique);
        this.thematique = thematique;
    }

    public Integer getThematiqueOrdre() {
        return thematiqueOrdre;
    }

    public void setThematiqueOrdre(Integer thematiqueOrdre) {
        this.thematiqueOrdre = thematiqueOrdre;
    }

    public SortOrder getTitreActe() {
        return titreActe;
    }

    public void setTitreActe(SortOrder titreActe) {
        checkChangeEvent(titreActe);
        this.titreActe = titreActe;
    }

    public Integer getTitreActeOrdre() {
        return titreActeOrdre;
    }

    public void setTitreActeOrdre(Integer titreActeOrdre) {
        this.titreActeOrdre = titreActeOrdre;
    }

    public SortOrder getDateSignature() {
        return dateSignature;
    }

    public void setDateSignature(SortOrder dateSignature) {
        checkChangeEvent(dateSignature);
        this.dateSignature = dateSignature;
    }

    public Integer getDateSignatureOrdre() {
        return dateSignatureOrdre;
    }

    public void setDateSignatureOrdre(Integer dateSignatureOrdre) {
        this.dateSignatureOrdre = dateSignatureOrdre;
    }

    public SortOrder getDateEntreeEnVigueur() {
        return dateEntreeEnVigueur;
    }

    public void setDateEntreeEnVigueur(SortOrder dateEntreeEnVigueur) {
        checkChangeEvent(dateEntreeEnVigueur);
        this.dateEntreeEnVigueur = dateEntreeEnVigueur;
    }

    public Integer getDateEntreeEnVigueurOrdre() {
        return dateEntreeEnVigueurOrdre;
    }

    public void setDateEntreeEnVigueurOrdre(Integer dateEntreeEnVigueurOrdre) {
        this.dateEntreeEnVigueurOrdre = dateEntreeEnVigueurOrdre;
    }

    @Override
    protected void setDefaultSort() {
        setPromulgation(SortOrder.DESC);
    }

    private void checkChangeEvent(SortOrder value) {
        if (value != null) {
            isTableChangeEvent = true;
        }
    }

    @Override
    public Map<String, FormSort> getSortForm() {
        Map<String, FormSort> map = new HashMap<>();
        map.put(TEXTE_MAITRE_PREFIX + ":" + MINISTERE_PILOTE, new FormSort(ministere, ministereOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + NUMERO_NOR, new FormSort(nor, norOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + NUMERO, new FormSort(numero, numeroOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + TITRE_OFFICIEL, new FormSort(titre, titreOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + MOT_CLE, new FormSort(motCle, motCleOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + DATE_PROMULGATION, new FormSort(promulgation, promulgationOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + APPLICATION_DIRECTE, new FormSort(application, applicationOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + NATURE_TEXTE, new FormSort(natureTexte, natureTexteOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + DATE_PUBLICATION, new FormSort(datePublication, datePublicationOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + INTITULE, new FormSort(intitule, intituleOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + DATE_ECHEANCE, new FormSort(dateEcheance, dateEcheanceOrdre));
        map.put(
            TEXTE_MAITRE_PREFIX + ":" + DATE_PROCHAIN_TAB_AFFICHAGE,
            new FormSort(dateProchainTabAffichage, dateProchainTabAffichageOrdre)
        );
        map.put(TEXTE_MAITRE_PREFIX + ":" + ACHEVEE, new FormSort(achevee, acheveeOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + PROCEDURE_VOTE, new FormSort(procedureVote, procedureVoteOrdre));
        map.put(
            TEXTE_MAITRE_PREFIX + ":" + LEGISLATURE_PUBLICATION,
            new FormSort(legislaturePublication, legislaturePublicationOrdre)
        );
        map.put(TEXTE_MAITRE_PREFIX + ":" + COMMISSION_ASS_NATIONALE, new FormSort(commissionAN, commissionANOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + COMMISSION_SENAT, new FormSort(commissionSenat, commissionSenatOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + NUMERO_INTERNE, new FormSort(numeroInterne, numeroInterneOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + CHAMP_LIBRE, new FormSort(champLibre, champLibreOrdre));
        map.put(
            TEXTE_MAITRE_PREFIX + ":" + DISPOSITION_HABILITATION,
            new FormSort(fondementConstitutionnel, fondementConstitutionnelOrdre)
        );
        map.put(TEXTE_MAITRE_PREFIX + ":" + CATEGORIE, new FormSort(categorie, categorieOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + ORGANISATION, new FormSort(pays, paysOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + THEMATIQUE, new FormSort(thematique, thematiqueOrdre));
        map.put(TEXTE_MAITRE_PREFIX + ":" + DATE_SIGNATURE, new FormSort(dateSignature, dateSignatureOrdre));
        map.put(
            TEXTE_MAITRE_PREFIX + ":" + DATE_ENTREE_EN_VIGUEUR,
            new FormSort(dateEntreeEnVigueur, dateEntreeEnVigueurOrdre)
        );
        map.put(TEXTE_MAITRE_PREFIX + ":" + INTITULE, new FormSort(intitule, intituleOrdre));
        return map;
    }
}
