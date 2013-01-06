package core.analysis.staticanalysis.const_propagation;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import core.common.cfg.interfaces.*;
import core.common.cfg.model.AbstractBasicBlock;
import core.common.model.functionblock.AnalysisBlockImpl;

public class ConProBlock extends AnalysisBlockImpl{

	public ConProBlock(IBasicBlock bb, int lable) {
		super(bb, lable);
	}

	// 这个地方还要好好考虑一下，现在的想法是不论是向前分析还是向后分析，先将传入的表达式放入_temp，然后对_temp进行kill和gen的操作
	public void transfer(){
		//清空_temp
		this._temp.clear();
		//将_entry的元素加到_temp
		if (this._entry.size() != 0){
			for (int i = 0; i < this._entry.size(); i++){
				try {
					this._temp.add(((Var2Value)this._entry.get(i)).clone());
				} catch (CloneNotSupportedException e) {
					System.err.println("clone not supported !\n");
				}
			}
		}
		
		this.update();

		//将_temp的元素赋给_exit
		if (this._temp.size() != 0){
			this._exit.clear();
			for (int i = 0; i < this._temp.size(); i++){
				this._exit.add((Var2Value)this._temp.get(i));
			}
		}
	}

	protected void update() {
		if (_bb instanceof IPlainNode) {
			this.updateIPlainNode();
		} 
	}

	protected void updateIPlainNode() {
		//暂时默认为带分析的变量都是int型
		//先判断是否是赋值语句或者是声明语句，如果是，就更新_temp中变量的状态，声明语句就将变量加入_temp,赋值语句就更改_temp中变量的状态
		ASTNode node = (ASTNode) ((AbstractBasicBlock) this._bb).getData();
		
		if (node instanceof VariableDeclarationStatement) {
			VariableDeclarationStatement statement = (VariableDeclarationStatement) node;
			List fragments = statement.fragments();
			
			for (int i = 0; i < fragments.size(); i++) {
				VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(i);
				String varName = fragment.getName().toString();
				Expression exp = fragment.getInitializer();
				if(exp instanceof NumberLiteral){
					Var2Value v2v = new Var2Value(varName, exp.toString(), "INT", true);
					this._temp.add(v2v);
					continue;
				}
				
				if(exp instanceof InfixExpression){
					InfixExpression infix = (InfixExpression)exp;
					Expression oprand1 = infix.getLeftOperand();
					Expression oprand2 = infix.getRightOperand();
					
					int value1=0;
					if(oprand1 instanceof NumberLiteral){
						value1 = Integer.parseInt(oprand1.toString());
					}else{
						for (int j = 0; j < this._temp.size(); j++){
							if (((Var2Value)this._temp.get(j)).getVarName().compareTo(oprand1.toString()) == 0){
								value1 = Integer.parseInt(((Var2Value)this._temp.get(j)).getValue());
							}
						}
					}
					
					int value2=0;
					if(oprand2 instanceof NumberLiteral){
						value2 = Integer.parseInt(oprand2.toString());
					}else{
						for (int j = 0; j < this._temp.size(); j++){
							if (((Var2Value)this._temp.get(j)).getVarName().compareTo(oprand1.toString()) == 0){
								value2 = Integer.parseInt(((Var2Value)this._temp.get(j)).getValue());
							}
						}
					}
					
					int result=0;
					Operator op = infix.getOperator();
					switch(op.toString()){
					case "+":
						result = value1 + value2;
						break;
					case "*":
						result = value1 * value2;
						break;
					case "-":
						result = value1 - value2;
						break;
					case "/":
						result = value1 / value2;
						break;
					default:
						System.err.println("undefined operator" + op);
						break;
					}
					
					Var2Value v2v = new Var2Value(varName.toString(), result+"", "INT", true);
					this._temp.add(v2v);
					continue;
				}
				
				int value;
				if( exp != null){
					for (int j = 0; j < this._temp.size(); j++){
						if (((Var2Value)this._temp.get(j)).getVarName().compareTo(exp.toString()) == 0){
							value = Integer.parseInt(((Var2Value)this._temp.get(j)).getValue());
							Var2Value v2v = new Var2Value(varName.toString(), value+"", "INT", true);
							this._temp.add(v2v);
							break;
						}
					}
				}
			}
		} else if (node instanceof ExpressionStatement) {
			Expression exp = ((ExpressionStatement) node).getExpression();
			if (exp instanceof Assignment) {
				Assignment assignment = (Assignment)exp;
				Expression leftSide = assignment.getLeftHandSide();
				Expression rightSide = assignment.getRightHandSide();
				if(rightSide instanceof NumberLiteral){
					for (int i = 0; i < this._temp.size(); i++){
						Var2Value v2v = (Var2Value)this._temp.get(i);
						if(v2v.getVarName().equals(leftSide.toString()) && !v2v.getValue().equals(rightSide.toString())){
							v2v.setValue(rightSide.toString());
							v2v.setIsConstant(false);
						}
					}
				} else if(rightSide instanceof InfixExpression){
					InfixExpression infix = (InfixExpression)rightSide;
					Expression oprand1 = infix.getLeftOperand();
					Expression oprand2 = infix.getRightOperand();
					
					int value1=0;
					if(oprand1 instanceof NumberLiteral){
						value1 = Integer.parseInt(oprand1.toString());
					}else{
						for (int j = 0; j < this._temp.size(); j++){
							if (((Var2Value)this._temp.get(j)).getVarName().compareTo(oprand1.toString()) == 0){
								value1 = Integer.parseInt(((Var2Value)this._temp.get(j)).getValue());
							}
						}
					}
					
					int value2=0;
					if(oprand2 instanceof NumberLiteral){
						value2 = Integer.parseInt(oprand2.toString());
					}else{
						for (int j = 0; j < this._temp.size(); j++){
							if (((Var2Value)this._temp.get(j)).getVarName().compareTo(oprand1.toString()) == 0){
								value2 = Integer.parseInt(((Var2Value)this._temp.get(j)).getValue());
							}
						}
					}

					int result=0;
					Operator op = infix.getOperator();
					
					
					switch(op.toString()){
					case "+":
						result = value1 + value2;
						break;
					case "*":
						result = value1 * value2;
						break;
					case "-":
						result = value1 - value2;
						break;
					case "/":
						result = value1 / value2;
						break;
					default:
						System.err.println("undefined operator" + op);
						break;
					}
					
					for (int i = 0; i < this._temp.size(); i++){
						Var2Value v2v = (Var2Value)this._temp.get(i);
						if(v2v.getVarName().equals(leftSide.toString()) && !v2v.getValue().equals(result+"")){
							v2v.setValue(result+"");
							v2v.setIsConstant(false);
						}
					}
				}else if( rightSide != null){
					int value = 0;
					for (int j = 0; j < this._temp.size(); j++){
						if (((Var2Value)this._temp.get(j)).getVarName().compareTo(rightSide.toString()) == 0){
							value = Integer.parseInt(((Var2Value)this._temp.get(j)).getValue());
							break;
						}
					}
					
					for (int i = 0; i < this._temp.size(); i++){
						Var2Value v2v = (Var2Value)this._temp.get(i);
						if(v2v.getVarName().equals(leftSide.toString()) && !v2v.getValue().equals(value+"")){
							v2v.setValue(value+"");
							v2v.setIsConstant(false);
						}
					}
				}
			}
		}
	}

}
