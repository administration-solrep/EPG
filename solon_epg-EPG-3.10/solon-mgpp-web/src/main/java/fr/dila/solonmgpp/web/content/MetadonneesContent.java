package fr.dila.solonmgpp.web.content;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.descriptor.PropertyDescriptor;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;

public abstract class MetadonneesContent {

    /*****************************
     * Modes du layout
     ****************************/
    public static final String LAYOUT_MODE_RECTIFIER = "rectifier";
    public static final String LAYOUT_MODE_COMPLETER = "completer";
    public static final String LAYOUT_MODE_CREER = "edit";

    protected String currentLayoutMode;

    /**
     * {@link PropertyDescriptor} cache
     */
    protected Map<String, Map<String, PropertyDescriptor>> currentMap;

    /**
     * isColumnVisible
     * 
     * @param columnName nom de la metadonnée
     * @return true s'il existe un {@link PropertyDescriptor} correspondant a la colonne
     */
    public Boolean isColumnVisible(String columnName) {

        if (StringUtils.isNotEmpty(columnName)) {
            final EvenementDTO evt = getCurrentEvenement();
            if (evt != null) {
                try {
                    PropertyDescriptor propertyDescriptor = getPropertyDescriptor(columnName, evt.getTypeEvenementName());

                    if (propertyDescriptor != null && propertyDescriptor.isVisibility()) {
                        // MGPP = Gouvernement
                        return Boolean.FALSE;
                    }

                    return propertyDescriptor != null;

                } catch (ClientException e) {
                    logErrorRecuperation(evt, e);
                }
            }
        }

        return false;

    }
    
    /**
     * Retourne true si un des évènements de la procédure courante contient cette métadonnée.
     * 
     * @param columnName Nom de la metadonnée
     * @return True s'il existe un {@link PropertyDescriptor} correspondant a la colonne
     * @throws ClientException 
     */
    public Boolean isColumnVisibleInProcedure(String columnName) {

        if (StringUtils.isNotEmpty(columnName)) {
            final String procedure = getCurrentProcedure();
            if (procedure != null) {
                List<EvenementTypeDescriptor> evenementTypeList = SolonMgppServiceLocator.getEvenementTypeService().findEvenementTypeByProcedure(procedure);
                for (EvenementTypeDescriptor evenementType : evenementTypeList) {
                    PropertyDescriptor propertyDescriptor;
                    try {
                        propertyDescriptor = getPropertyDescriptor(columnName, evenementType.getName());
                    } catch (ClientException e) {
                        continue;
                    }
    
                    if (propertyDescriptor != null && !propertyDescriptor.isVisibility()) {
                        // MGPP = Gouvernement
                        return Boolean.TRUE;
                    }
                }
            }
        }

        return false;

    }

    protected PropertyDescriptor getPropertyDescriptor(final String columnName, final String typeEvenementName) throws ClientException {

        if (currentMap == null) {
            currentMap = new HashMap<String, Map<String, PropertyDescriptor>>();
        }

        Map<String, PropertyDescriptor> map = currentMap.get(typeEvenementName);
        if (map == null) {
            // on vide la currentMap pour ne garder dans le bean que 1 typeEvenement
            currentMap.clear();

            map = SolonMgppServiceLocator.getMetaDonneesService().getMapProperty(typeEvenementName);
            currentMap.put(typeEvenementName, map);
        }

        return map.get(columnName);
        
    }

    /**
     * isColumnEditable
     * 
     * @param columnName nom de la metadonnée
     * @return true si le {@link PropertyDescriptor} de la metadonne est editable
     */
    public Boolean isColumnRequired(String columnName) {

        if (StringUtils.isNotEmpty(columnName)) {
            final EvenementDTO evt = getCurrentEvenement();
            if (evt != null) {
                try {

                    PropertyDescriptor propertyDescriptor = getPropertyDescriptor(columnName, evt.getTypeEvenementName());
                    if (propertyDescriptor != null) {
                        return propertyDescriptor.isObligatoire();
                    }

                } catch (ClientException e) {
                    logErrorRecuperation(evt, e);
                }
            }
        }

        return false;

    }

