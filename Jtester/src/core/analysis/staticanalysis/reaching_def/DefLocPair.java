package core.analysis.staticanalysis.reaching_def;

public class DefLocPair {
	private String _varName;
	private int _startLine;
	
	public DefLocPair(String name, int line){
		this._varName = name;
		this._startLine = line;
	}
	
	public int getStartLine(){
		return this._startLine;
	}
	
	public void setStartLine(int line){
		this._startLine = line;
	}
	
	public String getVarName(){
		return this._varName;
	}
	public void setVarName(String name){
		this._varName = name;		
	}
	
	public String displayPair(){
		return "( " + this._varName + ", " + this._startLine + " )";
	}
}
