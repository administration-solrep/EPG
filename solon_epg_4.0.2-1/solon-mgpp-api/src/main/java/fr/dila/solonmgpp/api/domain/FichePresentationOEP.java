package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de representation d'une fiche presentation d'un OEP
 *
 * @author asatre
 *
 */
public interface FichePresentationOEP {
    String getIdDossier();

    void setIdDossier(String idDossier);

    String getMinistereRattachement();

    void setMinistereRattachement(String ministereRattachement);

    String getMinistereRattachement2();

    void setMinistereRattachement2(String ministereRattachement2);

    String getMinistereRattachement3();

    void setMinistereRattachement3(String ministereRattachement3);

    String getTexteRef();

    void setTexteRef(String texteRef);

    List<String> getChargesMission();

    void setChargesMission(List<String> charges);

    boolean informeChargeMission();

    void setInformeChargeMission(boolean informe);

    String getNatureJuridique();

    void setNatureJuridique(String nature);

    boolean isCommissionDuDecret2006();

    void setCommissionDuDecret2006(boolean commission);

    boolean isSuivieParDQD();

    void setSuivieParDQD(boolean suivi);

    boolean isSuivieParAppSuiviMandats();

    void setSuivieparAppSuiviMandats(boolean suivi);

    String getMotifFin();

    void setMotifFin(String motif);

    String getAdresse();

    void setAdresse(String adresse);

    String getTel();

    void setTel(String tel);

    String getFax();

    void setFax(String fax);

    String getMail();

    void setMail(String mail);

    String getDureeMandatAN();

    void setDureeMandatAN(String dureeMandatAN);

    boolean isRenouvelableAN();

    void setRenouvelableAN(boolean renouvelable);

    Long getNbMandatsAN();

    void setNbMandatsAN(Long nbMandats);

    String getDureeMandatSE();

    void setDureeMandatSE(String dureeMandatSE);

    boolean isRenouvelableSE();

    void setRenouvelableSE(boolean renouvelable);

    Long getNbMandatsSE();

    void setNbMandatsSE(Long nbMandats);

    String getIdOrganismeEPP();

    void setIdOrganismeEPP(String idOrganismeEPP);

    DocumentModel getDocument();

    String getNomOrganisme();

    void setNomOrganisme(String nomOrganisme);

    Calendar getDate();

    void setDate(Calendar date);

    String getIdCommun();

    void setIdCommun(String idCommun);

    Long getNbDepute();

    void setNbDepute(Long nbDepute);

    Long getNbSenateur();

    void setNbSenateur(Long nbSenateur);

    String getTexteDuree();

    void setTexteDuree(String texteDuree);

    String getCommentaire();

    void setCommentaire(String commentaire);

    String getNomInterlocuteurRef();

    void setNomInterlocuteurRef(String nomInterlocuteurRef);

    String getFonctionInterlocuteurRef();

    void setFonctionInterlocuteurRef(String fonctionInterlocuteurRef);

    Calendar getDateFin();

    void setDateFin(Calendar dateFin);

    Calendar getDateDiffusion();

    void setDateDiffusion(Calendar dateDiffusion);

    boolean isDiffuse();

    void setDiffuse(boolean diffuse);

    public String getIdsANATLies();

    public void addToIdsANATLies(String anatId);

    List<String> getListOfIdsANATLies();
}
