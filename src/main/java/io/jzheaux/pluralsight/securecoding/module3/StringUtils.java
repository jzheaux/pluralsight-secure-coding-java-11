package io.jzheaux.pluralsight.securecoding.module3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StringUtils {
	private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

	public static boolean isEmpty(String str) {
		logger.trace("Testing {} for emptiness", str);
		return str == null || str.length() == 0;
	}
}
