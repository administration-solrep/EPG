package fr.dila.solonepg.ui.bean;

import static fr.dila.solonepg.api.constant.SolonEpgConstant.FAVORIS_RECHERCHE_DOCUMENT_TYPE;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_DATE_MAX_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_DATE_MIN_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_UTILISATEUR_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_DIRECTION_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_MINISTERE_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_NUMERO_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_TITLE_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_TYPE_ACTE_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_AUTOMATIC_VALIDATION_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_DISTRIBUTION_MAILBOX_ID_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_ECHEANCE_INDICATIVE_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_OBLIGATOIRE_MINISTERE_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_OBLIGATOIRE_SGG_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_ROUTING_TASK_TYPE_XPATH;

import fr.dila.ss.core.enumeration.StatutModeleFDR;
import fr.dila.ss.ui.th.bean.RechercheModeleFdrForm;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.mapper.MapDoc2BeanBooleanStringProcess;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class EpgFavorisRechercheModeleFdrForm extends RechercheModeleFdrForm {
    @FormParam("intitule_modele")
    @NxProp(docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE, xpath = FAVORIS_RECHERCHE_FEUILLE_ROUTE_TITLE_XPATH)
    private String intitule;

    @FormParam("numero_modele")
    @NxProp(docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE, xpath = FAVORIS_RECHERCHE_FEUILLE_ROUTE_NUMERO_XPATH)
    private String numero;

    @FormParam("typeActe")
    @NxProp(docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE, xpath = FAVORIS_RECHERCHE_FEUILLE_ROUTE_TYPE_ACTE_XPATH)
    private String typeActe;

    @FormParam("ministere_modele-key")
    @NxProp(docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE, xpath = FAVORIS_RECHERCHE_FEUILLE_ROUTE_MINISTERE_XPATH)
    private String ministere;

    @FormParam("direction_modele-key")
    @NxProp(docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE, xpath = FAVORIS_RECHERCHE_FEUILLE_ROUTE_DIRECTION_XPATH)
    private String direction;

    @FormParam("utilisateurCreateur-key")
    @NxProp(
        docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE,
        xpath = FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_UTILISATEUR_XPATH
    )
    private String utilisateurCreateur;

    @FormParam("dateCreationDebut")
    @NxProp(docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE, xpath = FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_DATE_MIN_XPATH)
    private Calendar dateCreationStart;

    @FormParam("dateCreationFin")
    @NxProp(docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE, xpath = FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_DATE_MAX_XPATH)
    private Calendar dateCreationEnd;

    @FormParam("statut")
    private StatutModeleFDR statut;

    @FormParam("typeEtape")
    @NxProp(docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE, xpath = FAVORIS_RECHERCHE_ROUTE_STEP_ROUTING_TASK_TYPE_XPATH)
    private String typeEtape;

    @FormParam("destinataire-key")
    @NxProp(
        docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE,
        xpath = FAVORIS_RECHERCHE_ROUTE_STEP_DISTRIBUTION_MAILBOX_ID_XPATH
    )
    private String destinataire;

    @FormParam("echeanceIndicative")
    @NxProp(docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE, xpath = FAVORIS_RECHERCHE_ROUTE_STEP_ECHEANCE_INDICATIVE_XPATH)
    private String echeanceIndicative;

    @FormParam("franchissementAutomatique")
    @NxProp(
        docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE,
        xpath = FAVORIS_RECHERCHE_ROUTE_STEP_AUTOMATIC_VALIDATION_XPATH,
        process = MapDoc2BeanBooleanStringProcess.class
    )
    private Boolean franchissementAutomatique;

    @FormParam("obligatoireSGG")
    @NxProp(
        docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE,
        xpath = FAVORIS_RECHERCHE_ROUTE_STEP_OBLIGATOIRE_SGG_XPATH,
        process = MapDoc2BeanBooleanStringProcess.class
    )
    private Boolean obligatoireSGG;

    @FormParam("obligatoireMinistere")
    @NxProp(
        docType = FAVORIS_RECHERCHE_DOCUMENT_TYPE,
        xpath = FAVORIS_RECHERCHE_ROUTE_STEP_OBLIGATOIRE_MINISTERE_XPATH,
        process = MapDoc2BeanBooleanStringProcess.class
    )
    private Boolean obligatoireMinistere;

    public EpgFavorisRechercheModeleFdrForm() {
        super();
    }

    @Override
    public String getIntitule() {
        return intitule;
    }

    @Override
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    @Override
    public String getNumero() {
        return numero;
    }

    @Override
    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String getTypeActe() {
        return typeActe;
    }

    @Override
    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    @Override
    public String getMinistere() {
        return ministere;
    }

    @Override
    public void setMinistere(String ministere) {
        this.ministere = ministere;
    }

    @Override
    public String getDirection() {
        return direction;
    }

    @Override
    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String getUtilisateurCreateur() {
        return utilisateurCreateur;
    }

    @Override
    public void setUtilisateurCreateur(String utilisateurCreateur) {
        this.utilisateurCreateur = utilisateurCreateur;
    }

    @Override
    public Calendar getDateCreationStart() {
        return dateCreationStart;
    }

    @Override
    public void setDateCreationStart(Calendar dateCreationStart) {
        this.dateCreationStart = dateCreationStart;
    }

    @Override
    public Calendar getDateCreationEnd() {
        return dateCreationEnd;
    }

    @Override
    public void setDateCreationEnd(Calendar dateCreationEnd) {
        this.dateCreationEnd = dateCreationEnd;
    }

    @Override
    public StatutModeleFDR getStatut() {
        return statut;
    }

    @Override
    public void setStatut(StatutModeleFDR statut) {
        this.statut = statut;
    }

    @Override
    public String getTypeEtape() {
        return typeEtape;
    }

    @Override
    public void setTypeEtape(String typeEtape) {
        this.typeEtape = typeEtape;
    }

    @Override
    public String getDestinataire() {
        return destinataire;
    }

    @Override
    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    @Override
    public String getEcheanceIndicative() {
        return echeanceIndicative;
    }

    @Override
    public void setEcheanceIndicative(String echeanceIndicative) {
        this.echeanceIndicative = echeanceIndicative;
    }

    @Override
    public Boolean getFranchissementAutomatique() {
        return franchissementAutomatique;
    }

    @Override
    public void setFranchissementAutomatique(Boolean franchissementAutomatique) {
        this.franchissementAutomatique = franchissementAutomatique;
    }

    @Override
    public Boolean getObligatoireSGG() {
        return obligatoireSGG;
    }

    @Override
    public void setObligatoireSGG(Boolean obligatoireSGG) {
        this.obligatoireSGG = obligatoireSGG;
    }

    @Override
    public Boolean getObligatoireMinistere() {
        return obligatoireMinistere;
    }

    @Override
    public void setObligatoireMinistere(Boolean obligatoireMinistere) {
        this.obligatoireMinistere = obligatoireMinistere;
    }
}
