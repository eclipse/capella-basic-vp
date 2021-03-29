/*******************************************************************************
 * Copyright (c) 2020 Obeo
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * 
 *   Contributors:
 *      Obeo - initial API and implementation
 ******************************************************************************/
package org.polarsys.capella.vp.price.ju.testSuites;

import java.util.ArrayList;
import java.util.List;

import org.polarsys.capella.test.framework.api.BasicTestArtefact;
import org.polarsys.capella.test.framework.api.BasicTestSuite;
import org.polarsys.capella.vp.price.ju.testCases.AddOneBehaviorOneNodeWithPriceTest;
import org.polarsys.capella.vp.price.ju.testCases.AddPriceBehaviorTest;
import org.polarsys.capella.vp.price.ju.testCases.AddPriceNodeTest;
import org.polarsys.capella.vp.price.ju.testCases.DeleteBehaviorWithPriceTest;
import org.polarsys.capella.vp.price.ju.testCases.DeleteManyNodesWithPriceTest;
import org.polarsys.capella.vp.price.ju.testCases.DeleteNodeWithPriceTest;
import org.polarsys.capella.vp.price.ju.testCases.DeletePartPriceBehaviorTest;
import org.polarsys.capella.vp.price.ju.testCases.DeletePartPriceNodeTest;
import org.polarsys.capella.vp.price.ju.testCases.EditPriceBehaviorTest;
import org.polarsys.capella.vp.price.ju.testCases.EditPriceNodeTest;
import org.polarsys.capella.vp.price.ju.testCases.PriceCalculatedOpeningSessionTest;

import junit.framework.Test;

/**
 * This test suite starts tests that check whether the price listeners are
 * correctly installed, and if the price of the elements is correctly calculated
 * after certain events
 */
public class PriceSessionListenerTestSuite extends BasicTestSuite {

	/**
	 * Returns the suite. This is required to unary launch this test.
	 */
	public static Test suite() {
		return new PriceSessionListenerTestSuite();
	}

	@Override
	protected List<BasicTestArtefact> getTests() {
		List<BasicTestArtefact> tests = new ArrayList<BasicTestArtefact>();
		tests.add(new PriceCalculatedOpeningSessionTest());
		tests.add(new EditPriceNodeTest());
		tests.add(new EditPriceBehaviorTest());
		tests.add(new AddPriceNodeTest());
		tests.add(new AddPriceBehaviorTest());
		tests.add(new DeleteNodeWithPriceTest());
		tests.add(new DeleteBehaviorWithPriceTest());
		tests.add(new DeletePartPriceBehaviorTest());
		tests.add(new DeletePartPriceNodeTest());
		tests.add(new AddOneBehaviorOneNodeWithPriceTest());
		tests.add(new DeleteManyNodesWithPriceTest());
		return tests;
	}

}
