/**
 *
 */
package fr.dila.solonepg.api.cases.typescomplexe;

import fr.dila.st.api.domain.ComplexeType;
import java.util.Calendar;

/**
 * type complexe Info Epreuve.
 *
 * @author antoine Rolin
 *
 */
public interface InfoEpreuve extends ComplexeType {
    Calendar getEpreuveDemandeLe();
    void setEpreuveDemandeLe(Calendar epreuveDemandeLe);

    Calendar getEpreuvePourLe();
    void setEpreuvePourLe(Calendar epreuvePourLe);

    String getNumeroListe();
    void setNumeroListe(String numeroListe);

    Calendar getEnvoiRelectureLe();
    void setEnvoiRelectureLe(Calendar envoiRelectureLe);

    String getDestinataireEntete();
    void setDestinataireEntete(String DestinataireEntete);

    String getNomsSignataires();
    void setNomsSignataires(String nomsSignataires);
}
