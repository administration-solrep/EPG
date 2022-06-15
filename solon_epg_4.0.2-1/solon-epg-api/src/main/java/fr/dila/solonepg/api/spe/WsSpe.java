package fr.dila.solonepg.api.spe;

import java.io.Serializable;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface WsSpe extends Serializable {
    /**
     * Retourne le document model
     *
     * @return le document
     */
    DocumentModel getDocument();

    /**
     * Setter pour le document model.
     *
     * @param doc
     */
    void setDocument(DocumentModel doc);

    String getPosteId();

    void setPosteId(String posteId);

    String getWebservice();

    void setWebservice(String webservice);

    int getNbEssais();

    void setNbEssais(int nbEssais);

    String getIdDossier();

    void setIdDossier(String idDossier);

    String getTypePublication();

    void setTypePublication(String typePublication);
}
