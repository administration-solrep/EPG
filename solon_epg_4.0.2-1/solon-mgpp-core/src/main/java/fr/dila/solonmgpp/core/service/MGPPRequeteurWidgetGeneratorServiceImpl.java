package fr.dila.solonmgpp.core.service;

import static fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil.getRequiredService;

import fr.dila.solonmgpp.api.service.MGPPRequeteurWidgetGeneratorService;
import fr.dila.st.api.constant.STRequeteurExpertConstants;
import fr.dila.st.api.requeteur.RequeteurAddInfoField;
import fr.dila.st.api.requeteur.RequeteurWidgetDescription;
import fr.dila.st.api.requeteur.SelectedDocument;
import fr.dila.st.core.requeteur.CategoryWidgetDescriptionComparator;
import fr.dila.st.core.util.CollectionHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.nuxeo.ecm.core.schema.SchemaManager;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

public class MGPPRequeteurWidgetGeneratorServiceImpl
    extends DefaultComponent
    implements MGPPRequeteurWidgetGeneratorService {
    private static final String SELECTED_DOCUMENTS_EP = "selecteddocuments";

    private static final String WIDGET_DESCRIPTIONS_EP = "widgetdescriptions";

    private static final String ADD_INFO_FIELDS_EP = "addinfofields";

    private List<SelectedDocument> selectedDocuments;

    protected Map<String, RequeteurWidgetDescription> widgetDescriptionRegistry;

    protected Map<String, RequeteurAddInfoField> addInfoFieldsRegistry;

    @Override
    public void activate(ComponentContext context) {
        selectedDocuments = new ArrayList<>();
        widgetDescriptionRegistry = new HashMap<>();
        addInfoFieldsRegistry = new HashMap<>();
    }

    @Override
    public void deactivate(ComponentContext context) {
        selectedDocuments = null;
    }

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        if (SELECTED_DOCUMENTS_EP.equals(extensionPoint)) {
            SelectedDocument selectedDocument = (SelectedDocument) contribution;
            addSelectedDocument(selectedDocument);
        }
        if (WIDGET_DESCRIPTIONS_EP.equals(extensionPoint)) {
            RequeteurWidgetDescription widgetDescription = (RequeteurWidgetDescription) contribution;
            registerWidgetDescription(widgetDescription);
        }
        if (ADD_INFO_FIELDS_EP.equals(extensionPoint)) {
            RequeteurAddInfoField addInfoField = (RequeteurAddInfoField) contribution;
            registerAddInfoField(addInfoField);
        }
    }

    @Override
    public void start(ComponentContext context) {}

    @Override
    public void unregisterContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        if (SELECTED_DOCUMENTS_EP.equals(extensionPoint)) {
            SelectedDocument selectedDocument = (SelectedDocument) contribution;
            removeSelectedDocument(selectedDocument);
        }
        if (WIDGET_DESCRIPTIONS_EP.equals(extensionPoint)) {
            RequeteurWidgetDescription widgetTemplate = (RequeteurWidgetDescription) contribution;
            unregistrerWidgetDescription(widgetTemplate);
        }
        if (ADD_INFO_FIELDS_EP.equals(extensionPoint)) {
            RequeteurAddInfoField addInfoField = (RequeteurAddInfoField) contribution;
            unregisterAddInfoField(addInfoField);
        }
    }

    private void registerWidgetDescription(RequeteurWidgetDescription widgetDescription) {
        String name = widgetDescription.getName();
        if (widgetDescriptionRegistry.containsKey(name)) {
            widgetDescriptionRegistry.remove(name);
        }
        widgetDescriptionRegistry.put(name, widgetDescription);
    }

    private void registerAddInfoField(RequeteurAddInfoField addInfoField) {
        String name = addInfoField.getName();
        if (addInfoFieldsRegistry.containsKey(name)) {
            addInfoFieldsRegistry.remove(name);
        }
        addInfoFieldsRegistry.put(name, addInfoField);
    }

    private void unregisterAddInfoField(RequeteurAddInfoField addInfoField) {
        String name = addInfoField.getName();
        addInfoFieldsRegistry.remove(name);
    }

    private void unregistrerWidgetDescription(RequeteurWidgetDescription widgetDescription) {
        String name = widgetDescription.getName();
        widgetDescriptionRegistry.remove(name);
    }

    private void removeSelectedDocument(SelectedDocument selectedDocument) {
        selectedDocuments.remove(selectedDocument);
    }

    protected void addSelectedDocument(SelectedDocument selectedDocument) {
        selectedDocuments.add(selectedDocument);
    }

    @Override
    public Collection<RequeteurWidgetDescription> getFieldsDescription() throws Exception {
        List<RequeteurWidgetDescription> results = new ArrayList<>();
        for (SelectedDocument selectedDocument : selectedDocuments) {
            results.addAll(selectedDocument.createWidgetDescriptions(getRequiredService(SchemaManager.class)));
        }
        return getSortedWidgetDescriptionList(results);
    }

    @Override
    public URL generateWidgets() throws Exception {
        //        RequeteurContributionBuilderLegacyImpl builder = new RequeteurContributionBuilderLegacyImpl(
        //            "generated_mgpp_contrib",
        //            "fr.dila.solonmgpp.web.widget.generatedrequeteexperte",
        //            "incremental_smart_query_selection_mgpp",
        //            "incremental_smart_query_selection_layout_mgpp"
        //        );
        //        Collection<RequeteurWidgetDescription> descriptions = getSortedWidgetDescriptionList(
        //            widgetDescriptionRegistry.values()
        //        );
        //        return builder.createRequeteurContribution(descriptions);
        return null;
    }

    /**
     * Renvoie une liste de description de widget triée
     * @param initialCollection la collection de widget initiale
     * @return une liste de widget avec les informations supplémentaires concernant le type, la catégory, etc ...
     */
    protected List<RequeteurWidgetDescription> getSortedWidgetDescriptionList(
        Collection<RequeteurWidgetDescription> initialCollection
    ) {
        return CollectionHelper.asSortedList(initialCollection, new CategoryWidgetDescriptionComparator());
    }

    /**
     * Ajoute à chaque widget les informations supplémentaires possible et met à jour le registre de description
     * @return
     */
    private void refreshExtraInformationForWidgetDescriptionRegistry() {
        for (RequeteurWidgetDescription description : widgetDescriptionRegistry.values()) {
            if (addInfoFieldsRegistry != null && addInfoFieldsRegistry.containsKey(description.getName())) {
                RequeteurAddInfoField addInfoField = addInfoFieldsRegistry.get(description.getName());
                if (addInfoField.getNewType() != null) {
                    description.setType(addInfoField.getNewType());
                }
                if (addInfoField.getNewCategory() != null) {
                    description.setCategory(addInfoField.getNewCategory());
                }
                description.setExtraProperties(addInfoField.getProperties());
            }
        }
    }

    /**
     * Libère la mémoire une fois les widgets générés
     */
    protected void reset() {
        widgetDescriptionRegistry = null;
        selectedDocuments = null;
        addInfoFieldsRegistry = null;
    }

    @Override
    public Map<String, RequeteurWidgetDescription> getWigetDescriptionRegistry() {
        return widgetDescriptionRegistry;
    }

    @Override
    public List<String> getCategories() {
        Set<String> categories = new HashSet<>();
        categories.add(STRequeteurExpertConstants.REQUETEUR_EXPERT_ALL_CATEGORY);
        widgetDescriptionRegistry.values().forEach(widget -> categories.add(widget.getCategory()));
        return CollectionHelper.asSortedList(categories);
    }
}
