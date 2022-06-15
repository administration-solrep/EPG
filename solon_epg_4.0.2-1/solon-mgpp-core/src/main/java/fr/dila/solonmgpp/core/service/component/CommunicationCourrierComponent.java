package fr.dila.solonmgpp.core.service.component;

import fr.dila.solonmgpp.api.service.CommunicationCourrierService;
import fr.dila.solonmgpp.core.service.CommunicationCourrierServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class CommunicationCourrierComponent
    extends ServiceEncapsulateComponent<CommunicationCourrierService, CommunicationCourrierServiceImpl> {

    public CommunicationCourrierComponent() {
        super(CommunicationCourrierService.class, new CommunicationCourrierServiceImpl());
    }
}
