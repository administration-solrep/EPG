package fr.dila.solonmgpp.core.domain;

import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implementation de {@link FichePresentationOEP}
 *
 * @author asatre
 *
 */
public class FichePresentationOEPImpl implements FichePresentationOEP, Serializable {
    private static final long serialVersionUID = 6717935433174615506L;

    public static final String DOC_TYPE = "FichePresentationOEP";
    public static final String SCHEMA = "fiche_presentation_oep";
    public static final String PREFIX = "fpoep";

    public static final String ID_DOSSIER = "idDossier";
    public static final String MINISTERE_RATTACHEMENT = "ministereRattachement";
    public static final String MINISTERE_RATTACHEMENT_2 = "ministereRattachement2";
    public static final String MINISTERE_RATTACHEMENT_3 = "ministereRattachement3";
    public static final String TEXTE_REF = "texteRef";
    public static final String CHARGE_MISSION = "chargeMission";
    public static final String INFORMER_CHARGE_MISSION = "informerChargeMission";
    public static final String NATURE_JURIDIQUE = "natureJuridiqueOrganisme";
    public static final String COMMISSION_DU_DECRET2006 = "commissionDuDecret2006";
    public static final String IS_SUIVIE_PAR_DQD = "isSuivieDQD";
    public static final String IS_SUIVIE_PAR_APP_SUIVI_MANDATS = "isSuivieAppSuiviMandats";
    public static final String MOTIF_FIN = "motifFinOEP";
    public static final String IDS_ANAT_LIES = "idsANATLies";

    // Coordonnées
    public static final String ADRESSE = "adresse";
    public static final String TEL = "tel";
    public static final String FAX = "fax";
    public static final String MAIL = "mail";

    // Durée mandat
    public static final String DUREE_MANDAT_AN = "dureeMandatAN";
    public static final String RENOUVELABLE_AN = "renouvelableAN";
    public static final String NB_MANDATS_AN = "nbMandatsAN";
    public static final String DUREE_MANDAT_SE = "dureeMandatSE";
    public static final String RENOUVELABLE_SE = "renouvelableSE";
    public static final String NB_MANDATS_SE = "nbMandatsSE";

    public static final String ID_ORGANISME_EPP = "idOrganismeEPP";
    public static final String NOM_ORGANISME = "nomOrganisme";
    public static final String ID_COMMUN = "idCommun";

    public static final String NB_DEPUTE = "nbDeputes";
    public static final String NB_SENATEUR = "nbSenateurs";

    public static final String TEXTE_DUREE = "texteDuree";
    public static final String COMMENTAIRE = "commentaire";
    public static final String OBSERVATION = "observation";

    public static final String DATE = "date";
    public static final String DATE_FIN = "dateFin";
    public static final String DATE_DIFFUSION = "dateDiffusion";

    public static final String NOM_INTERLOCUTEUR_REF = "nomInterlocuteurRef";
    public static final String FONCTION_INTERLOCUTEUR_REF = "fonctionInterlocuteurRef";

    public static final String DIFFUSE = "diffuse";

    private final DocumentModel document;

