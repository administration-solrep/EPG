package fr.dila.solonepg.ui.bean.dossier.similaire;

import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_STATUT_LABEL_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_XPATH;
import static fr.dila.st.api.constant.STSchemaConstant.DUBLINCORE_CREATOR_XPATH;

import fr.dila.solonepg.ui.bean.AbstractEpgSortablePaginationForm;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.FormSort;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

@SwBean(keepdefaultValue = true)
public class DossierSimilaireListForm extends AbstractEpgSortablePaginationForm {

    public DossierSimilaireListForm() {
        super();
    }

    @QueryParam("titre")
    @FormParam("titre")
    protected SortOrder titre;

    @QueryParam("titreOrder")
    @FormParam("titreOrder")
    protected Integer titreOrder;

    @QueryParam("nor")
    @FormParam("nor")
    protected SortOrder nor;

    @QueryParam("norOrder")
    @FormParam("norOrder")
    protected Integer norOrder;

    @QueryParam("statut")
    @FormParam("statut")
    protected SortOrder statut;

    @QueryParam("statutOrder")
    @FormParam("statutOrder")
    protected Integer statutOrder;

    @QueryParam("auteur")
    @FormParam("auteur")
    protected SortOrder auteur;

    @QueryParam("auteurOrder")
    @FormParam("auteurOrder")
    protected Integer auteurOrder;

    @SwRequired
    @SwId
    @QueryParam("dossierId")
    @FormParam("dossierId")
    protected String dossierId;

    public SortOrder getTitre() {
        return titre;
    }

    public void setTitre(SortOrder titre) {
        this.titre = titre;
    }

    public SortOrder getNor() {
        return nor;
    }

    public void setNor(SortOrder nor) {
        this.nor = nor;
    }

    public SortOrder getStatut() {
        return statut;
    }

    public void setStatut(SortOrder statut) {
        this.statut = statut;
    }

    public SortOrder getAuteur() {
        return auteur;
    }

    public void setAuteur(SortOrder auteur) {
        this.auteur = auteur;
    }

    public Integer getTitreOrder() {
        return titreOrder;
    }

    public void setTitreOrder(Integer titreOrder) {
        this.titreOrder = titreOrder;
    }

    public Integer getNorOrder() {
        return norOrder;
    }

    public void setNorOrder(Integer norOrder) {
        this.norOrder = norOrder;
    }

    public Integer getStatutOrder() {
        return statutOrder;
    }

    public void setStatutOrder(Integer statutOrder) {
        this.statutOrder = statutOrder;
    }

    public Integer getAuteurOrder() {
        return auteurOrder;
    }

    public void setAuteurOrder(Integer auteurOrder) {
        this.auteurOrder = auteurOrder;
    }

    public String getDossierId() {
        return dossierId;
    }

    public void setDossierId(String dossierId) {
        this.dossierId = dossierId;
    }

    @Override
    protected void setDefaultSort() {
        titre = SortOrder.ASC;
    }

    @Override
    protected Map<String, FormSort> getSortForm() {
        Map<String, FormSort> map = new HashMap<>();
        map.put(DOSSIER_NUMERO_NOR_XPATH, new FormSort(nor, norOrder));
        map.put(DOSSIER_TITRE_ACTE_XPATH, new FormSort(titre, titreOrder));
        map.put(DOSSIER_STATUT_LABEL_XPATH, new FormSort(statut, statutOrder));
        map.put(DUBLINCORE_CREATOR_XPATH, new FormSort(auteur, auteurOrder));

        return map;
    }
}
