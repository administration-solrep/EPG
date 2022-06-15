package fr.dila.solonmgpp.core.domain;

import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.io.Serializable;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FichePresentationDecretImpl implements FichePresentationDecret, Serializable {
    private static final long serialVersionUID = -7930837590475478769L;

    public static final String DOC_TYPE = "FichePresentationDecret";
    public static final String SCHEMA = "fiche_presentation_decret";
    public static final String PREFIX = "fpdec";

    public static final String NOR = "nor";
    public static final String NOR_LOI = "norLoi";
    public static final String DATE_JO = "dateJO";
    public static final String NOR_PUBLICATION = "norPublication";
    public static final String PAGE_JO = "pageJO";
    public static final String NOR_DECRET_OUVERTURE_SESSION_EXTRAORDINAIRE = "norOuvertureSessionExt";
    public static final String URL_PUBLICATION = "urlPublication";
    public static final String NUM_JO = "numJO";
    public static final String INTITULE = "intitule";
    public static final String NUMERO_ACTE = "numeroActe";
    public static final String OBJET = "objet";
    public static final String DATE = "date";
    public static final String ACTIF = "actif";
    public static final String RUBRIQUE = "rubrique";

    private final DocumentModel document;

    public FichePresentationDecretImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public String getNor() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOR);
    }

    @Override
    public void setNor(String nor) {
        PropertyUtil.setProperty(document, SCHEMA, NOR, nor);
    }

    @Override
    public String getNorLoi() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOR_LOI);
    }

    @Override
    public void setNorLoi(String norLoi) {
        PropertyUtil.setProperty(document, SCHEMA, NOR_LOI, norLoi);
    }

    @Override
    public Calendar getDateJO() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_JO);
    }

    @Override
    public void setDateJO(Calendar dateJO) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_JO, dateJO);
    }

    @Override
    public String getNorPublication() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOR_PUBLICATION);
    }

    @Override
    public void setNorPublication(String norPublication) {
        PropertyUtil.setProperty(document, SCHEMA, NOR_PUBLICATION, norPublication);
    }

    @Override
    public String getPageJO() {
        return PropertyUtil.getStringProperty(document, SCHEMA, PAGE_JO);
    }

    @Override
    public void setPageJO(String pageJO) {
        PropertyUtil.setProperty(document, SCHEMA, PAGE_JO, pageJO);
    }

    @Override
    public String getRubrique() {
        return PropertyUtil.getStringProperty(document, SCHEMA, RUBRIQUE);
    }

    @Override
    public void setRubrique(String rubrique) {
        PropertyUtil.setProperty(document, SCHEMA, RUBRIQUE, rubrique);
    }

    @Override
    public String getUrlPublication() {
        return PropertyUtil.getStringProperty(document, SCHEMA, URL_PUBLICATION);
    }

    @Override
    public void setUrlPublication(String urlPublication) {
        PropertyUtil.setProperty(document, SCHEMA, URL_PUBLICATION, urlPublication);
    }

    @Override
    public String getNumJO() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NUM_JO);
    }

    @Override
    public void setNumJO(String numJo) {
        PropertyUtil.setProperty(document, SCHEMA, NUM_JO, numJo);
    }

    @Override
    public String getNorOuvertureSessionExt() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOR_DECRET_OUVERTURE_SESSION_EXTRAORDINAIRE);
    }

    @Override
    public void setNorOuvertureSessionExt(String norOuvertureSessionExt) {
        PropertyUtil.setProperty(document, SCHEMA, NOR_DECRET_OUVERTURE_SESSION_EXTRAORDINAIRE, norOuvertureSessionExt);
    }

    @Override
    public String getIntitule() {
        return PropertyUtil.getStringProperty(document, SCHEMA, INTITULE);
    }

    @Override
    public void setIntitule(String intitule) {
        PropertyUtil.setProperty(document, SCHEMA, INTITULE, intitule);
    }

    @Override
    public String getNumeroActe() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NUMERO_ACTE);
    }

    @Override
    public void setNumeroActe(String numeroActe) {
        PropertyUtil.setProperty(document, SCHEMA, NUMERO_ACTE, numeroActe);
    }

    @Override
    public String getObjet() {
        return PropertyUtil.getStringProperty(document, SCHEMA, OBJET);
    }

    @Override
    public void setObjet(String objet) {
        PropertyUtil.setProperty(document, SCHEMA, OBJET, objet);
    }

    @Override
    public Calendar getDate() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE);
    }

    @Override
    public void getDate(Calendar date) {
        PropertyUtil.setProperty(document, SCHEMA, DATE, date);
    }

    @Override
    public Boolean isActif() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, ACTIF);
    }

    @Override
    public void setActif(Boolean actif) {
        PropertyUtil.setProperty(document, SCHEMA, ACTIF, actif);
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }
}