    public FichePresentationOEPImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public String getIdDossier() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ID_DOSSIER);
    }

    @Override
    public void setIdDossier(String idDossier) {
        PropertyUtil.setProperty(document, SCHEMA, ID_DOSSIER, idDossier);
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

    @Override
    public String getMinistereRattachement() {
        return PropertyUtil.getStringProperty(document, SCHEMA, MINISTERE_RATTACHEMENT);
    }

    @Override
    public void setMinistereRattachement(String ministereRattachement) {
        PropertyUtil.setProperty(document, SCHEMA, MINISTERE_RATTACHEMENT, ministereRattachement);
    }

    @Override
    public String getTexteRef() {
        return PropertyUtil.getStringProperty(document, SCHEMA, TEXTE_REF);
    }

    @Override
    public void setTexteRef(String texteRef) {
        PropertyUtil.setProperty(document, SCHEMA, TEXTE_REF, texteRef);
    }

    @Override
    public String getAdresse() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ADRESSE);
    }

    @Override
    public void setAdresse(String adresse) {
        PropertyUtil.setProperty(document, SCHEMA, ADRESSE, adresse);
    }

    @Override
    public String getTel() {
        return PropertyUtil.getStringProperty(document, SCHEMA, TEL);
    }

    @Override
    public void setTel(String tel) {
        PropertyUtil.setProperty(document, SCHEMA, TEL, tel);
    }

    @Override
    public String getFax() {
        return PropertyUtil.getStringProperty(document, SCHEMA, FAX);
    }

    @Override
    public void setFax(String fax) {
        PropertyUtil.setProperty(document, SCHEMA, FAX, fax);
    }

    @Override
    public String getMail() {
        return PropertyUtil.getStringProperty(document, SCHEMA, MAIL);
    }

    @Override
    public void setMail(String mail) {
        PropertyUtil.setProperty(document, SCHEMA, MAIL, mail);
    }

    @Override
    public String getDureeMandatAN() {
        return PropertyUtil.getStringProperty(document, SCHEMA, DUREE_MANDAT_AN);
    }

    @Override
    public void setDureeMandatAN(String dureeMandatAN) {
        PropertyUtil.setProperty(document, SCHEMA, DUREE_MANDAT_AN, dureeMandatAN);
    }

    @Override
    public String getDureeMandatSE() {
        return PropertyUtil.getStringProperty(document, SCHEMA, DUREE_MANDAT_SE);
    }

    @Override
    public void setDureeMandatSE(String dureeMandatSE) {
        PropertyUtil.setProperty(document, SCHEMA, DUREE_MANDAT_SE, dureeMandatSE);
    }

    @Override
    public String getIdOrganismeEPP() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ID_ORGANISME_EPP);
    }

    @Override
    public void setIdOrganismeEPP(String idOrganismeEPP) {
        PropertyUtil.setProperty(document, SCHEMA, ID_ORGANISME_EPP, idOrganismeEPP);
    }

    @Override
    public String getNomOrganisme() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOM_ORGANISME);
    }

    @Override
    public void setNomOrganisme(String nomOrganisme) {
        PropertyUtil.setProperty(document, SCHEMA, NOM_ORGANISME, nomOrganisme);
    }

    @Override
    public Calendar getDate() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE);
    }

    @Override
    public void setDate(Calendar date) {
        PropertyUtil.setProperty(document, SCHEMA, DATE, date);
    }

    @Override
    public String getIdCommun() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ID_COMMUN);
    }

    @Override
    public void setIdCommun(String idCommun) {
        PropertyUtil.setProperty(document, SCHEMA, ID_COMMUN, idCommun);
    }

    @Override
    public String getMinistereRattachement2() {
        return PropertyUtil.getStringProperty(document, SCHEMA, MINISTERE_RATTACHEMENT_2);
    }

    @Override
    public void setMinistereRattachement2(String ministereRattachement2) {
        PropertyUtil.setProperty(document, SCHEMA, MINISTERE_RATTACHEMENT_2, ministereRattachement2);
    }

    @Override
    public String getMinistereRattachement3() {
        return PropertyUtil.getStringProperty(document, SCHEMA, MINISTERE_RATTACHEMENT_3);
    }

    @Override
    public void setMinistereRattachement3(String ministereRattachement3) {
        PropertyUtil.setProperty(document, SCHEMA, MINISTERE_RATTACHEMENT_3, ministereRattachement3);
    }

    @Override
    public Long getNbDepute() {
        return PropertyUtil.getLongProperty(document, SCHEMA, NB_DEPUTE);
    }

    @Override
    public void setNbDepute(Long nbDepute) {
        PropertyUtil.setProperty(document, SCHEMA, NB_DEPUTE, nbDepute);
    }

    @Override
    public Long getNbSenateur() {
        return PropertyUtil.getLongProperty(document, SCHEMA, NB_SENATEUR);
    }

    @Override
    public void setNbSenateur(Long nbSenateur) {
        PropertyUtil.setProperty(document, SCHEMA, NB_SENATEUR, nbSenateur);
    }

    @Override
    public String getTexteDuree() {
        return PropertyUtil.getStringProperty(document, SCHEMA, TEXTE_DUREE);
    }

    @Override
    public void setTexteDuree(String texteDuree) {
        PropertyUtil.setProperty(document, SCHEMA, TEXTE_DUREE, texteDuree);
    }

    @Override
    public String getCommentaire() {
        return PropertyUtil.getStringProperty(document, SCHEMA, COMMENTAIRE);
    }

    @Override
    public void setCommentaire(String commentaire) {
        PropertyUtil.setProperty(document, SCHEMA, COMMENTAIRE, commentaire);
    }

    @Override
    public String getNomInterlocuteurRef() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOM_INTERLOCUTEUR_REF);
    }

    @Override
    public void setNomInterlocuteurRef(String nomInterlocuteurRef) {
        PropertyUtil.setProperty(document, SCHEMA, NOM_INTERLOCUTEUR_REF, nomInterlocuteurRef);
    }

    @Override
    public String getFonctionInterlocuteurRef() {
        return PropertyUtil.getStringProperty(document, SCHEMA, FONCTION_INTERLOCUTEUR_REF);
    }

    @Override
    public void setFonctionInterlocuteurRef(String fonctionInterlocuteurRef) {
        PropertyUtil.setProperty(document, SCHEMA, FONCTION_INTERLOCUTEUR_REF, fonctionInterlocuteurRef);
    }

    @Override
    public Calendar getDateFin() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_FIN);
    }

    @Override
    public void setDateFin(Calendar dateFin) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_FIN, dateFin);
    }

    @Override
    public List<String> getChargesMission() {
        return PropertyUtil.getStringListProperty(document, SCHEMA, CHARGE_MISSION);
    }

    @Override
    public void setChargesMission(List<String> charges) {
        PropertyUtil.setProperty(document, SCHEMA, CHARGE_MISSION, charges);
    }

    @Override
    public boolean informeChargeMission() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, INFORMER_CHARGE_MISSION);
    }

    @Override
    public void setInformeChargeMission(boolean informe) {
        PropertyUtil.setProperty(document, SCHEMA, INFORMER_CHARGE_MISSION, informe);
    }

    @Override
    public String getNatureJuridique() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NATURE_JURIDIQUE);
    }

    @Override
    public void setNatureJuridique(String nature) {
        PropertyUtil.setProperty(document, SCHEMA, NATURE_JURIDIQUE, nature);
    }

    @Override
    public boolean isCommissionDuDecret2006() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, COMMISSION_DU_DECRET2006);
    }

    @Override
    public void setCommissionDuDecret2006(boolean commission) {
        PropertyUtil.setProperty(document, SCHEMA, COMMISSION_DU_DECRET2006, commission);
    }

    @Override
    public boolean isSuivieParDQD() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, IS_SUIVIE_PAR_DQD);
    }

    @Override
    public void setSuivieParDQD(boolean suivi) {
        PropertyUtil.setProperty(document, SCHEMA, IS_SUIVIE_PAR_DQD, suivi);
    }

    @Override
    public boolean isSuivieParAppSuiviMandats() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, IS_SUIVIE_PAR_APP_SUIVI_MANDATS);
    }

    @Override
    public void setSuivieparAppSuiviMandats(boolean suivi) {
        PropertyUtil.setProperty(document, SCHEMA, IS_SUIVIE_PAR_APP_SUIVI_MANDATS, suivi);
    }

    @Override
    public String getMotifFin() {
        return PropertyUtil.getStringProperty(document, SCHEMA, MOTIF_FIN);
    }

    @Override
    public void setMotifFin(String motif) {
        PropertyUtil.setProperty(document, SCHEMA, MOTIF_FIN, motif);
    }

    @Override
    public boolean isRenouvelableAN() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, RENOUVELABLE_AN);
    }

    @Override
    public void setRenouvelableAN(boolean renouvelable) {
        PropertyUtil.setProperty(document, SCHEMA, RENOUVELABLE_AN, renouvelable);
    }

    @Override
    public Long getNbMandatsAN() {
        return PropertyUtil.getLongProperty(document, SCHEMA, NB_MANDATS_AN);
    }

    @Override
    public void setNbMandatsAN(Long nbMandats) {
        PropertyUtil.setProperty(document, SCHEMA, NB_MANDATS_AN, nbMandats);
    }

    @Override
    public boolean isRenouvelableSE() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, RENOUVELABLE_SE);
    }

    @Override
    public void setRenouvelableSE(boolean renouvelable) {
        PropertyUtil.setProperty(document, SCHEMA, RENOUVELABLE_SE, renouvelable);
    }

    @Override
    public Long getNbMandatsSE() {
        return PropertyUtil.getLongProperty(document, SCHEMA, NB_MANDATS_SE);
    }

    @Override
    public void setNbMandatsSE(Long nbMandats) {
        PropertyUtil.setProperty(document, SCHEMA, NB_MANDATS_SE, nbMandats);
    }

    @Override
    public Calendar getDateDiffusion() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_DIFFUSION);
    }

    @Override
    public void setDateDiffusion(Calendar dateDiffusion) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_DIFFUSION, dateDiffusion);
    }

    @Override
    public boolean isDiffuse() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, DIFFUSE);
    }

    @Override
    public void setDiffuse(boolean diffuse) {
        PropertyUtil.setProperty(document, SCHEMA, DIFFUSE, diffuse);
    }

    @Override
    public String getIdsANATLies() {
        String idsList = PropertyUtil.getStringProperty(document, SCHEMA, IDS_ANAT_LIES);
        if (idsList == null || idsList.isEmpty()) {
            idsList = getIdDossier();
            PropertyUtil.setProperty(document, SCHEMA, IDS_ANAT_LIES, idsList);
        }
        return idsList;
    }

    @Override
    public List<String> getListOfIdsANATLies() {
        List<String> toReturn = new ArrayList<String>();
        Collections.addAll(toReturn, PropertyUtil.getStringProperty(document, SCHEMA, IDS_ANAT_LIES).split(";"));
        return toReturn;
    }

    @Override
    public void addToIdsANATLies(String anatId) {
        String idsList = getIdsANATLies();
        if (idsList == null || idsList.isEmpty()) {
            idsList = anatId;
        } else {
            idsList = idsList.concat(";").concat(anatId);
        }
        PropertyUtil.setProperty(document, SCHEMA, IDS_ANAT_LIES, idsList);
    }
}
