package fr.dila.solonmgpp.core.domain;

import fr.dila.solonmgpp.api.domain.Navette;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public class NavetteImpl implements Navette, Serializable {
    private static final long serialVersionUID = 2073053179376953612L;

    private final DocumentModel document;

    public NavetteImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public String getCodeLecture() {
        return PropertyUtil.getStringProperty(document, SCHEMA, CODE_LECTURE);
    }

    @Override
    public void setCodeLecture(String codeLecture) {
        PropertyUtil.setProperty(document, SCHEMA, CODE_LECTURE, codeLecture);
    }

    @Override
    public Long getNiveauLecture() {
        return PropertyUtil.getLongProperty(document, SCHEMA, NIVEAU_LECTURE);
    }

    @Override
    public void setNiveauLecture(Long niveauLecture) {
        PropertyUtil.setProperty(document, SCHEMA, NIVEAU_LECTURE, niveauLecture);
    }

    @Override
    public Calendar getDateTransmission() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_TRANSMISSION);
    }

    @Override
    public void setDateTransmission(Calendar dateTransmission) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_TRANSMISSION, dateTransmission);
    }

    @Override
    public Calendar getDateNavette() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_NAVETTE);
    }

    @Override
    public void setDateNavette(Calendar dateNavette) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_NAVETTE, dateNavette);
    }

    @Override
    public Calendar getDateAdoption() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_ADOPTION);
    }

    @Override
    public void setDateAdoption(Calendar dateAdoption) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_ADOPTION, dateAdoption);
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

    @Override
    public void setNumeroTexte(String numeroTexte) {
        PropertyUtil.setProperty(document, SCHEMA, NUMERO_TEXTE, numeroTexte);
    }

    @Override
    public String getNumeroTexte() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NUMERO_TEXTE);
    }

    @Override
    public String getSortAdoption() {
        return PropertyUtil.getStringProperty(document, SCHEMA, SORT_ADOPTION);
    }

    @Override
    public void setSortAdoption(String sortAdoption) {
        PropertyUtil.setProperty(document, SCHEMA, SORT_ADOPTION, sortAdoption);
    }

    @Override
    public String getUrl() {
        return PropertyUtil.getStringProperty(document, SCHEMA, URL);
    }

    @Override
    public void setUrl(String url) {
        PropertyUtil.setProperty(document, SCHEMA, URL, url);
    }

    @Override
    public String getIdFiche() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ID_FICHE);
    }

    @Override
    public void setIdFiche(String idFiche) {
        PropertyUtil.setProperty(document, SCHEMA, ID_FICHE, idFiche);
    }

    @Override
    public List<Calendar> getDateCMP() {
        Object[] calendarArray = (Object[]) document.getProperty(SCHEMA, DATE_CMP);
        List<Calendar> calendarList = new ArrayList<Calendar>();
        if (calendarArray == null) {
            return calendarList;
        }
        for (Object object : calendarArray) {
            calendarList.add((Calendar) object);
        }
        return calendarList;
    }

    @Override
    public void setDateCMP(List<Calendar> dateCMP) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_CMP, dateCMP.toArray());
    }

    @Override
    public String getResultatCMP() {
        return PropertyUtil.getStringProperty(document, SCHEMA, RESULTAT_CMP);
    }

    @Override
    public void setResultatCMP(String resultatCMP) {
        PropertyUtil.setProperty(document, SCHEMA, RESULTAT_CMP, resultatCMP);
    }

    @Override
    public Calendar getDateAdoptionAN() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_ADOPTION_AN);
    }

    @Override
    public void setDateAdoptionAN(Calendar dateAdoptionAN) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_ADOPTION_AN, dateAdoptionAN);
    }

    @Override
    public String getSortAdoptionAN() {
        return PropertyUtil.getStringProperty(document, SCHEMA, SORT_ADOPTION_AN);
    }

    @Override
    public void setSortAdoptionAN(String sortAdoptionAN) {
        PropertyUtil.setProperty(document, SCHEMA, SORT_ADOPTION_AN, sortAdoptionAN);
    }

    @Override
    public Calendar getDateAdoptionSE() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_ADOPTION_SE);
    }

    @Override
    public void setDateAdoptionSE(Calendar dateAdoptionSE) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_ADOPTION_SE, dateAdoptionSE);
    }

    @Override
    public String getSortAdoptionSE() {
        return PropertyUtil.getStringProperty(document, SCHEMA, SORT_ADOPTION_SE);
    }

    @Override
    public void setSortAdoptionSE(String sortAdoptionSE) {
        PropertyUtil.setProperty(document, SCHEMA, SORT_ADOPTION_SE, sortAdoptionSE);
    }
}
