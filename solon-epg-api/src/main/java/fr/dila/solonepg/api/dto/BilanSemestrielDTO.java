package fr.dila.solonepg.api.dto;

import fr.dila.solonepg.api.enums.BilanSemestrielType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface BilanSemestrielDTO extends Serializable {
    List<TexteBilanSemestrielDTO> getListeTextes();

    BilanSemestrielType getType();

    void setType(BilanSemestrielType type);

    Date getDateFinIntervalleTextesPublies();

    void setDateFinIntervalleTextesPublies(Date dateFinIntervalleTextesPublies);

    Date getDateFinIntervalleMesures();

    void setDateFinIntervalleMesures(Date dateFinIntervalleMesures);

    Date getDateDebutIntervalleTextesPublies();

    void setDateDebutIntervalleTextesPublies(Date dateDebutIntervalleTextesPublies);

    Date getDateDebutIntervalleMesures();

    void setDateDebutIntervalleMesures(Date dateDebutIntervalleMesures);

    Date getDateBilan();

    void setDateBilan(Date dateBilan);
}
