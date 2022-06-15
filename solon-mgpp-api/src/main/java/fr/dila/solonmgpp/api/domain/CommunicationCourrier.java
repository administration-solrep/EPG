package fr.dila.solonmgpp.api.domain;

import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface CommunicationCourrier {
    DocumentModel getDocument();

    String getAuteurLex01();

    void setAuteurLex01(String value);

    List<String> getSignatureSGG();

    void setSignatureSGG(List<String> value);

    String getSignataireSGG();

    void setSignataireSgg(String signataire);

    String getSignataireAdjointSGG();

    void setSignataireAdjointSgg(String signataire);

    String getNomAN();

    void setNomAN(String value);

    String getNomSenat();

    void setNomSenat(String value);

    String getAdopteLe1ereLectureAN();

    void setAdopteLe1ereLectureAN(String value);

    String getAdopteLe1ereLectureSenat();

    void setAdopteLe1ereLectureSenat(String value);

    String getAdopteLe2emeLectureAN();

    void setAdopteLe2emeLectureAN(String value);

    String getAdopteLe2emeLectureSenat();

    void setAdopteLe2emeLectureSenat(String value);

    String getAdopteLeNouvelleLectureAN();

    void setAdopteLeNouvelleLectureAN(String value);

    String getAdopteLeNouvelleLectureSenat();

    void setAdopteLeNouvelleLectureSenat(String value);

    String getRejeteLe1ereLectureAN();

    void setRejeteLe1ereLectureAN(String value);

    String getRejeteLe1ereLectureSenat();

    void setRejeteLe1ereLectureSenat(String value);

    String getRejeteLe2emeLectureAN();

    void setRejeteLe2emeLectureAN(String value);

    String getRejeteLe2emeLectureSenat();

    void setRejeteLe2emeLectureSenat(String value);

    String getRejeteLeNouvelleLectureAN();

    void setRejeteLeNouvelleLectureAN(String value);

    String getRejeteLeNouvelleLectureSenat();

    void setRejeteLeNouvelleLectureSenat(String value);

    String getTransmisLeNouvelleLectureAN();

    void setTransmisLeNouvelleLectureAN(String value);

    String getTransmisLeNouvelleLectureSenat();

    void setTransmisLeNouvelleLectureSenat(String value);

    String getAssembleDepot();

    void setAssembleDepot(String value);

    String getDestinataireCourrier();

    void setDestinataireCourrier(String value);

    List<String> getCoauteursLEX01();

    void setCoauteursLEX01(List<String> value);

    String getDateConseilMinistres();

    void setDateConseilMinistres(String value);

    String getDateDepotFicheLoi();

    void setDateDepotFicheLoi(String value);

    String getIntituleDerniereCommunication();

    void setIntituleDerniereCommunication(String value);

    String getIntitulePremiereCommunication();

    void setIntitulePremiereCommunication(String value);

    String getNavetteLe1ereLectureAN();

    void setNavetteLe1ereLectureAN(String value);

    String getNavetteLe1ereLectureSenat();

    void setNavetteLe1ereLectureSenat(String value);

    String getSortAdoptionAN();

    void setSortAdoptionAN(String value);

    String getAuteur();

    void setAuteur(String value);

    String getDateDuJour();

    void setDateDuJour(String value);

    String getNor();

    void setNor(String value);

    String getTitreActe();

    void setTitreActe(String value);

    String getMinistereResponsableDossier();

    void setMinistereResponsableDossier(String value);
}
