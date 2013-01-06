package core.common.cfg.javacfg;

import org.eclipse.jdt.core.dom.ASTNode;

import core.common.cfg.model.BranchNode;



public class JavaBranchNode extends BranchNode {
	private ASTNode labelData;
	JavaBranchNode(ASTNode label) {
		//TODO figure out how to use label
		super("JavaBranchNode Label");
		this.labelData = label;
	}

}