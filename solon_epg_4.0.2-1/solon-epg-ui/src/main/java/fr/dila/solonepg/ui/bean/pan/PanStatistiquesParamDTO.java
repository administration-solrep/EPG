package fr.dila.solonepg.ui.bean.pan;

import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class PanStatistiquesParamDTO {

    public static class PanLegislatureDTO {
        private Date debutLegislature;
        private String libelle;
        private Pair<Date, Date> promulgationLoisBS;
        private Pair<Date, Date> publicationDecretsLoisBS;
        private Pair<Date, Date> promulgationOrdonnancesBS;
        private Pair<Date, Date> publicationDecretsOrdonnancesBS;
        private Pair<Date, Date> promulgationLoisTXD;
        private Pair<Date, Date> publicationDecretsLoisTXD;
        private Pair<Date, Date> promulgationLoisTX;
        private Pair<Date, Date> publicationDecretsLoisTX;
        private Pair<Date, Date> promulgationOrdonnancesTXD;
        private Pair<Date, Date> publicationDecretsOrdonnancesTXD;
        private Pair<Date, Date> promulgationOrdonnancesTX;
        private Pair<Date, Date> publicationDecretsOrdonnancesTX;

        public PanLegislatureDTO(String libelle) {
            this.libelle = libelle;
        }

        public Date getDebutLegislature() {
            return debutLegislature;
        }

        public String getLibelle() {
            return libelle;
        }

        public void setLibelle(String libelle) {
            this.libelle = libelle;
        }

        public void setDebutLegislature(Date debutLegislature) {
            this.debutLegislature = debutLegislature;
        }

        public Pair<Date, Date> getPromulgationLoisBS() {
            return promulgationLoisBS;
        }

        public void setPromulgationLoisBS(Pair<Date, Date> promulgationLoisBS) {
            this.promulgationLoisBS = promulgationLoisBS;
        }

        public Pair<Date, Date> getPublicationDecretsLoisBS() {
            return publicationDecretsLoisBS;
        }

        public void setPublicationDecretsLoisBS(Pair<Date, Date> publicationDecretsLoisBS) {
            this.publicationDecretsLoisBS = publicationDecretsLoisBS;
        }

        public Pair<Date, Date> getPromulgationOrdonnancesBS() {
            return promulgationOrdonnancesBS;
        }

        public void setPromulgationOrdonnancesBS(Pair<Date, Date> promulgationOrdonnancesBS) {
            this.promulgationOrdonnancesBS = promulgationOrdonnancesBS;
        }

        public Pair<Date, Date> getPublicationDecretsOrdonnancesBS() {
            return publicationDecretsOrdonnancesBS;
        }

        public void setPublicationDecretsOrdonnancesBS(Pair<Date, Date> publicationDecretsOrdonnancesBS) {
            this.publicationDecretsOrdonnancesBS = publicationDecretsOrdonnancesBS;
        }

        public Pair<Date, Date> getPromulgationLoisTXD() {
            return promulgationLoisTXD;
        }

        public void setPromulgationLoisTXD(Pair<Date, Date> promulgationLoisTXD) {
            this.promulgationLoisTXD = promulgationLoisTXD;
        }

        public Pair<Date, Date> getPublicationDecretsLoisTXD() {
            return publicationDecretsLoisTXD;
        }

        public void setPublicationDecretsLoisTXD(Pair<Date, Date> publicationDecretsLoisTXD) {
            this.publicationDecretsLoisTXD = publicationDecretsLoisTXD;
        }

        public Pair<Date, Date> getPromulgationLoisTX() {
            return promulgationLoisTX;
        }

        public void setPromulgationLoisTX(Pair<Date, Date> promulgationLoisTX) {
            this.promulgationLoisTX = promulgationLoisTX;
        }

        public Pair<Date, Date> getPublicationDecretsLoisTX() {
            return publicationDecretsLoisTX;
        }

        public void setPublicationDecretsLoisTX(Pair<Date, Date> publicationDecretsLoisTX) {
            this.publicationDecretsLoisTX = publicationDecretsLoisTX;
        }

        public Pair<Date, Date> getPromulgationOrdonnancesTXD() {
            return promulgationOrdonnancesTXD;
        }

        public void setPromulgationOrdonnancesTXD(Pair<Date, Date> promulgationOrdonnancesTXD) {
            this.promulgationOrdonnancesTXD = promulgationOrdonnancesTXD;
        }

        public Pair<Date, Date> getPublicationDecretsOrdonnancesTXD() {
            return publicationDecretsOrdonnancesTXD;
        }

        public void setPublicationDecretsOrdonnancesTXD(Pair<Date, Date> publicationDecretsOrdonnancesTXD) {
            this.publicationDecretsOrdonnancesTXD = publicationDecretsOrdonnancesTXD;
        }

        public Pair<Date, Date> getPromulgationOrdonnancesTX() {
            return promulgationOrdonnancesTX;
        }

        public void setPromulgationOrdonnancesTX(Pair<Date, Date> promulgationOrdonnancesTX) {
            this.promulgationOrdonnancesTX = promulgationOrdonnancesTX;
        }

        public Pair<Date, Date> getPublicationDecretsOrdonnancesTX() {
            return publicationDecretsOrdonnancesTX;
        }

        public void setPublicationDecretsOrdonnancesTX(Pair<Date, Date> publicationDecretsOrdonnancesTX) {
            this.publicationDecretsOrdonnancesTX = publicationDecretsOrdonnancesTX;
        }
    }

    private List<SelectValueDTO> legislatures;
    private List<String> legislaturesRaw;
    private String legislatureEnCours;
    private PanLegislatureDTO legisEnCours;
    private PanLegislatureDTO legisPrec;

    private static final String LIBELLE_EN_COURS = "EnCours";
    private static final String LIBELLE_PRECED = "Precedente";

    public PanStatistiquesParamDTO() {
        legisEnCours = new PanLegislatureDTO(LIBELLE_EN_COURS);
        legisPrec = new PanLegislatureDTO(LIBELLE_PRECED);
    }

    public List<SelectValueDTO> getLegislatures() {
        return legislatures;
    }

    public void setLegislatures(List<SelectValueDTO> legislatures) {
        this.legislatures = legislatures;
    }

    public List<String> getLegislaturesRaw() {
        return legislaturesRaw;
    }

    public void setLegislaturesRaw(List<String> legislaturesRaw) {
        this.legislaturesRaw = legislaturesRaw;
    }

    public String getLegislatureEnCours() {
        return legislatureEnCours;
    }

    public void setLegislatureEnCours(String legislatureEnCours) {
        this.legislatureEnCours = legislatureEnCours;
    }

    public PanLegislatureDTO getLegisEnCours() {
        return legisEnCours;
    }

    public void setLegisEnCours(PanLegislatureDTO legisEnCours) {
        this.legisEnCours = legisEnCours;
    }

    public PanLegislatureDTO getLegisPrec() {
        return legisPrec;
    }

    public void setLegisPrec(PanLegislatureDTO legisPrec) {
        this.legisPrec = legisPrec;
    }
}
