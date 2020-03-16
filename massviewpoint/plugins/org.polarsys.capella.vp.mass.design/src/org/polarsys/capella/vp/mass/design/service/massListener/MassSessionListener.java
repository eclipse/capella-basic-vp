/*******************************************************************************
 * Copyright (c) 2020 Obeo
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * 
 *   Contributors:
 *      Obeo - initial API and implementation
 ******************************************************************************/
package org.polarsys.capella.vp.mass.design.service.massListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManagerListener;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.polarsys.capella.core.data.capellamodeller.impl.ProjectImpl;
import org.polarsys.capella.core.data.capellamodeller.impl.SystemEngineeringImpl;
import org.polarsys.capella.core.data.capellamodeller.util.CapellamodellerResourceImpl;
import org.polarsys.capella.core.data.pa.PhysicalComponentPkg;
import org.polarsys.capella.core.data.pa.impl.PhysicalArchitectureImpl;
import org.polarsys.capella.vp.mass.design.service.massSwitch.TopDownComputeMassPaSwitch;

/**
 * This session manager listener is used to install/remove a Mass Change
 * PreCommit Listener to every new session and to start the mass computation of
 * the elements of this session
 *
 */
public class MassSessionListener implements SessionManagerListener {

	Map<Session, MassChangePreCommitListener> sessionsMassPreCommitListener = new HashMap<>();

	@Override
	public void notifyAddSession(Session newSession) {
		MassChangePreCommitListener massListener = new MassChangePreCommitListener();
		newSession.getTransactionalEditingDomain().addResourceSetListener(massListener);
		registerPreCommitListener(newSession, massListener);

		computeMass(newSession);
	}

	@Override
	public void notifyRemoveSession(Session removedSession) {
	}

	@Override
	public void viewpointSelected(Viewpoint selectedSirius) {
	}

	@Override
	public void viewpointDeselected(Viewpoint deselectedSirius) {

		// for each session opened, remove the pre-commit listener
		Set<Entry<Session, MassChangePreCommitListener>> setSessionsMassPreCommitListener = sessionsMassPreCommitListener
				.entrySet();
		Iterator<Entry<Session, MassChangePreCommitListener>> it = setSessionsMassPreCommitListener.iterator();
		while (it.hasNext()) {
			Entry<Session, MassChangePreCommitListener> e = it.next();
			TransactionalEditingDomain transDomain = e.getKey().getTransactionalEditingDomain();
			if(transDomain != null) {
				transDomain.removeResourceSetListener(e.getValue());
			}
		}
	}

	@Override
	public void notify(Session updated, int notification) {
	}

	/**
	 * Saves a session and its mass listener
	 * 
	 * @param session
	 * @param preCommit
	 */
	public void registerPreCommitListener(Session session, MassChangePreCommitListener preCommit) {
		sessionsMassPreCommitListener.put(session, preCommit);
	}

	/**
	 * Compute the mass of the elements of a session
	 * 
	 * @param the session
	 */
	public void computeMass(Session session) {
		TransactionalEditingDomain domain = session.getTransactionalEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {

			@Override
			protected void doExecute() {
				TopDownComputeMassPaSwitch topDownSwitch = new TopDownComputeMassPaSwitch();

				ArrayList<PhysicalComponentPkg> physicalComponentPkgs = retrievesPhysicalComponentPkgFromSession(session);
				
				for(PhysicalComponentPkg physicalComponentPkg : physicalComponentPkgs) {
					topDownSwitch.doSwitch(physicalComponentPkg);
				}
			}

		});

	}

	/**
	 * Retrieves the root PhysicalComponentPkg of the Physical Architecture part of a capella model from its session
	 * @param session
	 * @return the PhysicalComponentPkg containing the components of the physical architecture of a capella model
	 */
	private ArrayList<PhysicalComponentPkg> retrievesPhysicalComponentPkgFromSession(Session session) {
		Collection<Resource> resources = session.getSemanticResources();
		ArrayList<PhysicalComponentPkg> physicalComponentPkg = new ArrayList<>();

		resources.stream()
			.filter(resource -> resource instanceof CapellamodellerResourceImpl)
			.forEach((resource) -> {
				resource.getContents().stream()
					.filter(content -> content instanceof ProjectImpl)
					.forEach((content) -> {
						((ProjectImpl) content).getOwnedModelRoots().stream()
							.filter(modelRoot -> modelRoot instanceof SystemEngineeringImpl)
							.forEach((modelRoot) -> {
								((SystemEngineeringImpl) modelRoot).getOwnedArchitectures().stream()
								.filter(architecture -> architecture instanceof PhysicalArchitectureImpl)
								.forEach((physicalArchitecture) -> {
									physicalComponentPkg.add(((PhysicalArchitectureImpl) physicalArchitecture)
										.getOwnedPhysicalComponentPkg()) ;
									});
							;
						});
				;
			});
			;
		});
		return physicalComponentPkg;
	}

}
