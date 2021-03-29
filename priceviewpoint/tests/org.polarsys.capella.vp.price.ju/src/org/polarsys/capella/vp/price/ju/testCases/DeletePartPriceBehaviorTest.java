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

import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.common.ef.command.AbstractReadWriteCommand;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.core.data.cs.impl.PartImpl;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.test.framework.helpers.EObjectHelper;
import org.polarsys.capella.vp.price.price.impl.PartPriceImpl;

/**
 * This test case checks if when deleting the partPrice of a behavior PC the price
 * of its parents is re-calculated.
 * Used to check if the listener responds to the notification REMOVE
 */
public class DeletePartPriceBehaviorTest extends PriceTest {

	private PartPriceImpl pc1PartPrice;
	private PhysicalComponent pc12;
	private PartPriceImpl pc12PartPrice;

	@Override
	public void test() throws Exception {
		PhysicalComponent pc1 = physicalSystem.getOwnedPhysicalComponents().get(0);
		pc1PartPrice = (PartPriceImpl) ((PartImpl) pc1.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);
		pc12 = pc1.getDeployedPhysicalComponents().get(0);
		pc12PartPrice = (PartPriceImpl) ((PartImpl) pc12.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);

		// remove pc12PartMass and check if the mass of pc1 is re-calculated
		ExecutionManager manager = TransactionHelper.getExecutionManager(pc12PartPrice);
		manager.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {
				EObjectHelper.removeElement(pc12PartPrice);
			}

		});
		assertEquals("The price of PC1 was not changed after the deletion of the part price of PC 1.2",
				pc1PartPrice.getCurrentPrice(), 75);
	}

}
