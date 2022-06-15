package fr.dila.solonepg.core.export.dto;

import fr.dila.solonepg.api.cases.Dossier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ExportDossierDTO {
    private final String numeroNor;
    private final String titreActe;
    private final String ministereResp;
    private final String ministereAttache;
    private final String nomCompletRespDossier;
    private final boolean hasMinistereLabel;

    public ExportDossierDTO(
        String numeroNor,
        String titreActe,
        String ministereResp,
        String ministereAttache,
        String prenomRespDossier,
        String nomRespDossier
    ) {
        this.numeroNor = numeroNor;
        this.titreActe = titreActe;
        this.ministereResp = ministereResp;
        this.ministereAttache = ministereAttache;
        nomCompletRespDossier =
            Stream
                .of(prenomRespDossier, nomRespDossier)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(" "));
        hasMinistereLabel = true;
    }

    public ExportDossierDTO(DocumentModel dossierDoc) {
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        numeroNor = dossier.getNumeroNor();
        titreActe = dossier.getTitreActe();
        ministereResp = dossier.getMinistereResp();
        ministereAttache = dossier.getMinistereAttache();
        nomCompletRespDossier = dossier.getNomCompletRespDossier();
        hasMinistereLabel = false;
    }

    public String getNumeroNor() {
        return numeroNor;
    }

    public String getTitreActe() {
        return titreActe;
    }

    public String getMinistereResp() {
        return ministereResp;
    }

    public String getMinistereAttache() {
        return ministereAttache;
    }

    public String getNomCompletRespDossier() {
        return nomCompletRespDossier;
    }

    public boolean isHasMinistereLabel() {
        return hasMinistereLabel;
    }

    @Override
    public String toString() {
        return (
            "ExportDossierDTO{" +
            "numeroNor='" +
            numeroNor +
            '\'' +
            ", titreActe='" +
            titreActe +
            '\'' +
            ", ministereResp='" +
            ministereResp +
            '\'' +
            ", ministereAttache='" +
            ministereAttache +
            '\'' +
            ", nomCompletRespDossier='" +
            nomCompletRespDossier +
            '\'' +
            ", hasMinistereLabel=" +
            hasMinistereLabel +
            '}'
        );
    }
}
