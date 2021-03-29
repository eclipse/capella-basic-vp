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
package org.polarsys.capella.vp.mass.design.service.massSwitch;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.common.data.modellingcore.AbstractTypedElement;
import org.polarsys.capella.core.data.cs.Part;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.PhysicalComponentNature;
import org.polarsys.capella.core.data.pa.PhysicalComponentPkg;
import org.polarsys.capella.vp.mass.helpers.MassHelper;
import org.polarsys.capella.vp.mass.mass.PartMass;

/**
 * This switch is used to trigger the mass computation of a parent after one of
 * its children's mass changed
 */
public class BottomUpComputeMassPaSwitch extends ComputeMassSwitch {

	@Override
	public Void casePhysicalComponent(PhysicalComponent physicalComponent) {
		
		MassHelper massHelper = new MassHelper();
		EList<EObject> massObjects = massHelper.getMassObjects(physicalComponent);
		boolean computeParents = true;
		
		//if the component has a mass, we have to compute it
		if(massObjects.size() != 0) {
			int oldMass = ((PartMass)(massObjects.get(0))).getCurrentMass();
			computeMass(physicalComponent);
			int currentMass = ((PartMass)(massObjects.get(0))).getCurrentMass();
			
			//if the mass didn't changed, we do not need to go further up the tree
			if(oldMass == currentMass) {
				computeParents=false;
			}
		}
		
		if(computeParents) {
			if (physicalComponent.getNature() == PhysicalComponentNature.BEHAVIOR) {
				//the parents of a behavior node are accessible through its abstract elements
				EList<AbstractTypedElement> abstractTypedElements = physicalComponent.getAbstractTypedElements();

				for (AbstractTypedElement part : abstractTypedElements) {
					EList<Part> deployingParts = ((Part) part).getDeployingParts();
					for (Part deployingPart : deployingParts) {
						this.doSwitch(deployingPart.getAbstractType());
					}
				}
			} else {
				this.doSwitch(physicalComponent.eContainer());
			}
		}
		return null;
	}

	@Override
	public Void casePhysicalComponentPkg(PhysicalComponentPkg physicalComponentPkg) {
		//a pkg can't have a mass
		this.doSwitch(physicalComponentPkg.eContainer());
		return null;
	}
}
