package fr.dila.solonepg.core.cases.typescomplexe;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

import fr.dila.solonepg.api.cases.typescomplexe.InfoNumeroListe;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.core.domain.ComplexeTypeImpl;

/**
 * InfoNumeroListe Implementation.
 * 
 * @author antoine Rolin
 *
 */
public class InfoNumeroListeImpl extends ComplexeTypeImpl implements InfoNumeroListe {

    private static final long serialVersionUID = 7088611546775926459L;

    protected Calendar dateGenerationListe;
    
    protected String numeroListe;
    
    public InfoNumeroListeImpl(){
        super();
    }
    
    public InfoNumeroListeImpl(Map<String, Serializable> serializableMap) {
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_SIGNATURE_PROPERTY) instanceof Calendar) {
            this.dateGenerationListe = (Calendar) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_SIGNATURE_PROPERTY);
        }
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_NUMERO_LISTE_SIGNATURE_PROPERTY) instanceof String) {
            this.numeroListe = (String) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_NUMERO_LISTE_SIGNATURE_PROPERTY);
        }
    }

    /* (non-Javadoc)
     * @see fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication#setDestinataireCommunication(java.util.Map)
     */
    @Override
    public void setSerializableMap(Map<String, Serializable> serializableMap) {
        Serializable dateGenerationListe = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_SIGNATURE_PROPERTY);
        Serializable numeroListe = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_NUMERO_LISTE_SIGNATURE_PROPERTY);
        //set the map
        super.setSerializableMap(serializableMap);
        //set all the field
        setDateGenerationListe((Calendar)dateGenerationListe);
        setNumeroListe((String)numeroListe);
    }
    
    public Calendar getDateGenerationListe() {
        return dateGenerationListe;
    }
    
    public void setDateGenerationListe(Calendar dateGenerationListe) {
        this.dateGenerationListe = dateGenerationListe;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_SIGNATURE_PROPERTY,dateGenerationListe);
    }
    
    public String getNumeroListe() {
        return numeroListe;
    }
    
    public void setNumeroListe(String numeroListe) {
        this.numeroListe = numeroListe;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_NUMERO_LISTE_SIGNATURE_PROPERTY,numeroListe);
    }
    
}
