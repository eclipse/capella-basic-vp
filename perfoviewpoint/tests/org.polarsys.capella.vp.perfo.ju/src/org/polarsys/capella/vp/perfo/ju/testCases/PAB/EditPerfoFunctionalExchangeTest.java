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
import org.polarsys.capella.vp.perfo.perfo.impl.TimeConsumptionImpl;

/**
 * This test case checks if when editing the perfo of a FunctionalExchange the perfo of
 * the FunctionalChains it is a part of are re-calculated.
 * Used to check if the listener responds to the notification SET
 */
public class EditPerfoFunctionalExchangeTest extends PABPerfoTest {

	@Override
	public void test() throws Exception {
		FunctionalExchange functionalExchange2 = rootAbstractFunction.getOwnedFunctionalExchanges().get(1);
		FunctionalChain functionalChain = functionalExchange2.getInvolvingFunctionalChains().get(0);
		
		editPerfoFunctionalExchange(functionalExchange2, 50);
		
		assertEquals("The performance of FE2 was not changed after its modification",((TimeConsumptionImpl) functionalExchange2.getOwnedExtensions().get(0)).getValue(), 50);
		assertEquals("The performance of FC1 was not changed after the modification of FE2", ((TimeCapacityImpl) functionalChain.getOwnedExtensions().get(0)).getCurrentExecutionTime(),
				130);

	}

}
