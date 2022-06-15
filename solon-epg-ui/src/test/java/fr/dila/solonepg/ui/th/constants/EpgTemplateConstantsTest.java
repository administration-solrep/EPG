package fr.dila.solonepg.ui.th.constants;

import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.ui.AbstractTestConstants;
import org.junit.Test;

public class EpgTemplateConstantsTest extends AbstractTestConstants<EpgTemplateConstants> {

    @Override
    protected Class<EpgTemplateConstants> getConstantClass() {
        return EpgTemplateConstants.class;
    }

    @Test
    public void constantsShouldNotHaveDuplicates() {
        verifyDuplicatesWithSTTemplateConstants();
        verifyDuplicatesWithClasses(SSTemplateConstants.class, MgppTemplateConstants.class, PanTemplateConstants.class);
    }
}
