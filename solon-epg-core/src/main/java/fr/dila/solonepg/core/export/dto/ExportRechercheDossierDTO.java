package fr.dila.solonepg.core.export.dto;

import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SolonDateConverter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public class ExportRechercheDossierDTO {
    private static final Map<String, String> USER_FULLNAMES = new HashMap<>();

    private final String numeroNor;
    private final String titreActe;
    private final String dateCreation;
    private final String dernierContributeur;
    private final String creePar;
    private final String statut;
    private final String typeActe;

    public ExportRechercheDossierDTO(
        String numeroNor,
        String titreActe,
        Date dateCreation,
        String dernierContributeurId,
        String creeParId,
        String statut,
        String typeActe
    ) {
        this.numeroNor = numeroNor;
        this.titreActe = titreActe;
        this.dateCreation = SolonDateConverter.DATE_SLASH.format(dateCreation);
        this.dernierContributeur = getUserFullname(dernierContributeurId);
        this.creePar = getUserFullname(creeParId);
        this.statut = statut;
        this.typeActe = typeActe;
    }

    public String getNumeroNor() {
        return numeroNor;
    }

    public String getTitreActe() {
        return titreActe;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDernierContributeur() {
        return dernierContributeur;
    }

    public String getCreePar() {
        return creePar;
    }

    public String getStatut() {
        return statut;
    }

    public String getTypeActe() {
        return typeActe;
    }

    private static String getUserFullname(String username) {
        String userFullname = "";
        if (StringUtils.isNotBlank(username)) {
            userFullname =
                Optional
                    .ofNullable(USER_FULLNAMES.get(username))
                    .orElseGet(
                        () -> {
                            String fullname = STServiceLocator.getSTUserService().getUserFullName(username);
                            USER_FULLNAMES.put(username, fullname);
                            return fullname;
                        }
                    );
        }
        return userFullname;
    }

    @Override
    public String toString() {
        return (
            "ExportRechercheDossierDTO{" +
            "numeroNor='" +
            numeroNor +
            '\'' +
            ", titreActe='" +
            titreActe +
            '\'' +
            ", dateCreation='" +
            dateCreation +
            '\'' +
            ", dernierContributeur='" +
            dernierContributeur +
            '\'' +
            ", creePar='" +
            creePar +
            '\'' +
            ", statut='" +
            statut +
            '\'' +
            ", typeActe=" +
            typeActe +
            '}'
        );
    }
}
