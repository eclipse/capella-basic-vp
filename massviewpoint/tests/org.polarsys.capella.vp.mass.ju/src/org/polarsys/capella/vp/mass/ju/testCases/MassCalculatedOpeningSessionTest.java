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
package org.polarsys.capella.vp.mass.ju.testCases;

import org.junit.Test;
import org.polarsys.capella.core.data.cs.impl.PartImpl;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.vp.mass.mass.impl.PartMassImpl;

/**
 * This test makes sure the mass of all the elements are correctly calculated
 * when opening a new session
 * Used to check if the listener responds to the notification SET
 */
public class MassCalculatedOpeningSessionTest extends MassTest {
	PartMassImpl pc1PartMass;
	PartMassImpl pc11PartMass;
	PartMassImpl pc12PartMass;
	PartMassImpl pc121PartMass;

	@Test
	public void test() {

		// fetch pc1 and check if its mass is correct
		PhysicalComponent pc1 = physicalSystem.getOwnedPhysicalComponents().get(0);
		assertNotNull("PC1 could not be retrieved", pc1);
		pc1PartMass = (PartMassImpl) ((PartImpl) pc1.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);
		assertEquals("The mass of PC1 is incorrect", pc1PartMass.getCurrentMass(), 85);

		// fetch pc1.1 and check if its mass is correct
		PhysicalComponent pc11 = pc1.getOwnedPhysicalComponents().get(0);
		assertNotNull("PC1.1 could not be retrieved", pc11);
		pc11PartMass = (PartMassImpl) ((PartImpl) pc11.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);
		assertEquals("The mass of PC1.1 is incorrect", pc11PartMass.getCurrentMass(), 50);

		// fetch pc1.2 and check if its mass is correct
		PhysicalComponent pc12 = pc1.getDeployedPhysicalComponents().get(0);
		assertNotNull("PC1.2 could not be retrieved", pc12);
		pc12PartMass = (PartMassImpl) ((PartImpl) pc12.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);
		assertEquals("The mass of PC1.2 is incorrect", pc12PartMass.getCurrentMass(), 15);

		// fetch pc1.2.1 and check if its mass is correct
		PhysicalComponent pc121 = pc12.getDeployedPhysicalComponents().get(0);
		assertNotNull("PC1.2.1 could not be retrieved", pc121);
		pc121PartMass = (PartMassImpl) ((PartImpl) pc121.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);
		assertEquals("The mass of PC1.2.1 is incorrect", pc121PartMass.getCurrentMass(), 5);
	}

}
