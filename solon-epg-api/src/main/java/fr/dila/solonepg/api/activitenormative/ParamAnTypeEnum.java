package fr.dila.solonepg.api.activitenormative;

import fr.dila.solonepg.api.administration.ParametrageAN;
import java.util.Date;
import java.util.function.Function;

/**
 * Enumeration des types de paramètres à appliquer sur un rapport.
 */
public enum ParamAnTypeEnum {
    NONE(new Builder()),
    /** Application des lois - bilans semestriels */
    APPLI_LOIS_BILANS_SEMESTRIELS(
        new Builder()
            .setDebutPromulgationCurLegisSupplier(ParametrageAN::getLECBSDatePromulBorneInf)
            .setDebutPromulgationPrevLegisSupplier(ParametrageAN::getLPBSDatePromulBorneInf)
            .setFinPromulgationCurLegisSupplier(ParametrageAN::getLECBSDatePromulBorneSup)
            .setFinPromulgationPrevLegisSupplier(ParametrageAN::getLPBSDatePromulBorneSup)
            .setDebutPublicationCurLegisSupplier(ParametrageAN::getLECBSDatePubliBorneInf)
            .setDebutPublicationPrevLegisSupplier(ParametrageAN::getLPBSDatePubliBorneInf)
            .setFinPublicationCurLegisSupplier(ParametrageAN::getLECBSDatePubliBorneSup)
            .setFinPublicationPrevLegisSupplier(ParametrageAN::getLPBSDatePubliBorneSup)
    ),
    /** Application des ordonnances - bilans semestriels */
    APPLI_ORDO_BILANS_SEMESTRIELS(
        new Builder()
            .setDebutPromulgationCurLegisSupplier(ParametrageAN::getLECBSDatePubliOrdoBorneInf)
            .setDebutPromulgationPrevLegisSupplier(ParametrageAN::getLPBSDatePubliOrdoBorneInf)
            .setFinPromulgationCurLegisSupplier(ParametrageAN::getLECBSDatePubliOrdoBorneSup)
            .setFinPromulgationPrevLegisSupplier(ParametrageAN::getLPBSDatePubliOrdoBorneSup)
            .setDebutPublicationCurLegisSupplier(ParametrageAN::getLECBSDatePubliDecretOrdoBorneInf)
            .setDebutPublicationPrevLegisSupplier(ParametrageAN::getLPBSDatePubliDecretOrdoBorneInf)
            .setFinPublicationCurLegisSupplier(ParametrageAN::getLECBSDatePubliDecretOrdoBorneSup)
            .setFinPublicationPrevLegisSupplier(ParametrageAN::getLPBSDatePubliDecretOrdoBorneSup)
    ),
    /** Application des lois - Taux d'exécution depuis législature */
    APPLI_LOIS_TAUX_EXEC(
        new Builder()
            .setDebutPromulgationCurLegisSupplier(ParametrageAN::getLECTauxDebutLegisDatePromulBorneInf)
            .setDebutPromulgationPrevLegisSupplier(ParametrageAN::getLPTauxDebutLegisDatePromulBorneInf)
            .setFinPromulgationCurLegisSupplier(ParametrageAN::getLECTauxDebutLegisDatePromulBorneSup)
            .setFinPromulgationPrevLegisSupplier(ParametrageAN::getLPTauxDebutLegisDatePromulBorneSup)
            .setDebutPublicationCurLegisSupplier(ParametrageAN::getLECTauxDebutLegisDatePubliBorneInf)
            .setDebutPublicationPrevLegisSupplier(ParametrageAN::getLPTauxDebutLegisDatePubliBorneInf)
            .setFinPublicationCurLegisSupplier(ParametrageAN::getLECTauxDebutLegisDatePubliBorneSup)
            .setFinPublicationPrevLegisSupplier(ParametrageAN::getLPTauxDebutLegisDatePubliBorneSup)
    ),
    /** Application des ordonnances - Taux d'exécution depuis législature */
    APPLI_ORDOS_TAUX_EXEC(
        new Builder()
            .setDebutPromulgationCurLegisSupplier(ParametrageAN::getLECTauxDebutLegisDatePubliOrdoBorneInf)
            .setDebutPromulgationPrevLegisSupplier(ParametrageAN::getLPTauxDebutLegisDatePubliOrdoBorneInf)
            .setFinPromulgationCurLegisSupplier(ParametrageAN::getLECTauxDebutLegisDatePubliOrdoBorneSup)
            .setFinPromulgationPrevLegisSupplier(ParametrageAN::getLPTauxDebutLegisDatePubliOrdoBorneSup)
            .setDebutPublicationCurLegisSupplier(ParametrageAN::getLECTauxDebutLegisDatePubliDecretOrdoBorneInf)
            .setDebutPublicationPrevLegisSupplier(ParametrageAN::getLPTauxDebutLegisDatePubliDecretOrdoBorneInf)
            .setFinPublicationCurLegisSupplier(ParametrageAN::getLECTauxDebutLegisDatePubliDecretOrdoBorneSup)
            .setFinPublicationPrevLegisSupplier(ParametrageAN::getLPTauxDebutLegisDatePubliDecretOrdoBorneSup)
    ),
    /** Législatures - dates début */
    DATE_DEBUT_LEGIS(
        new Builder()
            .setDebutCurLegislatureSupplier(ParametrageAN::getLegislatureEnCoursDateDebut)
            .setDebutPrevLegislatureSupplier(ParametrageAN::getLegislaturePrecedenteDateDebut)
    ),
    /** Application des lois - Taux d'exécution session parlementaire */
    APPLI_LOIS_TAUX_EXEC_SESSION_PARLEMENTAIRE(
        new Builder()
            .setDebutPromulgationCurLegisSupplier(ParametrageAN::getLECTauxSPDatePromulBorneInf)
            .setDebutPromulgationPrevLegisSupplier(ParametrageAN::getLPTauxSPDatePromulBorneInf)
            .setFinPromulgationCurLegisSupplier(ParametrageAN::getLECTauxSPDatePromulBorneSup)
            .setFinPromulgationPrevLegisSupplier(ParametrageAN::getLPTauxSPDatePromulBorneSup)
            .setDebutPublicationCurLegisSupplier(ParametrageAN::getLECTauxSPDatePubliBorneInf)
            .setDebutPublicationPrevLegisSupplier(ParametrageAN::getLPTauxSPDatePubliBorneInf)
            .setFinPublicationCurLegisSupplier(ParametrageAN::getLECTauxSPDatePubliBorneSup)
            .setFinPublicationPrevLegisSupplier(ParametrageAN::getLPTauxSPDatePubliBorneSup)
    ),
    /** Application des ordonnances - Taux d'exécution session parlementaire */
    APPLI_ORDOS_TAUX_EXEC_SESSION_PARLEMENTAIRE(
        new Builder()
            .setDebutPromulgationCurLegisSupplier(ParametrageAN::getLECTauxSPDatePubliOrdoBorneInf)
            .setDebutPromulgationPrevLegisSupplier(ParametrageAN::getLPTauxSPDatePubliOrdoBorneInf)
            .setFinPromulgationCurLegisSupplier(ParametrageAN::getLECTauxSPDatePubliOrdoBorneSup)
            .setFinPromulgationPrevLegisSupplier(ParametrageAN::getLPTauxSPDatePubliOrdoBorneSup)
            .setDebutPublicationCurLegisSupplier(ParametrageAN::getLECTauxSPDatePubliDecretOrdoBorneInf)
            .setDebutPublicationPrevLegisSupplier(ParametrageAN::getLPTauxSPDatePubliDecretOrdoBorneInf)
            .setFinPublicationCurLegisSupplier(ParametrageAN::getLECTauxSPDatePubliDecretOrdoBorneSup)
            .setFinPublicationPrevLegisSupplier(ParametrageAN::getLPTauxSPDatePubliDecretOrdoBorneSup)
    );

