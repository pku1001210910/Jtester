package core.analysis.staticanalysis.reaching_def;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import core.common.cfg.interfaces.*;
import core.common.cfg.model.AbstractBasicBlock;
import core.common.model.functionblock.AnalysisBlockImpl;

public class RchDefBlock extends AnalysisBlockImpl {
	private CompilationUnit parser;

	public RchDefBlock(IBasicBlock bb, int lable) {
		super(bb, lable);
	}

	public void setParser(CompilationUnit parser) {
		this.parser = parser;
	}

	// 这个地方还要好好考虑一下，现在的想法是不论是向前分析还是向后分析，先将传入的表达式放入_temp，然后对_temp进行kill和gen的操作
	public void tansfer() {
		// 清空_temp
		this._temp.clear();
		// 将_entry的元素加到_temp
		if (this._entry.size() != 0) {
			for (int i = 0; i < this._entry.size(); i++) {
				this._temp.add(this._entry.get(i));
			}
		}

		this.kill();
		this.gen();

		// 将_temp的元素赋给_exit
		if (this._temp.size() != 0) {
			this._exit.clear();
			for (int i = 0; i < this._temp.size(); i++) {
				this._exit.add(this._temp.get(i));
			}
		}

	}

	protected void killIPlainNode() {
		// 判断是否是赋值语句，如果是就kill掉_entry中含有“=”左边变量的表达式
		ASTNode node = (ASTNode) ((AbstractBasicBlock) this._bb).getData();
		if (node instanceof ExpressionStatement) {
			Expression exp = ((ExpressionStatement) node).getExpression();
			if (exp instanceof Assignment) {
				Expression leftOperand = ((Assignment) exp).getLeftHandSide();
				for (int j = 0; j < this._temp.size(); j++) {
					if (leftOperand.toString().equals(((DefLocPair) _temp.get(j)).getVarName())) {
						this._temp.remove(j--);
					}
				}
			}
		}
	}

	public void gen() {
		if (_bb instanceof IPlainNode) {
			this.genIPlainNode();
		}
	}

	protected void genIPlainNode() {
		// 先判断是否是初始化或者赋值语句，再将“=”左侧的变量和这句的行号组成一个DefLocPair加入_temp
		ASTNode node = (ASTNode) ((AbstractBasicBlock) this._bb).getData();
		
		if (node instanceof ExpressionStatement) {
			int startline = parser.getLineNumber(node.getStartPosition());
			Expression exp = ((ExpressionStatement) node).getExpression();
			if (exp instanceof Assignment) {
				Expression leftOperand = ((Assignment) exp).getLeftHandSide();
				DefLocPair dl = new DefLocPair(leftOperand.toString(),startline);
				this._temp.add(dl);
			}
		} else if (node instanceof VariableDeclarationStatement) {
			int startline = parser.getLineNumber(node.getStartPosition());
			VariableDeclarationStatement statement = (VariableDeclarationStatement) node;
			List fragments = statement.fragments();

			for (int i = 0; i < fragments.size(); i++) {
				VariableDeclarationFragment fragment = (VariableDeclarationFragment) fragments.get(i);
				SimpleName exp = fragment.getName();
				DefLocPair dl = new DefLocPair(exp.toString(),startline);
				this._temp.add(dl);
			}
		}
	}
}
