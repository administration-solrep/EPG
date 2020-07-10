/**
 * 
 */
package fr.dila.solonepg.api.cases.typescomplexe;

import java.util.Calendar;
import java.util.List;

import fr.dila.st.api.domain.ComplexeType;

/**
 * type complexe Info Historique Ampliation.
 * 
 * @author antoine Rolin
 *
 */
public interface InfoHistoriqueAmpliation extends ComplexeType {

    Calendar getDateEnvoiAmpliation();
    void setDateEnvoiAmpliation(Calendar dateEnvoiAmpliation);

    List<String> getAmpliationDestinataireMails();
    void setAmpliationDestinataireMails(List<String> ampliationDestinataireMails);
}
