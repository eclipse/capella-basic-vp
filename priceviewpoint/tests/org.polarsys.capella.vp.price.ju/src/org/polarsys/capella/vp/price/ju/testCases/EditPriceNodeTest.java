/*******************************************************************************
 * Copyright (c) 2006, 2020 Obeo
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * 
 *   Contributors:
 *      Obeo - initial API and implementation
 ******************************************************************************/
package org.polarsys.capella.vp.price.ju.testCases;

import org.polarsys.capella.core.data.cs.impl.PartImpl;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.vp.price.price.impl.PartPriceImpl;

/**
 * This test case checks if when editing the price of a node the prices of its
 * parents is re-calculated.
 * Used to check if the listener responds to the notification SET
 */
public class EditPriceNodeTest extends PriceTest {
	PartPriceImpl pc1PartPrice;
	PartPriceImpl pc11PartPrice;

	@Override
	public void test() throws Exception {
		PhysicalComponent pc1 = physicalSystem.getOwnedPhysicalComponents().get(0);
		pc1PartPrice = (PartPriceImpl) ((PartImpl) pc1.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);
		PhysicalComponent pc11 = pc1.getOwnedPhysicalComponents().get(0);
		pc11PartPrice = (PartPriceImpl) ((PartImpl) pc11.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);

		// change the price of pc1.1 (a node) and check if the price of pc1 is
		// re-calculated
		editPricePhysicalComponent(pc11, 40);
		assertEquals("The price of PC1.1 was not changed after its modification", pc11PartPrice.getCurrentPrice(), 40);
		assertEquals("The price of PC1 was not changed after the modification of pc1.1", pc1PartPrice.getCurrentPrice(),
				75);

	}

}
