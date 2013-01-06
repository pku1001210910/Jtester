package core.common.cfg.javacfg;

import org.eclipse.jdt.core.dom.ASTNode;

import core.common.cfg.interfaces.IExitNode;
import core.common.cfg.model.ExitNode;



public class JavaExitNode extends ExitNode implements IExitNode {
	
	public void setNode(ASTNode node) {
		setData(node);
	}

	public ASTNode getNode() {
		return (ASTNode) getData();
	}
}
