package fr.dila.solonepg.api.cases;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TYPE;
import fr.dila.st.api.domain.STDomainObject;

/**
 * Mise à jour ministérielle
 * 
 * @author jgomez
 *
 */

public interface MajMin extends STDomainObject, Serializable{

    <T> T getAdapter(Class<T> itf);

	String getIdDossier();

	void setIdDossier(String idDossier);

	String getNorDossier();

	void setNorDossier(String norDossier);

	String getType();

	void setType(String type);

	String getRef();

	void setRef(String ref);

	String getTitre();

	void setTitre(String titre);

	String getNumeroArticle();

	void setNumeroArticle(String numeroArticle);

	String getCommentMaj();

	void setCommentMaj(String commentMaj);

	MAJ_TYPE getModification();

	void setModification(MAJ_TYPE modification);
	
	String getRefMesure();

	void setRefMesure(String refMesure);

	void copyContentFromMap(Map<String, Serializable> serializableMap);

	String getId();

	Calendar getDateCreation();

	void setDateCreation(Calendar dateCreation);
    
    
}
