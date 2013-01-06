package core.common.model.functionblock;
import java.util.ArrayList;

import core.common.cfg.interfaces.IBasicBlock;

public class AnalysisBlock {
	protected IBasicBlock _bb;
	protected int _lable;
	protected ArrayList<Object> _entry;
	protected ArrayList<Object> _exit;
	protected ArrayList<Object> _temp;
	protected ArrayList<AnalysisBlock> _incoming;
	protected ArrayList<AnalysisBlock> _outgoing;
	protected boolean _visited;
	protected ContextInfo _contextInfo;
	
	public AnalysisBlock(IBasicBlock bb, int lable){
		this._bb = bb;
		this._lable = lable;
		this._entry = new ArrayList<Object>();
		this._exit  = new ArrayList<Object>();
		this._temp  = new ArrayList<Object>();
		this._incoming = new  ArrayList<AnalysisBlock>();
		this._outgoing = new  ArrayList<AnalysisBlock>();
		this._visited = false;
		
	}
	
	public ArrayList<Object> getEntry(){
		return this._entry;
	}
	
	public ArrayList<Object> getExit(){
		return this._exit;
	}
	
	public void setBasicBlock(IBasicBlock n) {
		this._bb = n;
	}

	public IBasicBlock getBasicBlock() {
		return this._bb;
	}
	
	public void addIncoming(AnalysisBlock alsBlock){
		this._incoming.add(alsBlock);		
	}

	public void addOutgoing(AnalysisBlock alsBlock) {
		this._outgoing.add(alsBlock);
	}
	
	public ArrayList<AnalysisBlock> getOutgoing(){
		return this._outgoing;
	}
	
	public ArrayList<AnalysisBlock> getIncoming(){
		return this._incoming;
	}
	
	public void setVisited(boolean b) {
		this._visited = b;
	}

	public boolean isVisited() {
		return this._visited;
	}
	
	public int getLable(){
		return this._lable;
	}

	public void setEntry(ArrayList<Object> entry) {
		this._entry.clear();
		for (int i = 0; i < entry.size(); i++){
			this._entry.add(entry.get(i));
		}
	}
	public void setExit(ArrayList<Object> exit){
		this._exit.clear();
		for (int i = 0; i < exit.size(); i++){
			this._exit.add(exit.get(i));
		}
	}
}
