/**
 *
 */
package fr.dila.solonepg.core.cases.typescomplexe;

import com.google.common.base.Objects;
import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.core.domain.ComplexeTypeImpl;
import fr.dila.st.core.util.SolonDateConverter;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

/**
 * InfoEpreuve implementation.
 *
 * @author antoine Rolin
 *
 */
public class InfoEpreuveImpl extends ComplexeTypeImpl implements InfoEpreuve {
    private static final long serialVersionUID = 8762404809990822155L;

    protected Calendar epreuveDemandeLe;

    protected Calendar epreuvePourLe;

    protected String numeroListe;

    protected Calendar envoiRelectureLe;

    protected String destinataireEntete;

    protected String nomsSignataires;

    public InfoEpreuveImpl() {
        super();
    }

    public InfoEpreuveImpl(Map<String, Serializable> serializableMap) {
        setSerializableMap(serializableMap);
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication#setDestinataireCommunication(java.util.Map)
     */
    @Override
    public void setSerializableMap(Map<String, Serializable> serializableMap) {
        // set the map
        super.setSerializableMap(serializableMap);
        // set all the field
        setEpreuveDemandeLe(
            (Calendar) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_DEMANDE_LE_PROPERTY)
        );
        setEpreuvePourLe(
            (Calendar) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_POUR_LE_PROPERTY)
        );
        setNumeroListe(
            (String) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_NUMERO_LISTE_PROPERTY)
        );
        setEnvoiRelectureLe(
            (Calendar) serializableMap.get(
                TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_ENVOI_RELECTURE_LE_PROPERTY
            )
        );
        setDestinataireEntete(
            (String) serializableMap.get(
                TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_DESTINATAIRE_ENTETE_PROPERTY
            )
        );
        setNomsSignataires(
            (String) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_NOMS_SIGNATAIRES_PROPERTY)
        );
    }

    @Override
    public Calendar getEpreuveDemandeLe() {
        return epreuveDemandeLe;
    }

    @Override
    public void setEpreuveDemandeLe(Calendar epreuveDemandeLe) {
        this.epreuveDemandeLe = epreuveDemandeLe;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_DEMANDE_LE_PROPERTY, epreuveDemandeLe);
    }

    @Override
    public Calendar getEpreuvePourLe() {
        return epreuvePourLe;
    }

    @Override
    public void setEpreuvePourLe(Calendar epreuvePourLe) {
        this.epreuvePourLe = epreuvePourLe;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_POUR_LE_PROPERTY, epreuvePourLe);
    }

    @Override
    public String getNumeroListe() {
        return numeroListe;
    }

    @Override
    public void setNumeroListe(String numeroListe) {
        this.numeroListe = numeroListe;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_NUMERO_LISTE_PROPERTY, numeroListe);
    }

    @Override
    public Calendar getEnvoiRelectureLe() {
        return envoiRelectureLe;
    }

    @Override
    public void setEnvoiRelectureLe(Calendar envoiRelectureLe) {
        this.envoiRelectureLe = envoiRelectureLe;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_ENVOI_RELECTURE_LE_PROPERTY, envoiRelectureLe);
    }

    @Override
    public String getDestinataireEntete() {
        return destinataireEntete;
    }

    @Override
    public void setDestinataireEntete(String destinataireEntete) {
        this.destinataireEntete = destinataireEntete;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_DESTINATAIRE_ENTETE_PROPERTY, destinataireEntete);
    }

    @Override
    public String getNomsSignataires() {
        return nomsSignataires;
    }

    @Override
    public void setNomsSignataires(String nomsSignataires) {
        this.nomsSignataires = nomsSignataires;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_NOMS_SIGNATAIRES_PROPERTY, nomsSignataires);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("{");
        builder.append("epreuveDemandeLe = ");
        if (epreuveDemandeLe != null) {
            builder.append(SolonDateConverter.DATE_SLASH.format(epreuveDemandeLe));
        } else {
            builder.append(" ");
        }
        builder.append(", ");
        builder.append("epreuvePourLe = ");
        if (epreuvePourLe != null) {
            builder.append(SolonDateConverter.DATE_SLASH.format(epreuvePourLe));
        } else {
            builder.append(" ");
        }
        builder.append(", ");
        builder.append("numeroListe = " + numeroListe);
        builder.append(", ");
        builder.append("envoiRelectureLe = ");
        if (envoiRelectureLe != null) {
            builder.append(SolonDateConverter.DATE_SLASH.format(envoiRelectureLe));
        } else {
            builder.append(" ");
        }
        builder.append(", ");
        builder.append("destinataireEntete = " + destinataireEntete);
        builder.append(", ");
        builder.append("nomsSignataires = " + nomsSignataires);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        boolean resultat = false;
        if (obj != null && obj instanceof InfoEpreuveImpl) {
            InfoEpreuveImpl infoEpreuve = (InfoEpreuveImpl) obj;
            if (
                (
                    (this.destinataireEntete == null && infoEpreuve.destinataireEntete == null) ||
                    (this.destinataireEntete != null && this.destinataireEntete.equals(infoEpreuve.destinataireEntete))
                ) &&
                (
                    (this.envoiRelectureLe == null && infoEpreuve.envoiRelectureLe == null) ||
                    (this.envoiRelectureLe != null && this.envoiRelectureLe.equals(infoEpreuve.envoiRelectureLe))
                ) &&
                (
                    (this.epreuveDemandeLe == null && infoEpreuve.epreuveDemandeLe == null) ||
                    (this.epreuveDemandeLe != null && this.epreuveDemandeLe.equals(infoEpreuve.epreuveDemandeLe))
                ) &&
                (
                    (this.numeroListe == null && infoEpreuve.numeroListe == null) ||
                    (this.numeroListe != null && this.numeroListe.equals(infoEpreuve.numeroListe))
                ) &&
                (
                    (this.nomsSignataires == null && infoEpreuve.nomsSignataires == null) ||
                    (this.nomsSignataires != null && this.nomsSignataires.equals(infoEpreuve.nomsSignataires))
                ) &&
                (
                    (this.epreuvePourLe == null && infoEpreuve.epreuvePourLe == null) ||
                    (this.epreuvePourLe != null && this.epreuvePourLe.equals(infoEpreuve.epreuvePourLe))
                )
            ) {
                resultat = true;
            }
        }
        return resultat;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
            getDestinataireEntete(),
            getEnvoiRelectureLe(),
            getEpreuveDemandeLe(),
            getEpreuvePourLe(),
            getNomsSignataires(),
            getNumeroListe()
        );
    }
}
