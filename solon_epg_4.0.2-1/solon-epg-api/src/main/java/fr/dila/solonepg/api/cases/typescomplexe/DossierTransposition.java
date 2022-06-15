package fr.dila.solonepg.api.cases.typescomplexe;

import fr.dila.st.api.domain.ComplexeType;

public interface DossierTransposition extends ComplexeType, DossierTranspositionImmutable {
    void setRef(String ref);

    void setTitre(String titre);

    void setNumeroArticles(String numeroArticles);

    void setRefMesure(String refMesure);

    void setCommentaire(String commentaire);
}
