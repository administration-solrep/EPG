package fr.dila.solonmgpp.core.service;

import static fr.dila.solonmgpp.api.constant.MgppDocTypeConstants.MODELE_COURRIER_TYPE;
import static fr.dila.solonmgpp.api.constant.ModeleCourrierConstants.MODELE_COURRIER_ROOT_NAME;
import static fr.dila.solonmgpp.api.constant.ModeleCourrierConstants.MODELE_COURRIER_ROOT_PATH_REF;
import static fr.dila.st.api.constant.STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.nuxeo.ecm.core.api.security.SecurityConstants.READ_WRITE;

import com.google.common.collect.Lists;
import fr.dila.solonmgpp.api.domain.ModeleCourrier;
import fr.dila.st.core.exception.STValidationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.security.Access;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Deploy("fr.sword.naiad.nuxeo.ufnxql.core")
@Deploy("fr.dila.solonmgpp.core:OSGI-INF/solonmgpp-core-type-contrib.xml")
@Deploy("fr.dila.solonmgpp.core:OSGI-INF/solonmgpp-schema-contrib.xml")
@Deploy("fr.dila.solonmgpp.core:OSGI-INF/solonmgpp-adapter-contrib.xml")
@Deploy("fr.dila.solonmgpp.core:OSGI-INF/solonmgpp-operation-contrib.xml")
@Deploy("fr.dila.solonmgpp.core:OSGI-INF/service/modele-courrier-framework.xml")
@Deploy("fr.dila.solonmgpp.core:OSGI-INF/solonmgpp-event-contrib.xml")
@Ignore("Pipeline faied with exception 'java.lang.IllegalArgumentException: URI is not hierarchical' for DOCX_URL")
public class ModeleCourrierServiceIT extends AbstractCourrierIT {

    @Test
    public void testPermissionsOnModeleCourrierRootFolder() {
        assertThat(session.exists((MODELE_COURRIER_ROOT_PATH_REF))).isTrue();
        DocumentModel doc = session.getDocument(MODELE_COURRIER_ROOT_PATH_REF);
        Access access = session.getACP(doc.getRef()).getAccess(ADMIN_FONCTIONNEL_GROUP_NAME, READ_WRITE);
        assertThat(access).isEqualTo(Access.GRANT);
    }

    @Test
    public void testValidateModeleCourrierCreate() throws IOException, URISyntaxException {
        // modele valide
        ModeleCourrier modele = session
            .createDocumentModel(MODELE_COURRIER_ROOT_NAME, "modele", MODELE_COURRIER_TYPE)
            .getAdapter(ModeleCourrier.class);
        modele.setTitle("title");
        File docxFile = new File(DOCX_URL.toURI());
        modele.setFile(docxFile);
        modele.setTypesCommuncation(singletonList("typeComm"));

        Throwable exc = catchThrowable(() -> mcService.validateModeleCourrier(session, modele));
        assertThat(exc).isNull();

        // test null title
        modele.setTitle(null);
        exc = catchThrowable(() -> mcService.validateModeleCourrier(session, modele));
        assertThat(exc).isInstanceOf(STValidationException.class);

        // test null file
        modele.setTitle("title");
        modele.setFile(null);
        exc = catchThrowable(() -> mcService.validateModeleCourrier(session, modele));
        assertThat(exc).isInstanceOf(STValidationException.class);

        // test incorrect mimetype
        modele.setFile(File.createTempFile("temp", ".txt"));
        exc = catchThrowable(() -> mcService.validateModeleCourrier(session, modele));
        assertThat(exc).isInstanceOf(STValidationException.class);

        // test null types Communication
        modele.setFile(docxFile);
        modele.setTypesCommuncation(null);
        exc = catchThrowable(() -> mcService.validateModeleCourrier(session, modele));
        assertThat(exc).isInstanceOf(STValidationException.class);
    }

    @Test
    public void testValidateModeleCourrierUpdate() throws IOException, URISyntaxException {
        ModeleCourrier modele1 = createModele("modele1");
        createModele("modele2");
        session.save();

        ModeleCourrier newModele = session
            .createDocumentModel(MODELE_COURRIER_ROOT_NAME, "modele3", MODELE_COURRIER_TYPE)
            .getAdapter(ModeleCourrier.class);
        newModele.setTitle("modele1");
        newModele.setFile(new File(DOCX_URL.toURI()));
        newModele.setTypesCommuncation(singletonList("typeComm"));

        // update avec nouveau titre
        Throwable exc = catchThrowable(() -> mcService.validateModeleCourrier(session, newModele, modele1));
        assertThat(exc).isNull();

        // update avec titre existant
        newModele.setTitle("modele2");
        exc = catchThrowable(() -> mcService.validateModeleCourrier(session, newModele, modele1));
        assertThat(exc).isInstanceOf(STValidationException.class);
    }

    @Test
    public void testCreateModeleCourrier() {
        ModeleCourrier modele1 = createModele("modele1");
        session.save();
        assertThat(modele1).isNotNull();
        assertThat(modele1.getTitle()).isEqualTo("modele1");
        assertThat(modele1.getFile()).isNotNull();
        assertThat(modele1.getTypesCommuncation()).contains("typeComm");

        // test duplicate title
        Throwable exc = catchThrowable(() -> createModele("modele1"));
        assertThat(exc).isInstanceOf(STValidationException.class);

        ModeleCourrier modele2 = createModele("modele2");
        assertThat(modele2).isNotNull();
    }

    @Test
    public void testUpdateModeleCourrier() {
        ModeleCourrier modele = createModele("modele1");
        session.save();
        assertThat(modele).isNotNull();

        modele.setTitle("modele1bis");
        modele.setTypesCommuncation(Lists.newArrayList("tc1", "tc2"));

        modele = session.saveDocument(modele.getDocument()).getAdapter(ModeleCourrier.class);

        assertThat(modele.getTitle()).isEqualTo("modele1bis");
        assertThat(modele.getFile()).isNotNull();
        assertThat(modele.getTypesCommuncation()).contains("tc1", "tc2");
    }

    @Test
    public void testGetModeleCourrier() {
        createModele("modele");
        session.save();

        Optional<ModeleCourrier> modele = mcService.getModeleCourrierFromModeleName(session, "modele");
        assertThat(modele).isPresent();
        assertThat(modele.get().getTitle()).isEqualTo("modele");
    }

    @Test
    public void testGetModelesCourrier() {
        createModele("modele1");
        createModele("modele2");
        createModele("modele3");
        session.save();

        List<ModeleCourrier> modeles = mcService.getModelesCourrier(session);
        assertThat(modeles).hasSize(3);
    }
}
