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
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.polarsys.capella.core.data.capellamodeller.impl.ProjectImpl;
import org.polarsys.capella.core.data.capellamodeller.impl.SystemEngineeringImpl;
import org.polarsys.capella.core.data.fa.AbstractFunction;
import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.core.data.fa.FunctionalChainInvolvement;
import org.polarsys.capella.core.data.fa.FunctionalExchange;
import org.polarsys.capella.core.data.pa.PhysicalArchitecture;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.impl.PhysicalArchitectureImpl;
import org.polarsys.capella.core.model.helpers.FunctionalChainExt;
import org.polarsys.capella.vp.perfo.perfo.PerformanceCriteria;
import org.polarsys.capella.vp.perfo.services.PerformanceServices;

/**
 * This listener is used to trigger the performance computation of the functional chains
 * before a change in the model is committed
 * Directly inspired from org.polarsys.capella.vp.mass.design.service.massListener.MassChangePreCommitListener
 */
public class PerfoChangePreCommitListener implements ResourceSetListener {
	
	PerformanceServices performanceServices = new PerformanceServices();

	@Override
	public NotificationFilter getFilter() {

		// the only notifications that should trigger a performance computation are:
		// - add or remove a component of the model
		// - change the performance of one of the elements of the model
		return new NotificationFilter.Custom() {
			@Override
			public boolean matches(Notification notification) {
				int eventType = notification.getEventType();
				Object notifier = notification.getNotifier();
				if (!notification.isTouch() && (eventType == Notification.REMOVE || eventType == Notification.ADD
						|| eventType == Notification.ADD_MANY || eventType == Notification.REMOVE_MANY)) {
					Object feature = notification.getFeature();
					if (feature instanceof EReference) {
						return ((EReference) feature).isContainment();
					}
				} else if (!notification.isTouch() && eventType == Notification.SET && notifier instanceof PerformanceCriteria) {
					return true;
				}
				
				return false;
			}
		};
	}

	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event) throws RollbackException {
		TransactionalEditingDomain domain = event.getEditingDomain();
		List<Command> commands = new ArrayList<Command>();
		Iterator<?> iter = event.getNotifications().iterator();

		while (iter.hasNext()) {
			Notification next = (Notification) iter.next();
			EObject notifier = (EObject) next.getNotifier();

			switch (next.getEventType()) {

			// if the performance of an element of the model changes, calculate the performance of the
			// functional chains it is a part of
			case Notification.SET:
				commands.add(new RecordingCommand(domain) {

					@Override
					protected void doExecute() {
						if (notifier.eContainer() instanceof FunctionalChain) {
							// trigger computation of functional chain directly from notifier.eContainer()
							performanceServices.checkPerformance(notifier, notifier.eContainer());

						} else if (notifier.eContainer() instanceof AbstractFunction) {
							// trigger computation of functional chains containing notifier.eContainer()
							refreshFunctionalChainsPerfo((AbstractFunction) notifier.eContainer());

						} else if (notifier.eContainer() instanceof FunctionalExchange) {
							// trigger computation of functional chains containing notifier.eContainer()
							refreshFunctionalChainsPerfo((FunctionalExchange) notifier.eContainer());
							
						}
					}
				});
				break;
				
				
			// if an element is added calculate the performance of the functional chains it is a part of
			case Notification.ADD:
				commands.add(new RecordingCommand(domain) {

					@Override
					protected void doExecute() {
						if (notifier instanceof FunctionalChain) {

							if (next.getNewValue() instanceof PerformanceCriteria) {
								performanceServices.checkPerformance((EObject) next.getNewValue(), notifier);

							} else if (next.getNewValue() instanceof FunctionalChainInvolvement) {
								notifier.eContents().stream().filter(content -> content instanceof PerformanceCriteria)
										.forEach((perfoObject) -> {
											performanceServices.checkPerformance(perfoObject, notifier);
										});
							}
						}
					}
				});
				break;
				
			
			//This notification is sent when several elements are added to the same element
			case Notification.ADD_MANY:
				commands.add(new RecordingCommand(domain) {

					@Override
					protected void doExecute() {
						if (notifier instanceof FunctionalChain) {
							notifier.eContents().stream().filter(content -> content instanceof PerformanceCriteria)
									.forEach((perfoObject) -> {
										performanceServices.checkPerformance(perfoObject, notifier);
									});
						}
					}
				});
				break;
				
			// if an element is removed calculate the performance of the functional chains it is a part of	
			case Notification.REMOVE:
				commands.add(new RecordingCommand(domain) {

					@Override
					protected void doExecute() {
						
						// Performance information is removed from an element
						if (next.getOldValue() instanceof PerformanceCriteria) {

							if (notifier instanceof AbstractFunction) {
								refreshFunctionalChainsPerfo((AbstractFunction) notifier);

							} else if (notifier instanceof FunctionalExchange) {
								refreshFunctionalChainsPerfo((FunctionalExchange) notifier);
							}
							
						// An element is removed from a functional chain
						} else if (notifier instanceof FunctionalChain && next.getOldValue() instanceof FunctionalChainInvolvement) {
							notifier.eContents().stream().filter(content -> content instanceof PerformanceCriteria)
									.forEach((perfoObject) -> {
										performanceServices.checkPerformance(perfoObject, notifier);
									});
							
						// An element containing performance informations is removed
						} else if (next.getOldValue() instanceof AbstractFunction 
								|| next.getOldValue() instanceof FunctionalExchange
								|| next.getOldValue() instanceof PhysicalComponent) {
							
							List<FunctionalChain> functionalChains = FunctionalChainExt.getAllFunctionalChains(retrievePhysicalArchitectureFromEObject((EObject) notifier));
							
							functionalChains.stream().forEach((functionalChain) ->  {
								functionalChain.eContents().stream().filter(content -> content instanceof PerformanceCriteria).forEach((perfoObject) -> {
									performanceServices.checkPerformance(perfoObject, functionalChain);
								});
							});
						} 
					}
				});
				break;
				
			//This notification is sent when several elements are removed from the same element	
			case Notification.REMOVE_MANY:
				commands.add(new RecordingCommand(domain) {

					@Override
					protected void doExecute() {
						if (notifier instanceof FunctionalChain) {
							notifier.eContents().stream().filter(content -> content instanceof PerformanceCriteria)
									.forEach((perfoObject) -> {
										performanceServices.checkPerformance(perfoObject, notifier);
									});
							
						} else if (notifier instanceof AbstractFunction) {
							refreshFunctionalChainsPerfo((AbstractFunction) notifier);
							
						} else if (notifier instanceof FunctionalExchange) {
							refreshFunctionalChainsPerfo((FunctionalExchange) notifier);
							
						} else {
							List<FunctionalChain> functionalChains = FunctionalChainExt.getAllFunctionalChains(retrievePhysicalArchitectureFromEObject((EObject) notifier));
							
							functionalChains.stream().forEach((functionalChain) ->  {
								functionalChain.eContents().stream().filter(content -> content instanceof PerformanceCriteria).forEach((perfoObject) -> {
									performanceServices.checkPerformance(perfoObject, functionalChain);
								});
							});
						}
					}  
				});
				break;

			default:
				break;
			}

		}
		return commands.isEmpty() ? null : new CompoundCommand(commands);
	}

	@Override
	public boolean isPrecommitOnly() {
		return true;
	}

	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event) {
	}

	@Override
	public boolean isAggregatePrecommitListener() {
		return false;
	}

	@Override
	public boolean isPostcommitOnly() {
		return false;
	}

	/**
	 * Compute the performance of the functional chains involving the abstract function
	 * 
	 * @param abstractFunction
	 */
	private void refreshFunctionalChainsPerfo(AbstractFunction abstractFunction) {
		List<FunctionalChain> functionalChains = ((AbstractFunction) abstractFunction)
				.getInvolvingFunctionalChains();

		functionalChains.stream().forEach((functionalChain) -> {
			functionalChain.eContents().stream()
					.filter(content -> content instanceof PerformanceCriteria)
					.forEach((perfoObject) -> {
						performanceServices.checkPerformance(perfoObject, functionalChain);
					});
		});
	}
	
	
	/**
	 * Compute the performance of the functional chains involving the functional exchange
	 * 
	 * @param functionalExchange
	 */
	private void refreshFunctionalChainsPerfo(FunctionalExchange functionalExchange) {
		List<FunctionalChain> functionalChains = functionalExchange.getInvolvingFunctionalChains();

		functionalChains.stream().forEach((functionalChain) -> {
			functionalChain.eContents().stream()
					.filter(content -> content instanceof PerformanceCriteria)
					.forEach((perfoObject) -> {
						performanceServices.checkPerformance(perfoObject, functionalChain);
					});
		});
	}
	
	/**
	 * Retrieves the Physical Architecture part of a capella model from an EObject
	 * @param eobject
	 * @return the physical architecture of a capella model
	 */
	private PhysicalArchitecture retrievePhysicalArchitectureFromEObject(EObject eobject) {
		List<PhysicalArchitecture> physicalAchitecture = new ArrayList<>();

		eobject.eResource().getContents().stream().filter(content -> content instanceof ProjectImpl)
				.forEach((content) -> {
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
		
		return physicalAchitecture.get(0);
	}
}

