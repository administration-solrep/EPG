package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.sword.xsd.solon.epp.EppPG04;

public class FichePresentationDPGImpl extends FichePresentationSupportVoteImpl implements FichePresentationDPG, Serializable {

    public FichePresentationDPGImpl(DocumentModel document) {
        super(document);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Calendar getDatePresentation() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_PRESENTATION);
    }

    @Override
    public void setDatePresentation(Calendar datePresentation) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_PRESENTATION, datePresentation);
    }

    @Override
    public void assignData(EppPG04 eppPG04) {

        if (eppPG04.getDateVote() != null) {
            this.setDateVote(eppPG04.getDateVote().toGregorianCalendar());
        }

        if (eppPG04.getSensVote() != null) {
            this.setSensAvis(eppPG04.getSensVote().name());
        }

        if (eppPG04.getNombreSuffrage() != null) {
            this.setSuffrageExprime(eppPG04.getNombreSuffrage().longValue());
        }

        if (eppPG04.getVoteContre() != null) {
            this.setVoteContre(eppPG04.getVoteContre().longValue());
        }

        if (eppPG04.getVotePour() != null) {
            this.setVotePour(eppPG04.getVotePour().longValue());
        }

        if (eppPG04.getAbstention() != null) {
            this.setAbstention(eppPG04.getAbstention().longValue());
        }
    }

    @Override
    protected String getSchemas() {
        return SCHEMA;
    }

}
