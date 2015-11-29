package relation.progelem;

import java.util.ArrayList;
import java.util.List;

public class AnnotatedFieldDecl {
	public String			superclazz;
	public String			clazz;
	public String			name;
	public String			type;

	public List<String>	normalAnnotationList	= new ArrayList<String>();
	public List<String>	markerAnnotationList	= new ArrayList<String>();

	public AnnotatedFieldDecl(String name, String type, String superclass, String clazz) {
		this.name = name;
		this.type = type;
		this.superclazz = superclass;
		this.clazz = clazz;
	}

	public AnnotatedFieldDecl() {
		/* ^.^ */
	}

	public void addNormalAnnotationList(String parm) {
		normalAnnotationList.add(parm);
	}

	public void addMarkerAnnotationList(String parm) {
		markerAnnotationList.add(parm);
	}

	public boolean compare(AnnotatedFieldDecl obj) {
		if (this.name.equals(obj.name))
			return true;
		return false;
	}

	public String toString() {
		String dotline = "\n------------------------------------------";
		String normalAnnotations = "";
		for (String elem : normalAnnotationList) {
			normalAnnotations += (elem + " ");
		}
		String markerAnnotations = "";
		for (String elem : markerAnnotationList) {
			markerAnnotations += (elem + " ");
		}
		return "   " + "Type: " + type +
				"\n   " + "Name: " + name +
				"\n   " + "Supr: " + superclazz +
				"\n   " + "Clzz: " + clazz +
				"\n   " + "@Normal: " + (normalAnnotations.trim().isEmpty() ? "-" : normalAnnotations.trim()) +
				"\n   " + "@Marker: " + (markerAnnotations.trim().isEmpty() ? "-" : markerAnnotations.trim()) + dotline;

	}

	public static class Util {
		/** @METHOD */
		public static boolean compare(List<AnnotatedFieldDecl> xfieldlist, List<AnnotatedFieldDecl> yfieldlist) {
			if (xfieldlist.size() != yfieldlist.size())
				return false;

			int counter = 0;
			for (AnnotatedFieldDecl theFieldDecl : xfieldlist) {
				if (contains(yfieldlist, theFieldDecl)) {
					counter++;
				}
			}
			return (counter == xfieldlist.size());
		}
	}

	/** @METHOD */
	public static boolean contains(List<AnnotatedFieldDecl> fieldlist, AnnotatedFieldDecl pField) {
		for (AnnotatedFieldDecl theFieldDecl : fieldlist) {
			if (theFieldDecl.compare(pField)) {
				return true;
			}
		}
		return false;
	}
}
