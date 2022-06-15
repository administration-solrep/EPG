package fr.dila.solonepg.ui.bean.pan.birt;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.ibm.icu.text.DateFormatSymbols;
import fr.dila.solonepg.ui.constants.pan.PanConstants;
import fr.dila.solonepg.ui.enums.pan.PanBirtFieldTypeEnum;
import fr.dila.ss.api.birt.BirtReport;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang.StringUtils;

public class PanBirtDTO {
    @BirtMapping(birtParam = PanConstants.BirtParam.DEBUT_INTERVALLE1_PARAM)
    private Date debutIntervalle1;

    @BirtMapping(birtParam = PanConstants.BirtParam.FIN_INTERVALLE1_PARAM)
    private Date finIntervalle1;

    @BirtMapping(birtParam = PanConstants.BirtParam.DEBUT_INTERVALLE2_PARAM)
    private Date debutIntervalle2;

    @BirtMapping(birtParam = PanConstants.BirtParam.FIN_INTERVALLE2_PARAM)
    private Date finIntervalle2;

    @BirtMapping(birtParam = PanConstants.BirtParam.DEBUTLEGISLATURE_PARAM)
    private Date debutLegislature;

    @BirtMapping(birtParam = PanConstants.BirtParam.LEGISLATURES)
    private String legislature;

    @BirtMapping(birtParam = PanConstants.BirtParam.LEGISLATURES_LABEL)
    private String legislatureLabel;

    @BirtMapping(birtParam = PanConstants.BirtParam.MINISTERES_PARAM)
    private String ministeres;

    @BirtMapping(birtParam = PanConstants.BirtParam.DIRECTIONS_PARAM)
    private String directions;

    @BirtMapping(birtParam = PanConstants.BirtParam.DERNIERE_SESSION_PARAM)
    private String derniereSession;

    @BirtMapping(birtParam = PanConstants.BirtParam.FIGER_PARAM)
    private String figerParam;

    @BirtMapping(birtParam = PanConstants.BirtParam.MINISTEREPILOTE_PARAM)
    private String ministerePilote;

    @BirtMapping(birtParam = PanConstants.BirtParam.MINISTEREPILOTELABEL_PARAM)
    private String ministerePiloteLabel;

    @BirtMapping(birtParam = PanConstants.BirtParam.DOSSIERID_PARAM)
    private String dossierId;

    @BirtMapping(birtParam = PanConstants.BirtParam.ANNEE_PARAM)
    private Integer annee;

    @BirtMapping(birtParam = PanConstants.BirtParam.MOIS_PARAM)
    private Integer mois;

    @BirtMapping(birtParam = PanConstants.BirtParam.PERIODE_PARAM)
    private String periode;

    private BirtReport birtReport;
    private List<SelectValueDTO> optionsLegislatures;
    private String libelleDateIntervalle1;
    private String libelleDateIntervalle2;
    private Integer anneeDebut;

    public PanBirtDTO(BirtReport report) {
        super();
        birtReport = report;
    }

    public PanBirtDTO() {
        super();
    }

    public String getLibelleDateIntervalle1() {
        return libelleDateIntervalle1;
    }

    public void setLibelleDateIntervalle1(String libelleDateIntervalle1) {
        this.libelleDateIntervalle1 = libelleDateIntervalle1;
    }

    public String getLibelleDateIntervalle2() {
        return libelleDateIntervalle2;
    }

    public void setLibelleDateIntervalle2(String libelleDateIntervalle2) {
        this.libelleDateIntervalle2 = libelleDateIntervalle2;
    }

    public Date getDebutIntervalle1() {
        return debutIntervalle1;
    }

    public void setDebutIntervalle1(Date debutIntervalle1) {
        this.debutIntervalle1 = debutIntervalle1;
    }

    public Date getFinIntervalle1() {
        return finIntervalle1;
    }

    public void setFinIntervalle1(Date finIntervalle1) {
        this.finIntervalle1 = finIntervalle1;
    }

