package fr.dila.solonepg.api.dto;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

public interface VecteurPublicationDTO extends Map<String, Serializable>{
    
	String getId();
	void setId(String id);
	
    String getIntitule();
    void setIntitule(String intitule);
    
    Date getDateDebut();
    void setDateDebut(Date debut);

    Date getDateFin();
    void setDateFin(Date fin);
    	
	public static Comparator<VecteurPublicationDTO> COMPARE = new Comparator<VecteurPublicationDTO>() {
		public int compare(VecteurPublicationDTO one,VecteurPublicationDTO two) {
			return one.getIntitule().compareTo(two.getIntitule());
			
		}
	};
    
}
