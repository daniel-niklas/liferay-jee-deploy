package org.liferayext.portal.config;

import java.util.EventListener;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.liferayext.portal.config.common.ServletContextConfigurator;
import org.liferayext.portal.config.dd.Portlet;
import org.liferayext.portal.config.dd.PortletApp;

/**
 * <p>
 * This class configures a portlet webapplication for Liferay.</br>
 * <em>Important:</em> the webapplication will only be configured, when the
 * context-param {@value #ENABLE_PARAM} contains {@value #ENABLE_PARAM_ON}.
 * </p>
 * 
 * <p>
 * The followig settings are covered:
 * <ul>
 * <li>register a Liferay servlet for for each portlet</li>
 * <li>register the Liferay-Plugin-Context-Listener
 * <code>com.liferay.portal.kernel.servlet.PluginContextListener</code></li>
 * <li>disable session-timeout<br/>
 * (this is valid for the sessions of the porlet webapplications. These will be
 * automatically ended, when the session of the ROOT application is invalidated.
 * As a result all HTTP-Sessions ar "aggregated" to the lifetime of the
 * ROOT-session.</li>
 * </ul>
 * </p>
 */
public class LiferayConfigurator extends ServletContextConfigurator {
	public static final String ENABLE_PARAM_ON = "on";

	/**
	 * Set this init-parameter to {@value #ENABLE_PARAM_ON} to enable the
	 * automatic configuration.
	 */
	public static final String ENABLE_PARAM = "org.liferayext.portal.config.jeedeploy";

	// see PortalUtil.getJsSafePortletId().
	private static String getJsSafePortletId(String portletName) {
		return portletName.replaceAll("[ -.]", "");
	}

	public LiferayConfigurator(ServletContext ctx) {
		super(ctx);
	}

	public void configure() throws ServletException {
		if (!isEnabled()) {
			return;
		}

		configureLiferayPortlets();

		configureSessionManagement();

	}

	private boolean isEnabled() {
		String enabled = ctx.getInitParameter(ENABLE_PARAM);
		return enabled != null && enabled.trim().toLowerCase().equals(ENABLE_PARAM_ON);
	}

	private void configureLiferayPortlets() throws ServletException {
		Class<? extends Servlet> portletServletClass = findClass("com.liferay.portal.kernel.servlet.PortletServlet",
				Servlet.class);
		Class<? extends EventListener> pluginContextListenerClass = findClass(
				"com.liferay.portal.kernel.servlet.PluginContextListener", EventListener.class);

		if (portletServletClass == null || pluginContextListenerClass == null) {
			ctx.log("Liferay is not available.");
			// kein Liferay verfügbar
			return;
		}

		PortletApp portletApp = readPortletApp();
		if (portletApp == null) {
			ctx.log("no portlet descriptor available.");
		} else {
			for (Portlet portlet : portletApp.getPortlets()) {
				String liferayPortletId = getJsSafePortletId(portlet.getPortletName());
				String servletName = liferayPortletId + " Servlet";
				ServletRegistration.Dynamic portletServlet = ctx.addServlet(servletName, portletServletClass);
				portletServlet.setLoadOnStartup(1);
				portletServlet.setInitParameter("portlet-class", portlet.getPortletClass());
				portletServlet.addMapping("/" + liferayPortletId + "/*");
				ctx.log("Portlet " + portlet.getPortletName() + " registered.");
			}
		}

		ctx.log("Liferay Plugin Context Listener registered.");
		ctx.addListener(pluginContextListenerClass);
	}

	private void configureSessionManagement() {
		ctx.log("Disable session-timeout.");
		ctx.addListener(SessionConfiguratorListener.class);
	}
}
