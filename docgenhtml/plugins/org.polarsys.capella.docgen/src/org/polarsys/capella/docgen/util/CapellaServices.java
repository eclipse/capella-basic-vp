/*******************************************************************************
 * Copyright (c) 2006, 2015 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *   Thales - initial API and implementation
 ******************************************************************************/
package org.polarsys.capella.docgen.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.polarsys.kitalpha.doc.gen.business.core.util.DocGenHtmlUtil;
import org.polarsys.kitalpha.doc.gen.business.core.util.EscapeChars;
import org.polarsys.kitalpha.doc.gen.business.core.util.LabelProviderHelper;

import org.polarsys.capella.core.data.capellacore.AbstractDependenciesPkg;
import org.polarsys.capella.core.data.capellacore.Classifier;
import org.polarsys.capella.core.data.capellacore.CapellaElement;

import org.eclipse.sirius.diagram.DSemanticDiagram;

public class CapellaServices {
	public static final String BOLD_BEGIN = "<b>";
	public static final String BOLD_END = "</b>";
	public static final String ITALIC_BEGIN = "<i>";
	public static final String ITALIC_END = "</i>";
	
	public static final String SPAN_BEGIN_LABEL = "<span class=\"label\">";
	public static final String SPAN_END = "</span>";
	
	public static final String DIV_WITH_PADDING = "<div style=\"padding-left:20px\">";
	public static final String DIV_END = "</div>";

	public static final String NEW_LINE = "<br/>";

	public static final String LI_OPEN = "<li>";
	public static final String LI_CLOSE = "</li>";

	public static final String UL_OPEN = "<ul class=\"generatedList\">";
	public static final String UL_OPEN_WITH_BORDER = "<ul class=\"generatedListWithBorder\">";
	public static final String UL_CLOSE = "</ul>";

	public static final String NONE = "None";
	
	public static final String NO_NAME = "<i>[No Name]</i>";

	public static final String SPACE = " ";

	public static final String EMPTY = "";

	public static final String VALUE_PRESENTER = " : ";
	public static final String VALUE_EQUAL = " = ";

	public static final String IS_ABSTRACT = "Is Abstract\t:\t";
	public static final String IS_PRIMITIVE = "Is Primitive\t:\t";
	public static final String IS_DISCRETE = "Is Discrete\t:\t";
	public static final String PATTERN = "Pattern\t:\t";
	public static final String DEFAULT_FEATURE = "Default\t:\t";
	public static final String MIN_FEATURE = "Min\t:\t";
	public static final String MAX_FEATURE = "Max\t:\t";
	public static final String NULL_FEATURE = "Null\t:\t";
	public static final String MINLENGTH_FEATURE = "Min. length\t:\t";
	public static final String MAXLENGTH_FEATURE = "Max. length\t:\t";
	public static final String PROP_ABSTRACT = "abstract  ";
	public static final String PROP_STATIC = "static  ";
	public static final String PROP_KEY = "key  ";
	public static final String PROP_DERIVED = "derived  ";
	public static final String PROP_READONLY = "readonly  ";
	public static final String PROP_ORDERED = "ordered  ";
	public static final String PROP_UNIQUE = "unique  ";
	public static final String PROP_GENERATED = "generated  ";
	public static final String PROP_CHANGEABLE = "changeable  ";
	public static final String PROP_CRO_ABSTRACT = "{abstract}  ";
	public static final String PROP_CRO_STATIC = "{static}  ";
	public static final String PROP_CRO_KEY = "{key}  ";
	public static final String PROP_CRO_DERIVED = "{derived}  ";
	public static final String PROP_CRO_READONLY = "{readonly}  ";
	public static final String PROP_CRO_ORDERED = "{ordered}  ";
	public static final String PROP_CRO_UNIQUE = "{unique}  ";
	public static final String PROP_CRO_GENERATED = "{generated}  ";
	public static final String PROP_CRO_CHANGEABLE = "{changeable}  ";

