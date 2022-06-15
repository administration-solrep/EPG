package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface Navette {
    public static final String DOC_TYPE = "Navette";
    public static final String SCHEMA = "navette";
    public static final String PREFIX = "nav";

    public static final String ID_FICHE = "idFiche";
    public static final String CODE_LECTURE = "codeLecture";
    public static final String NIVEAU_LECTURE = "niveauLecture";
    public static final String DATE_TRANSMISSION = "dateTransmission";
    public static final String DATE_NAVETTE = "dateNavette";
    public static final String DATE_CMP = "dateCMP";
    public static final String RESULTAT_CMP = "resultatCMP";
    public static final String DATE_ADOPTION = "dateAdoption";
    public static final String SORT_ADOPTION = "sortAdoption";
    public static final String DATE_ADOPTION_AN = "dateAdoptionAN";
    public static final String SORT_ADOPTION_AN = "sortAdoptionAN";
    public static final String DATE_ADOPTION_SE = "dateAdoptionSE";
    public static final String SORT_ADOPTION_SE = "sortAdoptionSE";
    public static final String NUMERO_TEXTE = "numeroTexte";
    public static final String URL = "url";

    DocumentModel getDocument();

    String getCodeLecture();

    void setCodeLecture(String codeLecture);

    Long getNiveauLecture();

    void setNiveauLecture(Long niveauLecture);

    Calendar getDateTransmission();

    void setDateTransmission(Calendar dateTransmission);

    List<Calendar> getDateCMP();

    void setDateCMP(List<Calendar> dateCMP);

    String getResultatCMP();

    void setResultatCMP(String resultatCMP);

    Calendar getDateAdoption();

    void setDateAdoption(Calendar dateAdoption);

    String getSortAdoption();

    void setSortAdoption(String sortAdoption);

    Calendar getDateAdoptionAN();

    void setDateAdoptionAN(Calendar dateAdoptionAN);

    String getSortAdoptionAN();

    void setSortAdoptionAN(String sortAdoptionAN);

    Calendar getDateAdoptionSE();

    void setDateAdoptionSE(Calendar dateAdoptionSE);

    String getSortAdoptionSE();

    void setSortAdoptionSE(String sortAdoptionSE);

    String getNumeroTexte();

    void setNumeroTexte(String numeroTexte);

    String getUrl();

    void setUrl(String url);

    String getIdFiche();

    void setIdFiche(String idFiche);

    Calendar getDateNavette();

    void setDateNavette(Calendar dateNavette);
}
