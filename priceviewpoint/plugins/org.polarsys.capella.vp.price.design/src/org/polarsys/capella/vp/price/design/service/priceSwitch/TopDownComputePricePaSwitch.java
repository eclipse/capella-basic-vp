/*******************************************************************************
 * Copyright (c) 2021 Obeo
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * 
 *   Contributors:
 *      Obeo - initial API and implementation 
 ******************************************************************************/
package org.polarsys.capella.vp.price.design.service.priceSwitch;

import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.PhysicalComponentPkg;

/**
 * This switch is used to trigger the mass computation of all the children of a
 * component
 * Directly inspired from org.polarsys.capella.vp.mass.design.service.massSwitch.TopDownComputeMassPaSwitch
 */
public class TopDownComputePricePaSwitch extends ComputePriceSwitch {

	@Override
	public Void casePhysicalComponent(PhysicalComponent physicalComponent) {
		computePrice(physicalComponent);
		for (PhysicalComponent child : physicalComponent.getOwnedPhysicalComponents()) {
			this.doSwitch(child);
		}
		return null;
	}

	@Override
	public Void casePhysicalComponentPkg(PhysicalComponentPkg physicalComponentPkg) {
		for (PhysicalComponent physicalComponent : physicalComponentPkg.getOwnedPhysicalComponents()) {
			this.doSwitch(physicalComponent);
		}
		return null;
	}
}

