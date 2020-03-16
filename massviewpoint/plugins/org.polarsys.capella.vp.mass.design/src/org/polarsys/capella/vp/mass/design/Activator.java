/*******************************************************************************
 * Copyright (c) 2006, 2020 Thales Global Services
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * 
 *   Contributors:
 *      Thales - initial API and implementation
 *      Obeo - Code improvement
 ******************************************************************************/
package org.polarsys.capella.vp.mass.design;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.componentization.ViewpointRegistry;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.polarsys.capella.vp.mass.design.service.massListener.MassChangePreCommitListener;
import org.polarsys.capella.vp.mass.design.service.massListener.MassSessionListener;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	MassSessionListener sessionListener;

	// The plug-in ID
	public static final String PLUGIN_ID = "org.polarsys.capella.vp.mass.design";

	// The shared instance
	private static Activator plugin;

	private static Set<Viewpoint> viewpoints;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
		viewpoints = new HashSet<Viewpoint>();
		viewpoints.addAll(ViewpointRegistry.getInstance().registerFromPlugin(PLUGIN_ID + "/description/mass.odesign"));

		// add a listener to the session manager so that the mass of the elements of the
		// model can be calculated when a new session is opened
		sessionListener = new MassSessionListener();
		SessionManager.INSTANCE.addSessionsListener(sessionListener);

		// for each session already opened, a pre-commit listener is installed so the
		// mass of the elements of the model can be calculated after specific events
		Collection<Session> sessions = SessionManager.INSTANCE.getSessions();
		for (Session session : sessions) {
			MassChangePreCommitListener massListener = new MassChangePreCommitListener();
			TransactionalEditingDomain transDomain = session.getTransactionalEditingDomain();
			if(transDomain != null) {
				transDomain.addResourceSetListener(massListener);
				// the listener and the session its attached to are saved in order to unregister
				// him when the viewpoint is deselected
				sessionListener.registerPreCommitListener(session, massListener);
				sessionListener.computeMass(session);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		SessionManager.INSTANCE.removeSessionsListener(sessionListener);

		// for each session opened, removes the pre-commit listener
		Collection<Session> sessions = SessionManager.INSTANCE.getSessions();
		for (Session session : sessions) {
			EList<Adapter> adapters = session.getTransactionalEditingDomain().getResourceSet().eAdapters();
			for (Adapter adapter : adapters) {
				if (adapter instanceof MassChangePreCommitListener) {
					session.getTransactionalEditingDomain()
							.removeResourceSetListener((MassChangePreCommitListener) adapter);
				}
			}
		}

		plugin = null;
		if (viewpoints != null) {
			for (final Viewpoint viewpoint : viewpoints) {
				ViewpointRegistry.getInstance().disposeFromPlugin(viewpoint);
			}
			viewpoints.clear();
			viewpoints = null;
		}
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
}
