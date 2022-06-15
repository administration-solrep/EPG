package fr.dila.solonepg.ui.services.component;

import fr.dila.solonepg.ui.services.IndexationUIService;
import fr.dila.solonepg.ui.services.impl.IndexationUIServiceImpl;
import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class IndexationUIComponent extends ServiceEncapsulateComponent<IndexationUIService, IndexationUIServiceImpl> {

    public IndexationUIComponent() {
        super(IndexationUIService.class, new IndexationUIServiceImpl());
    }
}
