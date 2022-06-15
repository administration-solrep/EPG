package fr.dila.solonmgpp.core.builder;

import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.MgppPieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.dto.MgppPieceJointeDTOImpl;
import fr.dila.solonmgpp.core.dto.PieceJointeFichierDTOImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.StringHelper;
import fr.sword.xsd.solon.epp.CompressionFichier;
import fr.sword.xsd.solon.epp.ContenuFichier;
import fr.sword.xsd.solon.epp.EppBaseEvenement;
import fr.sword.xsd.solon.epp.PieceJointe;
import fr.sword.xsd.solon.epp.PieceJointeType;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;

public class PieceJointeBuilder {
    public static final String LIBELLE = "libelle";
    public static final String ID_INTERNE_EPP = "idInterneEpp";
    public static final String TYPE = "type";
    public static final String URL = "url";
    public static final String FICHIER = "fichier";
    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(PieceJointeBuilder.class);
    private static final String MAJ_SPLIT_REGEX = "(?=[A-Z])";

    private static PieceJointeBuilder instance;

    private PieceJointeBuilder() {
        // default private constructor
    }

    public static PieceJointeBuilder getInstance() {
        if (instance == null) {
            instance = new PieceJointeBuilder();
        }

        return instance;
    }

    public void buildPieceJointeWithEppEvt(
        EvenementDTO evenementDTO,
        EppBaseEvenement eppBaseEvenement,
        CoreSession session
    ) {
        // on remplit le DTO avec la classe de base (EppEvt01, EppEvt02, ...)
        for (Field field : eppBaseEvenement.getClass().getDeclaredFields()) {
            buildPieceJointeDTOFromField(evenementDTO, field, session);
        }

        // on remplit le DTO avec la superclass (EppBaseEvenement)
        for (Field field : eppBaseEvenement.getClass().getSuperclass().getDeclaredFields()) {
            buildPieceJointeDTOFromField(evenementDTO, field, session);
        }
    }

    @SuppressWarnings("unchecked")
    private void buildPieceJointeDTOFromField(EvenementDTO evenementDTO, Field field, CoreSession session) {
        try {
            // traite que les protected
            if (Modifier.isProtected(field.getModifiers())) {
                field.setAccessible(true);
                if (PieceJointe.class.isAssignableFrom(field.getType())) {
                    String correctType = getTypePieceJointeFromType(field.getName());
                    List<MgppPieceJointeDTO> list = buildPieceJointeDTO(
                        (Map<String, Serializable>) evenementDTO.get(field.getName()),
                        correctType
                    );
                    replaceOldMap(evenementDTO, field, list, correctType);
                } else if (List.class.isAssignableFrom(field.getType())) {
                    Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    if (PieceJointe.class.isAssignableFrom(((Class<?>) type))) {
                        String correctType = getTypePieceJointeFromType(field.getName());
                        List<MgppPieceJointeDTO> list = buildListPieceJointeDTO(
                            (List<Map<String, Serializable>>) evenementDTO.get(field.getName()),
                            correctType
                        );
                        replaceOldMap(evenementDTO, field, list, correctType);
                    }
                }
            }
        } catch (Exception e) {
            this.logDebug(session, e);
        } finally {
            field.setAccessible(false);
        }
    }

    private void replaceOldMap(
        EvenementDTO evenementDTO,
        Field field,
        List<MgppPieceJointeDTO> list,
        String correctType
    ) {
        // remove old map
        evenementDTO.remove(field.getName());
        // set new map
        evenementDTO.put(correctType, (Serializable) list);
    }

