/**
 * 
 */
package fr.dila.solonepg.core.cases.typescomplexe;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

import com.google.common.base.Objects;

import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.core.domain.ComplexeTypeImpl;
import fr.dila.st.core.util.DateUtil;

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
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_DEMANDE_LE_PROPERTY) instanceof Calendar) {
            this.epreuveDemandeLe = (Calendar) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_DEMANDE_LE_PROPERTY);
        }
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_POUR_LE_PROPERTY) instanceof Calendar) {
            this.epreuvePourLe = (Calendar) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_POUR_LE_PROPERTY);
        }
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_NUMERO_LISTE_PROPERTY) instanceof String) {
            this.numeroListe = (String) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_NUMERO_LISTE_PROPERTY);
        }
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_ENVOI_RELECTURE_LE_PROPERTY) instanceof Calendar) {
            this.envoiRelectureLe = (Calendar) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_ENVOI_RELECTURE_LE_PROPERTY);
        }
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_DESTINATAIRE_ENTETE_PROPERTY) instanceof String) {
            this.destinataireEntete = (String) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_DESTINATAIRE_ENTETE_PROPERTY);
        }
        if (serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_NOMS_SIGNATAIRES_PROPERTY) instanceof String) {
            this.nomsSignataires = (String) serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_NOMS_SIGNATAIRES_PROPERTY);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication#setDestinataireCommunication(java.util.Map)
     */
    @Override
    public void setSerializableMap(Map<String, Serializable> serializableMap) {
        Serializable epreuveDemandeLe = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_DEMANDE_LE_PROPERTY);
        Serializable epreuvePourLe = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_POUR_LE_PROPERTY);
        Serializable numeroListe = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_NUMERO_LISTE_PROPERTY);
        Serializable envoiRelectureLe = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_ENVOI_RELECTURE_LE_PROPERTY);
        Serializable destinataireEntete = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_DESTINATAIRE_ENTETE_PROPERTY);
        Serializable nomsSignataires = serializableMap.get(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_NOMS_SIGNATAIRES_PROPERTY);
        
        // set the map
        super.setSerializableMap(serializableMap);
        // set all the field
        setEpreuveDemandeLe((Calendar) epreuveDemandeLe);
        setEpreuvePourLe((Calendar) epreuvePourLe);
        setNumeroListe((String) numeroListe);
        setEnvoiRelectureLe((Calendar) envoiRelectureLe);
        setDestinataireEntete((String) destinataireEntete);
        setNomsSignataires((String)nomsSignataires);
    }

    public Calendar getEpreuveDemandeLe() {
        return epreuveDemandeLe;
    }

    public void setEpreuveDemandeLe(Calendar epreuveDemandeLe) {
        this.epreuveDemandeLe = epreuveDemandeLe;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_DEMANDE_LE_PROPERTY, epreuveDemandeLe);
    }

    public Calendar getEpreuvePourLe() {
        return epreuvePourLe;
    }

    public void setEpreuvePourLe(Calendar epreuvePourLe) {
        this.epreuvePourLe = epreuvePourLe;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_POUR_LE_PROPERTY, epreuvePourLe);
    }

    public String getNumeroListe() {
        return numeroListe;
    }

    public void setNumeroListe(String numeroListe) {
        this.numeroListe = numeroListe;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_NUMERO_LISTE_PROPERTY, numeroListe);
    }

    public Calendar getEnvoiRelectureLe() {
        return envoiRelectureLe;
    }

    public void setEnvoiRelectureLe(Calendar envoiRelectureLe) {
        this.envoiRelectureLe = envoiRelectureLe;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_ENVOI_RELECTURE_LE_PROPERTY, envoiRelectureLe);
    }

    public String getDestinataireEntete() {
        return destinataireEntete;
    }

    public void setDestinataireEntete(String destinataireEntete) {
        this.destinataireEntete = destinataireEntete;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_DESTINATAIRE_ENTETE_PROPERTY, destinataireEntete);
    }
    
    
    public String getNomsSignataires() {
        return nomsSignataires;
    }
    
    public void setNomsSignataires(String nomsSignataires) {
        this.nomsSignataires = nomsSignataires;
        put(TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_NOMS_SIGNATAIRES_PROPERTY, nomsSignataires);
    }

    public String toString() {

        final StringBuilder builder = new StringBuilder();

        builder.append("{");
        builder.append("epreuveDemandeLe = ");
        if (epreuveDemandeLe != null) {
            builder.append(DateUtil.simpleDateFormat("dd/MM/yyyy").format((epreuveDemandeLe).getTime()));
        } else {
            builder.append(" ");
        }
        builder.append(", ");
        builder.append("epreuvePourLe = ");
        if (epreuvePourLe != null) {
            builder.append(DateUtil.simpleDateFormat("dd/MM/yyyy").format((epreuvePourLe).getTime()));
        } else {
            builder.append(" ");
        }
        builder.append(", ");
        builder.append("numeroListe = " + numeroListe);
        builder.append(", ");
        builder.append("envoiRelectureLe = ");
        if (envoiRelectureLe != null) {
            builder.append(DateUtil.simpleDateFormat("dd/MM/yyyy").format((envoiRelectureLe).getTime()));
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
            if (((this.destinataireEntete == null && infoEpreuve.destinataireEntete == null) || (this.destinataireEntete != null && this.destinataireEntete.equals(infoEpreuve.destinataireEntete))) 
                    && ((this.envoiRelectureLe == null && infoEpreuve.envoiRelectureLe == null) || (this.envoiRelectureLe != null && this.envoiRelectureLe.equals(infoEpreuve.envoiRelectureLe))) 
                    && ((this.epreuveDemandeLe == null && infoEpreuve.epreuveDemandeLe == null) || (this.epreuveDemandeLe != null && this.epreuveDemandeLe.equals(infoEpreuve.epreuveDemandeLe))) 
                    && ((this.numeroListe == null && infoEpreuve.numeroListe == null) || (this.numeroListe != null && this.numeroListe.equals(infoEpreuve.numeroListe))) 
                    && ((this.nomsSignataires == null && infoEpreuve.nomsSignataires == null) || (this.nomsSignataires != null && this.nomsSignataires.equals(infoEpreuve.nomsSignataires))) 
                    && ((this.epreuvePourLe == null && infoEpreuve.epreuvePourLe == null) || (this.epreuvePourLe != null && this.epreuvePourLe.equals(infoEpreuve.epreuvePourLe)))) {
                resultat = true;
            }
        }
        return resultat;

    }
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(getDestinataireEntete(),
                getEnvoiRelectureLe(),
                getEpreuveDemandeLe(),
                getEpreuvePourLe(),
                getNomsSignataires(),
                getNumeroListe());
    }
    
}
