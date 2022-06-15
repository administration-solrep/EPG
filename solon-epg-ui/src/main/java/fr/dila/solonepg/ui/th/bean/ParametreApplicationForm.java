package fr.dila.solonepg.ui.th.bean;

import com.google.common.collect.Lists;
import fr.dila.solonepg.api.constant.SolonEpgParametrageApplicationConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.FormParam;

@SwBean
public class ParametreApplicationForm {
    @SwNotEmpty
    @FormParam("nbDossierPage")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_NB_DOSSIER_PAGE
    )
    private Long nbDossierPage;

    @SwNotEmpty
    @FormParam("dateRafraichissementCorbeille")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_DELAI_RAFRAICHISSEMENT_CORBEILLE
    )
    private Long dateRafraichissementCorbeille;

    @FormParam("metadonneSelected")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_META_DISPO_CORBEILLE
    )
    private ArrayList<String> metadonneDisponibleColonneCorbeille = new ArrayList<>();

    private List<SelectValueDTO> metadonneDispos = new ArrayList<>();

    @SwNotEmpty
    @FormParam("nbDerniersResultatsConsultes")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_NB_DERNIERS_RESULTATS
    )
    private Long nbDerniersResultatsConsultes;

    @SwNotEmpty
    @FormParam("nbFavorisConsultation")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_NB_FAVORIS_CONSULTATION
    )
    private Long nbFavorisConsultation;

    @SwNotEmpty
    @FormParam("nbFavorisRecherche")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_NB_FAVORIS_RECHERCHE
    )
    private Long nbFavorisRecherche;

    @SwNotEmpty
    @FormParam("nbDossiersSignales")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_NB_DOSSIERS_SIGNALES
    )
    private Long nbDossiersSignales;

    @SwNotEmpty
    @FormParam("nbTableauxDynamiques")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_NB_TABLEAUX_DYNAMIQUES
    )
    private Long nbTableauxDynamiques;

    @SwNotEmpty
    @FormParam("delaiEnvoiMailMaintienAlerte")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_DELAI_ENVOI_MAIL_ALERTE
    )
    private Long delaiEnvoiMailMaintienAlerte;

    @SwNotEmpty
    @FormParam("delaiAffichageMessage")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_DELAI_AFFICHAGE_MESSAGE
    )
    private Long delaiAffichageMessage;

    @FormParam("suffixesMailsAutorises")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_SUFF_MAILS_AUTORISES
    )
    private ArrayList<String> suffixesMailsAutorises = new ArrayList<>();

    @SwNotEmpty
    @FormParam("urlEppInfosParl")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_URL_EPP_INFOS_PARL
    )
    private String urlEppInfosParl;

    @SwNotEmpty
    @FormParam("loginEppInfosParl")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_LOGIN_EPP_INFOS_PARL
    )
    private String loginEppInfosParl;

    @SwNotEmpty
    @FormParam("passEppInfosParl")
    @NxProp(
        docType = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
        xpath = SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_XPATH_PASS_EPP_INFOS_PARL
    )
    private String passEppInfosParl;

    public ParametreApplicationForm() {
        this.nbDossierPage = null;
        this.delaiAffichageMessage = null;
        this.dateRafraichissementCorbeille = null;
        this.nbDerniersResultatsConsultes = null;
        this.nbFavorisConsultation = null;
        this.nbFavorisRecherche = null;
        this.nbDossiersSignales = null;
        this.nbTableauxDynamiques = null;
        this.delaiEnvoiMailMaintienAlerte = null;
        this.urlEppInfosParl = null;
        this.loginEppInfosParl = null;
        this.passEppInfosParl = null;
    }

    public Long getNbDossierPage() {
        return nbDossierPage;
    }

    public void setNbDossierPage(Long nbDossierPage) {
        this.nbDossierPage = nbDossierPage;
    }

    public Long getDateRafraichissementCorbeille() {
        return dateRafraichissementCorbeille;
    }

    public void setDateRafraichissementCorbeille(Long dateRafraichissementCorbeille) {
        this.dateRafraichissementCorbeille = dateRafraichissementCorbeille;
    }

    public List<String> getMetadonneDisponibleColonneCorbeille() {
        return metadonneDisponibleColonneCorbeille;
    }

    public void setMetadonneDisponibleColonneCorbeille(List<String> metadonneDisponibleColonneCorbeille) {
        this.metadonneDisponibleColonneCorbeille = Lists.newArrayList(metadonneDisponibleColonneCorbeille);
    }

    public Long getNbDerniersResultatsConsultes() {
        return nbDerniersResultatsConsultes;
    }

    public void setNbDerniersResultatsConsultes(Long nbDerniersResultatsConsultes) {
        this.nbDerniersResultatsConsultes = nbDerniersResultatsConsultes;
    }

    public Long getNbFavorisConsultation() {
        return nbFavorisConsultation;
    }

    public void setNbFavorisConsultation(Long nbFavorisConsultation) {
        this.nbFavorisConsultation = nbFavorisConsultation;
    }

    public Long getNbFavorisRecherche() {
        return nbFavorisRecherche;
    }

    public void setNbFavorisRecherche(Long nbFavorisRecherche) {
        this.nbFavorisRecherche = nbFavorisRecherche;
    }

    public Long getNbDossiersSignales() {
        return nbDossiersSignales;
    }

    public void setNbDossiersSignales(Long nbDossiersSignales) {
        this.nbDossiersSignales = nbDossiersSignales;
    }

    public Long getNbTableauxDynamiques() {
        return nbTableauxDynamiques;
    }

    public void setNbTableauxDynamiques(Long nbTableauxDynamiques) {
        this.nbTableauxDynamiques = nbTableauxDynamiques;
    }

    public Long getDelaiEnvoiMailMaintienAlerte() {
        return delaiEnvoiMailMaintienAlerte;
    }

    public void setDelaiEnvoiMailMaintienAlerte(Long delaiEnvoiMailMaintienAlerte) {
        this.delaiEnvoiMailMaintienAlerte = delaiEnvoiMailMaintienAlerte;
    }

    public Long getDelaiAffichageMessage() {
        return delaiAffichageMessage;
    }

    public void setDelaiAffichageMessage(Long delaiAffichageMessage) {
        this.delaiAffichageMessage = delaiAffichageMessage;
    }

    public ArrayList<String> getSuffixesMailsAutorises() {
        return suffixesMailsAutorises;
    }

    public void setSuffixesMailsAutorises(ArrayList<String> suffixesMailsAutorises) {
        this.suffixesMailsAutorises = suffixesMailsAutorises;
    }

    public String getUrlEppInfosParl() {
        return urlEppInfosParl;
    }

    public void setUrlEppInfosParl(String urlEppInfosParl) {
        this.urlEppInfosParl = urlEppInfosParl;
    }

    public String getLoginEppInfosParl() {
        return loginEppInfosParl;
    }

    public void setLoginEppInfosParl(String loginEppInfosParl) {
        this.loginEppInfosParl = loginEppInfosParl;
    }

    public String getPassEppInfosParl() {
        return passEppInfosParl;
    }

    public void setPassEppInfosParl(String passEppInfosParl) {
        this.passEppInfosParl = passEppInfosParl;
    }

    public List<SelectValueDTO> getMetadonneDispos() {
        return metadonneDispos;
    }

    public void setMetadonneDispos(List<SelectValueDTO> metadonneDispos) {
        this.metadonneDispos = metadonneDispos;
    }
}
