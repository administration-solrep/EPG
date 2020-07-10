/**
 * 
 */
package fr.dila.solonepg.core.cases.typescomplexe;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.core.domain.ComplexeTypeImpl;

/**
 * DestinataireCommunication Implementation.
 * 
 * @author antoine Rolin
 *
 */
public class DestinataireCommunicationImpl extends ComplexeTypeImpl implements DestinataireCommunication {

    private static final long serialVersionUID = -2648569632965678122L;

    protected String nomDestinataireCommunication;
    
    protected Calendar dateEnvoi;
    
    protected Calendar dateRetour;
    
    protected Calendar dateRelance;
    
    protected String sensAvis;
    
    protected String objet;

    public DestinataireCommunicationImpl(){
        super();
    }
    
    public DestinataireCommunicationImpl(Map<String, Serializable> serializableMap) {
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_NOM_DESTINATAIRE_COMMUNICATION_PROPERTY) instanceof String) {
            this.nomDestinataireCommunication = (String) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_NOM_DESTINATAIRE_COMMUNICATION_PROPERTY);
        }
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_COMMUNICATION_PROPERTY) instanceof Calendar) {
            this.dateEnvoi = (Calendar) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_COMMUNICATION_PROPERTY);
        }
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_COMMUNICATION_PROPERTY) instanceof Calendar) {
            this.dateRetour = (Calendar) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_COMMUNICATION_PROPERTY);
        }
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RELANCE_COMMUNICATION_PROPERTY) instanceof Calendar) {
            this.dateRelance = (Calendar) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RELANCE_COMMUNICATION_PROPERTY);
        }
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_SENS_AVIS_COMMUNICATION_PROPERTY) instanceof String) {
            this.sensAvis = (String) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_SENS_AVIS_COMMUNICATION_PROPERTY);
        }
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_OBJET_COMMUNICATION_PROPERTY) instanceof String) {
            this.objet = (String) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_OBJET_COMMUNICATION_PROPERTY);
        }
    }
   

    /* (non-Javadoc)
     * @see fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication#setDestinataireCommunication(java.util.Map)
     */
    @Override
    public void setSerializableMap(Map<String, Serializable> serializableMap) {
        Serializable nomDestinataireCommunication = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_NOM_DESTINATAIRE_COMMUNICATION_PROPERTY);
        Serializable dateEnvoi = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_COMMUNICATION_PROPERTY);
        Serializable dateRetour = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_COMMUNICATION_PROPERTY);
        Serializable dateRelance = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RELANCE_COMMUNICATION_PROPERTY);
        Serializable sensAvis = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_SENS_AVIS_COMMUNICATION_PROPERTY);
        Serializable objet = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_OBJET_COMMUNICATION_PROPERTY);
        //set the map
        super.setSerializableMap(serializableMap);
        //set all the field
        setNomDestinataireCommunication((String)nomDestinataireCommunication);
        setDateEnvoi((Calendar)dateEnvoi);
        setDateRetour((Calendar)dateRetour);
        setDateRelance((Calendar)dateRelance);
        setSensAvis((String)sensAvis);
        setObjet((String)objet);
    }
    
    public String getNomDestinataireCommunication() {
        return nomDestinataireCommunication;
    }

    public void setNomDestinataireCommunication(String nomDestinataireCommunication) {
        this.nomDestinataireCommunication = nomDestinataireCommunication;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_NOM_DESTINATAIRE_COMMUNICATION_PROPERTY,nomDestinataireCommunication);
    }

    public Calendar getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Calendar dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_COMMUNICATION_PROPERTY,dateEnvoi);
    }

    public Calendar getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Calendar dateRetour) {
        this.dateRetour = dateRetour;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_COMMUNICATION_PROPERTY,dateRetour);
    }

    public Calendar getDateRelance() {
        return dateRelance;
    }

    public void setDateRelance(Calendar dateRelance) {
        this.dateRelance = dateRelance;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RELANCE_COMMUNICATION_PROPERTY,dateRelance);
    }

    public String getSensAvis() {
        return sensAvis;
    }

    public void setSensAvis(String sensAvis) {
        this.sensAvis = sensAvis;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_SENS_AVIS_COMMUNICATION_PROPERTY,sensAvis);
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_OBJET_COMMUNICATION_PROPERTY,objet);
    }
}
