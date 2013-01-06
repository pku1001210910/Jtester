package core.common.cfg.javacfg;

import org.eclipse.jdt.core.dom.ASTNode;

import core.common.cfg.model.PlainNode;



public class JavaPlainNode extends PlainNode {
	
	public void setNode(ASTNode node) {
		setData(node);
	}

	public ASTNode getNode() {
		return (ASTNode) getData();
	}
}
