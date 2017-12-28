package org.liferayext.portal.config;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * This {@link ServletContainerInitializer} delegates the configuration
 * ofdelegiert die Konfiguration der the portlet webapplication to an instance
 * of {@link LiferayConfigurator}.
 */
public class LiferayInitializer implements ServletContainerInitializer {
	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		LiferayConfigurator configurator = new LiferayConfigurator(ctx);
		configurator.configure();
	}
}
