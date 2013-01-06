package core.common.model.functionblock;

import core.common.cfg.javacfg.JavaControlFlowGraph;

public class FunctionInfo {
	private String funcName;
	private ContextInfo context;
	private JavaControlFlowGraph javacfg;

	public void setFuncName(String name) {
		this.funcName = name;
	}

	public String getFuncName() {
		return this.funcName;
	}

	public void setContextInfo(ContextInfo context) {
		this.context = context;
	}

	public ContextInfo getContextInfo() {
		return this.context;
	}

	public void setJavaControlFlowGraph(JavaControlFlowGraph javacfg) {
		this.javacfg = javacfg;
	}

	public JavaControlFlowGraph getJavaControlFlowGraph() {
		return this.javacfg;
	}
}
