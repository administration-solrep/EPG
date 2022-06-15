package fr.dila.solonepg.core.dto.activitenormative.injectionbdj;

import fr.dila.solonepg.api.dto.TexteBilanSemestrielDTO;

/**
 * Texte de bilan semestriel à envoyer au BDJ (élément textes).
 *
 * @author nvezian
 */
public class TexteBilanSemestrielDTOImpl implements TexteBilanSemestrielDTO {
    private static final long serialVersionUID = -1247523690080774381L;

    private String type;
    private String id;
    private String titre;
    private long mesuresAttendues;
    private long mesuresAppliquees;
    private long mesuresEnAttente;

    public TexteBilanSemestrielDTOImpl(
        String type,
        String id,
        String titre,
        long mesuresAttendues,
        long mesuresAppliquees,
        long mesuresEnAttente
    ) {
        super();
        this.type = type;
        this.id = id;
        this.titre = titre;
        this.mesuresAttendues = mesuresAttendues;
        this.mesuresAppliquees = mesuresAppliquees;
        this.mesuresEnAttente = mesuresEnAttente;
    }

    @Override
    public String getTitre() {
        return titre;
    }

    @Override
    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public long getMesuresAttendues() {
        return mesuresAttendues;
    }

    @Override
    public void setMesuresAttendues(long mesuresAttendues) {
        this.mesuresAttendues = mesuresAttendues;
    }

    @Override
    public long getMesuresAppliquees() {
        return mesuresAppliquees;
    }

    @Override
    public void setMesuresAppliquees(long mesuresAppliquees) {
        this.mesuresAppliquees = mesuresAppliquees;
    }

    @Override
    public long getMesuresEnAttente() {
        return mesuresEnAttente;
    }

    @Override
    public void setMesuresEnAttente(long mesuresEnAttente) {
        this.mesuresEnAttente = mesuresEnAttente;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Double getTaux() {
        if (mesuresAppliquees == 0 || mesuresAttendues == 0) {
            return 0d;
        } else {
            return (double) mesuresAppliquees / mesuresAttendues * 100;
        }
    }
}