    private List<MgppPieceJointeDTO> buildListPieceJointeDTO(
        List<Map<String, Serializable>> mapPieceJointe,
        String correctType
    ) {
        List<MgppPieceJointeDTO> list = new ArrayList<>();

        if (mapPieceJointe != null) {
            for (Map<String, Serializable> map : mapPieceJointe) {
                list.addAll(buildPieceJointeDTO(map, correctType));
            }
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    private List<MgppPieceJointeDTO> buildPieceJointeDTO(Map<String, Serializable> mapPieceJointe, String correctType) {
        List<MgppPieceJointeDTO> list = new ArrayList<>();

        if (StringUtils.isBlank((String) mapPieceJointe.get(ID_INTERNE_EPP))) {
            // pas d'id pas de piecejointe
            return list;
        }

        MgppPieceJointeDTO pieceJointeDTO = new MgppPieceJointeDTOImpl();
        pieceJointeDTO.setUrl((String) mapPieceJointe.get(URL));
        pieceJointeDTO.setLibelle((String) mapPieceJointe.get(LIBELLE));
        pieceJointeDTO.setIdInterneEpp((String) mapPieceJointe.get(ID_INTERNE_EPP));

        List<PieceJointeFichierDTO> listPJF = new ArrayList<>();
        if (mapPieceJointe.get(FICHIER) != null) {
            // remap de fichier
            for (Map<String, Serializable> mapFichier : (List<Map<String, Serializable>>) mapPieceJointe.get(FICHIER)) {
                PieceJointeFichierDTO pieceJointeFichierDTO = new PieceJointeFichierDTOImpl();
                pieceJointeFichierDTO.setContenu((byte[]) mapFichier.get(PieceJointeFichierDTO.CONTENU));
                pieceJointeFichierDTO.setNomFichier((String) mapFichier.get(PieceJointeFichierDTO.NOM_FICHIER));
                if (mapFichier.get(PieceJointeFichierDTO.COMPRESSION) != null) {
                    pieceJointeFichierDTO.setCompression(
                        CompressionFichier.fromValue((String) mapFichier.get(PieceJointeFichierDTO.COMPRESSION))
                    );
                }
                pieceJointeFichierDTO.setMimeType((String) mapFichier.get(PieceJointeFichierDTO.MIME_TYPE));
                pieceJointeFichierDTO.setSha512((String) mapFichier.get(PieceJointeFichierDTO.SHA512));

                listPJF.add(pieceJointeFichierDTO);
            }
        }

        pieceJointeDTO.setType(correctType);
        pieceJointeDTO.setFichier(listPJF);
        list.add(pieceJointeDTO);

        return list;
    }

    private String getTypePieceJointeFromType(String type) {
        String[] typeTab = type.split(MAJ_SPLIT_REGEX);
        String result = StringHelper.join(typeTab, "_", "");
        return result.toUpperCase();
    }

    public void buildPieceJointeFromEvenementDTO(
        EvenementDTO evenementDTO,
        EppBaseEvenement eppBaseEvenement,
        CoreSession session
    ) {
        // on remplit le DTO avec la classe de base (EppEvt01, EppEvt02, ...)
        for (Field field : eppBaseEvenement.getClass().getDeclaredFields()) {
            buildPieceJointeFromField(evenementDTO, eppBaseEvenement, field, session);
        }

        // on remplit le DTO avec la superclass (EppBaseEvenement)
        for (Field field : eppBaseEvenement.getClass().getSuperclass().getDeclaredFields()) {
            buildPieceJointeFromField(evenementDTO, eppBaseEvenement, field, session);
        }
    }

    @SuppressWarnings("unchecked")
    private void buildPieceJointeFromField(
        EvenementDTO evenementDTO,
        EppBaseEvenement eppBaseEvenement,
        Field field,
        CoreSession session
    ) {
        try {
            // traite que les protected
            if (Modifier.isProtected(field.getModifiers())) {
                field.setAccessible(true);
                if (PieceJointe.class.isAssignableFrom(field.getType())) {
                    String correctType = getTypePieceJointeFromType(field.getName());
                    List<MgppPieceJointeDTO> listPJ = (List<MgppPieceJointeDTO>) evenementDTO.get(correctType);
                    for (MgppPieceJointeDTO pieceJointeDTO : listPJ) {
                        // une seule piece jointe
                        PieceJointe pieceJointe = buildPieceJointe(correctType, pieceJointeDTO);
                        field.set(eppBaseEvenement, pieceJointe);
                        break;
                    }
                } else if (List.class.isAssignableFrom(field.getType())) {
                    Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    if (PieceJointe.class.isAssignableFrom(((Class<?>) type))) {
                        String correctType = getTypePieceJointeFromType(field.getName());
                        List<MgppPieceJointeDTO> listPJ = (List<MgppPieceJointeDTO>) evenementDTO.get(correctType);
                        List<PieceJointe> list = new ArrayList<>();

                        if (listPJ != null) {
                            for (MgppPieceJointeDTO pieceJointeDTO : listPJ) {
                                // une seule piece jointe
                                list.add(buildPieceJointe(correctType, pieceJointeDTO));
                            }
                        }
                        field.set(eppBaseEvenement, list);
                    }
                }
            }
        } catch (Exception e) {
            this.logDebug(session, e);
        } finally {
            field.setAccessible(false);
        }
    }

    private PieceJointe buildPieceJointe(String correctType, MgppPieceJointeDTO pieceJointeDTO) {
        if (pieceJointeDTO == null) {
            return null;
        }

        PieceJointe pieceJointe = new PieceJointe();
        pieceJointe.setIdInterneEpp(pieceJointeDTO.getIdInterneEpp());
        pieceJointe.setLibelle(pieceJointeDTO.getLibelle());

        PieceJointeType pieceJointeType = SolonMgppServiceLocator
            .getPieceJointeService()
            .getCorrectPieceJointeType(correctType);

        pieceJointe.setType(pieceJointeType);
        pieceJointe.setUrl(pieceJointeDTO.getUrl());

        List<ContenuFichier> list = pieceJointe.getFichier();

        for (PieceJointeFichierDTO pieceJointeFichierDTO : pieceJointeDTO.getFichier()) {
            ContenuFichier contenuFichier = new ContenuFichier();
            CompressionFichier compression = pieceJointeFichierDTO.getCompression();
            contenuFichier.setCompression(ObjectHelper.requireNonNullElse(compression, CompressionFichier.AUCUNE));
            contenuFichier.setContenu(pieceJointeFichierDTO.getContenu());
            contenuFichier.setSha512(pieceJointeFichierDTO.getSha512());
            contenuFichier.setMimeType(pieceJointeFichierDTO.getMimeType());
            contenuFichier.setNomFichier(pieceJointeFichierDTO.getNomFichier());

            contenuFichier.setADuContenu(contenuFichier.getContenu() != null && contenuFichier.getContenu().length > 0);
            list.add(contenuFichier);
        }

        return pieceJointe;
    }

    private void logDebug(CoreSession session, Exception exception) {
        if (session != null) {
            LOGGER.debug(session, MgppLogEnumImpl.FAIL_BUILD_PIECE_JOINTE_TEC, exception);
        }
    }
}
