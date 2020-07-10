package fr.dila.solonepg.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Afficheur de propriétés (@see TestAdapterContentPrinter)
 * @author jgomez
 *
 */
public class PropertiesPrinter {
	
	public void print(Object impl,Class<?> interf)
			throws IllegalAccessException, InvocationTargetException {
		List<Method> getters = getPropGetters(interf);
    	for (Method getter: getters){
    		getter.invoke(impl);
    	}
	}


	private List<Method> getPropGetters(Class<?> interf) {
	    Method[] allMethods = interf.getDeclaredMethods();
	    List<Method> getters = new ArrayList<Method>();
	    for (Method m : allMethods) {
	    	if (m.getName().startsWith("get") || m.getName().startsWith("is")){
	    		if (m.getTypeParameters().length == 0){
	    			getters.add(m);
	    		}
	    	}
	    }
	    return getters;
	}
}
