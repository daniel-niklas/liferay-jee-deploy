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
 * Diese Klasse konfiguriert eine Portlet-Anwendung für Liferay.
 * <em>Wichtig:</em> die Portlet-Anwendung wird nur konfiguriert, wenn der
 * Init-Parameter {@value #ENABLE_PARAM} den Wert {@value #ENABLE_PARAM_ON}
 * enthält.
 * </p>
 * 
 * <p>
 * Folgende Einstellungen werden vorgenommen:
 * <ul>
 * <li>Für jedes Portlet wird ein Liferay-Portlet-Servlet registriert und der
 * Liferay-Plugin-Context-Listener wird registriert.</li>
 * <li>Der Session-Timeout wird ausgestellt.<br/>
 * (das gilt für die Sessions der Portlet-Webanwendungen. Dieser werden
 * automatisch invalidiert/beendet, sobald die Session der ROOt-Webanwendung
 * beendet wird. So sind alle HTTP-Sessions auf die ROOT-Session
 * "aggregiert".</li>
 * </ul>
 * </p>
 */
public class LiferayConfigurator extends ServletContextConfigurator {
	public static final String ENABLE_PARAM_ON = "on";

	/**
	 * Dieser Init-Parameter muß auf {@value #ENABLE_PARAM_ON} gesetzt werden,
	 * damit die Portlet-Anwendung konfiguriert wird.
	 */
	public static final String ENABLE_PARAM = "org.liferayext.portal.config.jeedeploy";

	/*
	 * Siehe PortalUtil.getJsSafePortletId().
	 */
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
			ctx.log("Liferay ist nicht verfügbar.");
			// kein Liferay verfügbar
			return;
		}

		PortletApp portletApp = readPortletApp();
		if (portletApp == null) {
			ctx.log("Portlet Deskriptor ist nicht vorhanden.");
		} else {
			for (Portlet portlet : portletApp.getPortlets()) {
				String liferayPortletId = getJsSafePortletId(portlet.getPortletName());
				String servletName = liferayPortletId + " Servlet";
				ServletRegistration.Dynamic portletServlet = ctx.addServlet(servletName, portletServletClass);
				portletServlet.setLoadOnStartup(1);
				portletServlet.setInitParameter("portlet-class", portlet.getPortletClass());
				portletServlet.addMapping("/" + liferayPortletId + "/*");
				ctx.log("Portlet " + portlet.getPortletName() + " registriert.");
			}
		}

		ctx.log("Liferay Plugin Context Listener wird registriert.");
		ctx.addListener(pluginContextListenerClass);
	}

	private void configureSessionManagement() {
		ctx.log("Session-Timeout wird ausgestellt.");
		ctx.addListener(SessionConfiguratorListener.class);

		ctx.log("Servlet für Portlet-Session-Invalidierung wird installiert.");
		Class<? extends Servlet> servletClass = findClass(
				"de.continentale.portal.dynamicsite.PortletSessionInvalidationServlet", Servlet.class);
		if (servletClass != null) {
			ctx.addServlet("portlet-session-invalidation-servlet", servletClass);
		}
	}
}