	public static final String MIN = "MIN  ";
	public static final String MAX = "MAX  ";
	public static final String DEFAULT = "DEFAULT  ";
	public static final String NULL = "NULL  ";
	
	public static final String UNDEFINED = "undefined";
	public static final String UNDEFINED_CHEVRON = "&lt;undefined&gt;";

	public static final String PAR_OPEN = "(  ";
	public static final String PAR_CLOSE = " )";
	public static final String CRO_OPEN = "[";
	public static final String CRO_CLOSE = "]";
	public static final String COMMA = ", ";
	
	public static final String CHEV_OPEN = "&lt";
	public static final String CHEV_CLOSE = "&gt";

	protected final static String HYPERLINK_OPEN = "<a href=\"";
	protected final static String HYPERLINK_SEPARATOR = "/";
	protected final static String HYPERLINK_COMPLETE = "\">";
	protected final static String HYPERLINK_CLOSE = "</a>";
	private static final Object PATH_OPEN = "../";
	private static final Object PATH_COMPLETE = ".html";

	public static String getHyperlinkFromElement(EObject element, String label) {
		if (element instanceof DSemanticDiagram) {
			return getHyperlinkFromDiagram((DSemanticDiagram) element);
		}
		int linked = isLinkable(element);
		// Get the representation name
		String text = label;
		// Format representation name with html rules
		text = EscapeChars.forHTML(text);
		// Initialize Buffer
		StringBuffer buffer = new StringBuffer();
		if (linked != -1) {
			// Add the opening href tag to the buffer
			buffer.append(HYPERLINK_OPEN);
			// Add the preoject root ressource to the buffer
			if (linked == 1) {
				buffer.append(getPathFromElement(element.eContainer()));
				buffer.append("#");
				buffer.append(getAnchorId(element));
			} else
				buffer.append(getPathFromElement(element));
			// Add the href tag completion to the buffer
			buffer.append(HYPERLINK_COMPLETE);
		}
		// Add the name to present to the buffer
		buffer.append(text);
		if (linked != -1) {
			// Add the close href tag to the buffer
			buffer.append(HYPERLINK_CLOSE);
		}
		// return the buffer
		return buffer.toString();
	}

	/**
	 * <b>Create a html hyper link from an element</b>
	 * <p>
	 * Create and format a html hyper link (a href) from a Capella Element
	 * 
	 * @param element
	 * @return
	 */
	public static String getHyperlinkFromElement(EObject element) {
		return getHyperlinkFromElement(element, LabelProviderHelper.getText(element));
	}

	public static boolean isElementLinkable(EObject element) {
		return isLinkable(element) >= 0;
	}

	/**
	 * 
	 * @param element
	 * @return 0 if element has page, 1 if parent element has page, otherwise -1
	 */
	public static int isLinkable(EObject element) {
		if (element instanceof CapellaElement) {
			if (DocGenHtmlCapellaControl.isPageCandidate((CapellaElement) element))
				return 0;
			else {
				EObject parent = element.eContainer();
				if (parent instanceof CapellaElement) {
					if (DocGenHtmlCapellaControl.isPageCandidate((CapellaElement) parent)) {
						return 1;
					}
				}
			}
		}
		return -1;
	}

