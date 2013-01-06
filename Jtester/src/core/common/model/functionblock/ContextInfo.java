package core.common.model.functionblock;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Initializer;

public class ContextInfo {
	private Expression callerName;
	private int callerLine;
	private Expression calleeName;
	private Initializer[] calleeArguments;

	
	public ContextInfo(Expression erName, Expression eeName, Initializer[] arguments, int line){
		this.callerName = erName;
		this.calleeName = eeName;
		this.calleeArguments = arguments;
		this.callerLine = line;
	}
	
	public Expression getCalleeName(){
		return this.calleeName;
	}
	
	public Initializer[] getCallerArguments(){
		return this.calleeArguments;
	}
	
	public int getCallerLine(){
		return this.callerLine;
	}
}
