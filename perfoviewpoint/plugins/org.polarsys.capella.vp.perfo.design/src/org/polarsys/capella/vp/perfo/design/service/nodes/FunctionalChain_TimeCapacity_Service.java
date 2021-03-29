/******************************************************************************
* Copyright (c) 2006, 2020 Thales Global Services 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0 
 * 
 * Contributors: 
 *    Thales - initial API and implementation
*****************************************************************************/
package org.polarsys.capella.vp.perfo.design.service.nodes;

import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.vp.perfo.perfo.TimeCapacity;

/**
 * <!-- begin-user-doc -->
 * This class is an implementation of the DoReMi JavaExtension '<em><b>[org.polarsys.capella.vp.perfo.design.service.nodes.FunctionalChain_TimeCapacity_Service]</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */

public class FunctionalChain_TimeCapacity_Service {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param eObject : the current semantic object
	 * @param view : the current view
	 * @param container : the semantic container of the current object
	 * @generated NOT
	 */
	public boolean performanceSaturated(EObject eObject, EObject view, EObject container) {
		int value = ((TimeCapacity) eObject).getValue();
		int executionTime = ((TimeCapacity) eObject).getCurrentExecutionTime();
		return value != 0 && value == executionTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param eObject : the current semantic object
	 * @param view : the current view
	 * @param container : the semantic container of the current object
	 * @generated NOT
	 */
	public boolean performanceOverhead(EObject eObject, EObject view, EObject container) {
		int value = ((TimeCapacity) eObject).getValue();
		int executionTime = ((TimeCapacity) eObject).getCurrentExecutionTime();
		return value != 0 && value < executionTime;
	}

}