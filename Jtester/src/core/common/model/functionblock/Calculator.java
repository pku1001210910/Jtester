package core.common.model.functionblock;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.SimpleName;

import core.analysis.api.RuleSet;
import core.analysis.staticanalysis.const_propagation.Var2Value;
import core.analysis.staticanalysis.reaching_def.DefLocPair;
import core.common.cfg.interfaces.*;
import core.common.cfg.model.AbstractBasicBlock;
import core.common.model.functionblock.AnalysisBlock;
import core.common.model.functionblock.Flow;
import core.common.model.jobflow.IJob;
import core.common.model.test.TestData;
import core.common.model.test.TestResult;
import core.common.model.test.TestResultItem;

public abstract class Calculator implements IJob {
	protected String name = this.getClass().getSimpleName();
	
	protected ArrayList<AnalysisBlock> analysisBlocks;
	protected ArrayList<Flow> flow;
	protected ArrayList<Flow> workList;
	
	protected void initWorklist() {
	}


	protected void transfer(int first) {
		
	}

	protected void setExit(int lab, ArrayList<Object> exit) {
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			if (this.analysisBlocks.get(i).getLable() == lab) {
				this.analysisBlocks.get(i).setExit(exit);
				break;
			}
		}		
	}

	protected ArrayList<Object> computeIntersection(ArrayList<Object> exit, ArrayList<Object> entry) {
		ArrayList<Object> insction = new ArrayList<Object>();
		for (int i = 0; i < entry.size(); i++) {
			for (int j = 0; j < exit.size(); j++) {
				if (((Expression) exit.get(j)).toString().equals( ((Expression) entry.get(i)).toString())) {
					insction.add(entry.get(i));
					break;
				}
			}
		}
		
		return insction;
	}
	
	protected ArrayList<Object> computeUnion(ArrayList<Object> exit, ArrayList<Object> entry) {
		ArrayList<Object> union = new ArrayList<Object>();
		for (int k = 0; k < entry.size(); k++){
			union.add(entry.get(k));
		}
		for (int i = 0; i < exit.size(); i++) {
			boolean flag = false;
			for (int j = 0; j < entry.size(); j++) {
				if (((Expression) exit.get(i)).toString().equals( ((Expression) entry.get(j)).toString())) {
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

	public ArrayList<Object> getExit(int lab) {
		ArrayList<Object> exit = null;
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			if (this.analysisBlocks.get(i).getLable() == lab) {
				exit = this.analysisBlocks.get(i).getExit();
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

	protected boolean isContained(ArrayList<Object> fstExit,
			ArrayList<Object> sndEntry) {
		boolean flag = false;
		for (int i = 0; i < sndEntry.size(); i++) {
			boolean flag2 = false;
			for (int j = 0; j < fstExit.size(); j++) {
				if (((Expression)sndEntry.get(i)).equals(((Expression)fstExit.get(j)))) {
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
	
	protected void setEntry(int lab, ArrayList<Object> entry) {
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			if (this.analysisBlocks.get(i).getLable() == lab) {
				this.analysisBlocks.get(i).setEntry(entry);
				break;
			}
		}
	}

	protected void displaySolution(TestData data) {
		TestResult result = data.getTestResult();
		for (int i = 0; i < this.analysisBlocks.size(); i++) {
			String lineNumber="";
			String src="";
			String entry="";
			String exit="";
			
			if (((this.analysisBlocks.get(i).getBasicBlock() instanceof IPlainNode)
					|| (this.analysisBlocks.get(i).getBasicBlock() instanceof IDecisionNode)
					|| (this.analysisBlocks.get(i).getBasicBlock() instanceof IExitNode)) 
					&& (!(this.analysisBlocks.get(i).getBasicBlock() instanceof IBranchNode))) {
				
				lineNumber = this.analysisBlocks.get(i).getLable() + "";
				src = ((AbstractBasicBlock) this.analysisBlocks	.get(i).getBasicBlock()).toStringData();
				
				if (this.analysisBlocks.get(i).getEntry().size() == 0) {
					entry = "null";
				} else {
					for (int j = 0; j < this.analysisBlocks.get(i).getEntry().size(); j++) {
						Object exp =  this.analysisBlocks.get(i).getEntry().get(j);
						if( exp instanceof InfixExpression){
							entry += ((InfixExpression) exp)+ "; ";			
						} else if (exp instanceof SimpleName){
							entry += ((SimpleName) exp)+ "; ";		
						} else if (exp instanceof DefLocPair){
							entry += ((DefLocPair) exp).displayPair();
						} else if (exp instanceof Var2Value){
							entry += ((Var2Value) exp).display() + "; ";
						}
					}
				}
				
				if (this.analysisBlocks.get(i).getExit().size() == 0) {
					exit = "null";
				} else {
					for (int j = 0; j < this.analysisBlocks.get(i).getExit().size(); j++) {
						Object exp =  this.analysisBlocks.get(i).getExit().get(j);
						if( exp instanceof InfixExpression){
							exit += ((InfixExpression) exp)+ "; ";			
						} else if (exp instanceof SimpleName){
							exit += ((SimpleName) exp)+ "; ";		
						} else if (exp instanceof DefLocPair){
							exit += ((DefLocPair) exp).displayPair();
						} else if (exp instanceof Var2Value){
							exit += ((Var2Value) exp).display() + "; ";
						}
					}
				}
				
				TestResultItem item = new TestResultItem(data.getCurrentTestFile().getPath(), getName(), RuleSet.RESULT);
				item.add(lineNumber);
				item.add(src);
				item.add(entry);
				item.add(exit);
				result.add(data.getCurrentTestFile().getPath(), item);
				//System.out.println(lineNumber + ": "  + src + ": " + entry + " :" + exit);
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}
}
