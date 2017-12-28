package org.liferayext.portal.config;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Dieser Listener stellt den Session-Timeout aus.
 */
public class SessionConfiguratorListener implements HttpSessionListener
{
  @Override
  public void sessionCreated(HttpSessionEvent se)
  {
    HttpSession session = se.getSession();
    session.setMaxInactiveInterval(-1);
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se)
  {
  }
}
