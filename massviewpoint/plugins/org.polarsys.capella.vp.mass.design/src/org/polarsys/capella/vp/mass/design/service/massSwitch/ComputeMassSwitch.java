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
import org.polarsys.capella.common.data.modellingcore.AbstractTypedElement;
import org.polarsys.capella.core.data.pa.PhysicalComponent;
import org.polarsys.capella.core.data.pa.util.PaSwitch;
import org.polarsys.capella.vp.mass.mass.impl.PartMassImpl;
import org.polarsys.capella.vp.mass.services.MassCapellaService;
import org.polarsys.kitalpha.emde.model.ElementExtension;

public class ComputeMassSwitch extends PaSwitch<Void> {

	/**
	 * Check if a physical component has a mass. If he does, compute its mass.
	 * 
	 * @param physicalComponent
	 */
	public void computeMass(PhysicalComponent physicalComponent) {
		EList<AbstractTypedElement> abstractTypedElements = physicalComponent.getAbstractTypedElements();

		for (AbstractTypedElement part : abstractTypedElements) {
			EList<ElementExtension> ownedExtensions = part.getOwnedExtensions();

			for (ElementExtension extension : ownedExtensions) {

				if (extension instanceof PartMassImpl) {
					MassCapellaService maMassService = new MassCapellaService();
					maMassService.computeMass(part);
				}
			}
		}
	}
}
