package fr.dila.solonepg.api.cases;

import java.util.List;


/**
 * 
 * Texte transposant dans une directive européenne
 * 
 * @author asatre
 * 
 */
public interface TexteTransposition extends TexteMaitre {

    String getId();

    String getNature();
    
    String getTypeActe();

    String getIntitule();

    String getNumeroTextePublie();

    String getTitreTextePublie();

    List<String> getTranspositionIds();

    void setNature(String nature);

    void setTypeActe(String typeActe);

    void setIntitule(String intitule);

    void setNumeroTextePublie(String numeroTextePublie);

    void setTitreTextePublie(String titreTextePublie);

    void setTranspositionIds(List<String> transpositionIds);

}
