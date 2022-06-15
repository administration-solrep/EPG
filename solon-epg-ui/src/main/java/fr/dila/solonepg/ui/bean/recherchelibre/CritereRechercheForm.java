package fr.dila.solonepg.ui.bean.recherchelibre;

import static fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.EpgRechercheLibre.OPTIONS_TRI_PERTINENCE;

import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.bean.PaginationForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@SwBean(keepdefaultValue = true)
public class CritereRechercheForm extends PaginationForm {
    @FormParam("sort")
    private String sort = OPTIONS_TRI_PERTINENCE;

    @FormParam("vecteurPublicationIds[]")
    private ArrayList<SelectValueDTO> vecteurPublication = new ArrayList<>();

    @FormParam("typeActeIds[]")
    private ArrayList<SelectValueDTO> typeActe = new ArrayList<>();

    @FormParam("categorieActeIds[]")
    private ArrayList<SelectValueDTO> categorieActe = new ArrayList<>();

    @FormParam("statutIds[]")
    private ArrayList<SelectValueDTO> statut = new ArrayList<>();

    @FormParam("ministereAttacheIds[]")
    private ArrayList<SelectValueDTO> ministereAttache = new ArrayList<>();

    @FormParam("directionAttacheIds[]")
    private ArrayList<SelectValueDTO> directionAttache = new ArrayList<>();

    @FormParam("statutArchivageIds[]")
    private ArrayList<SelectValueDTO> statutArchivage = new ArrayList<>();

    @FormParam("posteId")
    private String posteId;

    @FormParam("posteEnCours")
    private Boolean posteEnCours;

    private Map<String, String> mapPoste = new HashMap<>();

    @FormParam("dateDebutCreation")
    private Calendar dateDebutCreation;

    @FormParam("dateFinCreation")
    private Calendar dateFinCreation;

    @FormParam("dateDebutPublication")
    private Calendar dateDebutPublication;

    @FormParam("dateFinPublication")
    private Calendar dateFinPublication;

    @FormParam("dateDebutSignature")
    private Calendar dateDebutSignature;

    @FormParam("dateFinSignature")
    private Calendar dateFinSignature;

    @FormParam("dateDebutPublicationJO")
    private Calendar dateDebutPublicationJO;

    @FormParam("dateFinPublicationJO")
    private Calendar dateFinPublicationJO;

    @FormParam("baseProduction")
    @QueryParam("baseProduction")
    private Boolean baseProduction;

    @FormParam("baseArchivage")
    @QueryParam("baseArchivage")
    private Boolean baseArchivage;

    @FormParam("expressionExacte")
    @QueryParam("expressionExacte")
    private Boolean expressionExacte;

    @FormParam("derniereVersion")
    @QueryParam("derniereVersion")
    private Boolean derniereVersion = true;

    @FormParam("recherche")
    @QueryParam("recherche")
    private String recherche;

    public CritereRechercheForm() {}

    public CritereRechercheForm(
        ArrayList<SelectValueDTO> vecteurPublication,
        ArrayList<SelectValueDTO> typeActe,
        ArrayList<SelectValueDTO> categorieActe,
        ArrayList<SelectValueDTO> statut,
        ArrayList<SelectValueDTO> ministereAttache,
        ArrayList<SelectValueDTO> directionAttache,
        ArrayList<SelectValueDTO> statutArchivage,
        String posteId,
        Boolean posteEnCours,
        Calendar dateDebutCreation,
        Calendar dateFinCreation,
        Calendar dateDebutPublication,
        Calendar dateFinPublication,
        Calendar dateDebutSignature,
        Calendar dateFinSignature,
        Calendar dateDebutPublicationJO,
        Calendar dateFinPublicationJO,
        Boolean baseProduction,
        Boolean baseArchivage,
        Boolean expressionExacte,
        Boolean derniereVersion,
        String recherche
    ) {
        this.vecteurPublication = vecteurPublication;
        this.typeActe = typeActe;
        this.categorieActe = categorieActe;
        this.statut = statut;
        this.ministereAttache = ministereAttache;
        this.directionAttache = directionAttache;
        this.statutArchivage = statutArchivage;
        this.posteId = posteId;
        this.posteEnCours = posteEnCours;
        this.dateDebutCreation = dateDebutCreation;
        this.dateFinCreation = dateFinCreation;
        this.dateDebutPublication = dateDebutPublication;
        this.dateFinPublication = dateFinPublication;
        this.dateDebutSignature = dateDebutSignature;
        this.dateFinSignature = dateFinSignature;
        this.dateDebutPublicationJO = dateDebutPublicationJO;
        this.dateFinPublicationJO = dateFinPublicationJO;
        this.baseProduction = baseProduction;
        this.baseArchivage = baseArchivage;
        this.expressionExacte = expressionExacte;
        this.derniereVersion = derniereVersion;
        this.recherche = recherche;
    }

    public ArrayList<SelectValueDTO> getVecteurPublication() {
        return vecteurPublication;
    }

    public void setVecteurPublication(ArrayList<SelectValueDTO> vecteurPublication) {
        this.vecteurPublication = vecteurPublication;
    }

    public ArrayList<SelectValueDTO> getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(ArrayList<SelectValueDTO> typeActe) {
        this.typeActe = typeActe;
    }

    public ArrayList<SelectValueDTO> getCategorieActe() {
        return categorieActe;
    }

    public void setCategorieActe(ArrayList<SelectValueDTO> categorieActe) {
        this.categorieActe = categorieActe;
    }

    public ArrayList<SelectValueDTO> getStatut() {
        return statut;
    }