    private final Function<ParametrageAN, Date> debutPromulgationCurLegisSupplier;
    private final Function<ParametrageAN, Date> debutPromulgationPrevLegisSupplier;

    private final Function<ParametrageAN, Date> finPromulgationCurLegisSupplier;
    private final Function<ParametrageAN, Date> finPromulgationPrevLegisSupplier;

    private final Function<ParametrageAN, Date> debutPublicationCurLegisSupplier;
    private final Function<ParametrageAN, Date> debutPublicationPrevLegisSupplier;

    private final Function<ParametrageAN, Date> finPublicationCurLegisSupplier;
    private final Function<ParametrageAN, Date> finPublicationPrevLegisSupplier;

    private final Function<ParametrageAN, Date> debutCurLegislatureSupplier;
    private final Function<ParametrageAN, Date> debutPrevLegislatureSupplier;

    ParamAnTypeEnum(Builder builder) {
        this.debutPromulgationCurLegisSupplier = builder.debutPromulgationCurLegisSupplier;
        this.debutPromulgationPrevLegisSupplier = builder.debutPromulgationPrevLegisSupplier;

        this.finPromulgationCurLegisSupplier = builder.finPromulgationCurLegisSupplier;
        this.finPromulgationPrevLegisSupplier = builder.finPromulgationPrevLegisSupplier;

        this.debutPublicationCurLegisSupplier = builder.debutPublicationCurLegisSupplier;
        this.debutPublicationPrevLegisSupplier = builder.debutPublicationPrevLegisSupplier;

        this.finPublicationCurLegisSupplier = builder.finPublicationCurLegisSupplier;
        this.finPublicationPrevLegisSupplier = builder.finPublicationPrevLegisSupplier;

        this.debutCurLegislatureSupplier = builder.debutCurLegislatureSupplier;
        this.debutPrevLegislatureSupplier = builder.debutPrevLegislatureSupplier;
    }

