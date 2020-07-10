package fr.dila.solonepg.web.client;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface FeuilleRouteDTO extends Map<String, Serializable> {

    String getId();

    void setId(String id);

    String getEtat();

    void setEtat(String etat);

    String getTitle();

    void setTitle(String title);

    String getNumero();

    void setNumero(String numero);

    String getMinistere();

    void setMinistere(String ministere);

    String getAuteur();

    void setAuteur(String auteur);

    Date getDateModification();

    void setDateModification(Date dateModification);

    String getLockInfo();

    void setLockInfo(String lockInfo);

    Boolean isDeleter();

    void setDeleter(Boolean deleter);

    Boolean isDefault();

    void setDefault(Boolean isDefault);

    void setDocIdForSelection(String docIdForSelection);

}
