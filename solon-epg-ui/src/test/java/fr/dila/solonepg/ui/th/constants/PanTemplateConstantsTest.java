package fr.dila.solonepg.ui.th.constants;

import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.ui.AbstractTestConstants;
import org.junit.Test;

public class PanTemplateConstantsTest extends AbstractTestConstants<PanTemplateConstants> {

    @Override
    protected Class<PanTemplateConstants> getConstantClass() {
        return PanTemplateConstants.class;
    }

    @Test
    public void constantsShouldNotHaveDuplicates() {
        verifyDuplicatesWithSTTemplateConstants();
        verifyDuplicatesWithClasses(SSTemplateConstants.class, EpgTemplateConstants.class, MgppTemplateConstants.class);
    }
}
