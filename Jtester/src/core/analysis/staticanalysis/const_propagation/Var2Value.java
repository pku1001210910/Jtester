package core.analysis.staticanalysis.const_propagation;

public class Var2Value implements Cloneable{
	private String _varName;
	private String _value;
	private String _type;
	private boolean _isConstant;
	
	 @Override
	 public Object clone() throws CloneNotSupportedException {
	        return super.clone();
	 }
	 
	public Var2Value(Var2Value temp){
		this._varName = temp._varName;
		this._value = temp._value;
		this._type = temp._type;
		this._isConstant = temp._isConstant;
	}
	
	public Var2Value(String name, String value, String type, boolean flag){
		this._varName = name;
		this._value = value;
		this._type = type;
		this._isConstant = flag;
	}
	
	public void setVarName(String name){
		this._varName = name;
	}
	public String getVarName(){
		return this._varName;
	}
	
	public void setValue(String value){
		this._value = value;
	}
	public String getValue(){
		return this._value;
	}
	
	public void setType(String type){
		this._type = type;
	}
	public String getType(){
		return this._type;
	}
	
	public void setIsConstant(boolean flag){
		this._isConstant = flag;
	}
	public boolean getIsConstant(){
		return this._isConstant;
	}
	
	public String display(){
		if (this._isConstant){
			return this._varName + " -> " + this._value;
		} else {
			return this._varName + " -> " + "T";
		}
	}
}
