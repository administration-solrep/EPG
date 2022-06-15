/**
 *
 */
package fr.dila.solonepg.core.cases.typescomplexe;

import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.core.domain.ComplexeTypeImpl;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

/**
 * DestinataireCommunication Implementation.
 *
 * @author antoine Rolin
 *
 */
public class DestinataireCommunicationImpl extends ComplexeTypeImpl implements DestinataireCommunication {
    private static final long serialVersionUID = -2648569632965678122L;

    protected String idDestinataireCommunication;

    protected String nomDestinataireCommunication;

    protected Calendar dateEnvoi;

    protected Calendar dateRetour;

    protected Calendar dateRelance;

    protected String sensAvis;

    protected String objet;

    public DestinataireCommunicationImpl() {
        super();
    }

    public DestinataireCommunicationImpl(Map<String, Serializable> serializableMap) {
        if (
            serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_ID_COMMUNICATION_PROPERTY) instanceof String
        ) {
            this.setIdDestinataireCommunication(
                    (String) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_ID_COMMUNICATION_PROPERTY)
                );
        }
        if (
            serializableMap.get(
                TraitementPapierConstants.TRAITEMENT_PAPIER_NOM_DESTINATAIRE_COMMUNICATION_PROPERTY
            ) instanceof String
        ) {
            this.setNomDestinataireCommunication(
                    (String) serializableMap.get(
                        TraitementPapierConstants.TRAITEMENT_PAPIER_NOM_DESTINATAIRE_COMMUNICATION_PROPERTY
                    )
                );
        }
        if (
            serializableMap.get(
                TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_COMMUNICATION_PROPERTY
            ) instanceof Calendar
        ) {
            this.setDateEnvoi(
                    (Calendar) serializableMap.get(
                        TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_COMMUNICATION_PROPERTY
                    )
                );
        }
        if (
            serializableMap.get(
                TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_COMMUNICATION_PROPERTY
            ) instanceof Calendar
        ) {
            this.setDateRetour(
                    (Calendar) serializableMap.get(
                        TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_COMMUNICATION_PROPERTY
                    )
                );
        }
        if (
            serializableMap.get(
                TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RELANCE_COMMUNICATION_PROPERTY
            ) instanceof Calendar
        ) {
            this.setDateRelance(
                    (Calendar) serializableMap.get(
                        TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RELANCE_COMMUNICATION_PROPERTY
                    )
                );
        }
        if (
            serializableMap.get(
                TraitementPapierConstants.TRAITEMENT_PAPIER_SENS_AVIS_COMMUNICATION_PROPERTY
            ) instanceof String
        ) {
            this.setSensAvis(
                    (String) serializableMap.get(
                        TraitementPapierConstants.TRAITEMENT_PAPIER_SENS_AVIS_COMMUNICATION_PROPERTY
                    )
                );
        }
        if (
            serializableMap.get(
                TraitementPapierConstants.TRAITEMENT_PAPIER_OBJET_COMMUNICATION_PROPERTY
            ) instanceof String
        ) {
            this.setObjet(
                    (String) serializableMap.get(
                        TraitementPapierConstants.TRAITEMENT_PAPIER_OBJET_COMMUNICATION_PROPERTY
                    )
                );
        }
    }

    /* (non-Javadoc)
     * @see fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication#setDestinataireCommunication(java.util.Map)
     */
    @Override
    public void setSerializableMap(Map<String, Serializable> serializableMap) {
        Serializable idDestinataireCommunication = serializableMap.get(
            TraitementPapierConstants.TRAITEMENT_PAPIER_ID_COMMUNICATION_PROPERTY
        );
        Serializable nomDestinataireCommunication = serializableMap.get(
            TraitementPapierConstants.TRAITEMENT_PAPIER_NOM_DESTINATAIRE_COMMUNICATION_PROPERTY
        );
        Serializable dateEnvoi = serializableMap.get(
            TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_COMMUNICATION_PROPERTY
        );
        Serializable dateRetour = serializableMap.get(
            TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_COMMUNICATION_PROPERTY
        );
        Serializable dateRelance = serializableMap.get(
            TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RELANCE_COMMUNICATION_PROPERTY
        );
        Serializable sensAvis = serializableMap.get(
            TraitementPapierConstants.TRAITEMENT_PAPIER_SENS_AVIS_COMMUNICATION_PROPERTY
        );
        Serializable objet = serializableMap.get(
            TraitementPapierConstants.TRAITEMENT_PAPIER_OBJET_COMMUNICATION_PROPERTY
        );
        //set the map
        super.setSerializableMap(serializableMap);
        //set all the field
        setIdDestinataireCommunication((String) idDestinataireCommunication);
        setNomDestinataireCommunication((String) nomDestinataireCommunication);
        setDateEnvoi((Calendar) dateEnvoi);
        setDateRetour((Calendar) dateRetour);
        setDateRelance((Calendar) dateRelance);
        setSensAvis((String) sensAvis);
        setObjet((String) objet);
    }

    @Override
    public String getIdDestinataireCommunication() {
        return idDestinataireCommunication;
    }

    @Override
    public void setIdDestinataireCommunication(String idDestinataireCommunication) {
        this.idDestinataireCommunication = idDestinataireCommunication;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_ID_COMMUNICATION_PROPERTY, idDestinataireCommunication);
    }

    @Override
    public String getNomDestinataireCommunication() {
        return nomDestinataireCommunication;
    }

    @Override
    public void setNomDestinataireCommunication(String nomDestinataireCommunication) {
        this.nomDestinataireCommunication = nomDestinataireCommunication;
        put(
            TraitementPapierConstants.TRAITEMENT_PAPIER_NOM_DESTINATAIRE_COMMUNICATION_PROPERTY,
            nomDestinataireCommunication
        );
    }

    @Override
    public Calendar getDateEnvoi() {
        return dateEnvoi;
    }

    @Override
    public void setDateEnvoi(Calendar dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_COMMUNICATION_PROPERTY, dateEnvoi);
    }

    @Override
    public Calendar getDateRetour() {
        return dateRetour;
    }

    @Override
    public void setDateRetour(Calendar dateRetour) {
        this.dateRetour = dateRetour;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_COMMUNICATION_PROPERTY, dateRetour);
    }

    @Override
    public Calendar getDateRelance() {
        return dateRelance;
    }

    @Override
    public void setDateRelance(Calendar dateRelance) {
        this.dateRelance = dateRelance;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RELANCE_COMMUNICATION_PROPERTY, dateRelance);
    }

    @Override
    public String getSensAvis() {
        return sensAvis;
    }

    @Override
    public void setSensAvis(String sensAvis) {
        this.sensAvis = sensAvis;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_SENS_AVIS_COMMUNICATION_PROPERTY, sensAvis);
    }

    @Override
    public String getObjet() {
        return objet;
    }

    @Override
    public void setObjet(String objet) {
        this.objet = objet;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_OBJET_COMMUNICATION_PROPERTY, objet);
    }
}
