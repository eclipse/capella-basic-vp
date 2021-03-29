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

import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.common.ef.ExecutionManager;
import org.polarsys.capella.common.ef.command.AbstractReadWriteCommand;
import org.polarsys.capella.common.helpers.TransactionHelper;
import org.polarsys.capella.core.data.fa.AbstractFunction;
import org.polarsys.capella.core.data.fa.FunctionalChain;
import org.polarsys.capella.test.framework.helpers.EObjectHelper;
import org.polarsys.capella.vp.perfo.perfo.impl.TimeCapacityImpl;

/**
 * This test case checks if when deleting a perfo from a LogicalFunction
 * the perfo of the FunctionalChains it is a part of are re-calculated.
 * Used to check if the listener responds to the notification REMOVE
 */
public class DeletePerfoLogicalFunctionTest extends LABPerfoTest {

	@Override
	public void test() throws Exception {
		AbstractFunction logicalFunction3 = rootAbstractFunction.getOwnedFunctions().get(2);
		FunctionalChain functionalChain = logicalFunction3.getInvolvingFunctionalChains().get(0);
		EObject perfoObject = logicalFunction3.getOwnedExtensions().get(0);
		
		ExecutionManager manager = TransactionHelper.getExecutionManager(perfoObject);
		manager.execute(new AbstractReadWriteCommand() {

			@Override
			public void run() {
				EObjectHelper.removeElement(perfoObject);
			}
		});
		
		assertEquals("The performance of FC1 was not changed after the deletion of the performance of LF3",
				((TimeCapacityImpl) functionalChain.getOwnedExtensions().get(0)).getCurrentExecutionTime(), 90);
	}

}