    /**
     * getWidgetMode
     * 
     * @param columnName nom de la metadonnée
     * @return 'edit' si le {@link PropertyDescriptor} de la metadonne est editable
     */
    public String getWidgetMode(String columnName) {

        if (StringUtils.isNotEmpty(columnName)) {
            final EvenementDTO evt = getCurrentEvenement();
            if (evt != null) {
                try {

                    PropertyDescriptor propertyDescriptor = getPropertyDescriptor(columnName, evt.getTypeEvenementName());
                    if (propertyDescriptor != null) {
                        if (propertyDescriptor.isModifiable() && !propertyDescriptor.isRenseignerEpp()) {

                            if ((LAYOUT_MODE_RECTIFIER.equals(currentLayoutMode) || LAYOUT_MODE_COMPLETER.equals(currentLayoutMode))
                                    && (EvenementDTO.EMETTEUR.equals(columnName) || EvenementDTO.DESTINATAIRE.equals(columnName)
                                            || EvenementDTO.DESTINATAIRE_COPIE.equals(columnName) || EvenementDTO.DOSSIER.equals(columnName))) {
                                return "view";
                            } else if (LAYOUT_MODE_COMPLETER.equals(currentLayoutMode) && propertyDescriptor.isObligatoire()
                                    && !propertyDescriptor.isMultiValue()) {
                                return "view";
                            } else {
                                return "edit";
                            }

                        } else if (LAYOUT_MODE_RECTIFIER.equals(currentLayoutMode) && propertyDescriptor.isRenseignerEpp()) {
                            return "hidden";
                        }
                    }
                } catch (ClientException e) {
                    logErrorRecuperation(evt, e);
                }
            }
        }

        return "view";

    }

    /**
     * 
     * @param niveauLecture
     * @return true si le niveau de lecture est AN ou SENAT
     */
    public Boolean isNumeroLectureVisible(String selectionListId, String niveauLecture) {
        EvenementDTO evenementDTO = getCurrentEvenement();
        if (evenementDTO != null) {
            // creation en cours
            FacesContext context = FacesContext.getCurrentInstance();
            UIViewRoot root = context.getViewRoot();
            UIComponent selectionList = findComponent(root, selectionListId);
            if (selectionList instanceof ValueHolder) {
                niveauLecture = (String) ((ValueHolder) selectionList).getValue();
            }
        }
        return "AN".equals(niveauLecture) || "SENAT".equals(niveauLecture);
    }

    protected UIComponent findComponent(UIComponent component, String ident) {
        if (ident.equals(component.getId())) {
            return component;
        }
        Iterator<UIComponent> kids = component.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent found = findComponent(kids.next(), ident);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public String getWidgetLabel(Boolean translated, String defaultLabel, String columnName) {
        String label = "";
        if (StringUtils.isNotEmpty(columnName)) {
            final EvenementDTO evt = getCurrentEvenement();
            if (evt != null) {
                try {
                    PropertyDescriptor propertyDescriptor = getPropertyDescriptor(columnName, evt.getTypeEvenementName());

                    if (propertyDescriptor != null && StringUtils.isNotBlank(propertyDescriptor.getLabel())) {
                        if (translated) {
                            label = getResourcesAccessor().getMessages().get(propertyDescriptor.getLabel());
                        } else {
                            label = propertyDescriptor.getLabel();
                        }
                    } else if (translated) {
                        label = getResourcesAccessor().getMessages().get(defaultLabel);
                    } else {
                        label = defaultLabel;
                    }
                } catch (ClientException e) {
                    logErrorRecuperation(evt, e);
                }
            }
        }
        return label;
    }

    protected abstract void logErrorRecuperation(EvenementDTO evt, ClientException exc);

    protected abstract EvenementDTO getCurrentEvenement();
    
    protected abstract String getCurrentProcedure(); 

    protected abstract ResourcesAccessor getResourcesAccessor();
}
