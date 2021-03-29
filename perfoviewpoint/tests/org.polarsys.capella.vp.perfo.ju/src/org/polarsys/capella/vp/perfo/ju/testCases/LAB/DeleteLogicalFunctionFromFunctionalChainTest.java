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

import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.common.ef.command.AbstractReadWriteCommand;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.core.data.fa.FunctionalChainInvolvement;
import org.polarsys.capella.test.framework.helpers.EObjectHelper;
import org.polarsys.capella.vp.perfo.perfo.impl.TimeCapacityImpl;

/**
 * This test case checks if when deleting a LogicalFunction from a
 * FunctionalChain, the perfo of the FunctionalChain is re-calculated.
 * Used to check if the listener responds to the notification REMOVE
 */
public class DeleteLogicalFunctionFromFunctionalChainTest extends LABPerfoTest {

	@Override
	public void test() throws Exception {
		FunctionalChain functionalChain1 = rootAbstractFunction.getOwnedFunctionalChains().get(0);
		FunctionalChainInvolvement functionalChainInvolvement = functionalChain1.getInvolvedFunctionalChainInvolvements().get(0);
		
		ExecutionManager manager = TransactionHelper.getExecutionManager(functionalChainInvolvement);
		manager.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {
				EObjectHelper.removeElement(functionalChainInvolvement);
			}
		});
		
		// Deleting the logical function also deletes all functional exchanges it is linked to,
		// in this case FE2 is deleted
		assertEquals("The performance of FC1 was not changed after the deletion of LF3 from the functional chain",
				((TimeCapacityImpl) functionalChain1.getOwnedExtensions().get(0)).getCurrentExecutionTime(), 60);
	}
	
}
