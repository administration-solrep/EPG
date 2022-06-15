package fr.dila.solonepg.ui.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.enums.EpgColumnEnum;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.st.ui.bean.IColonneInfo;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({ EpgUIServiceLocator.class, ServiceUtil.class, UserSessionHelper.class, WebEngine.class })
@PowerMockIgnore("javax.management.*")
public class EpgDossierListTest {

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule powerMockRule = new PowerMockRule();

    @Mock
    private WebContext webContext;

    @Mock
    private CoreSession coreSession;

    @Mock
    private ProfilUtilisateurService profilUtilisateurService;

    @Mock
    private ProfilUtilisateur profilUtilisateur;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(ServiceUtil.class);
        PowerMockito.mockStatic(UserSessionHelper.class);
        PowerMockito.mockStatic(EpgUIServiceLocator.class);
        PowerMockito.mockStatic(WebEngine.class);

        when(WebEngine.getActiveContext()).thenReturn(webContext);
        when(webContext.getCoreSession()).thenReturn(coreSession);

        when(SolonEpgServiceLocator.getProfilUtilisateurService()).thenReturn(profilUtilisateurService);
        when(profilUtilisateurService.getProfilUtilisateurForCurrentUser(coreSession)).thenReturn(profilUtilisateur);
        when(profilUtilisateur.getNbDossiersVisiblesMax()).thenReturn(10);
    }

    @Test
    public void testConstructor() {
        EpgDossierList dto = new EpgDossierList();

        assertNotNull(dto.getListeColonnes());
        assertEquals(new Integer(0), dto.getNbTotal());
    }

    @Test
    public void testSetter() {
        EpgDossierList dto = new EpgDossierList();
        assertNotNull(dto.getListeColonnes());
        assertEquals(new Integer(0), dto.getNbTotal());

        dto.setNbTotal(5);
        assertEquals(new Integer(5), dto.getNbTotal());
    }

    @Test
    public void getIHMColonnes() {
        EpgDossierList dto = new EpgDossierList();
        dto.buildColonnes(null, null, true, true);

        List<IColonneInfo> lstColonnes = dto.getListeColonnes();
        assertNotNull(lstColonnes);

        DossierListForm form = DossierListForm.newForm();

        lstColonnes = dto.getListeColonnes();
        assertNotNull(lstColonnes);
        assertEquals(58, lstColonnes.size());

        int expectedVisibleColonnes = (int) Stream
            .of(EpgColumnEnum.values())
            .filter(EpgColumnEnum::isAlwaysDisplayed)
            .count();

        assertValidData(lstColonnes, expectedVisibleColonnes, 41);

        IColonneInfo questionCol = lstColonnes.get(0);
        assertNotNull(questionCol);
        assertEquals("label.content.header.numeroNor", questionCol.getLabel());
        assertTrue(questionCol instanceof FilterableColonneInfo);
        assertTrue(questionCol.isVisible());
        assertTrue(questionCol.isSortable());
        assertEquals("numeroNorSort", questionCol.getSortName());
        assertEquals("numeroNorSortHeader", questionCol.getSortId());

        dto.buildColonnes(form, Lists.newArrayList(EpgColumnEnum.MINISTERE_RESP_BORD.name()));
        lstColonnes = dto.getListeColonnes();
        assertNotNull(lstColonnes);
        assertEquals(58, lstColonnes.size());
        assertValidData(lstColonnes, expectedVisibleColonnes, 41);
    }

    private void assertValidData(List<IColonneInfo> lstColonnes, int expectedVisible, int expectedSortable) {
        int actualVisible = 0;
        int actualSortable = 0;

        for (IColonneInfo col : lstColonnes) {
            assertNotNull(col);
            if (col.isVisible()) {
                actualVisible++;
            }

            if (col.isSortable()) {
                actualSortable++;
                assertNotNull(col.getSortName());
                assertNotNull(col.getSortId());
            }
        }

        assertEquals(expectedSortable, actualSortable);
        assertEquals(expectedVisible, actualVisible);
    }
}
