package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface FichePresentationSupportVote {
    public static final String ID_DOSSIER = "idDossier";
    public static final String OBJET = "objet";
    public static final String DATE_LETTRE_PM = "dateLettrePm";
    public static final String DATE_VOTE = "dateVote";
    public static final String SUFFRAGE_EXPRIME = "suffrageExprime";
    public static final String VOTE_POUR = "votePour";
    public static final String VOTE_CONTRE = "voteContre";
    public static final String ABSTENTION = "abstention";
    public static final String SENS_AVIS = "sensAvis";

    public void setIdDossier(String idDossier);

    public String getIdDossier();

    String getObjet();

    void setObjet(String objet);

    Calendar getDateLettrePm();

    void setDateLettrePm(Calendar dateLettrePm);

    Calendar getDateVote();

    void setDateVote(Calendar dateVote);

    Long getSuffrageExprime();

    void setSuffrageExprime(Long suffrageExprime);

    Long getVotePour();

    void setVotePour(Long votePour);

    Long getVoteContre();

    void setVoteContre(Long voteContre);

    Long getAbstention();

    void setAbstention(Long abstention);

    String getSensAvis();

    void setSensAvis(String sensAvis);

    DocumentModel getDocument();
}
