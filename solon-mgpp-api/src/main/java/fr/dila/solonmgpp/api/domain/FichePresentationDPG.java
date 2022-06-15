package fr.dila.solonmgpp.api.domain;

import fr.sword.xsd.solon.epp.EppPG04;
import java.util.Calendar;

public interface FichePresentationDPG extends FichePresentationSupportVote {
    public static final String DOC_TYPE = "FichePresentationDPG";
    public static final String SCHEMA = "fiche_presentation_dpg";
    public static final String PREFIX = "fpdpg";
    public static final String DATE_PRESENTATION = "datePresentation";

    Calendar getDatePresentation();

    void setDatePresentation(Calendar datePresentation);

    void assignData(EppPG04 eppPG04);
}
