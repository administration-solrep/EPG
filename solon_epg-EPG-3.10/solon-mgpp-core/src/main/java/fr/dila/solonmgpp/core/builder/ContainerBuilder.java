package fr.dila.solonmgpp.core.builder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.constant.BuilderConstant;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.DateUtil;
import fr.sword.xsd.solon.epp.EppBaseEvenement;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.Mandat;
import fr.sword.xsd.solon.epp.Organisme;

public class ContainerBuilder {

	public static final String		EVT_PREFIX_MAJ	= "EVT";

	public static final String		QUINQUIES_UPPER	= "Quinquies";

	public static final String		BIS_UPPER		= "Bis";

	public static final String		BISAB_UPPER		= "BisAB";

	public static final String		TER_UPPER		= "Ter";

	public static final String		QUINQUIES_LOWER	= "quinquies";

	public static final String		BIS_LOWER		= "bis";

	public static final String		BISAB_LOWER		= "bisab";

	public static final String		TER_LOWER		= "ter";

	public static final String		QUATER_UPPER	= "Quater";

	public static final String		QUATER_LOWER	= "quater";

	public static final String		EVT_PREFIX		= "evt";

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger	LOGGER			= STLogFactory.getLog(ContainerBuilder.class);

	private static ContainerBuilder	instance;

	private ContainerBuilder() {
		// default private constructor
	}

	public static ContainerBuilder getInstance() {
		if (instance == null) {
			instance = new ContainerBuilder();
		}

		return instance;
	}

	/**
	 * Construction d'un {@link EppEvtContainer} a partir d'un {@link EppEvtContainer}
	 * 
	 * @param evenementDTO
	 * @return
	 * @throws ClientException
	 */
	public EppBaseEvenement buildEppEvtFromEvenementDTO(EvenementDTO evenementDTO, CoreSession session)
			throws ClientException {
		if (evenementDTO != null) {

			String typeEvenement = evenementDTO.getTypeEvenementName();
			if (StringUtils.isNotBlank(typeEvenement)) {

				EppBaseEvenement eppBaseEvenement = findEppEvtFromTypeEvenement(typeEvenement, session);

				if (eppBaseEvenement != null) {

					initEppEvtWithEvenemetDTO(evenementDTO, eppBaseEvenement, session);

					return eppBaseEvenement;
				}
			}
		}
		return null;

	}

