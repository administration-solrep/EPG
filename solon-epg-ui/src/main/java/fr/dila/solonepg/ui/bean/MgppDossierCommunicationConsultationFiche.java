package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.ui.th.bean.MgppRepresentantTableForm;
import fr.dila.st.ui.bean.VersionSelectDTO;
import fr.dila.st.ui.bean.WidgetDTO;
import java.util.ArrayList;
import java.util.List;

public class MgppDossierCommunicationConsultationFiche {
    private List<WidgetDTO> lstWidgetsPresentation = new ArrayList<>();
    private List<WidgetDTO> lstWidgetsFiche = new ArrayList<>();
    private List<WidgetDTO> lstWidgetsDepot = new ArrayList<>();
    private List<MgppNavetteDTO> lstNavettes = new ArrayList<>();
    private List<WidgetDTO> lstWidgetsLoiVotee = new ArrayList<>();
    private List<WidgetDTO> lstWidgetsDetails = new ArrayList<>();
    private List<VersionSelectDTO> lstVersions = new ArrayList<>();
    private List<MgppRepresentantTableForm> lstTablesRepresentants = new ArrayList<>();
    private String curVersion;
    private String natureVersion;
    private String typeFiche;
    private String idDossier;
    private boolean isFicheDiffusee = false;

    public MgppDossierCommunicationConsultationFiche() {
        super();
    }

    public List<WidgetDTO> getLstWidgetsFiche() {
        return lstWidgetsFiche;
    }

    public void setLstWidgetsFiche(List<WidgetDTO> lstWidgets) {
        this.lstWidgetsFiche = lstWidgets;
    }

    public List<WidgetDTO> getLstWidgetsDepot() {
        return lstWidgetsDepot;
    }

    public void setLstWidgetsDepot(List<WidgetDTO> lstWidgetsDepot) {
        this.lstWidgetsDepot = lstWidgetsDepot;
    }

    public List<MgppNavetteDTO> getLstNavettes() {
        return lstNavettes;
    }

    public void setLstNavettes(List<MgppNavetteDTO> lstNavettes) {
        this.lstNavettes = lstNavettes;
    }

    public List<WidgetDTO> getLstWidgetsLoiVotee() {
        return lstWidgetsLoiVotee;
    }

    public void setLstWidgetsLoiVotee(List<WidgetDTO> lstWidgetsLoiVotee) {
        this.lstWidgetsLoiVotee = lstWidgetsLoiVotee;
    }

    public List<WidgetDTO> getLstWidgetsPresentation() {
        return lstWidgetsPresentation;
    }

    public void setLstWidgetsPresentation(List<WidgetDTO> lstWidgetsPresentation) {
        this.lstWidgetsPresentation = lstWidgetsPresentation;
    }

    public List<WidgetDTO> getLstWidgetsDetails() {
        return lstWidgetsDetails;
    }

    public void setLstWidgetsDetails(List<WidgetDTO> lstWidgetsDetails) {
        this.lstWidgetsDetails = lstWidgetsDetails;
    }

    public List<VersionSelectDTO> getLstVersions() {
        return lstVersions;
    }

    public void setLstVersions(List<VersionSelectDTO> lstVersions) {
        this.lstVersions = lstVersions;
    }

    public String getCurVersion() {
        return curVersion;
    }

    public void setCurVersion(String curVersion) {
        this.curVersion = curVersion;
    }

    public String getNatureVersion() {
        return natureVersion;
    }

    public void setNatureVersion(String natureVersion) {
        this.natureVersion = natureVersion;
    }

    public List<MgppRepresentantTableForm> getLstTablesRepresentants() {
        return lstTablesRepresentants;
    }

    public void setLstTablesRepresentants(List<MgppRepresentantTableForm> lstTablesRepresentants) {
        this.lstTablesRepresentants = lstTablesRepresentants;
    }

    public String getTypeFiche() {
        return typeFiche;
    }

    public void setTypeFiche(String typeFiche) {
        this.typeFiche = typeFiche;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public boolean isFicheDiffusee() {
        return isFicheDiffusee;
    }

    public void setFicheDiffusee(boolean isFicheDiffusee) {
        this.isFicheDiffusee = isFicheDiffusee;
    }
}
