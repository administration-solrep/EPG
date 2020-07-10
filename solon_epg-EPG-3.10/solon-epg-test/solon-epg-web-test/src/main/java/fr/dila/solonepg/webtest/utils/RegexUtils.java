package fr.dila.solonepg.webtest.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

public class RegexUtils {

	public static String getGroupMatch(Pattern regexp, String str) {
		Matcher m = regexp.matcher(str);
		Assert.assertTrue(m.matches());
		Assert.assertEquals(1, m.groupCount());
		return m.group(1);		
	}
}
