package fr.dila.solonepg.ui.th.constants;

import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.ui.AbstractTestConstants;
import org.junit.Test;

public class MgppTemplateConstantsTest extends AbstractTestConstants<MgppTemplateConstants> {

    @Override
    protected Class<MgppTemplateConstants> getConstantClass() {
        return MgppTemplateConstants.class;
    }

    @Test
    public void constantsShouldNotHaveDuplicates() {
        verifyDuplicatesWithSTTemplateConstants();
        verifyDuplicatesWithClasses(SSTemplateConstants.class, EpgTemplateConstants.class, PanTemplateConstants.class);
    }
}
