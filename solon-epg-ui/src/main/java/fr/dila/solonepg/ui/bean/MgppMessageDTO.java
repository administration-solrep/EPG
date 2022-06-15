package fr.dila.solonepg.ui.bean;

import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.st.core.service.VocabularyServiceImpl;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.parlement.STMessageDTO;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class MgppMessageDTO extends STMessageDTO {
    private static final long serialVersionUID = 1L;

    private static final String NIV_LECTURE = "nivLecture";
    private static final String TYPE_EVENEMENT = "typeEvenement";

    public MgppMessageDTO() {
        super();
    }

    public MgppMessageDTO(MessageDTO message, Map<String, String> mapNiveauLecture, EvenementTypeDescriptor eventType) {
        setIdDossier(message.getIdDossier());
        setObjetDossier(message.getObjet());
        Integer niveauLectureNiveau = message.getNiveauLecture();
        String niveauLectureCode = VocabularyServiceImpl.UNKNOWN_ENTRY;
        if (StringUtils.isNotBlank(message.getCodeLecture())) {
            niveauLectureCode =
                mapNiveauLecture.getOrDefault(message.getCodeLecture(), VocabularyServiceImpl.UNKNOWN_ENTRY);
        }
        setNivLecture(
            String.format(
                "%s%s",
                niveauLectureNiveau == null ? "" : niveauLectureNiveau + " - ",
                VocabularyServiceImpl.UNKNOWN_ENTRY.equals(niveauLectureCode) ? "" : niveauLectureCode
            )
        );
        setEmetteur(Optional.ofNullable(message.getEmetteurEvenement()).map(ResourceHelper::getString).orElse(""));
        setDestinataire(
            Optional.ofNullable(message.getDestinataireEvenement()).map(ResourceHelper::getString).orElse("")
        );
        setCopie(message.getCopieEvenement().stream().map(ResourceHelper::getString).collect(Collectors.joining(", ")));
        setTypeEvenement(message.getTypeEvenement());
        setCommunication(
            Optional.ofNullable(eventType).map(EvenementTypeDescriptor::getLabel).orElse(message.getTypeEvenement())
        );
        setDate(SolonDateConverter.DATETIME_SLASH_MINUTE_COLON.format(message.getDateEvenement()));
        setId(message.getIdEvenement());

        setEtatMessage(message.getEtatMessage());
        setEtatEvenement(message.getEtatEvenement());
        setPieceJointe(message.isPresencePieceJointe());
        setEnAlerte(message.getTypeEvenement().startsWith("ALERTE"));
        setDossierEnAlerte("ALERTE".equals(message.getEtatDossier()));
        setEnAttente(message.isEnAttente());
    }

    public String getNivLecture() {
        return getString(NIV_LECTURE);
    }

    public void setNivLecture(String nivLecture) {
        put(NIV_LECTURE, nivLecture);
    }

    public Boolean isEnAttente() {
        return getBoolean(MessageDTO.EN_ATTENTE);
    }

    public void setEnAttente(Boolean enAttente) {
        put(MessageDTO.EN_ATTENTE, enAttente);
    }

    public String getTypeEvenement() {
        return getString(TYPE_EVENEMENT);
    }

    public void setTypeEvenement(String typeEvenement) {
        put(TYPE_EVENEMENT, typeEvenement);
    }
}
