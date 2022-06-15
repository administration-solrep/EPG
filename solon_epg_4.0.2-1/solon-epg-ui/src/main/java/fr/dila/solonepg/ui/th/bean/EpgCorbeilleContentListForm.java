package fr.dila.solonepg.ui.th.bean;

import com.google.gson.Gson;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.bean.PaginationForm;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

@SwBean
public class EpgCorbeilleContentListForm extends PaginationForm {
    public static final String NOR_TRI = "norTri";
    public static final String ID_TRI = "idTri";
    public static final String ID_DOSSIER_TRI = "idDossierTri";
    public static final String INTITULE_TRI = "intituleTri";
    public static final String EMETTEUR_TRI = "emetteurTri";
    public static final String OBJET_TRI = "objetTri";
    public static final String ORG_NOM_TRI = "nomOrgTri";
    public static final String OEP_NOM_TRI = "nomOepTri";
    public static final String DATE_CREATION_TRI = "dateCreationTri";
    public static final String TITRE_ACTE_TRI = "titreTri";
    public static final String DATE_ARRIVEE_TRI = "dateArriveeTri";
    public static final String DATE_DEPOT_TRI = "dateDepotTri";
    public static final String DATE_DEBUT_TRI = "dateDebutTri";
    public static final String DATE_FIN_TRI = "dateFinTri";
    public static final String DATE_PUB_TRI = "datePubTri";
    public static final String DATE_TRANS_TRI = "dateTransTri";
    public static final String STATUT_TRI = "statutTri";
    public static final String TYPE_ACTE_TRI = "typeActeTri";
    public static final String TYPE_RAPPORT_TRI = "typeRapportTri";
    public static final String BASE_LEGALE_TRI = "baseLegaleTri";
    public static final String MIN_RATTACH_TRI = "minRattachTri";
    public static final String TEXTE_REF_TRI = "texteRefTri";

    private Map<String, Object> formData;

    @SuppressWarnings("unchecked")
    public EpgCorbeilleContentListForm(String json) {
        Gson gson = new Gson();
        formData = gson.fromJson(json, Map.class);

        if (formData != null) {
            setPage((String) formData.get(PAGE_PARAM_NAME));
            setSize((String) formData.get(SIZE_PARAM_NAME));

            nor = SortOrder.fromValue((String) formData.get(NOR_TRI));
            idDossier = SortOrder.fromValue((String) formData.get(ID_DOSSIER_TRI));
            id = SortOrder.fromValue((String) formData.get(ID_TRI));

            baseLegale = SortOrder.fromValue((String) formData.get(BASE_LEGALE_TRI));
            emetteur = SortOrder.fromValue((String) formData.get(EMETTEUR_TRI));
            intitule = SortOrder.fromValue((String) formData.get(INTITULE_TRI));
            minRattach = SortOrder.fromValue((String) formData.get(MIN_RATTACH_TRI));
            objet = SortOrder.fromValue((String) formData.get(OBJET_TRI));
            nomOep = SortOrder.fromValue((String) formData.get(OEP_NOM_TRI));
            nomOrg = SortOrder.fromValue((String) formData.get(ORG_NOM_TRI));
            statut = SortOrder.fromValue((String) formData.get(STATUT_TRI));
            texteRef = SortOrder.fromValue((String) formData.get(TEXTE_REF_TRI));
            titreActe = SortOrder.fromValue((String) formData.get(TITRE_ACTE_TRI));
            typeActe = SortOrder.fromValue((String) formData.get(TYPE_ACTE_TRI));
            typeRapport = SortOrder.fromValue((String) formData.get(TYPE_RAPPORT_TRI));

            dateCreation = SortOrder.fromValue((String) formData.get(DATE_CREATION_TRI));
            dateArrivee = SortOrder.fromValue((String) formData.get(DATE_ARRIVEE_TRI));
            dateDebut = SortOrder.fromValue((String) formData.get(DATE_DEBUT_TRI));
            dateFin = SortOrder.fromValue((String) formData.get(DATE_FIN_TRI));
            dateDepot = SortOrder.fromValue((String) formData.get(DATE_DEPOT_TRI));
            datePub = SortOrder.fromValue((String) formData.get(DATE_PUB_TRI));
            dateTrans = SortOrder.fromValue((String) formData.get(DATE_TRANS_TRI));
        }
    }

    @QueryParam(ID_DOSSIER_TRI)
    @FormParam(ID_DOSSIER_TRI)
    private SortOrder idDossier;

    @QueryParam(NOR_TRI)
    @FormParam(NOR_TRI)
    private SortOrder nor;

    @QueryParam(ID_TRI)
    @FormParam(ID_TRI)
    private SortOrder id;

    @QueryParam(ORG_NOM_TRI)
    @FormParam(ORG_NOM_TRI)
    private SortOrder nomOrg;

    @QueryParam(OEP_NOM_TRI)
    @FormParam(OEP_NOM_TRI)
    private SortOrder nomOep;

    @QueryParam(MIN_RATTACH_TRI)
    @FormParam(MIN_RATTACH_TRI)
    private SortOrder minRattach;

    @QueryParam(BASE_LEGALE_TRI)
    @FormParam(BASE_LEGALE_TRI)
    private SortOrder baseLegale;

    @QueryParam(TYPE_ACTE_TRI)
    @FormParam(TYPE_ACTE_TRI)
    private SortOrder typeActe;

