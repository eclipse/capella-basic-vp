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
import org.polarsys.capella.vp.perfo.ju.testCases.PAB.AddPerfoFunctionalChainTest;
import org.polarsys.capella.vp.perfo.ju.testCases.PAB.AddPerfoFunctionalExchangeTest;
import org.polarsys.capella.vp.perfo.ju.testCases.PAB.AddPerfoPhysicalFunctionTest;
import org.polarsys.capella.vp.perfo.ju.testCases.PAB.DeleteFunctionalExchangeFromFunctionalChainTest;
import org.polarsys.capella.vp.perfo.ju.testCases.PAB.DeletePerfoFunctionalExchangeTest;
import org.polarsys.capella.vp.perfo.ju.testCases.PAB.DeletePerfoPhysicalFunctionTest;
import org.polarsys.capella.vp.perfo.ju.testCases.PAB.DeletePhysicalFunctionFromFunctionalChainTest;
import org.polarsys.capella.vp.perfo.ju.testCases.PAB.DeletePhysicalFunctionWithPerfoTest;
import org.polarsys.capella.vp.perfo.ju.testCases.PAB.EditPerfoFunctionalExchangeTest;
import org.polarsys.capella.vp.perfo.ju.testCases.PAB.EditPerfoPhysicalFunctionTest;

import junit.framework.Test;

public class PABPerfoSessionListenerTestSuite extends BasicTestSuite {

	/**
	 * Returns the suite. This is required to unary launch this test.
	 */
	public static Test suite() {
		return new PABPerfoSessionListenerTestSuite();
	}
	
	@Override
	protected List<BasicTestArtefact> getTests() {
		List<BasicTestArtefact> tests = new ArrayList<BasicTestArtefact>();
		tests.add(new AddPerfoFunctionalChainTest());
		tests.add(new AddPerfoFunctionalExchangeTest());
		tests.add(new AddPerfoPhysicalFunctionTest());
		tests.add(new DeleteFunctionalExchangeFromFunctionalChainTest());
		tests.add(new DeletePerfoFunctionalExchangeTest());
		tests.add(new DeletePerfoPhysicalFunctionTest());
		tests.add(new DeletePhysicalFunctionFromFunctionalChainTest());
		tests.add(new DeletePhysicalFunctionWithPerfoTest());
		tests.add(new EditPerfoFunctionalExchangeTest());
		tests.add(new EditPerfoPhysicalFunctionTest());
		
		return tests;
	}
}
