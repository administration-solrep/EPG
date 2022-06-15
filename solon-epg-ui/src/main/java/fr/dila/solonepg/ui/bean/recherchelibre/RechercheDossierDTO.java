package fr.dila.solonepg.ui.bean.recherchelibre;

import fr.dila.ss.api.recherche.IdLabel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RechercheDossierDTO {
    private String titre;

    private String nor;

    private Calendar dateCreation;

    private Calendar datePublication;

    private String dossierId;

    private String ministereResponsable;

    private String statut;

    private List<DocumentRechercheDTO> lstDocuments = new ArrayList<>();

    private Boolean isInSelectionTool;

    private List<IdLabel> caseLinkIdsLabels;

    public RechercheDossierDTO() {}

    public RechercheDossierDTO(
        String titre,
        String nor,
        Calendar dateCreation,
        Calendar datePublication,
        String ministereResponsable,
        String statut,
        List<DocumentRechercheDTO> lstDocuments
    ) {
        this.titre = titre;
        this.nor = nor;
        this.dateCreation = dateCreation;
        this.datePublication = datePublication;
        this.ministereResponsable = ministereResponsable;
        this.statut = statut;
        this.lstDocuments = lstDocuments;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public Calendar getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Calendar dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Calendar getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Calendar datePublication) {
        this.datePublication = datePublication;
    }

    public String getDossierId() {
        return dossierId;
    }

    public void setDossierId(String dossierId) {
        this.dossierId = dossierId;
    }

    public String getMinistereResponsable() {
        return ministereResponsable;
    }

    public void setMinistereResponsable(String ministereResponsable) {
        this.ministereResponsable = ministereResponsable;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<DocumentRechercheDTO> getLstDocuments() {
        return lstDocuments;
    }

    public void setLstDocuments(List<DocumentRechercheDTO> lstDocuments) {
        this.lstDocuments = lstDocuments;
    }

    public Boolean getIsInSelectionTool() {
        return isInSelectionTool;
    }

    public void setIsInSelectionTool(Boolean isInSelectionTool) {
        this.isInSelectionTool = isInSelectionTool;
    }

    public List<IdLabel> getCaseLinkIdsLabels() {
        return caseLinkIdsLabels;
    }

    public void setCaseLinkIdsLabels(List<IdLabel> caseLinkIdsLabels) {
        this.caseLinkIdsLabels = caseLinkIdsLabels;
    }
}
