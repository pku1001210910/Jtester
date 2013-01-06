package core.common.cfg.javacfg;

import org.eclipse.jdt.core.dom.ASTNode;

import core.common.cfg.model.DecisionNode;


public class JavaDecisionNode extends DecisionNode {
	
	public void setNode(ASTNode node) {
		setData(node);
	}

	public ASTNode getNode() {
		return (ASTNode) getData();
	}
}
