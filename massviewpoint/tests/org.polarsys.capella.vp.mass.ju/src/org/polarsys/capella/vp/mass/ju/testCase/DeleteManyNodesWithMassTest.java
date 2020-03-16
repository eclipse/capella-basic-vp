/*******************************************************************************
 * Copyright (c) 2020 OBEO
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * 
 *   Contributors:
 *      Obeo - initial API and implementation
 ******************************************************************************/
package org.polarsys.capella.vp.mass.ju.testCase;

import org.eclipse.emf.common.util.EList;
import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.common.ef.command.AbstractReadWriteCommand;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.core.data.capellacore.Feature;
import org.polarsys.capella.core.data.cs.impl.PartImpl;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.vp.mass.mass.impl.PartMassImpl;

/**
 * This test case checks if when deleting several nodes with masses the mass of
 * their parent is re-calculated.
 * Used to check if the listener responds to the notification REMOVE_MANY
 */
public class DeleteManyNodesWithMassTest extends MassTest {

	private PartMassImpl pc1PartMass;

	@Override
	public void test() throws Exception {
		PhysicalComponent pc1 = physicalSystem.getOwnedPhysicalComponents().get(0);
		pc1PartMass = (PartMassImpl) ((PartImpl) pc1.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);

		ExecutionManager manager = TransactionHelper.getExecutionManager(pc1);
		manager.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {
				EList<PhysicalComponent> physicalComponentsTobeRemoved = pc1.getOwnedPhysicalComponents();
				EList<Feature> featuresTobeRemoved = pc1.getOwnedFeatures();

				// remove pc 1.1, pc 1.3 and pc 1.5 and check if the mass of pc1 is
				// re-calculated
				ExecutionManager manager = TransactionHelper.getExecutionManager(pc1);
				manager.execute(new AbstractReadWriteCommand() {

					@Override
					public void run() {
						pc1.getOwnedPhysicalComponents().removeAll(physicalComponentsTobeRemoved);
						pc1.getOwnedFeatures().removeAll(featuresTobeRemoved);
					}

				});

				assertEquals("The mass of PC1 was not changed after the deletion of pc 1.1, pc 1.3 and pc 1.5",
						pc1PartMass.getCurrentMass(), 35);
			}

		});
	}

}
