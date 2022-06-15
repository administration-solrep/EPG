package fr.dila.solonepg.core.cases.typescomplexe;

import fr.dila.solonepg.api.cases.typescomplexe.DossierTransposition;
import fr.dila.solonepg.api.cases.typescomplexe.DossierTranspositionImmutable;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.st.core.domain.ComplexeTypeImpl;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DossierTranspositionImpl extends ComplexeTypeImpl implements DossierTransposition {
    private static final long serialVersionUID = 1L;

    public DossierTranspositionImpl() {
        this(new HashMap<>());
    }

    public DossierTranspositionImpl(Map<String, Serializable> serializableMap) {
        setSerializableMap(serializableMap);
    }

    public DossierTranspositionImpl(DossierTranspositionImmutable immutable) {
        this();
        setCommentaire(immutable.getCommentaire());
        setNumeroArticles(immutable.getNumeroArticles());
        setRef(immutable.getRef());
        setRefMesure(immutable.getRefMesure());
        setTitre(immutable.getTitre());
    }

    @Override
    public String getRef() {
        return (String) getSerializableMap().get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY);
    }

    @Override
    public void setRef(String ref) {
        getSerializableMap().put(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY, ref);
    }

    @Override
    public String getTitre() {
        return (String) getSerializableMap().get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY);
    }

    @Override
    public void setTitre(String titre) {
        getSerializableMap().put(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY, titre);
    }

    @Override
    public String getNumeroArticles() {
        return (String) getSerializableMap().get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_NUMERO_PROPERTY);
    }

    @Override
    public void setNumeroArticles(String numeroArticles) {
        getSerializableMap().put(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_NUMERO_PROPERTY, numeroArticles);
    }

    @Override
    public String getRefMesure() {
        return (String) getSerializableMap().get(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY);
    }

    @Override
    public void setRefMesure(String refMesure) {
        getSerializableMap().put(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY, refMesure);
    }

    @Override
    public String getCommentaire() {
        return (String) getSerializableMap().get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY);
    }

    @Override
    public void setCommentaire(String commentaire) {
        getSerializableMap().put(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY, commentaire);
    }
}
