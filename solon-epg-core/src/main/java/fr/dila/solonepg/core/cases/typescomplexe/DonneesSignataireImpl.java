/**
 *
 */
package fr.dila.solonepg.core.cases.typescomplexe;

import com.google.common.base.Objects;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.core.domain.ComplexeTypeImpl;
import fr.dila.st.core.util.SolonDateConverter;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

/**
 * DonneesSignataire Implementation.
 *
 * @author antoine Rolin
 *
 */
public class DonneesSignataireImpl extends ComplexeTypeImpl implements DonneesSignataire {
    private static final long serialVersionUID = 1L;

    protected Calendar dateEnvoiSignature;

    protected Calendar dateRetourSignature;

    protected String commentaireSignature;

    public DonneesSignataireImpl() {
        super();
    }

    public DonneesSignataireImpl(Map<String, Serializable> serializableMap) {
        if (
            serializableMap.get(
                TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_SIGNATURE_PROPERTY
            ) instanceof Calendar
        ) {
            this.dateEnvoiSignature =
                (Calendar) serializableMap.get(
                    TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_SIGNATURE_PROPERTY
                );
        }
        if (
            serializableMap.get(
                TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_SIGNATURE_PROPERTY
            ) instanceof Calendar
        ) {
            this.dateRetourSignature =
                (Calendar) serializableMap.get(
                    TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_SIGNATURE_PROPERTY
                );
        }
        if (
            serializableMap.get(
                TraitementPapierConstants.TRAITEMENT_PAPIER_COMMENTAIRE_SIGNATURE_PROPERTY
            ) instanceof String
        ) {
            this.commentaireSignature =
                (String) serializableMap.get(
                    TraitementPapierConstants.TRAITEMENT_PAPIER_COMMENTAIRE_SIGNATURE_PROPERTY
                );
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication#setDestinataireCommunication(java.util.Map)
     */
    @Override
    public void setSerializableMap(final Map<String, Serializable> serializableMap) {
        Serializable dateEnvoiSignature = serializableMap.get(
            TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_SIGNATURE_PROPERTY
        );
        Serializable dateRetourSignature = serializableMap.get(
            TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_SIGNATURE_PROPERTY
        );
        Serializable commentaireSignature = serializableMap.get(
            TraitementPapierConstants.TRAITEMENT_PAPIER_COMMENTAIRE_SIGNATURE_PROPERTY
        );
        // set the map
        super.setSerializableMap(serializableMap);
        // set all the field
        setDateEnvoiSignature((Calendar) dateEnvoiSignature);
        setDateRetourSignature((Calendar) dateRetourSignature);
        setCommentaireSignature((String) commentaireSignature);
    }

    public Calendar getDateEnvoiSignature() {
        return dateEnvoiSignature;
    }

    public void setDateEnvoiSignature(Calendar dateEnvoiSignature) {
        this.dateEnvoiSignature = dateEnvoiSignature;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_SIGNATURE_PROPERTY, dateEnvoiSignature);
    }

    public Calendar getDateRetourSignature() {
        return dateRetourSignature;
    }

    public void setDateRetourSignature(Calendar dateRetourSignature) {
        this.dateRetourSignature = dateRetourSignature;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_SIGNATURE_PROPERTY, dateRetourSignature);
    }

    public String getCommentaireSignature() {
        return commentaireSignature;
    }

    public void setCommentaireSignature(String commentaireSignature) {
        this.commentaireSignature = commentaireSignature;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_COMMENTAIRE_SIGNATURE_PROPERTY, commentaireSignature);
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("{");
        builder.append("dateEnvoiSignature = ");
        if (dateEnvoiSignature != null) {
            builder.append(SolonDateConverter.DATE_SLASH.format(dateEnvoiSignature));
        } else {
            builder.append(" ");
        }
        builder.append(", ");
        builder.append("dateRetourSignature = ");
        if (dateRetourSignature != null) {
            builder.append(SolonDateConverter.DATE_SLASH.format(dateRetourSignature));
        } else {
            builder.append(" ");
        }
        builder.append(", ");
        builder.append("commentaireSignature = " + commentaireSignature);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultat = false;
        if (obj != null && obj instanceof DonneesSignataireImpl) {
            DonneesSignataireImpl donneesSignataire = (DonneesSignataireImpl) obj;
            if (
                (
                    (this.dateEnvoiSignature == null && donneesSignataire.dateEnvoiSignature == null) ||
                    (
                        this.dateEnvoiSignature != null &&
                        this.dateEnvoiSignature.equals(donneesSignataire.dateEnvoiSignature)
                    )
                ) &&
                (
                    (this.dateRetourSignature == null && donneesSignataire.dateRetourSignature == null) ||
                    (
                        this.dateRetourSignature != null &&
                        this.dateRetourSignature.equals(donneesSignataire.dateRetourSignature)
                    )
                ) &&
                (
                    (this.commentaireSignature == null && donneesSignataire.commentaireSignature == null) ||
                    (
                        this.commentaireSignature != null &&
                        this.commentaireSignature.equals(donneesSignataire.commentaireSignature)
                    )
                )
            ) {
                resultat = true;
            }
        }
        return resultat;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCommentaireSignature(), getDateEnvoiSignature(), getDateRetourSignature());
    }
}
