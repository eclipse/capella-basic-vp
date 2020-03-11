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
package org.polarsys.capella.vp.mass.ju.testCase;

import org.polarsys.capella.core.data.cs.impl.PartImpl;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.vp.mass.mass.impl.PartMassImpl;

/**
 * This test case checks if when deleting a behavior PC owning a partMass the
 * mass of its parents is re-calculated
 */
public class DeleteBehaviorWithMassTest extends MassTest {

	PartMassImpl pc1PartMass;
	PartMassImpl pc12PartMass;
	PhysicalComponent pc12;

	@Override
	public void test() throws Exception {

		PhysicalComponent pc1 = physicalSystem.getOwnedPhysicalComponents().get(0);
		pc1PartMass = (PartMassImpl) ((PartImpl) pc1.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);
		pc12 = pc1.getDeployedPhysicalComponents().get(0);
		pc12PartMass = (PartMassImpl) ((PartImpl) pc12.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);

		// remove pc1.2 and check if the mass of pc1 is re-calculated
		deleteElement(pc12);
		assertEquals("The mass of PC1 was not changed after the deletion of pc1.1", pc1PartMass.getCurrentMass(), 70);

	}

}