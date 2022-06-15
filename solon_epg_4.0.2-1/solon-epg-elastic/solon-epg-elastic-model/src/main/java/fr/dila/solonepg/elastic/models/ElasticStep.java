package fr.dila.solonepg.elastic.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ElasticStep implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ECM_CURRENT_LIFE_CYCLE_STATE = "ecm:currentLifeCycleState";
    public static final String RTSK_AUTOMATIC_VALIDATED = "rtsk:automaticValidated";
    public static final String RTSK_AUTOMATIC_VALIDATION = "rtsk:automaticValidation";
    public static final String RTSK_COMMENTS = "rtsk:comments";
    public static final String RTSK_DATE_DEBUT_ETAPE = "rtsk:dateDebutEtape";
    public static final String RTSK_DATE_FIN_ETAPE = "rtsk:dateFinEtape";
    public static final String RTSK_DISTRIBUTION_MAILBOX_ID = "rtsk:distributionMailboxId";
    public static final String RTSK_DUE_DATE = "rtsk:dueDate";
    public static final String RTSK_MINISTERE_ID = "rtsk:ministereId";
    public static final String RTSK_OBLIGATOIRE_MINISTERE = "rtsk:obligatoireMinistere";
    public static final String RTSK_OBLIGATOIRE_SGG = "rtsk:obligatoireSGG";
    public static final String RTSK_TYPE = "rtsk:type";
    public static final String RTSK_VALIDATION_STATUS = "rtsk:validationStatus";
    public static final String RTSK_VALIDATION_USER_ID = "rtsk:validationUserId";

    @JsonProperty(ECM_CURRENT_LIFE_CYCLE_STATE)
    private String ecmCurrentLifeCycleState;

    @JsonProperty(RTSK_AUTOMATIC_VALIDATED)
    private Boolean automaticValidated;

    @JsonProperty(RTSK_AUTOMATIC_VALIDATION)
    private Boolean automaticValidation;

    @JsonProperty(RTSK_COMMENTS)
    private List<String> comments;

    @JsonProperty(RTSK_DATE_DEBUT_ETAPE)
    private Date dateDebutEtape;

    @JsonProperty(RTSK_DATE_FIN_ETAPE)
    private Date dateFinEtape;

    @JsonProperty(RTSK_DISTRIBUTION_MAILBOX_ID)
    private String distributionmailboxId;

    @JsonProperty(RTSK_DUE_DATE)
    private Date dueDate;

    @JsonProperty(RTSK_MINISTERE_ID)
    private String ministereID;

    @JsonProperty(RTSK_OBLIGATOIRE_MINISTERE)
    private Boolean obligatoireMinistere;

    @JsonProperty(RTSK_OBLIGATOIRE_SGG)
    private Boolean obligatoireSgg;

    @JsonProperty(RTSK_TYPE)
    private String type;

    @JsonProperty(RTSK_VALIDATION_STATUS)
    private String validationStatus;

    @JsonProperty(RTSK_VALIDATION_USER_ID)
    private String validationUserId;

    public String getEcmCurrentLifeCycleState() {
        return ecmCurrentLifeCycleState;
    }

    public void setEcmCurrentLifeCycleState(String ecmCurrentLifeCycleState) {
        this.ecmCurrentLifeCycleState = ecmCurrentLifeCycleState;
    }

    public Boolean getAutomaticValidated() {
        return automaticValidated;
    }

    public void setAutomaticValidated(Boolean automaticValidated) {
        this.automaticValidated = automaticValidated;
    }

    public Boolean getAutomaticValidation() {
        return automaticValidation;
    }

    public void setAutomaticValidation(Boolean automaticValidation) {
        this.automaticValidation = automaticValidation;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public Date getDateDebutEtape() {
        return dateDebutEtape;
    }

    public void setDateDebutEtape(Date dateDebutEtape) {
        this.dateDebutEtape = dateDebutEtape;
    }

    public Date getDateFinEtape() {
        return dateFinEtape;
    }

    public void setDateFinEtape(Date dateFinEtape) {
        this.dateFinEtape = dateFinEtape;
    }

    public String getDistributionmailboxId() {
        return distributionmailboxId;
    }

    public void setDistributionmailboxId(String distributionmailboxId) {
        this.distributionmailboxId = distributionmailboxId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getMinistereID() {
        return ministereID;
    }

    public void setMinistereID(String ministereID) {
        this.ministereID = ministereID;
    }

    public Boolean getObligatoireMinistere() {
        return obligatoireMinistere;
    }

    public void setObligatoireMinistere(Boolean obligatoireMinistere) {
        this.obligatoireMinistere = obligatoireMinistere;
    }

    public Boolean getObligatoireSgg() {
        return obligatoireSgg;
    }

    public void setObligatoireSgg(Boolean obligatoireSgg) {
        this.obligatoireSgg = obligatoireSgg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }

    public String getValidationUserId() {
        return validationUserId;
    }

    public void setValidationUserId(String validationUserId) {
        this.validationUserId = validationUserId;
    }
}
