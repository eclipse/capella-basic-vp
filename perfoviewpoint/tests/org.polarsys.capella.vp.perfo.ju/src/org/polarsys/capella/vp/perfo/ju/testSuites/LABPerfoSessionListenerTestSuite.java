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
package org.polarsys.capella.vp.perfo.ju.testSuites;

import java.util.ArrayList;
import java.util.List;

import org.polarsys.capella.test.framework.api.BasicTestArtefact;
import org.polarsys.capella.test.framework.api.BasicTestSuite;
import org.polarsys.capella.vp.perfo.ju.testCases.LAB.AddPerfoFunctionalChainTest;
import org.polarsys.capella.vp.perfo.ju.testCases.LAB.AddPerfoFunctionalExchangeTest;
import org.polarsys.capella.vp.perfo.ju.testCases.LAB.AddPerfoLogicalFunctionTest;
import org.polarsys.capella.vp.perfo.ju.testCases.LAB.DeleteFunctionalExchangeFromFunctionalChainTest;
import org.polarsys.capella.vp.perfo.ju.testCases.LAB.DeleteLogicalFunctionFromFunctionalChainTest;
import org.polarsys.capella.vp.perfo.ju.testCases.LAB.DeleteLogicalFunctionWithPerfoTest;
import org.polarsys.capella.vp.perfo.ju.testCases.LAB.DeletePerfoFunctionalExchangeTest;
import org.polarsys.capella.vp.perfo.ju.testCases.LAB.DeletePerfoLogicalFunctionTest;
import org.polarsys.capella.vp.perfo.ju.testCases.LAB.EditPerfoFunctionalExchangeTest;
import org.polarsys.capella.vp.perfo.ju.testCases.LAB.EditPerfoLogicalFunctionTest;

import junit.framework.Test;

public class LABPerfoSessionListenerTestSuite extends BasicTestSuite {

	/**
	 * Returns the suite. This is required to unary launch this test.
	 */
	public static Test suite() {
		return new LABPerfoSessionListenerTestSuite();
	}
	
	@Override
	protected List<BasicTestArtefact> getTests() {
		List<BasicTestArtefact> tests = new ArrayList<BasicTestArtefact>();
		tests.add(new AddPerfoFunctionalChainTest());
		tests.add(new AddPerfoFunctionalExchangeTest());
		tests.add(new AddPerfoLogicalFunctionTest());
		tests.add(new DeleteFunctionalExchangeFromFunctionalChainTest());
		tests.add(new DeleteLogicalFunctionFromFunctionalChainTest());
		tests.add(new DeleteLogicalFunctionWithPerfoTest());
		tests.add(new DeletePerfoFunctionalExchangeTest());
		tests.add(new DeletePerfoLogicalFunctionTest());
		tests.add(new EditPerfoFunctionalExchangeTest());
		tests.add(new EditPerfoLogicalFunctionTest());
		
		return tests;
	}

}
