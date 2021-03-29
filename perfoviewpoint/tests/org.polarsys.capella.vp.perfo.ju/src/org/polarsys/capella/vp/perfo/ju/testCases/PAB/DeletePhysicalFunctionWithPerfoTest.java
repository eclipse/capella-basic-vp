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

import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.common.ef.command.AbstractReadWriteCommand;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.core.data.fa.AbstractFunction;
import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.test.framework.helpers.EObjectHelper;
import org.polarsys.capella.vp.perfo.perfo.impl.TimeCapacityImpl;

/**
 * This test case checks if when deleting a PhysicalFunction
 * the perfo of the FunctionalChains it is a part of are re-calculated.
 * Used to check if the listener responds to the notification REMOVE
 */
public class DeletePhysicalFunctionWithPerfoTest extends PABPerfoTest {

	@Override
	public void test() throws Exception {
		AbstractFunction physicalFunction3 = rootAbstractFunction.getOwnedFunctions().get(2);
		FunctionalChain functionalChain = physicalFunction3.getInvolvingFunctionalChains().get(0);
		
		ExecutionManager manager = TransactionHelper.getExecutionManager(physicalFunction3);
		manager.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {
				EObjectHelper.removeElement(physicalFunction3);
			}
		});
		
		// Deleting the physical function also deletes all functional exchanges it is linked to,
		// in this case FE2 is deleted
		assertEquals("The performance of FC1 was not changed after the deletion of PF3",
				((TimeCapacityImpl) functionalChain.getOwnedExtensions().get(0)).getCurrentExecutionTime(), 60);
	}

}
