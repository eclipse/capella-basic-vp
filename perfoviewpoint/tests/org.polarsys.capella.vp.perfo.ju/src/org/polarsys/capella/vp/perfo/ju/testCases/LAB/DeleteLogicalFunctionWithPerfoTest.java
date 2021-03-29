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
import org.polarsys.capella.core.data.fa.AbstractFunction;
import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.test.framework.helpers.EObjectHelper;
import org.polarsys.capella.vp.perfo.perfo.impl.TimeCapacityImpl;

/**
 * This test case checks if when deleting a LogicalFunction
 * the perfo of the FunctionalChains it is a part of are re-calculated.
 * Used to check if the listener responds to the notification REMOVE
 */
public class DeleteLogicalFunctionWithPerfoTest extends LABPerfoTest {

	@Override
	public void test() throws Exception {
		AbstractFunction logicalFunction3 = rootAbstractFunction.getOwnedFunctions().get(2);
		FunctionalChain functionalChain = logicalFunction3.getInvolvingFunctionalChains().get(0);
		
		ExecutionManager manager = TransactionHelper.getExecutionManager(logicalFunction3);
		manager.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {
				EObjectHelper.removeElement(logicalFunction3);
			}
		});
		
		// Deleting the logical function also deletes all functional exchanges it is linked to,
		// in this case FE2 is deleted
		assertEquals("The performance of FC1 was not changed after the deletion of LF3",
				((TimeCapacityImpl) functionalChain.getOwnedExtensions().get(0)).getCurrentExecutionTime(), 60);
	}

}
