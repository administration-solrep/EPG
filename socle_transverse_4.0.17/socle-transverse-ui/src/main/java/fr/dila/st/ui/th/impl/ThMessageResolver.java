package fr.dila.st.ui.th.impl;

import java.util.Locale;
import java.util.Map;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templateresource.ITemplateResource;

public class ThMessageResolver extends StandardMessageResolver {

    @Override
    protected Map<String, String> resolveMessagesForTemplate(
        String template,
        ITemplateResource templateResource,
        Locale locale
    ) {
        templateResource = templateResource.relative("/messages/");
        return super.resolveMessagesForTemplate(template, templateResource, locale);
    }
}
