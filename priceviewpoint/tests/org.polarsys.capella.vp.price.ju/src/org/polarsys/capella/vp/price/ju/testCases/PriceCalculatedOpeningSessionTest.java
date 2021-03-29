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

import org.junit.Test;
import org.polarsys.capella.core.data.cs.impl.PartImpl;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.vp.price.price.impl.PartPriceImpl;

/**
 * This test makes sure the price of all the elements are correctly calculated
 * when opening a new session Used to check if the listener responds to the
 * notification SET
 */
public class PriceCalculatedOpeningSessionTest extends PriceTest {
	PartPriceImpl pc1PartPrice;
	PartPriceImpl pc11PartPrice;
	PartPriceImpl pc12PartPrice;
	PartPriceImpl pc121PartPrice;

	@Test
	public void test() {

		// fetch pc1 and check if its mass is correct
		PhysicalComponent pc1 = physicalSystem.getOwnedPhysicalComponents().get(0);
		assertNotNull("PC1 could not be retrieved", pc1);
		pc1PartPrice = (PartPriceImpl) ((PartImpl) pc1.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);
		assertEquals("The mass of PC1 is incorrect", pc1PartPrice.getCurrentPrice(), 85);

		// fetch pc1.1 and check if its mass is correct
		PhysicalComponent pc11 = pc1.getOwnedPhysicalComponents().get(0);
		assertNotNull("PC1.1 could not be retrieved", pc11);
		pc11PartPrice = (PartPriceImpl) ((PartImpl) pc11.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);
		assertEquals("The mass of PC1.1 is incorrect", pc11PartPrice.getCurrentPrice(), 50);

		// fetch pc1.2 and check if its mass is correct
		PhysicalComponent pc12 = pc1.getDeployedPhysicalComponents().get(0);
		assertNotNull("PC1.2 could not be retrieved", pc12);
		pc12PartPrice = (PartPriceImpl) ((PartImpl) pc12.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);
		assertEquals("The mass of PC1.2 is incorrect", pc12PartPrice.getCurrentPrice(), 15);

		// fetch pc1.2.1 and check if its mass is correct
		PhysicalComponent pc121 = pc12.getDeployedPhysicalComponents().get(0);
		assertNotNull("PC1.2.1 could not be retrieved", pc121);
		pc121PartPrice = (PartPriceImpl) ((PartImpl) pc121.getAbstractTypedElements().get(0)).getOwnedExtensions()
				.get(0);
		assertEquals("The mass of PC1.2.1 is incorrect", pc121PartPrice.getCurrentPrice(), 5);
	}

}
