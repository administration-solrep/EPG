package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.api.cases.MajMin;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TYPE;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implementation de {@link MajMin} .
 *
 * @author jgomez
 *
 */
public class MajMinImpl extends STDomainObjectImpl implements MajMin {
    private static final long serialVersionUID = -2648569632965678124L;

    public MajMinImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public <T> T getAdapter(Class<T> itf) {
        return getDocument().getAdapter(itf);
    }

    private void setProperty(String propertyName, Object value) {
        super.setProperty(ActiviteNormativeConstants.MAJ_MIN_SCHEMA, propertyName, value);
    }

    private String getStringProperty(String propertyName) {
        return super.getStringProperty(ActiviteNormativeConstants.MAJ_MIN_SCHEMA, propertyName);
    }

    private Calendar getDateProperty(String propertyName) {
        return super.getDateProperty(ActiviteNormativeConstants.MAJ_MIN_SCHEMA, propertyName);
    }

    @Override
    public String getIdDossier() {
        return getStringProperty(ActiviteNormativeConstants.MAJ_MIN_ID_DOSSIER);
    }

    @Override
    public void setIdDossier(String idDossier) {
        setProperty(ActiviteNormativeConstants.MAJ_MIN_ID_DOSSIER, idDossier);
    }

    @Override
    public String getNorDossier() {
        return getStringProperty(ActiviteNormativeConstants.MAJ_MIN_NOR_DOSSIER);
    }

    @Override
    public void setNorDossier(String norDossier) {
        setProperty(ActiviteNormativeConstants.MAJ_MIN_NOR_DOSSIER, norDossier);
    }

    @Override
    public String getType() {
        return getStringProperty(ActiviteNormativeConstants.MAJ_MIN_TYPE);
    }

    @Override
    public void setType(String type) {
        setProperty(ActiviteNormativeConstants.MAJ_MIN_TYPE, type);
    }

    @Override
    public String getRef() {
        return getStringProperty(ActiviteNormativeConstants.MAJ_MIN_REF);
    }

    @Override
    public void setRef(String ref) {
        setProperty(ActiviteNormativeConstants.MAJ_MIN_REF, ref);
    }

    @Override
    public String getTitre() {
        return getStringProperty(ActiviteNormativeConstants.MAJ_MIN_TITRE);
    }

    @Override
    public void setTitre(String titre) {
        setProperty(ActiviteNormativeConstants.MAJ_MIN_TITRE, titre);
    }

    @Override
    public String getNumeroArticle() {
        return getStringProperty(ActiviteNormativeConstants.MAJ_MIN_NUMERO_ARTICLE);
    }

    @Override
    public void setNumeroArticle(String numeroArticle) {
        setProperty(ActiviteNormativeConstants.MAJ_MIN_NUMERO_ARTICLE, numeroArticle);
    }

    @Override
    public String getCommentMaj() {
        return getStringProperty(ActiviteNormativeConstants.MAJ_MIN_COMMENT_MAJ);
    }

    @Override
    public void setCommentMaj(String commentMaj) {
        setProperty(ActiviteNormativeConstants.MAJ_MIN_COMMENT_MAJ, commentMaj);
    }

    @Override
    public Calendar getDateCreation() {
        return getDateProperty(ActiviteNormativeConstants.MAJ_MIN_DATE_CREATION);
    }

    @Override
    public void setDateCreation(Calendar dateCreation) {
        setProperty(ActiviteNormativeConstants.MAJ_MIN_DATE_CREATION, dateCreation);
    }

    @Override
    public MAJ_TYPE getModification() {
        String modification = getStringProperty(ActiviteNormativeConstants.MAJ_MIN_MODIFICATION);
        MAJ_TYPE result = null;
        if (modification == null) {
            return MAJ_TYPE.UNKNOW;
        }
        try {
            result = MAJ_TYPE.valueOf(modification);
        } catch (IllegalArgumentException ex) {
            result = MAJ_TYPE.UNKNOW;
        }
        return result;
    }

    @Override
    public void setModification(MAJ_TYPE modification) {
        setProperty(ActiviteNormativeConstants.MAJ_MIN_MODIFICATION, modification.toString());
    }

    @Override
    public void copyContentFromMap(Map<String, Serializable> serializableMap) {
        for (Entry<String, Serializable> entry : serializableMap.entrySet()) {
            PropertyUtil.setProperty(
                document,
                ActiviteNormativeConstants.MAJ_MIN_SCHEMA,
                entry.getKey(),
                entry.getValue()
            );
        }
    }

    @Override
    public String getRefMesure() {
        return getStringProperty(ActiviteNormativeConstants.MAJ_MIN_REF_MESURE);
    }

    @Override
    public void setRefMesure(String refMesure) {
        setProperty(ActiviteNormativeConstants.MAJ_MIN_REF_MESURE, refMesure);
    }

    @Override
    public String getId() {
        if (getDocument() == null) {
            return null;
        } else {
            return getDocument().getId();
        }
    }
}
