package fr.dila.solonepg.ui.th.bean;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.FormSort;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

@SwBean(keepdefaultValue = true)
public class StatutArchivageDossierForm extends AbstractSortablePaginationForm {
    public static final String NOR_NAME = "norTri";
    public static final String TITRE_ACTE_NAME = "titreActeTri";
    public static final String STATUT_ARCHIVAGE_NAME = "statutArchivagePeriodeTri";
    public static final String DATE_CHANGEMENT_STATUT_NAME = "dateChangementStatutTri";
    public static final String STATUT_EN_COURS_NAME = "statutEnCoursTri";
    public static final String MESSAGE_NAME = "messageTri";

    @QueryParam("periodeDebut")
    @FormParam("periodeDebut")
    private Calendar periodeDebut;

    @QueryParam("periodeFin")
    @FormParam("periodeFin")
    private Calendar periodeFin;

    @QueryParam("statutArchivage")
    @FormParam("statutArchivage")
    private String statutArchivage;

    @FormParam(NOR_NAME)
    private SortOrder norTri;

    @FormParam(TITRE_ACTE_NAME)
    private SortOrder titreActeTri;

    @FormParam(STATUT_ARCHIVAGE_NAME)
    private SortOrder statutArchivagePeriodeTri;

    @FormParam(DATE_CHANGEMENT_STATUT_NAME)
    private SortOrder dateChangementStatutTri;

    @FormParam(STATUT_EN_COURS_NAME)
    private SortOrder statutEnCoursTri;

    @FormParam(MESSAGE_NAME)
    private SortOrder messageTri;

    public SortOrder getNorTri() {
        return norTri;
    }

    public void setNorTri(SortOrder norTri) {
        this.norTri = norTri;
    }

    public SortOrder getTitreActeTri() {
        return titreActeTri;
    }

    public void setTitreActeTri(SortOrder titreActeTri) {
        this.titreActeTri = titreActeTri;
    }

    public SortOrder getStatutArchivagePeriodeTri() {
        return statutArchivagePeriodeTri;
    }

    public void setStatutArchivagePeriodeTri(SortOrder statutArchivagePeriodeTri) {
        this.statutArchivagePeriodeTri = statutArchivagePeriodeTri;
    }

    public SortOrder getDateChangementStatutTri() {
        return dateChangementStatutTri;
    }

    public void setDateChangementStatutTri(SortOrder dateChangementStatutTri) {
        this.dateChangementStatutTri = dateChangementStatutTri;
    }

    public SortOrder getStatutEnCoursTri() {
        return statutEnCoursTri;
    }

    public void setStatutEnCoursTri(SortOrder statutEnCoursTri) {
        this.statutEnCoursTri = statutEnCoursTri;
    }

    public SortOrder getMessageTri() {
        return messageTri;
    }

    public void setMessageTri(SortOrder messageTri) {
        this.messageTri = messageTri;
    }

    @Override
    protected void setDefaultSort() {
        norTri = SortOrder.ASC;
    }

    public Calendar getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(Calendar periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public Calendar getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(Calendar periodeFin) {
        this.periodeFin = periodeFin;
    }

    public String getStatutArchivage() {
        return statutArchivage;
    }

    public void setStatutArchivage(String statutArchivage) {
        this.statutArchivage = statutArchivage;
    }

    @Override
    protected Map<String, FormSort> getSortForm() {
        Map<String, FormSort> map = new HashMap<>();
        map.put("nor", new FormSort(norTri));
        map.put("titreActe", new FormSort(titreActeTri));
        map.put("dateExt", new FormSort(dateChangementStatutTri));
        map.put("erreur", new FormSort(messageTri));
        map.put("statutPeriode", new FormSort(statutArchivagePeriodeTri));
        map.put("statut", new FormSort(statutEnCoursTri));
        return map;
    }

    public boolean isEmpty() {
        return periodeDebut == null && periodeFin == null && statutArchivage == null;
    }
}