    private static class Builder {
        private Function<ParametrageAN, Date> debutPromulgationCurLegisSupplier = paramAN -> null;
        private Function<ParametrageAN, Date> debutPromulgationPrevLegisSupplier = paramAN -> null;

        private Function<ParametrageAN, Date> finPromulgationCurLegisSupplier = paramAN -> null;
        private Function<ParametrageAN, Date> finPromulgationPrevLegisSupplier = paramAN -> null;

        private Function<ParametrageAN, Date> debutPublicationCurLegisSupplier = paramAN -> null;
        private Function<ParametrageAN, Date> debutPublicationPrevLegisSupplier = paramAN -> null;

        private Function<ParametrageAN, Date> finPublicationCurLegisSupplier = paramAN -> null;
        private Function<ParametrageAN, Date> finPublicationPrevLegisSupplier = paramAN -> null;

        private Function<ParametrageAN, Date> debutCurLegislatureSupplier = paramAN -> null;
        private Function<ParametrageAN, Date> debutPrevLegislatureSupplier = paramAN -> null;

        public Builder setDebutPromulgationCurLegisSupplier(
            Function<ParametrageAN, Date> debutPromulgationCurLegisSupplier
        ) {
            this.debutPromulgationCurLegisSupplier = debutPromulgationCurLegisSupplier;
            return this;
        }

        public Builder setDebutPromulgationPrevLegisSupplier(
            Function<ParametrageAN, Date> debutPromulgationPrevLegisSupplier
        ) {
            this.debutPromulgationPrevLegisSupplier = debutPromulgationPrevLegisSupplier;
            return this;
        }

        public Builder setFinPromulgationCurLegisSupplier(
            Function<ParametrageAN, Date> finPromulgationCurLegisSupplier
        ) {
            this.finPromulgationCurLegisSupplier = finPromulgationCurLegisSupplier;
            return this;
        }

        public Builder setFinPromulgationPrevLegisSupplier(
            Function<ParametrageAN, Date> finPromulgationPrevLegisSupplier
        ) {
            this.finPromulgationPrevLegisSupplier = finPromulgationPrevLegisSupplier;
            return this;
        }

        public Builder setDebutPublicationCurLegisSupplier(
            Function<ParametrageAN, Date> debutPublicationCurLegisSupplier
        ) {
            this.debutPublicationCurLegisSupplier = debutPublicationCurLegisSupplier;
            return this;
        }

        public Builder setDebutPublicationPrevLegisSupplier(
            Function<ParametrageAN, Date> debutPublicationPrevLegisSupplier
        ) {
            this.debutPublicationPrevLegisSupplier = debutPublicationPrevLegisSupplier;
            return this;
        }

        public Builder setFinPublicationCurLegisSupplier(Function<ParametrageAN, Date> finPublicationCurLegisSupplier) {
            this.finPublicationCurLegisSupplier = finPublicationCurLegisSupplier;
            return this;
        }

        public Builder setFinPublicationPrevLegisSupplier(
            Function<ParametrageAN, Date> finPublicationPrevLegisSupplier
        ) {
            this.finPublicationPrevLegisSupplier = finPublicationPrevLegisSupplier;
            return this;
        }

        public Builder setDebutCurLegislatureSupplier(Function<ParametrageAN, Date> debutCurLegislatureSupplier) {
            this.debutCurLegislatureSupplier = debutCurLegislatureSupplier;
            return this;
        }

        public Builder setDebutPrevLegislatureSupplier(Function<ParametrageAN, Date> debutPrevLegislatureSupplier) {
            this.debutPrevLegislatureSupplier = debutPrevLegislatureSupplier;
            return this;
        }
    }

    public Date getDebutLegislatureDate(boolean curLegis, ParametrageAN paramAn) {
        return curLegis ? debutCurLegislatureSupplier.apply(paramAn) : debutPrevLegislatureSupplier.apply(paramAn);
    }

    public Date getFinPublicationDate(boolean curLegis, ParametrageAN paramAn) {
        return curLegis
            ? finPublicationCurLegisSupplier.apply(paramAn)
            : finPublicationPrevLegisSupplier.apply(paramAn);
    }

    public Date getDebutPublicationDate(boolean curLegis, ParametrageAN paramAn) {
        return curLegis
            ? debutPublicationCurLegisSupplier.apply(paramAn)
            : debutPublicationPrevLegisSupplier.apply(paramAn);
    }

    public Date getFinPromulgationDate(boolean curLegis, ParametrageAN paramAn) {
        return curLegis
            ? finPromulgationCurLegisSupplier.apply(paramAn)
            : finPromulgationPrevLegisSupplier.apply(paramAn);
    }

    public Date getDebutPromulgationDate(boolean curLegis, ParametrageAN paramAn) {
        return curLegis
            ? debutPromulgationCurLegisSupplier.apply(paramAn)
            : debutPromulgationPrevLegisSupplier.apply(paramAn);
    }
}
