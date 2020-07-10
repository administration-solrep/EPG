package fr.dila.solonepg.core.cases;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;

/**
 * @see TranspositionDirective
 * @author asatre
 * 
 */
public class TranspositionDirectiveImpl extends TexteMaitreImpl implements TranspositionDirective {

    private static final long serialVersionUID = -2238065072545306447L;

    public TranspositionDirectiveImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getId() {
        return document.getId();
    }

    @Override
    public Calendar getDateDirective() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_DIRECTIVE);
    }

    @Override
    public String getTitre() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TITRE_ACTE);
    }

    @Override
    public Calendar getDateEcheance() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_ECHEANCE);
    }

    @Override
    public Boolean isTabAffichageMarcheInt() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TAB_AFFICHAGE_MARCHE_INT);
    }

    @Override
    public Calendar getDateProchainTabAffichage() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PROCHAIN_TAB_AFFICHAGE);
    }

    @Override
    public Boolean isAchevee() {
        return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ACHEVEE);
    }

    @Override
    public List<String> getDossiersNor() {
        return getListStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DOSSIERS_NOR);
    }

    @Override
    public void setDateDirective(Calendar dateDirective) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_DIRECTIVE, dateDirective);
    }

    @Override
    public void setTitre(String titre) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TITRE_ACTE, titre);
    }

    @Override
    public void setDateEcheance(Calendar dateEcheance) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_ECHEANCE, dateEcheance);
    }

    @Override
    public void setTabAffichageMarcheInt(Boolean tabAffichageMarcheInt) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TAB_AFFICHAGE_MARCHE_INT, tabAffichageMarcheInt);
    }

    @Override
    public void setDateProchainTabAffichage(Calendar dateProchainTabAffichage) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PROCHAIN_TAB_AFFICHAGE, dateProchainTabAffichage);
    }

    @Override
    public void setAchevee(Boolean achevee) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ACHEVEE, achevee);
    }

    @Override
    public void setDossiersNor(List<String> dossierIds) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DOSSIERS_NOR, dossierIds);
    }

    @Override
    public void setDirectionResponsable(String directionResponsable) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DIRECTION_RESPONSABLE, directionResponsable);
    }

    @Override
    public String getDirectionResponsable() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DIRECTION_RESPONSABLE);
    }

    @Override
    public Calendar getDateTranspositionEffective() {
        return getDateProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_TRANSPOSITION_EFFECTIVE);
    }

    @Override
    public void setDateTranspositionEffective(Calendar dateTranspositionEffective) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_TRANSPOSITION_EFFECTIVE, dateTranspositionEffective);
    }
    
    @Override
    public String getEtat() {
      return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DIRECTIVE_ETAT);
    }
    
    @Override
    public void setEtat(String etat) {
      setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DIRECTIVE_ETAT, etat);
    }

}
