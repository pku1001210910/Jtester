package core.analysis.staticanalysis.reaching_def;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jdt.core.dom.CompilationUnit;

import core.common.cfg.interfaces.*;
import core.common.model.functionblock.AnalysisBlock;
import core.common.model.functionblock.Calculator;
import core.common.model.functionblock.Flow;
import core.common.model.functionblock.FunctionInfo;
import core.common.model.jobflow.IJob;
import core.common.model.jobflow.JobConst;
import core.common.model.test.TestData;
import core.common.model.test.TestFile;

public class RchDefCalculator extends Calculator implements IJob {

	public void initAnalysisBlocks(TestData data) {
		TestFile file = data.getCurrentTestFile();
		
		analysisBlocks = new ArrayList<AnalysisBlock>();
		HashMap<String, FunctionInfo> funsInfo = (HashMap<String, FunctionInfo>) file.get(JobConst.FUNCTIONSINFO);
		FunctionInfo functionInfo0 = funsInfo.get("f0");
		// 通过getNodes获取一个cfg中所有的节点
		Collection<IBasicBlock> bbRes = functionInfo0.getJavaControlFlowGraph().getNodes();
		// 将每一个node放到一个AnalysisBlock里面
		Iterator<IBasicBlock> itr = bbRes.iterator();
		int i = 0;
		while (itr.hasNext()) {
			IBasicBlock bb = itr.next();
			RchDefBlock alsBlock = new RchDefBlock(bb, i++);
			alsBlock.setParser((CompilationUnit)data.getCurrentTestFile().get(JobConst.AST));
			this.analysisBlocks.add(alsBlock);
			// AnalysisBlock alsBlock = new AnalysisBlock(bb, i++);
			// this.analysisBlocks.add(alsBlock);
		}
		// 计算每一个AnalysisBlock的_incoming 和 _outgoing
		for (i = 0; i < this.analysisBlocks.size(); i++) {
			IBasicBlock[] incoming = this.analysisBlocks.get(i).getBasicBlock().getIncomingNodes();
			IBasicBlock[] outgoing = this.analysisBlocks.get(i).getBasicBlock().getOutgoingNodes();
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

	public void calculateSolution() {
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
			}
			// 如果first出口中含有second的入口中没有的元素就需要求交集
			else if (isContained(this.getEntry(second), this.getExit(first))) {
				//求并集
				ArrayList<Object> entry = computeUnion(this.getExit(first), this.getEntry(second));
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

	protected ArrayList<Object> computeUnion(ArrayList<Object> exit, ArrayList<Object> entry) {
		ArrayList<Object> union = new ArrayList<Object>();
		for (int k = 0; k < entry.size(); k++){
			union.add(entry.get(k));
		}
		for (int i = 0; i < exit.size(); i++) {
			boolean flag = false;
			for (int j = 0; j < entry.size(); j++) {
				if ((((DefLocPair) exit.get(i)).getVarName().compareTo(((DefLocPair) entry.get(j)).getVarName()) == 0)
						&& (((DefLocPair) exit.get(i)).getStartLine() == ((DefLocPair) entry.get(j)).getStartLine())) {
					flag = true;
					break;
				}
			}
			if (!flag){
				union.add(exit.get(i));
			}
		}
		return union;
	}

	protected void transfer(int lab) {
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			if (this.analysisBlocks.get(i).getLable() == lab) {
				((RchDefBlock) this.analysisBlocks.get(i)).tansfer();
				break;
			}
		}
	}

	public ArrayList<Object> getEntry(int lab) {
		ArrayList<Object> entry = null;
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			if (this.analysisBlocks.get(i).getLable() == lab) {
				entry = this.analysisBlocks.get(i).getEntry();
				break;
			}
		}
		return entry;
	}
	
	protected boolean isContained(ArrayList<Object> fstExit, ArrayList<Object> sndEntry) {
		boolean flag = false;
		for (int i = 0; i < sndEntry.size(); i++) {
			boolean flag2 = false;
			for (int j = 0; j < fstExit.size(); j++) {
				if ((((DefLocPair) sndEntry.get(i)).getVarName().compareTo(((DefLocPair) fstExit.get(j)).getVarName()) == 0)
						&& (((DefLocPair) sndEntry.get(i)).getStartLine() == ((DefLocPair) fstExit.get(j)).getStartLine())) {
					flag2 = true;
					break;
				}
			}
			if (!flag2) {
				flag = true;
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
		return true;
	}
}
