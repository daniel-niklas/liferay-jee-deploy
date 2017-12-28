package org.liferayext.portal.config.dd;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "portlet-app")
public class PortletApp
{
  @XmlElement(name = "portlet")
  private List<Portlet> portlets = new LinkedList<Portlet>();

  public List<Portlet> getPortlets()
  {
    return portlets;
  }
}