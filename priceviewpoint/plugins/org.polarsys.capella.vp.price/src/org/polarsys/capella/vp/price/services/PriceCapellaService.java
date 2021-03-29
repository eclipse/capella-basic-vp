/*******************************************************************************
 * Copyright (c) 2006, 2020 Thales Global Services
 *   This program and the accompanying materials are made available under the
 *   terms of the Eclipse Public License 2.0 which is available at
 *   http://www.eclipse.org/legal/epl-2.0
 *   
 *   SPDX-License-Identifier: EPL-2.0
 * 
 *   Contributors:
 *      Thales - initial API and implementation
 *      Obeo - Code improvement
 ******************************************************************************/
package org.polarsys.capella.vp.price.services;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.core.data.cs.Part;
import org.polarsys.capella.vp.price.generic.IPriceVisitor;
import org.polarsys.capella.vp.price.generic.PriceGenericRootService;
import org.polarsys.capella.vp.price.price.PartPrice;
import org.polarsys.capella.vp.price.price.Price;
import org.polarsys.kitalpha.emde.model.ElementExtension;



public class PriceCapellaService extends PriceGenericRootService {
	
	public PriceCapellaService(){
		super();
		registerVisitor(null);
	}
	
	/**
	 * Visitor
	 */
	
	@Override
	public void registerVisitor(IPriceVisitor iWPVisitor) {
		super.priceVisitor = CapellaPriceVisitor.INSTANCE;
	}
	
	public EObject getPriceObject(EObject eObject) {



		if (eObject instanceof Part)
		{

			final EList<ElementExtension> ownedExtensions = ((Part) eObject).getOwnedExtensions();
			if (ownedExtensions != null && ! ownedExtensions.isEmpty())
			{
				for (ElementExtension extension : ownedExtensions) 
				{
					if (extension instanceof Price)
						return extension;
				}
			}
		}

		if (eObject instanceof Price)
		{
			return eObject;
		}
		return null;
	}
	
	
	
	/**
	 * Adapted Price Services 
	 */
	
	public int computePrice(EObject eObject) {
		int m = super.compute(eObject, getVisitor(), PartPrice.class);
		if (m!=-1){
			((PartPrice)getPriceObject(eObject)).setCurrentPrice(m);
		}
		return m;
	}
	

	/**
	 * Unused IWPService methods
	 */
	
	public int compute(EObject eObject, IPriceVisitor iWPVisitor) {
		return -1;
	}
}
