package core.common.cfg.javacfg;

import org.eclipse.jdt.core.dom.ASTNode;

import core.common.cfg.interfaces.IBranchNode;
import core.common.cfg.interfaces.ICfgData;
import core.common.cfg.interfaces.IDecisionNode;
import core.common.cfg.interfaces.IExitNode;
import core.common.cfg.interfaces.INodeFactory;
import core.common.cfg.interfaces.IPlainNode;
import core.common.cfg.model.NodeFactory;



public class JavaNodeFactory extends NodeFactory implements INodeFactory {
	public JavaNodeFactory() {
		super();
	}

	public IPlainNode createPlainNode() {
		return new JavaPlainNode();
	}

	public IDecisionNode createDecisionNode() {
		return new JavaDecisionNode();
	}

	public IExitNode createExitNode() {
		return new JavaExitNode();
	}

	public JavaPlainNode createPlainNode(ASTNode ast) {
		IPlainNode node = createPlainNode();
		((ICfgData) node).setData(ast);
		return (JavaPlainNode) node;
	}

	public JavaDecisionNode createDecisionNode(ASTNode ast) {
		IDecisionNode node = createDecisionNode();
		((ICfgData) node).setData(ast);
		return (JavaDecisionNode) node;
	}

	public JavaExitNode createExitNode(ASTNode ast) {
		IExitNode node = createExitNode();
		((ICfgData) node).setData(ast);
		return (JavaExitNode) node;
	}

	public IBranchNode createBranchNode(ASTNode caseSt) {
		IBranchNode node = createBranchNode("Java Branch");
		((ICfgData) node).setData(caseSt);
		return node;
	}
}
