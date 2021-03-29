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
package org.polarsys.capella.vp.mass.ju.testSuites;

import java.util.ArrayList;
import java.util.List;

import org.polarsys.capella.test.framework.api.BasicTestArtefact;
import org.polarsys.capella.test.framework.api.BasicTestSuite;
import org.polarsys.capella.vp.mass.ju.testCases.AddMassBehaviorTest;
import org.polarsys.capella.vp.mass.ju.testCases.AddMassNodeTest;
import org.polarsys.capella.vp.mass.ju.testCases.AddOneBehaviorOneNodeWithMassTest;
import org.polarsys.capella.vp.mass.ju.testCases.DeleteBehaviorWithMassTest;
import org.polarsys.capella.vp.mass.ju.testCases.DeleteManyNodesWithMassTest;
import org.polarsys.capella.vp.mass.ju.testCases.DeleteNodeWithMassTest;
import org.polarsys.capella.vp.mass.ju.testCases.DeletePartMassBehaviorTest;
import org.polarsys.capella.vp.mass.ju.testCases.DeletePartMassNodeTest;
import org.polarsys.capella.vp.mass.ju.testCases.EditMassBehaviorTest;
import org.polarsys.capella.vp.mass.ju.testCases.EditMassNodeTest;
import org.polarsys.capella.vp.mass.ju.testCases.MassCalculatedOpeningSessionTest;

import junit.framework.Test;

/**
 * This test suite starts tests that check whether the mass listeners are
 * correctly installed, and if the mass of the elements is correctly calculated
 * after certain events
 */
public class MassSessionListenerTestSuite extends BasicTestSuite {

	/**
	 * Returns the suite. This is required to unary launch this test.
	 */
	public static Test suite() {
		return new MassSessionListenerTestSuite();
	}

	@Override
	protected List<BasicTestArtefact> getTests() {
		List<BasicTestArtefact> tests = new ArrayList<BasicTestArtefact>();
		tests.add(new MassCalculatedOpeningSessionTest());
		tests.add(new EditMassNodeTest());
		tests.add(new EditMassBehaviorTest());
		tests.add(new AddMassNodeTest());
		tests.add(new AddMassBehaviorTest());
		tests.add(new DeleteNodeWithMassTest());
		tests.add(new DeleteBehaviorWithMassTest());
		tests.add(new DeletePartMassBehaviorTest());
		tests.add(new DeletePartMassNodeTest());
		tests.add(new AddOneBehaviorOneNodeWithMassTest());
		tests.add(new DeleteManyNodesWithMassTest());
		return tests;
	}

}
