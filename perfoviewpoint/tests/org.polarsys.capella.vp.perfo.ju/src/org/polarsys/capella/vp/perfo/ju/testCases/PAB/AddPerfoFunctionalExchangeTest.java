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
package org.polarsys.capella.vp.perfo.ju.testCases.PAB;

import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.core.data.fa.FunctionalExchange;
import org.polarsys.capella.vp.perfo.perfo.impl.TimeCapacityImpl;

/**
 * This test case checks if when adding a perfo to a FunctionalExchange the perfo of the
 * FunctionalChains it is a part of are re-calculated.
 * Used to check if the listener responds to the notification ADD
 */
public class AddPerfoFunctionalExchangeTest extends PABPerfoTest {

	@Override
	public void test() throws Exception {
		FunctionalExchange functionalExchange1 = rootAbstractFunction.getOwnedFunctionalExchanges().get(0);
		FunctionalChain functionalChain = functionalExchange1.getInvolvingFunctionalChains().get(0);
		
		addTimeConsumption(functionalExchange1, 40);
		assertEquals("The perfo of FC1 was not changed after the addition of a perfo to FE1",
				((TimeCapacityImpl) functionalChain.getOwnedExtensions().get(0)).getCurrentExecutionTime(), 150);

	}

}
