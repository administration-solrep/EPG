/**
 * 
 */
package fr.dila.solonepg.api.cases.typescomplexe;

import java.util.Calendar;

import fr.dila.st.api.domain.ComplexeType;

/**
 * type complexe Destinataire Communication.
 * 
 * @author antoine Rolin
 *
 */
public interface DestinataireCommunication extends ComplexeType {

    /**
     * get Nom Destinataire Communication
     * 
     * @return String
     */
    String getNomDestinataireCommunication();
    void setNomDestinataireCommunication(String nomDestinataireCommunication);

    /**
     * get Date Envoi
     * 
     * @return Calendar
     */
    Calendar getDateEnvoi();
    void setDateEnvoi(Calendar dateEnvoi);

    /**
     * get Date Retour
     * 
     * @return Calendar
     */
    Calendar getDateRetour();
    void setDateRetour(Calendar dateRetour);

    /**
     * get Date Relance
     * 
     * @return Calendar
     */
    Calendar getDateRelance();
    void setDateRelance(Calendar dateRelance);

    /**
     * get sens Avis
     * 
     * @return String
     */
    String getSensAvis();
    void setSensAvis(String sensAvis);

    /**
     * get Objet
     * 
     * @return String
     */
    String getObjet();
    void setObjet(String objet);
}
