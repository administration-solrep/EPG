package fr.dila.solonepg.ui.th.bean;

import com.google.gson.Gson;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.FormSort;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang3.StringUtils;

@SwBean
public class MessageListForm extends AbstractSortablePaginationForm {
    private static final String ID_DOSSIER_TRI = "idDossierTri";
    private static final String OBJET_DOSSIER_TRI = "objetDossierTri";
    private static final String NIV_LECTURE_TRI = "nivLectureTri";
    private static final String EMETTEUR_TRI = "emetteurTri";
    private static final String DESTINATAIRE_TRI = "destinataireTri";
    private static final String COPIE_TRI = "copieTri";
    private static final String COMMUNICATION_TRI = "communicationTri";
    private static final String DATE_TRI = "dateTri";
    private static final String ID_COMMUNICATION_TRI = "idCommunicationTri";

    private static final String EVT_SCHEMA_PREFIX = "evt:";
    private static final String MSG_SCHEMA_PREFIX = "msg:";

    @SuppressWarnings("unchecked")
    public MessageListForm(String json) {
        super();
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, Map.class);

        if (map != null) {
            idDossier = SortOrder.fromValue((String) map.get(ID_DOSSIER_TRI));
            objetDossier = SortOrder.fromValue((String) map.get(OBJET_DOSSIER_TRI));
            emetteur = SortOrder.fromValue((String) map.get(EMETTEUR_TRI));
            destinataire = SortOrder.fromValue((String) map.get(DESTINATAIRE_TRI));
            copie = SortOrder.fromValue((String) map.get(COPIE_TRI));
            communication = SortOrder.fromValue((String) map.get(COMMUNICATION_TRI));
            date = SortOrder.fromValue((String) map.get(DATE_TRI));
            idCommunication = SortOrder.fromValue((String) map.get(ID_COMMUNICATION_TRI));
            nivLecture = SortOrder.fromValue((String) map.get(NIV_LECTURE_TRI));
            if (StringUtils.isNotEmpty((String) map.get(PAGE_PARAM_NAME))) {
                setPage((String) map.get(PAGE_PARAM_NAME));
            }
            if (StringUtils.isNotEmpty((String) map.get(SIZE_PARAM_NAME))) {
                setSize((String) map.get(SIZE_PARAM_NAME));
            }
        }
    }

    @QueryParam(ID_DOSSIER_TRI)
    @FormParam(ID_DOSSIER_TRI)
    private SortOrder idDossier;

    @QueryParam(OBJET_DOSSIER_TRI)
    @FormParam(OBJET_DOSSIER_TRI)
    private SortOrder objetDossier;

    @QueryParam(EMETTEUR_TRI)
    @FormParam(EMETTEUR_TRI)
    private SortOrder emetteur;

    @QueryParam(DESTINATAIRE_TRI)
    @FormParam(DESTINATAIRE_TRI)
    private SortOrder destinataire;

    @QueryParam(COPIE_TRI)
    @FormParam(COPIE_TRI)
    private SortOrder copie;

    @QueryParam(COMMUNICATION_TRI)
    @FormParam(COMMUNICATION_TRI)
    private SortOrder communication;

    @QueryParam(DATE_TRI)
    @FormParam(DATE_TRI)
    private SortOrder date;

    @QueryParam(ID_COMMUNICATION_TRI)
    @FormParam(ID_COMMUNICATION_TRI)
    private SortOrder idCommunication;

    @QueryParam(NIV_LECTURE_TRI)
    @FormParam(NIV_LECTURE_TRI)
    private SortOrder nivLecture;

    public MessageListForm() {
        super();
    }

    public SortOrder getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(SortOrder idDossier) {
        this.idDossier = idDossier;
    }

    public SortOrder getObjetDossier() {
        return objetDossier;
    }

    public void setObjetDossier(SortOrder objetDossier) {
        this.objetDossier = objetDossier;
    }

    public SortOrder getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(SortOrder emetteur) {
        this.emetteur = emetteur;
    }

    public SortOrder getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(SortOrder destinataire) {
        this.destinataire = destinataire;
    }

    public SortOrder getCopie() {
        return copie;
    }

    public void setCopie(SortOrder copie) {
        this.copie = copie;
    }

    public SortOrder getCommunication() {
        return communication;
    }

    public void setCommunication(SortOrder communication) {
        this.communication = communication;
    }

    public SortOrder getDate() {
        return date;
    }

    public void setDate(SortOrder date) {
        this.date = date;
    }

    public SortOrder getIdCommunication() {
        return idCommunication;
    }

    public void setIdCommunication(SortOrder idCommunication) {
        this.idCommunication = idCommunication;
    }

    public SortOrder getNivLecture() {
        return nivLecture;
    }

    public void setNivLecture(SortOrder nivLecture) {
        this.nivLecture = nivLecture;
    }

    @Override
    protected void setDefaultSort() {
        date = SortOrder.DESC;
    }

    @Override
    protected Map<String, FormSort> getSortForm() {
        Map<String, FormSort> map = new HashMap<>();
        map.put(MSG_SCHEMA_PREFIX + "id_dossier", new FormSort(getIdDossier()));
        map.put(EVT_SCHEMA_PREFIX + "objet", new FormSort(getObjetDossier()));
        map.put(MSG_SCHEMA_PREFIX + "niveau_lecture.code", new FormSort(getNivLecture(), 1));
        map.put(MSG_SCHEMA_PREFIX + "niveau_lecture.niveau", new FormSort(getNivLecture(), 2));
        map.put(MSG_SCHEMA_PREFIX + "emetteur_evenement", new FormSort(getEmetteur()));
        map.put(MSG_SCHEMA_PREFIX + "destinataire_evenement", new FormSort(getDestinataire()));
        map.put(MSG_SCHEMA_PREFIX + "copie_evenement", new FormSort(getCopie()));
        map.put(MSG_SCHEMA_PREFIX + "type_evenement", new FormSort(getCommunication()));
        map.put(MSG_SCHEMA_PREFIX + "date_evenement", new FormSort(getDate()));
        map.put(MSG_SCHEMA_PREFIX + "id_evenement", new FormSort(getIdCommunication()));
        return map;
    }

    public static MessageListForm newForm() {
        return initForm(new MessageListForm());
    }
}
