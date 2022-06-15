package fr.dila.solonmgpp.api.domain;

import java.io.Serializable;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface du document contenant les paramètres du mgpp.
 *
 * @author asatre
 */
public interface ParametrageMgpp extends Serializable {
    String getUrlEpp();

    void setUrlEpp(String urlEpp);

    String getLoginEpp();

    void setLoginEpp(String loginEpp);

    String getPassEpp();

    void setPassEpp(String passEpp);

    Long getNbJourAffichable();

    void setNbJourAffichable(Long nbJourAffichable);

    String getNomAN();

    void setNomAN(String nomAN);

    String getNomSenat();

    void setNomSenat(String nomSenat);

    DocumentModel getDocument();

    Long getDelai();

    void setDelai(Long delai);

    String getAuteurLex01();

    void setAuteurLex01(String auteurLex01);

    String getTexteLibreListeOep();

    void setTexteLibreListeOep(String texteLibreListeOep);

    Long getDelaiPurgeCalendrier();

    void setDelaiPurgeCalendrier(Long delaiPurgeCalendrier);

    Long getFiltreDateCreationLoi();

    void setFiltreDateCreationLoi(Long filtreDateCreationLoi);

    Boolean isMinistre();

    void setIsMinistre(Boolean isMinistre);

    void setNomSGG(String nomSGG);

    String getNomSGG();

    String getNomDirecteurAdjointSGG();

    void setNomDirecteurAdjointSGG(String nomDirAdjoint);
}