    public void setStatut(ArrayList<SelectValueDTO> statut) {
        this.statut = statut;
    }

    public ArrayList<SelectValueDTO> getMinistereAttache() {
        return ministereAttache;
    }

    public void setMinistereResponsable(ArrayList<SelectValueDTO> ministereAttache) {
        this.ministereAttache = ministereAttache;
    }

    public ArrayList<SelectValueDTO> getDirectionAttache() {
        return directionAttache;
    }

    public void setDirectionResponsable(ArrayList<SelectValueDTO> directionAttache) {
        this.directionAttache = directionAttache;
    }

    public ArrayList<SelectValueDTO> getStatutArchivage() {
        return statutArchivage;
    }

    public void setStatutArchivage(ArrayList<SelectValueDTO> statutArchivage) {
        this.statutArchivage = statutArchivage;
    }

    public String getPosteId() {
        return posteId;
    }

    public void setPosteId(String posteId) {
        this.posteId = posteId;
    }

    public Boolean getPosteEnCours() {
        return posteEnCours;
    }

    public void setPosteEnCours(Boolean posteEnCours) {
        this.posteEnCours = posteEnCours;
    }

    public Map<String, String> getMapPoste() {
        return mapPoste;
    }

    public void setMapPoste(Map<String, String> mapPoste) {
        this.mapPoste = mapPoste;
    }

    public Calendar getDateDebutCreation() {
        return dateDebutCreation;
    }

    public void setDateDebutCreation(Calendar dateDebutCreation) {
        this.dateDebutCreation = dateDebutCreation;
    }

    public Calendar getDateFinCreation() {
        return dateFinCreation;
    }

    public void setDateFinCreation(Calendar dateFinCreation) {
        this.dateFinCreation = dateFinCreation;
    }

    public Calendar getDateDebutPublication() {
        return dateDebutPublication;
    }

    public void setDateDebutPublication(Calendar dateDebutPublication) {
        this.dateDebutPublication = dateDebutPublication;
    }

    public Calendar getDateFinPublication() {
        return dateFinPublication;
    }

    public void setDateFinPublication(Calendar dateFinPublication) {
        this.dateFinPublication = dateFinPublication;
    }

    public Calendar getDateDebutSignature() {
        return dateDebutSignature;
    }

    public void setDateDebutSignature(Calendar dateDebutSignature) {
        this.dateDebutSignature = dateDebutSignature;
    }

    public Calendar getDateFinSignature() {
        return dateFinSignature;
    }

    public void setDateFinSignature(Calendar dateFinSignature) {
        this.dateFinSignature = dateFinSignature;
    }

    public Calendar getDateDebutPublicationJO() {
        return dateDebutPublicationJO;
    }

    public void setDateDebutPublicationJO(Calendar dateDebutPublicationJO) {
        this.dateDebutPublicationJO = dateDebutPublicationJO;
    }

    public Calendar getDateFinPublicationJO() {
        return dateFinPublicationJO;
    }

    public void setDateFinPublicationJO(Calendar dateFinPublicationJO) {
        this.dateFinPublicationJO = dateFinPublicationJO;
    }

    public Boolean getBaseProduction() {
        return baseProduction;
    }

    public void setBaseProduction(Boolean baseProduction) {
        this.baseProduction = baseProduction;
    }

    public Boolean getBaseArchivage() {
        return baseArchivage;
    }

    public void setBaseArchivage(Boolean baseArchivage) {
        this.baseArchivage = baseArchivage;
    }

    public Boolean getExpressionExacte() {
        return expressionExacte;
    }

    public void setExpressionExacte(Boolean expressionExacte) {
        this.expressionExacte = expressionExacte;
    }

    public Boolean getDerniereVersion() {
        return derniereVersion;
    }

    public void setDerniereVersion(Boolean derniereVersion) {
        this.derniereVersion = derniereVersion;
    }

    public String getRecherche() {
        return recherche;
    }

    public void setRecherche(String recherche) {
        this.recherche = recherche;
    }

    private List<String> convertToListId(ArrayList<SelectValueDTO> list) {
        return list.stream().map(t -> t.getId()).collect(Collectors.toList());
    }

    public List<String> getVecteurPublicationId() {
        return convertToListId(getVecteurPublication());
    }

    public List<String> getTypeActeId() {
        return convertToListId(getTypeActe());
    }

    public List<String> getCategorieActeId() {
        return convertToListId(getCategorieActe());
    }

    public List<String> getStatutId() {
        return convertToListId(getStatut());
    }

    public List<String> getMinistereAttacheId() {
        return convertToListId(getMinistereAttache());
    }

    public List<String> getDirectionAttacheId() {
        return convertToListId(getDirectionAttache());
    }

    public List<String> getStatutArchivageId() {
        return convertToListId(getStatutArchivage());
    }

    public boolean isEmpty() {
        return (
            Arrays
                .asList(
                    this.vecteurPublication,
                    this.typeActe,
                    this.categorieActe,
                    this.statut,
                    this.ministereAttache,
                    this.directionAttache,
                    this.statutArchivage
                )
                .stream()
                .allMatch(CollectionUtils::isEmpty) &&
            StringUtils.isAllBlank(posteId, recherche) &&
            ObjectHelper.allNull(
                posteEnCours,
                dateDebutCreation,
                dateFinCreation,
                dateDebutPublication,
                dateFinPublication,
                dateDebutSignature,
                dateFinSignature,
                dateDebutPublicationJO,
                dateFinPublicationJO
            )
        );
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