	public static String getPathFromElement(EObject element) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(PATH_OPEN);
		// Add the project root resource to the buffer
		buffer.append(DocGenHtmlUtil.getModelName(element));
		// Add the Separator to the buffer
		buffer.append(HYPERLINK_SEPARATOR);
		// Add the file name to the buffer
		buffer.append(DocGenHtmlCapellaUtil.SERVICE.getFileName(element));
		// Add the href tag completion to the buffer
		buffer.append(PATH_COMPLETE);
		return buffer.toString();
	}

	private static String getHyperlinkFromDiagram(DSemanticDiagram diagram) {
		// Get the representation name
		String text = LabelProviderHelper.getText(diagram);
		// Format representation name with html rules
		text = EscapeChars.forHTML(text);
		// Initialize Buffer
		StringBuffer buffer = new StringBuffer();
		// Add the opening href tag to the buffer
		buffer.append(HYPERLINK_OPEN);
		// Add the project root resource to the buffer
		buffer.append(getPathFromElement(diagram.getTarget()));
		// Add diagram fragment id as link anchor
		buffer.append("#");
		buffer.append(getDiagramId(diagram));
		// Add the href tag completion to the buffer
		buffer.append(HYPERLINK_COMPLETE);
		// Add the name to present to the buffer
		buffer.append(text);
		// Add the close href tag to the buffer
		buffer.append(HYPERLINK_CLOSE);
		// return the buffer
		return buffer.toString();
	}

	public static String getAnchorId(EObject element) {
		String id = "id" + EcoreUtil.getURI(element).fragment();
		return id;
	}

	public static String getDiagramId(DSemanticDiagram diagram) {
		return diagram.eResource().getURIFragment(diagram);
	}

	public static String getImageLinkFromElement(EObject element, String projectName, String outputFolder) {
		String imageFileName = LabelProviderHelper.getImageFileName(element, projectName, outputFolder);
		StringBuffer buffer = new StringBuffer();
		buffer.append("<img src=\"../icon/");
		buffer.append(imageFileName);
		buffer.append("\" alt=\"");
		buffer.append(element.eClass().getName());
		buffer.append("\" />");
		return buffer.toString();
	}

	/**
	 * 
	 * @param elt
	 * @return
	 */
	public static String getFullDataPkgHierarchyLink(EObject elt) {
		return getFullDataPkgHierarchyLink(elt, true);
	}

	/**
	 * 
	 * @param elt
	 * @param linkedLastElement
	 *            whether to display a link on the last element of hierarchy.
	 * @return
	 */
	public static String getFullDataPkgHierarchyLink(EObject elt, boolean linkedLastElement) {
		StringBuffer buffer = new StringBuffer();
		for (EObject pkg : getPackageElementPath(elt)) {
			buffer.append(CapellaServices.getHyperlinkFromElement(pkg));
			buffer.append(".");
		}
		if (linkedLastElement)
			buffer.append(CapellaServices.getHyperlinkFromElement(elt));
		else {
			buffer.append(LabelProviderHelper.getText(elt));
		}
		return buffer.toString();
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	public static String getElementPath(EObject element) {
		StringBuffer buffer = new StringBuffer();
		Iterator<EObject> iterator = getFullElementPath(element).iterator();
		while (iterator.hasNext()) {
			buffer.append(CapellaServices.getHyperlinkFromElement(iterator.next()));
			if (iterator.hasNext()) {
				buffer.append(" > ");
			}
		}
		return buffer.toString();
	}

	private static List<EObject> getFullElementPath(EObject element) {
		List<EObject> eObjects = new ArrayList<EObject>();
		EObject parent = element.eContainer();
		if (parent instanceof EObject) {
			eObjects.addAll(getFullElementPath(parent));
		}
		eObjects.add(element);
		return eObjects;
	}

	private static List<EObject> getPackageElementPath(EObject element) {
		EObject parent = element.eContainer();
		if (parent instanceof AbstractDependenciesPkg || parent instanceof Classifier) {
			return getPkgTree(parent);
		}
		return Collections.emptyList();
	}

	private static List<EObject> getPkgTree(EObject pkg) {
		List<EObject> pkgs = new ArrayList<EObject>();
		pkgs.add(pkg);
		EObject parent = pkg.eContainer();
		if (parent instanceof AbstractDependenciesPkg) {
			pkgs.addAll(0, getPkgTree((AbstractDependenciesPkg) parent));
		}
		return pkgs;
	}

	public static List<EObject> removeDuplicates(List<EObject> list) {
		Set<EObject> set = new HashSet<EObject>(list);
		list.clear();
		list.addAll(set);
		return list;
	}

}