package fr.dila.ng.edit.service;

import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class EditServiceComponent extends ServiceEncapsulateComponent<EditService, EditServiceImpl> {

    public EditServiceComponent() {
        super(EditService.class, new EditServiceImpl());
    }
}
