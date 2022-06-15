package fr.dila.solonepg.api.cases.typescomplexe;

import fr.dila.st.api.domain.ComplexeType;
import java.util.Calendar;

/**
 * type complexe representant une ligne dans le tableau de programmation du suivi des habilitations de l'activite normative
 *
 * @author asatre
 *
 */
public interface LigneProgrammationHabilitation extends ComplexeType {
    String getNumeroOrdre();

    void setNumeroOrdre(String numeroOrdre);

    String getMinisterePilote();

    void setMinisterePilote(String ministerePilote);

    Boolean getCodification();

    void setCodification(Boolean codification);

    String getArticle();

    void setArticle(String article);

    String getObjetRIM();

    void setObjetRIM(String objetRIM);

    String getTypeHabilitation();

    void setTypeHabilitation(String typeHabilitation);

    String getConvention();

    void setConvention(String convention);

    Calendar getDateTerme();

    void setDateTerme(Calendar dateTerme);

    String getConventionDepot();

    void setConventionDepot(String conventionDepot);

    Calendar getDatePrevisionnelleSaisineCE();

    void setDatePrevisionnelleSaisineCE(Calendar datePrevisionnelleSaisineCE);

    Calendar getDatePrevisionnelleCM();

    void setDatePrevisionnelleCM(Calendar datePrevisionnelleCM);

    String getObservation();

    void setObservation(String observation);

    String getLegislature();

    void setLegislature(String legislature);
}
