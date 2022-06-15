package fr.dila.solonepg.core.dto.activitenormative.injectionbdj;

import fr.dila.solonepg.api.dto.BilanSemestrielDTO;
import fr.dila.solonepg.api.dto.TexteBilanSemestrielDTO;
import fr.dila.solonepg.api.enums.BilanSemestrielType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Bilan semestriel à envoyer au BDJ (élément textes) pour plusieurs lois ou ordonnances.
 *
 * @author nvezian
 */
public class BilanSemestrielDTOImpl implements BilanSemestrielDTO {
    private static final long serialVersionUID = 955272844836901569L;

    private BilanSemestrielType type;
    private Date dateDebutIntervalleTextesPublies;
    private Date dateDebutIntervalleMesures;
    private Date dateFinIntervalleTextesPublies;
    private Date dateFinIntervalleMesures;
    private Date dateBilan;

    private List<TexteBilanSemestrielDTO> listeTextes = new ArrayList<>();

    /**
     * Crée un nouveau bilanSemestriel.
     * Initialise sa date de création (dateBilan) à la date courante.
     */
    public BilanSemestrielDTOImpl() {
        super();
        this.setDateBilan(new Date());
    }

    public void addTexte(
        String id,
        String titre,
        long mesuresAttendues,
        long mesuresAppliquees,
        long mesuresEnAttente
    ) {
        TexteBilanSemestrielDTOImpl nouveauTexteBilan = new TexteBilanSemestrielDTOImpl(
            this.type.getLabel(),
            id,
            titre,
            mesuresAttendues,
            mesuresAppliquees,
            mesuresEnAttente
        );
        this.listeTextes.add(nouveauTexteBilan);
    }

    @Override
    public List<TexteBilanSemestrielDTO> getListeTextes() {
        return listeTextes;
    }

    @Override
    public BilanSemestrielType getType() {
        return type;
    }

    @Override
    public void setType(BilanSemestrielType type) {
        this.type = type;
    }

    @Override
    public Date getDateFinIntervalleTextesPublies() {
        return dateFinIntervalleTextesPublies;
    }

    @Override
    public void setDateFinIntervalleTextesPublies(Date dateFinIntervalleTextesPublies) {
        this.dateFinIntervalleTextesPublies = dateFinIntervalleTextesPublies;
    }

    @Override
    public Date getDateFinIntervalleMesures() {
        return dateFinIntervalleMesures;
    }

    @Override
    public void setDateFinIntervalleMesures(Date dateFinIntervalleMesures) {
        this.dateFinIntervalleMesures = dateFinIntervalleMesures;
    }

    @Override
    public Date getDateDebutIntervalleTextesPublies() {
        return dateDebutIntervalleTextesPublies;
    }

    @Override
    public void setDateDebutIntervalleTextesPublies(Date dateDebutIntervalleTextesPublies) {
        this.dateDebutIntervalleTextesPublies = dateDebutIntervalleTextesPublies;
    }

    @Override
    public Date getDateDebutIntervalleMesures() {
        return dateDebutIntervalleMesures;
    }

    @Override
    public void setDateDebutIntervalleMesures(Date dateDebutIntervalleMesures) {
        this.dateDebutIntervalleMesures = dateDebutIntervalleMesures;
    }

    @Override
    public Date getDateBilan() {
        return dateBilan;
    }

    @Override
    public void setDateBilan(Date dateBilan) {
        this.dateBilan = dateBilan;
    }
}
