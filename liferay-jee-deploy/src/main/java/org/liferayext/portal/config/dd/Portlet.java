package org.liferayext.portal.config.dd;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "portlet")
public class Portlet
{
  @XmlElement(name = "portlet-name")
  private String portletName;

  @XmlElement(name = "portlet-class")
  private String portletClass;

  public String getPortletName()
  {
    return portletName;
  }

  public String getPortletClass()
  {
    return portletClass;
  }
}
