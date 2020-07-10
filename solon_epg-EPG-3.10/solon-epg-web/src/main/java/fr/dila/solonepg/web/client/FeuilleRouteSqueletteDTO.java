package fr.dila.solonepg.web.client;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface FeuilleRouteSqueletteDTO extends Map<String, Serializable> {

	String getId();

	void setId(String id);

	String getTitle();

	void setTitle(String title);

	String getAuteur();

	void setAuteur(String auteur);

	Date getDateModification();

	void setDateModification(Date dateModification);

	void setDocIdForSelection(String docIdForSelection);

	String getTypeActe();

	void setTypeActe(String typeActe);

	String getEtat();

	void setEtat(String etat);

	String getLockInfo();

	void setLockInfo(String lockInfo);

	Boolean isDeleter();

	void setDeleter(Boolean deleter);
}