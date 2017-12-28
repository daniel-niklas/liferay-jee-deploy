/**
 * <p>
 * Dieses Paket stelle die automatische Konfiguration einer Portlet-Anwendung für Liferay bereit.
 * Damit ist das Deployment über den Liferay-Auto-Deployer nicht mehr nötig.
 * </p>
 * <p>
 * Für die Details der Konfiguration siehe {@link org.liferayext.portal.config.LiferayConfigurator}.
 * </p>
 * <p>
 * <em>Installation:</em> diese Bibliothek sollte in das JBOSS-Modul <code>org.jboss.as.web</code>
 * aufgenommen werden und benötigt als Extra-Dependency das Modul <code>javax.xml.bind.api</code>. 
 * </p>
 */
package org.liferayext.portal.config;