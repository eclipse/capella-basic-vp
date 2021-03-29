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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.common.data.modellingcore.AbstractTypedElement;
import org.polarsys.capella.core.data.cs.Part;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.PhysicalComponentNature;
import org.polarsys.capella.core.data.pa.PhysicalComponentPkg;
import org.polarsys.capella.vp.price.helpers.PriceHelper;
import org.polarsys.capella.vp.price.price.PartPrice;

/**
 * This switch is used to trigger the price computation of a parent after one of
 * its children's price changed
 * Directly inspired from org.polarsys.capella.vp.mass.design.service.massSwitch.BottomUpComputeMassPaSwitch
 */
public class BottomUpComputePricePaSwitch extends ComputePriceSwitch {

	@Override
	public Void casePhysicalComponent(PhysicalComponent physicalComponent) {
		
		PriceHelper priceHelper = new PriceHelper();
		EList<EObject> priceObjects = priceHelper.getAllPriceObjects(physicalComponent);
		boolean computeParents = true;
		
		//if the component has a price, we have to compute it
		if(priceObjects.size() != 0) {
			int oldPrice = ((PartPrice)(priceObjects.get(0))).getCurrentPrice();
			computePrice(physicalComponent);
			int currentPrice = ((PartPrice)(priceObjects.get(0))).getCurrentPrice();
			
			//if the price didn't changed, we do not need to go further up the tree
			if(oldPrice == currentPrice) {
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
		//a pkg can't have a price
		this.doSwitch(physicalComponentPkg.eContainer());
		return null;
	}
}

