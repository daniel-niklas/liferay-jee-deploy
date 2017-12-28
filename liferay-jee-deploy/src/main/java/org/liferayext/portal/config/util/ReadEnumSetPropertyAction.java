package org.liferayext.portal.config.util;

import java.security.PrivilegedAction;
import java.util.EnumSet;

import javax.servlet.ServletContext;

public class ReadEnumSetPropertyAction<T extends Enum<T>> implements PrivilegedAction<EnumSet<T>> {
	private final ServletContext ctx;
	private final Class<T> type;

	public ReadEnumSetPropertyAction(ServletContext ctx, Class<T> type) {
		this.ctx = ctx;
		this.type = type;
	}

	@Override
	public EnumSet<T> run() {
		EnumSet<T> set = EnumSet.noneOf(type);
		try {
			String prop = System.getProperty(type.getName());
			if (prop != null) {
				for (String value : prop.split(",")) {
					set.add(Enum.valueOf(type, value));
				}
			}
		} catch (Throwable th) {
			ctx.log("Property " + type.getName() + " kann nicht ausgewertet werden.", th);
		}
		return set;
	}
}
