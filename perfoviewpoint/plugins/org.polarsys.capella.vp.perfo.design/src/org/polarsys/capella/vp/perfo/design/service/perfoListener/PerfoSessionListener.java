/*******************************************************************************
 * Copyright (c) 2021 Obeo
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * 
 *   Contributors:
 *      Obeo - initial API and implementation
 ******************************************************************************/
package org.polarsys.capella.vp.perfo.design.service.perfoListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.business.api.session.SessionManagerListener;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.polarsys.capella.core.data.capellamodeller.impl.ProjectImpl;
import org.polarsys.capella.core.data.capellamodeller.impl.SystemEngineeringImpl;
import org.polarsys.capella.core.data.capellamodeller.util.CapellamodellerResourceImpl;
import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.core.data.la.LogicalArchitecture;
import org.polarsys.capella.core.data.la.impl.LogicalArchitectureImpl;
import org.polarsys.capella.core.data.pa.PhysicalArchitecture;
import org.polarsys.capella.core.data.pa.impl.PhysicalArchitectureImpl;
import org.polarsys.capella.core.model.helpers.FunctionalChainExt;
import org.polarsys.capella.vp.perfo.perfo.PerformanceCriteria;
import org.polarsys.capella.vp.perfo.services.PerformanceServices;

/**
 * This session manager listener is used to install/remove a Performance Change
 * PreCommit Listener to every new session and to start the performance computation of
 * the elements of this session
 * Directly inspired from org.polarsys.capella.vp.mass.design.service.massListener.MassSessionListener
 *
 */
public class PerfoSessionListener implements SessionManagerListener {

	Map<Session, PerfoChangePreCommitListener> sessionsPerfoPreCommitListener = new HashMap<>();

	@Override
	public void notifyAddSession(Session newSession) {
		//check if viewpoint is selected to add listener
		Collection<Viewpoint> viewpoints = newSession.getSelectedViewpoints(false);
		for (Viewpoint viewpoint : viewpoints) {
			if (viewpoint.getName().equals("Perfo_ID")) return;
		}
		
		PerfoChangePreCommitListener perfoListener = new PerfoChangePreCommitListener();
		newSession.getTransactionalEditingDomain().addResourceSetListener(perfoListener);
		registerPreCommitListener(newSession, perfoListener);

		computePerfo(newSession);
	}

	@Override
	public void notifyRemoveSession(Session removedSession) {
	}

	@Override
	public void viewpointSelected(Viewpoint selectedSirius) {
		
		if (!selectedSirius.getName().equals("Perfo_ID")) return;
		
		// for each session already opened, a pre-commit listener is installed so the
		// performance of the elements of the model can be calculated after specific events
		Collection<Session> sessions = SessionManager.INSTANCE.getSessions();
		for (Session session : sessions) {
			PerfoChangePreCommitListener perfoListener = new PerfoChangePreCommitListener();
			TransactionalEditingDomain transDomain = session.getTransactionalEditingDomain();
			if (transDomain != null) {
				transDomain.addResourceSetListener(perfoListener);
				// the listener and the session its attached to are saved in order to unregister
				// him when the viewpoint is deselected
				registerPreCommitListener(session, perfoListener);
				computePerfo(session);
			}
		}
	}

