/*
 * @(#) FieldRefNode.java
 * 
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package pbse.gen.node;

import pbse.gen.visitor.CodeGeneratingVisitor;

/**
 * @author Myoungkyu Song
 * @date Aug 11, 2011
 * @since JDK1.6
 */
public class FieldRefNode implements Node {
	private String	name	= "Field f in c"; // default

	public String getName() {
		return name;
	}

	@Override
	public void accept(CodeGeneratingVisitor visitor) {
		visitor.visitFieldRef(this);
	}

}
