package core.common.model.functionblock;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import core.common.cfg.interfaces.*;
import core.common.cfg.model.AbstractBasicBlock;
import core.common.model.functionblock.AnalysisBlock;

public class AnalysisBlockImpl extends AnalysisBlock{
	
	public AnalysisBlockImpl(IBasicBlock bb, int lable) {
		super(bb, lable);
	}

	// 这个地方还要好好考虑一下，现在的想法是不论是向前分析还是向后分析，先将传入的表达式放入_temp，然后对_temp进行kill和gen的操作
	public void transfer(){
	}

	public void kill() {
		if (_bb instanceof IStartNode) {
		} else if (_bb instanceof IBranchNode) {
		} else if (_bb instanceof IPlainNode) {
			this.killIPlainNode();
		} else if (_bb instanceof IExitNode) {
		} else if (_bb instanceof IDecisionNode) {
		} else if (_bb instanceof IJumpNode) {
		} else if (_bb instanceof IConnectorNode) {
		} else {
		}
	}

	protected void killIPlainNode() {
		// 判断是否是赋值语句，如果是就kill掉_entry中含有“=”左边变量的表达式
		ASTNode node = (ASTNode) ((AbstractBasicBlock) this._bb).getData();

		if (node instanceof VariableDeclarationStatement) {
			VariableDeclarationStatement statement = (VariableDeclarationStatement)node;
			List fragments = statement.fragments();
			
			for(int i=0;i<fragments.size();i++){
				VariableDeclarationFragment fragment = (VariableDeclarationFragment)fragments.get(i);
				Expression exp = fragment.getName();
				
				for (int j = 0; j < this._temp.size(); j++) {
					Expression oprand1 = ((InfixExpression) _temp.get(j)).getLeftOperand();
					Expression oprand2 = ((InfixExpression) _temp.get(j)).getRightOperand();
					if (exp.equals(oprand1)|| exp.equals(oprand2)) {
						this._temp.remove(j--);
					}
				}
			}
		} else if (node instanceof ExpressionStatement){
			Expression exp = ((ExpressionStatement)node).getExpression();
			if(exp instanceof Assignment){
				Expression leftOperand = ((Assignment)exp).getLeftHandSide();
				for (int j = 0; j < this._temp.size(); j++) {
					Expression oprand1 = ((InfixExpression) _temp.get(j)).getLeftOperand();
					Expression oprand2 = ((InfixExpression) _temp.get(j)).getRightOperand();
					
					if (leftOperand.toString().equals(oprand1.toString())|| leftOperand.toString().equals(oprand2.toString())) {
						this._temp.remove(j--);
					}
				}
			}
		}
	}

	public void gen() {
		if (_bb instanceof IStartNode) {
		} else if (_bb instanceof IBranchNode) {
		} else if (_bb instanceof IPlainNode) {
			this.genIPlainNode();
		} else if (_bb instanceof IExitNode) {
		} else if (_bb instanceof IDecisionNode) {
			this.genIDecisionNode();
		} else if (_bb instanceof IJumpNode) {
		} else if (_bb instanceof IConnectorNode) {
		} else {
		}
	}

	// 加入时先判断待加入表达式是否存在
	// 默认现在的Expression只是2元操作，e.g. a + b
	// 可用递归来实现复杂表达式存在与否的判断
	protected boolean isExist(InfixExpression exp) {
		boolean flag = false;
		Expression leftOperand =  exp.getLeftOperand();
		Expression rightOperand =  exp.getRightOperand();
		Operator operator =  exp.getOperator();

		for (int i = 0; i < this._temp.size(); i++) {
			Expression lOperand = ((InfixExpression) _temp.get(i)).getLeftOperand();
			Expression rOperand = ((InfixExpression) _temp.get(i)).getRightOperand();
			Operator op = ((InfixExpression) _temp.get(i)).getOperator();
			if (leftOperand.equals(lOperand) 
					&& rightOperand.equals(rOperand) 
					&& operator.equals(op)) {
				flag = true;
			}
		}
		return flag;
	}
	
	protected boolean isExist(Expression exp){
		for (int i = 0; i < this._temp.size(); i++) {
			Expression operand = (Expression) _temp.get(i);
			if(exp.toString().equals(operand.toString())){
				return true;
			}
		}
		return false;
	}

	protected void genIPlainNode() {
		// 先判断是否是初始化或者赋值语句，再判断“=”右边是不是表达式，如果是就加入_temp
		ASTNode node = (ASTNode) ((AbstractBasicBlock) this._bb).getData();
		
		if (node instanceof VariableDeclarationStatement) {
			VariableDeclarationStatement statement = (VariableDeclarationStatement)node;
			List fragments = statement.fragments();
			
			for(int i=0;i<fragments.size();i++){
				VariableDeclarationFragment fragment = (VariableDeclarationFragment)fragments.get(i);
				Expression exp = fragment.getInitializer();
				if(exp instanceof InfixExpression && !isExist((InfixExpression)exp)){
					this._temp.add((InfixExpression)exp);
				}
			}
		} else if (node instanceof ExpressionStatement){
			Expression exp = ((ExpressionStatement)node).getExpression();
			if(exp instanceof Assignment){
				Expression rightOperand = ((Assignment)exp).getRightHandSide();
				if(rightOperand instanceof InfixExpression && !isExist((InfixExpression)rightOperand)){
					this._temp.add((InfixExpression)rightOperand);
				}
			}
		}
	}

	protected void genIDecisionNode() {
		// 判断 >,>=,<,<=,两边是否有表达式，如果有就加入_exit
		ASTNode node = (ASTNode) ((AbstractBasicBlock) this._bb).getData();
		
		Expression exp = null;
		if (node instanceof WhileStatement) {
			exp = ((WhileStatement) node).getExpression();
		} else if( node instanceof IfStatement){
			exp = ((IfStatement) node).getExpression();
		}		
		
		if(exp instanceof InfixExpression && !isExist((InfixExpression)exp)){
			Expression oprand1 = ((InfixExpression) exp).getLeftOperand();
			Expression oprand2 = ((InfixExpression) exp).getRightOperand();
			if (oprand1 instanceof InfixExpression){
				if (!isExist((InfixExpression)oprand1)){
					this._temp.add(oprand1);
				}
			}
				
			if (oprand2 instanceof InfixExpression){
				if (!isExist((InfixExpression)oprand2)){
					this._temp.add(oprand2);
				}
			}
		}
	}

	// 默认不会返回诸如 a = b + c 这样的表达式
	protected void genIExitNode() {
		// 判断该节点是否是return，如果是return，再判断它的返回值是否是一个表达式，如果是就加入_exit

	}

}
