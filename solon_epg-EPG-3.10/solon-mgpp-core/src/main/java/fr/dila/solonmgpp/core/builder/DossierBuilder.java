package fr.dila.solonmgpp.core.builder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.constant.BuilderConstant;
import fr.dila.solonmgpp.api.dto.FicheDossierDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.DateUtil;
import fr.sword.xsd.solon.epp.FicheDossier;
import fr.sword.xsd.solon.epp.Mandat;
import fr.sword.xsd.solon.epp.Organisme;

public class DossierBuilder {

    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOGGER = STLogFactory.getLog(DossierBuilder.class);
    
    private static DossierBuilder instance;

    private DossierBuilder() {
        // default private constructor
    }

    public static DossierBuilder getInstance() {
        if (instance == null) {
            instance = new DossierBuilder();
        }

        return instance;
    }

    /**
     * Construction d'un {@link FicheDossierDTO} a partir d'un {@link FicheDossier}
     * 
     * @param ficheDossierDTO
     * @param ficheDossier
     * @throws ClientException
     */
    public void buildDossierDTOFromFicheDossier(FicheDossierDTO ficheDossierDTO, FicheDossier ficheDossier, CoreSession session) throws ClientException {

        // on remplit le DTO avec la classe de base (EppEvt01, EppEvt02, ...)
        for (Field field : ficheDossier.getClass().getDeclaredFields()) {
            buildField(ficheDossierDTO, ficheDossier, field, session);
        }

    }

    private void buildField(FicheDossierDTO ficheDossierDTO, FicheDossier ficheDossier, Field field, CoreSession session) {
        try {
            // traite que les protected
            if (Modifier.isProtected(field.getModifiers())) {
                field.setAccessible(true);
                Object obj = field.get(ficheDossier);
                buildField(ficheDossierDTO, field.getName(), obj, field.getType(), session);
            }
        } catch (Exception e) {
            LOGGER.debug(session, MgppLogEnumImpl.FAIL_BUILD_FOLDER_TEC) ;
        } finally {
            field.setAccessible(false);
        }
    }

    private void buildField(Map<String, Serializable> dto, String name, Object obj, Class<?> fieldClass, CoreSession session) {

        if (byte[].class.isAssignableFrom(fieldClass)) {
            // contenu fichier
            dto.put(name, (Serializable) obj);
        } else if (String.class.isAssignableFrom(fieldClass)) {
            dto.put(name, (String) obj);
        } else if (Boolean.class.isAssignableFrom(fieldClass)) {
            dto.put(name, (Boolean) obj);
        } else if (Long.class.isAssignableFrom(fieldClass)) {
            dto.put(name, (Long) obj);
        } else if (Integer.class.isAssignableFrom(fieldClass)) {
            dto.put(name, (Integer) obj);
        } else if (XMLGregorianCalendar.class.isAssignableFrom(fieldClass)) {
            setDateObject(dto, (XMLGregorianCalendar) obj, name);
        } else if (List.class.isAssignableFrom(fieldClass)) {
            setListObject(dto, (List<?>) obj, name, fieldClass, session);
        } else if (Enum.class.isAssignableFrom(fieldClass)) {
            setEnumObject(dto, (Enum<?>) obj, name);
        } else if (Serializable.class.isAssignableFrom(fieldClass)) {
            setSerializableObject(dto, (Serializable) obj, name, fieldClass,session);
        } else {
            dto.put(name, (Serializable) obj);
        }

    }

    /**
     * Construction des objets de type {@link List}
     * 
     * @param dto
     * @param list
     * @param dtoFieldName
     * @param fieldClass
     */
    @SuppressWarnings("unchecked")
    private void setListObject(Map<String, Serializable> dto, List<?> list, String dtoFieldName, Class<?> fieldClass, CoreSession session) {
        List<Object> result = (List<Object>) dto.get(dtoFieldName);

        if (result == null) {
            result = new ArrayList<Object>();
        }

        if (list == null) {
            if (byte[].class.isAssignableFrom(fieldClass)) {
                // contenu fichier
                dto.put(dtoFieldName, null);
            } else if (String.class.isAssignableFrom(fieldClass)) {
                dto.put(dtoFieldName, null);
            } else if (Boolean.class.isAssignableFrom(fieldClass)) {
                dto.put(dtoFieldName, null);
            } else if (Long.class.isAssignableFrom(fieldClass)) {
                dto.put(dtoFieldName, null);
            } else if (Integer.class.isAssignableFrom(fieldClass)) {
                dto.put(dtoFieldName, null);
            } else if (XMLGregorianCalendar.class.isAssignableFrom(fieldClass)) {
                dto.put(dtoFieldName, null);
            } else if (List.class.isAssignableFrom(fieldClass)) {
                dto.put(dtoFieldName, null);
            } else if (Enum.class.isAssignableFrom(fieldClass)) {
                dto.put(dtoFieldName, null);
            } else if (Serializable.class.isAssignableFrom(fieldClass)) {
                if (Mandat.class.isAssignableFrom(fieldClass)) {
                    dto.put(dtoFieldName, null);
                } else if (Organisme.class.isAssignableFrom(fieldClass)) {
                    dto.put(dtoFieldName, null);
                } else {
                    Map<String, Serializable> mapResult = buildNewSerializableField(dto, dtoFieldName, fieldClass, session);

                    dto.put(dtoFieldName, (Serializable) mapResult);
                }
            } else {
                dto.put(dtoFieldName, null);
            }

            return;
        }

        for (Object object : list) {
            if (object instanceof String) {
                result.add(object);
            } else if (object instanceof Boolean) {
                result.add(object);
            } else if (object instanceof Long) {
                result.add(object);
            } else if (object instanceof Integer) {
                result.add(object);
            } else if (object instanceof XMLGregorianCalendar) {
                result.add(DateUtil.xmlGregorianCalendarToDate((XMLGregorianCalendar) object));
            } else if (object instanceof Enum<?>) {
                Enum<?> enumeration = (Enum<?>) object;
                try {
                    Method method = enumeration.getClass().getDeclaredMethod(BuilderConstant.XSD_ENUM_METHOD_VALUE);
                    result.add(method.invoke(enumeration));
                } catch (Exception e) {
                    result.add(enumeration);
                }
            } else if (object instanceof Serializable) {
                if (Mandat.class.isAssignableFrom(object.getClass())) {
                    result.add(((Mandat) object).getId());
                } else if (Organisme.class.isAssignableFrom(object.getClass())) {
                    result.add(((Organisme) object).getId());
                } else {
                    Map<String, Serializable> map = new HashMap<String, Serializable>();
                    for (Field field : object.getClass().getDeclaredFields()) {
                        try {
                            // traite que les protected
                            if (Modifier.isProtected(field.getModifiers())) {
                                field.setAccessible(true);
                                Object obj = field.get(object);
                                buildField(map, field.getName(), obj, field.getType(), session);
                            }
                        } catch (Exception e) {
                            LOGGER.debug(session, MgppLogEnumImpl.FAIL_BUILD_FOLDER_TEC) ;
                        } finally {
                            field.setAccessible(false);
                        }
                    }
                    result.add(map);
                }
            } else {
                result.add(object);
            }
        }

        dto.put(dtoFieldName, (Serializable) result);

    }

