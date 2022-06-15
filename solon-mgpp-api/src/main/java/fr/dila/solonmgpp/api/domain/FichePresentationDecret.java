package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de representation d'une fiche presentation d'un decret
 *
 * @author asatre
 *
 */
public interface FichePresentationDecret {
    String getNor();

    void setNor(String nor);

    String getNorLoi();

    void setNorLoi(String norLoi);

    Calendar getDateJO();

    void setDateJO(Calendar dateJO);

    String getNorPublication();

    void setNorPublication(String norPublication);

    String getPageJO();

    void setPageJO(String pageJO);

    String getRubrique();

    void setRubrique(String rubrique);

    String getUrlPublication();

    void setUrlPublication(String urlPublication);

    String getNumJO();

    void setNumJO(String numJo);

    String getIntitule();

    void setIntitule(String intitule);

    String getNumeroActe();

    void setNumeroActe(String numeroActe);

    String getObjet();

    void setObjet(String objet);

    Calendar getDate();

    void getDate(Calendar date);

    Boolean isActif();

    void setActif(Boolean actif);

    public String getNorOuvertureSessionExt();

    public void setNorOuvertureSessionExt(String norOuvertureSessionExt);

    DocumentModel getDocument();
}
