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
import org.polarsys.capella.common.data.modellingcore.AbstractTypedElement;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.util.PaSwitch;
import org.polarsys.capella.vp.price.price.impl.PartPriceImpl;
import org.polarsys.capella.vp.price.services.PriceCapellaService;
import org.polarsys.kitalpha.emde.model.ElementExtension;

/**
 * Directly inspired from org.polarsys.capella.vp.mass.design.service.massSwitch.ComputeMassSwitch
 */
public class ComputePriceSwitch extends PaSwitch<Void> {

	/**
	 * Check if a physical component has a price. If he does, compute its price.
	 * 
	 * @param physicalComponent
	 */
	public void computePrice(PhysicalComponent physicalComponent) {
		EList<AbstractTypedElement> abstractTypedElements = physicalComponent.getAbstractTypedElements();

		for (AbstractTypedElement part : abstractTypedElements) {
			EList<ElementExtension> ownedExtensions = part.getOwnedExtensions();

			for (ElementExtension extension : ownedExtensions) {

				if (extension instanceof PartPriceImpl) {
					PriceCapellaService maPriceService = new PriceCapellaService();
					maPriceService.computePrice(part);
				}
			}
		}
	}
}

