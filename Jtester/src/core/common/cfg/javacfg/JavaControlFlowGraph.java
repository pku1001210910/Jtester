package core.common.cfg.javacfg;

import java.util.Collection;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import core.common.cfg.interfaces.IExitNode;
import core.common.cfg.interfaces.IStartNode;
import core.common.cfg.model.ControlFlowGraph;

public class JavaControlFlowGraph extends ControlFlowGraph {

	public JavaControlFlowGraph(IStartNode start, Collection<IExitNode> exitNodes) {
		super(start, exitNodes);
	}

	public static JavaControlFlowGraph build(MethodDeclaration def) {
		return new ControlFlowGraphBuilder().build(def);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
