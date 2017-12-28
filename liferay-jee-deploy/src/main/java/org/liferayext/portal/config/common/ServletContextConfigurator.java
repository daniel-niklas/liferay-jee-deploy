package org.liferayext.portal.config.common;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.xml.bind.JAXB;

import org.liferayext.portal.config.dd.PortletApp;

public abstract class ServletContextConfigurator
{
  protected final ServletContext ctx;

  protected ServletContextConfigurator(ServletContext ctx)
  {
    this.ctx = ctx;
  }

  protected void closeQuietly(InputStream in)
  {
    if (in != null)
    {
      try
      {
        in.close();
      }
      catch (IOException e)
      {
      }
    }
  }

  public abstract void configure() throws ServletException;

  protected <T> Class<? extends T> findClass(String className, Class<T> type)
  {
    try
    {
      return Class.forName(className, false, ctx.getClassLoader()).asSubclass(
          type);
    }
    catch (ClassCastException e)
    {
      ctx.log("Klasse " + className + " ist keine Unterklasse von "
          + type.getName());
      return null;
    }
    catch (ClassNotFoundException e)
    {
      ctx.log("Klasse " + className + " nicht gefunden");
      return null;
    }
  }

  protected Class<?> findClass(String className)
  {
    try
    {
      return Class.forName(className, false, ctx.getClassLoader());
    }
    catch (ClassNotFoundException e)
    {
      ctx.log("Klasse " + className + " nicht gefunden");
      return null;
    }
  }

  protected ClassLoader findClassLoader(String className)
  {
    try
    {
      return Class.forName(className, false, ctx.getClassLoader())
          .getClassLoader();
    }
    catch (ClassNotFoundException e)
    {
      return null;
    }
  }

  protected boolean setInitParameter(String name, String value)
  {
    if (!ctx.setInitParameter(name, value))
    {
      String currentValue = ctx.getInitParameter(name);
      if (!currentValue.equals(ctx.getInitParameter(name)))
      {
        ctx.log("Init-Parameter " + name + " -> " + value
            + " kann nicht gesetzt werden (aktueller Wert: " + currentValue
            + ").");
        return false;
      }
    }

    ctx.log("Init-Parameter " + name + " -> " + value);
    return true;
  }

  protected FilterRegistration findFilter(Class<? extends Filter> filterClass)
  {
    for (FilterRegistration filterRegistration : ctx.getFilterRegistrations()
        .values())
    {
      if (filterClass.getName().equals(filterRegistration.getClassName()))
      {
        return filterRegistration;
      }
    }
    return null;
  }

  protected ServletRegistration findServlet(
      Class<? extends Servlet> servletClass)
  {
    for (ServletRegistration servletRegistration : ctx
        .getServletRegistrations().values())
    {
      if (servletClass.getName().equals(servletRegistration.getClassName()))
      {
        return servletRegistration;
      }
    }
    return null;
  }

  protected PortletApp readPortletApp()
  {
    PortletApp portletApp = null;
    InputStream portletAppIn = ctx.getResourceAsStream("/WEB-INF/portlet.xml");
    if (portletAppIn != null)
    {
      try
      {
        portletApp = JAXB.unmarshal(portletAppIn, PortletApp.class);
      }
      finally
      {
        closeQuietly(portletAppIn);
      }
    }
    return portletApp;
  }
}