    @QueryParam(TYPE_RAPPORT_TRI)
    @FormParam(TYPE_RAPPORT_TRI)
    private SortOrder typeRapport;

    @QueryParam(STATUT_TRI)
    @FormParam(STATUT_TRI)
    private SortOrder statut;

    @QueryParam(INTITULE_TRI)
    @FormParam(INTITULE_TRI)
    private SortOrder intitule;

    @QueryParam(TITRE_ACTE_TRI)
    @FormParam(TITRE_ACTE_TRI)
    private SortOrder titreActe;

    @QueryParam(OBJET_TRI)
    @FormParam(OBJET_TRI)
    private SortOrder objet;

    @QueryParam(TEXTE_REF_TRI)
    @FormParam(TEXTE_REF_TRI)
    private SortOrder texteRef;

    @QueryParam(EMETTEUR_TRI)
    @FormParam(EMETTEUR_TRI)
    private SortOrder emetteur;

    @QueryParam(DATE_CREATION_TRI)
    @FormParam(DATE_CREATION_TRI)
    private SortOrder dateCreation;

    @QueryParam(DATE_FIN_TRI)
    @FormParam(DATE_FIN_TRI)
    private SortOrder dateFin;

    @QueryParam(DATE_DEBUT_TRI)
    @FormParam(DATE_DEBUT_TRI)
    private SortOrder dateDebut;

    @QueryParam(DATE_ARRIVEE_TRI)
    @FormParam(DATE_ARRIVEE_TRI)
    private SortOrder dateArrivee;

    @QueryParam(DATE_DEPOT_TRI)
    @FormParam(DATE_DEPOT_TRI)
    private SortOrder dateDepot;

    @QueryParam(DATE_PUB_TRI)
    @FormParam(DATE_PUB_TRI)
    private SortOrder datePub;

    @QueryParam(DATE_TRANS_TRI)
    @FormParam(DATE_TRANS_TRI)
    private SortOrder dateTrans;

    public EpgCorbeilleContentListForm() {
        formData = new HashMap<>();
    }

    public SortOrder getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(SortOrder idDossier) {
        this.idDossier = idDossier;
    }

    public SortOrder getNor() {
        return nor;
    }

    public void setNor(SortOrder nor) {
        this.nor = nor;
    }

    public SortOrder getIntitule() {
        return intitule;
    }

    public void setIntitule(SortOrder intitule) {
        this.intitule = intitule;
    }

    public SortOrder getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(SortOrder dateCreation) {
        this.dateCreation = dateCreation;
    }

    public SortOrder getId() {
        return id;
    }

    public void setId(SortOrder id) {
        this.id = id;
    }

    public SortOrder getNomOrg() {
        return nomOrg;
    }

    public void setNomOrg(SortOrder nomOrg) {
        this.nomOrg = nomOrg;
    }

    public SortOrder getNomOep() {
        return nomOep;
    }

    public void setNomOep(SortOrder nomOep) {
        this.nomOep = nomOep;
    }

    public SortOrder getMinRattach() {
        return minRattach;
    }

    public void setMinRattach(SortOrder minRattach) {
        this.minRattach = minRattach;
    }

    public SortOrder getBaseLegale() {
        return baseLegale;
    }

    public void setBaseLegale(SortOrder baseLegale) {
        this.baseLegale = baseLegale;
    }

    public SortOrder getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(SortOrder typeActe) {
        this.typeActe = typeActe;
    }

    public SortOrder getTypeRapport() {
        return typeRapport;
    }

    public void setTypeRapport(SortOrder typeRapport) {
        this.typeRapport = typeRapport;
    }

    public SortOrder getStatut() {
        return statut;
    }

    public void setStatut(SortOrder statut) {
        this.statut = statut;
    }

    public SortOrder getTitreActe() {
        return titreActe;
    }

    public void setTitreActe(SortOrder titreActe) {
        this.titreActe = titreActe;
    }

    public SortOrder getObjet() {
        return objet;
    }

    public void setObjet(SortOrder objet) {
        this.objet = objet;
    }

    public SortOrder getTexteRef() {
        return texteRef;
    }

    public void setTexteRef(SortOrder texteRef) {
        this.texteRef = texteRef;
    }

    public SortOrder getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(SortOrder emetteur) {
        this.emetteur = emetteur;
    }

    public SortOrder getDateFin() {
        return dateFin;
    }

    public void setDateFin(SortOrder dateFin) {
        this.dateFin = dateFin;
    }

    public SortOrder getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(SortOrder dateDebut) {
        this.dateDebut = dateDebut;
    }

    public SortOrder getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(SortOrder dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public SortOrder getDateDepot() {
        return dateDepot;
    }

    public void setDateDepot(SortOrder dateDepot) {
        this.dateDepot = dateDepot;
    }

    public SortOrder getDatePub() {
        return datePub;
    }

    public void setDatePub(SortOrder datePub) {
        this.datePub = datePub;
    }

    public SortOrder getDateTrans() {
        return dateTrans;
    }

    public void setDateTrans(SortOrder dateTrans) {
        this.dateTrans = dateTrans;
    }

    public String getValue(String name) {
        if (formData != null) {
            return (String) formData.get(name);
        }
        return name;
    }

    public Map<String, Object> getFormData() {
        return formData;
    }

    public void setFormData(Map<String, Object> formData) {
        this.formData = formData;
    }
}
