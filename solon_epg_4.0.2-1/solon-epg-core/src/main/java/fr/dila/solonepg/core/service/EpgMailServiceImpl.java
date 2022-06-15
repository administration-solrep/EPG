package fr.dila.solonepg.core.service;

import fr.dila.st.api.service.STMailService;
import fr.dila.st.core.service.STMailServiceImpl;

public class EpgMailServiceImpl extends STMailServiceImpl implements STMailService {

    @Override
    protected String getMailArchiveSuffix() {
        return "SOLON-EPG_";
    }
}
