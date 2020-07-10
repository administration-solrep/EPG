package fr.dila.solonepg.webtest;

import java.util.Calendar;

import fr.dila.solonepg.webtest.common.AbstractEpgWebTest;

public abstract class AbstractSolonSuite extends AbstractEpgWebTest {
    
    // Mise en paramètre de l'année courante (attention, en 3000, ça ne marchera plus !)
    protected static String anneeCourante = String.format("%02d", Calendar.getInstance().get(Calendar.YEAR) % 100);
    
}
