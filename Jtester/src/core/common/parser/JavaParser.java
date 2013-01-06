package core.common.parser;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import core.common.model.jobflow.IJob;
import core.common.model.jobflow.JobConst;
import core.common.model.test.TestData;
import core.common.model.test.TestFile;

public class JavaParser implements IJob{
	private String name = this.getClass().getName();

	public JavaParser(){
	}
	
	@Override
	public boolean run(TestData data) {
		TestFile file = data.getCurrentTestFile();
        CompilationUnit tu= (CompilationUnit) parseCode(file.getCode());
		if(tu==null){ 
			return false;
		}
		else{		
			// 将AST结果存入data
			file.put(JobConst.AST, tu);
			return true;
		}
	}

	public ASTNode parseCode(String code) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);    
		parser.setKind(ASTParser.K_COMPILATION_UNIT);     
		parser.setSource(code.toCharArray());
		parser.setResolveBindings(true);  
		CompilationUnit result = (CompilationUnit) parser.createAST(null);  

		return result;
	}

	@Override
	public String getName() {
		return name;
	}

}
