package core.analysis.staticanalysis.const_propagation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import core.common.cfg.interfaces.*;
import core.common.model.functionblock.AnalysisBlock;
import core.common.model.functionblock.Calculator;
import core.common.model.functionblock.Flow;
import core.common.model.functionblock.FunctionInfo;
import core.common.model.jobflow.IJob;
import core.common.model.jobflow.JobConst;
import core.common.model.test.TestData;
import core.common.model.test.TestFile;

public class ConProCalculator extends Calculator implements IJob {

	public void initAnalysisBlocks(TestData data) {
		TestFile file = data.getCurrentTestFile();
		
		this.analysisBlocks = new ArrayList<AnalysisBlock>();
		HashMap<String, FunctionInfo> funsInfo = (HashMap<String, FunctionInfo>) file.get(JobConst.FUNCTIONSINFO);
		FunctionInfo functionInfo0 = funsInfo.get("f0");
		// 通过getNodes获取一个cfg中所有的节点
		Collection<IBasicBlock> bbRes = functionInfo0.getJavaControlFlowGraph().getNodes();
		// 将每一个node放到一个AnalysisBlock里面
		Iterator<IBasicBlock> itr = bbRes.iterator();
		int i = 0;
		while (itr.hasNext()) {
			IBasicBlock bb = itr.next();
			AnalysisBlock alsBlock = new ConProBlock(bb, i++);
			this.analysisBlocks.add(alsBlock);
		}
		// 计算每一个AnalysisBlock的_incoming 和 _outgoing
		for (i = 0; i < this.analysisBlocks.size(); i++) {
			IBasicBlock[] incoming = this.analysisBlocks.get(i).getBasicBlock().getIncomingNodes();
			IBasicBlock[] outgoing = this.analysisBlocks.get(i).getBasicBlock()	.getOutgoingNodes();
			// 加_incoming
			for (int m = 0; m < incoming.length; m++) {
				for (int n = 0; n < this.analysisBlocks.size(); n++) {
					if (this.analysisBlocks.get(n).getBasicBlock().equals(incoming[m])) {
						this.analysisBlocks.get(i).addIncoming(	this.analysisBlocks.get(n));
					}
				} 
			}
			// 加_outgoing
			for (int m = 0; m < outgoing.length; m++) {
				for (int n = 0; n < this.analysisBlocks.size(); n++) {
					if (this.analysisBlocks.get(n).getBasicBlock().equals(outgoing[m])) {
						this.analysisBlocks.get(i).addOutgoing(	this.analysisBlocks.get(n));
					}
				}
			}
		}

	}

	protected void initWorklist() {

		this.flow = new ArrayList<Flow>();
		this.workList = new ArrayList<Flow>();
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			ArrayList<AnalysisBlock> outgoing = this.analysisBlocks.get(i).getOutgoing();
			for (int j = 0; j < outgoing.size(); j++) {
				Flow f = new Flow(this.analysisBlocks.get(i).getLable(), outgoing.get(j).getLable());
				this.flow.add(f);
				this.workList.add(f);
			}
		}
	}

	public void calculateSolution(){
		while (this.workList.size() != 0) {
			int first = this.workList.get(0).getFirst();
			int second = this.workList.get(0).getSecond();
			this.transfer(first);
			
			if (!this.isVisited(second)) {
				this.setEntry(second, this.getExit(first));
				for (int i = 0; i < this.flow.size(); i++) {
					if (this.flow.get(i).getFirst() == second) {
						this.workList.add(this.flow.get(i));
					}
				}
			} // 如果second的入口中含有first出口中没有的元素就需要求交集
			else if (!isConstant(this.getExit(first), this.getEntry(second))) {
				ArrayList<Object> entry = updateState(this.getExit(first), this.getEntry(second));
				this.setEntry(second, entry);
				for (int i = 0; i < this.flow.size(); i++) {
					if (this.flow.get(i).getFirst() == second) {
						this.workList.add(this.flow.get(i));
					}
				}
			}
			
			this.workList.remove(0);
		}
	}

	protected ArrayList<Object> updateState(ArrayList<Object> exit, ArrayList<Object> entry) {
		ArrayList<Object> state = new ArrayList<Object>();
		for (int i = 0; i < entry.size(); i++) {
			for (int j = 0; j < exit.size(); j++) {
				if (((Var2Value) entry.get(i)).getVarName().compareTo(((Var2Value) exit.get(j)).getVarName()) == 0) {
					if (!((Var2Value) exit.get(j)).getIsConstant()){
						((Var2Value)entry.get(i)).setIsConstant(false);
						((Var2Value)entry.get(i)).setValue("-1000");
					}
					else if (((Var2Value) entry.get(i)).getValue().compareTo(((Var2Value) exit.get(j)).getValue()) != 0){
						((Var2Value)entry.get(i)).setIsConstant(false);
						//((Var2Value)entry.get(i)).setValue(((Var2Value)exit.get(j)).getValue());
						((Var2Value)entry.get(i)).setValue("-1000");
					}
					break;
				}
			}
			try {
				state.add(((Var2Value)entry.get(i)).clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		
		boolean flag = false;
		for (int i = 0; i < exit.size(); i++){
			flag = false;
			for (int j = 0; j < state.size(); j++){
				if (((Var2Value) exit.get(i)).getVarName().compareTo(((Var2Value) state.get(j)).getVarName()) == 0) {
					flag = true;
					break;
				}
			}
			if (!flag){
				try {
					state.add(((Var2Value)exit.get(i)).clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}
		return state;
	}

	protected boolean isConstant(ArrayList<Object> exit, ArrayList<Object> entry) {
		boolean flag = true;
		for (int i = 0; i < exit.size(); i++) {
			boolean flag2 = true;
			for (int j = 0; j < entry.size(); j++) {
				if (((Var2Value) exit.get(i)).getVarName().compareTo(((Var2Value) entry.get(j)).getVarName()) == 0) {
					if (((Var2Value) exit.get(i)).getValue().compareTo(((Var2Value) entry.get(j)).getValue()) != 0){
						flag2 = false;
						break;
					}
				}
			}
			if (!flag2) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	protected void transfer(int lab){
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			if (this.analysisBlocks.get(i).getLable() == lab) {
				((ConProBlock) this.analysisBlocks.get(i)).transfer();
				break;
			}
		}
	}

	public ArrayList<Object> getEntry(int lab) {
		ArrayList<Object> entry = null;
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			if (this.analysisBlocks.get(i).getLable() == lab) {
				entry = (ArrayList<Object>) this.analysisBlocks.get(i).getEntry().clone();
				break;
			}
		}
		return entry;
	}

	public ArrayList<Object> getExit(int lab) {
		ArrayList<Object> exit = null;
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			if (this.analysisBlocks.get(i).getLable() == lab) {
				exit = (ArrayList<Object>) this.analysisBlocks.get(i).getExit().clone();
				break;
			}
		}
		return exit;
	}

	public boolean isVisited(int lab) {
		boolean flag = true;
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			if (this.analysisBlocks.get(i).getLable() == lab) {
				flag = this.analysisBlocks.get(i).isVisited();
				this.analysisBlocks.get(i).setVisited(true);
				break;
			}
		}
		return flag;
	}
	
	
	@Override
	public boolean run(TestData data) {
		this.initAnalysisBlocks(data);
		this.initWorklist();
		this.calculateSolution();
		this.displaySolution(data);
		return false;
	}

}
