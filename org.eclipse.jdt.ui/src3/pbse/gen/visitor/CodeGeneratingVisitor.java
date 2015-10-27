/*
 * @(#) CodeGeneratingVisitor.java
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package pbse.gen.visitor;

import java.io.FileWriter;
import java.io.IOException;

import metadata.invariant.pbse.Comm;
import metadata.invariant.pbse.STR;

import pbse.gen.ParameterHelper;
import pbse.gen.node.ClazzRefNode;
import pbse.gen.node.FieldRefNode;
import pbse.gen.node.MethodRefNode;
import pbse.gen.node.WhereBodyNode;
import pbse.gen.node.WhereQueryNode;

/**
 * @author Myoungkyu Song
 * @date Aug 11, 2011
 * @since JDK1.6
 */
public class CodeGeneratingVisitor extends ParameterHelper implements NodeVisitor {
	FileWriter	filewriter	= null;

	public void visitClazzRef(ClazzRefNode node)
	{
		println(node.getName());
	}

	public void visitMethodRef(MethodRefNode node)
	{
		println(node.getName());
	}

	public void visitFieldRef(FieldRefNode node)
	{
		if (getPattern().startsWith(STR.pbse_ptrn_contains + "(" + // "CONTAINS("
				STR.var_metadata + ".ATTRVAL")) { // "$METADATA.ATTRVAL"
			println(sp_indent + node.getName());
		}
		else
			println(node.getName());
	}

	public void visitWhereQuery(WhereQueryNode node)
	{
		println(node.getQuery());
	}

	public void visitWhereBody(WhereBodyNode node)
	{
		println(node.getBody());
	}

	public void fileopen()
	{
		try {
			filewriter = new FileWriter("outpbse/" + Comm.getGlobalParm("run") + ".pbse");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void println(String buf)
	{
		System.out.println(buf);
		try {
			filewriter.write(buf + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fileclose()
	{
		try {
			filewriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
