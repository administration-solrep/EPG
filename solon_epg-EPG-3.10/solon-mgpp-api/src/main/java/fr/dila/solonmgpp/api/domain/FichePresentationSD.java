package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import java.util.List;

public interface FichePresentationSD extends FichePresentationSupportVote {

    public static final String DOC_TYPE = "FichePresentationSD";
    public static final String SCHEMA = "fiche_presentation_sd";
    public static final String PREFIX = "fpsd";
    public static final String DATE_DECLARATION = "dateDeclaration";
    public static final String DATE_DEMANDE = "dateDemande";
    public static final String GROUPE_PARLEMENTAIRE = "groupeParlementaire";
    public static final String DEMANDE_VOTE = "demandeVote";

    Calendar getDateDeclaration();

    void setDateDeclaration(Calendar dateDeclaration);

    Calendar getDateDemande();

    void setDateDemande(Calendar dateDemande);

    List<String> getGroupeParlementaire();

    void setGroupeParlementaire(List<String> groupeParlementaire);

    void setDemandeVote(boolean demandeVote);

    boolean getDemandeVote();
}