	@Override
	public void viewpointDeselected(Viewpoint deselectedSirius) {
		
		if (!deselectedSirius.getName().equals("Price_ID")) return;

		// for each session opened, remove the pre-commit listener
		Set<Entry<Session, PerfoChangePreCommitListener>> setSessionsPerfoPreCommitListener = sessionsPerfoPreCommitListener
				.entrySet();
		Iterator<Entry<Session, PerfoChangePreCommitListener>> it = setSessionsPerfoPreCommitListener.iterator();
		while (it.hasNext()) {
			Entry<Session, PerfoChangePreCommitListener> e = it.next();
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
	 * Saves a session and its performance listener
	 * 
	 * @param session
	 * @param preCommit
	 */
	public void registerPreCommitListener(Session session, PerfoChangePreCommitListener preCommit) {
		sessionsPerfoPreCommitListener.put(session, preCommit);
	}

	/**
	 * Compute the performance of the elements of a session
	 * 
	 * @param the session
	 */
	public void computePerfo(Session session) {
		TransactionalEditingDomain domain = session.getTransactionalEditingDomain();
		domain.getCommandStack().execute(new RecordingCommand(domain) {

			@Override
			protected void doExecute() {
	
				PerformanceServices performanceServices = new PerformanceServices();
				List<FunctionalChain> physicalFunctionalChains = FunctionalChainExt.getAllFunctionalChains(retrievesPhysicalArchitectureFromSession(session));
				List<FunctionalChain> logicalFunctionalChains = FunctionalChainExt.getAllFunctionalChains(retrievesLogicalArchitectureFromSession(session));
				
				physicalFunctionalChains.stream().forEach((functionalChain) ->  {
					functionalChain.eContents().stream().filter(content -> content instanceof PerformanceCriteria).forEach((perfoObject) -> {
						performanceServices.checkPerformance(perfoObject, functionalChain);
					});
				});
				
				logicalFunctionalChains.stream().forEach((functionalChain) ->  {
					functionalChain.eContents().stream().filter(content -> content instanceof PerformanceCriteria).forEach((perfoObject) -> {
						performanceServices.checkPerformance(perfoObject, functionalChain);
					});
				});
			}

		});

	}

	/**
	 * Retrieves the Physical Architecture part of a capella model from its session
	 * @param session
	 * @return the physical architecture of a capella model
	 */
	private PhysicalArchitecture retrievesPhysicalArchitectureFromSession(Session session) {
		Collection<Resource> resources = session.getSemanticResources();
		List<PhysicalArchitecture> physicalAchitecture = new ArrayList<>();

		resources.stream().filter(resource -> resource instanceof CapellamodellerResourceImpl).forEach((resource) -> {
			resource.getContents().stream().filter(content -> content instanceof ProjectImpl).forEach((content) -> {
				((ProjectImpl) content).getOwnedModelRoots().stream()
						.filter(modelRoot -> modelRoot instanceof SystemEngineeringImpl).forEach((modelRoot) -> {
							((SystemEngineeringImpl) modelRoot).getOwnedArchitectures().stream()
									.filter(architecture -> architecture instanceof PhysicalArchitectureImpl)
									.forEach((physicalArchitecture) -> {
										physicalAchitecture.add(((PhysicalArchitectureImpl) physicalArchitecture));
									});
							;
						});
				;
			});
			;
		});
		
		return physicalAchitecture.get(0);
	}
	
	/**
	 * Retrieves the Logical Architecture part of a capella model from its session
	 * @param session
	 * @return the logical architecture of a capella model
	 */
	private LogicalArchitecture retrievesLogicalArchitectureFromSession(Session session) {
		Collection<Resource> resources = session.getSemanticResources();
		List<LogicalArchitecture> logicalAchitecture = new ArrayList<>();

		resources.stream().filter(resource -> resource instanceof CapellamodellerResourceImpl).forEach((resource) -> {
			resource.getContents().stream().filter(content -> content instanceof ProjectImpl).forEach((content) -> {
				((ProjectImpl) content).getOwnedModelRoots().stream()
						.filter(modelRoot -> modelRoot instanceof SystemEngineeringImpl).forEach((modelRoot) -> {
							((SystemEngineeringImpl) modelRoot).getOwnedArchitectures().stream()
									.filter(architecture -> architecture instanceof LogicalArchitectureImpl)
									.forEach((logicalArchitecture) -> {
										logicalAchitecture.add(((LogicalArchitectureImpl) logicalArchitecture));
									});
							;
						});
				;
			});
			;
		});
		
		return logicalAchitecture.get(0);
	}

}


