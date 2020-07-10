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

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.constant.BuilderConstant;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.DateUtil;
import fr.sword.xsd.solon.epp.EppBaseEvenement;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.Mandat;
import fr.sword.xsd.solon.epp.Organisme;

public class EvenementBuilder {

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(ContainerBuilder.class);    

    private static EvenementBuilder instance;

    private EvenementBuilder() {
        // default private constructor
    }

    public static EvenementBuilder getInstance() {
        if (instance == null) {
            instance = new EvenementBuilder();
        }

        return instance;
    }

    /**
     * Construction d'un {@link EvenementDTO} a partir d'un {@link EppEvtContainer}
     * 
     * @param evenementDTO
     * @param eppEvtContainer
     * @throws ClientException
     */
    public void buildEvenementDTOFromContainer(EvenementDTO evenementDTO, EppEvtContainer eppEvtContainer, CoreSession session) throws ClientException {

        EvenementTypeDescriptor evenementTypeDescriptor = SolonMgppServiceLocator.getEvenementTypeService().getEvenementType(
                eppEvtContainer.getType().value());
        if (evenementTypeDescriptor != null) {
            Map<String, Serializable> map = new HashMap<String, Serializable>();
            map.put(EvenementDTO.LABEL, evenementTypeDescriptor.getLabel());
            map.put(EvenementDTO.NAME, evenementTypeDescriptor.getName());
            evenementDTO.put(EvenementDTO.TYPE_EVENEMENT, (Serializable) map);
        }

        EppBaseEvenement eppBaseEvenement = null;

        // recherche de l'evenement dans les eppEvt01, eppEvt02, ...
        for (Method method : eppEvtContainer.getClass().getMethods()) {
            if (EppBaseEvenement.class.isAssignableFrom(method.getReturnType())) {
                try {
                    Object object = method.invoke(eppEvtContainer);
                    if (object instanceof EppBaseEvenement) {
                        eppBaseEvenement = (EppBaseEvenement) object;
                        // ok on sort
                        break;
                    }
                } catch (Exception e) {
                    this.logDebug(session, e) ;
                }
            }
        }

        if (eppBaseEvenement == null) {
            throw new ClientException("Les données récupérées de SOLON EPP sont incohérentes");
        }

        // initialisation générique de l'evenementDTO
        initEvenementDTOWithEppEvt(evenementDTO, eppBaseEvenement, session);

        // remap des pieces jointes
        PieceJointeBuilder.getInstance().buildPieceJointeWithEppEvt(evenementDTO, eppBaseEvenement, session);

    }

    public void initEvenementDTOWithEppEvt(EvenementDTO evenementDTO, EppBaseEvenement eppBaseEvenement, CoreSession session) {

        // on remplit le DTO avec la classe de base (EppEvt01, EppEvt02, ...)
        for (Field field : eppBaseEvenement.getClass().getDeclaredFields()) {
            buildField(evenementDTO, eppBaseEvenement, field, session);
        }

        // on remplit le DTO avec la superclass (EppBaseEvenement)
        for (Field field : eppBaseEvenement.getClass().getSuperclass().getDeclaredFields()) {
            buildField(evenementDTO, eppBaseEvenement, field, session);
        }
    }

    private void buildField(EvenementDTO evenementDTO, EppBaseEvenement eppBaseEvenement, Field field, CoreSession session) {
        try {
            // traite que les protected
            if (Modifier.isProtected(field.getModifiers())) {
                field.setAccessible(true);
                Object obj = field.get(eppBaseEvenement);
                buildField(evenementDTO, field.getName(), obj, field.getType(), session);
            }
        } catch (Exception e) {
              this.logDebug(session, e) ;
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
                if (StringUtils.isNotBlank((String) object)) {
                    result.add(object);
                }
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
                    Mandat mandat = (Mandat) object;
                    if (StringUtils.isNotBlank(mandat.getId())) {
                        result.add(mandat.getId());
                    }
                } else if (Organisme.class.isAssignableFrom(object.getClass())) {
                    Organisme organisme = (Organisme) object;
                    if (StringUtils.isNotBlank(organisme.getId())) {
                        result.add(organisme.getId());
                    }
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
                            this.logDebug(session, e) ;
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
              this.logDebug(session, e) ;
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
                  this.logDebug(session, e) ;
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
                this.logDebug(session, e) ;
            }
        }

        if(objet != null) {
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
	                    buildField(result, field.getName(), obj, field.getType(),session);
	                }
	            } catch (Exception e) {
	                  this.logDebug(session, e) ;
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
        if (enumeration != null) {
            try {
                Method method = enumeration.getClass().getDeclaredMethod(BuilderConstant.XSD_ENUM_METHOD_VALUE);
                Object object = method.invoke(enumeration);
                evenementDTO.put(dtoFieldName, (Serializable) object);
            } catch (Exception e) {
                evenementDTO.put(dtoFieldName, enumeration);
            }
        } else {
            evenementDTO.put(dtoFieldName, null);
        }
    }
    
    private void logDebug(CoreSession session , Exception exception) {
        if(session != null){
            LOGGER.debug(session, MgppLogEnumImpl.FAIL_BUILD_EVENT_TEC,exception) ;
        }
    }
}
