package fr.dila.solonepg.core.dto.tablereference;

import java.util.Calendar;
import java.util.Date;

import fr.dila.solonepg.api.constant.SolonEpgTableReferenceConstants;
import fr.dila.solonepg.api.dto.tablereference.ModeParutionDTO;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.st.core.client.AbstractMapDTO;

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
		return getString(SolonEpgTableReferenceConstants.TABLE_REFERENCE_MODE_PARUTION_PROPERTY);
	}

	@Override
	public void setMode(String mode) {
		put(SolonEpgTableReferenceConstants.TABLE_REFERENCE_MODE_PARUTION_PROPERTY, mode);
	}

	@Override
	public Date getDateDebut() {
		return getDate(SolonEpgTableReferenceConstants.TABLE_REFERENCE_DEBUT_MODE_PARUTION_PROPERTY);
	}

	@Override
	public void setDateDebut(Date dateDebut) {
		put(SolonEpgTableReferenceConstants.TABLE_REFERENCE_DEBUT_MODE_PARUTION_PROPERTY, dateDebut);
	}

	@Override
	public Date getDateFin() {
		return getDate(SolonEpgTableReferenceConstants.TABLE_REFERENCE_FIN_MODE_PARUTION_PROPERTY);
	}

	@Override
	public void setDateFin(Date dateFin) {
		put(SolonEpgTableReferenceConstants.TABLE_REFERENCE_FIN_MODE_PARUTION_PROPERTY, dateFin);
	}	

}
