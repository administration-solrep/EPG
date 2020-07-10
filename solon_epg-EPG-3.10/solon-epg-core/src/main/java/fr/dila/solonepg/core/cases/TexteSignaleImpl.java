package fr.dila.solonepg.core.cases;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.TexteSignale;
import fr.dila.solonepg.api.constant.TexteSignaleConstants;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.st.core.domain.STDomainObjectImpl;

/**
 * Implementation de {@link TexteSignale}
 * 
 * @author asatre
 * 
 */
public class TexteSignaleImpl extends STDomainObjectImpl implements fr.dila.solonepg.api.cases.TexteSignale {

    private static final long serialVersionUID = 5129813344484661841L;

    public TexteSignaleImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public Calendar getDateDemandePublicationTS() {
        return PropertyUtil.getCalendarProperty(document, TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA,
                TexteSignaleConstants.DATE_DEMANDE_PUBLICATION_TS);
    }

    @Override
    public void setDateDemandePublicationTS(Calendar dateDemandePublicationTS) {
        PropertyUtil.setProperty(document, TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA, TexteSignaleConstants.DATE_DEMANDE_PUBLICATION_TS,
                dateDemandePublicationTS);
    }

    @Override
    public String getVecteurPublicationTS() {
        return PropertyUtil.getStringProperty(document, TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA, TexteSignaleConstants.VECTEUR_PUBLICATION_TS);
    }

    @Override
    public void setVecteurPublicationTS(String vecteurPublicationTS) {
        PropertyUtil.setProperty(document, TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA, TexteSignaleConstants.VECTEUR_PUBLICATION_TS,
                vecteurPublicationTS);
    }

    @Override
    public String getObservationTS() {
        return PropertyUtil.getStringProperty(document, TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA, TexteSignaleConstants.OBSERVATION_TS);
    }

    @Override
    public void setObservationTS(String observationTS) {
        PropertyUtil.setProperty(document, TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA, TexteSignaleConstants.OBSERVATION_TS, observationTS);
    }

    @Override
    public String getIdDossier() {
        return PropertyUtil.getStringProperty(document, TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA, TexteSignaleConstants.ID_DOSSIER_TS);
    }

    @Override
    public void setIdDossier(String idDossier) {
        PropertyUtil.setProperty(document, TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA, TexteSignaleConstants.ID_DOSSIER_TS, idDossier);
    }

    @Override
    public String getType() {
        return PropertyUtil.getStringProperty(document, TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA, TexteSignaleConstants.TYPE);
    }

    @Override
    public void setType(String type) {
        PropertyUtil.setProperty(document, TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA, TexteSignaleConstants.TYPE, type);
    }

}
