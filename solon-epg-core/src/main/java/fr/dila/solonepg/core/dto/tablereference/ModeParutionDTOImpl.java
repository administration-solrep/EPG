package fr.dila.solonepg.core.dto.tablereference;

import fr.dila.solonepg.api.constant.ModeParutionConstants;
import fr.dila.solonepg.api.dto.tablereference.ModeParutionDTO;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.st.core.client.AbstractMapDTO;
import java.util.Calendar;
import java.util.Date;

/**
 * DTO for {@link ModeParution}
 *
 *
 */
public class ModeParutionDTOImpl extends AbstractMapDTO implements ModeParutionDTO {
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 5011392243302262328L;

    public ModeParutionDTOImpl() {
        // Default constructor
    }

    public ModeParutionDTOImpl(ModeParution modeParution) {
        setMode(modeParution.getMode());
        Calendar dateDebut = modeParution.getDateDebut();
        Calendar dateFin = modeParution.getDateFin();
        if (dateDebut != null) {
            setDateDebut(dateDebut.getTime());
        }
        if (dateFin != null) {
            setDateFin(dateFin.getTime());
        }
        setId(modeParution.getId());
    }

    @Override
    public String getType() {
        return ModeParution.class.getSimpleName();
    }

    @Override
    public String getDocIdForSelection() {
        return getId();
    }

    @Override
    public String getId() {
        return getString("uuid");
    }

    @Override
    public void setId(String id) {
        put("uuid", id);
    }

    @Override
    public String getMode() {
        return getString(ModeParutionConstants.MODE_PARUTION_MODE_PROPERTY);
    }

    @Override
    public void setMode(String mode) {
        put(ModeParutionConstants.MODE_PARUTION_MODE_PROPERTY, mode);
    }

    @Override
    public Date getDateDebut() {
        return getDate(ModeParutionConstants.MODE_PARUTION_DATE_DEBUT_PROPERTY);
    }

    @Override
    public void setDateDebut(Date dateDebut) {
        put(ModeParutionConstants.MODE_PARUTION_DATE_DEBUT_PROPERTY, dateDebut);
    }

    @Override
    public Date getDateFin() {
        return getDate(ModeParutionConstants.MODE_PARUTION_DATE_FIN_PROPERTY);
    }

    @Override
    public void setDateFin(Date dateFin) {
        put(ModeParutionConstants.MODE_PARUTION_DATE_FIN_PROPERTY, dateFin);
    }
}
