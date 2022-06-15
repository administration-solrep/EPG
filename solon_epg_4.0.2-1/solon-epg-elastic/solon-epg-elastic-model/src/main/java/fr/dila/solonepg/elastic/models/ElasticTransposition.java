package fr.dila.solonepg.elastic.models;

import fr.dila.solonepg.api.cases.typescomplexe.DossierTransposition;

public interface ElasticTransposition {
    String getNumeroArticle();

    void setNumeroArticle(String numeroArticle);

    String getRef();

    void setRef(String ref);

    String getRefMesure();

    void setRefMesure(String refMesure);

    String getTitre();

    void setTitre(String titre);

    default ElasticTransposition mapTransposition(DossierTransposition transposition) {
        this.setNumeroArticle(transposition.getNumeroArticles());
        this.setRef(transposition.getRef());
        this.setRefMesure(transposition.getRefMesure());
        this.setTitre(transposition.getTitre());

        mapSpecific(transposition);
        return this;
    }

    void mapSpecific(DossierTransposition transposition);
}
