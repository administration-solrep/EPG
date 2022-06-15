package fr.dila.solonmgpp.core.domain;

import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.io.Serializable;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

public class RepresentantOEPImpl implements RepresentantOEP, Serializable {
    private static final long serialVersionUID = 6717935433174615506L;

    public static final String DOC_TYPE = "RepresentantOEP";
    public static final String SCHEMA = "representant_oep";
    public static final String REPRESENTANT_PREFIX = "roep";

    public static final String ID_FICHE_ROEP = "idfroep";
    public static final String TYPE = "type";

    public static final String REPRESENTANT = "representant";
    public static final String FONCTION = "fonction";

    public static final String DATE_DEBUT = "dateDebut";
    public static final String DATE_FIN = "dateFin";

    public static final String NUMERO_MANDAT = "numeroMandat";
    public static final String AUTORITE_DESIGNATION = "autoriteDesignation";
    public static final String COMMISSION_SAISIE = "commissionSaisie";

    private final DocumentModel document;

    public RepresentantOEPImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

    @Override
    public String getIdFicheRepresentationOEP() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ID_FICHE_ROEP);
    }

    @Override
    public void setIdFicheRepresentationOEP(String idFicheRepresentationOEP) {
        PropertyUtil.setProperty(document, SCHEMA, ID_FICHE_ROEP, idFicheRepresentationOEP);
    }

    @Override
    public String getType() {
        return PropertyUtil.getStringProperty(document, SCHEMA, TYPE);
    }

    @Override
    public void setType(String type) {
        PropertyUtil.setProperty(document, SCHEMA, TYPE, type);
    }

    @Override
    public String getRepresentant() {
        return PropertyUtil.getStringProperty(document, SCHEMA, REPRESENTANT);
    }

    @Override
    public void setRepresentant(String representant) {
        PropertyUtil.setProperty(document, SCHEMA, REPRESENTANT, representant);
    }

    @Override
    public String getFonction() {
        return PropertyUtil.getStringProperty(document, SCHEMA, FONCTION);
    }

    @Override
    public void setFonction(String fonction) {
        PropertyUtil.setProperty(document, SCHEMA, FONCTION, fonction);
    }

    @Override
    public Calendar getDateDebut() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_DEBUT);
    }

    @Override
    public void setDateDebut(Calendar dateDebut) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_DEBUT, dateDebut);
    }

    @Override
    public Calendar getDateFin() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_FIN);
    }

    @Override
    public void setDateFin(Calendar dateFin) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_FIN, dateFin);
    }

    @Override
    public Long getNumeroMandat() {
        return PropertyUtil.getLongProperty(document, SCHEMA, NUMERO_MANDAT);
    }

    @Override
    public void setNumeroMandat(Long numeroMandat) {
        PropertyUtil.setProperty(document, SCHEMA, NUMERO_MANDAT, numeroMandat);
    }

    @Override
    public String getAutoriteDesignation() {
        return PropertyUtil.getStringProperty(document, SCHEMA, AUTORITE_DESIGNATION);
    }

    @Override
    public void setAutoriteDesignation(String autoriteDesignation) {
        PropertyUtil.setProperty(document, SCHEMA, AUTORITE_DESIGNATION, autoriteDesignation);
    }

    @Override
    public String getCommissionSaisie() {
        return PropertyUtil.getStringProperty(document, SCHEMA, COMMISSION_SAISIE);
    }

    @Override
    public void setCommissionSaisie(String commissionSaisie) {
        PropertyUtil.setProperty(document, SCHEMA, COMMISSION_SAISIE, commissionSaisie);
    }
}
