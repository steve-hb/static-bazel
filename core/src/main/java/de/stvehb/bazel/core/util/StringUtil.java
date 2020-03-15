package de.stvehb.bazel.core.util;

import java.util.regex.Pattern;

public class StringUtil {

	private static final Pattern FORMAT = Pattern.compile("(\\{})");

	public static String format(String format, Object... args) {
		return String.format(FORMAT.matcher(format).replaceAll("%s"), args);
	}

}
