package org.liferayext.portlet.jsf;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.faces.GenericFacesPortlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.faces.application.ApplicationAssociate;

/**
 * <p>
 * Workaround for https://issues.jboss.org/browse/UNDERTOW-921.
 * </p>
 * 
 * <p>
 * Because of "missing ServletRequestEvent in per dispatcher included servlet"
 * the ApplicationAssociate ist not set properly. This implementation will set
 * set correct instance for each request.
 * </p>
 */
public class ApplicationAssociatePatchedPortlet extends GenericFacesPortlet {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationAssociatePatchedPortlet.class);

	@Override
	protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws PortletException, IOException {
		try {
			setApplicationAssociate();

			super.doView(renderRequest, renderResponse);
		} finally {
			ApplicationAssociate.setCurrentInstance(null);
		}
	}

	@Override
	public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		try {
			setApplicationAssociate();

			super.render(request, response);
		} finally {
			ApplicationAssociate.setCurrentInstance(null);
		}
	}

	private void setApplicationAssociate() {
		try {
			ApplicationAssociate applicationAssociate = (com.sun.faces.application.ApplicationAssociate) FacesContext
					.getCurrentInstance().getExternalContext().getApplicationMap()
					.get("com.sun.faces.ApplicationAssociate");
			ApplicationAssociate.setCurrentInstance(applicationAssociate);
		} catch (Exception e) {
			logger.error("error setting ApplicationAssociate", e);
		}
	}

	@Override
	public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
			throws PortletException, IOException {
		try {
			setApplicationAssociate();

			super.processAction(actionRequest, actionResponse);
		} finally {
			ApplicationAssociate.setCurrentInstance(null);
		}
	}

	@Override
	public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws PortletException, IOException {
		try {
			setApplicationAssociate();

			super.serveResource(resourceRequest, resourceResponse);
		} finally {
			ApplicationAssociate.setCurrentInstance(null);
		}

	}

}