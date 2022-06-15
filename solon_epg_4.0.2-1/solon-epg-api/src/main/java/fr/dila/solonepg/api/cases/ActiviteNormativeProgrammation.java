package fr.dila.solonepg.api.cases;

import fr.dila.solonepg.api.cases.typescomplexe.LigneProgrammationHabilitation;
import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Activite Normative Programmation.
 *
 * @author asatre
 *
 */
public interface ActiviteNormativeProgrammation extends STDomainObject, Serializable {
    List<LigneProgrammation> getLigneProgrammation(CoreSession session);

    void emptyLigneProgrammation(CoreSession session, String value);

    String getLockUser();

    void setLockUser(String lockUser);

    Calendar getLockDate();

    void setLockDate(Calendar lockDate);

    List<LigneProgrammation> getTableauSuivi(CoreSession session);

    void setTableauSuivi(List<LigneProgrammation> tableauSuivi);

    Calendar getTableauSuiviPublicationDate();

    void setTableauSuiviPublicationDate(Calendar tableauSuiviPublicationDate);

    String getTableauSuiviPublicationUser();

    void setTableauSuiviPublicationUser(String tableauSuiviPublicationUser);

    List<LigneProgrammationHabilitation> getLigneProgrammationHabilitation();

    void setLigneProgrammationHabilitation(List<LigneProgrammationHabilitation> ligneProgrammationHabilitations);

    List<LigneProgrammationHabilitation> getTableauSuiviHab();

    void setTableauSuiviHab(List<LigneProgrammationHabilitation> tableauSuiviHab);

    Calendar getTableauSuiviHabPublicationDate();

    void setTableauSuiviHabPublicationDate(Calendar tableauSuiviHabPublicationDate);

    String getTableauSuiviHabPublicationUser();

    void setTableauSuiviHabPublicationUser(String tableauSuiviHabPublicationUser);

    String getLockHabUser();

    void setLockHabUser(String lockHabUser);

    Calendar getLockHabDate();

    void setLockHabDate(Calendar lockHabDate);
}
