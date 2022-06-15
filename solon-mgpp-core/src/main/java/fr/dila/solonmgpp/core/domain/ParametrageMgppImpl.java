package fr.dila.solonmgpp.core.domain;

import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.st.core.util.CryptoUtils;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.io.Serializable;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implementation de {@link ParametrageMgpp}
 *
 * @author asatre
 *
 */
public class ParametrageMgppImpl implements ParametrageMgpp, Serializable {
    private static final long serialVersionUID = -5991496049977912872L;
    public static final String PATH = "/case-management/workspaces/admin";

    public static final String DOC_TYPE = "ParametrageMgpp";
    public static final String SCHEMA = "parametrage_mgpp";
    public static final String PREFIX = "pmgpp";

    public static final String URL_EPP = "urlEpp";
    public static final String LOGIN_EPP = "loginEpp";
    public static final String PASSWORD_EPP = "passEpp";

    public static final String NB_JOUR_AFFICHABLE = "nbJourAffichable";

    public static final String NOM_AN = "nomAN";
    public static final String NOM_SENAT = "nomSenat";

    public static final String NOM_SGG = "nomSGG";
    public static final String NOM_DIRECTEUR_ADJOINT_SGG = "nomDirecteurAdjointSGG";

    public static final String DELAI = "delai";

    public static final String AUTEUR_LEX01 = "auteurLEX01";

    public static final String TEXTE_LIBRE_LISTE_OEP = "texteLibreListeOep";

    public static final String DELAI_PURGE_CALENDRIER = "delaiPurgeCalendrier";

    public static final String MINISTRE_OR_SECRETAIRE = "ministreOrSecretaire";

    public static final String FILTRE_DATE_CREATION_LOI = "filtreDateCreationLoi";

    private final DocumentModel document;

    public ParametrageMgppImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

    @Override
    public String getUrlEpp() {
        return PropertyUtil.getStringProperty(document, SCHEMA, URL_EPP);
    }

    @Override
    public void setUrlEpp(String urlEpp) {
        PropertyUtil.setProperty(document, SCHEMA, URL_EPP, urlEpp);
    }

    @Override
    public String getLoginEpp() {
        return PropertyUtil.getStringProperty(document, SCHEMA, LOGIN_EPP);
    }

    @Override
    public void setLoginEpp(String loginEpp) {
        PropertyUtil.setProperty(document, SCHEMA, LOGIN_EPP, loginEpp);
    }

    @Override
    public String getPassEpp() {
        String passEpp = PropertyUtil.getStringProperty(document, SCHEMA, PASSWORD_EPP);
        return CryptoUtils.decodeValue(passEpp);
    }

    @Override
    public void setPassEpp(String passEpp) {
        PropertyUtil.setProperty(document, SCHEMA, PASSWORD_EPP, CryptoUtils.encodeValue(passEpp));
    }

    @Override
    public Long getNbJourAffichable() {
        return PropertyUtil.getLongProperty(document, SCHEMA, NB_JOUR_AFFICHABLE);
    }

    @Override
    public void setNbJourAffichable(Long nbJourAffichable) {
        PropertyUtil.setProperty(document, SCHEMA, NB_JOUR_AFFICHABLE, nbJourAffichable);
    }

    @Override
    public String getNomAN() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOM_AN);
    }

    @Override
    public void setNomAN(String nomAN) {
        PropertyUtil.setProperty(document, SCHEMA, NOM_AN, nomAN);
    }

    @Override
    public String getNomSenat() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOM_SENAT);
    }

    @Override
    public void setNomSenat(String nomSenat) {
        PropertyUtil.setProperty(document, SCHEMA, NOM_SENAT, nomSenat);
    }

    @Override
    public Long getDelai() {
        return PropertyUtil.getLongProperty(document, SCHEMA, DELAI);
    }

    @Override
    public void setDelai(Long delai) {
        PropertyUtil.setProperty(document, SCHEMA, DELAI, delai);
    }

    @Override
    public String getAuteurLex01() {
        return PropertyUtil.getStringProperty(document, SCHEMA, AUTEUR_LEX01);
    }

    @Override
    public void setAuteurLex01(String auteurLex01) {
        PropertyUtil.setProperty(document, SCHEMA, AUTEUR_LEX01, auteurLex01);
    }

    @Override
    public String getTexteLibreListeOep() {
        return PropertyUtil.getStringProperty(document, SCHEMA, TEXTE_LIBRE_LISTE_OEP);
    }

    @Override
    public void setTexteLibreListeOep(String texteLibreListeOep) {
        PropertyUtil.setProperty(document, SCHEMA, TEXTE_LIBRE_LISTE_OEP, texteLibreListeOep);
    }

    @Override
    public Long getDelaiPurgeCalendrier() {
        return PropertyUtil.getLongProperty(document, SCHEMA, DELAI_PURGE_CALENDRIER);
    }

    @Override
    public void setDelaiPurgeCalendrier(Long delaiPurgeCalendrier) {
        PropertyUtil.setProperty(document, SCHEMA, DELAI_PURGE_CALENDRIER, delaiPurgeCalendrier);
    }

    @Override
    public Boolean isMinistre() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, MINISTRE_OR_SECRETAIRE);
    }

    @Override
    public void setIsMinistre(Boolean isMinistre) {
        PropertyUtil.setProperty(document, SCHEMA, MINISTRE_OR_SECRETAIRE, isMinistre);
    }

    @Override
    public Long getFiltreDateCreationLoi() {
        return PropertyUtil.getLongProperty(document, SCHEMA, FILTRE_DATE_CREATION_LOI);
    }

    @Override
    public void setFiltreDateCreationLoi(Long filtreDateCreationLoi) {
        PropertyUtil.setProperty(document, SCHEMA, FILTRE_DATE_CREATION_LOI, filtreDateCreationLoi);
    }

    @Override
    public String getNomSGG() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOM_SGG);
    }

    @Override
    public void setNomSGG(String nomSGG) {
        PropertyUtil.setProperty(document, SCHEMA, NOM_SGG, nomSGG);
    }

    @Override
    public String getNomDirecteurAdjointSGG() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOM_DIRECTEUR_ADJOINT_SGG);
    }

    @Override
    public void setNomDirecteurAdjointSGG(String nomDirAdjoint) {
        PropertyUtil.setProperty(document, SCHEMA, NOM_DIRECTEUR_ADJOINT_SGG, nomDirAdjoint);
    }
}
