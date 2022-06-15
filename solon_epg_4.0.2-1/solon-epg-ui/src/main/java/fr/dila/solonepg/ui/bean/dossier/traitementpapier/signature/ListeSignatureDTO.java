package fr.dila.solonepg.ui.bean.dossier.traitementpapier.signature;

import fr.dila.solonepg.api.documentmodel.ListeTraitementPapier;
import java.util.Calendar;

public class ListeSignatureDTO {
    private Long numeroListe;

    private Calendar dateGenerationListe;

    public ListeSignatureDTO() {}

    public ListeSignatureDTO(ListeTraitementPapier listeTraitementPapier) {
        numeroListe = listeTraitementPapier.getNumeroListe();
        dateGenerationListe = listeTraitementPapier.getDateGenerationListe();
    }

    public Long getNumeroListe() {
        return numeroListe;
    }

    public void setNumeroListe(Long numeroListe) {
        this.numeroListe = numeroListe;
    }

    public Calendar getDateGenerationListe() {
        return dateGenerationListe;
    }

    public void setDateGenerationListe(Calendar dateGenerationListe) {
        this.dateGenerationListe = dateGenerationListe;
    }
}
