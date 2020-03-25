package org.processmining.aam.exp.util;

import java.util.List;

import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributable;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeLiteral;

public class XHierarchyExtension {

	public static final String KEY_NAME = "hier:name";
	
	static XFactory factory = XFactoryRegistry.instance().currentDefault();
	
	private XHierarchyExtension() {
//		super("Concept", "concept", EXTENSION_URI);
		
		
	}
	
	
	public static String extractHierarchyLabel(XAttributable element) {
		XAttribute attribute = element.getAttributes().get(KEY_NAME);
		if (attribute == null) {
			return null;
		} else {
			return ((XAttributeLiteral) attribute).getValue();
		}
	}

	public static void assignHierarchyLabel(XAttributable element, List<String> path) {
		String label = UtilIO.concatenate(path, "_");
		if (label != null && label.trim().length() > 0) {
			XAttributeLiteral attr = factory.createAttributeLiteral(KEY_NAME, "__INVALID__",
					null);
			attr.setValue(label);
			element.getAttributes().put(KEY_NAME, attr);
		}
	}

}
