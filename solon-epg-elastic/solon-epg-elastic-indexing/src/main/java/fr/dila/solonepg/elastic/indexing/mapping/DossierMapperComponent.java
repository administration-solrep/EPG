package fr.dila.solonepg.elastic.indexing.mapping;

import fr.dila.st.core.proxy.ServiceEncapsulateComponent;

public class DossierMapperComponent extends ServiceEncapsulateComponent<IDossierMapper, DossierMapper> {

    public DossierMapperComponent() {
        super(IDossierMapper.class, new DossierMapper());
    }
}