    @SuppressWarnings("unchecked")
    private Map<String, Serializable> buildNewSerializableField(Map<String, Serializable> dto, String dtoFieldName, Class<?> fieldClass, CoreSession session) {
        Map<String, Serializable> mapResult = (Map<String, Serializable>) dto.get(dtoFieldName);

        if (mapResult == null) {
            mapResult = new HashMap<String, Serializable>();
        }

        Object object = null;
        try {
            object = fieldClass.newInstance();
        } catch (Exception e) {
            LOGGER.debug(session, MgppLogEnumImpl.FAIL_BUILD_FOLDER_TEC, e) ;
        }

        for (Field field : fieldClass.getDeclaredFields()) {
            try {
                // traite que les protected
                if (Modifier.isProtected(field.getModifiers())) {
                    field.setAccessible(true);
                    Object obj = field.get(object);
                    buildField(mapResult, field.getName(), obj, field.getType(), session);
                }
            } catch (Exception e) {
                LOGGER.debug(session, MgppLogEnumImpl.FAIL_BUILD_FOLDER_TEC,e) ;
            } finally {
                field.setAccessible(false);
            }
        }
        return mapResult;
    }

    /**
     * Construction des objets de type {@link Serializable}
     * 
     * @param dto
     * @param objet
     * @param dtoFieldName
     * @param objetClass
     */
    @SuppressWarnings("unchecked")
    private void setSerializableObject(Map<String, Serializable> dto, Serializable objet, String dtoFieldName, Class<?> objetClass, CoreSession session) {
        Map<String, Serializable> result = (Map<String, Serializable>) dto.get(dtoFieldName);
        if (result == null) {
            result = new HashMap<String, Serializable>();
        }

        if (objet == null) {
            try {
                objet = (Serializable) objetClass.newInstance();
            } catch (Exception e) {
                LOGGER.debug(session, MgppLogEnumImpl.FAIL_BUILD_FOLDER_TEC, e) ;
            }
        }

		if (objet != null) {
			if (Mandat.class.isAssignableFrom(objetClass)) {
				dto.put(dtoFieldName, ((Mandat) objet).getId());
				return;
			}

			if (Organisme.class.isAssignableFrom(objetClass)) {
				dto.put(dtoFieldName, ((Organisme) objet).getId());
				return;
			}

			for (Field field : objetClass.getDeclaredFields()) {
				try {
					// traite que les protected
					if (Modifier.isProtected(field.getModifiers())) {
						field.setAccessible(true);
						Object obj = field.get(objet);
						buildField(result, field.getName(), obj, field.getType(), session);
					}
				} catch (Exception e) {
					LOGGER.debug(session, MgppLogEnumImpl.FAIL_BUILD_FOLDER_TEC);
				} finally {
					field.setAccessible(false);
				}
			}

			dto.put(dtoFieldName, (Serializable) result);
		}
    }

    /**
     * Construction des objets de type {@link XMLGregorianCalendar}
     * 
     * @param dto
     * @param xmlGregorianCalendar
     * @param dtoFieldName
     */
    private void setDateObject(Map<String, Serializable> evenementDTO, XMLGregorianCalendar xmlGregorianCalendar, String dtoFieldName) {
        evenementDTO.put(dtoFieldName, DateUtil.xmlGregorianCalendarToDate(xmlGregorianCalendar));
    }

    /**
     * Construction des objets de type {@link Enum}
     * 
     * @param dto
     * @param xmlGregorianCalendar
     * @param dtoFieldName
     */
    private void setEnumObject(Map<String, Serializable> evenementDTO, Enum<?> enumeration, String dtoFieldName) {
        if (enumeration == null) {
            evenementDTO.put(dtoFieldName, null);
        } else {
            try {
                Method method = enumeration.getClass().getDeclaredMethod(BuilderConstant.XSD_ENUM_METHOD_VALUE);
                Object object = method.invoke(enumeration);
                evenementDTO.put(dtoFieldName, (Serializable) object);
            } catch (Exception e) {
                evenementDTO.put(dtoFieldName, enumeration);
            }
        }

    }

}
