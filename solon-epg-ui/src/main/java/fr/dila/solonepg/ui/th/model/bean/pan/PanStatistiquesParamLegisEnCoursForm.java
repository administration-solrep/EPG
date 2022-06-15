package fr.dila.solonepg.ui.th.model.bean.pan;

import fr.dila.st.ui.annot.SwBean;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class PanStatistiquesParamLegisEnCoursForm {
    @FormParam("debutLegislatureEnCours")
    private Calendar debutLegislatureEnCours;

    @FormParam("promulgationLoisBSLegisEnCoursDebut")
    private Calendar debutPromulgationLoisBSLegisEnCours;

    @FormParam("promulgationLoisBSLegisEnCoursFin")
    private Calendar finPromulgationLoisBSLegisEnCours;

    @FormParam("publicationDecretsLoisBSLegisEnCoursDebut")
    private Calendar debutPublicationDecretsLoisBSLegisEnCours;

    @FormParam("publicationDecretsLoisBSLegisEnCoursFin")
    private Calendar finPublicationDecretsLoisBSLegisEnCours;

    @FormParam("promulgationOrdonnancesBSLegisEnCoursDebut")
    private Calendar debutPromulgationOrdonnancesBSLegisEnCours;

    @FormParam("promulgationOrdonnancesBSLegisEnCoursFin")
    private Calendar finPromulgationOrdonnancesBSLegisEnCours;

    @FormParam("publicationDecretsOrdonnancesBSLegisEnCoursDebut")
    private Calendar debutPublicationDecretsOrdonnancesBSLegisEnCours;

    @FormParam("publicationDecretsOrdonnancesBSLegisEnCoursFin")
    private Calendar finPublicationDecretsOrdonnancesBSLegisEnCours;

    @FormParam("promulgationLoisTXDLegisEnCoursDebut")
    private Calendar debutPromulgationLoisTXDLegisEnCours;

    @FormParam("promulgationLoisTXDLegisEnCoursFin")
    private Calendar finPromulgationLoisTXDLegisEnCours;

    @FormParam("publicationDecretsLoisTXDLegisEnCoursDebut")
    private Calendar debutPublicationDecretsLoisTXDLegisEnCours;

    @FormParam("publicationDecretsLoisTXDLegisEnCoursFin")
    private Calendar finPublicationDecretsLoisTXDLegisEnCours;

    @FormParam("promulgationLoisTXLegisEnCoursDebut")
    private Calendar debutPromulgationLoisTXLegisEnCours;

    @FormParam("promulgationLoisTXLegisEnCoursFin")
    private Calendar finPromulgationLoisTXLegisEnCours;

    @FormParam("publicationDecretsLoisTXLegisEnCoursDebut")
    private Calendar debutPublicationDecretsLoisTXLegisEnCours;

    @FormParam("publicationDecretsLoisTXLegisEnCoursFin")
    private Calendar finPublicationDecretsLoisTXLegisEnCours;

    @FormParam("promulgationOrdonnancesTXDLegisEnCoursDebut")
    private Calendar debutPromulgationOrdonnancesTXDLegisEnCours;

    @FormParam("promulgationOrdonnancesTXDLegisEnCoursFin")
    private Calendar finPromulgationOrdonnancesTXDLegisEnCours;

    @FormParam("publicationDecretsOrdonnancesTXDLegisEnCoursDebut")
    private Calendar debutPublicationDecretsOrdonnancesTXDLegisEnCours;

    @FormParam("publicationDecretsOrdonnancesTXDLegisEnCoursFin")
    private Calendar finPublicationDecretsOrdonnancesTXDLegisEnCours;

    @FormParam("promulgationOrdonnancesTXLegisEnCoursDebut")
    private Calendar debutPromulgationOrdonnancesTXLegisEnCours;

    @FormParam("promulgationOrdonnancesTXLegisEnCoursFin")
    private Calendar finPromulgationOrdonnancesTXLegisEnCours;

    @FormParam("publicationDecretsOrdonnancesTXLegisEnCoursDebut")
    private Calendar debutPublicationDecretsOrdonnancesTXLegisEnCours;

    @FormParam("publicationDecretsOrdonnancesTXLegisEnCoursFin")
    private Calendar finPublicationDecretsOrdonnancesTXLegisEnCours;

    public PanStatistiquesParamLegisEnCoursForm() {}

    public Calendar getDebutLegislatureEnCours() {
        return debutLegislatureEnCours;
    }

    public void setDebutLegislatureEnCours(Calendar debutLegislatureEnCours) {
        this.debutLegislatureEnCours = debutLegislatureEnCours;
    }

    public Calendar getDebutPromulgationLoisBSLegisEnCours() {
        return debutPromulgationLoisBSLegisEnCours;
    }

    public void setDebutPromulgationLoisBSLegisEnCours(Calendar debutPromulgationLoisBSLegisEnCours) {
        this.debutPromulgationLoisBSLegisEnCours = debutPromulgationLoisBSLegisEnCours;
    }

    public Calendar getFinPromulgationLoisBSLegisEnCours() {
        return finPromulgationLoisBSLegisEnCours;
    }

    public void setFinPromulgationLoisBSLegisEnCours(Calendar finPromulgationLoisBSLegisEnCours) {
        this.finPromulgationLoisBSLegisEnCours = finPromulgationLoisBSLegisEnCours;
    }

    public Calendar getDebutPublicationDecretsLoisBSLegisEnCours() {
        return debutPublicationDecretsLoisBSLegisEnCours;
    }

    public void setDebutPublicationDecretsLoisBSLegisEnCours(Calendar debutPublicationDecretsLoisBSLegisEnCours) {
        this.debutPublicationDecretsLoisBSLegisEnCours = debutPublicationDecretsLoisBSLegisEnCours;
    }

    public Calendar getFinPublicationDecretsLoisBSLegisEnCours() {
        return finPublicationDecretsLoisBSLegisEnCours;
    }

    public void setFinPublicationDecretsLoisBSLegisEnCours(Calendar finPublicationDecretsLoisBSLegisEnCours) {
        this.finPublicationDecretsLoisBSLegisEnCours = finPublicationDecretsLoisBSLegisEnCours;
    }

    public Calendar getDebutPromulgationOrdonnancesBSLegisEnCours() {
        return debutPromulgationOrdonnancesBSLegisEnCours;
    }

    public void setDebutPromulgationOrdonnancesBSLegisEnCours(Calendar debutPromulgationOrdonnancesBSLegisEnCours) {
        this.debutPromulgationOrdonnancesBSLegisEnCours = debutPromulgationOrdonnancesBSLegisEnCours;
    }

    public Calendar getFinPromulgationOrdonnancesBSLegisEnCours() {
        return finPromulgationOrdonnancesBSLegisEnCours;
    }

    public void setFinPromulgationOrdonnancesBSLegisEnCours(Calendar finPromulgationOrdonnancesBSLegisEnCours) {
        this.finPromulgationOrdonnancesBSLegisEnCours = finPromulgationOrdonnancesBSLegisEnCours;
    }

    public Calendar getDebutPublicationDecretsOrdonnancesBSLegisEnCours() {
        return debutPublicationDecretsOrdonnancesBSLegisEnCours;
    }

    public void setDebutPublicationDecretsOrdonnancesBSLegisEnCours(
        Calendar debutPublicationDecretsOrdonnancesBSLegisEnCours
    ) {
        this.debutPublicationDecretsOrdonnancesBSLegisEnCours = debutPublicationDecretsOrdonnancesBSLegisEnCours;
    }

    public Calendar getFinPublicationDecretsOrdonnancesBSLegisEnCours() {
        return finPublicationDecretsOrdonnancesBSLegisEnCours;
    }

    public void setFinPublicationDecretsOrdonnancesBSLegisEnCours(
        Calendar finPublicationDecretsOrdonnancesBSLegisEnCours
    ) {
        this.finPublicationDecretsOrdonnancesBSLegisEnCours = finPublicationDecretsOrdonnancesBSLegisEnCours;
    }

    public Calendar getDebutPromulgationLoisTXDLegisEnCours() {
        return debutPromulgationLoisTXDLegisEnCours;
    }

    public void setDebutPromulgationLoisTXDLegisEnCours(Calendar debutPromulgationLoisTXDLegisEnCours) {
        this.debutPromulgationLoisTXDLegisEnCours = debutPromulgationLoisTXDLegisEnCours;
    }

    public Calendar getFinPromulgationLoisTXDLegisEnCours() {
        return finPromulgationLoisTXDLegisEnCours;
    }

    public void setFinPromulgationLoisTXDLegisEnCours(Calendar finPromulgationLoisTXDLegisEnCours) {
        this.finPromulgationLoisTXDLegisEnCours = finPromulgationLoisTXDLegisEnCours;
    }

    public Calendar getDebutPublicationDecretsLoisTXDLegisEnCours() {
        return debutPublicationDecretsLoisTXDLegisEnCours;
    }

    public void setDebutPublicationDecretsLoisTXDLegisEnCours(Calendar debutPublicationDecretsLoisTXDLegisEnCours) {
        this.debutPublicationDecretsLoisTXDLegisEnCours = debutPublicationDecretsLoisTXDLegisEnCours;
    }

    public Calendar getFinPublicationDecretsLoisTXDLegisEnCours() {
        return finPublicationDecretsLoisTXDLegisEnCours;
    }

    public void setFinPublicationDecretsLoisTXDLegisEnCours(Calendar finPublicationDecretsLoisTXDLegisEnCours) {
        this.finPublicationDecretsLoisTXDLegisEnCours = finPublicationDecretsLoisTXDLegisEnCours;
    }

    public Calendar getDebutPromulgationLoisTXLegisEnCours() {
        return debutPromulgationLoisTXLegisEnCours;
    }

    public void setDebutPromulgationLoisTXLegisEnCours(Calendar debutPromulgationLoisTXLegisEnCours) {
        this.debutPromulgationLoisTXLegisEnCours = debutPromulgationLoisTXLegisEnCours;
    }

    public Calendar getFinPromulgationLoisTXLegisEnCours() {
        return finPromulgationLoisTXLegisEnCours;
    }

    public void setFinPromulgationLoisTXLegisEnCours(Calendar finPromulgationLoisTXLegisEnCours) {
        this.finPromulgationLoisTXLegisEnCours = finPromulgationLoisTXLegisEnCours;
    }

    public Calendar getDebutPublicationDecretsLoisTXLegisEnCours() {
        return debutPublicationDecretsLoisTXLegisEnCours;
    }

    public void setDebutPublicationDecretsLoisTXLegisEnCours(Calendar debutPublicationDecretsLoisTXLegisEnCours) {
        this.debutPublicationDecretsLoisTXLegisEnCours = debutPublicationDecretsLoisTXLegisEnCours;
    }

    public Calendar getFinPublicationDecretsLoisTXLegisEnCours() {
        return finPublicationDecretsLoisTXLegisEnCours;
    }

    public void setFinPublicationDecretsLoisTXLegisEnCours(Calendar finPublicationDecretsLoisTXLegisEnCours) {
        this.finPublicationDecretsLoisTXLegisEnCours = finPublicationDecretsLoisTXLegisEnCours;
    }

    public Calendar getDebutPromulgationOrdonnancesTXDLegisEnCours() {
        return debutPromulgationOrdonnancesTXDLegisEnCours;
    }

    public void setDebutPromulgationOrdonnancesTXDLegisEnCours(Calendar debutPromulgationOrdonnancesTXDLegisEnCours) {
        this.debutPromulgationOrdonnancesTXDLegisEnCours = debutPromulgationOrdonnancesTXDLegisEnCours;
    }

    public Calendar getFinPromulgationOrdonnancesTXDLegisEnCours() {
        return finPromulgationOrdonnancesTXDLegisEnCours;
    }

    public void setFinPromulgationOrdonnancesTXDLegisEnCours(Calendar finPromulgationOrdonnancesTXDLegisEnCours) {
        this.finPromulgationOrdonnancesTXDLegisEnCours = finPromulgationOrdonnancesTXDLegisEnCours;
    }

    public Calendar getDebutPublicationDecretsOrdonnancesTXDLegisEnCours() {
        return debutPublicationDecretsOrdonnancesTXDLegisEnCours;
    }

    public void setDebutPublicationDecretsOrdonnancesTXDLegisEnCours(
        Calendar debutPublicationDecretsOrdonnancesTXDLegisEnCours
    ) {
        this.debutPublicationDecretsOrdonnancesTXDLegisEnCours = debutPublicationDecretsOrdonnancesTXDLegisEnCours;
    }

    public Calendar getFinPublicationDecretsOrdonnancesTXDLegisEnCours() {
        return finPublicationDecretsOrdonnancesTXDLegisEnCours;
    }

    public void setFinPublicationDecretsOrdonnancesTXDLegisEnCours(
        Calendar finPublicationDecretsOrdonnancesTXDLegisEnCours
    ) {
        this.finPublicationDecretsOrdonnancesTXDLegisEnCours = finPublicationDecretsOrdonnancesTXDLegisEnCours;
    }

    public Calendar getDebutPromulgationOrdonnancesTXLegisEnCours() {
        return debutPromulgationOrdonnancesTXLegisEnCours;
    }

    public void setDebutPromulgationOrdonnancesTXLegisEnCours(Calendar debutPromulgationOrdonnancesTXLegisEnCours) {
        this.debutPromulgationOrdonnancesTXLegisEnCours = debutPromulgationOrdonnancesTXLegisEnCours;
    }

    public Calendar getFinPromulgationOrdonnancesTXLegisEnCours() {
        return finPromulgationOrdonnancesTXLegisEnCours;
    }

    public void setFinPromulgationOrdonnancesTXLegisEnCours(Calendar finPromulgationOrdonnancesTXLegisEnCours) {
        this.finPromulgationOrdonnancesTXLegisEnCours = finPromulgationOrdonnancesTXLegisEnCours;
    }

    public Calendar getDebutPublicationDecretsOrdonnancesTXLegisEnCours() {
        return debutPublicationDecretsOrdonnancesTXLegisEnCours;
    }

    public void setDebutPublicationDecretsOrdonnancesTXLegisEnCours(
        Calendar debutPublicationDecretsOrdonnancesTXLegisEnCours
    ) {
        this.debutPublicationDecretsOrdonnancesTXLegisEnCours = debutPublicationDecretsOrdonnancesTXLegisEnCours;
    }

    public Calendar getFinPublicationDecretsOrdonnancesTXLegisEnCours() {
        return finPublicationDecretsOrdonnancesTXLegisEnCours;
    }

    public void setFinPublicationDecretsOrdonnancesTXLegisEnCours(
        Calendar finPublicationDecretsOrdonnancesTXLegisEnCours
    ) {
        this.finPublicationDecretsOrdonnancesTXLegisEnCours = finPublicationDecretsOrdonnancesTXLegisEnCours;
    }
}
