package core.analysis.staticanalysis.verybusy_exp;

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

public class VeyBsyExpCalculator extends Calculator implements IJob {
	private String name = this.getClass().getSimpleName();
	
	public void initAnalysisBlocks(TestData data) {
		TestFile file = data.getCurrentTestFile();
		
		this.analysisBlocks = new ArrayList<AnalysisBlock>();
		HashMap<String, FunctionInfo> funsInfo = (HashMap<String, FunctionInfo>) file.get(JobConst.FUNCTIONSINFO);
		FunctionInfo functionInfo0 = funsInfo.get("f0");
		
		// 通过getNodes获取一个cfg中所有的节点
		Collection<IBasicBlock> bbRes = functionInfo0.getJavaControlFlowGraph()
				.getNodes();
		// 将每一个node放到一个AnalysisBlock里面
		Iterator<IBasicBlock> itr = bbRes.iterator();
		int i = 0;
		while (itr.hasNext()) {
			IBasicBlock bb = itr.next();
			AnalysisBlock alsBlock = new VeyBsyExpBlock(bb, i++);
			this.analysisBlocks.add(alsBlock);
			// AnalysisBlock alsBlock = new AnalysisBlock(bb, i++);
			// this.analysisBlocks.add(alsBlock);
		}
		// 计算每一个AnalysisBlock的_incoming 和 _outgoing
		for (i = 0; i < this.analysisBlocks.size(); i++) {
			IBasicBlock[] incoming = this.analysisBlocks.get(i).getBasicBlock()
					.getIncomingNodes();
			IBasicBlock[] outgoing = this.analysisBlocks.get(i).getBasicBlock()
					.getOutgoingNodes();
			// 加_incoming
			for (int m = 0; m < incoming.length; m++) {
				for (int n = 0; n < this.analysisBlocks.size(); n++) {
					if (this.analysisBlocks.get(n).getBasicBlock()
							.equals(incoming[m])) {
						this.analysisBlocks.get(i).addIncoming(
								this.analysisBlocks.get(n));
					}
				}
			}
			// 加_outgoing
			for (int m = 0; m < outgoing.length; m++) {
				for (int n = 0; n < this.analysisBlocks.size(); n++) {
					if (this.analysisBlocks.get(n).getBasicBlock()
							.equals(outgoing[m])) {
						this.analysisBlocks.get(i).addOutgoing(
								this.analysisBlocks.get(n));
					}
				}
			}
		}

	}
	
	public void calculateSolution() {
		while (this.workList.size() != 0) {
			int first = this.workList.get(0).getFirst();
			int second = this.workList.get(0).getSecond();
			transfer(first);
			
			if (!this.isVisited(second)) {
				this.setExit(second, this.getEntry(first));
				for (int i = 0; i < this.flow.size(); i++) {
					if (this.flow.get(i).getFirst() == second) {
						this.workList.add(this.flow.get(i));
					}
				}
			} // 如果second的入口中含有first出口中没有的元素就需要求交集
			else if (isContained(this.getEntry(first), this.getExit(second))) {
				// 求交集
				ArrayList<Object> exit = computeIntersection(this.getEntry(first), this.getExit(second));
				this.setExit(second, exit);
				for (int i = 0; i < this.flow.size(); i++) {
					if (this.flow.get(i).getFirst() == second) {
						this.workList.add(this.flow.get(i));
					}
				}
			}
			this.workList.remove(0);
		}
	}
	
	protected void initWorklist() {
		this.flow = new ArrayList<Flow>();
		this.workList = new ArrayList<Flow>();
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			ArrayList<AnalysisBlock> incoming = this.analysisBlocks.get(this.analysisBlocks.size()-i-1).getIncoming();
			for (int j = 0; j < incoming.size(); j++) {
				Flow f = new Flow( this.analysisBlocks.get(this.analysisBlocks.size()-i-1).getLable(), incoming.get(j).getLable());
				this.flow.add(f);
				this.workList.add(f);
			}
		}
	}
	
	protected void transfer(int lab) {
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			if (this.analysisBlocks.get(i).getLable() == lab) {
				((VeyBsyExpBlock) this.analysisBlocks.get(i)).tansfer();
				break;
			}
		}
	}
	
	public boolean run(TestData data) {
		this.initAnalysisBlocks(data);
		this.initWorklist();
		this.calculateSolution();
		this.displaySolution(data);
		return true;
	}
}
