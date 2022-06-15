package fr.dila.solonmgpp.api.constant;

import static fr.dila.st.api.constant.STConstant.CASE_MANAGEMENT_PATH;
import static fr.dila.st.api.constant.STPathConstant.PATH_SEP;
import static java.lang.String.join;

import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;

public final class ModeleCourrierConstants {
    public static final String MODELE_COURRIER_ROOT_NAME = "modele-courrier-root";
    public static final String MODELE_COURRIER_ROOT_PATH = join(
        PATH_SEP,
        CASE_MANAGEMENT_PATH,
        MODELE_COURRIER_ROOT_NAME
    );
    public static final DocumentRef MODELE_COURRIER_ROOT_PATH_REF = new PathRef(MODELE_COURRIER_ROOT_PATH);

    private ModeleCourrierConstants() {}
}
