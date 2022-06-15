package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implementation de {@link ConseilEtat} .
 *
 * @author asatre
 *
 */
public class ConseilEtatImpl extends STDomainObjectImpl implements ConseilEtat {
    private static final long serialVersionUID = 4376293619619241293L;

    public ConseilEtatImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getSectionCe() {
        return PropertyUtil.getStringProperty(
            document,
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_SECTION_CE_PROPERTY
        );
    }

    @Override
    public void setSectionCe(String sectionCe) {
        setProperty(
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_SECTION_CE_PROPERTY,
            sectionCe
        );
    }

    @Override
    public String getRapporteurCe() {
        return PropertyUtil.getStringProperty(
            document,
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_RAPPORTEUR_CE_PROPERTY
        );
    }

    @Override
    public void setRapporteurCe(String rapporteurCe) {
        setProperty(
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_RAPPORTEUR_CE_PROPERTY,
            rapporteurCe
        );
    }

    @Override
    public Calendar getDateTransmissionSectionCe() {
        return PropertyUtil.getCalendarProperty(
            document,
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_PROPERTY
        );
    }

    @Override
    public void setDateTransmissionSectionCe(Calendar dateTransmissionSectionCe) {
        setProperty(
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_PROPERTY,
            dateTransmissionSectionCe
        );
    }

    @Override
    public Calendar getDateSectionCe() {
        return PropertyUtil.getCalendarProperty(
            document,
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_DATE_SECTION_CE_PROPERTY
        );
    }

    @Override
    public void setDateSectionCe(Calendar dateSectionCe) {
        setProperty(
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_DATE_SECTION_CE_PROPERTY,
            dateSectionCe
        );
    }

    @Override
    public Calendar getDateAgCe() {
        return PropertyUtil.getCalendarProperty(
            document,
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_DATE_AG_CE_PROPERTY
        );
    }

    @Override
    public void setDateAgCe(Calendar dateAgCe) {
        setProperty(
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_DATE_AG_CE_PROPERTY,
            dateAgCe
        );
    }

    @Override
    public String getNumeroISA() {
        return PropertyUtil.getStringProperty(
            document,
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_NUMERO_ISA_PROPERTY
        );
    }

    @Override
    public void setNumeroISA(String numeroISA) {
        setProperty(
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_NUMERO_ISA_PROPERTY,
            numeroISA
        );
    }

    @Override
    public Calendar getDateSaisineCE() {
        return PropertyUtil.getCalendarProperty(
            document,
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_DATE_SAISINE_CE_PROPERTY
        );
    }

    @Override
    public void setDateSaisineCE(Calendar dateSaisineCE) {
        setProperty(
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_DATE_SAISINE_CE_PROPERTY,
            dateSaisineCE
        );
    }

    @Override
    public Calendar getDateSortieCE() {
        return PropertyUtil.getCalendarProperty(
            document,
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_DATE_SORTIE_CE_PROPERTY
        );
    }

    @Override
    public void setDateSortieCE(Calendar dateSortieCE) {
        setProperty(
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_DATE_SORTIE_CE_PROPERTY,
            dateSortieCE
        );
    }

    @Override
    public String getPriorite() {
        return PropertyUtil.getStringProperty(
            document,
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_PROPERTY
        );
    }

    @Override
    public void setPriorite(String priorite) {
        setProperty(
            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
            ConseilEtatConstants.CONSEIL_ETAT_PRIORITE_PROPERTY,
            priorite
        );
    }
}
