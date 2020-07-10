package fr.dila.solonepg.elastic.models.tests;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class ModelsUtils {
	public static String getFileContent(String fileName, ClassLoader classLoader) {

		String result = "";

		try {
			result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}
}
