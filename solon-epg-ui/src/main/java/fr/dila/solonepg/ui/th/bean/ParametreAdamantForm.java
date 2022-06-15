package fr.dila.solonepg.ui.th.bean;

import fr.dila.solonepg.api.constant.SolonEpgParametrageAdamantConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.util.ArrayList;
import javax.ws.rs.FormParam;

@SwBean
public class ParametreAdamantForm {
    @SwNotEmpty
    @FormParam("numEntreeSolon")
    @NxProp(
        docType = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_XPATH_NUMERO_SOLON
    )
    private Long numEntreeSolon;

    @SwNotEmpty
    @FormParam("originatingAgencyId")
    @NxProp(
        docType = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_XPATH_ORIGINATING_AGENCY_IDENTIFIER
    )
    private String originatingAgencyId;

    @SwNotEmpty
    @FormParam("submissionAgencyId")
    @NxProp(
        docType = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_XPATH_SUBMISSION_AGENCY_IDENTIFIER
    )
    private String submissionAgencyId;

    @SwNotEmpty
    @FormParam("archivalProfile")
    @NxProp(
        docType = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_XPATH_ARCHIVAL_PROFILE
    )
    private String archivalProfile;

    @SwNotEmpty
    @FormParam("archivalProfileSpecific")
    @NxProp(
        docType = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_XPATH_ARCHIVAL_PROFILE_SPECIFIC
    )
    private String archivalProfileSpecific;

    @SwNotEmpty
    @FormParam("originatingAgencyId2")
    @NxProp(
        docType = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_XPATH_ORIGINATING_AGENCY_BLOC_IDENTIFIER
    )
    private String originatingAgencyId2;

    @SwNotEmpty
    @FormParam("submissionAgencyId2")
    @NxProp(
        docType = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_XPATH_SUBMISSION_AGENCY_BLOC_IDENTIFIER
    )
    private String submissionAgencyId2;

    @SwNotEmpty
    @FormParam("delaiEligibilite")
    @NxProp(
        docType = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_XPATH_DELAI_ELIGIBILITE
    )
    private Long delaiEligibilite;

    @SwNotEmpty
    @FormParam("nbDossier")
    @NxProp(
        docType = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_XPATH_NB_DOSSIER
    )
    private Long nbDossier;

    @SwNotEmpty
    @FormParam("vecteurPublication")
    @NxProp(
        docType = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_XPATH_VECTEUR_PUBLICATION
    )
    private ArrayList<String> vecteurPublication = new ArrayList<>();

    @SwNotEmpty
    @FormParam("typeActe")
    @NxProp(
        docType = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_XPATH_TYPE_ACTE
    )
    private ArrayList<String> typeActe = new ArrayList<>();

    public ParametreAdamantForm() {}

    public Long getNumEntreeSolon() {
        return numEntreeSolon;
    }

    public void setNumEntreeSolon(Long numEntreeSolon) {
        this.numEntreeSolon = numEntreeSolon;
    }

    public String getOriginatingAgencyId() {
        return originatingAgencyId;
    }

    public void setOriginatingAgencyId(String originatingAgencyId) {
        this.originatingAgencyId = originatingAgencyId;
    }

    public String getSubmissionAgencyId() {
        return submissionAgencyId;
    }

    public void setSubmissionAgencyId(String submissionAgencyId) {
        this.submissionAgencyId = submissionAgencyId;
    }

    public String getArchivalProfile() {
        return archivalProfile;
    }

    public void setArchivalProfile(String archivalProfile) {
        this.archivalProfile = archivalProfile;
    }

    public String getArchivalProfileSpecific() {
        return archivalProfileSpecific;
    }

    public void setArchivalProfileSpecific(String archivalProfileSpecific) {
        this.archivalProfileSpecific = archivalProfileSpecific;
    }

    public String getOriginatingAgencyId2() {
        return originatingAgencyId2;
    }

    public void setOriginatingAgencyId2(String originatingAgencyId2) {
        this.originatingAgencyId2 = originatingAgencyId2;
    }

    public String getSubmissionAgencyId2() {
        return submissionAgencyId2;
    }

    public void setSubmissionAgencyId2(String submissionAgencyId2) {
        this.submissionAgencyId2 = submissionAgencyId2;
    }

    public Long getDelaiEligibilite() {
        return delaiEligibilite;
    }

    public void setDelaiEligibilite(Long delaiEligibilite) {
        this.delaiEligibilite = delaiEligibilite;
    }

    public Long getNbDossier() {
        return nbDossier;
    }

    public void setNbDossier(Long nbDossier) {
        this.nbDossier = nbDossier;
    }

    public ArrayList<String> getVecteurPublication() {
        return vecteurPublication;
    }

    public void setVecteurPublication(ArrayList<String> vecteurPublication) {
        this.vecteurPublication = vecteurPublication;
    }

    public ArrayList<String> getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(ArrayList<String> typeActe) {
        this.typeActe = typeActe;
    }
}
