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

import org.polarsys.capella.core.data.cs.impl.PartImpl;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.impl.PhysicalComponentImpl;
import org.polarsys.capella.vp.price.price.impl.PartPriceImpl;

/**
 * This test case checks if after adding a price to an already existing physical
 * node, the price of its parent is re-calculated
 * Used to check if the listener responds to the notification ADD
 */
public class AddPriceNodeTest extends PriceTest {
	PartPriceImpl pc1PartPrice;
	
	@Override
	public void test() throws Exception {
		
		PhysicalComponent pc1 = physicalSystem.getOwnedPhysicalComponents().get(0);
		pc1PartPrice = (PartPriceImpl) ((PartImpl) pc1.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);
		PhysicalComponentImpl pc1_3 = (PhysicalComponentImpl) pc1.getOwnedPhysicalComponents().get(1);
		
		// add a price to pc1.3 and check if the mass of pc1 is re-calculated
		addPriceToPhysicalComponent(pc1_3, 10);
		assertEquals("The price of PC1 was not changed after the addition of pc1.3", pc1PartPrice.getCurrentPrice(), 95);

	}

}
