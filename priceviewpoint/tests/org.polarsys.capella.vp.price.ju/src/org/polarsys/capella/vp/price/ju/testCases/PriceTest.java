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
package org.polarsys.capella.vp.price.ju.testCases;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.ui.internal.Workbench;
import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.common.ef.command.AbstractReadWriteCommand;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.core.data.capellamodeller.Project;
import org.polarsys.capella.core.data.capellamodeller.SystemEngineering;
import org.polarsys.capella.core.data.cs.impl.PartImpl;
import org.polarsys.capella.core.data.pa.PhysicalArchitecture;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.PhysicalComponentPkg;
import org.polarsys.capella.core.libraries.model.CapellaModel;
import org.polarsys.capella.test.framework.api.BasicTestCase;
import org.polarsys.capella.vp.price.helpers.PriceCreationToolHelper;
import org.polarsys.capella.vp.price.price.impl.PartPriceImpl;

/**
 * Implements methods often used by price test cases
 */
public abstract class PriceTest extends BasicTestCase {
	protected PhysicalComponent physicalSystem;
	public static final String MODEL_NAME = "PriceProject";

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
		physicalSystem = getPhysicalSystem();
	}

	/**
	 * Retrieves the PhysicalComponent root of the Physical Architecture part of a
	 * capella model
	 * 
	 * @return the PhysicalSystem component from a model
	 */
	protected PhysicalComponent getPhysicalSystem() {
		// fetch the root element
		CapellaModel model = getTestModel(getRequiredTestModels().get(0));
		Session session = getSession(getRequiredTestModels().get(0));
		Project project = model.getProject(session.getTransactionalEditingDomain());
		SystemEngineering eng = ((SystemEngineering) (project.getOwnedModelRoots().get(0)));
		PhysicalArchitecture physicalArchi = (PhysicalArchitecture) eng.getOwnedArchitectures().get(3);
		PhysicalComponentPkg physicalComponentPkg = physicalArchi.getOwnedPhysicalComponentPkg();
		return physicalComponentPkg.getOwnedPhysicalComponents().get(0);
	}

	/**
	 * Add a PartPrice to a PhysicalComponent
	 * @param element the element we wish to add a price to
	 * @param priceValue the value of the price
	 */
	protected void addPriceToPhysicalComponent(PhysicalComponent element, int priceValue) {
		ExecutionManager manager = TransactionHelper.getExecutionManager(element);
		manager.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {
				PriceCreationToolHelper priceCreationToolHelper = new PriceCreationToolHelper();
				priceCreationToolHelper.createPrice(element, 2);
				PartPriceImpl elementPartPrice = (PartPriceImpl) ((PartImpl) element.getAbstractTypedElements().get(0))
						.getOwnedExtensions().get(0);
				elementPartPrice.setValue(priceValue);
			}

		});
	}

	/**
	 * Change the value of the PartPrice of a PhysicalComponent
	 * @param element the element we wish to change the price
	 * @param newPrice the new value of the price
	 */
	protected void editPricePhysicalComponent(PhysicalComponent element, int newPrice) {
		PartPriceImpl pcPartMass = (PartPriceImpl) ((PartImpl) element.getAbstractTypedElements().get(0))
				.getOwnedExtensions().get(0);
		ExecutionManager managerpc = TransactionHelper.getExecutionManager(element);
		managerpc.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {
				pcPartMass.setValue(newPrice);
			}
		});
	}

}
