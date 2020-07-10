package fr.dila.solonepg.core.cases;

import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.TexteTransposition;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;

public class TexteTranspositionImpl extends TexteMaitreImpl implements TexteTransposition {

    private static final long serialVersionUID = -396940299858236104L;

    public TexteTranspositionImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getId() {
        return document.getId();
    }

    @Override
    public String getNature() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NATURE_TEXTE);
    }

    @Override
    public String getTypeActe() {
      return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TYPE_ACTE);
    }

    @Override
    public String getIntitule() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.INTITULE);
    }

    @Override
    public String getNumeroTextePublie() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_TEXTE_PUBLIE);
    }

    @Override
    public String getTitreTextePublie() {
        return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TITRE_OFFICIEL);
    }

    @Override
    public void setNature(String nature) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NATURE_TEXTE, nature);
    }
    
    @Override
    public void setTypeActe(String typeActe) {
      setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TYPE_ACTE, typeActe);
    }

    @Override
    public void setIntitule(String intitule) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.INTITULE, intitule);
    }

    @Override
    public void setNumeroTextePublie(String numeroTextePublie) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_TEXTE_PUBLIE, numeroTextePublie);
    }

    @Override
    public void setTitreTextePublie(String titreTextePublie) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TITRE_OFFICIEL, titreTextePublie);
    }

    @Override
    public List<String> getTranspositionIds() {
        return getListStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TRANSPOSITION_IDS);
    }

    @Override
    public void setTranspositionIds(List<String> transpositionIds) {
        setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TRANSPOSITION_IDS, transpositionIds);
    }
}
