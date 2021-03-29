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
package org.polarsys.capella.vp.perfo.ju.testCases.LAB;

import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.vp.perfo.perfo.impl.TimeCapacityImpl;

/**
 * This test case checks if when adding a perfo to a FunctionalChain it is
 * correctly computed
 * Used to check if the listener responds to the notification ADD
 */
public class AddPerfoFunctionalChainTest extends LABPerfoTest {

	@Override
	public void test() throws Exception {
		FunctionalChain functionalChain2 = rootAbstractFunction.getOwnedFunctionalChains().get(1);
		
		addTimeCapacity(functionalChain2);
		assertEquals("The perfo of FC2 was not computed after the addition of a perfo to it",
				((TimeCapacityImpl) functionalChain2.getOwnedExtensions().get(0)).getCurrentExecutionTime(), 130);

	}

}
