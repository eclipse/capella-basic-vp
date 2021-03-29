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

import org.polarsys.capella.core.data.cs.impl.PartImpl;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.impl.PhysicalComponentImpl;
import org.polarsys.capella.vp.mass.mass.impl.PartMassImpl;

/**
 * This test case checks if after adding a mass to an already existing physical
 * node, the mass of its parent is re-calculated
 * Used to check if the listener responds to the notification ADD
 */
public class AddMassNodeTest extends MassTest {
	PartMassImpl pc1PartMass;
	
	@Override
	public void test() throws Exception {
		
		PhysicalComponent pc1 = physicalSystem.getOwnedPhysicalComponents().get(0);
		pc1PartMass = (PartMassImpl) ((PartImpl) pc1.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);
		PhysicalComponentImpl pc1_3 = (PhysicalComponentImpl) pc1.getOwnedPhysicalComponents().get(1);

		// add a mass to pc1.3 and check if the mass of pc1 is re-calculated
		addMassToPhysicalComponent(pc1_3, 10);
		assertEquals("The mass of PC1 was not changed after the addition of pc1.3", pc1PartMass.getCurrentMass(), 95);

	}

}
