package fr.dila.solonepg.ui.bean;

import fr.dila.st.ui.bean.WidgetDTO;
import java.util.ArrayList;
import java.util.List;

public class MgppRechercheDynamique {
    private List<WidgetDTO> metadonneesTechniques = new ArrayList<>();
    private List<WidgetDTO> metadonneesDistribution = new ArrayList<>();
    private List<WidgetDTO> metadonneesPublication = new ArrayList<>();
    private List<WidgetDTO> autresMetadonnees = new ArrayList<>();
    private List<WidgetDTO> piecesJointes = new ArrayList<>();

    public List<WidgetDTO> getMetadonneesTechniques() {
        return metadonneesTechniques;
    }

    public void setMetadonneesTechniques(List<WidgetDTO> metadonneesTechniques) {
        this.metadonneesTechniques = metadonneesTechniques;
    }

    public List<WidgetDTO> getMetadonneesDistribution() {
        return metadonneesDistribution;
    }

    public void setMetadonneesDistribution(List<WidgetDTO> metadonneesDistribution) {
        this.metadonneesDistribution = metadonneesDistribution;
    }

    public List<WidgetDTO> getAutresMetadonnees() {
        return autresMetadonnees;
    }

    public void setAutresMetadonnees(List<WidgetDTO> autresMetadonnees) {
        this.autresMetadonnees = autresMetadonnees;
    }

    public List<WidgetDTO> getPiecesJointes() {
        return piecesJointes;
    }

    public void setPiecesJointes(List<WidgetDTO> piecesJointes) {
        this.piecesJointes = piecesJointes;
    }

    public List<WidgetDTO> getMetadonneesPublication() {
        return metadonneesPublication;
    }

    public void setMetadonneesPublication(List<WidgetDTO> metadonneesPublication) {
        this.metadonneesPublication = metadonneesPublication;
    }
}
