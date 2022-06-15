/**
 *
 */
package fr.dila.solonepg.api.cases.typescomplexe;

import fr.dila.st.api.domain.ComplexeType;
import java.util.Calendar;

/**
 * type complexe Donnees Signataire.
 *
 * @author antoine Rolin
 *
 */
public interface DonneesSignataire extends ComplexeType {
    Calendar getDateEnvoiSignature();
    void setDateEnvoiSignature(Calendar dateEnvoiSignature);

    Calendar getDateRetourSignature();
    void setDateRetourSignature(Calendar dateRetourSignature);

    String getCommentaireSignature();
    void setCommentaireSignature(String commentaireSignature);
}
