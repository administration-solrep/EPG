package fr.dila.solonepg.ui.bean.pan.birt;

import fr.dila.solonepg.ui.constants.pan.PanConstants;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BirtMapping {
    PanConstants.BirtParam birtParam();
}
