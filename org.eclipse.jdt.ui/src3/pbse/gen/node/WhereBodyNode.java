/*
 * @(#) WhereStmtNode.java
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package pbse.gen.node;

import metadata.invariant.pbse.Comm;
import metadata.invariant.pbse.STR;
import pbse.gen.ParameterHelper;
import pbse.gen.visitor.CodeGeneratingVisitor;

/**
 * @author Myoungkyu Song
 * @date Aug 11, 2011
 * @since JDK1.6
 */
public class WhereBodyNode extends ParameterHelper implements Node {
	private String				body;

	private Helper4Clazz		helper4clazz	= new Helper4Clazz();
	private Helper4Method	helper4method	= new Helper4Method();
	private Helper4Field		helper4field	= new Helper4Field();

	@Override
	public void accept(CodeGeneratingVisitor visitor)
	{
		visitor.visitWhereBody(this);
	}

	/** @METHOD */
	public String getBody()
	{
		if (relationContains("class") || relationVarContains("class")) {
			helper4clazz.getBody();
		}
		else if (relationContains("method")) {
			helper4method.getBody();
		}
		else if (relationContains("field")) {
			helper4field.getBody();
		}
		return (body = Aux.removeLF(body));
	}

	/**
	 * @author Myoungkyu Song
	 * @date Aug 12, 2011
	 * @since JDK1.6
	 */
	class Helper4Clazz {

		/** @METHOD */
		public void getBody()
		{
			body = sp_indent + pbse_class + sp + pbse_op_append + " @";

			String pattern = getPattern();

			if (relationVarContains(parm_metadata)) {

				body += (parm_metadata + "\n");

				if (getPattern().startsWith(pbse_ptrn_match)) {

					body += (sp_indent + "@" + parm_metadata + " = ");

					if (pattern.contains(var_classnamebig))
						body += (pbse_classname + "\n");
				}
			}
			else if (Comm.getParm(parm_relation).contains(parm_annotation + "Name")) {
				// e.g., className, annotationName, @Configuration
				body += (getRelationParameter(parm_annotation) + "\n");
			}
			else if (Comm.getParm(parm_metadata).equals(parm_xml)) {
				// if metdata associates with "xml",..

				String relationVar = Comm.getParm("relation-var");

				// if xml tag doesn't express the name of program construct,
				// we define variable of relation separately,...
				if ((relationVar != null) && (!relationVar.isEmpty())) {

					String arrayRelationVar[] = relationVar.split(",");
					assert (arrayRelationVar.length == 2);
					String tagpart = arrayRelationVar[0];

					String arrayTagpart[] = tagpart.split("-");
					assert (arrayTagpart.length == 2);

					body += (arrayTagpart[0] + "\n");
					body += (sp_indent + "@" + arrayTagpart[0] + "." + arrayTagpart[1] + sp + "=" + sp);

					String rex = getRex(pattern);
					body += (rex + "\n");
				}
				else {
					String tag = getRelationParameter(parm_tag);
					String attr = getRelationParameter(parm_attr);

					body += (tag + "\n");
					body += (sp_indent + "@" + tag + "." + attr + " = ");

					String rex = getRex(pattern);
					body += (rex + "\n");
				}
			}
		}
	}

	/**
	 * @author Myoungkyu Song
	 * @date Aug 12, 2011
	 * @since JDK1.6
	 */
	class Helper4Method {

		/** @METHOD */
		public void getBody()
		{
			body = sp_indent;
			String pattern = getPattern();

			if (pattern.startsWith(pbse_ptrn_match)) {

				String parmRelation = Comm.getParm("relation");

				if (parmRelation.split(",").length == 3 && //
						parmRelation.split(",")[1].trim().equalsIgnoreCase("ANNOTATION") && //
						parmRelation.split(",")[2].trim().startsWith("@")) {

					String annotation = getRelationParameter(parm_annotation);
					body += STR.mil_assert + "(" + "@" + annotation + " " + pbse_method;
					body += ")" + "\n";
				}
				else if (Comm.getParm("relation").split(",").length > 3 &&
						Comm.getParm("relation").split(",")[3].trim().startsWith("@") &&
						Comm.getParm("relation").split(",")[3].trim().contains("*")) {

					// String annotationPattern = Comm.getParm("relation").split(",")[3].trim();
					// String annotationVar = getPatternParameter(parm_annotation);
					// String methodVar = getPatternParameter(parm_method);

					String annotation = getRelationParameter(parm_annotation);

					body += (pbse_method + sp + pbse_op_append + sp + "@" + annotation + "\n");

					// String ptrnArray[] = getPatternParameters(pattern);

					String partOfAnnot = getPatternParameter(parm_annotation + "|" + parm_annotation.toUpperCase());
					String partOfProgC = getPatternParameter(parm_method + "|" + parm_method.toUpperCase());

					String partOfAnnotAttr = "", partOfPcAttr = "";
					for (int i = partOfAnnot.length() - 1; i > -1; i--) {
						if (partOfAnnot.charAt(i) == '.' || partOfAnnot.charAt(i) == '_')
							break;
						partOfAnnotAttr = (String.valueOf(partOfAnnot.charAt(i)) + partOfAnnotAttr);
					}
					for (int i = partOfProgC.length() - 1; i > -1; i--) {
						if (partOfProgC.charAt(i) == '.' || partOfProgC.charAt(i) == '_')
							break;
						partOfPcAttr = (String.valueOf(partOfProgC.charAt(i)) + partOfPcAttr);
					}

					body +=
							(sp_indent + "@" + annotation + "." + partOfAnnotAttr.toLowerCase() + sp + "=" + sp +
									pbse_method + "." + partOfPcAttr.toLowerCase() + "\n");

					// body += (annotationPattern);
					//
					// if (annotationVar.contains(".NAME"))
					// body += ("." + STR.parm_name2);
					//
					// body += (sp + "=" + sp);
					//
					// if (methodVar.contains("RETURNTYPE"))
					// body += (pbse_methodreturntype);
				}
			}
			else if (pattern.startsWith(pbse_ptrn_contains)) {

				if (hasVarInPattern(var_method, pattern) && hasVarInPattern(var_superclass, pattern)) {
					body += STR.mil_assert + "(" + STR.pbse_class + "." + "super" + " " + //
							STR.mil_has + " " + STR.pbse_method;
					body += ")" + "\n";
				}
				// body += (pbse_method + sp + pbse_op_append + sp + "@" +
				// getRelationParameter(parm_annotation));
			}
		}
	}

