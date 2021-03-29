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

import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.common.ef.command.AbstractReadWriteCommand;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.core.data.cs.CsFactory;
import org.polarsys.capella.core.data.cs.Part;
import org.polarsys.capella.core.data.cs.impl.PartImpl;
import org.polarsys.capella.core.data.pa.PaFactory;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.PhysicalComponentNature;
import org.polarsys.capella.core.data.pa.deployment.DeploymentFactory;
import org.polarsys.capella.core.data.pa.deployment.PartDeploymentLink;
import org.polarsys.capella.vp.mass.helpers.MassCreationToolHelper;
import org.polarsys.capella.vp.mass.mass.impl.PartMassImpl;

/**
 * This test case checks if when adding one node with mass and one behavior with
 * mass to the same component, this components mass is recalculated.
 * Used to check if the listener responds to the notification ADD_MANY
 */
public class AddOneBehaviorOneNodeWithMassTest extends MassTest {

	private PartMassImpl pc1PartMass;

	@Override
	public void test() throws Exception {

		PhysicalComponent pc1 = physicalSystem.getOwnedPhysicalComponents().get(0);
		PartImpl pc1Part = (PartImpl) pc1.getAbstractTypedElements().get(0);
		pc1PartMass = (PartMassImpl) ((PartImpl) pc1.getAbstractTypedElements().get(0)).getOwnedExtensions().get(0);

		ExecutionManager manager = TransactionHelper.getExecutionManager(pc1);
		manager.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {

				// create two behaviors and their parts
				PhysicalComponent pc15 = PaFactory.eINSTANCE.createPhysicalComponent("PC 1.5");
				PhysicalComponent pc16 = PaFactory.eINSTANCE.createPhysicalComponent("PC 1.6");
				pc15.setNature(PhysicalComponentNature.NODE);
				pc16.setNature(PhysicalComponentNature.BEHAVIOR);
				Part partpc15 = CsFactory.eINSTANCE.createPart();
				partpc15.setAbstractType(pc15);
				Part partpc16 = CsFactory.eINSTANCE.createPart();
				partpc16.setAbstractType(pc16);

				// create a mass for the part of each nodes
				MassCreationToolHelper massCreationToolHelper = new MassCreationToolHelper();
				massCreationToolHelper.createMass(partpc15, 1);
				PartMassImpl elementPartMasspc15 = (PartMassImpl) (partpc15).getOwnedExtensions().get(0);
				elementPartMasspc15.setValue(10);
				massCreationToolHelper.createMass(partpc16, 1);
				PartMassImpl elementPartMasspc16 = (PartMassImpl) (partpc16).getOwnedExtensions().get(0);
				elementPartMasspc16.setValue(10);

				//create and correctly set a deployment link to deploy pc 1.6 to pc 1
				PartDeploymentLink partpc16DeploymentLink = DeploymentFactory.eINSTANCE.createPartDeploymentLink();
				partpc16DeploymentLink.setDeployedElement(partpc16);
				partpc16DeploymentLink.setLocation(pc1Part);

				physicalSystem.getOwnedPhysicalComponents().add(pc16);
				physicalSystem.getOwnedFeatures().add(partpc16);
				pc1Part.getOwnedDeploymentLinks().add(partpc16DeploymentLink);

				pc1.getOwnedPhysicalComponents().add(pc15);
				pc1.getOwnedFeatures().add(partpc15);

			}

		});

		assertEquals("The mass of PC1 was not changed after the addition of pc1.5 and pc1.6",
				pc1PartMass.getCurrentMass(), 105);

	}

}
