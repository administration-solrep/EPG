package fr.dila.solonepg.core.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface DossierArchivageStatDTO extends Map<String, Serializable> {

	String getDocIdForSelection();

	void setDocIdForSelection(String id);

	String getNor();

	void setNor(String nor);

	String getTitreActe();

	void setTitreActe(String titreActe);

	String getStatutPeriode();

	void setStatutPeriode(String statutPeriode);

	Date getDateModif();

	void setDateModif(Date dateModif);

	String getStatut();

	void setStatut(String statut);

	String getErreur();

	void setErreur(String erreur);
}