	/**
	 * 
	 * @param evenementDTO
	 * @param eppBaseEvenement
	 */
	private void initEppEvtWithEvenemetDTO(EvenementDTO evenementDTO, EppBaseEvenement eppBaseEvenement,
			CoreSession session) {

		for (Entry<String, Serializable> entry : evenementDTO.entrySet()) {
			String key = entry.getKey();
			if (EvenementDTO.TYPE_EVENEMENT.equals(key)) {
				// info contenu par le container
			} else {
				Field field = null;
				try {
					try {
						field = eppBaseEvenement.getClass().getDeclaredField(key);
					} catch (NoSuchFieldException e) {
						// try superclass
						field = eppBaseEvenement.getClass().getSuperclass().getDeclaredField(key);
					}

					if (field == null) {
						throw new ClientException("Champ inconnu " + key);
					} else {
						field.setAccessible(true);
						Class<?> fieldClass = field.getType();
						setFieldValue(eppBaseEvenement, field, fieldClass, entry.getValue());
					}

				} catch (Exception e) {
					if (session != null) {
						LOGGER.debug(session, MgppLogEnumImpl.FAIL_BUILD_CONTAINER_TEC, e);
					}
				} finally {
					if (field != null) {
						field.setAccessible(false);
					}
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void setFieldValue(Object object, Field field, Class<?> fieldClass, Object value)
			throws IllegalAccessException, Exception {
		try {
			field.setAccessible(true);
			if (byte[].class.isAssignableFrom(fieldClass)) {
				// contenu fichier
				field.set(object, value);
			} else if (String.class.isAssignableFrom(fieldClass)) {
				field.set(object, value);
			} else if (Boolean.class.isAssignableFrom(fieldClass)) {
				if (value instanceof String) {
					value = Boolean.parseBoolean((String) value);
				}
				field.set(object, value);
			} else if (Long.class.isAssignableFrom(fieldClass)) {
				if (value instanceof String) {
					value = Long.parseLong((String) value);
				}
				field.set(object, value);
			} else if (Integer.class.isAssignableFrom(fieldClass)) {
				if (value instanceof String) {
					value = Integer.parseInt((String) value);
				} else if (value instanceof Long) {
					value = ((Long) value).intValue();
				}
				field.set(object, value);
			} else if (XMLGregorianCalendar.class.isAssignableFrom(fieldClass)) {
				Date date = (Date) value;
				buildXMLGregorianCalendar(object, date, field);
			} else if (List.class.isAssignableFrom(fieldClass)) {
				List<?> list = (List<?>) value;
				buildListObject(object, list, field);
			} else if (Enum.class.isAssignableFrom(fieldClass)) {
				String enumeration = (String) value;
				buildEnumObject(object, enumeration, field);
			} else if (Serializable.class.isAssignableFrom(fieldClass)) {
				if (Mandat.class.isAssignableFrom(fieldClass)) {
					Mandat mandat = Mandat.class.newInstance();
					mandat.setId((String) value);
					field.set(object, mandat);
				} else if (Organisme.class.isAssignableFrom(fieldClass)) {
					Organisme organisme = Organisme.class.newInstance();
					organisme.setId((String) value);
					field.set(object, organisme);
				} else {
					Map<String, Serializable> map = (Map<String, Serializable>) value;
					buildMapObject(object, map, field);
				}
			} else if (int.class.isAssignableFrom(fieldClass)) {
				if (value instanceof String) {
					value = Integer.parseInt((String) value);
				} else if (value instanceof Long) {
					value = ((Long) value).intValue();
				}
				field.set(object, value);
			} else {
				field.set(object, value);
			}
		} finally {
			field.setAccessible(false);
		}
	}

	private void buildMapObject(Object object, Map<String, Serializable> map, Field field) throws Exception {

		Object newObject = field.getType().newInstance();

		for (Entry<String, Serializable> entry : map.entrySet()) {
			Field newField = newObject.getClass().getDeclaredField(entry.getKey());
			setFieldValue(newObject, newField, newField.getType(), entry.getValue());
		}

		field.set(object, newObject);

	}

	private void buildEnumObject(Object object, String enumValue, Field field) throws Exception {
		if (StringUtils.isNotBlank(enumValue)) {
			Method method = field.getType().getDeclaredMethod(BuilderConstant.XSD_ENUM_METHOD_FROM_VALUE, String.class);
			Object obj = method.invoke(field, enumValue);
			field.set(object, obj);
		} else {
			field.set(object, null);
		}

	}

	/**
	 * construction d'une {@link List}
	 * 
	 * @param eppBaseEvenement
	 * @param list
	 * @param field
	 */
	@SuppressWarnings("unchecked")
	private void buildListObject(Object obje, List<?> listDTO, Field field) throws Exception {
		List<Object> result = new ArrayList<Object>();
		Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];

		for (Object object : listDTO) {
			if (String.class.isAssignableFrom((Class<?>) type)) {
				result.add(object);
			} else if (Boolean.class.isAssignableFrom((Class<?>) type)) {
				result.add(object);
			} else if (Long.class.isAssignableFrom((Class<?>) type)) {
				result.add(object);
			} else if (Integer.class.isAssignableFrom((Class<?>) type)) {
				result.add(object);
			} else if (XMLGregorianCalendar.class.isAssignableFrom((Class<?>) type)) {
				Date date = (Date) object;
				if (date != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					XMLGregorianCalendar xmlGregorianCalendar = DateUtil.calendarToXMLGregorianCalendar(cal);
					result.add(xmlGregorianCalendar);
				}
			} else if (Enum.class.isAssignableFrom((Class<?>) type)) {
				String enumValue = (String) object;
				if (StringUtils.isNotBlank(enumValue)) {
					Method method = ((Class<?>) type).getDeclaredMethod(BuilderConstant.XSD_ENUM_METHOD_FROM_VALUE,
							String.class);
					Object obj = method.invoke(type, enumValue);
					result.add(obj);
				}
			} else if (Serializable.class.isAssignableFrom((Class<?>) type)) {
				if (Mandat.class.isAssignableFrom((Class<?>) type)) {
					Mandat mandat = Mandat.class.newInstance();
					mandat.setId((String) object);
					result.add(mandat);
				} else if (Organisme.class.isAssignableFrom((Class<?>) type)) {
					Organisme organisme = Organisme.class.newInstance();
					organisme.setId((String) object);
					result.add(organisme);
				} else {
					Map<String, Serializable> map = (Map<String, Serializable>) object;
					Object newObject = ((Class<?>) type).newInstance();

					for (Entry<String, Serializable> entry : map.entrySet()) {
						Field newField = newObject.getClass().getDeclaredField(entry.getKey());
						setFieldValue(newObject, newField, newField.getType(), entry.getValue());
					}
					result.add(newObject);
				}

			} else {
				field.set(obje, object);
			}
		}

		field.set(obje, result);
	}

	/**
	 * construction d'un {@link XMLGregorianCalendar} à partir d'une date
	 * 
	 * @param eppBaseEvenement
	 * @param key
	 * @param field
	 * @throws IllegalAccessException
	 */
	private void buildXMLGregorianCalendar(Object object, Date date, Field field) throws Exception {
		XMLGregorianCalendar xmlGregorianCalendar = null;
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			xmlGregorianCalendar = DateUtil.calendarToXMLGregorianCalendar(cal);
		}
		field.set(object, xmlGregorianCalendar);
	}

	/**
	 * Recherche du bon evenement (eppEvt01, EppEvt02, ...)
	 * 
	 * @param typeEvenement
	 * @return
	 */
	private EppBaseEvenement findEppEvtFromTypeEvenement(final String typeEvenement, CoreSession session) {
		String evenementType = typeEvenement.replaceAll(BuilderConstant.NOT_ALPHA, "");

		EppBaseEvenement eppEvent = searchContainer(evenementType, session);

		if (eppEvent == null) {
			// retry sans le prefix EVT
			evenementType = evenementType.replaceFirst(EVT_PREFIX_MAJ, "");
			eppEvent = searchContainer(evenementType, session);
		}

		return eppEvent;
	}

	private EppBaseEvenement searchContainer(String evenementType, CoreSession session) {
		EppBaseEvenement eppEvent = null;

		// search with same formatter
		for (EvenementType evenementTypeEnum : EvenementType.values()) {
			if (evenementTypeEnum.value().replaceAll(BuilderConstant.NOT_ALPHA, "").equals(evenementType)) {
				Field field = null;
				// get correct EppEvt
				EppEvtContainer eppEvtContainer = new EppEvtContainer();
				try {
					field = eppEvtContainer.getClass().getDeclaredField(evenementTypeEnum.value().toLowerCase());
					if (field != null) {
						field.setAccessible(true);
						eppEvent = (EppBaseEvenement) field.getType().newInstance();
					}
				} catch (Exception e) {
					eppEvent = null;
					try {
						// retry avec le prefix evt et la premiere lettre en majuscule...
						String value = evenementTypeEnum.value();
						String type = EVT_PREFIX + value.substring(0, 1).toUpperCase()
								+ value.substring(1, value.length()).toLowerCase();
						field = eppEvtContainer.getClass().getDeclaredField(type);
						if (field != null) {
							field.setAccessible(true);
							eppEvent = (EppBaseEvenement) field.getType().newInstance();
						}
					} catch (Exception e1) {
						eppEvent = null;
						try {
							field = eppEvtContainer.getClass().getDeclaredField(
									evenementTypeEnum.value().toLowerCase().replaceAll(BuilderConstant.NOT_ALPHA, ""));
							if (field != null) {
								field.setAccessible(true);
								eppEvent = (EppBaseEvenement) field.getType().newInstance();
							}
						} catch (Exception e2) {
							eppEvent = null;
							try {
								// retry avec avec Ter au lieu de ter

								String type = evenementTypeEnum.value().toLowerCase()
										.replaceAll(BuilderConstant.NOT_ALPHA, "")
										.replaceAll(QUATER_LOWER, QUATER_UPPER);
								field = eppEvtContainer.getClass().getDeclaredField(type);

								if (field != null) {
									field.setAccessible(true);
									eppEvent = (EppBaseEvenement) field.getType().newInstance();
								}
							} catch (Exception e3) {
								eppEvent = null;
								try {
									// retry avec avec Ter au lieu de ter

									String type = evenementTypeEnum.value().toLowerCase()
											.replaceAll(BuilderConstant.NOT_ALPHA, "").replaceAll(TER_LOWER, TER_UPPER);
									field = eppEvtContainer.getClass().getDeclaredField(type);

									if (field != null) {
										field.setAccessible(true);
										eppEvent = (EppBaseEvenement) field.getType().newInstance();
									}
								} catch (Exception e4) {
									eppEvent = null;
									try {
										// retry avec avec Ter au lieu de ter

										String type = evenementTypeEnum.value().toLowerCase()
												.replaceAll(BuilderConstant.NOT_ALPHA, "")
												.replaceAll(BISAB_LOWER, BISAB_UPPER);
										field = eppEvtContainer.getClass().getDeclaredField(type);

										if (field != null) {
											field.setAccessible(true);
											eppEvent = (EppBaseEvenement) field.getType().newInstance();
										}
									} catch (Exception e5) {
										eppEvent = null;
										try {
											// retry avec avec Ter au lieu de ter

											String type = evenementTypeEnum.value().toLowerCase()
													.replaceAll(BuilderConstant.NOT_ALPHA, "")
													.replaceAll(BIS_LOWER, BIS_UPPER);
											field = eppEvtContainer.getClass().getDeclaredField(type);

											if (field != null) {
												field.setAccessible(true);
												eppEvent = (EppBaseEvenement) field.getType().newInstance();
											}
										} catch (Exception e6) {
											eppEvent = null;
											try {
												// retry avec avec Ter au lieu de ter

												String type = evenementTypeEnum.value().toLowerCase()
														.replaceAll(BuilderConstant.NOT_ALPHA, "")
														.replaceAll(QUINQUIES_LOWER, QUINQUIES_UPPER);
												field = eppEvtContainer.getClass().getDeclaredField(type);

												if (field != null) {
													field.setAccessible(true);
													eppEvent = (EppBaseEvenement) field.getType().newInstance();
												}
											} catch (Exception e7) {
												this.logInfo(e7, session);
												eppEvent = null;
											}
										}
									}
								}
							}
						}
					}

				} finally {
					if (field != null) {
						field.setAccessible(false);
					}
				}
				break;
			}
		}
		return eppEvent;
	}

	private void logInfo(Exception exception, CoreSession session) {
		if (session != null) {
			LOGGER.info(session, MgppLogEnumImpl.FAIL_BUILD_CONTAINER_TEC, exception);
		}
	}
}
