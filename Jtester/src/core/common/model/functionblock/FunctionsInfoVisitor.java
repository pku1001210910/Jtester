package core.common.model.functionblock;

import java.util.HashMap;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import core.common.cfg.javacfg.JavaControlFlowGraph;
import core.common.model.jobflow.IJob;
import core.common.model.jobflow.JobConst;
import core.common.model.test.TestResultItem;
import core.common.model.test.TestData;
import core.common.model.test.TestFile;

public class FunctionsInfoVisitor extends ASTVisitor implements IJob {
	private String name = this.getClass().getSimpleName();
	
	private HashMap<String, FunctionInfo> functionsInfo;
	private int funCount = 0;

	public FunctionsInfoVisitor() {
		this(true);
	}

	public FunctionsInfoVisitor(boolean visitNodes) {
		super(visitNodes);
	}

	@Override
	public boolean run(TestData data) {
		if (data == null) {
			return false;
		}
		
		functionsInfo = new HashMap<String, FunctionInfo>();
		funCount = 0;
		
		TestFile file = data.getCurrentTestFile();
		Object astObj = file.get(JobConst.AST);
		if (astObj == null) {
			TestResultItem item = new TestResultItem(file.getPath(), getName(), JobConst.AST_NOT_FOUND);
			data.getTestResult().add(file.getPath(), item);
			return false;
		}

		ASTNode n = (ASTNode) astObj;
		n.accept(this);

		file.put(JobConst.FUNCTIONSINFO, functionsInfo);
		return true;
	}

	public boolean visit(MethodDeclaration n) {
		FunctionInfo functionInfo = new FunctionInfo();
		String functionName = ((MethodDeclaration) n).getName().getFullyQualifiedName();
		JavaControlFlowGraph javacfg = JavaControlFlowGraph.build((MethodDeclaration) n);
		functionInfo.setFuncName(functionName);
		functionInfo.setJavaControlFlowGraph(javacfg);
		functionsInfo.put("f" + funCount, functionInfo);
		this.funCount++;

		return true;
	}

	// visit methods
	public boolean visit(CompilationUnit n) {
		return true;
	}

	// leave methods
	public boolean leave(CompilationUnit tu) {
		return true;
	}

	// TODO
	// i changed AmbiguousNode to ASTNode
	// check it
	public boolean visit(ASTNode astAmbiguousNode) {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}
}
