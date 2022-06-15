/**
 *
 */
package fr.dila.solonepg.core.cases.typescomplexe;

import fr.dila.solonepg.api.cases.typescomplexe.InfoHistoriqueAmpliation;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.core.domain.ComplexeTypeImpl;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * InfoHistoriqueAmpliation Implementation.
 *
 * @author antoine Rolin
 *
 */
public class InfoHistoriqueAmpliationImpl extends ComplexeTypeImpl implements InfoHistoriqueAmpliation {
    private static final long serialVersionUID = -4401080506953704835L;

    protected Calendar dateEnvoiAmpliation;

    protected List<String> ampliationDestinataireMails;

    public InfoHistoriqueAmpliationImpl() {
        super();
    }

    public InfoHistoriqueAmpliationImpl(Map<String, Serializable> serializableMap) {
        //        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_AMPLIATION_PROPERTY) instanceof Calendar) {
        //            this.dateEnvoiAmpliation = (Calendar) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_AMPLIATION_PROPERTY);
        //        }
        //        // Hack check (List<?>)
        //        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_AMPLIATION_DESTINATAIRE_MAILS_PROPERTY) instanceof List<?>) {
        //            this.ampliationDestinataireMails = (List<String>) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_AMPLIATION_DESTINATAIRE_MAILS_PROPERTY);
        //        }
        this.setSerializableMap(serializableMap);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setSerializableMap(Map<String, Serializable> serializableMap) {
        Serializable dateEnvoiAmpliation = serializableMap.get(
            TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_AMPLIATION_PROPERTY
        );
        Serializable ampliationDestinataireMails = serializableMap.get(
            TraitementPapierConstants.TRAITEMENT_PAPIER_AMPLIATION_DESTINATAIRE_MAILS_PROPERTY
        );
        //set the map
        super.setSerializableMap(serializableMap);
        //set all the field
        setDateEnvoiAmpliation((Calendar) dateEnvoiAmpliation);
        setAmpliationDestinataireMails((List<String>) ampliationDestinataireMails);
    }

    public Calendar getDateEnvoiAmpliation() {
        return dateEnvoiAmpliation;
    }

    public void setDateEnvoiAmpliation(Calendar dateEnvoiAmpliation) {
        this.dateEnvoiAmpliation = dateEnvoiAmpliation;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_AMPLIATION_PROPERTY, dateEnvoiAmpliation);
    }

    public List<String> getAmpliationDestinataireMails() {
        return ampliationDestinataireMails;
    }

    public void setAmpliationDestinataireMails(List<String> ampliationDestinataireMails) {
        this.ampliationDestinataireMails = ampliationDestinataireMails;
        put(
            TraitementPapierConstants.TRAITEMENT_PAPIER_AMPLIATION_DESTINATAIRE_MAILS_PROPERTY,
            (Serializable) ampliationDestinataireMails
        );
    }
}
