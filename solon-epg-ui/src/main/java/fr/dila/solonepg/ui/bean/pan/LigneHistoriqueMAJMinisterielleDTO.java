package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.st.core.client.AbstractMapDTO;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class LigneHistoriqueMAJMinisterielleDTO extends AbstractMapDTO {
    private static final long serialVersionUID = 1L;

    private static final String ID = "id";
    private static final String ACTIVITE_NORMATIVE_ID = "activiteNormativeId";
    private static final String DOC_ID_FOR_SELECTION = "docIdForSelection";

    public LigneHistoriqueMAJMinisterielleDTO() {
        super();
    }

    public LigneHistoriqueMAJMinisterielleDTO(Map<String, Serializable> map) {
        setId((String) map.get("docIdForSelection"));
        setDateMaJ((Date) map.get("dateCreation"));
        setEtat((String) map.get("modification"));
        setIdDossier((String) map.get("idDossier"));
        setActiviteNormativeId((String) map.get("activiteNormativeId"));
        setNumeroNor((String) map.get("norDossier"));
        setReference((String) map.get("ref"));
        setTitre((String) map.get("titre"));
        setNumeroArticles((String) map.get("numeroArticles"));
        setNumeroOrdre((String) map.get("refMesure"));
        setCommentaire((String) map.get("commentaire"));
    }

    public String getId() {
        return getString(ID);
    }

    public void setId(String id) {
        put(ID, id);
    }

    public String getIdDossier() {
        return getString(ActiviteNormativeConstants.MAJ_MIN_ID_DOSSIER);
    }

    public void setIdDossier(String idDossier) {
        put(ActiviteNormativeConstants.MAJ_MIN_ID_DOSSIER, idDossier);
    }

    public String getActiviteNormativeId() {
        return getString(ACTIVITE_NORMATIVE_ID);
    }

    public void setActiviteNormativeId(String activiteNormativeId) {
        put(ACTIVITE_NORMATIVE_ID, activiteNormativeId);
    }

    public Date getDateMaJ() {
        return getDate(ActiviteNormativeConstants.MAJ_MIN_DATE_CREATION);
    }

    public void setDateMaJ(Date dateMaJ) {
        put(ActiviteNormativeConstants.MAJ_MIN_DATE_CREATION, dateMaJ);
    }

    public String getEtat() {
        return getString(ActiviteNormativeConstants.MAJ_MIN_MODIFICATION);
    }

    public void setEtat(String etat) {
        put(ActiviteNormativeConstants.MAJ_MIN_MODIFICATION, etat);
    }

    public String getNumeroNor() {
        return getString(ActiviteNormativeConstants.MAJ_MIN_NOR_DOSSIER);
    }

    public void setNumeroNor(String numeroNor) {
        put(ActiviteNormativeConstants.MAJ_MIN_NOR_DOSSIER, numeroNor);
    }

    public String getReference() {
        return getString(ActiviteNormativeConstants.MAJ_MIN_REF);
    }

    public void setReference(String reference) {
        put(ActiviteNormativeConstants.MAJ_MIN_REF, reference);
    }

    public String getTitre() {
        return getString(ActiviteNormativeConstants.MAJ_MIN_TITRE);
    }

    public void setTitre(String titre) {
        put(ActiviteNormativeConstants.MAJ_MIN_TITRE, titre);
    }

    public String getNumeroArticles() {
        return getString(ActiviteNormativeConstants.MAJ_MIN_NUMERO_ARTICLE);
    }

    public void setNumeroArticles(String numeroArticles) {
        put(ActiviteNormativeConstants.MAJ_MIN_NUMERO_ARTICLE, numeroArticles);
    }

    public String getNumeroOrdre() {
        return getString(ActiviteNormativeConstants.MAJ_MIN_NUMERO_ORDRE);
    }

    public void setNumeroOrdre(String numeroOrdre) {
        put(ActiviteNormativeConstants.MAJ_MIN_NUMERO_ORDRE, numeroOrdre);
    }

    public String getCommentaire() {
        return getString(ActiviteNormativeConstants.MAJ_MIN_COMMENT_MAJ);
    }

    public void setCommentaire(String commentaire) {
        put(ActiviteNormativeConstants.MAJ_MIN_COMMENT_MAJ, commentaire);
    }

    @Override
    public String getType() {
        return "";
    }

    @Override
    public String getDocIdForSelection() {
        return getString(DOC_ID_FOR_SELECTION);
    }
}