    public Date getDebutIntervalle2() {
        return debutIntervalle2;
    }

    public void setDebutIntervalle2(Date debutIntervalle2) {
        this.debutIntervalle2 = debutIntervalle2;
    }

    public Date getFinIntervalle2() {
        return finIntervalle2;
    }

    public void setFinIntervalle2(Date finIntervalle2) {
        this.finIntervalle2 = finIntervalle2;
    }

    public Date getDebutLegislature() {
        return debutLegislature;
    }

    public void setDebutLegislature(Date debutLegislature) {
        this.debutLegislature = debutLegislature;
    }

    public String getDerniereSession() {
        return derniereSession;
    }

    public void setDerniereSession(String derniereSession) {
        this.derniereSession = derniereSession;
    }

    public String getFigerParam() {
        return figerParam;
    }

    public void setFigerParam(String figerParam) {
        this.figerParam = figerParam;
    }

    public String getMinisterePilote() {
        return ministerePilote;
    }

    public void setMinisterePilote(String ministerePilote) {
        this.ministerePilote = ministerePilote;
    }

    public String getDossierId() {
        return dossierId;
    }

    public void setDossierId(String dossierId) {
        this.dossierId = dossierId;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Integer getMois() {
        return mois;
    }

    public void setMois(Integer mois) {
        this.mois = mois;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Integer getAnneeDebut() {
        return anneeDebut;
    }

    public void setAnneeDebut(Integer anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public String getLegislature() {
        return legislature;
    }

    public void setLegislature(String legislature) {
        this.legislature = legislature;
        this.legislatureLabel = "la " + legislature + " législature";
    }

    public List<SelectValueDTO> getOptionsLegislatures() {
        return optionsLegislatures;
    }

    public void setOptionsLegislatures(List<SelectValueDTO> optionsLegislatures) {
        this.optionsLegislatures = optionsLegislatures;
    }

    public String getLegislatureLabel() {
        return legislatureLabel;
    }

    public void setLegislatureLabel(String legislatureLabel) {
        this.legislatureLabel = legislatureLabel;
    }

    public String getMinisterePiloteLabel() {
        return ministerePiloteLabel;
    }

    public void setMinisterePiloteLabel(String ministerePiloteLabel) {
        this.ministerePiloteLabel = ministerePiloteLabel;
    }

    public String getMinisteres() {
        return ministeres;
    }

    public void setMinisteres(String ministeres) {
        this.ministeres = ministeres;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    /*
     *
     * Convertit le DTO en map de paramétrage BIRT
     *
     */
    public Map<String, String> toBirt() {
        Map<String, String> res = new HashMap<>();

        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(BirtMapping.class)) {
                String annotName = field.getAnnotation(BirtMapping.class).birtParam().name();
                if (birtReport.getProperties().containsKey(annotName)) {
                    Object val;
                    try {
                        val = field.get(this);
                        if (val != null && Date.class.isAssignableFrom(field.getType())) {
                            val = SolonDateConverter.DATE_SLASH.format((Date) val);
                        }
                    } catch (IllegalAccessException e) {
                        val = null;
                    }
                    res.put(annotName, val != null ? val.toString() : "");
                }
            }
        }
        return res;
    }

    /*
     *
     * Convertit le DTO en DTO de fragment Thymeleaf
     *
     */
    public List<PanBirtFragmentDTO> toFragment() {
        List<PanBirtFragmentDTO> fields = new ArrayList<>();

        for (String rp : birtReport.getProperties().keySet()) {
            PanConstants.BirtParam param = PanConstants.BirtParam.findByName(rp);
            switch (Objects.requireNonNull(param)) {
                case DEBUT_INTERVALLE1_PARAM:
                    fields.add(
                        new PanBirtFragmentDTO(
                            PanBirtFieldTypeEnum.DATE_RANGE,
                            ImmutableMap.of(
                                "id",
                                "intervalle1_" + birtReport.getId(),
                                "name",
                                "intervalle1",
                                "label",
                                "pan.stats.form.intervalle." + libelleDateIntervalle1 + ".label",
                                "valuebeginning",
                                debutIntervalle1,
                                "valueending",
                                finIntervalle1
                            )
                        )
                    );
                    break;
                case DEBUT_INTERVALLE2_PARAM:
                    fields.add(
                        new PanBirtFragmentDTO(
                            PanBirtFieldTypeEnum.DATE_RANGE,
                            ImmutableMap.of(
                                "id",
                                "intervalle2_" + birtReport.getId(),
                                "name",
                                "intervalle2",
                                "label",
                                "pan.stats.form.intervalle." + libelleDateIntervalle2 + ".label",
                                "valuebeginning",
                                debutIntervalle2,
                                "valueending",
                                finIntervalle2
                            )
                        )
                    );
                    break;
                case DEBUTLEGISLATURE_PARAM:
                    fields.add(
                        new PanBirtFragmentDTO(
                            PanBirtFieldTypeEnum.DATE_SIMPLE,
                            ImmutableMap.of(
                                "id",
                                "date_" + birtReport.getId(),
                                "label",
                                "pan.stats.form.debut.legislature.label",
                                "name",
                                "debutLegislature",
                                "value",
                                debutLegislature
                            )
                        )
                    );
                    break;
                case LEGISLATURES:
                    fields.add(
                        new PanBirtFragmentDTO(
                            PanBirtFieldTypeEnum.SELECT,
                            ImmutableMap.of(
                                "id",
                                "select_" + birtReport.getId(),
                                "label",
                                "pan.stats.form.legislature.label",
                                "name",
                                "legislatures",
                                "options",
                                optionsLegislatures,
                                "value",
                                legislature
                            )
                        )
                    );
                    break;
                case ANNEE_PARAM:
                    List<SelectValueDTO> annees = new ArrayList<>();

                    for (Integer i = anneeDebut; i <= annee; ++i) {
                        annees.add(new SelectValueDTO(i.toString(), i.toString()));
                    }

                    fields.add(
                        new PanBirtFragmentDTO(
                            PanBirtFieldTypeEnum.SELECT,
                            ImmutableMap.of(
                                "id",
                                "annee",
                                "label",
                                "pan.stats.form.annee.label",
                                "name",
                                "annee",
                                "options",
                                annees,
                                "value",
                                annee.toString()
                            )
                        )
                    );
                    break;
                case MOIS_PARAM:
                    String[] monthLabels = new DateFormatSymbols().getMonths();

                    List<SelectValueDTO> optionsMois = IntStream
                        .range(1, monthLabels.length + 1)
                        .mapToObj(
                            it -> new SelectValueDTO(String.valueOf(it), StringUtils.capitalize(monthLabels[it - 1]))
                        )
                        .collect(Collectors.toList());

                    fields.add(
                        new PanBirtFragmentDTO(
                            PanBirtFieldTypeEnum.SELECT,
                            ImmutableMap.of(
                                "id",
                                "mois",
                                "label",
                                "pan.stats.form.mois.label",
                                "name",
                                "mois",
                                "options",
                                optionsMois,
                                "value",
                                mois.toString()
                            )
                        )
                    );
                    break;
                case PERIODE_PARAM:
                    List<SelectValueDTO> optionsPeriode = ImmutableList.of(
                        new SelectValueDTO("M", "pan.stats.form.periode.mois.label"),
                        new SelectValueDTO("A", "pan.stats.form.periode.annee.label")
                    );
                    fields.add(
                        new PanBirtFragmentDTO(
                            PanBirtFieldTypeEnum.RADIO,
                            ImmutableMap.of(
                                "id",
                                "periode",
                                "label",
                                "pan.stats.form.periode.label",
                                "name",
                                "periode",
                                "options",
                                optionsPeriode,
                                "value",
                                periode
                            )
                        )
                    );
                    break;
                default:
                    break;
            }
        }

        return fields;
    }
}
