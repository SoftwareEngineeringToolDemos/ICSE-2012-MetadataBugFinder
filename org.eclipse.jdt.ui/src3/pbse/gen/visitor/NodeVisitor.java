/*
 * @(#) NodeVisitor.java
 * 
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package pbse.gen.visitor;

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
public interface NodeVisitor {
	public void visitClazzRef(ClazzRefNode node);

	public void visitMethodRef(MethodRefNode node);

	public void visitFieldRef(FieldRefNode node);

	public void visitWhereQuery(WhereQueryNode node);

	public void visitWhereBody(WhereBodyNode node);
}
