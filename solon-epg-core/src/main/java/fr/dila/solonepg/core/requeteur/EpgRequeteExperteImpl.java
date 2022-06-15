package fr.dila.solonepg.core.requeteur;

import fr.dila.solonepg.api.constant.EpgRequeteConstants;
import fr.dila.solonepg.api.enumeration.QueryType;
import fr.dila.solonepg.api.requeteur.EpgRequeteExperte;
import fr.dila.st.api.constant.STRequeteConstants;
import fr.dila.st.core.requeteur.RequeteExperteImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import org.nuxeo.ecm.core.api.DocumentModel;

public class EpgRequeteExperteImpl extends RequeteExperteImpl implements EpgRequeteExperte {

    public EpgRequeteExperteImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.valueOf(
            PropertyUtil.getStringProperty(
                document,
                STRequeteConstants.SMART_FOLDER_SCHEMA,
                EpgRequeteConstants.SMART_FOLDER_QUERY_TYPE_PROP
            )
        );
    }

    @Override
    public void setQueryType(QueryType queryType) {
        PropertyUtil.setProperty(
            document,
            STRequeteConstants.SMART_FOLDER_SCHEMA,
            EpgRequeteConstants.SMART_FOLDER_QUERY_TYPE_PROP,
            queryType.name()
        );
    }
}