	/**
	 * @author Myoungkyu Song
	 * @date Aug 12, 2011
	 * @since JDK1.6
	 */
	class Helper4Field {

		/** @METHOD */
		public void getBody()
		{
			body = sp_indent;

			if (getPattern().startsWith(STR.pbse_ptrn_contains + "(" + // "CONTAINS("
					STR.var_metadata + ".ATTRVAL")) { // "$METADATA.ATTRVAL"

				String annotation = Comm.getParm("relation").split(",")[3].trim();
				String annotationVar = getPatternParameter(parm_annotation + "|" + var_metadata);

				if (annotationVar.contains(STR.parm_name)) {
					// TODO
				}
				else if (annotationVar.contains(parm_attrval)) {
					body += sp_indent + STR.mil_assert_exists + "(";
					body += (annotation + "." + Comm.getParm("relation").split(",")[2].trim() + " " + //
							STR.mil_has + " " + pbse_fieldname);
					body += ")" + "\n";
				}
			}
			else if (getPattern().startsWith(STR.pbse_ptrn_match)) {
				String annotation = Comm.getParm("relation").split(",")[3].trim();
				String annotationVar = getPatternParameter(parm_annotation + "|" + var_metadata);

				if (annotationVar.contains("$" + STR.relation_annotation_attribute + "." + STR.parm_name)) {
					body += STR.mil_assert + "(";
					body += annotation + "." + STR.parm_name2 + " " + STR.mil_eq + " " + pbse_fieldname + " | ";
					body += annotation + "." + STR.parm_name2 + " " + STR.mil_eq + " " + "*" + pbse_fieldname + " | ";
					body += annotation + "." + STR.parm_name2 + " " + STR.mil_eq + " " + //
							"*Lc" + "(" + pbse_fieldname + ")";
					body += ")" + "\n";
				}
				else if (Comm.getParm(STR.parm_relation).split(",").length > 3 &&
						Comm.getParm("relation").split(",")[3].trim().startsWith("@")) {
					annotation = Comm.getParm("relation").split(",")[3].trim();
					annotationVar = getPatternParameter(parm_annotation + "|" + var_metadata);
					body += (pbse_field + sp + pbse_op_append + sp + annotation + "\n");

					if (annotationVar.contains(STR.parm_name))
						body += (sp_indent + annotation + "." + STR.parm_name2 + " = " + pbse_fieldname + "\n");
					else if (annotationVar.contains("ATTRVAL"))
						body +=
								(sp_indent + annotation + "." + Comm.getParm("relation").split(",")[2].trim() + " = " +
										pbse_fieldname + "\n");
				}
			}
			else if (getPattern().startsWith("CONTAINS")) {
				if (Comm.getParm("relation").split(",").length > 3 &&
						Comm.getParm("relation").split(",")[3].trim().startsWith("@")) {
					String annotation = Comm.getParm("relation").split(",")[3].trim();
					String annotationVar = getPatternParameter(parm_annotation + "|" + var_metadata);
					body += (pbse_field + sp + pbse_op_append + sp + annotation + "\n");

					if (annotationVar.contains(STR.parm_name2))
						body += (sp_indent + annotation + "." + STR.parm_name2 + " -> " + pbse_fieldname + "\n");
					else if (annotationVar.contains("ATTRVAL"))
						body +=
								(sp_indent + annotation + "." + Comm.getParm("relation").split(",")[2].trim() + " -> " +
										pbse_fieldname + "\n");
				}
			}
		}
	}

	/**
	 * @author Myoungkyu Song
	 * @date Aug 12, 2011
	 * @since JDK1.6
	 */
	static class Aux {

		/** @METHOD */
		static String removeLF(String body)
		{
			for (int i = body.length() - 1; i > -1; i--) {
				if (body.charAt(i) == '\n') {
					char[] arraydata = body.toCharArray();
					arraydata[i] = ' ';
					body = String.valueOf(arraydata);
					break;
				}
			}
			return body;
		}
	}
}
