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
 ******************************************************************************/
package org.polarsys.capella.vp.mass.generic;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.vp.mass.mass.Mass;


public interface IMassVisitor {
	
	
	/**
	 * return the Mass Object for a given container
	 * @param container : a Part or a PhusicalComponent
	 * @return
	 */
	public EList<Mass> getMassObjects(EObject container);
	
	public Mass getMassObject(EObject eObject);
	
	/**
	 * return the child object needed to browse the model. 
	 * @param modelElement : father 
	 * @return EList<EObject> : child
	 */
	public EList<EObject> getSubComponentsObjects(EObject modelElement);
}
