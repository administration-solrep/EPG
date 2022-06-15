package fr.dila.solonepg.ui.th.bean;

import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.FormSort;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

@SwBean(keepdefaultValue = true)
public class EpgUserListForm extends AbstractSortablePaginationForm {
    public static final String NOM = "nom";
    public static final String PRENOM = "prenom";
    public static final String UTILISATEUR = "utilisateur";
    public static final String MEL = "mel";
    public static final String DATE_DEBUT = "dateDebut";

    @QueryParam(NOM)
    @FormParam(NOM)
    protected SortOrder nom;

    @QueryParam(PRENOM)
    @FormParam(PRENOM)
    protected SortOrder prenom;

    @QueryParam(UTILISATEUR)
    @FormParam(UTILISATEUR)
    protected SortOrder utilisateur;

    @QueryParam(MEL)
    @FormParam(MEL)
    private SortOrder mel;

    @QueryParam(DATE_DEBUT)
    @FormParam(DATE_DEBUT)
    private SortOrder dateDebut;

    public EpgUserListForm() {}

    public SortOrder getNom() {
        return nom;
    }

    public void setNom(SortOrder nom) {
        this.nom = nom;
    }

    public SortOrder getPrenom() {
        return prenom;
    }

    public void setPrenom(SortOrder prenom) {
        this.prenom = prenom;
    }

    public SortOrder getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(SortOrder utilisateur) {
        this.utilisateur = utilisateur;
    }

    public SortOrder getMel() {
        return mel;
    }

    public void setMel(SortOrder mel) {
        this.mel = mel;
    }

    public SortOrder getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(SortOrder dateDebut) {
        this.dateDebut = dateDebut;
    }

    @Override
    protected void setDefaultSort() {
        nom = SortOrder.ASC;
    }

    @Override
    protected Map<String, FormSort> getSortForm() {
        Map<String, FormSort> map = new HashMap<>();
        map.put(STSchemaConstant.USER_LAST_NAME, new FormSort(getNom()));
        map.put(STSchemaConstant.USER_FIRST_NAME, new FormSort(getPrenom()));
        map.put(STSchemaConstant.USER_USERNAME, new FormSort(getUtilisateur()));
        map.put(STSchemaConstant.USER_EMAIL, new FormSort(getMel()));
        map.put(STSchemaConstant.USER_DATE_DEBUT, new FormSort(getDateDebut()));
        return map;
    }
}
