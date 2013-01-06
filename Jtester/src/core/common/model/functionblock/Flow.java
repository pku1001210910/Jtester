package core.common.model.functionblock;

public class Flow {
	private int fst;
	private int snd;
	
	public Flow(int f, int s){
		this.fst = f;
		this.snd = s;
	}
	
	public void setFirst(int f){
		this.fst = f;
	}
	public int getFirst(){
		return this.fst;
	}
	
	public void setSecond(int s){
		this.snd = s;
	}
	public int getSecond(){
		return this.snd;
	}
}
