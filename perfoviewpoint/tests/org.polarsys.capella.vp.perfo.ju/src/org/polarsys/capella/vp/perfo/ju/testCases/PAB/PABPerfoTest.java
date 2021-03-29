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
package org.polarsys.capella.vp.perfo.ju.testCases.PAB;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.ui.internal.Workbench;
import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.common.ef.command.AbstractReadWriteCommand;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.core.data.capellamodeller.Project;
import org.polarsys.capella.core.data.capellamodeller.SystemEngineering;
import org.polarsys.capella.core.data.fa.AbstractFunction;
import org.polarsys.capella.core.data.fa.FunctionPkg;
import org.polarsys.capella.core.data.fa.FunctionalExchange;
import org.polarsys.capella.core.data.helpers.fa.services.FunctionPkgExt;
import org.polarsys.capella.core.data.pa.PhysicalArchitecture;
import org.polarsys.capella.core.data.pa.PhysicalFunction;
import org.polarsys.capella.core.libraries.model.CapellaModel;
import org.polarsys.capella.test.framework.api.BasicTestCase;
import org.polarsys.capella.vp.perfo.helpers.PerformanceCriteriaCreationToolHelper;
import org.polarsys.capella.vp.perfo.perfo.TimeConsumption;
import org.polarsys.capella.vp.perfo.perfo.impl.TimeConsumptionImpl;

/**
 * Implements methods often used by perfo test cases
 */
public abstract class PABPerfoTest extends BasicTestCase {
	protected AbstractFunction rootAbstractFunction;
	public static final String MODEL_NAME = "PerfoProject";

	@Override
	public List<String> getRequiredTestModels() {
		return Arrays.asList(MODEL_NAME);
	}

	@SuppressWarnings("restriction")
	protected void setUp() throws Exception {
		super.setUp();
		// Deadlock because of Workbench Auto-Save Job, so we have to remove it
		Job[] allJobs = Job.getJobManager().find(null);
		for (Job job : allJobs) {
			if (Workbench.WORKBENCH_AUTO_SAVE_JOB.equals(job.getName())) {
				job.cancel();
			}
		}
		rootAbstractFunction = getRootAbstractFunction();
	}

	/**
	 * Retrieves the AbstractFunction root of the Physical Architecture part of a
	 * capella model
	 * 
	 * @return the AbstractFunction component from a model
	 */
	protected AbstractFunction getRootAbstractFunction() {
		// fetch the root element
		CapellaModel model = getTestModel(getRequiredTestModels().get(0));
		Session session = getSession(getRequiredTestModels().get(0));
		Project project = model.getProject(session.getTransactionalEditingDomain());
		SystemEngineering eng = ((SystemEngineering) (project.getOwnedModelRoots().get(0)));
		PhysicalArchitecture physicalArchi = (PhysicalArchitecture) eng.getOwnedArchitectures().get(3);
		FunctionPkg functionPkg = physicalArchi.getOwnedFunctionPkg();
		return FunctionPkgExt.getOwnedFunctions(functionPkg).get(0);
	}

	
	/**
	 * Add a TimeCapacity to an element
	 * @param element the element we wish to add a TimeCapacity to
	 */
	protected void addTimeCapacity(EObject element) {
		ExecutionManager manager = TransactionHelper.getExecutionManager(element);
		manager.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {
				PerformanceCriteriaCreationToolHelper perfoCreationToolHelper = new PerformanceCriteriaCreationToolHelper();
				perfoCreationToolHelper.createTimeCapacity(element);
			}
		});
	}
	
	/**
	 * Add a TimeConsumption to an element
	 * @param element the element we wish to add a TimeConsumption to
	 * @param perfoValue the value of the performance
	 */
	protected void addTimeConsumption(EObject element, int perfoValue) {
		ExecutionManager manager = TransactionHelper.getExecutionManager(element);
		manager.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {
				PerformanceCriteriaCreationToolHelper perfoCreationToolHelper = new PerformanceCriteriaCreationToolHelper();
				perfoCreationToolHelper.createTimeConsumption(element);
				
				if (element instanceof AbstractFunction) {
					TimeConsumption elementTimeConsumption = (TimeConsumption) ((AbstractFunction) element).getOwnedExtensions().get(0);
					elementTimeConsumption.setValue(perfoValue);
				} else if (element instanceof FunctionalExchange) {
					TimeConsumption elementTimeConsumption = (TimeConsumption) ((FunctionalExchange) element).getOwnedExtensions().get(0);
					elementTimeConsumption.setValue(perfoValue);
				}
			}
		});
	}

	/**
	 * Change the value of the PerformanceCriteria of a PhysicalFunction
	 * @param element the element we wish to change the performance
	 * @param newPerfo the new value of the performance
	 */
	protected void editPerfoPhysicalFunction(PhysicalFunction element, int newPerfo) {
		TimeConsumptionImpl pfTimeConsumption = (TimeConsumptionImpl) element.getOwnedExtensions().get(0);
		ExecutionManager managerpc = TransactionHelper.getExecutionManager(element);
		managerpc.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {
				pfTimeConsumption.setValue(newPerfo);
			}
		});
	}
	
	/**
	 * Change the value of the PerformanceCriteria of a FunctionalExchange
	 * @param element the element we wish to change the performance
	 * @param newPerfo the new value of the performance
	 */
	protected void editPerfoFunctionalExchange(FunctionalExchange element, int newPerfo) {
		TimeConsumptionImpl feTimeConsumption = (TimeConsumptionImpl) element.getOwnedExtensions().get(0);
		ExecutionManager managerpc = TransactionHelper.getExecutionManager(element);
		managerpc.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {
				feTimeConsumption.setValue(newPerfo);
			}
		});
	}

}
