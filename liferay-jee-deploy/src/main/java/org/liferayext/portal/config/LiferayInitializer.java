package org.liferayext.portal.config;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Dieser {@link ServletContainerInitializer} delegiert die Konfiguration der
 * Portlet-Anwendung an eine Instanz von {@link LiferayConfigurator}.
 * 
 * @author moeller
 */
public class LiferayInitializer implements ServletContainerInitializer
{
  @Override
  public void onStartup(Set<Class<?>> c, ServletContext ctx)
      throws ServletException
  {
    LiferayConfigurator configurator = new LiferayConfigurator(ctx);
    configurator.configure();
  }
}
